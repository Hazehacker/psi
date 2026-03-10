<template>
	<div class="stock">
		<el-dialog v-model="dialog" title="库存详情" width="320px" @closed="close" :append-to-body="true" v-madeDialog>
			<el-table :data="tableData" @row-click="rowClick" height="220px" size="mini" border>
				<el-table-column prop="name" label="仓库名称" align="center" min-width="160px"></el-table-column>
				<el-table-column prop="nums" label="库存数量" align="center" min-width="120px"></el-table-column>
			</el-table>
			<el-pagination class="tablePagination" v-model:current-page="page.current" :total="page.total" v-model:page-size="page.size" :page-sizes="page.sizes" :pager-count="page.count" @current-change="record" layout="prev,pager,next"></el-pagination>
		</el-dialog>
	</div>
</template>
<script setup>
	import { ref, reactive, onMounted } from 'vue'
	import { ElMessage } from 'element-plus'

	const props = defineProps({
		condition:{
			required: true,
			type: Object
		}
	})

	const emit = defineEmits(['rowClick', 'destroy'])

	const dialog = ref(true)
	const tableData = ref([])
	const page = reactive({
		current: 1,
		total: 0,
		size: 5,
		sizes: [5, 10, 15, 20, 30],
		count: 5
	})

	const record = () => {
		const parm = Object.assign({
			page: page.current,
			limit: page.size
		}, props.condition);
		window.$axios.post("service/goodsDepot", parm).then(result => {
			if (result.state == "success") {
				tableData.value = result.info;
				page.total = result.count;
			} else if (result.state == "error") {
				ElMessage({
					type: "warning",
					message: result.info
				});
			} else {
				ElMessage({
					type: "error",
					message: "[ ERROR ] 服务器响应超时!"
				});
			}
		});
	}

	const rowClick = (row) => {
		dialog.value=false;
		emit('rowClick',row);
	}

	//弹层关闭事件
	const close = () => {
		emit('destroy',true);
	}

	onMounted(() => {
		record();
	})
</script>
