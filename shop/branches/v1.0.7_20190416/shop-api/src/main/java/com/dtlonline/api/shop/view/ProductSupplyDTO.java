package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductSupplyDTO {

    @JsonProperty("productId")
    private Long id;

    private String txnId;

    private Integer type;

    private String title;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    /**
     * 店铺信息
     */
    private ShopDTO shopBriefness;
    /**
     * 供应信息
     */
    private ProductCarInfoDTO supply = new ProductCarInfoDTO();
    /**
     * 需求信息
     */
    private ProductPositionDTO demand = new ProductPositionDTO();
    /**
     * 规格ID
     */
    private Long categoryId;
    /**
     * 规格名称
     */
    private String categoryName;
    /**
     * 创建人ID
     */
    private Long userId;
    /**
     * 审核状态
     */
    private Integer status;
}
