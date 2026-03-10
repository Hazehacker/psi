<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-19 15:28:13
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-19 15:44:41
 * @FilePath: \psi-frontend\src\views\sample\inputList\InputListTest.vue
 * @Description: 通用标签管理组件的测试页
-->
<template>
  <div class="sys area">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="90px" class="formAdapt">
      <el-tabs tab-position="left" width="200px">
        <!-- 使用通用组件替换原有代码 -->
        <el-tab-pane label="商品品牌">
          <InputList
            label="商品品牌"
            placeholder="请输入品牌名称"
            v-model:model-value="form.brand"
            v-model:input-text="text.brand"
            type="brand"
            @add="handleAddBrand"
            @remove="handleRemoveBrand"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="计量单位">
          <InputList
            label="计量单位"
            placeholder="请输入计量单位名称"
            v-model:model-value="form.unit"
            v-model:input-text="text.unit"
            type="unit"
            :reserved-items="['多单位', '-1']"
            @add="handleAddUnit"
            @remove="handleRemoveUnit"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="客户类别">
          <InputList
            label="客户类别"
            placeholder="请输入客户类别名称"
            v-model:model-value="form.crCategory"
            v-model:input-text="text.crCategory"
            type="crCategory"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
            @add="handleAddCrCategory"
            @remove="handleRemoveCrCategory"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="客户等级">
          <InputList
            label="客户等级"
            placeholder="请输入客户等级名称"
            v-model:model-value="form.crGrade"
            v-model:input-text="text.crGrade"
            type="crGrade"
            :protected-items="['常规等级']"
            :reserved-items="['常规等级']"
            @add="handleAddCrGrade"
            @remove="handleRemoveCrGrade"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="供应商类别">
          <InputList
            label="供应商类别"
            placeholder="请输入供应商类别名称"
            v-model:model-value="form.srCategory"
            v-model:input-text="text.srCategory"
            type="srCategory"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
            @add="handleAddSrCategory"
            @remove="handleRemoveSrCategory"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <!-- 数据预览区域 -->
    <el-card class="preview-card" v-if="showPreview">
      <template #header>
        <span>数据预览</span>
        <el-button type="text" @click="showPreview = !showPreview">隐藏</el-button>
      </template>
      <pre>{{ JSON.stringify(form, null, 2) }}</pre>
    </el-card>

    <div class="layerBtn">
      <el-button type="primary" @click="save">保存设置</el-button>
      <el-button @click="resetForm">重置</el-button>
      <el-button type="info" @click="showPreview = !showPreview">
        {{ showPreview ? '隐藏' : '显示' }}数据预览
      </el-button>
      <el-button type="success" @click="addSampleData">添加示例数据</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import InputList from '@/components/inputList/InputList.vue'

// 表单引用
const formRef = ref<FormInstance>()

// 显示预览
const showPreview = ref(true)

// 表单数据
const form = reactive({
  name: '企业资源管理系统',
  company: '示例科技有限公司',
  icp: '京ICP备12345678号',
  notice: '系统维护公告：本周六凌晨2:00-4:00进行系统维护，期间系统将无法访问。',
  brand: ['苹果', '华为', '小米'],
  unit: ['个', '箱', '包'],
  crCategory: ['常规类别', 'VIP客户', '企业客户'],
  crGrade: ['常规等级', '金牌客户', '银牌客户'],
  srCategory: ['常规类别', '一级供应商', '二级供应商']
})

