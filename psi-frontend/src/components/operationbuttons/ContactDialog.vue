<!-- 联系资料子弹窗 -->
<template>
  <el-dialog title="联系人信息" v-model="visible" width="400px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="主联系人">
        <el-switch v-model="form.main" />
      </el-form-item>
      <el-form-item label="联系人">
        <el-input v-model="form.person" placeholder="请输入联系人" />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="form.phone" placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item label="联系地址">
        <el-input v-model="form.address" placeholder="请输入联系地址" />
      </el-form-item>
      <el-form-item label="备注信息">
        <el-input v-model="form.remark" placeholder="请输入备注信息" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="onCancel">取消</el-button>
      <el-button type="primary" @click="onSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible', 'save'])

const visible = ref(props.visible)
watch(
  () => props.visible,
  (v) => (visible.value = v)
)
watch(visible, (v) => emit('update:visible', v))

const form = reactive({
  main: false,
  person: '',
  phone: '',
  address: '',
  remark: ''
})

const onCancel = () => (visible.value = false)
const onSave = () => {
  emit('save', { ...form })
  visible.value = false
}
</script>

<style scoped>
:deep(.el-dialog) {
  border-radius: 6px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}
:deep(.el-dialog__header) {
  border-bottom: 1px solid #e5e5e5;
}
:deep(.el-dialog__title) {
  font-size: 15px;
  color: #333;
  font-weight: 600;
}
:deep(.el-form-item__label) {
  color: #666;
  font-size: 13px;
}
:deep(.el-input__inner) {
  width: 240px;
}
.dialog-footer {
  border-top: 1px solid #e5e5e5;
  padding-top: 10px;
  text-align: right;
}
</style>
