<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-26 01:49:50
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 10:30:17
 * @FilePath: \psi-frontend\src\views\warehouse\inventory\InventoryQuery.vue
 * @Description: 仓库管理-库存盘点主页面
-->
<template>
  <div class="inventory area">
    <div class="layout">
      <SearchPopover v-model="searchFrom" @search="handleSearch(1)" />
      <el-button-group>
        <el-button @click="handleBuild">生成盘点单</el-button>
        <el-button @click="handleExport">导出</el-button>
        <el-button @click="handleReload">刷新</el-button>
      </el-button-group>
    </div>
    <el-divider />

    <NormalTable
      :tabdata="tablePageData"
      :istabexpand="false"
      :istabmultiple="false"
      :tabattr="tableAttr"
      @page-change="handlePageChange"
      @taboper-click="handleTableOperClick"
    >
      <!-- 多级表头定义 - 使用 columns 插槽 -->
      <template #columns>
        <el-table-column prop="name" label="商品名称" width="220px" fixed="left" />
        <el-table-column prop="totalStock" label="库存数量" width="120px" />
        <!-- 动态仓库列 -->
        <el-table-column label="仓库">
          <template v-for="wh in tableColumn" :key="wh.warehouseId">
            <el-table-column :label="wh.name">
              <!-- 库存数 -->
              <el-table-column
                :prop="`record.warehouse_${wh.warehouseId}.basisAlias`"
                label="库存数"
                width="90px"
              />
              <!-- 盘点数 -->
              <el-table-column label="盘点数" width="120px">
                <template #default="scope">
                  <template v-if="scope.row.hasOwnProperty('attrs') && scope.row.attrs.length > 0">
                    <span>-</span>
                  </template>
                  <template v-else>
                    <template
                      v-if="
                        scope.row.record &&
                        Array.isArray(scope.row.record[`warehouse_${wh.warehouseId}`]?.enter)
                      "
                    >
                      <div class="unitGroup">
                        <template
                          v-for="(item, index) in scope.row.record[`warehouse_${wh.warehouseId}`]
                            .enter"
                          :key="index"
                        >
                          <div class="unitItem">
                            <input
                              type="text"
                              v-model="item.nums"
                              @input="unitHandle(false)"
                              class="unit-input"
                            />
                            <span @click="unitNameClick">{{ item.name }}</span>
                          </div>
                        </template>
                      </div>
                    </template>
                    <template
                      v-else-if="
                        scope.row.record && scope.row.record[`warehouse_${wh.warehouseId}`]
                      "
                    >
                      <input
                        type="text"
                        v-model="scope.row.record[`warehouse_${wh.warehouseId}`].enter"
                        @input="
                          handleCheckCountChange(scope.row, `warehouse_${wh.warehouseId}`, $event)
                        "
                        class="check-input"
                      />
                    </template>
                    <template v-else>
                      <span>-</span>
                    </template>
                  </template>
                </template>
              </el-table-column>
              <!-- 盘盈盘亏 -->
              <el-table-column label="盘盈盘亏" width="120px">
                <template #default="scope">
                  <template v-if="scope.row.hasOwnProperty('attrs') && scope.row.attrs.length > 0">
                    <span>-</span>
                  </template>
                  <template v-else>
                    <span
                      :class="
                        getDiffClass(
                          scope.row.record && scope.row.record[`warehouse_${wh.warehouseId}`]?.diff
                        )
                      "
                    >
                      {{
                        (scope.row.record &&
                          scope.row.record[`warehouse_${wh.warehouseId}`]?.diffAlias) ||
                        '-'
                      }}
                    </span>
                  </template>
                </template>
              </el-table-column>
            </el-table-column>
          </template>
        </el-table-column>

        <el-table-column prop="number" label="商品编号" width="160px" />
        <el-table-column prop="spec" label="规格型号" width="160px" />
        <el-table-column prop="categoryName" label="商品分类" width="120px" />
        <el-table-column prop="brand" label="商品品牌" width="120px" />
        <el-table-column prop="unit" label="商品单位" width="120px" />
        <el-table-column prop="code" label="商品条码" width="160px" />
        <el-table-column prop="remark" label="商品备注" min-width="160px" />
      </template>

      <!-- 自定义单元格内容 -->
      <template #customercell="{ column, prop, row }">
        <!-- 盘点数输入框 -->
        <template v-if="prop.startsWith('warehouse_') && prop.endsWith('.checkCount')">
          <template v-if="row.hasOwnProperty('attrs') && row.attrs && row.attrs.length > 0">
            <span>-</span>
          </template>
          <template v-else>
            <template v-if="Array.isArray(row[prop])">
              <div class="unitGroup">
                <template v-for="(item, index) in row[prop]" :key="index">
                  <div class="unitItem">
                    <input
                      type="text"
                      v-model="item.nums"
                      @input="unitHandle(false)"
                      class="unit-input"
                    />
                    <span @click="unitNameClick">{{ item.name }}</span>
                  </div>
                </template>
              </div>
            </template>
            <template v-else>
              <input
                type="text"
                v-model="row[prop]"
                @input="handleCheckCountChange(row, prop, $event)"
                class="check-input"
              />
            </template>
          </template>
        </template>

        <!-- 盘盈盘亏显示 -->
        <template v-else-if="prop.startsWith('warehouse_') && prop.endsWith('.diff')">
          <template v-if="row.hasOwnProperty('attrs') && row.attrs && row.attrs.length > 0">
            <span>-</span>
          </template>
          <template v-else>
            <span :class="getDiffClass(row[prop])">
              {{ row[prop] || '-' }}
            </span>
          </template>
        </template>

        <!-- 库存数显示 -->
        <template v-else-if="prop.startsWith('warehouse_') && prop.endsWith('.stockNum')">
          <template v-if="row.hasOwnProperty('attrs') && row.attrs && row.attrs.length > 0">
            <span>-</span>
          </template>
          <template v-else>
            {{ row[prop] || '-' }}
          </template>
        </template>

        <!-- 商品分类显示 -->
        <template v-else-if="prop === 'categoryName'">
          {{ row.categoryName || '-' }}
        </template>

        <!-- 辅助属性名称显示 -->
        <template v-else-if="prop === 'name' && row.hasOwnProperty('pid')">
          <span style="color: #909399">└─ {{ row.attrName || row.name }}</span>
        </template>

        <!-- 辅助属性编号显示 -->
        <template v-else-if="prop === 'number' && row.hasOwnProperty('pid')">
          <span style="color: #909399">{{ row.attrCode || '-' }}</span>
        </template>

        <!-- 辅助属性库存显示 -->
        <template v-else-if="prop === 'totalStock' && row.hasOwnProperty('pid')">
          <span style="color: #909399">{{ row.totalStock || '-' }}</span>
        </template>

        <!-- 默认显示 -->
        <template v-else>
          {{ row[prop] || '-' }}
        </template>
      </template>
    </NormalTable>

    <!-- 盘盈单弹窗 -->
    <InventoryProfitDialog
      v-model="dialogVisible"
      :mode="currentDialogMode"
      :table-data="currentTableData"
      :inventory-verify-data="inventoryVerifyData"
      :current-type="currentDialogType"
      @build-entry="handleBuildEntry"
      @build-extry="handleBuildExtry"
      @confirm="handleProfitConfirm"
      @close="handleProfitClose"
      @export-verify="handleExportVerify"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import type { PageDTO } from '@/components/normaltable/type'
