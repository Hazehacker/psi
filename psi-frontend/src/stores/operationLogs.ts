// 操作日志全局 Store
// 用途：按 billCode 存储并提供行级操作日志，支持跨页面（列表/详情）联动显示
import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface OperationLog {
  time: string
  user: string
  action: string
}

export const useOperationLogsStore = defineStore('operationLogs', () => {
  // 操作日志存储，以 billCode 为键
  const logs = ref<Map<string, OperationLog[]>>(new Map())

  // 添加操作日志
  const addLog = (billCode: string, action: string, user: string = '管理员') => {
    const now = new Date()
    const timeStr = now.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).replace(/\//g, '-')

    const log: OperationLog = {
      time: timeStr,
      user,
      action
    }

    if (!logs.value.has(billCode)) {
      logs.value.set(billCode, [])
    }

    const billLogs = logs.value.get(billCode) || []
    billLogs.unshift(log) // 新日志添加到开头

    // 只保留最近10条日志
    if (billLogs.length > 10) {
      billLogs.splice(10)
    }

    logs.value.set(billCode, billLogs)
  }

  // 获取指定单据的日志（若没有则给出默认示例日志）
  const getLogs = (billCode: string): OperationLog[] => {
    const billLogs = logs.value.get(billCode) || []
    
    // 如果没有日志，返回默认日志
    if (billLogs.length === 0) {
      return [
        { time: '2025-10-20 10:54', user: '管理员', action: '创建单据' },
        { time: '2025-10-20 10:49', user: '管理员', action: '审核单据' }
      ]
    }
    
    return billLogs
  }

  // 初始化单据日志（用于页面初始演示）
  const initializeBillLogs = (billCode: string) => {
    if (!logs.value.has(billCode)) {
      addLog(billCode, '创建单据')
      addLog(billCode, '审核单据')
    }
  }

  // 清空所有日志（可用于调试/重置）
  const clearLogs = () => {
    logs.value.clear()
  }

  return {
    logs,
    addLog,
    getLogs,
    initializeBillLogs,
    clearLogs
  }
})
