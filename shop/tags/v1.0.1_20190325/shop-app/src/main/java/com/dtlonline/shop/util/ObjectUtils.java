package com.dtlonline.shop.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ObjectUtils {

    public static <T, R> R transfer(T source, Class<R> targetClass) {
        R target = createObject(targetClass);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T, R> List<R> transfer(List<T> sourceList, Class<R> targetClass) {
        List<R> targetList = new ArrayList<R>();
        for (T source : sourceList) {
            targetList.add(transfer(source, targetClass));
        }
        return targetList;
    }

    private static <R> R createObject(Class<R> targetClass) {
        R target;
        try {
            target = targetClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("类型反射创建不成功");
        }
        return target;
    }

}
