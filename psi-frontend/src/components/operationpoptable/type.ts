export interface TableItem {
  id: string
  name: string
  code: string
  specification: string
  category: string
  brand: string
  unit: string
  purchasePrice: number
  sellingPrice: number
  barcode: string
  children?: TableItem[]
  selected?: boolean
  expanded?: boolean
  checked?: boolean
}
