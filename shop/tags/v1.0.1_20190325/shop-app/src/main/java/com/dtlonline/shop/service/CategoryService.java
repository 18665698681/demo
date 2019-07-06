package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.CategoryPackDTO;
import com.dtlonline.api.shop.command.CategoryParamDTO;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.StandardDao;
import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.model.Standard;
import com.dtlonline.shop.util.ObjectUtils;
import com.dtlonline.shop.view.CategoryDTO;
import com.dtlonline.shop.view.StandardDTO;
import com.dtlonline.shop.view.ViewCode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import io.alpha.app.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService extends BaseService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private StandardDao standardDao;

    @Autowired
    private StandardService standardService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    public List<CategoryDTO> queryCategoryList(Integer type) {
        //非父级品类集合
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("parentId", 0);
        queryWrapper.eq("active", SUCCESS);
        queryWrapper.eq("status", SUCCESS);
        Integer currentType = TypeEnum.PRODUCT_PURCHASE.getValue().equals(type) || TypeEnum.PRODUCT_SPOT.getValue().equals(type) || TypeEnum.PRODUCT_AUCTION.getValue().equals(type) ? TypeEnum.PRODUCT_SPOT.getValue() : type;
        queryWrapper.eq("type", currentType);
        queryWrapper.orderByAsc("`index`");
        List<Category> categorys = categoryDao.selectList(queryWrapper);
        //所以父级品类集合
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parentId", 0);
        wrapper.eq("active", SUCCESS);
        wrapper.eq("status", SUCCESS);
        wrapper.eq("type", currentType);
        wrapper.orderByAsc("`index`");
        List<Category> topList = categoryDao.selectList(wrapper);
        List<CategoryDTO> categoryList = new ArrayList<>(categorys.size());
        categorys.forEach(addr -> categoryList.add(Category.of(addr)));
        List<CategoryDTO> topLists = new ArrayList<>(topList.size());
        topList.forEach(addr -> topLists.add(Category.of(addr)));
        //循环顶级类别插入子品类
        Set<Long> set = Sets.newHashSetWithExpectedSize(categoryList.size());
        topLists.forEach(category -> queryChild(category, categoryList, set));
        return topLists;
    }

    public void queryChild(CategoryDTO category, List<CategoryDTO> categoryList, Set<Long> set) {
        List<CategoryDTO> childList = new ArrayList<>();
        categoryList.stream().filter(c -> !set.contains(c.getId()))
                .filter(c -> c.getParentId().equals(category.getId()))
                .filter(c -> set.size() <= categoryList.size())
                .forEach(c -> {
                    set.add(c.getId());
                    queryChild(c, categoryList, set);
                    childList.add(c);
                });
        category.setChildCategory(childList);
    }

    public List<CategoryDTO> queryCategoryListSuggest(Integer type) {
        //查询所有类别，转成dto，下方都用dto做处理，不涉及model
        Integer currentType = TypeEnum.PRODUCT_PURCHASE.getValue().equals(type) || TypeEnum.PRODUCT_SPOT.getValue().equals(type) || TypeEnum.PRODUCT_AUCTION.getValue().equals(type) ? TypeEnum.PRODUCT_SPOT.getValue() : type;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", currentType);
        queryWrapper.eq("active", SUCCESS);
        queryWrapper.eq("status", SUCCESS);
        queryWrapper.orderByAsc("`index`");
        List<Category> categorys = categoryDao.selectList(queryWrapper);
        List<CategoryDTO> categoryDtos = ObjectUtils.transfer(categorys, CategoryDTO.class);
        //做一个parentId到类别的映射，再遍历类别用自己的Id取出子类别，对象之间的引用会自动分层嵌套起来。
        // 好处是只循环两次、不用考虑递归、层级。
        ArrayListMultimap<Long, CategoryDTO> parentIdToSubCategoryDtos = ArrayListMultimap.create();
        categoryDtos.forEach(categoryDTO -> parentIdToSubCategoryDtos.put(categoryDTO.getParentId(), categoryDTO));
        categoryDtos.forEach(categoryDTO -> categoryDTO.setChildCategory(parentIdToSubCategoryDtos.get(categoryDTO.getId())));
        List<CategoryDTO> topCategories = categoryDtos.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        return topCategories;
    }

    public CategoryDTO queryCategorys(Long categoryId) {
        Category category = categoryDao.queryCategorys(categoryId);
        CategoryDTO categoryDTO = Category.of(category);
        return categoryDTO;
    }

    public CategoryDTO queryCategoryForId(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", id);
        Category category = categoryDao.selectOne(wrapper);
        CategoryDTO categoryDTO = Category.of(category);
        List<StandardDTO> standardList = standardService.queryStandardForCategoryId(id);
        Optional.ofNullable(categoryDTO.getImgs()).filter(img -> StringUtils.isNotBlank(img)).ifPresent(img ->categoryDTO.setImgs(imageRemoteService.queryUrl(Long.valueOf(img))));
        Optional.ofNullable(standardList).filter(standard -> CollectionUtils.isNotEmpty(standard)).ifPresent(standard ->categoryDTO.setStandardList(standard));
        return categoryDTO;
    }

    public Map<Long, CategoryDTO> queryCategoryMap(Set<Long> categoryIds) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", categoryIds);
        List<Category> categoryDTOList = categoryDao.selectList(wrapper);
        Map<Long, CategoryDTO> categoryDTOMap = categoryDTOList.stream().collect(Collectors.toMap(Category::getId, Category::of));
        return categoryDTOMap;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(CategoryPackDTO categoryPackDTO, Long userId) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryPackDTO, category);
        category.setUserId(userId);
        if (CollectionUtils.isNotEmpty(categoryPackDTO.getImgList())) {
            List<Long> imgs = imageRemoteService.batchSaveImage(categoryPackDTO.getImgList());
            category.setImgs(String.valueOf(imgs.get(0)));
        }
        categoryDao.insert(category);
        if (CollectionUtils.isNotEmpty(categoryPackDTO.getStandardList())){
            categoryPackDTO.getStandardList().stream().forEach(s ->{
                Standard standard = new Standard();
                BeanUtils.copyProperties(s,standard);
                standard.setUserId(userId).setCategoryId(category.getId());
                standardDao.insert(standard);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer updateCategoryForId(Long id, CategoryParamDTO categoryParamDTO,Long userId) {
        Integer reslut = categoryDao.updateCategoryForId(id, categoryParamDTO);
        if (CollectionUtils.isNotEmpty(categoryParamDTO.getStandardList())){
            categoryParamDTO.getStandardList().stream().forEach(s ->{
                if (StringUtils.isNotBlank(s.getTxnId())){
                    Standard standard = new Standard();
                    BeanUtils.copyProperties(s,standard);
                    standard.setUserId(userId).setCategoryId(id);
                    standardDao.insert(standard);
                }else {
                    standardDao.updateStandard(s);
                }
            });
        }
        Optional.ofNullable(reslut).filter(r -> !r.equals(0)).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), "修改品类信息失败"));
        return reslut;
    }

}
