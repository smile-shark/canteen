<template>
  <div class="dish-sales-rank">
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
      <el-table-column prop="dishName" label="菜品名称" align="center"></el-table-column>
      <el-table-column prop="price" label="单价" align="center"></el-table-column>
      <el-table-column prop="salesVolume" label="销售数量" align="center"></el-table-column>
      <el-table-column prop="amount" label="金额" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'DishSalesRank',
  data() {
    return {
      // 筛选表单
      searchForm: {
        trendType: 'month',
        dateRange: ['2020-09-09', '2021-10-09'],
        store: ''
      },
      // 模拟柱状图数据
      chartData: [
        { name: '千叶豆腐', height: '400px' },
        { name: '西红柿鸡蛋汤', height: '380px' },
        { name: '清炒娃娃菜', height: '350px' },
        { name: '西蓝花', height: '320px' },
        { name: '烤肉', height: '280px' },
        { name: '干锅土豆片', height: '250px' },
        { name: '毛血旺', height: '220px' },
        { name: '炒菜花', height: '180px' },
        { name: '红烧肉', height: '150px' },
        { name: '烧茄子', height: '120px' }
      ],
      // 销售排行表格数据
      tableData: [
        { rank: 1, dishName: '千叶豆腐', price: '¥45', salesVolume: 914, amount: 41130 },
        { rank: 2, dishName: '西红柿鸡蛋汤', price: '¥23', salesVolume: 869, amount: 19987 },
        { rank: 3, dishName: '清炒娃娃菜', price: '¥40', salesVolume: 800, amount: 32000 },
        { rank: 4, dishName: '西蓝花', price: '¥35', salesVolume: 700, amount: 24500 }
        // 可继续补充数据
      ]
    };
  },
  methods: {
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm);
      // 可在此处调用接口，根据筛选条件更新图表和表格数据
    },
    // 导出按钮
    handleExport() {
      this.$message.success('导出功能暂未实现，可在此添加导出逻辑');
    }
  }
};
</script>

<style scoped>
.dish-sales-rank {
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