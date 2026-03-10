/**
 * 搜索组件的临时本地模拟数据
 * 用于在没有后端接口时进行开发和测试
 */

import type {
  Warehouse,
  SupplierListItem,
  CustomerListItem,
  AccountListItem,
  UserListItem,
  PersonnelListItem,
  ProductListItem
} from '@/apis/common/type'

/**
 * 仓库模拟数据
 */
export const mockWarehouses: Warehouse[] = [
  {
    id: '1983381181546139963',
    name: '主仓库',
    code: 'WH001',
    address: '北京市朝阳区仓库路1号',
    manager: '张经理',
    phone: '010-12345678',
    type: '主仓库',
    status: 1
  },
  {
    id: '1983381181546139964',
    name: '分仓库A',
    code: 'WH002',
    address: '上海市浦东新区仓库路2号',
    manager: '李经理',
    phone: '021-87654321',
    type: '分仓库',
    status: 1
  },
  {
    id: '1983381181546139965',
    name: '分仓库B',
    code: 'WH003',
    address: '广州市天河区仓库路3号',
    manager: '王经理',
    phone: '020-11223344',
    type: '分仓库',
    status: 1
  },
  {
    id: '1983381181546139966',
    name: '临时仓库',
    code: 'WH004',
    address: '深圳市南山区仓库路4号',
    manager: '赵经理',
    phone: '0755-55667788',
    type: '临时仓库',
    status: 1
  },
  {
    id: '1983381181546139967',
    name: '成品仓库',
    code: 'WH005',
    address: '杭州市西湖区仓库路5号',
    manager: '刘经理',
    phone: '0571-99887766',
    type: '成品仓库',
    status: 1
  },
  {
    id: '1983381181546139968',
    name: '原料仓库',
    code: 'WH006',
    address: '南京市鼓楼区仓库路6号',
    manager: '陈经理',
    phone: '025-22334455',
    type: '原料仓库',
    status: 1
  },
  {
    id: '1983381181546139969',
    name: '半成品仓库',
    code: 'WH007',
    address: '武汉市洪山区仓库路7号',
    manager: '周经理',
    phone: '027-33445566',
    type: '半成品仓库',
    status: 1
  },
  {
    id: '1983381181546139970',
    name: '退货仓库',
    code: 'WH008',
    address: '西安市雁塔区仓库路8号',
    manager: '吴经理',
    phone: '029-44556677',
    type: '退货仓库',
    status: 1
  },
  {
    id: '1983381181546139971',
    name: '中转仓库',
    code: 'WH009',
    address: '重庆市渝中区仓库路9号',
    manager: '郑经理',
    phone: '023-55667788',
    type: '中转仓库',
    status: 1
  },
  {
    id: '1983381181546139972',
    name: '配送仓库',
    code: 'WH010',
    address: '成都市锦江区仓库路10号',
    manager: '冯经理',
    phone: '028-66778899',
    type: '配送仓库',
    status: 1
  }
]

/**
 * 供应商模拟数据
 */
export const mockSuppliers: SupplierListItem[] = [
  {
    id: '1983381181546139983',
    name: '北京供应商A',
    code: 'SUP001',
    category: '常规类别',
    payableBalance: 50000,
    deptName: '采购部',
    ownerName: '张三',
    contact: '张经理',
    phone: '010-11111111',
    address: '北京市朝阳区供应商路1号',
    email: 'supplier1@example.com',
    status: 1
  },
  {
    id: '1983381181546139984',
    name: '上海供应商B',
    code: 'SUP002',
    category: '本地',
    payableBalance: 30000,
    deptName: '采购部',
    ownerName: '李四',
    contact: '李经理',
    phone: '021-22222222',
    address: '上海市浦东新区供应商路2号',
    email: 'supplier2@example.com',
    status: 1
  },
  {
    id: '1983381181546139985',
    name: '电商供应商C',
    code: 'SUP003',
    category: '电商',
    payableBalance: 80000,
    deptName: '电商部',
    ownerName: '王五',
    contact: '王经理',
    phone: '020-33333333',
    address: '广州市天河区供应商路3号',
    email: 'supplier3@example.com',
    status: 1
  },
  {
    id: '1983381181546139986',
    name: '成都供应商D',
    code: 'SUP004',
    category: '成都',
    payableBalance: 20000,
    deptName: '采购部',
    ownerName: '赵六',
    contact: '赵经理',
    phone: '028-44444444',
    address: '成都市锦江区供应商路4号',
    email: 'supplier4@example.com',
    status: 1
  },
  {
    id: '1983381181546139987',
    name: '深圳供应商E',
    code: 'SUP005',
    category: '常规类别',
    payableBalance: 60000,
    deptName: '采购部',
    ownerName: '刘七',
    contact: '刘经理',
    phone: '0755-55555555',
    address: '深圳市南山区供应商路5号',
    email: 'supplier5@example.com',
    status: 1
  },
  {
    id: '1983381181546139988',
    name: '杭州供应商F',
    code: 'SUP006',
    category: '本地',
    payableBalance: 40000,
    deptName: '采购部',
    ownerName: '张三',
    contact: '钱经理',
    phone: '0571-66666666',
    address: '杭州市西湖区供应商路6号',
    email: 'supplier6@example.com',
    status: 1
  },
  {
    id: '1983381181546139989',
    name: '南京供应商G',
    code: 'SUP007',
    category: '常规类别',
    payableBalance: 35000,
    deptName: '采购部',
    ownerName: '李四',
    contact: '孙经理',
    phone: '025-77777777',
    address: '南京市鼓楼区供应商路7号',
    email: 'supplier7@example.com',
    status: 1
  },
  {
    id: '1983381181546139990',
    name: '武汉供应商H',
    code: 'SUP008',
    category: '电商',
    payableBalance: 55000,
    deptName: '电商部',
    ownerName: '王五',
    contact: '周经理',
    phone: '027-88888888',
    address: '武汉市洪山区供应商路8号',
    email: 'supplier8@example.com',
    status: 1
  },
  {
    id: '1983381181546139991',
    name: '西安供应商I',
    code: 'SUP009',
    category: '成都',
    payableBalance: 25000,
    deptName: '采购部',
    ownerName: '赵六',
    contact: '吴经理',
    phone: '029-99999999',
    address: '西安市雁塔区供应商路9号',
    email: 'supplier9@example.com',
    status: 1
  },
  {
    id: '1983381181546139992',
    name: '重庆供应商J',
    code: 'SUP010',
    category: '常规类别',
    payableBalance: 45000,
    deptName: '采购部',
    ownerName: '刘七',
    contact: '郑经理',
    phone: '023-10101010',
    address: '重庆市渝中区供应商路10号',
    email: 'supplier10@example.com',
    status: 1
  }
]

