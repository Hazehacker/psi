# TypeScript 类型检查报告

## 执行信息

- **执行时间**: 2025-11-06
- **执行命令**: `vue-tsc --build`
- **项目路径**: `D:\code\01s\202510-12psi\yunxiao-code\zero-one-12psi\psi-frontend`

## 错误统计

- **总错误数**: 403 个

## 错误分类汇总

### 1. 类型缺失/导出错误 (约 20 个)

涉及到模块导出成员缺失的问题。

#### 主要问题文件

- `src/apis/type.ts` - 缺少 `PageResult`, `ApiResponse` 导出
- `src/apis/sysmanage/dictionary/type.ts` - 缺少多个类型导出
- `src/apis/warehouse/inventory/type.ts` - 缺少 `DetailRequest` 导出

### 2. 类型不匹配错误 (约 150 个)

包括属性类型不匹配、可选/必填不匹配等。

#### 典型问题

- `JsonVO<any>` 与 `ApiResponse<T>` 类型不兼容
- 字符串与字符串数组类型不匹配
- 可选属性与必填属性冲突
- `undefined` 与具体类型不匹配

### 3. 隐式 any 类型错误 (约 40 个)

函数参数或变量缺少显式类型声明。

#### 主要问题

- 回调函数参数未声明类型
- 变量类型推断为 any
- 索引访问返回 any 类型

### 4. 空值检查错误 (约 30 个)

可能为 null 或 undefined 的值未进行检查。

#### 典型场景

- ref 对象可能为 null
- 可选属性未进行空值检查
- Date 类型可能为 undefined

### 5. 联合类型比较错误 (约 15 个)

类型联合之间没有重叠导致的比较错误。

#### 问题示例

```typescript
// dialogMode 类型为 'add' | 'edit'
if (dialogMode.value === 'view') // 错误：类型没有重叠
```

### 6. 文件名大小写不一致 (2 个)

Windows 系统上的路径大小写问题。

#### 问题文件

- `src/components/NormalTable/type.ts` vs `src/components/normaltable/type.ts`

### 7. 索引签名错误 (约 30 个)

对象上不存在索引签名导致动态属性访问失败。

### 8. 其他类型错误 (约 116 个)

包括接口属性不匹配、泛型约束错误等。

---

## 详细错误列表

### APIs 模块错误

#### src/apis/basicdata/customer.ts

```
Line 2:15 - error TS2305: Module '"@/apis/type"' has no exported member 'PageResult'.
Line 3:15 - error TS2305: Module '"@/apis/type"' has no exported member 'ApiResponse'.
Line 91:23 - error TS2339: Property 'size' does not exist on type 'CustomerQuery'.
Line 92:26 - error TS2339: Property 'current' does not exist on type 'CustomerQuery'.
```

#### src/apis/basicdata/product.ts

```
Line 2:15 - error TS2305: Module '"@/apis/type"' has no exported member 'PageResult'.
Line 3:15 - error TS2305: Module '"@/apis/type"' has no exported member 'ApiResponse'.
Line 93:23 - error TS2339: Property 'size' does not exist on type 'ProductQuery'.
Line 94:26 - error TS2339: Property 'current' does not exist on type 'ProductQuery'.
```

#### src/apis/basicdata/supplier.ts

```
Line 2:15 - error TS2305: Module '"@/apis/type"' has no exported member 'PageResult'.
Line 64:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<PageResult<Supplier>>'.
Line 104:23 - error TS2339: Property 'size' does not exist on type 'SupplierQuery'.
Line 105:26 - error TS2339: Property 'current' does not exist on type 'SupplierQuery'.
Line 120:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<Supplier>'.
Line 154:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 174:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 195:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
```

#### src/apis/basicdata/warehouse.ts

```
Line 2:15 - error TS2305: Module '"@/apis/type"' has no exported member 'PageResult'.
Line 45:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<PageResult<Warehouse>>'.
Line 79:23 - error TS2339: Property 'size' does not exist on type 'WarehouseQuery'.
Line 80:26 - error TS2339: Property 'current' does not exist on type 'WarehouseQuery'.
Line 95:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<Warehouse>'.
Line 125:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 145:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 165:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 185:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<string>'.
Line 205:7 - error TS2322: Type 'JsonVO<any>' is not assignable to type 'ApiResponse<{ id: string; name: string; }[]>'.
```

