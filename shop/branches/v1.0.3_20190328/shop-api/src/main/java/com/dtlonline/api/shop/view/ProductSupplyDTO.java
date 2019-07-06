package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductSupplyDTO {

    @JsonProperty("productId")
    private Long id;

    private Integer type;

    private String title;

    private ProductCarInfoDTO supply = new ProductCarInfoDTO();

    private ProductPositionDTO demand = new ProductPositionDTO();
}