/**
 * 客户模拟数据
 */
export const mockCustomers: CustomerListItem[] = [
  {
    id: 'CUS001',
    name: '零售客户A',
    code: 'CUS001',
    category: '零售',
    level: 'A',
    receivableBalance: 100000,
    contact: '陈经理',
    phone: '010-66666666',
    address: '北京市海淀区客户路1号',
    email: 'customer1@example.com',
    ownerName: '张三',
    status: 1
  },
  {
    id: 'CUS002',
    name: '批发客户B',
    code: 'CUS002',
    category: '批发',
    level: 'B',
    receivableBalance: 200000,
    contact: '周经理',
    phone: '021-77777777',
    address: '上海市黄浦区客户路2号',
    email: 'customer2@example.com',
    ownerName: '李四',
    status: 1
  },
  {
    id: 'CUS003',
    name: '电商客户C',
    code: 'CUS003',
    category: '电商',
    level: 'A',
    receivableBalance: 300000,
    contact: '吴经理',
    phone: '020-88888888',
    address: '广州市越秀区客户路3号',
    email: 'customer3@example.com',
    ownerName: '王五',
    status: 1
  },
  {
    id: 'CUS004',
    name: 'VIP客户D',
    code: 'CUS004',
    category: 'VIP',
    level: 'A',
    receivableBalance: 500000,
    contact: '郑经理',
    phone: '028-99999999',
    address: '成都市武侯区客户路4号',
    email: 'customer4@example.com',
    ownerName: '赵六',
    status: 1
  },
  {
    id: 'CUS005',
    name: '零售客户E',
    code: 'CUS005',
    category: '零售',
    level: 'C',
    receivableBalance: 50000,
    contact: '冯经理',
    phone: '0755-10101010',
    address: '深圳市福田区客户路5号',
    email: 'customer5@example.com',
    ownerName: '刘七',
    status: 1
  },
  {
    id: 'CUS006',
    name: '批发客户F',
    code: 'CUS006',
    category: '批发',
    level: 'A',
    receivableBalance: 150000,
    contact: '钱经理',
    phone: '0571-20202020',
    address: '杭州市西湖区客户路6号',
    email: 'customer6@example.com',
    ownerName: '张三',
    status: 1
  },
  {
    id: 'CUS007',
    name: '电商客户G',
    code: 'CUS007',
    category: '电商',
    level: 'B',
    receivableBalance: 180000,
    contact: '孙经理',
    phone: '025-30303030',
    address: '南京市鼓楼区客户路7号',
    email: 'customer7@example.com',
    ownerName: '李四',
    status: 1
  },
  {
    id: 'CUS008',
    name: 'VIP客户H',
    code: 'CUS008',
    category: 'VIP',
    level: 'A',
    receivableBalance: 600000,
    contact: '周经理',
    phone: '027-40404040',
    address: '武汉市洪山区客户路8号',
    email: 'customer8@example.com',
    ownerName: '王五',
    status: 1
  },
  {
    id: 'CUS009',
    name: '零售客户I',
    code: 'CUS009',
    category: '零售',
    level: 'B',
    receivableBalance: 80000,
    contact: '吴经理',
    phone: '029-50505050',
    address: '西安市雁塔区客户路9号',
    email: 'customer9@example.com',
    ownerName: '赵六',
    status: 1
  },
  {
    id: 'CUS010',
    name: '批发客户J',
    code: 'CUS010',
    category: '批发',
    level: 'C',
    receivableBalance: 120000,
    contact: '郑经理',
    phone: '023-60606060',
    address: '重庆市渝中区客户路10号',
    email: 'customer10@example.com',
    ownerName: '刘七',
    status: 1
  }
]

/**
 * 账户模拟数据
 */
