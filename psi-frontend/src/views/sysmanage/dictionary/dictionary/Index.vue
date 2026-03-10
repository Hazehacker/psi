<template>
  <div class="layout">
    <SearchPopover
      v-model="searchFrom"
      :dictionary-types="props.dictionaryTypes"
      @search="handleSearch(1)"
      @reset="handleReset"
    />
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
  getDictionaryList,
  addDictionary,
  updateDictionary,
  deleteDictionary
} from '@/apis/sysmanage/dictionary/index'
import type {
  DictPageRequest,
  DictDTO,
  DictNameItem,
  DictDetailDTO,
  DictionaryTypeItem
} from '@/apis/sysmanage/dictionary/type'

const props = defineProps<{
  dictionaryTypes: DictionaryTypeItem[] // 从父组件接收数据
}>()

// 响应式数据
const tableData: Ref<DictDTO[]> = ref([]) // 表格数据
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentFormData = ref<DictDetailDTO>()

// 搜索表单 - 调整为字典搜索字段
const searchFrom = ref<DictPageRequest>({
  name: '',
  type_name: '',
  value: '',
  remark: ''
})

// 表格分页数据
const tablePageData = ref<PageDTO<DictDTO>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 30
})

// 表格列配置
const tableColumns = ref<MyTableColumn[]>([
  { prop: 'name', label: '字典名称', 'min-width': '220px', align: 'left' },
  { prop: 'type_name', label: '字典类型名称', 'min-width': '120px', align: 'center' },
  { prop: 'value', label: '字典值', 'min-width': '160px', align: 'center' },
  { prop: 'remark', label: '描述', 'min-width': '220px', align: 'center' },
  { prop: 'operate', label: '相关操作', 'min-width': '160px', align: 'center' }
])

// 表格操作按钮配置
const tableOperBtns = ref<MyTableOperationsBtn[]>([
  { text: '编辑', evtname: 'edit', attr: { size: 'small', type: 'primary' } },
  { text: '删除', evtname: 'delete', attr: { size: 'small', type: 'danger' } }
])

const fetchDictionaryData = async (): Promise<void> => {
  try {
    const params: DictPageRequest = {
      pageIndex: tablePageData.value.pageIndex,
      pageSize: tablePageData.value.pageSize,
      name: searchFrom.value.name || undefined,
      value: searchFrom.value.value || undefined,
      tid: searchFrom.value.tid || undefined
    }

    // 正确使用回调函数
    getDictionaryList(
      params,
      (data) => {
        tablePageData.value.rows = data.rows || []
        tablePageData.value.total = data.total || 0
        tableData.value = data.rows || []
      },
      (error) => {
        ElMessage({ type: 'warning', message: '获取数据失败: ' + (error as Error).message })
      }
    )
  } catch (error) {
    ElMessage({ type: 'error', message: '服务器响应超时!' })
    console.error('Error fetching dictionary data:', error)
  }
}

// 搜索方法
const handleSearch = (currentPage?: number): void => {
  if (currentPage) {
    tablePageData.value.pageIndex = currentPage
  }
  fetchDictionaryData()
}

// 重置搜索
const handleReset = (): void => {
  tablePageData.value.pageIndex = 1
  fetchDictionaryData()
  ElMessage({
    type: 'success',
    message: '搜索条件已重置!'
  })
}

// NormalTable 分页变化处理
const handlePageChange = (data: PageDTO<DictDTO>): void => {
  tablePageData.value = data
  console.log('分页变化')
  fetchDictionaryData()
}

// NormalTable 操作按钮点击处理
const handleTableOperClick = (index: number, row: DictDTO, evtname: string): void => {
  switch (evtname) {
    case 'edit':
      handleEdit(row)
      break
    case 'delete':
      handleDelete(row?.id)
      break
    default:
      console.warn('未知的操作类型:', evtname)
  }
}

const handleAdd = (): void => {
  dialogMode.value = 'add'
  currentFormData.value = undefined
  dialogVisible.value = true
}

const handleEdit = async (row: DictDTO): Promise<void> => {
  dialogMode.value = 'edit'

  if (row.id) {
    currentFormData.value = {
      id: row.id,
      name: row.name,
      type_name: row.type_name,
      tid: row.tid,
      value: row.value,
      remark: row.remark
    }
    dialogVisible.value = true
  } else {
    ElMessage({
      type: 'warning',
      message: '未找到字典信息'
    })
  }
}

const handleSave = async (formData: DictDetailDTO): Promise<void> => {
  if (dialogMode.value === 'add') {
    console.log('add')
    addDictionary(
      formData,
      () => {
        fetchDictionaryData()
        dialogVisible.value = false
        ElMessage({ type: 'success', message: '字典新增成功!' })
      },
      (error) => {
        ElMessage({ type: 'error', message: '新增失败: ' + (error as Error).message })
      }
    )
  } else {
    console.log('edit:', formData)
    if (formData?.id) {
      updateDictionary(
        formData,
        () => {
          fetchDictionaryData()
          dialogVisible.value = false
          ElMessage({ type: 'success', message: '字典编辑成功!' })
        },
        (error) => {
          ElMessage({ type: 'error', message: '编辑失败: ' + (error as Error).message })
        }
      )
    }
  }
}

const handleDelete = async (id: string): Promise<void> => {
  try {
    await ElMessageBox.confirm('您确定要删除该字典吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // 使用回调函数模式调用 deleteDictionary
    deleteDictionary(
      id,
      () => {
        // 成功回调
        fetchDictionaryData()
        ElMessage({
          type: 'success',
          message: '删除成功!'
        })
      },
      (error) => {
        // 失败回调
        ElMessage({
          type: 'warning',
          message: error instanceof Error ? error.message : '删除失败'
        })
        console.error('Error deleting dictionary:', error)
      }
    )
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage({
        type: 'error',
        message: '服务器响应超时!'
      })
      console.error('Error in delete confirmation:', error)
    }
  }
}

const handleReload = (): void => {
  fetchDictionaryData()
  ElMessage({
    type: 'success',
    message: '页面刷新成功!'
  })
}

const handleDialogCancel = (): void => {
  dialogVisible.value = false
  currentFormData.value = undefined
}

// 生命周期
onMounted(() => {
  fetchDictionaryData()
})
</script>