#### src/apis/common/api.ts

```
Line 113:15 - error TS2353: Object literal may only specify known properties, and 'code' does not exist in type 'TreeNode'.
Line 120:15 - error TS2353: Object literal may only specify known properties, and 'code' does not exist in type 'TreeNode'.
Line 136:15 - error TS2353: Object literal may only specify known properties, and 'code' does not exist in type 'TreeNode'.
Line 143:15 - error TS2353: Object literal may only specify known properties, and 'code' does not exist in type 'TreeNode'.
```

#### src/apis/sysmanage/dictionary/index.ts

```
Line 161:30 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
Line 166:31 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
Line 322:30 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
```

#### src/apis/sysmanage/import/index.ts

```
Line 99:38 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
Line 104:38 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
```

#### src/apis/warehouse/inventory/index.ts

```
Line 19:3 - error TS2305: Module '"./type"' has no exported member 'DetailRequest'.
```

---

### Components 模块错误

#### src/components/lib/NodTree.vue

```
Line 17:5 - error TS2322: Type 'unknown[]' is not assignable to type 'TreeNodeData[]'.
Line 20:5 - error TS2322: Type 'null' is not assignable to type 'string | number | undefined'.
Line 85:4 - error TS2322: Type 'any[]' is not assignable to type 'never[]'.
Line 88:4 - error TS2322: Type '{}' is not assignable to type 'null'.
Line 94:42 - error TS7006: Parameter 'obj' implicitly has an 'any' type.
Line 96:25 - error TS2345: Argument of type 'any' is not assignable to parameter of type 'never'.
Line 108:41 - error TS7006: Parameter 'obj' implicitly has an 'any' type.
Line 117:18 - error TS7006: Parameter 'data' implicitly has an 'any' type.
Line 119:20 - error TS18047: 'treeRef.value' is possibly 'null'.
Line 124:3 - error TS18047: 'nodTreeRef.value' is possibly 'null'.
Line 133:3 - error TS18047: 'treeRef.value' is possibly 'null'.
Line 136:3 - error TS18047: 'treeRef.value' is possibly 'null'.
Line 139:2 - error TS18047: 'nodTreeRef.value' is possibly 'null'.
Line 143:21 - error TS7006: Parameter 'e' implicitly has an 'any' type.
Line 145:6 - error TS18047: 'nodTreeRef.value' is possibly 'null'.
Line 154:4 - error TS2322: Type 'unknown' is not assignable to type 'never[]'.
Line 155:19 - error TS2339: Property 'setCheckedKeys' does not exist on type 'never'.
Line 159:4 - error TS2322: Type '{}' is not assignable to type 'null'.
Line 160:19 - error TS2339: Property 'setCurrentKey' does not exist on type 'never'.
```

#### src/components/myform/props/upload.ts

```
Line 7:18 - error TS2430: Interface 'MyFormUploadProps' incorrectly extends interface 'Partial<UploadProps>'.
  Types of property 'onExceed' are incompatible.
```

#### src/components/normaltable/NormalTable.vue

```
Line 339:24 - error TS7006: Parameter 'row' implicitly has an 'any' type.
Line 339:29 - error TS7006: Parameter 'expanded' implicitly has an 'any' type.
Line 342:19 - error TS7006: Parameter 'row' implicitly has an 'any' type.
```

---

### Router 模块错误

#### src/router/main/basicdata/index.ts

```
Line 3:7 - error TS2741: Property 'default' is missing in type but required in type 'RouteRecordRawArray'.
```

---

### Views 模块错误

#### src/views/basicdata/account/AccountList.vue

```
Line 443:15 - error TS2769: No overload matches this call. addEventListener 类型不匹配
Line 450:13 - error TS2769: No overload matches this call. addEventListener 类型不匹配
```

#### src/views/basicdata/customer/CustomerList.vue

