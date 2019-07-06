package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Shop;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author Deveik
 * @date 2019/01/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopBriefnessDTO {
    private Integer type;
    private String name;
    private String level;
    private String levelNickname;

    public static ShopBriefnessDTO of(Shop shop) {
        if (shop == null){
            return null;
        }
        ShopBriefnessDTO shopBriefnessDTO = new ShopBriefnessDTO();
        BeanUtils.copyProperties(shop, shopBriefnessDTO);
        return shopBriefnessDTO;
    }
}
