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
import com.dtlonline.api.user.remote.UserAuthRemoteService;
import com.dtlonline.api.user.remote.UserInfosRemoteService;
import com.dtlonline.api.user.remote.UserPrivilegeRemoteService;
import com.dtlonline.api.user.view.UserDTO;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.ShopAuthRecordDao;
import com.dtlonline.shop.mapper.ShopDao;
import com.dtlonline.shop.model.Shop;
import com.dtlonline.shop.model.ShopAuthRecord;
import com.dtlonline.shop.view.*;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.ObjectUtils;
import io.alpha.security.aes.AESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ShopService extends BaseService {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopAuthRecordDao shopAuthRecordDao;

    @Autowired
    private UserAuthRemoteService userAuthRemoteService;

    @Autowired
    private UserInfosRemoteService userInfosRemoteService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private UserPrivilegeRemoteService userPrivilegeRemoteService;

    public IPage<ShopAuthRecordPageInfoDTO> queryShopAuthRecordsPage(ShopAuthQueryPageDTO<ShopAuthRecord> shopAuthQueryPageDTO) {
        return shopAuthRecordDao.queryShopAuthRecordsInPageByCondition(shopAuthQueryPageDTO,
                shopAuthQueryPageDTO.getName(), shopAuthQueryPageDTO.getShopType(), shopAuthQueryPageDTO.getStatus());
    }

    public ShopAuthRecord queryAuditOperationById(Long id) {
        ShopAuthRecord shopAuthRecord = shopAuthRecordDao.selectOne(Wrappers.<ShopAuthRecord>lambdaQuery().eq(ShopAuthRecord::getId, id));
        Optional.ofNullable(shopAuthRecord).orElseThrow(() -> new ShopException(ViewCode.SHOP_FAILURE.getCode(), "店铺详情出现错误,请联系管理员"));
        shopAuthRecord.setMobile(AESUtil.decrypt(shopAuthRecord.getMobile()));
        return shopAuthRecord;
    }

    public ShopAuthRecord queryAuditOperationByTxnId(String txnId) {
        return shopAuthRecordDao.selectOne(Wrappers.<ShopAuthRecord>lambdaQuery().eq(ShopAuthRecord::getTxnId, txnId));
    }

    public void saveAuditOperation(ShopAuditDTO shopAuditDTO, Long userId) {
        Boolean enterpriseAuthStatus = userAuthRemoteService.queryEnterpriseAuthStatus(userId).getData();
        if (!enterpriseAuthStatus) {
            throw new ShopException(ShopViewCode.NOT_ENTERPRISE_AUTH_STATUS.getCode(), ShopViewCode.NOT_ENTERPRISE_AUTH_STATUS.getMessage());
        }
        Integer shopAuthStatus = shopAuthRecordDao.queryLastAuthStatusByUserId(userId);
        // 处理存在【待审核】、【通过】情况
        if (shopAuthStatus != null) {
            if (shopAuthStatus.equals(Status.PENDING.getValue())) {
                throw new ShopException(io.alpha.app.core.view.ViewCode.FAILURE.getCode(), "已存在审核中的记录");
            } else if (shopAuthStatus.equals(Status.PASS.getValue())) {
                throw new ShopException(io.alpha.app.core.view.ViewCode.FAILURE.getCode(), "店铺审核已通过");
            }
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
        if (!Status.PASS.getValue().equals(result)) {
            throw new ShopException(ViewCode.SHOP_FAILURE.getCode(), "已审核过的店铺不能再审核");
        }
    }

    public ShopBriefnessDTO queryShopBriefnessInformation(Long id) {
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, id));
        if (shop == null) {
            return new ShopBriefnessDTO();
        }
        ShopBriefnessDTO shopDto = ObjectUtils.copy(shop, ShopBriefnessDTO.class);
        UserDTO userDto = userInfosRemoteService.infos(null, shop.getUserId()).getData();
        if (userDto!=null) {
            Long profileImgId = userDto.getProfileImg();
            if (profileImgId != null) {
                shopDto.setProfileImg(profileImgId);
                shopDto.setHeaderUrl(imageRemoteService.queryUrl(profileImgId));
            }
        }
        return shopDto;
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
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, shopId).eq(Shop::getStatus, ShopStatus.VALID.getValue()));
        if (shop == null) {
            return shopDTO;
        }
        shopDTO.setPersonalCertificate(userAuthRemoteService.queryRealnameAuthStatus(shop.getUserId()).getData());
        shopDTO.setEnterpriseCertification(userAuthRemoteService.queryEnterpriseAuthStatus(shop.getUserId()).getData());
        shopDTO.setFieldCertification(userAuthRemoteService.querySiteAuthStatus(shop.getUserId()).getData());
        BeanUtils.copyProperties(shop, shopDTO);
        shopDTO.setMobile(AESUtil.decrypt(shop.getMobile()));
        return shopDTO;
    }

    public ShopDTO queryByUserId(Long userId) {
        ShopDTO shopDTO = new ShopDTO();
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getUserId, userId).eq(Shop::getStatus, ShopStatus.VALID.getValue()));
        if (shop == null) {
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
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getUserId, userId).eq(Shop::getStatus, Status.PASS.getValue()));
        return changeShopIntoShopWithStatusDTO(shop);
    }

    public ShopWithStatusDTO queryShopWithStatusDTO(Long id) {
        Shop shop = shopDao.selectOne(Wrappers.<Shop>lambdaQuery().eq(Shop::getId, id).eq(Shop::getStatus, Status.PASS.getValue()));
        return changeShopIntoShopWithStatusDTO(shop);
    }

    private ShopWithStatusDTO changeShopIntoShopWithStatusDTO(Shop shop) {
        ShopWithStatusDTO shopWithStatusDTO = ShopWithStatusDTO.of(shop);
        if (shop == null) {
            return shopWithStatusDTO;
        }
        SellerPrivilegeDTO sellerPrivilegeDTO = userPrivilegeRemoteService.sellerPrivilegeByUser(shop.getId()).getData();
        shopWithStatusDTO.setEnterpriseCertification(userAuthRemoteService.queryEnterpriseAuthStatus(shop.getUserId()).getData())
                .setPersonalCertificate(userAuthRemoteService.queryRealnameAuthStatus(shop.getUserId()).getData())
                .setFieldCertification(userAuthRemoteService.querySiteAuthStatus(shop.getUserId()).getData())
                .setSellerPrivilegeDTO(sellerPrivilegeDTO);
        UserDTO userDto = userInfosRemoteService.infos(null, shop.getUserId()).getData();
        Long profileImgId = userDto.getProfileImg();
        if (profileImgId != null) {
            shopWithStatusDTO.setProfileImg(profileImgId);
            shopWithStatusDTO.setHeaderUrl(imageRemoteService.queryUrl(profileImgId));
        }
        return shopWithStatusDTO;
    }

    public Map<Long, ShopDTO> queryByIds(Set<Long> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", ids);
        List<Shop> list = shopDao.selectList(wrapper);
        Map<Long, ShopDTO> shopDTOMap = new HashMap<>(list.size());
        list.stream().forEach(shop -> {
            ShopDTO shopDTO = Shop.of(shop);
            shopDTO.setEnterpriseCertification(userAuthRemoteService.queryEnterpriseAuthStatus(shop.getUserId()).getData())
                    .setPersonalCertificate(userAuthRemoteService.queryRealnameAuthStatus(shop.getUserId()).getData())
                    .setFieldCertification(userAuthRemoteService.querySiteAuthStatus(shop.getUserId()).getData());
            shopDTOMap.put(shop.getId(), shopDTO);
        });
        return shopDTOMap;
    }
}
