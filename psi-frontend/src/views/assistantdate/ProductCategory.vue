<template>
  <section class="root-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="left-toolbar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入类别名称"
          style="width: 200px; margin-right: 10px"
          clearable
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
      <div class="right-toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增</el-button
        >
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新</el-button
        >
      </div>
    </div>

    <!-- 表格操作列演示 -->
    <div class="table-wrapper">
      <my-table
        :istabpage="false"
        :tabattr="tabattr"
        :tabdatacolumns="tabopercolumns"
        :tabdata="tabdata"
        :taboperbtns="taboperbtns"
        @taboper-click="handleOperation"
      />
    </div>

    <!-- 详情/编辑数据表单对话框 -->
    <my-form-dialog ref="formDialogEdit" :="formDialogEditProps" @confirm="onEditSubmit" />

    <!-- 新增数据表单对话框 -->
    <my-form-dialog ref="formDialogAdd" :="formDialogAddProps" @confirm="onAddSubmit" />
  </section>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'

// 引入组件和类型
import MyTable from '@/components/mytable/MyTable.vue'
import {
  createPageDTO,
  type PageDTO,
  type MyTableAttr,
  type MyTableColumn,
  type MyTableOperationsColumn,
  type MyTableOperationsBtn,
  createMyTableOperationsColumn
} from '@/components/mytable/type'

// 导入表单对话框组件和类型
import MyFormDialog from '@/components/mydialog/MyFormDialog.vue'
import type { MyFormDialogProps } from '@/components/mydialog/type'
import type {
  MyFormItemAttr,
  MyFormInputProps,
  MyFormInputNumberProps,
  MyFormSelectProps
} from '@/components/myform/type'

// 导入API
import {
  getCategoryTree,
  addCategory,
  updateCategory,
  deleteCategory,
  type ProductCategory,
  type CategoryTreeNode
} from '@/apis/assistantdata/productCategory'

// 定义类别数据类型
interface CategoryData {
  id: string
  name: string
  sort: number
  remark: string
  parentId?: string
  children?: CategoryData[]
  // 添加唯一标识符
  uniqueId?: string
}

// 搜索关键词
const searchKeyword = ref('')

// 定义表格属性 - 启用树形表格和网格样式
const tabattr: MyTableAttr = {
  height: 400,
  'row-key': 'uniqueId',
  'tree-props': { children: 'children', hasChildren: 'hasChildren' },
  stripe: true, // 斑马纹
  border: true // 边框
}

// 定义列数据 - 类别管理专用列
const tabdatacolumns: MyTableColumn[] = [
  {
    prop: 'name',
    label: '类别名称',
    'min-width': 150,
    align: 'left'
  },
  {
    prop: 'sort',
    label: '类别排序',
    width: '100px',
    align: 'center'
  },
  {
    prop: 'remark',
    label: '备注信息',
    width: '200px',
    'show-overflow-tooltip': true,
    align: 'left'
  }
]

// 定义表格数据
const tabdata = ref<PageDTO<CategoryData>>(createPageDTO())

// 原始树形数据
const treeData = ref<CategoryData[]>([])

// 备份原始数据用于搜索后恢复
const originalTreeData = ref<CategoryData[]>([])

// 加载状态
const loading = ref(false)

/**
 * 生成唯一ID
 */
