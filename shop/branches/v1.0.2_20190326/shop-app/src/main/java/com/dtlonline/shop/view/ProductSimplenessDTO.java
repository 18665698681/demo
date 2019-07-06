package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author Deveik
 * @date 2019/01/24
 */
@Data
public class ProductSimplenessDTO {
    @JsonProperty("productId")
    private Long id;
    @JsonIgnore
    private String txnId;
    private String title;
    private BigDecimal unitPrice;
    private Integer stock;
    private String cityName;
    @JsonIgnore
    private String imgs;
    private String mainImage;

    public static ProductSimplenessDTO of(Product product) {
        if (product == null) {
            return null;
        }
        ProductSimplenessDTO productSimplenessDTO = new ProductSimplenessDTO();
        BeanUtils.copyProperties(product, productSimplenessDTO);
        return productSimplenessDTO;
    }
}
