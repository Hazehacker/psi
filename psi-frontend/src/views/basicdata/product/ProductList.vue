<template>
  <div class="sys-area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 三点下拉菜单 -->
        <div class="dropdown-menu">
          <button class="dropdown-button" @click="dropdownVisible = !dropdownVisible">
            <span class="dots-menu">•••</span>
          </button>
          <div v-if="dropdownVisible" class="dropdown-content" @click.stop>
            <!-- 搜索表单 -->
            <div class="search-form">
              <div class="form-row">
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入商品名称"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.code"
                  placeholder="请输入商品编号"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-input
                  v-model="searchForm.model"
                  placeholder="请输入商品型号"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-cascader
                  v-model="searchForm.category"
                  :options="categoryOptions"
                  placeholder="请选择商品类别"
                  clearable
                  size="large"
                  class="form-select"
                  :props="{ expandTrigger: 'hover', multiple: false }"
                />
              </div>
              <div class="form-row">
                <el-select
                  v-model="searchForm.brand"
                  placeholder="请选择商品品牌"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="品牌1" value="品牌1" />
                  <el-option label="品牌2" value="品牌2" />
                </el-select>
                <el-input
                  v-model="searchForm.barcode"
                  placeholder="请输入商品条码"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-select
                  v-model="searchForm.type"
                  placeholder="请选择商品类型"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="常规商品" value="常规商品" />
                  <el-option label="特殊商品" value="特殊商品" />
                </el-select>
                <el-input
                  v-model="searchForm.remark"
                  placeholder="请输入备注信息"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row search-button-row">
                <el-button circle @click="handleSearch">
                  <el-icon><Search /></el-icon>
                </el-button>
                <el-button @click="handleResetSearch">重置</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="operation-right">
        <el-button type="primary" @click="handleAdd" class="action-btn">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button @click="handleBatch" class="action-btn">
          <el-icon><Collection /></el-icon>
          批量
        </el-button>
        <el-button @click="handleRefresh" class="action-btn">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-divider class="custom-divider" />

    <!-- 主内容区域：表格和右侧操作栏 -->
    <div class="main-content">
      <!-- 数据表格 -->
      <div class="table-section">
        <!-- 表格容器 - 添加滚动条 -->
        <div class="table-container">
          <NormalTable
            :tabdata="tablePageData"
            :tabdatacolumns="tabdatacolumns"
            :taboperbtns="taboperbtns"
            :istabmultiple="true"
            @selection-change="handleSelectionChange"
            @taboper-click="handleTableOperClick"
            @page-change="handlePageChange"
            :tabattr="tableAttr"
          />
        </div>
      </div>
    </div>
  </div>

  <!-- 商品编辑对话框 -->
  <el-dialog
    v-model="dialogVisible"
    title="商品详情"
    width="800px"
    :before-close="handleDialogClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 基础信息标签页 -->
      <el-tab-pane label="基础信息" name="basic">
        <div class="form-content">
          <div class="form-grid">
            <div class="form-item">
              <label class="required">商品名称</label>
              <el-input v-model="productForm.name" placeholder="请输入商品名称" />
            </div>
            <div class="form-item">
              <label class="required">商品编号</label>
              <el-input v-model="productForm.code" placeholder="请输入商品编号" />
            </div>
            <div class="form-item">
              <label>商品型号</label>
              <el-input v-model="productForm.model" placeholder="请输入商品型号" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label class="required">商品类别</label>
              <el-select v-model="productForm.category" placeholder="请选择商品类别">
                <el-option label="类别1" value="类别1" />
                <el-option label="类别2" value="类别2" />
              </el-select>
            </div>
            <div class="form-item">
              <label>商品品牌</label>
              <el-select v-model="productForm.brand" placeholder="请选择商品品牌">
                <el-option label="品牌1" value="品牌1" />
                <el-option label="品牌2" value="品牌2" />
              </el-select>
            </div>
            <div class="form-item">
              <label class="required">商品单位</label>
              <el-select v-model="productForm.unit" placeholder="请选择商品单位">
                <el-option label="个" value="个" />
                <el-option label="件" value="件" />
                <el-option label="箱" value="箱" />
              </el-select>
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label class="required">采购价格</label>
              <el-input
                v-model.number="productForm.purchasePrice"
                placeholder="请输入采购价格"
                type="number"
              />
            </div>
            <div class="form-item">
              <label class="required">销售价格</label>
              <el-input
                v-model.number="productForm.salesPrice"
                placeholder="请输入销售价格"
                type="number"
              />
            </div>
            <div class="form-item">
              <label>商品条码</label>
              <el-input v-model="productForm.barcode" placeholder="请输入商品条码" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>商品货位</label>
              <el-input v-model="productForm.location" placeholder="请输入商品货位" />
            </div>
            <div class="form-item">
              <label class="required">库存阈值</label>
              <el-input
                v-model.number="productForm.stockThreshold"
                placeholder="请输入库存阈值"
                type="number"
              />
            </div>
            <div class="form-item">
              <label>商品类型</label>
              <el-select v-model="productForm.type" placeholder="请选择商品类型">
                <el-option label="常规商品" value="常规商品" />
                <el-option label="特殊商品" value="特殊商品" />
              </el-select>
            </div>
          </div>
          <div class="form-item full-width">
            <label>备注信息</label>
            <el-input v-model="productForm.remark" placeholder="请输入备注信息" type="textarea" />
          </div>
        </div>
      </el-tab-pane>

      <!-- 辅助属性标签页（补充所有勾选项） -->
      <el-tab-pane label="辅助属性" name="auxiliary">
        <div class="attributes-container">
          <div class="attribute-item">
            <span class="attribute-name">SN码</span>
            <el-checkbox-group v-model="productForm.attributes.sn">
              <el-checkbox label="SN序列号">SN序列号</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">OEM</span>
            <el-checkbox-group v-model="productForm.attributes.oem">
              <el-checkbox label="OEM">OEM</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">材质</span>
            <el-checkbox-group v-model="productForm.attributes.material">
              <el-checkbox label="4.8">4.8</el-checkbox>
              <el-checkbox label="8.8">8.8</el-checkbox>
              <el-checkbox label="10.9">10.9</el-checkbox>
              <el-checkbox label="12.9">12.9</el-checkbox>
              <el-checkbox label="304">304</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">表面</span>
            <el-checkbox-group v-model="productForm.attributes.surface">
              <el-checkbox label="发黑">发黑</el-checkbox>
              <el-checkbox label="本色">本色</el-checkbox>
              <el-checkbox label="镀锌">镀锌</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">颜色</span>
            <el-checkbox-group v-model="productForm.attributes.color">
              <el-checkbox label="红色">红色</el-checkbox>
              <el-checkbox label="白色">白色</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">内存</span>
            <el-checkbox-group v-model="productForm.attributes.memory">
              <el-checkbox label="8G">8G</el-checkbox>
              <el-checkbox label="10G">10G</el-checkbox>
              <el-checkbox label="12G">12G</el-checkbox>
              <el-checkbox label="6G">6G</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">存储空间</span>
            <el-checkbox-group v-model="productForm.attributes.storage">
              <el-checkbox label="128G">128G</el-checkbox>
              <el-checkbox label="256G">256G</el-checkbox>
              <el-checkbox label="512G">512G</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">sn</span>
            <el-checkbox-group v-model="productForm.attributes.sn2">
              <el-checkbox label="编号">编号</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">4444</span>
            <el-checkbox-group v-model="productForm.attributes.code4444">
              <el-checkbox label="ee">ee</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">渠道</span>
            <el-checkbox-group v-model="productForm.attributes.channel">
              <el-checkbox label="自研">自研</el-checkbox>
              <el-checkbox label="经销">经销</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">尺码</span>
            <el-checkbox-group v-model="productForm.attributes.size">
              <el-checkbox label="S">S</el-checkbox>
              <el-checkbox label="M">M</el-checkbox>
              <el-checkbox label="L">L</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">生产日期</span>
            <el-checkbox-group v-model="productForm.attributes.productionDate">
              <el-checkbox label="生产日期">生产日期</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">长</span>
            <el-checkbox-group v-model="productForm.attributes.length">
              <el-checkbox label="1">1</el-checkbox>
              <el-checkbox label="2">2</el-checkbox>
              <el-checkbox label="3">3</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">色温</span>
            <el-checkbox-group v-model="productForm.attributes.colorTemp">
              <el-checkbox label="www">www</el-checkbox>
              <el-checkbox label="34343">34343</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="attribute-item">
            <span class="attribute-name">宽</span>
            <el-checkbox-group v-model="productForm.attributes.width">
              <el-checkbox label="1111">1111</el-checkbox>
              <el-checkbox label="11111">11111</el-checkbox>
            </el-checkbox-group>
          </div>
          <div class="custom-table-container" style="margin-top: 30px">
            <NormalTable
              :tabdata="{
                rows: attributeTableData,
                total: attributeTableData.length,
                pageIndex: 1,
                pageSize: 10
              }"
              :tabdatacolumns="attributeTableColumns"
              emptyText="暂无数据"
              :istabpage="false"
              :containerStyle="{ border: '1px solid #ebeef5', borderRadius: '4px' }"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 属性配置标签页 -->
      <el-tab-pane label="属性配置" name="config">
        <div class="config-container">
          <!-- 折扣策略标题行 -->
          <div
            class="discount-strategy-header"
            @click="showDiscountTable = !showDiscountTable"
            style="
              display: flex;
              justify-content: space-between;
              align-items: center;
              cursor: pointer;
              padding: 10px 0;
              background-color: transparent;
              border: none;
            "
          >
            <span>折扣策略</span>
            <i
              class="el-icon-arrow-down"
              :class="{ 'rotate-180': showDiscountTable }"
              style="font-size: 14px; transition: transform 0.3s"
            ></i>
          </div>

          <!-- 折扣策略表格（使用公共组件） -->
          <div
            v-if="showDiscountTable"
            class="discount-table-container"
            style="margin-bottom: 15px"
          >
            <NormalTable
              :tabdata="{
                rows: discountStrategies,
                total: discountStrategies.length,
                pageIndex: 1,
                pageSize: 10
              }"
              :tabdatacolumns="discountTableColumns"
              :taboperbtns="discountTableButtons"
              @taboper-click="handleDiscountTableOperate"
              :istabpage="false"
              :istabseq="false"
              :containerStyle="{ border: '1px solid #ebeef5', borderRadius: '0 0 4px 4px' }"
              emptyText="暂无数据"
            >
              <!-- 自定义操作列 -->
              <template #customercell="{ column, row, index }">
                <template v-if="column.prop === 'operate'">
                  <el-dropdown @command="(cmd) => handleDiscountStrategy(cmd, index)">
                    <el-button type="text" size="small">
                      相关操作 <i class="el-icon-more"></i>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="edit">编辑</el-dropdown-item>
                        <el-dropdown-item command="delete">删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </template>
              </template>
            </NormalTable>
          </div>

          <!-- 序列商品和批次商品配置 -->
          <div class="config-item">
            <span>批次商品</span>
            <el-switch v-model="productForm.config.batchProduct" style="float: right" />
          </div>
          <div class="config-item" v-if="productForm.config.batchProduct">
            <span>有效期</span>
            <el-switch v-model="productForm.config.validPeriod" style="float: right" />
          </div>
          <div
            class="config-item"
            v-if="productForm.config.batchProduct && productForm.config.validPeriod"
          >
            <label class="required">保质期</label>
            <el-input
              v-model.number="productForm.config.shelfLife"
              placeholder="0"
              type="number"
              style="width: 150px"
            >
              <template #append>天</template>
            </el-input>
          </div>
          <div
            class="config-item"
            v-if="productForm.config.batchProduct && productForm.config.validPeriod"
          >
            <label class="required">预警阈值</label>
            <el-input
              v-model.number="productForm.config.warningThreshold"
              placeholder="0"
              type="number"
              style="width: 150px"
            >
              <template #append>天</template>
            </el-input>
          </div>
        </div>
      </el-tab-pane>

      <!-- 图文详情标签页 -->
      <el-tab-pane label="图文详情" name="detail">
        <div class="detail-container">
          <div class="image-upload">
            <label>商品图像</label>
            <div class="upload-area" @click="handleImageUpload">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <span class="upload-text">点击上传</span>
            </div>
            <!-- 显示已上传的图片 -->
            <div class="image-list" v-if="productForm.detail.images.length > 0">
              <div
                v-for="(image, index) in productForm.detail.images"
                :key="index"
                class="image-item"
              >
                <img :src="image" alt="商品图片" class="preview-image" />
                <el-icon class="delete-icon" @click.stop="handleDeleteImage(index)"
                  ><Delete
                /></el-icon>
              </div>
            </div>
          </div>
          <div class="rich-text">
            <label>图文详情</label>
            <div class="editor-toolbar">
              <!-- 简化的编辑器工具栏 -->
              <el-button size="small" type="text"><strong>H</strong></el-button>
              <el-button size="small" type="text"><strong>B</strong></el-button>
              <el-button size="small" type="text"><em>I</em></el-button>
              <el-button size="small" type="text"><u>U</u></el-button>
              <el-button size="small" type="text">S</el-button>
              <el-button size="small" type="text">≡</el-button>
              <el-button size="small" type="text">≣</el-button>
              <el-button size="small" type="text">📎</el-button>
              <el-button size="small" type="text">📷</el-button>
            </div>
            <el-input
              v-model="productForm.detail.content"
              placeholder="请输入内容..."
              type="textarea"
              :rows="10"
              style="margin-top: 10px"
            />
          </div>
        </div>
      </el-tab-pane>

      <!-- 拓展信息标签页 -->
      <el-tab-pane label="拓展信息" name="extension">
        <div class="extension-container">
          <div class="form-grid">
            <div class="form-item">
              <label>多选菜单</label>
              <el-checkbox-group v-model="productForm.extension.multiSelect1">
                <el-checkbox label="选项1">选项1</el-checkbox>
                <el-checkbox label="选项2">选项2</el-checkbox>
              </el-checkbox-group>
            </div>
            <div class="form-item">
              <label>附件数据</label>
              <div class="upload-container">
                <el-input
                  v-model="productForm.extension.attachment"
                  placeholder="点击上传"
                  readonly
                  style="width: 200px"
                >
                  <template #append>
                    <el-button @click="handleAttachmentUpload">上传</el-button>
                  </template>
                </el-input>
              </div>
            </div>
          </div>

          <div class="form-grid">
            <div class="form-item">
              <label>多选菜单</label>
              <el-checkbox-group v-model="productForm.extension.multiSelect2">
                <el-checkbox label="选项1">选项1</el-checkbox>
                <el-checkbox label="选项2">选项2</el-checkbox>
              </el-checkbox-group>
            </div>
            <div class="form-item">
              <label>单选菜单</label>
              <el-select v-model="productForm.extension.singleSelect" placeholder="请选择">
                <el-option label="选项1" value="选项1" />
                <el-option label="选项2" value="选项2" />
                <el-option label="选项3" value="选项3" />
              </el-select>
            </div>
          </div>

          <div class="form-item full-width">
            <label>多行文本</label>
            <el-input
              v-model="productForm.extension.multiText"
              placeholder="请输入内容..."
              type="textarea"
              :rows="4"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <template #footer>
      <div style="display: flex; justify-content: flex-end; gap: 10px">
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 批量操作对话框 -->
  <el-dialog
    v-model="batchDialogVisible"
    title="批量"
    width="400px"
    @close="handleBatchDialogClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-tabs v-model="batchActiveTab" class="batch-tabs">
      <!-- 导入数据标签页 -->
      <el-tab-pane label="导入数据" name="import">
        <div class="import-content">
          <ol class="import-instructions">
            <li>该功能适用于批量导入数据。</li>
            <li>您需要下载数据模板后使用Excel录入数据。</li>
            <li>录入数据时，请勿修改首行数据标题以及排序。</li>
            <li>请查询使用文档获取字段格式内容以及相关导入须知。</li>
            <li>点击下方上传模板，选择您编辑好的模板文件即可。</li>
          </ol>
          <div class="import-actions">
            <el-button @click="handleDownloadTemplate">下载模板</el-button>
            <el-button type="primary" @click="handleUploadTemplate" :disabled="uploadDisabled"
              >上传模板</el-button
            >
          </div>
        </div>
      </el-tab-pane>

      <!-- 导出数据标签页 -->
      <el-tab-pane label="导出数据" name="export">
        <div class="export-content" @click="handleExportData">
          <div class="export-button-container">
            <el-icon class="export-icon"><Refresh /></el-icon>
            <span class="export-text">导出数据</span>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup lang="ts">
