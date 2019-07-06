package com.dtlonline.shop.service.store;

import java.time.LocalDate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.StandardRecordDTO;
import com.dtlonline.api.shop.command.store.*;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.constant.StoreGoodsBizTypeEnum;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsProductTitleDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsRequireDTO;
import com.dtlonline.api.shop.constant.StoreGoodsRequireTradeTypeEnum;
import com.dtlonline.api.shop.constant.StoreGoodsRequireTypeEnum;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.mapper.store.*;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.model.store.*;
import com.dtlonline.shop.util.PageUtils;
import com.dtlonline.api.shop.view.ProductCarInfoDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.dtlonline.shop.constant.store.StoreGoodsViewCode;
import com.dtlonline.shop.exception.StoreGoodsException;
import com.dtlonline.shop.mapper.ProductCarInfoMapper;
import com.dtlonline.shop.model.ProductCarInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.util.ObjectUtils;
import io.alpha.app.core.util.SequenceUtils;
import io.alpha.security.aes.AESUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

@Service
public class StoreGoodsRequireService extends ServiceImpl<StoreGoodsRequireMapper, StoreGoodsRequire> {

    @Autowired
    private ImageRemoteService imageRemoteService;
    @Autowired
    private StoreGoodsService storeGoodsService;
    @Autowired
    private StoreGoodsSplitService storeGoodsSplitService;
    @Autowired
    private StoreGoodsMapper storeGoodsMapper;
    @Autowired
    private StoreGoodsRequireMapper storeGoodsRequireMapper;
    @Autowired
    private StoreGoodsRequireFutureMapper storeGoodsRequireFutureMapper;
    @Autowired
    private ProductCarInfoMapper productCarInfoMapper;
    @Autowired
    private CategoryDao categoryMapper;
    @Autowired
    private ProductStandardRecordDao productStandardRecordMapper;
    @Autowired
    private StoreGoodsRequirePathMapper storeGoodsRequirePathMapper;
    @Autowired
    private StoreGoodsRequireExtMapper storeGoodsRequireExtMapper;


