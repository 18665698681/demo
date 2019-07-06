package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductAuctionUserDao extends BaseMapper<ProductAuctionUser> {


    @Update("update  product_auction_user set isSuccess=3 where productId=#{productId}")
    int updateByFail(@Param("productId") Long productId);

    @Update("<script>"
            + "UPDATE "
            + "product_auction_user set isSuccess=2 "
            + "where  productId = #{productId} and userId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    int updateBySuccess(@Param("productId") Long productId,@Param("list") Set<Long> userIds);

}
