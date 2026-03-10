import { h, defineComponent } from 'vue'
import { Icon as IconifyIcon, addIcon } from '@iconify/vue/dist/offline'
import type { ExtendedIconifyIcon } from '@iconify/types'

// Iconify Icon在Vue里本地使用（用于内网环境）
export default defineComponent({
  name: 'IconifyIconOffline',
  components: { IconifyIcon },
  props: {
    icon: {
      type: [String, Object],
      default: null
    }
  },
  render() {
    if (typeof this.icon === 'object')
      addIcon(this.icon as unknown as string, this.icon as unknown as ExtendedIconifyIcon)
    const attrs = this.$attrs
    if (typeof this.icon === 'string') {
      return h(
        IconifyIcon,
        {
          icon: this.icon,
          'aria-hidden': false,
          style: attrs?.style
            ? Object.assign(attrs.style, { outline: 'none' })
            : { outline: 'none' },
          ...attrs
        },
        {
          default: () => []
        }
      )
    } else {
      return h(
        this.icon,
        {
          'aria-hidden': false,
          style: attrs?.style
            ? Object.assign(attrs.style, { outline: 'none' })
            : { outline: 'none' },
          ...attrs
        },
        {
          default: () => []
        }
      )
    }
  }
})
