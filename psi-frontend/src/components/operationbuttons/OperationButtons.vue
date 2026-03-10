<!-- 主按钮组件 -->
<template>
  <div class="flex gap-2 justify-end">
    <el-button v-if="showAdd" type="primary" @click="openAddDialog">新增</el-button>

    <el-button v-if="showBatch" type="warning" @click="openBatchDialog">批量</el-button>

    <el-button v-if="showRefresh" type="info" @click="onRefresh">刷新</el-button>

    <!-- 新增弹窗 -->
    <AddDialog v-model:visible="addDialogVisible" />
    
    <!-- 批量操作弹窗 -->
    <el-dialog title="批量" v-model="batchDialogVisible" width="600px" :show-close="true">
      <div class="batch-content">
        <el-tabs v-model="activeTab" class="batch-tabs">
          <el-tab-pane label="导入数据" name="import">
            <div class="tab-content">
              <div class="tips-list">
                <div class="tip-item">
                  <span class="tip-number">1.</span>
                  <span class="tip-text">该功能适用于批量导入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">2.</span>
                  <span class="tip-text">您需要下载数据模板后使用Excel录入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">3.</span>
                  <span class="tip-text">录入数据时，请勿修改首行数据标题以及排序。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">4.</span>
                  <span class="tip-text">请查阅使用文档获取字段格式内容以及相关导入须知。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">5.</span>
                  <span class="tip-text">点击下方上传模板，选择您编辑好的模板文件即可。</span>
                </div>
              </div>
              <div class="button-group">
                <el-button class="download-btn" @click="downloadTemplate">下载模板</el-button>
                <el-upload
                  ref="uploadRef"
                  :auto-upload="false"
                  :show-file-list="false"
                  accept=".xlsx,.xls"
                  :on-change="handleFileChange"
                  :disabled="uploading"
                  style="display: inline-block;"
                >
                  <el-button type="primary" class="upload-btn" :loading="uploading">
                    {{ uploading ? '上传中...' : '上传模板' }}
                  </el-button>
                </el-upload>
              </div>
              
              <!-- 上传数据预览 -->
              <div v-if="uploadedData.length > 0" class="upload-preview">
                <h4>上传数据预览 ({{ uploadedData.length }} 条记录)</h4>
                <el-table :data="uploadedData.slice(0, 5)" border style="width: 100%; margin-top: 10px;">
                  <el-table-column 
                    v-for="col in matchedColumns" 
                    :key="col.prop"
                    :prop="col.prop" 
                    :label="col.label" 
                    :width="col.width"
                  />
                </el-table>
                <div v-if="uploadedData.length > 5" class="preview-note">
                  仅显示前5条记录，共{{ uploadedData.length }}条
                </div>
                <div class="preview-actions">
                  <el-button type="primary" @click="confirmImport">确认导入</el-button>
                  <el-button @click="clearUploadedData">取消</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="tab-content">
              <div class="export-content">
                <el-button type="primary" class="export-btn" @click="exportSimpleReport">导出简单报表</el-button>
                <el-button type="success" class="export-btn" @click="exportDetailReport">导出详细报表</el-button>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, type Ref, withDefaults } from 'vue'
import { utils, writeFileXLSX, read } from 'xlsx'
import { ElMessage } from 'element-plus'
import type { UploadFile, UploadFiles, UploadInstance } from 'element-plus'
import AddDialog from './AddDialog.vue'

// 定义列配置类型
interface TableColumn {
  prop: string
  label: string
  type?: string
  width?: number | string
}