export const mockAccounts: AccountListItem[] = [
  {
    id: 'ACC001',
    name: '中国银行主账户',
    accountNo: '6222021234567890123',
    deptName: '财务部',
    balance: 1000000,
    type: '银行账户',
    bank: '中国银行',
    remark: '主账户',
    status: 1
  },
  {
    id: 'ACC002',
    name: '工商银行备用账户',
    accountNo: '6222021234567890124',
    deptName: '财务部',
    balance: 500000,
    type: '银行账户',
    bank: '工商银行',
    remark: '备用账户',
    status: 1
  },
  {
    id: 'ACC003',
    name: '支付宝账户',
    accountNo: '13800138000',
    deptName: '电商部',
    balance: 200000,
    type: '第三方支付',
    bank: '支付宝',
    remark: '电商专用',
    status: 1
  },
  {
    id: 'ACC004',
    name: '微信支付账户',
    accountNo: 'wxpay001',
    deptName: '电商部',
    balance: 150000,
    type: '第三方支付',
    bank: '微信支付',
    remark: '电商专用',
    status: 1
  },
  {
    id: 'ACC005',
    name: '建设银行现金账户',
    accountNo: '6222021234567890125',
    deptName: '财务部',
    balance: 300000,
    type: '银行账户',
    bank: '建设银行',
    remark: '现金账户',
    status: 1
  },
  {
    id: 'ACC006',
    name: '农业银行对公账户',
    accountNo: '6228481234567890126',
    deptName: '财务部',
    balance: 800000,
    type: '银行账户',
    bank: '农业银行',
    remark: '对公账户',
    status: 1
  },
  {
    id: 'ACC007',
    name: '交通银行结算账户',
    accountNo: '6222601234567890127',
    deptName: '财务部',
    balance: 600000,
    type: '银行账户',
    bank: '交通银行',
    remark: '结算账户',
    status: 1
  },
  {
    id: 'ACC008',
    name: '招商银行理财账户',
    accountNo: '6225881234567890128',
    deptName: '财务部',
    balance: 1200000,
    type: '银行账户',
    bank: '招商银行',
    remark: '理财账户',
    status: 1
  },
  {
    id: 'ACC009',
    name: '京东支付账户',
    accountNo: 'jdpay001',
    deptName: '电商部',
    balance: 180000,
    type: '第三方支付',
    bank: '京东支付',
    remark: '电商专用',
    status: 1
  },
  {
    id: 'ACC010',
    name: '平安银行企业账户',
    accountNo: '6221551234567890129',
    deptName: '财务部',
    balance: 450000,
    type: '银行账户',
    bank: '平安银行',
    remark: '企业账户',
    status: 1
  }
]

/**
 * 用户模拟数据
 */
export const mockUsers: UserListItem[] = [
  {
    id: '1983381181546139993',
    username: 'zhangsan',
    realName: '张三',
    phone: '13800138001',
    email: 'zhangsan@example.com',
    deptId: 'DEPT001',
    deptName: '采购部',
    roleName: '采购经理',
    roleIds: ['ROLE001'],
    status: 1
  },
  {
    id: '1983381181546139994',
    username: 'lisi',
    realName: '李四',
    phone: '13800138002',
    email: 'lisi@example.com',
    deptId: 'DEPT002',
    deptName: '销售部',
    roleName: '销售经理',
    roleIds: ['ROLE002'],
    status: 1
  },
  {
    id: '1983381181546139995',
    username: 'wangwu',
    realName: '王五',
    phone: '13800138003',
    email: 'wangwu@example.com',
    deptId: 'DEPT003',
    deptName: '电商部',
    roleName: '电商经理',
    roleIds: ['ROLE003'],
    status: 1
  },
  {
    id: '1983381181546139996',
    username: 'zhaoliu',
    realName: '赵六',
    phone: '13800138004',
    email: 'zhaoliu@example.com',
    deptId: 'DEPT001',
    deptName: '采购部',
    roleName: '采购员',
    roleIds: ['ROLE004'],
    status: 1
  },
  {
    id: '1983381181546139997',
    username: 'liuqi',
    realName: '刘七',
    phone: '13800138005',
    email: 'liuqi@example.com',
    deptId: 'DEPT002',
    deptName: '销售部',
    roleName: '销售员',
    roleIds: ['ROLE005'],
    status: 1
  },
  {
    id: '1983381181546139998',
    username: 'chenba',
    realName: '陈八',
    phone: '13800138006',
    email: 'chenba@example.com',
    deptId: 'DEPT004',
    deptName: '财务部',
    roleName: '财务经理',
    roleIds: ['ROLE006'],
    status: 1
  },
  {
    id: '1983381181546139999',
    username: 'zhoujiu',
    realName: '周九',
    phone: '13800138007',
    email: 'zhoujiu@example.com',
    deptId: 'DEPT005',
    deptName: '人事部',
    roleName: '人事经理',
    roleIds: ['ROLE007'],
    status: 1
  },
  {
    id: '1983381181546140000',
    username: 'wushi',
    realName: '吴十',
    phone: '13800138008',
    email: 'wushi@example.com',
    deptId: 'DEPT003',
    deptName: '电商部',
    roleName: '电商专员',
    roleIds: ['ROLE008'],
    status: 1
  },
  {
    id: '1983381181546140001',
    username: 'zhengshiyi',
    realName: '郑十一',
    phone: '13800138009',
    email: 'zhengshiyi@example.com',
    deptId: 'DEPT001',
    deptName: '采购部',
    roleName: '采购助理',
    roleIds: ['ROLE009'],
    status: 1
  },
  {
    id: '1983381181546140002',
    username: 'fengshier',
    realName: '冯十二',
    phone: '13800138010',
    email: 'fengshier@example.com',
    deptId: 'DEPT002',
    deptName: '销售部',
    roleName: '销售助理',
    roleIds: ['ROLE010'],
    status: 1
  }
]

/**
 * 人员模拟数据
 */
