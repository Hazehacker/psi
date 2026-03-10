import { addIcon } from '@iconify/vue/dist/offline'

/**
 * 这里存放本地图标，用于离线使用
 * 本地菜单图标，后端在路由的icon中返回对应的图标字符串并且前端在此处使用addIcon添加即可渲染
 *
 * 使用方式:
 * 1. 在 https://icon-sets.iconify.design/ 搜索需要的图标
 * 2. 在下方导入图标: import EpMenu from '~icons/ep/menu?raw'
 * 3. 在 icons 数组中添加映射: ['ep/menu', EpMenu]
 */

// 导入 Element Plus 图标
import EpHomeFilled from '~icons/ep/home-filled?raw'
import EpMenu from '~icons/ep/menu?raw'
import EpEdit from '~icons/ep/edit?raw'
import EpSetting from '~icons/ep/setting?raw'
import EpDocument from '~icons/ep/document?raw'

// 导入 Remix Icon 图标
import RiAdminFill from '~icons/ri/admin-fill?raw'
import RiUserFill from '~icons/ri/user-fill?raw'

/**
 * 将 SVG 字符串转换为 iconify 格式
 */
function getSvgInfo(svg: string) {
  const match = svg.match(/<svg[^>]*viewBox="([^"]*)"[^>]*>([\s\S]*?)<\/svg>/)
  if (!match) return null

  const viewBox = match[1]
  const body = match[2]
    .replace(/<title>.*?<\/title>/g, '')
    .replace(/fill="currentColor"/g, '')
    .trim()

  const [left, top, width, height] = viewBox.split(' ').map(Number)

  return {
    left,
    top,
    width,
    height,
    body
  }
}

// 图标映射数组: [图标名称, SVG内容]
const icons = [
  ['ep/home-filled', EpHomeFilled],
  ['ep/menu', EpMenu],
  ['ep/edit', EpEdit],
  ['ep/setting', EpSetting],
  ['ep/document', EpDocument],
  ['ri/admin-fill', RiAdminFill],
  ['ri/user-fill', RiUserFill]
]

// 本地菜单图标预加载
icons.forEach(([name, icon]) => {
  const iconInfo = getSvgInfo(icon as string)
  if (iconInfo) {
    addIcon(name as string, iconInfo)
  }
})