    /**
     * 发布需求，需求类型有三种则调用三次。
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishOne(StoreGoodsRequireDTOI publishDTO, Long userId) {

        Long storeGoodsId = publishDTO.getStoreGoodsId();
        String outProductInfo = "";
        String outProductTitle = "";
        String inProductInfo = "";
        String inProductTitle = "";

        if (storeGoodsId!=null) {
            StoreGoods storeGoods = storeGoodsMapper.selectById(storeGoodsId);
            //取出仓单规格
            outProductInfo = storeGoods.getProductInfo();
            outProductTitle = storeGoods.getProductTitle();
        }

        //生成图片
        if (CollectionUtils.isNotEmpty(publishDTO.getOutProductImages())) {
            List<Long> imageIds = imageRemoteService.batchSaveImage(publishDTO.getOutProductImages()).getData();
            String images = StringUtils.join(imageIds, ",");
            publishDTO.setProductImgs(images);
        }

        //处理借出/换出的规格信息，有些需求可以不选仓单，简单存入规格。
        // TODO: 2019/4/23 重构烂代码
        if (StringUtils.isEmpty(outProductInfo)){
            if (CollectionUtils.isNotEmpty(publishDTO.getOutStandardList())) {
                StoreGoodsProductInfoDTO inProductStandardInfo = saveStandards(publishDTO.getOutCategoryId(), publishDTO.getOutStandardList(), publishDTO.getTxnId(), userId);
                outProductInfo = JsonUtils.toJSON(inProductStandardInfo);
                outProductTitle = storeGoodsService.getProductTitle(inProductStandardInfo);
            } else {
                throw new StoreGoodsException(StoreGoodsViewCode.LACK_STANDARD.getCode(), StoreGoodsViewCode.LACK_STANDARD.getMessage());
            }
        }
        publishDTO.setProductInfo(outProductInfo);
        publishDTO.setProductTitle(outProductTitle);

        //处理借入/换入的规格信息
        // TODO: 2019/4/23 重构烂代码
        List<StoreGoodsOutStandardInfoDTO> inStandardList = publishDTO.getInStandardList();
        if (publishDTO.getTradeType().equals(3)) {
            if (inStandardList.size() > 3) {
                throw new StoreGoodsException(StoreGoodsViewCode.STANDARD_EXCESS_COUNT_LIMIT.getCode(), StoreGoodsViewCode.STANDARD_EXCESS_COUNT_LIMIT.getMessage());
            }
            ArrayList<StoreGoodsProductInfoDTO> inProductStandardInfos = new ArrayList<>();
            for (StoreGoodsOutStandardInfoDTO inStandard : inStandardList) {
                StoreGoodsProductInfoDTO inProductStandardInfo = saveStandards(inStandard.getInCategoryId(), inStandard.getInStandardList(), publishDTO.getTxnId(), userId);
                inProductStandardInfos.add(inProductStandardInfo);
            }
            inProductInfo = JsonUtils.toJSON(inProductStandardInfos);
            inProductTitle = storeGoodsService.getProductTitle(inProductStandardInfos);
        }

        //生成编号
        String goodsRequireNo = getGoodsRequireNo(publishDTO.getType());

        //平台仓单需要把仓单拆出一部分，进行冻结
        if (StoreGoodsRequireTypeEnum.PLATFORM.getValue().equals(publishDTO.getTradeType())) {
            Long newStoreGoodsId = storeGoodsSplitService.split(publishDTO.getStoreGoodsId(), StoreGoodsBizTypeEnum.PROVIDING.getValue(), publishDTO.getQuantity(), publishDTO.getUserId(), SequenceUtils.getSequence());
            publishDTO.setStoreGoodsId(newStoreGoodsId);
        }

        //插入数据到需求表
        StoreGoodsRequire insertRecord = ObjectUtils.copy(publishDTO, StoreGoodsRequire.class);
        insertRecord.setProductInfo(outProductInfo);
        insertRecord.setProductTitle(outProductTitle);
        insertRecord.setRequireProductInfo(inProductInfo);
        insertRecord.setRequireProductTitle(inProductTitle);
        insertRecord.setId(null);
        insertRecord.setRequireNo(goodsRequireNo);
        insertRecord.setUserId(userId);
        insertRecord.setAuditUser(publishDTO.getAuditUser());
        insertRecord.setAuditOpinion("");
        insertRecord.setAuditStatus(Status.PENDING.getValue());
        insertRecord.setCreateTime(LocalDateTime.now());
        insertRecord.setLastModifyTime(LocalDateTime.now());
        insertRecord.setValidQuantity(insertRecord.getQuantity());
        storeGoodsRequireMapper.insert(insertRecord);

        //插入期货、在途扩展信息、需求扩展信息
        insertStoreGoodsRequireFuture(publishDTO);
        insertProductCarInfo(publishDTO);
        insertRequireExt(publishDTO);

    }


    /**
     * 发布需求，可卖、换入、换出、
     * 交换单需要单独设置换入的商品列表
     */
    @Transactional
    public void publishMulti(StoreGoodsRequireDTOI publishDTO, Long userId) {

        Integer quantityLend = publishDTO.getQuantityLend();
        Integer quantityBorrow = publishDTO.getQuantityBorrow();
        Integer quantitySell = publishDTO.getQuantitySell();
        Integer quantityBuy = publishDTO.getQuantityBuy();
        Integer quantityExchange = publishDTO.getQuantityExchange();

        //开始逐个生成需求单
        if (quantityLend != null) {
            publishDTO.setTradeType(StoreGoodsRequireTradeTypeEnum.LENDING.getValue());
            publishDTO.setQuantity(quantityLend);
            publishOne(publishDTO, userId);
            return;
        }
        if (quantityBorrow != null) {
            publishDTO.setTradeType(StoreGoodsRequireTradeTypeEnum.BORROW.getValue());
            publishDTO.setQuantity(quantityBorrow);
            publishOne(publishDTO, userId);
            return;
        }
        if (quantitySell != null) {
            publishDTO.setTradeType(StoreGoodsRequireTradeTypeEnum.SELL_OUT.getValue());
            publishDTO.setQuantity(quantitySell);
            publishOne(publishDTO, userId);
            return;
        }
        if (quantityBuy != null) {
            publishDTO.setTradeType(StoreGoodsRequireTradeTypeEnum.BUY_IN.getValue());
            publishDTO.setQuantity(quantityBuy);
            publishOne(publishDTO, userId);
            return;
        }
        if (quantityExchange != null) {
            publishDTO.setTradeType(StoreGoodsRequireTradeTypeEnum.EXCHANGE.getValue());
            publishDTO.setQuantity(quantityExchange);
            publishOne(publishDTO, userId);
            return;
        }

    }

