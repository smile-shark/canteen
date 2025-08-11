<template>
  <div>
    <el-page-header @back="$router.back()" content="餐桌预约" style="padding: 10px;"></el-page-header>
    <el-form :model="form" label-width="100px" @submit.native.prevent="onSubmit" style="padding: 20px;">
      <el-form-item label="预约日期">
        <el-date-picker
          v-model="form.date"
          type="date"
          placeholder="选择日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="就餐人数">
        <el-select v-model="form.peopleCount" placeholder="请选择人数" style="width: 100%">
          <el-option
            v-for="item in peopleOptions"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="餐桌类型">
        <el-select v-model="form.tableType" placeholder="请选择类型" style="width: 100%">
          <el-option
            v-for="item in tableOptions"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="预订餐桌数">
        <el-select v-model="form.tableCount" placeholder="请选择" style="width: 100%">
          <el-option
            v-for="item in tableCountOptions"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="可预订时间">
        <el-time-select
          v-model="form.time"
          :picker-options="{
            start: '12:00',
            step: '00:30',
            end: '20:00'
          }"
          placeholder="选择时间"
          style="width: 100%">
        </el-time-select>
      </el-form-item>

      <el-form-item label="联系人">
        <el-input v-model="form.contactName" placeholder="请输入姓名"></el-input>
      </el-form-item>

      <el-form-item label="手机号">
        <el-input v-model="form.phoneNumber" placeholder="请输入手机号"></el-input>
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="form.remark" placeholder="备注" type="textarea"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="danger" native-type="submit" style="width: 100%">立即预订</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        date: '',
        peopleCount: '',
        tableType: '',
        tableCount: '',
        time: '',
        contactName: '',
        phoneNumber: '',
        remark: ''
      },

      peopleOptions: ['1', '2', '3', '4', '5', '6', '7', '8'],
      tableOptions: ['小桌（1-2人）', '中桌（3-4人）', '大桌（5-8人）', '包间（9-10人）', '包间（10人以上）'],
      tableCountOptions: ['1', '2', '3', '4', '5']
    };
  },
  computed: {
    calendarRows() {
      const firstDayOfMonth = new Date(this.selectedYear, this.selectedMonth, 1);
      const lastDayOfMonth = new Date(this.selectedYear, this.selectedMonth + 1, 0);
      const daysInMonth = lastDayOfMonth.getDate();
      const startDayOfWeek = firstDayOfMonth.getDay();

      const rows = [];
      let currentRow = [];

      for (let i = 0; i < startDayOfWeek; i++) {
        currentRow.push({ day: '', disabled: true });
      }

      for (let day = 1; day <= daysInMonth; day++) {
        if (currentRow.length === 7) {
          rows.push(currentRow);
          currentRow = [];
        }
        currentRow.push({ day, month: this.selectedMonth, year: this.selectedYear });
      }

      if (currentRow.length > 0) {
        while (currentRow.length < 7) {
          currentRow.push({ day: '', disabled: true });
        }
        rows.push(currentRow);
      }

      return rows;
    }
  },
  methods: {

    onSubmit() {
      if (!this.form.date || !this.form.peopleCount || !this.form.tableType || !this.form.tableCount || !this.form.time || !this.form.contactName || !this.form.phoneNumber) {
        this.$message({
          message: '请填写完整信息',
          type: 'warning'
        });
        return;
      }
      // 提交逻辑
      console.log('预约信息:', this.form);
      this.$message({
        message: '预订成功',
        type: 'success'
      });
    }
  }
};
</script>

<style scoped>
/* Add any custom styles here */
</style>