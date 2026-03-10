<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRenderIcon } from '@/components/ReIcon/src/hooks'
import { IconifyIconOffline, IconifyIconOnline, FontIcon } from '@/components/ReIcon'

// 导入图标组件（方式二）
import HomeIcon from '~icons/ep/home-filled'
import UserIcon from '~icons/ri/user-fill'
import SettingIcon from '~icons/ep/setting'
import LockIcon from '~icons/ri/lock-fill'

// 使用 useRenderIcon Hook
const renderIconHome = useRenderIcon(HomeIcon, { width: '32px', height: '32px' })
const renderIconUser = useRenderIcon(UserIcon, { width: '40px', height: '40px', color: '#409eff' })
const renderIconSetting = useRenderIcon('ep/setting', {
  width: '32px',
  height: '32px',
  color: '#9c27b0'
})

// 验证清单
const checks = ref({
  offlineString: false,
  offlineImport: false,
  online: false,
  hook: false,
  size: false,
  noError: false
})

const allChecked = computed(() => {
  return Object.values(checks.value).every((check) => check === true)
})
</script>

<template>
  <div class="iconify-test-page">
    <el-card class="header-card">
      <h1>Iconify 图标测试页面</h1>
      <p>测试 pure-admin 的 iconify 图标方案在本项目中的集成效果</p>
    </el-card>

    <!-- 测试离线图标（字符串方式） -->
    <el-card class="test-section">
      <template #header>
        <h2>1. 离线图标 - 字符串方式</h2>
        <p>使用预加载的图标，通过字符串名称引用</p>
      </template>
      <div class="icon-grid">
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="32" :height="32" />
          <span>ep/home-filled</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/menu" :width="32" :height="32" />
          <span>ep/menu</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/edit" :width="32" :height="32" color="#409eff" />
          <span>ep/edit (蓝色)</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/setting" :width="32" :height="32" />
          <span>ep/setting</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/document" :width="32" :height="32" />
          <span>ep/document</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ri/admin-fill" :width="32" :height="32" color="#67c23a" />
          <span>ri/admin-fill (绿色)</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ri/user-fill" :width="32" :height="32" />
          <span>ri/user-fill</span>
        </div>
      </div>
    </el-card>

    <!-- 测试离线图标（导入方式） -->
    <el-card class="test-section">
      <template #header>
        <h2>2. 离线图标 - 导入方式</h2>
        <p>直接导入图标组件，类型安全，按需打包</p>
      </template>
      <div class="icon-grid">
        <div class="icon-item">
          <IconifyIconOffline :icon="HomeIcon" :width="32" :height="32" />
          <span>HomeIcon</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline :icon="UserIcon" :width="32" :height="32" color="#e6a23c" />
          <span>UserIcon (橙色)</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline :icon="SettingIcon" :width="32" :height="32" />
          <span>SettingIcon</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline :icon="LockIcon" :width="32" :height="32" color="#f56c6c" />
          <span>LockIcon (红色)</span>
        </div>
      </div>
    </el-card>

    <!-- 测试在线图标 -->
    <el-card class="test-section">
      <template #header>
        <h2>3. 在线图标</h2>
        <p>从 Iconify CDN 加载图标，需要网络连接</p>
      </template>
      <div class="icon-grid">
        <div class="icon-item">
          <IconifyIconOnline icon="ep:home" :width="32" :height="32" />
          <span>ep:home</span>
        </div>
        <div class="icon-item">
          <IconifyIconOnline icon="ri:user-fill" :width="32" :height="32" />
          <span>ri:user-fill</span>
        </div>
        <div class="icon-item">
          <IconifyIconOnline icon="mdi:github" :width="32" :height="32" />
          <span>mdi:github</span>
        </div>
        <div class="icon-item">
          <IconifyIconOnline icon="logos:vue" :width="32" :height="32" />
          <span>logos:vue</span>
        </div>
        <div class="icon-item">
          <IconifyIconOnline icon="vscode-icons:file-type-vite" :width="32" :height="32" />
          <span>vite</span>
        </div>
      </div>
    </el-card>

    <!-- 测试 useRenderIcon Hook -->
    <el-card class="test-section">
      <template #header>
        <h2>4. useRenderIcon Hook</h2>
        <p>使用 Hook 函数动态渲染图标，适用于路由配置等场景</p>
      </template>
      <div class="icon-grid">
        <div class="icon-item">
          <component :is="renderIconHome" />
          <span>renderIconHome</span>
        </div>
        <div class="icon-item">
          <component :is="renderIconUser" />
          <span>renderIconUser (40px)</span>
        </div>
        <div class="icon-item">
          <component :is="renderIconSetting" />
          <span>renderIconSetting (紫色)</span>
        </div>
      </div>
    </el-card>

    <!-- 图标尺寸测试 -->
    <el-card class="test-section">
      <template #header>
        <h2>5. 图标尺寸测试</h2>
        <p>测试不同尺寸的图标渲染</p>
      </template>
      <div class="size-test">
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="16" :height="16" />
          <span>16px</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="24" :height="24" />
          <span>24px</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="32" :height="32" />
          <span>32px</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="48" :height="48" />
          <span>48px</span>
        </div>
        <div class="icon-item">
          <IconifyIconOffline icon="ep/home-filled" :width="64" :height="64" />
          <span>64px</span>
        </div>
      </div>
    </el-card>

    <!-- 验证清单 -->
    <el-card class="test-section checklist-card">
      <template #header>
        <h2>验证清单</h2>
      </template>
      <ul class="checklist">
        <li>
          <el-checkbox v-model="checks.offlineString">
            离线图标（字符串方式）能够正常显示
          </el-checkbox>
        </li>
        <li>
          <el-checkbox v-model="checks.offlineImport">
            离线图标（导入方式）能够正常显示
          </el-checkbox>
        </li>
        <li>
          <el-checkbox v-model="checks.online"> 在线图标能够正常显示（需要网络连接） </el-checkbox>
        </li>
        <li>
          <el-checkbox v-model="checks.hook"> useRenderIcon Hook 能够正常工作 </el-checkbox>
        </li>
        <li>
          <el-checkbox v-model="checks.size"> 图标尺寸和颜色属性生效 </el-checkbox>
        </li>
        <li>
          <el-checkbox v-model="checks.noError"> 控制台没有报错 </el-checkbox>
        </li>
      </ul>
      <div class="check-result" v-if="allChecked">
        <el-alert type="success" :closable="false" show-icon>
          <template #title>
            <strong>恭喜！所有功能验证通过！</strong>
          </template>
          Iconify 图标方案已成功集成到项目中
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.iconify-test-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.header-card h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
}

.header-card p {
  margin: 0;
  opacity: 0.9;
}

.test-section {
  margin-bottom: 20px;
}

.test-section h2 {
  margin: 0 0 5px 0;
  font-size: 20px;
  color: #303133;
}

.test-section p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  transition: all 0.3s;
}

.icon-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.3);
  transform: translateY(-2px);
}

.icon-item span {
  font-size: 12px;
  color: #606266;
  text-align: center;
  word-break: break-all;
}

.size-test {
  display: flex;
  align-items: flex-end;
  gap: 30px;
  padding: 20px 0;
}

.size-test .icon-item {
  flex-direction: column;
  border: none;
  padding: 10px;
}

.checklist {
  list-style: none;
  padding: 0;
  margin: 0;
}

.checklist li {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.checklist li:last-child {
  border-bottom: none;
}

.check-result {
  margin-top: 20px;
}

.checklist-card {
  border: 2px solid #67c23a;
}
</style>