const text = reactive({
  brand: '',
  unit: '',
  crCategory: '',
  crGrade: '',
  srCategory: ''
})

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入系统名称', trigger: 'blur' },
    { min: 2, max: 50, message: '系统名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  company: [
    { required: true, message: '请输入公司名称', trigger: 'blur' },
    { min: 2, max: 100, message: '公司名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  icp: [{ pattern: /^[\u4e00-\u9fa5a-zA-Z0-9-]+$/, message: '备案信息格式不正确', trigger: 'blur' }]
}

// 事件处理函数
const handleAddBrand = (value: string) => {
  form.brand.push(value)
  ElMessage.success(`成功添加品牌: ${value}`)
}

const handleRemoveBrand = (index: number) => {
  const brandName = form.brand[index]
  form.brand.splice(index, 1)
  ElMessage.warning(`已删除品牌: ${brandName}`)
}

const handleAddUnit = (value: string) => {
  form.unit.push(value)
  ElMessage.success(`成功添加计量单位: ${value}`)
}

const handleRemoveUnit = (index: number) => {
  const unitName = form.unit[index]
  form.unit.splice(index, 1)
  ElMessage.warning(`已删除计量单位: ${unitName}`)
}

const handleAddCrCategory = (value: string) => {
  form.crCategory.push(value)
  ElMessage.success(`成功添加客户类别: ${value}`)
}

const handleRemoveCrCategory = (index: number) => {
  const categoryName = form.crCategory[index]
  form.crCategory.splice(index, 1)
  ElMessage.warning(`已删除客户类别: ${categoryName}`)
}

const handleAddCrGrade = (value: string) => {
  form.crGrade.push(value)
  ElMessage.success(`成功添加客户等级: ${value}`)
}

const handleRemoveCrGrade = (index: number) => {
  const gradeName = form.crGrade[index]
  form.crGrade.splice(index, 1)
  ElMessage.warning(`已删除客户等级: ${gradeName}`)
}

const handleAddSrCategory = (value: string) => {
  form.srCategory.push(value)
  ElMessage.success(`成功添加供应商类别: ${value}`)
}

const handleRemoveSrCategory = (index: number) => {
  const categoryName = form.srCategory[index]
  form.srCategory.splice(index, 1)
  ElMessage.warning(`已删除供应商类别: ${categoryName}`)
}

// 统一处理验证错误
const handleValidationError = (message: string) => {
  ElMessage({ type: 'warning', message })
}

// 保存方法
const save = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (valid) {
      // 模拟保存到服务器
      ElMessageBox.confirm('确定要保存系统设置吗？', '确认保存', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 这里应该是实际的API调用
          console.log('保存的数据:', JSON.stringify(form, null, 2))

          ElMessage.success('系统设置保存成功！')
        })
        .catch(() => {
          ElMessage.info('已取消保存')
        })
    }
  } catch (error) {
    ElMessage.warning('表单验证失败，请检查输入内容')
  }
}

// 重置表单
const resetForm = () => {
  ElMessageBox.confirm('确定要重置所有数据吗？重置后将无法恢复。', '确认重置', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      Object.assign(form, {
        name: '',
        company: '',
        icp: '',
        notice: '',
        brand: [],
        unit: [],
        crCategory: ['常规类别'],
        crGrade: ['常规等级'],
        srCategory: ['常规类别']
      })

      Object.assign(text, {
        brand: '',
        unit: '',
        crCategory: '',
        crGrade: '',
        srCategory: ''
      })

      ElMessage.success('表单重置成功！')
    })
    .catch(() => {
      ElMessage.info('已取消重置')
    })
}

// 添加示例数据
const addSampleData = () => {
  // 商品品牌示例数据
  if (form.brand.length === 0) {
    form.brand.push('苹果', '华为', '小米', 'OPPO', 'vivo')
  }

  // 计量单位示例数据
  if (form.unit.length === 0) {
    form.unit.push('个', '箱', '包', '千克', '米')
  }

  // 客户类别示例数据
  if (form.crCategory.length <= 1) {
    form.crCategory.push('VIP客户', '企业客户', '个人客户')
  }

  // 客户等级示例数据
  if (form.crGrade.length <= 1) {
    form.crGrade.push('金牌客户', '银牌客户', '铜牌客户')
  }

  // 供应商类别示例数据
  if (form.srCategory.length <= 1) {
    form.srCategory.push('一级供应商', '二级供应商', '临时供应商')
  }

  // 基础信息示例数据
  if (!form.name) form.name = '企业资源管理系统'
  if (!form.company) form.company = '示例科技有限公司'
  if (!form.icp) form.icp = '京ICP备12345678号'
  if (!form.notice) form.notice = '系统维护公告：本周六凌晨2:00-4:00进行系统维护。'

  ElMessage.success('示例数据添加成功！')
}
</script>

<style scoped>
.sys {
  position: relative;
  padding: 20px;
}

.preview-card {
  margin: 20px 0;
}

.preview-card pre {
  background: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.4;
}

.layerBtn {
  margin-top: 20px;
  text-align: center;
}

.layerBtn .el-button {
  margin: 0 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sys {
    padding: 10px;
  }

  .layerBtn .el-button {
    margin: 5px;
    width: 100%;
  }
}
</style>

<style>
/* 全局样式调整 */
.formAdapt .el-tabs__content {
  /* padding: 20px; */
  min-height: 400px;
}

.formAdapt .el-tabs--left .el-tabs__header.is-left {
  width: 200px;
}

.formAdapt .el-tab-pane {
  padding: 0 20px;
}
</style>
