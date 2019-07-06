package com.dtlonline.shop.utilities;

import com.google.gson.Gson;
import io.alpha.core.view.RestResult;

/**
 * @author Deveik
 * @date 2019/01/16
 */
public class GetJsonFromClass {
    private static Gson gson = new Gson();

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static RestResult restResultFromJson(String content){
        return gson.fromJson(content, RestResult.class);
    }
}
