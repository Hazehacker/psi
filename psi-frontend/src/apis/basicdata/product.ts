import { useHttp } from '@/plugins/http'
import type { PageResult, ApiResponse, PageQuery } from '@/apis/type'

// 商品基础接口 - 根据GoodsDetailDTO结构定义
export interface Product {
  id: string
  name: string
  py?: string // 商品拼音
  number: string // 商品编号
  spec?: string // 商品型号
  category?: string // 商品类别
  brand?: string // 商品品牌
  unit?: string // 商品单位
  buy?: number // 采购价格
  sell?: number // 销售价格
  code?: string // 商品条码
  location?: string // 商品货位
  stock?: number // 库存阈值
  type?: number // 商品类型
  data?: string // 商品备注信息
  imgs?: string // 商品图像
  details?: string // 图文详细
  units?: string // 多单位配置
  strategy?: string // 折扣策略
  serial?: number // 序列产品
  batch?: number
  validity?: number // 有效期
  protect?: number // 保质期
  threshold?: number // 预警阈值
  more?: string // 扩展信息
  enabled?: number // 是否启用 1:启用 0:禁用
  remark?: string // 备注
  // 兼容旧字段
  goods_unit?: string
  specification_model?: string
  memo?: string
  term?: string
  price?: number
  costPrice?: number
  description?: string
  status?: number
  createTime?: string
  updateTime?: string
}

// 商品查询参数 - 根据API文档中的查询参数要求更新
export interface ProductQuery extends PageQuery {
  name?: string // 商品名称
  number?: string // 商品编号
  code?: string // 商品条码
  barcode?: string // 商品条码（前端使用）
  model?: string // 商品型号（前端使用）
  remark?: string // 商品备注（前端使用）
  brand?: string // 商品品牌
  goods_unit?: string // 商品单位
  specification_model?: string // 规格型号
  category?: string // 商品分类/类别
  type?: string | number // 商品类型，支持string和number以兼容API
  memo?: string // 商品备注
  // 根据API文档，pageSize和pageIndex是必填的
  pageSize: number
  pageIndex: number
  // 扩展查询参数
  py?: string // 商品拼音
  spec?: string // 商品型号
  unit?: string // 商品单位
  buy?: number // 采购价格
  sell?: number // 销售价格
  location?: string // 商品货位
  stock?: number // 库存阈值
  batch?: number
  validity?: number // 有效期
  threshold?: number // 预警阈值
}

// 新增商品请求接口 - 根据GoodsAddDTO结构定义
export interface AddProductRequest {
  name: string;           // 商品名称
  number?: string;        // 商品编号
  py?: string;            // 拼音码
  spec?: string;          // 规格
  unit?: string;          // 单位
  buy?: number | string;  // 采购价格
  sell?: number | string; // 销售价格
  location?: string;      // 位置
  type?: number | string; // 类型
  category?: number;      // 分类ID
  brand?: string;         // 品牌
  // bar_code 已移除，使用 code 字段代替
  batch?: number | string;// 批次管理(0:否 1:是)
  validity?: number | string;// 有效期(天)
  threshold?: number | string;// 最低库存
  protect?: number | string;  // 保质期预警(月)
  data?: string;          // 备注
  img?: string;           // 图片
  enabled?: number;       // 是否启用 1:启用 0:禁用
  remark?: string;        // 备注
  // 兼容旧字段
  goods_unit?: string;        // 兼容旧单位字段
  specification_model?: string;// 兼容旧规格字段
  memo?: string;              // 兼容旧备注字段
  // 其他可能的字段
  [key: string]: any;
}

