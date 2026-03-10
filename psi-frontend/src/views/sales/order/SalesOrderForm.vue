<template>
  <div class="sales-order-form">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 左上角搜索组件 -->
        <GoodSearchForm
          :model-value="searchModel"
          @update:model-value="(val) => Object.assign(searchModel, val)"
          class="mr-8"
          @search="onGoodSearch"
          ref="goodSearchRef"
        />
      </div>
      <div class="toolbar-right">
        <div class="btn-join">
          <!-- 批量操作按钮组：当有选中项时显示 -->
          <template v-if="selected.length > 0">
            <el-popover
              class="btnGroupPopover"
              placement="bottom"
              popper-class="blockPopover"
              trigger="click"
            >
              <template #reference>
                <el-button type="info">操作</el-button>
              </template>
              <ul>
                <li @click="onBatchExamine(0)">审核</li>
                <li @click="onBatchExamine(1)">反审核</li>
                <li @click="onBatchCheck(0)">核对</li>
                <li @click="onBatchCheck(1)">反核对</li>
                <li @click="exportDetailReport">导出详情报表</li>
                <li @click="exportSimpleReport">导出简易报表</li>
              </ul>
            </el-popover>
            <el-button @click="onBatchDelete" type="info">删除</el-button>
          </template>

          <!-- 新增的批量按钮 -->
          <el-button type="warning" @click="openBatchDialog">批量</el-button>

          <!-- 刷新按钮 -->
          <el-button :loading="refreshing" type="primary" @click="onRefresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 表格 + 固定横向滚动条 + 状态栏 -->
    <div class="table-section">
      <!-- 表格滚动容器 -->
      <div ref="tableWrapper" class="table-wrapper">
        <el-table
          ref="tableRef"
          :data="pagedData"
          border
          style="width: 100%"
          :header-cell-style="{ background: '#fafafa' }"
          @selection-change="handleSelectionChange"
        >
          <!-- 左侧固定选择列 -->
          <el-table-column fixed="left" type="selection" width="48" align="center" />

          <!-- 所属组织 -->
          <el-table-column
            prop="org"
            label="所属组织"
            width="120"
            align="center"
            header-align="center"
          />

          <!-- 客户 -->
          <el-table-column
            prop="customer"
            label="客户"
            width="120"
            align="center"
            header-align="center"
          />

          <!-- 单据时间（可排序） -->
          <el-table-column label="单据时间" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据时间</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billTime' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billTime', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billTime' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billTime', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ row.billTime }}
            </template>
          </el-table-column>

          <!-- 单据编号（可排序） -->
          <el-table-column label="单据编号" width="200" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据编号</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billNo' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billNo', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billNo' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billNo', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ row.billNo }}
            </template>
          </el-table-column>

          <!-- 单据金额（可排序） -->
          <el-table-column label="单据金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.billAmount) }}
            </template>
          </el-table-column>

          <!-- 实际金额（可排序） -->
          <el-table-column label="实际金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">实际金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'realAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('realAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'realAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('realAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.realAmount) }}
            </template>
          </el-table-column>

          <!-- 单据收款（可排序） -->
          <el-table-column label="单据收款" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据收款</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'receiptAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('receiptAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'receiptAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('receiptAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.receiptAmount) }}
            </template>
          </el-table-column>

          <!-- 核销金额（可排序） -->
          <el-table-column label="核销金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">核销金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'writeoffAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('writeoffAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'writeoffAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('writeoffAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.writeoffAmount) }}
            </template>
          </el-table-column>

          <!-- 单据费用（可排序） -->
          <el-table-column label="单据费用" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据费用</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'feeAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('feeAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'feeAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('feeAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.feeAmount) }}
            </template>
          </el-table-column>

          <!-- 关联人员 -->
          <el-table-column
            prop="relatedPerson"
            label="关联人员"
            width="130"
            align="center"
            header-align="center"
          />

          <!-- 审核状态（黑白文字） -->
          <el-table-column label="审核状态" width="100" align="center" header-align="center">
            <template #default="{ row }">
              <span>{{ row.auditStatus }}</span>
            </template>
          </el-table-column>

          <!-- 核销状态：已核销 / 部分核销 时带小表格弹出 -->
          <el-table-column label="核销状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.writeoffStatus">
                <span>{{ row.writeoffStatus }}</span>
                <el-popover
                  v-if="row.writeoffStatus === '已核销' || row.writeoffStatus === '部分核销'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>单据类型</th>
                          <th>单据时间</th>
                          <th>单据编号</th>
                          <th>核销金额</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.writeoffDetails" :key="i">
                          <td>{{ d.billType }}</td>
                          <td>{{ d.billTime }}</td>
                          <td>{{ d.billNo }}</td>
                          <td class="num">{{ formatNumber(d.amount) }}</td>
                        </tr>
                        <tr v-if="!row.writeoffDetails || row.writeoffDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 费用状态：未结算 时可展开 -->
          <el-table-column label="费用状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.feeStatus">
                <span>{{ row.feeStatus }}</span>
                <el-popover
                  v-if="row.feeStatus === '未结算'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>支出类别</th>
                          <th>金额</th>
                          <th>结算金额</th>
                          <th>结算状态</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.feeDetails" :key="i">
                          <td>{{ d.expenseType }}</td>
                          <td class="num">{{ formatNumber(d.amount) }}</td>
                          <td class="num">{{ formatNumber(d.settleAmount) }}</td>
                          <td>{{ d.settleStatus }}</td>
                        </tr>
                        <tr v-if="!row.feeDetails || row.feeDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 发票状态：已开具 时可展开 -->
          <el-table-column label="发票状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.invoiceStatus">
                <span>{{ row.invoiceStatus }}</span>
                <el-popover
                  v-if="row.invoiceStatus === '已开具'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>开票日期</th>
                          <th>发票号码</th>
                          <th>发票抬头</th>
                          <th>发票金额</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.invoiceDetails" :key="i">
                          <td>{{ d.invoiceDate }}</td>
                          <td>{{ d.invoiceNo }}</td>
                          <td>{{ d.invoiceTitle }}</td>
                          <td class="num">{{ formatNumber(d.invoiceAmount) }}</td>
                        </tr>
                        <tr v-if="!row.invoiceDetails || row.invoiceDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 核对状态（普通文字） -->
          <el-table-column
            prop="checkStatus"
            label="核对状态"
            width="100"
            align="center"
            header-align="center"
          />

          <!-- 制单人 -->
          <el-table-column
            prop="maker"
            label="制单人"
            width="100"
            align="center"
            header-align="center"
          />

          <!-- 备注信息 -->
          <el-table-column
            prop="remark"
            label="备注信息"
            width="160"
            align="center"
            header-align="center"
            :show-overflow-tooltip="true"
          />

          <!-- 操作列 -->
          <el-table-column fixed="right" label="相关操作" width="170" align="center">
            <template #default="scope">
              <div class="op-row">
                <el-button size="small" class="op-btn op-gray" @click="openDetail(scope.row)">
                  详情
                </el-button>
                <el-button size="small" class="op-btn op-gray" @click="onDelete(scope.row)">
                  删除
                </el-button>
                <el-dropdown trigger="click">
                  <el-button size="small" class="op-btn op-gray op-more">
                    <el-icon><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu class="info-dropdown">
                      <el-dropdown-item>单据号：{{ scope.row.billNo }}</el-dropdown-item>
                      <el-dropdown-item>客户：{{ scope.row.customer }}</el-dropdown-item>
                      <el-dropdown-item
                        >金额：{{ formatNumber(scope.row.billAmount) }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 中：我们的自定义横向拖动条 -->
      <div ref="xScroll" class="x-scroll">
        <div ref="xInner" class="x-scroll-inner"></div>
      </div>

      <!-- 底部状态栏 -->
      <div class="status-bar">
        <div class="status-left">
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper, total"
            :total="tableData.length"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
          />
        </div>
        <div class="status-right">
          <span>总单据金额：{{ totalBillAmount.toLocaleString() }}</span>
          <span class="ml-24">总实际金额：{{ totalRealAmount.toLocaleString() }}</span>
          <span class="ml-24">总单据收款：{{ totalReceipt.toLocaleString() }}</span>
          <span class="ml-24">总核销金额：{{ totalWriteoff.toLocaleString() }}</span>
          <span class="ml-24">总单据费用：{{ totalFee.toLocaleString() }}</span>
        </div>
      </div>
    </div>

    <!-- 销售单详情弹窗（你说的那个） -->
    <SalesOrderDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />

    <!-- 批量操作弹窗 -->
    <el-dialog title="批量操作" v-model="batchDialogVisible" width="600px" :show-close="true">
      <div class="batch-content">
        <el-tabs v-model="activeTab" class="batch-tabs">
          <el-tab-pane label="导入数据" name="import">
            <div class="tab-content">
              <div class="tips-list">
                <div class="tip-item">
                  <span class="tip-number">1.</span>
                  <span class="tip-text">该功能适用于批量导入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">2.</span>
                  <span class="tip-text">您需要下载数据模板后使用Excel录入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">3.</span>
                  <span class="tip-text">录入数据时，请勿修改首行数据标题以及排序。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">4.</span>
                  <span class="tip-text">请查阅使用文档获取字段格式内容以及相关导入须知。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">5.</span>
                  <span class="tip-text">点击下方上传模板，选择您编辑好的模板文件即可。</span>
                </div>
              </div>
              <div class="button-group">
                <el-button class="download-btn" @click="downloadTemplate">下载模板</el-button>
                <el-upload
                  ref="uploadRef"
                  :auto-upload="false"
                  :show-file-list="false"
                  accept=".xlsx,.xls"
                  :on-change="handleFileChange"
                  :disabled="uploading"
                  style="display: inline-block"
                >
                  <el-button type="primary" class="upload-btn" :loading="uploading">
                    {{ uploading ? '上传中...' : '上传模板' }}
                  </el-button>
                </el-upload>
              </div>

              <!-- 上传数据预览 -->
              <div v-if="uploadedData.length > 0" class="upload-preview">
                <h4>上传数据预览 ({{ uploadedData.length }} 条记录)</h4>
                <el-table
                  :data="uploadedData.slice(0, 5)"
                  border
                  style="width: 100%; margin-top: 10px"
                >
                  <el-table-column
                    v-for="col in matchedColumns"
                    :key="col.prop"
                    :prop="col.prop"
                    :label="col.label"
                    :width="col.width"
                  />
                </el-table>
                <div v-if="uploadedData.length > 5" class="preview-note">
                  仅显示前5条记录，共{{ uploadedData.length }}条
                </div>
                <div class="preview-actions">
                  <el-button type="primary" @click="confirmImport">确认导入</el-button>
                  <el-button @click="clearUploadedData">取消</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="tab-content">
              <div class="export-content">
                <el-button type="primary" class="export-btn" @click="exportSimpleReport"
                  >导出简单报表</el-button
                >
                <el-button type="success" class="export-btn" @click="exportDetailReport"
                  >导出详细报表</el-button
                >
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick, type Ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import { utils, writeFileXLSX, read } from 'xlsx'
import type { UploadFile, UploadInstance } from 'element-plus'
import SalesOrderDetailDialog from './SalesOrderDetailDialog.vue'

import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'

// 导入销售单API
import { saleOrderApi } from '@/apis/sales/order'
import type { SaleListItem, SaleQueryParams } from '@/apis/sales/order/type'

/** 表格行结构 */
interface WriteoffDetail {
  billType: string
  billTime: string
  billNo: string
  amount: number
}

interface FeeDetail {
  expenseType: string
  amount: number
  settleAmount: number
  settleStatus: string
}

interface InvoiceDetail {
  invoiceDate: string
  invoiceNo: string
  invoiceTitle: string
  invoiceAmount: number
}

interface RowData {
  id: string
  org: string
  customer: string
  billTime: string
  billNo: string
  billAmount: number
  realAmount: number
  receiptAmount: number
  writeoffAmount: number
  feeAmount: number
  relatedPerson: string
  auditStatus: string
  writeoffStatus: string
  feeStatus: string
  invoiceStatus: string
  checkStatus: string
  maker: string
  remark: string
  writeoffDetails?: WriteoffDetail[]
  feeDetails?: FeeDetail[]
  invoiceDetails?: InvoiceDetail[]
}

/* 示例数据：你可以等接接口以后直接替换 */
const tableData = ref<RowData[]>([])

// 从后端加载销售单列表
async function loadSaleList() {
  try {
    const params: SaleQueryParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      customer: searchModel.customer || undefined,
      number: searchModel.number || undefined,
      startTime: searchModel.startTime || undefined,
      endTime: searchModel.endTime || undefined
    }

    const response = await saleOrderApi.getSaleList(params)

    if (response.code === 10000 && response.data?.rows) {
      // 转换后端数据为前端表格格式
      tableData.value = response.data.rows.map((item) => ({
        id: item.id,
        org: item.frame,
        customer: item.customer,
        billTime: item.time,
        billNo: item.number,
        billAmount: item.total,
        realAmount: item.actual,
        receiptAmount: item.money,
        writeoffAmount: 0, // 需要从其他接口获取
        feeAmount: item.cost,
        relatedPerson: item.people,
        auditStatus: item.examine === 1 ? '已审核' : '未审核',
        writeoffStatus: getWriteoffStatusText(item.nucleus),
        feeStatus: getFeeStatusText(item.cse),
        invoiceStatus: getInvoiceStatusText(item.invoice),
        checkStatus: item.check === 1 ? '已核对' : '未核对',
        maker: item.user,
        remark: item.data,
        writeoffDetails: [],
        feeDetails: [],
        invoiceDetails: []
      }))

      // 更新原始数据
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('加载销售单列表失败:', error)
    ElMessage.error('获取数据失败')
    // 使用模拟数据
    tableData.value = getMockData()
    originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
  }
}

// 辅助函数：获取核销状态文本
function getWriteoffStatusText(status: number): string {
  const statusMap: Record<number, string> = {
    0: '未核销',
    1: '部分核销',
    2: '已核销'
  }
  return statusMap[status] || '未知'
}

// 辅助函数：获取费用状态文本
function getFeeStatusText(status: number): string {
  const statusMap: Record<number, string> = {
    0: '未结算',
    1: '部分结算',
    2: '已结算',
    3: '无需结算'
  }
  return statusMap[status] || '未知'
}

// 辅助函数：获取发票状态文本
function getInvoiceStatusText(status: number): string {
  const statusMap: Record<number, string> = {
    0: '未开具',
    1: '部分开具',
    2: '已开具',
    3: '无需开具'
  }
  return statusMap[status] || '未知'
}

// 模拟数据（备用）
function getMockData(): RowData[] {
  return [
    {
      id: 'mock-1',
      org: '默认组织',
      customer: '楚渝帅',
      billTime: '2025-10-30',
      billNo: 'XSD2510302238442',
      billAmount: 720781,
      realAmount: 702335.5,
      receiptAmount: 675731.5,
      writeoffAmount: 675430.5,
      feeAmount: 0,
      relatedPerson: '崔渝帅',
      auditStatus: '已审核',
      writeoffStatus: '已核销',
      feeStatus: '无需结算',
      invoiceStatus: '未开具',
      checkStatus: '已核对',
      maker: '管理员',
      remark: '',
      writeoffDetails: [
        {
          billType: '采购退货单',
          billTime: '2025-10-30',
          billNo: 'CGTHD2510302238442',
          amount: 31
        }
      ]
    },
    {
      id: 'mock-2',
      org: '默认组织',
      customer: 'jack',
      billTime: '2025-10-30',
      billNo: 'XSD251030111001',
      billAmount: 98,
      realAmount: 97,
      receiptAmount: 97,
      writeoffAmount: 0,
      feeAmount: 0,
      relatedPerson: 'jack',
      auditStatus: '已审核',
      writeoffStatus: '部分核销',
      feeStatus: '无需结算',
      invoiceStatus: '未开具',
      checkStatus: '未核对',
      maker: '管理员',
      remark: '',
      writeoffDetails: [
        { billType: '销售收款单', billTime: '2025-10-29', billNo: 'SKD2510290001', amount: 50 },
        { billType: '销售收款单', billTime: '2025-10-30', billNo: 'SKD2510300006', amount: 30 }
      ]
    }
  ]
}

// 给 OperationButtons 用的列描述（不是表格必须按这个渲染）
const columns = [
  { prop: 'org', label: '所属组织', width: 120 },
  { prop: 'customer', label: '客户', width: 120 },
  { prop: 'billTime', label: '单据时间', width: 130 },
  { prop: 'billNo', label: '单据编号', width: 200 },
  { prop: 'billAmount', label: '单据金额', width: 130 },
  { prop: 'realAmount', label: '实际金额', width: 130 },
  { prop: 'receiptAmount', label: '单据收款', width: 130 },
  { prop: 'writeoffAmount', label: '核销金额', width: 130 },
  { prop: 'feeAmount', label: '单据费用', width: 130 },
  { prop: 'relatedPerson', label: '关联人员', width: 120 },
  { prop: 'auditStatus', label: '审核状态', width: 100 },
  { prop: 'writeoffStatus', label: '核销状态', width: 120 },
  { prop: 'feeStatus', label: '费用状态', width: 120 },
  { prop: 'invoiceStatus', label: '发票状态', width: 120 },
  { prop: 'checkStatus', label: '核对状态', width: 100 },
  { prop: 'maker', label: '制单人', width: 100 },
  { prop: 'remark', label: '备注信息', width: 160 }
]

/* 搜索部分（你已经把类型中 customer 改成 string | null 了，就保持一致） */
type SearchFormData = {
  customer?: string | null
  number?: string
  startTime?: string
  endTime?: string
  [k: string]: any
}
const searchModel = reactive<SearchFormData>({})
const goodSearchRef = ref<InstanceType<typeof GoodSearchForm> | null>(null)

const originalAllData = ref<RowData[]>([])

/* 选择 */
const selected = ref<RowData[]>([])
const handleSelectionChange = (rows: RowData[]) => {
  selected.value = rows
}

/* 排序 */
const sortKey = ref<string>('')
const sortOrder = ref<'asc' | 'desc' | ''>('')
const sortTable = (key: string, order: 'asc' | 'desc') => {
  sortKey.value = key
  sortOrder.value = order
  tableData.value.sort((a, b) => {
    const va = (a as any)[key]
    const vb = (b as any)[key]
    if (order === 'asc') return va > vb ? 1 : va < vb ? -1 : 0
    return va < vb ? 1 : va > vb ? -1 : 0
  })
}

/* 合计 / 分页 */
const totalBillAmount = computed(() => tableData.value.reduce((s, r) => s + (r.billAmount || 0), 0))
const totalRealAmount = computed(() => tableData.value.reduce((s, r) => s + (r.realAmount || 0), 0))
const totalReceipt = computed(() => tableData.value.reduce((s, r) => s + (r.receiptAmount || 0), 0))
const totalWriteoff = computed(() =>
  tableData.value.reduce((s, r) => s + (r.writeoffAmount || 0), 0)
)
const totalFee = computed(() => tableData.value.reduce((s, r) => s + (r.feeAmount || 0), 0))

const currentPage = ref(1)
const pageSize = ref(10)
const pagedData = computed(() => {
  const s = (currentPage.value - 1) * pageSize.value
  return tableData.value.slice(s, s + pageSize.value)
})

/* 搜索过滤（这里还是本地的） */
const includes = (src?: string, kw?: string | null) =>
  (src ?? '').toLowerCase().includes((kw ?? '').trim().toLowerCase())

const onGoodSearch = (payload: SearchFormData) => {
  const cond = payload ?? {}
  const list = originalAllData.value.filter((row) => {
    if (cond.customer && !includes(row.customer, cond.customer)) return false
    if (cond.number && !includes(row.billNo, cond.number)) return false
    // 时间区间你可以按需要再补
    return true
  })
  tableData.value = list
  currentPage.value = 1
}

/* 刷新 */
const refreshing = ref(false)
const onRefresh = async () => {
  if (refreshing.value) return
  refreshing.value = true
  try {
    await loadSaleList()
    currentPage.value = 1
    goodSearchRef.value?.resetForm?.()
    sortKey.value = ''
    sortOrder.value = ''
    ElMessage.success('已刷新为默认排序')
  } catch (error) {
    console.error('刷新失败:', error)
    ElMessage.error('刷新失败')
  } finally {
    refreshing.value = false
  }
}

/* 详情 */
const detailVisible = ref(false)
const currentRow = ref<RowData | null>(null)
const openDetail = (row: RowData) => {
  currentRow.value = row
  detailVisible.value = true
}
const handleDetailSave = () => {
  ElMessage.success('详情已保存（示例）')
  detailVisible.value = false
}

/* 删除 */
const onDelete = async (row: RowData) => {
  try {
    await ElMessageBox.confirm(`确定删除单据【${row.billNo}】吗？`, '提示', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })

    // 调用删除API
    const response = await saleOrderApi.deleteSales([row.id])

    if (response.code === 10000) {
      const i = tableData.value.findIndex((r) => r.billNo === row.billNo)
      if (i > -1) tableData.value.splice(i, 1)
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
      ElMessage.success('删除成功')
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请重试')
    }
  }
}

