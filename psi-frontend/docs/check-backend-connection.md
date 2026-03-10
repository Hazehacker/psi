# 前端与后端连接检查指南

本文档详细说明如何检查前端是否成功连接到后端服务器。

## 📋 接口清单

根据后端接口文档，以下是登录相关的所有接口：

| 接口名称 | HTTP方法 | 接口路径 | 状态 | 说明 |
|---------|---------|---------|------|------|
| 授权登录 | POST | `/login/auth-login` | ✅ 已实现 | 需要 username, password, code(可选) |
| 验证码二次校验 | POST | `/login/captcha/second-check` | ⚠️ 未实现 | 当前使用第三方验证码组件 |
| 获取当前用户 | GET | `/login/current-user` | ✅ 已实现 | 无参数 |
| 获取菜单 | GET | `/login/get-menus` | ✅ 已实现 | 无参数 |
| 退出登录 | GET | `/login/logout` | ❌ 未实现 | 无参数 |
| 刷新令牌 | POST | `/login/refresh-token` | ✅ 已实现 | 需要 refreshToken |

## 🔍 检查步骤

### 步骤1：确认后端服务器地址

1. 打开 `vite.config.ts` 文件
2. 检查 `proxy` 配置中的 `target` 地址：
   ```typescript
   proxy: {
     '/api': {
       target: 'http://39.103.62.65:10001',  // 确认这个地址是否正确
       // ...
     }
   }
   ```
3. 如果后端服务器地址不同，请修改为正确的地址

### 步骤2：启动开发服务器

```bash
# 确保开发服务器正在运行
pnpm dev
```

服务器启动后，前端地址通常是：`http://localhost:3000`

### 步骤3：打开浏览器开发者工具

1. 在浏览器中打开登录页面（`http://localhost:3000`）
2. 按 `F12` 打开开发者工具
3. 切换到 **Network（网络）** 标签页
4. 确保 **Preserve log（保留日志）** 选项已勾选（这样页面跳转后请求不会消失）

### 步骤4：测试登录接口

#### 4.1 执行登录操作

1. 在登录页面输入用户名和密码（可以使用后端文档中的示例值：`admin` / `123456`）
2. 如果启用了验证码，完成验证码验证
3. 点击"登录"按钮

#### 4.2 检查登录请求

在 Network 标签页中查找名为 `auth-login` 的请求：

**✅ 成功连接的标志：**

- **请求URL**: 应该显示为 `http://localhost:3000/api/login/auth-login`
- **请求方法**: `POST`
- **状态码**: 
  - `200 OK` - 请求成功
  - `401 Unauthorized` - 认证失败（但连接成功）
  - `400 Bad Request` - 参数错误（但连接成功）
- **请求头 (Headers)**:
  - `Content-Type: application/x-www-form-urlencoded`
- **请求参数 (Payload)**:
  - 应该包含 `username` 和 `password` 字段
  - 如果使用了验证码，可能包含 `code` 字段

**❌ 连接失败的标志：**

- **状态码**: 
  - `(failed)` 或 `net::ERR_CONNECTION_REFUSED` - 无法连接到后端服务器
  - `404 Not Found` - 接口路径错误或代理配置错误
  - `500 Internal Server Error` - 后端服务器错误
- **请求根本没有出现** - 前端代码可能有问题

#### 4.3 查看响应数据

点击 `auth-login` 请求，切换到 **Response（响应）** 标签页：

- **成功响应**: 应该包含 token 相关信息，例如：
  ```json
  {
    "code": 10000,
    "data": {
      "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "..."
    }
  }
  ```

- **错误响应**: 会显示具体的错误信息，例如：
  ```json
  {
    "code": 401,
    "message": "账号或密码错误"
  }
  ```

### 步骤5：检查控制台日志

切换到 **Console（控制台）** 标签页，查看调试信息：

**开发环境下，你应该能看到：**

```
[登录接口] 请求参数: { username: 'admin', password: '***' }
[登录接口] 请求URL: /login/auth-login
[登录接口] 响应数据: { ... }
```

如果登录失败，还会看到：
```
[登录失败] 错误详情: { ... }
```

### 步骤6：测试其他接口

登录成功后，前端会自动调用以下接口：

#### 6.1 获取当前用户信息

- **接口路径**: `/api/login/current-user`
- **请求方法**: `GET`
- **触发时机**: 登录成功后，路由守卫会自动调用
- **检查方法**: 在 Network 标签页中查找 `current-user` 请求

#### 6.2 获取菜单列表

- **接口路径**: `/api/login/get-menus`
- **请求方法**: `GET`
- **触发时机**: 登录成功后，路由守卫会自动调用
- **检查方法**: 在 Network 标签页中查找 `get-menus` 请求