import '../styles/common-styles.css'
import { ref, computed, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElCascader } from 'element-plus'
import { Plus, Refresh, Collection, Search, Delete } from '@element-plus/icons-vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import {
  type PageDTO,
  createPageDTO,
  type MyTableColumn,
  type MyTableOperationsBtn
} from '@/components/normaltable/type'
import { ProductAPI } from '@/apis/basicdata/product'
import type { ProductQuery, AddProductRequest } from '@/apis/basicdata/product'
import type { Product as ApiProduct } from '@/apis/basicdata/product'

// API错误处理函数
const handleApiError = (error: any, operation: string): void => {
  console.error(`${operation}失败:`, error)

  // 尝试获取错误信息
  let errorMessage = '操作失败，请稍后重试'
  if (error?.response?.data?.message) {
    errorMessage = error.response.data.message
  } else if (error?.message) {
    errorMessage = error.message
  }

  ElMessage.error(errorMessage)
}

// 三点下拉菜单可见性
const dropdownVisible = ref(false)

// 对话框可见性
const dialogVisible = ref(false)

// 批量操作对话框可见性
const batchDialogVisible = ref(false)

// 当前激活的标签页
const activeTab = ref('basic')

// 批量操作当前激活的标签页
const batchActiveTab = ref('import')

