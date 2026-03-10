<template>
  <div class="frame area user-manage">
    <div class="layout">
      <!-- 搜索筛选弹窗 -->
      <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="400">
        <div class="search-form-container">
          <el-form class="search-form" ref="searchFromRef" :model="searchForm">
            <!-- 两列网格布局的表单 -->
            <div class="form-grid">
              <div class="form-item">
                <el-input placeholder="请输入用户名称" v-model="searchForm.name" clearable />
              </div>
              <div class="form-item">
                <el-input placeholder="请输入用户账号" v-model="searchForm.username" clearable />
              </div>
              <div class="form-item">
                <el-input placeholder="请输入手机号码" v-model="searchForm.tel" clearable />
              </div>
              <div class="form-item">
                <el-input placeholder="请输入备注信息" v-model="searchForm.remark" clearable />
              </div>
            </div>
            <el-divider />
            <!-- 搜索按钮和重置按钮 -->
            <div class="form-actions">
              <el-button class="reset-btn" @click="handleResetSearch">重置</el-button>
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
        <el-button type="primary" @click="openDialogForAdd">新增</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
      </el-button-group>
    </div>

    <el-divider />

    <!-- 用户数据表格 -->
    <el-table :data="tableRows" height="calc(100% - 90px)" border v-loading="loading">
      <el-table-column prop="name" label="用户名称" align="center" width="160" />
      <el-table-column prop="orgName" label="所属组织" align="center" width="160" />
      <el-table-column prop="roleName" label="用户角色" align="center" width="160" />
      <el-table-column prop="username" label="用户账号" align="center" width="160" />
      <el-table-column prop="tel" label="手机号码" align="center" width="160" />
      <el-table-column prop="remark" label="备注信息" align="center" />
      <el-table-column label="相关操作" align="center" width="180">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="primary" @click="openDialogForEdit(row)">详情</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态提示 -->
    <div v-if="tableRows.length === 0 && !loading" class="empty-state">
      <el-empty description="暂无用户数据" />
    </div>

    <!-- 分页组件 -->
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

    <!-- 用户详情/新增对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名称" />
        </el-form-item>
        <!-- 非编辑自身时才显示组织和角色选择 -->
        <el-form-item v-if="!isEditingSelf" label="所属组织" prop="orgId">
          <el-tree-select 
            v-model="form.orgId" 
            :data="orgTree" 
            :props="treeSelectProps" 
            placeholder="请选择所属组织" 
            style="width: 100%" 
            check-strictly
            @change="onOrgChange"
          />
        </el-form-item>
        <el-form-item v-if="!isEditingSelf && form.orgId" label="用户角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择用户角色" style="width:100%">
            <el-option v-for="r in roleNameList" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="手机号码" prop="tel">
          <el-input v-model="form.tel" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="用户密码" prop="pwd">
          <el-input v-model="form.pwd" :placeholder="form.id ? '不修改密码请留空' : '请输入用户密码'" />
        </el-form-item>
        <el-form-item label="用户头像" prop="avatar">
          <el-upload :action="uploadAction" :show-file-list="false" :on-success="onAvatarUploadSuccess">
            <img v-if="form.avatar" :src="form.avatar" class="userAvatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.remark" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, MoreFilled, Search } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { 
  getUserList, 
  getUserDetail, 
  addUser, 
  modifyUser, 
  deleteUser 
} from '@/apis/advancedSettings/user'
import { getOrganizationTree } from '@/apis/advancedSettings/organization'
import { getRoleNameList } from '@/apis/advancedSettings/userrole'
import type { UserAddDTO, UserModDTO } from '@/apis/advancedSettings/user/type'

// 搜索表单数据
const searchForm = reactive({ 
  name: '', 
  username: '', 
  tel: '', 
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

// 表格数据和加载状态
const tableRows = ref<any[]>([])
const loading = ref(false)

// 对话框状态
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const formRef = ref<FormInstance>()

// 表单数据
const form = reactive<any>({ 
  id: '', 
  name: '', 
  orgId: '', 
  roleId: '', 
  username: '', 
  tel: '', 
  pwd: '', 
  avatar: '', 
  remark: '' 
})

// 组织树和角色列表数据
const orgTree = ref<any[]>([])
const roleNameList = ref<any[]>([])
const uploadAction = ref('/user/upload')

// 组织映射表 (用于将组织ID转换为组织名称)
const orgMap = ref<Record<string, string>>({})

// 树形选择器配置
const treeSelectProps = { 
  value: 'id', 
  label: 'name', 
  children: 'children' 
}

// 表单验证规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
  orgId: [{ required: true, message: '请选择所属组织', trigger: 'change' }],
  roleId: [{ required: false, message: '请选择用户角色', trigger: 'change' }],
  username: [{ required: true, message: '请输入用户账号', trigger: 'blur' }],
  tel: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { 
      validator: (_r, v, cb) => { 
        /^\d{6,20}$/.test(v) ? cb() : cb(new Error('手机号码格式不正确')) 
      }, 
      trigger: 'blur' 
    }
  ]
}

