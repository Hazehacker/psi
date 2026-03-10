<template>
	<div class="inputGroup">
		<input class="ag-cell-edit-input" type="text" v-model="value" ref="input">
		<template v-if="hasSource">
			<el-popover class="popoverTag" popper-class="agCpePopover" trigger="click" @show="getCost">
				<template #reference>
					<i class="el-icon-collection-tag"></i>
				</template>
				<el-table :data="cost" height="80px" size="small" border>
					<el-table-column prop="extension.type" label="单据类型" align="center" width="90px"></el-table-column>
					<el-table-column prop="sourceData.number" label="单据编号" align="center" width="180px"></el-table-column>
					<el-table-column prop="ietData.name" label="支出名称" align="center" width="120px"></el-table-column>
					<el-table-column prop="uat" label="未结算金额" align="center" width="90px"></el-table-column>
				</el-table>
			</el-popover>
		</template>
	</div>
</template>
<script setup>
	/**
	 * Cell Editor | 支出类别 (Vue 3)
	 * 用途：在 ag-Grid 中编辑支出类别，并在有来源时显示来源单据详情。
	 * 依赖：window.$axios / Element Plus ElMessage；ag-Grid 编辑器接口。
	 * 参数：props.params.node.data.source!=0 时展示 popover 详情按钮。
	 */
	import { ref, computed, onMounted, nextTick } from 'vue'
	import { ElMessage } from 'element-plus'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	// 输入值、详情数据与输入框引用
	const value = ref('')
	const cost = ref([])
	const input = ref(null)

	// 有来源则允许展示详情
	const hasSource = computed(()=> props.params.node.data.source != 0 )

	/** 拉取来源单据的支出详情 */
	function getCost(){
		const source = props.params.node.data.source
		const axios = typeof window !== 'undefined' ? window.$axios : undefined
		if (!axios) return
		axios.post('service/getCost', { cost: source }).then(result => {
			if (result.state == 'success') {
				cost.value = [result.info]
			} else if (result.state == 'error') {
				ElMessage({ type: 'warning', message: result.info })
			} else {
				ElMessage({ type: 'error', message: '[ ERROR ] 服务器响应超时!' })
			}
		})
	}

	/** 返回编辑结果给 ag-Grid */
	function getValue(){
		return value.value
	}

	// 初始化输入值并聚焦
	onMounted(()=>{
		value.value = props.params.value
		nextTick(()=>{
			if (input.value){
				input.value.focus()
				if (typeof input.value.select === 'function') input.value.select()
			}
		})
	})

	// 暴露给 ag-Grid
	defineExpose({ getValue })
</script>
<style>
	.agCpePopover{
		min-width: auto;
		padding: 6px;
	}
</style>
<style scoped>
	.inputGroup{
		position: relative;
		width: 100%;
		height: 100%;
	}
	.popoverTag{
		position: absolute;
		right: 6px;
		top: 6px;
		line-height: initial;
	}
	.costText{
		font-size: 12px;
		cursor: default;
	}
</style>