<template>
  <div class="coupon-management">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="优惠券类别">
        <el-select v-model="searchForm.type" placeholder="请选择" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="满减券" value="满减券"></el-option>
          <el-option label="折扣券" value="折扣券"></el-option>
          <el-option label="全场赠券" value="全场赠券"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select v-model="searchForm.store" placeholder="请选择门店" clearable>
          <el-option label="泛海国际店" value="泛海国际店"></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
          <el-option label="其他门店" value="其他门店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="未过期" value="未过期"></el-option>
          <el-option label="已过期" value="已过期"></el-option>
          <el-option label="已停用" value="已停用"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 操作 -->
    <div class="table-header">
      <span>数据列表</span>
      <el-button type="warning" @click="openAddCouponDialog">新增优惠券</el-button>
    </div>

    <!-- 优惠券表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="name" label="优惠券名称" align="center"></el-table-column>
      <el-table-column prop="denomination" label="面值" align="center"></el-table-column>
      <el-table-column prop="type" label="优惠券类型" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template #default="scope">
          <el-tag 
            :type="scope.row.status === '未过期'? 'success' : 
                   scope.row.status === '已过期'? 'info' : 'danger'"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="total" label="发放量" align="center"></el-table-column>
      <el-table-column prop="used" label="使用量" align="center"></el-table-column>
      <el-table-column prop="validPeriod" label="有效期" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button type="text" @click="handleView(scope.row)">查看</el-button>
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.page"
      :page-sizes="[10, 20, 30]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right;"
    ></el-pagination>

    <!-- 新增/编辑优惠券弹窗 -->
    <el-dialog 
      :title="isEdit? '编辑优惠券' : '新增优惠券'" 
      :visible.sync="couponDialogVisible" 
      width="600px"
    >
      <el-form :model="couponForm" label-width="120px" ref="couponForm">
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="couponForm.name" placeholder="请输入优惠券名称"></el-input>
        </el-form-item>
        <el-form-item label="优惠券类型" prop="type">
          <el-select v-model="couponForm.type" placeholder="请选择优惠券类型">
            <el-option label="满减券" value="满减券"></el-option>
            <el-option label="折扣券" value="折扣券"></el-option>
            <el-option label="全场赠券" value="全场赠券"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="面值" prop="denomination">
          <el-input v-model="couponForm.denomination" placeholder="如：¥20 或 8折"></el-input>
        </el-form-item>
        <el-form-item label="使用门槛" prop="threshold">
          <el-input v-model="couponForm.threshold" placeholder="如：满100使用"></el-input>
        </el-form-item>
        <el-form-item label="有效期" prop="validPeriod">
          <el-date-picker
            v-model="couponForm.validPeriod"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="发放总量" prop="total">
          <el-input v-model.number="couponForm.total" type="number" min="1"></el-input>
        </el-form-item>
        <el-form-item label="适用门店" prop="store">
          <el-select v-model="couponForm.store" placeholder="请选择门店">
            <el-option label="泛海国际店" value="泛海国际店"></el-option>
            <el-option label="大悦城店" value="大悦城店"></el-option>
            <el-option label="其他门店" value="其他门店"></el-option>
            <el-option label="全部门店" value="全部门店"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="couponDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCoupon">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看优惠券详情弹窗 -->
    <el-dialog 
      title="优惠券详情" 
      :visible.sync="detailDialogVisible" 
      width="80%"
      fullscreen="false"
    >
      <coupon-detail 
        :coupon-id="currentCouponId" 
        @close="detailDialogVisible = false"
      ></coupon-detail>
    </el-dialog>
  </div>
</template>

<script>
import CouponDetail from './CouponDetail.vue'

