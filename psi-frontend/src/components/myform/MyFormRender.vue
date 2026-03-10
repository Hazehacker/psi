<template>
  <!-- 输入框 -->
  <el-input
    v-if="item.type === 'input'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @input="$emit('update:model', $event)"
  />
  <!-- 数字输入框 -->
  <el-input-number
    v-else-if="item.type === 'number'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 下拉选择框 -->
  <el-select
    v-else-if="item.type === 'select'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  >
    <el-option
      v-for="(option, index) in ((item.fprops as MyFormSelectProps)?.options || [])"
      :key="index"
      :="option"
    />
  </el-select>
  <!-- 日期选择器 -->
  <el-date-picker
    v-else-if="item.type === 'date'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 复选框 -->
  <el-checkbox
    v-else-if="item.type === 'checkbox'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 复选框组 -->
  <el-checkbox-group
    v-else-if="item.type === 'checkbox-group'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  >
    <el-checkbox
      v-for="(checkbox, index) in ((item.fprops as MyFormCheckBoxGroupProps)?.checkboxes || [])"
      :key="index"
      :style="{ width: item.fprops?.width }"
      :="checkbox"
    />
  </el-checkbox-group>
  <!-- 单选框 -->
  <el-radio
    v-else-if="item.type === 'radio'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 单选框组 -->
  <el-radio-group
    v-else-if="item.type === 'radio-group'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  >
    <el-radio
      v-for="(radio, index) in ((item.fprops as MyFormRadioBoxGroupProps)?.radioboxes || [])"
      :key="index"
      :style="{ width: item.fprops?.width }"
      :="radio"
    />
  </el-radio-group>
  <!-- 级联选择器 -->
  <el-cascader
    v-else-if="item.type === 'cascader'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 级联面板 -->
  <el-cascader-panel
    v-else-if="item.type === 'cascader-panel'"
    v-model="localmodel"
    :style="{ width: item.fprops?.width }"
    :="item.fprops"
    @change="$emit('update:model', $event)"
  />
  <!-- 上传组件 -->
  <CustomUpload
    v-else-if="item.type === 'upload'"
    :style="{ width: item.fprops?.width }"
    :multiple="(item.fprops as MyFormUploadProps).multiple"
    :accept="(item.fprops as MyFormUploadProps).accept"
    :disabled="(item.fprops as MyFormUploadProps).disabled"
    @change="handleCustomUploadChange"
  />
  <!-- //FIXME 扩充其他表单组件需要在这里扩展代码 -->
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import CustomUpload from '../CustomUpload/CustomUpload.vue'
import type {
  MyFormItemAttr,
  MyFormSelectProps,
  MyFormCheckBoxGroupProps,
  MyFormRadioBoxGroupProps,
  MyFormUploadProps
} from './type'

// 定义组件属性
const props = defineProps<{
  item: MyFormItemAttr
  model: any
}>()

// 定义事件
const emit = defineEmits<{
  'update:model': [value: any]
}>()

// 使用本地状态代替props.model
const localmodel = ref(props.model)
// 监听props.model变化，保持本地状态同步
watch(
  () => props.model,
  (val) => {
    localmodel.value = val
  }
)

/**
 * 上传组件变化处理
 * @param file 文件对象
 * @param fileList 文件列表
 */
function handleUploadChange(file: any, fileList: any[]) {
  localmodel.value = fileList
  emit('update:model', fileList)
}

/**
 * 自定义上传组件变化处理
 * @param files 文件列表
 */
function handleCustomUploadChange(files: FileList | null) {
  if (files) {
    const fileArray = Array.from(files)
    localmodel.value = fileArray
    emit('update:model', fileArray)
  }
}
</script>
<style scoped>
/* 解决内联模式宽度异常问题,设置一个默认宽度 */
.el-form--inline {
  .el-form-item {
    .el-input,
    .el-input-number,
    .el-select,
    .el-cascader,
    .el-autocomplete {
      width: 188px;
    }
  }
}
</style>
