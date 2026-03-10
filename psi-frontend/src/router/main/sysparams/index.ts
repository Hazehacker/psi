import type { RouteRecordRaw } from 'vue-router'

/*
 * @Description: 系统参数的路由
 */
const routes: RouteRecordRaw[] = [
  // 高级设置
  {
    path: '/system-settings',
    name: 'SystemSettings',
    meta: {
      label: '系统设置',
      icon: 'mdi:cog'
    },
    component: () => import('@/views/advancedSettings/sysmanage/SystemSettings.vue')
  },
  {
    path: '/org-structure',
    name: 'OrgStructure',
    meta: {
      label: '组织结构',
      icon: 'mdi:sitemap'
    },
    component: () => import('@/views/advancedSettings/organization/OrganizationManage.vue')
  },
  {
    path: '/user-role',
    name: 'UserRole',
    meta: {
      label: '用户角色',
      icon: 'mdi:account-group'
    },
    component: () => import('@/views/advancedSettings/userrole/UserRoleManage.vue')
  },
  {
    path: '/user-manage',
    name: 'UserManage',
    meta: {
      label: '用户管理',
      icon: 'mdi:account-multiple'
    },
    component: () => import('@/views/advancedSettings/user/UserManage.vue')
  },
  {
    path: '/staff-manage',
    name: 'StaffManage',
    meta: {
      label: '人员管理',
      icon: 'mdi:account-tie'
    },
    component: () => import('@/views/advancedSettings/people/PeopleManage.vue')
  },
  {
    path: '/operation-log',
    name: 'OperationLog',
    meta: {
      label: '操作日志',
      icon: 'mdi:history'
    },
    component: () => import('@/views/advancedSettings/OperationLog/log.vue')
  },
  {
    path: '/account-manage',
    name: 'AccountManage',
    meta: {
      label: '结账管理',
      icon: 'mdi:calendar-check'
    },
    component: () => import('@/views/advancedSettings/period/period.vue')
  },
  {
    path: '/doc-numbering',
    name: 'DocNumbering',
    meta: {
      label: '单据编号',
      icon: 'mdi:numeric'
    },
    component: () => import('@/views/advancedSettings/numbertype/numbertype.vue')
  },
  {
    path: '/common-functions',
    name: 'CommonFunctions',
    meta: {
      label: '常用功能',
      icon: 'mdi:star'
    },
    component: () => import('@/views/status/404.vue')
  },
  {
    path: '/advanced-settings',
    name: 'AdvancedSettings',
    meta: {
      label: '高级设置',
      icon: 'mdi:cog-outline'
    },
    component: () => import('@/views/status/404.vue')
  },
  // 辅助资料
  {
    path: '/income-expense-category',
    name: 'IncomeExpenseCategory',
    meta: {
      label: '收支类别',
      icon: 'mdi:format-list-bulleted-type'
    },
    component: () => import('@/views/assistantdate/IncomeCategory.vue')
  },
  {
    path: '/product-category',
    name: 'ProductCategory',
    meta: {
      label: '商品类别',
      icon: 'mdi:format-list-bulleted-type'
    },
    component: () => import('@/views/assistantdate/ProductCategory.vue')
  },
  {
    path: '/memo-manage',
    name: 'MemoManage',
    meta: {
      label: '条码管理',
      icon: 'mdi:barcode'
    },
    component: () => import('@/views/assistantdate/BarcodeManage.vue')
  },
  {
    path: '/auxiliary-property',
    name: 'AuxiliaryProperty',
    meta: {
      label: '辅助属性',
      icon: 'mdi:tag-multiple'
    },
    component: () => import('@/views/assistantdate/AssistantAttribute.vue')
  },
  {
    path: '/attachment-manage',
    name: 'AttachmentManage',
    meta: {
      label: '常用功能',
      icon: 'mdi:star'
    },
    component: () => import('@/views/assistantdate/CommonFunction.vue')
  },
  {
    path: '/auxiliary-data',
    name: 'AuxiliaryData',
    meta: {
      label: '辅助资料',
      icon: 'mdi:folder-information'
    },
    component: () => import('@/views/status/404.vue')
  },
  {
    path: '/file-manage',
    name: 'FileManage',
    meta: {
      label: '附件管理',
      icon: 'mdi:paperclip'
    },
    component: () => import('@/views/assistantdate/AttachmentManage.vue')
  },

  // 基础资料
  {
    path: '/product-manage',
    name: 'ProductManage',
    meta: {
      label: '商品管理',
      icon: 'mdi:package'
    },
    component: () => import('@/views/basicdata/product/ProductList.vue')
  },
  {
    path: '/customer-manage',
    name: 'CustomerManage',
    meta: {
      label: '客户管理',
      icon: 'mdi:account-multiple-outline'
    },
    component: () => import('@/views/basicdata/customer/CustomerList.vue')
  },
  {
    path: '/supplier-manage',
    name: 'SupplierManage',
    meta: {
      label: '供应商管理',
      icon: 'mdi:truck'
    },
    component: () => import('@/views/basicdata/supplier/SupplierList.vue')
  },
  {
    path: '/warehouse-manage',
    name: 'WarehouseManage',
    meta: {
      label: '仓库管理',
      icon: 'mdi:warehouse'
    },
    component: () => import('@/views/basicdata/warehouse/WarehouseList.vue')
  },
  {
    path: '/fund-account',
    name: 'FundAccount',
    meta: {
      label: '资金账户',
      icon: 'mdi:bank'
    },
    component: () => import('@/views/basicdata/account/AccountList.vue')
  },
  {
    path: '/basic-data',
    name: 'BasicData',
    meta: {
      label: '基础资料',
      icon: 'mdi:database'
    },
    component: () => import('@/views/status/404.vue')
  }
]

export default routes