import SearchPopover from './SearchPopover.vue'
import InventoryProfitDialog from './InventoryProfitDialog.vue'
import { getInventoryList, exportInventoryExcel } from '@/apis/warehouse/inventory/index'
import type { InventoryList, InventoryListRequest } from '@/apis/warehouse/inventory/type'
import {
  getInventoryPlus,
  getInventoryReduce,
  exportInventoryVerify
} from '@/apis/warehouse/check/index'
import type { InventoryVerifyDTO, InventoryVerifyRequest } from '@/apis/warehouse/check/type'

// 响应式数据
const router = useRouter()
const tableColumn = ref<any[]>([])
const summary = ref<any[]>([]) // 添加汇总数据
const inventoryVerifyData = ref<InventoryVerifyDTO[]>([]) //盘点数据

// 弹窗相关状态
const dialogVisible = ref(false)
const currentDialogMode = ref<'select' | 'list'>('select')
const currentDialogType = ref<'profit' | 'loss'>('profit')
const currentTableData = ref<any[]>([])

// 分页数据 - 使用 NormalTable 的 PageDTO 格式
const tablePageData = reactive<PageDTO<any>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 30
})

// 搜索表单
const searchFrom = ref<InventoryListRequest>({
  goodsName: '',
  goodsNumber: '',
  goodsSpec: '',
  goodsCategoryId: '',
  goodsBrand: '',
  goodsCode: '',
  goodsRemark: '',
  warehouseId: [],
  stockState: undefined
})

