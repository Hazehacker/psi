📊 功能模块

1. 销售订单跟踪表(SaleOrder.vue)
   功能特点：
   树形结构展示订单父子关系
   支持按客户、商品、单据编号等多条件搜索
   日期范围筛选和排序功能
   数据导出和刷新功能

2. 销售明细表(SaleDetail.vue)
   功能特点：
   销售单与销售退货单明细展示
   金额统计（销售总额、退货总额）
   支持商品、客户、单据类型筛选
   分页显示和数据导出

3. 销售汇总表(SaleSummary.vue)
   功能特点：
   按商品、客户、用户、人员四种维度汇总
   销售与销售退货数据对比
   动态列显示（根据筛选类型调整）
   金额汇总统计

4. 销售收款表(SaleReceipt.vue)
   功能特点：
   收款核销状态管理
   应收款余额统计
   收款率进度条展示
   核销状态筛选

5. 销售排行表(SaleList.vue)
   功能特点：
   商品销售数量排行
   成本与利润分析
   简化的搜索筛选
   数据导出功能

6. 销售利润表(SaleProfit.vue)
   功能特点：
   毛利润与净利润计算
   毛利率和净利率展示
   明细与汇总视图切
   利润颜色标识

🛠 技术栈
前端框架
Vue 3 - 渐进式JavaScript框架
TypeScript - 类型安全的JavaScript超集
Composition API - Vue 3 组合式API
UI组件库
Element Plus - 基于Vue 3的组件库
图标 - Element Plus图标组件
开发工具
Vite - 下一代前端构建工具
Vue Router - 官方路由管理器（假设使用）

📁 项目结构
text
src/
├── components/
│ └── goodSearchConpoent/ # 通用搜索组件
│ ├── GoodSearchForm.vue # 搜索表单组件
│ └── type.ts # 类型定义
├── views/ # 页面组件
│ ├── SalesOrderTracking.vue # 销售订单跟踪表
│ ├── SalesDetail.vue # 销售明细表
│ ├── SalesSummary.vue # 销售汇总表
│ ├── SalesReceipt.vue # 销售收款表
│ ├── SalesRanking.vue # 销售排行表
│ └── SalesProfit.vue # 销售利润表
├── types/ # 全局类型定义
└── utils/ # 工具函数
🎯 核心组件说明
GoodSearch 通用搜索组件
功能：统一的搜索表单组件
配置化：通过config对象动态配置显示字段
自定义字段：支持扩展自定义搜索条件
事件：提供搜索、重置等事件

类型定义 (type.ts)
typescript
interface SearchFormData {
goods?: string
number?: string
customer?: string | null
// ... 其他字段
}

interface GoodSearchConfig {
showGoods: boolean
showNumber: boolean
showCustomer: boolean
// ... 其他配置
customFields?: CustomField[]
}
🔧 安装和运行
环境要求
Node.js 16.0 或更高版本
npm 或 yarn 包管理器

安装依赖
bash
npm install

# 或

yarn install
开发模式
bash
npm run dev

# 或

yarn dev
生产构建
bash
npm run build

# 或

yarn build
💡 主要特性

1. 响应式设计
   支持桌面端和移动端适配
   弹性布局和媒体查询

2. 数据可视化
   表格数据分页展示
   金额格式化显示
   进度条和颜色标识

3. 搜索筛选
   多条件组合搜索
   日期范围选择
   动态字段显示

4. 数据导出
   CSV格式导出
   完整数据下载
   导出文件命名规范

🎨 样式规范
颜色方案
css
/_ 主色调 _/
--el-color-primary: #409EFF;
/_ 成功色 _/
.positive-amount { color: #67C23A; }
/_ 警告色 _/
.negative-amount { color: #F56C6C; }
/_ 中性色 _/
.amount-value { border-color: #DCDFE6; }
布局规范
操作栏：固定高度，左右分布
表格区域：弹性布局，占据剩余空间
分页器：底部固定，支持左右定位

🔄 数据流管理
组件通信
Props：父向子传递配置和数据
Events：子向父传递用户操作
v-model：双向数据绑定

状态管理
Reactive：响应式数据对象
Computed：计算属性
Ref：基本类型响应式引用

📝 开发规范
代码规范
使用TypeScript严格模式
组件使用Composition API
类型定义优先
样式使用Scoped CSS