    @Transactional
    public StoreGoodsProductInfoDTO saveStandards(Long categoryId, List<ProductStandardRecordDTO> standardList, String txnId, Long userId) {
        //规格存入数据库
        List<ProductStandardRecord> productStandardRecord = ObjectUtils.copy(standardList, ProductStandardRecord.class);
        productStandardRecord.forEach(r -> r.setTxnId(txnId).setCategoryId(categoryId).setUserId(userId));
        productStandardRecord.forEach(productStandardRecordMapper::insert);
        //转换成可存储信息
        List<Long> productStandardId = productStandardRecord.stream().map(ProductStandardRecord::getId).collect(Collectors.toList());
        StoreGoodsProductInfoDTO productTypeInfoDTO = new StoreGoodsProductInfoDTO(categoryId, productStandardId);
        return productTypeInfoDTO;
    }

    private void insertProductCarInfo(StoreGoodsRequireDTOI storeGoodsRequire) {
        //在途信息
        ProductCarInfoDTO productCarInfo = storeGoodsRequire.getProductCarInfo();
        if (productCarInfo == null) {
            return;
        }
        ProductCarInfo insertCardInfo = ObjectUtils.copy(productCarInfo, ProductCarInfo.class);
        insertCardInfo.setId(null);
        insertCardInfo.setTxnId(storeGoodsRequire.getTxnId());
        insertCardInfo.setCarPlate(insertCardInfo.getCarPlate());
        insertCardInfo.setDriverName(AESUtil.encrypt(insertCardInfo.getDriverName()));
        insertCardInfo.setDriverIdCard(AESUtil.encrypt(insertCardInfo.getDriverIdCard()));
        insertCardInfo.setDriverPhone(AESUtil.encrypt(insertCardInfo.getDriverPhone()));
        insertCardInfo.setSendDate(LocalDate.now());
        insertCardInfo.setArrivalDate(LocalDate.now());
        insertCardInfo.setCreateTime(LocalDateTime.now());
        insertCardInfo.setLastModifyTime(LocalDateTime.now());
        productCarInfoMapper.insert(insertCardInfo);

        //途经路径
        List<StoreGoodsRequirePathDTOI> pathDTOs = productCarInfo.getPaths();
        if (pathDTOs == null) {
            return;
        }
        List<StoreGoodsRequirePath> paths = ObjectUtils.copy(pathDTOs, StoreGoodsRequirePath.class);
        for (StoreGoodsRequirePath path : paths) {
            path.setTxnId(storeGoodsRequire.getTxnId());
            storeGoodsRequirePathMapper.insert(path);
        }
    }


    private void insertStoreGoodsRequireFuture(StoreGoodsRequireDTOI storeGoodsRequire) {
        //插入期货供应信息
        StoreGoodsRequireFutureDTOI futureDTOI = storeGoodsRequire.getStoreGoodsRequireFuture();
        if (futureDTOI == null) {
            return;
        }
        StoreGoodsRequireFuture future = ObjectUtils.copy(futureDTOI, StoreGoodsRequireFuture.class);
        future.setId(null);
        future.setTxnId(storeGoodsRequire.getTxnId());
        future.setCreateTime(LocalDateTime.now());
        future.setLastModifyTime(LocalDateTime.now());
        storeGoodsRequireFutureMapper.insert(future);
    }

    private void insertRequireExt(StoreGoodsRequireDTOI storeGoodsRequire) {
        //插入需求扩展信息
        StoreGoodsRequireExtDTOI storeGoodsRequireExtDTOI = storeGoodsRequire.getStoreGoodsRequireExtDTOI();
        if (storeGoodsRequireExtDTOI == null) {
            return;
        }
        StoreGoodsRequireExt ext = ObjectUtils.copy(storeGoodsRequireExtDTOI, StoreGoodsRequireExt.class);
        ext.setTxnId(storeGoodsRequire.getTxnId());
        storeGoodsRequireExtMapper.insert(ext);
    }

