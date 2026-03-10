<template>
  <el-popover class="nodTree" ref="nodTreeRef" width="230px">
    <template #reference>
      <el-input v-model="label" :placeholder="placeholder" @keydown="inputEvent" readonly>
        <template #suffix>
          <i
            :class="['el-input__icon', label != '' ? 'el-icon-circle-close' : 'el-icon-arrow-down']"
            @click.stop="delModel"
          ></i>
        </template>
      </el-input>
    </template>
    <el-tree
      ref="treeRef"
      @check="setTree"
      @node-click="setTree"
      :data="treeData"
      :props="treeProps"
      :node-key="treeKey"
      :current-node-key="checked"
      :show-checkbox="checkbox"
      :check-strictly="checkStrictly"
      :default-checked-keys="checkeds"
      :default-expanded-keys="expanded"
      :highlight-current="true"
    ></el-tree>
  </el-popover>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, getCurrentInstance, nextTick } from 'vue'
import type { ElTree, ElPopover } from 'element-plus'

interface TreeNode {
  id?: any
  pid?: any
  name?: string
  [key: string]: any
}

const props = defineProps<{
  value: any
  treeData: any[]
  treeKey?: string
  treeProps?: { label: string; children: string }
  placeholder?: string
  checkStrictly?: boolean
}>()

const emit = defineEmits<{
  (e: 'input', value: any): void
}>()

const instance = getCurrentInstance()
const nodTreeRef = ref<InstanceType<typeof ElPopover> | null>(null)
const treeRef = ref<InstanceType<typeof ElTree> | null>(null)

const label = ref('')
const checked = ref<any>(null)
const checkeds = ref<any[]>([])
const expanded = ref<any[]>([])
const checkbox = ref(false)

// 获取全局属性
const $lib = instance?.appContext.config.globalProperties.$lib

// 扁平化数据
const flatTree = computed<TreeNode[]>(() => {
  const flat = $lib?.flatten(props.treeData) || []
  return flat
})

// 初始化
const init = () => {
  if (props.value != null) {
    //数据模式
    if (Array.isArray(props.value)) {
      checkbox.value = true
      checkeds.value = props.value
    } else {
      checkbox.value = false
      checked.value = props.value
    }
    //设置展开项
    const items = checkbox.value ? checkeds.value : [checked.value]
    for (const item of items) {
      // 使用不同的变量名，避免与循环变量冲突
      const foundItem = flatTree.value.find((obj: TreeNode) => obj.id == item) //匹配数据
      if (foundItem != undefined && foundItem.hasOwnProperty('pid') && foundItem.pid != null) {
        expanded.value.push(foundItem.pid)
      }
    }
  }
}

// 设置文本信息
const setInput = () => {
  const text: string[] = []
  const items = checkbox.value ? checkeds.value : [checked.value]
  for (const item of items) {
    // 使用不同的变量名
    const foundItem = flatTree.value.find((obj: TreeNode) => obj.id == item) //匹配数据
    if (foundItem != undefined && foundItem.name) {
      text.push(foundItem.name)
    }
  }
  label.value = text.join(',') //赋值数据
}

// 设置树数据
const setTree = (data: any) => {
  if (checkbox.value) {
    if (treeRef.value) {
      checkeds.value = treeRef.value.getCheckedKeys() as any[]
      emit('input', checkeds.value)
    }
  } else {
    checked.value = data?.id
    emit('input', checked.value)
    if (nodTreeRef.value) {
      ;(nodTreeRef.value as any).showPopper = false
    }
  }
}

// 删除绑定数据
const delModel = () => {
  label.value = ''
  if (checkbox.value) {
    checkeds.value = []
    if (treeRef.value) {
      treeRef.value.setCheckedKeys([])
    }
  } else {
    checked.value = null
    if (treeRef.value) {
      treeRef.value.setCurrentKey(undefined)
    }
  }
  emit('input', checkbox.value ? [] : null)
  if (nodTreeRef.value) {
    ;(nodTreeRef.value as any).showPopper = false
  }
}

// 输入框-键盘事件
const inputEvent = (e: Event | KeyboardEvent) => {
  //ESC优化
  const keyEvent = e as KeyboardEvent
  if (nodTreeRef.value && (nodTreeRef.value as any).showPopper && keyEvent.keyCode == 27) {
    keyEvent.stopPropagation()
  }
}

// 监听 value 变化
watch(
  () => props.value,
  (val: any) => {
    if (checkbox.value) {
      if ($lib?.comparison(val, checkeds.value)) {
        checkeds.value = Array.isArray(val) ? val : []
        if (treeRef.value) {
          treeRef.value.setCheckedKeys(val, true)
        }
      }
    } else {
      if (val != checked.value) {
        checked.value = val
        if (treeRef.value) {
          treeRef.value.setCurrentKey(val)
        }
      }
    }
  },
  { deep: true }
)

// 监听 checked 变化
watch(checked, () => {
  setInput()
})

// 监听 checkeds 变化
watch(
  checkeds,
  () => {
    setInput()
  },
  { deep: true }
)

// 监听 treeData 变化，确保数据准备好后再初始化
watch(
  () => props.treeData,
  () => {
    nextTick(() => {
      init()
      setInput()
    })
  },
  { deep: true }
)

onMounted(() => {
  // 使用 nextTick 确保所有计算属性都已初始化
  nextTick(() => {
    init()
    setInput()
  })
})
</script>

<style scoped>
.nodTree {
  display: inline-block;
}
</style>
