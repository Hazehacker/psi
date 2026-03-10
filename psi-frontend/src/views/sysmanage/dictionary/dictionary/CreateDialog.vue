<!-- src\views\sysmanage\dictionary\dictionary\CreateDialog.vue -->
<template>
  <el-dialog
    v-model="visible"
    :title="mode === 'add' ? '新增字典' : '编辑字典'"
    width="500px"
    @close="handleCancel"
  >
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="字典名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入字典名称" />
      </el-form-item>

      <el-form-item label="字典类型" prop="tid">
        <el-select v-model="formData.tid" placeholder="请选择字典类型" filterable>
          <el-option
            v-for="type in (props.dictionaryTypes || [])"
            :key="type.id"
            :label="type.name"
            :value="type.id || ''"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="字典值" prop="value">
        <el-input v-model="formData.value" placeholder="请输入字典值" />
      </el-form-item>

      <el-form-item label="描述" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="4"
          placeholder="请输入字典描述信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSave">
          {{ mode === 'add' ? '添加' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, watch, nextTick } from 'vue'
import type { FormInstance } from 'element-plus'
import type { DictDetailDTO, DictionaryTypeItem } from '@/apis/sysmanage/dictionary/type'

const props = defineProps<{
  visible: boolean
  formData?: DictDetailDTO
  mode: 'add' | 'edit'
  dictionaryTypes?: DictionaryTypeItem[] // 字典类型列表
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'save', form: DictDetailDTO): void
  (e: 'cancel'): void
}>()

const visible = ref(props.visible)
const formRef = ref<FormInstance>()
const formData = reactive<DictDetailDTO>({
  name: '',
  tid: '',
  value: '',
  remark: '',
  ...props.formData
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入字典名称', trigger: 'blur' },
    { min: 1, max: 50, message: '字典名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  tid: [{ required: true, message: '请选择字典类型', trigger: 'change' }], // 验证 tid
  value: [
    { required: true, message: '请输入字典值', trigger: 'blur' },
    { min: 1, max: 100, message: '字典值长度在 1 到 100 个字符', trigger: 'blur' }
  ]
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
          tid: '', // 重置为 tid
          value: '',
          remark: '',
          ...props.formData
        })
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
  { deep: true }
)

// 同步 visible 变化到父组件
watch(visible, (val) => {
  emit('update:visible', val)
})

const handleSave = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      // 构建提交数据，确保包含 tid
      const submitData: DictDetailDTO = {
        id: formData.id,
        name: formData.name || '',
        tid: formData.tid || '',
        value: formData.value || '',
        remark: formData.remark
      }
      console.log('submitData', submitData)

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

/* 确保选择器宽度合适 */
:deep(.el-select) {
  width: 100%;
}
</style>
