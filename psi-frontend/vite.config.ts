import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import Icons from 'unplugin-icons/vite'
import svgLoader from 'vite-svg-loader'

// 本地https支持插件，插件启动的时候需要从Github下载文件，所以没有梯子可能无法正常工作
// import mkcert from 'vite-plugin-mkcert'

// https://vite.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 3000,
    open: true,
    // 配置 HMR WebSocket，使用与主服务器相同的端口
    hmr: {
      protocol: 'ws',
      host: 'localhost',
      port: 3000
    },
    proxy: {
      '/api': {
        changeOrigin: true,
        //FIXME[TEST_CODE]:使用Apifox云MOCK,联调阶段需要修改成网关地址
        target: 'http://39.103.62.65:10001',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/captcha': {
        changeOrigin: true,
        //FIXME[TEST_CODE]:联调阶段需要修改成网关地址
        target: 'http://39.103.62.65:10001',
        rewrite: (path) => path.replace(/^\/captcha/, '')
      }
    }
  },
  build: {
    assetsDir: 'static',
    chunkSizeWarningLimit: 1000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id.toString().split('node_modules/')[1].split('/')[0].toString()
          }
        }
      }
    }
  },
  plugins: [
    //mkcert(),
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    }),
    // svg 组件化支持
    svgLoader(),
    // 自动按需加载图标
    Icons({
      compiler: 'vue3',
      scale: 1
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
