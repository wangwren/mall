package com.core.mall.coupon.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.coupon.dto.HomeAdvDTO;
import com.core.mall.coupon.service.HomeAdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 首页轮播广告
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("coupon/homeadv")
public class HomeAdvController {
    @Autowired
    private HomeAdvService homeAdvService;

    @GetMapping("page")
    public Result<PageData<HomeAdvDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<HomeAdvDTO> page = homeAdvService.page(params);

        return new Result<PageData<HomeAdvDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<HomeAdvDTO> get(@PathVariable("id") Long id){
        HomeAdvDTO data = homeAdvService.get(id);

        return new Result<HomeAdvDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody HomeAdvDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        homeAdvService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody HomeAdvDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        homeAdvService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        homeAdvService.delete(ids);

        return new Result();
    }


}
