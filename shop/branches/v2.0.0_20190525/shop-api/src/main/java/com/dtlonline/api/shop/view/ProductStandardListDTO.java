package com.dtlonline.api.shop.view;

import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductStandardListDTO extends BaseObject {
    /**
     * 商品唯一标识
     */
    private String productTxnId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 品类ID
     */
    private Long categoryId;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 规格ID
     */
    private Long standardId;
    /**
     * 规格名称
     */
    private String standardName;
    /**
     * 数据
     */
    private String data;
}