export const mockPersonnel: PersonnelListItem[] = [
  {
    id: '1',
    name: '陈八',
    employeeNo: 'EMP001',
    deptId: 'DEPT001',
    deptName: '采购部',
    gender: '男',
    phone: '13900139001',
    address: '北京市朝阳区人员路1号',
    idCard: '110101199001011234',
    position: '采购专员',
    status: 1
  },
  {
    id: '2',
    name: '周九',
    employeeNo: 'EMP002',
    deptId: 'DEPT002',
    deptName: '销售部',
    gender: '女',
    phone: '13900139002',
    address: '上海市浦东新区人员路2号',
    idCard: '310101199002022345',
    position: '销售专员',
    status: 1
  },
  {
    id: '3',
    name: '吴十',
    employeeNo: 'EMP003',
    deptId: 'DEPT003',
    deptName: '电商部',
    gender: '男',
    phone: '13900139003',
    address: '广州市天河区人员路3号',
    idCard: '440101199003033456',
    position: '电商专员',
    status: 1
  },
  {
    id: '4',
    name: '郑十一',
    employeeNo: 'EMP004',
    deptId: 'DEPT001',
    deptName: '采购部',
    gender: '女',
    phone: '13900139004',
    address: '成都市锦江区人员路4号',
    idCard: '510101199004044567',
    position: '采购助理',
    status: 1
  },
  {
    id: '5',
    name: '冯十二',
    employeeNo: 'EMP005',
    deptId: 'DEPT002',
    deptName: '销售部',
    gender: '男',
    phone: '13900139005',
    address: '深圳市南山区人员路5号',
    idCard: '440301199005055678',
    position: '销售助理',
    status: 1
  },
  {
    id: '6',
    name: '钱一',
    employeeNo: 'EMP006',
    deptId: 'DEPT004',
    deptName: '财务部',
    gender: '女',
    phone: '13900139006',
    address: '杭州市西湖区人员路6号',
    idCard: '330101199006066789',
    position: '财务专员',
    status: 1
  },
  {
    id: '7',
    name: '孙二',
    employeeNo: 'EMP007',
    deptId: 'DEPT005',
    deptName: '人事部',
    gender: '男',
    phone: '13900139007',
    address: '南京市鼓楼区人员路7号',
    idCard: '320101199007077890',
    position: '人事专员',
    status: 1
  },
  {
    id: '8',
    name: '李三',
    employeeNo: 'EMP008',
    deptId: 'DEPT003',
    deptName: '电商部',
    gender: '女',
    phone: '13900139008',
    address: '武汉市洪山区人员路8号',
    idCard: '420101199008088901',
    position: '电商助理',
    status: 1
  },
  {
    id: '9',
    name: '王四',
    employeeNo: 'EMP009',
    deptId: 'DEPT001',
    deptName: '采购部',
    gender: '男',
    phone: '13900139009',
    address: '西安市雁塔区人员路9号',
    idCard: '610101199009099012',
    position: '采购专员',
    status: 1
  },
  {
    id: '10',
    name: '赵五',
    employeeNo: 'EMP010',
    deptId: 'DEPT002',
    deptName: '销售部',
    gender: '女',
    phone: '13900139010',
    address: '重庆市渝中区人员路10号',
    idCard: '500101199010100123',
    position: '销售专员',
    status: 1
  }
]

/**
 * 根据条件过滤仓库数据
 * 支持 warehouseId, warehouseName, warehouseCode 等字段
 */
export function filterWarehouses(params: Record<string, any>): Warehouse[] {
  let results = [...mockWarehouses]
  
  // 仓库ID搜索（支持 id 和 warehouseId）
  if (params.id || params.warehouseId) {
    const id = params.id || params.warehouseId
    results = results.filter(item => item.id.includes(id))
  }
  
  // 仓库名称搜索（支持 name 和 warehouseName）
  if (params.name || params.warehouseName) {
    const name = params.name || params.warehouseName
    results = results.filter(item => item.name.includes(name))
  }
  
  // 仓库编号搜索（支持 code 和 warehouseCode）
  if (params.code || params.warehouseCode) {
    const code = params.code || params.warehouseCode
    results = results.filter(item => item.code?.includes(code))
  }
  
  return results
}

/**
 * 根据条件过滤供应商数据
 * 支持 supplierName, supplierCode, supplierCategory, contactPerson, contactPhone, user 等字段
 */
export function filterSuppliers(params: Record<string, any>): SupplierListItem[] {
  let results = [...mockSuppliers]
  
  // 供应商名称搜索（支持 name 和 supplierName）
  if (params.name || params.supplierName) {
    const name = params.name || params.supplierName
    results = results.filter(item => item.name.includes(name))
  }
  
  // 供应商编号搜索（支持 number, code, supplierCode）
  if (params.number || params.code || params.supplierCode) {
    const code = params.number || params.code || params.supplierCode
    results = results.filter(item => item.code?.includes(code))
  }
  
  // 供应商类别搜索（支持 category 和 supplierCategory）
  if (params.category || params.supplierCategory) {
    const category = params.category || params.supplierCategory
    results = results.filter(item => item.category === category)
  }
  
  // 联系人搜索（支持 contacts, contact, contactPerson）
  if (params.contacts || params.contact || params.contactPerson) {
    const contact = params.contacts || params.contact || params.contactPerson
    results = results.filter(item => item.contact?.includes(contact))
  }
  
  // 联系电话搜索（支持 tel, phone, contactPhone）
  if (params.tel || params.phone || params.contactPhone) {
    const phone = params.tel || params.phone || params.contactPhone
    results = results.filter(item => item.phone?.includes(phone))
  }
  
  // 所属用户搜索（支持 user 和 ownerName）
  if (params.user || params.ownerName) {
    const owner = params.user || params.ownerName
    results = results.filter(item => item.ownerName?.includes(owner))
  }
  
  return results
}

/**
 * 根据条件过滤客户数据
 * 支持 customerName, customerCode, customerCategory, customerLevel, contactPerson, contactPhone, user 等字段
 */
