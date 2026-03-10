# Common 组件库使用说明

Common 组件库包含了一些通用的业务组件，这些组件主要用于处理常见的业务逻辑。

## 组件列表

### 1. MenuList.vue - 菜单列表组件
**功能**: 递归渲染菜单列表，支持多种菜单类型
**特点**:
- 支持独立菜单、附属菜单、父子菜单三种类型
- 支持递归渲染子菜单
- 支持图标显示
- 支持菜单层级控制

**使用示例**:
```vue
<template>
  <el-menu>
    <MenuList :menuList="menuData" :level="0" />
  </el-menu>
</template>

<script setup>
import MenuList from '@/components/common/MenuList.vue'

const menuData = ref([
  {
    id: 1,
    key: 'dashboard',
    name: '仪表盘',
    ico: 'el-icon-dashboard',
    type: 'routine'
  },
  {
    id: 2,
    key: 'user-management',
    name: '用户管理',
    ico: 'el-icon-user',
    type: 'parent',
    sub: [
      {
        id: 21,
        key: 'user-list',
        name: '用户列表',
        ico: 'el-icon-list'
      },
      {
        id: 22,
        key: 'user-add',
        name: '添加用户',
        ico: 'el-icon-plus'
      }
    ]
  },
  {
    id: 3,
    key: 'settings',
    name: '系统设置',
    ico: 'el-icon-setting',
    type: 'subsidiary',
    sub: [
      {
        id: 31,
        key: 'system-config',
        name: '系统配置|配置管理'
      }
    ]
  }
])
</script>
```

**Props**:
- `menuList` (Array, required): 菜单数据数组
- `level` (Number, default: 0): 菜单层级，用于控制缩进

**菜单数据结构**:
```typescript
interface MenuItem {
  id: number | string
  key: string
  name: string
  ico?: string
  type?: 'routine' | 'parent' | 'subsidiary'
  sub?: MenuItem[]
}
```

**菜单类型说明**:

1. **独立菜单 (routine)**:
   - 普通的菜单项，没有子菜单
   - 直接渲染为 `el-menu-item`

2. **父子菜单 (parent)**:
   - 有子菜单的父级菜单
   - 渲染为 `el-submenu`，包含子菜单项

3. **附属菜单 (subsidiary)**:
   - 特殊的菜单类型，包含一个主菜单和一个附属菜单
   - 渲染为 `el-menu-item-group`
   - 附属菜单的名称格式为 "主名称|附属名称"

**样式类名**:
- `.menuList`: 根容器
- `.subsidiaryMenu`: 附属菜单组
- `[level]`: 根据层级添加的类名，用于控制缩进

**使用场景**:
- 侧边栏导航菜单
- 顶部导航菜单
- 面包屑导航
- 任何需要递归渲染的树形菜单结构

**注意事项**:
1. 组件使用 Vue 3 的 `defineComponent` 定义
2. 支持无限层级的菜单嵌套
3. 图标使用 Element Plus 的图标类名
4. 菜单的 `key` 属性应该唯一，用于路由跳转
5. 组件会自动处理菜单的展开/收起状态

**自定义样式**:
```css
/* 根据层级控制缩进 */
.menuList[level="1"] {
  padding-left: 20px;
}

.menuList[level="2"] {
  padding-left: 40px;
}

/* 附属菜单样式 */
.subsidiaryMenu {
  background-color: #f5f5f5;
}
```
