<template>
  <!-- 根元素：用于继承外部传入的属性（如 class） -->
  <div v-bind="$attrs">
    <!-- 内联模式：直接显示表单 -->
    <div v-if="config.inline" class="inline-search-form">
    <el-form class="searchFrom" ref="searchFormRef" inline>
      <!-- 商品名称 -->
      <el-form-item v-if="config.showGoods !== false">
        <el-input placeholder="请输入商品名称" v-model="formData.goods" clearable></el-input>
      </el-form-item>

      <!-- 单据编号 -->
      <el-form-item v-if="config.showNumber !== false">
        <el-input placeholder="请输入单据编号" v-model="formData.number" clearable></el-input>
      </el-form-item>

      <!-- 供应商（点击弹出供应商高级搜索） -->
      <el-form-item v-if="config.showSupplier !== false">
        <el-input
          v-model="formData.supplier"
          placeholder="请选择供应商"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleSupplierInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleSupplierInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 客户（点击弹出客户高级搜索） -->
      <el-form-item v-if="config.showCustomer === true">
        <el-input
          v-model="formData.customer"
          placeholder="请选择客户"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleCustomerInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleCustomerInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 仓库（点击弹出仓库高级搜索） -->
      <el-form-item v-if="config.showWarehouse === true">
        <el-input
          v-model="formData.warehouse"
          placeholder="请选择仓库"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleWarehouseInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleWarehouseInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 资金账户（点击弹出账户高级搜索） -->
      <el-form-item v-if="config.showAccount === true">
        <el-input
          v-model="formData.account"
          placeholder="请选择资金账户"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleAccountInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleAccountInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 关联人员（点击弹出人员高级搜索） -->
      <el-form-item v-if="config.showPeople !== false">
        <el-input
          v-model="formData.people"
          placeholder="请选择关联人员"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handlePeopleInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handlePeopleInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 单据开始日期 -->
      <el-form-item v-if="config.showBillDate !== false">
        <el-date-picker
          v-model="formData.startTime"
          placeholder="单据开始日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 单据结束日期 -->
      <el-form-item v-if="config.showBillDate !== false">
        <el-date-picker
          v-model="formData.endTime"
          placeholder="单据结束日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 到货开始日期 -->
      <el-form-item v-if="config.showArrivalDate !== false">
        <el-date-picker
          v-model="formData.startArrival"
          placeholder="到货开始日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 到货结束日期 -->
      <el-form-item v-if="config.showArrivalDate !== false">
        <el-date-picker
          v-model="formData.endArrival"
          placeholder="到货结束日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 制单人（点击弹出用户高级搜索） -->
      <el-form-item v-if="config.showUser !== false">
        <el-input
          v-model="formData.user"
          :placeholder="config.userPlaceholder || '请选择制单人'"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleUserInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleUserInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 审核状态 -->
      <el-form-item v-if="config.showExamine !== false">
        <el-select
          v-model="formData.examine"
          placeholder="请选择审核状态"
          clearable
          class="full-width-select"
          :teleported="false"
          @click.stop
        >
          <el-option label="未审核" :value="1" @click.stop></el-option>
          <el-option label="已审核" :value="2" @click.stop></el-option>
        </el-select>
      </el-form-item>

      <!-- 入库状态 -->
      <el-form-item v-if="config.showState !== false">
        <el-select
          v-model="formData.state"
          placeholder="请选择入库状态"
          clearable
          class="full-width-select"
          :teleported="false"
          @click.stop
        >
          <el-option label="未入库" :value="1" @click.stop></el-option>
          <el-option label="部分入库" :value="2" @click.stop></el-option>
          <el-option label="已入库" :value="3" @click.stop></el-option>
          <el-option label="关闭" :value="4" @click.stop></el-option>
        </el-select>
      </el-form-item>

      <!-- 备注信息 -->
      <el-form-item v-if="config.showRemark !== false">
        <el-input placeholder="请输入备注信息" v-model="formData.data" clearable></el-input>
      </el-form-item>

      <!-- 自定义字段 -->
      <template v-if="config.customFields && config.customFields.length > 0">
        <el-form-item v-for="field in config.customFields" :key="field.key">
          <!-- 输入框类型 -->
          <el-input
            v-if="field.type === 'input'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            clearable
          ></el-input>

          <!-- 下拉选择类型 -->
          <el-select
            v-else-if="field.type === 'select'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            :multiple="field.multiple || false"
            :collapse-tags="field.collapseTags !== false"
            clearable
            class="full-width-select"
            :teleported="false"
            @click.stop
          >
            <el-option
              v-for="option in field.options"
              :key="option.value"
              :label="option.label"
              :value="option.value"
              @click.stop
            ></el-option>
          </el-select>

          <!-- 日期选择类型 -->
          <el-date-picker
            v-else-if="field.type === 'date'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            value-format="yyyy-MM-dd"
            type="date"
          ></el-date-picker>

          <!-- NodList类型 -->
          <nodList
            v-else-if="field.type === 'nodList' && field.nodListConfig"
            v-model="formData[field.key]"
            :placeholder="field.label"
            :action="field.nodListConfig.action"
            :scene="field.nodListConfig.scene"
          ></nodList>

          <!-- 树形选择器类型 -->
          <el-tree-select
            v-else-if="field.type === 'treeSelect'"
            v-model="formData[field.key]"
            :data="field.treeData || []"
            :placeholder="field.label"
            :check-strictly="field.checkStrictly !== false"
            :render-after-expand="field.renderAfterExpand !== false"
            :multiple="field.multiple || false"
            :clearable="field.clearable !== false"
            class="full-width-select"
            :teleported="false"
            @click.stop
            @visible-change="handleSelectVisibleChange"
            @change="handleSelectChange"
          />
        </el-form-item>
      </template>

      <el-divider></el-divider>

      <!-- 搜索按钮 -->
      <el-button class="searchBtn" @click="handleSearch">
        <el-icon><Search /></el-icon>
      </el-button>
    </el-form>
  </div>

  <!-- Popover模式：带触发按钮 -->
  <el-popover
    v-else
    ref="searchPopover"
    v-model:visible="popoverVisible"
    popper-class="searchPopover"
    placement="bottom-start"
    trigger="click"
    :hide-after="0"
    :popper-options="{ modifiers: [{ name: 'eventListeners', options: { scroll: false } }] }"
    @before-hide="handlePopoverBeforeHide"
  >
    <el-form class="searchFrom" ref="searchFormRef" inline>
      <!-- 商品名称 -->
      <el-form-item v-if="config.showGoods !== false">
        <el-input placeholder="请输入商品名称" v-model="formData.goods" clearable></el-input>
      </el-form-item>

      <!-- 单据编号 -->
      <el-form-item v-if="config.showNumber !== false">
        <el-input placeholder="请输入单据编号" v-model="formData.number" clearable></el-input>
      </el-form-item>

      <!-- 供应商（点击弹出供应商高级搜索） -->
      <el-form-item v-if="config.showSupplier !== false">
        <el-input
          v-model="formData.supplier"
          placeholder="请选择供应商"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleSupplierInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleSupplierInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 客户（点击弹出客户高级搜索） -->
      <el-form-item v-if="config.showCustomer === true">
        <el-input
          v-model="formData.customer"
          placeholder="请选择客户"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleCustomerInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleCustomerInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 仓库（点击弹出仓库高级搜索） -->
      <el-form-item v-if="config.showWarehouse === true">
        <el-input
          v-model="formData.warehouse"
          placeholder="请选择仓库"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleWarehouseInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleWarehouseInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 资金账户（点击弹出账户高级搜索） -->
      <el-form-item v-if="config.showAccount === true">
        <el-input
          v-model="formData.account"
          placeholder="请选择资金账户"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleAccountInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleAccountInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 关联人员（点击弹出人员高级搜索） -->
      <el-form-item v-if="config.showPeople !== false">
        <el-input
          v-model="formData.people"
          placeholder="请选择关联人员"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handlePeopleInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handlePeopleInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 单据开始日期 -->
      <el-form-item v-if="config.showBillDate !== false">
        <el-date-picker
          v-model="formData.startTime"
          placeholder="单据开始日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 单据结束日期 -->
      <el-form-item v-if="config.showBillDate !== false">
        <el-date-picker
          v-model="formData.endTime"
          placeholder="单据结束日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 到货开始日期 -->
      <el-form-item v-if="config.showArrivalDate !== false">
        <el-date-picker
          v-model="formData.startArrival"
          placeholder="到货开始日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 到货结束日期 -->
      <el-form-item v-if="config.showArrivalDate !== false">
        <el-date-picker
          v-model="formData.endArrival"
          placeholder="到货结束日期"
          value-format="yyyy-MM-dd"
          type="date"
        ></el-date-picker>
      </el-form-item>

      <!-- 制单人（点击弹出用户高级搜索） -->
      <el-form-item v-if="config.showUser !== false">
        <el-input
          v-model="formData.user"
          :placeholder="config.userPlaceholder || '请选择制单人'"
          readonly
          clearable
          style="cursor: pointer"
          @focus="handleUserInputFocus"
        >
          <template #suffix>
            <el-icon @click.stop="handleUserInputFocus" style="cursor: pointer">
              <Search />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 审核状态 -->
      <el-form-item v-if="config.showExamine !== false">
        <el-select
          v-model="formData.examine"
          placeholder="请选择审核状态"
          clearable
          class="full-width-select"
          :teleported="false"
          @visible-change="handleSelectVisibleChange"
          @change="handleSelectChange"
          @click.stop
        >
          <el-option label="未审核" :value="1" @click.stop></el-option>
          <el-option label="已审核" :value="2" @click.stop></el-option>
        </el-select>
      </el-form-item>

      <!-- 入库状态 -->
      <el-form-item v-if="config.showState !== false">
        <el-select
          v-model="formData.state"
          placeholder="请选择入库状态"
          clearable
          class="full-width-select"
          :teleported="false"
          @visible-change="handleSelectVisibleChange"
          @change="handleSelectChange"
          @click.stop
        >
          <el-option label="未入库" :value="1" @click.stop></el-option>
          <el-option label="部分入库" :value="2" @click.stop></el-option>
          <el-option label="已入库" :value="3" @click.stop></el-option>
          <el-option label="关闭" :value="4" @click.stop></el-option>
        </el-select>
      </el-form-item>

      <!-- 备注信息 -->
      <el-form-item v-if="config.showRemark !== false">
        <el-input placeholder="请输入备注信息" v-model="formData.data" clearable></el-input>
      </el-form-item>

      <!-- 自定义字段 -->
      <template v-if="config.customFields && config.customFields.length > 0">
        <el-form-item v-for="field in config.customFields" :key="field.key">
          <!-- 输入框类型 -->
          <el-input
            v-if="field.type === 'input'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            clearable
          ></el-input>

          <!-- 下拉选择类型 -->
          <el-select
            v-else-if="field.type === 'select'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            :multiple="field.multiple || false"
            :collapse-tags="field.collapseTags !== false"
            clearable
            class="full-width-select"
            :teleported="false"
            @visible-change="handleSelectVisibleChange"
            @change="handleSelectChange"
            @click.stop
          >
            <el-option
              v-for="option in field.options"
              :key="option.value"
              :label="option.label"
              :value="option.value"
              @click.stop
            ></el-option>
          </el-select>

          <!-- 日期选择类型 -->
          <el-date-picker
            v-else-if="field.type === 'date'"
            v-model="formData[field.key]"
            :placeholder="field.label"
            value-format="yyyy-MM-dd"
            type="date"
          ></el-date-picker>

          <!-- NodList类型 -->
          <nodList
            v-else-if="field.type === 'nodList' && field.nodListConfig"
            v-model="formData[field.key]"
            :placeholder="field.label"
            :action="field.nodListConfig.action"
            :scene="field.nodListConfig.scene"
          ></nodList>

          <!-- 树形选择器类型 -->
          <el-tree-select
            v-else-if="field.type === 'treeSelect'"
            v-model="formData[field.key]"
            :data="field.treeData || []"
            :placeholder="field.label"
            :check-strictly="field.checkStrictly !== false"
            :render-after-expand="field.renderAfterExpand !== false"
            :multiple="field.multiple || false"
            :clearable="field.clearable !== false"
            class="full-width-select"
            :teleported="false"
            @click.stop
            @visible-change="handleSelectVisibleChange"
            @change="handleSelectChange"
          />
        </el-form-item>
      </template>

      <el-divider></el-divider>

      <!-- 搜索按钮 -->
      <el-button class="searchBtn" @click="handleSearch">
        <el-icon><Search /></el-icon>
      </el-button>
      <!-- 关闭按钮 -->
      <el-button class="closeBtn" @click.stop="handleClosePopover"> 关闭 </el-button>
    </el-form>

    <!-- 触发按钮 -->
    <template #reference>
      <el-button>
        <el-icon><MoreFilled /></el-icon>
      </el-button>
    </template>
  </el-popover>

  <!-- 供应商高级搜索弹窗 -->
  <el-dialog
    :model-value="showSupplierDialog"
    @update:model-value="handleSupplierDialogUpdate"
    title="供应商搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handleSupplierDialogOpen"
    @closed="handleSupplierDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <Supplier @search="handleSupplierDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleCloseSupplierDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 人员高级搜索弹窗 -->
  <el-dialog
    :model-value="showPeopleDialog"
    @update:model-value="handlePeopleDialogUpdate"
    title="人员搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handlePeopleDialogOpen"
    @closed="handlePeopleDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <PeopleList @search="handlePeopleDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleClosePeopleDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 用户高级搜索弹窗 -->
  <el-dialog
    :model-value="showUserDialog"
    @update:model-value="handleUserDialogUpdate"
    title="用户搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handleUserDialogOpen"
    @closed="handleUserDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <UserList @search="handleUserDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleCloseUserDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 客户高级搜索弹窗 -->
  <el-dialog
    :model-value="showCustomerDialog"
    @update:model-value="handleCustomerDialogUpdate"
    title="客户搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handleCustomerDialogOpen"
    @closed="handleCustomerDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <Customer @search="handleCustomerDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleCloseCustomerDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 仓库高级搜索弹窗 -->
  <el-dialog
    :model-value="showWarehouseDialog"
    @update:model-value="handleWarehouseDialogUpdate"
    title="仓库搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handleWarehouseDialogOpen"
    @closed="handleWarehouseDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <Warehouse @search="handleWarehouseDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleCloseWarehouseDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 资金账户高级搜索弹窗 -->
  <el-dialog
    :model-value="showAccountDialog"
    @update:model-value="handleAccountDialogUpdate"
    title="资金账户搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    destroy-on-close
    @open="handleAccountDialogOpen"
    @closed="handleAccountDialogClosed"
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <Account @search="handleAccountDialogSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="handleCloseAccountDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted, nextTick } from 'vue'
import type { SearchFormData, GoodSearchConfig } from './type'
import NodList from '@/components/lib/NodList.vue'
import { Search, MoreFilled } from '@element-plus/icons-vue'
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import Account from '@/components/goodSearchConpoent/Account.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'
import UserList from '@/components/goodSearchConpoent/UserList.vue'

