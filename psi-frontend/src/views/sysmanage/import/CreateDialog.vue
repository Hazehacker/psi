<!-- src/views/sysmanage/import/CreateDialog.vue -->
<template>
  <el-dialog
    v-model="visible"
    :title="mode === 'add' ? '新增模板' : '编辑模板'"
    width="500px"
    @close="handleCancel"
  >
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="选择文件" prop="file">
        <!-- 编辑模式且已有文件时的显示 -->
        <div v-if="mode === 'edit' && hasExistingFile" class="existing-file">
          <div class="file-info">
            <el-icon><Document /></el-icon>
            <span class="file-name">{{ getFileNameFromUrl(formData.downloadUrl) }}</span>
            <el-button
              type="primary"
              link
              @click="handleDownloadExistingFile"
              :disabled="isUploading"
            >
              下载
            </el-button>
          </div>
          <el-button
            type="danger"
            size="small"
            @click="handleRemoveExistingFile"
            :disabled="isUploading"
          >
            删除文件
          </el-button>
          <div class="file-tip">已存在文件，如需更换请先删除当前文件</div>
        </div>

        <!-- 文件上传组件（没有已有文件或已删除时显示） -->
        <el-upload
          v-if="mode === 'add' || !hasExistingFile"
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :limit="1"
          accept=".xls,.xlsx"
          :file-list="fileList"
          :disabled="isUploading"
        >
          <el-button type="primary" :disabled="isUploading">选择文件</el-button>
          <template #tip>
            <div class="el-upload__tip">
              支持 xls、xlsx 格式，建议选择后编辑类型使模板名称自动加载
            </div>
          </template>
        </el-upload>

        <!-- 新增模式下显示已选文件 -->
        <div v-if="mode === 'add' && formData.file" class="mt-2">
          已选文件：{{ formData.file.name }}
        </div>
      </el-form-item>

      <el-form-item label="模板名称" prop="templateName">
        <el-input v-model="formData.templateName" placeholder="请输入" />
      </el-form-item>

      <el-form-item label="存储方式" prop="saveType">
        <el-input v-model="formData.saveType" placeholder="请输入" />
      </el-form-item>

      <el-form-item v-if="mode === 'edit'" label="数据状态" prop="status">
        <el-select v-model="formData.status" placeholder="请选择">
          <el-option label="使用中" :value="1" />
          <el-option label="未使用" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="4" placeholder="请输入" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel" :disabled="isUploading">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="isUploading">
          {{ mode === 'add' ? '添加' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, watch, nextTick, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type UploadFile } from 'element-plus'
import { Document } from '@element-plus/icons-vue'
import type { ImportTemplateList } from '@/apis/sysmanage/import/type'

const props = defineProps<{
  visible: boolean
  formData?: ImportTemplateList
  mode: 'add' | 'edit'
  treeData?: any[]
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'save', form: any): void
  (e: 'cancel'): void
}>()

const visible = ref(props.visible)
const formRef = ref<FormInstance>()
const fileList = ref<UploadFile[]>([])
const isUploading = ref(false)

// 标记是否删除了原有文件
const isExistingFileRemoved = ref(false)

const formData = reactive<{
  templateName: string
  saveType: string
  status: number
  remark: string
  file: File | null
  downloadUrl?: string
}>({
  templateName: '',
  saveType: 'local',
  status: 1,
  remark: '',
  file: null,
  downloadUrl: undefined,
  ...props.formData
})

// 计算属性：判断是否有已有文件
const hasExistingFile = computed(() => {
  return props.mode === 'edit' && props.formData?.downloadUrl && !isExistingFileRemoved.value
})

const rules = {
  file: [
    {
      required: true,
      message: '请选择模板文件',
      trigger: 'change',
      validator: (rule: any, value: any, callback: any) => {
        // 编辑模式下，如果有已有文件或者新选择了文件，都算通过
        if (props.mode === 'edit' && (hasExistingFile.value || formData.file)) {
          callback()
        }
        // 新增模式下必须选择文件
        else if (props.mode === 'add' && formData.file) {
          callback()
        } else {
          callback(new Error('请选择模板文件'))
        }
      }
    }
  ],
  templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  saveType: [{ required: false, message: '请输入存储方式', trigger: 'blur' }],
  status: [{ required: true, message: '请选择数据状态', trigger: 'change' }]
}

