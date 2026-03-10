# 系统设置模块

## 概述

系统设置模块提供了完整的系统配置管理功能，包括基础信息、功能参数、物流配置、商品品牌、计量单位、客户类别、客户等级和供应商类别等配置项的管理。

## 文件结构

```
src/views/sysmanage/
├── SystemSettings.vue          # 主页面组件
├── components/                 # 子组件目录
│   └── InputListConfig.vue    # 输入列表配置组件
└── README.md                  # 说明文档

src/apis/advancedSettings/
├── index.ts                   # 接口定义文件
└── README.md                  # API接口文档

src/views/sample/systemsettings/
└── SystemSettingsDemo.vue     # 演示页面
```

## 功能模块

### 1. 基础信息 (BasicInfo)

**功能描述**: 管理系统的基本信息配置

**配置项**:
- `systemName`: 系统名称 (必填)
- `companyName`: 公司名称 (必填)
- `filingInfo`: 备案信息
- `notice`: 公告信息

**组件特性**:
- 表单验证
- 响应式布局
- 实时数据绑定

### 2. 功能参数 (FunctionParams)

**功能描述**: 配置系统的核心功能参数

**配置项**:
- `autoAudit`: 自动审核开关
- `enableTax`: 启用税金开关
- `taxRate`: 税率设置 (0-100%)
- `allowNegativeInventory`: 允许负库存开关
- `pricingMethod`: 计价方法 (fifo/base/ma)
- `accountingType`: 核算类型 (warehouse/headquarters)
- `costRule`: 成本规则 (balance/attr/batch/aab)
- `quantityDecimal`: 数量小数位数 (0-4位)
- `amountDecimal`: 金额小数位数 (2-4位)
- `reportDays`: 报表统计天数

**组件特性**:
- 表格形式展示
- 开关、下拉框、数字输入框混合使用
- 滚动条支持
- 实时配置更新

### 3. 物流配置 (LogisticsConfig)

**功能描述**: 管理物流服务商的启用状态

**配置项**:
- `key`: 物流标识
- `name`: 物流名称
- `enabled`: 启用状态

**预设物流商**:
- 自动识别 (不可修改)
- 德邦物流、邮政快递、快捷速递等

**组件特性**:
- 表格形式展示
- 支持添加自定义物流
- 支持删除非系统物流
- 滚动条支持

### 4. 输入列表配置 (InputListConfig)

**功能描述**: 通用的标签列表管理组件

**适用场景**:
- 商品品牌管理
- 计量单位管理
- 客户类别管理
- 客户等级管理
- 供应商类别管理

**组件特性**:
- 动态添加/删除标签
- 受保护项目设置
- 保留文本验证
- 重复项检查

## 接口设计

### 获取系统设置

```typescript
GET /getMsg
```

**响应格式**:
```json
/**
 * SystemJsonVO
 */
export interface Response {
    /**
     * 状态码
     */
    code: number;
    /**
     * 数据对象
     */
    data?: SystemDTO[];
    /**
     * 提示信息
     */
    message: string;
    [property: string]: any;
}

/**
 * SystemDTO
 */
export interface SystemDTO {
    /**
     * id
     */
    id?: string;
    /**
     * 配置内容
     */
    info?: string;
    /**
     * 配置名称
     */
    name?: string;
    /**
     * 备注信息
     */
    remark?: string;
    [property: string]: any;
}
```

### 保存系统设置

```typescript
PUT /putMsg
```

**请求体**: SystemDTO[] 格式的配置数组
/**
 * SystemDTO
 */
export interface Request {
    /**
     * id
     */
    id?: string;
    /**
     * 配置内容
     */
    info?: string;
    /**
     * 配置名称
     */
    name?: string;
    /**
     * 备注信息
     */
    remark?: string;
    [property: string]: any;
}

### 数据转换

前端使用 `SystemConfigTransformer` 类进行数据格式转换：

- **前端 → 后端**: `transformToApiFormat()` 将表单数据转换为 SystemDTO[] 格式
- **后端 → 前端**: `transformFromApiFormat()` 将 SystemDTO[] 转换为表单数据

详细接口文档请参考: [API接口文档](./src/apis/advancedSettings/README.md)