```
Line 231:17 - error TS2322: Type 'string' is not assignable to type 'number'.
Line 247:18 - error TS2739: Type missing properties 'pageIndex', 'pageSize'.
Line 437:5 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 447:5 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 457:5 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 467:5 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 477:5 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 598:3 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 599:3 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 905:7 - error TS2322: Type 'boolean | undefined' is not assignable to type 'boolean'.
Line 938:7 - error TS2322: Property 'isMain' type incompatible.
Line 970:7 - error TS2322: Type 'string' is not assignable to type 'string[]'.
Line 991:9 - error TS2322: Type 'string' is not assignable to type 'string[]'.
```

#### src/views/basicdata/product/ProductList.vue

```
Line 349:16 - error TS2739: Type missing properties 'pageIndex', 'pageSize'.
Line 391:16 - error TS2739: Type missing properties 'pageIndex', 'pageSize'.
Line 392:16 - error TS2322: Type incompatible with 'MyTableColumn[]'.
Line 619:35 - error TS2724: '"element-plus"' has no exported member named 'ElFilePicker'.
Line 744:7 - error TS7034: Variable 'discountTableButtons' implicitly has type 'any[]'.
Line 747:37 - error TS7006: Parameter 'index' implicitly has an 'any' type.
Line 747:44 - error TS7006: Parameter 'row' implicitly has an 'any' type.
Line 747:49 - error TS7006: Parameter 'evtName' implicitly has an 'any' type.
Line 1042:33 - error TS7006: Parameter 'command' implicitly has an 'any' type.
Line 1086:22 - error TS2339: Property 'serialProduct' does not exist.
Line 1119:42 - error TS2345: Argument of type 'string' is not assignable to parameter of type 'never'.
```

#### src/views/basicdata/supplier/SupplierList.vue

```
Line 59:20 - error TS2322: Type incompatible with CascaderValue handler.
Line 109:14 - error TS2322: Type incompatible with 'MyTableOperationsBtn[]'.
Line 135:87 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 139:87 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 151:28 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 166:28 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 199:92 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 206:101 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 212:86 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 216:89 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 222:89 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 226:88 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 241:18 - error TS2739: Type missing properties 'pageIndex', 'pageSize'.
Line 243:18 - error TS2322: Type incompatible with 'MyTableOperationsBtn[]'.
Line 259:36 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 274:28 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 582:3 - error TS2322: Type 'string[]' is not assignable to type 'never[]'.
Line 596:15 - error TS2769: addEventListener type mismatch.
Line 603:13 - error TS2769: addEventListener type mismatch.
Line 694:27 - error TS2367: Comparison 'add' | 'edit' vs 'view' has no overlap.
Line 744:103 - error TS2345: Argument of type 'string' is not assignable to parameter of type 'never'.
Line 881:3 - error TS2322: Type '"view"' is not assignable to type '"add" | "edit"'.
Line 1004:7 - error TS2322: Type 'boolean | undefined' is not assignable to type 'boolean'.
Line 1037:7 - error TS2322: Property 'isMain' type incompatible.
```

#### src/views/basicdata/warehouse/WarehouseList.vue

```
Line 402:108 - error TS2345: Argument of type 'string' is not assignable to parameter of type 'never'.
```

#### src/views/demo/SearchComponentDemo.vue

```
Line 253:5 - error TS2322: Type '{ ... }[]' is not assignable to type 'never[]'.
Line 277:5 - error TS2322: Type '{ ... }[]' is not assignable to type 'never[]'.
Line 302:5 - error TS2322: Type '{ ... }[]' is not assignable to type 'never[]'.
Line 327:5 - error TS2322: Type '{ ... }[]' is not assignable to type 'never[]'.
```

#### src/views/purchase/return/PurchaseReturnDetailDialog.vue

```
Line 419:8 - error TS2345: Argument of type '"save"' is not assignable to parameter of type '"update:visible"'.
```

#### src/views/sales/order/SalesOrder.vue

```
Line 1058:20 - error TS2339: Property 'supplier' does not exist.
Line 1124:18 - error TS2339: Property 'supplier' does not exist.
Line 1436:45 - error TS2339: Property 'supplier' does not exist.
Line 1464:18 - error TS2339: Property 'supplier' does not exist.
Line 1588:45 - error TS2339: Property 'supplier' does not exist.
Line 1608:43 - error TS2339: Property 'supplier' does not exist.
```