// 文件上传相关
const fileList = ref<
  Array<{
    name: string
    url?: string
    raw?: File
  }>
>([])

const uploadDisabled = ref(false)

// 表格属性配置 - 复刻InventoryQuery样式
const tableAttr = reactive({
  'header-cell-style': { textAlign: 'center', backgroundColor: '#f5f7fa' },
  'cell-style': { textAlign: 'center' }
})

// 搜索表单数据
const searchForm = reactive({
  name: '',
  code: '',
  model: '',
  category: '',
  brand: '',
  type: '',
  barcode: '',
  remark: ''
})

// 商品表单数据（补充所有辅助属性）
const productForm = reactive({
  id: '',
  name: '',
  code: '',
  model: '',
  category: '',
  brand: '',
  unit: '',
  purchasePrice: 0,
  salesPrice: 0,
  barcode: '',
  location: '',
  stockThreshold: 30,
  type: '常规商品',
  remark: '',
  attributes: {
    sn: [], // SN码
    oem: [], // OEM
    material: [], // 材质
    surface: [], // 表面
    color: [], // 颜色
    memory: [], // 内存
    storage: [], // 存储空间
    sn2: [], // sn 编号
    code4444: [], // 4444 ee
    channel: [], // 渠道
    size: [], // 尺码
    productionDate: [], // 生产日期
    length: [], // 长
    colorTemp: [], // 色温
    width: [] // 宽
  },
  config: {
    serialProduct: false,
    batchProduct: false,
    validPeriod: false,
    shelfLife: 0,
    warningThreshold: 0
  },
  detail: {
    images: [] as string[],
    content: ''
  },
  // 拓展信息
  extension: {
    multiSelect1: [] as string[], // 多选菜单1
    attachment: '', // 附件数据
    multiSelect2: [] as string[], // 多选菜单2
    singleSelect: '', // 单选菜单
    multiText: '' // 多行文本
  }
})

