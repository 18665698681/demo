package com.dtlonline.shop.model;

import com.dtlonline.api.shop.view.ShopDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Shop {
    private Long id;
    private String txnId;
    private Long userId;
    private String name;
    private Long headerId;
    private Integer type;
    private String area;
    private String address;
    private String contactName;
    private String mobile;
    private Integer status;
    private Date createTime;
    private Date lastModifyTime;

    public static Shop of(ShopAuthRecord shopAuthRecord) {
        Shop shop = new Shop();
        BeanUtils.copyProperties(shopAuthRecord, shop);
        return shop;
    }

    public static ShopDTO of(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shop, shopDTO);
        return shopDTO;
    }
}
