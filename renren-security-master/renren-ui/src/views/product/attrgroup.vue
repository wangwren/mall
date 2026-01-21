<template>
  <div class="mod-product__attrgroup">
    <!-- 左侧分类树 -->
    <div class="left-tree">
      <ren-category-tree
        ref="categoryTreeRef"
        title="商品分类"
        @node-click="handleCategoryClick"
      />
    </div>
    
    <!-- 右侧表格区域 -->
    <div class="right-table">
      <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
        <el-form-item>
          <el-input v-model="state.dataForm.attrGroupName" placeholder="分组名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="state.getDataList()">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="state.hasPermission('product:attrgroup:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="state.hasPermission('product:attrgroup:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="attrGroupId" label="分组id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrGroupName" label="组名" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
        <el-table-column prop="descript" label="描述" header-align="center" align="center"></el-table-column>
        <el-table-column prop="icon" label="组图标" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catelogId" label="所属分类id" header-align="center" align="center"></el-table-column>
        <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
          <template v-slot="scope">
            <el-button v-if="state.hasPermission('product:attrgroup:update')" type="primary" link @click="addOrUpdateHandle(scope.row.attrGroupId)">修改</el-button>
            <el-button v-if="state.hasPermission('product:attrgroup:delete')" type="primary" link @click="state.deleteHandle(scope.row.attrGroupId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    </div>
    
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./attrgroup-add-or-update.vue";
import RenCategoryTree from "@/components/ren-category-tree/index";

const view = reactive({
  createdIsNeed: false, // 页面加载时不自动查询，等待选择分类
  deleteIsBatch: true,
  getDataListURL: "/product/attrgroup/page",
  getDataListIsPage: true,
  exportURL: "/product/attrgroup/export",
  deleteURL: "/product/attrgroup",
  dataForm: {
    attrGroupName: "",
    categoryId: ""
  }
});

const state = reactive({ ...useView(view), ...toRefs(view) });

// 当前选中的分类ID
const currentCategoryId = ref<number | string>("");

// 分类树引用
const categoryTreeRef = ref();

// 处理分类树节点点击
const handleCategoryClick = (data: any) => {
  // 只有点击三级分类时才调用接口
  if (data.catLevel === 3) {
    currentCategoryId.value = data.catId;
    // 将分类ID添加到查询参数中
    state.dataForm.categoryId = data.catId;
    // 重新获取数据
    state.getDataList();
  }
};

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};
</script>

<style lang="less" scoped>
.mod-product__attrgroup {
  display: flex;
  gap: 15px;
  height: calc(100vh - 130px);
  
  .left-tree {
    width: 280px;
    flex-shrink: 0;
  }
  
  .right-table {
    flex: 1;
    min-width: 0;
    background-color: #fff;
    padding: 15px;
    border-radius: 4px;
    border: 1px solid #e4e7ed;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    
    .el-form {
      flex-shrink: 0;
    }
    
    .el-table {
      flex: 1;
    }
    
    .el-pagination {
      flex-shrink: 0;
      margin-top: 15px;
      justify-content: flex-end;
    }
  }
}
</style>
