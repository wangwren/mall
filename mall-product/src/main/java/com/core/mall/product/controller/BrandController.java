package com.core.mall.product.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.product.dto.BrandDTO;
import com.core.mall.product.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 品牌
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/all")
    public Result queryAllBrand() {
        List<BrandDTO> all = brandService.getAll();
        return new Result<List<BrandDTO>>().ok(all);
    }

    @GetMapping("page")
    public Result<PageData<BrandDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<BrandDTO> page = brandService.page(params);

        return new Result<PageData<BrandDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<BrandDTO> get(@PathVariable("id") Long id){
        BrandDTO data = brandService.get(id);

        return new Result<BrandDTO>().ok(data);
    }

    /**
     * 如果不使用分组，可以使用 @Valid做校验
     * 如果使用分组，需要使用 @Validated 做校验,并指定要用的分组;如果用了分组，那么其他字段的校验都要定义分组，否则校验不生效
     */
    @PostMapping
    public Result save(@RequestBody @Validated(AddGroup.class) BrandDTO dto){

        brandService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody @Validated(UpdateGroup.class) BrandDTO dto){

        brandService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        brandService.delete(ids);

        return new Result();
    }


}
