<!-- 登录页面 -->
<template>
  <div class="login-container">
    <!-- 左侧品牌展示区域 -->
    <div class="login-banner">
      <div class="banner-content">
        <div class="logo-wrapper">
          <div class="logo-circle">
            <el-icon :size="60" color="#fff">
              <Shop />
            </el-icon>
          </div>
        </div>
        <h1 class="system-title">零壹进销存</h1>
        <p class="system-subtitle">
          <span class="planet-badge">01星球</span>
          智能化进销存管理平台
        </p>
        <div class="digital-code">
          <span class="code-text">{{ binaryCode }}</span>
        </div>
        <div class="banner-features">
          <div class="feature-item" v-for="(item, index) in features" :key="index">
            <el-icon :size="20">
              <component :is="item.icon" />
            </el-icon>
            <span>{{ item.text }}</span>
          </div>
        </div>
      </div>
      <!-- 装饰性星球和几何图形 -->
      <div class="geometric-shapes">
        <!-- 星球元素 -->
        <div class="planet planet-1">
          <div class="planet-ring"></div>
          <div class="planet-surface">
            <span class="binary-text">01</span>
          </div>
        </div>
        <div class="planet planet-2"></div>
        <div class="star star-1"></div>
        <div class="star star-2"></div>
        <div class="star star-3"></div>
        <div class="star star-4"></div>
        <div class="star star-5"></div>
        <!-- 原有几何图形 -->
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <!-- 右侧登录表单区域 -->
    <div class="login-form-wrapper">
      <div class="login-form-container">
        <div class="form-header">
          <h2 class="form-title">欢迎登录</h2>
          <p class="form-subtitle">请输入您的账号和密码</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="formData"
          :rules="rules"
          class="login-form"
          size="large"
        >
          <el-form-item prop="username">
            <el-input
              v-model="formData.username"
              placeholder="请输入账号"
              clearable
              @keyup.enter="submitForm"
            >
              <template #prefix>
                <el-icon>
                  <User />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="formData.password"
              type="password"
              placeholder="请输入密码"
              show-password
              clearable
              @keyup.enter="submitForm"
            >
              <template #prefix>
                <el-icon>
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <div class="form-options">
              <el-checkbox v-model="rememberMe" label="记住密码" />
              <el-link type="primary" :underline="false" v-if="showTestLink">
                <router-link to="/sample" class="test-link">示例演示</router-link>
              </el-link>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="login-button"
              :loading="loading"
              @click="submitForm"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 底部信息 -->
        <div class="form-footer">
          <p class="copyright">© 2025 零壹进销存 版权所有</p>
        </div>
      </div>
    </div>

    <!-- 验证码组件 -->
    <Verify
      mode="pop"
      :captchaType="captchaType"
      :imgSize="{ width: '400px', height: '200px' }"
      ref="verify"
      @success="handleSuccess"
    ></Verify>
  </div>
</template>

<script setup lang="ts">
import Verify from '@/components/verifition/Verify.vue'
import { ref, reactive, onMounted } from 'vue'
import { login } from '@/apis/login/index'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { useTabStore } from '@/stores/tab'
import {
  User,
  Lock,
  Shop,
  Checked,
  Timer,
  DataAnalysis
} from '@element-plus/icons-vue'
import { onBeforeUnmount } from 'vue'

// 是否显示示例演示界面连接
const showTestLink = ref(import.meta.env.DEV)

// 是否启用验证码
const enableVerify = ref(!import.meta.env.DEV)

// 加载状态
const loading = ref(false)

// 记住密码
const rememberMe = ref(false)

// 获取router对象
const $router = useRouter()

// 表单引用
const loginFormRef = ref<FormInstance>()

// 定义登录数据对象
const formData = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度不能少于3位', trigger: 'blur' }
  ]
})

// 功能特性列表
const features = [
  { icon: Checked, text: '安全可靠' },
  { icon: Timer, text: '高效便捷' },
  { icon: DataAnalysis, text: '数据分析' }
]

