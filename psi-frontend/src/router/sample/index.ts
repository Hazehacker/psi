import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/sample',
    name: 'Sample',
    redirect: { path: '/sample/file' },
    component: () => import('@/views/sample/SampleView.vue'),
    children: [
      {
        name: 'sfile',
        path: '/sample/file',
        meta: { title: '文件上传' },
        component: () => import('@/views/sample/file/FileUpload.vue')
      },
      {
        name: 'sexcel',
        path: '/sample/excel',
        meta: { title: 'Excel演示' },
        component: () => import('@/views/sample/excel/Excel.vue')
      },
      {
        name: 'sprint',
        path: '/sample/print',
        meta: { title: '打印演示' },
        component: () => import('@/views/sample/print/Print.vue')
      },
      {
        name: 'seditor',
        path: '/sample/editor',
        meta: { title: '富文本框演示' },
        component: () => import('@/views/sample/editor/Editor.vue')
      },
      {
        name: 'spdf',
        path: '/sample/pdfobject',
        meta: { title: 'PDF预览演示' },
        component: () => import('@/views/sample/pdfobject/PdfObject.vue')
      },
      {
        name: 'secharts',
        path: '/sample/echarts',
        meta: { title: '图表演示' },
        component: () => import('@/views/sample/echarts/Echarts.vue')
      },
      {
        name: 'sformcreate',
        path: '/sample/formcreate',
        meta: { title: '可视化表单演示' },
        component: () => import('@/views/sample/formcreate/FormCreate.vue')
      },
      {
        name: 'sflow',
        path: '/sample/flow',
        meta: { title: '流程图编辑器演示' },
        component: () => import('@/views/sample/flow/Flow.vue')
      },
      {
        name: 'smytable',
        path: '/sample/mytable',
        meta: { title: '自定义表格' },
        component: () => import('@/views/sample/mytable/Index.vue')
      },
      {
        name: 'smyform',
        path: '/sample/myform',
        meta: { title: '自定义表单' },
        component: () => import('@/views/sample/myform/Index.vue')
      },
      {
        name: 'smysearch',
        path: '/sample/mysearch',
        meta: { title: '自定义搜索' },
        component: () => import('@/views/sample/mysearch/Index.vue')
      },
      {
        name: 'smydialog',
        path: '/sample/mydialog',
        meta: { title: '自定义对话框' },
        component: () => import('@/views/sample/mydialog/Index.vue')
      },
      {
        name: 'sinputlist',
        path: '/sample/inputlist',
        meta: { title: '通用标签管理组件' },
        component: () => import('@/views/sample/inputList/InputListTest.vue')
      },
      {
        name: 'soperationbuttons',
        path: '/sample/operationbuttons',
        meta: { title: '操作按钮组件' },
        component: () => import('@/views/sample/demo/OperationButtonsDemo.vue')
      },
      {
        name: 'sdocumentform',
        path: '/sample/documentform',
        meta: { title: '单据表单演示' },
        component: () => import('@/components/documentform/DocumentForm.vue')
      },
      // 数据表格组件示例
      {
        name: 'sdatatable',
        path: '/sample/datatable',
        meta: { title: '结算表格组件' },
        component: () => import('@/views/sample/datatable/Index.vue')
      },
      {
        name: 'smydatatable',
        path: '/sample/mydatatable',
        meta: { title: '商品数据表格组件' },
        component: () => import('@/views/sample/mydatatable/Index.vue')
      },
      {
        name: 'sampleReportButtonTable',
        path: '/sample/reportButtonTable',
        meta: { title: '报表按钮表格' },
        component: () => import('@/views/sample/reportButtonTable/reportButtonTable.vue')
      },
      {
        name: 'sampleReportDetail',
        path: '/sample/reportButtonTable/detail',
        meta: { title: '报表按钮表格详情' },
        component: () => import('@/views/sample/reportButtonTable/reportDetail.vue')
      },
      {
        name: 'snormaltable',
        path: '/sample/normaltable',
        meta: { title: '报表目录内含表格组件' },
        component: () => import('@/views/sample/normalTable/Index.vue')
      },
      {
        name: 'stest',
        path: '/sample/test',
        meta: { title: '自定义表格筛选框' },
        component: () => import('@/views/sample/test/selecttest.vue')
      },
      {
        name: 'smybutton',
        path: '/sample/mybutton',
        meta: { title: 'MyButton按钮组件' },
        component: () => import('@/views/sample/mybutton/index.vue')
      },
      {
        name: 'ssearchdemo',
        path: '/sample/searchdemo',
        meta: { title: '通用搜索组件演示' },
        component: () => import('@/views/demo/SearchComponentDemo.vue')
      },
      {
        name: 'sgoodsearch',
        path: '/sample/goodsearch',
        meta: { title: '商品搜索组件演示' },
        component: () => import('@/views/sample/goodsearch/Index.vue')
      },
      {
        name: 'ssystemSettings',
        path: '/sample/systemSettings',
        meta: { title: '系统设置' },
        component: () => import('@/views/sample/systemsettings/SystemSettingsDemo.vue')
      },
      {
        name: 'sorganization',
        path: '/sample/organization',
        meta: { title: '组织机构管理' },
        component: () => import('@/views/sample/organization/OrganizationDemo.vue')
      },
      {
        name: 'suserrole',
        path: '/sample/userrole',
        meta: { title: '用户角色管理' },
        component: () => import('@/views/sample/userrole/UserRoleDemo.vue')
      },
      {
        name: 'suser',
        path: '/sample/user',
        meta: { title: '用户管理' },
        component: () => import('@/views/sample/user/UserManageDemo.vue')
      },
      {
        name: 'speople',
        path: '/sample/people',
        meta: { title: '人员管理' },
        component: () => import('@/views/sample/people/PeopleManageDemo.vue')
      },
      {
        name: 'slog',
        path: '/sample/OperationLog',
        meta: { title: '操作日志' },
        component: () => import('@/views/sample/OperationLog/logDemo.vue')
      },
      {
        name: 'speriod',
        path: '/sample/period',
        meta: { title: '结账管理' },
        component: () => import('@/views/sample/period/periodDemo.vue')
      },
      {
        name: 'snumbertype',
        path: '/sample/numbertype',
        meta: { title: '单据编号' },
        component: () => import('@/views/sample/numbertype/numbertypeDemo.vue')
      },
      {
        name: 'siconify',
        path: '/sample/iconify',
        meta: { title: 'Iconify 图标测试' },
        component: () => import('@/views/sample/re-icon-iconify-test/index.vue')
      }
    ]
  }
]

export default routes
