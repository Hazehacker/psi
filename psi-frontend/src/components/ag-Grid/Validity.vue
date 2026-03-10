<template>
	<el-date-picker 
		ref="datePicker" 
		v-model="value" 
		type="date" 
		:editable="false" 
		:clearable="false" 
		@change="change" 
		value-format="YYYY-MM-DD" 
		placeholder="点击选择">
	</el-date-picker>
</template>
<script setup>
	/**
	 * Cell Editor Component | 有效期|生产日期 (Vue 3)
	 * 用途：在 ag-Grid 单元格中选择日期。
	 * 依赖：Element Plus DatePicker。
	 * 参数：props.params 由 ag-Grid 注入，包含 node、data、api、value 等。
	 */
	import { ref, computed, onMounted, nextTick } from 'vue'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	const value = ref('')
	const datePicker = ref(null)

	// 判断是否有数据
	const hasData = computed(() => {
		const data = props.params.node.data
		if (data.hasOwnProperty('validityType') && data.validityType) {
			return true
		} else {
			return false
		}
	})

	// 初始化
	onMounted(() => {
		value.value = props.params.value
		if (hasData.value) {
			nextTick(() => {
				if (datePicker.value) {
					// Element Plus Vue3 中需要这样访问
					datePicker.value.handleOpen && datePicker.value.handleOpen()
				}
			})
		}
	})

	/**
	 * 日期改变
	 */
	function change() {
		props.params.api.stopEditing()
	}

	/**
	 * 返回内容 - ag-Grid 编辑器接口
	 */
	function getValue() {
		return value.value
	}

	/**
	 * 编辑状态 - ag-Grid 编辑器接口
	 */
	function isCancelBeforeStart() {
		return !hasData.value
	}

	// 暴露方法给 ag-Grid
	defineExpose({ getValue, isCancelBeforeStart })
</script>
<style scoped>
	.el-date-editor{
		width: 100%;
		height: 100%;
	}
	.el-date-editor >>> .el-input__inner{
		width: 100%;
		height: 100%;
		padding: 0;
		border:none;
		border-radius:0;
		text-align: center;
		font-size: 12px;
		color: #000000;
	}
	.el-date-editor >>> .el-input__prefix , .el-date-editor >>> .el-input__suffix{
		display: none;
	}
</style>