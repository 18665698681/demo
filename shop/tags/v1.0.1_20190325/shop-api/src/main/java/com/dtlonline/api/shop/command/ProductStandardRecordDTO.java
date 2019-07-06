package com.dtlonline.api.shop.command;

import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductStandardRecordDTO extends BaseObject {
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
