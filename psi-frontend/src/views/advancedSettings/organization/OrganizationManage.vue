<!--
 * @Description: 组织机构管理页面
-->
<template>
  <div class="frame area organization-manage">
    <div class="layout">
      <el-button-group>
        <el-button @click="handleAdd">新增</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
        <el-button @click="checkTableData">检查数据</el-button>
      </el-button-group>
    </div>
    <el-divider></el-divider>
    
    <el-table 
      :data="tableData.rows" 
      height="calc(100% - 60px)" 
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" 
      row-key="id" 
      border
      stripe
      :key="tableKey"
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
              :type="getDeleteButtonType(scope.row) as any"
              :disabled="!canDeleteOrganization(scope.row)"
              :title="getDeleteButtonTooltip(scope.row)"
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
              <el-tree-select
                v-model="dialogData.parentOrg"
                :data="treeSelectOptions"
                :props="treeSelectProps"
                placeholder="请选择所属组织"
                style="width: 100%"
                check-strictly
                :render-after-expand="false"
                clearable
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
import { ref, reactive, onMounted, nextTick, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules, ButtonProps } from 'element-plus'
import {
  getOrganizationList,
  getOrganizationDetail,
  addOrganization,
  updateOrganization,
  deleteOrganization
} from '@/apis/advancedSettings/organization'
import type { OrganizationAddDTO, OrganizationUpdateDTO } from '@/apis/advancedSettings/organization/type'

// 表格数据
const tableData = reactive({
  total: 0,
  pageIndex: 1,
  pageSize: 100,
  rows: [] as any[]
})

// 表格重新渲染的 key
const tableKey = ref(0)

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

// 计算属性：获取显示的父组织名称
const displayParentOrgName = computed(() => {
  if (dialogData.parentOrg === '') {
    return '默认组织'
  }
  
  // 在树形选项中查找对应的组织名称
  const findOrgName = (nodes: any[], targetId: string): string | null => {
    for (const node of nodes) {
      if (node.id === targetId) {
        return node.name
      }
      if (node.children && node.children.length > 0) {
        const found = findOrgName(node.children, targetId)
        if (found) return found
      }
    }
    return null
  }
  
  return findOrgName(treeSelectOptions.value, dialogData.parentOrg) || '未知组织'
})

/**
 * 强制表格重新渲染
 */
const forceTableUpdate = () => {
  tableKey.value += 1
  console.log('🔄 强制表格更新，key:', tableKey.value)
}

/**
 * 获取组织列表
 */
const fetchOrgList = () => {
  console.log('🔄 开始获取组织列表...')
  
  getOrganizationList().then(res => {
    console.log('📊 组织列表原始响应:', res)
    
    if (res.code === 10000 && res.data) {
      console.log('✅ 后端返回完整的树形结构数据')
      
      // 直接使用后端返回的树形结构数据
      const treeData = res.data
      console.log('🌳 树形数据:', treeData)
      
      // 计算总节点数
      const countNodes = (nodes: any[]): number => {
        return nodes.reduce((count, node) => {
          return count + 1 + (node.children ? countNodes(node.children) : 0)
        }, 0)
      }
      
      // 确保表格数据正确设置
      tableData.rows = treeData
      tableData.total = countNodes(treeData)
      
      console.log('✅ 表格数据已更新:')
      console.log('   - 根节点数:', tableData.rows.length)
      console.log('   - 总节点数:', tableData.total)
      
      // 强制表格重新渲染
      nextTick(() => {
        forceTableUpdate()
      })
      
      // 构建树形选择器数据
      buildTreeSelectOptions(treeData)
    } else {
      console.error('❌ 获取组织列表失败:', res.message)
      ElMessage.error(res.message || '获取组织列表失败')
    }
  }).catch(error => {
    console.error('❌ 获取组织列表失败:', error)
    ElMessage.error('获取组织列表失败，请稍后重试')
  })
}

/**
 * 构建树形选择器选项
 * 修复：确保根组织显示为"默认组织"，同时保留完整的组织树
 */
const buildTreeSelectOptions = (treeData: any[]) => {
  console.log('🌳 构建树形选择器选项，使用完整的树形数据')
  
  // 创建一个"默认组织"作为顶级选项，并将整个组织树作为其子节点
  treeSelectOptions.value = [{
    id: '', // 空ID表示默认组织
    name: '默认组织',
    children: treeData  // 将完整的组织树作为子节点
  }]
  
  console.log('✅ 树形选择器选项构建完成:', treeSelectOptions.value)
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
    parentOrg: '', // 默认选择根节点（默认组织）
    sort: 0,
    remark: ''
  })
  
  dialogVisible.value = true
  
  // 清除验证
  nextTick(() => {
    formRef.value?.clearValidate()
    console.log('✅ 新增对话框已打开，parentOrg:', dialogData.parentOrg)
  })
}

