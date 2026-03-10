import type { Component } from 'vue'

/**
 * 基础按钮属性
 */
export interface MyButtonProps {
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text'
  size?: 'large' | 'default' | 'small'
  text?: string
  icon?: Component
  loading?: boolean
  disabled?: boolean
  plain?: boolean
  round?: boolean
  circle?: boolean
  link?: boolean
  customClass?: string
  actionType?: 'save' | 'refresh' | 'unaudit' | 'check' | 'generate'
  dataSource?: any
  confirmMessage?: string
}

/**
 * 业务按钮属性
 */
export interface BusinessButtonProps {
  actionType: 'save' | 'refresh' | 'unaudit' | 'check' | 'uncheck' | 'generate'
  size?: 'large' | 'default' | 'small'
  loading?: boolean
  disabled?: boolean
  dataSource?: any
  customText?: string
}

/**
 * 按钮组属性
 */
export interface ButtonGroupProps {
  direction?: 'horizontal' | 'vertical'
  align?: 'left' | 'center' | 'right'
  gap?: string
}

/**
 * 采购审核按钮配置
 */
export const PURCHASE_AUDIT_BUTTONS: BusinessButtonProps[] = [
  { actionType: 'unaudit' },
  { actionType: 'check' },
  { actionType: 'refresh' }
]

/**
 * 销售审核按钮配置
 */
export const SALE_AUDIT_BUTTONS: BusinessButtonProps[] = [
  { actionType: 'generate' },
  { actionType: 'unaudit' },
  { actionType: 'refresh' }
]

/**
 * 通用按钮配置
 */
export const COMMON_BUTTONS: BusinessButtonProps[] = [
  { actionType: 'save' },
  { actionType: 'refresh' }
]