// 分页数据
const tablePageData = ref<PageDTO<any>>(createPageDTO())
const selectedProducts = ref<any[]>([])

// 折扣策略数据
const discountStrategies = ref([])
// 控制折扣策略表格显示状态
const showDiscountTable = ref(false)

// 折扣策略表格列配置
const discountTableColumns: MyTableColumn[] = [
  { prop: 'customerLevel', label: '客户等级', width: '150px' },
  { prop: 'discountRate', label: '折扣率(%)', width: '100px' },
  { prop: 'remark', label: '备注信息', minWidth: '200px' },
  { prop: 'operate', label: '相关操作', width: '100px', fixed: 'right' as const }
]

// 折扣策略表格按钮配置（预留，实际操作通过自定义插槽实现）
const discountTableButtons: MyTableOperationsBtn[] = []

// 定义商品类型
interface Product {
  id: string
  name: string
  code: string
  model: string
  category: string
  brand: string
  type: string
  unit: string
  barcode: string
  remark: string
  purchasePrice?: number
  salesPrice?: number
  location?: string
  stockThreshold?: number
  detail?: {
    images: string[]
    content: string
  }
  extension?: {
    multiSelect1: string[]
    attachment: string
    multiSelect2: string[]
    singleSelect: string
    multiText: string
  }
}

// 折扣表格操作处理函数（预留，便于后续扩展）
const handleDiscountTableOperate = (
  index: number,
  row: Record<string, unknown>,
  evtName: string
) => {
  console.log('Table operation:', evtName, row)
}

// 表格数据
const tableData = ref([
  {
    id: '1',
    name: '商品名称1',
    code: 'SP001',
    model: 'M001',
    category: '类别1',
    brand: '品牌1',
    type: '类型1',
    unit: '个',
    barcode: '1234567890',
    remark: '备注1'
  },
  {
    id: '2',
    name: '商品名称2',
    code: 'SP002',
    model: 'M002',
    category: '类别2',
    brand: '品牌2',
    type: '类型2',
    unit: '件',
    barcode: '0987654321',
    remark: '备注2'
  },
  {
    id: '3',
    name: '商品名称3',
    code: 'SP003',
    model: 'M003',
    category: '类别3',
    brand: '品牌3',
    type: '类型3',
    unit: '箱',
    barcode: '1122334455',
    remark: '备注3'
  },
  {
    id: '4',
    name: '商品名称4',
    code: 'SP004',
    model: 'M004',
    category: '类别4',
    brand: '品牌4',
    type: '类型4',
    unit: '个',
    barcode: '5544332211',
    remark: '备注4'
  },
  {
    id: '5',
    name: '商品名称5',
    code: 'SP005',
    model: 'M005',
    category: '类别5',
    brand: '品牌5',
    type: '类型5',
    unit: '件',
    barcode: '1234567895',
    remark: '备注5'
  }
])

