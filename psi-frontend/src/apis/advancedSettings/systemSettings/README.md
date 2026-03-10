# 系统设置 API 接口文档

## 概述

系统设置模块的 API 接口文档，基于实际后端接口规范编写。包含获取和保存系统配置的完整接口定义。

## 接口列表

### 1. 获取配置信息

**接口描述**: 获取系统所有配置信息

**请求方式**: `GET`

**请求路径**: `/getMsg`

**请求参数**: 无

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**响应格式**:
```typescript
{
  code: number,        // 状态码，10000表示成功
  message: string,      // 提示信息
  data: SystemDTO[]     // 配置数据数组
}
```

**SystemDTO 数据结构**:
```typescript
interface SystemDTO {
  id?: string,         // 配置项ID
  name?: string,       // 配置名称
  info?: string,       // 配置内容
  remark?: string      // 备注信息
}
```

**响应示例**:
```json
{
  "code": 10000,
  "message": "success",
  "data": [
    {
      "id": "basic_system_name",
      "name": "系统名称",
      "info": "点可云进销存软件",
      "remark": "系统基础配置"
    },
    {
      "id": "func_auto_audit",
      "name": "自动审核",
      "info": "1",
      "remark": "是否启用自动审核"
    }
  ]
}
```

### 2. 保存配置信息

**接口描述**: 保存系统配置信息

**请求方式**: `PUT`

**请求路径**: `/putMsg`

**请求参数**:
```typescript
SystemDTO[]  // 配置数据数组
```

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体示例**:
```json
[
  {
    "id": "basic_system_name",
    "name": "系统名称",
    "info": "点可云进销存软件",
    "remark": "系统基础配置"
  },
  {
    "id": "func_auto_audit",
    "name": "自动审核",
    "info": "1",
    "remark": "是否启用自动审核"
  }
]
```



## 配置项映射规则

### 基础信息配置

| 前端字段 | 后端ID | 说明 |
|---------|--------|------|
| `systemName` | `basic_system_name` | 系统名称 |
| `companyName` | `basic_company_name` | 公司名称 |
| `filingInfo` | `basic_filing_info` | 备案信息 |
| `notice` | `basic_notice` | 公告信息 |

### 功能参数配置

| 前端字段 | 后端ID | 数据类型 | 说明 |
|---------|--------|----------|------|
| `autoAudit` | `func_auto_audit` | boolean → "1"/"0" | 自动审核 |
| `enableTax` | `func_enable_tax` | boolean → "1"/"0" | 启用税金 |
| `taxRate` | `func_tax_rate` | number → string | 税率 |
| `allowNegativeInventory` | `func_negative_inventory` | boolean → "1"/"0" | 允许负库存 |
| `pricingMethod` | `func_pricing_method` | string | 计价方法 |
| `accountingType` | `func_accounting_type` | string | 核算类型 |
| `costRule` | `func_cost_rule` | string | 成本规则 |
| `quantityDecimal` | `func_quantity_decimal` | number → string | 数量小数位数 |
| `amountDecimal` | `func_amount_decimal` | number → string | 金额小数位数 |
| `reportDays` | `func_report_days` | number → string | 报表天数 |

### 物流配置

| 前端字段 | 后端ID | 数据类型 | 说明 |
|---------|--------|----------|------|
| `logistics[].key` | `logistics_{key}` | string | 物流标识 |
| `logistics[].name` | `logistics_{key}` | string | 物流名称 |
| `logistics[].enabled` | `logistics_{key}` | boolean → "1"/"0" | 启用状态 |

### 列表配置

| 前端字段 | 后端ID | 数据类型 | 说明 |
|---------|--------|----------|------|
| `brand` | `list_brand` | string[] → JSON | 商品品牌 |
| `unit` | `list_unit` | string[] → JSON | 计量单位 |
| `customerCategory` | `list_customer_category` | string[] → JSON | 客户类别 |
| `customerGrade` | `list_customer_grade` | string[] → JSON | 客户等级 |
| `supplierCategory` | `list_supplier_category` | string[] → JSON | 供应商类别 |

## 前端实现

### 1. 接口调用

```typescript
import { getSystemSettings, saveSystemSettings, SystemConfigTransformer } from '@/apis/advancedSettings/systemSettings'

// 获取配置
const fetchConfig = async () => {
  try {
    const res = await getSystemSettings()
    if (res.code === 10000) {
      // 转换为前端格式
      const formData = SystemConfigTransformer.transformFromApiFormat(res.data || [])
      return formData
    }
  } catch (error) {
    console.error('获取配置失败:', error)
  }
}

// 保存配置
const saveConfig = async (formData: any) => {
  try {
    // 转换为后端格式
    const apiData = SystemConfigTransformer.transformToApiFormat(formData)
    const res = await saveSystemSettings(apiData)
    
    if (res.code === 10000) {
      return true
    }
  } catch (error) {
    console.error('保存配置失败:', error)
  }
}
```

### 2. 数据转换器

`SystemConfigTransformer` 类提供了双向数据转换功能：

- `transformToApiFormat()`: 前端格式 → 后端格式
- `transformFromApiFormat()`: 后端格式 → 前端格式

### 3. 错误处理

```typescript
// 统一错误处理
const handleApiError = (error: any) => {
  if (error.response?.data?.message) {
    ElMessage.error(error.response.data.message)
  } else {
    ElMessage.error('操作失败，请稍后重试')
  }
}
```

## 接口文件位置

**文件路径**: `src/apis/advancedSettings/systemSettings/index.ts`

**主要导出**:
- `getSystemSettings()`: 获取系统配置
- `saveSystemSettings()`: 保存系统配置  
- `SystemConfigTransformer`: 数据转换工具类
- `SystemDTO`: 系统配置数据传输对象接口
- `ApiResponse`: API 响应格式接口

## 使用示例

### 获取配置数据
```typescript
import { getSystemSettings, SystemConfigTransformer } from '@/apis/advancedSettings/systemSettings'

const loadSettings = async () => {
  const response = await getSystemSettings()
  if (response.code === 10000) {
    const formData = SystemConfigTransformer.transformFromApiFormat(response.data || [])
    // 使用 formData 更新界面
  }
}
```

### 保存配置数据
```typescript
import { saveSystemSettings, SystemConfigTransformer } from '@/apis/advancedSettings/systemSettings'

const saveSettings = async (formData: any) => {
  const apiData = SystemConfigTransformer.transformToApiFormat(formData)
  const response = await saveSystemSettings(apiData)
  if (response.code === 10000) {
    // 保存成功处理
  }
}
```