/**
 * 处理详情
 * 修复：确保根组织正确显示"默认组织"
 */
const handleDetail = async (row: any) => {
  dialogTitle.value = '详情'
  currentOrgId.value = row.id

  if (row.id) {
    console.log('🔍 获取组织详情，ID:', row.id)
    
    try {
      const res = await getOrganizationDetail(row.id)
      console.log('✅ 组织详情响应:', res)
      
      if (res.code === 10000 && res.data) {
        const data = res.data
        
        // 修复：直接判断是否是根组织（pid为空字符串）
        const isRootOrganization = data.pid === "" || data.pid === null || data.pid === undefined
        
        // 设置父组织ID：根组织设为空字符串，其他组织设为实际的pid
        const parentOrg = isRootOrganization ? '' : (data.pid || '')
        
        Object.assign(dialogData, {
          name: data.name || '',
          parentOrg: parentOrg,
          sort: data.sort || 0,
          remark: data.remark || ''
        })
        
        console.log('✅ 详情表单数据设置完成:', dialogData)
        console.log('🔍 组织详情判断:')
        console.log('   - 原始pid:', data.pid, '类型:', typeof data.pid)
        console.log('   - 是否是根组织:', isRootOrganization)
        console.log('   - 设置的parentOrg:', parentOrg, '类型:', typeof parentOrg)
        console.log('   - 显示的父组织名称:', displayParentOrgName.value)

        dialogVisible.value = true

        // 等待对话框完全渲染后再处理
        await nextTick()
        formRef.value?.clearValidate()
        
        console.log('✅ 详情对话框已打开，表单验证已清除')
        console.log('🌳 当前树形选择器选项:', treeSelectOptions.value)
        
        // 再次确认树形选择器的值
        await nextTick()
        console.log('🔍 树形选择器当前值:', dialogData.parentOrg)
        console.log('🔍 显示的父组织名称:', displayParentOrgName.value)
      } else {
        ElMessage.error(res.message || '获取组织详情失败')
      }
    } catch (error) {
      console.error('❌ 获取组织详情失败:', error)
      ElMessage.error('获取组织详情失败，请稍后重试')
    }
  } else {
    ElMessage.error('组织ID无效')
  }
}

/**
 * 检查组织是否可以被删除
 */
const canDeleteOrganization = (org: any): boolean => {
  // 检查是否有子组织
  const hasChildren = org.children && org.children.length > 0
  return !hasChildren && !isDefaultRootOrganization(org)
}

/**
 * 检查是否是默认根组织
 */
const isDefaultRootOrganization = (org: any): boolean => {
  // 根据业务逻辑来判断根组织
  // 主要判断是否有父组织
  return org.pid === "" || !org.pid || org.pid === null || org.pid === undefined
}

/**
 * 获取删除按钮的类型
 */
const getDeleteButtonType = (org: any): ButtonProps['type'] => {
  return canDeleteOrganization(org) ? 'danger' : 'info'
}

/**
 * 获取删除按钮的提示信息
 */
const getDeleteButtonTooltip = (org: any): string => {
  const hasChildren = org.children && org.children.length > 0
  if (hasChildren) {
    return '该组织下有子组织，无法删除'
  }
  if (isDefaultRootOrganization(org)) {
    return '默认根组织无法删除'
  }
  return '删除该组织'
}

/**
 * 处理删除
 */
