<template>
  <div class="cos-image-upload">
    <el-upload
      class="image-uploader"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :http-request="customUpload"
      :accept="accept"
      :disabled="disabled"
    >
      <div v-if="imageUrl" class="image-preview">
        <el-image :src="imageUrl" fit="contain" class="preview-image" />
        <div class="image-actions">
          <el-icon class="action-icon" @click.stop="handlePreview"><ZoomIn /></el-icon>
          <el-icon v-if="!disabled" class="action-icon" @click.stop="handleRemove"><Delete /></el-icon>
        </div>
      </div>
      <div v-else class="upload-placeholder">
        <el-icon v-if="!uploading" class="upload-icon"><Plus /></el-icon>
        <el-icon v-else class="upload-icon is-loading"><Loading /></el-icon>
        <div class="upload-text">{{ uploading ? '上传中...' : uploadText }}</div>
      </div>
    </el-upload>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图片预览" width="800px" append-to-body>
      <div class="preview-dialog-content">
        <el-image :src="imageUrl" fit="contain" style="max-width: 100%; max-height: 70vh;" />
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from "vue";
import { Plus, Delete, ZoomIn, Loading } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import type { UploadRequestOptions, UploadRawFile } from "element-plus";
import baseService from "@/service/baseService";

interface Props {
  modelValue?: string;
  disabled?: boolean;
  accept?: string;
  maxSize?: number; // 最大文件大小，单位MB
  uploadText?: string;
  customDir?: string; // 自定义上传目录
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: "",
  disabled: false,
  accept: "image/jpeg,image/png,image/gif,image/webp",
  maxSize: 10,
  uploadText: "点击上传",
  customDir: "brand"
});

const emit = defineEmits<{
  (e: "update:modelValue", value: string): void;
  (e: "change", value: string): void;
}>();

const uploading = ref(false);
const previewVisible = ref(false);

const imageUrl = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit("update:modelValue", val);
    emit("change", val);
  }
});

// 上传前校验
const beforeUpload = (file: UploadRawFile) => {
  // 检查文件类型
  const acceptTypes = props.accept.split(",").map((t) => t.trim());
  if (!acceptTypes.includes(file.type)) {
    ElMessage.error(`只能上传 ${props.accept} 格式的图片!`);
    return false;
  }

  // 检查文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize;
  if (!isLtMaxSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB!`);
    return false;
  }

  return true;
};

// 自定义上传逻辑
const customUpload = async (options: UploadRequestOptions) => {
  const file = options.file;
  uploading.value = true;

  try {
    // 1. 获取上传签名
    const signResponse = await baseService.post("/thirdparty/cos/getUploadSign", {
      fileName: file.name,
      contentType: file.type,
      fileSize: file.size,
      customDir: props.customDir
    });

    if (signResponse.code !== 0) {
      throw new Error(signResponse.msg || "获取上传签名失败");
    }

    const { uploadUrl, accessUrl, contentType } = signResponse.data;

    // 2. 直接上传文件到 COS
    const uploadResponse = await fetch(uploadUrl, {
      method: "PUT",
      headers: {
        "Content-Type": contentType
      },
      body: file
    });

    if (!uploadResponse.ok) {
      throw new Error("文件上传失败");
    }

    // 3. 上传成功，设置访问URL
    imageUrl.value = accessUrl;
    ElMessage.success("上传成功");
  } catch (error: any) {
    console.error("上传失败:", error);
    ElMessage.error(error.message || "上传失败，请重试");
  } finally {
    uploading.value = false;
  }
};

// 预览图片
const handlePreview = () => {
  previewVisible.value = true;
};

// 删除图片
const handleRemove = () => {
  imageUrl.value = "";
};
</script>

<style lang="less" scoped>
.cos-image-upload {
  .image-uploader {
    :deep(.el-upload) {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;
      width: 148px;
      height: 148px;
      display: flex;
      align-items: center;
      justify-content: center;

      &:hover {
        border-color: var(--el-color-primary);
      }
    }
  }

  .image-preview {
    width: 146px;
    height: 146px;
    position: relative;

    .preview-image {
      width: 100%;
      height: 100%;
    }

    .image-actions {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
      opacity: 0;
      transition: opacity 0.3s;

      .action-icon {
        font-size: 24px;
        color: #fff;
        cursor: pointer;
        transition: transform 0.2s;

        &:hover {
          transform: scale(1.2);
        }
      }
    }

    &:hover .image-actions {
      opacity: 1;
    }
  }

  .upload-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: var(--el-text-color-placeholder);

    .upload-icon {
      font-size: 28px;
      margin-bottom: 8px;

      &.is-loading {
        animation: rotating 2s linear infinite;
      }
    }

    .upload-text {
      font-size: 12px;
    }
  }

  .preview-dialog-content {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 300px;
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>

