<template>
  <el-dialog v-model="dialogVisible" :title="title" width="420px">
    <transition name="el-fade-in">
      <el-form v-if="dialogVisible" :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单标识" prop="key">
          <el-input v-model="form.key" placeholder="请输入菜单标识" />
        </el-form-item>
        <el-form-item label="所属菜单" prop="pid">
          <el-tree-select
            v-model="form.pid"
            :data="processedTreeData"
            :props="treeProps"
            placeholder="请选择所属菜单"
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单模式">
          <el-select v-model="form.model" placeholder="请选择菜单模式" style="width: 100%">
            <el-option label="标签模式" :value="MenuModel.TAB" />
            <el-option label="新页模式" :value="MenuModel.NEW_PAGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="form.type" placeholder="请选择菜单类型" style="width: 100%">
            <el-option label="独立菜单" :value="MenuType.INDEPENDENT" />
            <el-option label="附属菜单" :value="MenuType.AFFILIATED" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单地址">
          <el-input v-model="form.resource" placeholder="请输入菜单地址" />
        </el-form-item>
        <el-form-item label="菜单排序" prop="sort">
          <el-input v-model.number="form.sort" placeholder="请输入菜单排序" />
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input v-model="form.ico" placeholder="请输入菜单图标" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="form.root" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.data" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
    </transition>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import type { MenuItem, MenuNameTreeVO } from '@/apis/sysmanage/menu/type'
import { MenuType, MenuModel } from '@/apis/sysmanage/menu/index'

interface Props {
  visible: boolean
  formData?: MenuItem
  treeData: MenuNameTreeVO[]
  mode: 'add' | 'edit'
}

interface Emits {
  (e: 'update:visible', value: boolean): void
  (e: 'save', data: MenuItem): void
  (e: 'cancel'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref<FormInstance>()

// 表单数据
const initialForm: MenuItem = {
  id: undefined,
  name: '',
  key: '',
  pid: undefined,
  model: MenuModel.TAB, // 使用枚举默认值
  type: MenuType.INDEPENDENT, // 使用枚举默认值
  resource: '',
  sort: 0,
  ico: '',
  root: '',
  data: ''
}

const form = reactive<MenuItem>({ ...initialForm })

// 树形配置
const treeProps = {
  value: 'id',
  label: 'name',
  children: 'children'
}

// 计算属性
const title = computed(() => (props.mode === 'add' ? '新增菜单' : '编辑菜单'))
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 处理树形数据，确保符合 el-tree-select 组件要求
const processedTreeData = computed(() => {
  if (!props.treeData || props.treeData.length === 0) return []

  // 递归处理树形数据，确保结构正确
  const processTree = (nodes: MenuNameTreeVO[]): any[] => {
    return nodes.map((node) => {
      const processedNode = {
        id: node.id,
        name: node.name,
        pid: node.pid,
        children: node.children && node.children.length > 0 ? processTree(node.children) : []
      }
      return processedNode
    })
  }

  const result = processTree(props.treeData)
  return result
})

// 表单验证规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  key: [{ required: true, message: '请输入菜单标识', trigger: 'blur' }],
  sort: [
    { required: true, message: '请输入菜单排序', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === '' || value === null || value === undefined) {
          callback(new Error('请输入菜单排序'))
        } else if (!Number.isInteger(Number(value)) || Number(value) < 0) {
          callback(new Error('菜单排序必须为非负整数'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 监听表单数据变化
watch(
  () => props.formData,
  (newData) => {
    if (newData) {
      // 只同步MenuItem中存在的属性
      const { id, name, key, pid, type, model, resource, sort, ico, root, data } = newData
      Object.assign(form, {
        id,
        name,
        key,
        pid: pid || undefined,
        type: type ?? MenuType.INDEPENDENT, // 使用空值合并运算符
        model: model ?? MenuModel.TAB, // 使用空值合并运算符
        resource,
        sort: sort ?? 0,
        ico,
        root,
        data
      })
    } else {
      Object.assign(form, initialForm)
    }
  },
  { immediate: true, deep: true }
)

// 方法
const handleSave = async (): Promise<void> => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    // 确保数据格式正确
    const submitData: MenuItem = {
      ...form,
      pid: form.pid || undefined,
      sort: Number(form.sort) || 0, // 确保sort是数字
      model: form.model ?? MenuModel.TAB,
      type: form.type ?? MenuType.INDEPENDENT
    }

    emit('save', submitData)
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '表单验证失败，请检查输入项!'
    })
    console.error('Form validation error:', error)
  }
}

const handleCancel = (): void => {
  emit('cancel')
  // 重置表单
  formRef.value?.resetFields()
  Object.assign(form, initialForm)
}

// 暴露方法给父组件
defineExpose({
  resetForm: () => {
    formRef.value?.resetFields()
    Object.assign(form, initialForm)
  }
})
</script>
