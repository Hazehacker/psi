<template>
  <!-- 主弹窗 -->
  <el-dialog v-model="visible" title="商品列表" width="40%" class="operation-pop-dialog">
    <!-- 表格 -->
    <div class="table-container">
      <el-table
        ref="mainTable"
        :data="filteredData"
        row-key="id"
        border
        style="width: 100%"
        @expand-change="handleExpandChange"
      >
        <!-- 展开行的子表格 -->
        <template #default-expand-row="{ row }">
          <div v-if="row.children && row.children.length > 0" class="child-table-wrapper">
            <el-table
              :data="row.children"
              row-key="id"
              border
              style="width: 100%"
              @selection-change="(selection) => handleChildSelectChange(row, selection)"
            >
              <el-table-column type="selection" width="50" />
              <el-table-column prop="name" label="子商品名称" width="150" />
              <el-table-column prop="code" label="商品编号" />
              <el-table-column prop="model" label="规格型号" />
              <el-table-column prop="category" label="商品分类" />
              <el-table-column prop="unit" label="商品单位" />
              <el-table-column prop="purchasePrice" label="采购价格" />
              <el-table-column prop="salePrice" label="销售价格" />
              <el-table-column label="数量" width="100">
                <template #default="{ row: childRow }">
                  <el-input-number
                    v-model="childRow.quantity"
                    :min="1"
                    :controls="false"
                    size="small"
                    style="width: 80px"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
        <!-- 合并了展开功能的选择列（放在最左侧并固定） -->
        <el-table-column width="50" align="center" fixed="left">
          <template #default="{ row }">
            <!-- 无子数据时显示勾选框 -->
            <template v-if="!row.children || row.children.length === 0">
              <el-checkbox
                v-model="row._checked"
                @change="handleSingleSelect(row)"
                size="small"
              />
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" width="150" fixed="left" />
        <el-table-column prop="code" label="商品编号" />
        <el-table-column prop="model" label="规格型号" />
        <el-table-column prop="category" label="商品分类" />
        <el-table-column prop="brand" label="商品品牌" />
        <el-table-column prop="unit" label="商品单位" />
        <el-table-column prop="purchasePrice" label="采购价格" />
        <el-table-column prop="salePrice" label="销售价格" />
        <el-table-column prop="barcode" label="商品条码" />
        <el-table-column label="数量" width="100" fixed="right">
          <template #default="{ row }">
            <el-input-number
              v-model="row.quantity"
              :min="1"
              :controls="false"
              size="small"
              style="width: 80px"
            />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[30, 60, 90]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredData.length"
      />
    </div>

    <!-- 底部按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button v-if="showSearchButton" @click="toggleSearchPopup">
          <el-icon><MoreFilled /></el-icon>
        </el-button>

        <div class="right-buttons">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="handleConfirm">选择</el-button>
          <el-button type="success" @click="addItem">新增商品</el-button>
        </div>
      </div>
    </template>

    <!-- 内嵌搜索小弹窗 -->
    <div class="search-popup-container" v-if="showSearchPopup">
      <SearchPopup
        :options="selectOptions"
        @close="showSearchPopup = false"
        @search="handleSearch"
      />
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { MoreFilled } from '@element-plus/icons-vue'
import SearchPopup from './SearchPopup.vue'

const props = defineProps({
  showSearchButton: {
    type: Boolean,
    default: true
  }
})

const visible = ref(true)

//导出事件定义
const emit = defineEmits(['confirm', 'close'])

// 确保每次组件挂载时弹窗都是打开的
onMounted(() => {
  visible.value = true
})

// 监听 visible 变化，当弹窗关闭时触发 close 事件
watch(visible, (newVal) => {
  if (!newVal) {
    emit('close')
  }
})

// 模拟数据（未来可替换API）
const productList = ref([
  {
    id: 1,
    name: '办公剪刀',
    code: '001',
    model: 'M1',
    category: '',
    brand: '得力',
    unit: 'pcs',
    purchasePrice: 8,
    salePrice: 12,
    barcode: '111111',
    quantity: 0,
    children: [
      {
        id: 11,
        name: '办公剪刀-大号',
        code: '001-A',
        model: 'M1-L',
        category: '',
        brand: '得力',
        unit: 'pcs',
        purchasePrice: 10,
        salePrice: 14,
        barcode: '111112',
        quantity: 0
      },
      {
        id: 12,
        name: '办公剪刀-小号',
        code: '001-B',
        model: 'M1-S',
        category: '',
        brand: '得力',
        unit: 'pcs',
        purchasePrice: 6,
        salePrice: 10,
        barcode: '111113',
        quantity: 0
      }
    ]
  },
  {
    id: 2,
    name: '订书机',
    code: '002',
    model: 'ST10',
    category: '',
    brand: '晨光',
    unit: 'set',
    purchasePrice: 18,
    salePrice: 28,
    barcode: '222222',
    quantity: 0
  }
])

// 数据副本
const filteredData = ref([...productList.value])

// 分页
const pagination = ref({
  page: 1,
  pageSize: 30
})

// 主表格引用和选中项
const mainTable = ref()
const mainSelectedItems = ref<any[]>([])
const childSelectedItems = ref<Map<number, any[]>>(new Map())
// 展开行管理
const expandedRows = ref<number[]>([])
// 子表格引用Map
const childTables = ref<Map<number, any>>(new Map())

// 搜索弹窗
const showSearchPopup = ref(false)

// 下拉选项
const selectOptions = ref({
  categories: [] as string[],
  brands: [] as string[],
  types: [] as string[]
})

