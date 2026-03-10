<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮和弹框 -->
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
              <!-- 属性名称输入框 -->
              <div class="form-item">
                <label class="form-label">属性名称</label>
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入属性名称"
                  clearable
                  @clear="handleClearInput('name')"
                />
              </div>

              <!-- 备注信息输入框 -->
              <div class="form-item">
                <label class="form-label">备注信息</label>
                <el-input
                  v-model="searchForm.data"
                  placeholder="请输入备注信息"
                  clearable
                  @clear="handleClearInput('data')"
                />
              </div>

              <!-- 搜索按钮 -->
              <div class="form-actions">
                <el-button type="primary" @click="handleSearch" class="search-action-btn">
                  <el-icon><Search /></el-icon>
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

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-container">
      <ReportButtonTable
        :columns="columns"
        :data="tableData"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        :show-selection="false"
        :show-operations="true"
        :loading="loading"
        @view="handleView"
        @delete="handleDelete"
        @page-change="handlePageChange"
      />
    </div>

    <!-- 详情/编辑弹框 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="isEditing ? '编辑属性' : '属性详情'"
      width="600px"
      :before-close="handleCloseDetailDialog"
    >
      <el-form ref="detailFormRef" :model="detailForm" :rules="formRules" label-width="80px">
        <el-form-item label="属性名称" prop="name">
          <el-input v-model="detailForm.name" placeholder="请输入属性名称" :disabled="!isEditing" />
        </el-form-item>

        <el-form-item label="属性排序" prop="sort">
          <el-input
            v-model.number="detailForm.sort"
            placeholder="请输入排序数字"
            type="number"
            :disabled="!isEditing"
          />
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input
            v-model="detailForm.data"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
            :disabled="!isEditing"
          />
        </el-form-item>

        <!-- 属性内容管理区域 -->
        <el-form-item label="属性内容" v-if="isEditing">
          <div class="property-content-management">
            <div class="content-input-section">
              <el-input
                v-model="newPropertyContent"
                placeholder="请输入属性内容"
                style="width: calc(100% - 80px); margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="handleAddPropertyContent"
                :disabled="!newPropertyContent"
              >
                添加
              </el-button>
            </div>

            <!-- 属性内容表格 -->
            <div class="content-table-section">
              <el-table
                :data="detailForm.content"
                style="width: 100%; margin-top: 10px"
                border
                size="small"
              >
                <el-table-column prop="name" label="属性内容" min-width="200" />
                <el-table-column label="操作" width="80" align="center">
                  <template #default="scope">
                    <el-button
                      type="danger"
                      link
                      size="small"
                      @click="handleDeletePropertyContent(scope.$index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-form-item>

        <!-- 只读模式下显示属性内容 -->
        <el-form-item label="属性内容" v-else>
          <div class="property-content-readonly">
            <el-tag
              v-for="(content, index) in detailForm.content"
              :key="index"
              style="margin: 2px 4px"
            >
              {{ content.name }}
            </el-tag>
            <div v-if="!detailForm.content || detailForm.content.length === 0" class="empty-tips">
              暂无属性内容
            </div>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCloseDetailDialog">取消</el-button>
          <el-button
            v-if="isEditing"
            type="primary"
            @click="handleSaveDetail"
            :loading="saveLoading"
          >
            保存
          </el-button>
          <el-button v-else type="primary" @click="handleEditDetail"> 编辑 </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新增弹框 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增属性"
      width="600px"
      :before-close="handleCloseAddDialog"
    >
      <el-form ref="addFormRef" :model="addForm" :rules="formRules" label-width="80px">
        <el-form-item label="属性名称" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入属性名称" />
        </el-form-item>

        <el-form-item label="属性排序" prop="sort">
          <el-input v-model.number="addForm.sort" placeholder="请输入排序数字" type="number" />
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input v-model="addForm.data" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>

        <!-- 属性内容管理区域 -->
        <el-form-item label="属性内容">
          <div class="property-content-management">
            <div class="content-input-section">
              <el-input
                v-model="newPropertyContentForAdd"
                placeholder="请输入属性内容"
                style="width: calc(100% - 80px); margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="handleAddPropertyContentForAdd"
                :disabled="!newPropertyContentForAdd"
              >
                添加
              </el-button>
            </div>

            <!-- 属性内容表格 -->
            <div class="content-table-section">
              <el-table
                :data="addForm.content"
                style="width: 100%; margin-top: 10px"
                border
                size="small"
              >
                <el-table-column prop="name" label="属性内容" min-width="200" />
                <el-table-column label="操作" width="80" align="center">
                  <template #default="scope">
                    <el-button
                      type="danger"
                      link
                      size="small"
                      @click="handleDeletePropertyContentForAdd(scope.$index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCloseAddDialog">取消</el-button>
          <el-button type="primary" @click="handleSaveAdd" :loading="saveLoading"> 保存 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, nextTick, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { MoreFilled, Plus, Refresh, Search } from '@element-plus/icons-vue'
