/**
 * 导入模板分页请求参数
 */
export interface PageRequest {
  /**
   * 查询页码
   */
  pageIndex?: number
  /**
   * 查询条数
   */
  pageSize?: number
  /**
   * 文件存储方式（字典值）
   */
  saveType?: string
  /**
   * 状态：0=未使用，1=使用中
   */
  status?: number
  /**
   * 模板编码
   */
  templateCode?: string
  /**
   * 模板名称关键词，模糊匹配
   */
  templateName?: string
  [property: string]: any
}

/**
 * 导入模板返回参数
 */
export interface ImportTemplateList {
  /**
   * 模板ID
   */
  id?: string
  /**
   * 备注
   */
  remark?: string
  /**
   * 文件存储方式
   */
  saveType?: string
  /**
   * 状态：0=未使用，1=使用中
   */
  status?: number
  /**
   * 模板编码
   */
  templateCode?: string
  /**
   * 模板名称
   */
  templateName?: string
  [property: string]: any
}

/**
 * 导入模板表单数据
 */
export interface ImportTemplateForm {
  /**
   * 数据状态
   */
  status?: number
  /**
   * 模板名称
   */
  templateName?: string
  /**
   * 模板编码
   */
  templateCode?: string
  /**
   * 文件存储方式
   */
  saveType?: string
  /**
   * 备注
   */
  remark?: string
  /**
   * 文件
   */
  file?: File
  [property: string]: any
}

/**
 * 导入模板下载响应数据
 */
export interface ImportTemplateDetail {
  /**
   * 下载URL
   */
  downloadUrl?: string
  /**
   * 模板ID
   */
  id?: string
  /**
   * 文件存储方式
   */
  saveType?: string
  /**
   * 模板编码
   */
  templateCode?: string
  /**
   * 模板名称
   */
  templateName?: string
  /**
   * 存储路径或文件标识（后端用于区分已上传文件）
   */
  savePath?: string
  /**
   * 文件原始名称
   */
  fileName?: string
  /**
   * 文件大小（字节）
   */
  fileSize?: number
  /**
   * 文件 MIME 类型
   */
  fileType?: string
  /**
   * 备注
   */
  remark?: string
  /**
   * 数据状态：0=未使用，1=使用中
   */
  status?: number
  /**
   * 兼容前端上传控件的文件信息（可选）
   */
  file?: {
    uid?: string | number
    name?: string
    size?: number
    type?: string
    url?: string
    lastModified?: number
  }
  [property: string]: any
}
