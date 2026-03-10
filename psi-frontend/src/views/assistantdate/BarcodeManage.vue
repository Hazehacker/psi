<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮和弹框 -->
        <el-popover
          placement="bottom-start"
          :width="350"
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
              <!-- 条码名称输入框 -->
              <div class="form-item">
                <label class="form-label">条码名称</label>
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入条码名称"
                  clearable
                  @clear="handleClearInput('name')"
                />
              </div>

              <!-- 条码内容输入框 -->
              <div class="form-item">
                <label class="form-label">条码内容</label>
                <el-input
                  v-model="searchForm.info"
                  placeholder="请输入条码内容"
                  clearable
                  @clear="handleClearInput('info')"
                />
              </div>

              <!-- 条码类型下拉框 -->
              <div class="form-item">
                <label class="form-label">条码类型</label>
                <el-select
                  v-model="searchForm.type"
                  placeholder="请选择条码类型"
                  clearable
                  @clear="handleClearInput('type')"
                  :style="{ width: '100%' }"
                >
                  <el-option label="条形码" :value="0" />
                  <el-option label="二维码" :value="1" />
                </el-select>
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
                  搜索
                </el-button>
                <el-button @click="handleResetSearch" class="reset-action-btn"> 重置 </el-button>
              </div>
            </div>
          </div>
        </el-popover>
      </div>

      <div class="operation-right">
        <!-- 自定义按钮组 -->
        <div class="custom-buttons">
          <el-button type="primary" @click="handleAdd" class="action-btn">
            <el-icon><Plus /></el-icon>
            新增
          </el-button>
          <!-- 自定义批量按钮 -->
          <el-button type="warning" @click="handleBatch" class="action-btn batch-btn">
            <el-icon><Document /></el-icon>
            批量
          </el-button>
          <el-button type="info" @click="handleRefresh" class="action-btn">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </div>

    <el-divider class="custom-divider" />

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-container">
      <!-- 如果表格组件有问题，显示备用表格 -->
      <div v-if="tableComponentAvailable">
        <ReportButtonTable
          :columns="columns"
          :data="tableData"
          :total="pagination.total"
          :current-page="pagination.pageIndex"
          :page-size="pagination.pageSize"
          :show-selection="true"
          :show-operations="true"
          :loading="loading"
          @delete="handleDelete"
          @page-change="handlePageChange"
          @selection-change="handleSelectionChange"
          @view="handleNoop"
        >
          <!-- 自定义操作列插槽 -->
          <template #operations="{ row }">
            <div class="operation-buttons">
              <div class="button-group">
                <!-- 查看按钮 -->
                <button class="view-btn" @click="handleViewDetail(row)">查看</button>
                <!-- 详情按钮 -->
                <button class="detail-btn" @click="handleView(row)">详情</button>
                <!-- 删除按钮 -->
                <button class="delete-btn" @click="handleDelete([row])">删除</button>
              </div>
            </div>
          </template>
        </ReportButtonTable>
      </div>
      <div v-else class="fallback-table">
        <el-table
          :data="tableData"
          style="width: 100%"
          border
          v-loading="loading"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="条码名称" width="120" />
          <el-table-column prop="info" label="条码内容" width="150" />
          <el-table-column prop="type" label="条码类型" width="100">
            <template #default="scope">
              {{ getTypeText(scope.row.type) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewDetail(scope.row)">查看</el-button>
              <el-button size="small" type="primary" @click="handleView(scope.row)">详情</el-button>
              <el-button size="small" type="danger" @click="handleDelete([scope.row])"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.pageIndex"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handlePageChange(pagination.pageIndex, $event)"
            @current-change="handlePageChange($event, pagination.pageSize)"
          />
        </div>
      </div>
    </div>

    <!-- 批量操作弹窗 -->
    <el-dialog
      v-model="batchDialogVisible"
      title="批量操作"
      width="600px"
      :before-close="handleCloseBatchDialog"
    >
      <div class="batch-operation">
        <div class="operation-section">
          <h3>数据导入</h3>
          <div class="import-info">
            <div class="info-list">
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>该功能适用于批量导入数据。</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>您需要下载数据模板后使用Excel录入数据。</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>录入数据时，请勿修改首行数据标题以及排序。</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>请查阅使用文档获取字段格式内容以及相关导入须知。</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>点击下方上传模板，选择您编辑好的模板文件即可。</span>
              </div>
            </div>
          </div>
          <div class="import-actions">
            <el-button
              type="primary"
              @click="handleDownloadTemplate"
              class="download-btn"
              :loading="downloadLoading"
            >
              <el-icon><Download /></el-icon>
              下载导入模板
            </el-button>
            <el-upload
              class="upload-demo"
              :before-upload="beforeUpload"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :show-file-list="false"
              accept=".xlsx,.xls,.csv"
              :disabled="uploadLoading"
            >
              <el-button type="success" class="upload-btn" :loading="uploadLoading">
                <el-icon><Upload /></el-icon>
                {{ uploadLoading ? '上传中...' : '上传数据文件' }}
              </el-button>
            </el-upload>
          </div>
          <p class="import-tips">支持 .xlsx, .xls, .csv 格式文件，文件大小不超过 10MB</p>
        </div>

        <el-divider />

        <div class="operation-section">
          <h3>数据导出</h3>
          <div class="export-actions">
            <el-button
              type="warning"
              @click="handleExportSelected"
              :disabled="selectedRows.length === 0 || exportLoading"
              class="export-selected-btn"
            >
              <el-icon><Document /></el-icon>
              导出选中数据 ({{ selectedRows.length }})
            </el-button>
            <el-button
              type="success"
              @click="handleExportAll"
              :disabled="exportLoading"
              class="export-all-btn"
            >
              <el-icon><Finished /></el-icon>
              导出全部数据 ({{ pagination.total }})
            </el-button>
          </div>
          <p class="export-tips">导出的数据将包含当前筛选条件下的所有记录</p>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCloseBatchDialog">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情/编辑弹框 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="isEditing ? '编辑条码' : '条码详情'"
      width="500px"
      :before-close="handleCloseDetailDialog"
    >
      <el-form ref="detailFormRef" :model="detailForm" :rules="formRules" label-width="80px">
        <el-form-item label="条码名称" prop="name">
          <el-input v-model="detailForm.name" placeholder="请输入条码名称" :disabled="!isEditing" />
        </el-form-item>

        <el-form-item label="条码内容" prop="info">
          <el-input v-model="detailForm.info" placeholder="请输入条码内容" :disabled="!isEditing" />
        </el-form-item>

        <el-form-item label="条码类型" prop="type">
          <el-select
            v-model="detailForm.type"
            placeholder="请选择条码类型"
            :style="{ width: '100%' }"
            :disabled="!isEditing"
          >
            <el-option label="条形码" :value="0" />
            <el-option label="二维码" :value="1" />
          </el-select>
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
      title="新增条码"
      width="500px"
      :before-close="handleCloseAddDialog"
    >
      <el-form ref="addFormRef" :model="addForm" :rules="formRules" label-width="80px">
        <el-form-item label="条码名称" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入条码名称" />
        </el-form-item>

        <el-form-item label="条码内容" prop="info">
          <el-input v-model="addForm.info" placeholder="请输入条码内容" />
        </el-form-item>

        <el-form-item label="条码类型" prop="type">
          <el-select v-model="addForm.type" placeholder="请选择条码类型" :style="{ width: '100%' }">
            <el-option label="条形码" :value="0" />
            <el-option label="二维码" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input v-model="addForm.data" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCloseAddDialog">取消</el-button>
          <el-button type="primary" @click="handleSaveAdd" :loading="saveLoading"> 保存 </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看条码弹框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="查看条码"
      width="500px"
      :before-close="handleCloseViewDialog"
    >
      <div class="barcode-view">
        <div class="barcode-info">
          <div class="info-item">
            <span class="label">条码名称：</span>
            <span class="value">{{ viewForm.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">条码内容：</span>
            <span class="value">{{ viewForm.info }}</span>
          </div>
          <div class="info-item">
            <span class="label">条码类型：</span>
            <span class="value">{{ getTypeText(viewForm.type) }}</span>
          </div>
          <div class="info-item">
            <span class="label">备注信息：</span>
            <span class="value">{{ viewForm.data || '无' }}</span>
          </div>
        </div>
        <div class="barcode-preview">
          <div class="preview-title">条码预览</div>
          <div class="preview-content">
            <!-- 条码图片预览 -->
            <div v-if="barcodeImage" class="barcode-image">
              <img
                :src="barcodeImage"
                :alt="viewForm.name"
                style="max-width: 100%; max-height: 200px"
              />
            </div>
            <div v-else class="barcode-placeholder">
              {{ barcodeImageError || '暂无条码图片' }}
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleCloseViewDialog">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, nextTick, onMounted, defineAsyncComponent } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  MoreFilled,
  Search,
  Plus,
  Refresh,
  Download,
  Upload,
  Document,
  Finished,
  InfoFilled
} from '@element-plus/icons-vue'
import {
  getBarcodeList,
  getBarcodeDetail,
  getBarcodeImage,
  addBarcode,
  updateBarcode,
  deleteBarcodes,
  importBarcodes,
  exportBarcodes,
  type BarcodeDTO,
  type BarcodeUpdateDTO,
  type BarcodeQueryParams,
  type BarcodeSearchParams,
  type BarcodeDetailVO,
  type BarcodeListVO,
  type ExportBarcodeItem
} from '@/apis/assistantdata/barcode'

// 组件可用性状态
const tableComponentAvailable = ref(false)

// 使用 defineAsyncComponent 异步加载表格组件
const ReportButtonTable = defineAsyncComponent(() =>
  import('@/components/report/reportButtonTable.vue')
    .then((module) => {
      tableComponentAvailable.value = true
      return module.default || module
    })
    .catch((error) => {
      console.warn('ReportButtonTable 组件加载失败，使用备用表格', error)
      tableComponentAvailable.value = false
      // 返回一个空的组件作为占位符
      return {
        template: '<div></div>',
        props: [],
        setup: () => ({})
      }
    })
)

// 搜索弹框显示状态
const searchPopoverVisible = ref(false)

// 批量操作弹窗显示状态
const batchDialogVisible = ref(false)

// 加载状态
const downloadLoading = ref(false)
const uploadLoading = ref(false)
const exportLoading = ref(false)
const loading = ref(false)
const saveLoading = ref(false)

// 搜索表单数据 - 只包含搜索条件，不包含分页
const searchForm = reactive<BarcodeSearchParams>({
  name: '',
  info: '',
  type: undefined,
  data: ''
})

// 弹框相关状态
const detailDialogVisible = ref(false)
const addDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const isEditing = ref(false)

// 表单引用
const detailFormRef = ref<FormInstance>()
const addFormRef = ref<FormInstance>()

// 当前操作的行数据
const currentRow = ref<any>(null)

// 选中的行数据
const selectedRows = ref<any[]>([])

// 条码图片
const barcodeImage = ref<string>('')
// 条码图片错误信息
const barcodeImageError = ref<string>('')

// 分页数据
const pagination = reactive({
  pageIndex: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref<BarcodeListVO[]>([])

// 详情表单数据
const detailForm = reactive<BarcodeUpdateDTO>({
  id: '',
  name: '',
  info: '',
  type: 0,
  data: ''
})

// 新增表单数据
const addForm = reactive<BarcodeDTO>({
  name: '',
  info: '',
  type: 0,
  data: ''
})

// 查看表单数据
const viewForm = reactive<BarcodeDetailVO>({
  id: '',
  name: '',
  info: '',
  type: 0,
  data: ''
})

// 表单验证规则
const formRules: FormRules = {
  name: [{ required: true, message: '请输入条码名称', trigger: 'blur' }],
  info: [{ required: true, message: '请输入条码内容', trigger: 'blur' }],
  type: [{ required: true, message: '请选择条码类型', trigger: 'change' }]
}

// 获取条码类型文本
const getTypeText = (type: number) => {
  return type === 0 ? '条形码' : '二维码'
}

// 获取条码类型字符串（用于导出）
const getTypeString = (type: number) => {
  return type === 0 ? '条形码' : '二维码'
}

// 空操作函数 - 用于处理不需要的操作
const handleNoop = () => {
  // 什么都不做
}

// 加载条码列表
const loadBarcodeList = async () => {
  loading.value = true
  try {
    // 修复：避免字段重复，明确指定查询参数
    const queryParams: BarcodeQueryParams = {
      pageIndex: pagination.pageIndex,
      pageSize: pagination.pageSize,
      name: searchForm.name,
      info: searchForm.info,
      type: searchForm.type,
      data: searchForm.data
    }

    const response = await getBarcodeList(queryParams)
    if (response.code === 10000 && response.data) {
      tableData.value = response.data.rows || []
      pagination.total = response.data.total || 0
      pagination.pageIndex = response.data.pageIndex || 1
      pagination.pageSize = response.data.pageSize || 10
    } else {
      ElMessage.error(response.message || '获取条码列表失败')
    }
  } catch (error) {
    console.error('加载条码列表失败:', error)
    ElMessage.error('加载条码列表失败')
  } finally {
    loading.value = false
  }
}

// 事件处理函数
const handleAdd = () => {
  // 确保其他弹框关闭
  detailDialogVisible.value = false
  viewDialogVisible.value = false
  isEditing.value = false

  // 重置新增表单
  Object.assign(addForm, {
    name: '',
    info: '',
    type: 0,
    data: ''
  })

  // 清除表单验证
  nextTick(() => {
    addFormRef.value?.clearValidate()
  })

  addDialogVisible.value = true
}

// 批量操作
const handleBatch = () => {
  batchDialogVisible.value = true
}

// 修复：刷新时重置搜索条件
const handleRefresh = () => {
  // 重置搜索条件
  Object.assign(searchForm, {
    name: '',
    info: '',
    type: undefined,
    data: ''
  })
  // 重置分页
  pagination.pageIndex = 1
  // 重新加载数据
  loadBarcodeList()
  ElMessage.info('数据已刷新')
}

// 批量操作相关方法
const handleDownloadTemplate = async () => {
  downloadLoading.value = true
  try {
    // 模拟下载延迟
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 创建模板数据
    const templateData = [
      ['条码名称', '条码内容', '条码类型', '备注信息'],
      ['产品二维码', 'PROD-001-2024', '1', '产品标识二维码'],
      ['仓库条码', 'WH-001-001', '0', '仓库货架标识']
    ]

    // 转换为CSV格式
    const csvContent = templateData
      .map((row) => row.map((cell) => `"${cell}"`).join(','))
      .join('\n')

    // 创建Blob对象并下载
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.setAttribute('href', url)
    link.setAttribute('download', '条码数据导入模板.csv')
    link.style.visibility = 'hidden'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    ElMessage.success('模板下载成功')
  } catch (error) {
    ElMessage.error('模板下载失败')
    console.error('下载模板失败:', error)
  } finally {
    downloadLoading.value = false
  }
}

const beforeUpload = (file: File) => {
  const isExcel =
    file.type === 'application/vnd.ms-excel' ||
    file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
    file.name.endsWith('.csv')
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传Excel或CSV文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过10MB!')
    return false
  }

  uploadLoading.value = true
  return true
}

const handleUploadSuccess = async (response: any, file: any) => {
  uploadLoading.value = false
  try {
    const result = await importBarcodes(file.raw)
    if (result.code === 10000) {
      ElMessage.success('文件上传成功，数据已导入')
      // 重新加载列表
      loadBarcodeList()
      // 关闭弹窗
      batchDialogVisible.value = false
    } else {
      ElMessage.error(result.message || '导入失败')
    }
  } catch (error) {
    ElMessage.error('文件上传失败')
    console.error('上传失败:', error)
  }
}

const handleUploadError = (error: any, file: any) => {
  uploadLoading.value = false
  ElMessage.error('文件上传失败')
  console.error('上传失败:', error, file)
}

// 简化的导出函数 - 不包含 data 字段
const handleExportSelected = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  exportLoading.value = true
  try {
    // 转换数据格式以匹配导出接口 - 不包含 data 字段
    const exportData: ExportBarcodeItem[] = selectedRows.value.map((row) => ({
      data: '', // 列表数据中没有 data，留空
      img: '', // 图片字段暂时留空
      info: row.info,
      name: row.name,
      type: getTypeString(row.type)
    }))

    const blob = await exportBarcodes(exportData)

    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute(
      'download',
      `条码数据_选中${selectedRows.value.length}条_${new Date().getTime()}.xlsx`
    )
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success(`成功导出 ${selectedRows.value.length} 条选中的数据`)
  } catch (error) {
    ElMessage.error('导出失败')
    console.error('导出失败:', error)
  } finally {
    exportLoading.value = false
  }
}

const handleExportAll = async () => {
  exportLoading.value = true
  try {
    // 获取所有数据 - 修复字段重复问题
    const queryParams: BarcodeQueryParams = {
      pageIndex: 1,
      pageSize: pagination.total,
      name: searchForm.name,
      info: searchForm.info,
      type: searchForm.type,
      data: searchForm.data
    }

    const response = await getBarcodeList(queryParams)
    if (response.code === 10000 && response.data) {
      const allData = response.data.rows || []

      // 转换数据格式以匹配导出接口 - 不包含 data 字段
      const exportData: ExportBarcodeItem[] = allData.map((row) => ({
        data: '', // 列表数据中没有 data，留空
        img: '', // 图片字段暂时留空
        info: row.info,
        name: row.name,
        type: getTypeString(row.type)
      }))

      const blob = await exportBarcodes(exportData)

      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `条码数据_全部${allData.length}条_${new Date().getTime()}.xlsx`)
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

      ElMessage.success(`成功导出 ${allData.length} 条数据`)
    } else {
      ElMessage.error('获取数据失败')
    }
  } catch (error) {
    ElMessage.error('导出失败')
    console.error('导出失败:', error)
  } finally {
    exportLoading.value = false
  }
}

const handleCloseBatchDialog = () => {
  batchDialogVisible.value = false
}

// 搜索处理
const handleSearch = () => {
  searchPopoverVisible.value = false
  pagination.pageIndex = 1
  loadBarcodeList()
  ElMessage.success('搜索完成')
}

// 重置搜索
const handleResetSearch = () => {
  searchForm.name = ''
  searchForm.info = ''
  searchForm.type = undefined
  searchForm.data = ''
  pagination.pageIndex = 1
  loadBarcodeList()
  ElMessage.info('已重置搜索条件')
}

// 清空输入框
const handleClearInput = (field: string) => {
  ;(searchForm as any)[field] = ''
  if (field === 'type') {
    ;(searchForm as any)[field] = undefined
  }
}

// 查看详情
const handleView = async (row: any) => {
  currentRow.value = row
  isEditing.value = false

  try {
    const response = await getBarcodeDetail(row.id)
    if (response.code === 10000 && response.data) {
      Object.assign(detailForm, response.data)

      // 确保其他弹框关闭
      addDialogVisible.value = false
      viewDialogVisible.value = false

      detailDialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取条码详情失败')
    }
  } catch (error) {
    console.error('获取条码详情失败:', error)
    ElMessage.error('获取条码详情失败')
  }
}

// 查看条码 - 修复图片显示问题
const handleViewDetail = async (row: any) => {
  try {
    // 重置图片状态
    barcodeImage.value = ''
    barcodeImageError.value = ''

    // 获取条码详情
    const detailResponse = await getBarcodeDetail(row.id)
    if (detailResponse.code === 10000 && detailResponse.data) {
      Object.assign(viewForm, detailResponse.data)

      // 获取条码图片
      try {
        const imageResponse = await getBarcodeImage(row.id)
        if (imageResponse.code === 10000 && imageResponse.data) {
          // 检查返回的数据格式
          const imageData = imageResponse.data

          // 如果已经是完整的data URL，直接使用
          if (imageData.startsWith('data:')) {
            barcodeImage.value = imageData
          }
          // 如果是纯base64字符串，添加前缀
          else if (imageData.startsWith('iVBORw0KGgo') || /^[A-Za-z0-9+/=]+$/.test(imageData)) {
            barcodeImage.value = `data:image/png;base64,${imageData}`
          }
          // 其他情况，可能是错误的格式
          else {
            console.warn('未知的图片数据格式:', imageData.substring(0, 50))
            barcodeImageError.value = '图片格式不支持'
          }
        } else {
          barcodeImageError.value = imageResponse.message || '获取条码图片失败'
          console.warn('获取条码图片失败:', imageResponse.message)
        }
      } catch (imageError) {
        console.error('获取条码图片异常:', imageError)
        barcodeImageError.value = '获取条码图片时发生异常'
      }

      // 确保其他弹框关闭
      addDialogVisible.value = false
      detailDialogVisible.value = false

      viewDialogVisible.value = true
    } else {
      ElMessage.error(detailResponse.message || '获取条码详情失败')
    }
  } catch (error) {
    console.error('查看条码失败:', error)
    ElMessage.error('查看条码失败')
  }
}

// 编辑详情
const handleEditDetail = () => {
  isEditing.value = true
}

// 保存详情（编辑）
const handleSaveDetail = async () => {
  if (!detailFormRef.value) return

  try {
    // 验证表单
    await detailFormRef.value.validate()

    saveLoading.value = true

    const response = await updateBarcode(detailForm)
    if (response.code === 10000) {
      ElMessage.success('保存成功')
      detailDialogVisible.value = false
      isEditing.value = false
      // 重新加载列表
      loadBarcodeList()
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

    const response = await addBarcode(addForm)
    if (response.code === 10000) {
      ElMessage.success('新增成功')
      addDialogVisible.value = false
      // 重新加载列表
      loadBarcodeList()
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

// 选择变化处理
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

// 关闭详情弹框
const handleCloseDetailDialog = () => {
  detailDialogVisible.value = false
  isEditing.value = false
  currentRow.value = null
}

// 关闭新增弹框
const handleCloseAddDialog = () => {
  addDialogVisible.value = false
}

// 关闭查看弹框
const handleCloseViewDialog = () => {
  viewDialogVisible.value = false
  barcodeImage.value = ''
  barcodeImageError.value = ''
}

// 正确定义 columns 类型
interface TableColumn {
  prop: string
  label: string
  width?: string
  minWidth?: string
  sortable?: boolean
  align?: 'left' | 'center' | 'right'
  slot?: string
}

// 使用 ref 定义 columns
const columns = ref<TableColumn[]>([
  {
    prop: 'name',
    label: '条码名称',
    width: '120',
    align: 'center'
  },
  {
    prop: 'info',
    label: '条码内容',
    width: '150'
  },
  {
    prop: 'type',
    label: '条码类型',
    width: '100',
    slot: 'type'
  }
])

// 删除数据函数
const handleDelete = async (rows: any[]) => {
  if (rows.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }

  ElMessageBox.confirm('确定要删除选中的数据吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  })
    .then(async () => {
      try {
        const ids = rows.map((row) => row.id)
        const response = await deleteBarcodes(ids)
        if (response.code === 10000) {
          ElMessage.success(`成功删除 ${rows.length} 条数据`)
          // 重新加载列表
          loadBarcodeList()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {
      // 用户取消删除
    })
}

const handlePageChange = (page: number, size: number) => {
  pagination.pageIndex = page
  pagination.pageSize = size
  loadBarcodeList()
}

// 组件挂载后加载数据
onMounted(() => {
  loadBarcodeList()
  console.log('页面已加载')
})
</script>

<style scoped>
/* 原有的CSS样式保持不变 */
.sys.area {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 操作栏样式 */
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

.custom-buttons {
  display: flex;
  gap: 8px;
}

.search-btn,
.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 批量按钮特殊样式 */
.batch-btn {
  background-color: #e6a23c;
  border-color: #e6a23c;
  color: white;
}

.batch-btn:hover {
  background-color: #d4882c;
  border-color: #d4882c;
}

/* 批量操作弹窗样式 */
.batch-operation {
  padding: 10px 0;
}

.operation-section {
  margin-bottom: 20px;
}

.operation-section h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

/* 导入信息样式 */
.import-info {
  background-color: #f8f9fa;
  border-radius: 6px;
  padding: 16px;
  margin-bottom: 16px;
  border-left: 4px solid #409eff;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
}

.info-item .el-icon {
  color: #409eff;
  margin-top: 2px;
  flex-shrink: 0;
}

.import-actions,
.export-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
}

.download-btn,
.upload-btn,
.export-selected-btn,
.export-all-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

.import-tips,
.export-tips {
  font-size: 12px;
  color: #909399;
  margin: 0;
  line-height: 1.4;
}

/* 搜索弹框样式 */
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

.search-action-btn,
.reset-action-btn {
  min-width: 80px;
}

/* 分割线样式 */
.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

/* 表格容器 - 占据剩余空间 */
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

.fallback-table {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

/* 确保表格组件内部也充分利用空间 */
:deep(.report-table-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.table-wrapper) {
  flex: 1;
  min-height: 0;
}

:deep(.el-table) {
  height: 100% !important;
}

:deep(.el-table__body-wrapper) {
  max-height: none !important;
}

/* 自定义操作按钮样式 */
.operation-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
}

.button-group {
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
}

.view-btn,
.detail-btn,
.delete-btn {
  width: 60px;
  height: 32px;
  padding: 0;
  font-size: 12px;
  background-color: #fff;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
  position: relative;
}

.view-btn {
  color: #67c23a;
}

.view-btn::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background-color: #dcdfe6;
}

.view-btn:hover {
  background-color: #f0f9eb;
}

.detail-btn {
  color: #409eff;
}

.detail-btn::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background-color: #dcdfe6;
}

.detail-btn:hover {
  background-color: #f5f7fa;
}

.delete-btn {
  color: #f56c6c;
}

.delete-btn:hover {
  background-color: #fef0f0;
}

/* 弹框样式优化 */
:deep(.el-dialog) {
  border-radius: 8px;
}

:deep(.el-dialog__header) {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #e4e7ed;
  margin-right: 0;
}

:deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-dialog__footer) {
  padding: 10px 20px 20px;
  border-top: 1px solid #e4e7ed;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 查看条码弹框样式 */
.barcode-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.barcode-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  width: 80px;
  font-weight: 500;
  color: #606266;
}

.info-item .value {
  flex: 1;
  color: #303133;
}

.barcode-preview {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 16px;
}

.preview-title {
  font-weight: 500;
  margin-bottom: 12px;
  color: #303133;
}

.preview-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 120px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.barcode-placeholder {
  color: #909399;
  font-size: 14px;
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

  .custom-buttons {
    flex-wrap: wrap;
    justify-content: center;
  }

  .search-popover {
    width: 280px !important;
  }

  .import-actions,
  .export-actions {
    flex-direction: column;
  }

  :deep(.el-dialog) {
    width: 90% !important;
    max-width: 400px;
  }
}

/* 美化滚动条 */
:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
