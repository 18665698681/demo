package com.dtlonline.api.shop.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopAuditDTO {
    @NotBlank(message = "流水号不能为空")
    private String txnId;
    @NotBlank(message = "店铺名不能为空")
    @Size(max = 32, message = "店铺名长度不能超过32")
    private String name;
    private Integer type;
    @Size(max = 32, message = "区域长度不能超过32")
    private String area;
    @NotBlank(message = "地址不能为空")
    @Size(max = 256, message = "地址长度不能超过256")
    private String address;
    @NotBlank(message = "联系人名称不能为空")
    @Size(max = 32)
    private String contactName;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "手机号输入有误")
    private String mobile;
}
