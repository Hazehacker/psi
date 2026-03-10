# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 主动问询实施细节

在我与你沟通并要求你具体实施更改时，难免会遇到很多模糊不清的事情。

请你深度思考这些`遗漏点`，`缺漏点`，和`冲突相悖点`，**并主动的向我问询这些你不清楚的实施细节**。

我会与你共同补充细化实现细节。我们先迭代出一轮完整完善的实施清单，然后再由你亲自落实实施下去。

## 代码/编码格式要求

### 1. markdown 文档的 table 编写格式

每当你在 markdown 文档内编写表格时，表格的格式一定是**居中对齐**的，必须满足**居中对齐**的格式要求。

### 2. markdown 文档的 vue 组件代码片段编写格式

错误写法：

1. 代码块语言用 vue，且不带有 `<template>` 标签来包裹。

```vue
<wd-popup v-model="showModal">
  <wd-cell-group>
    <!-- 内容 -->
  </wd-cell-group>
</wd-popup>
```

2. 代码块语言用 html。

```html
<wd-popup v-model="showModal">
  <wd-cell-group>
    <!-- 内容 -->
  </wd-cell-group>
</wd-popup>
```

正确写法：代码块语言用 vue ，且带有 `<template>` 标签来包裹。

```vue
<template>
  <wd-popup v-model="showModal">
    <wd-cell-group>
      <!-- 内容 -->
    </wd-cell-group>
  </wd-popup>
</template>
```

### 3. javascript / typescript 的代码注释写法

代码注释写法应该写成 jsdoc 格式。而不是单纯的双斜杠注释。比如：

不合适的双斜线注释写法如下：

```ts
// 模拟成功响应
export function successResponse<T>(data: T, message: string = '操作成功') {
  return {
    success: true,
    code: ResultEnum.Success,
    message,
    data,
    timestamp: Date.now()
  }
}
```

合适的，满足期望的 jsdoc 注释写法如下：

```ts
/** 模拟成功响应 */
export function successResponse<T>(data: T, message: string = '操作成功') {
  return {
    success: true,
    code: ResultEnum.Success,
    message,
    data,
    timestamp: Date.now()
  }
}
```

### 4. markdown 的多级标题要主动提供序号

对于每一份 markdown 文件的`二级标题`和`三级标题`，你都应该要：

1. 主动添加**数字**序号，便于我阅读文档。
2. 主动**维护正确的数字序号顺序**。如果你处理的 markdown 文档，其手动添加的序号顺序不对，请你及时的更新序号顺序。

## 报告编写规范

在大多数情况下，你的更改是**不需要**编写任何说明报告的。但是每当你需要编写报告时，请你首先遵循以下要求：

- 报告地址： 默认在 `docs\reports` 文件夹内编写报告。
- 报告文件格式： `*.md` 通常是 markdown 文件格式。
- 报告文件名称命名要求：
  1. 前缀以日期命名。包括年月日。日期格式 `YYYY-MM-DD` 。
  2. 用小写英文加短横杠的方式命名。
- 报告语言： 默认用简体中文。

## 项目概述

这是一个基于 Vue 3 + TypeScript + Vite 构建的进销存管理系统(PSI - Purchase, Sales, Inventory)前端项目。项目使用 Element Plus 作为 UI 组件库,采用 Pinia 进行状态管理,Vue Router 进行路由管理。

## 常用命令

### 开发和构建

```bash
# 安装依赖 (使用 pnpm)
pnpm install

# 启动开发服务器 (运行在 http://localhost:3000)
pnpm dev

# 类型检查
pnpm type-check

# 仅构建 (不进行类型检查)
pnpm build-only

# 完整构建 (包含类型检查)
pnpm build

# 预览构建产物
pnpm preview
```

### 代码质量

```bash
# 运行 ESLint 并自动修复
pnpm lint

# 格式化代码 (使用 Prettier)
pnpm format

# 运行单元测试
pnpm test:unit
```

