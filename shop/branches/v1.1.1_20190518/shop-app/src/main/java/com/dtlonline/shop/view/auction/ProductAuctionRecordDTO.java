package com.dtlonline.shop.view.auction;

import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuctionRecordDTO {

    private Long productId;

    private Long userId;

    private String account;

    private Integer quantity;

    private BigDecimal price;

    private String auctionUserNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public static ProductAuctionRecordDTO of(ProductAuctionRecord record, ProductAuctionUser auctionUser) {
        ProductAuctionRecordDTO dto = new ProductAuctionRecordDTO();
        Optional.ofNullable(record).ifPresent(record2 -> {
            BeanUtils.copyProperties(record2, dto);
        });
        dto.setAuctionUserNo(auctionUser.getAuctionUserNo());
        return dto;
    }
}