// 商品类别树形数据
const categoryOptions = ref([
  {
    value: '默认类别',
    label: '默认类别',
    children: [
      { value: '电脑/配件', label: '电脑/配件' },
      { value: '染色器', label: '染色器' },
      { value: '医疗器械', label: '医疗器械' }
    ]
  },
  {
    value: '水果',
    label: '水果',
    children: [
      { value: '瓜类', label: '瓜类' },
      { value: '手机', label: '手机' }
    ]
  },
  {
    value: '1',
    label: '1',
    children: [{ value: '2', label: '2' }]
  },
  {
    value: '下一代防火墙',
    label: '下一代防火墙'
  },
  {
    value: '鞋子',
    label: '鞋子'
  },
  {
    value: '办公耗材',
    label: '办公耗材'
  }
])

// 表格列配置
const tabdatacolumns: MyTableColumn[] = [
  { prop: 'name', label: '商品名称', width: 180 },
  { prop: 'code', label: '商品编号', width: 120 },
  { prop: 'model', label: '商品型号', width: 120 },
  { prop: 'category', label: '商品类别', width: 100 },
  { prop: 'brand', label: '商品品牌', width: 100 },
  { prop: 'type', label: '商品类型', width: 100 },
  { prop: 'unit', label: '商品单位', width: 80 },
  { prop: 'barcode', label: '商品条码', width: 150 },
  { prop: 'remark', label: '备注' },
  { prop: 'operate', label: '相关操作', width: 150, fixed: 'right' }
]

// 表格操作按钮配置
const taboperbtns: MyTableOperationsBtn[] = [
  {
    text: '详情',
    attr: {
      size: 'small',
      type: 'primary',
      style: {
        backgroundColor: 'white',
        color: '#409eff',
        borderColor: '#dcdfe6',
        marginRight: '4px'
      }
    },
    evtname: 'view'
  },
  {
    text: '删除',
    attr: {
      size: 'small',
      type: 'danger',
      style: {
        backgroundColor: 'white',
        color: '#f56c6c',
        borderColor: '#dcdfe6'
      }
    },
    evtname: 'delete'
  }
]

// 表格操作处理函数
const handleTableOperClick = (index: number, row: Product, evtname: string) => {
  if (evtname === 'view') {
    handleView(row)
  } else if (evtname === 'delete') {
    handleDelete(row)
  }
}

// 计算过滤后的商品
const filteredProducts = computed(() => {
  return tableData.value.filter((product) => {
    const categoryValue = Array.isArray(searchForm.category)
      ? searchForm.category[searchForm.category.length - 1]
      : searchForm.category

    return (
      (!searchForm.name || product.name.includes(searchForm.name)) &&
      (!searchForm.code || product.code.includes(searchForm.code)) &&
      (!searchForm.model || product.model.includes(searchForm.model)) &&
      (!categoryValue || product.category.includes(categoryValue)) &&
      (!searchForm.brand || product.brand.includes(searchForm.brand)) &&
      (!searchForm.type || product.type.includes(searchForm.type)) &&
      (!searchForm.barcode || product.barcode.includes(searchForm.barcode)) &&
      (!searchForm.remark || product.remark.includes(searchForm.remark))
    )
  })
})

// 更新表格数据
const updateTableData = async () => {
  try {
    // 构建查询参数
    const queryParams: ProductQuery = {
      name: searchForm.name || undefined,
      code: searchForm.code || undefined,
      model: searchForm.model || undefined,
      category: Array.isArray(searchForm.category)
        ? searchForm.category[searchForm.category.length - 1]
        : searchForm.category || undefined,
      brand: searchForm.brand || undefined,
      type: searchForm.type || undefined,
      barcode: searchForm.barcode || undefined,
      remark: searchForm.remark || undefined,
      pageIndex: tablePageData.value.pageIndex,
      pageSize: tablePageData.value.pageSize
    }

    const response = await ProductAPI.getProductList(queryParams)
    if (response.data) {
      tablePageData.value = response.data
    }
  } catch (error) {
    handleApiError(error, '获取商品列表')
  }
}

// 处理选中项变化
const handleSelectionChange = (val: Product[]) => {
  selectedProducts.value = val
}

// 分页处理函数
const handlePageChange = (data: PageDTO<Product>) => {
  tablePageData.value = data
  updateTableData()
}

// 搜索处理
const handleSearch = () => {
  tablePageData.value.pageIndex = 1
  dropdownVisible.value = false
  updateTableData()
}

// 重置搜索
const handleResetSearch = () => {
  Object.keys(searchForm).forEach((key) => {
    searchForm[key as keyof typeof searchForm] = ''
  })
  tablePageData.value.pageIndex = 1
  updateTableData()
}

// 批量操作
const handleBatch = () => {
  batchDialogVisible.value = true
  batchActiveTab.value = 'import'
}

// 下载模板
const handleDownloadTemplate = () => {
  ElMessage.success('模板下载中...')
}

// 上传模板
const handleUploadTemplate = () => {
  const fileInput = document.createElement('input')
  fileInput.type = 'file'
  fileInput.accept = '.xlsx,.xls'

  fileInput.onchange = (event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
      ElMessage.success(`已选择文件: ${files[0].name}`)
      uploadDisabled.value = true
      setTimeout(() => {
        ElMessage.success('文件上传成功')
        uploadDisabled.value = false
      }, 1000)
    }
  }

  fileInput.click()
}

// 导出数据
const handleExportData = () => {
  if (selectedProducts.value.length === 0) {
    ElMessage.warning('请先选择要导出的商品')
    return
  }

  ElMessage.success(`正在导出${selectedProducts.value.length}条数据...`)
  setTimeout(() => {
    ElMessage.success('数据导出成功')
    const csvContent =
      '商品ID,商品名称,商品编号,商品型号,商品类别,商品品牌\n' +
      selectedProducts.value
        .map((p) => `${p.id},${p.name},${p.code},${p.model},${p.category},${p.brand}`)
        .join('\n')
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.setAttribute('href', url)
    link.setAttribute('download', `商品数据_${new Date().getTime()}.csv`)
    link.style.display = 'none'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }, 1000)
}

