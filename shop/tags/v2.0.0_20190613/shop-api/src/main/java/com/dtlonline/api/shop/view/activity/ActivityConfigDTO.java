package com.dtlonline.api.shop.view.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ActivityConfigDTO {

    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="业务唯一标识")
    private String txnId;

    @ApiModelProperty(value="活动类型 1-团购")
    private Integer type;

    @ApiModelProperty(value="广告图")
    private Long imgId;

    @ApiModelProperty(value="内部链接详情ID")
    private Long activityId;

    @ApiModelProperty(value="跳转外部链接url")
    private String url;

    @ApiModelProperty(value="排序")
    private Integer index;

    @ApiModelProperty(value="状态 -1-逻辑删除 1-已发布 2-未发布")
    private Integer status;

    @ApiModelProperty(value="创建人")
    private String createdBy;

    @ApiModelProperty(value="创建日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GMT+8")
    private LocalDateTime lastModifyTime;

}
