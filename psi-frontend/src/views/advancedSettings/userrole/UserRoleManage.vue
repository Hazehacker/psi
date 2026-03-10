<!--
 * @Description: 用户角色管理页面
 * 修复了功能权限树勾选状态问题
 * 修复了参数传递格式问题，避免嵌套参数导致的500错误
 * 修复了TypeScript类型检查错误，确保dataPermissions一定存在
-->
<template>
  <div class="frame area user-role-manage">
    <!-- 顶部操作栏 -->
    <div class="layout">
      <!-- 筛选弹出框 -->
      <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="400">
        <div class="search-form-container">
          <el-form class="search-form" ref="searchFromRef" :model="searchParams">
            <div class="form-grid">
              <div class="form-item">
                <el-input placeholder="请输入角色名称" v-model="searchParams.name" clearable />
              </div>
              <div class="form-item">
                <el-input placeholder="请输入备注信息" v-model="searchParams.data" clearable />
              </div>
            </div>
            <el-divider />
            <div class="form-actions">
              <el-button class="search-btn" type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            </div>
          </el-form>
        </div>
        <template #reference>
          <el-button :icon="MoreFilled"></el-button>
        </template>
      </el-popover>

      <!-- 操作按钮组 -->
      <el-button-group>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </el-button-group>
    </div>

    <el-divider />

    <!-- 表格 -->
    <el-table 
      :data="tableRows" 
      height="calc(100% - 90px)" 
      border
      v-loading="loading"
    >
      <el-table-column prop="name" label="角色名称" align="center" width="200" />
      <el-table-column prop="remark" label="备注信息" align="center" />
      <el-table-column label="相关操作" align="center" width="200" fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
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

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" :close-on-click-modal="false" destroy-on-close>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基础资料 -->
        <el-tab-pane label="基础资料" name="basic">
          <el-form :model="formData" label-width="100px">
            <el-form-item label="角色名称" required>
              <el-input v-model="formData.name" placeholder="请输入角色名称" />
            </el-form-item>
            <el-form-item label="备注信息">
              <el-input
                v-model="formData.remark"
                type="textarea"
                :rows="4"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 功能权限 -->
        <el-tab-pane label="功能权限" name="function">
          <div class="permission-tree-container">
            <el-tree
              ref="treeRef"
              :data="permissionTree"
              :props="{ label: 'name', children: 'children' }"
              show-checkbox
              node-key="id"
              :default-checked-keys="checkedKeys"
              @check-change="handleCheckChange"
            />
          </div>
        </el-tab-pane>

        <!-- 数据权限 -->
        <el-tab-pane label="数据权限" name="data">
          <el-form :model="formData.dataPermissions" label-width="120px">
            <!-- 组织机构 -->
            <el-form-item label="组织机构">
              <el-popover
                placement="bottom-start"
                :width="300"
                trigger="click"
                popper-class="org-tree-popover"
              >
                <template #reference>
                  <el-input
                    v-model="orgDisplayText"
                    placeholder="请选择"
                    readonly
                    suffix-icon="ArrowDown"
                    style="width: 100%"
                  />
                </template>
                <div class="org-tree-container">
                  <el-tree
                    ref="orgTreeRef"
                    :data="orgTreeData"
                    :props="{ label: 'name', children: 'children' }"
                    show-checkbox
                    node-key="id"
                    :default-checked-keys="orgCheckedKeys"
                    @check="onOrgTreeCheck"
                  />
                </div>
              </el-popover>
            </el-form-item>

            <!-- 客户 -->
            <el-form-item label="客户">
              <el-select v-model="formData.dataPermissions.customer" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 供应商 -->
            <el-form-item label="供应商">
              <el-select v-model="formData.dataPermissions.supplier" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 仓库 -->
            <el-form-item label="仓库">
              <el-select v-model="formData.dataPermissions.warehouse" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 资金账户 -->
            <el-form-item label="资金账户">
              <el-select v-model="formData.dataPermissions.account" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 用户 -->
            <el-form-item label="用户">
              <el-select v-model="formData.dataPermissions.user" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 人员 -->
            <el-form-item label="人员">
              <el-select v-model="formData.dataPermissions.personnel" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, MoreFilled, Search } from '@element-plus/icons-vue'
