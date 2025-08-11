<template>
  <div class="dish-flow-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="菜品">
        <el-input
          v-model="searchForm.dishName"
          placeholder="请输入菜品名称"
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
          <el-option label="泛海国际店" value="泛海国际店"></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
          <el-option label="西单店" value="西单店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="时间">
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

    <!-- 数据列表 & 导出按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <span>数据列表</span>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 菜品流水表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="编号" align="center"></el-table-column>
      <el-table-column prop="dishName" label="菜品名称" align="center"></el-table-column>
      <el-table-column prop="price" label="单价" align="center"></el-table-column>
      <el-table-column prop="store" label="门店" align="center"></el-table-column>
      <el-table-column prop="salesVolume" label="销售量" align="center"></el-table-column>
      <el-table-column prop="time" label="时间" align="center"></el-table-column>
      <el-table-column prop="amount" label="金额" align="center"></el-table-column>
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
export default {
  name: 'DishFlow',
  data() {
    return {
      // 筛选表单
      searchForm: {
        dishName: '',
        store: '',
        timeRange: []
      },
      // 菜品流水数据
      tableData: [
        {
          id: '100001',
          dishName: '千叶豆腐',
          price: '¥45.00',
          store: '泛海国际店',
          salesVolume: 200,
          time: '2020-04-07 21:00:00',
          amount: 9000
        }
        // 可继续补充更多模拟数据...
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 100
      }
    }
  },
  methods: {
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm);
      // 模拟筛选逻辑，可根据 searchForm 过滤 tableData
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
  }
}
</script>

<style scoped>
.dish-flow-page {
  padding: 20px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
</style>