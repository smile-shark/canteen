<template>
  <div class="takeout-order-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="订单编号">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
        >
          <el-option label="全部" value="">全部</el-option>
          <el-option label="大悦城店" value="大悦城店">大悦城店</el-option>
          <el-option label="泛海国际店" value="泛海国际店">泛海国际店</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择订单状态">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option label="全部" value="">全部</el-option>
          <el-option label="未处理" value="未处理">未处理</el-option>
          <el-option label="已接单" value="已接单">已接单</el-option>
          <el-option label="已完成" value="已完成">已完成</el-option>
          <el-option label="已取消" value="已取消">已取消</el-option>
          <el-option label="已拒绝" value="已拒绝">已拒绝</el-option>
          <el-option label="已送达" value="已送达">已送达</el-option>
          <el-option label="已自提" value="已自提">已自提</el-option>
          <el-option label="已作废" value="已作废">已作废</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
          v-model="searchForm.startTime"
          type="date"
          placeholder="开始时间"
          value-format="yyyy-MM-dd"
          clearable
        />
        <span style="margin: 0 5px;">—</span>
        <el-date-picker
          v-model="searchForm.endTime"
          type="date"
          placeholder="结束时间"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 导出按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <div></div>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 订单表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="orderNo"
        label="订单编号"
        align="center"
      />
      <el-table-column
        prop="orderTime"
        label="下单时间"
        align="center"
      />
      <el-table-column
        prop="orderAmount"
        label="订单金额"
        align="center"
      />
      <el-table-column
        prop="store"
        label="所属门店"
        align="center"
      />
      <el-table-column
        prop="username"
        label="用户名"
        align="center"
      />
      <el-table-column
        prop="phone"
        label="手机号"
        align="center"
      />
      <el-table-column
        prop="address"
        label="收货地址"
        align="center"
      />
      <el-table-column
        prop="status"
        label="订单状态"
        align="center"
      />
      <el-table-column
        label="操作"
        align="center"
      >
        <template #default="scope">
          <el-button type="text" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.page"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      style="margin-top: 10px; text-align: right;"
    />

    <!-- 查看订单弹窗 -->
    <el-dialog
      title="订单详情"
      :visible.sync="viewDialogVisible"
      width="600px"
    >
      <el-descriptions
        :column="1"
        border
        style="width: 100%"
      >
        <el-descriptions-item label="订单编号">{{ viewOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ viewOrder.orderTime }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">{{ viewOrder.orderAmount }}</el-descriptions-item>
        <el-descriptions-item label="所属门店">{{ viewOrder.store }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ viewOrder.username }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ viewOrder.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址">{{ viewOrder.address }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ viewOrder.status }}</el-descriptions-item>
        <el-descriptions-item label="其他信息">
          <span>这里可扩展订单详情，如商品明细、配送信息等</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TakeoutOrderPage',
  data() {
    return {
      // 筛选表单
      searchForm: {
        orderNo: '',
        store: '',
        status: '',
        startTime: '',
        endTime: ''
      },
      // 表格数据
      tableData: [
        {
          orderNo: '2021091008181',
          orderTime: '2021-09-10 12:00:12',
          orderAmount: '¥ 500.00',
          store: '大悦城店',
          username: 'qianqianlu',
          phone: '18811111111',
          address: '朝阳区石佛营路XX号1单元2号',
          status: '未处理'
        },
        {
          orderNo: '2021091008181',
          orderTime: '2021-09-10 12:00:12',
          orderAmount: '¥ 500.00',
          store: '泛海国际店',
          username: 'qianqianlu',
          phone: '18811111111',
          address: '朝阳区石佛营路XX号1单元2号',
          status: '已接单'
        }
        // 可继续补充更多模拟数据...
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 100 // 模拟共100条数据
      },
      // 查看订单弹窗
      viewDialogVisible: false,
      viewOrder: {}
    }
  },
  methods: {
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm);
      // 模拟筛选逻辑，可根据 searchForm 过滤 tableData
      // 实际项目中需结合接口请求数据
    },
    // 导出按钮
    handleExport() {
      this.$message.success('模拟导出操作，可结合后端实现真实导出');
    },
    // 分页 - 每页条数变化
    handleSizeChange(val) {
      this.pagination.size = val;
      // 模拟页码变化后重新获取数据，实际需结合接口
    },
    // 分页 - 当前页变化
    handleCurrentChange(val) {
      this.pagination.page = val;
      // 模拟页码变化后重新获取数据，实际需结合接口
    },
    // 查看订单
    handleView(row) {
      this.viewOrder = row;
      this.viewDialogVisible = true;
    }
  }
}
</script>

<style scoped>
.takeout-order-page {
  padding: 20px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
</style>