function generateUniqueId(): string {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

/**
 * 将后端树形数据转换为前端需要的格式，并按照sort字段排序
 */
function transformCategoryTree(node: CategoryTreeNode): CategoryData {
  const transformed: CategoryData = {
    id: node.id,
    name: node.name,
    sort: node.sort,
    remark: node.data || '',
    parentId: node.pid,
    uniqueId: generateUniqueId()
  }

  if (node.tnChildren && node.tnChildren.length > 0) {
    // 对子节点按照 sort 字段从小到大排序
    const sortedChildren = node.tnChildren
      .sort((a, b) => a.sort - b.sort)
      .map((child) => transformCategoryTree(child))

    transformed.children = sortedChildren
  }

  return transformed
}

/**
 * 从树形数据中删除节点及其所有子节点
 */
function deleteNodeFromTree(nodes: CategoryData[], uniqueId: string): boolean {
  for (let i = 0; i < nodes.length; i++) {
    if (nodes[i].uniqueId === uniqueId) {
      // 找到节点，直接删除
      nodes.splice(i, 1)
      return true
    }

    // 递归在子节点中查找
    if (nodes[i].children && nodes[i].children!.length > 0) {
      const deleted = deleteNodeFromTree(nodes[i].children!, uniqueId)
      if (deleted) return true
    }
  }
  return false
}

/**
 * 从树中移除节点（不删除，用于移动操作）
 */
function removeNodeFromTree(nodes: CategoryData[], uniqueId: string): CategoryData | null {
  for (let i = 0; i < nodes.length; i++) {
    if (nodes[i].uniqueId === uniqueId) {
      // 找到节点，移除并返回
      return nodes.splice(i, 1)[0]
    }

    // 递归在子节点中查找
    if (nodes[i].children && nodes[i].children!.length > 0) {
      const removed = removeNodeFromTree(nodes[i].children!, uniqueId)
      if (removed) return removed
    }
  }
  return null
}

/**
 * 根据ID查找节点并返回节点及其父节点数组
 */
function findNodeAndParentById(
  nodes: CategoryData[],
  id: string
): { node: CategoryData; parent: CategoryData[] } | null {
  for (let i = 0; i < nodes.length; i++) {
    if (nodes[i].id === id) {
      return { node: nodes[i], parent: nodes }
    }

    if (nodes[i].children && nodes[i].children!.length > 0) {
      const result = findNodeAndParentById(nodes[i].children!, id)
      if (result) return result
    }
  }
  return null
}

/**
 * 加载数据 - 从后端API加载
 */
async function loadData() {
  try {
    loading.value = true
    const response = await getCategoryTree('0')

    if (response.code === 10000 && response.data) {
      // 转换数据格式
      const rootNode = response.data
      let dataToLoad: CategoryData[] = []

      if (rootNode.tnChildren && rootNode.tnChildren.length > 0) {
        // 对根节点的子节点按照 sort 字段排序
        const sortedChildren = rootNode.tnChildren
          .sort((a, b) => a.sort - b.sort)
          .map((child) => transformCategoryTree(child))

        dataToLoad = sortedChildren
      }

      // 保存原始树形数据
      treeData.value = JSON.parse(JSON.stringify(dataToLoad))
      originalTreeData.value = JSON.parse(JSON.stringify(dataToLoad))

      // 设置表格数据 - 直接使用树形数据
      tabdata.value = {
        pageIndex: 1,
        pageSize: 10,
        total: calculateTotalNodes(treeData.value),
        rows: treeData.value
      }
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error: any) {
    console.error('加载数据失败:', error)
    // 更详细的错误信息
    const errorMsg = error?.message || error?.data?.message || '加载数据失败，请检查网络连接'
    ElMessage.error(`加载数据失败: ${errorMsg}`)
  } finally {
    loading.value = false
  }
}

/**
 * 计算树形数据中的节点总数
 */
function calculateTotalNodes(nodes: CategoryData[]): number {
  let count = 0
  nodes.forEach((node) => {
    count++ // 当前节点
    if (node.children && node.children.length > 0) {
      count += calculateTotalNodes(node.children) // 递归计算子节点
    }
  })
  return count
}

/**
 * 搜索功能 - 显示所有包含搜索关键词的行
 */
function handleSearch() {
  if (!searchKeyword.value.trim()) {
    // 如果搜索关键词为空，恢复原始数据
    treeData.value = JSON.parse(JSON.stringify(originalTreeData.value))
  } else {
    // 根据关键词过滤数据 - 显示所有匹配的行
    const filteredData = filterAllMatchingNodes(originalTreeData.value, searchKeyword.value.trim())
    treeData.value = filteredData
  }

  // 更新表格数据
  tabdata.value.rows = treeData.value
  tabdata.value.total = calculateTotalNodes(treeData.value)
}

/**
 * 过滤所有匹配的节点，显示所有包含搜索关键词的行
 */
function filterAllMatchingNodes(nodes: CategoryData[], keyword: string): CategoryData[] {
  const result: CategoryData[] = []

  nodes.forEach((node) => {
    // 检查当前节点是否匹配
    const isMatch = node.name.includes(keyword)

    // 递归检查子节点
    let matchingChildren: CategoryData[] = []
    if (node.children && node.children.length > 0) {
      matchingChildren = filterAllMatchingNodes(node.children, keyword)
    }

    // 如果当前节点匹配，保留该节点（包含子节点）
    if (isMatch) {
      const newNode = { ...node }
      // 保留子节点，以便显示完整的匹配行
      if (matchingChildren.length > 0) {
        newNode.children = matchingChildren
      }
      result.push(newNode)
    } else if (matchingChildren.length > 0) {
      // 如果当前节点不匹配但有匹配的子节点，显示匹配的子节点
      result.push(...matchingChildren)
    }
  })

  return result
}

/**
 * 刷新功能 - 重置搜索并重新加载数据
 */
function handleRefresh() {
  searchKeyword.value = ''
  loadData()
  ElMessage.success('数据已刷新')
}

/**
 * 组件挂载钩子函数
 */
onMounted(() => {
  loadData()
})

// #region 表格操作栏需要处理的逻辑代码

// 定义列数据
const tabopercolumns: MyTableOperationsColumn[] = [
  // 操作列
  createMyTableOperationsColumn({
    'min-width': 150,
    align: 'center',
    fixed: 'right'
  }),
  // 其他列
  ...tabdatacolumns
]

// 定义对话框引用
const formDialogEdit = ref()
const formDialogAdd = ref()

// 生成类别选项数据（用于所属类别下拉框）
const categoryOptions = ref<Array<{ value: string; label: string }>>([])

/**
 * 生成树形结构的类别选项
 */
function generateTreeOptions(): Array<{ value: string; label: string }> {
  const options: Array<{ value: string; label: string }> = [{ value: '0', label: '根类别' }]

  // 递归函数生成树形选项
  function addTreeOptions(nodes: CategoryData[], level = 0) {
    nodes.forEach((node) => {
      const prefix = '　'.repeat(level) // 使用全角空格缩进
      options.push({
        value: node.id,
        label: `${prefix}${node.name}`
      })

      if (node.children && node.children.length > 0) {
        addTreeOptions(node.children, level + 1)
      }
    })
  }

  addTreeOptions(originalTreeData.value)

  return options
}

// 定义编辑对话框属性
const formDialogEditProps = reactive<MyFormDialogProps<CategoryData>>({
  title: '类别详情',
  submitText: '保存',
  cancelText: '取消',
  // 对话框表单数据
  data: reactive<CategoryData>({
    id: '',
    name: '',
    sort: 0,
    remark: '',
    parentId: '0'
  }),
  // 对话框表单属性
  formattr: {
    'label-width': '100px'
  },
  // 对话框表单域数据
  formitemdata: reactive<MyFormItemAttr[]>([
    {
      type: 'input',
      prop: 'name',
      label: '类别名称',
      required: true,
      rules: [{ required: true, message: '请输入类别名称', trigger: 'change' }],
      fprops: {
        placeholder: '请输入类别名称',
        clearable: true
      } as MyFormInputProps
    },
    {
      type: 'select',
      prop: 'parentId',
      label: '所属类别',
      required: true,
      rules: [{ required: true, message: '请选择所属类别', trigger: 'change' }],
      fprops: {
        placeholder: '请选择所属类别',
        options: categoryOptions.value
      } as MyFormSelectProps
    },
    {
      type: 'number',
      prop: 'sort',
      label: '类别排序',
      required: true,
      rules: [{ required: true, message: '请输入排序号', trigger: 'change' }],
      fprops: {
        width: '100%',
        placeholder: '请输入排序号',
        min: 0,
        max: 999,
        step: 1
      } as MyFormInputNumberProps
    },
    {
      type: 'input',
      prop: 'remark',
      label: '备注信息',
      fprops: {
        type: 'textarea',
        rows: 3,
        placeholder: '请输入备注信息',
        clearable: true
      } as MyFormInputProps
    }
  ])
})

// 定义新增对话框属性
const formDialogAddProps = reactive<MyFormDialogProps<CategoryData>>({
  title: '新增类别',
  submitText: '保存',
  cancelText: '取消',
  reset: true,
  // 对话框表单数据
  data: reactive<CategoryData>({
    id: '',
    name: '',
    sort: 0,
    remark: '',
    parentId: '0'
  }),
  // 对话框表单属性
  formattr: {
    'label-width': '100px'
  },
  // 对话框表单域数据
  formitemdata: reactive<MyFormItemAttr[]>([
    {
      type: 'input',
      prop: 'name',
      label: '类别名称',
      required: true,
      rules: [{ required: true, message: '请输入类别名称', trigger: 'change' }],
      fprops: {
        placeholder: '请输入类别名称',
        clearable: true
      } as MyFormInputProps
    },
    {
      type: 'select',
      prop: 'parentId',
      label: '所属类别',
      required: true,
      rules: [{ required: true, message: '请选择所属类别', trigger: 'change' }],
      fprops: {
        placeholder: '请选择所属类别',
        options: categoryOptions.value
      } as MyFormSelectProps
    },
    {
      type: 'number',
      prop: 'sort',
      label: '类别排序',
      required: true,
      rules: [{ required: true, message: '请输入排序号', trigger: 'change' }],
      fprops: {
        width: '100%',
        placeholder: '请输入排序号',
        min: 0,
        max: 999,
        step: 1
      } as MyFormInputNumberProps
    },
    {
      type: 'input',
      prop: 'remark',
      label: '备注信息',
      fprops: {
        type: 'textarea',
        rows: 3,
        placeholder: '请输入备注信息',
        clearable: true
      } as MyFormInputProps
    }
  ])
})

// 当前编辑的节点唯一ID
const editingNodeUniqueId = ref<string>('')
// 当前编辑的节点原始父ID
const editingNodeOriginalParentId = ref<string>('0')
// 当前编辑的节点数据
const editingNodeData = ref<CategoryData | null>(null)

// 定义操作列按钮数据 - 只有详情和删除
const taboperbtns = ref<MyTableOperationsBtn[]>([
  {
    evtname: 'info',
    text: '详情',
    attr: {
      type: 'primary',
      icon: 'icon-view'
    }
  },
  {
    evtname: 'delete',
    text: '删除',
    attr: {
      type: 'danger',
      icon: 'icon-delete'
    }
  }
])

/**
 * 根据唯一ID查找节点及其父节点
 */
function findNodeAndParent(
  nodes: CategoryData[],
  uniqueId: string
): { node: CategoryData; parent: CategoryData[] } | null {
  for (let i = 0; i < nodes.length; i++) {
    if (nodes[i].uniqueId === uniqueId) {
      return { node: nodes[i], parent: nodes }
    }

    if (nodes[i].children && nodes[i].children!.length > 0) {
      const result = findNodeAndParent(nodes[i].children!, uniqueId)
      if (result) return result
    }
  }
  return null
}

/**
 * 根据ID查找节点
 */
function findNodeById(nodes: CategoryData[], id: string): CategoryData | null {
  for (let i = 0; i < nodes.length; i++) {
    if (nodes[i].id === id) {
      return nodes[i]
    }

    if (nodes[i].children && nodes[i].children!.length > 0) {
      const result = findNodeById(nodes[i].children!, id)
      if (result) return result
    }
  }
  return null
}

/**
 * 获取节点的当前父ID
 */
function getNodeParentId(nodes: CategoryData[], uniqueId: string): string {
  const nodeInfo = findNodeAndParent(nodes, uniqueId)
  if (!nodeInfo) return '0'

  const { parent } = nodeInfo

  // 如果父节点是顶级数组，则返回0
  if (parent === nodes) {
    return '0'
  }

  // 否则返回父节点的ID
  if (parent.length > 0 && parent[0].parentId !== undefined) {
    return parent[0].parentId || '0'
  }

  return '0'
}

/**
 * 移动节点到新的父节点
 */
function moveNodeToNewParent(
  nodes: CategoryData[],
  nodeToMove: CategoryData,
  newParentId: string
): boolean {
  // 1. 从原位置移除节点
  const removedNode = removeNodeFromTree(nodes, nodeToMove.uniqueId!)
  if (!removedNode) {
    console.error('未找到要移动的节点')
    return false
  }

  // 2. 更新节点的parentId
  removedNode.parentId = newParentId

  // 3. 找到新的父节点
  let newParent: CategoryData[] = nodes
  if (newParentId !== '0') {
    const parentNodeInfo = findNodeAndParentById(nodes, newParentId)
    if (!parentNodeInfo) {
      console.error('未找到新的父节点')
      return false
    }
    if (!parentNodeInfo.node.children) {
      parentNodeInfo.node.children = []
    }
    newParent = parentNodeInfo.node.children
  }

  // 4. 将节点添加到新父节点
  newParent.push(removedNode)

  // 5. 对新父节点的子节点按sort排序
  newParent.sort((a, b) => a.sort - b.sort)

  return true
}

/**
 * 表格操作栏事件处理
 * @param index 当前操作行索引
 * @param row 当前操作行数据
 * @param evtname 当前操作名称
 */
function handleOperation(index: number, row: CategoryData, evtname: string) {
  switch (evtname) {
    case 'info':
      // 更新类别选项 - 使用树形结构
      categoryOptions.value = generateTreeOptions()
      // 更新表单中的选项
      const parentSelectItem = formDialogEditProps.formitemdata.find(
        (item) => item.prop === 'parentId'
      )
      if (parentSelectItem && parentSelectItem.fprops) {
        ;(parentSelectItem.fprops as MyFormSelectProps).options = categoryOptions.value
      }

      // 记录当前编辑的节点唯一ID和原始父ID
      editingNodeUniqueId.value = row.uniqueId || ''
      editingNodeOriginalParentId.value = getNodeParentId(treeData.value, row.uniqueId || '')
      editingNodeData.value = { ...row }

      // 填充表单数据
      Object.assign(formDialogEditProps.data, row)
      // 设置表单中的parentId为当前节点的实际父ID
      formDialogEditProps.data.parentId = editingNodeOriginalParentId.value
      formDialogEdit.value.openDialog(true)
      break
    case 'delete':
      ElMessageBox.confirm('确定要删除这个类别吗？删除后无法恢复！', '删除确认', {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          try {
            const response = await deleteCategory(row.id)
            if (response.code === 10000) {
              // 从树形数据中删除节点
              if (row.uniqueId) {
                deleteNodeFromTree(treeData.value, row.uniqueId)
                deleteNodeFromTree(originalTreeData.value, row.uniqueId)

                // 更新表格数据
                tabdata.value.rows = [...treeData.value]
                tabdata.value.total = calculateTotalNodes(treeData.value)

                // 如果当前在搜索状态，重新搜索
                if (searchKeyword.value.trim()) {
                  handleSearch()
                }

                ElMessage.success('删除成功')
              }
            } else {
              ElMessage.error(response.message || '删除失败')
            }
          } catch (error: any) {
            console.error('删除失败:', error)
            const errorMsg = error?.message || error?.data?.message || '删除失败，请重试'
            ElMessage.error(`删除失败: ${errorMsg}`)
          }
        })
        .catch(() => {})
      break
    default:
      break
  }
}

/**
 * 新增按钮处理
 */
function handleAdd() {
  // 更新类别选项 - 使用树形结构
  categoryOptions.value = generateTreeOptions()
  // 更新表单中的选项
  const parentSelectItem = formDialogAddProps.formitemdata.find((item) => item.prop === 'parentId')
  if (parentSelectItem && parentSelectItem.fprops) {
    ;(parentSelectItem.fprops as MyFormSelectProps).options = categoryOptions.value
  }

  // 清空表单数据
  Object.assign(formDialogAddProps.data, {
    id: '',
    name: '',
    sort: 0,
    remark: '',
    parentId: '0'
  })

  formDialogAdd.value.openDialog(true)
}

/**
 * 提交修改
 * @param data 表单数据
 */
async function onEditSubmit(data: CategoryData) {
  try {
    if (!editingNodeData.value) {
      ElMessage.error('未找到要修改的节点')
      return
    }

    const updateData: ProductCategory = {
      id: data.id,
      name: data.name,
      pid: data.parentId || '0',
      sort: data.sort,
      data: data.remark
    }

    const response = await updateCategory(updateData)
    if (response.code === 10000) {
      // 检查父类别是否发生变化
      const parentIdChanged = data.parentId !== editingNodeOriginalParentId.value

      if (parentIdChanged) {
        // 父类别发生变化，需要移动节点
        const moveSuccess = moveNodeToNewParent(
          treeData.value,
          editingNodeData.value,
          data.parentId || '0'
        )

        if (!moveSuccess) {
          ElMessage.warning('节点移动失败，将重新加载数据')
          // 如果移动失败，重新加载数据
          await loadData()
        } else {
          // 同时更新原始数据
          moveNodeToNewParent(originalTreeData.value, editingNodeData.value, data.parentId || '0')

          // 更新表格数据
          tabdata.value.rows = [...treeData.value]
          tabdata.value.total = calculateTotalNodes(treeData.value)
        }
      } else {
        // 父类别没有变化，只更新节点数据
        const nodeInfo = findNodeAndParent(treeData.value, editingNodeUniqueId.value)
        if (nodeInfo) {
          const { node } = nodeInfo
          node.name = data.name
          node.sort = data.sort
          node.remark = data.remark

          // 更新表格数据
          tabdata.value.rows = [...treeData.value]
        }

        // 同时更新原始数据
        const originalNodeInfo = findNodeAndParent(
          originalTreeData.value,
          editingNodeUniqueId.value
        )
        if (originalNodeInfo) {
          const { node: originalNode } = originalNodeInfo
          originalNode.name = data.name
          originalNode.sort = data.sort
          originalNode.remark = data.remark
        }
      }

      // 如果当前在搜索状态，重新搜索
      if (searchKeyword.value.trim()) {
        handleSearch()
      }

      // 关闭对话框
      formDialogEdit.value.closeDialog()
      ElMessage.success('修改成功')
    } else {
      ElMessage.error(response.message || '修改失败')
    }
  } catch (error: any) {
    console.error('修改失败:', error)
    const errorMsg = error?.message || error?.data?.message || '修改失败，请重试'
    ElMessage.error(`修改失败: ${errorMsg}`)
  }
}

/**
 * 提交新增
 * @param data 表单数据
 */
async function onAddSubmit(data: CategoryData) {
  try {
    const addData: Omit<ProductCategory, 'id'> = {
      name: data.name,
      pid: data.parentId || '0',
      sort: data.sort,
      data: data.remark
    }

    const response = await addCategory(addData)
    if (response.code === 10000) {
      // 重新加载数据以获取最新的树形结构
      await loadData()

      // 关闭对话框
      formDialogAdd.value.closeDialog()
      ElMessage.success('新增成功')
    } else {
      ElMessage.error(response.message || '新增失败')
    }
  } catch (error: any) {
    console.error('新增失败:', error)
    const errorMsg = error?.message || error?.data?.message || '新增失败，请重试'
    ElMessage.error(`新增失败: ${errorMsg}`)
  }
}

// #endregion
</script>

<style scoped>
.root-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 20px;
  background-color: #ffffff;
  box-sizing: border-box;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-bottom: 16px;
  padding: 16px 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #e8e8e8;
  flex-shrink: 0;
}

.left-toolbar {
  display: flex;
  align-items: center;
}

.right-toolbar {
  display: flex;
  gap: 10px;
}

.table-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #e8e8e8;
  overflow: hidden;
}

