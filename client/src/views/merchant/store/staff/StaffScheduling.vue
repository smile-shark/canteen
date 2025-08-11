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
        <el-select v-model="searchForm.shopId" placeholder="请选择门店">
          <el-option label="全部" value="" />
          <el-option
            v-for="(shop, index) in shopOptions"
            :key="index"
            :label="shop.name"
            :value="shop.shopId"
          ></el-option>
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
      <el-form-item>
        <el-button type="warning" icon="el-icon-upload" @click="handleImport">
          导入
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          type="info"
          icon="el-icon-edit"
          @click="openManualSchedulingDialog"
        >
          手动排班
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 排班表格 -->
    <el-card shadow="hover" style="margin-bottom: 20px">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="jobNum" label="工号" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="shopName" label="所属门店" />
        <el-table-column label="职位">
          <template #default="scope">
            <el-tag>{{ staffType(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <!-- 动态日期列 -->
        <el-table-column
          v-for="(date, index) in dateColumns"
          :key="index"
          :label="date"
        >
          <template #default="scope">
            {{ workingType(scope.row.workingSchedules[index]) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @current-change="handleSearch"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="size"
        :total="total"
        style="margin-top: 15px; text-align: right"
      />
    </el-card>

    <!-- 手动排班弹窗 -->
    <el-dialog
      title="手动排班"
      :visible.sync="schedulingDialogVisible"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="门店" required>
          <el-select
            v-model="form.shopId"
            placeholder="请选择门店"
            @change="searchStaffOptions"
          >
            <el-option
              v-for="(shop, index) in shopOptions"
              :key="index"
              :label="shop.name"
              :value="shop.shopId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工" required>
          <el-select v-model="form.staffId" placeholder="请选择员工">
            <el-option
              v-for="(staff, index) in staffOptions"
              :key="index"
              :label="staff.name"
              :value="staff.staffId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排班日期" required>
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="请选择"
          />
        </el-form-item>
        <el-form-item label="班次">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="早班" :value="0" />
            <el-option label="晚班" :value="1" />
            <el-option label="休息" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="schedulingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSchedulingConfirm"
            >确定</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api, { formatDate } from "@/api";

export default {
  name: "StaffScheduling",
  data() {
    return {
      // 搜索表单数据
      searchForm: {
        dateRange: [new Date(), new Date()],
        shopId: "",
      },

      // 表格数据
      tableData: [],

      // 分页数据
      page: 1,
      size: 10,
      total: 1,

      // 排班日期列（示例：近3天）
      dateColumns: [],

      // 弹窗状态
      schedulingDialogVisible: false,

      // 表单数据
      form: {
        staffId: "",
        shopId: "",
        date: "",
        type: "",
      },
      shopOptions: [],
      staffOptions: [],
    };
  },
  methods: {
    staffType(type) {
      //职务(0:店长、1:收银、2:主厨、3、保洁、4:服务员、5:主厨助理、6:切菜员、7:传菜员)
      switch (type) {
        case 0:
          return "店长";
        case 1:
          return "收银";
        case 2:
          return "主厨";
        case 3:
          return "保洁";
        case 4:
          return "服务员";
        case 5:
          return "主厨助理";
        case 6:
          return "切菜员";
        case 7:
          return "传菜员";
        default:
          return "未知";
      }
    },
    searchStaffOptions() {
      this.staffOptions = [];
      this.form.staffId = "";
      // 查询门店的员工
      api.staff.simpleListByShopId(this.form.shopId).then((res) => {
        if (res.data.code == 200) {
          this.staffOptions = res.data.data;
        }
      });
    },
    // 查询
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.page = page;
      console.log(this.searchForm);

      // 实际项目中调用接口查询数据
      api.workingSchedule
        .pageList(
          this.page,
          this.size,
          this.searchForm.shopId,
          this.searchForm.dateRange[0],
          this.searchForm.dateRange[1]
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
            // 查询到了后生成 dateColumns
            this.dateColumns = [];
            const startDate = new Date(this.searchForm.dateRange[0]);
            const endDate = new Date(this.searchForm.dateRange[1]);

            // 防止修改原始日期对象，创建副本
            let currentDate = new Date(startDate);

            // 循环遍历从开始日期到结束日期的每一天
            while (currentDate <= endDate) {
              // 格式化月份（补零）
              const month = String(currentDate.getMonth() + 1).padStart(2, "0");
              // 格式化日期（补零）
              const day = String(currentDate.getDate()).padStart(2, "0");
              // 拼接成"MM-dd"格式并添加到数组
              this.dateColumns.push(`${month}-${day}`);

              // 日期加1天
              currentDate.setDate(currentDate.getDate() + 1);
            }
          }
        });
    },

    // 手动排班弹窗
    openManualSchedulingDialog() {
      this.schedulingDialogVisible = true;
      this.form = {
        staffId: "",
        shopId: "",
        workingDate: "",
        type: "",
      };
    },

    // 导出
    handleExport() {
      console.log("导出排班数据");
      this.$message.success("导出成功");
    },

    // 导入
    handleImport() {
      console.log("导入排班数据");
      this.$message.success("导入成功");
    },

    // 排班表单确认
    handleSchedulingConfirm() {
      if (!this.form.staffId) {
        this.$message.warning("请选择员工");
        return;
      }
      this.form.workingDate = formatDate(this.form.date);
      // 实际调用接口保存
      api.workingSchedule.add(this.form).then((res) => {
        if (res.data.code == 200) {
          this.schedulingDialogVisible = false;
          this.$message.success("排班成功");
          this.handleSearch();
        } else {
          this.$message.error(res.data.msg || "排班失败");
        }
      });
    },
    workingType(info) {
      if (!info) return "无";
      switch (info.type) {
        case 0:
          return "早班";
        case 1:
          return "晚班";
        case 2:
          return "休";
        default:
          return "无";
      }
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
.tab-content {
  margin-top: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
