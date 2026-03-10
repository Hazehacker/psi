<template>
  <div class="frame area people-manage">
    <div class="layout">
      <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="520">
        <div class="search-form-container">
          <el-form class="search-form" ref="searchFromRef" :model="searchForm">
            <div class="form-grid">
              <el-input placeholder="请输入人员名称" v-model="searchForm.name" clearable />
              <el-input placeholder="请输入人员编号" v-model="searchForm.number" clearable />
              <el-select v-model="searchForm.sex" placeholder="请选择人员性别" clearable>
                <el-option label="女" :value="0" />
                <el-option label="男" :value="1" />
              </el-select>
              <el-input placeholder="请输入联系电话" v-model="searchForm.tel" clearable />
              <el-input placeholder="请输入联系地址" v-model="searchForm.address" clearable />
              <el-input placeholder="请输入身份证号" v-model="searchForm.idCard" clearable />
              <el-input placeholder="请输入备注信息" v-model="searchForm.remark" clearable />
            </div>
            <el-divider />
            <div class="form-actions">
              <el-button @click="handleResetSearch">重置</el-button>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
            </div>
          </el-form>
        </div>
        <template #reference>
          <el-button :icon="MoreFilled" />
        </template>
      </el-popover>

      <el-button-group>
        <el-button type="danger" v-if="selection.length" @click="handleBatchDelete">删除</el-button>
        <el-button type="primary" @click="openDialogForAdd">新增</el-button>
        <el-button @click="openBatchDialog">批量</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
      </el-button-group>
    </div>

    <el-divider />

    <el-table :data="tableRows" height="calc(100% - 90px)" border @selection-change="onSelectionChange">
      <el-table-column type="selection" align="center" width="48" fixed="left" />
      <el-table-column prop="name" label="人员名称" align="center" width="140" />
      <el-table-column prop="number" label="人员编号" align="center" width="140" />
      <el-table-column prop="orgName" label="所属组织" align="center" width="160" />
      <el-table-column prop="sex" label="人员性别" align="center" width="100">
        <template #default="{ row }">{{ row.sex === 1 ? '男' : row.sex === 0 ? '女' : '' }}</template>
      </el-table-column>
      <el-table-column prop="tel" label="联系电话" align="center" width="160" />
      <el-table-column prop="address" label="联系地址" align="center" width="200" />
      <el-table-column prop="idCard" label="身份证号" align="center" width="200" />
      <el-table-column prop="remark" label="备注信息" align="center" width="200" />
      <el-table-column label="相关操作" align="center" width="200" fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="primary" @click="openDialogForEdit(row)">详情</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="tablePagination"
      :current-page="page.current"
      :total="page.total"
      :page-size="page.size"
      :page-sizes="page.sizes"
      :pager-count="page.count"
      @size-change="onPageSizeChange"
      @current-change="onPageChange"
      layout="prev,pager,next,jumper,sizes,total"
    />

    <!-- 详情 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基础资料" name="base">
            <!-- 基础资料表单 - 使用两列布局 -->
            <div class="form-layout-base">
              <!-- 左侧列 -->
              <div class="form-column">
                <!-- 人员名称 - 必填 -->
                <el-form-item label="人员名称" prop="name" class="required-field">
                  <el-input v-model="form.name" placeholder="请输入人员名称" />
                </el-form-item>
                
                <!-- 所属组织 - 必填 -->
                <el-form-item label="所属组织" prop="orgId" class="required-field">
                  <el-tree-select 
                    v-model="form.orgId" 
                    :data="orgTree" 
                    :props="treeSelectProps" 
                    placeholder="请选择所属组织"
                    check-strictly 
                  />
                </el-form-item>
                
                <!-- 联系电话 -->
                <el-form-item label="联系电话">
                  <el-input v-model="form.tel" placeholder="请输入联系电话" />
                </el-form-item>
                
                <!-- 身份证号 -->
                <el-form-item label="身份证号">
                  <el-input v-model="form.idCard" placeholder="请输入身份证号" />
                </el-form-item>
              </div>
              
              <!-- 右侧列 -->
              <div class="form-column">
                <!-- 人员编号 - 必填 -->
                <el-form-item label="人员编号" prop="number" class="required-field">
                  <el-input v-model="form.number" placeholder="请输入人员编号" />
                </el-form-item>
                
                <!-- 人员性别 -->
                <el-form-item label="人员性别">
                  <el-select v-model="form.sex" placeholder="请选择人员性别">
                    <el-option label="女" :value="0" />
                    <el-option label="男" :value="1" />
                  </el-select>
                </el-form-item>
                
                <!-- 联系地址 -->
                <el-form-item label="联系地址">
                  <el-input v-model="form.address" placeholder="请输入联系地址" />
                </el-form-item>
                
                <!-- 备注信息 -->
                <el-form-item label="备注信息">
                  <el-input v-model="form.remark" placeholder="请输入备注信息" />
                </el-form-item>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 批量（导入/导出） -->
    <el-dialog v-model="batchVisible" title="批量" width="520px" :close-on-click-modal="false">
      <el-tabs v-model="batchTab">
        <el-tab-pane label="导入数据" name="import">
          <ul class="import-tip">
            <li>1. 下载模板并按要求录入数据。</li>
            <li>2. 上传编辑好的模板文件。</li>
            <li class="highlight-important">请使用系统提供的标准模板，确保格式正确</li>
          </ul>
          
          <el-divider />
          
          <el-row justify="space-between">
            <el-col :span="12">
              <el-button @click="downloadTemplate" type="info">下载模板</el-button>
            </el-col>
            <el-col :span="12" style="text-align:right">
              <el-upload 
                :show-file-list="false" 
                :before-upload="beforeUpload"
                accept=".xlsx,.xls"
              >
                <el-button type="primary">上传模板</el-button>
              </el-upload>
            </el-col>
          </el-row>
        </el-tab-pane>
        
        <el-tab-pane label="导出数据" name="export">
          <div class="export-options">
            <!-- 导出选中数据 -->
            <div class="export-item" @click="exportSelected">
              <el-icon><Download /></el-icon>
              <div class="export-info">
                <p class="export-title">导出</p>
                <p class="export-desc">导出当前选中的 {{ selection.length }} 条数据</p>
              </div>
            </div>
            
            <el-divider />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { MoreFilled, Download, Document, Files } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { 
  getPersonnelList, 
  getPersonnelDetail, 
  addPersonnel, 
  modifyPersonnel, 
  deletePersonnel, 
  exportPersonnel,
  importPersonnel 
} from '@/apis/advancedSettings/people'
import { getOrganizationTree } from '@/apis/advancedSettings/organization'
import type { PersonnelAddDTO, PersonnelEditDTO, PeopleImportErrorDTO } from '@/apis/advancedSettings/people/type'

