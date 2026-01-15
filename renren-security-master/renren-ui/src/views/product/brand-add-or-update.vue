<template>
  <el-dialog v-model="visible" :title="!dataForm.brandId ? '新增' : '修改'" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="rules" ref="dataFormRef" @keyup.enter="dataFormSubmitHandle()" label-width="120px">
          <el-form-item label="品牌名" prop="name">
        <el-input v-model="dataForm.name" placeholder="品牌名"></el-input>
      </el-form-item>
          <el-form-item label="品牌Logo" prop="logo">
        <CosImageUpload v-model="dataForm.logo" custom-dir="brand" upload-text="上传Logo" />
      </el-form-item>
          <el-form-item label="介绍" prop="descript">
        <el-input v-model="dataForm.descript" placeholder="介绍"></el-input>
      </el-form-item>
          <el-form-item label="显示状态" prop="showStatus">
        <el-switch v-model="dataForm.showStatus" :active-value="1" :inactive-value="0" active-text="显示" inactive-text="隐藏" />
      </el-form-item>
          <el-form-item label="检索首字母" prop="firstLetter">
        <el-input v-model="dataForm.firstLetter" placeholder="检索首字母"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
      </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";
import CosImageUpload from "@/components/cos-image-upload";
const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  brandId: '',  name: '',  logo: '',  descript: '',  showStatus: 1,  firstLetter: '',  sort: ''});

const rules = ref({
          name: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          logo: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          descript: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          showStatus: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          firstLetter: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ],
          sort: [
      { required: true, message: '必填项不能为空', trigger: 'blur' }
    ]
  });

const init = (brandId?: number) => {
  visible.value = true;
  dataForm.brandId = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  if (brandId) {
    getInfo(brandId);
  }
};

// 获取信息
const getInfo = (brandId: number) => {
  baseService.get("/product/brand/" + brandId).then((res) => {
    Object.assign(dataForm, res.data);
  });
};

// 表单提交
const dataFormSubmitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    (!dataForm.brandId ? baseService.post : baseService.put)("/product/brand", dataForm).then((res) => {
      ElMessage.success({
        message: '成功',
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshDataList");
        }
      });
    });
  });
};

defineExpose({
  init
});
</script>
