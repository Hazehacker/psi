import { useHttp } from '@/plugins/http'
import type { PageDTO, PageQuery } from '../type'

/**
 * 附件信息数据类型
 */
export interface AttachmentInfo {
  /** 文件唯一标识 */
  id?: string
  /** 文件名称（包含扩展名） */
  name: string
  /** 文件类型（对应字典表value，如doc、pdf、jpg等） */
  fileType: string
  /** 文件存储路径（不含服务器域名和端口） */
  savePath: string
  /** 文件存储方式（对应字典表value，如local-本地存储、oss-对象存储等） */
  saveType: string
  /** 文件备注信息 */
  remark?: string
  /** 数据状态（0-未使用，1-使用中） */
  status: number
}

/**
 * 附件查询参数
 */
export interface AttachmentQuery extends PageQuery {
  /** 文件名 */
  name?: string
  /** 文件类型 */
  fileType?: string
  /** 文件存储路径 */
  savePath?: string
  /** 文件存储方式 */
  saveType?: string
  /** 文件状态 */
  status?: number
}

// Java组接口基础路径 - 根据服务器资源文档修正
const JAVA_BASE_URL = '/j7-sysargs/attachment'

/**
 * 获取附件列表（分页+条件）
 */
export const getAttachmentList = async (params: AttachmentQuery) => {
  const http = useHttp()
  return http.get<PageDTO<AttachmentInfo>>(`${JAVA_BASE_URL}/list`, params, {
    showLoading: true
  })
}

/**
 * 上传附件
 */
export const uploadAttachment = async (file: File) => {
  const http = useHttp()
  return http.postWithFile<string>(
    `${JAVA_BASE_URL}/add`,
    { file },
    {
      timeout: 60000, // 增加超时时间到60秒
      showLoading: true
    }
  )
}

/**
 * 修改附件
 */
export const updateAttachment = async (data: AttachmentInfo) => {
  const http = useHttp()
  return http.put<string>(`${JAVA_BASE_URL}/update`, data, {
    showLoading: true
  })
}

/**
 * 删除附件
 */
export const deleteAttachment = async (id: string) => {
  const http = useHttp()
  return http.delete<string>(`${JAVA_BASE_URL}/${id}`, null, {
    showLoading: true
  })
}