export function filterCustomers(params: Record<string, any>): CustomerListItem[] {
  let results = [...mockCustomers]
  
  // 客户名称搜索（支持 name 和 customerName）
  if (params.name || params.customerName) {
    const name = params.name || params.customerName
    results = results.filter(item => item.name.includes(name))
  }
  
  // 客户编号搜索（支持 number, code, customerCode）
  if (params.number || params.code || params.customerCode) {
    const code = params.number || params.code || params.customerCode
    results = results.filter(item => item.code?.includes(code))
  }
  
  // 客户类别搜索（支持 category 和 customerCategory）
  if (params.category || params.customerCategory) {
    const category = params.category || params.customerCategory
    results = results.filter(item => item.category === category)
  }
  
  // 客户等级搜索（支持 level 和 customerLevel）
  if (params.level || params.customerLevel) {
    const level = params.level || params.customerLevel
    results = results.filter(item => item.level === level)
  }
  
  // 联系人搜索（支持 contacts, contact, contactPerson）
  if (params.contacts || params.contact || params.contactPerson) {
    const contact = params.contacts || params.contact || params.contactPerson
    results = results.filter(item => item.contact?.includes(contact))
  }
  
  // 联系电话搜索（支持 tel, phone, contactPhone）
  if (params.tel || params.phone || params.contactPhone) {
    const phone = params.tel || params.phone || params.contactPhone
    results = results.filter(item => item.phone?.includes(phone))
  }
  
  // 所属用户搜索（支持 user 和 ownerName）
  if (params.user || params.ownerName) {
    const owner = params.user || params.ownerName
    results = results.filter(item => item.ownerName?.includes(owner))
  }
  
  return results
}

/**
 * 根据条件过滤账户数据
 * 支持 accountName, accountCode 等字段
 */
export function filterAccounts(params: Record<string, any>): AccountListItem[] {
  let results = [...mockAccounts]
  
  // 账户名称搜索（支持 name 和 accountName）
  if (params.name || params.accountName) {
    const name = params.name || params.accountName
    results = results.filter(item => item.name.includes(name))
  }
  
  // 账户编号搜索（支持 number, accountNo, accountCode）
  if (params.number || params.accountNo || params.accountCode) {
    const accountNo = params.number || params.accountNo || params.accountCode
    results = results.filter(item => item.accountNo?.includes(accountNo))
  }
  
  return results
}

/**
 * 根据条件过滤用户数据
 */
export function filterUsers(params: Record<string, any>): UserListItem[] {
  let results = [...mockUsers]
  
  if (params.name || params.realName) {
    const name = params.name || params.realName
    results = results.filter(item => item.realName?.includes(name))
  }
  if (params.user || params.username) {
    const username = params.user || params.username
    results = results.filter(item => item.username.includes(username))
  }
  if (params.tel || params.phone) {
    const phone = params.tel || params.phone
    results = results.filter(item => item.phone?.includes(phone))
  }
  
  return results
}

/**
 * 根据条件过滤人员数据
 * 支持 peopleName, peopleCode, gender, contactPhone, contactAddress, idCard 等字段
 */
export function filterPersonnel(params: Record<string, any>): PersonnelListItem[] {
  let results = [...mockPersonnel]
  
  // 人员名称搜索（支持 name 和 peopleName）
  if (params.name || params.peopleName) {
    const name = params.name || params.peopleName
    results = results.filter(item => item.name.includes(name))
  }
  
  // 人员编号搜索（支持 number, employeeNo, peopleCode）
  if (params.number || params.employeeNo || params.peopleCode) {
    const employeeNo = params.number || params.employeeNo || params.peopleCode
    results = results.filter(item => item.employeeNo?.includes(employeeNo))
  }
  
  // 性别搜索
  if (params.gender) {
    results = results.filter(item => item.gender === params.gender)
  }
  
  // 联系电话搜索（支持 tel, phone, contactPhone）
  if (params.tel || params.phone || params.contactPhone) {
    const phone = params.tel || params.phone || params.contactPhone
    results = results.filter(item => item.phone?.includes(phone))
  }
  
  // 联系地址搜索（支持 address 和 contactAddress）
  if (params.address || params.contactAddress) {
    const addr = params.address || params.contactAddress
    results = results.filter(item => item.address?.includes(addr))
  }
  
  // 身份证号搜索
  if (params.idCard) {
    results = results.filter(item => item.idCard?.includes(params.idCard))
  }
  
  return results
}

/**
 * 商品模拟数据
 */
