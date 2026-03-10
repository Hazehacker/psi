import { defineStore } from 'pinia'

export interface LogisticsItem {
  key: string
  name: string
  enable: boolean
}

export const useLogisticsStore = defineStore('logistics', {
  state: () => ({
    logistics: [
      { key: 'auto', name: '自动识别', enable: true },
      { key: 'sf', name: '顺丰速运', enable: true },
      { key: 'yto', name: '圆通速递', enable: true },
      { key: 'zto', name: '中通快递', enable: true },
      { key: 'yunda', name: '韵达速递', enable: true },
      { key: 'jd', name: '京东物流', enable: true },
      { key: 'ems', name: '中国邮政', enable: true },
      { key: 'other', name: '其他', enable: true }
    ] as LogisticsItem[]
  }),
  getters: {
    enabledLogistics: (state) => state.logistics.filter(item => item.enable)
  },
  actions: {
    addLogistics(item: LogisticsItem) {
      this.logistics.push(item)
    },
    updateLogistics(key: string, updates: Partial<LogisticsItem>) {
      const index = this.logistics.findIndex(item => item.key === key)
      if (index !== -1) {
        this.logistics[index] = { ...this.logistics[index], ...updates }
      }
    },
    removeLogistics(key: string) {
      this.logistics = this.logistics.filter(item => item.key !== key)
    }
  }
})
