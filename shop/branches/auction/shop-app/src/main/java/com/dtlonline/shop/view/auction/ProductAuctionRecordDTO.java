package com.dtlonline.shop.view.auction;

import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
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

    private Date createTime;

    public static ProductAuctionRecordDTO of(ProductAuctionRecord record) {
        ProductAuctionRecordDTO dto = new ProductAuctionRecordDTO();
        Optional.ofNullable(record).ifPresent(record2 -> {
            BeanUtils.copyProperties(record2, dto);
        });
        return dto;
    }
}
