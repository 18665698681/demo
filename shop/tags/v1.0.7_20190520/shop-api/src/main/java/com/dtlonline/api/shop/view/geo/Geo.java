package com.dtlonline.api.shop.view.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

    /**
     * 返回结果状态值 返回值为 0 或 1，0 表示请求失败；1 表示请求成功。
     */
    private String status;
    /**
     * 返回状态说明
     */
    private String info;
    /**
     * 返回结果数目
     */
    private String count;
    /**
     * 地理编码信息列表
     */
    private List<GeoCodes> geocodes;
}
