<template>
  <div class="receipt-report">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="趋势类型">
        <el-select v-model="searchForm.trendType" placeholder="月走势">
          <el-option label="月走势" value="month"></el-option>
          <!-- 可扩展其他走势类型，如周、年 -->
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="—"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select v-model="searchForm.store" placeholder="请选择门店" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="泛海国际店" value="泛海国际店"></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 折线图区域 -->
    <div class="chart-container">
      <h3>销售统计</h3>
      <div id="salesChart" style="width: 100%; height: 400px;"></div>
    </div>

    <!-- 数据列表 & 导出按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin: 20px 0;">
      <span>数据列表</span>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 收款数据表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" align="center"></el-table-column>
      <el-table-column prop="payType" label="支付方式" align="center"></el-table-column>
      <el-table-column prop="amount" label="金额" align="center"></el-table-column>
      <el-table-column prop="payTime" label="支付时间" align="center"></el-table-column>
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
  </div>
</template>

<script>
import * as echarts from 'echarts'; 

export default {
  name: 'ReceiptReport',
  data() {
    return {
      searchForm: {
        trendType: 'month', // 趋势类型，默认月走势
        dateRange: ['2020-09-09', '2021-06-09'],
        store: ''
      },
      tableData: [
        {
          orderNo: '20210909120000',
          payType: '支付宝',
          amount: '¥500.00',
          payTime: '2020-04-07 21:00:00'
        }
        // 可补充更多模拟数据
      ],
      pagination: {
        page: 1,
        size: 10,
        total: 100
      },
      chartInstance: null // 用于存储 ECharts 实例
    };
  },
  mounted() {
    this.initChart();
  },
  methods: {
    // 初始化折线图
    initChart() {
      this.chartInstance = echarts.init(document.getElementById('salesChart'));
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['现金', '微信', '支付宝', '银行卡', '会员卡']
        },
        xAxis: {
          type: 'category',
          data: ['9月', '10月', '11月', '12月', '2021年1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '现金',
            type: 'line',
            data: [3000, 3200, 3100, 3300, 3500, 3400, 3600, 3550, 3580, 3500]
          },
          {
            name: '微信',
            type: 'line',
            data: [8000, 8200, 8500, 8300, 8600, 8400, 8700, 8600, 8650, 8550]
          },
          {
            name: '支付宝',
            type: 'line',
            data: [7000, 7200, 7100, 7300, 7500, 7400, 7600, 7550, 7580, 7500]
          },
          {
            name: '银行卡',
            type: 'line',
            data: [2000, 2200, 2100, 2300, 2500, 2400, 2600, 2550, 2580, 2500]
          },
          {
            name: '会员卡',
            type: 'line',
            data: [4000, 4200, 4100, 4300, 4500, 4400, 4600, 4550, 4580, 4500]
          }
        ]
      };
      this.chartInstance.setOption(option);
    },
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm);
      // 可在此处调用接口，根据筛选条件更新表格和图表数据
    },
    // 导出按钮
    handleExport() {
      this.$message.success('导出功能暂未实现，可在此添加导出逻辑');
    },
    // 分页 - 每页条数变化
    handleSizeChange(val) {
      this.pagination.size = val;
    },
    // 分页 - 当前页变化
    handleCurrentChange(val) {
      this.pagination.page = val;
    }
  },
  beforeDestroy() {
    // 销毁 ECharts 实例，避免内存泄漏
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  }
};
</script>

<style scoped>
.receipt-report {
  padding: 20px;
  background-color: #fff;
}
.search-form {
  margin-bottom: 20px;
}
.chart-container {
  margin-bottom: 20px;
}
</style>