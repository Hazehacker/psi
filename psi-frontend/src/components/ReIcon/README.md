# ReIcon 图标组件使用文档

## 1. 组件概述

ReIcon 是一个基于 Iconify 封装的增强型图标组件库，提供了在线和离线两种图标使用方式，同时支持 iconfont 图标。**封装的主要思路是提供灵活多样的图标使用方式，满足内网和外网环境下的不同需求**。

### 1.1 核心功能

- **离线图标**：支持预加载的图标，适用于内网环境
- **在线图标**：从 Iconify CDN 加载，可访问完整图标库
- **iconfont 支持**：兼容传统 iconfont 图标方案
- **Hook 函数**：提供 `useRenderIcon` Hook，用于动态渲染图标

### 1.2 可用组件和函数

| 名称                 | 类型 | 说明                         |
| :------------------- | :--- | :--------------------------- |
| `IconifyIconOffline` | 组件 | 离线图标组件（内网环境推荐） |
| `IconifyIconOnline`  | 组件 | 在线图标组件（外网环境推荐） |
| `FontIcon`           | 组件 | iconfont 图标组件            |
| `useRenderIcon`      | Hook | 动态渲染图标的 Hook 函数     |
| `iconType`           | 类型 | 图标属性类型定义             |

## 2. 快速开始

### 2.1 方式一：使用离线图标（字符串方式）

通过字符串名称引用预加载的图标，适用于内网环境。

```vue
<template>
  <div>
    <!-- 基础使用 -->
    <IconifyIconOffline icon="ep/home-filled" :width="32" :height="32" />

    <!-- 带颜色 -->
    <IconifyIconOffline icon="ep/edit" :width="32" :height="32" color="#409eff" />

    <!-- Element Plus 图标集 -->
    <IconifyIconOffline icon="ep/menu" :width="24" :height="24" />
    <IconifyIconOffline icon="ep/setting" :width="24" :height="24" />
    <IconifyIconOffline icon="ep/document" :width="24" :height="24" />

    <!-- Remix Icon 图标集 -->
    <IconifyIconOffline icon="ri/admin-fill" :width="32" :height="32" color="#67c23a" />
    <IconifyIconOffline icon="ri/user-fill" :width="32" :height="32" />
    <IconifyIconOffline icon="ri/lock-fill" :width="32" :height="32" color="#f56c6c" />
  </div>
</template>

<script setup lang="ts">
// 组件已全局注册，无需导入
</script>
```

**图标命名规则**：使用斜杠分隔，格式为 `{图标集}/{图标名}`，例如：

- `ep/home-filled`：Element Plus 图标集中的 home-filled 图标
- `ri/user-fill`：Remix Icon 图标集中的 user-fill 图标

**注意**：使用此方式前，需要在 `src/components/ReIcon/src/offlineIcon.ts` 中预加载对应图标。

### 2.2 方式二：使用离线图标（导入方式）

直接导入图标组件，类型安全，按需打包，推荐用于页面内常用图标。

```vue
<template>
  <div>
    <!-- 使用导入的图标组件 -->
    <IconifyIconOffline :icon="HomeIcon" :width="32" :height="32" />
    <IconifyIconOffline :icon="UserIcon" :width="32" :height="32" color="#e6a23c" />
    <IconifyIconOffline :icon="SettingIcon" :width="32" :height="32" />
    <IconifyIconOffline :icon="LockIcon" :width="32" :height="32" color="#f56c6c" />
  </div>
</template>

<script setup lang="ts">
// 导入图标组件
import HomeIcon from '~icons/ep/home-filled'
import UserIcon from '~icons/ri/user-fill'
import SettingIcon from '~icons/ep/setting'
import LockIcon from '~icons/ri/lock-fill'

// IconifyIconOffline 组件已全局注册，无需导入
</script>
```

**图标导入规则**：使用 `~icons/` 前缀，格式为 `~icons/{图标集}/{图标名}`

### 2.3 方式三：使用在线图标

从 Iconify CDN 加载图标，可访问完整图标库，需要网络连接。

