<template>
  <div class="table-reservation-order">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="订单编号">
        <el-input v-model="searchForm.orderNo" placeholder="请输入" clearable></el-input>
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select v-model="searchForm.store" placeholder="请选择门店" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
          <el-option label="泛海国际店" value="泛海国际店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择订单状态">
        <el-select v-model="searchForm.orderStatus" placeholder="请选择订单状态" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="未处理" value="未处理"></el-option>
          <el-option label="已确认" value="已确认"></el-option>
          <el-option label="已完成" value="已完成"></el-option>
          <el-option label="已取消" value="已取消"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
          v-model="searchForm.orderTime"
          type="daterange"
          range-separator="—"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表及导出按钮 -->
    <div class="data-list-header">
      <span>数据列表</span>
      <el-button type="warning" @click="handleExport">导出</el-button>
    </div>

    <!-- 订单表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" align="center"></el-table-column>
      <el-table-column prop="submitTime" label="提交时间" align="center"></el-table-column>
      <el-table-column prop="reserveTime" label="预约时间" align="center"></el-table-column>
      <el-table-column prop="store" label="所属门店" align="center"></el-table-column>
      <el-table-column prop="dinerNum" label="就餐人数" align="center"></el-table-column>
      <el-table-column prop="tableNo" label="桌号" align="center"></el-table-column>
      <el-table-column prop="contactName" label="联系人" align="center"></el-table-column>
      <el-table-column prop="phone" label="手机号" align="center"></el-table-column>
      <el-table-column prop="booker" label="预订人" align="center"></el-table-column>
      <el-table-column prop="orderStatus" label="订单状态" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.orderStatus === '未处理'">
            <el-button type="text" @click="handleConfirm(scope.row)">确认</el-button>
            <el-button type="text" @click="handleCancel(scope.row)" style="color: #f56c6c;">取消</el-button>
          </template>
          <template v-if="scope.row.orderStatus === '已确认'">
            <el-button type="text" @click="handleModify(scope.row)">修改</el-button>
            <el-button type="text" @click="handleCancel(scope.row)" style="color: #f56c6c;">取消</el-button>
          </template>
          <template v-if="scope.row.orderStatus === '已完成' || scope.row.orderStatus === '已取消'">
            <el-button type="text" style="color: #f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
          </template>
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
    >
    </el-pagination>

    <!-- 操作弹窗 -->
    <el-dialog title="操作" :visible.sync="dialogVisible" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="联系人">
          <el-input v-model="form.contactName"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="门店">
          <el-select v-model="form.store" placeholder="请选择门店">
            <el-option label="大悦城店" value="大悦城店"></el-option>
            <el-option label="泛海国际店" value="泛海国际店"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间">
          <el-datetime-picker
            v-model="form.reserveTime"
            type="datetime"
            value-format="yyyy-MM-dd HH: mm"
            placeholder="选择预约时间"
          >
          </el-datetime-picker>
        </el-form-item>
        <el-form-item label="就餐人数">
          <el-input v-model.number="form.dinerNum"></el-input>
        </el-form-item>
        <el-form-item label="桌号">
          <el-input v-model="form.tableNo"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TableReservationOrder',
  data() {
    return {
      searchForm: {
        orderNo: '',
        store: '',
        orderStatus: '',
        orderTime: []
      },
      tableData: [
        {
          orderNo: '2021091008181',
          submitTime: '2021-09-10 12:00:12',
          reserveTime: '2021-09-10 12:00:12',
          store: '大悦城店',
          dinerNum: '6人',
          tableNo: '1F 0010',
          contactName: '王五',
          phone: '18811111111',
          booker: '员工名',
          orderStatus: '未处理'
        },
        {
          orderNo: '2021091008181',
          submitTime: '2021-09-10 12:00:12',
          reserveTime: '2021-09-10 12:00:12',
          store: '泛海国际店',
          dinerNum: '6人',
          tableNo: '1F 0010',
          contactName: '王五',
          phone: '18811111111',
          booker: '员工名',
          orderStatus: '已确认'
        }
        // 可继续添加更多模拟数据
      ],
      pagination: {
        page: 1,
        size: 10,
        total: 100
      },
      dialogVisible: false,
      form: {
        contactName: '',
        phone: '',
        store: '',
        reserveTime: '',
        dinerNum: '',
        tableNo: ''
      }
    };
  },
  methods: {
    handleQuery() {
      console.log('查询条件：', this.searchForm);
      // 这里可添加实际的查询逻辑，比如根据searchForm过滤tableData
    },
    handleExport() {
      this.$message.success('导出功能暂未实现，可在此添加导出逻辑');
    },
    handleSizeChange(val) {
      this.pagination.size = val;
    },
    handleCurrentChange(val) {
      this.pagination.page = val;
    },
    handleConfirm(row) {
      this.form = {
        contactName: row.contactName,
        phone: row.phone,
        store: row.store,
        reserveTime: row.reserveTime,
        dinerNum: row.dinerNum,
        tableNo: row.tableNo
      };
      this.dialogVisible = true;
    },
    handleModify(row) {
      this.form = {
        contactName: row.contactName,
        phone: row.phone,
        store: row.store,
        reserveTime: row.reserveTime,
        dinerNum: row.dinerNum,
        tableNo: row.tableNo
      };
      this.dialogVisible = true;
    },
    handleCancel(row) {
      this.$confirm('确定要取消该订单吗？', '提示', {
        type: 'warning'
      }).then(() => {
        // 这里可添加取消订单的逻辑
        this.$message.success('取消成功');
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    },
    handleDelete(row) {
      this.$confirm('确定要删除该订单吗？', '提示', {
        type: 'warning'
      }).then(() => {
        const index = this.tableData.findIndex(item => item.orderNo === row.orderNo);
        if (index!== -1) {
          this.tableData.splice(index, 1);
          this.pagination.total--;
        }
        this.$message.success('删除成功');
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    },
    handleSubmit() {
      // 这里可添加提交表单的逻辑，比如更新订单信息
      this.dialogVisible = false;
      this.$message.success('操作成功');
    }
  }
};
</script>

<style scoped>
.table-reservation-order {
  padding: 20px;
  background-color: #fff;
}
.search-form {
  margin-bottom: 10px;
}
.data-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
}
</style>