// 搜索表单数据
const searchForm = reactive({ 
  name: '', 
  number: '', 
  sex: '' as any, 
  tel: '', 
  address: '', 
  idCard: '', 
  remark: '' 
})

// 分页配置
const page = reactive({ 
  current: 1, 
  total: 0, 
  size: 30, 
  sizes: [30, 60, 90, 150, 300], 
  count: 5 
})

// 表格数据
const tableRows = ref<any[]>([])
// 选中的行
const selection = ref<string[]>([])

// 对话框相关状态
const dialogVisible = ref(false)
const dialogTitle = ref('新增人员')
const formRef = ref<FormInstance>()
const activeTab = ref('base')

// 表单数据
const form = reactive<any>({ 
  id: '', 
  name: '', 
  number: '', 
  orgId: '', 
  sex: 0, 
  tel: '', 
  address: '', 
  idCard: '', 
  remark: '', 
  more: {} 
})

// 组织树数据
const orgTree = ref<any[]>([])
// 树选择器配置
const treeSelectProps = { 
  value: 'id', 
  label: 'name', 
  children: 'children' 
}

// 批量操作对话框相关状态
const batchVisible = ref(false)
const batchTab = ref('import')

// 表单验证规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入人员名称', trigger: 'blur' }],
  number: [{ required: true, message: '请输入人员编号', trigger: 'blur' }],
  orgId: [{ required: true, message: '请选择所属组织', trigger: 'change' }]
}

/**
 * 获取组织树数据
 */
const fetchOrg = () => {
  getOrganizationTree().then(res => { 
    if (res.code === 10000) orgTree.value = res.data || [] 
  })
}

/**
 * 获取人员列表
 */
