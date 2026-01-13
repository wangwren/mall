<template>
  <div class="mod-product__category">
    <el-form :inline="true" :model="dataForm">
      <el-form-item>
        <el-input v-model="dataForm.name" placeholder="分类名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="updateNodes.length > 0" type="success" @click="batchSave()">批量保存</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 树形列表容器 -->
    <div ref="treeContainerRef" class="category-tree-container">
      <el-tree
        ref="categoryTreeRef"
        v-loading="dataListLoading"
        :data="dataList"
        :props="treeProps"
        node-key="catId"
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        @node-drag-start="handleDragStart"
        @node-drag-enter="handleDragEnter"
        @node-drag-leave="handleDragLeave"
        @node-drag-end="handleDragEnd"
        @node-drop="handleDrop"
        class="category-tree"
      >
        <template #default="{ node, data }">
          <div class="category-tree-node">
            <span class="col-name">{{ data.name }}</span>
            <span class="col-action">
              <el-button type="primary" link size="small" @click.stop="addOrUpdateHandle(data.catId)">修改</el-button>
              <el-button v-if="data.catLevel < 3" type="primary" link size="small" @click.stop="addChildHandle(data)">添加</el-button>
              <el-button type="danger" link size="small" @click.stop="deleteHandle(data.catId)">删除</el-button>
            </span>
          </div>
        </template>
      </el-tree>
    </div>
    
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted, onUnmounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import baseService from "@/service/baseService";
import AddOrUpdate from "./category-add-or-update.vue";

// 表单数据
const dataForm = reactive({
  name: ""
});

// 数据列表
const dataList = ref<any[]>([]);
const dataListLoading = ref(false);

// 树形配置
const treeProps = {
  children: "childrens",
  label: "name"
};

// 记录需要更新的节点
const updateNodes = ref<any[]>([]);

// 新增/修改弹窗
const addOrUpdateRef = ref();

// 树组件引用
const categoryTreeRef = ref();

// 树容器引用
const treeContainerRef = ref<HTMLElement>();

// 拖拽悬停展开定时器
let dragExpandTimer: ReturnType<typeof setTimeout> | null = null;

// 拖拽自动滚动相关
let isDragging = false;
let autoScrollTimer: ReturnType<typeof setInterval> | null = null;
const SCROLL_EDGE_SIZE = 60; // 边缘触发区域大小（像素）
const SCROLL_SPEED = 10; // 滚动速度（像素/次）

// 获取数据列表
const getDataList = () => {
  dataListLoading.value = true;
  updateNodes.value = []; // 清空待更新列表
  baseService.get("/product/category/pageWithTree", dataForm).then((res) => {
    if (res.code === 0) {
      dataList.value = res.data || [];
    } else {
      ElMessage.error(res.msg || "获取数据失败");
    }
    dataListLoading.value = false;
  }).catch((err) => {
    ElMessage.error(err?.message || "获取数据失败，请稍后重试");
    dataListLoading.value = false;
  });
};

// 计算节点的最大深度（包含自身）
const getNodeMaxDepth = (node: any): number => {
  if (!node.childrens || node.childrens.length === 0) {
    return 1;
  }
  let maxChildDepth = 0;
  for (const child of node.childrens) {
    const childDepth = getNodeMaxDepth(child);
    if (childDepth > maxChildDepth) {
      maxChildDepth = childDepth;
    }
  }
  return maxChildDepth + 1;
};

// 处理拖拽过程中的自动滚动
const handleDragOver = (e: DragEvent) => {
  if (!isDragging || !treeContainerRef.value) return;
  
  const container = treeContainerRef.value;
  const rect = container.getBoundingClientRect();
  const mouseY = e.clientY;
  
  // 清除之前的滚动定时器
  if (autoScrollTimer) {
    clearInterval(autoScrollTimer);
    autoScrollTimer = null;
  }
  
  // 判断是否在顶部边缘区域
  if (mouseY < rect.top + SCROLL_EDGE_SIZE && mouseY >= rect.top) {
    autoScrollTimer = setInterval(() => {
      container.scrollTop -= SCROLL_SPEED;
    }, 20);
  }
  // 判断是否在底部边缘区域
  else if (mouseY > rect.bottom - SCROLL_EDGE_SIZE && mouseY <= rect.bottom) {
    autoScrollTimer = setInterval(() => {
      container.scrollTop += SCROLL_SPEED;
    }, 20);
  }
};

