import { defineStore } from 'pinia'

export interface PurchaseItem {
  // total price with tax
  tpt: number
  // ... other fields from table may exist
  [key: string]: any
}

export interface PurchaseBookData {
  headerForm: {
    supplier: string
    documentDate: string
    documentNumber: string
    customer?: string
  }
  tableData: any[]
  isAudited: boolean
}

export interface PurchaseOrderData {
  headerForm: {
    supplier: string
    documentDate: string
    documentNumber: string
  }
  tableData: any[]
  isAudited: boolean
  isChecked: boolean
}

export interface PurchaseReturnData {
  headerForm: {
    supplier: string
    documentDate: string
    documentNumber: string
  }
  tableData: any[]
  isAudited: boolean
  isChecked: boolean
}

/**
 * 数据传输接口 - 用于页面间传递 AG-Grid 表格数据
 */
export interface TransferData {
  tableData: any[]
  supplier?: string
  documentDate?: string
  documentNumber?: string
  from?: string // 来源页面标识
  timestamp?: number
}

export const usePurchaseStore = defineStore('purchase', {
  state: () => ({
    items: [] as PurchaseItem[],
    // 采购订单数据
    purchaseBookData: null as PurchaseBookData | null,
    // 采购单数据
    purchaseOrderData: null as PurchaseOrderData | null,
    // 采购退货单数据
    purchaseReturnData: null as PurchaseReturnData | null,
    // 页面间数据传输（临时存储）
    transferData: null as TransferData | null
  }),
  getters: {
    getItems: (state) => state.items,
    getPurchaseBookData: (state) => state.purchaseBookData,
    getPurchaseOrderData: (state) => state.purchaseOrderData,
    getPurchaseReturnData: (state) => state.purchaseReturnData,
    // 获取传输数据
    getTransferData: (state) => state.transferData
  },
  actions: {
    setItems(items: PurchaseItem[]) {
      this.items = items
    },
    addItem(item: PurchaseItem) {
      this.items.push(item)
    },
    updateItem(index: number, item: Partial<PurchaseItem>) {
      if (index < 0 || index >= this.items.length) return
      this.items[index] = { ...this.items[index], ...item }
    },
    removeItem(index: number) {
      if (index < 0 || index >= this.items.length) return
      this.items.splice(index, 1)
    },
    reset() {
      this.items = []
    },
    // 保存采购订单数据
    savePurchaseBookData(data: PurchaseBookData) {
      this.purchaseBookData = data
    },
    // 清空采购订单数据
    clearPurchaseBookData() {
      this.purchaseBookData = null
    },
    // 保存采购单数据
    savePurchaseOrderData(data: PurchaseOrderData) {
      this.purchaseOrderData = data
    },
    // 清空采购单数据
    clearPurchaseOrderData() {
      this.purchaseOrderData = null
    },
    // 保存采购退货单数据
    savePurchaseReturnData(data: PurchaseReturnData) {
      this.purchaseReturnData = data
    },
    // 清空采购退货单数据
    clearPurchaseReturnData() {
      this.purchaseReturnData = null
    },
    /**
     * 设置传输数据 - 用于页面跳转前存储数据
     * @param data 要传输的数据
     */
    setTransferData(data: TransferData) {
      this.transferData = {
        ...data,
        timestamp: Date.now() // 自动添加时间戳
      }
      console.log('📤 [Store] 设置传输数据:', this.transferData)
    },
    /**
     * 清空传输数据 - 用于数据接收后清理
     */
    clearTransferData() {
      console.log('🧹 [Store] 清空传输数据')
      this.transferData = null
    }
  }
})
