<template>
  <div class="mod-product__brand">
    <el-form :inline="true" :model="state.dataForm" @keyup.enter="state.getDataList()">
      <el-form-item>
        <el-button v-if="state.hasPermission('product:brand:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-if="state.hasPermission('product:brand:delete')" type="danger" @click="state.deleteHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border @selection-change="state.dataListSelectionChangeHandle" style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
              <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
              <el-table-column prop="name" label="品牌名" header-align="center" align="center"></el-table-column>
              <el-table-column prop="logo" label="品牌Logo" header-align="center" align="center" width="120">
                <template v-slot="scope">
                  <el-image
                    v-if="scope.row.logo"
                    :src="scope.row.logo"
                    :preview-src-list="[scope.row.logo]"
                    fit="contain"
                    style="width: 60px; height: 60px;"
                    preview-teleported
                  />
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="descript" label="介绍" header-align="center" align="center"></el-table-column>
              <el-table-column prop="showStatus" label="显示状态" header-align="center" align="center">
                <template v-slot="scope">
                  <el-switch v-model="scope.row.showStatus" :active-value="1" :inactive-value="0" @change="updateShowStatus(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column prop="firstLetter" label="检索首字母" header-align="center" align="center"></el-table-column>
              <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template v-slot="scope">
          <el-button v-if="state.hasPermission('product:brand:update')" type="primary" link @click="addOrUpdateHandle(scope.row.brandId)">修改</el-button>
          <el-button v-if="state.hasPermission('product:brand:delete')" type="primary" link @click="state.deleteHandle(scope.row.brandId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="[10, 20, 50, 100]" :page-size="state.limit" :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="state.pageSizeChangeHandle" @current-change="state.pageCurrentChangeHandle"> </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="state.getDataList">确定</add-or-update>
  </div>
</template>

<script lang="ts" setup>
import useView from "@/hooks/useView";
import { reactive, ref, toRefs } from "vue";
import AddOrUpdate from "./brand-add-or-update.vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

const view = reactive({
  deleteIsBatch: true,
  getDataListURL: "/product/brand/page",
  getDataListIsPage: true,
  exportURL: "/product/brand/export",
  deleteURL: "/product/brand",
  deleteIsBatchKey: "brandId"
});

const state = reactive({ ...useView(view), ...toRefs(view) });

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

// 更新显示状态
const updateShowStatus = (row: any) => {
  baseService.put("/product/brand", {
    brandId: row.brandId,
    showStatus: row.showStatus
  }).then(() => {
    ElMessage.success("修改成功");
  }).catch(() => {
    // 失败时恢复原状态
    row.showStatus = row.showStatus === 1 ? 0 : 1;
  });
};
</script>
