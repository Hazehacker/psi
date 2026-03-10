<template>
  <div class="header-row">
    <!-- 应用信息栏 -->
    <div class="header-title">
      <img v-show="!isCollapse" class="app-icon" src="/logo.jpg" :title="appName" />
      <el-text v-show="!isCollapse" class="app-name">{{ appName }}</el-text>
      <el-button
        v-show="isCollapse"
        link
        class="collapse-btn"
        icon="IconExpand"
        @click="isCollapse = !isCollapse"
      ></el-button>
      <el-button
        v-show="!isCollapse"
        link
        class="collapse-btn"
        icon="IconFold"
        @click="isCollapse = !isCollapse"
      ></el-button>
    </div>
    <!-- 导航栏 -->
    <div class="header-nav">示例效果演示页</div>
  </div>
  <div class="content-row">
    <!-- 侧边菜单栏 -->
    <el-menu
      :collapse="isCollapse"
      :default-active="activeIndex"
      active-text-color="#409EFF"
      text-color="#fff"
      background-color="#545c64"
      unique-opened
      :collapse-transition="false"
      router
    >
      <el-menu-item
        v-for="(menu, index) in menus"
        :key="index"
        :index="menu.path"
        :title="isCollapse ? menu.title : ''"
      >
        <el-icon>
          <icon-menu />
        </el-icon>
        <span>{{ menu.title }}</span>
      </el-menu-item>
    </el-menu>
    <!-- 主内容区 -->
    <div class="main">
      <!-- 二级路由 -->
      <router-view />
    </div>
  </div>
</template>

<style>
:root {
  --sample-header-height: 60px;
  --sample-menu-width: 220px;
}
</style>
<style scoped>
.header-row {
  height: var(--sample-header-height);
  background-color: #6c777f;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .header-title {
    width: var(--sample-menu-width);
    padding-left: 15px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .app-icon {
      width: 30px;
      border-radius: 5px;
    }

    .app-name {
      color: white;
      font-size: 16px;
    }

    .collapse-btn {
      color: white;
      font-size: 24px;
    }
  }

  .header-nav {
    font-size: 18px;
    color: #f8f8f8;
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
  }
}

.content-row {
  height: calc(100vh - var(--sample-header-height));
  display: flex;
  flex-direction: row;

  .el-menu {
    border: 0;
    width: var(--sample-menu-width);
    height: 100%;
    overflow: auto;
  }

  .el-menu--collapse {
    width: calc(var(--el-menu-icon-width) + var(--el-menu-base-level-padding) * 2);
  }

  .main {
    flex: 1;
    padding: 10px;
    background-color: #edecec;
    width: calc(100vw - var(--sample-menu-width));
    height: 100%;
    overflow: auto;
  }
}
</style>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import sampleRoutes from '@/router/sample/index'

// 默认激活菜单
const activeIndex = ref('/sample/file')
// 监听路由地址改变
const route = useRoute()
watch(
  () => route.path,
  (newpath) => {
    activeIndex.value = newpath
  }
)
// 应用名称
const appName = import.meta.env.VITE_APP_NAME || '点可云进销存软件'
// 菜单是是否折叠
const isCollapse = ref(false)

/**
 * 演示菜单数据类型
 * @description
 * - path 代表菜单路径
 * - title 代表菜单标题
 */
interface SampleRouteItem {
  /** 菜单路径 */
  path: string
  /** 菜单标题 */
  title: string
}

/**
 * 演示菜单数据
 * @description
 * 添加新的演示数据，不允许再这里补充了。请在 `src\router\sample\index.ts` 按照路由规范补充
 */
const menus = reactive<SampleRouteItem[]>(
  (sampleRoutes[0]?.children || []).map((item) => ({
    path: item.path,
    title: item.meta?.title ?? '警告: 菜单标题未设置，请在路由配置中设置'
  }))
)
</script>
