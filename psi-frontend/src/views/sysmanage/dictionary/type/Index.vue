<template>
  <div class="layout">
    <SearchPopover v-model="searchFrom" @search="handleSearch(1)" @reset="handleReset" />
    <el-button-group>
      <el-button @click="handleAdd">新增</el-button>
      <el-button @click="handleReload">刷新</el-button>
    </el-button-group>
  </div>
  <el-divider />

  <NormalTable
    :tabdatacolumns="tableColumns"
    :tabdata="tablePageData"
    :taboperbtns="tableOperBtns"
    @update:tabdata="handlePageChange"
    @taboper-click="handleTableOperClick"
  >
    <!-- 自定义操作列插槽 -->
    <template #customercell="{ prop, row }">
      <template v-if="prop === 'operate'">
        <el-button-group>
          <el-button @click="handleEdit(row)" size="small">编辑</el-button>
          <el-button @click="handleDelete(row.id)" size="small">删除</el-button>
        </el-button-group>
      </template>
    </template>
  </NormalTable>

  <!-- 弹窗组件 -->
  <CreateDialog
    v-model:visible="dialogVisible"
    :form-data="currentFormData"
    :dictionary-types="props.dictionaryTypes"
    :mode="dialogMode"
    @save="handleSave"
    @cancel="handleDialogCancel"
  />
</template>

<script lang="ts" setup>
import { ref, onMounted, type Ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import type { MyTableColumn, MyTableOperationsBtn, PageDTO } from '@/components/normaltable/type'
import CreateDialog from './CreateDialog.vue'
import SearchPopover from './SearchPopover.vue'
import {
  getDictionaryTypeList,
  addDictionaryType,
  updateDictionaryType,
  deleteDictionaryType
} from '@/apis/sysmanage/dictionary/index'
import type {
  TypePageRequest,
  DictionTypeDTO,
  DictionaryTypeItem
} from '@/apis/sysmanage/dictionary/type'

const props = defineProps<{
  dictionaryTypes: DictionaryTypeItem[]
}>()

// 响应式数据
const tableData: Ref<DictionTypeDTO[]> = ref([])
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentFormData = ref<DictionTypeDTO>() // 弹窗数据

// 搜索表单
const searchFrom = ref<TypePageRequest>({
  name: '',
  code: '',
  remark: ''
})

// 表格分页数据
const tablePageData = ref<PageDTO<DictionTypeDTO>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 30
})

// 表格列配置
const tableColumns = ref<MyTableColumn[]>([
  { prop: 'name', label: '字典类型名称', 'min-width': '200px', align: 'left' },
  { prop: 'code', label: '字典类型编码', 'min-width': '150px', align: 'center' },
  { prop: 'remark', label: '备注', 'min-width': '250px', align: 'center' },
  { prop: 'operate', label: '相关操作', 'min-width': '160px', align: 'center' }
])

// 表格操作按钮配置
const tableOperBtns = ref<MyTableOperationsBtn[]>([
  { text: '编辑', evtname: 'edit', attr: { size: 'small', type: 'primary' } },
  { text: '删除', evtname: 'delete', attr: { size: 'small', type: 'danger' } }
])

// 方法：获取字典类型数据
async function fetchDictionaryData(): Promise<void> {
  try {
    const params: TypePageRequest = {
      pageIndex: tablePageData.value.pageIndex,
      pageSize: tablePageData.value.pageSize,
      name: searchFrom.value.name || undefined
    }

    getDictionaryTypeList(
      params,
      (data) => {
        tablePageData.value.rows = data.rows || []
        tablePageData.value.total = data.total || 0
        tableData.value = data.rows || []
      },
      (error) => {
        ElMessage({
          type: 'warning',
          message: error instanceof Error ? error.message : '获取数据失败'
        })
        console.error('获取字典类型数据失败:', error)
      }
    )
  } catch (error) {
    ElMessage({ type: 'error', message: '服务器响应超时!' })
    console.error('Error fetching dictionary type data:', error)
  }
}

// 搜索方法
function handleSearch(currentPage?: number) {
  if (currentPage) {
    tablePageData.value.pageIndex = currentPage
  }
  fetchDictionaryData()
}

// 重置搜索
function handleReset() {
  searchFrom.value = {
    name: '',
    code: '',
    remark: ''
  }
  tablePageData.value.pageIndex = 1
  fetchDictionaryData()
  ElMessage({
    type: 'success',
    message: '搜索条件已重置!'
  })
}

// 分页变化处理
function handlePageChange(data: PageDTO<DictionTypeDTO>) {
  tablePageData.value.pageIndex = data.pageIndex
  tablePageData.value.pageSize = data.pageSize
  fetchDictionaryData()
}

// NormalTable 操作按钮点击处理
function handleTableOperClick(index: number, row: DictionTypeDTO, optName: string) {
  switch (optName) {
    case 'edit':
      handleEdit(row)
      break
    case 'delete':
      handleDelete(row.id)
      break
    default:
      console.warn('未知的操作类型:', optName)
  }
}

function handleAdd() {
  dialogMode.value = 'add'
  currentFormData.value = undefined
  dialogVisible.value = true
}

async function handleEdit(row?: DictionTypeDTO): Promise<void> {
  dialogMode.value = 'edit'

  if (row) {
    currentFormData.value = {
      id: row.id,
      code: row.code || '',
      name: row.name || '',
      remark: row.remark
    }
    dialogVisible.value = true
  } else {
    ElMessage({
      type: 'warning',
      message: '未找到字典类型信息'
    })
  }
}

function handleSave(formData: DictionTypeDTO): void {
  if (dialogMode.value === 'add') {
    addDictionaryType(
      formData,
      () => {
        fetchDictionaryData()
        dialogVisible.value = false
        ElMessage({ type: 'success', message: '字典类型新增成功!' })
      },
      (error) => {
        ElMessage({ type: 'error', message: '新增失败: ' + (error as Error).message })
      }
    )
  } else {
    // 编辑时直接使用弹窗中传递的 id
    if (formData.id) {
      updateDictionaryType(
        formData.id, // 使用弹窗传递的 id
        formData,
        () => {
          fetchDictionaryData()
          dialogVisible.value = false
          ElMessage({ type: 'success', message: '字典类型编辑成功!' })
        },
        (error) => {
          ElMessage({ type: 'error', message: '编辑失败: ' + (error as Error).message })
        }
      )
    } else {
      ElMessage({ type: 'error', message: '编辑失败: 未找到字典类型ID' })
    }
  }
}

function handleDelete(id?: string): void {
  if (!id) {
    ElMessage({ type: 'warning', message: '未获取到有效ID，无法删除' })
    return
  }

  ElMessageBox.confirm('您确定要删除该字典类型吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      deleteDictionaryType(
        id,
        () => {
          fetchDictionaryData()
          ElMessage({ type: 'success', message: '删除成功!' })
        },
        (error) => {
          ElMessage({ type: 'error', message: '删除失败: ' + (error as Error).message })
        }
      )
    })
    .catch(() => {
      // 取消删除
    })
}

function handleReload() {
  fetchDictionaryData()
  ElMessage({
    type: 'success',
    message: '页面刷新成功!'
  })
}

function handleDialogCancel() {
  dialogVisible.value = false
  currentFormData.value = undefined
}

// 生命周期
onMounted(() => {
  fetchDictionaryData()
})
</script>