// 表格属性配置
const tableAttr = reactive({
  'header-cell-style': { textAlign: 'center', backgroundColor: '#f5f7fa' },
  'cell-style': { textAlign: 'center' },
  'tree-props': { children: 'attrs' }, // 修改为 attrs，匹配接口字段名
  'row-key': 'id' // 设置行key为id
})

// 处理数据，将仓库记录转换为表格列数据
function processInventoryData(data: InventoryList[]) {
  return data.map(function (item) {
    const processedItem: Record<string, any> = {
      ...item,
      key: item.id, // 添加key用于树形表格
      record: {} as Record<string, any> // 添加record对象用于存储仓库数据
    }

    // 处理仓库数据 - 根据样例数据结构
    if (item.goodsWarehouses && Array.isArray(item.goodsWarehouses)) {
      item.goodsWarehouses.forEach(function (wh) {
        if (wh.warehouseId) {
          processedItem.record[`warehouse_${wh.warehouseId}`] = {
            basis: wh.stockNum || 0, // 库存数
            basisAlias: wh.stockNum || 0, // 库存数显示
            enter: '', // 盘点数，初始为空，用户输入后计算差异
            diff: 0, // 差异
            diffAlias: '', // 差异显示
            warehouse: wh.warehouseId // 仓库ID
          }
        }
      })
    }

    // 如果有辅助属性，处理属性数据
    if (item.attrs && item.attrs.length > 0) {
      processedItem.attrs = item.attrs.map(function (attr) {
        const attrItem: Record<string, any> = {
          ...attr,
          key: attr.attrId,
          pid: item.id,
          record: {} as Record<string, any>,
          // 辅助属性使用attrName作为显示名称
          name: attr.attrName,
          // 辅助属性没有编号，使用商品编号
          number: item.number,
          // 辅助属性没有分类、品牌等信息
          categoryName: '-',
          brand: '-',
          spec: '-',
          unit: '-',
          code: '-',
          remark: '-',
          threshold: '-'
        }

        // 处理属性的仓库数据
        if (attr.warehouses && Array.isArray(attr.warehouses)) {
          attr.warehouses.forEach(function (wh) {
            if (wh.warehouseId) {
              attrItem.record[`warehouse_${wh.warehouseId}`] = {
                basis: wh.stockNum || 0,
                basisAlias: wh.stockNum || 0,
                enter: '',
                diff: 0,
                diffAlias: '',
                warehouse: wh.warehouseId
              }
            }
          })
        }

        return attrItem
      })
    }

    return processedItem
  })
}

// 处理盘点数变化
function handleCheckCountChange(row: any, warehouseKey: string, event?: Event | string) {
  if (!row.record || !row.record[warehouseKey]) return

  const record = row.record[warehouseKey]
  const basis = record.basis

  // 优先使用传入的 event（确保立即获取到最新输入值），否则使用绑定的 record.enter
  let enterValue: any = undefined
  if (event !== undefined && event !== null) {
    if (typeof event === 'string') {
      enterValue = event
    } else {
      const target = (event as Event).target as HTMLInputElement | null
      enterValue = target ? target.value : record.enter
    }
  } else {
    enterValue = record.enter
  }

  const enter = enterValue

  // 计算差异
  if (enter === '' || enter === null || enter === undefined) {
    record.diff = 0
    record.diffAlias = ''
  } else {
    const diff = parseFloat(enter) - parseFloat(basis)
    record.diff = diff
    record.diffAlias = diff > 0 ? `+${diff}` : `${diff}`

    // 更新盘点数据
    updateInventoryVerifyData(row, warehouseKey, diff)
  }
}

// 更新盘点数据
function updateInventoryVerifyData(row: any, warehouseKey: string, diff: number) {
  const warehouseId = warehouseKey.replace('warehouse_', '')

  // 查找是否已存在该商品的盘点数据
  const existingIndex = inventoryVerifyData.value.findIndex(
    (item) => item.goodId === row.id && item.warehouseId === warehouseId
  )

  if (existingIndex > -1) {
    // 更新现有数据
    inventoryVerifyData.value[existingIndex].inventoryDifference = diff
  } else {
    // 添加新数据
    inventoryVerifyData.value.push({
      goodId: row.id,
      warehouseId: warehouseId,
      attrId: row.hasOwnProperty('pid') ? row.attrId : undefined,
      inventoryDifference: diff
    })
  }
}

