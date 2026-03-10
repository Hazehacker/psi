<!-- 新增按钮弹窗 -->
<template>
  <el-dialog title="详情" v-model="visible" width="800px">
    <el-tabs v-model="activeTab">
      <!-- 基础资料 -->
      <el-tab-pane label="基础资料" name="base">
        <el-form
          :model="form"
          :rules="rules"
          ref="formRef"
          label-width="100px"
          status-icon
          show-message
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="客户名称" prop="name" required>
                <el-input v-model="form.name" placeholder="请输入客户名称" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="客户编号" prop="code" required>
                <el-input v-model="form.code" placeholder="请输入客户编号" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="所属组织" prop="org" required>
                <el-input v-model="form.org" placeholder="请输入所属组织" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="所属用户" prop="user" required>
                <el-input v-model="form.user" placeholder="请输入所属用户" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="客户类别" prop="type">
                <el-select v-model="form.type" placeholder="请选择类别">
                  <el-option label="常规类别" value="normal" />
                  <el-option label="重要客户" value="vip" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="客户等级" prop="level">
                <el-select v-model="form.level" placeholder="请选择等级">
                  <el-option label="常规等级" value="normal" />
                  <el-option label="高级等级" value="high" />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="开户银行" prop="bank">
                <el-input v-model="form.bank" placeholder="请输入开户银行" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="银行账号" prop="bankAccount">
                <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="纳税号码" prop="taxNo">
                <el-input v-model="form.taxNo" placeholder="请输入纳税识别号" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="备注信息" prop="remark">
                <el-input v-model="form.remark" placeholder="请输入备注信息" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>

      <!-- 联系资料 -->
      <el-tab-pane label="联系资料" name="contact">
        <el-table :data="contacts" border>
          <el-table-column prop="main" label="主联系人" width="100" />
          <el-table-column prop="person" label="联系人" />
          <el-table-column prop="phone" label="联系电话" />
          <el-table-column prop="address" label="联系地址" />
          <el-table-column prop="remark" label="备注信息" />
          <el-table-column fixed="right" label="相关操作" width="120">
            <template #default>
              <el-button size="small" type="primary" @click="openContactDialog">+</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 子弹窗 -->
        <ContactDialog v-model:visible="contactDialogVisible" @save="addContact" />
      </el-tab-pane>
    </el-tabs>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import ContactDialog from './ContactDialog.vue'

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible'])

const visible = ref(props.visible)
watch(
  () => props.visible,
  (v) => (visible.value = v)
)
watch(visible, (v) => emit('update:visible', v))

const activeTab = ref('base')
const formRef = ref()
const form = reactive({
  name: '',
  code: '',
  org: '',
  user: '',
  type: '',
  level: '',
  bank: '',
  bankAccount: '',
  taxNo: '',
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入客户名称' }],
  code: [{ required: true, message: '请输入客户编号' }],
  org: [{ required: true, message: '请输入所属组织' }],
  user: [{ required: true, message: '请输入所属用户' }]
}

const contacts = ref([])

const contactDialogVisible = ref(false)
const openContactDialog = () => (contactDialogVisible.value = true)

const addContact = (data) => {
  contacts.value.push(data)
}

const onCancel = () => (visible.value = false)
const onSave = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      console.log('表单数据：', form, contacts.value)
      visible.value = false
    }
  })
}
</script>

<style scoped>
/* ======= 弹窗整体 ======= */
:deep(.el-dialog) {
  border-radius: 6px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}
:deep(.el-dialog__header) {
  border-bottom: 1px solid #e5e5e5;
  padding-bottom: 10px;
}
:deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
:deep(.el-dialog__body) {
  padding: 20px 25px 10px 25px;
}

/* ======= Tabs 样式 ======= */
:deep(.el-tabs__header) {
  margin-bottom: 15px;
  border-bottom: 1px solid #e5e5e5;
}
:deep(.el-tabs__item) {
  font-size: 14px;
  color: #333;
}
:deep(.el-tabs__item.is-active) {
  color: #409eff;
  font-weight: 600;
}

/* ======= 表单布局 ======= */
:deep(.el-form-item) {
  margin-bottom: 16px;
}
:deep(.el-form-item__label) {
  color: #666;
  font-size: 13px;
}
:deep(.el-input__inner),
:deep(.el-select) {
  width: 100%;
}

/* ======= 红色必填星号 ======= */
:deep(.el-form-item.is-required .el-form-item__label::before) {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

/* ======= 按钮区 ======= */
.dialog-footer {
  border-top: 1px solid #e5e5e5;
  padding-top: 10px;
  text-align: right;
}
.dialog-footer .el-button {
  min-width: 70px;
}

/* ======= 联系资料表格 ======= */
:deep(.el-table) {
  font-size: 13px;
}
:deep(.el-table th) {
  background-color: #f9f9f9;
  color: #333;
  font-weight: 500;
}
</style>
