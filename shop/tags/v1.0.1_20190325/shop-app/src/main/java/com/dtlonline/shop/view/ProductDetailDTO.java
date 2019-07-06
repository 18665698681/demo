package com.dtlonline.shop.view;

import com.dtlonline.api.user.view.UserAuthDetailDTO;
import com.dtlonline.shop.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductDetailDTO extends BaseObject {
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 剩余库存
     */
    private Integer laveStock;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 最小交易量
     */
    private Integer minDeal;
    /**
     * 品种ID
     */
    private Long categoryId;
    /**
     * 地址
     */
    private String depotAddress;
    /**
     * 详细地址
     */
    private String address;
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
     * 是否显示价格 1-显示 2-不显示
     */
    private Integer showPrice;
    /**
     * 是否开发票
     */
    private Integer invoice;
    /**
     * 生产年份
     */
    private String productionYear;
    /**
     * 商品信息描述
     */
    @JsonProperty("descriptions")
    private String description;
    /**
     * 商品类型 1-现货 2-物流 3-仓储 99-采购
     */
    private Integer type;
    /**
     * 商品图片URL
     */
    private List imgList;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 是否自家店铺
     */
    private Integer userIfShop;
    /**
     * 是否实名认证 1-已认证 2-未认证
     */
    private Boolean personalCertificate;
    /**
     * 店铺信息
     */
    private ShopWithStatusDTO shopBriefness;
    /**
     * 用户信息
     */
    private UserAuthDetailDTO userEntity;
    /**
     * 基础信息
     */
    private List<ProductStandardRecordListDTO> productStandardList;
    /**
     * 品类信息
     */
    private CategoryDTO category;

    public static final ProductDetailDTO of(Product product) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        Optional.ofNullable(product).ifPresent(p -> BeanUtils.copyProperties(product, productDetailDTO));
        StringBuffer sbf = new StringBuffer();
        sbf.append(product.getProvince());
        sbf.append(product.getCity());
        sbf.append(product.getArea());
        productDetailDTO.setDepotAddress(sbf.toString());
        return productDetailDTO;
    }
}