export default {
  name: 'CouponManagement',
  components: {
    CouponDetail
  },
  data() {
    return {
      // 筛选表单
      searchForm: {
        type: '',
        store: '',
        status: ''
      },
      // 表格数据
      tableData: [
        {
          id: 1,
          name: '满100减20优惠券',
          denomination: '¥20',
          type: '满减券',
          status: '未过期',
          total: 10000,
          used: 3200,
          validPeriod: '2023-06-01 至 2023-12-31'
        },
        {
          id: 2,
          name: '8折折扣券',
          denomination: '8折',
          type: '折扣券',
          status: '未过期',
          total: 5000,
          used: 1800,
          validPeriod: '2023-07-01 至 2023-12-31'
        },
        {
          id: 3,
          name: '满200减50优惠券',
          denomination: '¥50',
          type: '满减券',
          status: '已过期',
          total: 8000,
          used: 7800,
          validPeriod: '2023-01-01 至 2023-05-31'
        },
        {
          id: 4,
          name: '新用户专享券',
          denomination: '¥15',
          type: '全场赠券',
          status: '已停用',
          total: 2000,
          used: 1500,
          validPeriod: '2023-06-01 至 2023-12-31'
        }
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 120
      },
      // 弹窗控制
      couponDialogVisible: false,
      detailDialogVisible: false,
      isEdit: false,
      currentCouponId: null,
      // 优惠券表单数据
      couponForm: {
        name: '',
        type: '',
        denomination: '',
        threshold: '',
        validPeriod: [],
        total: 0,
        store: ''
      },
      // 选中的优惠券
      selectedCoupons: []
    }
  },
  methods: {
    // 查询
    handleQuery() {
      console.log('查询条件:', this.searchForm)
      // 实际项目中调用接口查询数据
    },
    // 重置查询条件
    resetSearch() {
      this.searchForm = {
        type: '',
        store: '',
        status: ''
      }
    },
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.size = val
      // 实际项目中调用接口获取数据
    },
    // 页码变化
    handleCurrentChange(val) {
      this.pagination.page = val
      // 实际项目中调用接口获取数据
    },
    // 选择变化
    handleSelectionChange(rows) {
      this.selectedCoupons = rows
    },
    // 打开新增弹窗
    openAddCouponDialog() {
      this.isEdit = false
      this.couponForm = {
        name: '',
        type: '',
        denomination: '',
        threshold: '',
        validPeriod: [],
        total: 0,
        store: ''
      }
      this.couponDialogVisible = true
    },
    // 编辑优惠券
    handleEdit(row) {
      this.isEdit = true
      this.currentCouponId = row.id
      // 格式化有效期为日期范围数组
      const [start, end] = row.validPeriod.split(' 至 ')
      
      this.couponForm = {
        name: row.name,
        type: row.type,
        denomination: row.denomination,
        threshold: row.threshold || '',
        validPeriod: [start, end],
        total: row.total,
        store: row.store || '全部门店'
      }
      this.couponDialogVisible = true
    },
    // 保存优惠券
    saveCoupon() {
      // 简单验证
      if (!this.couponForm.name) {
        this.$message.warning('请输入优惠券名称')
        return
      }
      if (!this.couponForm.type) {
        this.$message.warning('请选择优惠券类型')
        return
      }
      if (!this.couponForm.denomination) {
        this.$message.warning('请输入面值')
        return
      }
      if (!this.couponForm.validPeriod || this.couponForm.validPeriod.length!== 2) {
        this.$message.warning('请选择有效期')
        return
      }
      if (!this.couponForm.total || this.couponForm.total <= 0) {
        this.$message.warning('请输入有效的发放总量')
        return
      }
      
      // 格式化有效期显示文本
      const validPeriodText = `${this.couponForm.validPeriod[0]} 至 ${this.couponForm.validPeriod[1]}`
      
      if (this.isEdit) {
        // 编辑操作
        const index = this.tableData.findIndex(item => item.id === this.currentCouponId)
        if (index!== -1) {
          this.tableData[index] = {
           ...this.tableData[index],
           ...this.couponForm,
            validPeriod: validPeriodText,
            status: '未过期' // 默认为未过期状态
          }
        }
        this.$message.success('优惠券编辑成功')
      } else {
        // 新增操作
        const newCoupon = {
          id: this.tableData.length + 1,
         ...this.couponForm,
          validPeriod: validPeriodText,
          status: '未过期',
          used: 0
        }
        this.tableData.unshift(newCoupon)
        this.pagination.total++
        this.$message.success('优惠券新增成功')
      }
      
      this.couponDialogVisible = false
    },
    // 查看详情
    handleView(row) {
      this.currentCouponId = row.id
      this.detailDialogVisible = true
    },
    // 删除优惠券
    handleDelete(row) {
      this.$confirm(`确定要删除优惠券【${row.name}】吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.tableData.findIndex(item => item.id === row.id)
        if (index!== -1) {
          this.tableData.splice(index, 1)
          this.pagination.total--
        }
        this.$message.success('删除成功')
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  }
}
</script>

<style scoped>
.coupon-management {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh-40px);
}

.search-form {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}
</style>
