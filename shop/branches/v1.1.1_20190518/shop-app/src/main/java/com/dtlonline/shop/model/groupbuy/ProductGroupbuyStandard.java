package com.dtlonline.shop.model.groupbuy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_groupbuy_standard")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuyStandard extends BaseObject {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 团购活动表ID
     */
    private Long productGroupbuyId;

    /**
     * 达标数量（吨）
     */
    private Double targetWeight;

    /**
     * 已参与数量（吨）
     */
    private Double joinWeight;

    /**
     * 是否达标
     */
    private Integer isReachTarget;

    /**
     * 达标时的参与数量
     */
    private Double reachTargetJoinWeight;

    /**
     * 达到目标数量的时间
     */
    private LocalDateTime reachTargetDate;

    /**
     * 团购价优惠比例
     */
    private BigDecimal priceScale;

    /**
     * 删除状态： -1 - 已删除 ，1 - 有效
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
