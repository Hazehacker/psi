import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 仓库管理的路由
 */
const routes: RouteRecordRaw[] = [
  // 基础查询功能
  {
    path: '/inventory-query',
    name: 'WarehouseInventoryQuery',
    meta: {
      label: '库存查询',
      icon: 'mdi:package-variant'
    },
    component: () => import('@/views/warehouse/inventory/Index.vue')
  },
  {
    path: '/batch-query',
    name: 'WarehouseBatchQuery',
    meta: {
      label: '批次查询',
      icon: 'mdi:package-variant-closed'
    },
    component: () => import('@/views/warehouse/batch/Index.vue')
  },
  {
    path: '/inventory-check',
    name: 'WarehouseInventoryCheck',
    meta: {
      label: '库存盘点',
      icon: 'mdi:clipboard-list'
    },
    component: () => import('@/views/warehouse/check/Index.vue')
  },

  // 其他出入库和调拨
  {
    path: '/other-inbound',
    name: 'WarehouseOtherInbound',
    meta: {
      label: '其他入库单',
      icon: 'mdi:package-down'
    },
    component: () => import('@/views/warehouse/inbound/OtherInbound.vue')
  },
  {
    path: '/other-outbound',
    name: 'WarehouseOtherOutbound',
    meta: {
      label: '其他出库单',
      icon: 'mdi:package-up'
    },
    component: () => import('@/views/warehouse/outbound/OtherOutbound.vue')
  },
  {
    path: '/transfer-order',
    name: 'WarehouseTransferOrder',
    meta: {
      label: '调拨单',
      icon: 'mdi:truck-delivery'
    },
    component: () => import('@/views/warehouse/transfer/TransferOrder.vue')
  },

  // 报表
  {
    path: '/transfer-order-report',
    name: 'WarehouseTransferOrderReport',
    meta: {
      label: '调拨单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/warehouse/transfer/TransferOrderReport.vue')
  },
  {
    path: '/other-inbound-report',
    name: 'WarehouseOtherInboundReport',
    meta: {
      label: '其他入库单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/warehouse/inbound/OtherInboundReport.vue')
  },
  {
    path: '/other-outbound-report',
    name: 'WarehouseOtherOutboundReport',
    meta: {
      label: '其他出库单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/warehouse/outbound/OtherOutboundReport.vue')
  },

  // 单据管理
  {
    path: '/inbound-order',
    name: 'WarehouseInboundOrder',
    meta: {
      label: '入库单',
      icon: 'mdi:package-down'
    },
    component: () => import('@/views/warehouse/document/InboundOrder.vue')
  },
  {
    path: '/outbound-order',
    name: 'WarehouseOutboundOrder',
    meta: {
      label: '出库单',
      icon: 'mdi:package-up'
    },
    component: () => import('@/views/warehouse/document/OutboundOrder.vue')
  },
  {
    path: '/document-transfer-order',
    name: 'WarehouseDocumentTransferOrder',
    meta: {
      label: '单据调拨单',
      icon: 'mdi:truck-delivery'
    },
    component: () => import('@/views/warehouse/document/TransferOrder.vue')
  },
  {
    path: '/inventory-check-order',
    name: 'WarehouseInventoryCheckOrder',
    meta: {
      label: '盘点单',
      icon: 'mdi:clipboard-list'
    },
    component: () => import('@/views/warehouse/document/InventoryCheckOrder.vue')
  },
  {
    path: '/assemble-disassemble',
    name: 'WarehouseAssembleDisassemble',
    meta: {
      label: '组装拆卸单',
      icon: 'mdi:tools'
    },
    component: () => import('@/views/warehouse/document/AssembleDisassemble.vue')
  },

  // 账户管理
  {
    path: '/transfer-account',
    name: 'WarehouseTransferAccount',
    meta: {
      label: '转账单',
      icon: 'mdi:bank-transfer'
    },
    component: () => import('@/views/warehouse/account/TransferAccount.vue')
  },

  // 收款单
  {
    path: '/receipt-order',
    name: 'WarehouseReceiptOrder',
    meta: {
      label: '收款单',
      icon: 'mdi:cash-plus'
    },
    component: () => import('@/views/warehouse/receipt/ReceiptOrder.vue')
  },

  // 付款单
  {
    path: '/payment-order',
    name: 'WarehousePaymentOrder',
    meta: {
      label: '付款单',
      icon: 'mdi:cash-minus'
    },
    component: () => import('@/views/warehouse/payment/PaymentOrder.vue')
  },

  // 核销单
  {
    path: '/writeoff-order',
    name: 'WarehouseWriteoffOrder',
    meta: {
      label: '核销单',
      icon: 'mdi:check-circle'
    },
    component: () => import('@/views/warehouse/writeoff/WriteoffOrder.vue')
  }
]

export default routes
