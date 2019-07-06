package com.dtlonline.shop.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Deveik
 * @date 2019/01/17
 */
@Data
public class ShopPageInfoDTO {
    @JsonProperty("shopId")
    private Long id;
    private String name;
    private Integer type;
    private String area;
    private String contactName;
    private String mobile;
    private Integer status;
    private Date createTime;
    @JsonIgnore
    private Date lastModifyTime;
}
