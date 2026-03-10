import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/sysmanage/dictionary',
    name: 'DICTIONARY',
    meta: {
      label: '字典管理',
      icon: 'mdi:book-open-variant'
    },
    component: () => import('@/views/sysmanage/dictionary/Index.vue')
  },
  {
    path: '/sysmanage/import',
    name: 'IMPORT',
    meta: {
      label: '导入模板',
      icon: 'mdi:file-import'
    },
    component: () => import('@/views/sysmanage/import/Index.vue')
  },
  {
    path: '/sysmanage/menu',
    name: 'MenuManage',
    meta: {
      label: '菜单管理',
      icon: 'mdi:menu'
    },
    component: () => import('@/views/sysmanage/menu/Index.vue')
  }
]

export default routes
