<!--
 * @Description: 组织机构管理演示页面
-->
<template>
  <div class="frame area organization-demo">
    <div class="layout">
      <el-button-group>
        <el-button @click="handleAdd">新增</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
      </el-button-group>
    </div>
    <el-divider></el-divider>
    
    <el-table 
      :data="tableData.rows" 
      height="calc(100% - 60px)" 
      :tree-props="{ children: 'children' }" 
      row-key="id" 
      border
    >
      <el-table-column prop="name" label="组织名称" width="200px"></el-table-column>
      <el-table-column prop="sort" label="组织排序" align="center" width="200px"></el-table-column>
      <el-table-column prop="remark" label="备注信息" align="center" width="200px"></el-table-column>
      <el-table-column prop="operate" label="相关操作" align="center" width="160px">
        <template #default="scope">
          <el-button-group>
            <el-button @click="handleDetail(scope.row)" size="small">详情</el-button>
            <el-button 
              @click="handleDelete(scope.row)" 
              size="small"
            >
              删除
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="420px"
      :close-on-click-modal="false"
    >
      <transition name="el-fade-in">
        <div v-if="dialogVisible">
          <el-form 
            :model="dialogData" 
            :rules="formRules" 
            ref="formRef" 
            label-width="80px"
          >
            <el-form-item label="组织名称" prop="name">
              <el-input 
                placeholder="请输入组织名称" 
                v-model="dialogData.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="所属组织" prop="parentOrg">
              <!-- 将级联选择器改为树形选择器 -->
              <el-tree-select
                v-model="dialogData.parentOrg"
                :data="treeSelectOptions"
                :props="treeSelectProps"
                placeholder="请选择所属组织"
                style="width: 100%"
                check-strictly
                :render-after-expand="false"
              />
            </el-form-item>
            <el-form-item label="组织排序" prop="sort">
              <el-input 
                placeholder="请输入组织排序" 
                v-model.number="dialogData.sort"
                type="number"
              ></el-input>
            </el-form-item>
            <el-form-item label="备注信息">
              <el-input 
                placeholder="请输入备注信息" 
                v-model="dialogData.remark"
              ></el-input>
            </el-form-item>
          </el-form>
        </div>
      </transition>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="handleConfirm" type="primary">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 演示数据
const demoData = [
  { id: '1', name: 'NTJK', sort: 0, remark: '', parentId: '0', hasChildren: true },
  { id: '2', name: '超级管理员', sort: 0, remark: '', parentId: '1', hasChildren: true },
  { id: '3', name: '法师', sort: 0, remark: '', parentId: '1', hasChildren: false },
  { id: '4', name: '公司1', sort: 0, remark: '44', parentId: '1', hasChildren: false }
]

type OrgTreeItem = {
  id: string; name: string; sort: number; remark?: string; parentId?: string;
  hasChildren?: boolean;
  children?: OrgTreeItem[]
}

// 数据存储相关
const STORAGE_KEY = 'organization_data'

// 获取存储的数据
const getStoredData = (): OrgTreeItem[] => {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
      return JSON.parse(stored)
    }
  } catch (error) {
    console.error('读取存储数据失败:', error)
  }
  return [...demoData] // 返回 demoData 的副本
}

// 保存数据
const saveData = (data: OrgTreeItem[]) => {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch (error) {
    console.error('保存数据失败:', error)
  }
}

// 构建树形数据函数
function buildTree(list: OrgTreeItem[], parentId = '0'): OrgTreeItem[] {
  const children = list.filter(item => (item.parentId || '0') === parentId)
  
  if (children.length === 0) return []
  
  return children.map(item => {
    const childNodes = buildTree(list, item.id)
    return {
      ...item,
      children: childNodes.length ? childNodes : undefined,
      hasChildren: childNodes.length > 0
    }
  })
}

// 表格数据
const tableData = reactive({
  total: 0,
  pageIndex: 1,
  pageSize: 100,
  rows: [] as OrgTreeItem[]
})

// 表单引用
const formRef = ref<FormInstance>()

