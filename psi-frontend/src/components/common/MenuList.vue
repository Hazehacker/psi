<template>
	<div class="menuList" :level="level">
		<template v-for="(list, index) in menuList" :key="list.id">
			<!-- 独立菜单 -->
			<el-menu-item v-if="getMenuType(list)==='routine'" :index="list.key">
				<template #title>
					<i v-if="list.ico !== ''" :class="list.ico"></i>
					<span>{{ list.name }}</span>
				</template>
			</el-menu-item>
			
			<!-- 附属菜单 -->
			<el-menu-item-group v-else-if="getMenuType(list)==='subsidiary'" class="subsidiaryMenu">
				<el-menu-item :index="list.key">
					<template #title>
						<i v-if="list.ico !== ''" :class="list.ico"></i>
						<span>{{ list.name }}</span>
					</template>
				</el-menu-item>
				<el-menu-item :index="list.sub[0].key">
					<template #title>
						<span>{{ list.sub[0].name.split('|')[0] }}</span>
					</template>
				</el-menu-item>
			</el-menu-item-group>
			
			<!-- 父子菜单 -->
			<el-submenu v-else :index="list.key">
				<template #title>
					<i v-if="list.ico !== ''" :class="list.ico"></i>
					<span>{{ list.name }}</span>
				</template>
				<Menulist :menuList="list.sub" :level="level+1" />
			</el-submenu>
		</template>
	</div>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
	name: "Menulist",
	props: {
		menuList: {
			required: true,
			type: Array
		},
		level: {
			type: Number,
			default: 1
		}
	},
	methods: {
		//检查子菜单类型
		getMenuType(menu) {
			if (menu.sub.length === 0) {
				return 'routine'
			} else if (menu.sub.length === 1 && menu.sub[0].type === 1) {
				return 'subsidiary'
			} else {
				return 'relation'
			}
		}
	}
})
</script>