// 关闭批量操作对话框
const handleBatchDialogClose = () => {
  batchDialogVisible.value = false
}

// 刷新
const handleRefresh = () => {
  ElMessage.info('正在刷新数据...')
  tablePageData.value.pageIndex = 1
  updateTableData()
}

// 处理折扣策略操作
const handleDiscountStrategy = (command: string, index: number | null = null) => {
  switch (command) {
    case 'add':
      ElMessage.success('添加折扣')
      break
    case 'edit':
      ElMessage.success(index !== null ? `编辑第${index + 1}个折扣` : '编辑折扣')
      break
    case 'delete':
      ElMessage.success(index !== null ? `删除第${index + 1}个折扣` : '删除折扣')
      break
  }
}

// 处理新增商品
const handleAdd = () => {
  resetProductForm()
  dialogVisible.value = true
  activeTab.value = 'basic'
}

// 重置商品表单
const resetProductForm = () => {
  productForm.id = ''
  productForm.name = ''
  productForm.code = ''
  productForm.model = ''
  productForm.category = ''
  productForm.brand = ''
  productForm.unit = ''
  productForm.purchasePrice = 0
  productForm.salesPrice = 0
  productForm.barcode = ''
  productForm.location = ''
  productForm.stockThreshold = 30
  productForm.type = '常规商品'
  productForm.remark = ''

  // 重置辅助属性
  Object.keys(productForm.attributes).forEach((key) => {
    ;(productForm.attributes as any)[key] = []
  })

  // 重置配置
  productForm.config.serialProduct = false
  productForm.config.batchProduct = false
  productForm.config.validPeriod = false
  productForm.config.shelfLife = 0
  productForm.config.warningThreshold = 0

  // 重置详情
  productForm.detail.content = ''
  productForm.detail.images = []

  // 重置拓展信息
  productForm.extension.multiSelect1 = [] as string[]
  productForm.extension.attachment = ''
  productForm.extension.multiSelect2 = [] as string[]
  productForm.extension.singleSelect = ''
  productForm.extension.multiText = ''
}

// 处理图片上传
const handleImageUpload = () => {
  const fileInput = document.createElement('input')
  fileInput.type = 'file'
  fileInput.accept = 'image/*'
  fileInput.multiple = true

  fileInput.onchange = (event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
      ElMessage.success(`已选择 ${files.length} 张图片`)
      Array.from(files).forEach((file) => {
        const reader = new FileReader()
        reader.onload = (e) => {
          const imageUrl = e.target?.result as string
          productForm.detail.images.push(imageUrl)
        }
        reader.readAsDataURL(file)
      })
    }
  }

  fileInput.click()
}

// 处理删除图片
const handleDeleteImage = (index: number) => {
  ElMessageBox.confirm('确定要删除这张图片吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      productForm.detail.images.splice(index, 1)
      ElMessage.success('图片删除成功')
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}

// 处理附件上传
const handleAttachmentUpload = () => {
  const fileInput = document.createElement('input')
  fileInput.type = 'file'
  fileInput.accept = '*'

  fileInput.onchange = (event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
      productForm.extension.attachment = files[0].name
      ElMessage.success(`附件「${files[0].name}」上传成功`)
    }
  }

  fileInput.click()
}

// 处理对话框关闭
const handleDialogClose = () => {
  dialogVisible.value = false
  resetProductForm()
}

// 处理保存
const handleSave = async () => {
  if (
    !productForm.name ||
    !productForm.code ||
    !productForm.category ||
    !productForm.unit ||
    productForm.purchasePrice === null ||
    productForm.salesPrice === null ||
    productForm.stockThreshold === null
  ) {
    ElMessage.warning('请填写所有必填项')
    return
  }

  if (
    productForm.config.validPeriod &&
    (productForm.config.shelfLife === null || productForm.config.warningThreshold === null)
  ) {
    ElMessage.warning('请填写保质期和预警阈值')
    return
  }

  try {
    // 构建请求数据，确保正确的字段映射
    const requestData: AddProductRequest = {
      name: productForm.name,
      code: productForm.code,
      model: productForm.model || undefined,
      category: typeof productForm.category === 'string' ? Number(productForm.category) || undefined : productForm.category,
      brand: productForm.brand || undefined,
      unit: productForm.unit,
      purchasePrice: Number(productForm.purchasePrice), // 确保是数字类型
      salesPrice: Number(productForm.salesPrice), // 确保是数字类型
      barcode: productForm.barcode || undefined,
      location: productForm.location || undefined,
      stockThreshold: Number(productForm.stockThreshold), // 确保是数字类型
      type: productForm.type,
      remark: productForm.remark || undefined,
      py: productForm.name || undefined, // 拼音字段，使用名称作为基础
      spec: productForm.model || productForm.code, // 规格字段
      batchProduct: !!productForm.config.batchProduct, // 确保是布尔类型
      validPeriod: !!productForm.config.validPeriod, // 确保是布尔类型
      shelfLife: productForm.config.shelfLife ? Number(productForm.config.shelfLife) : 0,
      warningThreshold: productForm.config.warningThreshold
        ? Number(productForm.config.warningThreshold)
        : 0,
      // 安全地序列化JSON数据
      attributes:
        typeof productForm.attributes === 'object'
          ? JSON.stringify(productForm.attributes || {})
          : '{}',
      images: Array.isArray(productForm.detail.images) ? productForm.detail.images : [],
      content: productForm.detail.content || undefined,
      // 安全地序列化拓展信息
      extension:
        typeof productForm.extension === 'object'
          ? JSON.stringify(productForm.extension || {})
          : '{}'
    }

    if (!productForm.id) {
      // 新增商品
      await ProductAPI.addProduct(requestData)
      ElMessage.success('商品新增成功')
    } else {
      // 编辑商品 - 构建Product对象
      const updateData: ApiProduct = {
        id: productForm.id,
        name: requestData.name,
        number: requestData.code || '',
        py: requestData.py,
        spec: requestData.spec,
        category: requestData.category?.toString(),
        brand: requestData.brand,
        unit: requestData.unit,
        buy: requestData.purchasePrice,
        sell: requestData.salesPrice,
        code: requestData.barcode,
        location: requestData.location,
        stock: requestData.stockThreshold,
        type: typeof requestData.type === 'string' ? Number(requestData.type) : requestData.type,
        data: requestData.remark
      }
      await ProductAPI.updateProduct(updateData)
      ElMessage.success('商品更新成功')
    }

    dialogVisible.value = false
    updateTableData()
  } catch (error) {
    handleApiError(error, productForm.id ? '更新商品' : '新增商品')
  }
}

