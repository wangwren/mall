package com.core.mall.coupon.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.coupon.dto.SpuBoundsDTO;
import com.core.mall.coupon.service.SpuBoundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 商品spu积分设置
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;

    @GetMapping("page")
    public Result<PageData<SpuBoundsDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpuBoundsDTO> page = spuBoundsService.page(params);

        return new Result<PageData<SpuBoundsDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<SpuBoundsDTO> get(@PathVariable("id") Long id){
        SpuBoundsDTO data = spuBoundsService.get(id);

        return new Result<SpuBoundsDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody SpuBoundsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spuBoundsService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody SpuBoundsDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spuBoundsService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        spuBoundsService.delete(ids);

        return new Result();
    }


}
