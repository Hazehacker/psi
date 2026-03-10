<template>
    <!-- 字段配置组件 -->
    <div class="dispose">
		<!-- 配置按钮 -->
		<el-icon class="ico" @click="openDialog" style="font-size: 16px; cursor: pointer;" title="字段配置">
			<setting />
		</el-icon>
		<!-- 使用 ColumnFilterDialog 组件 -->
		<ColumnFilterDialog 
			v-model="dialogVisible"
			:allColumns="allColumns"
			:selectedColumns="selectedColumns"
			@confirm="handleConfirm"
		/>
	</div>
</template>
<script setup>
	/**
	 * 字段配置组件 (Vue 3 Composition API)
	 * 功能：为 ag-Grid 表格提供字段显示/隐藏配置功能
	 * 特性：
	 * - 支持字段显示/隐藏切换
	 * - 配置持久化存储（localStorage）
	 * - 自动检测配置变更并恢复默认
	 * - 仅处理标记为 dispose=true 的字段
	 * - 使用 ColumnFilterDialog 组件进行字段筛选
	 */
	import { ref, computed, nextTick } from 'vue'
	import { ElMessage } from 'element-plus'
	import { Setting } from '@element-plus/icons-vue'
	import ColumnFilterDialog from '@/components/common-bai/ColumnFilterDialog.vue'

	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})

	// 响应式数据
	const dialogVisible = ref(false) // 弹窗显示状态
	const allColumns = ref([])       // 所有可配置的列
	const selectedColumns = ref([])  // 当前选中（显示）的列

	/**
	 * 初始化字段配置
	 * 从列定义中提取可配置字段，并加载本地存储的配置
	 */
	function init(){
		// 从列定义中提取可配置字段
		// ag-Grid v34+ 使用 api.getColumnDefs() 替代 columnApi.columnController.columnDefs
		const columnDefs = props.params.api.getColumnDefs() || []
		const tempAllColumns = []
		const tempSelectedColumns = []
		
		columnDefs.forEach(item => {
			if(item.dispose === true){
				const column = {
					title: item.headerName,
					key: item.field,
					width: item.width || 120
				}
				tempAllColumns.push(column)
				
				// 如果列不是隐藏的，添加到选中列表
				if(!item.hide){
					tempSelectedColumns.push(column)
				}
			}
		})
		
		allColumns.value = tempAllColumns
		
		// 尝试加载本地存储的配置
		const storage = localStorage.getItem(props.params.dispose.key)
		if(storage){
			try {
				const storedConfig = JSON.parse(storage)
				const storedKeys = storedConfig.map(item => item.key)
				const currentKeys = tempAllColumns.map(item => item.key)
				
				// 检查配置是否与当前字段匹配
				const allKeysMatch = storedKeys.every(key => currentKeys.includes(key)) && 
				                     currentKeys.every(key => storedKeys.includes(key))
				
				if(allKeysMatch){
					// 配置匹配，使用存储的配置
					selectedColumns.value = tempAllColumns.filter(col => 
						storedConfig.some(stored => stored.key === col.key && stored.show)
					)
				} else {
					// 配置不匹配，清除存储并恢复默认
					localStorage.removeItem(props.params.dispose.key)
					selectedColumns.value = tempSelectedColumns
					ElMessage({ type: 'success', message: '数据表格配置改变,恢复默认配置!' })
				}
			} catch(e) {
				console.error('解析存储配置失败:', e)
				selectedColumns.value = tempSelectedColumns
			}
		} else {
			selectedColumns.value = tempSelectedColumns
		}
		
		// 应用字段显示/隐藏配置
		applyColumnVisibility()
	}

	/**
	 * 应用列的显示/隐藏配置
	 */
	function applyColumnVisibility(){
		const selectedKeys = selectedColumns.value.map(col => col.key)
		
		allColumns.value.forEach(column => {
			const isVisible = selectedKeys.includes(column.key)
			props.params.api.setColumnsVisible([column.key], isVisible)
		})
	}

	/**
	 * 保存配置到本地存储
	 */
	function writeStorage(){
		const selectedKeys = selectedColumns.value.map(col => col.key)
		const config = allColumns.value.map(col => ({
			key: col.key,
			show: selectedKeys.includes(col.key)
		}))
		localStorage.setItem(props.params.dispose.key, JSON.stringify(config))
	}

	/**
	 * 打开字段配置弹窗
	 */
	function openDialog(){
		dialogVisible.value = true
	}

	/**
	 * 处理确认选择
	 * @param {Array} columns - 选中的列
	 */
	function handleConfirm(columns){
		selectedColumns.value = columns
		applyColumnVisibility()
		writeStorage()
		ElMessage({ type: 'success', message: '字段配置已更新!' })
	}

	// 组件挂载后初始化
	nextTick(() => { init() })
</script>
<style scoped>
	/* 组件容器样式 */
	.dispose{
		width: 100%;
	}
	/* 配置按钮样式 */
	.ico{
		display: block;
		text-align: center;
	}
</style>