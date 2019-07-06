package com.dtlonline.api.shop.view.groupbuy;

import com.dtlonline.api.shop.constant.groupbuy.BalanceType;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyPublish;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyStatus;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductGroupbuyResponseDTO extends BaseObject {

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 业务ID
     */
    private String txnId;

    /**
     * 团购类型：1 - 协议，2 - 意向
     */
    private Integer type;

    /**
     * 团购类型中文描述：1 - 协议，2 - 意向
     */
    private String typeZh;

    /**
     * 团购标题
     */
    private String title;

    /**
     * 品种名称
     */
    private String categoryTitle;

    /**
     * 结算方式
     */
    private Integer balanceType;

    /**
     * 结算方式中文描述
     */
    private String balanceTypeZh;

    /**
     * 报名截止时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate buyEndDate;

    /**
     * 活动截止时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate activityEndDate;

    /**
     * 是否已团购公示： 0 - 未公示，1 已公示
     **/
    private Integer isPublished;

    /**
     * 是否已团购公示中文描述
     **/
    private String isPublishedZh;

    /**
     * 展示图片列表
     */
    private List<String> imageUrls;

    /**
     * 简介
     */
    private String description;

    /**
     * 是否置顶：1 - 是，0 - 否
     **/
    private Integer isTop;

    /**
     * 状态： -1 - 已删除 ，1 - 待审核，2 - 审核通过，3 - 审核未通过
     **/
    private Integer status;

    /**
     * 状态中文描述
     **/
    private String statusZh;

    /**
     * 团购优惠比例
     **/
    private List<String> discounts;

    /**
     * 团购活动规则列表
     **/
    private List<ProductGroupbuyStandardResponseDTO> standardDTOs;

    public void setType(Integer type) {

        this.type = type;
        if (type != null) {
            this.typeZh = ProductGroupbuyType.getMessageByCode(type);
        }
    }

    public void setStatus(Integer status) {

        this.status = status;
        if (status != null) {
            this.statusZh = ProductGroupbuyStatus.getMessageByCode(status);
        }
    }

    public void setIsPublished(Integer isPublished) {

        this.isPublished = isPublished;
        if (isPublished != null) {
            this.isPublishedZh = ProductGroupbuyPublish.getMessageByCode(isPublished);
        }
    }

    public void setBalanceType(Integer balanceType) {

        this.balanceType = balanceType;
        if (balanceType != null) {
            this.balanceTypeZh = BalanceType.getMessageByCode(balanceType);
        }
    }

}
