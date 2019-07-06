package com.dtlonline.api.shop.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductCarInfoDTO {

    private String startingPoint;

    private String terminalPoint;

    private String carPlate;

    private LocalDate sendDate;

    private LocalDate arrivalDate;

    private Integer theWay;

    private String address;

    private String shipDate;

    private String reachDate;
}
