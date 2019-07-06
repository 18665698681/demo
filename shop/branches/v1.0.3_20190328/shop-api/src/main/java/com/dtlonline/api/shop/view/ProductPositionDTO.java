package com.dtlonline.api.shop.view;

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
public class ProductPositionDTO {

    private String txnId;

    private String province;

    private String city;

    private String area;

    private String address;

    private String longitude;

    private String latitude;
}
