package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.shop.view.ProductStandardRecordListDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("product_record")
@EqualsAndHashCode(callSuper = false)
public class ProductRecord extends BaseObject {
    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 1-可卖 2-可借 3可换
     */
    private Integer type;
    /**
     * 品类ID
     */
    private Long categoryId;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 图片
     */
    private String imgs;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 最小交易量
     */
    private Integer minDeal;
    /**
     * 备注
     */
    private String description;
    /**
     * 是否显示价格 1-显示 2-不显示
     */
    private Integer showPrice;
    /**
     * 生产年份
     */
    private String productionYear;
    /**
     * 是否开发票 1-开票 2-不开票
     */
    private Integer invoice;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 1-在库 2-非在库
     */
    private Integer theStock;
    /**
     * 1-在途 2-非在途
     */
    private Integer theWay;
    /**
     * 状态 1 -成功 2 -失败
     */
    private Integer status;
    /**
     * 审核意见
     */
    private String objection;
    /**
     * 审核人
     */
    private String recordName;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 审核时间
     */
    private Date lastModifyTime;

    public static final ProductStandardRecordListDTO of(ProductStandardRecord productStandardRecord) {
        if (productStandardRecord == null) {
            return null;
        }
        ProductStandardRecordListDTO productStandardRecordListDTO = new ProductStandardRecordListDTO();
        BeanUtils.copyProperties(productStandardRecord, productStandardRecordListDTO);
        return productStandardRecordListDTO;
    }
}