export const mockProducts: ProductListItem[] = [
  {
    id: 'PRD001',
    name: 'iPhone 15 Pro',
    code: 'PRD001',
    barcode: '6901234567890',
    spec: '256GB 深空黑色',
    unit: '台',
    price: 8999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD001',
    brandName: 'Apple',
    mainColor: '深空黑色',
    hasSpecs: false
  },
  {
    id: 'PRD002',
    name: 'MacBook Pro 14英寸',
    code: 'PRD002',
    barcode: '6901234567891',
    spec: 'M3芯片 16GB+512GB',
    unit: '台',
    price: 14999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD001',
    brandName: 'Apple',
    mainColor: '深空灰色',
    hasSpecs: false
  },
  {
    id: 'PRD003',
    name: '华为Mate 60 Pro',
    code: 'PRD003',
    barcode: '6901234567892',
    spec: '512GB 雅川青',
    unit: '台',
    price: 6999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD002',
    brandName: '华为',
    mainColor: '雅川青',
    hasSpecs: false
  },
  {
    id: 'PRD004',
    name: '小米14 Ultra',
    code: 'PRD004',
    barcode: '6901234567893',
    spec: '256GB 黑色',
    unit: '台',
    price: 5999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD003',
    brandName: '小米',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD005',
    name: '联想ThinkPad X1 Carbon',
    code: 'PRD005',
    barcode: '6901234567894',
    spec: 'i7 16GB+512GB',
    unit: '台',
    price: 12999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD004',
    brandName: '联想',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD006',
    name: '戴尔XPS 13',
    code: 'PRD006',
    barcode: '6901234567895',
    spec: 'i5 8GB+256GB',
    unit: '台',
    price: 7999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD005',
    brandName: '戴尔',
    mainColor: '银色',
    hasSpecs: false
  },
  {
    id: 'PRD007',
    name: 'iPad Air',
    code: 'PRD007',
    barcode: '6901234567896',
    spec: '256GB WiFi版',
    unit: '台',
    price: 4799,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD001',
    brandName: 'Apple',
    mainColor: '蓝色',
    hasSpecs: false
  },
  {
    id: 'PRD008',
    name: 'AirPods Pro',
    code: 'PRD008',
    barcode: '6901234567897',
    spec: '第二代 主动降噪',
    unit: '副',
    price: 1899,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD001',
    brandName: 'Apple',
    mainColor: '白色',
    hasSpecs: false
  },
  {
    id: 'PRD009',
    name: '三星Galaxy S24 Ultra',
    code: 'PRD009',
    barcode: '6901234567898',
    spec: '512GB 钛灰色',
    unit: '台',
    price: 8999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD006',
    brandName: '三星',
    mainColor: '钛灰色',
    hasSpecs: false
  },
  {
    id: 'PRD010',
    name: 'OPPO Find X7 Ultra',
    code: 'PRD010',
    barcode: '6901234567899',
    spec: '256GB 墨岩黑',
    unit: '台',
    price: 5999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD007',
    brandName: 'OPPO',
    mainColor: '墨岩黑',
    hasSpecs: false
  },
  {
    id: 'PRD011',
    name: 'vivo X100 Pro',
    code: 'PRD011',
    barcode: '6901234567900',
    spec: '512GB 星迹蓝',
    unit: '台',
    price: 5999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD008',
    brandName: 'vivo',
    mainColor: '星迹蓝',
    hasSpecs: false
  },
  {
    id: 'PRD012',
    name: '荣耀Magic6 Pro',
    code: 'PRD012',
    barcode: '6901234567901',
    spec: '256GB 海湖青',
    unit: '台',
    price: 5699,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD009',
    brandName: '荣耀',
    mainColor: '海湖青',
    hasSpecs: false
  },
  {
    id: 'PRD013',
    name: 'Nike Air Max 270',
    code: 'PRD013',
    barcode: '6901234567902',
    spec: '42码 黑色',
    unit: '双',
    price: 899,
    categoryId: 'CAT002',
    categoryName: '服装鞋帽',
    brandId: 'BRD010',
    brandName: 'Nike',
    mainColor: '黑色',
    hasSpecs: true
  },
  {
    id: 'PRD014',
    name: 'Adidas Ultraboost 22',
    code: 'PRD014',
    barcode: '6901234567903',
    spec: '43码 白色',
    unit: '双',
    price: 1299,
    categoryId: 'CAT002',
    categoryName: '服装鞋帽',
    brandId: 'BRD011',
    brandName: 'Adidas',
    mainColor: '白色',
    hasSpecs: true
  },
  {
    id: 'PRD015',
    name: '优衣库纯棉T恤',
    code: 'PRD015',
    barcode: '6901234567904',
    spec: 'L码 白色',
    unit: '件',
    price: 99,
    categoryId: 'CAT002',
    categoryName: '服装鞋帽',
    brandId: 'BRD012',
    brandName: '优衣库',
    mainColor: '白色',
    hasSpecs: true
  },
  {
    id: 'PRD016',
    name: 'ZARA休闲衬衫',
    code: 'PRD016',
    barcode: '6901234567905',
    spec: 'M码 蓝色',
    unit: '件',
    price: 299,
    categoryId: 'CAT002',
    categoryName: '服装鞋帽',
    brandId: 'BRD013',
    brandName: 'ZARA',
    mainColor: '蓝色',
    hasSpecs: true
  },
  {
    id: 'PRD017',
    name: 'iPhone 15 保护壳',
    code: 'PRD017',
    barcode: '6901234567906',
    spec: '透明款',
    unit: '个',
    price: 49,
    categoryId: 'CAT003',
    categoryName: '手机配件',
    brandId: 'BRD014',
    brandName: '第三方',
    mainColor: '透明',
    hasSpecs: false
  },
  {
    id: 'PRD018',
    name: '小米充电宝10000mAh',
    code: 'PRD018',
    barcode: '6901234567907',
    spec: '快充版 白色',
    unit: '个',
    price: 129,
    categoryId: 'CAT003',
    categoryName: '手机配件',
    brandId: 'BRD003',
    brandName: '小米',
    mainColor: '白色',
    hasSpecs: false
  },
  {
    id: 'PRD019',
    name: '华为无线充电器',
    code: 'PRD019',
    barcode: '6901234567908',
    spec: '15W快充',
    unit: '个',
    price: 199,
    categoryId: 'CAT003',
    categoryName: '手机配件',
    brandId: 'BRD002',
    brandName: '华为',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD020',
    name: 'Apple Watch Series 9',
    code: 'PRD020',
    barcode: '6901234567909',
    spec: '45mm GPS版 午夜色',
    unit: '只',
    price: 2999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD001',
    brandName: 'Apple',
    mainColor: '午夜色',
    hasSpecs: true
  },
  {
    id: 'PRD021',
    name: '华为Watch GT 4',
    code: 'PRD021',
    barcode: '6901234567910',
    spec: '46mm 曜石黑',
    unit: '只',
    price: 1488,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD002',
    brandName: '华为',
    mainColor: '曜石黑',
    hasSpecs: true
  },
  {
    id: 'PRD022',
    name: '索尼WH-1000XM5耳机',
    code: 'PRD022',
    barcode: '6901234567911',
    spec: '降噪版 黑色',
    unit: '副',
    price: 2499,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD015',
    brandName: '索尼',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD023',
    name: 'Bose QuietComfort 45',
    code: 'PRD023',
    barcode: '6901234567912',
    spec: '降噪耳机 白色',
    unit: '副',
    price: 2299,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD016',
    brandName: 'Bose',
    mainColor: '白色',
    hasSpecs: false
  },
  {
    id: 'PRD024',
    name: '佳能EOS R6 Mark II',
    code: 'PRD024',
    barcode: '6901234567913',
    spec: '全画幅微单 单机身',
    unit: '台',
    price: 15999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD017',
    brandName: '佳能',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD025',
    name: '索尼A7M4相机',
    code: 'PRD025',
    barcode: '6901234567914',
    spec: '全画幅微单 单机身',
    unit: '台',
    price: 16999,
    categoryId: 'CAT001',
    categoryName: '电子产品',
    brandId: 'BRD015',
    brandName: '索尼',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD026',
    name: '戴森V15吸尘器',
    code: 'PRD026',
    barcode: '6901234567915',
    spec: '无绳 红色',
    unit: '台',
    price: 4990,
    categoryId: 'CAT004',
    categoryName: '家用电器',
    brandId: 'BRD018',
    brandName: '戴森',
    mainColor: '红色',
    hasSpecs: false
  },
  {
    id: 'PRD027',
    name: '美的空调1.5匹',
    code: 'PRD027',
    barcode: '6901234567916',
    spec: '变频 冷暖',
    unit: '台',
    price: 2599,
    categoryId: 'CAT004',
    categoryName: '家用电器',
    brandId: 'BRD019',
    brandName: '美的',
    mainColor: '白色',
    hasSpecs: false
  },
  {
    id: 'PRD028',
    name: '海尔冰箱双开门',
    code: 'PRD028',
    barcode: '6901234567917',
    spec: '500L 银色',
    unit: '台',
    price: 3999,
    categoryId: 'CAT004',
    categoryName: '家用电器',
    brandId: 'BRD020',
    brandName: '海尔',
    mainColor: '银色',
    hasSpecs: false
  },
  {
    id: 'PRD029',
    name: '小米电视65英寸',
    code: 'PRD029',
    barcode: '6901234567918',
    spec: '4K 智能',
    unit: '台',
    price: 2999,
    categoryId: 'CAT004',
    categoryName: '家用电器',
    brandId: 'BRD003',
    brandName: '小米',
    mainColor: '黑色',
    hasSpecs: false
  },
  {
    id: 'PRD030',
    name: 'TCL 75英寸电视',
    code: 'PRD030',
    barcode: '6901234567919',
    spec: '4K QLED',
    unit: '台',
    price: 5999,
    categoryId: 'CAT004',
    categoryName: '家用电器',
    brandId: 'BRD021',
    brandName: 'TCL',
    mainColor: '黑色',
    hasSpecs: false
  }
]

