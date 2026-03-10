<template>
  <el-button
    :="buttonAttrs"
    :loading="isLoading"
    @click="handleClick"
    :class="['my-button', customClass]"
  >
    <el-icon v-if="icon && !isLoading" class="button-icon">
      <component :is="icon" />
    </el-icon>
    <el-icon v-else-if="isLoading" class="button-icon">
      <Loading />
    </el-icon>
    <slot>
      {{ text }}
    </slot>
  </el-button>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessageBox } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'

interface MyButtonProps {
  // 基础属性
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text'
  size?: 'large' | 'default' | 'small'
  text?: string
  icon?: any
  loading?: boolean
  disabled?: boolean
  plain?: boolean
  round?: boolean
  circle?: boolean
  link?: boolean
  customClass?: string

  // 确认提示
  confirmMessage?: string
  // 数据
  data?: any
}

const props = withDefaults(defineProps<MyButtonProps>(), {
  type: 'primary',
  size: 'default',
  text: '按钮',
  loading: false,
  disabled: false
})

const emit = defineEmits<{
  click: [e: MouseEvent, data?: any]
  confirm: [data?: any]
}>()

const internalLoading = ref(false)
const isLoading = computed(() => props.loading || internalLoading.value)

const buttonAttrs = computed(() => ({
  type: props.type,
  size: props.size,
  plain: props.plain,
  round: props.round,
  circle: props.circle,
  disabled: props.disabled || isLoading.value,
  link: props.link
}))

/**
 * 处理按钮点击
 */
async function handleClick(e: MouseEvent) {
  if (props.confirmMessage) {
    try {
      await ElMessageBox.confirm(props.confirmMessage, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      emit('confirm', props.data)
    } catch {
      return
    }
  } else {
    emit('click', e, props.data)
  }
}

// 暴露方法
defineExpose({
  setLoading: (loading: boolean) => {
    internalLoading.value = loading
  }
})
</script>

<style scoped>
.my-button {
  transition: all 0.3s ease;
}

.button-icon {
  margin-right: 4px;
}
</style>