// 接收父组件传入的按钮显示控制
// 使用对象语法，这样 Vue 会自动将 kebab-case 转换为 camelCase
const props = withDefaults(defineProps<{
  showAdd?: boolean
  showBatch?: boolean
  showRefresh?: boolean
  // 导出数据相关
  exportData?: any[]
  exportFileName?: string
  // 导出详细报表相关
  exportDetailData?: any[]
  exportDetailFileName?: string
  // 表格列配置 - 用于生成模板
  tableColumns?: TableColumn[]
  // 模板文件名
  templateFileName?: string
  // 导入API函数 - 如果提供，将直接上传文件到接口；否则解析Excel后emit数据
  importApi?: ((file: File) => Promise<any>) | null
  // 导出简单报表API函数 - 如果提供，将调用接口导出；否则使用本地数据导出
  // Vue 会自动将 kebab-case (export-simple-report-api) 转换为 camelCase (exportSimpleReportApi)
  exportSimpleReportApi?: ((orderId: string | number) => Promise<any>) | null
  // 选中的订单ID（用于导出简单报表）
  // Vue 会自动将 kebab-case (selected-order-ids) 转换为 camelCase (selectedOrderIds)
  selectedOrderIds?: (string | number)[]
  // 导出详细报表API函数 - 如果提供，将调用接口导出；否则使用本地数据导出
  // Vue 会自动将 kebab-case (export-detail-report-api) 转换为 camelCase (exportDetailReportApi)
  exportDetailReportApi?: ((ids: (string | number)[]) => Promise<any>) | null
}>(), {
  showAdd: true,
  showBatch: true,
  showRefresh: true,
  exportData: () => [],
  exportFileName: '数据导出',
  exportDetailData: () => [],
  exportDetailFileName: '详细报表导出',
  tableColumns: () => [],
  templateFileName: '数据导入模板',
  importApi: null,
  exportSimpleReportApi: null,
  selectedOrderIds: () => [],
  exportDetailReportApi: null
})

// 定义事件
const emit = defineEmits(['add', 'batch', 'refresh', 'export', 'export-detail', 'import-data'])

// 控制弹窗
const addDialogVisible = ref(false)
const batchDialogVisible = ref(false)
const activeTab = ref<'import' | 'export'>('import')

// 上传相关数据
const uploadRef = ref<UploadInstance | null>(null)
const uploadedData = ref<any[]>([])
const matchedColumns = ref<TableColumn[]>([])
const uploading = ref(false)
const currentFile = ref<File | null>(null)

const openAddDialog = () => {
  addDialogVisible.value = true
  emit('add')
}

// 打开批量操作弹窗
const openBatchDialog = () => {
  batchDialogVisible.value = true
  activeTab.value = 'import' // 默认显示导入标签页
  emit('batch')
}

// 关闭批量操作弹窗
const closeBatchDialog = () => {
  batchDialogVisible.value = false
}

// 下载模板功能 - 根据表格组件列配置生成模板
const downloadTemplate = () => {
  try {
    // 获取表格列配置 - 从父组件或props传入
    const tableColumns = props.tableColumns
    
    if (tableColumns.length === 0) {
      console.warn('未找到表格列配置，使用默认模板')
      // 如果没有表格列配置，使用默认模板
      downloadDefaultTemplate()
      return
    }
    
    // 根据表格列配置生成模板数据
    const templateData = generateTemplateFromColumns(tableColumns)
    
    // 创建工作表
    const ws = utils.json_to_sheet(templateData)
    
    // 根据列配置设置列宽
    const colWidths = tableColumns.map((col: TableColumn) => {
      let widthValue: number | undefined
      if (col.width) {
        if (typeof col.width === 'string') {
          // 如果是字符串，提取数字部分（如 "100px" -> 100）
          const numMatch = col.width.match(/\d+/)
          widthValue = numMatch ? parseInt(numMatch[0], 10) : undefined
        } else {
          widthValue = col.width
        }
      }
      return {
        wch: widthValue ? Math.max(widthValue / 8, 10) : 15 // 将px转换为字符宽度，最小10
      }
    })
    ws['!cols'] = colWidths
    
    // 创建工作簿
    const wb = utils.book_new()
    utils.book_append_sheet(wb, ws, '数据导入模板')
    
    // 导出模板文件
    const fileName = `${props.templateFileName}.xlsx`
    writeFileXLSX(wb, fileName)
    
    console.log('模板下载成功:', fileName)
  } catch (error) {
    console.error('模板下载失败:', error)
  }
}

