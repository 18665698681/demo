package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.view.CategoryDTO;
import com.google.common.collect.Sets;
import io.alpha.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService extends BaseService {

    @Autowired
    public CategoryDao categoryDao;

    // TODO: 2019/3/15 复制对象可换成 List<CategoryDTO> categorys = ObjectUtils.transfer(categoryList,CategoryDTO.class);
    public List<CategoryDTO> queryCategoryForList(){
        List<Category> categoryList = categoryDao.queryCategoryList();
        List<CategoryDTO> categorys =new ArrayList<>(categoryList.size());
        categoryList.forEach(category -> categorys.add(Category.of(category)));
        return categorys;
    }

    // TODO: 2019/3/15 复制对象可换成 List<CategoryDTO> categorys = ObjectUtils.transfer(categoryList,CategoryDTO.class);
    // TODO: 2019/3/15 魔法运算需要抽离出变量，取个有意义的名称
    // TODO: 2019/3/15 如： isErrorType ? 1 : type
    // TODO: 2019/3/15 考虑采用下方重构方式
    public List<CategoryDTO> queryCategoryList(Integer type){
        //非父级品类集合
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("parentId",0);
        queryWrapper.eq("active",SUCCESS);
        queryWrapper.eq("status",SUCCESS);
        queryWrapper.eq("type", TypeEnum.PRODUCT_PURCHASE.getValue().equals(type) || TypeEnum.PRODUCT_SPOT.getValue().equals(type) ? TypeEnum.PRODUCT_SPOT.getValue() : type);
        queryWrapper.orderByAsc("`index`");
        List<Category> categorys = categoryDao.selectList(queryWrapper);
        //所以父级品类集合
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parentId",0);
        wrapper.eq("active",SUCCESS);
        wrapper.eq("status",SUCCESS);
        wrapper.eq("type",TypeEnum.PRODUCT_PURCHASE.getValue().equals(type) || TypeEnum.PRODUCT_SPOT.getValue().equals(type) ? TypeEnum.PRODUCT_SPOT.getValue() : type);
        wrapper.orderByAsc("`index`");
        List<Category> topList = categoryDao.selectList(wrapper);
        List<CategoryDTO> categoryList = new ArrayList<>(categorys.size());
        categorys.forEach(addr->categoryList.add(Category.of(addr)));
        List<CategoryDTO> topLists = new ArrayList<>(topList.size());
        topList.forEach(addr->topLists.add(Category.of(addr)));
        //循环顶级类别插入子品类
        Set<Long> set = Sets.newHashSetWithExpectedSize(categoryList.size());
        topLists.forEach(category -> queryChild(category,categoryList,set));
        return topLists;
    }

    // TODO: 2019/3/15 可利用map重构
    public void queryChild(CategoryDTO category, List<CategoryDTO> categoryList,Set<Long> set){
        List<CategoryDTO> childList = new ArrayList<>();
        categoryList.stream().filter(c ->!set.contains(c.getId()))
                .filter(c->c.getParentId().equals(category.getId()))
                .filter(c->set.size() <= categoryList.size())
                .forEach(c->{
                    set.add(c.getId());
                    queryChild(c,categoryList,set);
                    childList.add(c);
                });
        category.setChildCategory(childList);
    }

    // TODO: 2019/3/15 queryCategoryList方法重构示例
//    public List<CategoryDTO> queryCategoryListSuggest(Integer type) {
//
//        //查询所有类别，转成dto，下方都用dto做处理，不涉及model
//        int correctType = 99 == type ? 1 : type;
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("type", correctType);
//        queryWrapper.eq("active", SUCCESS);
//        queryWrapper.eq("status", SUCCESS);
//        queryWrapper.orderByAsc("`index`");
//        List<Category> categorys = categoryDao.selectList(queryWrapper);
//        List<CategoryDTO> categoryDtos = ObjectUtils.transfer(categorys, CategoryDTO.class);
//
//        //做一个parentId到类别的映射，再遍历类别用自己的Id取出子类别，对象之间的引用会自动分层嵌套起来。
//        // 好处是只循环两次、不用考虑递归、层级。
//        ArrayListMultimap<Long, CategoryDTO> parentIdToSubCategoryDtos = ArrayListMultimap.create();
//        categoryDtos.forEach(categoryDTO->parentIdToSubCategoryDtos.put(categoryDTO.getParentId(),categoryDTO));
//        categoryDtos.forEach(categoryDTO->categoryDTO.setChildCategory(parentIdToSubCategoryDtos.get(categoryDTO.getId())));
//
//        return categoryDtos;
//    }

    public CategoryDTO queryCategorys(Long categoryId){
        Category category = categoryDao.queryCategorys(categoryId);
        CategoryDTO categoryDTO = Category.of(category);
        return categoryDTO;
    }
}
