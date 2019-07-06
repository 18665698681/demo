package com.dtlonline.shop.view;

import com.dtlonline.shop.model.ProductStandardRecord;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductStandardRecordListDTO extends BaseObject {
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

    public static final ProductStandardRecordListDTO of(ProductStandardRecord productStandardRecord) {
        if (productStandardRecord == null) {
            return null;
        }
        ProductStandardRecordListDTO productStandardRecordListDTO = new ProductStandardRecordListDTO();
        BeanUtils.copyProperties(productStandardRecord, productStandardRecordListDTO);
        return productStandardRecordListDTO;
    }
}
