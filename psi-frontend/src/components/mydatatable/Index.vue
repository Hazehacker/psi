<template>
    <section class="root-container">
        <!-- 商品数据表格 -->
        <MyDataTable
      ref="mydatatable"
      :tabdatacolumns="productColumns"
      :tabdata="tableData"
      :istabpage="true"
      :istabseq="true"
      :showDelete="true"
      scanColumnProp="productName"
      sumField="totalValue"
      @page-change="handlePageChange"
      @delete-click="handleDelete"
    />
    </section>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MyDataTable from '@/components/mydatatable/MyDataTable.vue'
import type { MyTableColumn, MyTableAttr, PageDTO } from '@/components/mydatatable/type'
import { createPageDTO } from '@/components/mydatatable/type'

// 定义商品数据类型
interface ProductData {
    id: number
    productCode: string
    productName: string
    specification: string
    category: string
    unit: string
    unitPrice: number
    purchasePrice: number
    inventory: number
    totalValue: number
    supplier: string
    storageLocation: string
    remarks: string
}

// 商品表格列定义
const productColumns: MyTableColumn[] = [
    {
        prop: 'productCode',
        label: '商品编码',
        width: '100px'
    },
    {
        prop: 'productName',
        label: '商品名称',
        width: '120px',
        'show-overflow-tooltip': true
    },
    {
        prop: 'specification',
        label: '规格型号',
        width: '100px'
    },
    {
        prop: 'category',
        label: '商品类别',
        width: '100px'
    },
    {
        prop: 'unit',
        label: '单位',
        width: '60px',
        align: 'center'
    },
    {
        prop: 'unitPrice',
        label: '单价',
        width: '80px',
        align: 'right'
    },
    {
        prop: 'purchasePrice',
        label: '采购价',
        width: '80px',
        align: 'right'
    },
    {
        prop: 'inventory',
        label: '库存',
        width: '80px',
        align: 'right'
    },
    {
        prop: 'totalValue',
        label: '总金额',
        width: '100px',
        align: 'right'
    },
    {
        prop: 'supplier',
        label: '供应商',
        width: '100px'
    },
    {
        prop: 'storageLocation',
        label: '存储位置',
        width: '100px'
    },
    {
        prop: 'remarks',
        label: '备注',
        'min-width': 100,
        'show-overflow-tooltip': true
    }
]

// 表格数据
const tableData = ref<PageDTO<ProductData>>(createPageDTO())

// 模拟商品数据
const mockProductData: ProductData[] = [
    {
        id: 1,
        productCode: '',
        productName: '',
        specification: '',
        category: '',
        unit: '',
        unitPrice: 0,
        purchasePrice: 0,
        inventory: 0,
        totalValue: 0,
        supplier: '',
        storageLocation: '',
        remarks: ''
    },
    {
        id: 2,
        productCode: 'sp014',
        productName: '测试商品A',
        specification: 'A型',
        category: '电子产品',
        unit: '个',
        unitPrice: 299.99,
        purchasePrice: 199.99,
        inventory: 150,
        totalValue: 44998.5,
        supplier: '供应商A',
        storageLocation: 'A区-01',
        remarks: '热销商品'
    },
    {
        id: 3,
        productCode: 'sp015',
        productName: '测试商品B',
        specification: 'B型',
        category: '日用品',
        unit: '件',
        unitPrice: 89.5,
        purchasePrice: 59.5,
        inventory: 200,
        totalValue: 17900,
        supplier: '供应商B',
        storageLocation: 'B区-02',
        remarks: '库存充足'
    },
    {
        id: 4,
        productCode: 'sp016',
        productName: '测试商品C',
        specification: 'C型',
        category: '办公用品',
        unit: '套',
        unitPrice: 156.8,
        purchasePrice: 98.8,
        inventory: 80,
        totalValue: 12544,
        supplier: '供应商C',
        storageLocation: 'C区-03',
        remarks: '办公必备'
    },
    {
        id: 5,
        productCode: 'sp017',
        productName: '测试商品D',
        specification: 'D型',
        category: '家居用品',
        unit: '个',
        unitPrice: 45.6,
        purchasePrice: 28.6,
        inventory: 300,
        totalValue: 13680,
        supplier: '供应商D',
        storageLocation: 'D区-04',
        remarks: '家居精品'
    }
]

/**
 * 加载数据
 */
function loadData() {
    // 模拟分页数据
    const { pageIndex, pageSize } = tableData.value
    const start = (pageIndex - 1) * pageSize
    const end = start + pageSize

    tableData.value = {
        pageIndex,
        pageSize,
        total: mockProductData.length,
        rows: mockProductData.slice(start, end)
    }
}

/**
 * 分页改变事件处理
 */
function handlePageChange(data: PageDTO<any>) {
    tableData.value.pageIndex = data.pageIndex
    tableData.value.pageSize = data.pageSize
    loadData()
}

// 删除商品
const handleDelete = async (index: number, row: any) => {
    try {
        await ElMessageBox.confirm(
            `确定要删除商品 "${row.productName}" 吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )

        // 从模拟数据中删除
        const productIndex = mockProductData.findIndex(item => item.id === row.id)
        if (productIndex !== -1) {
            mockProductData.splice(productIndex, 1)
            // 重新加载数据
            loadData()
            ElMessage.success('删除成功')
        }
    } catch {
        ElMessage.info('已取消删除')
    }
}

/**
 * 组件挂载时加载数据
 */
onMounted(() => {
    loadData()
})
</script>

<style scoped>
.root-container {
    padding: 20px;
}

.root-container>* {
    margin-bottom: 30px;
}
</style>