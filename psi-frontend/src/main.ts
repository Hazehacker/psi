/*
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-11-04 15:24:52
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 10:23:45
 * @FilePath: \psi-frontend\src\main.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'element-plus/es/components/message/style/css'
import 'element-plus/es/components/message-box/style/css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import './assets/main.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/assets/lib.css' // 引入公用库CSS

// 使用ElementPlus和FcDesigner
import FcDesigner from '@form-create/designer'
import ElementPlus from 'element-plus'

import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 使用ElementPlus和FcDesigner
app.use(ElementPlus, { locale: zhCn })
app.use(FcDesigner)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 安装http插件
import http from './plugins/http'
app.use(http, { router })

// 安装ElIcon
import icon from './plugins/icon'
app.use(icon)

// 全局注册 iconify 图标组件
import { IconifyIconOffline, IconifyIconOnline, FontIcon } from '@/components/ReIcon'
app.component('IconifyIconOffline', IconifyIconOffline)
app.component('IconifyIconOnline', IconifyIconOnline)
app.component('FontIcon', FontIcon)

// 所有插件安装和全局组件注册完成后再挂载
app.mount('#app')
