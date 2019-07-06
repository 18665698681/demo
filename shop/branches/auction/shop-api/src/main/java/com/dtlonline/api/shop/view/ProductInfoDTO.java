package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductInfoDTO extends BaseObject {
    /**
     * 商品ID
     */
    @JsonProperty("productId")
    private Long id;
    /**
     * 商品标题
     */
    private String title;

    private Long categoryId;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 剩余库存
     */
    private Integer laveStock;
    /**
     * 图片
     */
    @JsonProperty("imgUrl")
    private String imgs;
    /**
     * 单价 -吨
     */
    private BigDecimal unitPrice;
    /**
     * 最小交易量
     */
    private Integer minDeal;

    /**
     * 1-现货 2-物流 3-仓储 4-竞拍 99-采购
     **/
    private Integer type;

    /**
     * 是否显示价格 1-显示 2-不显示
     */
    private Integer showPrice;
    /**
     * 生产年份
     */
    private Integer productionYear;
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

    private String address;
    /**
     * 区
     */
    private String area;
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
     * 店铺ID
     */
    private Long shopId;
    /**
     * 商品信息描述
     */
    @JsonProperty("descriptions")
    private String description;
    /**
     * 商品图片URL
     */
    private List<String> imgList;
}