// 动态二进制代码
const binaryCode = ref('01010101 01010101 01010101')
let binaryTimer: number | null = null

// 生成随机二进制字符串
function generateBinaryCode() {
  const segments = 3
  const binarySegments = []
  for (let i = 0; i < segments; i++) {
    let segment = ''
    for (let j = 0; j < 8; j++) {
      segment += Math.random() > 0.5 ? '1' : '0'
    }
    binarySegments.push(segment)
  }
  binaryCode.value = binarySegments.join(' ')
}

/**
 * 执行登录
 * @param code 验证码字符串
 */
function doLogin(code: string) {
  loading.value = true
  // 发送登录请求
  login(
    {
      ...formData,
      code: code
    },
    () => {
      // 记住密码处理
      if (rememberMe.value) {
        localStorage.setItem('rememberedUsername', formData.username)
      } else {
        localStorage.removeItem('rememberedUsername')
      }
      // 重置标签页数据
      useTabStore().reset()
      // 登录成功提示
      ElMessage.success('登录成功，欢迎回来！')
      // 延迟跳转，让用户看到成功提示
      setTimeout(() => {
        loading.value = false
        $router.push('/home')
      }, 500)
    },
    (error) => {
      loading.value = false
      // 开发环境下输出详细错误信息
      if (import.meta.env.DEV) {
        console.error('[登录失败] 错误详情:', error)
      }
      // 根据错误类型显示不同的错误提示
      let errorMessage = '账号或密码错误，请重新输入'
      if (error && typeof error === 'object') {
        if ('message' in error && error.message) {
          errorMessage = error.message as string
        } else if ('data' in error && error.data && typeof error.data === 'object' && 'message' in error.data) {
          errorMessage = (error.data as any).message
        }
      } else if (typeof error === 'string') {
        errorMessage = error
      }
      ElMessage.error(errorMessage)
    }
  )
}

// 定义登录提交函数
async function submitForm() {
  if (!loginFormRef.value) return

  // 验证表单
  await loginFormRef.value.validate((valid) => {
    if (valid) {
      // 是否启用验证码
      if (!enableVerify.value) {
        doLogin('')
        return
      }
      // 弹出验证码框
      useVerify('clickWord')
    }
  })
}

// 验证码组件引用
const verify = ref<InstanceType<typeof Verify> | null>(null)

// 验证码类型
const captchaType = ref('')

/**
 * 弹出验证码框
 * @param type 验证码类型 blockPuzzle滑块验证 clickWord点击文字验证
 */
function useVerify(type: string) {
  captchaType.value = type
  if (verify.value) verify.value.show()
}

/**
 * 验证码验证通过执行登录二次验证逻辑
 * @param res 验证通过信息
 */
function handleSuccess(res: { captchaVerification: string }) {
  doLogin(res.captchaVerification)
}

// 组件挂载时恢复记住的用户名和启动二进制代码动画
onMounted(() => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    formData.username = rememberedUsername
    rememberMe.value = true
  }
  
  // 启动二进制代码动画
  binaryTimer = window.setInterval(() => {
    generateBinaryCode()
  }, 2000)
})

// 组件卸载时清理定时器
onBeforeUnmount(() => {
  if (binaryTimer) {
    clearInterval(binaryTimer)
  }
})
</script>

<style scoped>
.login-container {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 左侧品牌展示区域 */
.login-banner {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.banner-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: #fff;
}

.logo-wrapper {
  margin-bottom: 30px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

.logo-circle {
  width: 140px;
  height: 140px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.system-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 15px 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.system-subtitle {
  font-size: 18px;
  opacity: 0.9;
  margin: 0 0 20px 0;
  letter-spacing: 1px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
}

.planet-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.25);
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
  }
  50% {
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.6);
  }
}

