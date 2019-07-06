package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by Mybatis Generator 2019/04/16
 */
@Data
@TableName(value = "store_auth_record")
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreAuthRecord extends BaseObject {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * txnId
     */
    @TableField(value = "txnId")
    private String txnId;

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
     * 仓库类型 1自营仓库，2合作仓库，3社会仓库，4其他仓库
     */
    @TableField(value = "type")
    private Byte type;

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
     * 审核人
     */
    @TableField(value = "auditUser")
    private String auditUser;

    /**
     * 审核意见
     */
    @TableField(value = "auditOpinion")
    private String auditOpinion;

    /**
     * 审核状态（1：通过，2：未通过，3：待审核）
     */
    @TableField(value = "auditStatus")
    private Byte auditStatus;

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

    public static final String COL_CONTACTNAME = "contactName";

    public static final String COL_CONTACTMOBILE = "contactMobile";

    public static final String COL_ADDRESS = "address";

    public static final String COL_ZONE = "zone";

    public static final String COL_EXTENDDATA = "extendData";

    public static final String COL_AUDITUSER = "auditUser";

    public static final String COL_AUDITOPINION = "auditOpinion";

    public static final String COL_AUDITSTATUS = "auditStatus";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";

}