#### 6.3 刷新令牌（自动触发）

- **接口路径**: `/api/login/refresh-token`
- **请求方法**: `POST`
- **触发时机**: Token 过期时自动调用
- **检查方法**: 在 Network 标签页中查找 `refresh-token` 请求

## 🐛 常见问题排查

### 问题1：请求显示为 `(failed)` 或 `net::ERR_CONNECTION_REFUSED`

**可能原因：**
- 后端服务器未启动
- `vite.config.ts` 中的 `target` 地址错误
- 网络连接问题

**解决方法：**
1. 确认后端服务器已启动并可访问
2. 检查 `vite.config.ts` 中的代理配置
3. 尝试在浏览器中直接访问后端地址（例如：`http://39.103.62.65:10001`）

### 问题2：请求返回 `404 Not Found`

**可能原因：**
- 接口路径不正确
- 代理配置的 `rewrite` 规则有问题

**解决方法：**
1. 检查接口路径是否正确（应该是 `/login/auth-login`，不是 `/api/login/auth-login`）
2. 检查 `vite.config.ts` 中的 `rewrite` 配置：
   ```typescript
   rewrite: (path) => path.replace(/^\/api/, '')
   ```

### 问题3：请求返回 `401 Unauthorized`

**可能原因：**
- 用户名或密码错误
- Token 已过期

**解决方法：**
1. 检查用户名和密码是否正确
2. 查看响应数据中的错误信息
3. 检查控制台中的详细错误日志

### 问题4：请求返回 `500 Internal Server Error`

**可能原因：**
- 后端服务器内部错误
- 请求参数格式不正确

**解决方法：**
1. 检查后端服务器日志
2. 确认请求参数格式是否正确（应该是 `application/x-www-form-urlencoded`）
3. 查看响应数据中的错误详情

### 问题5：请求参数格式不正确

**检查方法：**
1. 在 Network 标签页中点击请求
2. 查看 **Headers** 标签页中的 `Content-Type`
3. 查看 **Payload** 标签页中的参数格式

**正确格式应该是：**
```
Content-Type: application/x-www-form-urlencoded

username=admin&password=123456
```

## 📊 快速检查清单

使用以下清单快速检查连接状态：

- [ ] 后端服务器已启动
- [ ] `vite.config.ts` 中的 `target` 地址正确
- [ ] 开发服务器已重启（修改配置后）
- [ ] 浏览器开发者工具已打开
- [ ] Network 标签页已打开
- [ ] 执行登录操作
- [ ] 在 Network 中找到了 `auth-login` 请求
- [ ] 请求状态码为 `200` 或其他非连接错误的状态码
- [ ] 请求头中的 `Content-Type` 正确
- [ ] 请求参数包含 `username` 和 `password`
- [ ] 响应数据格式正确
- [ ] 控制台中有调试日志输出

## 🔧 调试技巧

### 技巧1：使用过滤器

在 Network 标签页的过滤框中输入 `/login`，可以快速筛选出所有登录相关的请求。

### 技巧2：查看请求详情

点击任意请求，可以查看：
- **Headers**: 请求头和响应头
- **Payload**: 请求参数
- **Response**: 响应数据
- **Preview**: 格式化的响应数据（如果是 JSON）

### 技巧3：复制请求为 cURL

右键点击请求，选择 "Copy" -> "Copy as cURL"，可以在命令行中测试请求。

### 技巧4：使用 Postman 或 Apifox 测试

如果前端请求有问题，可以使用 Postman 或 Apifox 直接测试后端接口，确认后端是否正常工作。

## 📝 注意事项

1. **修改配置后必须重启开发服务器**
   - 修改 `vite.config.ts` 后，需要停止并重新启动 `pnpm dev`

2. **清除浏览器缓存**
   - 如果遇到奇怪的问题，尝试清除浏览器缓存或使用无痕模式

3. **检查 CORS 配置**
   - 如果后端有 CORS 限制，确保允许前端地址访问

4. **检查网络代理**
   - 如果使用 VPN 或代理，可能影响本地开发服务器的连接

## 🎯 成功标志

如果以下条件都满足，说明前端已成功连接到后端：

1. ✅ Network 中能看到所有接口请求
2. ✅ 请求状态码为 `200` 或业务错误码（如 `401`）
3. ✅ 响应数据格式正确
4. ✅ 控制台中有调试日志
5. ✅ 登录成功后能正常跳转到主页
6. ✅ 用户信息和菜单数据能正常加载

---

**需要帮助？** 如果按照以上步骤仍无法解决问题，请检查：
- 浏览器控制台的完整错误信息
- Network 标签页中的请求详情
- 后端服务器的日志输出

