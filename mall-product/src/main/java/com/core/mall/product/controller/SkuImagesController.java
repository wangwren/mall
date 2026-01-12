package com.core.mall.product.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.product.dto.SkuImagesDTO;
import com.core.mall.product.service.SkuImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * sku图片
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@RestController
@RequestMapping("product/skuimages")
public class SkuImagesController {
    @Autowired
    private SkuImagesService skuImagesService;

    @GetMapping("page")
    public Result<PageData<SkuImagesDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SkuImagesDTO> page = skuImagesService.page(params);

        return new Result<PageData<SkuImagesDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<SkuImagesDTO> get(@PathVariable("id") Long id){
        SkuImagesDTO data = skuImagesService.get(id);

        return new Result<SkuImagesDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody SkuImagesDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        skuImagesService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody SkuImagesDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        skuImagesService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        skuImagesService.delete(ids);

        return new Result();
    }


}