// 禁用自动属性继承，因为我们手动使用 $attrs 绑定
defineOptions({
  inheritAttrs: false
})

/**
 * 组件属性
 */
interface Props {
  /** 表单数据（支持v-model） */
  modelValue?: SearchFormData
  /** 配置项 */
  config?: GoodSearchConfig
  /** 默认日期范围（天数） */
  defaultDays?: number
}

/**
 * 组件事件
 */
interface Emits {
  (e: 'update:modelValue', value: SearchFormData): void
  (e: 'search', value: SearchFormData): void
  (e: 'confirm', value: any): void
  (e: 'reset'): void
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ({}),
  config: () => ({}),
  defaultDays: 30
})

const emit = defineEmits<Emits>()

// 表单引用
const searchPopover = ref()
const searchFormRef = ref()

// 表单数据
const formData = reactive<SearchFormData>({
  goods: '',
  number: '',
  supplier: null,
  customer: null,
  warehouse: null,
  account: null,
  people: null,
  user: null,
  startTime: '',
  endTime: '',
  startArrival: '',
  endArrival: '',
  examine: '',
  state: '',
  data: ''
})

// Popover 显示控制
const popoverVisible = ref(false)
// 锁定 popover，防止被意外关闭
const popoverLocked = ref(false)
// 标记是否有对话框打开（用于禁用 popover 的自动触发）
const hasDialogOpen = ref(false)
// 标记是否有 select 下拉菜单打开
const hasSelectOpen = ref(false)
// 标记是否是手动关闭（点击关闭按钮）
const isManualClose = ref(false)