    /**
     * 借换大厅列表/个人借换货列表，只显示审核通过的
     * userId 有传则为个人列表
     * 自动筛选过期的
     */
    public Page<StoreGoodsRequireDTO> queryList(StoreGoodsRequireQueryDTOI queryDTOI) {
        Page<StoreGoodsRequire> page = new Page<>(queryDTOI.getCurrent(), queryDTOI.getSize());
        LambdaQueryWrapper<StoreGoodsRequire> wrapper = new LambdaQueryWrapper<>();
        if (queryDTOI.getUserId() != null) {
            wrapper.eq(StoreGoodsRequire::getUserId, queryDTOI.getUserId());
        }
        if (queryDTOI.getAuditStatus() != null) {
            if(queryDTOI.getAuditStatus().equals(Status.PASS.getValue())){
                wrapper.eq(StoreGoodsRequire::getAuditStatus, Status.PASS.getValue());
            }else{
                wrapper.ne(StoreGoodsRequire::getAuditStatus, Status.PASS.getValue());
            }
        }
        if (StringUtils.isNotEmpty(queryDTOI.getCategoryName())) {
            List<String> categoryAndStandardNames = Lists.newArrayList(queryDTOI.getCategoryName());
            List<StandardRecordDTO> standardRecords = queryDTOI.getStandardList();
            if (CollectionUtils.isNotEmpty(standardRecords)) {
                standardRecords.sort((s1,s2)->s1.getStandardId().intValue()-s2.getStandardId().intValue());
                List<String> standardNames = standardRecords.stream().map(StandardRecordDTO::getData).collect(Collectors.toList());
                categoryAndStandardNames.addAll(standardNames);
            }
            wrapper.like(StoreGoodsRequire::getProductTitle, "%"+StringUtils.join(categoryAndStandardNames, " ")+"%");
        }
        if (queryDTOI.getType() != null) {
            if(queryDTOI.getType().equals(StoreGoodsRequireTypeEnum.REQUIRE.getValue())){
                wrapper.eq(StoreGoodsRequire::getType, StoreGoodsRequireTypeEnum.REQUIRE.getValue());
            }else{
                wrapper.ne(StoreGoodsRequire::getType, StoreGoodsRequireTypeEnum.REQUIRE.getValue());
            }
        }
        if (Boolean.TRUE.equals(queryDTOI.getHideOffSale())) {
            // 下架日期大于等于今天，才显示
            wrapper.ge(StoreGoodsRequire::getOffSaleDate, LocalDate.now());
        }
        wrapper.orderByDesc(StoreGoodsRequire::getId);
        IPage<StoreGoodsRequire> storeGoodsRequirePage = storeGoodsRequireMapper.selectPage(page, wrapper);
        Page<StoreGoodsRequireDTO> storeGoodsRecordsDTOPage = PageUtils.transferRecords(storeGoodsRequirePage, StoreGoodsRequireDTO.class);
        if (CollectionUtils.isEmpty(storeGoodsRecordsDTOPage.getRecords())) {
            return storeGoodsRecordsDTOPage;
        }
        storeGoodsRecordsDTOPage.getRecords().forEach(this::handleRequireExtra);
        return storeGoodsRecordsDTOPage;
    }

    private void handleRequireExtra(StoreGoodsRequireDTO record) {
        //仓库
        Long storeGoodsId = record.getStoreGoodsId();
        if (storeGoodsId!=null && !storeGoodsId.equals(0)) {
            StoreGoods storeGoods = storeGoodsMapper.selectById(storeGoodsId);
            StoreGoodsDTO storeGoodsDTO = ObjectUtils.copy(storeGoods, StoreGoodsDTO.class);
            record.setStoreGoodsDTO(storeGoodsDTO);
        }
        //图片
        String productImgs = record.getProductImgs();
        if (StringUtils.isNotEmpty(productImgs)) {
            Set<Long> imgIds = Arrays.stream(StringUtils.split(productImgs, ","))
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());
            Map<Long, String> idToUrl = imageRemoteService.queryUrls(imgIds);
            record.setImgUrls(new ArrayList<>(idToUrl.values()));;
        }
        //拆分标题和规格（新）
        String productInfo = record.getProductInfo();
        StoreGoodsProductInfoDTO productInfoDTO = JsonUtils.parseJSON(productInfo, StoreGoodsProductInfoDTO.class);
        String categoryName = transferCategory(productInfoDTO);
        record.setCategoryName(categoryName);
        List<ProductStandardRecordDTO> productStandardRecordDTOS = transferProductRecordStandards(productInfoDTO);
        record.setProductStandardRecordDTOs(productStandardRecordDTOS);

