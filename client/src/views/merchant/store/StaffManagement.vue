<template>
  <div class="staff-management">
    <!-- 标题 -->
    <el-page-header content="员工管理" style="margin-bottom: 20px;"></el-page-header>

    <!-- 标签页：员工列表、员工排班、员工登录日志 -->
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="员工列表" name="list">
        <staff-list />
      </el-tab-pane>
      <el-tab-pane label="员工排班" name="scheduling">
        <staff-scheduling />
      </el-tab-pane>
      <el-tab-pane label="员工登录日志" name="loginLog">
        <staff-login-log />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import StaffScheduling from './staff/StaffScheduling.vue'
import StaffLoginLog from './staff/StaffLoginLog.vue'
import StaffList from './staff/StaffList.vue';

export default {
  name: 'StaffManagement',
  components: {
    StaffList,
    StaffScheduling,
    StaffLoginLog
  },
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'list'
    };
  }
};
</script>

<style scoped>
.staff-management {
  padding: 20px;
}
</style>