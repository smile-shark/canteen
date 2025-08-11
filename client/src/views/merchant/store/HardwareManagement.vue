<template>
  <div class="hardware-management">
    <el-page-header content="硬件管理" style="margin-bottom: 20px;"></el-page-header>
    
    <!-- Tabs 切换：打印机管理 | 自定义小票 -->
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="打印机管理" name="printer">
        <PrinterManagement />
      </el-tab-pane>
      <el-tab-pane label="自定义小票" name="ticket">
        <CustomTicket />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import PrinterManagement from './hardware/PrinterManagement.vue'
import CustomTicket from './hardware/CustomTicket.vue'

export default {
  name: 'HardwareManagement',
  components: { PrinterManagement, CustomTicket },
  data() {
    return {
      activeTab: 'printer' // 默认选中打印机管理
    }
  }
}
</script>

<style scoped>
.hardware-management {
  padding: 20px;
}
</style>