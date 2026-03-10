import { h, defineComponent } from 'vue'

// iconfont 图标组件
export default defineComponent({
  name: 'FontIcon',
  props: {
    icon: {
      type: String,
      default: ''
    },
    iconType: {
      type: String,
      default: ''
    }
  },
  render() {
    const attrs = this.$attrs
    const { icon, iconType } = this

    // unicode 模式
    if (attrs?.['uni'] || iconType === 'uni') {
      return h(
        'i',
        {
          class: 'iconfont',
          ...attrs
        },
        icon
      )
    }

    // svg 模式
    if (attrs?.['svg'] || iconType === 'svg') {
      return h(
        'svg',
        {
          class: 'icon-svg',
          'aria-hidden': false,
          ...attrs
        },
        {
          default: () => [
            h('use', {
              'xlink:href': `#${icon}`
            })
          ]
        }
      )
    }

    // font-class 模式（默认）
    return h('i', {
      class: `iconfont ${icon}`,
      ...attrs
    })
  }
})
