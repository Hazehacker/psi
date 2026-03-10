// 销售收款数据类型
export interface SalesReceipt {
  id: number
  orderType: string
  orgName: string
  customerName: string
  orderDate: string
  orderNo: string
  orderAmount: number
  actualAmount: number
  receivedAmount: number
  receivableBalance: number
  receiptRate: number
  writeOffStatus: string
  remark: string
}

// 客户数据类型
export interface Customer {
  id: number
  name: string
  code: string
  contact: string
  phone: string
}

// 搜索表单数据类型
export interface SearchForm {
  customerName: string
  customerId: string
  orderNo: string
  orderType: string
  orderStartDate: string
  orderEndDate: string
  writeOffStatus: string
}