// 查看商品详情
const handleView = async (product: Product) => {
  try {
    // 调用API获取商品详情
    const response = await ProductAPI.getProductDetail(product.id)
    const detailData = response.data

    if (!detailData) {
      ElMessage.error('获取商品详情失败')
      return
    }

    // 填充表单数据，确保字段类型正确
    productForm.id = detailData.id
    productForm.name = String(detailData.name || '')
    productForm.code = String(detailData.code || detailData.number || '')
    productForm.model = String(detailData.spec || '')
    productForm.category = String(detailData.category || '')
    productForm.brand = String(detailData.brand || '')
    productForm.unit = String(detailData.unit || '')
    productForm.purchasePrice = Number(detailData.buy) || 0
    productForm.salesPrice = Number(detailData.sell) || 0
    productForm.barcode = String(detailData.code || '')
    productForm.location = String(detailData.location || '')
    productForm.stockThreshold = Number(detailData.stock || detailData.threshold) || 30
    productForm.type = String(detailData.type || '常规商品')
    productForm.remark = String(detailData.data || detailData.memo || '')

    // 解析配置信息，确保类型正确
    productForm.config.batchProduct = Boolean(detailData.batch)
    productForm.config.validPeriod = Boolean(detailData.validity)
    productForm.config.shelfLife = Number(detailData.validity) || 0
    productForm.config.warningThreshold = Number(detailData.protect || detailData.threshold) || 0

    // 解析详情信息，确保数据结构正确
    const imgs = detailData.imgs || ''
    productForm.detail.images = typeof imgs === 'string' && imgs ? imgs.split(',').filter(Boolean) : []
    productForm.detail.content = String(detailData.details || '')

    // 解析辅助属性，安全处理可能的JSON字符串
    try {
      const attributes = (detailData as any).attributes
      if (typeof attributes === 'string') {
        productForm.attributes = JSON.parse(attributes)
      } else if (typeof attributes === 'object' && attributes !== null) {
        productForm.attributes = attributes
      } else {
        productForm.attributes = {} as any
      }
    } catch (e) {
      console.warn('解析attributes失败，使用默认值:', e)
      productForm.attributes = {} as any
    }

    // 解析拓展信息，安全处理可能的JSON字符串
    try {
      let extensionData: any = {}
      const extension = (detailData as any).extension
      if (typeof extension === 'string') {
        extensionData = JSON.parse(extension)
      } else if (typeof extension === 'object' && extension !== null) {
        extensionData = extension
      }

      // 确保拓展信息结构完整
      productForm.extension = {
        multiSelect1: Array.isArray(extensionData.multiSelect1) ? extensionData.multiSelect1 : [],
        attachment: String(extensionData.attachment || ''),
        multiSelect2: Array.isArray(extensionData.multiSelect2) ? extensionData.multiSelect2 : [],
        singleSelect: String(extensionData.singleSelect || ''),
        multiText: String(extensionData.multiText || '')
      }
    } catch (e) {
      console.warn('解析extension失败，使用默认值:', e)
      // 重置为默认的拓展信息结构
      productForm.extension = {
        multiSelect1: [],
        attachment: '',
        multiSelect2: [],
        singleSelect: '',
        multiText: ''
      }
    }

    dialogVisible.value = true
  } catch (error) {
    handleApiError(error, '获取商品详情')
  }
}

