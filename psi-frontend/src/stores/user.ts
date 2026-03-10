import { defineStore } from 'pinia'
import type { Menu, MenuTreeVO, Oauth2TokenDTO, UserInfo } from '@/apis/login/type'
import { DataUpType, useHttp } from '@/plugins/http'
import { logout } from '@/apis/login'
import { ElMessage } from 'element-plus'
import { useRouterIcons } from '@/stores/router-icons'

/** 静态菜单配置 - 当后端接口不可用时使用 */
const STATIC_MENUS: Array<Menu> = [
  {
    id: 'sysmanage',
    text: '系统配置',
    icon: 'mdi:cogs',
    children: [
      {
        id: 'sysmanage-1',
        text: '字典管理',
        icon: 'mdi:book-open-variant',
        href: '/sysmanage/dictionary',
        hideReport: true
      },
      {
        id: 'sysmanage-2',
        text: '导入模板',
        icon: 'mdi:file-import',
        href: '/sysmanage/import',
        hideReport: true
      },
      {
        id: 'sysmanage-3',
        text: '菜单管理',
        icon: 'mdi:menu',
        href: '/sysmanage/menu',
        hideReport: true
      }
    ]
  },
  {
    id: 'sysparams',
    text: '系统参数',
    icon: 'mdi:tune-vertical',
    children: [
      {
        id: 'sysparams-auxiliary',
        text: '辅助资料',
        icon: 'mdi:folder-information',
        children: [
          {
            id: 'sysparams-auxiliary-1',
            text: '收支类别',
            icon: 'mdi:format-list-bulleted-type',
            href: '/income-expense-category'
          },
          {
            id: 'sysparams-auxiliary-2',
            text: '商品类别',
            icon: 'mdi:format-list-bulleted-type',
            href: '/product-category'
          },
          {
            id: 'sysparams-auxiliary-3',
            text: '条码管理',
            icon: 'mdi:barcode',
            href: '/memo-manage'
          },
          {
            id: 'sysparams-auxiliary-4',
            text: '辅助属性',
            icon: 'mdi:tag-multiple',
            href: '/auxiliary-property'
          },
          {
            id: 'sysparams-auxiliary-5',
            text: '常用功能',
            icon: 'mdi:star',
            href: '/attachment-manage'
          }
        ]
      },
      {
        id: 'sysparams-basic',
        text: '基础资料',
        icon: 'mdi:database',
        children: [
          {
            id: 'sysparams-basic-1',
            text: '商品管理',
            icon: 'mdi:package',
            href: '/product-manage'
          },
          {
            id: 'sysparams-basic-2',
            text: '客户管理',
            icon: 'mdi:account-multiple-outline',
            href: '/customer-manage'
          },
          {
            id: 'sysparams-basic-3',
            text: '供应商管理',
            icon: 'mdi:truck',
            href: '/supplier-manage'
          },
          {
            id: 'sysparams-basic-4',
            text: '仓库管理',
            icon: 'mdi:warehouse',
            href: '/warehouse-manage'
          },
          {
            id: 'sysparams-basic-5',
            text: '资金账户',
            icon: 'mdi:bank',
            href: '/fund-account'
          }
        ]
      },
      {
        id: 'sysparams-advanced',
        text: '高级设置',
        icon: 'mdi:cog-outline',
        children: [
          {
            id: 'sysparams-advanced-1',
            text: '系统设置',
            icon: 'mdi:cog',
            href: '/system-settings'
          },
          {
            id: 'sysparams-advanced-2',
            text: '组织结构',
            icon: 'mdi:sitemap',
            href: '/org-structure'
          },
          {
            id: 'sysparams-advanced-3',
            text: '用户角色',
            icon: 'mdi:account-group',
            href: '/user-role'
          },
          {
            id: 'sysparams-advanced-4',
            text: '用户管理',
            icon: 'mdi:account-multiple',
            href: '/user-manage'
          },
          {
            id: 'sysparams-advanced-5',
            text: '人员管理',
            icon: 'mdi:account-tie',
            href: '/staff-manage'
          },
          {
            id: 'sysparams-advanced-6',
            text: '操作日志',
            icon: 'mdi:history',
            href: '/operation-log'
          },
          {
            id: 'sysparams-advanced-7',
            text: '结账管理',
            icon: 'mdi:calendar-check',
            href: '/account-manage'
          },
          {
            id: 'sysparams-advanced-8',
            text: '单据编号',
            icon: 'mdi:numeric',
            href: '/doc-numbering'
          }
        ]
      }
    ]
  },
  {
    id: 'purchase',
    text: '采购管理',
    icon: 'mdi:cart-arrow-down',
    children: [
      {
        id: 'purchase-1',
        text: '采购订单',
        icon: 'mdi:cart-arrow-down',
        href: '/purchase-booking',
        hasReport: true
      },
      {
        id: 'purchase-2',
        text: '采购单',
        icon: 'mdi:cart-check',
        href: '/purchase-order',
        hasReport: true
      },
      {
        id: 'purchase-3',
        text: '采购退货单',
        icon: 'mdi:cart-remove',
        href: '/purchase-return',
        hasReport: true
      }
    ]
  },
  {
    id: 'sales',
    text: '销售管理',
    icon: 'mdi:cart-arrow-up',
    children: [
      {
        id: 'sales-1',
        text: '销售订单',
        icon: 'mdi:cart-arrow-up',
        href: '/sales-booking',
        hasReport: true
      },
      {
        id: 'sales-2',
        text: '销售单',
        icon: 'mdi:cart-check',
        href: '/sales-order',
        hasReport: true
      },
      {
        id: 'sales-3',
        text: '销售退货单',
        icon: 'mdi:cart-remove',
        href: '/sales-return',
        hasReport: true
      }
    ]
  },
  {
    id: 'warehouse',
    text: '仓库管理',
    icon: 'mdi:warehouse',
    children: [
      {
        id: 'warehouse-1',
        text: '库存查询',
        icon: 'mdi:package-variant',
        href: '/inventory-query'
      },
      {
        id: 'warehouse-2',
        text: '批次查询',
        icon: 'mdi:package-variant-closed',
        href: '/batch-query'
      },
      {
        id: 'warehouse-3',
        text: '库存盘点',
        icon: 'mdi:clipboard-list',
        href: '/inventory-check'
      },
      {
        id: 'warehouse-4',
        text: '其他入库单',
        icon: 'mdi:package-down',
        href: '/other-inbound',
        hasReport: true
      },
      {
        id: 'warehouse-5',
        text: '其他出库单',
        icon: 'mdi:package-up',
        href: '/other-outbound',
        hasReport: true
      },
      {
        id: 'warehouse-6',
        text: '调拨单',
        icon: 'mdi:truck-delivery',
        href: '/transfer-order',
        hasReport: true
      }
    ]
  },
  {
    id: 'finance',
    text: '资金管理',
    icon: 'mdi:cash-multiple',
    children: [
      {
        id: 'finance-1',
        text: '收款单',
        icon: 'mdi:cash-plus',
        href: '/receipt-order',
        hasReport: true
      },
      {
        id: 'finance-2',
        text: '付款单',
        icon: 'mdi:cash-minus',
        href: '/payment-order',
        hasReport: true
      },
      {
        id: 'finance-3',
        text: '核销单',
        icon: 'mdi:check-circle',
        href: '/writeoff-order',
        hasReport: true
      },
      {
        id: 'finance-4',
        text: '转账单',
        icon: 'mdi:bank-transfer',
        href: '/transfer-account',
        hasReport: true
      },
      {
        id: 'finance-5',
        text: '其它收入单',
        icon: 'mdi:cash-plus',
        href: '/other-income',
        hasReport: true
      },
      {
        id: 'finance-6',
        text: '其它支出单',
        icon: 'mdi:cash-minus',
        href: '/other-expense',
        hasReport: true
      },
      {
        id: 'finance-7',
        text: '购销费用',
        icon: 'mdi:currency-usd',
        href: '/purchase-sales-cost',
        hasReport: true
      },
      {
        id: 'finance-8',
        text: '购销发票',
        icon: 'mdi:receipt',
        href: '/purchase-sales-invoice',
        hasReport: true
      }
    ]
  },
  {
    id: 'report',
    text: '报表管理',
    icon: 'mdi:chart-bar',
    children: [
      {
        id: 'report-purchase',
        text: '采购报表',
        icon: 'mdi:chart-bar',
        href: '/purchase-report',
        children: [
          {
            id: 'report-purchase-1',
            text: '采购订单跟踪表',
            icon: 'mdi:chart-timeline',
            href: '/report/purchase/order-followup'
          },
          {
            id: 'report-purchase-2',
            text: '采购明细表',
            icon: 'mdi:file-table',
            href: '/report/purchase/detail'
          },
          {
            id: 'report-purchase-3',
            text: '采购汇总表',
            icon: 'mdi:file-document',
            href: '/report/purchase/summary'
          },
          {
            id: 'report-purchase-4',
            text: '采购付款表',
            icon: 'mdi:cash-check',
            href: '/report/purchase/payment'
          },
          {
            id: 'report-purchase-5',
            text: '采购排行表',
            icon: 'mdi:chart-bar',
            href: '/report/purchase/ranking'
          }
        ]
      },
      {
        id: 'report-sales',
        text: '销售报表',
        icon: 'mdi:chart-bar',
        href: '/sales-report',
        children: [
          {
            id: 'report-sales-1',
            text: '销售订单跟踪表',
            icon: 'mdi:chart-timeline',
            href: '/report/sales/order-followup'
          },
          {
            id: 'report-sales-2',
            text: '销售明细表',
            icon: 'mdi:file-table',
            href: '/report/sales/detail'
          },
          {
            id: 'report-sales-3',
            text: '销售汇总表',
            icon: 'mdi:file-document',
            href: '/report/sales/summary'
          },
          {
            id: 'report-sales-4',
            text: '销售收款表',
            icon: 'mdi:cash-check',
            href: '/report/sales/receipt'
          },
          {
            id: 'report-sales-5',
            text: '销售排行表',
            icon: 'mdi:chart-bar',
            href: '/report/sales/ranking'
          },
          {
            id: 'report-sales-6',
            text: '销售利润表',
            icon: 'mdi:currency-usd',
            href: '/report/sales/profit'
          }
        ]
      },
      {
        id: 'report-warehouse',
        text: '仓库报表',
        icon: 'mdi:chart-bar',
        href: '/warehouse-report',
        children: [
          {
            id: 'report-warehouse-1',
            text: '商品库存余额表',
            icon: 'mdi:warehouse',
            href: '/report/warehouse/inventory-summary'
          },
          {
            id: 'report-warehouse-2',
            text: '商品收发明细表',
            icon: 'mdi:file-table',
            href: '/report/warehouse/receipt-detail'
          },
          {
            id: 'report-warehouse-3',
            text: '商品收发汇总表',
            icon: 'mdi:file-document',
            href: '/report/warehouse/receipt-summary'
          }
        ]
      },
      {
        id: 'report-fund',
        text: '资金报表',
        icon: 'mdi:chart-bar',
        href: '/fund-report',
        children: [
          {
            id: 'report-fund-1',
            text: '现金银行报表',
            icon: 'mdi:bank',
            href: '/report/fund/current-account'
          },
          {
            id: 'report-fund-2',
            text: '应收款明细表',
            icon: 'mdi:file-table',
            href: '/report/fund/receivable-detail'
          },
          {
            id: 'report-fund-3',
            text: '应付款明细表',
            icon: 'mdi:file-table',
            href: '/report/fund/payable-detail'
          },
          {
            id: 'report-fund-4',
            text: '其他收支明细表',
            icon: 'mdi:file-table',
            href: '/report/fund/other-receipt-detail'
          },
          {
            id: 'report-fund-5',
            text: '客户对账单',
            icon: 'mdi:account-details',
            href: '/report/fund/customer-statement'
          },
          {
            id: 'report-fund-6',
            text: '供应商对账单',
            icon: 'mdi:account-details',
            href: '/report/fund/supplier-statement'
          },
          {
            id: 'report-fund-7',
            text: '利润表',
            icon: 'mdi:chart-line',
            href: '/report/fund/profit-statement'
          },
          {
            id: 'report-fund-8',
            text: '往来单位欠款表',
            icon: 'mdi:account-cash',
            href: '/report/fund/debt-age-analysis'
          }
        ]
      }
    ]
  }
]

