<template>
	<draggable v-if="fields.length>0" :list="fields" :disabled="!drag" @choose="dragChoose" @end="dragEnd" tag="div" class="fieldForm">
		<template v-for="(field,index) in fields" :key="index">
			<el-form-item :label="field.label" :prop="prefix+field.prop" :rules="field.rules">
				<template v-if="field.type == 'text'">
					<el-input type="text" v-model="source[field.model]" :placeholder="field.placeholder"></el-input>
				</template>
				<template v-if="field.type == 'time'">
					<el-date-picker type="date" v-model="source[field.model]" :placeholder="field.placeholder" value-format="yyyy-MM-dd"></el-date-picker>
				</template>
				<template v-if="field.type == 'textarea'">
					<el-input type="textarea" v-model="source[field.model]" :placeholder="field.placeholder"></el-input>
				</template>
				<template v-if="field.type == 'select'">
					<el-select v-model="source[field.model]" :placeholder="field.placeholder" clearable>
						<el-option v-for="option in field.options" :key="option" :label="option" :value="option">
						</el-option>
					</el-select>
				</template>
				<template v-if="field.type == 'checkbox'">
					<el-checkbox-group v-model="source[field.model]">
						<el-checkbox v-for="checkbox in field.checkboxs" :key="checkbox" :label="checkbox"></el-checkbox>
					</el-checkbox-group>
				</template>
				<template v-if="field.type == 'upload'">
					<NodUpload v-model="source[field.model]" :action="baseUrl + 'service/fieldUpload'"></NodUpload>
				</template>
			</el-form-item>
		</template>
	</draggable>
</template>
<script setup>
import { ref, reactive, computed, watch, onMounted, getCurrentInstance } from 'vue'
import draggable from 'vuedraggable'
import NodUpload from "@/components/lib/NodUpload.vue"

const props = defineProps({
	value: {
		required: true,
		type: Object
	},
	prefix: {
		default: ""
	},
	rule: {
		required: true
	},
	drag: {
		default: false
	}
})

const emit = defineEmits(['input', 'dragChoose', 'dragEnd'])

const instance = getCurrentInstance()
const source = ref({})
const fields = ref([])

// 获取全局属性
const $lib = instance?.appContext.config.globalProperties.$lib
const $base = instance?.appContext.config.globalProperties.$base
const $store = instance?.appContext.config.globalProperties.$store

const baseUrl = computed(() => $base?.web || '')

// 读取数据中心
const store = computed(() => $store?.state || {})

// 数据初始化
const init = () => {
	//1 配置字段和初始值
	const obj = {}
	const replace = {}
	for (const field of fields.value) {
		obj[field.model] = field.value
		//类型验证处理
		if (props.value.hasOwnProperty(field.model) && !$lib?.VariableEqual(field.value, props.value[field.model])) {
			replace[field.model] = field.value
		}
	}
	//2 覆盖默认值|深拷
	source.value = $lib?.extend(true, {}, obj, props.value, replace)
}

// 拖动元素选中事件
const dragChoose = (obj) => {
	emit('dragChoose', obj.oldIndex)
}

// 拖动结束事件
const dragEnd = (obj) => {
	emit('dragEnd', obj.newIndex)
}

// 监听 value 变化
watch(() => props.value, (val) => {
	if (!$lib?.comparison(val, source.value)) {
		init()
	}
}, { deep: true })

// 监听 source 变化
watch(source, (val) => {
	emit('input', $lib?.extend(true, {}, val))
}, { deep: true })

onMounted(() => {
	//字段配置
	if (Array.isArray(props.rule)) {
		fields.value = props.rule
		init()
	} else {
		if (store.value.fields?.hasOwnProperty(props.rule)) {
			fields.value = store.value.fields[props.rule]
			init()
		}
	}
})
</script>