### API 代码生成

```bash
# 从 OpenAPI 规范生成 API 客户端代码
pnpm gen:api
```

注意: API 生成使用 `openapi-ts-request` 工具,配置文件为 `openapi-ts-request.config.ts`。生成前需要在 Apifox 中导出 OpenAPI URL,并更新配置文件中的 `schemaPath`。

## 核心架构

### 1. HTTP 客户端架构

项目实现了自定义的 HTTP 客户端封装,位于 `src/apis/http.ts` 和 `src/plugins/http.ts`:

- **HttpClient 类**: 封装 Axios,提供类型安全的 HTTP 请求方法
- **拦截器机制**:
  - 请求拦截器: 自动添加 Authorization header (Bearer token)
  - 响应拦截器: 统一处理业务状态码 (code)、token 刷新、错误提示
- **数据上传类型** (`DataUpType`):
  - `form`: application/x-www-form-urlencoded
  - `json`: application/json (默认)
  - `file`: multipart/form-data
  - `stream`: application/octet-stream
- **组合式函数**: 在组件中使用 `useHttp()` 获取 HTTP 客户端实例

### 2. 认证和授权

- **Token 管理**: 使用 JWT token + refreshToken 双 token 机制
- **自动刷新**: Token 过期时自动调用 `/login/refresh-token` 接口刷新
- **路由守卫**: `src/router/index.ts` 中实现全局前置守卫
  - 白名单页面: Login, NotFound, Forbidden, Error
  - 已登录用户首次访问时加载用户信息
  - 未登录用户自动跳转到登录页

### 3. 状态管理 (Pinia Stores)

主要的 Store 位于 `src/stores/`:

- **user.ts**: 用户状态、token、菜单数据 (当前使用静态菜单配置)
- **tab.ts**: 标签页管理 (多标签页导航)
- **common.ts**: 通用状态
- **purchase.ts**: 采购相关状态
- **logistics.ts**: 物流相关状态
- **operationLogs.ts**: 操作日志状态

### 4. 路由架构

路由采用模块化组织方式:

```
src/router/
├── index.ts          # 主路由入口,使用 import.meta.glob 动态加载子模块
├── login/            # 登录模块路由
├── main/             # 主应用路由
│   ├── purchase/     # 采购管理
│   ├── sales/        # 销售管理
│   ├── warehouse/    # 仓库管理
│   ├── finance/      # 资金管理
│   ├── report/       # 报表管理
│   ├── sysmanage/    # 系统配置
│   ├── sysparams/    # 系统参数
│   └── basicdata/    # 基础资料
└── sample/           # 示例模块 (仅开发环境或启用 VITE_ENABLE_SAMPLE 时加载)
```

**标签页集成**: 路由 meta 中可设置 `label` 属性,用于在顶部标签栏显示。路由守卫会自动管理标签页的添加和切换。

### 5. API 调用模式

项目使用两种 API 组织方式:

1. **手动编写** (`src/apis/`): 业务 API 按模块组织,每个模块包含 `index.ts` (接口函数) 和 `type.ts` (类型定义)
2. **自动生成** (`src/apis/openapi-apifox/`): 使用 `openapi-ts-request` 从 Apifox 生成

API 调用示例:

```typescript
// 在组件中
import { useHttp, DataUpType } from '@/plugins/http'

const http = useHttp()

// GET 请求
const response = await http.get<UserInfo>('/login/current-user')

// POST 请求 (JSON)
const response = await http.post<Result>('/api/endpoint', data)

// POST 请求 (表单)
const response = await http.post<Result>('/api/endpoint', data, {
  upType: DataUpType.form
})

// 文件上传
const response = await http.postWithFile<Result>('/api/upload', {
  file: fileObject,
  ...otherData
})
```

### 6. 响应数据格式

后端统一响应格式 `JsonVO<T>`:

```typescript
{
  code: number      // 状态码 (10000=成功, 401=未授权, 403=禁止, 404=未找到等)
  message: string   // 提示信息
  data?: T          // 数据对象 (可选)
}
```

HTTP 插件会自动处理 `code`,在 `code !== 10000` 时显示错误信息或执行特殊逻辑(如 token 刷新)。

### 7. 自动导入配置

项目使用 `unplugin-auto-import` 和 `unplugin-vue-components` 实现:

- Element Plus 组件自动导入
- Element Plus 图标自动导入
- 无需手动注册常用组件

### 8. 环境配置

环境变量文件:

- `.env`: 全局配置
- `.env.development`: 开发环境
- `.env.production`: 生产环境
- `.env.utest`: 单元测试环境

关键环境变量:

- `VITE_API_URL`: API 基础路径 (默认 `/api`)
- `VITE_CAPTCHA_URL`: 验证码路径 (默认 `/captcha`)
- `VITE_ENABLE_SAMPLE`: 是否启用示例模块

开发环境代理配置在 `vite.config.ts` 中:

- `/api` -> `http://39.103.62.65:10001`
- `/captcha` -> `http://39.103.62.65:10001`

### 9. 菜单系统

当前使用静态菜单配置 (在 `src/stores/user.ts` 的 `menus` 状态中)。菜单结构支持多级嵌套,包含以下属性:

- `id`: 唯一标识
- `text`: 菜单文本
- `icon`: 图标名称 (Element Plus Icon)
- `href`: 路由路径
- `children`: 子菜单
- `hasReport`: 是否有报表功能
- `hideReport`: 是否隐藏报表入口

主要菜单模块:

- 系统配置 (字典管理、导入模板、菜单管理)
- 系统参数 (辅助资料、基础资料、高级设置)
- 采购管理 (采购订单、采购单、采购退货单)
- 销售管理 (销售订单、销售单、销售退货单)
- 仓库管理 (库存查询、批次查询、盘点等)
- 资金管理 (收款单、付款单、核销单、转账单等)
- 报表管理 (采购报表、销售报表、仓库报表、资金报表)

## 重要约定

### 文件命名

- Vue 组件文件名使用大写字母开头 (PascalCase)
- 其他 TypeScript 文件使用小写字母和连字符 (kebab-case) 或 camelCase

### 路径别名

- `@/` 映射到 `src/` 目录

### 代码组织

- 公用组件放在 `src/components/`
- 页面组件放在 `src/views/`
- 工具函数放在 `src/utils/`
- 类型定义放在 `src/types/` 或各模块的 `type.ts` 文件中

### Git 分支策略

- 主分支: `master`
- 当前开发分支: `f1-ruancat`
- 创建 PR 时默认目标分支为 `master`

## 第三方库集成

- **表单设计器**: `@form-create/designer` + `@form-create/element-ui`
- **流程图**: `@logicflow/core` + `@logicflow/extension`
- **富文本编辑器**: `@wangeditor/editor` + `@wangeditor/editor-for-vue`
- **数据表格**: `ag-grid-community` + `ag-grid-vue3`
- **图表**: `echarts`
- **Excel**: `xlsx`
- **打印**: `vue-plugin-hiprint`
- **PDF**: `pdfobject`
- **拼音**: `pinyin-pro`

## 开发注意事项

1. **API 接口联调**: 当前网关地址配置在 `vite.config.ts` 的 proxy 配置中,联调时需要确认地址正确
2. **示例模块**: 开发环境默认加载 `src/router/sample/` 下的示例路由,生产环境不加载
3. **TypeScript 严格模式**: 项目启用 TypeScript 严格类型检查,提交前运行 `pnpm type-check`
4. **请求加载状态**: HTTP 请求可通过 `showLoading: true` 配置项显示全屏加载动画
5. **响应错误处理**: HTTP 插件已统一处理常见错误,业务代码通常只需处理成功场景