// 拖拽开始
const handleDragStart = (node: any, ev: DragEvent) => {
  isDragging = true;
  // 添加全局拖拽监听
  document.addEventListener('dragover', handleDragOver);
};

// 拖拽进入节点时 - 悬停一段时间后自动展开
const handleDragEnter = (draggingNode: any, dropNode: any, ev: Event) => {
  // 清除之前的定时器
  if (dragExpandTimer) {
    clearTimeout(dragExpandTimer);
    dragExpandTimer = null;
  }
  // 如果目标节点有子节点且未展开，悬停500ms后自动展开
  if (dropNode.childNodes && dropNode.childNodes.length > 0 && !dropNode.expanded) {
    dragExpandTimer = setTimeout(() => {
      dropNode.expanded = true;
    }, 500);
  }
};

// 拖拽离开节点时 - 清除定时器
const handleDragLeave = (draggingNode: any, dropNode: any, ev: Event) => {
  if (dragExpandTimer) {
    clearTimeout(dragExpandTimer);
    dragExpandTimer = null;
  }
};

// 拖拽结束时 - 清除定时器和滚动
const handleDragEnd = (draggingNode: any, dropNode: any, dropType: string, ev: Event) => {
  isDragging = false;
  
  // 清除展开定时器
  if (dragExpandTimer) {
    clearTimeout(dragExpandTimer);
    dragExpandTimer = null;
  }
  
  // 清除滚动定时器
  if (autoScrollTimer) {
    clearInterval(autoScrollTimer);
    autoScrollTimer = null;
  }
  
  // 移除全局拖拽监听
  document.removeEventListener('dragover', handleDragOver);
};

// 判断是否允许拖拽
const allowDrag = (draggingNode: any) => {
  // 所有节点都可以拖拽
  return true;
};

// 判断是否允许放置
const allowDrop = (draggingNode: any, dropNode: any, type: string) => {
  // 计算拖拽节点的最大深度
  const draggingDepth = getNodeMaxDepth(draggingNode.data);
  
  if (type === "inner") {
    // 放置到节点内部，作为子节点
    // 目标节点的层级 + 拖拽节点的深度 不能超过 3
    const targetLevel = dropNode.data.catLevel;
    if (targetLevel + draggingDepth > 3) {
      return false;
    }
  } else {
    // 放置到节点前后，作为兄弟节点
    // 目标节点的父级层级 + 拖拽节点的深度 不能超过 3
    const targetLevel = dropNode.data.catLevel;
    if (targetLevel - 1 + draggingDepth > 3) {
      return false;
    }
  }
  return true;
};

// 处理拖拽完成
const handleDrop = (draggingNode: any, dropNode: any, dropType: string, ev: Event) => {
  // 计算新的父节点ID和层级
  let parentCid: string | number = 0;
  let level = 1;
  let siblings: any[] = [];
  
  if (dropType === "inner") {
    // 放置到节点内部
    parentCid = dropNode.data.catId;
    level = dropNode.data.catLevel + 1;
    siblings = dropNode.data.childrens || [];
  } else {
    // 放置到节点前后
    parentCid = dropNode.data.parentCid;
    level = dropNode.data.catLevel;
    // 获取兄弟节点
    if (dropNode.parent && dropNode.parent.data && dropNode.parent.data.childrens) {
      siblings = dropNode.parent.data.childrens;
    } else {
      siblings = dataList.value;
    }
  }
  
  // 更新拖拽节点及其子节点的层级
  updateNodeLevel(draggingNode.data, level);
  
  // 更新父节点ID
  draggingNode.data.parentCid = parentCid;
  
  // 记录需要更新的节点
  collectUpdateNodes(draggingNode.data);
  
  // 更新兄弟节点的排序
  siblings.forEach((node: any, index: number) => {
    if (node.sort !== index) {
      node.sort = index;
      addToUpdateList(node);
    }
  });
  
  ElMessage.success("拖拽成功，请点击【批量保存】按钮保存更改");
};

