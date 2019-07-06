package com.dtlonline.shop.service.store;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.store.StoreGoodsExtractDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsProductInfoDTO;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.constant.StoreGoodsBizTypeEnum;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsExtractDTO;
import com.dtlonline.api.user.remote.UserShippingAddressRemoteService;
import com.dtlonline.api.user.view.ShippingAddressDTO;
import com.dtlonline.shop.constant.store.StoreGoodsViewCode;
import com.dtlonline.shop.exception.StoreGoodsException;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.mapper.store.StoreGoodsMapper;
import com.dtlonline.shop.mapper.store.StoreMapper;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.model.store.Store;
import com.dtlonline.shop.model.store.StoreGoods;
import com.dtlonline.shop.util.PageUtils;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.util.ObjectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dtlonline.shop.mapper.store.StoreGoodsExtractMapper;
import com.dtlonline.shop.model.store.StoreGoodsExtract;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StoreGoodsExtractService extends ServiceImpl<StoreGoodsExtractMapper, StoreGoodsExtract> {

    @Autowired
    private StoreGoodsExtractMapper storeGoodsExtractMapper;
    @Autowired
    private StoreGoodsMapper storeGoodsMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StoreGoodsSplitService storeGoodsSplitService;
    @Autowired
    private UserShippingAddressRemoteService addressRemoteService;
    @Autowired
    private CategoryDao categoryMapper;
    @Autowired
    private ProductStandardRecordDao productStandardRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public void extractApply(StoreGoodsExtractDTOI storeGoodsExtractDTOI,Long userId) {
        //检查仓库数量
        Long storeGoodsId = storeGoodsExtractDTOI.getStoreGoodsId();
        StoreGoods storeGoods = storeGoodsMapper.selectById(storeGoodsId);
        if (storeGoodsId == null || !storeGoods.getUserId().equals(userId)) {
            throw new StoreGoodsException(StoreGoodsViewCode.NOT_BELONG_USER.getCode(), StoreGoodsViewCode.NOT_BELONG_USER.getMessage());
        }
        //发布供应的仓单不能提货
        if (StoreGoodsBizTypeEnum.PROVIDING.getValue().equals(storeGoods.getBizType())) {
            throw new StoreGoodsException(StoreGoodsViewCode.PUBLISH_PROVIDE.getCode(), StoreGoodsViewCode.PUBLISH_PROVIDE.getMessage());
        }
        Integer extractQuantity = storeGoodsExtractDTOI.getQuantity();
        boolean isSufficient = storeGoods.getValidQuantity() >= extractQuantity;
        if (!isSufficient) {
            throw new StoreGoodsException(StoreGoodsViewCode.INSUFFICIENT_QUANTITY.getCode(), StoreGoodsViewCode.INSUFFICIENT_QUANTITY.getMessage());
        }
        Long newStoreGoodsId = storeGoodsSplitService.split(storeGoodsId, StoreGoodsBizTypeEnum.RETRIEVE.getValue(), extractQuantity, userId, storeGoodsExtractDTOI.getTxnId());

        //插入申请数据
        StoreGoodsExtract storeGoodsExtract = ObjectUtils.copy(storeGoodsExtractDTOI, StoreGoodsExtract.class);
        storeGoodsExtract.setId(null);
        storeGoodsExtract.setUserId(userId);
        storeGoodsExtract.setStoreGoodsId(storeGoodsId);
        storeGoodsExtract.setSplitStoreGoodsId(newStoreGoodsId);
        storeGoodsExtract.setGoodsNo(storeGoods.getGoodsNo());
        storeGoodsExtract.setInventoryNo(storeGoods.getInventoryNo());
        storeGoodsExtract.setAuditUser("");
        storeGoodsExtract.setAuditStatus(Status.PENDING.getValue());
        storeGoodsExtract.setAuditOpinion("");
        storeGoodsExtract.setCreateTime(LocalDateTime.now());
        storeGoodsExtract.setLastModifyTime(LocalDateTime.now());
        storeGoodsExtractMapper.insert(storeGoodsExtract);
    }

    /**
     * 提货列表
     */
    public Page<StoreGoodsExtractDTO> queryList(Integer current,Integer size,Long userId) {
        Page<StoreGoodsExtract> page = new Page<>(current, size);
        LambdaQueryWrapper<StoreGoodsExtract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreGoodsExtract::getUserId,userId);
        wrapper.orderByDesc(StoreGoodsExtract::getId);
        IPage<StoreGoodsExtract> storeGoodsRequirePage = storeGoodsExtractMapper.selectPage(page, wrapper);
        Page<StoreGoodsExtractDTO> storeGoodsRecordsDTOPage = PageUtils.transferRecords(storeGoodsRequirePage, StoreGoodsExtractDTO.class);
        if (CollectionUtils.isEmpty(storeGoodsRecordsDTOPage.getRecords())) {
            return storeGoodsRecordsDTOPage;
        }
        storeGoodsRecordsDTOPage.getRecords().stream().forEach(this::handleExtra);
        return storeGoodsRecordsDTOPage;
    }

    private void handleExtra(StoreGoodsExtractDTO record) {
        //合并仓单信息
        StoreGoods storeGoods = storeGoodsMapper.selectById(record.getStoreGoodsId());
        StoreGoodsDTO storeGoodsDTO = ObjectUtils.copy(storeGoods, StoreGoodsDTO.class);
        record.setStoreGoodsDTO(storeGoodsDTO);
        //仓库地址、名称
        Store store = storeMapper.selectById(storeGoods.getStoreId());
        storeGoodsDTO.setStoreName(store.getName());
        storeGoodsDTO.setStoreAddress(store.getAddress());
        //转换addressId到address
        if (record.getAddressId()!=null) {
            ShippingAddressDTO address = addressRemoteService.queryUserShippingAddress(record.getAddressId()).getData();
            record.setAddress(address.getAddress());
        }
        //拆分标题和规格（新）
        String productInfo = storeGoodsDTO.getProductInfo();
        StoreGoodsProductInfoDTO productInfoDTO = JsonUtils.parseJSON(productInfo, StoreGoodsProductInfoDTO.class);
        record.setCategoryName(categoryMapper.selectById(productInfoDTO.getCategoryId()).getTitle());

        List<Long> productStandardRecordIds = productInfoDTO.getProductStandardRecordIds();
        if (CollectionUtils.isNotEmpty(productStandardRecordIds)) {
            List<ProductStandardRecord> productStandardRecords = productStandardRecordMapper.selectBatchIds(productStandardRecordIds);
            record.setProductStandardRecordDTOs(ObjectUtils.copy(productStandardRecords, ProductStandardRecordDTO.class));
        }
    }

    /**
     * 提货详情
     */
    public StoreGoodsExtractDTO queryDetail(Long id) {
        StoreGoodsExtract storeGoodsExtract = storeGoodsExtractMapper.selectById(id);
        StoreGoodsExtractDTO detail = ObjectUtils.copy(storeGoodsExtract, StoreGoodsExtractDTO.class);
        handleExtra(detail);
        return detail;
    }

}
