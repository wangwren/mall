package com.core.mall.order.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.order.dto.OrderReturnReasonDTO;
import com.core.mall.order.service.OrderReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 退货原因
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("order/orderreturnreason")
public class OrderReturnReasonController {
    @Autowired
    private OrderReturnReasonService orderReturnReasonService;

    @GetMapping("page")
    public Result<PageData<OrderReturnReasonDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<OrderReturnReasonDTO> page = orderReturnReasonService.page(params);

        return new Result<PageData<OrderReturnReasonDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<OrderReturnReasonDTO> get(@PathVariable("id") Long id){
        OrderReturnReasonDTO data = orderReturnReasonService.get(id);

        return new Result<OrderReturnReasonDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody OrderReturnReasonDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        orderReturnReasonService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody OrderReturnReasonDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        orderReturnReasonService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        orderReturnReasonService.delete(ids);

        return new Result();
    }


}