// 单位处理
function unitHandle(_isInit: boolean): boolean | string {
  let effect: boolean | string = true
  // 使用参数以避免未使用警告
  void _isInit

  // 结构数据
  const rows: any[] = []
  for (const item of tablePageData.rows || []) {
    if (item.hasOwnProperty('attrs') && item.attrs.length > 0) {
      for (const attr of item.attrs) {
        rows.push(attr)
      }
    } else {
      rows.push(item)
    }
  }

  // 数据处理
  summary.value = []
  inventoryVerifyData.value = [] // 重置盘点数据

  for (const row of rows) {
    const goods = row.hasOwnProperty('pid')
      ? (tablePageData.rows || []).find((item) => item.id === row.pid)
      : row

    if (!row.record) continue

    loop: for (const key in row.record) {
      const obj = row.record[key]
      if (!obj) continue

      // 单位判断
      if (Array.isArray(obj.enter)) {
        let nums = 0
        for (const node of obj.enter) {
          if (node.nums !== '' && node.nums !== null && node.nums !== undefined) {
            if (validateNumber(node.nums)) {
              if (parseFloat(node.nums) !== 0) {
                // 这里需要实现单位换算逻辑
                const compute = parseFloat(node.nums)
                nums += compute
              }
            } else {
              obj.diff = 0
              obj.diffAlias = ''
              if (effect === true) {
                effect = `${goods.name}盘点数不正确!`
              }
              continue loop
            }
          }
        }

        if (nums === 0) {
          obj.diff = 0
          obj.diffAlias = ''
        } else {
          const diff = nums - obj.basis
          const diffAbs = Math.abs(diff)
          obj.diff = diff
          obj.diffAlias = diff > 0 ? `+${diffAbs}` : `-${diffAbs}`
          summary.value.push({
            goods: goods.id,
            attr: row.hasOwnProperty('pid') ? row.attrName : '',
            warehouse: obj.warehouse,
            unit: goods.unit || '',
            nums: diff
          })

          // 添加到盘点数据
          updateInventoryVerifyData(row, key, diff)
        }
      } else {
        if (obj.enter === '' || obj.enter === null || obj.enter === undefined) {
          obj.diff = 0
          obj.diffAlias = ''
        } else {
          if (validateNumber(obj.enter)) {
            const diff = parseFloat(obj.enter) - obj.basis
            obj.diff = diff
            obj.diffAlias = diff.toString()
            summary.value.push({
              goods: goods.id,
              attr: row.hasOwnProperty('pid') ? row.attrName : '',
              warehouse: obj.warehouse,
              unit: '',
              nums: diff
            })

            // 添加到盘点数据
            updateInventoryVerifyData(row, key, diff)
          } else {
            obj.diff = 0
            obj.diffAlias = ''
            if (effect === true) {
              effect = `${goods.name}盘点数不正确!`
            }
          }
        }
      }
    }
  }
  return effect
}

// 数字验证
function validateNumber(value: any): boolean {
  if (value === '' || value === null || value === undefined) return false
  const num = parseFloat(value)
  return !isNaN(num) && isFinite(num)
}

// 单位名称点击
function unitNameClick() {
  // 单位名称点击逻辑
  console.log('单位名称点击')
}

// 获取差异样式
function getDiffClass(diff: number) {
  if (diff > 0) return 'profit'
  if (diff < 0) return 'loss'
  return ''
}

// 获取库存数据
async function fetchInventoryData(): Promise<void> {
  try {
    const params: InventoryListRequest = {
      pageIndex: tablePageData.pageIndex,
      pageSize: tablePageData.pageSize,
      ...searchFrom.value
    }

    // 清理空值参数
    Object.keys(params).forEach(function (key) {
      if (
        params[key] === '' ||
        params[key] === null ||
        params[key] === undefined ||
        (Array.isArray(params[key]) && params[key].length === 0)
      ) {
        delete params[key]
      }
    })

    // 使用回调方式调用API
    await getInventoryList(
      params,
      (data) => {
        console.log('库存API响应成功:', data)

        // 处理数据格式
        const processedData = processInventoryData(data.rows || [])

        // 转换数据格式适配 NormalTable
        tablePageData.rows = processedData
        tablePageData.total = data.total || 0

        // 设置表格列（仓库列）
        extractWarehouseColumns(data.rows || [])

        // 重置盘点数据
        inventoryVerifyData.value = []
      },
      (error) => {
        console.error('库存API响应失败:', error)
        ElMessage({
          type: 'error',
          message: '获取库存数据失败: ' + (error.message || '未知错误')
        })
      }
    )
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '服务器响应超时!'
    })
    console.error('Error fetching inventory data:', error)
  }
}

