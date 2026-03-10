<template>
  <div class="menu area">
    <div class="layout">
      <el-button-group>
        <el-button @click="handleAdd">新增</el-button>
        <el-button @click="handleReload">刷新</el-button>
      </el-button-group>
    </div>
    <el-divider />
    <el-table
      :data="tableData"
      height="calc(100% - 60px)"
      :tree-props="{ children: 'children' }"
      row-key="id"
      border
    >
      <el-table-column prop="name" label="菜单名称" width="220px" />
      <el-table-column prop="key" label="菜单标识" width="120px" />
      <el-table-column prop="model" label="菜单模式" width="120px">
        <template #default="scope">
          {{ getModelText(scope.row.model) }}
        </template>
      </el-table-column>
      <el-table-column prop="type" label="菜单类型" width="120px">
        <template #default="scope">
          {{ getTypeText(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="resource" label="菜单地址" width="160px" />
      <el-table-column prop="sort" label="菜单排序" width="90px" />
      <el-table-column prop="data" label="备注信息" width="160px" />
      <el-table-column label="相关操作" width="160px">
        <template #default="scope">
          <el-button-group>
            <el-button @click="handleEdit(scope.row.id)" size="small">详情</el-button>
            <el-button @click="handleDelete(scope.row.id)" size="small">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗组件 -->
    <AddDialog
      v-model:visible="dialogVisible"
      :form-data="currentFormData"
      :tree-data="treeData"
      :mode="dialogMode"
      @save="handleSave"
      @cancel="handleDialogCancel"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, type Ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AddDialog from './AddDialog.vue'
import type { MenuItem, MenuNameTreeVO } from '@/apis/sysmanage/menu/type'
import {
  getMenuList,
  getMenuTree,
  getMenuDetail,
  addMenu,
  updateMenu,
  deleteMenu,
  MenuType,
  MenuModel
} from '@/apis/sysmanage/menu/index'

// 响应式数据
const tableData: Ref<MenuItem[]> = ref([])
const treeData: Ref<MenuNameTreeVO[]> = ref([])
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentFormData = ref<MenuItem>()

// 菜单模式文本映射
const getModelText = (model: number | undefined) => {
  if (model === undefined) return '未知'
  const modelMap: Record<number, string> = {
    [MenuModel.TAB]: '标签模式',
    [MenuModel.NEW_PAGE]: '新页模式'
  }
  return modelMap[model] || '未知'
}

// 菜单类型文本映射
const getTypeText = (type: number | undefined) => {
  if (type === undefined) return '未知'
  const typeMap: Record<number, string> = {
    [MenuType.INDEPENDENT]: '独立菜单',
    [MenuType.AFFILIATED]: '附属菜单'
  }
  return typeMap[type] || '未知'
}

// 获取菜单列表数据
const fetchMenuData = async (): Promise<void> => {
  getMenuList(
    (data) => {
      tableData.value = data || []
    },
    (err) => {
      ElMessage({
        type: 'error',
        message: err instanceof Error ? err.message : '获取数据失败'
      })
      console.error('Error fetching menu data:', err)
    }
  )
}

// 获取菜单树数据
const fetchMenuTree = async (): Promise<void> => {
  getMenuTree(
    (data) => {
      treeData.value = data || []
    },
    (err) => {
      ElMessage({
        type: 'error',
        message: err instanceof Error ? err.message : '获取菜单树失败'
      })
      console.error('Error fetching menu tree:', err)
    }
  )
}

// 刷新数据
const refreshData = async (): Promise<void> => {
  await Promise.all([fetchMenuData(), fetchMenuTree()])
}

const handleAdd = (): void => {
  dialogMode.value = 'add'
  currentFormData.value = undefined
  dialogVisible.value = true
}

const handleEdit = async (id: number | undefined): Promise<void> => {
  if (!id) return

  dialogMode.value = 'edit'
  getMenuDetail(
    id.toString(),
    (data) => {
      currentFormData.value = data
      dialogVisible.value = true
    },
    (err) => {
      ElMessage({
        type: 'error',
        message: err instanceof Error ? err.message : '获取详情失败'
      })
      console.error('Error fetching menu detail:', err)
    }
  )
}

const handleSave = async (formData: MenuItem): Promise<void> => {
  if (dialogMode.value === 'add') {
    // 新增菜单
    addMenu(
      formData,
      async () => {
        await refreshData()
        dialogVisible.value = false
        ElMessage({ type: 'success', message: '新增成功!' })
      },
      (err) => {
        ElMessage({
          type: 'error',
          message: err instanceof Error ? err.message : '新增失败'
        })
        console.error('Error adding menu:', err)
      }
    )
  } else {
    // 编辑菜单
    if (formData.id) {
      updateMenu(
        formData.id.toString(),
        formData,
        async () => {
          await refreshData()
          dialogVisible.value = false
          ElMessage({ type: 'success', message: '编辑成功!' })
        },
        (err) => {
          ElMessage({
            type: 'error',
            message: err instanceof Error ? err.message : '编辑失败'
          })
          console.error('Error updating menu:', err)
        }
      )
    } else {
      ElMessage({ type: 'warning', message: '菜单ID不存在' })
    }
  }
}

const handleDelete = async (id: number | undefined): Promise<void> => {
  if (!id) return

  try {
    await ElMessageBox.confirm('您确定要删除该数据吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    deleteMenu(
      id.toString(),
      async () => {
        await refreshData()
        ElMessage({ type: 'success', message: '删除成功!' })
      },
      (err) => {
        ElMessage({
          type: 'error',
          message: err instanceof Error ? err.message : '删除失败'
        })
        console.error('Error deleting menu:', err)
      }
    )
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage({ type: 'error', message: '操作取消或失败' })
    }
  }
}

const handleReload = (): void => {
  refreshData()
  ElMessage({ type: 'success', message: '页面刷新成功!' })
}

const handleDialogCancel = (): void => {
  dialogVisible.value = false
  currentFormData.value = undefined
}

// 生命周期
onMounted(() => {
  refreshData()
})
</script>

<style scoped>
.layout {
  display: flex;
  justify-content: flex-end;
}
</style>
