<template>
  <el-dialog v-model="visible" title="盘点库存" width="420px" @close="handleClose">
    <transition name="el-fade-in">
      <template v-if="visible">
        <div v-if="mode === 'select'" class="exportGroup">
          <div class="item" @click="onBuildEntry">
            <el-icon><Document /></el-icon>
            <p>盘盈单</p>
          </div>
          <div class="item" @click="onBuildExtry">
            <el-icon><Document /></el-icon>
            <p>盘亏单</p>
          </div>
        </div>

        <div v-else class="profitList">
          <el-table :data="tableData || []" style="width: 100%">
            <el-table-column prop="name" label="商品名称" width="120" />
            <el-table-column prop="number" label="商品编号" width="100" />
            <el-table-column prop="warehouse" label="仓库" width="80" />
            <el-table-column prop="unit" label="单位" width="60" />
            <el-table-column prop="nums" label="数量" width="80">
              <template #default="scope">
                <span :style="{ color: scope.row.nums > 0 ? '#67c23a' : '#f56c6c' }">
                  {{ scope.row.nums > 0 ? `+${scope.row.nums}` : scope.row.nums }}
                </span>
              </template>
            </el-table-column>
          </el-table>
          <div style="text-align: right; margin-top: 12px">
            <el-button @click="handleClose">关闭</el-button>
            <el-button type="primary" @click="handleConfirm">生成</el-button>
          </div>
        </div>
      </template>
    </transition>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 导入库存盘点API
import { getInventoryPlus, getInventoryReduce } from '@/apis/warehouse/check/index'
import type { InventoryVerifyDTO, InventoryVerifyRequest } from '@/apis/warehouse/check/type'

interface Props {
  modelValue?: boolean
  mode?: 'select' | 'list'
  tableData?: any[]
  inventoryVerifyData?: InventoryVerifyDTO[] // 盘点数据
  currentType?: 'profit' | 'loss' // 当前类型：盘盈或盘亏
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: false,
  mode: 'select',
  tableData: () => [],
  inventoryVerifyData: () => [],
  currentType: 'profit'
})

const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
  (e: 'build-entry', data?: any[]): void
  (e: 'build-extry', data?: any[]): void
  (e: 'export-verify'): void
  (e: 'confirm', data: any[]): void
  (e: 'close'): void
}>()

const visible = ref<boolean>(props.modelValue)

watch(
  () => props.modelValue,
  (v) => {
    visible.value = v
  }
)

watch(visible, (v) => {
  emit('update:modelValue', v)
})

// 盘盈单
async function onBuildEntry() {
  if (!props.inventoryVerifyData || props.inventoryVerifyData.length === 0) {
    ElMessage.warning('请先输入盘点数据')
    return
  }

  // 过滤出盘盈数据
  const profitData = props.inventoryVerifyData.filter(
    (item) => item.inventoryDifference && item.inventoryDifference > 0
  )

  if (profitData.length === 0) {
    ElMessage.info('未发现盘盈数据')
    return
  }

  try {
    const verifyRequest: InventoryVerifyRequest = {
      inventoryVerifyList: profitData
    }

    await getInventoryPlus(
      verifyRequest,
      (data) => {
        console.log('盘盈单数据获取成功:', data)
        // 将数据传递给父组件
        emit('build-entry', data.rows || [])
        visible.value = false
      },
      (error) => {
        console.error('获取盘盈单数据失败:', error)
        ElMessage.error('获取盘盈单数据失败: ' + (error.message || '未知错误'))
        // 即使API失败也继续流程
        emit('build-entry')
        visible.value = false
      }
    )
  } catch (error) {
    console.error('盘盈单处理错误:', error)
    ElMessage.error('盘盈单处理过程出错')
    // 出错时也继续流程
    emit('build-entry')
    visible.value = false
  }
}

// 盘亏单
async function onBuildExtry() {
  if (!props.inventoryVerifyData || props.inventoryVerifyData.length === 0) {
    ElMessage.warning('请先输入盘点数据')
    return
  }

  // 过滤出盘亏数据
  const lossData = props.inventoryVerifyData.filter(
    (item) => item.inventoryDifference && item.inventoryDifference < 0
  )

  if (lossData.length === 0) {
    ElMessage.info('未发现盘亏数据')
    return
  }

  try {
    const verifyRequest: InventoryVerifyRequest = {
      inventoryVerifyList: lossData.map((item) => ({
        ...item,
        inventoryDifference: Math.abs(item.inventoryDifference!) // 取绝对值
      }))
    }

    await getInventoryReduce(
      verifyRequest,
      (data) => {
        console.log('盘亏单数据获取成功:', data)
        // 将数据传递给父组件
        emit('build-extry', data.rows || [])
        visible.value = false
      },
      (error) => {
        console.error('获取盘亏单数据失败:', error)
        ElMessage.error('获取盘亏单数据失败: ' + (error.message || '未知错误'))
        // 即使API失败也继续流程
        emit('build-extry')
        visible.value = false
      }
    )
  } catch (error) {
    console.error('盘亏单处理错误:', error)
    ElMessage.error('盘亏单处理过程出错')
    // 出错时也继续流程
    emit('build-extry')
    visible.value = false
  }
}

function handleConfirm() {
  emit('confirm', props.tableData || [])
  visible.value = false
}

function handleClose() {
  emit('close')
  visible.value = false
}
</script>

<style scoped lang="scss">
.exportGroup {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}
.item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 16px;
  border-radius: 8px;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    background-color: #f5f7fa;
  }
}
.item p {
  margin: 8px 0 0;
}

// 为表格底部的按钮添加悬浮阴影
:deep(.el-button) {
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
  }
}
</style>
