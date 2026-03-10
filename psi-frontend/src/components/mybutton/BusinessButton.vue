<template>
  <my-button
    :type="buttonConfig.type"
    :size="size"
    :text="buttonConfig.text"
    :icon="buttonConfig.icon"
    :loading="loading"
    :disabled="disabled"
    :confirm-message="buttonConfig.confirmMessage"
    :data="data"
    :custom-class="buttonConfig.customClass"
    @confirm="handleBusinessAction"
    @click="handleBusinessAction"
  >
    <template v-if="$slots.default">
      <slot />
    </template>
    <template v-else>
      {{ buttonConfig.text }}
    </template>
  </my-button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { DocumentAdd, Refresh, Check, Close, Document } from '@element-plus/icons-vue'

interface BusinessButtonProps {
  // 业务类型
  actionType: 'save' | 'refresh' | 'unaudit' | 'check' | 'uncheck' | 'generate'
  // 按钮尺寸
  size?: 'large' | 'default' | 'small'
  // 加载状态
  loading?: boolean
  // 禁用状态
  disabled?: boolean
  // 数据
  data?: any
  // 自定义文本
  customText?: string
}

const props = defineProps<BusinessButtonProps>()

const emit = defineEmits<{
  action: [actionType: string, data?: any]
}>()

// 按钮配置映射
const buttonConfigMap = {
  save: {
    type: 'primary' as const,
    text: '保存',
    icon: DocumentAdd,
    confirmMessage: '',
    customClass: ''
  },
  refresh: {
    type: 'info' as const,
    text: '刷新',
    icon: Refresh,
    confirmMessage: '',
    customClass: ''
  },
  unaudit: {
    type: 'warning' as const,
    text: '反审核',
    icon: Close,
    confirmMessage: '确定要反审核这条记录吗？',
    customClass: ''
  },
  check: {
    type: 'success' as const,
    text: '核对',
    icon: Check,
    confirmMessage: '确定要核对这条记录吗？',
    customClass: ''
  },
  uncheck: {
    type: 'warning' as const,
    text: '反核对',
    icon: Close,
    confirmMessage: '确定要取消核对吗？',
    customClass: ''
  },
  generate: {
    type: 'success' as const,
    text: '生成',
    icon: Document,
    confirmMessage: '确定要生成相关单据吗？',
    customClass: ''
  }
}

// 计算按钮配置
const buttonConfig = computed(() => {
  const config = { ...buttonConfigMap[props.actionType] }
  if (props.customText) {
    config.text = props.customText
  }
  return config
})

/**
 * 处理业务动作
 */
function handleBusinessAction(e: MouseEvent, data: any) {
  emit('action', props.actionType, props.data || data)
}
</script>
