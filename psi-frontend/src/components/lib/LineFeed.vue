<template>
  <div class="linefeed" :style="rootStyle">
    <slot />
  </div>
  
</template>

<script setup lang="ts">
import { computed, useSlots } from 'vue'

interface Props {
  tag?: string
  rule?: number | string
  gap?: number | string
  minColWidth?: number | string
}

const props = withDefaults(defineProps<Props>(), {
  tag: '.el-form-item',
  rule: 3,
  gap: 20,
  minColWidth: 280
})

// 计算根样式：使用 CSS Grid 自动换行布局
const rootStyle = computed(() => {
  const gapPx = typeof props.gap === 'number' ? `${props.gap}px` : String(props.gap)
  const minW = typeof props.minColWidth === 'number' ? `${props.minColWidth}px` : String(props.minColWidth)
  return {
    display: 'grid',
    gridTemplateColumns: `repeat(${props.rule}, minmax(${minW}, 1fr))`,
    gap: gapPx
  } as Record<string, string>
})

// 说明：通过 :deep 选择器限制子项，默认支持 Element Plus 的 .el-form-item
// 支持任意 tag 选择器，例如传入 .card、.field
const slots = useSlots()
</script>

<style scoped>
.linefeed :deep(.el-form-item) {
  margin-bottom: 0; /* 由网格 gap 控制垂直间距 */
}

/* 让子项自适应列宽 */
.linefeed :deep(.el-form-item),
.linefeed :deep(.el-form-item__content) {
  width: 100%;
}

/* 允许带有 col-span-all 的表单项跨越所有列，用于备注等全宽字段 */
.linefeed :deep(.col-span-all) {
  grid-column: 1 / -1;
}

/* 响应式优化：小屏幕收缩为两列/一列 */
@media (max-width: 1200px) {
  .linefeed {
    grid-template-columns: repeat(2, minmax(240px, 1fr)) !important;
  }
}

@media (max-width: 768px) {
  .linefeed {
    grid-template-columns: 1fr !important;
  }
}
</style>


