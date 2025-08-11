<template>
  <div class="refund-record-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="订单编号">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="请输入订单编号"
          clearable
        />
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
        >
          <el-option label="全部" value=""></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
          <el-option label="其他门店" value="其他门店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="退单时间">
        <el-date-picker
          v-model="searchForm.timeRange"
          type="daterange"
          range-separator="—"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 导出操作 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <span>数据列表</span>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 退单记录表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="orderNo" label="订单编号" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="amount" label="金额" align="center">
        <template #default="scope">
          ¥{{ scope.row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="store" label="门店" align="center"></el-table-column>
      <el-table-column prop="time" label="时间" align="center"></el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.page"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right;"
    />
  </div>
</template>

<script>
export default {
  name: 'RefundRecord',
  data() {
    return {
      // 筛选表单
      searchForm: {
        orderNo: '',
        store: '',
        timeRange: []
      },
      // 表格数据（模拟）
      tableData: [
        {
          orderNo: '20210909120000',
          username: 'qianqianlu',
          amount: 450,
          store: '大悦城店',
          time: '2021-09-09 12:00:00'
        }
        // 可扩展更多模拟数据
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 100 // 模拟共100条数据
      },
      // 批量选中的记录
      selectedRecords: []
    }
  },
  methods: {
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm)
      // 实际项目中调用接口：
      // this.$api.getRefundRecords(this.searchForm).then(res => {
      //   this.tableData = res.data
      //   this.pagination.total = res.total
      // })
    },
    // 导出按钮
    handleExport() {
      this.$message.success('导出功能暂未实现，可对接后端接口完成 Excel 导出')
      // 实际逻辑示例：
      // const params = { ...this.searchForm, page: this.pagination.page, size: this.pagination.size }
      // this.$api.exportRefundRecords(params).then(res => {
      //   // 处理文件流下载
      // })
    },
    // 表格多选事件
    handleSelectionChange(rows) {
      this.selectedRecords = rows
    },
    // 分页 - 每页条数变化
    handleSizeChange(val) {
      this.pagination.size = val
      this.handleQuery() // 切换条数后重新查询
    },
    // 分页 - 页码变化
    handleCurrentChange(val) {
      this.pagination.page = val
      this.handleQuery() // 切换页码后重新查询
    }
  }
}
</script>

<style scoped>
.refund-record-page {
  padding: 20px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
</style>