import { 
  getRolePermission, 
  getRoleList, 
  getRoleDetail, 
  addRole, 
  modifyRole, 
  deleteRole 
} from '@/apis/advancedSettings/userrole'
// 获取组织结构树
import { getOrganizationTree } from '@/apis/advancedSettings/organization'
import type { 
  RoleAddDTO, 
  RoleListDTO
} from '@/apis/advancedSettings/userrole/type'

// 分页配置
const page = reactive({ 
  current: 1,     // 当前页码
  total: 0,       // 总记录数
  size: 30,       // 每页显示数量
  sizes: [30, 60, 90, 150, 300], // 每页显示数量选项
  count: 5        // 页码按钮数量
})

// 搜索参数 - 根据接口文档修正
const searchParams = reactive({
  name: '',      // 角色名称
  data: ''       // 角色备注 - 接口参数名为data
})

// 表格数据
const tableRows = ref<any[]>([])

// 加载状态
const loading = ref(false)

// 对话框显示状态
const dialogVisible = ref(false)

// 对话框标题
const dialogTitle = ref('新增角色')

// 当前活跃标签页
const activeTab = ref('basic')

// 定义数据权限接口，确保类型安全
interface DataPermissions {
  customer: string
  supplier: string
  warehouse: string
  account: string
  user: string
  personnel: string
  org: string[]
}

// 创建默认数据权限对象，确保在初始化时就有值
const createDefaultDataPermissions = (): DataPermissions => ({
  customer: 'all',
  supplier: 'all',
  warehouse: 'all',
  account: 'all',
  user: 'all',
  personnel: 'all',
  org: []
})

// 表单数据 - 修复类型定义，确保dataPermissions一定存在
const formData = reactive<{
  id?: string
  name: string
  remark?: string
  data?: string
  root?: string
  auth?: string
  functionPermissions?: any
  dataPermissions: DataPermissions  // 移除了可选标识，确保一定存在
}>({
  id: '',
  name: '',
  remark: '',
  data: '',
  root: '',
  auth: '',
  functionPermissions: {},
  dataPermissions: createDefaultDataPermissions() // 使用工厂函数确保有默认值
})

// 组织机构树数据
const orgTreeData = ref<any[]>([])

// 组织机构树选中节点
const orgCheckedKeys = ref<string[]>([])

// 组织机构树引用
const orgTreeRef = ref()

// 权限树数据
const permissionTree = ref<any[]>([])

// 权限树引用
const treeRef = ref()

// 当前选中节点 - 使用响应式确保更新
const checkedKeys = ref<string[]>([])

// 权限树加载状态
const permissionTreeLoaded = ref(false)

// 监听权限树数据变化，当数据加载完成后设置选中状态
watch(permissionTree, (newVal) => {
  if (newVal && newVal.length > 0) {
    permissionTreeLoaded.value = true
    console.log('权限树数据已加载，准备设置选中状态:', checkedKeys.value)
    
    // 延迟设置选中状态，确保树组件已渲染
    nextTick(() => {
      setTimeout(() => {
        if (treeRef.value && checkedKeys.value.length > 0) {
          console.log('设置权限树选中状态:', checkedKeys.value)
          treeRef.value.setCheckedKeys(checkedKeys.value)
          
          // 验证设置是否成功
          setTimeout(() => {
            const currentChecked = treeRef.value.getCheckedKeys()
            console.log('验证 - 当前选中的权限:', currentChecked)
          }, 100)
        }
      }, 100)
    })
  }
}, { deep: true })

// 计算组织机构显示文本
const orgDisplayText = computed(() => {
  if (orgCheckedKeys.value.length === 0) {
    return '请选择'
  }
  
  const selectedNames: string[] = []
  
  // 递归查找节点名称
  const findNodeName = (nodes: any[], id: string): string | null => {
    for (const node of nodes) {
      if (node.id === id) {
        return node.name
      }
      if (node.children) {
        const found = findNodeName(node.children, id)
        if (found) return found
      }
    }
    return null
  }
  
  // 遍历所有选中的节点ID，查找对应的节点名称
  orgCheckedKeys.value.forEach(id => {
    const name = findNodeName(orgTreeData.value, id)
    if (name) {
      selectedNames.push(name)
    }
  })
  
  return selectedNames.join('，')
})