// 删除商品
const handleDelete = (product: Product) => {
  ElMessageBox.confirm(`确定要删除商品「${product.name}」吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        // 调用删除API
        await ProductAPI.deleteProduct([product.id])

        const index = tableData.value.findIndex((item) => item.id === product.id)
        if (index > -1) {
          tableData.value.splice(index, 1)
          ElMessage.success('删除成功')
          updateTableData()
        }
      } catch (error) {
        handleApiError(error, '删除商品')
      }
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}
// 表格列配置
const attributeTableColumns: MyTableColumn[] = [
  { prop: 'name', label: '属性名称', width: 200 },
  { prop: 'purchasePrice', label: '采购价格', width: 120, sortable: true },
  { prop: 'salesPrice', label: '销售价格', width: 120, sortable: true },
  { prop: 'barcode', label: '条形码', flex: 1 } // 改为flex: 1，自动填充剩余宽度
]

// 表格数据（初始为空）
const attributeTableData = ref<any[]>([])
// 组件挂载时的初始化操作
onMounted(() => {
  updateTableData()
})
</script>

<style scoped>
.custom-table-container {
  width: 100%;
  overflow-x: auto;
}

.sys-area {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 操作栏样式 */
.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
}

/* 商品编辑对话框样式 */
.form-content {
  padding: 20px 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-item.full-width {
  grid-column: span 3;
}

.form-item label {
  font-size: 14px;
  color: #606266;
}

.form-item label.required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

/* 辅助属性样式 */
.attributes-container {
  max-height: 400px;
  overflow-y: auto;
  padding: 20px 0;
}

.attribute-item {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.attribute-name {
  width: 80px;
  font-size: 14px;
  color: #606266;
  flex-shrink: 0;
}

.attribute-item .el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 折扣策略样式 */
.discount-strategy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.discount-strategy-header:hover {
  background-color: #f5f7fa;
}

.rotate-180 {
  transform: rotate(180deg);
}

:deep(.el-icon-arrow-down) {
  transition: transform 0.3s ease;
}

/* 配置样式 */
.config-container {
  padding: 20px 0;
}

.config-item {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 详情样式 */
.detail-container {
  padding: 20px 0;
}

.image-upload {
  margin-bottom: 30px;
}

.upload-area {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: #409eff;
}

.upload-icon {
  font-size: 24px;
  color: #c0c4cc;
}

.upload-text {
  display: block;
  margin-top: 8px;
  font-size: 12px;
  color: #c0c4cc;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.image-item {
  position: relative;
  width: 120px;
  height: 120px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.delete-icon {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  cursor: pointer;
}

.delete-icon:hover {
  background: rgba(245, 108, 108, 0.8);
}

.rich-text {
  margin-top: 20px;
}

.rich-text label {
  font-size: 14px;
  color: #606266;
}

.editor-toolbar {
  display: flex;
  gap: 5px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  margin: 10px 0;
}

.rich-text-editor {
  min-height: 300px;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  outline: none;
  margin-top: 10px;
}

.rich-text-editor[contenteditable='true']:empty:before {
  content: attr(placeholder);
  color: #c0c4cc;
  pointer-events: none;
}

/* 拓展信息样式 */
.extension-container {
  padding: 20px 0;
}

.upload-container {
  display: flex;
  align-items: center;
}

.upload-container .el-input {
  margin-right: 8px;
}

.form-item .el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 5px;
}

/* 批量操作对话框样式 */
.batch-tabs {
  width: 100%;
}

.batch-tabs .el-tabs__header {
  margin-bottom: 16px;
}

.batch-tabs .el-tabs__item {
  padding: 0 20px;
}

.batch-tabs .el-tabs__active-bar {
  height: 2px;
  background-color: #1890ff;
}

/* 导入数据样式 */
.import-content {
  padding: 0 8px;
}

.import-instructions {
  margin-bottom: 24px;
  padding-left: 20px;
  color: #333;
  line-height: 28px;
  font-size: 14px;
}

.import-instructions li {
  margin-bottom: 8px;
}

.import-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 16px;
}

/* 导出数据样式 */
.export-content {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 180px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.export-content:hover {
  background-color: #f5f5f5;
}

.export-button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 20px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  background-color: #fafafa;
  transition: all 0.3s ease;
}

.export-content:hover .export-button-container {
  border-color: #1890ff;
  background-color: #fff;
}

.export-icon {
  font-size: 32px;
  color: #909399;
  margin-bottom: 12px;
  transform: rotate(180deg);
}

.export-text {
  font-size: 14px;
  color: #1890ff;
  padding: 0;
}

.operation-left {
  display: flex;
  align-items: center;
}

.operation-right {
  display: flex;
  gap: 10px;
}

.action-btn {
  min-width: 80px;
  font-size: 14px;
}

/* 三点下拉菜单样式 */
.dropdown-menu {
  position: relative;
  display: inline-block;
}

.dropdown-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.dropdown-button:hover {
  background-color: #f5f5f5;
}

.dropdown-content {
  position: absolute;
  left: 0;
  top: 100%;
  background-color: white;
  min-width: 800px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  padding: 16px;
  z-index: 1000;
  margin-top: 4px;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
}

/* 搜索表单样式 */
.search-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-input,
.form-select {
  flex: 1;
}

.search-button-row {
  justify-content: flex-end;
  gap: 10px;
}

/* 确保搜索按钮没有背景色 */
.search-button-row .el-button {
  background-color: transparent !important;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.search-button-row .el-button:hover {
  background-color: #ecf5ff;
  border-color: #c6e2ff;
  color: #409eff;
}

/* 分割线样式 */
.custom-divider {
  margin: 16px 0;
}

/* 主内容区域样式 */
.main-content {
  display: flex;
  gap: 16px;
}

.table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 表格容器样式 */
.table-container {
  flex: 1;
  overflow-x: auto;
  margin-bottom: 16px;
}

/* 表格样式优化 */
:deep(.el-table) {
  border: 1px solid #ebeef5;
}

:deep(.el-table__header th) {
  text-align: center;
  background-color: #f5f7fa;
  padding: 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-table__body td) {
  text-align: center;
  padding: 12px 0;
  font-size: 14px;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #f5f7fa;
}

/* 确保选择框居中 */
:deep(.el-table__row .el-checkbox__inner) {
  margin: 0 auto;
  display: block;
}

/* 操作按钮样式 */
:deep(.el-table .el-button--small) {
  padding: 6px 12px;
  font-size: 13px;
}

/* 分页样式已由NormalTable组件内部实现 */

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .operation-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .operation-right {
    justify-content: center;
    flex-wrap: wrap;
  }

  .form-row {
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-item.full-width {
    grid-column: span 1;
  }
}

/* 滚动条样式优化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 确保文字清晰可见的样式 */
:deep(.el-input__inner),
:deep(.el-select__input),
:deep(.el-button),
:deep(.el-table__header th),
:deep(.el-table__body td) {
  font-size: 14px;
  line-height: 1.5;
  color: #303133;
}

:deep(.el-table__header th) {
  font-weight: 600;
  background-color: #fafafa;
}
</style>
