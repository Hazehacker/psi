<template>
  <div class="button-demo-container">
    <div class="demo-header">
      <h1>MyButton 组件演示</h1>
    </div>

    <!-- 基础按钮演示 -->
    <div class="demo-section">
      <h2>基础按钮</h2>
      <div class="demo-grid">
        <div class="demo-item">
          <h3>不同类型</h3>
          <div class="button-group">
            <MyButton text="主要按钮" type="primary" />
            <MyButton text="成功按钮" type="success" />
            <MyButton text="警告按钮" type="warning" />
            <MyButton text="危险按钮" type="danger" />
            <MyButton text="信息按钮" type="info" />
          </div>
        </div>

        <div class="demo-item">
          <h3>不同状态</h3>
          <div class="button-group">
            <MyButton text="默认按钮" />
            <MyButton text="加载中" :loading="true" />
            <MyButton text="已禁用" :disabled="true" />
            <MyButton text="朴素按钮" plain />
            <MyButton text="圆角按钮" round />
          </div>
        </div>

        <div class="demo-item">
          <h3>不同尺寸</h3>
          <div class="button-group">
            <MyButton text="大号按钮" size="large" />
            <MyButton text="默认按钮" size="default" />
            <MyButton text="小号按钮" size="small" />
          </div>
        </div>
      </div>
    </div>

    <!-- 业务按钮演示 -->
    <div class="demo-section">
      <h2>业务按钮</h2>
      <div class="demo-grid">
        <div class="demo-item">
          <h3>采购审核功能</h3>
          <ButtonGroup gap="12px">
            <BusinessButton action-type="unaudit" />
            <BusinessButton action-type="check" />
            <BusinessButton action-type="refresh" />
          </ButtonGroup>
        </div>

        <div class="demo-item">
          <h3>销售审核功能</h3>
          <ButtonGroup gap="12px">
            <BusinessButton action-type="generate" />
            <BusinessButton action-type="unaudit" />
            <BusinessButton action-type="refresh" />
          </ButtonGroup>
        </div>

        <div class="demo-item">
          <h3>通用功能</h3>
          <ButtonGroup gap="12px">
            <BusinessButton action-type="save" />
            <BusinessButton action-type="refresh" />
          </ButtonGroup>
        </div>
      </div>
    </div>

    <!-- 按钮组演示 -->
    <div class="demo-section">
      <h2>按钮组布局</h2>
      <div class="demo-grid">
        <div class="demo-item">
          <h3>左对齐</h3>
          <ButtonGroup align="left" gap="16px">
            <BusinessButton action-type="save" />
            <BusinessButton action-type="refresh" />
            <MyButton text="自定义" type="info" />
          </ButtonGroup>
        </div>

        <div class="demo-item">
          <h3>居中对齐</h3>
          <ButtonGroup align="center" gap="16px">
            <BusinessButton action-type="save" />
            <BusinessButton action-type="refresh" />
            <MyButton text="自定义" type="info" />
          </ButtonGroup>
        </div>

        <div class="demo-item">
          <h3>右对齐</h3>
          <ButtonGroup align="right" gap="16px">
            <BusinessButton action-type="save" />
            <BusinessButton action-type="refresh" />
            <MyButton text="自定义" type="info" />
          </ButtonGroup>
        </div>
      </div>
    </div>

    <!-- 交互演示 -->
    <div class="demo-section">
      <h2>交互演示</h2>
      <div class="demo-grid">
        <div class="demo-item">
          <h3>确认对话框</h3>
          <div class="button-group">
            <MyButton
              text="删除数据"
              type="danger"
              confirm-message="确定要删除这条数据吗？"
              @confirm="handleConfirm"
            />
          </div>
        </div>

        <div class="demo-item">
          <h3>加载状态</h3>
          <div class="button-group">
            <BusinessButton
              action-type="save"
              :loading="loadingState"
              @action="handleSaveWithLoading"
            />
          </div>
        </div>

        <div class="demo-item">
          <h3>动态禁用</h3>
          <div class="button-group">
            <BusinessButton action-type="unaudit" :disabled="!canOperate" @action="handleUnaudit" />
            <MyButton text="切换状态" @click="canOperate = !canOperate" />
          </div>
          <p class="status-text">当前状态: {{ canOperate ? '可操作' : '已禁用' }}</p>
        </div>
      </div>
    </div>

    <!-- 操作反馈 -->
    <div class="demo-section">
      <h2>操作反馈</h2>
      <div class="feedback-panel">
        <div
          class="feedback-item"
          :class="feedback.type"
          v-for="feedback in feedbackList"
          :key="feedback.id"
        >
          {{ feedback.message }}
        </div>
        <div v-if="feedbackList.length === 0" class="feedback-empty">暂无操作记录</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 导入组件
