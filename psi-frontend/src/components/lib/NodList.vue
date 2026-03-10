<template>
  <el-popover :width="218" class="nodList" ref="nodListRef" :disabled="disabled" trigger="click">
    <template #reference>
      <el-input
        v-model="label"
        ref="labelRef"
        :placeholder="placeholder"
        tabindex="-1"
        @keydown="inputEvent"
        :disabled="disabled"
        readonly
      >
        <template #suffix>
          <div @click.stop="delModel" class="common-icon">
            <el-icon v-if="label == ''"><ArrowDown /></el-icon>
            <el-icon v-else><CircleClose /></el-icon>
          </div>
          <!-- <i
            :class="['el-input__icon', label == '' ? 'el-icon-arrow-down' : 'el-icon-circle-close']"
            @click.stop="delModel"
          ></i> -->
        </template>
      </el-input>
    </template>
    <div class="selectBox">
      <el-input
        v-model="query"
        ref="searchRef"
        @keydown.stop="searchEvent"
        placeholder="F1 输入内容回车搜索"
        prefix-icon="el-icon-search"
        size="mini"
        tagkey="search"
      ></el-input>
      <template v-if="optionList.length > 0">
        <ul class="list">
          <template v-for="(option, index) in optionList" :key="option.id">
            <li
              :class="[{ selected: option.id == value }, { foucs: index == foucs }]"
              @click="setOption(option)"
            >
              {{ option.name }}
            </li>
          </template>
        </ul>
      </template>
      <template v-else>
        <p class="emptyList">未查询到数据</p>
      </template>
      <div class="operate">
        <el-pagination
          v-model:current-page="page.current"
          v-model:page-size="page.size"
          :total="page.total"
          @current-change="record(0)"
          layout="prev,next,total"
          background
        >
        </el-pagination>
      </div>
    </div>
  </el-popover>
</template>
<script setup>
import { ref, reactive, watch, onMounted, getCurrentInstance } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  value: {
    required: true
  },
  action: {
    required: true
  },
  scene: {
    required: true
  },
  parm: {
    default: () => {
      return {}
    }
  },
  disabled: {
    default: false
  },
  placeholder: {
    default: '请点击选择数据'
  }
})

const emit = defineEmits(['input', 'change'])

const instance = getCurrentInstance()
const nodListRef = ref(null)
const labelRef = ref(null)
const searchRef = ref(null)

const id = ref(null)
const label = ref('')
const query = ref('')
const optionList = ref([])
const foucs = ref(0)
const page = reactive({
  current: 1,
  total: 0,
  size: 6
})

// 获取全局属性
const $lib = instance?.appContext.config.globalProperties.$lib
const $axios = instance?.appContext.config.globalProperties.$axios

// 初始化组件
const init = () => {
  id.value = null
  label.value = ''
  query.value = ''
  page.current = 1
  page.total = 0
  page.size = 6
  record(1) //查询列表数据
  emit('input', null)
  emit('change', null)
}

// 查询列表数据
const record = (pageNum) => {
  if (pageNum !== 0) {
    page.current = pageNum
  }
  const parm = Object.assign({}, props.parm, {
    page: page.current,
    limit: page.size,
    query: query.value
  })
  $axios?.post(props.action, parm).then((result) => {
    if (result.state == 'success') {
      optionList.value = result.info
      page.total = result.count
      //激活键盘事件
      if (
        optionList.value.length > 0 &&
        document.activeElement.getAttribute('tagkey') == 'search'
      ) {
        foucs.value = 0
        labelRef.value?.focus()
      }
    } else if (result.state == 'error') {
      ElMessage({ type: 'warning', message: result.info })
    } else {
      ElMessage({ type: 'error', message: '[ ERROR ] 服务器响应超时!' })
    }
  })
}

// 查询指定数据
const get = () => {
  if (props.value != null && !$lib?.validate('empty', props.value)) {
    const find = optionList.value.find((option) => option.id == props.value)
    if (find == undefined) {
      $axios
        ?.post('service/getScene', {
          id: props.value,
          scene: props.scene
        })
        .then((result) => {
          if (result.state == 'success') {
            if (result.info == null) {
              ElMessage({ type: 'warning', message: '[ NodList ]指定数据查询结果为空!' })
            } else {
              setOption(result.info)
            }
          } else if (result.state == 'error') {
            ElMessage({ type: 'warning', message: result.info })
          } else {
            ElMessage({ type: 'error', message: '[ ERROR ] 服务器响应超时!' })
          }
        })
    } else {
      setOption(find)
    }
  }
}

// 设置选中
const setOption = (option) => {
  if (option.id != id.value) {
    id.value = option.id
    label.value = option.name
    emit('input', option.id)
    emit('change', option)
  }
  nodListRef.value.showPopper = false
}

// 删除绑定数据
const delModel = () => {
  id.value = null
  label.value = ''
  emit('input', null)
  emit('change', null)
  nodListRef.value.showPopper = false
}

// 组件-切换显示
const switchState = () => {
  labelRef.value?.focus()
  nodListRef.value.showPopper = !nodListRef.value.showPopper
}

// 输入框-键盘事件
const inputEvent = (e) => {
  const keyCode = e.keyCode
  if (nodListRef.value.showPopper) {
    if (keyCode == 13) {
      //回车
      setOption(optionList.value[foucs.value])
    } else if (keyCode == 27) {
      //ESC
      e.stopPropagation()
    } else if (keyCode == 38) {
      //上
      optionList.value.hasOwnProperty(foucs.value - 1) && foucs.value--
    } else if (keyCode == 40) {
      //下
      optionList.value.hasOwnProperty(foucs.value + 1) && foucs.value++
    } else if (keyCode == 112) {
      //F1
      searchRef.value?.focus()
      e.preventDefault()
      e.stopPropagation()
    }
  } else {
    if (keyCode == 13) {
      //回车
      nodListRef.value.showPopper = true
    }
  }
  //DEL
  keyCode == 46 && delModel()
}

// 搜索框-键盘事件
const searchEvent = (e) => {
  const keyCode = e.keyCode
  if (keyCode == 13) {
    //回车
    record(1)
  } else if (keyCode == 27) {
    //ESC
    labelRef.value?.focus()
    nodListRef.value.showPopper = false
  } else if (keyCode == 112) {
    //F1
    e.preventDefault()
  }
}

// 监听 value 变化
watch(
  () => props.value,
  (val, old) => {
    val == old || get()
  }
)

onMounted(() => {
  record(1) //查询列表数据
  get() //查询指定数据
})
</script>

<style scoped>
.nodList {
  display: inline-block;
}
.common-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}
.selectBox .list {
  padding: 6px 0;
  list-style: none;
  box-sizing: border-box;
}
.selectBox .list li {
  color: #606266;
  height: 34px;
  cursor: pointer;
  padding: 0 20px;
  overflow: hidden;
  line-height: 34px;
  white-space: nowrap;
  box-sizing: border-box;
  text-overflow: ellipsis;
}
.selectBox .list .selected {
  color: #409eff;
}
.selectBox .list .foucs {
  background: #f8f8f8;
}
.selectBox .list li:hover {
  background: #f5f7fa;
}
.emptyList {
  margin: 6px 0;
  font-size: 14px;
  line-height: 28px;
  text-align: center;
  background: #f2f2f2;
  letter-spacing: 1px;
}
.operate {
  padding-top: 6px;
  border-top: 1px solid #dcdfe6;
}
</style>
