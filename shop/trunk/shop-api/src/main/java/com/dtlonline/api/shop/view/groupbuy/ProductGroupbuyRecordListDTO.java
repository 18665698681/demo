package com.dtlonline.api.shop.view.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="团购报名列表",description="团购报名列表")
public class ProductGroupbuyRecordListDTO {

    @ApiModelProperty("团购ID")
    private Long groupbuyId;

    @ApiModelProperty("团购图片")
    private String groupbuyImg;

    @ApiModelProperty("报名编号")
    private String groupbuyRecordNo;

    @ApiModelProperty("报名ID")
    private Long groupbuyRecordId;

    @ApiModelProperty("团购类型：1 - 协议，2 - 意向 ")
    private Integer type;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("是否已公示 是否已团购公示： 1 - 已公示，2 - 未公示")
    private Integer isPublished;

    @ApiModelProperty("货物品类")
    private String categoryName;

    @ApiModelProperty("参团数量")
    private Integer quantity;

    @ApiModelProperty("审核状态 1-已通过 2-不通过 3-待审核 4-待撮合")
    private Integer status;

    @ApiModelProperty("审核状态名称")
    private String statusName;

    @ApiModelProperty("报名时间")
    private String createTime;

    @ApiModelProperty("活动截止日期")
    private String activityEndDate;

    @ApiModelProperty("单个品牌最小达标量")
    private Integer minDiscountQuantity;

    @ApiModelProperty("团购报名明细信息")
    private List<ProductGroupbuyRecordDetailListDTO> groupbuyRecordDetailList;
}