// 供应商弹窗控制
const showSupplierDialog = ref(false)
// 标记是否正在处理供应商对话框关闭，防止重复触发
const isClosingSupplierDialog = ref(false)

// 人员弹窗控制
const showPeopleDialog = ref(false)
// 标记是否正在处理人员对话框关闭，防止重复触发
const isClosingPeopleDialog = ref(false)

// 用户弹窗控制
const showUserDialog = ref(false)
// 标记是否正在处理用户对话框关闭，防止重复触发
const isClosingUserDialog = ref(false)

// 客户弹窗控制
const showCustomerDialog = ref(false)
// 标记是否正在处理客户对话框关闭，防止重复触发
const isClosingCustomerDialog = ref(false)

// 仓库弹窗控制
const showWarehouseDialog = ref(false)
// 标记是否正在处理仓库对话框关闭，防止重复触发
const isClosingWarehouseDialog = ref(false)

// 资金账户弹窗控制
const showAccountDialog = ref(false)
// 标记是否正在处理账户对话框关闭，防止重复触发
const isClosingAccountDialog = ref(false)

/**
 * 初始化表单数据
 */
const initFormData = () => {
  // 如果配置了默认日期范围，则初始化日期
  if (props.config.showBillDate !== false && props.defaultDays > 0) {
    const today = new Date()
    const startDate = new Date()
    startDate.setDate(today.getDate() - props.defaultDays)

    formData.startTime = formatDate(startDate)
    formData.endTime = formatDate(today)
  }

  // 合并外部传入的值
  if (props.modelValue) {
    Object.assign(formData, props.modelValue)
  }

  // 初始化多选字段为数组类型
  if (props.config.customFields) {
    props.config.customFields.forEach((field) => {
      if (
        (field.type === 'select' || field.type === 'treeSelect') &&
        field.multiple &&
        !Array.isArray(formData[field.key])
      ) {
        formData[field.key] = []
      }
    })
  }
}

