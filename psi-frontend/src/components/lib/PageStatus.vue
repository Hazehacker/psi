<template>
	<span class="pageStatus">
		{{text}}
	</span>
</template>

<script setup>
// [
// 	{text:'汇总数据',type:'text'},
// 	{text:'商品总数',type:'count'},
// 	{text:'总数量',type:'sum',key:'nums'},
// 	{text:'总价格',type:'sum',key:'total'},
// 	{text:'平均价',type:'avg',key:'total',fun(text){retrn 1;}}
// ]
import { ref, watch, onMounted, getCurrentInstance } from 'vue'

const props = defineProps({
	config: {
		default: () => {
			return [{ text: 'HELLO NODCLOUD', type: 'text' }]
		}
	},
	model: {
		default: ''
	}
})

const instance = getCurrentInstance()
const text = ref([])

// 获取全局属性
const $calc = instance?.appContext.config.globalProperties.$calc

// 处理数据
const handle = () => {
	const arr = []
	//循环配置项
	for (const configVo of props.config) {
		let textStr = ""
		if (configVo.type == 'text') {
			//文本类型
			textStr = configVo.text
		} else if (configVo.type == 'count') {
			//总数统计
			textStr = configVo.text + ':' + props.model.length
		} else if (configVo.type == 'sum') {
			//总和统计
			const number = $calc?.chain(0)
			props.model.map((modelVo) => {
				number.add(eval('modelVo.' + configVo.key))
			})
			textStr = configVo.text + ':' + number?.done()
		} else if (configVo.type == 'avg') {
			//平均值统计
			let number = $calc?.chain(0)
			props.model.map((modelVo) => {
				number.add(eval('modelVo.' + configVo.key))
			})
			number = $calc?.chain(number?.done()).divide(props.model.length).round(4).done()
			textStr = configVo.text + ':' + number
		}
		configVo.hasOwnProperty('fun') && (textStr = configVo.fun(textStr))
		arr.push(textStr)
	}
	text.value = arr.join(" | ")
}

// 监听 model 变化
watch(() => props.model, () => {
	handle()
})

onMounted(() => {
	handle()
})
</script>

<style scoped>
	.pageStatus{
		height: 24px !important;
		margin: 0 6px;
		padding: 0 6px;
		color: #606266;
		line-height: 24px !important;
		border-radius: 3px;
		border: 1px solid #dcdfe6;
		font-weight: normal;
	}
</style>