        //拆分换货标题和规格（新）
        // TODO: 2019/4/24 重构烂代码
        String productRequireInfo = record.getRequireProductInfo();
        if (StringUtils.isNotEmpty(productRequireInfo)) {

            List<StoreGoodsProductTitleDTO> storeGoodsProductTitleDTOS = new ArrayList<>();

            List<StoreGoodsProductInfoDTO> productRequireInfoDTOs = JsonUtils.parseJSON(productRequireInfo, new TypeReference<List<StoreGoodsProductInfoDTO>>(){});
            for (StoreGoodsProductInfoDTO productRequireInfoDTO : productRequireInfoDTOs) {

                String requireCategoryName = transferCategory(productRequireInfoDTO);
                List<ProductStandardRecordDTO> requireProductStandardRecordDTOS = transferProductRecordStandards(productRequireInfoDTO);

                StoreGoodsProductTitleDTO storeGoodsProductTitleDTO = new StoreGoodsProductTitleDTO();
                storeGoodsProductTitleDTO.setCategoryName(requireCategoryName);
                storeGoodsProductTitleDTO.setProductStandardRecordDTOs(requireProductStandardRecordDTOS);
                storeGoodsProductTitleDTOS.add(storeGoodsProductTitleDTO);
            }

            record.setStoreGoodsProductTitles(storeGoodsProductTitleDTOS);
        }
        //组装标题
        record.setTitle(StoreGoodsRequireTradeTypeEnum.getMessage(record.getTradeType()) + record.getTitle());

        //在途供应需要获取路径
        getOnWayTypePaths(record);
    }

    private void getOnWayTypePaths(StoreGoodsRequireDTO record) {
        if ( !StoreGoodsRequireTypeEnum.ON_WAY.getValue().equals(record.getType())) {
            return;
        }
        List<StoreGoodsRequirePath> paths = storeGoodsRequirePathMapper.selectByTxnId(record.getTxnId());
        if (CollectionUtils.isEmpty(paths)) {
            return;
        }
        List<String> cities = paths.stream().map(StoreGoodsRequirePath::getCity).collect(Collectors.toList());
        record.setPathJoin(StringUtils.join(cities,"->"));
    }

    private String transferCategory(StoreGoodsProductInfoDTO productInfoDTO) {
        return categoryMapper.selectById(productInfoDTO.getCategoryId()).getTitle();
    }
    private List<ProductStandardRecordDTO> transferProductRecordStandards(StoreGoodsProductInfoDTO productInfoDTO) {
        List<Long> productStandardRecordIds = productInfoDTO.getProductStandardRecordIds();
        if (CollectionUtils.isEmpty(productStandardRecordIds)) {
            return null;
        }
        List<ProductStandardRecord> productStandardRecords = productStandardRecordMapper.selectBatchIds(productStandardRecordIds);
        return ObjectUtils.copy(productStandardRecords, ProductStandardRecordDTO.class);
    }



    /**
     * 借换详情
     */
    public StoreGoodsRequireDTO queryOne(Long id) {
        StoreGoodsRequire storeGoodsRequire = storeGoodsRequireMapper.selectById(id);
        StoreGoodsRequireDTO storeGoodsRequireDTO = ObjectUtils.copy(storeGoodsRequire, StoreGoodsRequireDTO.class);
        handleRequireExtra(storeGoodsRequireDTO);
        return storeGoodsRequireDTO;
    }

    /**
     * 生成供需编号
     * @return
     */
    public String getGoodsRequireNo(int type) {
        StoreGoodsRequireTypeEnum requireTypeEnum = StoreGoodsRequireTypeEnum.getRequireTypeEnum(type);
        String transactionNoPrefix = requireTypeEnum.getTransactionNoPrefix();
        String nowNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return transactionNoPrefix + nowNo + RandomUtils.nextInt(100000, 999999);
    }


}
