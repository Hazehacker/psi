<template>
	<div class="billList">
		<el-dialog v-model="dialog" title="单据列表" width="820px" @closed="close" :append-to-body="true" v-madeDialog>
			<div class="group">
				<template v-for="bill in group.data">
					<div class="item">
						<p>{{bill.name}}</p>
						<div class="list">
							<template v-for="mold in bill.list">
								<span :class="{selected:mold.type==group.mold.type}" @click="switchBill(bill,mold)">{{mold.name}}</span>
							</template>
						</div>
					</div>
				</template>
			</div>
			<el-table
				class="dataTable gridTable"
				height="300px"
				ref="dataTable"
				:data="tableData"
				@row-click="rowClick"
				row-key="key"
				size="mini"
				border
				v-madeTable
			>
				<el-table-column type="selection" align="center" width="39px" fixed="left"></el-table-column>
				<el-table-column prop="frameData.name" label="所属组织" align="center" width="120px"></el-table-column>
				<el-table-column prop="time" label="单据时间" align="center" width="160px"></el-table-column>
				<el-table-column prop="number" label="单据编号" align="center" width="200px"></el-table-column>
				<el-table-column prop="extension.nucleus" label="核销状态" align="center" width="120px"></el-table-column>
				<el-table-column prop="total" label="单据金额" align="center" width="120px"></el-table-column>
				<el-table-column prop="extension.amount" label="已核销金额" align="center" width="120px"></el-table-column>
				<el-table-column prop="extension.anwo" label="未核销金额" align="center" width="120px"></el-table-column>
				<el-table-column prop="peopleData.name" label="关联人员" align="center" width="120px"></el-table-column>
				<el-table-column prop="userData.name" label="制单人" align="center" width="120px"></el-table-column>
				<el-table-column prop="data" label="备注信息 " align="center" min-width="200px"></el-table-column>
			</el-table>
			<el-pagination class="tablePagination" v-model:current-page="page.current" :total="page.total" v-model:page-size="page.size"
			 :page-sizes="page.sizes" :pager-count="page.count" @size-change="record(1)" @current-change="record(0)" layout="prev,pager,next,jumper,sizes,total">
			</el-pagination>
			<template #footer>
				<span class="dialog-footer">
				<el-popover ref="searchPopover" popper-class="searchPopover" placement="top-start">
					<el-form class="searchFrom" ref="searchFrom" inline>
						<el-form-item>
							<el-input placeholder="请输入单据编号" v-model="searchFrom.number" clearable></el-input>
						</el-form-item>
						<el-form-item>
							<el-select v-model="searchFrom.nucleus" placeholder="请选择核销状态" clearable>
								<el-option label="未核销" :value="1"></el-option>
								<el-option label="部分核销" :value="2"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item>
							<el-date-picker v-model="searchFrom.startTime" placeholder="请输入开始日期" value-format="yyyy-MM-dd" type="date"></el-date-picker>
						</el-form-item>
						<el-form-item>
							<el-date-picker v-model="searchFrom.endTime" placeholder="请输入结束日期" value-format="yyyy-MM-dd" type="date"></el-date-picker>
						</el-form-item>
						<el-form-item>
							<nodList v-model="searchFrom.user" placeholder="请选择制单人" action="service/userRecord" scene="user"></nodList>
						</el-form-item>
						<el-form-item>
							<el-input placeholder="请输入备注信息" v-model="searchFrom.data" clearable></el-input>
						</el-form-item>
						<el-divider></el-divider>
						<el-button class="searchBtn" icon="el-icon-search" @click="record(1)" ></el-button>
					</el-form>
					<template #reference>
						<el-button icon="el-icon-more" ></el-button>
					</template>
				</el-popover>
				<el-button @click="dialog = false">关闭</el-button>
				<el-button @click="choice" type="primary">添加</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import { ref, reactive, onMounted } from 'vue'
	import { ElMessage } from 'element-plus'
	import NodList from "@/components/lib/NodList.vue"

	const props = defineProps({
		parm:{
			required: true,
			type: Object
		}
	})

	const emit = defineEmits(['choice', 'destroy'])

	const dialog = ref(true)
	const group = reactive({})
	const tableData = ref([])
	const page = reactive({
		current: 1,
		total: 0,
		size: 30,
		sizes: [30, 60, 90, 150, 300],
		count: 5
	})
	const searchFrom = reactive({
		number:"",
		nucleus:"",
		startTime:"",
		endTime:"",
		user:null,
		data: ""
	})

	const dataTable = ref(null)
	const searchPopover = ref(null)

	//获取数据
	const init = () => {
		let data=[];
		const type=props.parm.type;
		if(type==0){
			data=[{name:"预收",type:"cia",list:[{name:"收款单",type:"imy"}]},{name:"应收",type:"re",list:[{name:"销售单",type:"sell"},{name:"销售退货单",type:"sre"},{name:"其它收入单",type:"ice"}]}];
		}else if(type==1){
			data=[{name:"预付",type:"pia",list:[{name:"付款单",type:"omy"}]},{name:"应付",type:"cw",list:[{name:"采购单",type:"buy"},{name:"采购退货单",type:"bre"},{name:"其它支出单",type:"oce"}]}];
		}else if(type==2){
			data=[{name:"应收",type:"re",list:[{name:"销售单",type:"sell"},{name:"销售退货单",type:"sre"},{name:"其它收入单",type:"ice"}]},{name:"应付",type:"cw",list:[{name:"采购单",type:"buy"},{name:"采购退货单",type:"bre"},{name:"其它支出单",type:"oce"}]}];
		}else if(type==3){
			data=[{name:"销退",type:"sre",list:[{name:"销售退货单",type:"sre"}]},{name:"销售",type:"sell",list:[{name:"销售单",type:"sell"}]}];
		}else if(type==4){
			data=[{name:"购退",type:"bre",list:[{name:"采购退货单",type:"bre"}]},{name:"采购",type:"buy",list:[{name:"采购单",type:"buy"}]}];
		}
		Object.assign(group, {
			bill:{
				name:data[0].name,
				type:data[0].type
			},
			mold:{
				name:data[0].list[0].name,
				type:data[0].list[0].type
			},
			data:data
		});
		record(1); //获取数据
	}

	//切换单据
	const switchBill = (bill,mold) => {
		group.bill.name=bill.name;
		group.bill.type=bill.type;
		group.mold.name=mold.name;
		group.mold.type=mold.type;
		record(1); //获取数据
	}

	//搜索单据
	const record = (pageNum) => {
		pageNum==0||(page.current=pageNum);
		const parm = Object.assign({
			page: page.current,
			limit: page.size
		}, searchFrom, props.parm, {mold: group.mold.type});
		window.$axios.post("service/billRecord",parm).then(result => {
			if (result.state == "success") {
				tableData.value = result.info;
				page.total = result.count;
				if (searchPopover.value) {
					searchPopover.value.showPopper=false;
				}
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

	//选择单据
	const choice = () => {
		//获取选中数据
		const selection=dataTable.value.selection;
		if(selection.length>0){
			//数据处理
			const source=[];
			for (const item of selection) {
				const row = window.$lib.extend(true,{},item,{bill:group.bill,mold:group.mold});
				source.push(row);
			}
			ElMessage({
				type: "success",
				message: "添加成功!"
			});
			emit('choice',source);
		}else{
			ElMessage({
				type: "warning",
				message: "您还未选择单据数据!"
			});
		}
	}

	//行单击事件
	const rowClick = (row) => {
		dataTable.value.toggleRowSelection(row);
	}

	//弹层关闭事件
	const close = () => {
		emit('destroy',true);
	}

	onMounted(() => {
		init();
	})
</script>
<style scoped>
	.group{
		display: flex;
		justify-content: space-between;
		border: 1px solid #e6e6e6;
		margin-bottom: -1px;
		cursor: pointer;
	}
	.group .item{
		width: 50%;
		background: #f5f7fa;
		border-right:1px solid #e6e6e6;
	}
	.group .item:last-child{
		border-right:none;
	}
	.group .item p{
		color: #909399;
		font-size: 12px;
		line-height: 32px;
		font-weight: bold;
		text-align: center;
		letter-spacing: 1px;
		border-bottom: 1px solid #e6e6e6;
	}
	.group .list{
		display: flex;
		justify-content: space-between;
	}
	.group .list span{
		flex: auto;
		text-align: center;
		font-size: 12px;
		line-height: 32px;
		border-right: 1px solid #e6e6e6;
	}
	.group .list span:last-child{
		border-right:none;
	}
	.group .list span.selected{
		color: #409eff;
		font-weight: bold;
		letter-spacing: 1px;
	}

	/* 操作按钮浮动 */
	.dialog-footer>span{
		float: left;
	}
</style>