/* 批量审核/反审核 */
const onBatchExamine = async (type: 0 | 1) => {
  // type: 0=审核, 1=反审核
  if (selected.value.length === 0) {
    ElMessage.warning('请选择要操作的单据！')
    return
  }

  try {
    const ids = selected.value.map((row) => row.id)
    const response = await saleOrderApi.examineSales(ids, type === 0)

    if (response.code === 10000) {
      // 更新本地数据状态
      selected.value.forEach((row) => {
        row.auditStatus = type === 0 ? '已审核' : '未审核'
      })

      // 清空选中项
      selected.value = []
      if (tableRef.value) {
        tableRef.value.clearSelection()
      }

      ElMessage.success(`操作成功！共处理 ${ids.length} 条单据`)
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('批量操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

/* 批量核对/反核对 */
const onBatchCheck = async (type: 0 | 1) => {
  // type: 0=核对, 1=反核对
  if (selected.value.length === 0) {
    ElMessage.warning('请选择要操作的单据！')
    return
  }

  try {
    const ids = selected.value.map((row) => row.id)
    const response = await saleOrderApi.checkSales(ids, type === 0)

    if (response.code === 10000) {
      // 更新本地数据状态
      selected.value.forEach((row) => {
        row.checkStatus = type === 0 ? '已核对' : '未核对'
      })

      // 清空选中项
      selected.value = []
      if (tableRef.value) {
        tableRef.value.clearSelection()
      }

      ElMessage.success(`操作成功！共处理 ${ids.length} 条单据`)
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('批量操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

/* 批量删除 */
const onBatchDelete = async () => {
  if (selected.value.length === 0) {
    ElMessage.warning('未选择要删除的数据！')
    return
  }

  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selected.value.length} 条单据吗？`, '提示', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })

    // 获取要删除的单据编号
    const ids = selected.value.map((r) => r.id)

    // 调用删除API
    const response = await saleOrderApi.deleteSales(ids)

    if (response.code === 10000) {
      // 从数据中删除
      tableData.value = tableData.value.filter((r) => !ids.includes(r.id))

      // 更新原始集合
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))

      // 清空选中项
      selected.value = []
      if (tableRef.value) {
        tableRef.value.clearSelection()
      }

      ElMessage.success(`删除成功！共删除 ${ids.length} 条单据`)
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('删除失败，请重试')
    }
  }
}

/**
 * 导出详情报表
 */
const exportDetailReport = async () => {
  if (selected.value.length === 0) {
    ElMessage.warning('请选择要导出的单据！')
    return
  }

  try {
    const ids = selected.value.map((row) => row.id)

    // 确保 IDs 是有效的
    if (ids.length === 0) {
      ElMessage.warning('选中的单据ID无效')
      return
    }

    console.log('开始导出详情报表，IDs:', ids)

    const blob = await saleOrderApi.exportDetail(ids)

    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售单详情报表_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success(`导出成功，共导出 ${ids.length} 条单据`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请重试')
  }
}

/**
 * 导出简单报表
 */
const exportSimpleReport = async () => {
  if (selected.value.length === 0) {
    ElMessage.warning('请选择要导出的单据！')
    return
  }

  try {
    const ids = selected.value.map((row) => row.id)

    // 确保 IDs 是有效的
    if (ids.length === 0) {
      ElMessage.warning('选中的单据ID无效')
      return
    }

    console.log('开始导出简单报表，IDs:', ids)

    const blob = await saleOrderApi.exportSimple(ids)

    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售单简易报表_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success(`导出成功，共导出 ${ids.length} 条单据`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请重试')
  }
}

/* 固定横向滚动条联动 */
const tableRef = ref()
const bodyWrapper = ref<HTMLElement | null>(null)
const headerWrapper = ref<HTMLElement | null>(null)
const xScroll = ref<HTMLElement | null>(null)
const xInner = ref<HTMLElement | null>(null)
let ro: ResizeObserver | null = null

const setWidths = () => {
  if (!bodyWrapper.value || !xInner.value) return
  xInner.value.style.width = bodyWrapper.value.scrollWidth + 'px'
}
const bodyScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (xScroll.value.scrollLeft !== bodyWrapper.value.scrollLeft) {
    xScroll.value.scrollLeft = bodyWrapper.value.scrollLeft
  }
  if (headerWrapper.value) headerWrapper.value.scrollLeft = bodyWrapper.value.scrollLeft
}
const xScrollScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (bodyWrapper.value.scrollLeft !== xScroll.value.scrollLeft) {
    bodyWrapper.value.scrollLeft = xScroll.value.scrollLeft
    if (headerWrapper.value) headerWrapper.value.scrollLeft = xScroll.value.scrollLeft
  }
}

onMounted(async () => {
  // 初始化原始数据
  await loadSaleList()

  await nextTick()
  const tableEl = (tableRef.value?.$el ?? null) as HTMLElement | null
  bodyWrapper.value = tableEl?.querySelector('.el-table__body-wrapper') as HTMLElement | null
  headerWrapper.value = tableEl?.querySelector('.el-table__header-wrapper') as HTMLElement | null

  if (bodyWrapper.value) {
    bodyWrapper.value.addEventListener('scroll', bodyScroll, { passive: true })
  }
  if (xScroll.value) {
    xScroll.value.addEventListener('scroll', xScrollScroll, { passive: true })
  }
  setWidths()
  if (bodyWrapper.value) {
    ro = new ResizeObserver(() => setWidths())
    ro.observe(bodyWrapper.value)
  }
})

onBeforeUnmount(() => {
  if (bodyWrapper.value) bodyWrapper.value.removeEventListener('scroll', bodyScroll)
  if (xScroll.value) xScroll.value.removeEventListener('scroll', xScrollScroll)
  if (ro && bodyWrapper.value) ro.unobserve(bodyWrapper.value)
  ro = null
})

/* 工具函数：数字加千分位 */
const formatNumber = (n: number | undefined | null) => {
  const num = Number(n ?? 0)
  return Number.isFinite(num) ? num.toLocaleString() : '0'
}

// ==================== 批量操作弹窗相关代码 ====================

// 批量操作弹窗控制
const batchDialogVisible = ref(false)
const activeTab = ref<'import' | 'export'>('import')

// 上传相关数据
const uploadRef = ref<UploadInstance | null>(null)
const uploadedData = ref<any[]>([])
const matchedColumns = ref<any[]>([])
const uploading = ref(false)
const currentFile = ref<File | null>(null)

// 打开批量操作弹窗
const openBatchDialog = () => {
  batchDialogVisible.value = true
  activeTab.value = 'import' // 默认显示导入标签页
}

// 下载模板功能
const downloadTemplate = () => {
  try {
    // 获取表格列配置
    const tableColumns = columns

    if (tableColumns.length === 0) {
      console.warn('未找到表格列配置，使用默认模板')
      downloadDefaultTemplate()
      return
    }

    // 根据表格列配置生成模板数据
    const templateData = generateTemplateFromColumns(tableColumns)

    // 创建工作表
    const ws = utils.json_to_sheet(templateData)

    // 根据列配置设置列宽
    const colWidths = tableColumns.map((col: any) => {
      let widthValue: number | undefined
      if (col.width) {
        if (typeof col.width === 'string') {
          const numMatch = col.width.match(/\d+/)
          widthValue = numMatch ? parseInt(numMatch[0], 10) : undefined
        } else {
          widthValue = col.width
        }
      }
      return {
        wch: widthValue ? Math.max(widthValue / 8, 10) : 15
      }
    })
    ws['!cols'] = colWidths

    // 创建工作簿
    const wb = utils.book_new()
    utils.book_append_sheet(wb, ws, '销售单导入模板')

    // 导出模板文件
    const fileName = `销售单导入模板.xlsx`
    writeFileXLSX(wb, fileName)

    console.log('模板下载成功:', fileName)
    ElMessage.success('模板下载成功')
  } catch (error) {
    console.error('模板下载失败:', error)
    ElMessage.error('模板下载失败')
  }
}

// 根据表格列配置生成模板数据
const generateTemplateFromColumns = (columns: any[]): any[] => {
  // 生成表头行
  const headerRow: Record<string, string> = {}
  columns.forEach((col: any) => {
    if (col.prop && col.label) {
      headerRow[col.prop] = col.label
    }
  })

  // 生成示例数据行（最多3行）
  const exampleRows: Record<string, string>[] = []
  for (let i = 1; i <= 3; i++) {
    const row: Record<string, string> = {}
    columns.forEach((col: any) => {
      if (col.prop) {
        row[col.prop] = generateExampleValue(col, i)
      }
    })
    exampleRows.push(row)
  }

  return [headerRow, ...exampleRows]
}

// 根据列配置生成示例值
const generateExampleValue = (column: any, index: number): string => {
  const { prop, type, label } = column

  if (type === 'date' || prop.includes('date') || prop.includes('Date') || prop.includes('Time')) {
    return `2024-0${index}-0${index}`
  } else if (
    type === 'number' ||
    prop.includes('id') ||
    prop.includes('Id') ||
    prop.includes('Amount')
  ) {
    return index.toString()
  } else if (prop.includes('phone') || prop.includes('Phone') || prop.includes('tel')) {
    return `1380013800${index}`
  } else if (prop.includes('email') || prop.includes('Email')) {
    return `user${index}@example.com`
  } else if (
    prop.includes('name') ||
    prop.includes('Name') ||
    prop.includes('姓名') ||
    prop.includes('customer') ||
    prop.includes('person')
  ) {
    return ['张三', '李四', '王五'][index - 1] || `示例${index}`
  } else if (prop.includes('status') || prop.includes('Status') || prop.includes('状态')) {
    return ['启用', '禁用', '待审核'][index - 1] || '示例状态'
  } else if (prop.includes('number') || prop.includes('Number') || prop.includes('编号')) {
    return `XSD2024010${index}`
  } else {
    return `示例数据${index}`
  }
}

// 默认模板（当没有表格列配置时使用）
const downloadDefaultTemplate = () => {
  const defaultData = [
    {
      单据编号: 'XSD20240001',
      客户: '示例客户1',
      单据时间: '2024-01-01',
      单据金额: '1000',
      实际金额: '980',
      关联人员: '张三',
      审核状态: '已审核',
      核对状态: '已核对',
      制单人: '管理员',
      备注信息: '示例备注'
    },
    {
      单据编号: 'XSD20240002',
      客户: '示例客户2',
      单据时间: '2024-01-02',
      单据金额: '2000',
      实际金额: '1960',
      关联人员: '李四',
      审核状态: '未审核',
      核对状态: '未核对',
      制单人: '管理员',
      备注信息: '示例备注'
    }
  ]

  const ws = utils.json_to_sheet(defaultData)
  const wb = utils.book_new()
  utils.book_append_sheet(wb, ws, '销售单导入模板')

  const fileName = '销售单导入模板.xlsx'
  writeFileXLSX(wb, fileName)
  console.log('默认模板下载成功:', fileName)
}

// 处理文件上传
const handleFileChange = async (file: UploadFile) => {
  if (!file.raw) {
    console.error('文件对象为空')
    return
  }

  // 使用导入API函数
  uploading.value = true
  currentFile.value = file.raw

  try {
    // 调用导入API函数
    const result = await saleOrderApi.importSale(file.raw)

    // 上传成功
    console.log('文件上传成功:', result)

    if (result.code === 10000) {
      ElMessage.success('导入成功')
      // 刷新数据
      await loadSaleList()
      // 关闭弹窗
      batchDialogVisible.value = false
    } else {
      ElMessage.error(result.message || '导入失败')
    }
  } catch (error) {
    console.error('文件上传失败:', error)
    ElMessage.error('导入失败，请重试')
  } finally {
    uploading.value = false
  }
}

// 确认导入数据
const confirmImport = () => {
  if (uploadedData.value.length === 0) {
    console.warn('没有可导入的数据')
    return
  }

  // 这里可以调用后端导入接口
  console.log('确认导入数据:', uploadedData.value)

  // 清空上传数据
  clearUploadedData()

  // 关闭弹窗
  batchDialogVisible.value = false

  ElMessage.success(`数据导入成功: ${uploadedData.value.length} 条记录`)
}

// 清空上传数据
const clearUploadedData = () => {
  uploadedData.value = []
  matchedColumns.value = []
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}
</script>

<style scoped>
.sales-order-form {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #fff;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.table-section {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
  position: relative;
}
.table-wrapper {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding-bottom: 14px;
  border-bottom: 1px solid #ebeef5;
}
.table-wrapper :deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}
.table-wrapper :deep(.el-table__header-wrapper) {
  overflow: hidden !important;
}

.bottom-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  padding: 4px 10px;
  height: 40px;
  /* 贴底 */
  position: sticky;
  bottom: 0;
  z-index: 5;
}

/* 左边分页不挤 */
.status-left {
  flex: 0 0 auto;
}

/* 中间的横向拖动条占剩余空间 */
.x-scroll {
  flex: 1 1 auto;
  height: 12px;
  overflow-x: auto;
  overflow-y: hidden;
  background: #f0f2f5;
  border-radius: 6px;
  /* 让滚动条更好点 */
}
.x-scroll-inner {
  height: 1px;
}
.scrollbar::-webkit-scrollbar,
.table-wrapper::-webkit-scrollbar {
  height: 12px;
  width: 12px;
}
.scrollbar::-webkit-scrollbar-thumb,
.table-wrapper::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 6px;
  border: 2px solid #f0f2f5;
}

/* 状态栏 */
.status-bar {
  position: sticky;
  bottom: 0;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  height: 28px;
  padding: 4px 10px;
  font-size: 13px;
  color: #606266;
}
/* 右边合计不换行 */
.status-right {
  flex: 0 0 auto;
  white-space: nowrap;
  font-size: 13px;
  color: #606266;
}
.ml-24 {
  margin-left: 24px;
}

/* 顶部按钮联动 */
.btn-join {
  display: inline-flex;
  align-items: center;
}
.btn-join > * + * {
  margin-left: -1px;
}

/* 表头排序样式 */
.header-sortable {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.col-title {
  white-space: nowrap;
}
.sort-vertical {
  display: inline-flex;
  flex-direction: column;
  margin-left: 4px;
  line-height: 10px;
}
.sort-vertical .el-icon {
  font-size: 14px;
  color: #c0c4cc;
  cursor: pointer;
}
.sort-vertical .el-icon.active {
  color: #409eff;
}

/* 操作列按钮 */
.op-row {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.op-btn {
  height: 28px;
  line-height: 26px;
  background: #fff;
  color: #606266;
  border: 1px solid #dcdfe6;
  border-radius: 0;
  padding: 0 10px;
  transition: all 0.15s ease;
}
.op-row > * + * {
  margin-left: -1px;
}
.op-row :deep(.el-dropdown .el-button) {
  border-radius: 0 !important;
}
.op-row .op-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 状态 + 小按钮 */
.status-with-trigger {
  display: inline-flex;
  align-items: center;
  gap: 2px;
}
.mini-trigger {
  padding: 0;
  min-height: 20px;
}

/* 内部弹窗表格 */
.inner-pop-table {
  width: 100%;
}
.inner-pop-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}
.inner-pop-table th,
.inner-pop-table td {
  border: 1px solid #ebeef5;
  padding: 4px 6px;
  text-align: center;
  white-space: nowrap;
}
.inner-pop-table th {
  background: #f7f8fa;
  font-weight: 500;
}
.inner-pop-table .num {
  text-align: right;
}
.inner-pop-table .empty {
  text-align: center;
  color: #999;
}

.mr-8 {
  margin-right: 8px;
}

/* 批量操作弹窗样式 */
.batch-content {
  padding: 0;
}

.batch-tabs {
  margin-top: 0;
}

.tab-content {
  padding: 20px 0;
}

.tips-list {
  margin-bottom: 30px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  line-height: 1.5;
}

.tip-number {
  color: #409eff;
  font-weight: 600;
  margin-right: 8px;
  min-width: 20px;
}

.tip-text {
  color: #333;
  font-size: 14px;
  flex: 1;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 20px;
}

.download-btn {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  color: #333;
  min-width: 100px;
}

.upload-btn {
  min-width: 100px;
}

.export-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  min-height: 200px;
}

.export-btn {
  min-width: 120px;
  height: 40px;
  font-size: 16px;
}

/* 上传预览样式 */
.upload-preview {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.upload-preview h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 14px;
  font-weight: 600;
}

.preview-note {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.preview-actions {
  margin-top: 15px;
  text-align: center;
}

.preview-actions .el-button {
  margin: 0 5px;
}

/* 标签页样式调整 */
:deep(.el-tabs__header) {
  margin: 0 0 20px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: #e4e7ed;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
}

:deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
}
</style>
