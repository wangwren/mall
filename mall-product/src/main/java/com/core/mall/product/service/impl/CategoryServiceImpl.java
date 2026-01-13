package com.core.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.mall.common.page.PageData;
import com.core.mall.common.service.impl.CrudServiceImpl;
import com.core.mall.common.utils.ConvertUtils;
import com.core.mall.product.dao.CategoryDao;
import com.core.mall.product.dto.CategoryDTO;
import com.core.mall.product.entity.CategoryEntity;
import com.core.mall.product.service.CategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品三级分类
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<CategoryDTO> pageWithTree(Map<String, Object> params) {
        // 1. 查询所有分类
        List<CategoryEntity> categoryEntities = baseDao.selectList(null);
        List<CategoryDTO> categoryDTOS = ConvertUtils.sourceToTarget(categoryEntities, CategoryDTO.class);

        // 2. 构建树并排序
        return buildCategoryTree(categoryDTOS);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 构建树形分类结构
     */
    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> allCategories) {
        if (allCategories == null || allCategories.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 按 parentCid 分组：parentCid -> 直属子节点列表
        Map<Long, List<CategoryDTO>> childrenMap = allCategories.stream()
                .collect(Collectors.groupingBy(dto ->
                        Optional.ofNullable(dto.getParentCid()).orElse(0L))
                );

        // 2. 找出根节点（parentCid = 0）
        List<CategoryDTO> roots = childrenMap.getOrDefault(0L, Collections.emptyList());

        // 3. 递归填充 children（但每次找子节点是 O(1)）
        roots.forEach(root -> fillChildren(root, childrenMap));

        // 4. 对根节点做排序
        roots.sort(categoryComparator());

        return roots;
    }

    /**
     * 递归设置 children，并对子节点列表排序
     */
    private void fillChildren(CategoryDTO node, Map<Long, List<CategoryDTO>> childrenMap) {
        List<CategoryDTO> childrens = childrenMap.getOrDefault(node.getCatId(), Collections.emptyList());

        // 先把孩子挂上去
        node.setChildrens(childrens.isEmpty() ? Collections.emptyList() : childrens);

        // 对子节点排序
        childrens.sort(categoryComparator());

        // 再递归对子节点处理
        childrens.forEach(child -> fillChildren(child, childrenMap));
    }

    /**
     * 分类排序比较器：按 sort 升序，null 当 0 处理
     */
    private Comparator<CategoryDTO> categoryComparator() {
        return Comparator.comparingInt(dto ->
                Optional.ofNullable(dto.getSort()).orElse(0)
        );
    }

}