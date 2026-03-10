import type { UploadProps, UploadFile, UploadUserFile } from 'element-plus'

/**
 * 自定义上传组件属性模型
 * 基于 Element Plus Upload 组件属性扩展
 */
export interface MyFormUploadProps extends Omit<Partial<UploadProps>, 'onExceed'> {
  /** 上传的地址 */
  action?: string
  /** 设置上传的请求头部 */
  headers?: Record<string, any>
  /** 上传的文件字段名 */
  name?: string
  /** 支持发送 cookie 凭证信息 */
  withCredentials?: boolean
  /** 支持多选文件 */
  multiple?: boolean
  /** 接受上传的文件类型 */
  accept?: string
  /** 是否在选取文件后立即进行上传 */
  autoUpload?: boolean
  /** 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传 */
  beforeUpload?: (rawFile: File) => boolean | Promise<File>
  /** 文件上传时的钩子 */
  onProgress?: (event: ProgressEvent, file: UploadFile, fileList: UploadFile[]) => void
  /** 文件上传成功时的钩子 */
  onSuccess?: (response: any, file: UploadFile, fileList: UploadFile[]) => void
  /** 文件上传失败时的钩子 */
  onError?: (error: Error, file: UploadFile, fileList: UploadFile[]) => void
  /** 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用 */
  onChange?: (file: UploadFile, fileList: UploadFile[]) => void
  /** 文件列表移除文件时的钩子 */
  onRemove?: (file: UploadFile, fileList: UploadFile[]) => void
  /** 点击文件列表中已上传的文件时的钩子 */
  onPreview?: (file: UploadFile) => void
  /** 文件超出个数限制时的钩子 */
  onExceed?: (files: File[], uploadFiles: UploadUserFile[]) => void
  /** 是否显示已上传文件列表 */
  'show-file-list'?: boolean
  /** 是否禁用 */
  disabled?: boolean
  /** 是否显示拖拽区域 */
  drag?: boolean
  /** 上传文件列表 */
  fileList?: UploadFile[]
  /** 最大允许上传个数 */
  limit?: number
  /** 是否只读 */
  readonly?: boolean
}