// 表单验证规则
const formRules: FormRules = {
  name: [
    { required: true, message: '请输入组织名称', trigger: 'blur' }
  ],
  parentOrg: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ],
  sort: [
    { required: true, message: '请输入组织排序', trigger: 'blur' },
    {
      validator: (rule: any, value: any, callback: any) => {
        if (isNaN(value) || value < 0) {
          callback(new Error('组织排序必须为数字且大于等于0'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 对话框显示状态
const dialogVisible = ref(false)

// 对话框标题
const dialogTitle = ref('新增组织')

// 对话框数据
const dialogData = reactive({
  name: '',
  parentOrg: '',
  sort: 0,
  remark: ''
})

// 树形选择器选项
const treeSelectOptions = ref<any[]>([])

// 树形选择器配置
const treeSelectProps = {
  value: 'id',
  label: 'name',
  children: 'children',
}

// 当前操作的组织ID
const currentOrgId = ref<string | null>(null)

/**
 * 加载数据
 */
const loadData = () => {
  const storedData = getStoredData()
  const treeRows = buildTree(storedData)
  tableData.rows = treeRows
  tableData.total = storedData.length
  
  // 重新构建树形选择器选项
  buildTreeSelectOptions()
}

/**
 * 构建树形选择器选项
 */
const buildTreeSelectOptions = () => {
  const currentData = getStoredData()
  
  // 构建完整的树形结构，包括根节点
  const treeData = buildTree(currentData)
  
  // 添加根节点选项
  treeSelectOptions.value = [{
    id: '0',
    name: '默认组织',
    children: treeData
  }]
}

/**
 * 处理新增
 */
const handleAdd = () => {
  dialogTitle.value = '新增组织'
  currentOrgId.value = null
  
  // 重置表单数据
  Object.assign(dialogData, {
    name: '',
    parentOrg: '0', // 默认选择根节点
    sort: 0,
    remark: ''
  })
  
  dialogVisible.value = true
  
  // 清除验证
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

/**
 * 处理详情
 */
const handleDetail = (row: any) => {
  dialogTitle.value = '详情'
  currentOrgId.value = row.id
  
  // 设置表单数据
  Object.assign(dialogData, {
    name: row.name || '',
    parentOrg: row.parentId || '0',
    sort: row.sort || 0,
    remark: row.remark || ''
  })
  
  dialogVisible.value = true
  
  // 清除验证
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

/**
 * 检查组织是否存在数据关联
 */
const checkOrgHasDataRelation = (orgId: string): boolean => {
  // 这里模拟数据关联检查逻辑
  // 实际项目中应该调用API检查该组织是否被用户、角色等其他数据引用
  
  // 模拟规则：ID为1、2的组织存在数据关联
  const orgsWithRelation = ['1', '2']
  return orgsWithRelation.includes(orgId)
}

/**
 * 处理删除
 */
const handleDelete = (row: any) => {
  // 检查是否存在数据关联
  if (checkOrgHasDataRelation(row.id)) {
    ElMessage.warning('该组织存在数据关联，不可删除')
    return
  }
  
  if (row.hasChildren) {
    ElMessage.warning('该组织下有子组织，无法删除')
    return
  }
  
  ElMessageBox.confirm('确定要删除该组织吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const currentData = getStoredData()
    const updatedData = currentData.filter(item => item.id !== row.id)
    saveData(updatedData)
    loadData()
    ElMessage.success('删除成功')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

/**
 * 处理刷新
 */
const handleRefresh = () => {
  loadData()
  ElMessage.success('刷新成功')
}


/**
 * 处理确认
 */
const handleConfirm = async () => {
  if (!formRef.value) return
  
  try {
    // 验证表单
    await formRef.value.validate()
    
    const currentData = getStoredData()
    
    if (currentOrgId.value) {
      // 修改逻辑
      const index = currentData.findIndex(item => item.id === currentOrgId.value)
      if (index > -1) {
        // 创建更新后的对象
        const updatedItem = {
          ...currentData[index],
          name: dialogData.name,
          sort: dialogData.sort,
          remark: dialogData.remark,
          parentId: dialogData.parentOrg === '0' ? undefined : dialogData.parentOrg
        }
        
        // 替换原对象
        currentData[index] = updatedItem
        
        // 保存数据
        saveData(currentData)
        
        // 重新加载数据
        loadData()
        
        ElMessage.success('修改成功')
      } else {
        ElMessage.error('修改失败，未找到该组织')
      }
    } else {
      // 新增逻辑
      const newItem: OrgTreeItem = {
        id: Date.now().toString(),
        name: dialogData.name,
        sort: dialogData.sort,
        remark: dialogData.remark,
        parentId: dialogData.parentOrg === '0' ? undefined : dialogData.parentOrg,
        hasChildren: false
      }
      
      currentData.push(newItem)
      saveData(currentData)
      loadData()
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
  } catch (error) {
    console.log('表单验证失败:', error)
  }
}

// 页面加载时初始化
onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.frame.area {
  height: 100%;
  display: flex;
  flex-direction: column;
  
  .layout {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 0;
  }
  
  .el-divider {
    margin: 16px 0;
  }
  
  .el-table {
    flex: 1;
    margin-top: 0;
  }
}

// 对话框过渡效果
:deep(.el-fade-in-enter-active),
:deep(.el-fade-in-leave-active) {
  transition: opacity 0.3s;
}

:deep(.el-fade-in-enter-from),
:deep(.el-fade-in-leave-to) {
  opacity: 0;
}

// 按钮组样式调整
:deep(.el-button-group) {
  .el-button {
    margin-right: 0;
    
    &:not(:last-child) {
      border-right-color: transparent;
    }
    
    &:first-child {
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
    }
    
    &:last-child {
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
    }
    
    &:not(:first-child):not(:last-child) {
      border-radius: 0;
    }
  }
}

// 表格样式调整
:deep(.el-table) {
  .el-table__body-wrapper {
    overflow-y: auto;
  }
  
  .el-table__row {
    .el-button-group {
      .el-button {
        padding: 5px 8px;
        font-size: 12px;
        
        & + .el-button {
          margin-left: 2px;
        }
      }
    }
  }
}

// 对话框样式调整
:deep(.el-dialog) {
  .el-dialog__body {
    padding: 20px;
  }
  
  .el-form-item {
    margin-bottom: 18px;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    
    .el-button + .el-button {
      margin-left: 10px;
    }
  }
}

// 树形选择器样式调整
:deep(.el-tree-select) {
  .el-select__wrapper {
    padding: 1px 11px;
  }
}
</style>