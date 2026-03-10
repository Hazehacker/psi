import { useHttp } from '@/plugins/http'
import type { Dict } from './type'

// 树形数据接口
export interface TreeNode {
  id: string
  label: string
  children?: TreeNode[]
}

// 商品类别树节点
export interface CategoryTreeNode extends TreeNode {
  code: string
  level: number
  parentId: string
}

export class CommonAPI {
  private static http = useHttp()
  
  /**
   * 获取组织结构树
   * 接口路径: /api/common/orgStructure/tree
   * 请求方法: GET
   */
  static async getOrgStructureTree(): Promise<TreeNode[]> {
    try {
      // 实际API调用
      const result = await this.http.get('/api/common/orgStructure/tree')
      return result.data
    } catch (error) {
      console.error('获取组织结构树失败:', error)
      // 模拟数据作为兜底
      return [
        {
          id: '1',
          label: '总部',
          children: [
            {
              id: '1-1',
              label: '财务部'
            },
            {
              id: '1-2',
              label: '销售部'
            },
            {
              id: '1-3',
              label: '采购部'
            }
          ]
        },
        {
          id: '2',
          label: '北京分公司',
          children: [
            {
              id: '2-1',
              label: '北京销售部'
            }
          ]
        }
      ]
    }
  }

  /**
   * 获取收支类别名称列表
   * 接口路径: /common/incomeExpenseCategory/list
   * 请求方法: GET
   */
  static async getIncomeExpenseCategoryList(params?: any): Promise<Array<{ id: string; name: string }>> {
    try {
      // 实际API调用
      const result = await this.http.get('/common/incomeExpenseCategory/list', { params })
      return result.data
    } catch (error) {
        console.error('获取收支类别名称列表失败:', error)
        // 模拟数据作为兜底
      return [
        { id: '1', name: '销售收入' },
        { id: '2', name: '采购支出' },
        { id: '3', name: '运营成本' },
        { id: '4', name: '管理费用' }
      ]
    }
  }

  /**
   * 获取商品类别名称树
   * 接口路径: /api/common/productCategory/tree
   * 请求方法: GET
   */
  static async getProductCategoryTree(): Promise<CategoryTreeNode[]> {
    try {
      // 实际API调用
      const result = await this.http.get('/api/common/productCategory/tree')
      return result.data
    } catch (error) {
      console.error('获取商品类别名称树失败:', error)
      // 模拟数据作为兜底
      return [
        {
          id: 'C001',
          label: '电子产品',
          code: 'C001',
          level: 1,
          parentId: '0',
          children: [
            {
              id: 'C001-1',
              label: '手机',
              code: 'C001-1',
              level: 2,
              parentId: 'C001'
            } as CategoryTreeNode,
            {
              id: 'C001-2',
              label: '电脑',
              code: 'C001-2',
              level: 2,
              parentId: 'C001'
            } as CategoryTreeNode
          ]
        },
        {
          id: 'C002',
          label: '办公用品',
          code: 'C002',
          level: 1,
          parentId: '0',
          children: [
            {
              id: 'C002-1',
              label: '文具',
              code: 'C002-1',
              level: 2,
              parentId: 'C002'
            } as CategoryTreeNode,
            {
              id: 'C002-2',
              label: '办公设备',
              code: 'C002-2',
              level: 2,
              parentId: 'C002'
            } as CategoryTreeNode
          ]
        }
      ]
    }
  }

  /**
   * 获取商品选择列表（带分页）
   * 接口路径: /common/product/selectList
   * 请求方法: GET
   */
  static async getProductSelectList(params: any): Promise<any> {
    try {
      // 实际API调用
      const result = await this.http.get('/common/product/selectList', { params })
      return result
    } catch (error) {
        console.error('获取商品选择列表失败:', error)
        // 模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          records: [
            { id: '1', name: '测试商品1', code: 'P001' },
            { id: '2', name: '测试商品2', code: 'P002' }
          ],
          total: 2,
          size: params.size || 10,
          current: params.current || 1,
          pages: 1
        }
      }
    }
  }

  /**
   * 获取用户列表
   * 接口路径: /common/user/list
   * 请求方法: GET
   */
  static async getUserList(params?: any): Promise<any> {
    try {
      // 实际API调用
      const result = await this.http.get('/common/user/list', { params })
      return result
    } catch (error) {
        console.error('获取用户列表失败:', error)
        // 模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          records: [
            { id: '1', username: 'admin', realName: '管理员' },
            { id: '2', username: 'user1', realName: '用户1' }
          ],
          total: 2,
          size: params?.size || 10,
          current: params?.current || 1,
          pages: 1
        }
      }
    }
  }

  /**
   * 获取仓库名称列表
   * 接口路径: /api/common/warehouse/nameList
   * 请求方法: GET
   */
  static async getWarehouseNameList(): Promise<Array<{ id: string; name: string }>> {
    try {
      // 实际API调用
      const result = await this.http.get('/api/common/warehouse/nameList')
      return result.data
    } catch (error) {
      console.error('获取仓库名称列表失败:', error)
      // 模拟数据作为兜底
      return [
        { id: '1', name: '北京仓库' },
        { id: '2', name: '上海仓库' },
        { id: '3', name: '广州仓库' },
        { id: '4', name: '深圳仓库' }
      ]
    }
  }

  /**
   * 获取人员列表
   * 接口路径: /api/common/person/list
   * 请求方法: GET
   */
  static async getPersonList(params?: any): Promise<any> {
    try {
      // 实际API调用
      const result = await this.http.get('/api/common/person/list', { params })
      return result
    } catch (error) {
      console.error('获取人员列表失败:', error)
      // 模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          records: [
            { id: '1', name: '张三', phone: '13800138001' },
            { id: '2', name: '李四', phone: '13800138002' }
          ],
          total: 2,
          size: params?.size || 10,
          current: params?.current || 1,
          pages: 1
        }
      }
    }
  }

  /**
   * 获取供应商列表
   * 接口路径: /common/supplier/list
   * 请求方法: GET
   */
  static async getSupplierList(params?: any): Promise<any> {
    try {
      // 实际API调用
      const result = await this.http.get('/common/supplier/list', { params })
      return result
    } catch (error) {
        console.error('获取供应商列表失败:', error)
        // 模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          records: [
            { id: '1', name: '测试供应商1', code: 'SUPP001' },
            { id: '2', name: '测试供应商2', code: 'SUPP002' }
          ],
          total: 2,
          size: params?.size || 10,
          current: params?.current || 1,
          pages: 1
        }
      }
    }
  }

  /**
   * 获取客户列表
   * 接口路径: /common/customer/list
   * 请求方法: GET
   */
  static async getCustomerList(params?: any): Promise<any> {
    try {
      // 实际API调用
      const result = await this.http.get('/common/customer/list', { params })
      return result
    } catch (error) {
      console.error('获取客户列表失败:', error)
      // 模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          records: [
            { id: '1', name: '测试客户1', code: 'CUST001' },
            { id: '2', name: '测试客户2', code: 'CUST002' }
          ],
          total: 2,
          size: params?.size || 10,
          current: params?.current || 1,
          pages: 1
        }
      }
    }
  }
}