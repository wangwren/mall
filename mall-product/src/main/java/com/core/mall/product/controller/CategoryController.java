package com.core.mall.product.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.ConvertUtils;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.product.dto.CategoryDTO;
import com.core.mall.product.entity.CategoryEntity;
import com.core.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("page")
    public Result<PageData<CategoryDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<CategoryDTO> page = categoryService.page(params);

        return new Result<PageData<CategoryDTO>>().ok(page);
    }

    @GetMapping("pageWithTree")
    public Result<List<CategoryDTO>> pageWithTree(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        List<CategoryDTO> page = categoryService.pageWithTree(params);

        return new Result<List<CategoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<CategoryDTO> get(@PathVariable("id") Long id){
        CategoryDTO data = categoryService.get(id);

        return new Result<CategoryDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody CategoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        categoryService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        categoryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        categoryService.deleteBatch(ids);

        return new Result();
    }

    @PutMapping("/batch")
    public Result batchUpdate(@RequestBody List<CategoryDTO> categories) {
        // 批量更新 catId, parentCid, catLevel, sort 字段
        List<CategoryEntity> categoryEntities = ConvertUtils.sourceToTarget(categories, CategoryEntity.class);
        categoryService.updateBatchById(categoryEntities);
        return new Result();
    }


}
