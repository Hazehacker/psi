import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import { ref } from 'vue'

/**
 * 路由图标数据集 Store
 * 用于存储和获取路由路径与图标的映射关系
 */
export const useRouterIcons = defineStore('router-icons', () => {
  /** 路由路径与图标的映射对象 */
  const pathIconMap = ref<Record<string, string>>({})

  /** 初始化状态标志 */
  const isInitialized = ref(false)

  /** 默认图标 */
  const DEFAULT_ICON_VALUE = 'mdi:menu' as const

  /**
   * 从路由配置中提取 path-icon 映射关系
   * 递归遍历所有路由（包括嵌套路由）
   *
   * @param routes - 路由配置数组
   * @returns 是否成功执行初始化
   */
  function extractIconsFromRoutes(routes: RouteRecordRaw[]): boolean {
    // 如果已经初始化，不再重复执行
    if (isInitialized.value) {
      if (import.meta.env.DEV) {
        console.warn('[RouterIcons] 路由图标映射已经初始化，跳过重复初始化')
      }
      return false
    }

    // 清空现有映射
    pathIconMap.value = {}

    // 递归遍历路由
    function traverseRoutes(routes: RouteRecordRaw[]) {
      routes.forEach((route) => {
        // 如果路由有 path，提取图标
        if (route.path) {
          const icon = route.meta?.icon as string | undefined
          // 即使 icon 为空字符串或 undefined，也存储（后续会处理为默认值）
          pathIconMap.value[route.path] = icon || ''
        }

        // 递归处理子路由
        if (route.children && route.children.length > 0) {
          traverseRoutes(route.children)
        }
      })
    }

    traverseRoutes(routes)

    // 标记为已初始化
    isInitialized.value = true

    if (import.meta.env.DEV) {
      console.group('🔍 [RouterIcons] 路由图标映射初始化完成')
      console.log('映射数量:', Object.keys(pathIconMap.value).length)
      console.log('完整映射:', pathIconMap.value)
      console.groupEnd()
    }

    return true
  }

  /**
   * 根据路由路径获取对应的图标
   *
   * @param path - 路由路径
   * @returns 图标名称，如果未找到则返回空字符串
   */
  function getIconByPath(path: string): string {
    return pathIconMap.value[path] || ''
  }

  return {
    /** 默认图标 */
    DEFAULT_ICON_VALUE,
    /** 路由路径与图标的映射对象（只读） */
    pathIconMap,
    /** 初始化状态标志 */
    isInitialized,
    /** 从路由配置中提取图标映射 */
    extractIconsFromRoutes,
    /** 根据路径获取图标 */
    getIconByPath
  }
})
