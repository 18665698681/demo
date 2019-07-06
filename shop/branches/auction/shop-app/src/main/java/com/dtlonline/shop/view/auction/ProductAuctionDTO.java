package com.dtlonline.shop.view.auction;

import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.shop.constant.auction.AuctionProcessStatus;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.view.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuctionDTO{
    private String txnId;
    private Long productId;
    private String title;
    private BigDecimal minAddPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    private BigDecimal sellerEarnestMoney;
    private BigDecimal buyerEarnestMoney;
    private Integer earnestMoneyStatus;
    private String deliveryTime;
    private String deliveryType;
    private String descriptions;
    private Long userId;
    private Date createTime;

    private Integer auditStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime currentTime = LocalDateTime.now();

    private String categoryName;
    /**平台竞拍列表状态 1未开始 2竞拍中 3已结束*/
    private Integer processStatus;

    private ProductInfoDTO product;

    /** 当前最高价 */
    private BigDecimal currenMaxPrice;

    public static ProductAuctionDTO of(ProductAuction auction, ProductInfoDTO product, Map<Long,CategoryDTO> categorys) {
        ProductAuctionDTO dto = new ProductAuctionDTO();
        BeanUtils.copyProperties(auction, dto);
        Optional.ofNullable(categorys).filter(c -> c.get(product.getCategoryId())!=null).ifPresent(c->{
            dto.setCategoryName(c.get(product.getCategoryId()).getTitle());
        });
        dto.setProduct(dto.getProduct());
        LocalDateTime date  = LocalDateTime.now();
        if(date.isBefore(auction.getBeginTime())){
            dto.setProcessStatus(AuctionProcessStatus.BEFORE.getCode());
        }else if (date.isAfter(auction.getBeginTime()) && date.isBefore(auction.getEndTime())){
            dto.setProcessStatus(AuctionProcessStatus.GOINGON.getCode());
        }else if(date.isAfter(auction.getEndTime())){
            dto.setProcessStatus(AuctionProcessStatus.OVER.getCode());
        }
        dto.setProduct(product);
        return dto;
    }



}
