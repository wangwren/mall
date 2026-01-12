package com.core.mall.product;

import com.core.mall.product.dto.BrandDTO;
import com.core.mall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MallProductApplicationTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void testAdd() {

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("特斯拉");
        brandService.save(brandDTO);
    }

    @Test
    public void testList() {

        BrandDTO brandDTO = brandService.get(1L);
        System.out.println(brandDTO);
    }

}
