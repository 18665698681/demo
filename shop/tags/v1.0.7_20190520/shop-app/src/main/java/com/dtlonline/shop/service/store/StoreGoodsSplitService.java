package com.dtlonline.shop.service.store;

import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.shop.constant.store.StoreGoodsViewCode;
import com.dtlonline.shop.exception.StoreGoodsException;
import com.dtlonline.shop.mapper.store.StoreGoodsMapper;
import com.dtlonline.shop.model.store.StoreGoods;
import io.alpha.app.core.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StoreGoodsSplitService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(StoreGoodsSplitService.class);

    @Autowired
    private StoreGoodsMapper storeGoodsMapper;
    @Autowired
    private StoreGoodsService storeGoodsService;

    /**
     * 分裂某个仓单，使其变成两个仓单，为待审核状态
     * 新的仓单是临时的，审核不通过可以返回数量
     * @param storeGoodsId 原始仓单Id
     */
    @Transactional
    public Long split(Long storeGoodsId, Integer bizType, Integer splitQuantity, Long newOwnerUserId, String newTxnId){
        String loggerInfo = "拆分仓单：id="+storeGoodsId+",type="+bizType+",count="+splitQuantity+",userId="+newOwnerUserId;
        logger.info(loggerInfo);

        StoreGoods fromGoods = storeGoodsMapper.selectById(storeGoodsId);
        //未审核的仓单不能拆单
        boolean isAuditPending = Status.PENDING.getValue().equals(fromGoods.getStatus());
        if (isAuditPending) {
            throw new StoreGoodsException(StoreGoodsViewCode.DISABLE_STORE_GOODS.getCode(),StoreGoodsViewCode.DISABLE_STORE_GOODS.getMessage());
        }
        //检查旧数据的数量
        boolean isSufficientQuantity = fromGoods.getValidQuantity() >= splitQuantity;
        if (!isSufficientQuantity) {
            throw new StoreGoodsException(StoreGoodsViewCode.INSUFFICIENT_QUANTITY.getCode(),StoreGoodsViewCode.INSUFFICIENT_QUANTITY.getMessage());
        }
        //减少旧数据的数量，sql强检查数量是否足够
        Integer updatedRecordSize = storeGoodsMapper.calculateValidQuantity(storeGoodsId, -splitQuantity, fromGoods.getValidQuantity());
        if (!updatedRecordSize.equals(1)) {
            throw new StoreGoodsException(StoreGoodsViewCode.CONCURRENT_OPERATE.getCode(),StoreGoodsViewCode.CONCURRENT_OPERATE.getMessage());
        }

        //拆分出新的一条数据
        StoreGoods newPartStoreGoods = new StoreGoods();
        BeanUtils.copyProperties(fromGoods, newPartStoreGoods);
        newPartStoreGoods.setId(null);
        newPartStoreGoods.setTxnId(newTxnId);
        newPartStoreGoods.setPid(fromGoods.getId());
        newPartStoreGoods.setGoodsNo(storeGoodsService.getGoodsNo());
        newPartStoreGoods.setUserId(newOwnerUserId);
        newPartStoreGoods.setBizType(bizType);
        newPartStoreGoods.setQuantity(splitQuantity);
        newPartStoreGoods.setValidQuantity(splitQuantity);
        newPartStoreGoods.setCreateTime(LocalDateTime.now());
        newPartStoreGoods.setLastModifyTime(LocalDateTime.now());
        newPartStoreGoods.setStatus(Status.PENDING.getValue());
        storeGoodsMapper.insert(newPartStoreGoods);
        return newPartStoreGoods.getId();
    }

    /**
     * 正式启用某个仓单，不允许再归还数量
     * @param storeGoodsId 仓单Id
     */
    @Transactional
    public void pass(Long storeGoodsId){
        Integer successDisableCount = storeGoodsMapper.enableStoreGoods(storeGoodsId);
        if (!successDisableCount.equals(1)) {
            throw new StoreGoodsException(StoreGoodsViewCode.ERROR_FLOW.getCode(),StoreGoodsViewCode.ERROR_FLOW.getMessage());
        }
    }

    /**
     * 关闭某个仓单，并且归还数量到父仓单
     * @param storeGoodsId 仓单Id
     */
    @Transactional
    public void refuseAndReturnSplit(Long storeGoodsId){
        //无效化当前仓单
        Integer successDisableCount = storeGoodsMapper.disableStoreGoods(storeGoodsId);
        if (!successDisableCount.equals(1)) {
            throw new StoreGoodsException(StoreGoodsViewCode.ERROR_FLOW.getCode(),StoreGoodsViewCode.ERROR_FLOW.getMessage());
        }

        //如果从某个仓单分裂出来的，归还数量
        StoreGoods subStoreGoods = storeGoodsMapper.selectById(storeGoodsId);
        Long parentId = subStoreGoods.getPid();
        if (parentId==null||parentId==-1) {
            return;
        }
        StoreGoods parentStoreGoods = storeGoodsMapper.selectById(parentId);
        storeGoodsMapper.calculateValidQuantity(parentStoreGoods.getId(), subStoreGoods.getValidQuantity(), parentStoreGoods.getValidQuantity());
    }

    /**
     * 删除仓单数量，提货专用
     * @param storeGoodsId 仓单Id
     */
    @Transactional
    public void delete(Long storeGoodsId){
        StoreGoods storeGoods = storeGoodsMapper.selectById(storeGoodsId);
        boolean isExtraType = storeGoods.getStatus().equals(8);
        if (!isExtraType) {
            throw new StoreGoodsException(StoreGoodsViewCode.ERROR_FLOW.getCode(),StoreGoodsViewCode.ERROR_FLOW.getMessage());
        }
        Integer successStatus = storeGoodsMapper.calculateValidQuantity(storeGoods.getId(), -storeGoods.getValidQuantity(), storeGoods.getValidQuantity());
        if (!successStatus.equals(1)) {
            throw new StoreGoodsException(StoreGoodsViewCode.ERROR_FLOW.getCode(),StoreGoodsViewCode.ERROR_FLOW.getMessage());
        }
    }

}
