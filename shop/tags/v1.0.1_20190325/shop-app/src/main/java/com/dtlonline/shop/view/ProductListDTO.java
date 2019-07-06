package com.dtlonline.shop.view;

import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductListDTO extends BaseObject {
    /**
     * 商品ID
     */
    @JsonProperty("productId")
    private Long id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private BigDecimal unitPrice;
    /**
     * 商品主图url地址
     */
    @JsonProperty("imgUrl")
    private String imgs;
    /**
     * 仓库地区
     */
    @JsonProperty("area")
    private String city;
    /**
     * 是否显示价格 1-显示 2-不显示
     */
    private Integer showPrice;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 店铺信息
     */
    private ShopBriefnessDTO shopBriefness;

    private Integer status;

    public static final ProductListDTO of(ProductInfoDTO product, ShopBriefnessDTO shopBriefnessDTO, String imgs) {
        if (product == null) {
            return null;
        }
        ProductListDTO productListDTO = new ProductListDTO();
        BeanUtils.copyProperties(product, productListDTO);
        if (shopBriefnessDTO != null) {
            productListDTO.setShopBriefness(shopBriefnessDTO);
        }
        productListDTO.setImgs(imgs);
        return productListDTO;
    }

}