const fetchPeople = (goFirst?: number) => {
  if (goFirst === 1) page.current = 1
  
  const queryParams: any = { 
    pageIndex: page.current, 
    pageSize: page.size
  }
  
  // 使用正确的字段映射
  if (searchForm.name) queryParams.name = searchForm.name
  if (searchForm.number) queryParams.number = searchForm.number
  if (searchForm.sex !== '') queryParams.sex = String(searchForm.sex)
  if (searchForm.tel) queryParams.tel = searchForm.tel
  if (searchForm.address) queryParams.add = searchForm.address
  if (searchForm.idCard) queryParams.card = searchForm.idCard
  if (searchForm.remark) queryParams.data = searchForm.remark
  
  getPersonnelList(queryParams).then(res => {
    if (res.code === 10000 && res.data) {
      const rows: any[] = res.data.rows || []
      tableRows.value = rows.map((p: any) => ({
        id: p.id,
        name: p.name,
        number: p.number,
        sex: typeof p.sex === 'string' ? Number(p.sex) : p.sex,
        tel: p.tel,
        address: p.add,
        idCard: p.card,
        remark: p.data,
        orgName: p.frame || ''
      }))
      page.total = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取人员列表失败')
    }
  }).catch((error) => {
    console.error('获取人员列表失败:', error)
    ElMessage.error('获取人员列表失败: ' + (error.response?.data?.message || error.message))
  })
}
  
/**
 * 搜索人员
 */
const handleSearch = () => fetchPeople(1)

/**
 * 重置搜索条件
 */
const handleResetSearch = () => { 
  Object.assign(searchForm, { 
    name: '', 
    number: '', 
    sex: '' as any, 
    tel: '', 
    address: '', 
    idCard: '', 
    remark: '' 
  })
  fetchPeople(1) 
}

/**
 * 打开新增人员对话框
 */
const openDialogForAdd = () => { 
  dialogTitle.value = '新增人员'
  Object.assign(form, { 
    id: '', 
    name: '', 
    number: '', 
    orgId: '', 
    sex: 0, 
    tel: '', 
    address: '', 
    idCard: '', 
    remark: '', 
    more: {} 
  })
  activeTab.value = 'base'
  dialogVisible.value = true 
}

/**
 * 打开编辑人员对话框
 */
const openDialogForEdit = (row: any) => {
  dialogTitle.value = '详情'
  getPersonnelDetail(row.id).then(res => {
    if (res.code === 10000 && res.data) {
      const d: any = res.data
      //详情返回字段：add/card/data；
      Object.assign(form, {
        id: d.id,
        name: d.name,
        number: d.number,
        orgId: d.frame || '',
        sex: typeof d.sex === 'string' ? Number(d.sex) : (d.sex ?? 0),
        tel: d.tel || '',
        address: d.add || '',
        idCard: d.card || '',
        remark: d.data || '',
        more: d.more || {}
      })
      activeTab.value = 'base'
      dialogVisible.value = true
    } else { 
      ElMessage.error(res.message || '获取人员详情失败') 
    }
  }).catch(() => ElMessage.error('获取人员详情失败'))
}

/**
 * 提交表单（新增或编辑）
 */
const handleSubmit = () => {
  if (!formRef.value) return
  formRef.value.validate((valid) => {
    if (!valid) return
    if (form.id) {
      // 修改人员
      const updateData: PersonnelEditDTO = {
        id: form.id,
        name: form.name,
        number: form.number,
        frame: form.orgId || '',
        sex: String(form.sex),
        tel: form.tel || '',
        add: form.address || '',
        card: form.idCard || '',
        data: form.remark || ''
      }
      modifyPersonnel([updateData]).then(res => {
        if (res.code === 10000) { 
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchPeople() 
        } else { 
          ElMessage.error(res.message || '保存失败') 
        }
      }).catch(() => ElMessage.error('保存失败'))
    } else {
      // 新增人员
      const addData: PersonnelAddDTO = {
        name: form.name,
        number: form.number,
        frame: form.orgId || '',
        sex: String(form.sex),
        tel: form.tel || '',
        add: form.address || '',
        card: form.idCard || '',
        data: form.remark || ''
      }
      addPersonnel(addData).then(res => {
        if (res.code === 10000) { 
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchPeople() 
        } else { 
          ElMessage.error(res.message || '保存失败') 
        }
      }).catch(() => ElMessage.error('保存失败'))
    }
  })
}

