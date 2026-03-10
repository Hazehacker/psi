<template>
	<!-- 资金账户选择编辑组件 -->
	<div class="fat" ref="root" tabindex="-1" @keydown.stop.prevent="fatEvent">
		<!-- 账户选择弹出框 -->
		<el-popover popper-class="agGridSelect" trigger="manual" v-model:visible="visible">
			<template #reference>
				<!-- 显示当前选中的账户名称 -->
				<p>{{ value }}</p>
			</template>
			<!-- 账户列表 -->
			<ul class="list">
				<template v-for="(item,index) in list" :key="index">
					<!-- 账户选项，支持键盘导航 -->
					<li @click="choice(item)" :class="{hover:index==hover}">{{item.name}}</li>
				</template>
			</ul>
		</el-popover>
	</div>
</template>
<script setup>
	/**
	 * 资金账户选择编辑组件 (Vue 3 Composition API)
	 * 功能：在 ag-Grid 中选择资金账户，支持键盘导航
	 * 特性：
	 * - 支持资金账户选择
	 * - 键盘导航支持（上下箭头、回车、ESC）
	 * - 从全局状态获取账户列表
	 * - 自动聚焦和显示弹出框
	 */
	import { ref, onMounted, nextTick } from 'vue'

	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})

	// 响应式数据
	const value = ref(null)    // 当前选中的账户名称
	const list = ref([])       // 账户选项列表
	const hover = ref(0)       // 当前悬停的选项索引
	const visible = ref(false) // 弹出框显示状态
	const root = ref(null)     // 根元素引用

	// 组件挂载时的初始化
	onMounted(() => {
		// 设置初始值
		value.value = props.params.node.data[props.params.dispose.text]
		
		// 从全局状态获取账户列表
		const store = typeof window !== 'undefined' ? window.$store : undefined
		list.value = store?.state?.account || []
		
		// 找到当前值在列表中的索引
		const findIndex = list.value.findIndex(item => item.name == value.value)
		findIndex == -1 || (hover.value = findIndex)
		
		// 自动聚焦并显示弹出框
		nextTick(() => {
			if (root.value) root.value.focus()
			visible.value = true
		})
	})

	/**
	 * 选择账户
	 * @param {Object} parm - 选中的账户对象
	 */
	function choice(parm) {
		value.value = parm.name
		props.params.node.data[props.params.dispose.key] = parm.id
		if (root.value && root.value.parentNode) root.value.parentNode.focus()
		props.params.api.stopEditing()
	}

	/**
	 * 键盘事件处理
	 * 支持键盘导航：上下箭头选择、回车确认、ESC取消
	 * @param {KeyboardEvent} e - 键盘事件对象
	 */
	function fatEvent(e) {
		const keyCode = e.keyCode
		if (keyCode == 13) {
			// 回车键：选择当前悬停的选项
			if (list.value[hover.value]) choice(list.value[hover.value])
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

	// 暴露给 ag-Grid 的接口方法
	defineExpose({ getValue })
</script>
<style scoped>
	/* 组件容器样式 */
	.fat{
		outline:none;
	}
	/* 悬停状态样式 */
	.hover{
		background: #f2f2f2;
	}
</style>