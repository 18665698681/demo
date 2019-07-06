package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.shop.model.auction.ProductAuction;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProductAuctionDao extends BaseMapper<ProductAuction> {

    @Select("select * from product_auction  where auditStatus=1 and endTime < sysdate()")
    List<ProductAuction> selectByOver();

    @Update("update  product_auction set buildOrder=2 where id=#{id}")
    int updateByCreateOrder(Long id);

    @Update("<script>" +
            " UPDATE product_auction" +
            "<set>" +
            " auditStatus = #{c.status},auditUser = #{auditUser}," +
            " auditOpinion = #{c.opinion}," +
            "<if test='productId != null'>" +
            " productId = #{productId}" +
            "</if> " +
            "</set>" +
            "WHERE txnId = #{txnId} AND auditStatus = 3 " +
            "</script>")
    Integer updateAuctionByStatus(@Param("auditUser") String auditUser, @Param("txnId") String txnId, @Param("productId") Long productId, @Param("c")CheckInfoDTO checkInfoDTO);
}
