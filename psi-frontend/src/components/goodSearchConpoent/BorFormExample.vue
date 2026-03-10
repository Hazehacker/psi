<template>
  <div class="borForm area">
    <div class="layout">
      <!-- 使用新的搜索组件 -->
      <GoodSearchForm 
        v-model="searchFrom"
        :default-days="defaultDays"
        @search="record(1)"
      />
      
      <el-button-group>
        <template v-if="tableSelection.length != 0">
          <template v-if="$lib.getUserRoot(store.root, 'bor', 'examine')">
            <el-popover
              class="btnGroupPopover"
              type="center"
              popper-class="blockPopover"
              trigger="click"
            >
              <template v-slot:reference>
<el-button type="info" >操作</el-button>
</template>
              <ul>
                <li @click="examine(0)">审核</li>
                <li @click="examine(1)">反审核</li>
              </ul>
            </el-popover>
          </template>
          <template v-if="$lib.getUserRoot(store.root, 'bor', 'del')">
            <el-button @click="del(tableSelection)" type="info">删除</el-button>
          </template>
        </template>
        <template v-if="$lib.getUserRoot(store.root, 'bor', 'batch')">
          <el-button @click="batch.dialog = true">批量</el-button>
        </template>
        <el-button @click="reload">刷新</el-button>
      </el-button-group>
    </div>
    <el-divider></el-divider>
    
    <!-- 表格部分保持不变 -->
    <el-table
      :data="tableData"
      height="calc(100% - 90px)"
      @sort-change="sortTableFun"
      @selection-change="selectionChange"
      border
      v-madeTable
    >
      <!-- 表格列定义... -->
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
      layout="prev,pager,next,jumper,sizes,total,slot"
    >
      <PageStatus :config="page.status" :model="tableData"></PageStatus>
    </el-pagination>
    
    <!-- 对话框部分... -->
  </div>
</template>

<script>
import GoodSearchForm from '@/components/goodSearchConpoent'
import PageStatus from "@/components/lib/PageStatus"

export default {
  name: "BorForm",
  components: {
    GoodSearchForm,
    PageStatus,
  },
  data() {
    return {
      // 搜索表单数据
      searchFrom: {
        goods: "",
        number: "",
        supplier: null,
        people: null,
        user: null,
        startTime: "",
        endTime: "",
        startArrival: "",
        endArrival: "",
        examine: "",
        state: "",
        data: "",
      },
      // 默认日期范围天数（从系统配置读取）
      defaultDays: 30,
      columns: {
        column: '',
        order: ''
      },
      tableData: [],
      tableSelection: [],
      page: {
        current: 1,
        total: 0,
        size: 30,
        sizes: [30, 60, 90, 150, 300],
        count: 5,
        status: [
          { text: "总单据金额", type: "sum", key: "total" },
          { text: "总实际金额", type: "sum", key: "actual" },
        ],
      },
      batch: {
        dialog: false,
        active: "import",
      },
    }
  },
  created() {
    this.init()
    this.record(1)
  },
  computed: {
    store() {
      return this.$store.state
    },
  },
  methods: {
    // 初始化
    init() {
      // 从系统配置获取默认天数
      this.defaultDays = this.store.sys?.fun?.days || 30
    },
    
    // 列表排序
    sortTableFun(column) {
      this.columns.column = column.prop
      if (column.prop) {
        if (column.order == 'ascending') {
          this.columns.order = 'asc'
        } else if (column.order == 'descending') {
          this.columns.order = 'desc'
        }
      }
      this.record(1)
    },
    
    // 获取数据
    record(page) {
      page == 0 || (this.page.current = page)
      const parm = Object.assign(
        {
          page: this.page.current,
          limit: this.page.size,
        },
        this.columns,
        this.searchFrom
      )
      this.$axios.post("bor/record", parm).then((result) => {
        if (result.state == "success") {
          this.tableData = result.info
          this.page.total = result.count
        } else if (result.state == "error") {
          this.$message({
            type: "warning",
            message: result.info,
          })
        } else {
          this.$message({
            type: "error",
            message: "[ ERROR ] 服务器响应超时!",
          })
        }
      })
    },
    
    // 审核单据
    examine(type) {
      const parm = []
      this.tableData.forEach((item) => {
        if (this.tableSelection.indexOf(item.id) != -1) {
          if (item.examine == 0) {
            type == 0 && parm.push(item.id)
          } else {
            type == 1 && parm.push(item.id)
          }
        }
      })
      if (parm.length == 0) {
        this.$message({
          type: "warning",
          message: "无可操作单据!",
        })
      } else {
        this.$axios
          .post("bor/examine", {
            parm: parm,
          })
          .then((result) => {
            if (result.state == "success") {
              this.record(0)
              this.$message({
                type: "success",
                message: "操作单据成功!",
              })
            } else if (result.state == "error") {
              this.record(0)
              this.$message({
                type: "warning",
                message: result.info,
              })
            } else {
              this.$message({
                type: "error",
                message: "[ ERROR ] 服务器响应超时!",
              })
            }
          })
      }
    },
    
    // 删除数据
    del(parm) {
      this.$confirm("您确定要删除选中数据吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .post("bor/del", {
              parm: parm,
            })
            .then((result) => {
              if (result.state == "success") {
                this.record(0)
                this.$message({
                  type: "success",
                  message: "删除成功!",
                })
              } else if (result.state == "error") {
                this.$message({
                  type: "warning",
                  message: result.info,
                })
              } else {
                this.$message({
                  type: "error",
                  message: "[ ERROR ] 服务器响应超时!",
                })
              }
            })
        })
        .catch(() => {})
    },
    
    // 表格选中数据改变
    selectionChange(parm) {
      const data = []
      for (const parmVo of parm) {
        data.push(parmVo.id)
      }
      this.tableSelection = data
    },
    
    // 页面刷新
    reload() {
      this.$bus.emit("homeReload", this.$options.name)
      this.$message({
        type: "success",
        message: "页面刷新成功!",
      })
    },
  },
}
</script>

<style>
.layout {
  display: flex;
  justify-content: space-between;
}
</style>

