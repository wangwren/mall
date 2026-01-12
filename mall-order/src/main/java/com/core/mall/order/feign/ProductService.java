package com.core.mall.order.feign;

import com.core.mall.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "mall-product")
public interface ProductService {

    @GetMapping("product/brand/all")
    Result queryAllBrand();
}