// 根据表格列配置生成模板数据
const generateTemplateFromColumns = (columns: TableColumn[]): any[] => {
  // 生成表头行
  const headerRow: Record<string, string> = {}
  columns.forEach((col: TableColumn) => {
    if (col.prop && col.label) {
      headerRow[col.prop] = col.label
    }
  })
  
  // 生成示例数据行（最多3行）
  const exampleRows: Record<string, string>[] = []
  for (let i = 1; i <= 3; i++) {
    const row: Record<string, string> = {}
    columns.forEach((col: TableColumn) => {
      if (col.prop) {
        // 根据列类型生成示例数据
        row[col.prop] = generateExampleValue(col, i)
      }
    })
    exampleRows.push(row)
  }
  
  return [headerRow, ...exampleRows]
}

// 根据列配置生成示例值
const generateExampleValue = (column: TableColumn, index: number): string => {
  const { prop, type, label } = column
  
  // 根据列类型和名称生成合适的示例数据
  if (type === 'date' || prop.includes('date') || prop.includes('Date')) {
    return `2024-0${index}-0${index}`
  } else if (type === 'number' || prop.includes('id') || prop.includes('Id')) {
    return index.toString()
  } else if (prop.includes('phone') || prop.includes('Phone') || prop.includes('tel')) {
    return `1380013800${index}`
  } else if (prop.includes('email') || prop.includes('Email')) {
    return `user${index}@example.com`
  } else if (prop.includes('name') || prop.includes('Name') || prop.includes('姓名')) {
    return ['张三', '李四', '王五'][index - 1] || `示例${index}`
  } else if (prop.includes('status') || prop.includes('Status') || prop.includes('状态')) {
    return ['启用', '禁用', '待审核'][index - 1] || '示例状态'
  } else {
    return `示例数据${index}`
  }
}

// 默认模板（当没有表格列配置时使用）
const downloadDefaultTemplate = () => {
  const defaultData = [
    {
      '序号': '1',
      '名称': '示例数据1',
      '状态': '启用',
      '创建时间': '2024-01-01',
      '备注': '示例数据'
    },
    {
      '序号': '2',
      '名称': '示例数据2',
      '状态': '启用',
      '创建时间': '2024-01-02',
      '备注': '示例数据'
    },
    {
      '序号': '3',
      '名称': '示例数据3',
      '状态': '启用',
      '创建时间': '2024-01-03',
      '备注': '示例数据'
    }
  ]
  
  const ws = utils.json_to_sheet(defaultData)
  const wb = utils.book_new()
  utils.book_append_sheet(wb, ws, '数据导入模板')
  
  const fileName = '数据导入模板.xlsx'
  writeFileXLSX(wb, fileName)
  console.log('默认模板下载成功:', fileName)
}

// 处理文件上传
const handleFileChange = async (file: UploadFile) => {
  if (!file.raw) {
    console.error('文件对象为空')
    return
  }
  
  // 如果提供了 importApi 函数，直接上传文件到接口
  if (props.importApi && typeof props.importApi === 'function') {
    uploading.value = true
    currentFile.value = file.raw
    
    try {
      // 调用导入API函数
      const result = await props.importApi(file.raw)
      
      // 上传成功
      console.log('文件上传成功:', result)
      
      // 触发导入成功事件
      emit('import-data', { success: true, result, file: file.raw })
      
      // 清空文件并关闭弹窗
      clearUploadedData()
      batchDialogVisible.value = false
      
      return
    } catch (error) {
      console.error('文件上传失败:', error)
      // 触发导入失败事件
      emit('import-data', { success: false, error, file: file.raw })
      // 不清空文件，让用户可以重试
      uploading.value = false
      return
    } finally {
      uploading.value = false
    }
  }
  
  // 如果没有提供 importApi，使用原有的解析Excel逻辑
  const reader = new FileReader()
  reader.onload = (e: ProgressEvent<FileReader>) => {
    try {
      if (!e.target?.result) {
        console.error('文件读取结果为空')
        return
      }
      const data = new Uint8Array(e.target.result as ArrayBuffer)
      const workbook = read(data, { type: 'array' })
      const sheetName = workbook.SheetNames[0]
      const worksheet = workbook.Sheets[sheetName]
      
      // 将Excel数据转换为JSON
      const jsonData = utils.sheet_to_json(worksheet, { header: 1 }) as any[][]
      
      if (jsonData.length === 0) {
        console.warn('Excel文件为空')
        return
      }
      
      // 处理数据
      processUploadedData(jsonData)
      
    } catch (error) {
      console.error('Excel文件解析失败:', error)
    }
  }
  reader.readAsArrayBuffer(file.raw)
}