import MyButton from '@/components/mybutton/MyButton.vue'
import BusinessButton from '@/components/mybutton/BusinessButton.vue'
import ButtonGroup from '@/components/mybutton/ButtonGroup.vue'

// 状态
const loadingState = ref(false)
const canOperate = ref(true)
const feedbackList = ref<Array<{ id: number; message: string; type: string }>>([])

// 添加反馈
const addFeedback = (message: string, type: string = 'info') => {
  feedbackList.value.unshift({
    id: Date.now(),
    message,
    type
  })

  // 只保留最近5条
  if (feedbackList.value.length > 5) {
    feedbackList.value.pop()
  }
}

// 处理确认
const handleConfirm = () => {
  addFeedback('确认删除操作', 'warning')
  ElMessage.success('删除成功')
}

// 处理带加载的保存
const handleSaveWithLoading = () => {
  loadingState.value = true
  addFeedback('开始保存数据...', 'info')

  setTimeout(() => {
    loadingState.value = false
    addFeedback('数据保存成功', 'success')
    ElMessage.success('保存成功')
  }, 1500)
}

// 处理反审核
const handleUnaudit = () => {
  addFeedback('执行反审核操作', 'warning')
  ElMessage.success('反审核成功')
}
</script>

<style scoped>
.button-demo-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.demo-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.demo-header h1 {
  margin: 0 0 8px 0;
  font-size: 2.5rem;
  font-weight: 700;
}

.demo-header p {
  margin: 0;
  opacity: 0.9;
  font-size: 1.1rem;
}

.demo-section {
  margin-bottom: 40px;
  padding: 24px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #eaeaea;
}

.demo-section h2 {
  margin: 0 0 24px 0;
  color: #2c3e50;
  font-size: 1.5rem;
  font-weight: 600;
  border-bottom: 2px solid #3498db;
  padding-bottom: 8px;
}

.demo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.demo-item {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.demo-item h3 {
  margin: 0 0 16px 0;
  color: #495057;
  font-size: 1.1rem;
  font-weight: 500;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.status-text {
  margin: 12px 0 0 0;
  font-size: 0.9rem;
  color: #6c757d;
  font-style: italic;
}

.feedback-panel {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.feedback-item {
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 6px;
  font-size: 0.9rem;
  border-left: 4px solid;
}

.feedback-item.info {
  background: #e3f2fd;
  border-left-color: #2196f3;
  color: #1565c0;
}

.feedback-item.success {
  background: #e8f5e8;
  border-left-color: #4caf50;
  color: #2e7d32;
}

.feedback-item.warning {
  background: #fff3e0;
  border-left-color: #ff9800;
  color: #ef6c00;
}

.feedback-empty {
  text-align: center;
  color: #6c757d;
  font-style: italic;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .button-demo-container {
    padding: 16px;
  }

  .demo-grid {
    grid-template-columns: 1fr;
  }

  .button-group {
    justify-content: center;
  }

  .demo-header h1 {
    font-size: 2rem;
  }
}
</style>
