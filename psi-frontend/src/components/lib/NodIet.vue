<template>
	<el-popover ref="popoverRef" placement="top-start" width="420" trigger="click" class="nodeIet" @show="check" readonly>
		<el-table :data="table" size="mini" class="gridTable" border>
			<el-table-column label="支出类别" align="center" min-width="120px">
				<template #default="scope">
					<select v-model="scope.row.iet" @change="change">
						<template v-for="item in source" :key="item.id">
							<option :value="item.id">{{item.name}}</option>
						</template>
					</select>
				</template>
			</el-table-column>
			<el-table-column label="金额" align="center" width="100px">
				<template #default="scope">
					<input type="text" v-model="scope.row.money" placeholder="金额" @input="change"></input>
				</template>
			</el-table-column>
			<el-table-column label="备注" align="center" width="100px">
				<template #default="scope">
					<input type="text" v-model="scope.row.data" placeholder="备注" @input="change"></input>
				</template>
			</el-table-column>
			<el-table-column align="center" width="80px">
				<template #header>
					<span>操作</span> <i class="el-icon-circle-plus-outline" @click="addIet"></i>
				</template>
				<template #default="scope">
					<i class="el-icon-delete" @click="delIet(scope.$index)"></i>
				</template>
			</el-table-column>
		</el-table>
		<template #reference>
			<el-input v-model="text" :disabled="true" suffix-icon="el-icon-tickets"></el-input>
		</template>
	</el-popover>
</template>
<script setup>
import { ref, computed, watch, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
	value: {
		required: true,
		type: Array
	},
	type: {
		required: true
	}
})

const emit = defineEmits(['input', 'change'])

const instance = getCurrentInstance()
const popoverRef = ref(null)
const text = ref(0)
const table = ref([])
const source = ref([])

// 获取全局属性
const $lib = instance?.appContext.config.globalProperties.$lib
const $calc = instance?.appContext.config.globalProperties.$calc
const $store = instance?.appContext.config.globalProperties.$store

// 读取数据中心
const store = computed(() => $store?.state || {})

// 显示验证
const check = () => {
	if (source.value.length == 0) {
		popoverRef.value.showPopper = false
		ElMessage({ type: "warning", message: "未设置收支类别" })
	}
}

// 添加类别
const addIet = () => {
	table.value.push({ iet: source.value[0].id, money: '', data: '' })
	change()
}

// 删除类别
const delIet = (index) => {
	table.value.splice(index, 1)
	change()
}

// 数据改变
const change = () => {
	const textCalc = $calc?.chain(0)
	for (const row of table.value) {
		if ($lib?.synValidate('numerical', row.money)) {
			textCalc.add(row.money)
		}
	}
	text.value = textCalc?.done() || 0
	emit('input', table.value)
	emit('change', text.value)
}

// 监听 value 变化
watch(() => props.value, (val) => {
	if (!$lib?.comparison(val, table.value)) {
		table.value = [].concat(val)
		change()
	}
}, { deep: true })

onMounted(() => {
	table.value = [].concat(props.value)
	source.value = store.value.iet?.[props.type] || []
})
</script>
<style scoped>
	.nodeIet >>> .el-input__inner{
		background: #fff;
	}
</style>