package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Shop;
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
    private Long userId;
    private String name;
    private Integer level;
    private String levelNickname;

    public static ShopBriefnessDTO of(Shop shop) {
        if (shop == null) {
            return null;
        }
        ShopBriefnessDTO shopBriefnessDTO = new ShopBriefnessDTO();
        BeanUtils.copyProperties(shop, shopBriefnessDTO);
        return shopBriefnessDTO;
    }
}
