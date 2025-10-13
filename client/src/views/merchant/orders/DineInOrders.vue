<template>
  <div class="dining-order-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="订单编号">
        <el-input v-model="searchForm.orderNo" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
        >
          <el-option label="全部" value="">全部</el-option>
          <el-option
            v-for="(shop, index) in shopOptions"
            :key="index"
            :label="shop.name"
            :value="shop.shopId"
          ></el-option>
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
        <span style="margin: 0 5px">—</span>
        <el-date-picker
          v-model="searchForm.endTime"
          type="date"
          placeholder="结束时间"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 导出按钮 -->
    <div
      style="
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
      "
    >
      <div></div>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 订单表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column
        prop="customerOrderId"
        label="订单编号"
        align="center"
        width="400"
      />
      <el-table-column
        prop="createTime"
        label="下单时间"
        align="center"
        width="320"
      >
      <template slot-scope="scope">
        {{formatDateTime(scope.row.createTime)}}
      </template>
    </el-table-column>
      <el-table-column prop="allPrice" label="订单金额" align="center">
        <template #default="scope">
          <span>￥{{ scope.row.allPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属门店" align="center" width="160">
        <template #default="scope">
          {{
            shopOptions.find((item) => item.shopId == scope.row.shopId)?.name
          }}
        </template>
      </el-table-column>
      <el-table-column prop="tableNum" label="桌号" align="center" />
      <el-table-column prop="state" label="订单状态" align="center">
        <template #default="scope">
          {{
            [
              "待付款",
              "待收取",
              "已完成",
              "已取消",
              "待评价",
              "未处理",
              "已接单",
              "已拒绝",
              "已送达",
              "已自提",
              "已作废",
              "已退款",
            ].find((item, index) => index == scope.row.state)
          }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button type="text" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @current-change="handleSearch"
      :current-page="pagination.page"
      background
      :page-size="pagination.size"
      :total="pagination.total"
      style="margin-top: 10px; text-align: right"
    />

    <!-- 查看订单弹窗 -->
    <el-dialog title="订单详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions
        :column="1"
        style="width: 100%"
        title="台桌信息"
        v-if="viewOrder.diningTableId"
      >
        <el-descriptions-item label="所属门店">{{
          shopOptions.find((item) => item.shopId == viewOrder.shopId)?.name
        }}</el-descriptions-item>
        <el-descriptions-item label="桌号">{{
          viewOrder.tableNum
        }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions
        :column="1"
        style="width: 100%"
        title="菜品信息"
        v-if="viewOrder.customerOrderCuisines"
      >
        <el-descriptions-item label="菜品详情">
          <el-table :data="viewOrder.customerOrderCuisines">
            <el-table-column prop="cuisineNum" label="编号"></el-table-column>
            <el-table-column prop="name" label="菜品名称"></el-table-column>
            <el-table-column label="单价" >
              <template #default="scope"> ￥{{ scope.row.price }} </template>
            </el-table-column>
            <el-table-column prop="num" label="数量"></el-table-column>
            <el-table-column label="总价" >
              <template #default="scope">
                ￥{{ scope.row.price * scope.row.num }}
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="订单信息" :column="2">
        <el-descriptions-item label="订单编号">{{ viewOrder.customerOrderId }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ [
              "待付款",
              "待收取",
              "已完成",
              "已取消",
              "待评价",
              "未处理",
              "已接单",
              "已拒绝",
              "已送达",
              "已自提",
              "已作废",
              "已退款",
            ].find((item, index) => index == viewOrder.state) }}</el-descriptions-item>
            <el-descriptions-item label="订单金额">{{ viewOrder.allPrice }}</el-descriptions-item>
            <el-descriptions-item label="下单时间">{{ formatDateTime(viewOrder.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ [
              '微信支付','支付宝支付','现金支付','银行卡支付','会员卡支付'
            ].find((item,index)=>index==viewOrder.payType) }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ formatDateTime(viewOrder.payTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";
import { formatDateTime } from "@/api";
export default {
  name: "DiningOrderPage",
  data() {
    return {
      formatDateTime:formatDateTime,
      // 筛选表单
      searchForm: {
        orderNo: "",
        store: "",
        startTime: "",
        endTime: "",
      },
      // 表格数据
      tableData: [],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 0, // 模拟共100条数据
      },
      // 查看订单弹窗
      viewDialogVisible: false,
      viewOrder: {},
      shopOptions: [],
    };
  },
  methods: {
    // 查询按钮
    handleSearch(page) {
      if (typeof page != "number") page = 1;
      this.pagination.page = page;
      api.customerOrder
        .pageList(
          this.pagination.page,
          this.pagination.size,
          0,
          this.searchForm.orderNo,
          this.searchForm.store,
          this.searchForm.startTime,
          this.searchForm.endTime
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableData = res.data.data.records;
            this.pagination.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    // 导出按钮
    handleExport() {
      this.$message.success("模拟导出操作，可结合后端实现真实导出");
    },
    // 查看订单
    handleView(row) {
      this.viewOrder = row;
      this.viewOrder.customerOrderCuisines = [];
      this.viewDialogVisible = true;
      // 查询一些数据下来
      api.customerOrderCuisine
        .listByCustomerOrderId(row.customerOrderId)
        .then((res) => {
          if (res.data.code == 200) {
            this.viewOrder.customerOrderCuisines = res.data.data;
          }
        });
    },
  },
  mounted() {
    api.shop.simpleList().then((res) => {
      if (res.data.code == 200) {
        this.shopOptions = res.data.data;
      }
    });
    this.handleSearch();
  },
};
</script>

<style scoped>
.dining-order-page {
  padding: 20px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
</style>
