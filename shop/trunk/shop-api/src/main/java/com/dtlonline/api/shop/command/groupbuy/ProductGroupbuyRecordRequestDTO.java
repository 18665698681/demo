package com.dtlonline.api.shop.command.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel("团购报名参数对象")
public class ProductGroupbuyRecordRequestDTO {

    @ApiModelProperty("团购ID")
    @NotNull(message = "团购ID不能为空")
    private Long productGroupbuyId;

    @ApiModelProperty("业务唯一标识")
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;

    @ApiModelProperty("生产年份")
    @NotNull(message = "生产年份不能为空")
    private Integer productiveYear;

    @ApiModelProperty("买家合同ID")
    private String buyerContractIds;

    @ApiModelProperty("报名明细")
    @Valid
    @NotNull.List({@NotNull(groups = ProductGroupbuyRecordDetailRequestDTO.class,message = "报名信息不能为空")})
    private List<ProductGroupbuyRecordDetailRequestDTO> groupbuyRecordDetailList;

}
