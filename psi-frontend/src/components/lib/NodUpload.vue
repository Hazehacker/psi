<template>
	<el-dropdown class="nodUpload" split-button placement="bottom-start">
		<el-upload
			:action="action"
			:headers="{Token: store.token}"
			:show-file-list="false"
			:on-success="uploadSuccess"
		>
			<i class="text el-icon-upload"> 点击上传</i>
		</el-upload>
		<template #dropdown>
			<el-dropdown-menu>
				<template v-if="files.length>0">
					<template v-for="(file,index) in files" :key="index">
						<li v-if="file && file.name">
							<el-row>
								<el-col :span="22">
									<p class="file" @click="preview(file)">{{file.name}}</p>
								</el-col>
								<el-col :span="2">
									<i class="el-icon-delete" @click="remove(index)"></i>
								</el-col>
							</el-row>
						</li>
					</template>
				</template>
				<template v-else>
					<li class="empty">暂无文件信息</li>
				</template>
			</el-dropdown-menu>
		</template>
	</el-dropdown>
</template>

<script setup>
import { ref, computed, watch, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
	value: {
		required: true,
		type: Array
	},
	action: {
		required: true
	}
})

const emit = defineEmits(['input', 'change'])

const instance = getCurrentInstance()
const files = ref([])

// 获取全局属性
const $lib = instance?.appContext.config.globalProperties.$lib
const $store = instance?.appContext.config.globalProperties.$store

// 读取数据中心
const store = computed(() => $store?.state || {})

// 上传成功
const uploadSuccess = (result, file) => {
	if (result.state == 'success') {
		files.value.push({
			name: file.name,
			url: result.info
		})
		incident()
		ElMessage({
			type: "success",
			message: "[ " + file.name + " ] 上传成功!"
		})
	} else if (result.state == "error") {
		ElMessage({
			type: "warning",
			message: "[ " + file.name + " ]" + result.info
		})
	} else {
		ElMessage({
			type: "error",
			message: "[ ERROR ] 服务器响应超时!"
		})
	}
}

// 删除文件
const remove = (index) => {
	if (index >= 0 && index < files.value.length) {
		files.value.splice(index, 1)
		incident()
	}
}

// 提交事件
const incident = () => {
	emit('input', files.value)
	emit('change', files.value)
}

// 下载文件
const preview = (file) => {
	if (!file || !file.url) {
		ElMessage({ type: "error", message: "文件信息不完整，无法下载" })
		return
	}
	ElMessage({ type: "success", message: "[ " + file.name + " ] 下载请求中..." })
	setTimeout(() => { window.open(file.url) }, 1000)
}

// 监听 value 变化
watch(() => props.value, (val) => {
	if (!$lib?.comparison(val, files.value)) {
		// 过滤掉无效的文件对象
		files.value = [].concat(val).filter(file => file && file.name)
	}
}, { deep: true })

onMounted(() => {
	// 过滤掉无效的文件对象
	files.value = [].concat(props.value).filter(file => file && file.name)
})
</script>

<style scoped>
	.nodUpload >>> .text{
		width: 142px;
		color: #606266;
	}
	.el-dropdown-menu{
		width: 202px;
		padding: 6px 12px;
		list-style-type:none;
		box-sizing: border-box;
	}
	.el-dropdown-menu li{
		color: #606266;
		font-size: 12px;
		cursor: pointer;
		line-height: 26px;
		border-bottom: 1px solid #ccc;
	}
	.el-dropdown-menu li:last-of-type{
		border-bottom:none;
	}
	.el-dropdown-menu .file{
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}
	.empty{
		text-align: center;
	}
</style>