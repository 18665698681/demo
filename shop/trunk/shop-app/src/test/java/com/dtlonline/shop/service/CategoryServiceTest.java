package com.dtlonline.shop.service;

import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.view.CategoryDTO;
import io.alpha.security.aes.AESUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceTest {


    @Autowired
    private CategoryService categoryService;

    @Test
    public void queryCategoryListSuggest() throws Exception {
        List<CategoryDTO> categoryDTOS = categoryService.queryCategoryListSuggest(14L);
        categoryDTOS.stream().forEach(s->{
            s.getChildCategory().stream().forEach(ss->{
                System.out.println("--->>>"+ss.getTitle());
            });
        });
    }
}