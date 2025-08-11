<template>
  <div class="dashboard-home">
    <!-- 头部选择门店区域 -->
    <el-select
      v-model="selectedStore"
      placeholder="请选择门店"
      class="store-select"
    >
      <el-option
        v-for="store in storeOptions"
        :key="store.value"
        :label="store.label"
        :value="store.value"
      ></el-option>
    </el-select>
    <!-- 营收统计卡片区域 -->
    <el-row :gutter="20" class="stats-card-row">
      <el-col :span="6">
        <el-card class="stats-card" style="background-color: red; color: #fff;">
          <div class="stats-value">120,000</div>
          <div class="stats-label">今日营收 (元)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card" style="background-color: orange; color: #fff;">
          <div class="stats-value">120,000</div>
          <div class="stats-label">昨日营收 (元)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card" style="background-color: blue; color: #fff;">
          <div class="stats-value">250,000</div>
          <div class="stats-label">本周营收 (元)</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card" style="background-color: lightgreen; color: #fff;">
          <div class="stats-value">500,000</div>
          <div class="stats-label">本月营收 (元)</div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 订单统计和支付方式统计饼图区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <div class="chart-container">
          <h3>订单统计</h3>
          <div class="date-range">2021-09-09—2021-10-09</div>
          <div id="order-stat-chart" style="width: 100%; height: 400px;"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-container">
          <h3>支付方式统计</h3>
          <div class="date-range">2021-09-09—2021-10-09</div>
          <div id="payment-stat-chart" style="width: 100%; height: 400px;"></div>
        </div>
      </el-col>
    </el-row>
    <!-- 菜品销售排行柱状图区域 -->
    <div class="chart-container">
      <h3>菜品销售排行</h3>
      <div class="date-range">2021-09-09—2021-10-09</div>
      <div id="dish-sales-chart" style="width: 100%; height: 400px;"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'DashboardHome',
  data() {
    return {
      selectedStore: '大悦城店',
      storeOptions: [
        { label: '大悦城店', value: '大悦城店' },
        { label: '其他门店', value: '其他门店' }
      ]
    };
  },
  mounted() {
    // 初始化订单统计饼图
    this.initOrderStatChart();
    // 初始化支付方式统计饼图
    this.initPaymentStatChart();
    // 初始化菜品销售排行柱状图
    this.initDishSalesChart();
  },
  methods: {
    initOrderStatChart() {
      const chartDom = document.getElementById('order-stat-chart');
      const myChart = echarts.init(chartDom);
      const option = {
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            data: [
              { value: 380, name: '堂食', itemStyle: { color: '#9370DB' } },
              { value: 160, name: '餐桌预订', itemStyle: { color: '#FF69B4' } },
              { value: 240, name: '外卖', itemStyle: { color: '#FFA07A' } }
            ],
            label: {
              show: true,
              formatter: '{b}: {c} ({d}%)'
            }
          }
        ]
      };
      myChart.setOption(option);
    },
    initPaymentStatChart() {
      const chartDom = document.getElementById('payment-stat-chart');
      const myChart = echarts.init(chartDom);
      const option = {
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            data: [
              { value: 380, name: '微信', itemStyle: { color: '#9370DB' } },
              { value: 160, name: '现金', itemStyle: { color: '#FFA07A' } },
              { value: 200, name: '会员卡', itemStyle: { color: '#40E0D0' } },
              { value: 240, name: '银行卡', itemStyle: { color: '#FF69B4' } },
              { value: 290, name: '支付宝', itemStyle: { color: '#98FB98' } }
            ],
            label: {
              show: true,
              formatter: '{b}: {c} ({d}%)'
            }
          }
        ]
      };
      myChart.setOption(option);
    },
    initDishSalesChart() {
      const chartDom = document.getElementById('dish-sales-chart');
      const myChart = echarts.init(chartDom);
      const option = {
        xAxis: {
          type: 'category',
          data: [
            '千叶豆腐', '西红柿鸡蛋汤', '清炒娃娃菜', '西蓝花', 
            '烤肉', '红烧狮子头', '清蒸桂鱼', '炒菜花', 
            '红烧肉', '凉拌肉皮冻'
          ],
          axisLabel: {
            rotate: 45,
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            type: 'bar',
            data: [900, 850, 800, 700, 600, 550, 500, 400, 250, 200],
            itemStyle: {
              color: '#CD5C5C'
            }
          }
        ]
      };
      myChart.setOption(option);
    }
  }
};
</script>

<style scoped>
.dashboard-home {
  padding: 20px;
}
.store-select {
  margin-bottom: 20px;
}
.stats-card-row {
  margin-bottom: 20px;
}
.stats-card {
  text-align: center;
  padding: 20px 0;
}
.stats-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}
.stats-label {
  font-size: 14px;
}
.chart-row {
  margin-bottom: 20px;
}
.chart-container {
  border: 1px solid #ebeef5;
  padding: 20px;
  border-radius: 8px;
  background-color: #fff;
}
.date-range {
  color: #999;
  font-size: 12px;
  margin-bottom: 10px;
}
</style>