/**
 * 根据条件过滤商品数据
 * 支持按商品名称、编码、条码、类别、品牌等条件搜索
 */
export function filterProducts(params: Record<string, any>): ProductListItem[] {
  let results = [...mockProducts]
  
  // 商品名称搜索（goods字段对应商品名称）
  if (params.goods || params.name) {
    const name = params.goods || params.name
    results = results.filter(item => item.name.includes(name))
  }
  
  // 商品编码搜索
  if (params.code || params.number) {
    const code = params.code || params.number
    results = results.filter(item => item.code?.includes(code))
  }
  
  // 商品条码搜索
  if (params.barcode) {
    results = results.filter(item => item.barcode?.includes(params.barcode))
  }
  
  // 商品类别搜索
  if (params.categoryId || params.categoryName) {
    const category = params.categoryId || params.categoryName
    results = results.filter(item => 
      item.categoryId === category || item.categoryName?.includes(category)
    )
  }
  
  // 商品品牌搜索
  if (params.brandId || params.brandName) {
    const brand = params.brandId || params.brandName
    results = results.filter(item => 
      item.brandId === brand || item.brandName?.includes(brand)
    )
  }
  
  // 规格型号搜索
  if (params.spec) {
    results = results.filter(item => item.spec?.includes(params.spec))
  }
  
  return results
}

/**
 * 采购单列表项数据结构
 */
export interface PurchaseOrderListItem {
  /** 唯一标识 */
  id: string
  /** 商品名称 */
  goods: string
  /** 单据编号 */
  number: string
  /** 供应商名称 */
  supplier: string
  /** 关联人员名称 */
  people: string
  /** 单据日期 */
  time: string
  /** 到货日期 */
  arrival: string
  /** 制单人名称 */
  user: string
  /** 审核状态：1-未审核，2-已审核 */
  examine: number
  /** 入库状态：1-未入库，2-部分入库，3-已入库，4-关闭 */
  state: number
  /** 备注信息 */
  data?: string
}

/**
 * 采购单模拟数据
 */
