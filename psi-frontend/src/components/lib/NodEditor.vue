<template>
	<div class="nodEditor">
		<div ref="editorRef"></div>
	</div>
</template>
<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue'
import E from 'wangeditor'

const props = defineProps({
	value: {
		required: true,
	},
	height: {
		default: 360
	},
	placeholder: {
		default: "请输入内容..."
	}
})

const emit = defineEmits(['input'])

const instance = getCurrentInstance()
const editorRef = ref(null)
const editor = ref(null)
const html = ref("")

// 获取全局属性
const $base = instance?.appContext.config.globalProperties.$base
const $store = instance?.appContext.config.globalProperties.$store

// 读取数据中心
const store = computed(() => $store?.state || {})

const init = () => {
	editor.value = new E(editorRef.value)
	
	editor.value.config.height = props.height
	editor.value.config.placeholder = props.placeholder
	editor.value.config.onchange = (html) => {
		html.value = html
		emit('input', html)
	}
	
	editor.value.config.withCredentials = true
	editor.value.config.uploadFileName = 'images[]'
	editor.value.config.uploadImgServer = $base?.web + 'service/editorUpload'
	editor.value.config.uploadImgHeaders = { Token: store.value.token }
	editor.value.config.uploadImgHooks = {
		customInsert: function (insert, result) {
			if (result.state == 'success') {
				for (const item of result.info) {
					insert(item)
				}
			} else if (result.state == 'error') {
				alert(result.info)
			} else {
				alert('[ ERROR ] 服务器响应超时!')
			}
		}
	}
	
	editor.value.create()
	editor.value.txt.html(props.value)
}

// 监听 value 变化
watch(() => props.value, (data) => {
	if (data != html.value) {
		editor.value?.txt.html(data)
	}
})

onMounted(() => {
	init()
})

onBeforeUnmount(() => {
	//销毁编辑器
	editor.value?.destroy()
	editor.value = null
})
</script>