export const useUserStore = defineStore('user', {
  state: () => ({
    // 记录token
    token: null as string | null,
    // 记录refreshToken
    refreshToken: null as string | null,
    // 保存一个标识信息，指示登陆后需要加载的初始化数据是否完成
    loaded: false,
    // 保存当前用户
    user: null as UserInfo | null,
    // 菜单数据 - 从后端动态加载
    menus: [] as Array<Menu>
  }),
  getters: {
    // 获取token
    getToken: (state) => state.token || localStorage.getItem('token'),
    // 是否已加载
    isLoaded: (state) => state.loaded,
    // 获取当前用户
    getUser: (state) => state.user,
    // 获取菜单
    getMenus: (state) => state.menus
  },
  actions: {
    // 加载用户
    async loadUser() {
      // 发送获取当前用户信息请求，添加时间戳参数防止缓存
      const data = await useHttp().get<UserInfo>(
        '/login/current-user',
        {
          _t: Date.now()
        },
        {
          headers: {
            'Cache-Control': 'no-cache'
          }
        }
      )
      if (data.data) this.user = data.data
      if (!this.user?.avatar) {
        this.user = {
          avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
          ...this.user
        } as UserInfo
      }
    },
    // 加载菜单
    async loadMenus() {
      // 是否使用静态菜单（当后端接口不可用时设置为 true）
      const USE_STATIC_MENUS = false // TODO: 后端接口联调好后改为 false

      // 如果使用静态菜单，直接返回静态配置
      if (USE_STATIC_MENUS) {
        if (import.meta.env.DEV) {
          console.log('[加载菜单] 使用静态菜单配置')
        }
        this.menus = STATIC_MENUS
        return
      }

      // 否则从后端动态加载菜单
      try {
        // 发送获取菜单请求，添加时间戳参数防止缓存
        const response = await useHttp().get<Array<MenuTreeVO>>(
          '/login/get-menus',
          {
            _t: Date.now()
          },
          {
            headers: {
              'Cache-Control': 'no-cache'
            }
          }
        )
        // 开发环境下输出调试信息
        if (import.meta.env.DEV) {
          console.group('🔍 [加载菜单] 开始加载菜单数据')
          console.log('[加载菜单] API响应完整对象:', response)
          console.log('[加载菜单] 原始数据 (response.data):', response.data)
          console.log(
            '[加载菜单] 数据类型:',
            typeof response.data,
            Array.isArray(response.data) ? '(数组)' : '(非数组)'
          )

          // 详细分析每个菜单项的结构
          if (response.data && Array.isArray(response.data)) {
            console.log('[加载菜单] 菜单项数量:', response.data.length)
            console.log('[加载菜单] 完整菜单树结构 (JSON):', JSON.stringify(response.data, null, 2))

            response.data.forEach((menuItem, index) => {
              console.group(`[加载菜单] 菜单项 ${index + 1}: ${menuItem.name || '(未命名)'}`)
              console.log('ID:', menuItem.id)
              console.log('名称:', menuItem.name)
              console.log('是否有子菜单:', !!(menuItem.children && menuItem.children.length > 0))
              console.log('子菜单数量:', menuItem.children?.length || 0)
              console.log('所有字段名:', Object.keys(menuItem))
              console.log('完整数据对象:', menuItem)

              // 递归打印子菜单
              if (menuItem.children && menuItem.children.length > 0) {
                console.group('子菜单:')
                menuItem.children.forEach((child, childIndex) => {
                  console.group(`子菜单 ${childIndex + 1}: ${child.name || '(未命名)'}`)
                  console.log('ID:', child.id)
                  console.log('名称:', child.name)
                  console.log('是否有子菜单:', !!(child.children && child.children.length > 0))
                  console.log('子菜单数量:', child.children?.length || 0)
                  console.log('完整数据对象:', child)

                  // 三级菜单
                  if (child.children && child.children.length > 0) {
                    console.group('三级菜单:')
                    child.children.forEach((grandchild, grandchildIndex) => {
                      console.log(
                        `三级菜单 ${grandchildIndex + 1}:`,
                        grandchild.name || '(未命名)',
                        grandchild
                      )
                    })
                    console.groupEnd()
                  }
                  console.groupEnd()
                })
                console.groupEnd()
              }
              console.groupEnd()
            })
          } else {
            console.warn('[加载菜单] ⚠️ 响应数据不是数组或为空:', response.data)
          }
          console.groupEnd()
        }
        if (response.data) {
          // 将后端返回的 MenuTreeVO 转换为前端需要的 Menu 格式
          let transformedMenus = this.transformMenuTree(response.data)

          // 处理首页菜单：如果首页菜单有子菜单，将子菜单提升到父级，然后过滤掉首页菜单项
          // 首页路径定义在 tab store 中，这里使用相同的常量
          const INDEX_PATH = '/dashboard'
          const HOME_MENU_NAMES = ['首页', 'Home', 'home'] // 可能的首页菜单名称

          // 查找首页菜单项
          const homeMenuIndex = transformedMenus.findIndex(
            (menu) =>
              menu.text === '首页' ||
              menu.href === INDEX_PATH ||
              HOME_MENU_NAMES.includes(menu.text || '')
          )

          if (homeMenuIndex !== -1) {
            const homeMenu = transformedMenus[homeMenuIndex]

            if (import.meta.env.DEV) {
              console.log('[加载菜单] 检测到首页菜单项:', {
                id: homeMenu.id,
                text: homeMenu.text,
                href: homeMenu.href,
                hasChildren: !!(homeMenu.children && homeMenu.children.length > 0),
                childrenCount: homeMenu.children?.length || 0
              })
            }

            // 如果首页菜单有子菜单，将子菜单提升到父级
            if (homeMenu.children && homeMenu.children.length > 0) {
              if (import.meta.env.DEV) {
                console.log('[加载菜单] 首页菜单有子菜单，将子菜单提升到父级')
              }
              // 将首页的子菜单插入到首页菜单的位置
              transformedMenus.splice(homeMenuIndex, 1, ...homeMenu.children)
            } else {
              // 如果首页菜单没有子菜单，直接移除
              if (import.meta.env.DEV) {
                console.log('[加载菜单] 首页菜单没有子菜单，直接移除')
              }
              transformedMenus.splice(homeMenuIndex, 1)
            }
          }

          if (import.meta.env.DEV) {
            console.group('🔄 [加载菜单] 转换后的菜单数据')
            console.log('[加载菜单] 转换后的菜单数组:', transformedMenus)
            console.log('[加载菜单] 转换后的菜单数量:', transformedMenus.length)
            console.log(
              '[加载菜单] 转换后的完整菜单树 (JSON):',
              JSON.stringify(transformedMenus, null, 2)
            )

            // 详细检查每个转换后的菜单项
            transformedMenus.forEach((menu, index) => {
              console.group(`[加载菜单] 转换后菜单项 ${index + 1}: ${menu.text || '(未命名)'}`)
              console.log('ID:', menu.id)
              console.log('文本:', menu.text)
              console.log('图标:', menu.icon)
              console.log('路径:', menu.href)
              console.log('父ID:', menu.pid)
              console.log('是否有子菜单:', !!(menu.children && menu.children.length > 0))
              console.log('子菜单数量:', menu.children?.length || 0)
              console.log('完整数据对象:', menu)

              // 递归打印子菜单
              if (menu.children && menu.children.length > 0) {
                console.group('转换后的子菜单:')
                menu.children.forEach((child, childIndex) => {
                  console.group(`子菜单 ${childIndex + 1}: ${child.text || '(未命名)'}`)
                  console.log('ID:', child.id)
                  console.log('文本:', child.text)
                  console.log('路径:', child.href)
                  console.log('是否有子菜单:', !!(child.children && child.children.length > 0))
                  console.log('子菜单数量:', child.children?.length || 0)
                  console.log('完整数据对象:', child)

                  // 三级菜单
                  if (child.children && child.children.length > 0) {
                    console.group('转换后的三级菜单:')
                    child.children.forEach((grandchild, grandchildIndex) => {
                      console.log(`三级菜单 ${grandchildIndex + 1}:`, {
                        id: grandchild.id,
                        text: grandchild.text,
                        href: grandchild.href,
                        fullData: grandchild
                      })
                    })
                    console.groupEnd()
                  }
                  console.groupEnd()
                })
                console.groupEnd()
              }
              console.groupEnd()
            })

            // 详细检查采购相关的菜单 - 递归查找
            const findMenuRecursive = (menus: any[], text: string): any => {
              for (const menu of menus) {
                if (menu.text === text || menu.text?.includes(text)) {
                  return menu
                }
                if (menu.children && menu.children.length > 0) {
                  const found = findMenuRecursive(menu.children, text)
                  if (found) return found
                }
              }
              return null
            }
            const purchaseMenu = findMenuRecursive(transformedMenus, '采购')
            if (purchaseMenu) {
              console.group('🛒 [加载菜单] 采购菜单详情')
              console.log('采购菜单完整数据:', purchaseMenu)
              if (purchaseMenu.children) {
                console.log('采购菜单子菜单数量:', purchaseMenu.children.length)
                purchaseMenu.children.forEach((child: any, index: number) => {
                  console.group(`采购子菜单 ${index + 1}: "${child.text}"`)
                  console.log('完整数据:', {
                    id: child.id,
                    text: child.text,
                    href: child.href,
                    hasChildren: !!(child.children && child.children.length > 0),
                    childrenCount: child.children?.length || 0,
                    children: child.children
                  })
                  if (child.children && child.children.length > 0) {
                    console.log('三级菜单列表:')
                    child.children.forEach((grandchild: any, gcIndex: number) => {
                      console.log(`  三级菜单 ${gcIndex + 1}:`, {
                        id: grandchild.id,
                        text: grandchild.text,
                        href: grandchild.href,
                        fullData: grandchild
                      })
                    })
                  }
                  console.groupEnd()
                })
              } else {
                console.warn('⚠️ 采购菜单没有子菜单!')
              }
              console.groupEnd()
            } else {
              console.warn('⚠️ 未找到采购菜单!')
            }
            console.groupEnd()
          }
          this.menus = transformedMenus
        } else {
          if (import.meta.env.DEV) {
            console.warn('[加载菜单] 响应数据为空，菜单数组将被清空')
          }
          this.menus = []
        }
      } catch (error: any) {
        // 记录详细错误信息
        console.error('[加载菜单] 请求失败:', error)
        console.error('[加载菜单] 错误详情:', {
          message: error?.message,
          status: error?.status || error?.response?.status,
          response: error?.response,
          responseData: error?.responseData || error?.response?.data,
          config: error?.config,
          url: error?.config?.url || error?.responseData?.path
        })

        // 请求失败时清空菜单，避免显示旧数据
        this.menus = []

        // 根据不同的错误状态码显示不同的提示
        const status = error?.status || error?.response?.status
        if (status === 500) {
          const errorMsg = error?.message || '获取菜单失败，服务器内部错误'
          const responseData = error?.responseData || error?.response?.data
          const detailMsg = responseData?.message || responseData?.error || ''
          const fullMsg = detailMsg ? `${errorMsg} (${detailMsg})` : errorMsg
          ElMessage.error(`加载菜单失败: ${fullMsg}。请检查后端服务是否正常运行，或联系管理员。`)
        } else if (status === 401) {
          ElMessage.error('未授权，请重新登录')
        } else if (status === 403) {
          ElMessage.error('权限不足，无法加载菜单')
        } else if (status === 404) {
          ElMessage.error('菜单接口不存在，请联系管理员')
        } else if (status) {
          ElMessage.error(`加载菜单失败: ${error?.message || `HTTP ${status} 错误`}`)
        } else {
          ElMessage.error(`加载菜单失败: ${error?.message || '未知错误'}`)
        }

        throw error
      }
    },
    // 菜单名称到路径的映射表（使用现有的前端路由）
    getMenuPathMap(): Record<string, string> {
      return {
        // 暂时未实现的功能，映射到404页面或最接近的现有路由
        数据备份: '/system-settings', // 映射到系统设置
        数据核准: '/operation-log', // 映射到操作日志
        表单字段: '/sysmanage/menu', // 映射到菜单管理
        序列查询: '/batch-query', // 映射到批次查询
        test2: '/dashboard', // 映射到首页
        // 首页
        首页: '/dashboard'
      }
    },
    // 将后端菜单树转换为前端菜单格式
    transformMenuTree(menuTree: Array<MenuTreeVO>): Array<Menu> {
      return menuTree.map((item) => {
        // 优先使用后端提供的路径（检查多个可能的字段名）
        let href =
          item.href ||
          item.resource ||
          (item as any).path ||
          (item as any).url ||
          (item as any).route ||
          (item as any).link ||
          undefined

        // 如果后端没有提供路径，尝试从映射表中查找
        if (!href && item.name) {
          const pathMap = this.getMenuPathMap()
          href = pathMap[item.name]
        }

        // 处理 hasReport 字段（可能是字符串 "true"/"false" 或布尔值）
        let hasReport = false
        if (item.hasReport !== undefined && item.hasReport !== null) {
          if (typeof item.hasReport === 'string') {
            hasReport = item.hasReport === 'true' || item.hasReport === '1'
          } else {
            hasReport = Boolean(item.hasReport)
          }
        }

        // 获取报表路径（优先使用 reportHref，如果没有则使用 href + '-report'）
        const reportHref = item.reportHref || undefined

        // 如果后端没有提供图标，则尝试从路由配置中获取
        const frontendIcon = href ? useRouterIcons().getIconByPath(href) : ''

        const menu: Menu = {
          id: String(item.id),
          text: item.name || '',
          icon: item.icon || frontendIcon || 'mdi:menu', // 优先使用后端图标，其次使用路由图标，最后使用默认图标
          href: href,
          pid: item.pid ? String(item.pid) : undefined,
          hideReport: item.hideReport || false,
          hasReport: hasReport,
          reportHref: reportHref
        }

        // 递归转换子菜单（即使 children 是空数组也要处理，确保结构完整）
        if (item.children && Array.isArray(item.children)) {
          if (item.children.length > 0) {
            menu.children = this.transformMenuTree(item.children)
          }
          // 注意：如果 children 是空数组，不设置 menu.children，表示这是叶子节点
        }

        // 开发环境下，如果叶子节点仍然没有路径，输出警告
        const isLeafNode = !item.children || item.children.length === 0
        if (import.meta.env.DEV && !href && isLeafNode) {
          console.warn('[菜单转换] 叶子菜单项缺少路径字段:', {
            id: item.id,
            name: item.name,
            availableFields: Object.keys(item),
            fullData: item,
            // 详细列出所有可能的路径字段
            pathFields: {
              href: item.href,
              resource: item.resource,
              path: item.path,
              url: item.url,
              route: item.route,
              link: item.link
            },
            // 提示：这是后端问题，需要后端在返回数据时提供 href 或 resource 字段
            suggestion:
              '后端返回的菜单数据缺少路径字段（href 或 resource），请联系后端开发人员添加该字段'
          })
        }

        return menu
      })
    },
    // 加载刷新凭证
    loadRefreshToken() {
      if (!this.refreshToken) this.refreshToken = localStorage.getItem('refreshToken')
    },
    // 加载token
    loadToken() {
      if (!this.token) this.token = localStorage.getItem('token')
    },
    // 刷新token
    async reloadToken() {
      // 先加载token和刷新凭证
      this.loadToken()
      this.loadRefreshToken()

      // 验证token和refreshToken是否存在
      if (!this.token || !this.refreshToken) {
        throw new Error('Token或RefreshToken不存在，请重新登录')
      }

      // 验证token格式（JWT应该包含两个点分隔符）
      if (this.token.split('.').length !== 3) {
        throw new Error('Token格式不正确，请重新登录')
      }

      if (this.refreshToken.split('.').length !== 3) {
        throw new Error('RefreshToken格式不正确，请重新登录')
      }

      // 发送刷新凭证请求
      const data = await useHttp().post<Oauth2TokenDTO>(
        '/login/refresh-token',
        {
          refreshToken: this.refreshToken,
          token: this.token
        },
        {
          upType: DataUpType.form
        }
      )
      //设置Token相关属性
      this.setToken(data.data)
    },
    // 设置是否加载完成
    setLoaded(loaded: boolean) {
      this.loaded = loaded
    },
    // 设置token
    setToken(data: any) {
      this.token = data.token
      this.refreshToken = data.refreshToken
      if (this.token) localStorage.setItem('token', this.token)
      if (this.refreshToken) localStorage.setItem('refreshToken', this.refreshToken)
    },
    // 重置数据（退出登录）
    async resetSaveData() {
      // 先调用后端退出登录接口
      try {
        await logout()
      } catch (err) {
        // 退出登录接口调用失败不影响前端清理数据
        if (import.meta.env.DEV) {
          console.warn('[退出登录] 后端接口调用失败，但继续清理前端数据:', err)
        }
      }
      // 清理前端数据
      this.loaded = false
      this.user = null
      this.token = null
      this.refreshToken = null
      localStorage.removeItem('token')
      localStorage.removeItem('refreshToken')
    }
  }
})
