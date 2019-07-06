package com.dtlonline.shop.utilities;

import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.service.CategoryService;
import com.dtlonline.shop.view.CategoryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void categoryTest(){
        Set<Long> longSet = new HashSet<>();
        longSet.add(13L);
        longSet.add(14L);
        Set<Long> longSet1 = categoryService.queryChildCategory(longSet);
        System.out.println(longSet1);
    }
}
