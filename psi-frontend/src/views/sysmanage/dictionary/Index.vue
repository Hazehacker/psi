<template>
  <div class="menu area">
    <el-tabs tab-position="left" class="demo-tabs">
      <!-- 字典管理子组件：传递字典类型列表 -->
      <el-tab-pane label="字典管理">
        <Dictionary :dictionary-types="dictionaryTypes" />
      </el-tab-pane>
      <!-- 字典类型管理子组件：传递字典类型列表 -->
      <el-tab-pane label="字典类型管理">
        <DictionaryType :dictionary-types="dictionaryTypes" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import Dictionary from './dictionary/Index.vue'
import DictionaryType from './type/Index.vue'
import { getDictionaryTypeNames } from '@/apis/sysmanage/dictionary/index'
import type { DictionaryTypeItem } from '@/apis/sysmanage/dictionary/type'

const dictionaryTypes = ref<DictionaryTypeItem[]>([])

async function loadDictionaryTypes(): Promise<void> {
  try {
    getDictionaryTypeNames(
      (data) => {
        dictionaryTypes.value = data || []
      },
      (error) => {
        console.error('获取字典类型名称列表失败:', error)
      }
    )
  } catch (error) {
    console.error('Error loading dictionary types:', error)
  }
}

// 父组件挂载时加载数据（仅加载一次）
onMounted(() => {
  loadDictionaryTypes()
})
</script>

<style scoped>
:deep(.el-tabs__header) {
  margin: 12px 12px 0 0 !important;
}
</style>
