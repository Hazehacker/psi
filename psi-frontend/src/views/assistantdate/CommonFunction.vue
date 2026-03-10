<template>
  <section class="root-container">
    <div class="header">
      <div class="title">
        <i class="el-icon-setting title-icon"></i>
        常用功能配置
        <span class="selected-count">已选择 {{ selectedFunctions.length }} 个功能</span>
      </div>
      <div class="button-group">
        <el-button
          type="primary"
          icon="el-icon-check"
          @click="saveConfig"
          :loading="saving"
          :disabled="selectedFunctions.length === 0"
        >
          保存配置
        </el-button>
        <el-button icon="el-icon-refresh" @click="refreshPage" :loading="loading"> 刷新 </el-button>
        <el-button @click="selectAll" :disabled="availableMenus.length === 0">全选</el-button>
        <el-button @click="clearAll">清空</el-button>
      </div>
    </div>

    <div class="content-wrapper">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-content">
          <el-skeleton :rows="8" animated />
        </div>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="loadError" class="error-container">
        <el-empty description="加载失败，请刷新重试">
          <el-button type="primary" @click="refreshPage">重新加载</el-button>
        </el-empty>
      </div>

      <!-- 正常内容 -->
      <div v-else class="main-content">
        <!-- 分组内容 -->
        <div class="groups-container">
          <div
            v-for="group in groupedMenus"
            :key="group.root"
            class="group-card"
            :class="{ 'group-expanded': expandedGroups[group.root] }"
          >
            <div class="group-header" @click="toggleGroup(group.root)">
              <div class="group-title">
                <div class="group-icon">
                  <i :class="getGroupIcon(group.root)"></i>
                </div>
                <div class="group-info">
                  <span class="group-name">{{ getGroupName(group.root) }}</span>
                  <span class="group-stats">
                    {{ group.selectedCount }}/{{ group.menus.length }} 已选择
                  </span>
                </div>
              </div>
              <div class="group-actions">
                <i
                  :class="expandedGroups[group.root] ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
                ></i>
              </div>
            </div>

            <el-collapse-transition>
              <div v-show="expandedGroups[group.root]" class="group-content">
                <div class="function-grid">
                  <div
                    v-for="menu in group.menus"
                    :key="menu.key"
                    class="function-card"
                    :class="{ 'function-selected': selectedFunctions.includes(menu.key) }"
                    @click="toggleFunction(menu.key)"
                  >
                    <div class="function-checkbox">
                      <el-checkbox
                        :model-value="selectedFunctions.includes(menu.key)"
                        @change="(val) => toggleFunction(menu.key)"
                      />
                    </div>
                    <div class="function-icon">
                      <i :class="menu.ico || 'el-icon-document'"></i>
                    </div>
                    <div class="function-info">
                      <div class="function-name">{{ menu.name }}</div>
                      <div class="function-desc" v-if="menu.data">{{ menu.data }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-collapse-transition>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="availableMenus.length === 0" class="empty-container">
          <el-empty description="暂无可用功能" />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue'
import { ElMessageBox, ElMessage, ElCollapseTransition } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getAvailableOftenMenus, getConfiguredOftenMenus, saveOftenMenus } from '@/apis/often'
import type { MenuItem, OftenMenuQuery } from '@/apis/often/type'

// 用户store
const userStore = useUserStore()

// 响应式数据
const availableMenus = ref<MenuItem[]>([])
const configuredMenus = ref<string[]>([])
const loading = ref(false)
const saving = ref(false)
const loadError = ref(false)

// 选中的功能列表
const selectedFunctions = ref<string[]>([])

// 展开的分组状态
const expandedGroups = reactive<Record<string, boolean>>({})

// 计算属性 - 分组后的菜单
const groupedMenus = computed(() => {
  const groups: {
    [key: string]: {
      root: string
      menus: MenuItem[]
      selectedCount: number
      index: number
    }
  } = {}

  const groupOrder = [
    'home',
    'purchase',
    'sales',
    'warehouse',
    'finance',
    'report',
    'sysparams',
    'sysconfig',
    'admin',
    'other'
  ]

  availableMenus.value.forEach((menu) => {
    const root = menu.root || 'other'

    if (!groups[root]) {
      const index = groupOrder.indexOf(root)
      groups[root] = {
        root: root,
        menus: [],
        selectedCount: 0,
        index: index === -1 ? 999 : index
      }
    }
    groups[root].menus.push(menu)

    // 统计已选择数量
    if (selectedFunctions.value.includes(menu.key)) {
      groups[root].selectedCount++
    }
  })

  // 按照预定义顺序排序
  return Object.values(groups)
    .filter((group) => group.menus.length > 0)
    .sort((a, b) => a.index - b.index)
})

// 分组图标映射
const groupIcons: { [key: string]: string } = {
  home: 'el-icon-house',
  purchase: 'el-icon-shopping-cart-2',
  sales: 'el-icon-sell',
  warehouse: 'el-icon-office-building',
  finance: 'el-icon-money',
  report: 'el-icon-data-analysis',
  sysparams: 'el-icon-setting',
  sysconfig: 'el-icon-cpu',
  admin: 'el-icon-user',
  other: 'el-icon-more'
}

// 分组名称映射
const groupNames: { [key: string]: string } = {
  home: '首页',
  purchase: '采购管理',
  sales: '销售管理',
  warehouse: '仓库管理',
  finance: '资金管理',
  report: '报表管理',
  sysparams: '系统参数',
  sysconfig: '系统配置',
  admin: '系统管理',
  other: '其他功能'
}

// 获取分组图标
const getGroupIcon = (root: string) => {
  return groupIcons[root] || 'el-icon-folder'
}

