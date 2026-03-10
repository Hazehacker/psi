<template>
    <div class="frame area people-demo">
      <div class="layout">
        <!-- 搜索弹出框 -->
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
                <el-button type="primary" @click="applySearch">搜索</el-button>
              </div>
            </el-form>
          </div>
          <template #reference>
            <el-button :icon="MoreFilled" />
          </template>
        </el-popover>
        
        <!-- 操作按钮组 -->
        <el-button-group>
          <el-button type="danger" v-if="selection.length" @click="handleBatchDelete">删除</el-button>
          <el-button type="primary" @click="openDialogForAdd">新增</el-button>
          <el-button @click="openBatchDialog">批量</el-button>
          <el-button @click="handleRefresh">刷新</el-button>
        </el-button-group>
      </div>
  
      <el-divider />
  
      <!-- 人员数据表格 -->
      <el-table :data="filteredRows" height="calc(100% - 90px)" border @selection-change="onSelectionChange">
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
        <el-table-column prop="remark" label="备注信息" align="center" />
        <el-table-column label="相关操作" align="center" width="200" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="small" type="primary" @click="openDialogForEdit(row)">详情</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 分页组件 -->
      <el-pagination class="tablePagination" :current-page="page.current" :total="page.total" :page-size="page.size" :page-sizes="page.sizes" :pager-count="page.count" @size-change="onPageSizeChange" @current-change="onPageChange" layout="prev,pager,next,jumper,sizes,total" />
  
      <!-- 新增/编辑人员对话框 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" :close-on-click-modal="false">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基础资料" name="base">
              <!-- 基础资料表单 - 按照图片格式重新排列 -->
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
                      check-strictly 
                      placeholder="请选择所属组织"
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
  
      <!-- 批量操作对话框 -->
      <el-dialog v-model="batchVisible" title="批量" width="520px" :close-on-click-modal="false">
        <el-tabs v-model="batchTab">
          <el-tab-pane label="导入数据" name="import">
            <ul class="import-tip">
              <li>1. 下载模板并按要求录入数据。</li>
              <li>2. 上传编辑好的模板文件。</li>
            </ul>
            <el-divider />
            <el-row justify="space-between">
              <el-col :span="12"><el-button @click="downloadTemplate" type="info">下载模板</el-button></el-col>
              <el-col :span="12" style="text-align:right">
                <el-upload :action="'/people/import'" :show-file-list="false">
                  <el-button type="primary">上传模板</el-button>
                </el-upload>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="export-item" @click="exportSelected">
              <el-icon><Download /></el-icon>
              <p>导出数据</p>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, onMounted, computed } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { MoreFilled, Download } from '@element-plus/icons-vue'
  import type { FormInstance, FormRules } from 'element-plus'
  
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
  const activeTab = ref('base') // 当前激活的标签页
  
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
   * 初始化演示数据
   */
  const initDemo = () => {
    // 初始化组织树数据
    orgTree.value = [
      { 
        id: '0', 
        name: '默认组织', 
        children: [
          { 
            id: '1', 
            name: '公司1', 
            children: [
              { id: '11', name: '部门1' }
            ] 
          }
        ] 
      }
    ]
    
    // 初始化表格数据
    tableRows.value = [
      { 
        id: 'p1', 
        name: '张三', 
        number: 'NO001', 
        orgId: '1', 
        orgName: '公司1', 
        sex: 1, 
        tel: '13500000000', 
        address: '山西太原', 
        idCard: '1401************001', 
        remark: '演示人员' 
      }
    ]
    
    // 设置总记录数
    page.total = tableRows.value.length
  }
  
  /**
   * 计算属性：过滤后的表格数据
   */
  const filteredRows = computed(() => {
    return tableRows.value.filter(row => {
      // 根据搜索条件过滤数据
      const nameMatch = !searchForm.name || row.name?.includes(searchForm.name)
      const numberMatch = !searchForm.number || row.number?.includes(searchForm.number)
      const telMatch = !searchForm.tel || row.tel?.includes(searchForm.tel)
      const addrMatch = !searchForm.address || row.address?.includes(searchForm.address)
      const idCardMatch = !searchForm.idCard || row.idCard?.includes(searchForm.idCard)
      const remarkMatch = !searchForm.remark || row.remark?.includes(searchForm.remark)
      const sexMatch = searchForm.sex === '' || row.sex === searchForm.sex
      
      // 所有条件都匹配才显示
      return nameMatch && numberMatch && telMatch && addrMatch && idCardMatch && remarkMatch && sexMatch
    })
  })
  
  /**
   * 应用搜索条件
   */
  const applySearch = () => { 
    page.total = filteredRows.value.length
    page.current = 1
    ElMessage.success('筛选完成') 
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
    if (!selection.value.length) return
    tableRows.value = tableRows.value.filter(r => !selection.value.includes(r.id))
    page.total = tableRows.value.length
    ElMessage.success('删除成功(演示)') 
  }
  
  /**
   * 打开批量操作对话框
   */
  const openBatchDialog = () => { 
    batchVisible.value = true
    batchTab.value = 'import' 
  }
  
  /**
   * 下载导入模板
   */
   const downloadTemplate = () => { 
    // 模板文件应该存放在服务器上的路径
    window.open('/template/people-import.xlsx') 
  }
  
  /**
   * 导出选中的数据
   */
  const exportSelected = () => { 
    ElMessage.success('导出数据(演示)') 
  }
  
  /**
   * 打开新增人员对话框
   */
  const openDialogForAdd = () => { 
    dialogTitle.value = '新增人员'
    // 重置表单数据
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
    dialogVisible.value = true 
  }
  
  /**
   * 打开编辑人员对话框
   */
  const openDialogForEdit = (row: any) => { 
    dialogTitle.value = '详情'
    // 将行数据复制到表单中
    Object.assign(form, { ...row })
    dialogVisible.value = true 
  }
  
  /**
   * 提交表单（新增或编辑）
   */
  const handleSubmit = () => { 
    if (!formRef.value) return
    
    // 验证表单
    formRef.value.validate(valid => { 
      if (!valid) return
      
      if (form.id) {
        // 编辑模式：更新现有数据
        const idx = tableRows.value.findIndex(r => r.id === form.id)
        if (idx > -1) {
          tableRows.value[idx] = { ...tableRows.value[idx], ...form }
        }
        ElMessage.success('修改成功(演示)') 
      } else {
        // 新增模式：添加新数据
        tableRows.value.push({ 
          ...form, 
          id: Date.now().toString(), 
          orgName: '默认组织' 
        })
        ElMessage.success('新增成功(演示)') 
      }
      
      // 关闭对话框并更新总记录数
      dialogVisible.value = false
      page.total = tableRows.value.length
    }) 
  }
  
  /**
   * 删除单个人员
   */
  const handleDelete = (row: any) => { 
    ElMessageBox.confirm('确定要删除该人员吗？(演示)', '提示', { 
      type: 'warning' 
    }).then(() => { 
      tableRows.value = tableRows.value.filter(r => r.id !== row.id)
      page.total = tableRows.value.length
      ElMessage.success('删除成功(演示)') 
    }) 
  }
  
  /**
   * 刷新数据
   */
  const handleRefresh = () => { 
    // 重置搜索表单
    Object.assign(searchForm, { 
      name: '', 
      number: '', 
      sex: '' as any, 
      tel: '', 
      address: '', 
      idCard: '', 
      remark: '' 
    })
    ElMessage.success('刷新成功') 
  }
  
  /**
   * 分页大小变化事件
   */
  const onPageSizeChange = (size: number) => { 
    page.size = size 
  }
  
  /**
   * 页码变化事件
   */
  const onPageChange = (p: number) => { 
    page.current = p 
  }
  
  // 组件挂载后初始化数据
  onMounted(() => { 
    initDemo() 
  })
  </script>
  
  <style scoped lang="scss">
  .people-demo { 
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
  }
  
  .import-tip { 
    padding-left: 16px; 
    line-height: 1.8; 
  }
  
  .export-item { 
    display: flex; 
    align-items: center; 
    gap: 8px; 
    cursor: pointer; 
  }
  
  /* 基础资料表单布局 */
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
  </style>