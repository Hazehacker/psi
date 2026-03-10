import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 报表管理的路由
 */
const routes: RouteRecordRaw[] = [
  // 采购报表（二级父项）
  {
    path: '/purchase-report',
    name: 'PurchaseReport',
    meta: {
      label: '采购报表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/status/404.vue')
  },
  // 采购报表 - 三级子项
  {
    path: '/report/purchase/order-followup',
    name: 'PurchaseOrderFollowup',
    meta: {
      label: '采购订单跟踪表',
      icon: 'mdi:chart-timeline'
    },
    component: () => import('@/views/report/purchase/PurchaseOrder.vue')
  },
  {
    path: '/report/purchase/detail',
    name: 'PurchaseDetail',
    meta: {
      label: '采购明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/purchase/PurchaseDetail.vue')
  },
  {
    path: '/report/purchase/summary',
    name: 'PurchaseSummary',
    meta: {
      label: '采购汇总表',
      icon: 'mdi:file-document'
    },
    component: () => import('@/views/report/purchase/PurchaseSummary.vue')
  },
  {
    path: '/report/purchase/payment',
    name: 'PurchasePayment',
    meta: {
      label: '采购付款表',
      icon: 'mdi:cash-check'
    },
    component: () => import('@/views/report/purchase/PurchasePayment.vue')
  },
  {
    path: '/report/purchase/ranking',
    name: 'PurchaseRanking',
    meta: {
      label: '采购排行表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/report/purchase/PurchaseList.vue')
  },
  // 销售报表（二级父项）
  {
    path: '/sales-report',
    name: 'SalesReport',
    meta: {
      label: '销售报表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/status/404.vue')
  },
  // 销售报表 - 三级子项
  {
    path: '/report/sales/order-followup',
    name: 'SalesOrderFollowup',
    meta: {
      label: '销售订单跟踪表',
      icon: 'mdi:chart-timeline'
    },
    component: () => import('@/views/report/sales/SaleOrder.vue')
  },
  {
    path: '/report/sales/detail',
    name: 'SalesDetail',
    meta: {
      label: '销售明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/sales/SaleDetail.vue')
  },
  {
    path: '/report/sales/summary',
    name: 'SalesSummary',
    meta: {
      label: '销售汇总表',
      icon: 'mdi:file-document'
    },
    component: () => import('@/views/report/sales/SaleSummary.vue')
  },
  {
    path: '/report/sales/receipt',
    name: 'SalesReceipt',
    meta: {
      label: '销售收款表',
      icon: 'mdi:cash-check'
    },
    component: () => import('@/views/report/sales/SaleReceipt.vue')
  },
  {
    path: '/report/sales/ranking',
    name: 'SalesRanking',
    meta: {
      label: '销售排行表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/report/sales/SaleList.vue')
  },
  {
    path: '/report/sales/profit',
    name: 'SalesProfit',
    meta: {
      label: '销售利润表',
      icon: 'mdi:currency-usd'
    },
    component: () => import('@/views/report/sales/SaleProfit.vue')
  },
  // 仓库报表（二级父项）
  {
    path: '/warehouse-report',
    name: 'WarehouseReport',
    meta: {
      label: '仓库报表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/status/404.vue')
  },
  // 仓库报表 - 三级子项
  {
    path: '/report/warehouse/inventory-summary',
    name: 'InventorySummary',
    meta: {
      label: '商品库存余额表',
      icon: 'mdi:warehouse'
    },
    component: () => import('@/views/report/warehouse/InventorySummary.vue')
  },
  {
    path: '/report/warehouse/receipt-detail',
    name: 'InventoryReceiptDetail',
    meta: {
      label: '商品收发明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/warehouse/InventoryReceiptDetail.vue')
  },
  {
    path: '/report/warehouse/receipt-summary',
    name: 'InventoryReceiptSummary',
    meta: {
      label: '商品收发汇总表',
      icon: 'mdi:file-document'
    },
    component: () => import('@/views/report/warehouse/InventoryReceiptSummary.vue')
  },
  // 资金报表（二级父项）
  {
    path: '/fund-report',
    name: 'FundReport',
    meta: {
      label: '资金报表',
      icon: 'mdi:chart-bar'
    },
    component: () => import('@/views/status/404.vue')
  },
  // 资金报表 - 三级子项
  {
    path: '/report/fund/current-account',
    name: 'CurrentAccount',
    meta: {
      label: '现金银行报表',
      icon: 'mdi:bank'
    },
    component: () => import('@/views/report/fund/CurrentAccount.vue')
  },
  {
    path: '/report/fund/receivable-detail',
    name: 'ReceivableDetail',
    meta: {
      label: '应收款明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/fund/ReceivableDetail.vue')
  },
  {
    path: '/report/fund/payable-detail',
    name: 'PayableDetail',
    meta: {
      label: '应付款明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/fund/PayableDetail.vue')
  },
  {
    path: '/report/fund/other-receipt-detail',
    name: 'OtherReceiptDetail',
    meta: {
      label: '其他收支明细表',
      icon: 'mdi:file-table'
    },
    component: () => import('@/views/report/fund/OtherReceiptDetail.vue')
  },
  {
    path: '/report/fund/customer-statement',
    name: 'CustomerStatement',
    meta: {
      label: '客户对账单',
      icon: 'mdi:account-details'
    },
    component: () => import('@/views/report/fund/CustomerStatement.vue')
  },
  {
    path: '/report/fund/supplier-statement',
    name: 'SupplierStatement',
    meta: {
      label: '供应商对账单',
      icon: 'mdi:account-details'
    },
    component: () => import('@/views/report/fund/SupplierStatement.vue')
  },
  {
    path: '/report/fund/profit-statement',
    name: 'ProfitStatement',
    meta: {
      label: '利润表',
      icon: 'mdi:chart-line'
    },
    component: () => import('@/views/report/fund/ProfitStatement.vue')
  },
  {
    path: '/report/fund/debt-age-analysis',
    name: 'DebtAgeAnalysis',
    meta: {
      label: '往来单位欠款表',
      icon: 'mdi:account-cash'
    },
    component: () => import('@/views/report/fund/DebtAgeAnalysis.vue')
  }
]

export default routes