/* 美化表格容器 */
:deep(.my-table-container) {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #ffffff;
}

/* 美化表格标题 */
:deep(.my-table-title) {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 16px 20px;
  border-bottom: 1px solid #e8e8e8;
  font-weight: 600;
  color: #2c3e50;
  font-size: 16px;
}

/* 美化表格 */
:deep(.el-table) {
  flex: 1;
  background-color: #ffffff;
  border: none;
  /* 通过 CSS 实现紧凑尺寸 */
  font-size: 13px;
}

:deep(.el-table th) {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  color: #2c3e50;
  font-weight: 600;
  border-right: 1px solid #f0f0f0;
  border-bottom: 2px solid #e8e8e8;
  padding: 8px 0;
  font-size: 13px;
}

:deep(.el-table tr) {
  background-color: #ffffff;
  transition: all 0.3s ease;
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
  background-color: #f8f9fa !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-table .el-table__cell) {
  padding: 6px 0;
  border-right: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 13px;
}

/* 斑马纹样式 */
:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

/* 树形表格样式 */
:deep(.el-table .el-table__row--level-0) {
  background-color: #ffffff;
}

:deep(.el-table .el-table__row--level-1) {
  background-color: #fdfdfd;
}

:deep(.el-table .el-table__row--level-2) {
  background-color: #fafafa;
}

