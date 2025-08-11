<template>
  <div class="my-reservation-page">
    <!-- 返回按钮 -->
    <el-row>
      <el-col :span="2">
        <el-button icon="el-icon-back" type="text" @click="$router.push('/customer/profile')"></el-button>
      </el-col>
      <el-col :span="20">
        <h2 class="page-title">我的预约</h2>
      </el-col>
    </el-row>
    <!-- 预约列表 -->
    <el-card
      v-for="(reservation, index) in reservations"
      :key="index"
      shadow="hover"
      class="reservation-card"
    >
      <div slot="header">
        <span>{{ reservation.hall }} ({{ reservation.peopleRange }})</span>
        <el-tag :type="getTagType(reservation.status)">{{ reservation.status }}</el-tag>
      </div>
      <div class="reservation-info">
        <p>应付金额: ¥{{ reservation.amount }}</p>
        <p>就诊人数: {{ reservation.peopleCount }}人</p>
        <!-- 小箭头，点击跳转到详情页 -->
        <el-button
          icon="el-icon-arrow-right"
          type="text"
          @click="gotoDetail(reservation)"
        ></el-button>
      </div>
    </el-card>
    <!-- 预约详情页，使用 Dialog 展示 -->
    <el-dialog
      title="餐桌预约详情"
      :visible.sync="dialogVisible"
      width="100%"
    >
      <div class="reservation-detail">
        <div class="status-tag" :style="{backgroundColor: statusBgColor}">
          <i :class="statusIcon"></i>
          {{ detailStatus }}
        </div>
        <div class="detail-section">
          <h3>预约信息</h3>
          <p>预定时间: {{ detailReservation.reserveTime }}</p>
          <p>餐桌类型: {{ detailReservation.tableType }}</p>
          <p>就餐人数: {{ detailReservation.peopleCount }}</p>
          <p>提交预约时间: {{ detailReservation.submitTime }}</p>
        </div>
        <div class="detail-section">
          <h3>联系人信息</h3>
          <p>联系人: {{ detailReservation.contactName }}</p>
          <p>手机号: {{ detailReservation.contactPhone }}</p>
          <p>备注: {{ detailReservation.remark }}</p>
        </div>
        <el-button
          type="warning"
          @click="cancelReservation"
          v-if="canCancel"
        >
          取消预约
        </el-button>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MyReservation',
  data() {
    return {
      reservations: [
        {
          id: 1,
          hall: '大厅',
          peopleRange: '3-5人',
          status: '等待确认',
          amount: 151,
          peopleCount: 4,
          reserveTime: '2018-12-12 08:30',
          tableType: '大厅 (3-5) 人桌',
          submitTime: '2012-12-12 12:30',
          contactName: '张三',
          contactPhone: '18866666666',
          remark: '无'
        },
        {
          id: 2,
          hall: '大厅',
          peopleRange: '3-5人',
          status: '预约成功',
          amount: 151,
          peopleCount: 4,
          reserveTime: '2018-12-12 08:30',
          tableType: '大厅 (3-5) 人桌',
          submitTime: '2012-12-12 12:30',
          contactName: '张三',
          contactPhone: '18866666666',
          remark: '无'
        },
        {
          id: 3,
          hall: '大厅',
          peopleRange: '3-5人',
          status: '已关闭',
          amount: 151,
          peopleCount: 4,
          reserveTime: '2018-12-12 08:30',
          tableType: '大厅 (3-5) 人桌',
          submitTime: '2012-12-12 12:30',
          contactName: '张三',
          contactPhone: '18866666666',
          remark: '无'
        },
        {
          id: 4,
          hall: '大厅',
          peopleRange: '3-5人',
          status: '已完成',
          amount: 151,
          peopleCount: 4,
          reserveTime: '2018-12-12 08:30',
          tableType: '大厅 (3-5) 人桌',
          submitTime: '2012-12-12 12:30',
          contactName: '张三',
          contactPhone: '18866666666',
          remark: '无'
        }
      ],
      dialogVisible: false,
      detailReservation: {},
      detailStatus: '',
      statusBgColor: '',
      statusIcon: ''
    };
  },
  computed: {
    canCancel() {
      return this.detailStatus === '等待确认';
    }
  },
  methods: {
    // 根据状态获取标签类型
    getTagType(status) {
      switch (status) {
        case '等待确认':
          return 'warning';
        case '预约成功':
          return'success';
        case '已关闭':
          return 'danger';
        case '已完成':
          return 'info';
        default:
          return '';
      }
    },
    // 跳转到详情页（这里用 Dialog 模拟）
    gotoDetail(reservation) {
      this.dialogVisible = true;
      this.detailReservation = reservation;
      this.setDetailStatus(reservation.status);
    },
    // 设置详情页的状态展示
    setDetailStatus(status) {
      this.detailStatus = status;
      switch (status) {
        case '等待确认':
          this.statusBgColor = '#ff9900';
          this.statusIcon = 'el-icon-time';
          break;
        case '预约成功':
          this.statusBgColor = '#e67e22';
          this.statusIcon = 'el-icon-success';
          break;
        case '已关闭':
          this.statusBgColor = '#999';
          this.statusIcon = 'el-icon-close';
          break;
        case '已完成':
          this.statusBgColor = '#5cb85c';
          this.statusIcon = 'el-icon-check';
          break;
        default:
          this.statusBgColor = '#999';
          this.statusIcon = 'el-icon-question';
      }
    },
    // 取消预约方法
    cancelReservation() {
      this.$confirm('确定要取消预约吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里可添加调用接口取消预约的逻辑
        this.$message.success('预约已取消');
        // 取消后更新列表数据，比如过滤掉已取消的预约
        this.reservations = this.reservations.filter(
          item => item.id!== this.detailReservation.id
        );
        this.dialogVisible = false;
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    }
  }
};
</script>

<style scoped>
.my-reservation-page {
  padding: 20px;
}
.page-title {
  margin: 0;
}
.reservation-card {
  margin-bottom: 10px;
}
.reservation-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.reservation-detail {
  padding: 20px;
}
.status-tag {
  display: flex;
  align-items: center;
  color: #fff;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.status-tag i {
  margin-right: 5px;
}
.detail-section {
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>