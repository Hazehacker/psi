import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 采购管理的路由
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/purchase-booking',
    name: 'PurchaseBooking',
    meta: {
      label: '采购订单',
      icon: 'mdi:cart-arrow-down'
    },
    component: () => import('@/views/purchase/booking/PurchaseBook.vue')
  },
  {
    path: '/purchase-booking-report',
    name: 'PurchaseBookingReport',
    meta: {
      label: '采购订单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/purchase/booking/PurchaseBookForm.vue')
  },
  {
    path: '/purchase-order',
    name: 'PurchaseOrder',
    meta: {
      label: '采购单',
      icon: 'mdi:cart-check'
    },
    component: () => import('@/views/purchase/order/PurchaseOrder.vue')
  },
  {
    path: '/purchase-order-report',
    name: 'PurchaseOrderReport',
    meta: {
      label: '采购订单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/purchase/order/PurchaseOrderForm.vue')
  },
  {
    path: '/purchase-return',
    name: 'PurchaseReturn',
    meta: {
      label: '采购退货单',
      icon: 'mdi:cart-remove'
    },
    component: () => import('@/views/purchase/return/PurchaseReturn.vue')
  },
  {
    path: '/purchase-return-report',
    name: 'PurchaseReturnReport',
    meta: {
      label: '采购退货单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/purchase/return/PurchaseReturnForm.vue')
  }
]

export default routes
