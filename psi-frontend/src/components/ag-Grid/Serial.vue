<template>
	<div class="inputGroup">
		<input class="ag-cell-edit-input" type="text" v-model="value" ref="input" :disabled="hasSerial">
		<span class="sn" v-if="hasSerial" @click="showDialog">SN</span>
		<Snu v-if="dialog" :config="config" @save="saveSnu" @destroy="dialog = false"></Snu>
	</div>
</template>
<script setup>
	/**
	 * Cell Editor Component | 常规数量AND序列号 (Vue 3)
	 * 用途：在 ag-Grid 单元格中输入数量或管理序列号。
	 * 依赖：window.$calc / Element Plus ElMessage / Snu 对话框组件。
	 * 参数：props.params 由 ag-Grid 注入，包含 node、data、api、dispose、value 等。
	 */
	import { ref, computed, onMounted, nextTick } from 'vue'
	import { ElMessage } from 'element-plus'
	import Snu from "@/components/dialog/Snu.vue"

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	const value = ref('')
	const dialog = ref(false)
	const config = ref({})
	const input = ref(null)

	// 判断是否有序列号
	const hasSerial = computed(() => {
		const data = props.params.node.data
		if (data.hasOwnProperty('serialType') && data.serialType) {
			return true
		} else {
			return false
		}
	})

	// 判断是否有记录
	const hasRecord = computed(() => {
		return props.params.dispose.record.show
	})

	// 初始化
	onMounted(() => {
		value.value = props.params.value
		if (!hasSerial.value) {
			nextTick(() => {
				if (input.value) {
					input.value.focus()
					input.value.select()
				}
			})
		}
	})

	/**
	 * 显示弹层
	 */
	function showDialog() {
		const data = props.params.node.data
		if (data.warehouseId == null) {
			ElMessage({ type: 'warning', message: '请先选择仓库信息' })
		} else {
			if (data.batchType && data.batch == '') {
				ElMessage({ type: 'warning', message: '请先选择批次信息' })
			} else if (data.validityType && data.mfd == '') {
				ElMessage({ type: 'warning', message: '请先选择生产日期' })
			} else {
				config.value = {
					record: props.params.dispose.record,
					source: data.serial,
					condition: {
						warehouse: data.warehouseId,
						goods: data.key,
						attr: data.attr,
						batch: data.batch,
						mfd: data.mfd
					}
				}
				dialog.value = true
			}
		}
	}

	/**
	 * 保存数据
	 */
	function saveSnu(serial) {
		const calc = typeof window !== 'undefined' ? window.$calc : undefined
		if (!calc) return

		const data = props.params.node.data
		if (data.unitData.length > 0) {
			value.value = calc.chain(serial.length).divide(data.unitRelation.multiple).done()
		} else {
			value.value = serial.length
		}
		data.serial = serial
		props.params.api.stopEditing()
	}

	/**
	 * 返回内容 - ag-Grid 编辑器接口
	 */
	function getValue() {
		return value.value
	}

	// 暴露方法给 ag-Grid
	defineExpose({ getValue })
</script>

<style scoped>
	.inputGroup{
		position: relative;
		width: 100%;
		height: 100%;
	}
	.sn{
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
	input[disabled]{
		background: #FFFFFF;
	}
</style>