<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <el-popover
          placement="bottom-start"
          :width="300"
          trigger="click"
          v-model:visible="searchPopoverVisible"
        >
          <template #reference>
            <el-button class="search-btn">
              <el-icon><MoreFilled /></el-icon>
            </el-button>
          </template>
          <div class="search-popover">
            <div class="search-form">
              <!-- 收支名称输入框 -->
              <div class="form-item">
                <label class="form-label">收支名称</label>
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入收支名称"
                  clearable
                  @clear="handleClearInput('name')"
                />
              </div>

              <!-- 收支类型下拉框 -->
              <div class="form-item">
                <label class="form-label">收支类型</label>
                <el-select
                  v-model="searchForm.type"
                  placeholder="请选择收支类型"
                  clearable
                  @clear="handleClearInput('type')"
                  style="width: 100%"
                >
                  <el-option label="收入" :value="0" />
                  <el-option label="支出" :value="1" />
                </el-select>
              </div>

              <!-- 搜索按钮 -->
              <div class="form-actions">
                <el-button type="primary" @click="handleSearch" class="search-action-btn">
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="handleResetSearch" class="reset-action-btn"> 重置 </el-button>
              </div>
            </div>
          </div>
        </el-popover>
      </div>

      <div class="operation-right">
        <el-button type="primary" @click="handleAdd" class="action-btn">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button type="info" @click="handleRefresh" class="action-btn">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-divider class="custom-divider" />

    <!-- 表格区域 -->
    <div class="table-container">
      <ReportButtonTable
        :columns="columns"
        :data="tableData"
        :total="pagination.total"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :show-selection="false"
        :show-operations="true"
        :loading="loading"
        @view="handleView"
        @edit="handleEdit"
        @delete="handleDelete"
        @page-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑/详情弹框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :before-close="handleCloseDialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        :disabled="isViewMode"
      >
        <el-form-item label="收支名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入收支名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="收支类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择收支类型" style="width: 100%">
            <el-option label="收入" :value="0" />
            <el-option label="支出" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item label="类别排序" prop="sort">
          <el-input-number
            v-model="formData.sort"
            placeholder="请输入排序数字"
            :min="1"
            :max="999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input
            v-model="formData.data"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCloseDialog" :disabled="saveLoading">取消</el-button>
          <el-button v-if="!isViewMode" type="primary" @click="handleSubmit" :loading="saveLoading">
            {{ saveLoading ? '保存中...' : '保存' }}
          </el-button>
          <el-button v-if="isViewMode" type="primary" @click="switchToEditMode"> 编辑 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { MoreFilled, Plus, Refresh, Search } from '@element-plus/icons-vue'
import ReportButtonTable from '@/components/report/reportButtonTable.vue'

// 导入API
import {
  getCategoryList,
  getCategoryDetail,
  addCategory,
  updateCategory,
  deleteCategory,
  type IncomeExpenseCategory,
  type IncomeExpenseCategoryQuery
} from '@/apis/assistantdata/incomeExpenseCategory'

// 定义表格列类型
interface TableColumn {
  prop: string
  label: string
  width?: string
  minWidth?: string
  align?: 'left' | 'center' | 'right'
  sortable?: boolean
  formatter?: (row: any, column: any, cellValue: any, index: number) => string
}

// 弹框模式枚举
enum DialogMode {
  ADD = 'add',
  EDIT = 'edit',
  VIEW = 'view'
}

// 搜索相关
const searchPopoverVisible = ref(false)
const searchForm = reactive({
  name: '',
  type: undefined as number | undefined
})

// 表格相关
const tableData = ref<IncomeExpenseCategory[]>([])
const loading = ref(false)
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 弹框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const saveLoading = ref(false)
const formRef = ref<FormInstance>()
const dialogMode = ref<DialogMode>(DialogMode.ADD)
const currentEditId = ref<string>('')

// 计算属性：是否处于查看模式
const isViewMode = ref(false)

// 表单数据
const formData = reactive({
  name: '',
  type: 0,
  sort: 1,
  data: ''
})

