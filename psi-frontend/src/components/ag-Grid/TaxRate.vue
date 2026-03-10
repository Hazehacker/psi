<template>
	<div class="taxRate">
		<div class="title">
			<span>{{ title }} </span>
			<el-popover popper-class="taxRatePopper" trigger="click" v-model:visible="visible" @show="onShow">
				<template #reference>
					<i class="el-icon-sort"></i>
				</template>
				<el-input ref="taxInput" v-model="value" @keydown.enter="save" placeholder="税率" size="small" clearable>
					<template #append>
						<span class="btn" @click="save">保存</span>
					</template>
				</el-input>
			</el-popover>
		</div>
	</div>
</template>
<script setup>
	/**
	 * Header Component | 批量税率 (Vue 3)
	 * 用途：一次性为当前 ag-Grid 网格的所有有效行设置税率。
	 * 依赖：window.$lib.validate / window.$grid.updateGridData / Element Plus ElMessage。
	 * 参数：props.params 由 ag-Grid 注入，包含 api、context、dispose({ title,key }) 等。
	 */
	import { ref, onMounted, nextTick } from 'vue'
	import { ElMessage } from 'element-plus'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	// 展示文案与输入值
	const title = ref('')
	const value = ref('')
	// 弹出层显隐与输入框引用
	const visible = ref(false)
	const taxInput = ref(null)

	// 初始化标题
	onMounted(() => {
		title.value = props.params.dispose.title
	})

	/**
	 * 保存批量税率
	 * 校验百分比格式后，遍历节点并写入 dispose.key 对应字段。
	 */
	function save() {
		const lib = typeof window !== 'undefined' ? window.$lib : undefined
		const grid = typeof window !== 'undefined' ? window.$grid : undefined
		if (!lib) return
		if (lib.validate('percentage', value.value)) {
			props.params.api.forEachNode((node) => {
				if (node.data && node.data.key != null) {
					grid && grid.updateGridData && grid.updateGridData(props.params, node.data.uniqid, props.params.dispose.key, value.value)
				}
			})
			props.params.context.runHandleGrid()
			visible.value = false
			ElMessage({ type: 'success', message: '批量设置完成!' })
		} else {
			ElMessage({ type: 'warning', message: '税率不正确!' })
		}
	}

	/**
	 * 弹出层展示回调：聚焦输入框
	 */
	function onShow() {
		nextTick(() => {
			if (taxInput.value) taxInput.value.focus()
		})
	}
</script>
<style>
	.taxRatePopper{
		width: 160px;
	}
	.taxRatePopper .el-input-group__append{
		padding: 0 12px;
	}
	.taxRatePopper .el-input-group__append span{
		cursor: pointer;
	}
</style>
<style scoped>
	.taxRate{
		width: 100%;
	}
	.title{
		text-align: center;
	}
</style>