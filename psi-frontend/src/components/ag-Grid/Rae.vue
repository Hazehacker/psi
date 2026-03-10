<template>
	<div class="rae" tabindex="-1" @keydown.stop.prevent="raeEvent">
		<el-popover popper-class="agGridSelect" ref="popover" trigger="manual" v-model:visible="visible">
			<template #reference>
				<p>{{ value }}</p>
			</template>
			<ul class="list">
				<li v-for="(item, index) in list" :key="index" @click="choice(item)" :class="{ hover: index == hover }">
					{{ item.name }}
				</li>
			</ul>
		</el-popover>
	</div>
</template>
<script setup>
	/**
	 * Cell Editor Component | 收支类型 (Vue 3)
	 * 用途：在 ag-Grid 单元格中选择收支类型。
	 * 依赖：Pinia store / Element Plus。
	 * 参数：props.params 由 ag-Grid 注入，包含 node、data、api、dispose({ key, text, type }) 等。
	 */
	import { ref, computed, onMounted, nextTick, getCurrentInstance } from 'vue'
	import { useCommonStore } from '@/stores/common'

	// ag-Grid 传入的参数对象
	const props = defineProps({ params: { type: Object, required: true } })

	const value = ref(null)
	const list = ref([])
	const hover = ref(0)
	const visible = ref(false)
	const popover = ref(null)

	// 访问 Pinia store
	const commonStore = useCommonStore()
	const iet = computed(() => commonStore.iet)

	// 获取当前实例
	const instance = getCurrentInstance()

	// 初始化
	onMounted(() => {
		value.value = props.params.node.data[props.params.dispose.text]
		list.value = iet.value[props.params.dispose.type]
		// 匹配索引
		const findIndex = list.value.findIndex(item => item.name == value.value)
		if (findIndex !== -1) {
			hover.value = findIndex
		}
		nextTick(() => {
			if (instance && instance.proxy && instance.proxy.$el) {
				instance.proxy.$el.focus()
			}
			visible.value = true
		})
	})

	/**
	 * 选择类型
	 */
	function choice(parm) {
		value.value = parm.name
		props.params.node.data[props.params.dispose.key] = parm.id
		if (instance && instance.proxy && instance.proxy.$el && instance.proxy.$el.parentNode) {
			instance.proxy.$el.parentNode.focus()
		}
		props.params.api.stopEditing()
	}

	/**
	 * 键盘事件处理
	 */
	function raeEvent(e) {
		const keyCode = e.keyCode
		if (keyCode == 13) {
			choice(list.value[hover.value])
		} else if (keyCode == 27) {
			if (instance && instance.proxy && instance.proxy.$el && instance.proxy.$el.parentNode) {
				instance.proxy.$el.parentNode.focus()
			}
			props.params.api.stopEditing()
		} else if (keyCode == 38) {
			if (hover.value > 0) hover.value--
		} else if (keyCode == 40) {
			if (hover.value < list.value.length - 1) hover.value++
		}
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
	.rae{
		outline:none;
	}
	.hover{
		background: #f2f2f2;
	}
</style>