/**
 * 删除单个人员
 */
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该人员吗？', '提示', { type: 'warning' })
    .then(() => deletePersonnel(row.id))
    .then(res => {
      if (res.code === 10000) { 
        ElMessage.success('删除成功')
        fetchPeople() 
      } else { 
        ElMessage.error(res.message || '删除失败') 
      }
    }).catch(() => {})
}

/**
 * 表格选中行变化事件
 */
const onSelectionChange = (rows: any[]) => { 
  selection.value = rows.map(r => r.id) 
}

/**
 * 批量删除选中的人员
 */
const handleBatchDelete = () => {
  if (!selection.value.length) { 
    ElMessage.warning('请选择要删除的数据')
    return 
  }
  ElMessageBox.confirm('您确定要删除选中数据吗？', '提示', { type: 'warning' })
    .then(() => deletePersonnel(selection.value.join(',')))
    .then(res => { 
      if (res.code === 10000) { 
        ElMessage.success('删除成功')
        fetchPeople() 
      } else { 
        ElMessage.error(res.message || '删除失败') 
      } 
    })
    .catch(() => {})
}

/**
 * 打开批量操作对话框
 */
const openBatchDialog = () => { 
  batchVisible.value = true
  batchTab.value = 'import' 
}

/**
 * 导入处理函数
 */
const handleImport = async (file: File) => {
  try {
    const formData = new FormData()
    formData.append('file', file)
    
    console.log('准备上传文件信息:', {
      name: file.name,
      size: file.size,
      type: file.type,
      lastModified: new Date(file.lastModified)
    })
    
    const res = await importPersonnel(formData)
    console.log('完整导入响应:', res)
    
    if (res.code === 10000) {
      ElMessage.success('导入成功')
      batchVisible.value = false
      fetchPeople(1)
      
      if (res.data) {
        const report = res.data
        const successCount = report.success || 0
        const failedCount = report.failed || 0
        
        if (failedCount > 0) {
          // 修复：确保传入的是数组，不是undefined
          showImportErrors(report.errors || [])
        } else {
          ElMessage.success(`导入完成：成功导入 ${successCount} 条数据`)
        }
      }
    } else {
      ElMessage.error(res.message || '导入失败')
    }
  } catch (error: any) {
    console.error('导入失败详情:', error)
    
    // 更详细的错误分析
    if (error.response) {
      console.error('响应数据:', error.response.data)
      console.error('响应状态:', error.response.status)
      console.error('响应头:', error.response.headers)
    }
    
    handleImportError(error)
  }
}

/**
 * 显示导入错误详情
 */
const showImportErrors = (errors: PeopleImportErrorDTO[]) => {
  let errorMessage = "导入过程中发现以下错误：\n\n"
  
  errors.forEach((error, index) => {
    if (index < 10) {
      const rowInfo = `第${error.row}行：`
      const fieldInfo = `${getFieldLabel(error.field || '')} `
      const valueInfo = `"${error.value}" `
      
      let solution = ""
      if (error.field === 'sex') {
        if (['男', 'male', 'Male', 'M'].includes(String(error.value))) {
          solution = "→ 请改为填写数字：1"
        } else if (['女', 'female', 'Female', 'F'].includes(String(error.value))) {
          solution = "→ 请改为填写数字：0"
        } else if (error.value === '-1') {
          solution = "→ 性别不能为空，请填写 0(女) 或 1(男)"
        } else {
          solution = "→ 请填写 0(女) 或 1(男)"
        }
      } else if (error.message?.includes('empty')) {
        // 修复：使用 error.message 而不是 message
        solution = "→ 该字段不能为空"
      } else if (error.message?.includes('format error')) {
        // 修复：使用 error.message 而不是 message
        solution = "→ 格式错误，请检查数据格式"
      }
      
      errorMessage += `${rowInfo}${fieldInfo}${valueInfo}${solution}\n`
    }
  })
  
  if (errors.length > 10) {
    errorMessage += `\n... 还有 ${errors.length - 10} 个错误未显示\n`
  }
  
  errorMessage += "\n请修改Excel文件中的上述错误后重新导入。"
  
  ElMessageBox.alert(errorMessage, '导入错误详情', {
    confirmButtonText: '确定',
    type: 'warning',
    customClass: 'import-error-dialog',
    dangerouslyUseHTMLString: false
  })
}

/**
 * 获取字段的中文标签
 */
