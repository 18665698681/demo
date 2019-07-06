package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ShopAuditDTO;
import com.dtlonline.api.shop.command.ShopAuthQueryPageDTO;
import com.dtlonline.api.shop.constant.ShopStatus;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.api.user.command.system.SellerPrivilegeDTO;
import com.dtlonline.api.user.remote.PrivilegeRemoteService;
import com.dtlonline.api.user.remote.UserRemoteService;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.ShopAuthRecordDao;
import com.dtlonline.shop.mapper.ShopDao;
import com.dtlonline.shop.model.Shop;
import com.dtlonline.shop.model.ShopAuthRecord;
import com.dtlonline.shop.view.*;
import io.alpha.core.base.BaseService;
import io.alpha.security.aes.AESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ShopService extends BaseService {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopAuthRecordDao shopAuthRecordDao;

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private PrivilegeRemoteService privilegeRemoteService;

    public IPage<ShopAuthRecordPageInfoDTO> queryShopAuthRecordsPage(ShopAuthQueryPageDTO<ShopAuthRecord> shopAuthQueryPageDTO) {
        return shopAuthRecordDao.queryShopAuthRecordsInPageByCondition(shopAuthQueryPageDTO,
                shopAuthQueryPageDTO.getName(), shopAuthQueryPageDTO.getShopType(), shopAuthQueryPageDTO.getStatus());
    }

    public ShopAuthRecord queryAuditOperationById(Long id) {
        ShopAuthRecord shopAuthRecord = shopAuthRecordDao.selectOne(Wrappers.<ShopAuthRecord>lambdaQuery().eq(ShopAuthRecord::getId, id));
        Optional.ofNullable(shopAuthRecord).orElseThrow(()-> new ShopException(ViewCode.SHOP_FAILURE.getCode(),"店铺详情出现错误,请联系管理员"));
        shopAuthRecord.setMobile(AESUtil.decrypt(shopAuthRecord.getMobile()));
        return shopAuthRecord;
    }

    public ShopAuthRecord queryAuditOperationByTxnId(String txnId) {
        return shopAuthRecordDao.selectOne(Wrappers.<ShopAuthRecord>lambdaQuery().eq(ShopAuthRecord::getTxnId, txnId));
    }

    public void saveAuditOperation(ShopAuditDTO shopAuditDTO,Long userId) {
        Boolean enterpriseAuthStatus = userRemoteService.queryEnterpriseAuthStatus(userId);
        if(!enterpriseAuthStatus){
            throw new ShopException(ShopViewCode.NOT_ENTERPRISE_AUTH_STATUS.getCode(),ShopViewCode.NOT_ENTERPRISE_AUTH_STATUS.getMessage());
        }
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.of(shopAuditDTO);
        shopAuthRecord.setMobile(AESUtil.encrypt(shopAuditDTO.getMobile()));
        shopAuthRecord.setUserId(userId);
        shopAuthRecord.setStatus(Status.PENDING.getValue());
        shopAuthRecordDao.insert(shopAuthRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkShopAudit(String staffName, CheckInfoDTO checkInfoDTO) {
        Integer result = shopAuthRecordDao.updateShopAuthRecordsById(AESUtil.encrypt(staffName), checkInfoDTO);
        if (Status.PASS.getValue().equals(checkInfoDTO.getStatus()) && Status.PASS.getValue().equals(result)) {
            ShopAuthRecord shopAuthRecord = shopAuthRecordDao.selectOne(Wrappers
                    .<ShopAuthRecord>lambdaQuery().eq(ShopAuthRecord::getId, checkInfoDTO.getId()));
            Shop shop = Shop.of(shopAuthRecord);
            shopDao.insert(shop);
        }
        if (!Status.PASS.getValue().equals(result)){
            throw new ShopException(ViewCode.SHOP_FAILURE.getCode(),"已审核过的店铺不能再审核");
        }
    }

    public ShopBriefnessDTO queryShopBriefnessInformation(Long id) {
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, id));
        if (shop == null){
            return new ShopBriefnessDTO();
        }
        return ShopBriefnessDTO.of(shop);
    }

    public Boolean queryShopStatusByUserId(Long userId) {
        Integer status = shopDao.queryShopStatusByUserId(userId);
        return ShopStatus.VALID.getValue().equals(status);
    }

    public Integer queryShopAuthRecordsByUserId(Long userId) {
        return Optional.ofNullable(shopDao.queryShopAuthRecordsByUserId(userId))
                .orElse(Status.NOT_AUTH.getValue());
    }

    public ShopDTO queryById(Long shopId) {
        ShopDTO shopDTO = new ShopDTO();
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, shopId).eq(Shop::getStatus,ShopStatus.VALID.getValue()));
        if (shop == null){
            return shopDTO;
        }
        BeanUtils.copyProperties(shop, shopDTO);
        return shopDTO;
    }

    public ShopDTO queryByUserId(Long userId) {
        ShopDTO shopDTO = new ShopDTO();
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getUserId, userId).eq(Shop::getStatus,ShopStatus.VALID.getValue()));
        if (shop ==null){
            return shopDTO;
        }
        BeanUtils.copyProperties(shop, shopDTO);
        return shopDTO;
    }

    public void updateShopHeaderImgage(Long shopId, Long headerId) {
        // TODO：后期会有账户体系的，比如店小二或老板身份验证，在这里做验证
        shopDao.updateShopImageByShopId(shopId, headerId);
    }

    public ShopWithStatusDTO queryShopByUserId(Long userId) {
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getUserId, userId).eq(Shop::getStatus,Status.PASS.getValue()));
        return changeShopIntoShopWithStatusDTO(shop);
    }

    public ShopWithStatusDTO queryShopWithStatusDTO(Long id) {
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, id).eq(Shop::getStatus,Status.PASS.getValue()));
        return changeShopIntoShopWithStatusDTO(shop);
    }

    private ShopWithStatusDTO changeShopIntoShopWithStatusDTO(Shop shop) {
        ShopWithStatusDTO shopWithStatusDTO = ShopWithStatusDTO.of(shop);
        if (shop == null){
            return shopWithStatusDTO;
        }
        SellerPrivilegeDTO sellerPrivilegeDTO = privilegeRemoteService.sellerPrivilegeByUser(shop.getId());
        shopWithStatusDTO
                .setEnterpriseCertification(userRemoteService.queryEnterpriseAuthStatus(shop.getUserId()))
                .setPersonalCertificate(userRemoteService.queryRealnameAuthStatus(shop.getUserId()))
                .setFieldCertification(userRemoteService.querySiteAuthStatus(shop.getUserId()))
                .setLevel(sellerPrivilegeDTO.getLevel())
                .setLevelNickname(sellerPrivilegeDTO.getLevelNickname());
        if (shop.getHeaderId()!=null){
            shopWithStatusDTO.setHeaderUrl(imageRemoteService.queryUrl(shopWithStatusDTO.getHeaderId()));
        }
        return shopWithStatusDTO;
    }

    public Map<Long, ShopDTO> queryByIds(Set<Long> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", ids);
        List<Shop> list = shopDao.selectList(wrapper);
        return list.stream().collect(Collectors.toMap(Shop::getId, Shop::of));
    }
}
