package com.dtlonline.shop.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShopAuthRecordPageInfoDTO {

    private Long id;

    private String name;

    private Integer type;

    private String area;

    private String address;

    private String contactName;

    private Integer status;

    private Date createTime;
}