// 从数据中提取仓库列信息
function extractWarehouseColumns(data: InventoryList[]) {
  const warehouseMap = new Map()

  data.forEach(function (item) {
    // 处理主商品的仓库
    if (item.goodsWarehouses && Array.isArray(item.goodsWarehouses)) {
      item.goodsWarehouses.forEach(function (wh) {
        if (wh.warehouseId && wh.warehouseName) {
          warehouseMap.set(wh.warehouseId, {
            warehouseId: wh.warehouseId,
            name: wh.warehouseName,
            key: `warehouse_${wh.warehouseId}`
          })
        }
      })
    }

    // 处理辅助属性的仓库（如果有的话）
    if (item.attrs && Array.isArray(item.attrs)) {
      item.attrs.forEach(function (attr) {
        if (attr.warehouses && Array.isArray(attr.warehouses)) {
          attr.warehouses.forEach(function (wh) {
            if (wh.warehouseId && wh.warehouseName) {
              warehouseMap.set(wh.warehouseId, {
                warehouseId: wh.warehouseId,
                name: wh.warehouseName,
                key: `warehouse_${wh.warehouseId}`
              })
            }
          })
        }
      })
    }
  })

  tableColumn.value = Array.from(warehouseMap.values())
}

// 搜索方法
function handleSearch(resetPage?: number): void {
  if (resetPage) {
    tablePageData.pageIndex = 1
  }
  fetchInventoryData()
}

// NormalTable 分页变化处理
function handlePageChange(data: PageDTO<any>): void {
  tablePageData.pageIndex = data.pageIndex
  tablePageData.pageSize = data.pageSize
  fetchInventoryData()
}

// NormalTable 操作按钮点击处理
function handleTableOperClick(index: number, row: any, evtname: string): void {
  switch (evtname) {
    case 'detail':
      // 可以添加详情查看逻辑
      console.log('查看详情:', row)
      break
    default:
      console.warn('未知的操作类型:', evtname)
  }
}

// 生成盘点单 - 打开选择界面
function handleBuild(): void {
  const effect = unitHandle(false)
  if (effect === true) {
    currentDialogMode.value = 'select'
    dialogVisible.value = true
  } else {
    ElMessage({
      type: 'warning',
      message: String(effect)
    })
  }
}

// 弹窗关闭处理
function handleProfitClose() {
  console.log('盘点弹窗关闭')
  // 可以在这里重置状态
  currentDialogMode.value = 'select'
  currentTableData.value = []
}

// 导出
async function handleExport(): Promise<void> {
  try {
    const params: InventoryListRequest = {
      ...searchFrom.value
    }

    // 清理空值参数
    Object.keys(params).forEach(function (key) {
      if (
        params[key] === '' ||
        params[key] === null ||
        params[key] === undefined ||
        (Array.isArray(params[key]) && params[key].length === 0)
      ) {
        delete params[key]
      }
    })

    await exportInventoryExcel(
      params,
      (blob) => {
        // 创建下载链接
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `库存数据_${new Date().getTime()}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        ElMessage({
          type: 'success',
          message: '导出成功'
        })
      },
      (error) => {
        console.error('导出失败:', error)
        ElMessage({
          type: 'error',
          message: '导出失败: ' + (error.message || '未知错误')
        })
      }
    )
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '导出过程出错'
    })
    console.error('Export error:', error)
  }
}

// 导出盘点单
async function handleExportVerify(): Promise<void> {
  try {
    await exportInventoryVerify(
      (blob) => {
        // 创建下载链接
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `库存盘点单_${new Date().getTime()}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        ElMessage({
          type: 'success',
          message: '盘点单导出成功'
        })
      },
      (error) => {
        console.error('盘点单导出失败:', error)
        ElMessage({
          type: 'error',
          message: '盘点单导出失败: ' + (error.message || '未知错误')
        })
      }
    )
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '盘点单导出过程出错'
    })
    console.error('Export verify error:', error)
  }
}

// 刷新
function handleReload(): void {
  fetchInventoryData()
  ElMessage({
    type: 'success',
    message: '页面刷新成功!'
  })
}

