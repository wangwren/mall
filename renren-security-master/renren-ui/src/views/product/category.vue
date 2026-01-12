<template>
  <div class="mod-product__category">
    <el-form :inline="true" :model="dataForm">
      <el-form-item>
        <el-input v-model="dataForm.name" placeholder="分类名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table 
      v-loading="dataListLoading" 
      :data="dataList" 
      row-key="catId" 
      border 
      default-expand-all
      :tree-props="{ children: 'childrens', hasChildren: 'hasChildren' }"
      style="width: 100%"
    >
      <el-table-column prop="name" label="分类名称" header-align="center" min-width="200"></el-table-column>
      <el-table-column prop="icon" label="图标" header-align="center" align="center" width="100">
        <template #default="scope">
          <el-image 
            v-if="scope.row.icon" 
            :src="scope.row.icon" 
            style="width: 40px; height: 40px;" 
            fit="cover"
          />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="catLevel" label="层级" header-align="center" align="center" width="80"></el-table-column>
      <el-table-column prop="sort" label="排序" header-align="center" align="center" width="80"></el-table-column>
      <el-table-column prop="showStatus" label="显示状态" header-align="center" align="center" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.showStatus === 1" type="success">显示</el-tag>
          <el-tag v-else type="info">隐藏</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="productUnit" label="计量单位" header-align="center" align="center" width="100">
        <template #default="scope">
          {{ scope.row.productUnit || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="productCount" label="商品数量" header-align="center" align="center" width="100"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="200">
        <template #default="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row.catId)">修改</el-button>
          <el-button type="primary" link @click="addChildHandle(scope.row)">添加子分类</el-button>
          <el-button type="danger" link @click="deleteHandle(scope.row.catId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import baseService from "@/service/baseService";

// 表单数据
const dataForm = reactive({
  name: ""
});

// 数据列表
const dataList = ref<any[]>([]);
const dataListLoading = ref(false);

// 获取数据列表
const getDataList = () => {
  dataListLoading.value = true;
  baseService.get("/product/category/pageWithTree", dataForm).then((res) => {
    dataList.value = res.data || [];
    dataListLoading.value = false;
  }).catch(() => {
    dataListLoading.value = false;
  });
};

// 新增/修改
const addOrUpdateHandle = (catId?: number) => {
  ElMessage.info(catId ? `修改分类，ID: ${catId}` : "新增分类");
  // TODO: 打开弹窗进行新增/修改操作
};

// 添加子分类
const addChildHandle = (row: any) => {
  ElMessage.info(`为 "${row.name}" 添加子分类`);
  // TODO: 打开弹窗进行新增子分类操作
};

// 删除
const deleteHandle = (catId: number) => {
  ElMessageBox.confirm("确定要删除该分类吗？删除后无法恢复！", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    baseService.delete(`/product/category/${catId}`, {}).then(() => {
      ElMessage.success("删除成功");
      getDataList();
    });
  }).catch(() => {
    // 取消删除
  });
};

// 组件挂载时获取数据
onMounted(() => {
  getDataList();
});
</script>

<style lang="less" scoped>
.mod-product__category {
  padding: 10px;
}
</style>
