package com.dtlonline.shop.view.auction;

import com.dtlonline.shop.constant.auction.AuctionProcessStatus;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.view.ProductRecordListDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuctionRecordListDTO {

    private String txnId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private String processStatus;

    private ProductRecordListDTO product;

    public static final ProductAuctionRecordListDTO of(ProductAuction productAuction) {
        ProductAuctionRecordListDTO AuctionRecordListDTO = new ProductAuctionRecordListDTO();
        Optional.ofNullable(productAuction).ifPresent(auction -> BeanUtils.copyProperties(auction, AuctionRecordListDTO));
        LocalDateTime date = LocalDateTime.now();
        if (date.isBefore(productAuction.getBeginTime())) {
            AuctionRecordListDTO.setProcessStatus(AuctionProcessStatus.BEFORE.getMessage());
        } else if (date.isAfter(productAuction.getBeginTime()) && date.isBefore(productAuction.getEndTime())) {
            AuctionRecordListDTO.setProcessStatus(AuctionProcessStatus.GOINGON.getMessage());
        } else if (date.isAfter(productAuction.getEndTime())) {
            AuctionRecordListDTO.setProcessStatus(AuctionProcessStatus.OVER.getMessage());
        }
        return AuctionRecordListDTO;
    }
}
