package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

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
    private Date createTime;
    /**
     * 修改时间
     */
    private Date lastModifyTime;
}