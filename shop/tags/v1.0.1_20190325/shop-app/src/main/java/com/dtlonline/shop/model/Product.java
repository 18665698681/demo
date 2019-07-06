package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("product")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 1-可卖 2-可借 3-可换
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
     * 剩余库存
     */
    private Integer laveStock;
    /**
     * 最小交易量
     */
    private Integer minDeal;
    /**
     * 备注
     */
    private String description;
    /**
     * 状态 1 -成功 2 -失败
     */
    private Integer status;
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
     * 1 -在途 2-非在途
     */
    private Integer theWay;
    /**
     * 1-在库 2-非在库
     */
    private Integer theStock;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    public static final ProductInfoDTO of(Product product, List<String> imageList) {
        if (product == null) {
            return null;
        }
        ProductInfoDTO dto = new ProductInfoDTO();
        BeanUtils.copyProperties(product, dto);
        dto.setImgList(imageList);
        return dto;
    }

    public static final ProductInfoDTO of(Product product) {
        if (product == null) {
            return null;
        }
        ProductInfoDTO dto = new ProductInfoDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}
