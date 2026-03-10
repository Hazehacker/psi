/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-23 22:55:27
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-08 23:41:51
 * @FilePath: \psi-frontend\src\apis\sysmanage\menu\type.ts
 * @Description: 系统配置-菜单管理相关接口类型定义
 */

/**
 * 菜单列表
 */
export interface MenuItem {
  /**
   * 子菜单列表
   */
  children?: MenuItem[]
  /**
   * 备注信息
   */
  data?: string
  /**
   * 菜单ID
   */
  id?: string
  /**
   * 菜单标识
   */
  key?: string
  /**
   * 菜单名称
   */
  name?: string
  /**
   * 父级菜单ID
   */
  pid?: string
  /**
   * 菜单地址
   */
  resource?: string
  /**
   * 菜单排序
   */
  sort?: number
  /**
   * 菜单类型
   */
  type?: number
  [property: string]: any
}

/**
 * MenuNameTreeVO
 */
export interface MenuNameTreeVO {
  /**
   * 节点包含的子节点
   */
  children?: MenuNameTreeVO[]
  /**
   * 菜单id
   */
  id?: string
  /**
   * 菜单名称
   */
  name?: string
  /**
   * 父级菜单id
   */
  pid?: string
  [property: string]: any
}
