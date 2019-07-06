package com.dtlonline.api.shop.view.groupbuy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductGroupbuyStandardResponseDTO extends BaseObject {

    /**
     * 活动规则ID
     */
    private Long id;

    /**
     * 团购活动表ID
     */
    private Long productGroupbuyId;

    /**
     * 达标数量（吨）
     */
    private Double targetWeight;

    /**
     * 团购价优惠比例
     */
    private BigDecimal priceScale;

}
