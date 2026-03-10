<template>
	<div class="inputGroup">
		<input class="batchInput ag-cell-edit-input" type="text" v-model="value" ref="input">
		<span class="bp" @click="showDialog">BP</span>
	</div>
</template>
<script setup>
	/**
	 * Cell Editor | 批次录入 (Vue 3)
	 * 用途：在 ag-Grid 中录入或选择商品批次号；可选集成外部批次选择弹窗。
	 * 依赖：window.openLotDialog 可选；Element Plus ElMessage；ag-Grid 编辑器接口。
	 * 参数：props.params 包含当前行数据 node.data、api、dispose 等。
	 */
	import { ref, computed, onMounted, nextTick } from 'vue'
	import { ElMessage } from 'element-plus'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	// 输入值与输入框引用
	const value = ref('')
	const input = ref(null)

	const hasBatch = computed(() => {
		const data = props.params?.node?.data || {}
		return Object.prototype.hasOwnProperty.call(data, 'batchType') && !!data.batchType
	})

	/** 停止编辑并归还焦点 */
	function stopEditing() {
		if (input.value && input.value.parentNode) input.value.parentNode.focus()
		props.params.api.stopEditing()
	}

	/** 从弹窗选择批次后的回填 */
	function rowSelected(row) {
		value.value = row.number
		if (row.time != null) {
			props.params.node.data.mfd = row.time
		}
		stopEditing()
	}

	/** 打开批次选择弹窗（需宿主实现 window.openLotDialog） */
	function showDialog() {
		const data = props.params.node.data
		if (data.warehouseId == null) {
			ElMessage({ type: 'warning', message: '请先选择仓库信息' })
			return
		}
		const condition = { goods: data.key, attr: data.attr, warehouse: data.warehouseId }
		if (typeof window !== 'undefined' && typeof window.openLotDialog === 'function') {
			window.openLotDialog(condition, rowSelected)
		} else {
			ElMessage({ type: 'info', message: '未集成批次选择弹窗，请实现 window.openLotDialog' })
		}
	}

	/** 返回编辑结果给 ag-Grid */
	function getValue() {
		return value.value
	}

	/** 无批次类型则取消启动编辑器 */
	function isCancelBeforeStart() {
		return !hasBatch.value
	}

	onMounted(() => {
		value.value = props.params.value
		if (hasBatch.value) {
			nextTick(() => {
				if (input.value) {
					input.value.focus()
					if (typeof input.value.select === 'function') input.value.select()
				}
			})
		}
	})

	// 暴露给 ag-Grid 的接口
	defineExpose({ getValue, isCancelBeforeStart })
</script>
<style scoped>
	.inputGroup{
		position: relative;
		width: 100%;
		height: 100%;
	}
	.bp{
		position: absolute;
		right: 6px;
		top: 6px;
		width: 22px;
		height: 16px;
		color: #FFFFFF;
		font-size: 12px;
		line-height: 16px;
		border-radius: 2px;
		background: #909399;
	}
	.batchInput{
		padding-right: 32px !important;
		box-sizing: border-box;
	}
</style>
