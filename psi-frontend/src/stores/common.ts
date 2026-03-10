import { defineStore } from 'pinia'
import type {
  Dict,
  OrgTreeNode,
  PaymentCategory,
  ProductCategory,
  Warehouse
} from '@/apis/common/type'
import { useHttp } from '@/plugins/http'
import {
  getOrgTree,
  getPaymentCategories,
  getProductCategories,
  getWarehouseList
} from '@/apis/common'

/**
 * 存放通用数据，如字典数据、组织结构、类别等
 */
export const useCommonStore = defineStore('common', {
  state: () => ({
    /** 字典数据 */
    dicts: {} as Record<string, Dict>,
    /** 组织结构树 */
    orgTree: [] as OrgTreeNode[],
    /** 收支类别列表 */
    paymentCategories: [] as PaymentCategory[],
    /** 商品类别列表 */
    productCategories: [] as ProductCategory[],
    /** 仓库列表 */
    warehouses: [] as Warehouse[],
    /** 收支类型数据（用于兼容旧的 Vuex iet 结构） */
    iet: {
      income: [] as PaymentCategory[],
      expense: [] as PaymentCategory[]
    }
  }),
  getters: {
    /** 通过类型编码获取字典数据 */
    getDicts: (state) => {
      return (code: string) => state.dicts[code]
    },
    /** 获取组织结构树 */
    getOrgTree: (state) => state.orgTree,
    /** 获取收支类别列表 */
    getPaymentCategories: (state) => state.paymentCategories,
    /** 获取商品类别列表 */
    getProductCategories: (state) => state.productCategories,
    /** 获取仓库列表 */
    getWarehouses: (state) => state.warehouses,
    /** 获取收支类型（兼容旧的 Vuex iet） */
    getIet: (state) => state.iet,
    /** 兼容旧 Vuex 的 warehouse 访问 */
    warehouse: (state) => state.warehouses
  },
  actions: {
    /**
     * 加载指定编码字典数据
     * @param code 字典编码
     * @param force 是否强制刷新
     */
    async loadDict(code: string, force: boolean = false) {
      // 已经加载过数据就不在加载
      if (this.dicts[code] && !force) {
        return
      }
      // 访问网络获取字典数据
      const data = await useHttp().get<Dict>('/dict', { code })
      // 数据存储到状态中
      if (data.data) this.dicts[code] = data.data
    },
    /**
     * 移除指定编码的字典
     * @param code 字典编码
     */
    remDict(code: string) {
      delete this.dicts[code]
    },

    /**
     * 加载组织结构树
     * @param force 是否强制刷新
     */
    async loadOrgTree(force: boolean = false) {
      if (this.orgTree.length > 0 && !force) {
        return
      }
      const data = await getOrgTree()
      this.orgTree = data
    },

    /**
     * 加载收支类别列表
     * @param params 查询条件
     * @param force 是否强制刷新
     */
    async loadPaymentCategories(
      params?: { type?: 'income' | 'expense'; keyword?: string },
      force: boolean = false
    ) {
      if (this.paymentCategories.length > 0 && !force) {
        return
      }
      const data = await getPaymentCategories(params)
      this.paymentCategories = data
    },

    /**
     * 加载商品类别列表
     * @param force 是否强制刷新
     */
    async loadProductCategories(force: boolean = false) {
      if (this.productCategories.length > 0 && !force) {
        return
      }
      const data = await getProductCategories()
      this.productCategories = data
    },

    /**
     * 加载仓库列表
     * @param force 是否强制刷新
     */
    async loadWarehouses(force: boolean = false) {
      if (this.warehouses.length > 0 && !force) {
        return
      }
      const data = await getWarehouseList()
      this.warehouses = data
    },

    /**
     * 加载收支类型（收入和支出）
     * @param force 是否强制刷新
     */
    async loadIet(force: boolean = false) {
      if ((this.iet.income.length > 0 || this.iet.expense.length > 0) && !force) {
        return
      }
      // 加载收入类型
      const incomeData = await getPaymentCategories({ type: 'income' })
      this.iet.income = incomeData
      // 加载支出类型
      const expenseData = await getPaymentCategories({ type: 'expense' })
      this.iet.expense = expenseData
    },

    /** 重置所有数据 */
    reset() {
      this.dicts = {}
      this.orgTree = []
      this.paymentCategories = []
      this.productCategories = []
      this.warehouses = []
      this.iet = {
        income: [],
        expense: []
      }
    }
  }
})
