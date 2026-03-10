<template>
	<!-- 多单位选择编辑组件 -->
	<div class="company" ref="root" tabindex="-1" @keydown.stop.prevent="companyEvent">
		<!-- 单位选择弹出框 -->
		<el-popover popper-class="agGridSelect" trigger="manual" v-model:visible="visible">
			<template #reference>
				<!-- 显示当前选中的单位 -->
				<p>{{ value }}</p>
			</template>
			<!-- 单位列表 -->
			<ul class="list">
				<template v-for="(item,index) in list" :key="index">
					<!-- 单位选项，支持键盘导航 -->
					<li @click="choice(item.name)" :class="{hover:index==hover}">{{item.name}}</li>
				</template>
			</ul>
		</el-popover>
	</div>
</template>
<script setup>
	/**
	 * 多单位选择编辑组件 (Vue 3 Composition API)
	 * 功能：在 ag-Grid 中选择商品的多单位，支持键盘导航
	 * 特性：
	 * - 支持多单位选择
	 * - 自动计算价格和数量
	 * - 键盘导航支持（上下箭头、回车、ESC）
	 * - 单位关系计算（倍数、折扣）
	 */
	import { ref, onMounted, nextTick } from 'vue'

	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})

	// 响应式数据
	const value = ref(null)    // 当前选中的单位值
	const list = ref([])       // 单位选项列表
	const hover = ref(0)       // 当前悬停的选项索引
	const visible = ref(false) // 弹出框显示状态
	const root = ref(null)     // 根元素引用

	/**
	 * 检查是否有单位数据
	 * @returns {boolean} 是否有有效的单位数据
	 */
	function hasData() {
		const data = props.params.node.data
		if (data.key == null) return false
		return Array.isArray(data.unitData) && data.unitData.length > 0
	}

	/**
	 * 选择单位
	 * 当用户选择新单位时，自动计算价格和数量
	 * @param {string} nextValue - 新选择的单位名称
	 */
	function choice(nextValue) {
		value.value = nextValue
		const data = props.params.node.data
		
		// 获取全局工具库引用
		const lib = typeof window !== 'undefined' ? window.$lib : undefined
		const calc = typeof window !== 'undefined' ? window.$calc : undefined
		const store = typeof window !== 'undefined' ? window.$store : undefined
		
		// 计算单位关系（倍数和折扣）
		const relation = lib && lib.unitRelation ? 
			lib.unitRelation(value.value, data.unitData, undefined) : 
			{ multiple: 1, discount: 1 }
		
		// 获取金额小数位数配置
		const digit = store?.state?.sys?.fun?.digit?.money ?? 2
		
		// 计算新价格
		let newPrice
		if (calc && calc.chain) {
			// 使用链式计算器
			newPrice = calc.chain(data.unitRelation.valence)
				.multiply(relation.multiple)
				.multiply(relation.discount)
				.round(digit)
				.done()
		} else {
			// 使用原生计算
			newPrice = Number(data.unitRelation.valence) * Number(relation.multiple) * Number(relation.discount)
			newPrice = Number.isFinite(newPrice) ? Number(newPrice.toFixed(digit)) : 0
		}
		
		// 更新单位倍数
		data.unitRelation.multiple = relation.multiple
		
		// 更新表格中的价格数据
		if (typeof window !== 'undefined' && window.$grid && window.$grid.updateGridData) {
			window.$grid.updateGridData(props.params, data.uniqid, 'price', newPrice)
		}
		
		// 如果有序列数据，重新计算数量
		if (Array.isArray(data.unitData) && data.hasOwnProperty('serial') && Array.isArray(data.serial) && data.serial.length > 0) {
			let nums
			if (calc && calc.chain) {
				nums = calc.chain(data.serial.length).divide(relation.multiple).done()
			} else {
				nums = data.serial.length / (Number(relation.multiple) || 1)
			}
			// 更新表格中的数量数据
			if (typeof window !== 'undefined' && window.$grid && window.$grid.updateGridData) {
				window.$grid.updateGridData(props.params, data.uniqid, 'nums', nums)
			}
		}
		
		// 结束编辑状态
		if (root.value && root.value.parentNode) root.value.parentNode.focus()
		props.params.api.stopEditing()
	}

	/**
	 * 键盘事件处理
	 * 支持键盘导航：上下箭头选择、回车确认、ESC取消
	 * @param {KeyboardEvent} e - 键盘事件对象
	 */
	function companyEvent(e) {
		const keyCode = e.keyCode
		if (keyCode == 13) {
			// 回车键：选择当前悬停的选项
			if (list.value[hover.value]) choice(list.value[hover.value].name)
		} else if (keyCode == 27) {
			// ESC键：取消选择，关闭弹出框
			visible.value = false
			if (root.value && root.value.parentNode) root.value.parentNode.focus()
			props.params.api.stopEditing()
		} else if (keyCode == 38) {
			// 上箭头：向上选择
			Object.prototype.hasOwnProperty.call(list.value, hover.value - 1) && hover.value--
		} else if (keyCode == 40) {
			// 下箭头：向下选择
			Object.prototype.hasOwnProperty.call(list.value, hover.value + 1) && hover.value++
		}
	}

	/**
	 * 获取当前值
	 * ag-Grid 编辑器接口方法
	 */
	function getValue() {
		return value.value
	}

	/**
	 * 判断是否取消编辑开始
	 * ag-Grid 编辑器接口方法
	 * 如果没有单位数据，则取消编辑
	 */
	function isCancelBeforeStart() {
		return !hasData()
	}

	// 组件挂载时的初始化
	onMounted(() => {
		if (hasData()) {
			// 设置初始值
			value.value = props.params.value
			const unitData = props.params.node.data.unitData
			// 构建单位列表（包含默认单位）
			list.value = [{ name: unitData[0].source }].concat(unitData)
			// 找到当前值在列表中的索引
			const findIndex = list.value.findIndex((item) => item.name == value.value)
			findIndex == -1 || (hover.value = findIndex)
			// 自动聚焦并显示弹出框
			nextTick(() => {
				if (root.value) root.value.focus()
				visible.value = true
			})
		}
	})

	// 暴露给 ag-Grid 的接口方法
	defineExpose({ getValue, isCancelBeforeStart })
</script>
<style scoped>
	/* 组件容器样式 */
	.company{
		outline:none;
	}
	/* 悬停状态样式 */
	.hover{
		background: #f2f2f2;
	}
</style>