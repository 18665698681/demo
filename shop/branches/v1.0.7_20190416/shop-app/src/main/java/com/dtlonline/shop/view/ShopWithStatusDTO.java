package com.dtlonline.shop.view;

import com.dtlonline.api.user.command.system.SellerPrivilegeDTO;
import com.dtlonline.shop.model.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.security.aes.AESUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ShopWithStatusDTO {
    @JsonProperty("shopId")
    private Long id;
    private Long userId;
    private String name;
    @JsonIgnore
    private Long headerId;
    private String headerUrl;
    private String mobile;
    private Integer type;
    private String area;
    private String address;
    private String contactName;
    private LocalDateTime createTime;
    private Boolean personalCertificate;
    private Boolean enterpriseCertification;
    private Boolean fieldCertification;
    private SellerPrivilegeDTO sellerPrivilegeDTO;

    public static ShopWithStatusDTO of(Shop shop) {
        if (shop == null) {
            return null;
        }
        ShopWithStatusDTO shopWithStatusDTO = new ShopWithStatusDTO();
        BeanUtils.copyProperties(shop, shopWithStatusDTO);
        shopWithStatusDTO.setMobile(AESUtil.decrypt(shopWithStatusDTO.getMobile()));
        return shopWithStatusDTO;
    }
}
