package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.api.shop.command.StandardParamDTO;
import com.dtlonline.shop.model.Standard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardDao extends BaseMapper<Standard> {

    @Update("UPDATE standard SET " +
            "title = #{s.title}," +
            "`type` = #{s.type}," +
            "`data` = #{s.data}," +
            "`status` = #{s.status}," +
            " required = #{s.required}" +
            " WHERE id = #{s.standardId} ")
    Integer updateStandard(@Param("s") StandardParamDTO standardParamDTO);
}