// 处理上传的数据
const processUploadedData = (jsonData: any[][]) => {
  if (jsonData.length < 2) {
    console.warn('Excel数据不足，至少需要表头和数据行')
    return
  }
  
  // 第一行作为表头
  const headers = jsonData[0]
  const dataRows = jsonData.slice(1)
  
  // 获取表格列配置
  const tableColumns = props.tableColumns
  
  if (tableColumns.length === 0) {
    console.warn('未找到表格列配置，无法匹配数据')
    return
  }
  
  // 创建列映射关系 - 根据label匹配
  const columnMapping: Record<string, number> = {}
  const matchedCols: TableColumn[] = []
  
  tableColumns.forEach((col: TableColumn) => {
    if (col.prop && col.label) {
      // 在Excel表头中查找匹配的列
      const matchedIndex = headers.findIndex((header: any) => 
        header && header.toString().trim() === col.label.trim()
      )
      
      if (matchedIndex !== -1) {
        columnMapping[col.prop] = matchedIndex
        matchedCols.push({
          prop: col.prop,
          label: col.label,
          width: col.width || 120
        })
      }
    }
  })
  
  if (matchedCols.length === 0) {
    console.warn('未找到匹配的列，请检查Excel表头是否与模板一致')
    return
  }
  
  // 提取匹配的数据
  const processedData: Record<string, any>[] = []
  dataRows.forEach((row: any[], index: number) => {
    if (row && row.some((cell: any) => cell !== undefined && cell !== null && cell !== '')) {
      const rowData: Record<string, any> = {}
      matchedCols.forEach((col: TableColumn) => {
        const colIndex = columnMapping[col.prop]
        if (colIndex !== undefined && row[colIndex] !== undefined) {
          rowData[col.prop] = row[colIndex]
        }
      })
      
      // 只添加有数据的行
      if (Object.keys(rowData).length > 0) {
        processedData.push(rowData)
      }
    }
  })
  
  // 更新数据
  uploadedData.value = processedData
  matchedColumns.value = matchedCols
  
  console.log(`成功解析Excel数据: ${processedData.length} 条记录，匹配 ${matchedCols.length} 个列`)
}

// 确认导入数据
const confirmImport = () => {
  if (uploadedData.value.length === 0) {
    console.warn('没有可导入的数据')
    return
  }
  
  // 触发父组件的导入事件
  emit('import-data', uploadedData.value)
  
  // 清空上传数据
  clearUploadedData()
  
  // 关闭弹窗
  batchDialogVisible.value = false
  
  console.log('数据导入成功:', uploadedData.value.length, '条记录')
}

