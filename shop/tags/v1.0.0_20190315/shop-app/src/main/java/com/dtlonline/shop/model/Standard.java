package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@TableName("standard")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class Standard extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;
    /**
     * 规格名称
     */
    private String title;
    /**
     * 规格输入方式
     */
    private Integer type;
    /**
     * 输入数据
     */
    private String data;
    /**
     * 是否必填
     */
    private Integer required;
    /**
     * 品类ID
     */
    private Long categoryId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastModifyTime;
}
