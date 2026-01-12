package com.core.mall.order.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.order.dto.OrderItemDTO;
import com.core.mall.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 订单项信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("order/orderitem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("page")
    public Result<PageData<OrderItemDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<OrderItemDTO> page = orderItemService.page(params);

        return new Result<PageData<OrderItemDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<OrderItemDTO> get(@PathVariable("id") Long id){
        OrderItemDTO data = orderItemService.get(id);

        return new Result<OrderItemDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody OrderItemDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        orderItemService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody OrderItemDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        orderItemService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        orderItemService.delete(ids);

        return new Result();
    }


}
