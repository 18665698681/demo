package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper
public interface ProductAuctionRecordDao extends BaseMapper<ProductAuctionRecord> {

    @Select("select * from product_auction_record  where productId=#{productId} ORDER BY price DESC , quantity DESC , createTime ASC LIMIT 3")
    List<ProductAuctionRecord> selectLastRecordsForUp(@Param("productId") Long productId);

    @Insert("<script>"
            + "insert into product_auction_record "
            + "(txnId,productId,userId,quantity,price,buildOrder) "
            + "SELECT #{record.txnId} ,#{record.productId} ,#{record.userId} ,#{record.quantity} ,#{record.price} ,#{record.buildOrder} "
            + "FROM DUAL WHERE NOT EXISTS  "
            + "(SELECT price FROM product_auction_record WHERE price &gt;= #{record.price} and productId=#{record.productId}) "
            + "</script>")
    int insertRecord(@Param("record") ProductAuctionRecord record);

    @Select("select * from product_auction_record  where productId=#{productId} ORDER BY createTime DESC LIMIT 3")
    List<ProductAuctionRecord> selectLastRecordsForDown(@Param("productId") Long productId);

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
            + "MAX(id) id "
            + "FROM product_auction_record "
            + "GROUP BY productId,userId HAVING  userId=#{userId} and productId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    Set<Long> selectDownUserPriceByProductId(@Param("list") Set<Long> productIds, @Param("userId") Long userId);

    @Select("select * from product_auction_record  where productId = #{productId} ORDER BY price desc LIMIT 1")
    ProductAuctionRecord selectUserNewPrice(@Param("productId") Long productId);

    @Select("<script>"
            + "SELECT "
            + "MAX(a.id) as id  FROM "
            + "(SELECT * FROM product_auction_record WHERE buildOrder=1 and  productId = #{productId} ) a "
            + "GROUP BY a.userId "
            + "</script>")
    Set<Long> selectUpNewUserRecordList(@Param("productId")Long productId);

    @Select("<script>"
            + "SELECT "
            + "* "
            + "FROM product_auction_record "
            + "where  price &gt;= #{unitPrice}  and id in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + " ORDER BY price DESC,quantity DESC,createTime ASC; "
            + "</script>")
    List<ProductAuctionRecord> selectUpSuccessList(@Param("list") Set<Long> ids, @Param("unitPrice")BigDecimal unitPrice);
}
