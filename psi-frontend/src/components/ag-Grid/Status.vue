<template>
	<div class="status">
		<p>{{ text }}</p>
		<slot></slot>
	</div>
</template>

<script setup>
	/**
	 * Status Component | 状态统计组件 (Vue 3)
	 * 用途：根据配置显示统计信息（文本、计数、求和、平均值）。
	 * 依赖：window.$calc 用于数值计算。
	 * 参数：
	 *   - config: 配置数组，如 [{text:'汇总数据',type:'text'}, {text:'商品总数',type:'count'}, ...]
	 *   - source: 数据源数组
	 */
	import { ref, watch, onMounted } from 'vue'

	// Props
	const props = defineProps({
		config: {
			type: Array,
			default: () => [{ text: 'HELLO NODCLOUD', type: 'text' }]
		},
		source: {
			type: Array,
			default: () => []
		}
	})

	const text = ref('')

	/**
	 * 处理数据 - 根据配置生成统计文本
	 */
	function handle() {
		const calc = typeof window !== 'undefined' ? window.$calc : undefined
		if (!calc) return

		const arr = []
		// 循环配置项
		for (const configVo of props.config) {
			let resultText = ""
			if (configVo.type === 'text') {
				// 文本类型
				resultText = configVo.text
			} else if (configVo.type === 'count') {
				// 总数统计
				resultText = configVo.text + ':' + props.source.length
			} else if (configVo.type === 'sum') {
				// 总和统计
				const number = calc.chain(0)
				props.source.forEach((item) => {
					number.add(item[configVo.key])
				})
				resultText = configVo.text + ':' + number.done()
			} else if (configVo.type === 'avg') {
				// 平均值统计
				let number = calc.chain(0)
				props.source.forEach((item) => {
					number.add(item[configVo.key])
				})
				number = calc.chain(number.done()).divide(props.source.length).round(4).done()
				resultText = configVo.text + ':' + (isNaN(number) ? 0 : number)
			}
			configVo.hasOwnProperty('fun') && (resultText = configVo.fun(resultText))
			arr.push(resultText)
		}
		text.value = arr.join(" | ")
	}

	// 监听 config 和 source 变化
	watch(() => props.config, () => handle(), { deep: true })
	watch(() => props.source, () => handle())

	// 初始化
	onMounted(() => {
		handle()
	})
</script>

<style scoped>
	.status{
		position: relative;
		width: 100%;
		font-size: 12px;
		padding: 3px 6px;
		color: rgba(0, 0, 0, 0.8);
		border: 1px solid #dcdfe6;
		border-top: none;
		box-sizing: border-box;
		line-height: 26px;
		background: #f5f7f7;
	}
</style>