/**
 * 格式化日期
 */
const formatDate = (date: Date): string => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  emit('update:modelValue', formData)
  emit('search', formData)

  // 关闭弹窗
  if (searchPopover.value) {
    searchPopover.value.showPopper = false
  }
}

/**
 * 手动关闭 popover
 * 强制关闭，不受锁定状态影响
 */
const handleClosePopover = () => {
  // 标记为手动关闭
  isManualClose.value = true
  // 先解锁所有状态，确保可以关闭
  popoverLocked.value = false
  hasSelectOpen.value = false
  // 清除 select 关闭的计时器
  if (selectCloseTimer) {
    clearTimeout(selectCloseTimer)
    selectCloseTimer = null
  }
  // 然后关闭 popover
  popoverVisible.value = false
  // 重置标志
  nextTick(() => {
    isManualClose.value = false
  })
}

/**
 * Popover 关闭前的拦截处理
 * 当 popover 被锁定时，阻止关闭
 * 返回 false 阻止关闭，返回 true 或不返回值允许关闭
 */
const handlePopoverBeforeHide = () => {
  // 如果是手动关闭，总是允许
  if (isManualClose.value) {
    return true
  }
  // 只有在 select 下拉菜单真正打开时才阻止关闭
  // 对话框的锁定通过其他机制处理
  if (hasSelectOpen.value) {
    return false
  }
  // 否则允许关闭
  return true
}

