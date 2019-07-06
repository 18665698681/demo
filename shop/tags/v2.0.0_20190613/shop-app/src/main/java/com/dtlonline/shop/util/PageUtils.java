package com.dtlonline.shop.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.shop.model.store.StoreGoods;
import io.alpha.app.core.util.ObjectUtils;

import java.util.List;

public class PageUtils {


    /**
     * 转换成对应格式的Page对象
     */
    public static <T,R> Page<R> transferRecords(IPage<T> pageList, Class<R> transferClass) {
        Page<R> newPage = new Page<>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal());
        List<R> copyRecords = ObjectUtils.copy(pageList.getRecords(), transferClass);
        newPage.setRecords(copyRecords);
        return newPage;
    }
}