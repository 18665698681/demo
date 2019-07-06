package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper
public interface ProductAuctionRecordDao extends BaseMapper<ProductAuctionRecord> {

    @Select("select * from product_auction_record  where productId=#{productId} ORDER BY price DESC , quantity DESC , createTime ASC LIMIT 3")
    List<ProductAuctionRecord> selectLastRecordsForUp(@Param("productId") Long productId);

    @Select("select * from product_auction_record  where productId=#{productId} ORDER BY createTime DESC LIMIT 3")
    List<ProductAuctionRecord> selectLastRecordsForDown(@Param("productId") Long productId);

    @Select("select * from product_auction_record  where productId=#{productId} and userId=#{userId} ORDER BY id desc LIMIT 1")
    ProductAuctionRecord selectLastOneRecord(@Param("productId") Long productId, @Param("userId") Long userId);

    @Select("select IFNULL(sum(quantity),0) from product_auction_record where buildOrder=1 and productId=#{productId} and price>=#{startPrice}")
    int selectUpSumQtyByProductId(@Param("productId") Long productId, @Param("startPrice")BigDecimal startPrice);

    @Update("update  product_auction_record set buildOrder=2 where  buildOrder=1 and id=#{id}")
    int updateByCreateOrder(@Param("id") Long id);

    @Update("update  product_auction_record set buildOrder=3 where  buildOrder=1 and id=#{id}")
    int updateByErrorOrder(@Param("id") Long id);

    @Select("<script>"
            + "SELECT "
            + "productId,MAX(price) price "
            + "FROM product_auction_record "
            + "GROUP BY productId HAVING productId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    List<ProductAuctionRecord> selectMaxPriceByProductId(@Param("list") Set<Long> productId);

    @Select("<script>"
            + "SELECT "
            + "productId,MAX(price) price "
            + "FROM product_auction_record "
            + "GROUP BY productId,userId HAVING  userId=#{userId} and productId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    List<ProductAuctionRecord> selectUpUserPriceByProductId(@Param("list") Set<Long> productIds, @Param("userId") Long userId);


    @Select("<script>"
            + "SELECT "
            + "MAX(id) id "
            + "FROM product_auction_record "
            + "GROUP BY productId,userId HAVING  userId=#{userId} and productId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    Set<Long> selectDownUserPriceByProductId(@Param("list") Set<Long> productIds, @Param("userId") Long userId);


    @Select("select * from product_auction_record  where userId=#{userId} and productId = #{productId} ORDER BY price desc LIMIT 1")
    ProductAuctionRecord selectUserMaxPrice(@Param("userId") Long userId, @Param("productId") Long productId);

    @Select("<script>"
            + "SELECT "
            + "MAX(a.id) as id  FROM "
            + "(SELECT * FROM product_auction_record WHERE buildOrder=1 and  productId = #{productId} ) a "
            + "GROUP BY a.userId "
            + "</script>")
    Set<Long> selectDownNewUserRecordList(@Param("productId")Long productId);

    @Select("<script>"
            + "SELECT "
            + "* "
            + "FROM product_auction_record "
            + "where  price &gt;= #{floorPrice}  and price &lt;= #{unitPrice} and id in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + " ORDER BY price DESC,quantity DESC,createTime ASC; "
            + "</script>")
    List<ProductAuctionRecord> selectDownSuccessList(@Param("list") Set<Long> ids, @Param("floorPrice")BigDecimal floorPrice, @Param("unitPrice")BigDecimal unitPrice);
}
