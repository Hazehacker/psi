import { defineConfig } from 'openapi-ts-request'

/**
 * @see https://github.com/openapi-ui/openapi-ts-request/blob/main/README.md
 */
export default defineConfig({
  // 生成的 API 文件输出目录
  serversPath: './src/apis/openapi-apifox',

  // 使用 axios 作为请求库
  requestLibPath: `import request from '@/apis/openapi-ts-request-shim';\n import type { CustomRequestOptions } from '@/apis/openapi-ts-request-shim';`,
  requestOptionsType: 'CustomRequestOptions',
  isGenJavaScript: false,
  // TODO: 现在暂时不生成 vue-query 的代码，晚些时候再生成
  // isGenReactQuery: true,
  // reactQueryMode: 'vue',

  // 在apifox内点击导出 点击打开url 使用本地生成的url地址
  // 注意 该地址应该是随时生成 随时更改的地址 不要写死
  schemaPath: 'http://127.0.0.1:4523/export/openapi/2?version=3.1'

  // priorityRule: 'include',
  // // includeTags: ['c1', 'c2', 'c3', 'c4', 'c5', 'c6']
  // includeTags: [
  //   'c1',
  //   'c1/系统设置',
  //   'c1/组织结构',
  //   'c1/用户角色',
  //   'c1/用户管理',
  //   'c1/用户管理/商品管理',
  //   'c1/用户管理/供应商管理',
  //   'c1/用户管理/资金账户',
  //   'c1/用户管理/客户管理',
  //   'c1/人员管理',
  //   'c1/操作日志',
  //   'c1/结账管理',
  //   'c1/单据编号',
  //   'c2',
  //   'c2/商品管理',
  //   'c2/供应商管理',
  //   'c2/仓库管理',
  //   'c2/资金账户',
  //   'c2/客户管理',
  //   'c3',
  //   'c3/收支类别',
  //   'c3/商品类别',
  //   'c3/辅助属性',
  //   'c4',
  //   'c4/采购单',
  //   'c4/采购单/获取采购单接口',
  //   'c4/采购单/获取采购单详情接口',
  //   'c4/采购单/获取生成采购退货单的数据',
  //   'c4/采购单/采购单-审核与反审核',
  //   'c4/采购单/订单管理',
  //   'c4/采购单/导出详细报表',
  //   'c4/采购退货单',
  //   'c4/采购退货单/采购退货单示例接口',
  //   'c4/采购退货单/导出详细报表',
  //   'c4/采购退货单/导出简易报表',
  //   'c4/采购退货单/采购退货单',
  //   'c4/采购退货单/导入数据',
  //   'c4/采购退货单/审核/反审核（支持批量）',
  //   'c4/采购订单',
  //   'c4/采购订单/导入数据',
  //   'c4/采购订单/采购管理',
  //   'c4/采购订单/订单管理',
  //   'c5',
  //   'c5/销售订单',
  //   'c5/销售退货单',
  //   'c5/销售单',
  //   'c6',
  //   'c6/采购明细表',
  //   'c6/客户对账单查询接口',
  //   'c6/销售订单跟踪表查询接口',
  //   'c6/销售明细单',
  //   'c6/销售排行表查询接口',
  //   'c6/商品收发汇总表查询接口',
  //   'c6/采购排行表查询接口',
  //   'c6/采购订单跟踪查询接口',
  //   'c6/销售订单汇总表查询接口',
  //   'c6/其他收入支出明细表查询接口',
  //   'c6/应付账款明细报表',
  //   'c6/库存余额报表',
  //   'c6/商品收发明细表',
  //   'c6/采购付款表',
  //   'c6/现金银行报表查询接口',
  //   'c6/采购汇总表查询接口',
  //   'c6/销售收款表',
  //   'c6/供应商对账单',
  //   'c6/应收款明细表接口',
  //   'c6/往来单位欠款表接口',
  //   'c6/利润表',
  //   'c6/销售利润表'
  // ]
})