/**
 * 获取角色列表
 */
const fetchRoleList = (): Promise<void> => {
  loading.value = true
  
  return getRoleList({
    pageIndex: page.current,
    pageSize: page.size,
    name: searchParams.name || undefined,
    data: searchParams.data || undefined
  }).then((res: any): void => {
    if (res.code === 10000) {
      const data = res.data
      if (data) {
        // 映射表格数据
        tableRows.value = (data.rows || []).map((r: any) => ({
          id: r.id,
          name: r.name,
          remark: r.data
        }))
        // 更新总记录数
        page.total = data.total || 0
      } else {
        ElMessage.error('返回数据格式错误')
      }
    } else {
      ElMessage.error(res.message || '获取角色列表失败')
    }
  }).catch((error): void => {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败，请稍后重试')
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 获取权限树
 */
const fetchPermissionTree = () => {
  return new Promise((resolve, reject) => {
    getRolePermission().then((res: any) => {
      if (res.code === 10000) {
        const data = res.data
        if (data && data.functionPermissions) {
          permissionTree.value = data.functionPermissions
          console.log('权限树数据结构:', permissionTree.value)
          
          // 打印所有权限节点的ID，用于调试
          const allPermissionIds: string[] = []
          const extractAllIds = (nodes: any[]) => {
            nodes.forEach(node => {
              if (node.id) allPermissionIds.push(node.id)
              if (node.children) extractAllIds(node.children)
            })
          }
          extractAllIds(permissionTree.value)
          console.log('权限树中所有节点的ID:', allPermissionIds)
          
          resolve(permissionTree.value)
        } else {
          permissionTree.value = []
          ElMessage.error('获取权限树数据失败')
          reject(new Error('获取权限树数据失败'))
        }
      } else {
        permissionTree.value = []
        ElMessage.error(res.message || '获取权限树失败')
        reject(new Error(res.message || '获取权限树失败'))
      }
    }).catch(error => {
      console.error('获取权限树失败:', error)
      permissionTree.value = []
      ElMessage.error('获取权限树失败，请稍后重试')
      reject(error)
    })
  })
}

/**
 * 获取组织结构树
 */
const fetchOrgTree = () => {
  getOrganizationTree().then((res: any) => {
    if (res.code === 10000) {
      const data = res.data
      if (data) {
        orgTreeData.value = [
          {id:'all',name:'全部数据'},
          {id:'custom',name:'用户数据'},
          ...data
        ]
      } else {
        ElMessage.error('获取组织结构树数据失败')
      }
    } else {
      ElMessage.error(res.message || '获取组织结构树失败')
    }
  }).catch(error => {
    console.error('获取组织结构树失败:', error)
    ElMessage.error('获取组织结构树失败，请稍后重试')
  })
}

/**
 * 组织机构树选中事件
 */
const onOrgTreeCheck = (data: any, checkedInfo: any) => {
  if (orgTreeRef.value) {
    formData.dataPermissions.org = orgTreeRef.value.getCheckedKeys()
  }
}

/**
 * 权限树勾选变化事件
 */
const handleCheckChange = () => {
  if (treeRef.value) {
    checkedKeys.value = treeRef.value.getCheckedKeys()
    console.log('权限树勾选变化:', checkedKeys.value)
  }
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  page.current = 1
  fetchRoleList()
}

/**
 * 重置搜索条件
 */
const handleResetSearch = () => {
  searchParams.name = ''
  searchParams.data = ''
  page.current = 1
  fetchRoleList()
  ElMessage.success('搜索条件已重置')
}

/**
 * 处理新增
 */
const handleAdd = () => {
  dialogTitle.value = '新增角色'
  activeTab.value = 'basic'
  Object.assign(formData, {
    id: '',
    name: '',
    remark: '',
    data: '',
    root: '',
    auth: '',
    functionPermissions: {},
    dataPermissions: createDefaultDataPermissions()
  })
  checkedKeys.value = []
  orgCheckedKeys.value = []
  dialogVisible.value = true
  
  nextTick(() => {
    if (orgTreeRef.value) {
      orgTreeRef.value.setCheckedKeys([])
    }
    if (treeRef.value) {
      treeRef.value.setCheckedKeys([])
    }
  })
}

/**
 * 处理详情
 */
const handleDetail = async (row: any) => {
  dialogTitle.value = '详情'
  activeTab.value = 'basic'
  
  try {
    // 确保权限树已加载
    if (permissionTree.value.length === 0) {
      await fetchPermissionTree()
    }
    
    const res = await getRoleDetail(row.id)
    console.log('角色详情响应:', res)
    
    if (res.code === 10000) {
      const data = res.data
      if (data) {
        const defaultDataPermissions = createDefaultDataPermissions()
        
        Object.assign(formData, {
          id: data.id,
          name: data.name || '',
          remark: data.data || '',
          data: data.data || '',
          root: data.root || '',
          auth: data.auth || '',
          functionPermissions: {},
          dataPermissions: { ...defaultDataPermissions }
        })
        
        // 重置选中节点
        checkedKeys.value = []
        
        // 解析功能权限（root字段可能是JSON字符串）
        if (data.root) {
          try {
            const rootData = typeof data.root === 'string' ? JSON.parse(data.root) : data.root
            formData.functionPermissions = rootData
            
            // 直接从解析后的对象中提取权限ID
            const permissionIds: string[] = []
            if (typeof rootData === 'object' && rootData !== null) {
              Object.keys(rootData).forEach(key => {
                if (rootData[key] === true) {
                  permissionIds.push(key)
                }
              })
            }
            
            checkedKeys.value = permissionIds
            
            console.log('=== 权限解析调试信息 ===')
            console.log('原始root数据:', data.root)
            console.log('解析后的root数据:', rootData)
            console.log('解析到的权限ID:', permissionIds)
            console.log('权限树数据:', permissionTree.value)
            
          } catch (e) {
            console.warn('解析功能权限失败:', e)
            console.log('原始root数据:', data.root)
          }
        }
        
        // 解析数据权限
        if (data.auth) {
          try {
            const authData = typeof data.auth === 'string' ? JSON.parse(data.auth) : data.auth
            if (authData.org) {
              orgCheckedKeys.value = authData.org
              formData.dataPermissions = { 
                ...formData.dataPermissions, 
                ...authData,
                org: authData.org || []
              }
            }
          } catch (e) {
            console.warn('解析数据权限失败:', e)
          }
        }
        
        dialogVisible.value = true
        
        // 延迟设置选中状态，确保树组件已完全渲染
        nextTick(() => {
          if (orgTreeRef.value) {
            orgTreeRef.value.setCheckedKeys(orgCheckedKeys.value)
          }
          
          // 多次尝试设置权限树选中状态
          const trySetCheckedKeys = (attempt = 0) => {
            if (treeRef.value && permissionTree.value.length > 0) {
              console.log(`第${attempt + 1}次尝试设置权限树选中状态:`, checkedKeys.value)
              treeRef.value.setCheckedKeys(checkedKeys.value)
              
              // 验证设置是否成功
              setTimeout(() => {
                const currentChecked = treeRef.value.getCheckedKeys()
                console.log(`第${attempt + 1}次验证 - 当前选中的权限:`, currentChecked)
                
                // 如果设置失败且还有尝试次数，继续尝试
                if (currentChecked.length === 0 && checkedKeys.value.length > 0 && attempt < 3) {
                  setTimeout(() => trySetCheckedKeys(attempt + 1), 200)
                }
              }, 200)
            } else if (attempt < 5) {
              // 如果树组件还未准备好，继续尝试
              setTimeout(() => trySetCheckedKeys(attempt + 1), 200)
            }
          }
          
          trySetCheckedKeys()
        })
      } else {
        ElMessage.error('获取详情数据失败')
      }
    } else {
      ElMessage.error(res.message || '获取详情失败')
    }
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败，请稍后重试')
  }
}

/**
 * 处理删除
 */
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteRole([row.id]).then((res: any) => {
      if (res.code === 10000) {
        ElMessage.success('删除成功')
        fetchRoleList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    }).catch(error => {
      console.error('删除角色失败:', error)
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
  handleResetSearch()
  ElMessage.success('刷新成功')
}

/**
 * 处理保存
 */
const handleSave = () => {
  // 获取勾选的权限
  if (treeRef.value) {
    const checkedKeys = treeRef.value.getCheckedKeys()
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys()
    
    const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys]
    
    formData.functionPermissions = allCheckedKeys.reduce((acc: any, key: string) => {
      acc[key] = true
      return acc
    }, {})
    
    console.log('保存的权限:', formData.functionPermissions)
  }
  
  // 获取组织机构树选中的节点
  if (orgTreeRef.value && formData.dataPermissions) {
    formData.dataPermissions.org = orgTreeRef.value.getCheckedKeys()
  }
  
  if (formData.id) {
    // 修改角色
    const updateData: RoleListDTO = {
      id: formData.id,
      name: formData.name,
      data: formData.data || formData.remark || '',
      root: JSON.stringify(formData.functionPermissions || {}),
      auth: JSON.stringify(formData.dataPermissions || {})
    }
    console.log('修改角色请求数据:', updateData)
    
    modifyRole(updateData).then((res: any) => {
      console.log('修改角色响应:', res)
      if (res.code === 10000) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        fetchRoleList()
      } else {
        ElMessage.error(res.message || '修改失败')
      }
    }).catch(error => {
      console.error('修改角色失败:', error)
      ElMessage.error('修改失败，请稍后重试')
    })
  } else {
    // 新增角色
    const addData: RoleAddDTO = {
      name: formData.name,
      data: formData.data || formData.remark || '',
      root: JSON.stringify(formData.functionPermissions || {}),
      auth: JSON.stringify(formData.dataPermissions || {})
    }
    console.log('新增角色请求数据:', addData)
    
    addRole(addData).then((res: any) => {
      console.log('新增角色响应:', res)
      if (res.code === 10000) {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        fetchRoleList()
      } else {
        ElMessage.error(res.message || '新增失败')
      }
    }).catch(error => {
      console.error('新增角色失败:', error)
      ElMessage.error('新增失败，请稍后重试')
    })
  }
}

/**
 * 分页大小改变
 */
const onPageSizeChange = (size: number) => { 
  page.size = size
  page.current = 1
  fetchRoleList()
}

/**
 * 页码改变
 */
const onPageChange = (p: number) => { 
  page.current = p
  fetchRoleList()
}

// 页面加载时初始化数据
onMounted(() => {
  fetchRoleList()
  fetchPermissionTree()
  fetchOrgTree()
})
</script>

<style scoped lang="scss">
.user-role-manage {
  .layout { 
    display: flex; 
    justify-content: space-between; 
    
    .el-button-group {
      display: flex;
    }
  }
}

/* 权限树容器 */
.permission-tree-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
}

/* 组织机构树容器 */
.org-tree-container {
  max-height: 300px;
  overflow-y: auto;
  padding: 10px;
}

// 搜索弹窗样式
:deep(.search-popover) {
  width: 400px !important;
  max-width: 400px !important;
  padding: 16px;
  
  .search-form-container {
    width: 100%;
    
    .search-form {
      width: 100%;
      
      .form-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 12px;
        width: 100%;
        
        .form-item {
          width: 100%;
          
          .el-input {
            width: 100%;
            
            .el-input__wrapper {
              width: 100%;
              box-sizing: border-box;
            }
          }
        }
      }
      
      .el-divider {
        margin: 16px 0;
      }
      
      .form-actions {
        width: 100%;
        display: flex;
        justify-content: center;
        
        .search-btn {
          width: 100%;
        }
      }
    }
  }
}

:deep(.org-tree-popover) {
  padding: 0;
}

// 重置 Element Plus 默认样式
:deep(.el-popper) {
  &.search-popover {
    .el-popper__arrow {
      display: none;
    }
    
    .el-input {
      .el-input__wrapper {
        width: 100%;
        box-sizing: border-box;
      }
    }
  }
}

/* 标签页样式优化 */
:deep(.el-tabs__content) {
  padding: 16px 0;
}

/* 对话框样式优化 */
:deep(.el-dialog__body) {
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .layout {
    flex-direction: column;
    gap: 10px;
    
    .el-button-group {
      justify-content: flex-end;
    }
  }
  
  :deep(.search-popover) {
    width: 90vw !important;
  }
  
  .search-form .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>