import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 销售管理的路由
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/sales-booking',
    name: 'SalesBooking',
    meta: {
      label: '销售订单',
      icon: 'mdi:cart-arrow-up'
    },
    component: () => import('@/views/sales/booking/SalesBook.vue')
  },
  {
    path: '/sales-booking-report',
    name: 'SalesBookingReport',
    meta: {
      label: '销售订单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/sales/booking/SalesBookForm.vue')
  },
  {
    path: '/sales-order',
    name: 'SalesOrder',
    meta: {
      label: '销售单',
      icon: 'mdi:cart-check'
    },
    component: () => import('@/views/sales/order/SalesOrder.vue')
  },
  {
    path: '/sales-order-report',
    name: 'SalesOrderReport',
    meta: {
      label: '销售单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/sales/order/SalesOrderForm.vue')
  },
  {
    path: '/sales-return',
    name: 'SalesReturn',
    meta: {
      label: '销售退货单',
      icon: 'mdi:cart-remove'
    },
    component: () => import('@/views/sales/return/SalesReturn.vue')
  },
  {
    path: '/sales-return-report',
    name: 'SalesReturnReport',
    meta: {
      label: '销售退货单报表',
      icon: 'mdi:file-chart'
    },
    component: () => import('@/views/sales/return/SalesReturnForm.vue')
  }
]

export default routes