.digital-code {
  margin-bottom: 30px;
  padding: 15px 25px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  font-family: 'Courier New', monospace;
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.code-text {
  font-size: 16px;
  color: #fff;
  letter-spacing: 3px;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.banner-features {
  display: flex;
  gap: 40px;
  justify-content: center;
  margin-top: 60px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px);
}

.feature-item span {
  font-size: 14px;
  font-weight: 500;
}

/* 装饰性几何图形 */
.geometric-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 30% 70% 70% 30% / 30% 30% 70% 70%;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  animation: rotate 20s linear infinite;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  left: 10%;
  animation: rotate 15s linear infinite reverse;
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 40%;
  left: -50px;
  animation: rotate 25s linear infinite;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 星球元素 */
.planet {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.3),
    inset -10px -10px 20px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(5px);
}

.planet-1 {
  width: 120px;
  height: 120px;
  top: 15%;
  right: 15%;
  animation: orbit 30s linear infinite;
}

.planet-2 {
  width: 80px;
  height: 80px;
  bottom: 20%;
  right: 25%;
  background: rgba(255, 255, 255, 0.1);
  animation: orbit 40s linear infinite reverse;
}

.planet-ring {
  position: absolute;
  width: 160px;
  height: 160px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) rotateX(75deg);
  border: 3px solid rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  animation: ringRotate 10s linear infinite;
}

@keyframes ringRotate {
  0% {
    transform: translate(-50%, -50%) rotateX(75deg) rotateZ(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotateX(75deg) rotateZ(360deg);
  }
}

.planet-surface {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}

.binary-text {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  font-family: 'Courier New', monospace;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
  animation: binaryGlow 2s ease-in-out infinite;
}

@keyframes binaryGlow {
  0%,
  100% {
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
  }
  50% {
    text-shadow: 0 0 20px rgba(255, 255, 255, 1), 0 0 30px rgba(255, 255, 255, 0.8);
  }
}

@keyframes orbit {
  0% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-20px) translateX(10px);
  }
  50% {
    transform: translateY(0) translateX(20px);
  }
  75% {
    transform: translateY(20px) translateX(10px);
  }
  100% {
    transform: translateY(0) translateX(0);
  }
}

/* 星星元素 */
.star {
  position: absolute;
  width: 3px;
  height: 3px;
  background: #fff;
  border-radius: 50%;
  box-shadow: 0 0 6px rgba(255, 255, 255, 0.8);
  animation: twinkle 3s ease-in-out infinite;
}

.star-1 {
  top: 10%;
  left: 20%;
  animation-delay: 0s;
}

.star-2 {
  top: 25%;
  left: 80%;
  animation-delay: 0.5s;
}

.star-3 {
  top: 60%;
  left: 15%;
  animation-delay: 1s;
}

.star-4 {
  top: 80%;
  right: 30%;
  animation-delay: 1.5s;
}

.star-5 {
  top: 40%;
  right: 10%;
  width: 4px;
  height: 4px;
  animation-delay: 2s;
}

@keyframes twinkle {
  0%,
  100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.5);
  }
}

/* 右侧登录表单区域 */
.login-form-wrapper {
  width: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  box-shadow: -10px 0 50px rgba(0, 0, 0, 0.1);
}

.login-form-container {
  width: 100%;
  max-width: 400px;
  padding: 40px;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-title {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin: 0 0 10px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.form-subtitle {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-form {
  margin-top: 30px;
}

.form-options {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.test-link {
  color: inherit;
  text-decoration: none;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.form-footer {
  margin-top: 40px;
  text-align: center;
}

.copyright {
  font-size: 12px;
  color: #999;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-banner {
    display: none;
  }

  .login-form-wrapper {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .login-form-wrapper {
    padding: 20px;
  }

  .login-form-container {
    padding: 20px;
  }

  .form-title {
    font-size: 28px;
  }
}

/* Element Plus 样式覆盖 */
:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #e4e7ed inset;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #667eea inset;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-checkbox__label) {
  font-size: 14px;
  color: #666;
}
</style>
