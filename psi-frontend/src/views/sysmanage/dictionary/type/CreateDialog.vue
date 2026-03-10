<template>
  <el-dialog
    v-model="visible"
    :title="mode === 'add' ? '新增字典类型' : '编辑字典类型'"
    width="500px"
    @close="handleCancel"
  >
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="类型名称" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入字典类型名称"
          :maxlength="50"
          show-word-limit
          :disabled="mode === 'edit'"
        />
      </el-form-item>

      <el-form-item label="类型编码" prop="code">
        <el-input
          v-model="formData.code"
          placeholder="请输入唯一编码"
          :maxlength="50"
          show-word-limit
          :disabled="mode === 'edit'"
        />
      </el-form-item>

      <el-form-item label="备注说明" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="4"
          placeholder="请输入备注信息"
          :maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSave">
          {{ mode === 'add' ? '确定' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, watch, nextTick } from 'vue'
import type { FormInstance } from 'element-plus'
import type { DictionTypeDTO } from '@/apis/sysmanage/dictionary/type'

const props = defineProps<{
  visible: boolean
  formData?: DictionTypeDTO
  mode: 'add' | 'edit'
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'save', form: DictionTypeDTO): void
  (e: 'cancel'): void
}>()

const visible = ref(props.visible)
const formRef = ref<FormInstance>()
const formData = reactive<DictionTypeDTO>({
  name: '',
  code: '',
  remark: '',
  ...props.formData
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入字典类型名称', trigger: 'blur' },
    { min: 1, max: 50, message: '字典类型名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入类型编码', trigger: 'blur' },
    { min: 1, max: 50, message: '类型编码长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  remark: [{ max: 200, message: '备注信息长度不能超过 200 个字符', trigger: 'blur' }]
}

// 监听 visible 变化
watch(
  () => props.visible,
  (val) => {
    visible.value = val
    if (val) {
      // 显示弹窗时重置表单数据
      nextTick(() => {
        Object.assign(formData, {
          name: '',
          code: '',
          remark: '',
          ...props.formData
        })
      })
    } else {
      // 关闭弹窗时清空数据
      Object.assign(formData, {
        name: '',
        code: '',
        remark: '',
        id: undefined
      })
    }
  }
)

// 监听 formData 变化（编辑时数据回填）
watch(
  () => props.formData,
  (newFormData) => {
    if (newFormData) {
      Object.assign(formData, newFormData)
    }
  },
  { deep: true, immediate: true } // 添加 immediate: true 确保初始数据正确加载
)

// 同步 visible 变化到父组件
watch(visible, (val) => {
  emit('update:visible', val)
})

const handleSave = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      // 构建提交数据，确保包含所有必要的字段
      const submitData: DictionTypeDTO = {
        code: formData.code,
        name: formData.name,
        remark: formData.remark
      }

      // 如果是编辑模式，添加 id
      if (props.mode === 'edit' && formData.id) {
        submitData.id = formData.id
      }

      emit('save', submitData)
    }
  })
}

const handleCancel = () => {
  visible.value = false
  emit('cancel')
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.mt-2 {
  margin-top: 8px;
}
</style>
