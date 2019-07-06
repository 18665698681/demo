package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductSupplyDetailDTO extends BaseObject {
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
     * 剩余库存
     */
    private Integer laveStock;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 地址
     */
    private String depotAddress;
    /**
     * 商品信息描述
     */
    @JsonProperty("descriptions")
    private String description;
    /**
     * 商品类型 97-需求 98-供应
     */
    private Integer type;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 店铺信息
     */
    private ShopDTO shopBriefness;
    /**
     * 基础信息
     */
    private List<ProductStandardListDTO> productStandardList;

}
