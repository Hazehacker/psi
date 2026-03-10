<template>
  <div class="attachment-manage">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>附件管理</h2>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>系统参数</el-breadcrumb-item>
        <el-breadcrumb-item>条码管理</el-breadcrumb-item>
        <el-breadcrumb-item>附件管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="queryParams" inline>
        <el-form-item label="文件名">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入文件名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-input
            v-model="queryParams.fileType"
            placeholder="请输入文件类型"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="未使用" :value="0" />
            <el-option label="使用中" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-area">
      <el-button type="primary" @click="handleUpload">
        <el-icon><Plus /></el-icon>
        上传附件
      </el-button>
      <el-button @click="handleRefresh">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-area">
      <el-table :data="tableData" v-loading="loading" border style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="文件名" min-width="200">
          <template #default="{ row }">
            <div class="file-name">
              <el-icon class="file-icon">
                <Document />
              </el-icon>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="fileType" label="文件类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getFileTypeTag(row.fileType) as any">
              {{ row.fileType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="saveType" label="存储方式" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.saveType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="savePath" label="存储路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '使用中' : '未使用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)"> 编辑 </el-button>
            <el-button link type="danger" @click="handleDelete(row)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-area">
        <el-pagination
          v-model:current-page="queryParams.pageIndex"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="editForm.id ? '编辑附件' : '上传附件'"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="80px">
        <el-form-item label="文件名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-input v-model="editForm.fileType" placeholder="文件类型" disabled />
        </el-form-item>
        <el-form-item label="存储方式">
          <el-input v-model="editForm.saveType" placeholder="存储方式" disabled />
        </el-form-item>
        <el-form-item label="存储路径">
          <el-input v-model="editForm.savePath" placeholder="存储路径" disabled />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="0">未使用</el-radio>
            <el-radio :label="1">使用中</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="editForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
        <el-form-item v-if="!editForm.id" label="上传文件" prop="file">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :on-change="handleFileChange"
            :show-file-list="false"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                当前文件: {{ selectedFile ? selectedFile.name : '未选择文件' }}
                <div v-if="fileSizeWarning" style="color: #e6a23c; margin-top: 5px">
                  <el-icon><Warning /></el-icon>
                  文件较大，上传可能需要较长时间
                </div>
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting"> 确定 </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type UploadInstance } from 'element-plus'
import { Plus, Refresh, Document, Warning } from '@element-plus/icons-vue'
import type { AttachmentInfo, AttachmentQuery } from '@/apis/assistantdata/attachment'
import {
  getAttachmentList,
  uploadAttachment,
  updateAttachment,
  deleteAttachment
} from '@/apis/assistantdata/attachment'

// 定义 ElTag 的 type 属性允许的类型
type ElTagType = 'primary' | 'success' | 'warning' | 'info' | 'danger'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const fileSizeWarning = ref(false)
const tableData = ref<AttachmentInfo[]>([])
const total = ref(0)

// 查询参数
const queryParams = reactive<AttachmentQuery>({
  pageIndex: 1,
  pageSize: 10,
  name: '',
  fileType: '',
  status: undefined
})

// 编辑表单相关
const editDialogVisible = ref(false)
const editFormRef = ref<FormInstance>()
const uploadRef = ref<UploadInstance>()
const selectedFile = ref<File | null>(null)

const editForm = reactive<AttachmentInfo>({
  name: '',
  fileType: '',
  savePath: '',
  saveType: '',
  status: 0,
  remark: ''
})

const editFormRules = {
  name: [{ required: true, message: '请输入文件名', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 文件类型标签映射 - 根据实际文件扩展名设置颜色
const getFileTypeTag = (fileType: string): ElTagType => {
  const typeMap: Record<string, ElTagType> = {
    pdf: 'warning',
    doc: 'primary',
    docx: 'primary',
    ppt: 'success',
    pptx: 'success',
    xls: 'info',
    xlsx: 'info',
    jpg: 'success',
    jpeg: 'success',
    png: 'success',
    gif: 'success',
    zip: 'info',
    rar: 'info',
    '7z': 'info'
  }
  return typeMap[fileType?.toLowerCase()] || 'info'
}

// 文件上传前检查
const beforeUpload = (file: File) => {
  fileSizeWarning.value = file.size > 10 * 1024 * 1024 // 10MB
  return true
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getAttachmentList(queryParams)
    if (res.code === 10000 && res.data) {
      tableData.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error: any) {
    console.error('加载附件列表失败:', error)
    ElMessage.error(error.message || '网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  queryParams.pageIndex = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(queryParams, {
    pageIndex: 1,
    pageSize: 10,
    name: '',
    fileType: '',
    status: undefined
  })
  loadData()
}

// 刷新
const handleRefresh = () => {
  loadData()
}

// 分页大小改变
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.pageIndex = 1
  loadData()
}

// 当前页改变
const handleCurrentChange = (page: number) => {
  queryParams.pageIndex = page
  loadData()
}

// 上传附件
const handleUpload = () => {
  editDialogVisible.value = true
  Object.assign(editForm, {
    id: undefined,
    name: '',
    fileType: '',
    savePath: '',
    saveType: '',
    status: 0,
    remark: ''
  })
  selectedFile.value = null
  fileSizeWarning.value = false
  nextTick(() => {
    editFormRef.value?.clearValidate()
  })
}

// 编辑附件
const handleEdit = (row: AttachmentInfo) => {
  editDialogVisible.value = true
  Object.assign(editForm, { ...row })
  selectedFile.value = null
  fileSizeWarning.value = false
  nextTick(() => {
    editFormRef.value?.clearValidate()
  })
}

// 文件选择变化
const handleFileChange = (file: any) => {
  selectedFile.value = file.raw
  fileSizeWarning.value = file.raw.size > 10 * 1024 * 1024 // 10MB

  // 自动填充文件名和文件类型
  if (selectedFile.value) {
    editForm.name = selectedFile.value.name
    const ext = selectedFile.value.name.split('.').pop()?.toLowerCase()
    if (ext) {
      editForm.fileType = ext
    }
  }
}

// 对话框关闭处理
const handleDialogClose = () => {
  selectedFile.value = null
  fileSizeWarning.value = false
  submitting.value = false
}

// 提交表单
const handleSubmit = async () => {
  if (!editFormRef.value) return

  // 验证表单
  try {
    await editFormRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    if (editForm.id) {
      // 更新附件 - 发送完整的附件信息
      const res = await updateAttachment(editForm)
      if (res.code === 10000) {
        ElMessage.success('更新成功')
        editDialogVisible.value = false
        loadData()
      } else {
        // 处理文件不存在的特定错误
        if (res.message?.includes('文件不存在')) {
          ElMessage.error('文件存储异常，请联系管理员检查文件存储服务')
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      }
    } else {
      // 上传附件
      if (!selectedFile.value) {
        ElMessage.warning('请选择要上传的文件')
        submitting.value = false
        return
      }

      const res = await uploadAttachment(selectedFile.value)
      if (res.code === 10000) {
        ElMessage.success('上传成功')
        editDialogVisible.value = false
        loadData()
      } else {
        ElMessage.error(res.message || '上传失败')
      }
    }
  } catch (error: any) {
    console.error('操作失败:', error)
    if (error?.message?.includes('超时')) {
      ElMessage.error('上传超时，请检查网络连接或文件大小，然后重试')
    } else if (error?.message?.includes('文件不存在')) {
      ElMessage.error('文件存储异常，请联系管理员检查文件存储服务')
    } else {
      ElMessage.error(error.message || '操作失败，请稍后重试')
    }
  } finally {
    submitting.value = false
  }
}

// 删除附件
const handleDelete = async (row: AttachmentInfo) => {
  if (!row.id) return

  try {
    await ElMessageBox.confirm(`确定要删除附件 "${row.name}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const res = await deleteAttachment(row.id)
    if (res.code === 10000) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch {
    // 用户取消删除
    console.log('取消删除')
  }
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.attachment-manage {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  font-size: 20px;
  color: #303133;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.action-area {
  margin-bottom: 20px;
}

.table-area {
  margin-bottom: 20px;
}

.pagination-area {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.file-name {
  display: flex;
  align-items: center;
}

.file-icon {
  margin-right: 8px;
  color: #409eff;
}
</style>
