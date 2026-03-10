// commitlint.config.cjs
// @ts-check
// eslint-disable-next-line @typescript-eslint/no-require-imports
module.exports = require('@ruan-cat/commitlint-config').getUserConfig({
  config: {
    // 推荐不打印提交范围
    isPrintScopes: false
  }
})
