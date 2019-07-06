package com.dtlonline.shop.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Deveik
 * @date 2019/01/15
 */
public class PropertyEditorConverter extends PropertyEditorSupport {
    private Logger logger = LoggerFactory.getLogger(PropertyEditorConverter.class);

    private Class clazz;

    public PropertyEditorConverter(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @param text 前端传入的字符串
     */
    @Override
    public void setAsText(String text) {
        try {
            // TODO: 简单的实现 - 其实这块可以考虑代理的方法来做
            Method method = Class.forName(this.clazz.getCanonicalName())
                    .getMethod("convert", String.class);
            setValue(method.invoke(null, text));
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Throwable targetEx = ((InvocationTargetException) e).getTargetException();
                if (targetEx != null) {
                    logger.error("{} 枚举转换异常 : \t传入值: {}\t详细异常: {}", clazz.getName(), text, targetEx.getMessage());
                }
            } else {
                logger.error("{} 枚举转换异常 : \t传入值: {}\t详细异常: {}", clazz.getName(), text, e.toString());
            }
        }
    }

    @Override
    public String getAsText() {
        return super.getAsText();
    }

}