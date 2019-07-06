package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductSupplyMapListDTO {

    @JsonProperty("productId")
    private Long id;

    private Integer theWay;

    private Integer type;

    private String longitude;

    private String latitude;
}
