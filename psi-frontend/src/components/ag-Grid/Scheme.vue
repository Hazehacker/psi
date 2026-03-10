<template>
	<div class="scheme">
		<div class="title">
			<span>{{ title }}</span>
			<el-tooltip placement="top" effect="light" :content="state ? '扫码模式' : '常规模式'">
				<el-switch v-model="state" :width="24" @change="change"></el-switch>
			</el-tooltip>
		</div>
	</div>
</template>
<script setup>
	/**
	 * Header Component | 模式切换 (Vue 3)
	 * 用途：切换扫码模式与常规模式。
	 * 参数：props.params 由 ag-Grid 注入，包含 dispose({ title }) 等。
	 */
	import { ref, onMounted, getCurrentInstance } from 'vue'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	const title = ref('')
	const state = ref(false)

	// 获取当前实例以访问 $parent
	const instance = getCurrentInstance()

	// 初始化标题
	onMounted(() => {
		title.value = props.params.dispose.title
	})

	/**
	 * 模式切换回调
	 */
	function change() {
		// 向父组件触发事件
		if (instance && instance.parent) {
			instance.parent.emit('schemeChange', state.value)
		}
	}
</script>
<style scoped>
	.scheme{
		width: 100%;
	}
	.title{
		text-align: center;
	}
	.el-switch{
		height: 12px;
		margin-left: 6px;
		line-height: 12px;
	}
	.el-switch >>> .el-switch__core{
		height: 12px;
	}
	.el-switch >>> .el-switch__core:after{
		width: 8px;
		height: 8px;
	}
	.el-switch.is-checked >>> .el-switch__core{
		border-color: #9E9E9E;
		background-color: #9E9E9E;
	}
	.el-switch.is-checked >>> .el-switch__core::after{
	    left: 100%;
	    margin-left: -9px;
	}
</style>