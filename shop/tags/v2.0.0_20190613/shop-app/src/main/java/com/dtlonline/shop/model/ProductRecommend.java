package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("product_recommend")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductRecommend {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 业务唯一标识
     */
    private String txnId;
    /**
     * 推荐商品唯一标识
     */
    private String productTxnId;

    /**
     * 类型 1-现货 2-物流 3-仓储 4-竞拍 5-仓单 10-团购活动 11-团购商品  99-采购
     */
    private Byte type;
    /**
     * banner图
     */
    private String bannerImgs;
    /**
     * 推荐权重
     */
    private Integer weights;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;
}
