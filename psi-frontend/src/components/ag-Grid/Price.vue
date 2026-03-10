<template>
  <!-- 价格编辑组件 -->
  <div class="inputGroup">
    <!-- 价格输入框 -->
    <input class="ag-cell-edit-input" type="text" v-model="value" ref="input" />
    <!-- 最近价格弹出框 -->
    <el-popover
      class="popoverTag"
      popper-class="agPricePopover"
      trigger="click"
      @show="getPrice"
    >
      <template #reference>
        <!-- 触发按钮：标签图标 -->
        <i class="el-icon-collection-tag" @click="showPopover"></i>
      </template>
      <!-- 最近价格显示 -->
      <p class="priceText" @click="setPrice">最近价格:{{ price }}</p>
    </el-popover>
  </div>
</template>
<script setup>
/**
 * 价格编辑组件 (Vue 3 Composition API)
 * 功能：在 ag-Grid 中编辑价格，并提供最近价格查询功能
 * 特性：
 * - 支持手动输入价格
 * - 提供最近价格查询（需要选择供应商/客户）
 * - 自动验证前置条件
 * - 支持点击应用最近价格
 */
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

// 组件属性定义
const props = defineProps({ 
  params: { type: Object, required: true } // ag-Grid 传入的参数
})

// 响应式数据
const value = ref('')  // 价格输入值
const price = ref(0)   // 最近价格
const input = ref(null) // 输入框引用

// 计算属性：判断是否有商品数据
const hasData = computed(() => props.params.node.data.key != null)

// 计算属性：根据模块类型返回对应的文本
const modelText = computed(() => {
  const model = props.params.dispose.model
  if (["bor", "buy", "bre", "sor", "sell", "sre"].indexOf(model) == -1) {
    return "未识别的模块"
  } else {
    if (["bor", "buy", "bre"].indexOf(model) != -1) {
      return "供应商"
    } else {
      return "客户"
    }
  }
})

/**
 * 显示弹出框前的验证
 * 检查是否已选择供应商/客户和单位
 * @param {Event} e - 点击事件对象
 */
function showPopover(e) {
  if (props.params.dispose.source() == null) {
    ElMessage({
      type: "warning",
      message: "请先选择" + modelText.value,
    })
    e.stopPropagation()
  } else {
    const data = props.params.node.data
    if (data.unitData.length > 0 && data.unit == "点击选择") {
      ElMessage({ type: "warning", message: "请先选择单位" })
      e.stopPropagation()
    }
  }
}

/**
 * 获取最近价格
 * 通过 API 请求获取指定条件下的最近价格
 */
function getPrice() {
  const dispose = props.params.dispose
  const data = props.params.node.data
  const axios = typeof window !== 'undefined' ? window.$axios : undefined
  if (!axios) return
  
  // 发送请求获取最近价格
  axios.post("service/recentPrice", {
    model: dispose.model,      // 模块类型
    source: dispose.source(),  // 供应商/客户ID
    goods: data.key,          // 商品ID
    attr: data.attr,          // 商品属性
    unit: data.unit,          // 单位
  }).then((result) => {
    if (result.state == "success") {
      price.value = result.info
    } else if (result.state == "error") {
      ElMessage({
        type: "warning",
        message: result.info,
      })
    } else {
      ElMessage({
        type: "error",
        message: "[ ERROR ] 服务器响应超时!",
      })
    }
  })
}

/**
 * 设置最近价格
 * 将最近价格应用到输入框中
 */
function setPrice() {
  if (price.value != 0) {
    value.value = price.value
    if (input.value && input.value.parentNode) input.value.parentNode.focus()
    props.params.api.stopEditing()
  }
}

/**
 * 获取当前值
 * ag-Grid 编辑器接口方法
 */
function getValue() {
  return value.value
}

/**
 * 判断是否取消编辑开始
 * ag-Grid 编辑器接口方法
 * 如果没有商品数据，则取消编辑
 */
function isCancelBeforeStart() {
  return !hasData.value
}

// 组件挂载时的初始化
onMounted(() => {
  // 设置初始值
  value.value = props.params.value
  if (hasData.value) {
    // 自动聚焦并选中输入框内容
    nextTick(() => {
      if (input.value) {
        input.value.focus()
        if (typeof input.value.select === 'function') input.value.select()
      }
    })
  }
})

// 暴露给 ag-Grid 的接口方法
defineExpose({ getValue, isCancelBeforeStart })
</script>
<style>
/* 弹出框样式 */
.agPricePopover {
  min-width: auto;
  padding: 6px;
}
</style>
<style scoped>
/* 输入组容器 */
.inputGroup {
  position: relative;
  width: 100%;
  height: 100%;
}
/* 弹出框触发按钮 */
.popoverTag {
  position: absolute;
  right: 6px;
  top: 6px;
  line-height: initial;
}
/* 价格文本样式 */
.priceText {
  font-size: 12px;
  cursor: default;
}
</style>
