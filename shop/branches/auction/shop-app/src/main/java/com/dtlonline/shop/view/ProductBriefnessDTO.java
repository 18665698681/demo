package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author Deveik
 * @date 2019/01/23
 */
@Data
public class ProductBriefnessDTO {
    @JsonProperty("productId")
    private Long id;
    @JsonIgnore
    private String txnId;
    private String title;
    private BigDecimal unitPrice;
    private Integer stock;
    @JsonProperty("imgUrl")
    private String imgs;
    private Integer status;
    @JsonProperty("area")
    private String city;
    private Integer type;

    public static ProductBriefnessDTO of(Product product){
        if (product == null){
            return null;
        }
        ProductBriefnessDTO productBriefnessDTO = new ProductBriefnessDTO();
        BeanUtils.copyProperties(product, productBriefnessDTO);
        return productBriefnessDTO;
    }
}