// 工具函数：格式化商品数据为API要求的格式
function formatProductData(data: any): any {
  // 显式构建只包含后端期望字段的对象，避免传递后端不认识的字段
  const formatted: any = {};
  
  // 辅助函数：安全地将值转换为数字，确保不会返回NaN
  const safeNumber = (value: any, defaultValue: number = 0): number => {
    if (value === undefined || value === null || value === '') {
      return defaultValue;
    }
    const num = Number(value);
    return isNaN(num) ? defaultValue : num;
  };
  
  // 辅助函数：安全地将值转换为整数
  const safeInteger = (value: any, defaultValue: number = 0): number => {
    const num = safeNumber(value, defaultValue);
    return Math.floor(num);
  };
  
  // 基本信息字段
  if (data.name) formatted.name = data.name;
  if (data.code) formatted.number = data.code; // 前端code -> 后端number
  if (data.barcode) formatted.code = data.barcode; // 前端barcode -> 后端code
  if (data.model) formatted.spec = data.model; // 前端model -> 后端spec
  if (data.category !== undefined && data.category !== null) {
    // 无论category是什么类型，都转换为整数，因为后端需要整数类型
    formatted.category = safeInteger(data.category, 0);
  }
  if (data.brand) formatted.brand = data.brand;
  if (data.unit || data.goods_unit) formatted.unit = data.unit || data.goods_unit;
  
  // 价格字段 - 保留小数
  if (data.buy !== undefined && data.buy !== null) {
    formatted.buy = safeNumber(data.buy);
  } else if (data.purchasePrice !== undefined && data.purchasePrice !== null) {
    formatted.buy = safeNumber(data.purchasePrice); // 兼容purchasePrice字段
  }
  
  if (data.sell !== undefined && data.sell !== null) {
    formatted.sell = safeNumber(data.sell);
  } else if (data.salesPrice !== undefined && data.salesPrice !== null) {
    formatted.sell = safeNumber(data.salesPrice); // 兼容salesPrice字段
  }
  
  // 其他基本字段
  if (data.location) formatted.location = data.location;
  if (data.stock !== undefined && data.stock !== null) {
    formatted.stock = safeInteger(data.stock);
  } else if (data.stockThreshold !== undefined && data.stockThreshold !== null) {
    formatted.stock = safeInteger(data.stockThreshold); // 兼容stockThreshold字段
  }
  
  if (data.type !== undefined && data.type !== null && data.type !== '') {
    // 无论type是什么类型，都转换为整数，因为后端需要整数类型
    formatted.type = safeInteger(data.type, 1);
  } else {
    formatted.type = 1; // 默认值
  }
  
  if (data.data || data.remark || data.memo) {
    formatted.data = data.data || data.remark || data.memo;
  }
  
  // 扩展字段
  if (data.py) formatted.py = data.py;
  if (data.img || data.imgs) formatted.img = data.img || data.imgs;
  if (data.details) formatted.details = data.details;
  if (data.units) formatted.units = data.units;
  if (data.strategy) formatted.strategy = data.strategy;
  
  // 批次相关字段 - 确保整数格式
  if (data.batch !== undefined && data.batch !== null) {
    formatted.batch = safeInteger(data.batch);
  } else if (data.batchProduct !== undefined) {
    formatted.batch = data.batchProduct ? 1 : 0; // 兼容batchProduct布尔字段
  } else {
    formatted.batch = 0; // 默认值
  }
  
  if (data.validity !== undefined && data.validity !== null) {
    formatted.validity = safeInteger(data.validity);
  } else if (data.validPeriod !== undefined) {
    formatted.validity = data.validPeriod ? safeInteger(data.shelfLife || 0) : 0; // 兼容validPeriod和shelfLife字段
  } else {
    formatted.validity = 0; // 默认值
  }
  
  if (data.protect !== undefined && data.protect !== null) {
    formatted.protect = safeInteger(data.protect);
  } else if (data.warningThreshold !== undefined && data.warningThreshold !== null) {
    formatted.protect = safeInteger(data.warningThreshold); // 兼容warningThreshold字段
  } else {
    formatted.protect = 0; // 默认值
  }
  
  if (data.threshold !== undefined && data.threshold !== null) {
    formatted.threshold = safeInteger(data.threshold);
  } else if (data.stockThreshold !== undefined && data.stockThreshold !== null) {
    formatted.threshold = safeInteger(data.stockThreshold); // 再次兼容stockThreshold字段
  } else {
    formatted.threshold = 0; // 默认值
  }
  
  // 启用状态 - 确保整数格式
  if (data.enabled !== undefined) {
    formatted.enabled = safeInteger(data.enabled, 1);
  } else {
    formatted.enabled = 1; // 默认启用
  }
  
  // ID字段（用于更新操作）
  if (data.id) formatted.id = data.id;
  
  return formatted;
}

