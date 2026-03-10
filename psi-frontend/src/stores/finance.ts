import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 其它收入单数据类型
export interface OtherIncomeHeaderForm {
  customer: string
  documentDate: string
  documentNumber: string
}

export interface OtherIncomeTableRow {
  incomeType: string
  settlementAmount: string
  remark: string
}

export interface OtherIncomeData {
  headerForm: OtherIncomeHeaderForm
  tableData: OtherIncomeTableRow[]
  isAudited: boolean
  isChecked: boolean
}

// 底部表单字段类型
export interface DocumentFormFields {
  documentCost?: string
  actualAmount?: string
  receivedAmount?: string
  paidAmount?: string // 添加实付金额字段
  settlementAccount?: string
  relatedPerson?: string
  documentFee?: string
  logistics?: string
  documentAttachment?: string
  remarks?: string
}

// 其它支出单数据类型
export interface OtherExpenseHeaderForm {
  supplier: string
  documentDate: string
  documentNumber: string
}

export interface OtherExpenseTableRow {
  expenseType: string
  settlementAmount: string
  remark: string
}

export interface OtherExpenseData {
  headerForm: OtherExpenseHeaderForm
  tableData: OtherExpenseTableRow[]
  isAudited: boolean
  isChecked: boolean
}

// 购销费用数据类型
export interface TradeExpenseRecord {
  id: number
  orderType: string
  orgName: string
  contactUnit: string
  orderDate: string
  orderNo: string
  expenseType: string
  settlementStatus: string
  amount: number
  settledAmount: number
  unsettledAmount: number
  settlementAmount: number
}

export interface TradeExpenseData {
  records: TradeExpenseRecord[]
  filters: {
    supplier?: string
    customer?: string
    orderNo?: string
    startTime?: string
    endTime?: string
    expenseType?: string
    settlementStatus?: string
    orderType?: string
  }
}

// 结算数据类型
export interface SettlementRecord {
  id: number
  orderNo: string
  contactUnit: string
  expenseType: string
  unsettledAmount: number
  currentSettlementAmount: number
  afterSettlementStatus: string
}

export interface SettlementForm {
  settlementAccount: string
  paidAmount: number
  settlementDate: string
  remarks: string
}

export interface SettlementData {
  records: SettlementRecord[]
  form: SettlementForm
}

