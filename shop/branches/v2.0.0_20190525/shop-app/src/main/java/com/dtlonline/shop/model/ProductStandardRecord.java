package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.ProductStandardListDTO;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@TableName("product_standard_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ProductStandardRecord extends BaseObject {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品唯一标识
     */
    private String txnId;
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
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime lastModifyTime;

    public static final ProductStandardListDTO of(ProductStandardRecord productStandardRecord){
        ProductStandardListDTO productStandardListDTO = new ProductStandardListDTO();
        Optional.ofNullable(productStandardRecord).ifPresent(record -> BeanUtils.copyProperties(productStandardRecord,productStandardListDTO));
        return productStandardListDTO;
    }
}