// 计算属性：判断是否正在编辑自身账户
const isEditingSelf = computed(() => false)

/**
 * 构建组织映射表
 */
const buildOrgMap = (orgs: any[]) => {
  orgs.forEach(org => {
    orgMap.value[org.id] = org.name
    if (org.children && org.children.length > 0) {
      buildOrgMap(org.children)
    }
  })
}

/**
 * 获取组织名称
 */
const getOrgName = (orgId: string): string => {
  return orgMap.value[orgId] || orgId
}

/**
 * 获取用户列表
 * @param goFirst 是否跳转到第一页
 */
const fetchUsers = (goFirst?: number): Promise<void> => {
  loading.value = true
  if (goFirst === 1) page.current = 1
  
  const params: any = {
    pageIndex: page.current, 
    pageSize: page.size
  }
  
  if (searchForm.name) params.name = searchForm.name
  if (searchForm.username) params.user = searchForm.username
  if (searchForm.tel) params.tel = searchForm.tel
  if (searchForm.remark) params.data = searchForm.remark
    
  return getUserList(params).then((res): void => {
    if (res.code === 10000 && res.data) {
      console.log('用户列表响应:', res.data.rows)
      
      tableRows.value = (res.data.rows || []).map((u: any) => {
      
        const userId = u.id
        
        return {
          id: userId,
          ...u,
          orgName: getOrgName(u.frame) || u.frame, // 使用组织名称，如果没有映射则显示ID
          roleName: u.role || '',
          username: u.user || '',
          remark: u.data || ''
        }
      })
      
      page.total = res.data.total || 0
      
      if (tableRows.value.length === 0 && (searchForm.name || searchForm.username || searchForm.tel || searchForm.remark)) {
        ElMessage.info('未找到符合条件的用户')
      }
    } else {
      ElMessage.error(res.message || '获取用户列表失败')
    }
  }).catch((err): void => {
    console.error('获取用户列表失败:', err)
    ElMessage.error('获取用户列表失败')
  }).finally(() => {
    loading.value = false
  })
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  fetchUsers(1)
}

/**
 * 重置搜索条件
 */
const handleResetSearch = () => {
  Object.assign(searchForm, { name: '', username: '', tel: '', remark: '' })
  fetchUsers(1)
}

/**
 * 获取组织树数据
 */
const fetchOrgTree = () => {
  getOrganizationTree().then(res => {
    console.log('组织树响应:', res)
    if (res.code === 10000) {
      orgTree.value = res.data || []
      // 构建组织映射表
      buildOrgMap(orgTree.value)
      console.log('组织映射表:', orgMap.value)
    } else {
      ElMessage.error('获取组织树失败: ' + (res.message || '未知错误'))
    }
  }).catch(err => {
    console.error('获取组织树失败:', err)
    ElMessage.error('获取组织树失败')
  })
}

/**
 * 获取角色列表数据
 */
const fetchRoleNames = () => {
  getRoleNameList().then(res => {
    if (res.code === 10000 && res.data && res.data.roleNameList) {
      roleNameList.value = res.data.roleNameList || []
    }
  }).catch(err => {
    console.error('获取角色列表失败:', err)
  })
}

/**
 * 检查组织ID是否存在
 */
const checkOrgExists = (orgId: string): boolean => {
  const findOrg = (orgs: any[]): boolean => {
    for (const org of orgs) {
      if (org.id === orgId) return true
      if (org.children && org.children.length > 0) {
        if (findOrg(org.children)) return true
      }
    }
    return false
  }
  
  return findOrg(orgTree.value)
}

/**
 * 组织选择变更事件
 */
const onOrgChange = (orgId: string) => {
  console.log('选择的组织ID:', orgId)
  console.log('选择的组织名称:', getOrgName(orgId))
  
  // 重置角色选择（因为角色可能依赖于组织）
  form.roleId = ''
}

/**
 * 打开新增用户对话框
 */
const openDialogForAdd = () => {
  dialogTitle.value = '新增用户'
  Object.assign(form, { 
    id: '', 
    name: '', 
    orgId: '', 
    roleId: '', 
    username: '', 
    tel: '', 
    pwd: '', 
    avatar: '', 
    remark: '' 
  })
  dialogVisible.value = true
}

/**
 * 打开编辑用户对话框
 * @param row 用户数据行
 */
const openDialogForEdit = (row: any) => {
  console.log('用户行数据:', row)
  
  //if (!row.id) {
    //ElMessage.error('用户ID不存在，无法获取详情')
    //return
  //}
  
  dialogTitle.value = '详情'
  getUserDetail(row.id).then(res => {
    console.log('用户详情响应:', res)
    if (res.code === 10000 && res.data) {
      const d: any = res.data
      Object.assign(form, { 
        id: row.id, 
        name: d.name || '', 
        orgId: d.frame || '', 
        roleId: d.role || '', 
        username: d.user || '', 
        tel: d.tel || '', 
        pwd: '', 
        avatar: d.imgUrl || d.img || '', 
        remark: d.data || '' 
      })
      dialogVisible.value = true
    } else {
      ElMessage.error(res.message || '获取用户详情失败')
    }
  }).catch(err => {
    console.error('获取用户详情失败:', err)
    ElMessage.error('获取用户详情失败')
  })
}

