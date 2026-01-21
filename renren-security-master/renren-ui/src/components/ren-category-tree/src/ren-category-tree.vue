<template>
  <div class="ren-category-tree">
    <div class="tree-header">
      <span class="tree-title">{{ title }}</span>
    </div>
    <el-input
      v-if="showFilter"
      v-model="filterText"
      placeholder="输入关键字进行过滤"
      clearable
      class="filter-input"
    />
    <div class="tree-container">
      <el-tree
        ref="treeRef"
        v-loading="loading"
        :data="categoryList"
        :props="treeProps"
        node-key="catId"
        :highlight-current="true"
        :expand-on-click-node="false"
        :default-expand-all="defaultExpandAll"
        :filter-node-method="filterNode"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="tree-node-content">
            <span class="node-label">{{ data.name }}</span>
          </span>
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from "vue";
import baseService from "@/service/baseService";
import { ElMessage } from "element-plus";

// Props 定义
const props = withDefaults(defineProps<{
  title?: string;
  showFilter?: boolean;
  defaultExpandAll?: boolean;
  url?: string;
}>(), {
  title: "分类列表",
  showFilter: true,
  defaultExpandAll: false,
  url: "/product/category/pageWithTree"
});

// Emits 定义
const emit = defineEmits<{
  (e: "node-click", data: any, node: any): void;
}>();

// 响应式数据
const filterText = ref("");
const loading = ref(false);
const categoryList = ref<any[]>([]);
const treeRef = ref();

// 树形配置
const treeProps = {
  children: "childrens",
  label: "name"
};

// 过滤节点
const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.includes(value);
};

// 监听过滤文本变化
watch(filterText, (val) => {
  treeRef.value?.filter(val);
});

// 获取分类列表
const getDataList = () => {
  loading.value = true;
  baseService.get(props.url).then((res) => {
    if (res.code === 0) {
      categoryList.value = res.data || [];
    } else {
      ElMessage.error(res.msg || "获取分类数据失败");
    }
    loading.value = false;
  }).catch((err) => {
    ElMessage.error(err?.message || "获取分类数据失败");
    loading.value = false;
  });
};

// 处理节点点击
const handleNodeClick = (data: any, node: any) => {
  emit("node-click", data, node);
};

// 获取当前选中的节点
const getCurrentNode = () => {
  return treeRef.value?.getCurrentNode();
};

// 设置当前选中的节点
const setCurrentKey = (key: string | number) => {
  treeRef.value?.setCurrentKey(key);
};

// 刷新数据
const refresh = () => {
  getDataList();
};

// 组件挂载时获取数据
onMounted(() => {
  getDataList();
});

// 暴露方法给父组件
defineExpose({
  getCurrentNode,
  setCurrentKey,
  refresh
});
</script>

<style lang="less" scoped>
.ren-category-tree {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;

  .tree-header {
    padding: 12px 16px;
    border-bottom: 1px solid #e4e7ed;
    background-color: #f5f7fa;

    .tree-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }

  .filter-input {
    margin: 10px;
    width: calc(100% - 20px);
  }

  .tree-container {
    flex: 1;
    overflow-y: auto;
    padding: 10px;

    :deep(.el-tree-node__content) {
      height: 36px;
      border-radius: 4px;
      
      &:hover {
        background-color: #f5f7fa;
      }
    }

    :deep(.el-tree-node.is-current > .el-tree-node__content) {
      background-color: #ecf5ff;
      color: #409eff;
    }
  }

  .tree-node-content {
    flex: 1;
    display: flex;
    align-items: center;
    overflow: hidden;

    .node-label {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>

