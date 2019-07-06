package com.dtlonline.shop.view.auction;

import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import com.dtlonline.shop.view.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAuctionUserDTO extends ProductAuctionDTO {
    /**
     * 当前用户最高出价
     */
    private BigDecimal userPrice;
    /**
     * 竞拍结果 1结算中  2已出局 3竞拍成功
     */
    private Integer userAuctionResult;

    /**
     * 用户申请记录审核
     */
    private Integer userAuditStatus;

    /**
     * 编号
     */
    private String auctionUserNo;

    public static ProductAuctionUserDTO of(ProductInfoDTO product, ProductAuction auctions, ProductAuctionUser user, BigDecimal currenMaxPrice, BigDecimal userPrice, Map<Long, CategoryDTO> categorys) {
        ProductAuctionDTO dto = ProductAuctionDTO.of(auctions, product, categorys);
        dto.setCurrenMaxPrice(currenMaxPrice == null ? product.getUnitPrice() : currenMaxPrice);
        ProductAuctionUserDTO userDTO = new ProductAuctionUserDTO();
        BeanUtils.copyProperties(dto, userDTO);
        userDTO.setUserPrice(userPrice);
        userDTO.setUserAuctionResult(user.getIsSuccess());
        userDTO.setUserAuditStatus(user.getAuditStatus());
        userDTO.setAuctionUserNo(user.getAuctionUserNo());
        return userDTO;
    }
}