/**
 * 提交表单（新增或修改用户）
 */
const handleSubmit = () => {
  if (!formRef.value) return
  
  formRef.value.validate((valid) => {
    if (!valid) return
    
    // 验证组织ID是否已选择且有效
    if (!form.orgId) {
      ElMessage.error('请选择所属组织')
      return
    }
    
    // 验证组织ID是否在组织树中存在
    const orgExists = checkOrgExists(form.orgId)
    if (!orgExists) {
      ElMessage.error('选择的组织不存在，请重新选择')
      return
    }
    
    // 调试：打印提交的数据
    console.log('准备提交的用户数据:')
    console.log('- 组织ID (frame):', form.orgId)
    console.log('- 组织名称:', getOrgName(form.orgId))
    console.log('- 完整表单数据:', form)
    
    if (form.id) {
      // 修改用户
      const updateData: UserModDTO = {
        id: form.id,
        name: form.name,
        frame: form.orgId,
        role: form.roleId || '',
        user: form.username,
        tel: form.tel,
        pwd: form.pwd || undefined,
        img: form.avatar || undefined,
        data: form.remark || undefined
      }
      
      console.log('修改用户请求数据:', updateData)
      
      modifyUser(updateData).then(res => {
        if (res.code === 10000) {
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchUsers()
        } else {
          ElMessage.error(res.message || '保存失败')
          console.error('修改用户失败响应:', res)
        }
      }).catch(err => {
        console.error('保存失败:', err)
        ElMessage.error('保存失败')
      })
    } else {
      // 新增用户
      const addData: UserAddDTO = {
        name: form.name,
        frame: form.orgId,
        role: form.roleId || '',
        user: form.username,
        tel: form.tel,
        pwd: form.pwd,
        img: form.avatar || undefined,
        data: form.remark || undefined
      }
      
      console.log('新增用户请求数据:', addData)
      
      // 确保密码不为空
      if (!addData.pwd) {
        ElMessage.error('请输入用户密码')
        return
      }
      
      addUser(addData).then(res => {
        console.log('新增用户响应:', res)
        if (res.code === 10000) {
          ElMessage.success('保存成功')
          dialogVisible.value = false
          fetchUsers()
        } else {
          ElMessage.error(res.message || '保存失败')
          console.error('新增用户失败响应:', res)
        }
      }).catch(err => {
        console.error('保存失败:', err)
        ElMessage.error('保存失败')
      })
    }
  })
}

/**
 * 删除用户
 * @param row 用户数据行
 */
const handleDelete = (row: any) => {
  ElMessageBox.confirm('您确定要删除该用户吗？', '提示', { type: 'warning' })
    .then(() => deleteUser(row.id))
    .then(res => {
      if (res && res.code === 10000) {
        ElMessage.success('删除成功')
        fetchUsers()
      } else {
        ElMessage.error(res?.message || '删除失败')
      }
    })
    .catch(() => {})
}

/**
 * 刷新用户列表
 */
const handleRefresh = () => { 
  handleResetSearch() // 重置搜索条件并刷新
  ElMessage.success('刷新成功') 
}

/**
 * 分页大小改变事件
 * @param size 新的分页大小
 */
const onPageSizeChange = (size: number) => { 
  page.size = size; 
  fetchUsers(1) 
}

/**
 * 页码改变事件
 * @param p 新的页码
 */
const onPageChange = (p: number) => { 
  page.current = p; 
  fetchUsers() 
}

/**
 * 头像上传成功回调
 * @param result 上传结果
 */
const onAvatarUploadSuccess = (result: any) => {
  if (result && result.state === 'success') form.avatar = result.info
}

// 页面加载时初始化数据
onMounted(() => {
  // 先加载组织树和角色列表，然后加载用户列表
  Promise.all([fetchOrgTree(), fetchRoleNames()]).then(() => {
    fetchUsers(1)
  })
})
</script>

<style scoped lang="scss">
.user-manage {
  .layout { 
    display: flex; 
    justify-content: space-between; 
    
    .el-button-group {
      display: flex;
    }
  }
  
  .userAvatar { 
    width: 100px; 
    height: 100px; 
    display: block; 
  }
  
  .avatar-uploader-icon { 
    width: 100px; 
    height: 100px; 
    display:flex; 
    align-items:center; 
    justify-content:center; 
  }
  
  .empty-state {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
  }
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
        gap: 12px;
        
        .reset-btn, .search-btn {
          flex: 1;
        }
      }
    }
  }
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
</style>