// select 关闭的计时器
let selectCloseTimer: ReturnType<typeof setTimeout> | null = null

/**
 * 处理 select 下拉菜单的显示/隐藏
 */
const handleSelectVisibleChange = (visible: boolean) => {
  if (visible) {
    // select 打开时，立即设置标志
    hasSelectOpen.value = true
    // 清除之前的计时器
    if (selectCloseTimer) {
      clearTimeout(selectCloseTimer)
      selectCloseTimer = null
    }
  } else {
    // select 关闭时，延迟清除标志
    // 使用更长的延迟（400ms），确保所有点击事件都已完成
    if (selectCloseTimer) {
      clearTimeout(selectCloseTimer)
    }
    selectCloseTimer = setTimeout(() => {
      hasSelectOpen.value = false
      selectCloseTimer = null
    }, 400)
  }
}

/**
 * 处理 select 值变化
 * 当值改变时，确保标志保持为 true，防止 popover 关闭
 */
const handleSelectChange = () => {
  // 值改变时，确保 hasSelectOpen 保持为 true
  // 这会覆盖 handleSelectVisibleChange 中可能的清除操作
  hasSelectOpen.value = true
  // 清除之前的计时器
  if (selectCloseTimer) {
    clearTimeout(selectCloseTimer)
  }
  // 延迟清除，确保点击事件不会触发 popover 关闭
  selectCloseTimer = setTimeout(() => {
    hasSelectOpen.value = false
    selectCloseTimer = null
  }, 400)
}

