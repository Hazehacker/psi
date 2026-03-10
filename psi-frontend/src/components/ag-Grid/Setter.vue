<template>
    <div class="setter">
		<i v-if="hasData" class="el-icon-delete" @click="delRow"></i>
		<i v-else class="el-icon-document" @click="setRow"></i>
	</div>
</template>

<script setup>
	/**
	 * Cell Component | 操作组件 (Vue 3)
	 * 用途：显示删除或选择图标，用于删除行或触发选择事件。
	 * 参数：props.params 由 ag-Grid 注入，包含 node、data、api、context 等。
	 */
	import { computed, getCurrentInstance } from 'vue'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	// 获取当前实例以访问 $parent
	const instance = getCurrentInstance()

	// 判断是否有数据
	const hasData = computed(() => {
		return props.params.node.data.key != null
	})

	/**
	 * 图标事件 - 触发选择
	 */
	function setRow() {
		if (instance && instance.parent) {
			instance.parent.emit('setter', props.params.api.getFocusedCell())
		}
	}

	/**
	 * 删除数据
	 */
	function delRow() {
		props.params.api.applyTransaction({ remove: [props.params.data] })
		props.params.context.runHandleGrid()
	}
</script>