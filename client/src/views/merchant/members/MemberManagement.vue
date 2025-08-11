<template>
  <div class="member-management-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="会员">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入会员号/姓名"
          clearable
        />
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
        >
          <el-option label="全部" value="">全部</el-option>
          <el-option label="泛海国际店" value="泛海国际店">泛海国际店</el-option>
          <el-option label="大悦城店" value="大悦城店">大悦城店</el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 操作按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <span>数据列表</span>
      <el-button type="warning" @click="openAddMemberDialog">新增会员</el-button>
    </div>

    <!-- 会员表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="memberNo" label="会员号" align="center"></el-table-column>
      <el-table-column prop="name" label="姓名" align="center"></el-table-column>
      <el-table-column prop="memberType" label="会员类别" align="center"></el-table-column>
      <el-table-column prop="discount" label="折扣" align="center"></el-table-column>
      <el-table-column prop="consumeTimes" label="消费次数" align="center"></el-table-column>
      <el-table-column prop="consumeAmount" label="消费金额" align="center"></el-table-column>
      <el-table-column prop="balance" label="余额" align="center"></el-table-column>
      <el-table-column prop="store" label="所属门店" align="center"></el-table-column>
      <el-table-column prop="phone" label="电话" align="center"></el-table-column>
      <el-table-column prop="createDate" label="创建日期" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === '正常'? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" @click="handleConsumeRecord(scope.row)">消费记录</el-button>
          <el-button type="text" @click="handlePointRecord(scope.row)">积分</el-button>
          <el-button type="text" @click="handleRechargeRecord(scope.row)">充值记录</el-button>
          <el-button type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
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
    />

    <!-- 批量操作 -->
    <div style="margin-top: 10px;">
      <el-button type="danger" @click="handleBatchDelete" icon="el-icon-delete">批量删除</el-button>
    </div>

    <!-- 新增会员弹窗 -->
    <el-dialog title="新增会员" :visible.sync="addMemberDialogVisible" width="500px">
      <el-form :model="memberForm" label-width="100px" ref="memberForm">
        <el-form-item label="会员号" prop="memberNo">
          <el-input
            v-model="memberForm.memberNo"
            placeholder="请输入会员号"
            clearable
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="memberForm.name"
            placeholder="请输入姓名"
            clearable
          />
        </el-form-item>
        <el-form-item label="门店" prop="store">
          <el-select
            v-model="memberForm.store"
            placeholder="请选择门店"
            clearable
          >
            <el-option label="泛海国际店" value="泛海国际店">泛海国际店</el-option>
            <el-option label="大悦城店" value="大悦城店">大悦城店</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="会员类别" prop="memberType">
          <el-select
            v-model="memberForm.memberType"
            placeholder="请选择会员类别"
            clearable
          >
            <el-option label="黄金卡" value="黄金卡">黄金卡</el-option>
            <el-option label="白银卡" value="白银卡">白银卡</el-option>
            <el-option label="普通会员" value="普通会员">普通会员</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input
            v-model="memberForm.phone"
            placeholder="请输入电话"
            clearable
          />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="memberForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker
            v-model="memberForm.birthday"
            type="date"
            placeholder="选择生日"
            value-format="yyyy-MM-dd"
            clearable
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="memberForm.status" active-color="#13ce66" inactive-color="#ff4949" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addMemberDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddMember">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MemberManagement',
  data() {
    return {
      // 筛选表单
      searchForm: {
        keyword: '',
        store: ''
      },
      // 会员列表数据
      tableData: [
        {
          memberNo: '00001',
          name: '李四',
          memberType: '黄金卡',
          discount: 9.0,
          consumeTimes: 10,
          consumeAmount: '¥ 5000',
          balance: '¥ 1000',
          store: '大悦城店',
          phone: '18811111111',
          createDate: '2020-04-07',
          status: '正常'
        }
        // 可继续补充更多模拟数据...
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 100
      },
      // 新增会员弹窗
      addMemberDialogVisible: false,
      memberForm: {
        memberNo: '',
        name: '',
        store: '',
        memberType: '',
        phone: '',
        gender: '男',
        birthday: '',
        status: true // switch 组件默认开启（正常）
      },
      // 批量选择的会员
      selectedMembers: []
    }
  },
  methods: {
    // 查询按钮
    handleQuery() {
      console.log('查询条件:', this.searchForm);
      // 模拟筛选逻辑，可根据 searchForm 过滤 tableData
    },
    // 表格多选事件
    handleSelectionChange(rows) {
      this.selectedMembers = rows;
    },
    // 分页 - 每页条数变化
    handleSizeChange(val) {
      this.pagination.size = val;
    },
    // 分页 - 当前页变化
    handleCurrentChange(val) {
      this.pagination.page = val;
    },
    // 打开新增会员弹窗
    openAddMemberDialog() {
      this.memberForm = {
        memberNo: '',
        name: '',
        store: '',
        memberType: '',
        phone: '',
        gender: '男',
        birthday: '',
        status: true
      };
      this.addMemberDialogVisible = true;
    },
    // 提交新增会员
    handleAddMember() {
      // 简单校验
      if (!this.memberForm.memberNo) {
        this.$message.warning('请输入会员号');
        return;
      }
      if (!this.memberForm.name) {
        this.$message.warning('请输入姓名');
        return;
      }

      // 模拟新增逻辑
      this.tableData.unshift({
       ...this.memberForm,
        consumeTimes: 0,
        consumeAmount: '¥ 0',
        balance: '¥ 0',
        createDate: new Date().toLocaleDateString()
      });
      this.pagination.total++;
      this.addMemberDialogVisible = false;
      this.$message.success('会员新增成功');
    },
    // 编辑会员（可扩展弹窗逻辑）
    handleEdit(row) {
      this.$message.info('点击了编辑，可扩展弹窗修改会员信息');
    },
    // 消费记录
    handleConsumeRecord(row) {
      this.$message.info('点击了消费记录，可跳转详情页或弹窗展示');
    },
    // 积分记录
    handlePointRecord(row) {
      this.$message.info('点击了积分记录，可跳转详情页或弹窗展示');
    },
    // 充值记录
    handleRechargeRecord(row) {
      this.$message.info('点击了充值记录，可跳转详情页或弹窗展示');
    },
    // 删除会员
    handleDelete(row) {
      this.$confirm(`确定删除会员【${row.name}】吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        const index = this.tableData.findIndex(item => item.memberNo === row.memberNo);
        if (index!== -1) {
          this.tableData.splice(index, 1);
          this.pagination.total--;
        }
        this.$message.success('会员删除成功');
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    // 批量删除
    handleBatchDelete() {
      if (this.selectedMembers.length === 0) {
        this.$message.warning('请至少选择1个会员');
        return;
      }
      this.$confirm('确定删除选中的会员吗？', '提示', {
        type: 'warning'
      }).then(() => {
        // 过滤掉选中的会员
        this.tableData = this.tableData.filter(item =>!this.selectedMembers.some(m => m.memberNo === item.memberNo));
        this.pagination.total = this.tableData.length;
        this.$message.success('批量删除成功');
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    }
  }
}
</script>

<style scoped>
.member-management-page {
  padding: 20px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
.dialog-footer {
  text-align: right;
}
</style>