// 清空上传数据
const clearUploadedData = () => {
  uploadedData.value = []
  matchedColumns.value = []
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 下载文件的辅助函数
const downFile = (blob: any, filename: string) => {
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  link.download = filename
  link.style.display = 'none'
  document.body.appendChild(link)
  link.click()
  window.URL.revokeObjectURL(link.href)
  document.body.removeChild(link)
}

// 导出简单报表
const exportSimpleReport = async () => {
  emit('export')
  
  console.log('🔍 [导出简单报表] 函数被调用')
  console.log('🔍 [导出简单报表] props.exportSimpleReportApi:', props.exportSimpleReportApi)
  console.log('🔍 [导出简单报表] props.selectedOrderIds:', props.selectedOrderIds)
  console.log('🔍 [导出简单报表] typeof exportSimpleReportApi:', typeof props.exportSimpleReportApi)
  
  // 检查是否提供了 API 函数
  const exportApi = props.exportSimpleReportApi
  
  // 如果提供了 API 函数，使用 API 导出
  if (exportApi && typeof exportApi === 'function') {
    console.log('🔍 [导出简单报表] 使用 API 导出模式')
    // 获取选中的订单ID
    const orderIds = props.selectedOrderIds || []
    
    // 检查是否有选中的订单
    if (!orderIds || orderIds.length === 0) {
      ElMessage.warning('请先选择要导出的订单')
      return
    }
    
    // 只导出第一个选中的订单
    const orderId = orderIds[0]
    
    console.log('🔍 [导出简单报表] 开始导出，orderId:', orderId)
    console.log('🔍 [导出简单报表] exportApi:', exportApi)
    
    try {
      // 调用API函数
      const result = await exportApi(orderId)
      
      // 处理文件下载
      if (result && result.data) {
        const headers = result.headers || {}
        const contentType = headers['content-type'] || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        const blob = new Blob([result.data], { type: contentType })
        
        // 从响应头获取文件名，如果没有则使用默认文件名
        const contentDisposition = headers['content-disposition']
        let filename = props.exportFileName || '简单报表导出.xlsx'
        if (contentDisposition) {
          try {
            const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
            if (filenameMatch && filenameMatch[1]) {
              filename = decodeURIComponent(filenameMatch[1].replace(/['"]/g, ''))
            }
          } catch (e) {
            console.warn('解析文件名失败，使用默认文件名', e)
          }
        }
        
        downFile(blob, filename)
        ElMessage.success('导出成功')
      } else {
        console.error('❌ [导出简单报表] 响应数据为空或格式不正确')
        ElMessage.error('导出失败：响应数据为空或格式不正确')
      }
    } catch (error: any) {
      console.error('❌ [导出简单报表] 导出失败:', error)
      ElMessage.error(error.message || '导出失败，请稍后重试')
    }
    return
  }
  
  // 如果没有提供 API 函数，使用本地数据导出（参考 Excel.vue 的实现方式）
  console.log('🔍 [导出简单报表] 使用本地数据导出模式')
  console.log('🔍 [导出简单报表] props.exportData:', props.exportData)
  try {
    // 使用传入的导出数据，如果没有则提示
    const dataToExport = props.exportData && props.exportData.length > 0 ? props.exportData : []
    
    if (dataToExport.length === 0) {
      console.warn('⚠️ [导出简单报表] 没有可导出的数据，exportApi:', exportApi)
      ElMessage.warning('没有可导出的数据')
      return
    }
    
    // 参考 Excel.vue 的导出方式
    const ws = utils.json_to_sheet(dataToExport)
    const wb = utils.book_new()
    utils.book_append_sheet(wb, ws, 'Data')
    
    const fileName = `${props.exportFileName || '简单报表导出'}.xlsx`
    writeFileXLSX(wb, fileName)
    
    ElMessage.success('导出成功')
    console.log('简单报表导出成功:', fileName)
  } catch (error: any) {
    console.error('导出失败:', error)
    ElMessage.error(error.message || '导出失败，请稍后重试')
  }
}

// 导出详细报表
const exportDetailReport = async () => {
  emit('export-detail')
  
  // 如果提供了导出详细报表API函数，优先使用API导出
  const exportApi = props.exportDetailReportApi
  if (exportApi && typeof exportApi === 'function') {
    console.log('🔍 [导出详细报表] 使用API导出模式')
    console.log('🔍 [导出详细报表] selectedOrderIds:', props.selectedOrderIds)
    
    // 检查是否有选中的订单ID
    if (!props.selectedOrderIds || props.selectedOrderIds.length === 0) {
      ElMessage.warning('请先选择要导出的订单')
      return
    }
    
    try {
      // 调用API导出详细报表，传递选中的订单ID数组
      const result = await exportApi(props.selectedOrderIds)
      
      console.log('🔍 [导出详细报表] API调用成功，result:', result)
      
      // 处理文件下载
      if (result && result.data) {
        const headers = result.headers || {}
        const contentType = headers['content-type'] || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        const blob = new Blob([result.data], { type: contentType })
        
        // 从响应头获取文件名，如果没有则使用默认文件名
        const contentDisposition = headers['content-disposition']
        let filename = props.exportDetailFileName || '详细报表导出.xlsx'
        if (contentDisposition) {
          try {
            const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
            if (filenameMatch && filenameMatch[1]) {
              filename = decodeURIComponent(filenameMatch[1].replace(/['"]/g, ''))
            }
          } catch (e) {
            console.warn('解析文件名失败，使用默认文件名', e)
          }
        }
        
        downFile(blob, filename)
        ElMessage.success('导出成功')
      } else {
        console.error('❌ [导出详细报表] 响应数据为空或格式不正确')
        ElMessage.error('导出失败：响应数据为空或格式不正确')
      }
    } catch (error: any) {
      console.error('❌ [导出详细报表] 导出失败:', error)
      ElMessage.error(error.message || '导出失败，请稍后重试')
    }
    return
  }
  
  // 如果没有提供 API 函数，使用本地数据导出（参考 Excel.vue 的实现方式）
  console.log('🔍 [导出详细报表] 使用本地数据导出模式')
  console.log('🔍 [导出详细报表] props.exportDetailData:', props.exportDetailData)
  try {
    // 使用传入的详细报表数据，如果没有则提示
    const dataToExport = props.exportDetailData && props.exportDetailData.length > 0 
      ? props.exportDetailData 
      : []
    
    if (dataToExport.length === 0) {
      console.warn('⚠️ [导出详细报表] 没有可导出的数据，exportApi:', exportApi)
      ElMessage.warning('没有可导出的数据')
      return
    }
    
    // 参考 Excel.vue 的导出方式
    const ws = utils.json_to_sheet(dataToExport)
    const wb = utils.book_new()
    utils.book_append_sheet(wb, ws, 'Data')
    
    const fileName = `${props.exportDetailFileName || '详细报表导出'}.xlsx`
    writeFileXLSX(wb, fileName)
    
    ElMessage.success('导出成功')
    console.log('详细报表导出成功:', fileName)
  } catch (error: any) {
    console.error('导出失败:', error)
    ElMessage.error(error.message || '导出失败，请稍后重试')
  }
}

// 刷新当前页面
const onRefresh = () => {
  emit('refresh')
}
</script>

<style scoped>
.flex {
  display: flex;
  align-items: center;
}
.gap-2 {
  gap: 8px;
}

.batch-content {
  padding: 0;
}

.batch-tabs {
  margin-top: 0;
}

.tab-content {
  padding: 20px 0;
}

.tips-list {
  margin-bottom: 30px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  line-height: 1.5;
}

.tip-number {
  color: #409eff;
  font-weight: 600;
  margin-right: 8px;
  min-width: 20px;
}

.tip-text {
  color: #333;
  font-size: 14px;
  flex: 1;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 20px;
}

.download-btn {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  color: #333;
  min-width: 100px;
}

.upload-btn {
  min-width: 100px;
}

.export-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  min-height: 200px;
}

.export-btn {
  min-width: 120px;
  height: 40px;
  font-size: 16px;
}

/* 上传预览样式 */
.upload-preview {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.upload-preview h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 14px;
  font-weight: 600;
}

.preview-note {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.preview-actions {
  margin-top: 15px;
  text-align: center;
}

.preview-actions .el-button {
  margin: 0 5px;
}

/* 标签页样式调整 */
:deep(.el-tabs__header) {
  margin: 0 0 20px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: #e4e7ed;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
}

:deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
}
</style>
