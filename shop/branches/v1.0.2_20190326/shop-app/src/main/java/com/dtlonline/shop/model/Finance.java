package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@TableName("finance")
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Finance extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 1-金融 2-借换
     */
    private Integer type;
    /**
     * 其他需求
     */
    private String other;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    private LocalDateTime lastModifyTime;
}
