<template>
  <div class="button-group" :class="[direction, align]">
    <slot />
  </div>
</template>

<script setup lang="ts">
interface ButtonGroupProps {
  direction?: 'horizontal' | 'vertical'
  align?: 'left' | 'center' | 'right'
  gap?: string
}

withDefaults(defineProps<ButtonGroupProps>(), {
  direction: 'horizontal',
  align: 'left',
  gap: '12px'
})
</script>

<style scoped>
.button-group {
  display: flex;
  gap: v-bind(gap);
}

.button-group.horizontal {
  flex-direction: row;
}

.button-group.vertical {
  flex-direction: column;
  align-items: flex-start;
}

.button-group.left {
  justify-content: flex-start;
}

.button-group.center {
  justify-content: center;
}

.button-group.right {
  justify-content: flex-end;
}
</style>
