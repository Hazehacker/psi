<template>
    <!-- 仓库选择显示组件 -->
    <div class="depot">
		<!-- 搜索按钮：仅当有商品数据时显示 -->
		<i v-if="hasDataComputed" class="search el-icon-search" @click.stop="openDialog"></i>
		<!-- 显示仓库名称 -->
		<span class="agGridPopup">{{ value }}</span>
	</div>
</template>
<script setup>
	/**
	 * 仓库选择显示组件 (Vue 3 Composition API)
	 * 功能：在 ag-Grid 中显示仓库信息，并提供仓库选择功能
	 * 特性：
	 * - 显示当前选中的仓库名称
	 * - 提供仓库选择弹窗（需要集成 window.openStockDialog）
	 * - 仅对商品类型为0的数据显示选择按钮
	 * - 支持库存查询条件传递
	 */
	import { ref, computed } from 'vue'
	import { ElMessage } from 'element-plus'

	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})

	// 响应式数据
	const value = ref('') // 仓库名称显示值

	// 计算属性：判断是否显示搜索按钮
	const hasDataComputed = computed(() => {
		const data = props.params.node.data;
		if (data.key == null) return false;
		return data.goodsType == 0; // 仅商品类型为0时显示
	})

	// 初始化显示值
	if (hasDataComputed.value) {
		value.value = props.params.value
	}

	/**
	 * 仓库选择回调
	 * 当用户在仓库选择弹窗中选择仓库时调用
	 * @param {Object} row - 选中的仓库数据
	 */
	function rowPicked(row){
		const data = props.params.node.data
		const dispose = props.params.dispose
		const compId = props.params.eGridCell?.getAttribute?.('comp-id') || ''
		
		// 更新仓库ID
		data[dispose.key] = row.warehouse
		
		// 更新表格显示数据
		if (typeof window !== 'undefined' && window.$grid && window.$grid.updateGridData) {
			window.$grid.updateGridData(props.params, data.uniqid, dispose.text, row.name)
		}
		
		// 将焦点移回单元格
		const el = document.querySelector(".ag-cell[comp-id='"+compId+"']")
		if (el) el.focus()
	}

	/**
	 * 打开仓库选择弹窗
	 * 检查前置条件并打开仓库选择对话框
	 */
	function openDialog(){
		const data = props.params.node.data
		// 构建查询条件：商品ID和属性
		const condition = { goods: data.key, attr: data.attr }
		
		// 检查是否已集成仓库选择弹窗
		if (typeof window !== 'undefined' && typeof window.openStockDialog === 'function') {
			window.openStockDialog(condition, rowPicked)
		} else {
			ElMessage({ type:'info', message:'未集成仓库选择弹窗，请实现 window.openStockDialog' })
		}
	}
</script>
<style scoped>
	/* 组件容器样式 */
	.depot{
		position: relative;
		height: 100%;
	}
	/* 搜索按钮样式 */
	.search{
		position: absolute;
		left: 0;
		top: 6px;
		width: 16px;
		height: 16px;
		line-height: 16px;
	}
</style>