// 递归更新节点层级
const updateNodeLevel = (node: any, level: number) => {
  if (node.catLevel !== level) {
    node.catLevel = level;
    addToUpdateList(node);
  }
  if (node.childrens && node.childrens.length > 0) {
    node.childrens.forEach((child: any) => {
      updateNodeLevel(child, level + 1);
    });
  }
};

// 收集需要更新的节点（自身和所有子节点）
const collectUpdateNodes = (node: any) => {
  addToUpdateList(node);
  if (node.childrens && node.childrens.length > 0) {
    node.childrens.forEach((child: any) => {
      collectUpdateNodes(child);
    });
  }
};

// 添加到更新列表
const addToUpdateList = (node: any) => {
  const exists = updateNodes.value.find((n: any) => String(n.catId) === String(node.catId));
  if (!exists) {
    updateNodes.value.push({
      catId: node.catId,
      parentCid: node.parentCid,
      catLevel: node.catLevel,
      sort: node.sort
    });
  } else {
    // 更新已存在的记录
    exists.parentCid = node.parentCid;
    exists.catLevel = node.catLevel;
    exists.sort = node.sort;
  }
};

// 批量保存拖拽结果
const batchSave = () => {
  if (updateNodes.value.length === 0) {
    ElMessage.warning("没有需要保存的更改");
    return;
  }
  
  baseService.put("/product/category/batch", updateNodes.value).then((res) => {
    if (res.code === 0) {
      ElMessage.success("保存成功");
      updateNodes.value = [];
      getDataList();
    } else {
      ElMessage.error(res.msg || "保存失败");
    }
  }).catch((err) => {
    ElMessage.error(err?.message || "保存失败，请稍后重试");
  });
};

// 新增/修改
const addOrUpdateHandle = (catId?: string | number) => {
  addOrUpdateRef.value.init(catId);
};

// 添加子分类
const addChildHandle = (row: any) => {
  addOrUpdateRef.value.init2(row);
};

// 删除
const deleteHandle = (catId: string | number) => {
  ElMessageBox.confirm("确定要删除该分类吗？删除后无法恢复！", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    baseService.delete("/product/category", [catId]).then((res) => {
      if (res.code === 0) {
        ElMessage.success("删除成功");
        getDataList();
      } else {
        ElMessage.error(res.msg || "删除失败");
      }
    }).catch((err) => {
      ElMessage.error(err?.message || "删除失败，请稍后重试");
    });
  }).catch(() => {
    // 取消删除
  });
};

// 组件挂载时获取数据
onMounted(() => {
  getDataList();
});

// 组件卸载时清理
onUnmounted(() => {
  document.removeEventListener('dragover', handleDragOver);
  if (autoScrollTimer) {
    clearInterval(autoScrollTimer);
  }
  if (dragExpandTimer) {
    clearTimeout(dragExpandTimer);
  }
});
</script>

<style lang="less" scoped>
.mod-product__category {
  padding: 10px;
  height: 100%;
  display: flex;
  flex-direction: column;
  
  .category-tree-container {
    flex: 1;
    overflow-y: auto;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    min-height: 300px;
    max-height: calc(100vh - 200px);
  }
  
  .category-tree {
    :deep(.el-tree-node__content) {
      height: 40px;
      border-bottom: 1px solid #ebeef5;
      
      &:hover {
        background-color: #f5f7fa;
      }
    }
    
    :deep(.el-tree-node:last-child > .el-tree-node__content) {
      border-bottom: none;
    }
    
    :deep(.el-tree-node__expand-icon) {
      font-size: 16px;
    }
  }
  
  .category-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 10px;
  }
  
  .col-name {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .col-action {
    flex-shrink: 0;
    margin-left: 20px;
  }
}
</style>