const getFieldLabel = (field: string): string => {
  const fieldLabels: { [key: string]: string } = {
    'name': '人员名称',
    'number': '人员编号',
    'sex': '性别',
    'tel': '联系电话',
    'add': '联系地址',
    'card': '身份证号',
    'data': '备注信息',
    'frame': '所属组织'
  }
  return fieldLabels[field] || field
}

/**
 * 处理导入错误
 */
const handleImportError = (error: any) => {
  let errorMessage = '导入失败'
  
  if (error.response) {
    const errorData = error.response.data
    if (errorData && errorData.message) {
      errorMessage = `导入失败: ${errorData.message}`
      if (errorData.code === 9994) {
        errorMessage = '系统配置错误，无法处理上传的文件'
      }
    } else {
      errorMessage = `导入失败: 服务器错误 (${error.response.status})`
    }
  } else if (error.request) {
    errorMessage = '导入失败: 无法连接到服务器，请检查网络连接'
  } else {
    errorMessage = `导入失败: ${error.message}`
  }
  
  ElMessage.error(errorMessage)
}

/**
 * 上传前验证和处理
 */
const beforeUpload = (file: File) => {
  console.log('文件上传前验证:', file)
  
  // 检查文件类型
  const validTypes = [
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    'application/vnd.ms-excel.sheet.macroEnabled.12'
  ]
  const isExcel = validTypes.includes(file.type) || 
                  file.name.endsWith('.xlsx') || 
                  file.name.endsWith('.xls')
  
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件 (.xlsx, .xls)!')
    return false
  }
  
  // 检查文件大小 (10MB)
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  
  // 显示上传中提示
  ElMessage.info('开始上传文件...')
  
  // 手动处理上传
  handleImport(file)
  return false // 返回 false 阻止自动上传
}

/**
 * 下载模板 - 使用后端提供的Excel模板
 */
const downloadTemplate = () => { 
  // 直接使用后端提供的模板URL
  const templateUrl = '/template/people-import.xlsx'
  
  ElMessageBox.alert(`
模板下载说明：

1. 请使用系统提供的标准Excel模板
2. 严格按照模板中的格式填写数据
3. 不要修改表头行的字段名称
4. 确保所有必填字段都有值

点击确定开始下载模板文件。
  `, '下载模板', {
    confirmButtonText: '确定下载',
    customClass: 'template-download-dialog',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        // 创建隐藏的链接进行下载
        const link = document.createElement('a')
        link.href = templateUrl
        link.target = '_blank'
        link.download = '人员导入模板.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        
        ElMessage.success('模板下载开始，请稍候...')
      }
      done()
    }
  })
}
/**
 * 导出选中数据 - 直接调用，添加详细日志
 */
