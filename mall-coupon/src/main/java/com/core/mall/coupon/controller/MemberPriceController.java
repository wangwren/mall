package com.core.mall.coupon.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.coupon.dto.MemberPriceDTO;
import com.core.mall.coupon.service.MemberPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 商品会员价格
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("coupon/memberprice")
public class MemberPriceController {
    @Autowired
    private MemberPriceService memberPriceService;

    @GetMapping("page")
    public Result<PageData<MemberPriceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<MemberPriceDTO> page = memberPriceService.page(params);

        return new Result<PageData<MemberPriceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<MemberPriceDTO> get(@PathVariable("id") Long id){
        MemberPriceDTO data = memberPriceService.get(id);

        return new Result<MemberPriceDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody MemberPriceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberPriceService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody MemberPriceDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberPriceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberPriceService.delete(ids);

        return new Result();
    }


}
