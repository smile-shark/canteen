<template>
  <div class="store-sales-rank">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="走势类型">
        <el-select v-model="searchForm.trendType" placeholder="月走势">
          <el-option label="月走势" value="month"></el-option>
          <el-option label="季走势" value="quarter"></el-option>
          <el-option label="年走势" value="year"></el-option>
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
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 柱状图区域（模拟，实际可用 ECharts 替换） -->
    <div class="chart-container">
      <h3>排行统计</h3>
      <div class="bar-chart">
        <div
          v-for="(item, index) in chartData"
          :key="index"
          class="bar-item"
        >
          <div class="bar" :style="{ height: item.height, backgroundColor: '#e67e22' }"></div>
          <div class="bar-label">{{ item.name }}</div>
        </div>
      </div>
    </div>

    <!-- 数据列表 & 导出按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin: 20px 0;">
      <span>排行详情</span>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 销售排行表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="rank" label="排序" align="center"></el-table-column>
      <el-table-column prop="storeName" label="门店名称" align="center"></el-table-column>
      <el-table-column prop="turnover" label="营业额" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'StoreSalesRank',
  data() {
    return {
      searchForm: {
        trendType: 'month',
        dateRange: ['2020-09-09', '2021-10-09']
      },
      // 模拟柱状图数据
      chartData: [
        { name: '泛海国际店', height: '450px' },
        { name: '西单店', height: '380px' },
        { name: '大悦城店', height: '320px' },
        { name: '石景山店', height: '300px' },
        { name: '平谷店', height: '250px' },
        { name: '昌平店', height: '220px' },
        { name: '东城店', height: '180px' },
        { name: '金融街店', height: '150px' },
        { name: '东坝店', height: '120px' },
        { name: '东四店', height: '100px' }
      ],
      // 模拟表格数据
      tableData: [
        { rank: 1, storeName: '泛海国际店', turnover: 41130 },
        { rank: 2, storeName: '西单店', turnover: 36000 },
        { rank: 3, storeName: '大悦城店', turnover: 32000 },
        { rank: 4, storeName: '石景山店', turnover: 30250 }
        // 可继续补充数据
      ]
    };
  },
  methods: {
    handleQuery() {
      console.log('查询条件:',  this.searchForm);
      // 可在此处调用接口，根据筛选条件更新图表和表格数据
    },
    handleExport() {
      this.$message.success('导出功能暂未实现，可在此添加导出逻辑');
    }
  }
};
</script>

<style scoped>
.store-sales-rank {
  padding: 20px;
  background-color: #fff;
}
.search-form {
  margin-bottom: 20px;
}
.chart-container {
  margin-bottom: 20px;
}
.bar-chart {
  display: flex;
  align-items: flex-end;
  height: 400px;
  padding: 0 20px;
  border-left: 2px solid #ccc;
  border-bottom: 2px solid #ccc;
}
.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 15px;
}
.bar {
  width: 30px;
  border-radius: 4px 4px 0 0;
}
.bar-label {
  margin-top: 5px;
}
</style>