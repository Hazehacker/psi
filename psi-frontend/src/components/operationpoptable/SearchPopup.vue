<template>
  <el-card class="search-popup-card">
    <div class="popup-header">
      <span>搜索条件</span>
      <el-button type="text" @click="closePopup">
        <el-icon><Close /></el-icon>
      </el-button>
    </div>

    <el-form :model="form" label-width="80px" size="small">
      <el-form-item label="关键字">
        <el-input v-model="form.keyword" placeholder="名称 / 编号 / 条码" clearable />
      </el-form-item>

      <el-form-item label="商品分类">
        <el-select v-model="form.category" placeholder="请选择分类" clearable>
          <el-option v-for="category in options.categories" :key="category" :label="category" :value="category" />
        </el-select>
      </el-form-item>

      <el-form-item label="商品品牌">
        <el-select v-model="form.brand" placeholder="请选择品牌" clearable>
          <el-option v-for="brand in options.brands" :key="brand" :label="brand" :value="brand" />
        </el-select>
      </el-form-item>

      <el-form-item label="商品类型">
        <el-select v-model="form.type" placeholder="请选择类型" clearable>
          <el-option v-for="type in options.types" :key="type" :label="type" :value="type" />
        </el-select>
      </el-form-item>
    </el-form>

    <div class="popup-footer">
      <el-button size="small" @click="resetForm">重置</el-button>
      <el-button size="small" type="primary" @click="submitSearch">搜索</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Close } from '@element-plus/icons-vue'

const props = defineProps({
  options: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'search'])

const form = ref({
  keyword: '',
  category: '',
  brand: '',
  type: ''
})

const closePopup = () => emit('close')
const resetForm = () => {
  form.value = { keyword: '', category: '', brand: '', type: '' }
}
const submitSearch = () => emit('search', { ...form.value })
</script>

<style scoped>
.search-popup-card {
  width: 320px;
  background: #fff;
  border: 1px solid #dcdfe6;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  margin-bottom: 10px;
}
.popup-footer {
  text-align: right;
  margin-top: 10px;
}
</style>
