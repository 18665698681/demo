package com.dtlonline.shop.view.auction;

import com.dtlonline.shop.constant.auction.AuctionProcessStatus;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.view.ProductDetailDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class
ProductAuctionDetailDTO {
    private String txnId;
    private Long productId;
    private String title;
    private BigDecimal minAddPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    private Integer hours;
    private BigDecimal sellerEarnestMoney;
    private BigDecimal buyerEarnestMoney;
    private Integer earnestMoneyStatus;
    private String deliveryTime;
    private Integer deliveryType;
    private String descriptions;
    private Long userId;
    private Date createTime;
    private Integer auditStatus;

    /**
     * 当前系统时间
     **/

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime currentTime = LocalDateTime.now();

    /**
     * 平台竞拍列表状态 1未开始 2竞拍中 3已结束
     */
    private Integer processStatus;

    /**
     * 当前最高价
     */
    private BigDecimal currenMaxPrice;

    /**
     * 用户参与竞拍审核
     * */
    private Integer userAuditStatus;

    /**
     * 用户是否参与  1已申请  2未申请
     */
    private Integer isApply;

    /**
     * 用户当前最新出价
     */
    private BigDecimal currentPrice;

    /**
     * 商品信息
     **/
    private ProductDetailDTO product;

    /**
     * 出价记录
     */
    private List<ProductAuctionRecordDTO> recordDtos;

    public static ProductAuctionDetailDTO of(ProductAuction auction) {
        ProductAuctionDetailDTO dto = new ProductAuctionDetailDTO();
        BeanUtils.copyProperties(auction, dto);
        LocalDateTime date = LocalDateTime.now();
        if (date.isBefore(auction.getBeginTime())) {
            dto.setProcessStatus(AuctionProcessStatus.BEFORE.getCode());
        } else if (date.isAfter(auction.getBeginTime()) && date.isBefore(auction.getEndTime())) {
            dto.setProcessStatus(AuctionProcessStatus.GOINGON.getCode());
        } else if (date.isAfter(auction.getEndTime())) {
            dto.setProcessStatus(AuctionProcessStatus.OVER.getCode());
        }
        Duration duration = java.time.Duration.between(auction.getBeginTime(), auction.getEndTime());
        dto.setHours((int) duration.toHours());
        return dto;
    }

}
