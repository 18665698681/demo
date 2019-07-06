package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.Attention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AttentionDao extends BaseMapper<Attention> {

    @Update("UPDATE attention SET  `status` = -1 WHERE userId = #{userId}")
    Integer updateAttention(@Param("userId") Long userId);
}
