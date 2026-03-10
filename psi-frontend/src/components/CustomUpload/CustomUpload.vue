<template>
  <div class="custom-upload">
    <div class="upload-trigger" @click="triggerUpload">
      <div class="upload-content">
        <el-icon class="upload-icon">
          <Upload />
        </el-icon>
        <span class="upload-text">点击上传</span>
      </div>
      <el-icon class="dropdown-icon">
        <ArrowDown />
      </el-icon>
    </div>
    <input
      ref="fileInput"
      type="file"
      :multiple="multiple"
      :accept="accept"
      style="display: none"
      @change="handleFileChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Upload, ArrowDown } from '@element-plus/icons-vue'

// 定义组件属性
const props = defineProps<{
  multiple?: boolean
  accept?: string
  disabled?: boolean
}>()

// 定义事件
const emit = defineEmits<{
  change: [files: FileList | null]
}>()

// 文件输入框引用
const fileInput = ref<HTMLInputElement>()

/**
 * 触发文件选择
 */
function triggerUpload() {
  if (props.disabled) return
  fileInput.value?.click()
}

/**
 * 处理文件选择变化
 */
function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  const files = target.files
  emit('change', files)
}
</script>

<style scoped>
.custom-upload {
  width: 100%;
}

.upload-trigger {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 32px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.upload-trigger:hover {
  border-color: #c0c4cc;
}

.upload-trigger:focus-within {
  border-color: #409eff;
  outline: none;
}

.upload-content {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.upload-icon {
  font-size: 14px;
  color: #606266;
}

.upload-text {
  font-size: 14px;
  color: #606266;
}

.dropdown-icon {
  font-size: 12px;
  color: #c0c4cc;
  margin-left: 8px;
}

.upload-trigger:hover .dropdown-icon {
  color: #909399;
}
</style>

