/**
 * 字典
 */

/**
 * 字典分页请求参数
 */
export interface DictPageRequest {
  /**
   * 字典名称
   */
  name?: string
  /**
   * 查询页码
   */
  pageIndex?: number
  /**
   * 查询条数
   */
  pageSize?: number
  /**
   * 字典类型唯一标识
   */
  tid?: string
  /**
   * 字典值
   */
  value?: string
  [property: string]: any
}

/**
 * 字典传输数据对象
 */
export interface DictDTO {
  /**
   * 唯一标识
   */
  id: string
  /**
   * 字典名称
   */
  name: string
  /**
   * 描述
   */
  remark?: string
  /**
   * 字典类型名称
   */
  type_name: string
  /**
   * 字典值
   */
  value: string
  [property: string]: any
}

/**
 * 字典名称数据对象
 */
export interface DictNameItem {
  /**
   * 唯一标识
   */
  id?: string
  /**
   * 字典名称
   */
  name?: string
  /**
   * 字典值
   */
  value?: string
  [property: string]: any
}

/**
 * 字典详情
 */
export interface DictDetailDTO {
  /**
   * 唯一标识
   */
  id?: string
  /**
   * 字典名称
   */
  name?: string
  /**
   * 描述
   */
  remark?: string
  /**
   * 所属字典类型
   */
  tid?: string
  /**
   * 字典值
   */
  value?: string
  [property: string]: any
}

/**
 * 字典类型
 */

/**
 * 字典类型分页请求参数
 */
export interface TypePageRequest {
  /**
   * 字典类型编码
   */
  code?: string
  /**
   * 字典类型id
   */
  id?: string
  /**
   * 字典类型名称
   */
  name?: string
  /**
   * 查询页码
   */
  pageIndex?: number
  /**
   * 查询条数
   */
  pageSize?: number
  /**
   * 字典类型备注
   */
  remark?: string
  [property: string]: any
}

/**
 * 字典类型DTO
 */
export interface DictionTypeDTO {
  /**
   * 字典类型编码
   */
  code: string
  /**
   * 主键ID
   */
  id?: string
  /**
   * 字典类型名称
   */
  name: string
  /**
   * 备注信息
   */
  remark?: string
  [property: string]: any
}

/**
 * 字典类型名称
 */
export interface DictionaryTypeItem {
  /**
   * 字典类型id
   */
  id?: string
  /**
   * 字典类型名称
   */
  name?: string
  [property: string]: any
}

/**
 * 字典项数据模型
 */
export interface DictionaryItem {
    id: number;
    name: string;
    type_name: string;
    value: string;
    remark?: string;
    tid?: number;
    createTime?: string;
    [property: string]: any;
}

/**
 * 字典类型数据模型
 */
export interface DictionaryType {
    id: number;
    code: string;
    name: string;
    remark?: string;
    createTime?: string;
    [property: string]: any;
}

/**
 * 字典类型查询数据对象（别名）
 */
export type 字典类型查询数据对象 = TypePageRequest