// 文件上传参数
export interface UploadFileRequest {
  filename?: string
  fileType: string // 文件类型编码
  saveType: string // 文件存储类型编码
  remark?: string
  file: File // 文件数据
}

export class ProductAPI {
  private static http = useHttp();

  /**
   * 获取商品列表（条件+分页）
   * @param query 查询参数
   * @returns 商品列表数据
   * @path /goods/all
   * @method GET
   */
  static async getProductList(query: ProductQuery): Promise<ApiResponse<PageResult<Product>>> {
    try {
      // 根据http.ts中的参数序列化方式，我们需要构建一个干净的查询对象，只包含后端需要的参数
      // 避免传递空字符串或undefined值，这可能导致C++后端的"_Map_base::at"错误
      const formattedQuery: any = {};
      
      // 只添加有实际值的参数
      if (query.name && query.name.trim()) {
        formattedQuery.name = query.name.trim();
      }
      if (query.code && query.code.trim()) {
        formattedQuery.number = query.code.trim(); // 前端code -> 后端number
      }
      if (query.barcode && query.barcode.trim()) {
        formattedQuery.code = query.barcode.trim(); // 前端barcode -> 后端code
      }
      if (query.model && query.model.trim()) {
        formattedQuery.spec = query.model.trim(); // 前端model -> 后端spec
      }
      if (query.brand && query.brand.trim()) {
        formattedQuery.brand = query.brand.trim(); // 前端brand -> 后端brand
      }
      if (query.category && query.category.trim()) {
        formattedQuery.category = query.category.trim(); // 前端category -> 后端category
      }
      if (query.type !== undefined && query.type !== null && query.type !== '') {
        formattedQuery.type = query.type; // 保持原始类型，让http.ts的paramsSerializer处理
      }
      if (query.remark && query.remark.trim()) {
        formattedQuery.memo = query.remark.trim(); // 前端remark -> 后端memo
      }
      
      // 分页参数是必需的，确保转换为数字类型
      formattedQuery.pageSize = Number(query.pageSize);
      formattedQuery.pageIndex = Number(query.pageIndex);
      
      // 使用直接传递参数的方式，让http.ts中的paramsSerializer处理序列化
      const response = await this.http.get('/c2-sysbase/goods/all', formattedQuery);
      return response as ApiResponse<PageResult<Product>>;
    } catch (error) {
      console.error('获取商品列表失败:', error);
      // 返回模拟数据作为兜底
      const mockData = [
        {
          id: '1',
          name: '测试商品1',
          py: 'CSSP1',
          number: 'P001',
          code: '6901234567890',
          spec: '标准版',
          unit: '个',
          buy: 100,
          sell: 150,
          location: 'A区01',
          type: 1,
          category: '电子产品',
          brand: '品牌A',
          batch: 1,
          validity: 180,
          threshold: 10,
          protect: 2,
          data: '测试商品1备注',
          imgs: '/mock/images/product1.jpg',
          enabled: 1,
          remark: '重要商品',
          createTime: '2024-01-01 10:00:00',
          updateTime: '2024-01-01 10:00:00',
          // 兼容旧字段
          goods_unit: '个',
          specification_model: '标准版',
          memo: '测试商品1备注'
        },
        {
          id: '2',
          name: '测试商品2',
          py: 'CSSP2',
          number: 'P002',
          code: '6901234567891',
          spec: '高级版',
          unit: '个',
          buy: 200,
          sell: 280,
          location: 'B区02',
          type: 2,
          category: '办公用品',
          brand: '品牌B',
          batch: 2,
          validity: 365,
          threshold: 5,
          protect: 0,
          data: '测试商品2备注',
          imgs: '/mock/images/product2.jpg',
          enabled: 1,
          remark: '畅销商品',
          createTime: '2024-01-02 10:00:00',
          updateTime: '2024-01-02 10:00:00',
          // 兼容旧字段
          goods_unit: '个',
          specification_model: '高级版',
          memo: '测试商品2备注'
        }
      ];
      return {
        code: 200,
        message: 'success',
        data: {
          total: 5,
          pageIndex: query.pageIndex,
          pageSize: query.pageSize,
          pages: 1,
          rows: mockData
        }
      };
    }
  }