/**
 * 重置表单
 */
const resetForm = () => {
  if (searchFormRef.value) {
    searchFormRef.value.resetFields()
  }
  initFormData()
  emit('reset')
}

// 监听外部数据变化
watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal) {
      Object.assign(formData, newVal)
    }
  },
  { deep: true }
)

// 监听内部数据变化
watch(
  formData,
  (newVal) => {
    emit('update:modelValue', newVal)
  },
  { deep: true }
)

// 监听 popoverVisible 的变化，如果被锁定则阻止关闭
watch(
  popoverVisible,
  (newVal, oldVal) => {
    if (!newVal && oldVal && popoverLocked.value) {
      // 如果 popover 被锁定，立即阻止关闭
      nextTick(() => {
        popoverVisible.value = true
      })
    }
  },
  { flush: 'sync' }
)

// 监听供应商对话框状态，控制 popover 的显示
watch(
  showSupplierDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 供应商对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 供应商对话框关闭时，标记正在关闭
      isClosingSupplierDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingSupplierDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 监听人员对话框状态，控制 popover 的显示
watch(
  showPeopleDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 人员对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 人员对话框关闭时，标记正在关闭
      isClosingPeopleDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingPeopleDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 监听用户对话框状态，控制 popover 的显示
watch(
  showUserDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 用户对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 用户对话框关闭时，标记正在关闭
      isClosingUserDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingUserDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 监听客户对话框状态，控制 popover 的显示
watch(
  showCustomerDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 客户对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 客户对话框关闭时，标记正在关闭
      isClosingCustomerDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingCustomerDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 监听仓库对话框状态，控制 popover 的显示
watch(
  showWarehouseDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 仓库对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 仓库对话框关闭时，标记正在关闭
      isClosingWarehouseDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingWarehouseDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 监听账户对话框状态变化
watch(
  showAccountDialog,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 账户对话框打开时，立即锁定 popover 防止关闭，并禁用 popover 触发
      popoverLocked.value = true
      hasDialogOpen.value = true
      // 确保 popover 保持可见
      if (!props.config.inline) {
        popoverVisible.value = true
      }
    } else if (!newVal && oldVal) {
      // 账户对话框关闭时，标记正在关闭
      isClosingAccountDialog.value = true

      // 延迟解锁 popover 并恢复触发功能
      setTimeout(() => {
        popoverLocked.value = false
        hasDialogOpen.value = false
        isClosingAccountDialog.value = false
        // 确保 popover 保持可见
        if (!props.config.inline) {
          popoverVisible.value = true
        }
      }, 200)
    }
  },
  { flush: 'sync' }
)

// 初始化
onMounted(() => {
  initFormData()
})

// 暴露方法给父组件
defineExpose({
  resetForm,
  formData
})

/**
 * 处理供应商输入框获得焦点：直接打开供应商搜索对话框
 */
function handleSupplierInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingSupplierDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showSupplierDialog.value = true
  }
}

/**
 * 关闭供应商搜索对话框
 */
function handleCloseSupplierDialog() {
  showSupplierDialog.value = false
}

/**
 * 处理供应商对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handleSupplierDialogUpdate(val: boolean) {
  showSupplierDialog.value = val
}

/**
 * 处理供应商弹窗内的搜索：取回供应商名称填入当前表单
 * 说明：Supplier 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handleSupplierDialogSearch(params: any) {
  // 以供应商名称作为本表单的 supplier 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 supplierName
    const name = params.name || params.supplierName || ''
    formData.supplier = name
  }
  showSupplierDialog.value = false
}

/**
 * 供应商对话框打开时的回调
 */
function handleSupplierDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 供应商对话框完全关闭后的回调
 */
function handleSupplierDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 处理人员输入框获得焦点：直接打开人员搜索对话框
 */
function handlePeopleInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingPeopleDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showPeopleDialog.value = true
  }
}

