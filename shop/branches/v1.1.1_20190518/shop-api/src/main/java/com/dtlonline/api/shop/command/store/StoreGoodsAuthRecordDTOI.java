package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="入库申请参数对象")
public class StoreGoodsAuthRecordDTOI {

    @ApiModelProperty(value="业务唯一标识")
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;

    @ApiModelProperty(value="仓库")
    @NotNull(message = "仓库ID不能为空")
    private Long storeId;

    @Min(value = 0,message = "数量输入错误")
    @NotNull(message = "数量不能为空")
    @ApiModelProperty(value="数量")
    private Integer quantity;

    @ApiModelProperty(value="预计入库日期")
    @NotNull(message = "预计入库日期不能为空")
    private LocalDate inStoreDate;


    @ApiModelProperty(value="车辆车牌")
    @NotBlank(message = "车牌不能为空")
    private String carNumber;


    @ApiModelProperty(value="商品保险金额")
    private BigDecimal insuranceMoney;


    @ApiModelProperty(value="商品保险开始日期")
    private LocalDate insuranceBeginDate;


    @ApiModelProperty(value="商品保险结束日期")
    private LocalDate insuranceEndDate;

    @ApiModelProperty(value="申请商品信息")
    private ProductRecordDTO productRecordDTO;

}