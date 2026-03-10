<template>
	<el-input 
		:class="['logistics', { moreIco: more.length > 0 }]" 
		v-model="number" 
		@input="inputChange" 
		:placeholder="placeholder"
	>
		<template #prefix>
			<el-dropdown placement="top" trigger="click" @command="command">
				<span class="el-dropdown-link">
					<el-tooltip placement="bottom" effect="light">
						<template #content>{{ mold.name }}</template>
						<i class="el-icon-truck"></i>
					</el-tooltip>
				</span>
				<template #dropdown>
					<el-dropdown-menu>
						<template v-for="list in lists" :key="list.key">
							<el-dropdown-item :command="list.key">{{ list.name }}</el-dropdown-item>
						</template>
					</el-dropdown-menu>
				</template>
			</el-dropdown>
		</template>
		<template #suffix>
			<div class="icoGroup">
				<i class="el-icon-link" @click="openLink"></i>
				<template v-if="more.length > 0">
					<el-divider direction="vertical"></el-divider>
					<el-popover placement="top" trigger="hover">
						<template #reference>
							<i class="el-icon-arrow-down"></i>
						</template>
						<ul class="moreInfo">
							<template v-for="item in more" :key="item">
								<li>{{ item }}</li>
							</template>
						</ul>
					</el-popover>
				</template>
			</div>
		</template>
	</el-input>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useLogisticsStore } from '@/stores/logistics'
import { ElMessage } from 'element-plus'

const props = defineProps({
	// {key:auto,name:'自动识别',number:'000000'}
	modelValue: {
		required: true,
		type: Object
	},
	placeholder: {
		type: String,
		default: "请输入物流单号"
	},
	more: {
		type: Array,
		default: () => []
	}
})

const emit = defineEmits(['update:modelValue'])

const logisticsStore = useLogisticsStore()

const mold = reactive({
	key: 'auto',
	name: '自动识别'					
})

const lists = ref([])
const number = ref("")

// 初始化
const init = () => {
	// 初始数据
	logisticsStore.enabledLogistics.forEach(item => {
		lists.value.push(item)
	})
}

// 数据处理
const handle = () => {
	const obj = Object.assign({}, props.modelValue)
	number.value = obj.number
	mold.key = obj.key
	mold.name = obj.name
}

// 物流改变
const command = (key) => {
	const find = lists.value.find(item => item.key == key)
	mold.key = find.key
	mold.name = find.name
	emit('update:modelValue', { key: find.key, name: find.name, number: number.value })
}

// 单号改变
const inputChange = (val) => {
	emit('update:modelValue', { key: mold.key, name: mold.name, number: val })
}

// 物流查询
const openLink = () => {
	if (!number.value || number.value.trim() === '') {
		ElMessage.warning("物流单号不可为空!")
	} else {
		const parm = { nu: number.value }
		mold.key == 'auto' || (parm.com = mold.key)
		window.open('https://www.kuaidi100.com/chaxun?' + objToParm(parm))
	}
}

// 工具函数：对象转参数（需要根据你的项目实际情况调整）
const objToParm = (obj) => {
	return Object.keys(obj).map(key => {
		return encodeURIComponent(key) + '=' + encodeURIComponent(obj[key])
	}).join('&')
}

// 工具函数：比较对象（需要根据你的项目实际情况调整）
const comparison = (obj1, obj2) => {
	return JSON.stringify(obj1) === JSON.stringify(obj2)
}

// 监听外部值变化
watch(
	() => props.modelValue,
	(val) => {
		!comparison(val, { key: mold.key, name: mold.name, number: number.value }) && handle()
	},
	{ deep: true }
)

onMounted(() => {
	init()
	handle()
})
</script>

<style scoped>
.logistics {
	width: 100%;
}

.el-dropdown-menu {
	max-height: 220px;
	overflow: auto;
}

:deep(.el-dropdown-menu .popper__arrow) {
	display: none;
}

.moreIco :deep(.el-input__inner) {
	padding-left: 22px;
	padding-right: 50px;
}

.moreInfo {
	list-style: none;
	margin: 0;
	padding: 8px;
}

.moreInfo li {
	font-size: 12px;
	line-height: 20px;
}

.icoGroup {
	display: flex;
	align-items: center;
	gap: 4px;
}

.icoGroup i {
	cursor: pointer;
	color: #909399;
}

.icoGroup i:hover {
	color: #409eff;
}

.el-dropdown-link {
	cursor: pointer;
	display: flex;
	align-items: center;
}
</style>