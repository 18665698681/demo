package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.command.store.StoreRepDTO;
import io.alpha.app.core.base.BaseObject;
import io.alpha.security.aes.AESUtil;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Mybatis Generator 2019/04/16
 */
@Data
@TableName(value = "store")
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Store extends BaseObject {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 仓库代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 仓库类型
     */
    @TableField(value = "type")
    private Byte type;

    /**
     * 认证状态（1：有效，2：失效）
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 仓库负责人
     */
    @TableField(value = "contactName")
    private String contactName;

    /**
     * 负责人电话
     */
    @TableField(value = "contactMobile")
    private String contactMobile;

    /**
     * 仓库地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 仓库范围
     */
    @TableField(value = "zone")
    private String zone;

    /**
     * 扩展数据{"business":"123,456","approval":"123,456","land":"123,456","file":"123,456"}
     */
    @TableField(value = "extendData")
    private String extendData;

    /**
     * 创建人
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "lastModifyTime")
    private Date lastModifyTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_TXNID = "txnId";

    public static final String COL_NAME = "name";

    public static final String COL_CODE = "code";

    public static final String COL_TYPE = "type";

    public static final String COL_STATUS = "status";

    public static final String COL_CONTACTNAME = "contactName";

    public static final String COL_CONTACTMOBILE = "contactMobile";

    public static final String COL_ADDRESS = "address";

    public static final String COL_ZONE = "zone";

    public static final String COL_EXTENDDATA = "extendData";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";


    public static final StoreRepDTO ofStoreAuthRecord(Store store) {
        if (store == null) {
            return null;
        }
        StoreRepDTO dto = new StoreRepDTO();
        BeanUtils.copyProperties(store, dto);
        dto.setAddress(AESUtil.decrypt(dto.getAddress()));
        return dto;
    }
}