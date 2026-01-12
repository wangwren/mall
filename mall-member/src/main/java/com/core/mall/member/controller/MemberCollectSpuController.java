package com.core.mall.member.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.member.dto.MemberCollectSpuDTO;
import com.core.mall.member.service.MemberCollectSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 会员收藏的商品
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("member/membercollectspu")
public class MemberCollectSpuController {
    @Autowired
    private MemberCollectSpuService memberCollectSpuService;

    @GetMapping("page")
    public Result<PageData<MemberCollectSpuDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<MemberCollectSpuDTO> page = memberCollectSpuService.page(params);

        return new Result<PageData<MemberCollectSpuDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<MemberCollectSpuDTO> get(@PathVariable("id") Long id){
        MemberCollectSpuDTO data = memberCollectSpuService.get(id);

        return new Result<MemberCollectSpuDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody MemberCollectSpuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberCollectSpuService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody MemberCollectSpuDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberCollectSpuService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberCollectSpuService.delete(ids);

        return new Result();
    }


}
