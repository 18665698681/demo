package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Shop;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class ShopDTO {
    @JsonProperty("shopId")
    private Long id;

    private Long userId;

    private String name;

    private Long headerId;

    private String headerUrl;

    private Integer type;

    private String area;

    private String address;

    private String contactName;

    private String mobile;

    private Integer status;

    private Date lastModifyTime;

    public static ShopDTO of(Shop shop) {
        if (shop == null) {
            return null;
        }
        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shop, shopDTO);
        return shopDTO;
    }
}
