package com.dtlonline.shop.model.auction;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_auction")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductAuction extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;

    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 价格类型(1散拍2整拍)
     */
    private Integer priceType;

    /**
     * 标题
     */
    private String title;

    /**
     * 最小加价
     */
    private BigDecimal minAddPrice;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 买家保证金
     */
    private BigDecimal buyerEarnestMoney;

    /**
     * 卖家保证金
     */
    private BigDecimal sellerEarnestMoney;

    /**
     * 保证金状态(1待缴纳2冻结成功3释放成功)
     */
    private Integer earnestMoneyStatus;

    /**
     * 审核状态 1-通过 2-未通过 3-待审核
     **/

    private Integer auditStatus;

    /**
     * 审核状态 审核人
     **/
    private String auditUser;

    /**
     * 审核意见
     */
    private String auditOpinion;


    /**
     * 交货时间
     */
    private String deliveryTime;

    /**
     * 交货方式(1自提2卖家配送)
     */
    private Integer deliveryType;

    /**
     * 创建人 发布者
     */
    private Long userId;

    /**
     * 竞拍结束生成订单(1未生成，2已生成)
     */
    private Integer buildOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifyTime;


}
