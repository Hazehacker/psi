import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/basicdata',
    name: 'BasicData',
    meta: {
      title: '基础资料',
      icon: 'mdi:database'
    },
    children: [
      // {
      //   path: '/basicdata/product',
      //   name: 'ProductManagement',
      //   meta: {
      //     title: '商品管理'
      //   },
      //   component: () => import('@/views/basicdata/product/ProductList.vue')
      // },
      // {
      //   path: '/basicdata/customer',
      //   name: 'CustomerManagement',
      //   meta: {
      //     title: '客户管理'
      //   },
      //   component: () => import('@/views/basicdata/customer/CustomerList.vue')
      // },
      // {
      //   path: '/basicdata/supplier',
      //   name: 'SupplierManagement',
      //   meta: {
      //     title: '供应商管理'
      //   },
      //   component: () => import('@/views/basicdata/supplier/SupplierList.vue')
      // },
      // {
      //   path: '/basicdata/warehouse',
      //   name: 'WarehouseManagement',
      //   meta: {
      //     title: '仓库管理'
      //   },
      //   component: () => import('@/views/basicdata/warehouse/WarehouseList.vue')
      // },
      // {
      //   path: '/basicdata/account',
      //   name: 'AccountManagement',
      //   meta: {
      //     title: '资金账户'
      //   },
      //   component: () => import('@/views/basicdata/account/AccountList.vue')
      // }
    ]
  }
]

export default routes
