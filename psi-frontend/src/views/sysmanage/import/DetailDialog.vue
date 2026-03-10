<!-- src/views/sysmanage/import/DetailDialog.vue -->
<template>
  <el-dialog v-model="visible" title="模板详情" width="500px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="模板名称">
        {{ currentFormData.templateName || '' }}
      </el-descriptions-item>

      <el-descriptions-item label="存储方式">
        {{ currentFormData.saveType || '' }}
      </el-descriptions-item>

      <el-descriptions-item label="数据状态">
        <el-tag :type="currentFormData.status === 1 ? 'success' : 'info'">
          {{ getDataStatusText(currentFormData.status || 0) }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="备注">
        {{ currentFormData.remark || '' }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 文件预览区域 -->
    <div class="file-preview" v-if="currentFormData.savePath || currentFormData.downloadUrl">
      <div class="preview-title">文件操作</div>
      <el-button type="primary" @click="handleDownload" :disabled="!currentFormData.downloadUrl">
        <el-icon><Download /></el-icon>
        下载文件
      </el-button>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import type { ImportTemplateList } from '@/apis/sysmanage/import/type'

const props = defineProps<{
  visible: boolean
  formData?: ImportTemplateList
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'close'): void
}>()

const visible = ref(props.visible)

// 使用 computed 属性来确保数据实时更新
const currentFormData = computed<ImportTemplateList>(() => {
  return {
    templateName: '',
    saveType: '',
    status: 0,
    remark: '',
    ...props.formData
  }
})

// 数据状态文本转换
const getDataStatusText = (status: number): string => {
  const dataStatusMap: { [key: number]: string } = {
    0: '未使用',
    1: '使用中'
  }
  return dataStatusMap[status] || '未知'
}

// 下载文件
const handleDownload = async (): Promise<void> => {
  if (currentFormData.value.downloadUrl) {
    try {
      // 创建隐藏的a标签进行下载
      const link = document.createElement('a')
      link.href = currentFormData.value.downloadUrl
      link.target = '_blank'

      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)

      ElMessage.success('开始下载文件')
    } catch (error) {
      console.error('下载失败:', error)
      ElMessage.error('下载失败，请重试')
    }
    return
  }

  // 如果没有 downloadUrl，使用原有的逻辑
  if (!currentFormData.value.id) {
    ElMessage.warning('无法下载：缺少文件信息')
    return
  }
}

// 监听 visible 变化
watch(
  () => props.visible,
  (val) => {
    visible.value = val
  }
)

// 同步 visible 变化到父组件
watch(visible, (val) => {
  emit('update:visible', val)
})

const handleClose = () => {
  visible.value = false
  emit('close')
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.file-preview {
  margin-top: 20px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
}

.preview-title {
  font-weight: bold;
  margin-bottom: 12px;
  color: #606266;
}

.preview-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.file-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.file-name {
  font-size: 14px;
  color: #909399;
  word-break: break-all;
}

:deep(.el-descriptions) {
  margin-bottom: 0;
}

:deep(.el-descriptions__label) {
  width: 100px;
  font-weight: normal;
}

:deep(.el-descriptions__content) {
  color: #606266;
}
</style>