#### src/views/sales/return/SalesReturn.vue

```
Line 1042:20 - error TS2339: Property 'supplier' does not exist.
Line 1112:18 - error TS2339: Property 'supplier' does not exist.
Line 1425:45 - error TS2339: Property 'supplier' does not exist.
Line 1453:18 - error TS2339: Property 'supplier' does not exist.
```

#### src/views/sample/normalTable/Index.vue

```
Line 12:10 - error TS2322: Type incompatible with 'MyTableColumn[]'.
```

#### src/views/sample/normalTable/TreeTableExample.vue

```
Line 7:8 - error TS2322: Type incompatible with 'MyTableColumn[]'.
Line 77:24 - error TS7031: Binding element 'row' implicitly has an 'any' type.
Line 155:5 - error TS2322: Type incompatible with PageDTO.
```

#### src/views/sysmanage/dictionary/dictionary/CreateDialog.vue

```
Line 16:37 - error TS2339: Property 'name' does not exist on type 'FormData'.
Line 21:29 - error TS2339: Property 'type_name' does not exist on type 'FormData'.
Line 36:37 - error TS2339: Property 'value' does not exist on type 'FormData'.
Line 41:29 - error TS2339: Property 'remark' does not exist on type 'FormData'.
Line 43:11 - error TS2322: Type 'string' is not assignable to type 'number'.
Line 63:15 - error TS2305: Module has no exported member 'Request'.
```

#### src/views/sysmanage/dictionary/dictionary/Dictionary.vue

```
Line 57:8 - error TS2305: Module has no exported member 'Request'.
Line 59:15 - error TS2305: Module has no exported member 'DictionaryItem'.
Line 59:31 - error TS2305: Module has no exported member 'DictionaryType'.
Line 73:3 - error TS2353: 'remark' does not exist in type 'SearchForm'.
Line 114:11 - error TS7053: Element implicitly has an 'any' type.
Line 120:9 - error TS18046: 'result' is of type 'unknown'.
... (多个 result 类型为 unknown 的错误)
```

#### src/views/sysmanage/dictionary/type/CreateDialog.vue

```
Line 17:29 - error TS2339: Property 'name' does not exist on type 'FormData'.
Line 27:29 - error TS2339: Property 'code' does not exist on type 'FormData'.
Line 37:29 - error TS2339: Property 'remark' does not exist on type 'FormData'.
Line 39:11 - error TS2322: Type 'string' is not assignable to type 'number'.
Line 61:15 - error TS2305: Module has no exported member 'Request'.
Line 134:26 - error TS2304: Cannot find name 'mode'.
Line 135:26 - error TS2339: Property 'id' does not exist on type 'FormData'.
Line 135:47 - error TS2339: Property 'remark' does not exist on type 'FormData'.
```

#### src/views/sysmanage/dictionary/type/DictionaryType.vue

```
Line 56:8 - error TS2459: Module declares 'TypeRequest' locally, but it is not exported.
Line 58:15 - error TS2305: Module has no exported member '字典类型查询数据对象'.
Line 114:11 - error TS7053: Element implicitly has an 'any' type.
Line 119:48 - error TS2345: Type incompatible with 'TypePageRequest'.
Line 120:9 - error TS18046: 'result' is of type 'unknown'.
... (多个 result 类型为 unknown 的错误)
```

#### src/views/sysmanage/import/CreateDialog.vue

```
Line 52:61 - error TS2322: Type 'string' is not assignable to type 'number'.
```

#### src/views/sysmanage/import/DetailDialog.vue

```
Line 18:31 - error TS2345: Argument of type 'string | undefined' is not assignable to parameter of type 'string'.
Line 23:32 - error TS2345: Argument of type 'number | undefined' is not assignable to parameter of type 'number'.
Line 123:9 - error TS18046: 'result' is of type 'unknown'.
Line 133:18 - error TS18046: 'result' is of type 'unknown'.
```

