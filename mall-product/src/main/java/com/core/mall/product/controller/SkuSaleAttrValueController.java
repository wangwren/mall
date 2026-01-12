package com.core.mall.product.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.product.dto.SkuSaleAttrValueDTO;
import com.core.mall.product.service.SkuSaleAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * sku销售属性&值
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
public class SkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @GetMapping("page")
    public Result<PageData<SkuSaleAttrValueDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SkuSaleAttrValueDTO> page = skuSaleAttrValueService.page(params);

        return new Result<PageData<SkuSaleAttrValueDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<SkuSaleAttrValueDTO> get(@PathVariable("id") Long id){
        SkuSaleAttrValueDTO data = skuSaleAttrValueService.get(id);

        return new Result<SkuSaleAttrValueDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody SkuSaleAttrValueDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        skuSaleAttrValueService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody SkuSaleAttrValueDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        skuSaleAttrValueService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        skuSaleAttrValueService.delete(ids);

        return new Result();
    }


}
