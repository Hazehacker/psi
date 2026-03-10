import { useHttp, DataUpType, type RequestCallback } from '@/plugins/http'
import type { LoginDTO, Oauth2TokenDTO } from './type'
import { useUserStore } from '@/stores/user'

// 定义一个功能模块基础url，方便替换
const currBaseUrl = '/login'

/**
 * 登录接口
 * @param data 登录数据
 * @param success 登录成功回调
 * @param fail 登录失败回调
 */
export const login = async (data: LoginDTO, success: RequestCallback, fail: RequestCallback) => {
  const us = useUserStore()
  const http = useHttp()
  try {
    // 开发环境下输出调试信息
    if (import.meta.env.DEV) {
      console.log('[登录接口] 请求参数:', { ...data, password: '***' })
      console.log('[登录接口] 请求URL:', currBaseUrl + '/auth-login')
    }
    // 发送登录请求
    const res = await http.post<Oauth2TokenDTO>(currBaseUrl + '/auth-login', data, {
      upType: DataUpType.form
    })
    // 开发环境下输出响应信息
    if (import.meta.env.DEV) {
      console.log('[登录接口] 响应数据:', res)
    }
    // 记录Token到本地
    if (res.data) {
      us.setToken(res.data)
      // 重置加载状态，确保路由守卫会重新加载用户信息和菜单数据
      us.setLoaded(false)
      // 执行成功回调
      success(res)
      return
    }
    // 执行失败回调
    if (import.meta.env.DEV) {
      console.warn('[登录接口] 响应数据为空:', res)
    }
    fail(res)
  } catch (err) {
    // 开发环境下输出错误信息
    if (import.meta.env.DEV) {
      console.error('[登录接口] 请求失败:', err)
    }
    // 执行失败回调
    fail(err)
  }
}

/**
 * 退出登录接口
 * @returns Promise<void>
 */
export const logout = async (): Promise<void> => {
  const http = useHttp()
  try {
    // 开发环境下输出调试信息
    if (import.meta.env.DEV) {
      console.log('[退出登录接口] 请求URL:', currBaseUrl + '/logout')
    }
    // 发送退出登录请求
    const res = await http.get(currBaseUrl + '/logout')
    // 开发环境下输出响应信息
    if (import.meta.env.DEV) {
      console.log('[退出登录接口] 响应数据:', res)
    }
  } catch (err) {
    // 开发环境下输出错误信息
    if (import.meta.env.DEV) {
      console.error('[退出登录接口] 请求失败:', err)
    }
    // 退出登录失败不影响前端清理，继续执行
  }
}
