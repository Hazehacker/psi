<template>
    <!-- 行操作组件 -->
    <div class="handle">
		<!-- 第一行显示添加按钮 -->
		<i v-if="firstRow" class="el-icon-circle-plus-outline" @click="addRow"></i>
		<!-- 其他行显示删除按钮 -->
		<i v-else class="el-icon-delete" @click="delRow"></i>
	</div>
</template>

<script setup>
	/**
	 * 行操作组件 (Vue 3 Composition API)
	 * 功能：为 ag-Grid 表格提供行级别的添加和删除操作
	 * 特性：
	 * - 第一行显示添加按钮
	 * - 其他行显示删除按钮
	 * - 支持添加新行（通过父组件事件）
	 * - 支持删除当前行
	 */
	import { computed, getCurrentInstance } from 'vue'

	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})
	
	// 获取当前组件实例
	const instance = getCurrentInstance()

	// 计算属性：判断是否为第一行
	const firstRow = computed(() => props.params.node.firstChild)

	/**
	 * 添加新行
	 * 通过父组件事件添加新行
	 */
	function addRow() {
		instance?.parent?.emit('addRow', props.params.api.getFocusedCell())
	}

	/**
	 * 删除当前行
	 * 从表格中移除当前行数据
	 */
	function delRow() {
		props.params.api.applyTransaction({ remove: [props.params.data] })
		props.params.context.runHandleGrid()
	}
</script>