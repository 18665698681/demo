package com.dtlonline.shop.model.groupbuy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("product_groupbuy")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuy extends BaseObject {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 业务ID
     */
    private String txnId;

    /**
     * 团购类型：1 - 协议，2 - 意向
     */
    private Integer type;

    /**
     * 团购标题
     */
    private String title;

    /**
     * 团购年份
     */
    private String year;

    /**
     * 团购月份
     */
    private String month;

    /**
     * 品种ID（一级）
     */
    private Long categoryId;

    /**
     * 品种名称
     */
    private String categoryTitle;

    /**
     * 结算方式： 1 - 款到发货 ，2 - 货到付款
     */
    private Integer balanceType;

    /**
     * 最小交易量（吨）
     */
    private Double minTrade;

    /**
     * 单个品牌最小达标量
     */
    private Double minDiscountWeight;

    /**
     * 市场参考价格
     */
    private BigDecimal marketPrice;

    /**
     * 报名开始时间
     */
    private LocalDate buyBeginDate;

    /**
     * 报名截止时间
     */
    private LocalDate buyEndDate;

    /**
     * 活动截止时间
     */
    private LocalDate activityEndDate;

    /**
     * 展示图片
     */
    private String showImageIds;

    /**
     * 项目简介
     */
    private String description;

    /**
     * 团购已参数总量
     */
    private Double buyTotalWeight;

    /**
     * 是否已团购公示： -1 - 未公示，1 已公示
     **/
    private Integer isPublished;

    /**
     * 是否置顶：1 - 是，0 - 否
     **/
    private Integer isTop;

    /**
     * 状态： -1 - 已删除 ，1 - 待审核，2 - 审核通过，3 - 审核未通过
     **/
    private Integer status;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifyTime;

}