const handleDelete = (row: any) => {
  // 更严格的删除前验证
  const hasChildren = row.children && row.children.length > 0
  if (hasChildren) {
    ElMessage.warning('该组织下有子组织，无法删除')
    return
  }
  
  // 检查是否是默认根组织
  if (isDefaultRootOrganization(row)) {
    ElMessage.warning('默认根组织无法删除')
    return
  }
  
  ElMessageBox.confirm(`确定要删除组织 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    console.log('🗑️ 删除组织:', row.name, 'ID:', row.id)
    
    deleteOrganization(row.id).then(res => {
      console.log('✅ 删除响应:', res)
      
      if (res.code === 10000) {
        ElMessage.success('删除成功')
        fetchOrgList()
      } else {
        // 更友好的错误提示
        let errorMessage = res.message || '删除失败'
        
        // 根据后端返回的错误消息提供更友好的提示
        if (res.message.includes('sub-organizations')) {
          errorMessage = '该组织下有子组织，请先删除子组织'
        } else if (res.message.includes('Default root organization')) {
          errorMessage = '默认根组织无法删除'
        }
        
        ElMessage.error(errorMessage)
      }
    }).catch(error => {
      console.error('❌ 删除组织失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    })
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

/**
 * 处理刷新
 */
const handleRefresh = () => {
  fetchOrgList()
  ElMessage.success('刷新成功')
}

/**
 * 检查表格数据状态
 */
const checkTableData = () => {
  console.log('🔍 当前表格数据状态:')
  console.log('   - 总行数:', tableData.total)
  console.log('   - 表格行数:', tableData.rows.length)
  console.log('   - 表格数据:', tableData.rows)
  
  // 检查是否有重复的ID
  const ids = getAllNodeIds(tableData.rows)
  const uniqueIds = [...new Set(ids)]
  console.log('   - 唯一ID数量:', uniqueIds.length, '总ID数量:', ids.length)
  
  if (uniqueIds.length !== ids.length) {
    console.warn('⚠️ 发现重复的ID!')
  }
  
  ElMessage.info(`表格数据: ${tableData.rows.length} 行, ${tableData.total} 总记录`)
}

/**
 * 获取所有节点的ID
 */
const getAllNodeIds = (nodes: any[]): string[] => {
  const ids: string[] = []
  
  const traverse = (nodeList: any[]) => {
    nodeList.forEach(node => {
      if (node.id) {
        ids.push(node.id)
      }
      if (node.children && node.children.length > 0) {
        traverse(node.children)
      }
    })
  }
  
  traverse(nodes)
  return ids
}

/**
 * 处理确认
 * 修复：保存时正确处理根组织的父组织ID
 */
const handleConfirm = async () => {
  if (!formRef.value) return
  
  try {
    // 验证表单
    await formRef.value.validate()
    
    console.log('🔍 准备保存数据:')
    console.log('   - 当前操作:', currentOrgId.value ? '修改' : '新增')
    console.log('   - 组织ID:', currentOrgId.value)
    console.log('   - 表单数据:', dialogData)
    
    // 确保数据类型正确，修复 pid 类型问题
    // 如果选择的是"默认组织"（parentOrg为空字符串），则pid应该设为空
    const processedData = {
      name: String(dialogData.name || '').trim(),
      pid: dialogData.parentOrg === '' ? '' : dialogData.parentOrg, // 空字符串表示根组织
      sort: Number(dialogData.sort) || 0,
      remark: String(dialogData.remark || '').trim()
    }
    
    console.log('🔍 处理后数据:', processedData)
    console.log('🔍 父组织处理:')
    console.log('   - 原始parentOrg:', dialogData.parentOrg)
    console.log('   - 处理后pid:', processedData.pid)
    
    if (currentOrgId.value) {
      // 修改 - 使用类型断言修复类型问题
      const updateData: OrganizationUpdateDTO = {
        name: processedData.name,
        pid: processedData.pid,
        sort: processedData.sort,
        remark: processedData.remark
      } as OrganizationUpdateDTO
      
      updateOrganization(currentOrgId.value, updateData).then(res => {
        console.log('✅ 修改响应:', res)
        
        if (res.code === 10000) {
          ElMessage.success('修改成功')
          dialogVisible.value = false
          console.log('🔄 修改成功，重新获取组织列表...')
          fetchOrgList()
        } else {
          console.error('❌ 修改失败，错误码:', res.code, '消息:', res.message)
          ElMessage.error(res.message || '修改失败')
        }
      }).catch(error => {
        console.error('❌ 修改组织失败:', error)
        ElMessage.error('修改失败，请稍后重试')
      })
    } else {
      // 新增 - 使用类型断言修复类型问题
      const addData: OrganizationAddDTO = {
        name: processedData.name,
        pid: processedData.pid,
        sort: processedData.sort,
        remark: processedData.remark
      } as OrganizationAddDTO
      
      addOrganization(addData).then(res => {
        console.log('✅ 新增响应:', res)
        
        if (res.code === 10000) {
          ElMessage.success('新增成功')
          dialogVisible.value = false
          
          // 确保重新获取数据
          console.log('🔄 新增成功，重新获取组织列表...')
          fetchOrgList()
          
          // 额外检查：延迟再次检查数据
          setTimeout(() => {
            console.log('⏰ 延迟检查表格数据:', tableData.rows.length, '行')
            console.log('⏰ 表格数据详情:', tableData.rows)
          }, 1000)
          
        } else {
          console.error('❌ 新增失败，错误码:', res.code, '消息:', res.message)
          ElMessage.error(res.message || '新增失败')
        }
      }).catch(error => {
        console.error('❌ 新增组织失败:', error)
        ElMessage.error('新增失败，请稍后重试')
      })
    }
  } catch (error) {
    console.log('表单验证失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchOrgList()
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

// 自定义显示父组织名称
.parent-org-display {
  font-weight: bold;
  color: #409eff;
}
</style>