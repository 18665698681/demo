package com.dtlonline.shop.service.store;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.store.StoreGoodsAuthRecordDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsProductInfoDTO;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.constant.StoreGoodsBizTypeEnum;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsAuthRecordDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsMergeQuantityDTO;
import com.dtlonline.shop.exception.StoreGoodsException;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.ProductRecordDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.mapper.store.StoreGoodsAuthRecordMapper;
import com.dtlonline.shop.mapper.store.StoreGoodsMapper;
import com.dtlonline.shop.mapper.store.StoreMapper;
import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.ProductRecord;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.model.store.Store;
import com.dtlonline.shop.model.store.StoreGoods;
import com.dtlonline.shop.model.store.StoreGoodsAuthRecord;
import com.dtlonline.shop.service.ProductRecordService;
import com.dtlonline.shop.util.NumberUtils;
import com.dtlonline.shop.util.PageUtils;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.util.ObjectUtils;
import io.alpha.app.core.view.ViewCode;
import io.alpha.security.aes.AESUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreGoodsService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(StoreGoodsService.class);

    @Autowired
    private ProductRecordService productRecordService;
    @Autowired
    private StoreGoodsMapper storeGoodsMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StoreGoodsAuthRecordMapper storeGoodsAuthRecordMapper;
    @Autowired
    private ProductRecordDao productRecordMapper;
    @Autowired
    private ProductDao productMapper;
    @Autowired
    private CategoryDao categoryMapper;
    @Autowired
    private ProductStandardRecordDao productStandardRecordMapper;

    private static String GOODS_NO_PREFIX = "WRGY";
    /**
     * 仓单申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void storeApply(StoreGoodsAuthRecordDTOI applyDTO,Long userId) {
        //生成未审核商品信息
        ProductRecordDTO productRecordDTO = applyDTO.getProductRecordDTO();
        if (productRecordDTO == null) {
            throw new StoreGoodsException(ViewCode.ARGUMENT_NOT_VALID.getCode(),ViewCode.ARGUMENT_NOT_VALID.getMessage());
        }
        if (CollectionUtils.isNotEmpty(productRecordDTO.getStandardList())) {
            productRecordDTO.getStandardList().sort((s1,s2)->s1.getStandardId().intValue()-s2.getStandardId().intValue());
        }
        productRecordDTO.setStock(applyDTO.getQuantity().intValue());
        productRecordDTO.setTxnId(applyDTO.getTxnId());
        productRecordDTO.setType(TypeEnum.PRODUCT_GOODS.getValue());
        productRecordDTO.setStatus(Status.PENDING.getValue());
        ProductRecord productRecord = productRecordService.saveProductOtherExtra(productRecordDTO, userId);

        //生成规格信息
        Long productCategoryId = productRecord.getCategoryId();
        List<Long> productStandardRecordIds = productStandardRecordMapper.selectProductStandardRecordIds(productRecord.getTxnId());
        if (CollectionUtils.isEmpty(productStandardRecordIds)) {
            throw new StoreGoodsException(ViewCode.ARGUMENT_NOT_VALID.getCode(),"不可能存在没有规格的商品");
        }
        StoreGoodsProductInfoDTO productTypeInfoDTO = new StoreGoodsProductInfoDTO(productCategoryId, productStandardRecordIds);
        String productInfo = JsonUtils.toJSON(productTypeInfoDTO);
        String productTitle = getProductTitle(productTypeInfoDTO);

        //生成申请仓单
        StoreGoodsAuthRecord record = ObjectUtils.copy(applyDTO, StoreGoodsAuthRecord.class);
        record.setProductId(productRecord.getId());
        record.setUserId(userId);
        record.setAuditStatus(Status.PENDING.getValue());
        record.setStatus(SUCCESS.intValue());
        record.setProductInfo(productInfo);
        record.setProductTitle(productTitle);
        storeGoodsAuthRecordMapper.insert(record);
    }

    /**
     * 查询未通过列表
     */
    public Page<StoreGoodsAuthRecordDTO> queryAuditingList(Integer current, Integer size, Long userId) {
        Page<StoreGoodsAuthRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<StoreGoodsAuthRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreGoodsAuthRecord::getUserId,userId);
        wrapper.ne(StoreGoodsAuthRecord::getAuditStatus, Status.PASS.getValue());
        wrapper.orderByDesc(StoreGoodsAuthRecord::getId);
        IPage<StoreGoodsAuthRecord> pageGoodsRecords = storeGoodsAuthRecordMapper.selectPage(page, wrapper);
        Page<StoreGoodsAuthRecordDTO> storeGoodsRecordsDTOPage = PageUtils.transferRecords(pageGoodsRecords, StoreGoodsAuthRecordDTO.class);
        if (CollectionUtils.isEmpty(storeGoodsRecordsDTOPage.getRecords())) {
            return storeGoodsRecordsDTOPage;
        }
        storeGoodsRecordsDTOPage.getRecords().stream().forEach(this::handleStoreGoodsAuditingExtra);
        return storeGoodsRecordsDTOPage;
    }

    /**
     * 查询通过列表
     */
    public Page<StoreGoodsDTO> queryFormalList(StoreGoodsDTOI storeGoodsDTOI) {
        Page<StoreGoods> page = new Page<>(storeGoodsDTOI.getCurrent(), storeGoodsDTOI.getSize());
        LambdaQueryWrapper<StoreGoods> wrapper = new LambdaQueryWrapper<>();
        if (storeGoodsDTOI.getStoreId()!=null) {
            wrapper.eq(StoreGoods::getStoreId,storeGoodsDTOI.getStoreId());
        }
        if (StringUtils.isNotEmpty(storeGoodsDTOI.getProductTitle())) {
            wrapper.like(StoreGoods::getProductTitle, "%"+storeGoodsDTOI.getProductTitle()+"%");
        }
        wrapper.eq(StoreGoods::getUserId,storeGoodsDTOI.getUserId());
        wrapper.eq(StoreGoods::getStatus,SUCCESS);
        wrapper.ne(StoreGoods::getBizType, StoreGoodsBizTypeEnum.RETRIEVE.getValue());
        wrapper.orderByDesc(StoreGoods::getId);
        IPage<StoreGoods> pageGoods = storeGoodsMapper.selectPage(page, wrapper);
        Page<StoreGoodsDTO> storeGoodsDTOPage = PageUtils.transferRecords(pageGoods, StoreGoodsDTO.class);
        if (CollectionUtils.isEmpty(storeGoodsDTOPage.getRecords())) {
            return storeGoodsDTOPage;
        }
        storeGoodsDTOPage.getRecords().stream().forEach(this::handleStoreGoodsFormalExtra);
        return storeGoodsDTOPage;
    }

    private void handleStoreGoodsFormalExtra(StoreGoodsDTO storeGoodsDTO) {
        //合并商品信息
        Product product = productMapper.selectById(storeGoodsDTO.getProductId());
        ProductInfoDTO productDto = ObjectUtils.copy(product, ProductInfoDTO.class);
        storeGoodsDTO.setProductDTO(productDto);

        //分隔标题
        // TODO: 2019/4/24 旧格式，待删除
        String productTitle = storeGoodsDTO.getProductTitle();
        if (StringUtils.isNotEmpty(productTitle) && StringUtils.contains(productTitle, " ")) {
            String[] categoryAndStandards = StringUtils.split(productTitle," ", 2);
            storeGoodsDTO.setProductTitleCategory(categoryAndStandards[0]);
            storeGoodsDTO.setProductTitleStandards(categoryAndStandards.length>=2?categoryAndStandards[1]:"无规格");
        }else{
            storeGoodsDTO.setProductTitleCategory(productTitle);
            storeGoodsDTO.setProductTitleStandards(productTitle);
        }

        //拆分标题和规格（新）
        String productInfo = storeGoodsDTO.getProductInfo();
        StoreGoodsProductInfoDTO productInfoDTO = JsonUtils.parseJSON(productInfo, StoreGoodsProductInfoDTO.class);
        storeGoodsDTO.setCategoryName(categoryMapper.selectById(productInfoDTO.getCategoryId()).getTitle());

        List<Long> productStandardRecordIds = productInfoDTO.getProductStandardRecordIds();
        if (CollectionUtils.isNotEmpty(productStandardRecordIds)) {
            List<ProductStandardRecord> productStandardRecords = productStandardRecordMapper.selectBatchIds(productStandardRecordIds);
            storeGoodsDTO.setProductStandardRecordDTOs(ObjectUtils.copy(productStandardRecords, ProductStandardRecordDTO.class));
        }

        //入库名称
        Long storeId = storeGoodsDTO.getStoreId();
        Store store = storeMapper.selectById(storeId);
        storeGoodsDTO.setStoreName(store.getName()).setStoreAddress(store.getAddress());
    }

    /**
     * 查询通过列表，合并为持仓列表
     * userId 必传
     */
    public Page<StoreGoodsMergeQuantityDTO> queryFormalListMergeByStandard(Integer current, Integer size, Long userId) {
        Page<StoreGoodsMergeQuantityDTO> page = new Page<>(current, size);
        Page<StoreGoodsMergeQuantityDTO> storeGoodsDTOPage = storeGoodsMapper.selectPassListByStandard(page,userId);
        if (CollectionUtils.isEmpty(storeGoodsDTOPage.getRecords())) {
            return storeGoodsDTOPage;
        }
        storeGoodsDTOPage.getRecords().stream().forEach(this::handleStoreGoodsMergeQuantityDTO);
        return storeGoodsDTOPage;
    }

    private void handleStoreGoodsMergeQuantityDTO(StoreGoodsMergeQuantityDTO storeGoodsMergeQuantityDTO) {
        Integer sumFrozenQuantity = storeGoodsMergeQuantityDTO.getSumFrozenQuantity();
        Integer sumLendingQuantity = storeGoodsMergeQuantityDTO.getSumLendingQuantity();
        Integer sumValidQuantity = storeGoodsMergeQuantityDTO.getSumValidQuantity();
        Integer sumAllQuantity = NumberUtils.sum(sumFrozenQuantity, sumLendingQuantity, sumValidQuantity);
        storeGoodsMergeQuantityDTO.setSumAllQuantity(sumAllQuantity);
    }

    private void handleStoreGoodsAuditingExtra(StoreGoodsAuthRecordDTO storeGoodsDTO) {
        //合并商品信息
        ProductRecord product = productRecordMapper.selectById(storeGoodsDTO.getProductId());
        ProductRecordDTO productDto = ObjectUtils.copy(product, ProductRecordDTO.class);
        storeGoodsDTO.setProductDTO(productDto);


        // TODO: 2019/4/24 旧格式，待删除
        String productTitle = storeGoodsDTO.getProductTitle();
        if (StringUtils.isNotEmpty(productTitle) && StringUtils.contains(productTitle, " ")) {
            String[] categoryAndStandards = StringUtils.split(productTitle," ", 2);
            storeGoodsDTO.setProductTitleCategory(categoryAndStandards[0]);
            storeGoodsDTO.setProductTitleStandards(categoryAndStandards.length>=2?categoryAndStandards[1]:"无规格");
        }else{
            storeGoodsDTO.setProductTitleCategory(productTitle);
            storeGoodsDTO.setProductTitleStandards(productTitle);
        }

        //拆分标题和规格（新）
        String productInfo = storeGoodsDTO.getProductInfo();
        StoreGoodsProductInfoDTO productInfoDTO = JsonUtils.parseJSON(productInfo, StoreGoodsProductInfoDTO.class);
        storeGoodsDTO.setCategoryName(categoryMapper.selectById(productInfoDTO.getCategoryId()).getTitle());

        List<Long> productStandardRecordIds = productInfoDTO.getProductStandardRecordIds();
        if (CollectionUtils.isNotEmpty(productStandardRecordIds)) {
            List<ProductStandardRecord> productStandardRecords = productStandardRecordMapper.selectBatchIds(productStandardRecordIds);
            storeGoodsDTO.setProductStandardRecordDTOs(ObjectUtils.copy(productStandardRecords, ProductStandardRecordDTO.class));
        }

        //入库名称
        Long storeId = storeGoodsDTO.getStoreId();
        Store store = storeMapper.selectById(storeId);
        storeGoodsDTO.setStoreName(store.getName()).setStoreAddress(store.getAddress());
    }

    //将规格转换为中文信息（一组）
    public String getProductTitle(List<StoreGoodsProductInfoDTO> storeGoodsProductInfoDTOS){
        Optional<String> productTitles = storeGoodsProductInfoDTOS.stream().map(this::getProductTitle).reduce((s1, s2) -> s1 + ","+s2);
        return productTitles.get();
    }
    //将规格转换为中文信息
    public String getProductTitle(StoreGoodsProductInfoDTO storeGoodsProductInfoDTO){
        if (storeGoodsProductInfoDTO==null) {
            return "";
        }

        Long categoryId = storeGoodsProductInfoDTO.getCategoryId();
        List<Long> productStandardRecordIds = storeGoodsProductInfoDTO.getProductStandardRecordIds();
        //类别名称
        Category category = categoryMapper.selectById(categoryId);
        if (CollectionUtils.isEmpty(productStandardRecordIds)) {
            return category.getTitle();
        }
        //规格名称，多个
        List<ProductStandardRecord> productStandardRecords = productStandardRecordMapper.selectBatchIds(productStandardRecordIds);
        List<String> standardNames = productStandardRecords.stream().map(ProductStandardRecord::getData).collect(Collectors.toList());
        String standardName = StringUtils.join(standardNames, " ");
        return category.getTitle()+" "+standardName;
    }

    public String getGoodsNo() {
        String nowNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return GOODS_NO_PREFIX + nowNo + RandomUtils.nextInt(100000, 999999);
    }

    public StoreGoodsAuthRecordDTO queryAuditOne(Long id) {
        StoreGoodsAuthRecord storeGoods = storeGoodsAuthRecordMapper.selectById(id);
        StoreGoodsAuthRecordDTO storeGoodDTO = ObjectUtils.copy(storeGoods, StoreGoodsAuthRecordDTO.class);
        handleStoreGoodsAuditingExtra(storeGoodDTO);
        return storeGoodDTO;
    }

    public StoreGoodsDTO queryPassedOne(Long id) {
        StoreGoods storeGoods = storeGoodsMapper.selectById(id);
        StoreGoodsDTO storeGoodDTO = ObjectUtils.copy(storeGoods, StoreGoodsDTO.class);
        handleStoreGoodsFormalExtra(storeGoodDTO);
        return storeGoodDTO;
    }

    public Map<Long, StoreGoodsDTO> queryStoreGoodsListById(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", ids);
        List<StoreGoods> list = storeGoodsMapper.selectList(wrapper);
        Map<Long, StoreGoodsDTO> goods = list.stream().collect(Collectors.toMap(StoreGoods::getId,this :: of));
        return goods;
    }

    public StoreGoodsDTO queryStoreGoodsById(Long goodsId) {
        StoreGoods storeGoods = storeGoodsMapper.selectById(goodsId);
        return of(storeGoods);
    }


    public  StoreGoodsDTO of(StoreGoods storeGoods) {
        if (storeGoods == null) {
            return null;
        }
        StoreGoodsDTO dto = new StoreGoodsDTO();
        BeanUtils.copyProperties(storeGoods, dto);
        return dto;
    }


}