// 表单验证规则
const formRules: FormRules = {
  name: [
    { required: true, message: '请输入收支名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择收支类型', trigger: 'change' }],
  sort: [
    { required: true, message: '请输入类别排序', trigger: 'blur' },
    { type: 'number', min: 1, max: 999, message: '排序范围 1-999', trigger: 'blur' }
  ]
}

// 表格列配置 - 修复：确保收支类型正确显示
const columns = ref<TableColumn[]>([
  {
    prop: 'name',
    label: '收支名称',
    width: '120',
    align: 'center'
  },
  {
    prop: 'type',
    label: '收支类型',
    width: '100',
    align: 'center',
    formatter: (row: any) => {
      // 确保正确处理类型显示，0=收入，1=支出
      const typeValue = Number(row.type)
      console.log(
        '类型格式化:',
        row.type,
        '->',
        typeValue,
        '显示:',
        typeValue === 0 ? '收入' : '支出'
      )
      return typeValue === 0 ? '收入' : typeValue === 1 ? '支出' : '未知'
    }
  },
  {
    prop: 'sort',
    label: '收支排序',
    width: '120',
    align: 'center',
    sortable: true
  },
  {
    prop: 'data',
    label: '备注信息',
    width: '120',
    align: 'center'
  }
])

// 生命周期
onMounted(() => {
  loadTableData()
})

