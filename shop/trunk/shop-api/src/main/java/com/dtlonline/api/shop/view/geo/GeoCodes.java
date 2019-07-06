package com.dtlonline.api.shop.view.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class GeoCodes {

    /**
     * 结构化地址信息
     */
    private String formatted_address;
    /**
     * 城市编码
     */
    private String country;

    /**
     * 地址所在的省份名
     */
    private String province;
    /**
     * 地址所在的城市名
     */
    private String city;
    /**
     * 坐标点 经度，纬度
     */
    private String location;
}