// 获取分组名称
const getGroupName = (root: string) => {
  return groupNames[root] || root
}

// 获取当前用户ID
const getCurrentUserId = () => {
  return userStore.getUser?.id || localStorage.getItem('userId') || '1001'
}

// 切换分组展开状态
const toggleGroup = (groupRoot: string) => {
  expandedGroups[groupRoot] = !expandedGroups[groupRoot]
}

// 切换功能选择状态
const toggleFunction = (menuKey: string) => {
  const index = selectedFunctions.value.indexOf(menuKey)
  if (index > -1) {
    selectedFunctions.value.splice(index, 1)
  } else {
    selectedFunctions.value.push(menuKey)
  }
}

// 全选
const selectAll = () => {
  selectedFunctions.value = availableMenus.value.map((menu) => menu.key)
}

// 清空
const clearAll = () => {
  selectedFunctions.value = []
}

// 加载数据
const loadData = async () => {
  loading.value = true
  loadError.value = false

  try {
    const userId = getCurrentUserId()

    // 并行获取可用功能和已设置功能
    const [availableData, configuredData] = await Promise.all([
      getAvailableOftenMenus(userId),
      getConfiguredOftenMenus({
        userId,
        pageIndex: 1,
        pageSize: 1000
      } as OftenMenuQuery)
    ])

    availableMenus.value = availableData

    // 处理已设置的功能
    if (configuredData.rows && configuredData.rows.length > 0) {
      const userConfig = configuredData.rows[0]
      configuredMenus.value = userConfig.menus.map((menu) => menu.key)
      selectedFunctions.value = [...configuredMenus.value]
    } else {
      configuredMenus.value = []
      selectedFunctions.value = []
    }

    // 初始化分组展开状态
    groupedMenus.value.forEach((group) => {
      if (!(group.root in expandedGroups)) {
        expandedGroups[group.root] = true
      }
    })
  } catch (error) {
    console.error('加载常用功能数据失败:', error)
    loadError.value = true
    ElMessage.error('加载数据失败，请重试')
  } finally {
    loading.value = false
  }
}

// 保存配置
const saveConfig = async () => {
  if (selectedFunctions.value.length === 0) {
    ElMessage.warning('请至少选择一个功能')
    return
  }

  saving.value = true

  try {
    const userId = getCurrentUserId()
    const saveRequest = {
      userId,
      menuKeys: selectedFunctions.value
    }

    const result = await saveOftenMenus(saveRequest)

    if (result.success) {
      ElMessage({
        message: '配置保存成功',
        type: 'success'
      })
      // 更新已设置的菜单
      configuredMenus.value = [...selectedFunctions.value]
    } else {
      ElMessage.error(result.message || '保存失败')
    }
  } catch (error) {
    console.error('保存配置失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

// 刷新页面
const refreshPage = async () => {
  await ElMessageBox.confirm('确定要刷新页面吗？未保存的更改将丢失。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      await loadData()
      ElMessage({
        type: 'success',
        message: '页面已刷新'
      })
    })
    .catch(() => {
      // 用户取消操作
    })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.root-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4efe9 100%);
  box-sizing: border-box;
  position: relative;
  overflow-x: hidden;
}

.root-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 300px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.05) 0%, rgba(103, 194, 58, 0.05) 100%);
  z-index: 0;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 20px 25px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 1;
}

.title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 15px;
}

.title-icon {
  color: #409eff;
  font-size: 28px;
}

.selected-count {
  font-size: 14px;
  color: #909399;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 12px;
}

.button-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.content-wrapper {
  position: relative;
  z-index: 1;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.groups-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.group-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.group-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.group-expanded {
  border-color: #409eff;
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.15);
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  cursor: pointer;
  transition: all 0.3s ease;
}

.group-header:hover {
  background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 100%);
}

.group-title {
  display: flex;
  align-items: center;
  gap: 16px;
}

.group-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.group-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.group-name {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.group-stats {
  font-size: 14px;
  color: #909399;
}

.group-actions {
  color: #909399;
  font-size: 16px;
  transition: transform 0.3s ease;
}

.group-expanded .group-actions {
  transform: rotate(180deg);
}

.group-content {
  padding: 24px;
  background: white;
}

.function-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.function-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  gap: 12px;
}

.function-card:hover {
  background: #e3f2fd;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.function-selected {
  border-color: #409eff;
  background: rgba(64, 158, 255, 0.05);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.function-checkbox {
  flex-shrink: 0;
}

.function-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  flex-shrink: 0;
}

.function-info {
  flex: 1;
  min-width: 0;
}

.function-name {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.function-desc {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.loading-container {
  background: white;
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 18px;
}

.loading-content {
  max-width: 800px;
  margin: 0 auto;
}

.error-container,
.empty-container {
  background: white;
  border-radius: 16px;
  padding: 60px 40px;
  text-align: center;
  margin-bottom: 18px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .function-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 768px) {
  .root-container {
    padding: 15px;
  }

  .header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
    padding: 15px 20px;
  }

  .title {
    font-size: 20px;
  }

  .button-group {
    width: 100%;
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .function-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .group-header {
    padding: 16px 20px;
  }

  .group-content {
    padding: 20px;
  }

  .group-icon {
    width: 40px;
    height: 40px;
    font-size: 18px;
  }

  .group-name {
    font-size: 16px;
  }
}

@media (max-width: 480px) {
  .function-card {
    padding: 12px;
  }

  .function-icon {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }

  .function-name {
    font-size: 13px;
  }
}

/* 自定义滚动条 */
.group-content::-webkit-scrollbar {
  width: 6px;
}

.group-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.group-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.group-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
