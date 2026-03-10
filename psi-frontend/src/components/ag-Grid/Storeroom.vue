<template>
	<div class="storeroom">
		<div class="title">
			<span>{{ title }} </span>
			<el-popover popper-class="agGridSelect" trigger="click" v-model:visible="visible">
				<template #reference>
					<i class="el-icon-sort"></i>
				</template>
				<ul class="list">
					<li v-for="(warehouse, index) in list" :key="index" @click="choice(warehouse)">
						{{ warehouse.name }}
					</li>
				</ul>
			</el-popover>
		</div>
	</div>
</template>
<script setup>
	/**
	 * Header Component | 批量仓库 (Vue 3)
	 * 用途：一次性为当前 ag-Grid 网格的所有有效行设置仓库。
	 * 依赖：window.$grid.updateGridData / Element Plus ElMessage / Pinia store。
	 * 参数：props.params 由 ag-Grid 注入，包含 api、context、dispose({ title, key, text }) 等。
	 */
	import { ref, onMounted, computed } from 'vue'
	import { ElMessage } from 'element-plus'
	import { useCommonStore } from '@/stores/common'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	const title = ref('')
	const list = ref([])
	const visible = ref(false)

	// 访问 Pinia store
	const commonStore = useCommonStore()
	const warehouse = computed(() => commonStore.warehouses)

	// 初始化
	onMounted(() => {
		title.value = props.params.dispose.title
		list.value = warehouse.value
	})

	/**
	 * 选择仓库
	 */
	function choice(item) {
		const grid = typeof window !== 'undefined' ? window.$grid : undefined
		const dispose = props.params.dispose
		props.params.api.forEachNode((node) => {
			if (node.data && node.data.key != null && node.data.goodsType == 0) {
				node.data[dispose.key] = item.id
				grid && grid.updateGridData && grid.updateGridData(props.params, node.data.uniqid, dispose.text, item.name)
			}
		})
		props.params.context.runHandleGrid()
		visible.value = false
		ElMessage({ type: 'success', message: '批量设置完成!' })
	}
</script>
<style scoped>
	.storeroom{
		width: 100%;
	}
	.title{
		text-align: center;
	}
</style>