// 提取选项（自动从商品中收集）
const extractOptions = () => {
  const categories = new Set<string>()
  const brands = new Set<string>()
  const types = new Set<string>()
  const traverse = (list: any[]) => {
    list.forEach((i) => {
      if (i.category) categories.add(i.category)
      brands.add(i.brand)
      if (i.model) types.add(i.model)
      if (i.children) traverse(i.children)
    })
  }
  traverse(productList.value)
  selectOptions.value = {
    categories: Array.from(categories),
    brands: Array.from(brands),
    types: Array.from(types)
  }
}

// 处理展开行变化
const handleExpandChange = (row: any, expanded: boolean) => {
  if (expanded) {
    // 展开时，记录展开状态
    if (!expandedRows.value.includes(row.id)) {
      expandedRows.value.push(row.id)
    }
  } else {
    // 收起时，移除展开状态
    const index = expandedRows.value.indexOf(row.id)
    if (index > -1) {
      expandedRows.value.splice(index, 1)
    }
    // 移除子表格选中状态
    childSelectedItems.value.delete(row.id)
  }
}

// 初始化数据中的选中状态
filteredData.value.forEach(item => {
  if (item.children) {
    item.children.forEach((child: any) => {
      child._checked = false
    })
  }
})

// 移除不再使用的toggleRowExpand函数

// 处理单个选择
const handleSingleSelect = (row: any) => {
  // 确保只有无子数据的行才能被选中
  if (row.children && row.children.length > 0) {
    return
  }
  
  // 更新主选中项列表
  const index = mainSelectedItems.value.findIndex(item => item.id === row.id)
  if (row._checked && index === -1) {
    mainSelectedItems.value.push(row)
  } else if (!row._checked && index > -1) {
    mainSelectedItems.value.splice(index, 1)
  }
}

// 子商品选中变化
const handleChildSelectChange = (parent: any, selectedChildren: any[]) => {
  // 保存子项选中状态
  if (selectedChildren.length > 0) {
    childSelectedItems.value.set(parent.id, selectedChildren)
  } else {
    childSelectedItems.value.delete(parent.id)
  }
}

// 搜索
const handleSearch = async (filters: Record<string, any>) => {
  showSearchPopup.value = false
  ElMessage.info('正在搜索...')

  // 预留API接入接口
  const results = await fetchProducts(filters)

  filteredData.value = results
  ElMessage.success('搜索完成！')
}

// 模拟异步API函数
async function fetchProducts(filters: Record<string, any>) {
  console.log('调用API参数:', filters)
  await new Promise((res) => setTimeout(res, 400)) // 模拟延迟

  return productList.value.filter((item) => {
    const matchText =
      (!filters.keyword ||
        item.name.includes(filters.keyword) ||
        item.code.includes(filters.keyword) ||
        item.barcode.includes(filters.keyword)) &&
      (!filters.category || item.category === filters.category) &&
      (!filters.brand || item.brand === filters.brand) &&
      (!filters.type || item.model === filters.type)
    return matchText
  })
}

// 处理确认按钮点击
const handleConfirm = () => {
  console.log('=== 弹窗确认按钮被点击 ===')
  console.log('主表格选中项:', mainSelectedItems.value)
  console.log('子表格选中项:', childSelectedItems.value)
  
  const selected: any[] = []
  
  // 收集主表格选中的项（无子项的商品）
  mainSelectedItems.value.forEach((item) => {
    console.log('处理主表格项:', item.name, '是否有子项:', item.children?.length)
    if (!item.children || item.children.length === 0) {
      selected.push({
        ...item,
        quantity: item.quantity || 1
      })
      console.log('添加主表格商品:', item.name)
    }
  })
  
  // 收集子表格选中的项
  childSelectedItems.value.forEach((children, parentId) => {
    console.log('处理父商品ID:', parentId, '的子项:', children)
    children.forEach((child) => {
      selected.push({
        ...child,
        parentId: parentId,
        quantity: child.quantity || 1
      })
      console.log('添加子表格商品:', child.name)
    })
  })

  console.log('最终选中的商品列表:', selected)
  
  if (selected.length === 0) {
    ElMessage.warning('请至少选择一个商品')
    return
  }

  console.log('触发 confirm 事件，传递选中商品:', selected)
  emit('confirm', selected)
  visible.value = false // 关闭弹窗
  
  // 重置选中状态
  resetSelectedState()
}

// 重置选中状态
const resetSelectedState = () => {
  filteredData.value.forEach(item => {
    if (item.children) {
      item.children.forEach((child: any) => {
        child._checked = false
      })
    }
  })
  // 清空展开行
  expandedRows.value = []
  childTables.value.clear()
  mainSelectedItems.value = []
  childSelectedItems.value.clear()
}

// 其他按钮逻辑
const cancel = () => {
  visible.value = false
}
const selectItems = () => ElMessage.success('已选择商品！')
const addItem = () => ElMessage.info('新增商品功能开发中')

const toggleSearchPopup = () => {
  showSearchPopup.value = !showSearchPopup.value
}

onMounted(extractOptions)
</script>

<style scoped>
.table-container {
  overflow-x: auto;
  max-height: 400px;
}
.pagination-container {
  text-align: center;
  margin-top: 10px;
}
.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.right-buttons {
  display: flex;
  gap: 10px;
}
.search-popup-container {
  position: absolute;
  bottom: 20px;
  left: 20px;
  z-index: 3001;
}
/* 移除不再使用的expand-button样式 */
/* 自定义展开行样式 */
:deep(.el-table__expanded-cell) {
  padding: 0 !important;
  background-color: #fafafa;
}
.child-table-wrapper {
  padding: 10px;
}
.child-table {
  width: 100%;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}
/* 固定列样式调整 */
:deep(.el-table__fixed-left) {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  z-index: 4;
}
/* 确保两个固定列之间无缝连接 */
:deep(.el-table__fixed-left:last-child) {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.1);
  z-index: 3;
}
</style>