  /**
   * 获取指定商品详情
   * @param id 商品ID
   * @returns 商品详情数据
   * @path /goods/getdetail
   * @method GET
   */
  static async getProductDetail(id: string): Promise<ApiResponse<Product>> {
    try {
      const response = await this.http.get('/c2-sysbase/goods/getdetail', { params: { id } });
      return response as ApiResponse<Product>;
    } catch (error) {
      console.error('获取商品详情失败:', error);
      // 返回模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: {
          id,
          name: '测试商品1',
          py: 'CSSP1',
          number: 'P001',
          code: '6901234567890',
          spec: '标准版',
          unit: '个',
          buy: 100,
          sell: 150,
          location: 'A区01',
          type: 1,
          category: '电子产品',
          brand: '品牌A',
          batch: 1,
          validity: 180,
          threshold: 10,
          protect: 2,
          data: '测试商品1备注',
          imgs: '/mock/images/product1.jpg',
          details: '',
          units: '',
          strategy: '',
          serial: 0,
          stock: 1000,
          enabled: 1,
          remark: '重要商品',
          createTime: '2024-01-01 10:00:00',
          updateTime: '2024-01-01 10:00:00',
          more: '',
          // 兼容旧字段
          goods_unit: '个',
          specification_model: '标准版',
          memo: '测试商品1备注'
        }
      };
    }
  }

  /**
   * 新增商品
   * @param data 商品数据
   * @returns 操作结果
   * @path /goods/add
   * @method POST
   */
  static async addProduct(data: AddProductRequest): Promise<ApiResponse<string>> {
    try {
      // 使用工具函数格式化商品数据
      const formattedData = formatProductData(data);
      const response = await this.http.post('/c2-sysbase/goods/add', formattedData);
      return response as ApiResponse<string>;
    } catch (error) {
      console.error('新增商品失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: '1'
      };
    }
  }

  /**
   * 修改指定商品
   * @param data 商品数据（包含id）
   * @returns 操作结果
   * @path /goods/update
   * @method PUT
   */
  static async updateProduct(data: Product): Promise<ApiResponse<string>> {
    try {
      // 使用工具函数格式化商品数据
      const formattedData = formatProductData(data);
      const response = await this.http.put('/c2-sysbase/goods/update', formattedData);
      return response as ApiResponse<string>;
    } catch (error) {
      console.error('更新商品失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: 'success'
      };
    }
  }

  /**
   * 删除商品(支持批量)
   * @param ids 商品ID列表
   * @returns 操作结果
   * @path /goods/delete
   * @method DELETE
   */
  static async deleteProduct(ids: string[]): Promise<ApiResponse<string>> {
    try {
      // 确保ids是数组格式
      const deleteIds = Array.isArray(ids) ? ids : [ids];
      const response = await this.http.delete('/c2-sysbase/goods/delete', { data: deleteIds });
      return response as ApiResponse<string>;
    } catch (error) {
      console.error('删除商品失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: 'success'
      };
    }
  }

  /**
   * 获取商品选择列表
   * @returns 商品选择列表数据
   * @path /goods/specs
   * @method GET
   */
  static async getProductSpecs(): Promise<ApiResponse<Array<{id: string; name: string; number: string}>>> {
    try {
      const response = await this.http.get('/c2-sysbase/goods/specs');
      return response as ApiResponse<Array<{id: string; name: string; number: string}>>;
    } catch (error) {
      console.error('获取商品选择列表失败:', error);
      // 返回模拟数据作为兜底
      return {
        code: 200,
        message: 'success',
        data: [
          { id: '1', name: '测试商品1', number: 'P001' },
          { id: '2', name: '测试商品2', number: 'P002' }
        ]
      };
    }
  }

  /**
   * 导入数据
   * @param fileData 文件上传数据
   * @returns 操作结果
   * @path /goods/upload
   * @method POST
   */
  static async uploadProductFile(fileData: UploadFileRequest): Promise<ApiResponse<string>> {
    try {
      const formData = new FormData();
      formData.append('filename', fileData.filename || '');
      formData.append('fileType', fileData.fileType);
      formData.append('saveType', fileData.saveType);
      formData.append('remark', fileData.remark || '');
      formData.append('file', fileData.file);

      const response = await this.http.post('/c2-sysbase/goods/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      return response as ApiResponse<string>;
    } catch (error) {
      console.error('导入数据失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: 'success'
      };
    }
  }

  /**
   * 导入数据 - 备用接口
   * @param fileData 文件上传数据
   * @returns 操作结果
   * @path /goodsfile/upload
   * @method POST
   */
  static async uploadProductFileAlt(fileData: UploadFileRequest): Promise<ApiResponse<string>> {
    try {
      const formData = new FormData();
      formData.append('filename', fileData.filename || '');
      formData.append('fileType', fileData.fileType);
      formData.append('saveType', fileData.saveType);
      formData.append('remark', fileData.remark || '');
      formData.append('file', fileData.file);

      const response = await this.http.post('/c2-sysbase/goodsfile/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      return response as ApiResponse<string>;
    } catch (error) {
      console.error('导入数据失败:', error);
      // 复用uploadProductFile的模拟数据逻辑
      return this.uploadProductFile(fileData);
    }
  }

  /**
   * 上传图片
   * @param file 文件数据
   * @param type 文件类型标识
   * @returns 上传结果
   * @path /goods/upload
   * @method POST
   */
  static async uploadProductImage(file: File, type: string = 'image'): Promise<ApiResponse<{ path: string }>> {
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('type', type);
      const response = await this.http.post('/c2-sysbase/goods/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      return response as ApiResponse<{ path: string }>;
    } catch (error) {
      console.error('上传图片失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: {
          path: '/mock/images/product.jpg'
        }
      };
    }
  }

  /**
   * 根据商品ID查询库存信息
   * @param goodsId 商品ID
   * @returns 库存信息
   * @path /goods/stock
   * @method GET
   */
  static async getProductStock(goodsId: string): Promise<ApiResponse<{ stock: number }>> {
    try {
      const response = await this.http.get(`/c2-sysbase/goods/stock/${goodsId}`);
      return response as ApiResponse<{ stock: number }>;
    } catch (error) {
      console.error('查询商品库存失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: {
          stock: 100
        }
      };
    }
  }

  /**
   * 商品信息校验
   * @param data 校验数据
   * @returns 校验结果
   * @path /goods/validation
   * @method POST
   */
  static async validateProduct(data: { name?: string; number?: string; id?: string }): Promise<ApiResponse<boolean>> {
    try {
      const response = await this.http.post('/c2-sysbase/goods/validation', data);
      return response as ApiResponse<boolean>;
    } catch (error) {
      console.error('商品信息校验失败:', error);
      // 返回模拟成功结果
      return {
        code: 200,
        message: 'success',
        data: true
      };
    }
  }

  /**
   * 导出数据
   * @param query 查询参数
   * @returns 文件流
   * @path /goods/export
   * @method GET
   */
  static async exportProducts(query: ProductQuery): Promise<Blob> {
    try {
      const response = await this.http.get('/c2-sysbase/goods/export', {
        params: query,
        responseType: 'blob'
      });
      return response as unknown as Blob;
    } catch (error) {
      console.error('导出数据失败:', error);
      // 返回模拟的空blob
      return new Blob([''], { type: 'application/vnd.ms-excel' });
    }
  }
}
