package com.dtlonline.shop.model.auction;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("product_auction_record")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductAuctionRecord extends BaseObject {

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
     * 数量
     */
    private Integer quantity;

    /**
     * 出价
     */
    private BigDecimal price;

    /**
     * 竞拍结束生成订单(1未生成2已生成)
     */
    private Integer buildOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

}
