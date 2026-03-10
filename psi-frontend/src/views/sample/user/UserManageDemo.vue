<template>
    <div class="frame area user-demo">
      <div class="layout">
        <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="400">
          <div class="search-form-container">
            <el-form class="search-form" ref="searchFromRef" :model="searchForm">
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
              <div class="form-actions">
                <el-button class="search-btn" type="primary" :icon="Search" @click="applySearch">搜索</el-button>
              </div>
            </el-form>
          </div>
          <template #reference>
            <el-button :icon="MoreFilled"></el-button>
          </template>
        </el-popover>
        <el-button-group>
          <el-button type="primary" @click="openDialogForAdd">新增</el-button>
          <el-button @click="handleRefresh">刷新</el-button>
        </el-button-group>
      </div>
  
      <el-divider />
      <el-table :data="filteredTableRows" height="calc(100% - 90px)" border>
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
  
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="用户名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入用户名称" />
          </el-form-item>
          <el-form-item label="所属组织" prop="orgId">
            <el-tree-select v-model="form.orgId" :data="orgTree" :props="treeSelectProps" placeholder="请选择所属组织" style="width: 100%" check-strictly />
          </el-form-item>
          <el-form-item v-if="form.orgId" label="用户角色" prop="roleId">
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
            <el-upload :action="'/upload'" :show-file-list="false" :on-success="onAvatarUploadSuccess">
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
  
  const searchForm = reactive({ name: '', username: '', tel: '', remark: '' })
  const page = reactive({ current: 1, total: 0, size: 30, sizes: [30, 60, 90, 150, 300], count: 5 })
  const tableRows = ref<any[]>([])
  const originalTableRows = ref<any[]>([]) // 保存原始数据用于筛选
  
  const dialogVisible = ref(false)
  const dialogTitle = ref('新增用户')
  const formRef = ref<FormInstance>()
  const form = reactive<any>({ id: '', name: '', orgId: '', roleId: '', username: '', tel: '', pwd: '', avatar: '', remark: '' })
  
  const orgTree = ref<any[]>([])
  const roleNameList = ref<any[]>([])
  
  const treeSelectProps = { value: 'id', label: 'name', children: 'children' }
  
  const rules: FormRules = {
    name: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
    orgId: [{ required: true, message: '请选择所属组织', trigger: 'change' }],
    username: [{ required: true, message: '请输入用户账号', trigger: 'blur' }],
    tel: [
      { required: true, message: '请输入手机号码', trigger: 'blur' },
      { validator: (_r, v, cb) => { /^\d{6,20}$/.test(v) ? cb() : cb(new Error('手机号码格式不正确')) }, trigger: 'blur' }
    ]
  }
  
  // 计算属性：根据搜索条件筛选表格数据
  const filteredTableRows = computed(() => {
    if (!searchForm.name && !searchForm.username && !searchForm.tel && !searchForm.remark) {
      return tableRows.value
    }
    
    return tableRows.value.filter(row => {
      const nameMatch = !searchForm.name || row.name?.includes(searchForm.name)
      const usernameMatch = !searchForm.username || row.username?.includes(searchForm.username)
      const telMatch = !searchForm.tel || row.tel?.includes(searchForm.tel)
      const remarkMatch = !searchForm.remark || row.remark?.includes(searchForm.remark)
      
      return nameMatch && usernameMatch && telMatch && remarkMatch
    })
  })
  
  // demo 初始化数据
  const initDemo = () => {
    orgTree.value = [
      { id: '0', name: '默认组织', children: [ 
        { id: '1', name: '公司1', children: [{ id: '11', name: '部门1' }] }, 
        { id: '2', name: '超级管理员' } 
      ] }
    ]
    roleNameList.value = [ 
      { id: 'r1', name: '管理员' }, 
      { id: 'r2', name: '财务' } 
    ]
    tableRows.value = [
      { id: 'u1', name: '张三', orgId: '1', orgName: '公司1', roleId: 'r1', roleName: '管理员', username: 'zhangsan', tel: '13500000000', remark: '演示用户' },
      { id: 'u2', name: '李四', orgId: '2', orgName: '超级管理员', roleId: 'r2', roleName: '财务', username: 'lisi', tel: '13600000000', remark: '财务主管' },
      { id: 'u3', name: '王五', orgId: '11', orgName: '部门1', roleId: 'r1', roleName: '管理员', username: 'wangwu', tel: '13700000000', remark: '部门经理' }
    ]
    originalTableRows.value = [...tableRows.value] // 保存原始数据副本
    page.total = tableRows.value.length
  }
  
  const applySearch = () => {
    // 更新分页总数
    page.total = filteredTableRows.value.length
    page.current = 1 // 重置到第一页
    ElMessage.success('筛选完成')
  }
  
  const openDialogForAdd = () => {
    dialogTitle.value = '新增用户'
    Object.assign(form, { id: '', name: '', orgId: '', roleId: '', username: '', tel: '', pwd: '', avatar: '', remark: '' })
    dialogVisible.value = true
  }
  
  const openDialogForEdit = (row: any) => {
    dialogTitle.value = '详情'
    Object.assign(form, { ...row, pwd: '' })
    dialogVisible.value = true
  }
  
  const handleSubmit = () => {
    if (!formRef.value) return
    formRef.value.validate((valid) => {
      if (!valid) return
      if (form.id) {
        const idx = tableRows.value.findIndex(r => r.id === form.id)
        if (idx > -1) tableRows.value[idx] = { ...tableRows.value[idx], ...form }
        ElMessage.success('修改成功(演示)')
      } else {
        tableRows.value.push({ ...form, id: Date.now().toString(), orgName: '默认组织', roleName: '管理员' })
        ElMessage.success('新增成功(演示)')
      }
      dialogVisible.value = false
      page.total = tableRows.value.length
    })
  }
  
  const handleDelete = (row: any) => {
    ElMessageBox.confirm('确定要删除该用户吗？(演示)', '提示', { type: 'warning' })
      .then(() => {
        const idx = tableRows.value.findIndex(r => r.id === row.id)
        if (idx > -1) tableRows.value.splice(idx, 1)
        page.total = tableRows.value.length
        ElMessage.success('删除成功(演示)')
      }).catch(() => {})
  }
  
  const handleRefresh = () => { 
    // 重置搜索条件
    Object.assign(searchForm, { name: '', username: '', tel: '', remark: '' })
    ElMessage.success('刷新成功')
  }
  
  const onPageSizeChange = (size: number) => { page.size = size }
  const onPageChange = (p: number) => { page.current = p }
  const onAvatarUploadSuccess = () => {}
  
  onMounted(() => { initDemo() })
  </script>
  
  <style scoped lang="scss">
  .user-demo {
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
  }
  
  // 搜索弹窗样式 - 完全重写
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