export const useFinanceStore = defineStore('finance', () => {
  // 状态
  const otherIncomeData = ref<OtherIncomeData | null>(null)
  const otherExpenseData = ref<OtherExpenseData | null>(null)
  const tradeExpenseData = ref<TradeExpenseData>({
    records: [],
    filters: {}
  })
  const settlementData = ref<SettlementData>({
    records: [],
    form: {
      settlementAccount: '',
      paidAmount: 0,
      settlementDate: new Date().toISOString().split('T')[0],
      remarks: ''
    }
  })

  const documentFormData = ref<DocumentFormFields>({
    documentCost: '',
    actualAmount: '',
    receivedAmount: '',
    paidAmount: '', // 初始化实付金额
    settlementAccount: '',
    relatedPerson: '',
    documentFee: '',
    logistics: '',
    documentAttachment: '',
    remarks: ''
  })

  const financeItems = ref<any[]>([])

  // Getters
  const getOtherIncomeData = computed(() => otherIncomeData.value)
  const getOtherExpenseData = computed(() => otherExpenseData.value)
  const getTradeExpenseData = computed(() => tradeExpenseData.value)
  const getSettlementData = computed(() => settlementData.value)
  const getDocumentFormData = computed(() => documentFormData.value)
  const getFinanceItems = computed(() => financeItems.value)

  // 购销费用相关的计算属性
  const getFilteredTradeExpenseRecords = computed(() => {
    let records = [...tradeExpenseData.value.records]
    const filters = tradeExpenseData.value.filters

    if (filters.supplier) {
      records = records.filter((record) => record.contactUnit.includes(filters.supplier!))
    }

    if (filters.customer) {
      records = records.filter((record) => record.contactUnit.includes(filters.customer!))
    }

    if (filters.orderNo) {
      records = records.filter((record) => record.orderNo.includes(filters.orderNo!))
    }

    if (filters.orderType) {
      records = records.filter((record) => record.orderType === filters.orderType)
    }

    if (filters.expenseType) {
      records = records.filter((record) => record.expenseType === filters.expenseType)
    }

    if (filters.settlementStatus) {
      records = records.filter((record) => record.settlementStatus === filters.settlementStatus)
    }

    if (filters.startTime) {
      records = records.filter((record) => record.orderDate >= filters.startTime!)
    }

    if (filters.endTime) {
      records = records.filter((record) => record.orderDate <= filters.endTime!)
    }

    return records
  })

  const getTradeExpenseTotals = computed(() => {
    const records = getFilteredTradeExpenseRecords.value
    return {
      totalAmount: records.reduce((sum, record) => sum + record.amount, 0),
      totalSettledAmount: records.reduce((sum, record) => sum + record.settledAmount, 0),
      totalUnsettledAmount: records.reduce((sum, record) => sum + record.unsettledAmount, 0)
    }
  })

  // Actions
  const saveOtherIncomeData = (data: OtherIncomeData) => {
    otherIncomeData.value = data
  }

  const saveOtherExpenseData = (data: OtherExpenseData) => {
    otherExpenseData.value = data
  }

  // 购销费用相关的方法
  const setTradeExpenseRecords = (records: TradeExpenseRecord[]) => {
    tradeExpenseData.value.records = records
  }

  const setTradeExpenseFilters = (filters: Partial<TradeExpenseData['filters']>) => {
    tradeExpenseData.value.filters = { ...tradeExpenseData.value.filters, ...filters }
  }

  const clearTradeExpenseFilters = () => {
    tradeExpenseData.value.filters = {}
  }

  // 结算相关的方法
  const setSettlementRecords = (records: SettlementRecord[]) => {
    settlementData.value.records = records
  }

  const setSettlementForm = (form: Partial<SettlementForm>) => {
    settlementData.value.form = { ...settlementData.value.form, ...form }
  }

  const processSettlement = async (settlementData: SettlementData) => {
    // 模拟结算处理
    return new Promise<void>((resolve) => {
      setTimeout(() => {
        // 在实际应用中，这里会调用API处理结算
        console.log('处理结算数据:', settlementData)
        resolve()
      }, 1000)
    })
  }

  const setDocumentFormData = (data: Partial<DocumentFormFields>) => {
    documentFormData.value = { ...documentFormData.value, ...data }
  }

  const setFinanceItems = (items: any[]) => {
    financeItems.value = items
  }

  const clearOtherIncomeData = () => {
    otherIncomeData.value = null
  }

  const clearOtherExpenseData = () => {
    otherExpenseData.value = null
  }

  const clearTradeExpenseData = () => {
    tradeExpenseData.value = {
      records: [],
      filters: {}
    }
  }

  const clearSettlementData = () => {
    settlementData.value = {
      records: [],
      form: {
        settlementAccount: '',
        paidAmount: 0,
        settlementDate: new Date().toISOString().split('T')[0],
        remarks: ''
      }
    }
  }

  const resetDocumentFormData = () => {
    documentFormData.value = {
      documentCost: '',
      actualAmount: '',
      receivedAmount: '',
      paidAmount: '',
      settlementAccount: '',
      relatedPerson: '',
      documentFee: '',
      logistics: '',
      documentAttachment: '',
      remarks: ''
    }
  }

  // 更新收入单金额
  const updateDocumentAmounts = (tableData: OtherIncomeTableRow[]) => {
    const totalAmount = tableData.reduce((sum, row) => {
      return sum + (parseFloat(row.settlementAmount) || 0)
    }, 0)

    documentFormData.value.documentCost = totalAmount.toFixed(2)
    documentFormData.value.actualAmount = totalAmount.toFixed(2)
    documentFormData.value.receivedAmount = totalAmount.toFixed(2)
  }

  // 更新支出单金额
  const updateExpenseDocumentAmounts = (tableData: OtherExpenseTableRow[]) => {
    const totalAmount = tableData.reduce((sum, row) => {
      return sum + (parseFloat(row.settlementAmount) || 0)
    }, 0)

    documentFormData.value.documentCost = totalAmount.toFixed(2)
    documentFormData.value.actualAmount = totalAmount.toFixed(2)
    documentFormData.value.paidAmount = totalAmount.toFixed(2) // 更新实付金额
  }

  return {
    // 状态
    otherIncomeData,
    otherExpenseData,
    tradeExpenseData,
    settlementData,
    documentFormData,
    financeItems,

    // Getters
    getOtherIncomeData,
    getOtherExpenseData,
    getTradeExpenseData,
    getSettlementData,
    getDocumentFormData,
    getFinanceItems,
    getFilteredTradeExpenseRecords,
    getTradeExpenseTotals,

    // Actions
    saveOtherIncomeData,
    saveOtherExpenseData,
    setTradeExpenseRecords,
    setTradeExpenseFilters,
    clearTradeExpenseFilters,
    setSettlementRecords,
    setSettlementForm,
    processSettlement,
    setDocumentFormData,
    setFinanceItems,
    clearOtherIncomeData,
    clearOtherExpenseData,
    clearTradeExpenseData,
    clearSettlementData,
    resetDocumentFormData,
    updateDocumentAmounts,
    updateExpenseDocumentAmounts
  }
})
