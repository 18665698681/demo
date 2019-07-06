package com.dtlonline.shop.view;

import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.shop.model.ProductRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ProductRecordDetailDTO extends BaseObject {

    @JsonProperty("productId")
    private Long id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 单价 -元
     */
    private BigDecimal unitPrice;
    /**
     * 库存 -吨
     */
    private Integer stock;
    /**
     * 最小交易量
     */
    private Integer minDeal;
    /**
     * 仓库地址
     */
    private String depotAddress;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 生产年份
     */
    private String productionYear;
    /**
     * 审核状态
     */
    private Integer status;

    private String label;
    /**
     * 基础信息
     */
    private List<ProductStandardRecordListDTO> productStandardList;
    /**
     * 商品图片URL
     */
    private List imgList;
    /**
     * 品类信息
     */
    private CategoryDTO category;

    private ShopDTO shop;

    public static final ProductRecordDetailDTO of(ProductRecord productRecord) {
        ProductRecordDetailDTO productRecordDetailDTO = new ProductRecordDetailDTO();
        Optional.ofNullable(productRecord).ifPresent(record -> BeanUtils.copyProperties(productRecord, productRecordDetailDTO));
        StringBuffer sbf = new StringBuffer();
        sbf.append(productRecord.getProvince());
        sbf.append(productRecord.getCity());
        sbf.append(productRecord.getArea());
        productRecordDetailDTO.setDepotAddress(sbf.toString());
        return productRecordDetailDTO;
    }
}
