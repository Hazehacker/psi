<template>
	<!-- 核销金额批量设置组件 -->
	<div class="bmy">
		<div class="title">
			<!-- 标题 -->
			<span>核销金额</span>
			<!-- 自动计算按钮 -->
			<div class="auto" @click="auto">自动</div>
		</div>
	</div>
</template>
<script setup>
	/**
	 * 核销金额批量设置组件 (Vue 3 Composition API)
	 * 功能：为 ag-Grid 表格中的所有行自动计算核销金额
	 * 特性：
	 * - 支持自动计算核销金额
	 * - 根据业务类型和金额正负进行智能分配
	 * - 使用高精度计算库避免浮点数误差
	 * - 支持多种业务模式（销售、采购、退货等）
	 */
	import { ElMessage } from 'element-plus'
	
	// 组件属性定义
	const props = defineProps({ 
		params: { type: Object, required: true } // ag-Grid 传入的参数
	})

	/**
	 * 加法运算（支持高精度计算）
	 * @param {number} a - 第一个数
	 * @param {number} b - 第二个数
	 * @returns {number} 计算结果
	 */
	function add(a, b) {
		a = Number(a) || 0
		b = Number(b) || 0
		// 尝试使用全局 $calc 精度计算
		if (typeof window !== 'undefined' && window.$calc && window.$calc.chain) {
			return window.$calc.chain(a).add(b).done()
		}
		return a + b
	}

	/**
	 * 减法运算（支持高精度计算）
	 * @param {number} a - 被减数
	 * @param {number} b - 减数
	 * @returns {number} 计算结果
	 */
	function sub(a, b) {
		a = Number(a) || 0
		b = Number(b) || 0
		if (typeof window !== 'undefined' && window.$calc && window.$calc.chain) {
			return window.$calc.chain(a).subtract(b).done()
		}
		return a - b
	}

	/**
	 * 自动计算核销金额
	 * 根据业务类型和金额正负进行智能分配核销金额
	 */
	function auto() {
		// 基础数据汇总 [+,-] 分别表示正数和负数
		const summary = [[0, 0], [0, 0]]
		
		// 业务类型映射配置
		const obj = [
			{ imy: 0, sell: 1, sre: 1, ice: 1 },      // 收入类
			{ omy: 0, buy: 1, bre: 1, oce: 1 },       // 支出类
			{ sell: 0, sre: 0, ice: 0, buy: 1, bre: 1, oce: 1 }, // 混合类
			{ sre: 0, sell: 1 },                      // 销售退货
			{ bre: 0, buy: 1 }                        // 采购退货
		][props.params.dispose.type()]
		
		// 获取表格数据
		const data = (typeof window !== 'undefined' && window.$grid && window.$grid.getGridData)
			? window.$grid.getGridData(props.params)
			: []
		
		// 第一遍遍历：汇总各类别的正负金额
		for (let i = 0; i < data.length; i++) {
			if (data[i].key != null) {
				const mold = obj[data[i].moldType]  // 获取业务类型索引
				const seat = data[i].total - 0 > 0 ? 0 : 1  // 0为正数，1为负数
				summary[mold][seat] = add(summary[mold][seat], data[i].anwo)
			}
		}
		
		// 计算核销金额 [+,-]
		const record = [
			[Math.min(summary[0][0], summary[1][0]), Math.max(summary[0][1], summary[1][1])],
			[Math.min(summary[0][0], summary[1][0]), Math.max(summary[0][1], summary[1][1])]
		]
		
		// 第二遍遍历：分配核销金额到各行
		for (let j = 0; j < data.length; j++) {
			if (data[j].key != null) {
				const mold = obj[data[j].moldType]
				const seat = data[j].total - 0 > 0 ? 0 : 1
				let money = record[mold][seat]
				
				if (money == 0) {
					money = ''
				} else {
					// 核销金额不能超过应收/应付金额
					if ((seat == 0 && money > data[j].anwo) || (seat == 1 && money < data[j].anwo)) {
						money = data[j].anwo
					}
					record[mold][seat] = sub(record[mold][seat], money)
				}
				
				// 更新表格数据
				if (typeof window !== 'undefined' && window.$grid && window.$grid.updateGridData) {
					window.$grid.updateGridData(props.params, data[j].uniqid, 'money', money)
				}
			}
		}
		
		// 显示成功消息并触发表格处理
		ElMessage({ type: 'success', message: '已自动计算核销金额!' })
		props.params.context.runHandleGrid()
	}
</script>

<style scoped>
	/* 组件容器样式 */
	.bmy{
		width: 100%;
	}
	/* 标题样式 */
	.title{
		text-align: center;
	}
	/* 自动计算按钮样式 */
	.auto{
		display: initial;
		color: #fff;
		margin-left: 4px;
		padding: 0px 4px;
		border-radius: 2px;
		background: #9E9E9E;
	}
</style>