// 加载表格数据
const loadTableData = async () => {
  try {
    loading.value = true
    console.log('🚀 加载表格数据，搜索条件:', searchForm)

    const queryParams: IncomeExpenseCategoryQuery = {
      pageIndex: pagination.currentPage,
      pageSize: pagination.pageSize,
      name: searchForm.name || undefined,
      type: searchForm.type
    }

    const response = await getCategoryList(queryParams)
    console.log('✅ 获取数据响应:', response)

    if (response.code === 10000 && response.data) {
      tableData.value = response.data.rows || []
      pagination.total = response.data.total || 0
      console.log('📊 成功加载数据，共', tableData.value.length, '条')
      // 打印数据详情，检查type字段
      tableData.value.forEach((item, index) => {
        console.log(`数据 ${index + 1}:`, item)
      })
    } else {
      console.error('❌ 接口返回错误:', response)
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error: any) {
    console.error('💥 请求异常:', error)
    ElMessage.error(error.message || '加载数据失败，请重试')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  console.log('🔍 执行搜索，条件:', searchForm)
  searchPopoverVisible.value = false
  pagination.currentPage = 1
  loadTableData()
}

// 重置搜索
const handleResetSearch = () => {
  console.log('🔄 重置搜索条件')
  searchForm.name = ''
  searchForm.type = undefined
  pagination.currentPage = 1
  loadTableData()
  ElMessage.info('已重置搜索条件')
}

// 清空输入框
const handleClearInput = (field: string) => {
  ;(searchForm as any)[field] = field === 'type' ? undefined : ''
}

// 刷新数据
const handleRefresh = () => {
  console.log('🔄 执行刷新，重置搜索条件')
  searchForm.name = ''
  searchForm.type = undefined
  pagination.currentPage = 1
  loadTableData()
  ElMessage.info('数据已刷新')
}

// 新增操作
const handleAdd = () => {
  dialogMode.value = DialogMode.ADD
  dialogTitle.value = '新增收支类别'
  isViewMode.value = false
  currentEditId.value = ''

  // 重置表单
  Object.assign(formData, {
    name: '',
    type: 0,
    sort: 1,
    data: ''
  })

  nextTick(() => {
    formRef.value?.clearValidate()
  })

  dialogVisible.value = true
}

// 查看详情
const handleView = async (row: any) => {
  try {
    console.log('👀 查看详情，ID:', row.id)
    const response = await getCategoryDetail(row.id)
    console.log('📋 详情响应:', response)

    if (response.code === 10000 && response.data) {
      dialogMode.value = DialogMode.VIEW
      dialogTitle.value = '收支类别详情'
      isViewMode.value = true
      currentEditId.value = row.id

      // 填充表单数据
      Object.assign(formData, {
        name: response.data.name || '',
        type: Number(response.data.type) || 0,
        sort: Number(response.data.sort) || 1,
        data: response.data.data || ''
      })

      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取详情失败')
    }
  } catch (error: any) {
    console.error('获取详情失败:', error)
    ElMessage.error(error.message || '获取详情失败，请重试')
  }
}

// 编辑操作
const handleEdit = async (row: any) => {
  try {
    console.log('✏️ 编辑数据，ID:', row.id)
    const response = await getCategoryDetail(row.id)
    console.log('📋 编辑数据响应:', response)

    if (response.code === 10000 && response.data) {
      dialogMode.value = DialogMode.EDIT
      dialogTitle.value = '编辑收支类别'
      isViewMode.value = false
      currentEditId.value = row.id

      // 填充表单数据
      Object.assign(formData, {
        name: response.data.name || '',
        type: Number(response.data.type) || 0,
        sort: Number(response.data.sort) || 1,
        data: response.data.data || ''
      })

      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取编辑数据失败')
    }
  } catch (error: any) {
    console.error('获取编辑数据失败:', error)
    ElMessage.error(error.message || '获取编辑数据失败，请重试')
  }
}

// 从查看模式切换到编辑模式
const switchToEditMode = () => {
  dialogMode.value = DialogMode.EDIT
  dialogTitle.value = '编辑收支类别'
  isViewMode.value = false
}

// 删除操作
const handleDelete = async (rows: any[]) => {
  if (rows.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }

  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${rows.length} 条数据吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('🗑️ 执行删除操作，数据:', rows)

    // 批量删除
    const deletePromises = rows.map((row) => {
      console.log('删除ID:', row.id)
      if (!row.id) {
        console.error('删除失败：ID为空')
        return Promise.reject(new Error('ID为空'))
      }
      return deleteCategory(row.id)
    })

    const results = await Promise.all(deletePromises)
    console.log('删除结果:', results)

    const successCount = results.filter((result) => result && result.code === 10000).length
    if (successCount > 0) {
      ElMessage.success(`成功删除 ${successCount} 条数据`)
      loadTableData()
    } else {
      const firstError = results.find((result) => result && result.code !== 10000)
      ElMessage.error(firstError?.message || '删除失败')
    }

    if (successCount < rows.length) {
      ElMessage.warning(`${rows.length - successCount} 条数据删除失败`)
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除操作失败:', error)
      ElMessage.error(error.message || '删除操作失败，请重试')
    } else {
      console.log('用户取消删除操作')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saveLoading.value = true

    console.log('💾 提交表单数据:', formData)
    console.log('当前模式:', dialogMode.value)
    console.log('当前编辑ID:', currentEditId.value)

    if (dialogMode.value === DialogMode.EDIT) {
      // 编辑操作
      const updateData = {
        id: currentEditId.value,
        name: formData.name,
        type: Number(formData.type),
        sort: Number(formData.sort),
        data: formData.data || ''
      }

      console.log('📤 发送更新数据:', updateData)
      const response = await updateCategory(updateData)
      console.log('📥 更新响应:', response)

      if (response.code === 10000) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(response.message || '更新失败')
      }
    } else if (dialogMode.value === DialogMode.ADD) {
      // 新增操作
      const createData = {
        name: formData.name,
        type: Number(formData.type),
        sort: Number(formData.sort),
        data: formData.data || ''
      }

      console.log('📤 发送新增数据:', createData)
      const response = await addCategory(createData)
      console.log('📥 新增响应:', response)

      if (response.code === 10000) {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        loadTableData()
      } else {
        ElMessage.error(response.message || '新增失败')
      }
    }
  } catch (error: any) {
    if (error && error.fields) {
      console.log('表单验证失败:', error.fields)
    } else {
      console.error('保存失败:', error)
      ElMessage.error(error.message || '保存失败，请重试')
    }
  } finally {
    saveLoading.value = false
  }
}

// 关闭弹框
const handleCloseDialog = () => {
  if (!saveLoading.value) {
    dialogVisible.value = false
    formRef.value?.clearValidate()
  }
}

// 分页变化
const handlePageChange = (page: number, size: number) => {
  pagination.currentPage = page
  pagination.pageSize = size
  loadTableData()
}
</script>

<style scoped>
.sys.area {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 0;
}

.operation-left,
.operation-right {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
}

.search-btn,
.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
}

.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}
</style>
