<template>
  <div class="stockDetail">
    <el-dialog
      v-model="dialog"
      title="库存详情"
      width="800px"
      @closed="close"
      :append-to-body="true"
      v-madeDialog
    >
      <el-table :data="tableData" height="320px" border v-madeTable>
        <el-table-column
          prop="sourceData.frameData.name"
          label="所属组织"
          align="center"
          width="120px"
        ></el-table-column>
        <el-table-column
          prop="sourceData.time"
          label="操作时间"
          align="center"
          width="120px"
        ></el-table-column>
        <el-table-column
          prop="extension.type"
          label="单据类型"
          align="center"
          width="120px"
        ></el-table-column>
        <el-table-column
          prop="sourceData.number"
          label="单据编号"
          align="center"
          min-width="200px"
        ></el-table-column>
        <el-table-column
          prop="extension.direction"
          label="操作类型"
          align="center"
          width="100px"
        ></el-table-column>
        <el-table-column
          prop="nums"
          label="操作数量"
          align="center"
          width="100px"
        ></el-table-column>
      </el-table>
      <el-pagination
        class="tablePagination"
        v-model:current-page="page.current"
        :total="page.total"
        v-model:page-size="page.size"
        :page-sizes="page.sizes"
        :pager-count="page.count"
        @size-change="record(1)"
        @current-change="record(0)"
        layout="prev,pager,next,jumper,sizes,total"
      >
      </el-pagination>
      <template #footer>
        <span class="dialog-footer">
        <el-popover
          ref="searchPopover"
          popper-class="searchPopover"
          placement="top-start"
        >
          <el-form class="searchFrom" ref="searchFrom" inline>
            <el-form-item>
              <el-input
                placeholder="请输入单据编号"
                v-model="searchFrom.number"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="searchFrom.type"
                placeholder="请选择单据类型"
                multiple
                collapse-tags
              >
                <el-option label="采购单" value="buy"></el-option>
                <el-option label="采购退货单" value="bre"></el-option>
                <el-option label="销售单" value="sell"></el-option>
                <el-option label="销售退货单" value="sre"></el-option>
                <el-option label="调拨单-出" value="swapOut"></el-option>
                <el-option label="调拨单-入" value="swapEnter"></el-option>
                <el-option label="其它入库单" value="entry"></el-option>
                <el-option label="其它出库单" value="extry"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="searchFrom.startTime"
                placeholder="请输入开始日期"
                value-format="yyyy-MM-dd"
                type="date"
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="searchFrom.endTime"
                placeholder="请输入结束日期"
                value-format="yyyy-MM-dd"
                type="date"
              ></el-date-picker>
            </el-form-item>
            <el-divider></el-divider>
            <el-button
              class="searchBtn"
              icon="el-icon-search"
              @click="record(1)"
            ></el-button>
          </el-form>
          <template #reference>
            <el-button icon="el-icon-more"></el-button>
          </template>
        </el-popover>
        <el-button @click="exports">导出</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  condition: {
    required: true,
    type: Object,
  },
})

const emit = defineEmits(['destroy'])

const dialog = ref(true)
const searchFrom = reactive({
  number: "",
  type: [],
  startTime: "",
  endTime: "",
})
const tableData = ref([])
const page = reactive({
  current: 1,
  total: 0,
  size: 10,
  sizes: [10, 20, 50, 100, 200],
  count: 5,
})

const searchPopover = ref(null)

//读取数据中心
const storeState = computed(() => store.state)

//获取数据
const record = () => {
  const parm = Object.assign(
    {
      page: page.current,
      limit: page.size,
    },
    searchFrom,
    props.condition
  )
  window.$axios.post("stock/detailRecord", parm).then((result) => {
    if (result.state == "success") {
      tableData.value = result.info
      page.total = result.count
      if (searchPopover.value) {
        searchPopover.value.showPopper = false
      }
    } else if (result.state == "error") {
      ElMessage({
        type: "warning",
        message: result.info,
      })
    } else {
      ElMessage({
        type: "error",
        message: "[ ERROR ] 服务器响应超时!",
      })
    }
  })
}

//导出数据
const exports = () => {
  ElMessage({
    type: "success",
    message: "[ 导出数据 ] 请求中...",
  })
  const parm = window.$lib.objToParm(
    Object.assign({}, searchFrom, props.condition),
    true
  )
  setTimeout(() => {
    window.open(window.$base.web + "stock/detailExports?" + parm)
  }, 1000)
}

//弹层关闭事件
const close = () => {
  emit("destroy", true)
}

onMounted(() => {
  record()
})
</script>
<style scoped>
/* 操作按钮浮动 */
.dialog-footer > span {
  float: left;
}
</style>
