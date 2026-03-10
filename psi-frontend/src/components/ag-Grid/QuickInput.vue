<template>
  <input
    ref="inputRef"
    class="ag-cell-edit-input"
    type="text"
    v-model="value"
    @keydown.enter="quickEnter"
  />
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, getCurrentInstance } from 'vue';

// 内容组件|扫码组件
interface Props {
  params?: any;
}

const props = withDefaults(defineProps<Props>(), {
  params: () => ({})
});

// Data
const value = ref('');
const inputRef = ref<HTMLInputElement | null>(null);

// 获取当前实例用于事件发射
const instance = getCurrentInstance();

// Computed
const hasKey = computed(() => {
  return props.params?.node?.data?.key != null;
});

// Methods
// 返回内容
const getValue = () => {
  return '';
};

// 回车事件
const quickEnter = () => {
  // 通过 GridOptions 的 context 或者直接发射事件到父组件
  if (instance?.parent) {
    instance.parent.emit('quickEnter', value.value, props.params.rowIndex);
  }
  // 也可以通过 params 的 context 来调用
  if (props.params?.context?.componentParent) {
    props.params.context.componentParent.quickEnter?.(value.value, props.params.rowIndex);
  }
};

// 编辑状态
const isCancelBeforeStart = () => {
  return hasKey.value;
};

// Lifecycle
onMounted(() => {
  if (!hasKey.value) {
    nextTick(() => {
      inputRef.value?.focus();
    });
  }
});

// 暴露方法给 ag-Grid (ag-Grid 需要调用 getValue 和 isCancelBeforeStart)
defineExpose({
  getValue,
  isCancelBeforeStart
});
</script>

<style scoped>
.ag-cell-edit-input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  padding: 0 8px;
  box-sizing: border-box;
}
</style>