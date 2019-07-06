package com.dtlonline.api.shop.command;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@Accessors
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductPositionParamDTO {

    @NotBlank(message = "省不能为空")
    private String province;

    @NotBlank(message = "市不能为空")
    private String city;

    @NotBlank(message = "区不能为空")
    private String area;

    @NotBlank(message = "详细地址不能为空")
    private String address;

}
