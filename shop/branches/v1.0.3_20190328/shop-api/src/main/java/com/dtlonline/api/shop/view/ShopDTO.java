package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    @JsonProperty("shopId")
    private Long id;

    private Long userId;

    private String name;

    private String headerId;

    private Integer type;

    private String area;

    private String address;

    private String contactName;

    private String mobile;

    private Integer status;

    private Boolean personalCertificate;

    private Boolean enterpriseCertification;

    private Boolean fieldCertification;

    private String level;

    private String levelNickname;
}
