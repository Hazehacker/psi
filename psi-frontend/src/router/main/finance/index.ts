import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 资金管理的路由
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/receipt-order',
    name: 'ReceiptOrder',
    meta: {
      label: '收款单',
      icon: 'mdi:cash-plus'
    },
    component: () => import('@/views/finance/receipt/receipt.vue')
  },
  {
    path: '/receipt-order-report',
    name: 'ReceiptOrderReport',
    meta: {
      label: '收款单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/receipt/receiptForm.vue')
  },
  {
    path: '/payment-order',
    name: 'PaymentOrder',
    meta: {
      label: '付款单',
      icon: 'mdi:cash-minus'
    },
    component: () => import('@/views/finance/payment/payment.vue')
  },
  {
    path: '/payment-order-report',
    name: 'PaymentOrderReport',
    meta: {
      label: '付款单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/payment/paymentForm.vue')
  },
  {
    path: '/writeoff-order',
    name: 'WriteoffOrder',
    meta: {
      label: '核销单',
      icon: 'mdi:check-circle'
    },
    component: () => import('@/views/finance/writeoff/writeoff.vue')
  },
  {
    path: '/writeoff-order-report',
    name: 'WriteoffOrderReport',
    meta: {
      label: '核销单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/writeoff/writeoffForm.vue')
  },
  {
    path: '/transfer-account',
    name: 'TransferAccount',
    meta: {
      label: '转账单',
      icon: 'mdi:bank-transfer'
    },
    component: () => import('@/views/finance/transfer/transfer.vue')
  },
  {
    path: '/transfer-account-report',
    name: 'TransferAccountReport',
    meta: {
      label: '转账单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/transfer/transferForm.vue')
  },
  {
    path: '/other-income',
    name: 'OtherIncome',
    meta: {
      label: '其它收入单',
      icon: 'mdi:cash-plus'
    },
    component: () => import('@/views/finance/income/OtherIncome.vue')
  },
  {
    path: '/other-expense',
    name: 'OtherExpense',
    meta: {
      label: '其它支出单',
      icon: 'mdi:cash-minus'
    },
    component: () => import('@/views/finance/expense/OtherExpense.vue')
  },
  {
    path: '/purchase-sales-cost',
    name: 'PurchaseSalesCost',
    meta: {
      label: '购销费用',
      icon: 'mdi:currency-usd'
    },
    component: () => import('@/views/finance/cost/PurchaseSalesCost.vue')
  },
  {
    path: '/purchase-sales-invoice',
    name: 'PurchaseSalesInvoice',
    meta: {
      label: '购销发票',
      icon: 'mdi:receipt'
    },
    component: () => import('@/views/finance/invoice/PurchaseSalesInvoice.vue')
  },
  {
    path: '/other-income-report',
    name: 'OtherIncomeReport',
    meta: {
      label: '其它收入单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/income/OtherIncomeReport.vue')
  },
  {
    path: '/other-expense-report',
    name: 'OtherExpenseReport',
    meta: {
      label: '其它支出单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/expense/OtherExpenseReport.vue')
  },
  {
    path: '/purchase-sales-cost-report',
    name: 'PurchaseSalesCostReport',
    meta: {
      label: '购销费用报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/cost/PurchaseSalesCostReport.vue')
  },
  {
    path: '/purchase-sales-invoice-report',
    name: 'PurchaseSalesInvoiceReport',
    meta: {
      label: '购销发票报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/finance/invoice/PurchaseSalesInvoiceReport.vue')
  }
]

export default routes
