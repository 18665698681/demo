package com.dtlonline.shop.model.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.activity.ActivityConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
* Created by Mybatis Generator 2019/06/04
*/
@ApiModel(value="com.dtlonline.shop.model.activity.ActivityConfig")
@Data
@TableName(value = "activity_config")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ActivityConfig implements Serializable {
    /**
     * id
     */
     @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 业务唯一标识
     */
    @TableField(value = "txnId")
    @ApiModelProperty(value="业务唯一标识")
    private String txnId;

    /**
     * 活动类型 1-团购
     */
    @TableField(value = "type")
    @ApiModelProperty(value="活动类型 1-团购")
    private Integer type;

    /**
     * 广告图
     */
    @TableField(value = "imgId")
    @ApiModelProperty(value="广告图")
    private Long imgId;

    /**
     * 内部链接详情ID
     */
    @TableField(value = "activityId")
    @ApiModelProperty(value="内部链接详情ID")
    private Long activityId;

    /**
     * 跳转外部链接url
     */
    @TableField(value = "url")
    @ApiModelProperty(value="跳转外部链接url")
    private String url;

    /**
     * 排序
     */
    @TableField(value = "`index`")
    @ApiModelProperty(value="排序")
    private Integer index;

    /**
     * 状态 -1-逻辑删除 1-已发布 2-未发布
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态 -1-逻辑删除 1-已发布 2-未发布")
    private Integer status;

    /**
     * 创建人
     */
    @TableField(value = "createdBy")
    @ApiModelProperty(value="创建人")
    private String createdBy;

    /**
     * 创建日期
     */
    @TableField(value = "createTime")
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="修改日期")
    private LocalDateTime lastModifyTime;


    public static final ActivityConfigDTO of(ActivityConfig activityConfig){
        ActivityConfigDTO activityConfigDTO = ActivityConfigDTO.builder().build();
        Optional.ofNullable(activityConfig).ifPresent(activity->{
            BeanUtils.copyProperties(activity,activityConfigDTO);
        });
        return activityConfigDTO;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_TXNID = "txnId";

    public static final String COL_TYPE = "type";

    public static final String COL_IMGID = "imgId";

    public static final String COL_JUMPTYPE = "jumptype";

    public static final String COL_ACTIVITYID = "activityId";

    public static final String COL_URL = "url";

    public static final String COL_INDEX = "index";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATEDBY = "createdBy";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}