/**
 * 关闭人员搜索对话框
 */
function handleClosePeopleDialog() {
  showPeopleDialog.value = false
}

/**
 * 处理人员对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handlePeopleDialogUpdate(val: boolean) {
  showPeopleDialog.value = val
}

/**
 * 处理人员弹窗内的搜索：取回人员名称填入当前表单
 * 说明：PeopleList 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handlePeopleDialogSearch(params: any) {
  // 以人员名称作为本表单的 people 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 peopleName
    const name = params.name || params.peopleName || ''
    formData.people = name
  }
  showPeopleDialog.value = false
}

/**
 * 人员对话框打开时的回调
 */
function handlePeopleDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 人员对话框完全关闭后的回调
 */
function handlePeopleDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 处理用户输入框获得焦点：直接打开用户搜索对话框
 */
function handleUserInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingUserDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showUserDialog.value = true
  }
}

/**
 * 关闭用户搜索对话框
 */
function handleCloseUserDialog() {
  showUserDialog.value = false
}

/**
 * 处理用户对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handleUserDialogUpdate(val: boolean) {
  showUserDialog.value = val
}

/**
 * 处理用户弹窗内的搜索：取回用户名称填入当前表单
 * 说明：UserList 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handleUserDialogSearch(params: any) {
  // 以用户名称作为本表单的 user 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 userName
    const name = params.name || params.userName || ''
    formData.user = name
  }
  showUserDialog.value = false
}

/**
 * 用户对话框打开时的回调
 */
function handleUserDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 用户对话框完全关闭后的回调
 */
function handleUserDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 处理客户输入框获得焦点：直接打开客户搜索对话框
 */
function handleCustomerInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingCustomerDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showCustomerDialog.value = true
  }
}

/**
 * 关闭客户搜索对话框
 */
function handleCloseCustomerDialog() {
  showCustomerDialog.value = false
}

/**
 * 处理客户对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handleCustomerDialogUpdate(val: boolean) {
  showCustomerDialog.value = val
}

/**
 * 处理客户弹窗内的搜索：取回客户名称填入当前表单
 * 说明：Customer 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handleCustomerDialogSearch(params: any) {
  // 以客户名称作为本表单的 customer 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 customerName
    const name = params.name || params.customerName || ''
    formData.customer = name
  }
  showCustomerDialog.value = false
}

/**
 * 客户对话框打开时的回调
 */
function handleCustomerDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 客户对话框完全关闭后的回调
 */
function handleCustomerDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 处理仓库输入框获得焦点：直接打开仓库搜索对话框
 */
function handleWarehouseInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingWarehouseDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showWarehouseDialog.value = true
  }
}

/**
 * 关闭仓库搜索对话框
 */
function handleCloseWarehouseDialog() {
  showWarehouseDialog.value = false
}

/**
 * 处理仓库对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handleWarehouseDialogUpdate(val: boolean) {
  showWarehouseDialog.value = val
}

/**
 * 处理仓库弹窗内的搜索：取回仓库名称填入当前表单
 * 说明：Warehouse 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handleWarehouseDialogSearch(params: any) {
  // 以仓库名称作为本表单的 warehouse 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 warehouseName
    const name = params.name || params.warehouseName || ''
    formData.warehouse = name
  }
  showWarehouseDialog.value = false
}

/**
 * 仓库对话框打开时的回调
 */
function handleWarehouseDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 仓库对话框完全关闭后的回调
 */
function handleWarehouseDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 处理账户输入框获得焦点：直接打开账户搜索对话框
 */
function handleAccountInputFocus() {
  // 如果正在关闭对话框，不重新打开
  if (!isClosingAccountDialog.value) {
    // 在打开对话框前，先锁定 popover
    popoverLocked.value = true
    hasDialogOpen.value = true
    if (!props.config.inline) {
      popoverVisible.value = true
    }
    // 然后打开对话框
    showAccountDialog.value = true
  }
}

/**
 * 关闭账户搜索对话框
 */
function handleCloseAccountDialog() {
  showAccountDialog.value = false
}

/**
 * 处理账户对话框的 model-value 更新
 * 当点击叉号或其他方式关闭对话框时，这个函数会被调用
 */
function handleAccountDialogUpdate(val: boolean) {
  showAccountDialog.value = val
}

/**
 * 处理账户弹窗内的搜索：取回账户名称填入当前表单
 * 说明：Account 组件会以 { name, number, ... } 形式回传搜索条件
 */
function handleAccountDialogSearch(params: any) {
  // 以账户名称作为本表单的 account 显示值
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 accountName
    const name = params.name || params.accountName || ''
    formData.account = name
  }
  showAccountDialog.value = false
}

/**
 * 账户对话框打开时的回调
 */
function handleAccountDialogOpen() {
  // 对话框打开时，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}

/**
 * 账户对话框完全关闭后的回调
 */
function handleAccountDialogClosed() {
  // 对话框关闭后，确保 popover 保持显示（如果在 popover 模式下）
  if (!props.config.inline) {
    popoverVisible.value = true
  }
}
</script>

<style lang="scss" scoped>
.searchFrom {
  max-width: 800px;
}

/* 让下拉框宽度和其他输入框一致 */
.full-width-select {
  width: 200px;
}
.closeBtn {
  margin: 0;
}
</style>
