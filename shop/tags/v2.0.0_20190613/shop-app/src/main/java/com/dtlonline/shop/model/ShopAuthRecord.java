package com.dtlonline.shop.model;

import com.dtlonline.api.shop.command.ShopAuditDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShopAuthRecord {
    @JsonProperty("shopId")
    private Long id;

    private String txnId;

    private Long userId;

    private String name;

    private Integer type;

    private String area;

    private String address;

    private String contactName;

    private String mobile;

    private String staffName;

    private Integer status;

    private String opinion;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;

    public static ShopAuthRecord of(ShopAuditDTO shopAuditDTO) {
        ShopAuthRecord shopAuthRecord = new ShopAuthRecord();
        BeanUtils.copyProperties(shopAuditDTO, shopAuthRecord);
        return shopAuthRecord;
    }
}