export const mockPurchaseOrders: PurchaseOrderListItem[] = [
  {
    id: 'PO001',
    goods: 'iPhone 15 Pro',
    number: 'CG20250101001',
    supplier: '北京供应商A',
    people: '张三',
    time: '2025-01-10',
    arrival: '2025-01-15',
    user: '李四',
    examine: 2,
    state: 3,
    data: '首批到货'
  },
  {
    id: 'PO002',
    goods: 'MacBook Pro 14英寸',
    number: 'CG20250102001',
    supplier: '上海供应商B',
    people: '王五',
    time: '2025-01-12',
    arrival: '2025-01-18',
    user: '赵六',
    examine: 2,
    state: 2,
    data: '部分到货'
  },
  {
    id: 'PO003',
    goods: '华为Mate 60 Pro',
    number: 'CG20250103001',
    supplier: '北京供应商A',
    people: '张三',
    time: '2025-01-15',
    arrival: '2025-01-20',
    user: '李四',
    examine: 1,
    state: 1,
    data: '待审核'
  },
  {
    id: 'PO004',
    goods: '小米14 Ultra',
    number: 'CG20250104001',
    supplier: '广州供应商C',
    people: '孙七',
    time: '2025-01-18',
    arrival: '2025-01-25',
    user: '周八',
    examine: 2,
    state: 3,
    data: '已全部入库'
  },
  {
    id: 'PO005',
    goods: '联想ThinkPad X1 Carbon',
    number: 'CG20250105001',
    supplier: '深圳供应商D',
    people: '吴九',
    time: '2025-01-20',
    arrival: '2025-01-28',
    user: '郑十',
    examine: 1,
    state: 1,
    data: ''
  },
  {
    id: 'PO006',
    goods: '戴尔XPS 13',
    number: 'CG20250106001',
    supplier: '上海供应商B',
    people: '王五',
    time: '2025-01-22',
    arrival: '2025-01-30',
    user: '赵六',
    examine: 2,
    state: 2,
    data: '部分到货，等待剩余'
  },
  {
    id: 'PO007',
    goods: 'iPad Air',
    number: 'CG20250107001',
    supplier: '北京供应商A',
    people: '张三',
    time: '2025-01-25',
    arrival: '2025-02-02',
    user: '李四',
    examine: 2,
    state: 4,
    data: '已关闭'
  },
  {
    id: 'PO008',
    goods: 'AirPods Pro',
    number: 'CG20250108001',
    supplier: '杭州供应商E',
    people: '钱一',
    time: '2025-01-28',
    arrival: '2025-02-05',
    user: '孙二',
    examine: 1,
    state: 1,
    data: '紧急订单'
  },
  {
    id: 'PO009',
    goods: '三星Galaxy S24 Ultra',
    number: 'CG20250109001',
    supplier: '广州供应商C',
    people: '孙七',
    time: '2025-02-01',
    arrival: '2025-02-08',
    user: '周八',
    examine: 2,
    state: 3,
    data: ''
  },
  {
    id: 'PO010',
    goods: 'OPPO Find X7 Ultra',
    number: 'CG20250110001',
    supplier: '深圳供应商D',
    people: '吴九',
    time: '2025-02-05',
    arrival: '2025-02-12',
    user: '郑十',
    examine: 2,
    state: 2,
    data: '分批到货'
  },
  {
    id: 'PO011',
    goods: 'vivo X100 Pro',
    number: 'CG20250111001',
    supplier: '北京供应商A',
    people: '张三',
    time: '2025-02-08',
    arrival: '2025-02-15',
    user: '李四',
    examine: 1,
    state: 1,
    data: ''
  },
  {
    id: 'PO012',
    goods: '荣耀Magic6 Pro',
    number: 'CG20250112001',
    supplier: '上海供应商B',
    people: '王五',
    time: '2025-02-10',
    arrival: '2025-02-18',
    user: '赵六',
    examine: 2,
    state: 3,
    data: '已完成'
  },
  {
    id: 'PO013',
    goods: 'Nike Air Max 270',
    number: 'CG20250113001',
    supplier: '广州供应商C',
    people: '孙七',
    time: '2025-02-12',
    arrival: '2025-02-20',
    user: '周八',
    examine: 2,
    state: 2,
    data: '部分到货'
  },
  {
    id: 'PO014',
    goods: 'Adidas Ultraboost 22',
    number: 'CG20250114001',
    supplier: '深圳供应商D',
    people: '吴九',
    time: '2025-02-15',
    arrival: '2025-02-22',
    user: '郑十',
    examine: 1,
    state: 1,
    data: ''
  },
  {
    id: 'PO015',
    goods: '优衣库纯棉T恤',
    number: 'CG20250115001',
    supplier: '杭州供应商E',
    people: '钱一',
    time: '2025-02-18',
    arrival: '2025-02-25',
    user: '孙二',
    examine: 2,
    state: 3,
    data: '已全部入库'
  }
]

/**
 * 根据条件过滤采购单数据
 * 支持按商品名称、单据编号、供应商、关联人员、日期范围、审核状态、入库状态等条件搜索
 */
export function filterPurchaseOrders(params: Record<string, any>): PurchaseOrderListItem[] {
  let results = [...mockPurchaseOrders]
  
  // 商品名称搜索
  if (params.goods) {
    results = results.filter(item => item.goods.includes(params.goods))
  }
  
  // 单据编号搜索
  if (params.number) {
    results = results.filter(item => item.number.includes(params.number))
  }
  
  // 供应商搜索
  if (params.supplier) {
    results = results.filter(item => item.supplier.includes(params.supplier))
  }
  
  // 关联人员搜索
  if (params.people) {
    results = results.filter(item => item.people.includes(params.people))
  }
  
  // 制单人搜索
  if (params.user) {
    results = results.filter(item => item.user.includes(params.user))
  }
  
  // 单据日期范围搜索
  if (params.startTime) {
    results = results.filter(item => item.time >= params.startTime)
  }
  if (params.endTime) {
    results = results.filter(item => item.time <= params.endTime)
  }
  
  // 到货日期范围搜索
  if (params.startArrival) {
    results = results.filter(item => item.arrival >= params.startArrival)
  }
  if (params.endArrival) {
    results = results.filter(item => item.arrival <= params.endArrival)
  }
  
  // 审核状态搜索
  if (params.examine !== undefined && params.examine !== null && params.examine !== '') {
    results = results.filter(item => item.examine === params.examine)
  }
  
  // 入库状态搜索
  if (params.state !== undefined && params.state !== null && params.state !== '') {
    results = results.filter(item => item.state === params.state)
  }
  
  // 备注信息搜索
  if (params.data) {
    results = results.filter(item => item.data && item.data.includes(params.data))
  }
  
  return results
}