// 生成盘盈单（点击弹窗中的"盘盈单"）
async function handleBuildEntry(): Promise<void> {
  // 先计算汇总并校验
  const effect = unitHandle(false)
  if (effect !== true) {
    ElMessage({ type: 'warning', message: String(effect) })
    return
  }

  // 查找盘盈数据（nums>0）
  const profitList = (summary.value || []).filter((it) => Number(it.nums) > 0)
  if (profitList && profitList.length > 0) {
    try {
      // 调用盘盈单API获取数据
      const verifyRequest: InventoryVerifyRequest = {
        inventoryVerifyList: inventoryVerifyData.value.filter(
          (item) => item.inventoryDifference && item.inventoryDifference > 0
        )
      }

      await getInventoryPlus(
        verifyRequest,
        (data) => {
          console.log('盘盈单数据获取成功:', data)
          // 保存到会话存储，并跳转到其他入库单页面
          try {
            sessionStorage.setItem('inventoryProfitData', JSON.stringify(data.rows || []))
          } catch (e) {
            console.warn('保存盘盈数据到 sessionStorage 失败', e)
          }
          router.push({ path: '/other-inbound' })
        },
        (error) => {
          console.error('获取盘盈单数据失败:', error)
          ElMessage({
            type: 'error',
            message: '获取盘盈单数据失败: ' + (error.message || '未知错误')
          })
        }
      )
    } catch (error) {
      console.error('盘盈单处理错误:', error)
      ElMessage({
        type: 'error',
        message: '盘盈单处理过程出错'
      })
    }
  } else {
    // 关闭弹窗并提示
    dialogVisible.value = false
    ElMessage({ type: 'info', message: '未匹配到盘盈单数据' })
  }
}

// 生成盘亏单（点击弹窗中的"盘亏单"）
async function handleBuildExtry(): Promise<void> {
  const effect = unitHandle(false)
  if (effect !== true) {
    ElMessage({ type: 'warning', message: String(effect) })
    return
  }

  // 查找盘亏数据（nums<0）
  const lossList = (summary.value || []).filter((it) => Number(it.nums) < 0)
  if (lossList && lossList.length > 0) {
    try {
      // 调用盘亏单API获取数据
      const verifyRequest: InventoryVerifyRequest = {
        inventoryVerifyList: inventoryVerifyData.value.filter(
          (item) => item.inventoryDifference && item.inventoryDifference < 0
        )
      }

      await getInventoryReduce(
        verifyRequest,
        (data) => {
          console.log('盘亏单数据获取成功:', data)
          try {
            sessionStorage.setItem('inventoryLossData', JSON.stringify(data.rows || []))
          } catch (e) {
            console.warn('保存盘亏数据到 sessionStorage 失败', e)
          }
          router.push({ path: '/other-outbound' })
        },
        (error) => {
          console.error('获取盘亏单数据失败:', error)
          ElMessage({
            type: 'error',
            message: '获取盘亏单数据失败: ' + (error.message || '未知错误')
          })
        }
      )
    } catch (error) {
      console.error('盘亏单处理错误:', error)
      ElMessage({
        type: 'error',
        message: '盘亏单处理过程出错'
      })
    }
  } else {
    dialogVisible.value = false
    ElMessage({ type: 'info', message: '未匹配到盘亏单数据' })
  }
}

// 处理盘盈单确认
function handleProfitConfirm(profitData: any[]) {
  console.log('生成盘盈单数据:', profitData)
  // 这里可以调用生成盘盈单的API
  ElMessage.success(`成功生成盘盈单，共 ${profitData.length} 条记录`)
}

// 生命周期
onMounted(function () {
  fetchInventoryData()
})
</script>

<style lang="scss" scoped>
.layout {
  display: flex;
  justify-content: space-between;
}
.unitGroup {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}
.unitItem {
  position: relative;
  width: 100%;
}
.unitItem input {
  padding: 0 25px 0 0;
  font-size: 12px;
}
.unitItem span {
  position: absolute;
  right: 0;
  line-height: 40px;
  width: 25px;
  text-align: center;
}
.check-input {
  width: 100% !important;
  border: none;
  text-align: center;
  ::deep(.el-input__inner):focus {
    outline: none;
  }
}
/* 覆盖表格样式，让第一列居左，其他列居中 */
:deep(.el-table .el-table__header-wrapper .el-table__header .el-table__cell:first-child),
:deep(.el-table .el-table__body-wrapper .el-table__body .el-table__cell:first-child) {
  text-align: left !important;
}
/* 盘盈盘亏样式 */
.profit {
  color: #588342;
  font-weight: bold;
}
.loss {
  color: #c93636;
  font-weight: bold;
}
</style>