:deep(.el-table .el-table__row--level-3) {
  background-color: #f7f7f7;
}

/* 移除分页组件样式，因为不再需要 */
</style>

<style>
/* 全局样式 - 自定义按钮样式 */
.custom-btn-info,
.custom-btn-delete {
  background-color: #ffffff !important;
  border: 1px solid #dcdfe6 !important;
  color: #606266 !important;
  transition: all 0.3s ease;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
}

.custom-btn-info:hover,
.custom-btn-delete:hover {
  background-color: #f5f7fa !important;
  border-color: #c6e2ff !important;
  color: #409eff !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.custom-btn-delete:hover {
  border-color: #fbc4c4 !important;
  color: #f56c6c !important;
}

/* 美化对话框 */
.el-dialog {
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.el-dialog__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  border-radius: 8px 8px 0 0;
  padding: 15px 20px;
}

.el-dialog__title {
  font-weight: 600;
  color: #303133;
}

.el-dialog__body {
  padding: 20px;
}

/* 美化表单元素 */
.el-form-item__label {
  color: #606266;
  font-weight: 500;
}

.el-input__inner,
.el-textarea__inner {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.el-input__inner:focus,
.el-textarea__inner:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 美化下拉框 */
.el-select .el-input__inner {
  border-radius: 4px;
}

/* 美化消息提示 */
.el-message {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 美化确认对话框 */
.el-message-box {
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.el-message-box__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  border-radius: 8px 8px 0 0;
  padding: 15px 20px;
}

.el-message-box__title {
  font-weight: 600;
  color: #303133;
}

.el-message-box__content {
  padding: 20px;
}
</style>