// 从URL中提取文件名
const getFileNameFromUrl = (url: string | undefined): string => {
  if (!url) return '未知文件'
  try {
    const urlObj = new URL(url)
    return urlObj.pathname.split('/').pop() || '未知文件'
  } catch {
    return url.split('/').pop() || '未知文件'
  }
}

// 监听 visible 变化
watch(
  () => props.visible,
  (val) => {
    visible.value = val
    if (val) {
      // 显示弹窗时重置表单数据
      nextTick(() => {
        resetFormData()
      })
    }
  }
)

// 监听 formData 变化（编辑时数据回填）
watch(
  () => props.formData,
  (newFormData) => {
    if (newFormData) {
      Object.assign(formData, newFormData)
    }
  },
  { deep: true, immediate: true }
)

// 同步 visible 变化到父组件
watch(visible, (val) => {
  emit('update:visible', val)
})

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    templateName: '',
    saveType: 'local',
    status: 1,
    remark: '',
    file: null,
    ...props.formData
  })
  fileList.value = []
  isExistingFileRemoved.value = false
}

const handleFileChange = (file: UploadFile) => {
  if (file.raw) {
    formData.file = file.raw
    // 尝试从文件名提取模板名称（如果模板名称为空）
    if (!formData.templateName && file.name) {
      formData.templateName = file.name.split('.')[0]
    }
  }
}

const handleFileRemove = () => {
  formData.file = null
  fileList.value = []
}

// 下载已有文件
const handleDownloadExistingFile = () => {
  if (formData.downloadUrl) {
    window.open(formData.downloadUrl, '_blank')
  }
}

// 删除已有文件
const handleRemoveExistingFile = () => {
  ElMessageBox.confirm('确定要删除已上传的文件吗？删除后需要重新上传文件。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    isExistingFileRemoved.value = true
    ElMessage.success('文件已删除，现在可以上传新文件')
  })
}

const handleSave = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      // 确保文件存在（新增时必填，编辑时可选但至少要有已有文件或新文件）
      if (props.mode === 'add' && !formData.file) {
        ElMessage.error('请选择模板文件')
        return
      }

      if (props.mode === 'edit' && !hasExistingFile.value && !formData.file) {
        ElMessage.error('请选择模板文件或保留已有文件')
        return
      }

      isUploading.value = true

      // 构建提交数据
      const submitData = {
        ...formData,
        // 文件处理逻辑：
        // - 新增模式：必须传入文件
        // - 编辑模式：
        //   - 如果删除了原有文件并上传了新文件：传入新文件
        //   - 如果保留了原有文件：不传入文件（file = null）
        //   - 如果保留了原有文件但上传了新文件：传入新文件（覆盖原有文件）
        file:
          props.mode === 'add'
            ? formData.file
            : isExistingFileRemoved.value || formData.file
              ? formData.file
              : null,
        // 标记是否删除了原有文件
        isExistingFileRemoved: isExistingFileRemoved.value
      }

      console.log('提交数据:', {
        mode: props.mode,
        hasExistingFile: hasExistingFile.value,
        isExistingFileRemoved: isExistingFileRemoved.value,
        hasNewFile: !!formData.file,
        file: submitData.file ? submitData.file.name : 'null'
      })

      emit('save', submitData)

      // 3秒后重置上传状态，防止一直loading
      setTimeout(() => {
        isUploading.value = false
      }, 3000)
    }
  })
}

const handleCancel = () => {
  if (!isUploading.value) {
    visible.value = false
    emit('cancel')
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
.mt-2 {
  margin-top: 8px;
}

.existing-file {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  background-color: #fafafa;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.file-name {
  font-size: 14px;
  color: #606266;
  flex: 1;
}

.file-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload .el-button) {
  width: 100%;
}
</style>
