package com.dtlonline.shop.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.alpha.app.core.util.ObjectUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class NumberUtils {


    /**
     * 转换成对应格式的Page对象
     */
    public static Integer sum(Integer... numbers) {
        if (ArrayUtils.isEmpty(numbers)) {
            return 0;
        }
        Integer sum = 0;
        for (Integer number : numbers) {
            sum += number==null?0:number.intValue();
        }
        return sum;
    }
}