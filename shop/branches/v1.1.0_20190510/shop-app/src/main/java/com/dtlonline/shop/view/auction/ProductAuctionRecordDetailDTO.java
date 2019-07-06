package com.dtlonline.shop.view.auction;

import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.view.ProductRecordDetailDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuctionRecordDetailDTO {

    private String txnId;

    private BigDecimal minAddPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private BigDecimal buyerEarnestMoney;

    private BigDecimal sellerEarnestMoney;

    private Integer earnestMoneyStatus;

    private String deliveryTime;

    private Integer deliveryType;

    private LocalDateTime createTime;

    private ProductRecordDetailDTO productDetail;

    public static final ProductAuctionRecordDetailDTO of(ProductAuction productAuction) {
        ProductAuctionRecordDetailDTO productAuctionRecordDetailDTO = new ProductAuctionRecordDetailDTO();
        Optional.ofNullable(productAuction).ifPresent(auction -> BeanUtils.copyProperties(productAuction, productAuctionRecordDetailDTO));
        return productAuctionRecordDetailDTO;
    }
}
