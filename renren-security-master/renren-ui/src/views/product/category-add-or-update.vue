<template>
  <el-dialog v-model="visible" :title="!dataForm.catId ? '新增分类' : '修改分类'" :close-on-click-modal="false" :close-on-press-escape="false" width="500px">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="100px">
      <el-form-item prop="name" label="分类名称">
        <el-input v-model="dataForm.name" placeholder="请输入分类名称"></el-input>
      </el-form-item>
      <el-form-item prop="parentName" label="上级分类" class="category-list">
        <el-popover ref="categoryListPopover" placement="bottom-start" trigger="click" :width="400" popper-class="popover-pop">
          <template v-slot:reference>
            <el-input v-model="dataForm.parentName" :readonly="true" placeholder="请选择上级分类">
              <template v-slot:suffix>
                <el-icon v-if="dataForm.parentCid !== 0" @click.stop="categoryListTreeSetDefaultHandle()" class="el-input__icon"><circle-close /></el-icon>
              </template>
            </el-input>
          </template>
          <div class="popover-pop-body">
            <el-tree 
              :data="categoryList" 
              :props="{ label: 'name', children: 'childrens' }" 
              node-key="catId" 
              ref="categoryListTree" 
              :highlight-current="true" 
              :expand-on-click-node="false" 
              accordion 
              @current-change="categoryListTreeCurrentChangeHandle"
            ></el-tree>
          </div>
        </el-popover>
      </el-form-item>
      <el-form-item prop="icon" label="图标">
        <el-input v-model="dataForm.icon" placeholder="请输入图标URL"></el-input>
      </el-form-item>
      <el-form-item prop="productUnit" label="计量单位">
        <el-input v-model="dataForm.productUnit" placeholder="请输入计量单位，如：件、个、台"></el-input>
      </el-form-item>
      <el-form-item prop="sort" label="排序">
        <el-input-number v-model="dataForm.sort" controls-position="right" :min="0" label="排序"></el-input-number>
      </el-form-item>
      <el-form-item prop="showStatus" label="显示状态">
        <el-radio-group v-model="dataForm.showStatus">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { IObject } from "@/types/interface";
import { ElMessage } from "element-plus";

const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const categoryList = ref<any[]>([]);
const dataFormRef = ref();
const categoryListTree = ref();
const categoryListPopover = ref();

const dataForm = reactive({
  catId: "",
  name: "",
  parentCid: 0,
  parentName: "",
  icon: "",
  productUnit: "",
  sort: 0,
  showStatus: 1,
  catLevel: 1
});

const rules = ref({
  name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
  parentName: [{ required: true, message: "请选择上级分类", trigger: "change" }]
});

// 初始化 - 新增/修改
const init = (catId?: string | number) => {
  visible.value = true;
  dataForm.catId = "";
  
  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  // 重置默认值
  dataForm.parentCid = 0;
  dataForm.parentName = "一级分类";
  dataForm.showStatus = 1;
  dataForm.sort = 0;
  dataForm.catLevel = 1;
  dataForm.icon = "";
  dataForm.productUnit = "";

  getCategoryList().then(() => {
    if (catId) {
      getInfo(catId);
    }
  });
};

// 初始化 - 添加子分类
const init2 = (row: IObject) => {
  visible.value = true;
  
  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  dataForm.catId = "";
  dataForm.parentCid = row.catId;
  dataForm.parentName = row.name;
  dataForm.catLevel = row.catLevel + 1;
  dataForm.showStatus = 1;
  dataForm.sort = 0;
  dataForm.icon = "";
  dataForm.productUnit = "";

  getCategoryList();
};

// 获取分类列表
const getCategoryList = () => {
  return baseService.get("/product/category/pageWithTree").then((res) => {
    if (res.code === 0) {
      categoryList.value = res.data || [];
    } else {
      ElMessage.error(res.msg || "获取分类列表失败");
    }
  });
};

// 获取分类信息
const getInfo = (catId: string | number) => {
  baseService.get(`/product/category/${catId}`).then((res) => {
    if (res.code === 0) {
      Object.assign(dataForm, res.data);
      if (dataForm.parentCid === 0) {
        return categoryListTreeSetDefaultHandle();
      }
      // 查找父分类名称
      const parentCategory = findCategoryById(categoryList.value, dataForm.parentCid);
      if (parentCategory) {
        dataForm.parentName = parentCategory.name;
        categoryListTree.value?.setCurrentKey(dataForm.parentCid);
      }
    } else {
      ElMessage.error(res.msg || "获取分类信息失败");
    }
  });
};

// 根据ID查找分类
const findCategoryById = (list: any[], catId: string | number): any => {
  for (const item of list) {
    if (String(item.catId) === String(catId)) {
      return item;
    }
    if (item.childrens && item.childrens.length > 0) {
      const found = findCategoryById(item.childrens, catId);
      if (found) return found;
    }
  }
  return null;
};

// 上级分类树, 设置默认值
const categoryListTreeSetDefaultHandle = () => {
  dataForm.parentCid = 0;
  dataForm.parentName = "一级分类";
  dataForm.catLevel = 1;
};

// 上级分类树, 选中
const categoryListTreeCurrentChangeHandle = (data: IObject) => {
  dataForm.parentCid = data.catId;
  dataForm.parentName = data.name;
  dataForm.catLevel = data.catLevel + 1;
  categoryListPopover.value.hide();
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    const submitData = {
      catId: dataForm.catId || null,
      name: dataForm.name || null,
      parentCid: dataForm.parentCid,
      icon: dataForm.icon || null,
      productUnit: dataForm.productUnit || null,
      sort: dataForm.sort,
      showStatus: dataForm.showStatus,
      catLevel: dataForm.catLevel
    };
    
    (!dataForm.catId ? baseService.post : baseService.put)("/product/category", submitData).then((res) => {
      if (res.code === 0) {
        ElMessage.success({
          message: "操作成功",
          duration: 500,
          onClose: () => {
            visible.value = false;
            emit("refreshDataList");
          }
        });
      } else {
        ElMessage.error(res.msg || "操作失败");
      }
    }).catch((err) => {
      ElMessage.error(err?.message || "操作失败，请稍后重试");
    });
  });
};

defineExpose({
  init,
  init2
});
</script>

<style lang="less" scoped>
.category-list {
  :deep(.el-input__inner),
  :deep(.el-input__suffix) {
    cursor: pointer;
  }
}
.popover-pop-body {
  max-height: 300px;
  overflow-y: auto;
}
</style>