import ReportButtonTable from '@/components/report/reportButtonTable.vue'
import {
  getAttributeList,
  getAttributeDetail,
  addAttribute,
  updateAttribute,
  deleteAttribute,
  type AttributeListData,
  type AttributeDetailData,
  type AttributeFormData,
  type AttributeQuery
} from '@/apis/assistantdata/assistantAttribute'

// 搜索弹框显示状态
const searchPopoverVisible = ref(false)

// 搜索表单数据
const searchForm = reactive({
  name: '',
  data: ''
})

// 弹框相关状态
const detailDialogVisible = ref(false)
const addDialogVisible = ref(false)
const isEditing = ref(false)
const saveLoading = ref(false)

// 属性内容输入框
const newPropertyContent = ref('')
const newPropertyContentForAdd = ref('')

// 表单引用
const detailFormRef = ref<FormInstance>()
const addFormRef = ref<FormInstance>()

// 当前操作的行数据
const currentRow = ref<any>(null)

// 表格数据
const tableData = ref<AttributeListData[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// 详情表单数据
const detailForm = reactive<AttributeFormData>({
  id: '',
  name: '',
  sort: 1,
  data: '',
  content: []
})

// 新增表单数据
const addForm = reactive<Omit<AttributeFormData, 'id'>>({
  name: '',
  sort: 1,
  data: '',
  content: []
})

// 表单验证规则
const formRules: FormRules = {
  name: [{ required: true, message: '请输入属性名称', trigger: 'blur' }],
  sort: [
    { required: true, message: '请输入属性排序', trigger: 'blur' },
    { type: 'number', min: 1, message: '排序必须为正整数', trigger: 'blur' }
  ]
}

// 表格列定义
interface TableColumn {
  prop: string
  label: string
  width?: string
  minWidth?: string
  sortable?: boolean
  align?: 'left' | 'center' | 'right'
  slot?: string
}

const columns = ref<TableColumn[]>([
  {
    prop: 'name',
    label: '属性名称',
    width: '120',
    align: 'center'
  },
  {
    prop: 'sort',
    label: '属性排序',
    width: '100',
    sortable: true
  },
  {
    prop: 'data',
    label: '备注信息',
    width: '150'
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
    const queryParams: AttributeQuery = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...(searchForm.name && { name: searchForm.name }),
      ...(searchForm.data && { data: searchForm.data })
    }

    const response = await getAttributeList(queryParams)
    if (response.code === 10000 && response.data) {
      tableData.value = response.data.rows || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('加载表格数据失败:', error)
    ElMessage.error('加载数据失败，请重试')
  } finally {
    loading.value = false
  }
}

// 事件处理函数
const handleAdd = () => {
  // 重置新增表单
  Object.assign(addForm, {
    name: '',
    sort: 1,
    data: '',
    content: []
  })

  // 重置属性内容输入框
  newPropertyContentForAdd.value = ''

  // 清除表单验证
  nextTick(() => {
    addFormRef.value?.clearValidate()
  })

  addDialogVisible.value = true
}

// 修改刷新功能：重置搜索条件并重新加载数据
const handleRefresh = () => {
  // 重置搜索条件
  searchForm.name = ''
  searchForm.data = ''
  // 重置页码
  currentPage.value = 1
  // 重新加载数据
  loadTableData()
  ElMessage.success('数据已刷新，搜索条件已重置')
}

// 搜索处理
const handleSearch = () => {
  searchPopoverVisible.value = false
  currentPage.value = 1
  loadTableData()
}

// 重置搜索
const handleResetSearch = () => {
  searchForm.name = ''
  searchForm.data = ''
  currentPage.value = 1
  loadTableData()
  ElMessage.info('已重置搜索条件')
}

// 清空输入框
const handleClearInput = (field: string) => {
  ;(searchForm as any)[field] = ''
}

// 查看详情
const handleView = async (row: AttributeListData) => {
  try {
    loading.value = true
    const response = await getAttributeDetail(row.id)
    if (response.code === 10000 && response.data) {
      currentRow.value = row
      isEditing.value = false

      // 填充表单数据
      Object.assign(detailForm, {
        id: response.data.id,
        name: response.data.name,
        sort: response.data.sort,
        data: response.data.data || '',
        content: response.data.content || []
      })

      // 重置属性内容输入框
      newPropertyContent.value = ''

      detailDialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取详情失败')
    }
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败，请重试')
  } finally {
    loading.value = false
  }
}

// 编辑详情
const handleEditDetail = () => {
  isEditing.value = true
}

// 添加属性内容（详情弹窗）
const handleAddPropertyContent = () => {
  if (newPropertyContent.value.trim()) {
    detailForm.content.push({
      name: newPropertyContent.value.trim()
    })
    newPropertyContent.value = ''
  }
}

// 删除属性内容（详情弹窗）
const handleDeletePropertyContent = (index: number) => {
  detailForm.content.splice(index, 1)
}

// 添加属性内容（新增弹窗）
const handleAddPropertyContentForAdd = () => {
  if (newPropertyContentForAdd.value.trim()) {
    addForm.content.push({
      name: newPropertyContentForAdd.value.trim()
    })
    newPropertyContentForAdd.value = ''
  }
}

// 删除属性内容（新增弹窗）
const handleDeletePropertyContentForAdd = (index: number) => {
  addForm.content.splice(index, 1)
}

// 保存详情（编辑）
const handleSaveDetail = async () => {
  if (!detailFormRef.value) return

  try {
    // 验证表单
    await detailFormRef.value.validate()

    saveLoading.value = true

    // 准备提交数据
    const submitData: AttributeFormData = {
      id: detailForm.id,
      name: detailForm.name,
      sort: detailForm.sort,
      data: detailForm.data,
      content: detailForm.content
    }

    const response = await updateAttribute(submitData)
    if (response.code === 10000) {
      ElMessage.success('保存成功')
      detailDialogVisible.value = false
      isEditing.value = false
      loadTableData() // 刷新表格数据
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    // 验证失败，不关闭弹框
    console.log('表单验证失败:', error)
  } finally {
    saveLoading.value = false
  }
}

// 保存新增
const handleSaveAdd = async () => {
  if (!addFormRef.value) return

  try {
    // 验证表单
    await addFormRef.value.validate()

    saveLoading.value = true

    // 准备提交数据
    const submitData: Omit<AttributeFormData, 'id'> = {
      name: addForm.name,
      sort: addForm.sort,
      data: addForm.data,
      content: addForm.content
    }

    const response = await addAttribute(submitData)
    if (response.code === 10000) {
      ElMessage.success('新增成功')
      addDialogVisible.value = false
      loadTableData() // 刷新表格数据
    } else {
      ElMessage.error(response.message || '新增失败')
    }
  } catch (error) {
    // 验证失败，不关闭弹框
    console.log('表单验证失败:', error)
  } finally {
    saveLoading.value = false
  }
}

// 关闭详情弹框
const handleCloseDetailDialog = () => {
  detailDialogVisible.value = false
  isEditing.value = false
  currentRow.value = null
  newPropertyContent.value = ''
}

// 关闭新增弹框
const handleCloseAddDialog = () => {
  addDialogVisible.value = false
  newPropertyContentForAdd.value = ''
}

// 删除数据函数
const handleDelete = async (rows: any[]) => {
  if (rows.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }

  try {
    if (rows.length === 1) {
      // 单条删除
      const rowToDelete = rows[0]
      const response = await deleteAttribute(rowToDelete.id)
      if (response.code === 10000) {
        ElMessage.success('删除成功')
        loadTableData() // 刷新表格数据
      } else {
        ElMessage.error(response.message || '删除失败')
      }
    } else {
      // 批量删除
      const deletePromises = rows.map((row) => deleteAttribute(row.id))
      const results = await Promise.all(deletePromises)
      const successCount = results.filter((result) => result.code === 10000).length

      if (successCount === rows.length) {
        ElMessage.success(`成功删除 ${rows.length} 条数据`)
      } else {
        ElMessage.warning(
          `成功删除 ${successCount} 条数据，${rows.length - successCount} 条数据删除失败`
        )
      }

      loadTableData() // 刷新表格数据
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败，请重试')
  }
}

const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
  loadTableData()
}
</script>

<style scoped>
/* 原有的样式保持不变 */
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

.search-popover {
  padding: 0;
}

.search-form {
  padding: 16px;
}

.form-item {
  margin-bottom: 16px;
}

.form-label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.property-content-management {
  width: 100%;
}

.content-input-section {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.content-table-section {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.property-content-readonly {
  min-height: 40px;
  padding: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
}

.empty-tips {
  color: #909399;
  font-size: 14px;
  text-align: center;
  padding: 10px;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .sys.area {
    padding: 8px;
    height: calc(100vh - 16px);
  }

  .operation-bar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .operation-left,
  .operation-right {
    justify-content: center;
  }

  .operation-left {
    border-bottom: 1px solid #e4e7ed;
    padding-bottom: 12px;
  }

  .search-popover {
    width: 280px !important;
  }
}
</style>