#### src/views/sysmanage/import/Import.vue

```
Line 110:3 - error TS2322: Type 'null' is not assignable to type 'string | undefined'.
Line 111:3 - error TS2322: Type 'null' is not assignable to type 'number | undefined'.
Line 161:11 - error TS7053: Element implicitly has an 'any' type.
Line 167:9 - error TS18046: 'result' is of type 'unknown'.
... (多个索引访问和 result 类型错误)
```

#### src/views/sysmanage/menu/Menu.vue

```
Line 81:27 - error TS2345: Argument of type 'number | undefined' is not assignable to parameter of type 'number'.
Line 87:7 - error TS7023: 'getChildren' implicitly has return type 'any'.
Line 88:63 - error TS7024: Function implicitly has return type 'any'.
Line 91:27 - error TS2345: Argument of type 'number | undefined' is not assignable to parameter of type 'number'.
```

#### src/views/warehouse/batch/DetailDialog.vue

```
Line 165:29 - error TS2345: Argument of type 'Date | undefined' is not assignable to parameter of type 'Date | null'.
Line 368:45 - error TS1149: File name differs from already included file name only in casing.
```

#### src/views/warehouse/batch/Index.vue

```
Line 36:44 - error TS2339: Property 'key' does not exist on type 'never'.
Line 36:64 - error TS2339: Property 'name' does not exist on type 'never'.
Line 61:55 - error TS2339: Property 'key' does not exist on type 'never'.
Line 208:9 - error TS7053: Element implicitly has an 'any' type.
Line 284:9 - error TS7053: Element implicitly has an 'any' type.
Line 347:3 - error TS2322: Type 'any[]' is not assignable to type 'never[]'.
```

#### src/views/warehouse/check/Index.vue

```
Line 238:15 - error TS2614: Module has no exported member 'SearchForm'.
Line 295:9 - error TS7053: Element implicitly has an 'any' type.
Line 331:13 - error TS7053: Element implicitly has an 'any' type.
```

#### src/views/warehouse/inventory/DetailDialog.vue

```
Line 152:29 - error TS2345: Argument of type 'Date | undefined' is not assignable to parameter of type 'Date | null'.
Line 353:45 - error TS1149: File name differs from already included file name only in casing.
```

#### src/views/warehouse/inventory/Index.vue

```
Line 36:38 - error TS2339: Property 'warehouseId' does not exist on type 'never'.
Line 37:26 - error TS2339: Property 'name' does not exist on type 'never'.
Line 114:15 - error TS2614: Module has no exported member 'SearchForm'.
Line 180:9 - error TS7053: Element implicitly has an 'any' type.
Line 208:13 - error TS7053: Element implicitly has an 'any' type.
Line 226:7 - error TS18048: 'tablePageData.rows' is possibly 'undefined'.
Line 360:3 - error TS2322: Type 'any[]' is not assignable to type 'never[]'.
```

---

## 建议修复优先级

### P0 - 高优先级（阻塞构建）

1. **修复核心类型导出**

   - 在 `src/apis/type.ts` 中导出 `PageResult` 和 `ApiResponse`
   - 修复字典和仓库模块的类型导出问题

2. **修复文件名大小写不一致**
   - 统一 `src/components/normaltable/` 目录的引用大小写

### P1 - 中优先级（类型安全）

3. **修复 API 响应类型不匹配**

   - 统一 `JsonVO` 和 `ApiResponse` 的使用
   - 确保 data 字段的可选性一致

4. **修复查询对象属性缺失**
   - 为各个 Query 接口添加 `size` 和 `current` 属性

### P2 - 低优先级（代码质量）

5. **添加显式类型声明**

   - 为回调函数参数添加类型
   - 为变量添加显式类型声明

6. **添加空值检查**

   - 为 ref 对象添加空值检查
   - 为可选属性添加空值保护

7. **修复联合类型问题**
   - 在 SupplierList 等组件中添加 'view' 到 dialogMode 联合类型

---

## 附录：原始日志

原始日志已保存至: `docs/reports/type-check-output.log`

---

**报告生成时间**: 2025-11-06
**生成工具**: Claude Code
