# 杂项提示词

## 01 制作满足 requestOpenApiTsRequestShim 的 CustomRequestOptions 类型

我正在使用 openapi-ts-request.config.ts 来自定义包装一个接口请求工具。

- 仓库： https://github.com/openapi-ui/openapi-ts-request

我简单模仿了别的请求库，得知为了让接口请求能够和 `openapi-ts-request` 生成的接口相适配，需要自己写一个垫片，做好接口请求的转换写法。即 `src\apis\openapi-ts-request-shim.ts` 文件。

请你阅读 `openapi-ts-request-shim.ts` 和本项目已经有的接口请求工具和类型：

- src\apis\http.ts
- src\plugins\http.ts
- src\apis\type.ts

请你在 `openapi-ts-request-shim.ts` 内编写 `CustomRequestOptions` 类型，实现对本项目接口请求工具的类型兼容，实现 requestOpenApiTsRequestShim 函数可以二次包装本项目的接口请求工具，进而对接生成 openapi-ts-request 工具生成的一些列接口。

## 02 为 `src\components\ReIcon` 组件编写使用用例

1. 阅读 `src\views\sample\re-icon-iconify-test\index.vue` 代码使用案例。
2. 适当的阅读 `.claude\agents\migrate-iconify.md` 文档，重点以代码使用，组件使用为方向去阅读文档。
3. 完整阅读 `src\components\ReIcon` 文件夹的代码，明确清楚那些内容可以被复用使用。
4. 模仿 `src\components\normaltable\README.md` ，增加 `src\components\ReIcon\README.md` 文档。为 `src\components\ReIcon` 组件编写使用文档。要求如下：
   - 完整说明有哪些组件和函数可以使用。
   - 给出经可能充足的，完整的例子。
   - 不需要你提供如何接入的教程，只需要说明如何使用。
   - 必须以相对路径的方式导入这些组件和工具，文档必须说明清楚。
   - 文档最后将 `.claude\agents\migrate-iconify.md` 文档提供的参考链接，补充到该文件的最底部。
5. 模仿 `src\components\normaltable\type.ts` 类型文件格式，增加 `src\components\ReIcon\type.ts` 文件。
6. 模仿 `src\components\operationbuttons\index.ts` ，增加 `src\components\ReIcon\index.ts` 文件。

## 03 设计一个基于前端路由的 icon 获取工具

请你阅读 `src\stores\user.ts` 的 `icon: item.icon || 'IconMenu', // 默认图标，如果后端没有提供` 逻辑，注意到从后端接口获取菜单的icon数据时，这里的默认处理逻辑是，默认赋予 `IconMenu` 这款图标。

我需要你更改逻辑，改成根据前端路由的 meta.icon 来设置默认的 icon 值。

### 你需要做的事情

1. 先帮我制作 `src\stores\router-icons\index.ts` 路由图标数据集，用于从扁平化的数组内，提取出icon。以pinia的形式存储 path-icon 映射关系的对象，便于下一个阶段使用。对外称呼为 `useRouterIcons` 路由图标数据集 。
2. 然后在 `src\router\index.ts` 内，定义完 createRouter 路由对象后，就使用 `useRouterIcons` 初始化并存储 path-icon 映射关系的对象。
3. 最后在 `src\stores\user.ts` 内，修改 `icon: item.icon || 'IconMenu'` 的逻辑，使用 `useRouterIcons` 提供的对象，基于路由的 path 路径来获取对应的 icon 值。

### 制作 `useRouterIcons` 路由图标数据集

1. 代码风格请你使用 pinia 的 组合式api 来完成，不要写成选项式的代码。
2. useRouterIcons 使用路由的 path 作为key，使用路由的 meta.icon 作为键值。如果目标路由本身没有提供任何icon，就保留空字符串即可。
3. 路由的path不需要考虑完整路径或者是短路径，直接使用扁平化路由提供给你的path即可。不做区分和考虑。

### 01 做法更改

对于 `useRouterIcons` 的使用，请你改成在 `src\router\index.ts` 的 router.beforeEach 路由全局前置守卫内，完成一次初始化。

你仅仅只需要做一次初始化，请在 `useRouterIcons` 内设计合适的变量和相关的机制，确保 `useRouterIcons` 只实现了一次存储数据初始化。

## 04 根据前端icon配置，改写静态菜单的icon取值

1. 全面阅读 `src/router/main/**/index.ts` 的路由配置文件，每个文件只阅读 icon 的取值。
2. 根据获取到的 icon 值，修改 `src\stores\user.ts` 的 STATIC_MENUS 数组。
3. 对于二级路由、一级路由这种父级路由，请你做图标替换。
   - 比如 `报表管理` 是一个一级路由，其icon为 `IconDataAnalysis` ，请你替换成 iconify 的 icon标签。
   - 比如 `采购报表` 是一个二级路由，其icon为 `IconTrendCharts` ，请你替换成 iconify 的 icon标签。
4. 做图标替换时，按照以下优先级完成替换：
   - 先阅读路由配置文件，通过比对 href 是否有对应的值，做一次替换。
   - 对于没有的对应上路由，请用已有的 icon 名称和路由标题名称，换成语义化的 iconify 标签值。
5. 图标icon的使用要求，请阅读 `docs\prompts\为路由配置增加icon图标.md` 文档。