```vue
<template>
  <div>
    <!-- Element Plus 图标 -->
    <IconifyIconOnline icon="ep:home" :width="32" :height="32" />
    <IconifyIconOnline icon="ep:setting" :width="32" :height="32" />

    <!-- Remix Icon 图标 -->
    <IconifyIconOnline icon="ri:user-fill" :width="32" :height="32" />

    <!-- Material Design Icons -->
    <IconifyIconOnline icon="mdi:github" :width="32" :height="32" />

    <!-- 品牌图标 -->
    <IconifyIconOnline icon="logos:vue" :width="32" :height="32" />
    <IconifyIconOnline icon="vscode-icons:file-type-vite" :width="32" :height="32" />
  </div>
</template>

<script setup lang="ts">
// IconifyIconOnline 组件已全局注册，无需导入
</script>
```

**图标命名规则**：使用冒号分隔，格式为 `{图标集}:{图标名}`

**图标搜索**：访问 [icon-sets.iconify.design](https://icon-sets.iconify.design/) 搜索需要的图标。

### 2.4 方式四：使用 useRenderIcon Hook

使用 Hook 函数动态渲染图标，适用于路由配置、菜单配置等场景。

```vue
<template>
  <div>
    <!-- 使用 Hook 渲染图标 -->
    <component :is="renderIconHome" />
    <component :is="renderIconUser" />
    <component :is="renderIconSetting" />
  </div>
</template>

<script setup lang="ts">
import { useRenderIcon } from '@/components/ReIcon/src/hooks'

// 方式 1：传入导入的图标组件
import HomeIcon from '~icons/ep/home-filled'
import UserIcon from '~icons/ri/user-fill'

const renderIconHome = useRenderIcon(HomeIcon, { width: '32px', height: '32px' })
const renderIconUser = useRenderIcon(UserIcon, { width: '40px', height: '40px', color: '#409eff' })

// 方式 2：传入字符串图标名
const renderIconSetting = useRenderIcon('ep/setting', {
  width: '32px',
  height: '32px',
  color: '#9c27b0'
})
</script>
```

**在路由配置中使用**：

```typescript
import { useRenderIcon } from '@/components/ReIcon/src/hooks'
import HomeIcon from '~icons/ep/home-filled'
import SettingIcon from '~icons/ep/setting'

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: {
      title: '首页',
      icon: useRenderIcon(HomeIcon)
    }
  },
  {
    path: '/setting',
    name: 'Setting',
    component: () => import('@/views/Setting.vue'),
    meta: {
      title: '设置',
      icon: useRenderIcon('ep/setting', { color: '#409eff' })
    }
  }
]
```

### 2.5 方式五：使用 iconfont 图标（可选）

支持传统 iconfont 图标方案，兼容旧项目。

```vue
<template>
  <div>
    <!-- font-class 模式（默认） -->
    <FontIcon icon="icon-home" />

    <!-- unicode 模式 -->
    <FontIcon icon="&#xe600;" uni />

    <!-- svg symbol 模式 -->
    <FontIcon icon="icon-user" svg />
  </div>
</template>

<script setup lang="ts">
// FontIcon 组件已全局注册，无需导入
</script>
```

## 3. 属性说明

### 3.1 IconifyIconOffline 组件属性

| 属性名   | 类型               | 默认值  | 说明                             |
| :------- | :----------------- | :------ | :------------------------------- |
| `icon`   | `string \| object` | -       | 图标名称或图标组件               |
| `width`  | `string \| number` | -       | 图标宽度                         |
| `height` | `string \| number` | -       | 图标高度                         |
| `color`  | `string`           | -       | 图标颜色                         |
| `rotate` | `number \| string` | -       | 旋转角度（0, 90, 180, 270）      |
| `flip`   | `string`           | -       | 翻转方向（horizontal, vertical） |
| `inline` | `boolean`          | `false` | 是否为内联图标                   |

### 3.2 IconifyIconOnline 组件属性

| 属性名   | 类型               | 默认值  | 说明                             |
| :------- | :----------------- | :------ | :------------------------------- |
| `icon`   | `string`           | -       | 图标名称（格式：图标集:图标名）  |
| `width`  | `string \| number` | -       | 图标宽度                         |
| `height` | `string \| number` | -       | 图标高度                         |
| `color`  | `string`           | -       | 图标颜色                         |
| `rotate` | `number \| string` | -       | 旋转角度（0, 90, 180, 270）      |
| `flip`   | `string`           | -       | 翻转方向（horizontal, vertical） |
| `inline` | `boolean`          | `false` | 是否为内联图标                   |

### 3.3 FontIcon 组件属性

| 属性名     | 类型      | 默认值  | 说明                                            |
| :--------- | :-------- | :------ | :---------------------------------------------- |
| `icon`     | `string`  | -       | 图标名称                                        |
| `iconType` | `string`  | -       | 图标类型（uni: unicode 模式，svg: symbol 模式） |
| `uni`      | `boolean` | `false` | 是否使用 unicode 模式                           |
| `svg`      | `boolean` | `false` | 是否使用 svg symbol 模式                        |

### 3.4 useRenderIcon Hook 参数

```typescript
useRenderIcon(icon: any, attrs?: iconType): Component
```

**参数说明**：

| 参数名  | 类型       | 必填 | 说明                                 |
| :------ | :--------- | :--- | :----------------------------------- |
| `icon`  | `any`      | 是   | 图标（组件、字符串或 iconfont 名称） |
| `attrs` | `iconType` | 否   | 图标属性（宽度、高度、颜色等）       |

**iconType 类型定义**：

```typescript
export interface iconType {
  inline?: boolean
  width?: string | number
  height?: string | number
  horizontalFlip?: boolean
  verticalFlip?: boolean
  flip?: string
  rotate?: number | string
  color?: string
  horizontalAlign?: boolean
  verticalAlign?: boolean
  align?: string
  onLoad?: Function
  includes?: Function
  fill?: string
  style?: object
}
```

## 4. 完整使用示例

### 4.1 综合示例

```vue
<template>
  <div class="icon-demo">
    <el-card class="section">
      <template #header>
        <h3>1. 离线图标 - 字符串方式</h3>
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
      </div>
    </el-card>

    <el-card class="section">
      <template #header>
        <h3>2. 离线图标 - 导入方式</h3>
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
      </div>
    </el-card>

    <el-card class="section">
      <template #header>
        <h3>3. 在线图标</h3>
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
      </div>
    </el-card>

    <el-card class="section">
      <template #header>
        <h3>4. useRenderIcon Hook</h3>
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

    <el-card class="section">
      <template #header>
        <h3>5. 图标尺寸测试</h3>
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
  </div>
</template>

<script setup lang="ts">
import { useRenderIcon } from '@/components/ReIcon/src/hooks'

// 导入图标组件（方式二）
import HomeIcon from '~icons/ep/home-filled'
import UserIcon from '~icons/ri/user-fill'
import SettingIcon from '~icons/ep/setting'

// 使用 useRenderIcon Hook
const renderIconHome = useRenderIcon(HomeIcon, { width: '32px', height: '32px' })
const renderIconUser = useRenderIcon(UserIcon, { width: '40px', height: '40px', color: '#409eff' })
const renderIconSetting = useRenderIcon('ep/setting', {
  width: '32px',
  height: '32px',
  color: '#9c27b0'
})
</script>

<style scoped>
.icon-demo {
  padding: 20px;
}

.section {
  margin-bottom: 20px;
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
  border: none;
  padding: 10px;
}
</style>
```

### 4.2 在组件中相对路径导入使用

```vue
<template>
  <div>
    <IconifyIconOffline :icon="HomeIcon" :width="24" :height="24" />
  </div>
</template>

<script setup lang="ts">
// 方式 1：从组件目录相对导入（如果在 src/components 目录下）
import { IconifyIconOffline, useRenderIcon } from '../ReIcon'
import type { iconType } from '../ReIcon'

// 方式 2：从项目根目录导入（如果在 src/views 目录下）
import { IconifyIconOffline, useRenderIcon } from '@/components/ReIcon'
import type { iconType } from '@/components/ReIcon'

// 方式 3：导入具体的 Hook 和类型（更细粒度）
import { useRenderIcon } from '@/components/ReIcon/src/hooks'
import type { iconType } from '@/components/ReIcon/src/types'

// 导入图标组件
import HomeIcon from '~icons/ep/home-filled'
</script>
```

### 4.3 在菜单配置中使用

```typescript
import { useRenderIcon } from '@/components/ReIcon/src/hooks'
import HomeIcon from '~icons/ep/home-filled'
import SettingIcon from '~icons/ep/setting'
import UserIcon from '~icons/ri/user-fill'

export const menuData = [
  {
    id: 1,
    text: '首页',
    icon: useRenderIcon(HomeIcon),
    href: '/home'
  },
  {
    id: 2,
    text: '用户管理',
    icon: useRenderIcon(UserIcon, { color: '#409eff' }),
    href: '/user'
  },
  {
    id: 3,
    text: '系统设置',
    icon: useRenderIcon('ep/setting'),
    href: '/setting',
    children: [
      {
        id: 31,
        text: '基础设置',
        icon: useRenderIcon('ep/setting'),
        href: '/setting/basic'
      }
    ]
  }
]
```

### 4.4 在表格操作列中使用

```vue
<template>
  <el-table :data="tableData">
    <el-table-column label="操作" width="150">
      <template #default="{ row }">
        <el-button :icon="EditIcon" @click="handleEdit(row)">编辑</el-button>
        <el-button :icon="DeleteIcon" type="danger" @click="handleDelete(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import EditIcon from '~icons/ep/edit'
import DeleteIcon from '~icons/ep/delete'

const tableData = ref([
  { id: 1, name: '张三' },
  { id: 2, name: '李四' }
])

const handleEdit = (row: any) => {
  console.log('编辑', row)
}

const handleDelete = (row: any) => {
  console.log('删除', row)
}
</script>
```

## 5. 高级功能

### 5.1 图标旋转和翻转

```vue
<template>
  <div>
    <!-- 旋转 90 度 -->
    <IconifyIconOffline icon="ep/arrow-right" :width="32" :height="32" :rotate="90" />

    <!-- 旋转 180 度 -->
    <IconifyIconOffline icon="ep/arrow-right" :width="32" :height="32" :rotate="180" />

    <!-- 水平翻转 -->
    <IconifyIconOffline icon="ep/arrow-right" :width="32" :height="32" flip="horizontal" />

    <!-- 垂直翻转 -->
    <IconifyIconOffline icon="ep/arrow-right" :width="32" :height="32" flip="vertical" />
  </div>
</template>
```

### 5.2 内联图标

```vue
<template>
  <div>
    <p>
      这是一段文字
      <IconifyIconOffline icon="ep/warning" :width="16" :height="16" inline color="#e6a23c" />
      这是行内图标示例
    </p>
  </div>
</template>
```

### 5.3 动态图标

```vue
<template>
  <div>
    <IconifyIconOffline :icon="currentIcon" :width="32" :height="32" :color="currentColor" />
    <el-button @click="changeIcon">切换图标</el-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const icons = ['ep/home-filled', 'ep/user', 'ep/setting']
const colors = ['#409eff', '#67c23a', '#e6a23c']
const currentIndex = ref(0)

const currentIcon = computed(() => icons[currentIndex.value])
const currentColor = computed(() => colors[currentIndex.value])

const changeIcon = () => {
  currentIndex.value = (currentIndex.value + 1) % icons.length
}
</script>
```

## 6. 图标预加载配置

如果需要在离线模式下使用新的图标（字符串方式），需要在 `src/components/ReIcon/src/offlineIcon.ts` 中预加载：

```typescript
import { getSvgInfo } from '@pureadmin/utils'
import { addIcon } from '@iconify/vue/dist/offline'

// 导入需要预加载的图标
import EpMenu from '~icons/ep/menu?raw'
import EpEdit from '~icons/ep/edit?raw'
import EpHomeFilled from '~icons/ep/home-filled?raw'
import RiAdminFill from '~icons/ri/admin-fill?raw'
// 根据项目需求添加更多图标

const icons = [
  ['ep/menu', EpMenu],
  ['ep/edit', EpEdit],
  ['ep/home-filled', EpHomeFilled],
  ['ri/admin-fill', RiAdminFill]
  // 添加更多图标映射
]

/** 本地菜单图标预加载 */
icons.forEach(([name, icon]) => {
  addIcon(name as string, getSvgInfo(icon as string))
})
```

## 7. 使用建议

### 7.1 图标使用场景建议

| 场景           | 推荐方式             | 理由               |
| :------------- | :------------------- | :----------------- |
| 菜单图标       | 离线模式 + 预加载    | 提升首屏加载速度   |
| 页面内常用图标 | 直接引入（方式二）   | 类型安全，按需打包 |
| 动态图标       | `useRenderIcon` Hook | 灵活性高           |
| 图标选择器     | 在线模式             | 可访问完整图标库   |
| 内网项目       | 离线模式             | 不依赖外网         |
| 路由/菜单配置  | `useRenderIcon` Hook | 适合配置文件使用   |

### 7.2 性能优化建议

1. **合理预加载**：仅预加载菜单等常用图标，不要过度预加载
2. **按需引入**：优先使用方式二（直接引入），避免打包未使用的图标
3. **避免重复**：同一个图标在项目中使用相同的引入方式
4. **懒加载**：对于不常用的大图标，使用在线模式或异步组件

### 7.3 命名规范

1. **在线图标**：使用冒号分隔（如 `ep:home`）
2. **离线图标**：使用斜杠分隔（如 `ep/menu`）
3. **Iconfont**：使用 `IF-` 前缀（如 `IF-icon-home`）

## 8. 注意事项

1. **组件全局注册**：`IconifyIconOffline`、`IconifyIconOnline`、`FontIcon` 组件已在 `main.ts` 中全局注册，无需在组件中导入
2. **图标预加载**：使用离线图标的字符串方式前，必须在 `offlineIcon.ts` 中预加载对应图标
3. **网络依赖**：在线图标需要网络连接，内网项目建议使用离线模式
4. **类型安全**：推荐使用导入方式（方式二），可以获得更好的类型提示
5. **尺寸单位**：图标尺寸支持数字（px）和字符串（'32px'、'2rem' 等）

## 9. 常见问题

### 9.1 图标不显示

**问题**：图标组件渲染为空

**排查步骤**：

1. 检查浏览器控制台是否有错误
2. 检查网络请求（针对在线图标）
3. 检查组件是否正确全局注册
4. 检查图标名称是否正确
5. 对于离线图标字符串方式，检查是否已预加载

### 9.2 离线图标（字符串）不显示

**问题**：使用 `<IconifyIconOffline icon="ep/menu" />` 不显示

**解决方案**：

1. 检查图标是否在 `offlineIcon.ts` 中预加载
2. 检查 `getSvgInfo` 函数是否正确导入
3. 确认 Vite 插件配置正确

### 9.3 在线图标不显示

**问题**：使用 `<IconifyIconOnline icon="ep:home" />` 不显示

**解决方案**：

1. 检查网络连接是否正常
2. 检查图标名称格式是否包含冒号（如 `ep:home`）
3. 检查浏览器控制台是否有 CORS 错误

### 9.4 TypeScript 类型错误

**问题**：导入图标组件时出现类型错误

**解决方案**：

1. 检查 `@iconify/vue` 是否正确安装
2. 检查 `unplugin-icons` 的类型声明是否生效
3. 在 `vite-env.d.ts` 中添加 `/// <reference types="unplugin-icons/types/vue" />`

## 10. 参考链接

- [Pure-Admin Icon 方案调研报告](https://01s-11comm-doc.ruan-cat.com/docs/reports/2025-11-14-pure-admin-icon-solution-research.md)
- [Pure-Admin 官方图标文档](https://github.com/pure-admin/pure-admin-doc/blob/master/docs/01.%E6%8C%87%E5%8D%97/02.%E8%BF%9B%E9%98%B6/01.%E5%9B%BE%E6%A0%87.md)
- [Iconify 官方文档](https://iconify.design/)
- [unplugin-icons GitHub](https://github.com/unplugin/unplugin-icons)
- [图标搜索工具](https://icon-sets.iconify.design/)
- [Vue Pure Admin GitHub](https://github.com/pure-admin/vue-pure-admin)
