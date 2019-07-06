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
import java.util.Date;

@Data
@TableName("product_auction_user")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductAuctionUser extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;

    /**
     * 产品Id
     */
    private Long productId;

    /**
     * 参与人
     */
    private Long userId;

    /**
     * 地址Id
     */
    private Long addressId;

    /**
     * 保证金
     */
    private BigDecimal earnestMoney;

    /**
     * 审核状态(1通过2不通过通过3待审核)
     */
    private Integer auditStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 审核人
     */
    private String auditStaffName;

    /**
     * 是否竞拍成功(1进行中、统计中2未成功 3已成功)
     */
    private Integer isSuccess;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;


}
