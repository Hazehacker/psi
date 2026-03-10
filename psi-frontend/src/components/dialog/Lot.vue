<template>
	<div class="lot" @keydown.esc.stop>
		<el-dialog v-model="dialog" title="批次详情" width="420px" @closed="close" :append-to-body="true" v-madeDialog>
			<el-table :data="tableData" height="200px" size="mini" @row-click="rowClick" border v-madeTable>
				<el-table-column prop="index" label="#" type="index" align="center" width="50px"></el-table-column>
				<el-table-column prop="number" label="批次号" align="center" min-width="150px"></el-table-column>
				<el-table-column prop="time" label="生产日期" align="center" min-width="90px"></el-table-column>
				<el-table-column prop="nums" label="库存数量" align="center" min-width="90px"></el-table-column>
			</el-table>
			<el-pagination
				class="tablePagination"
				v-model:current-page="page.current"
				:total="page.total"
				v-model:page-size="page.size"
				:page-sizes="page.sizes"
				:pager-count="page.count"
				@size-change="record(1)"
				@current-change="record(0)"
				layout="prev,pager,next,total,slot"
			>
				<el-popover class="batchSearch" ref="searchPopover" popper-class="batchPopover" placement="top-end">
					<el-form class="searchFrom" ref="searchFrom" inline>
						<el-form-item>
							<el-input v-model="searchFrom.number" placeholder="请输入批次号" clearable></el-input>
						</el-form-item>
						<el-form-item>
							<el-date-picker v-model="searchFrom.startTime" placeholder="请输入开始日期" value-format="yyyy-MM-dd" type="date"></el-date-picker>
						</el-form-item>
						<el-form-item>
							<el-date-picker v-model="searchFrom.endTime" placeholder="请输入结束日期" value-format="yyyy-MM-dd" type="date"></el-date-picker>
						</el-form-item>
						<el-divider></el-divider>
						<el-button class="searchBtn" icon="el-icon-search" @click="record(1)" size="mini"></el-button>
					</el-form>
					<template #reference>
						<el-button class="popoverBtn" icon="el-icon-more" size="mini"></el-button>
					</template>
				</el-popover>
			</el-pagination>
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
	const searchFrom = reactive({
		number: "",
		startTime:"",
		endTime:""
	})
	const tableData = ref([])
	const page = reactive({
		current: 1,
		total: 0,
		size: 30,
		sizes: [30, 60, 90, 150, 300],
		count: 5
	})

	//获取数据
	const record = (pageNum) => {
		pageNum==0||(page.current=pageNum);
		const parm = Object.assign({
			page: page.current,
			limit: page.size
		}, searchFrom, props.condition);
		window.$axios.post("service/getBatch",parm).then(result => {
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
		record(1);
	})
</script>
<style>
	.batchPopover{
		width: 206px;
	}
</style>
<style scoped>
	.batchSearch{
		float: right;
	}
	.popoverBtn{
		border: 1px solid #DCDFE6;
	}   
</style>