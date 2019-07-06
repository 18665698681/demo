package com.dtlonline.shop.view.auction;

import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.shop.model.auction.ProductAuction;
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
public class ProductAuctionUserDTO extends ProductAuctionDTO{
    /** 当前用户最高出价 */
    private BigDecimal userPrice;
    /** 竞拍结果 1结算中  2已出局 3竞拍成功*/
    private Integer userAuctionResult;

    public static ProductAuctionUserDTO of(ProductInfoDTO product, ProductAuction auctions, BigDecimal currenMaxPrice, BigDecimal userPrice, Integer userAuctionResult, Map<Long, CategoryDTO> categorys) {
        ProductAuctionDTO dto = ProductAuctionDTO.of(auctions,product,categorys);
        dto.setCurrenMaxPrice(currenMaxPrice);
        ProductAuctionUserDTO userDTO = new ProductAuctionUserDTO();
        BeanUtils.copyProperties(dto,userDTO);
        userDTO.setUserPrice(userPrice);
        userDTO.setUserAuctionResult(userAuctionResult);
        return userDTO;
    }
}
