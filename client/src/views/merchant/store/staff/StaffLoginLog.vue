<template>
  <div class="tab-content">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="日期">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item label="门店">
        <el-select v-model="searchForm.store" placeholder="请选择门店">
          <el-option label="全部" value="" />
          <el-option
            v-for="shop in shopOptions"
            :key="shop.shopId"
            :label="shop.name"
            :value="shop.shopId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 登录日志表格 -->
    <el-card shadow="hover" style="margin-bottom: 20px">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="staffJobNum" label="工号" width="80" />
        <el-table-column prop="staffName" label="姓名" />
        <el-table-column prop="shopName" label="所属门店" />
        <el-table-column label="职位">
          <!-- 职务(0:店长、1:收银、2:主厨、3:保洁、4:服务员、5:主厨助理、6:切菜员、7:传菜员) -->
          <template slot-scope="scope">
            {{
              scope.row.staffType === 0
                ? "店长"
                : scope.row.staffType === 1
                ? "收银"
                : scope.row.staffType === 2
                ? "主厨"
                : scope.row.staffType === 3
                ? "保洁"
                : scope.row.staffType === 4
                ? "服务员"
                : scope.row.staffType === 5
                ? "主厨助理"
                : scope.row.staffType === 6
                ? "切菜员"
                : scope.row.staffType === 7
                ? "传菜员"
                : "未知职务"
            }}
          </template>
        </el-table-column>
        <el-table-column prop="loginTime" label="登录时间" width="160">
        </el-table-column>
        <el-table-column prop="quiteTime" label="退出时间" width="160" >
          <template slot-scope="scope">
            {{ scope.row.quitTime ? scope.row.quitTime : "无" }}
          </template>
        </el-table-column>
        <el-table-column prop="loginIp" label="登录IP" width="120" />
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @current-change="handleSearch"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="total"
        style="margin-top: 15px; text-align: right"
      />
    </el-card>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "StaffLoginLog",
  data() {
    return {
      // 搜索表单数据
      searchForm: {
        dateRange: [null, null],
        store: "",
      },
      shopOptions: [],
      // 表格数据
      tableData: [],

      // 分页数据
      page: 1,
      pageSize: 10,
      total: 1,
    };
  },
  methods: {
    // 查询
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.page = page;
      api.staffLoginLog
        .pageList(
          this.page,
          this.pageSize,
          this.searchForm.store,
          this.searchForm.dateRange[0],
          this.searchForm.dateRange[1]
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },

    // 导出
    handleExport() {
      console.log("导出登录日志数据");
      this.$message.success("导出成功");
    },
  },
  mounted() {
    api.shop.simpleList().then((res) => {
      if (res.data.code == 200) {
        this.shopOptions = res.data.data;
      } else {
        this.$message.error(res.data.msg);
      }
    });
    this.handleSearch();
  },
};
</script>

<style scoped>
.tab-content {
  margin-top: 20px;
}

.search-form {
  margin-bottom: 20px;
}
</style>
