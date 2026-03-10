Common-Bai 组件库
一、概述
Common-Bai 是一个基于 Vue 3 + TypeScript + Element Plus 的表格组件库，提供了带有列筛选功能的增强型数据表格组件。

二、组件说明
DataTable 表格组件
模拟了一个功能完整的表格组件，内置列筛选功能，支持动态显示/隐藏表格列。

ColumnFilterDialog 列筛选弹窗组件
独立的列筛选弹窗组件，用于管理表格列的显示状态。

三、安装和使用
1. 引入组件
将 common-bai 文件夹放入您的项目中，然后在需要使用的页面中引入：
//vue
<template>
  <div class="page-container">
    <DataTable 
      :data="tableData"
      :columns="recordColumns"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import DataTable, { type TableColumn } from './common-bai/DataTable.vue';

// 定义列配置
const recordColumns: TableColumn[] = [
  { title: '苹果', key: 'apple', width: 100 },
  { title: '芒果', key: 'mango', width: 100 },
  { title: '火龙果', key: 'pitaya', width: 100 },
  { title: '西瓜', key: 'watermelon', width: 100 },
];

// 定义表格数据
const tableData = ref([
  { apple: '红富士', mango: '贵妃芒', pitaya: '红心', watermelon: '8424' },
  { apple: '嘎啦', mango: '金煌芒', pitaya: '白心', watermelon: '麒麟' },
  { apple: '阿克苏', mango: '凯特芒', pitaya: '红心', watermelon: '甜王' },
]);
</script>

四、API 文档
DataTable 组件 Props
属性名	             类型	         必填	    默认值	       说明
data	         TableData[]	     是	         -	      表格数据数组
columns	         ableColumn[]	     是	         -	       列配置数组

TableColumn 类型定义
//typescript
interface TableColumn {
  title: string;    // 列标题
  key: string;      // 列对应的数据键
  width: number;    // 列宽度
}

TableData 类型定义
//typescript
interface TableData {
  [key: string]: any;  // 动态数据字段，根据实际列配置而定
}

五、功能特性
1. 基础表格功能
支持数据绑定和显示
支持自定义列宽
支持文本溢出提示
内置加载状态
2. 列筛选功能
点击"列筛选"按钮打开筛选弹窗
多选列显示/隐藏
选择状态与表格列实时同步
至少保留一列显示
3. 用户体验
弹窗选择状态记忆
响应式设计
友好的交互反馈

六、使用示例
1.基础用法
vue
<template>
  <DataTable 
    :data="tableData"
    :columns="columns"
  />
</template>
<script setup>
import { ref } from 'vue';
import DataTable from './common-bai/DataTable.vue';
const columns = [
  { title: '姓名', key: 'name', width: 100 },
  { title: '年龄', key: 'age', width: 80 },
  { title: '城市', key: 'city', width: 120 },
];
const tableData = ref([
  { name: '张三', age: 25, city: '北京' },
  { name: '李四', age: 30, city: '上海' },
  { name: '王五', age: 28, city: '广州' },
]);
</script>

2.自定义列配置
vue
<script setup>
const productColumns = [
  { title: '产品名称', key: 'productName', width: 150 },
  { title: '价格', key: 'price', width: 100 },
  { title: '库存', key: 'stock', width: 80 },
  { title: '分类', key: 'category', width: 120 },
  { title: '创建时间', key: 'createTime', width: 180 },
];
const productData = ref([
  { productName: 'iPhone 14', price: 5999, stock: 100, category: '手机', createTime: '2023-01-01' },
  { productName: 'MacBook Pro', price: 12999, stock: 50, category: '电脑', createTime: '2023-01-02' },
]);
</script>

3.样式定制
组件支持通过 CSS 变量和类名进行样式定制：
css
/* 自定义表格样式 */
.data-table-container {
  --table-border-color: #e0e0e0;
  --table-header-bg: #f5f7fa;
}
/* 自定义按钮样式 */
.filter-btn {
  background-color: #409eff;
  color: white;
}
七、注意事项
1.依赖要求
Vue 3.x
Element Plus
TypeScript（推荐）
2.数据格式
确保 data 中的字段与 columns 中的 key 对应
列配置中的 key 必须唯一
浏览器兼容性
支持现代浏览器（Chrome、Firefox、Safari、Edge）