const exportSelected = async () => { 
  if (selection.value.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  try {
    console.log('=== 导出请求开始 ===')
    console.log('导出数据ID列表:', selection.value)
    console.log('请求参数:', { ids: selection.value })
    
    ElMessage.info('正在生成导出文件...')
    
    // 直接调用导出API，不经过安全导出函数
    const blob = await exportPersonnel({ ids: selection.value })
    
    console.log('=== 导出响应 ===')
    console.log('响应类型:', typeof blob)
    console.log('响应内容:', blob)
    console.log('Blob大小:', blob.size)
    console.log('Blob类型:', blob.type)
    
    // 创建下载链接
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    
    // 生成文件名
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-')
    link.download = `人员数据_${timestamp}.xlsx`
    
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
    
    console.log('=== 导出完成 ===')
    console.log('文件已下载:', link.download)
    
    ElMessage.success('导出成功')
  } catch (error: any) {
    console.error('=== 导出失败 ===')
    console.error('错误详情:', error)
    console.error('错误响应:', error.response)
    console.error('错误状态:', error.response?.status)
    console.error('错误数据:', error.response?.data)
    
    // 简化的错误处理
    let errorMessage = '导出失败'
    if (error.response?.data?.message) {
      errorMessage = `导出失败：${error.response.data.message}`
    } else if (error.message) {
      errorMessage = `导出失败：${error.message}`
    }
    
    ElMessage.error(errorMessage)
  }
}

// 移除 safeExport 函数，直接使用上面的代码

/**
 * 处理导出错误 - 简化版本
 */
const handleExportError = (error: any) => {
  console.error('导出错误处理:', error)
  let errorMessage = '导出失败'
  
  if (error.response?.data?.message) {
    errorMessage = `导出失败：${error.response.data.message}`
  } else if (error.message) {
    errorMessage = `导出失败：${error.message}`
  }
  
  ElMessage.error(errorMessage)
}

/**
 * 刷新数据
 */
const handleRefresh = () => { 
  handleResetSearch()
  ElMessage.success('刷新成功') 
}

/**
 * 分页大小变化事件
 */
const onPageSizeChange = (size: number) => { 
  page.size = size
  fetchPeople(1) 
}

/**
 * 页码变化事件
 */
const onPageChange = (p: number) => { 
  page.current = p
  fetchPeople() 
}

// 组件挂载后初始化数据
onMounted(() => { 
  fetchOrg()
  fetchPeople(1) 
})
</script>

<style scoped lang="scss">
.people-manage {
  .layout { 
    display: flex; 
    justify-content: space-between; 
  }
}

:deep(.search-popover) { 
  width: 520px !important; 
  padding: 16px; 
}

.search-form .form-grid { 
  display: grid; 
  grid-template-columns: 1fr 1fr; 
  gap: 12px; 
}

.form-actions { 
  display: flex; 
  gap: 12px; 
  justify-content: flex-end;
}

.import-tip { 
  padding-left: 16px; 
  line-height: 1.8; 
  color: #606266;
  font-size: 14px;
}

/* 高亮重要提示 */
.highlight-important {
  color: #e6a23c;
  font-weight: bold;
  background-color: #fdf6ec;
  padding: 8px;
  border-radius: 4px;
  margin: 10px 0;
  border-left: 4px solid #e6a23c;
}

.export-options {
  .export-item {
    display: flex;
    align-items: center;
    gap: 12px;
    cursor: pointer;
    padding: 16px;
    border-radius: 8px;
    transition: all 0.3s ease;
    border: 1px solid #e4e7ed;
    
    &:hover {
      background-color: #f5f7fa;
      border-color: #409eff;
      transform: translateY(-2px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
    
    .el-icon {
      font-size: 24px;
      color: #409eff;
    }
    
    .export-info {
      flex: 1;
      
      .export-title {
        font-weight: 600;
        color: #303133;
        margin: 0 0 4px 0;
        font-size: 14px;
      }
      
      .export-desc {
        color: #909399;
        margin: 0;
        font-size: 12px;
      }
    }
  }
}

/* 基础资料表单布局 - 两列布局 */
.form-layout-base {
  display: flex;
  gap: 40px; /* 左右两列之间的间距 */
  justify-content: space-between;
}

/* 表单列样式 */
.form-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px; /* 表单项之间的垂直间距 */
}

/* 表单项样式 */
.form-layout-base .el-form-item {
  margin-bottom: 0; /* 移除默认底部边距，由父级gap控制 */
  display: flex;
  flex-direction: column;
}

/* 必填字段标签样式 */
.form-layout-base .required-field :deep(.el-form-item__label)::before {
  content: '*';
  color: #F56C6C;
  margin-right: 4px;
}

/* 标签样式 */
.form-layout-base .el-form-item :deep(.el-form-item__label) {
  text-align: left;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

/* 输入框和选择器样式 */
.form-layout-base .el-input,
.form-layout-base .el-select,
.form-layout-base .el-tree-select {
  width: 100%;
}

/* 调整树选择器样式 */
.form-layout-base .el-tree-select :deep(.el-select) {
  width: 100%;
}

/* 调整对话框内边距 */
:deep(.el-dialog__body) {
  padding: 20px;
}

/* 调整标签页样式 */
:deep(.el-tabs) {
  margin-top: -10px;
}

/* 调整标签页内容区域样式 */
:deep(.el-tab-pane) {
  padding-top: 16px;
}

/* 表格样式优化 */
:deep(.el-table) {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

/* 分页样式优化 */
.tablePagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

/* 导入错误对话框样式 */
:deep(.import-error-dialog) {
  .el-message-box__content {
    white-space: pre-line;
    max-height: 400px;
    overflow-y: auto;
    font-family: 'Courier New', monospace;
  }
}

/* 模板下载对话框样式 */
:deep(.template-download-dialog) {
  .el-message-box__content {
    white-space: pre-line;
    line-height: 1.6;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .export-options {
    .export-item {
      padding: 12px;
      
      .el-icon {
        font-size: 20px;
      }
    }
  }
}
</style>