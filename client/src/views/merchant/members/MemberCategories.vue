<template>
  <div class="member-category-page">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="会员类别">
        <el-select
          v-model="searchForm.category"
          placeholder="请选择"
          clearable
        >
          <el-option label="全部" value=""></el-option>
          <el-option label="黄金" value="黄金"></el-option>
          <el-option label="钻石" value="钻石"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
        >
          <el-option label="泛海国际店" value="泛海国际店"></el-option>
          <el-option label="大悦城店" value="大悦城店"></el-option>
          <el-option label="西单店" value="西单店"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 & 操作按钮 -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <span>数据列表</span>
      <el-button type="warning" @click="openAddCategoryDialog">新增类别</el-button>
    </div>

    <!-- 会员类别表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="categoryName" label="会员类别" align="center"></el-table-column>
      <el-table-column prop="discount" label="折扣" align="center"></el-table-column>
      <el-table-column prop="birthdayGift" label="生日礼物" align="center"></el-table-column>
      <el-table-column prop="consumeAmount" label="消费金额" align="center"></el-table-column>
      <el-table-column prop="store" label="所属门店" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template #default="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 批量操作 -->
    <div style="margin-top: 10px;">
      <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
      <el-button type="danger" @click="handleBatchDelete" style="margin-left: 10px;">批量删除</el-button>
    </div>

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

    <!-- 新增类别弹窗 -->
    <el-dialog title="新增类别" :visible.sync="addCategoryDialogVisible" width="400px">
      <el-form :model="categoryForm" label-width="80px" ref="categoryForm">
        <el-form-item label="会员类别" prop="categoryName">
          <el-input
            v-model="categoryForm.categoryName"
            placeholder="请输入会员类别"
            clearable
          />
        </el-form-item>
        <el-form-item label="折扣" prop="discount">
          <el-input
            v-model="categoryForm.discount"
            placeholder="请输入折扣"
            clearable
          />
        </el-form-item>
        <el-form-item label="生日礼物" prop="birthdayGift">
          <el-input
            v-model="categoryForm.birthdayGift"
            placeholder="请输入生日礼物（如：10元标准）"
            clearable
          />
        </el-form-item>
        <el-form-item label="消费金额" prop="consumeAmount">
          <el-input
            v-model="categoryForm.consumeAmount"
            placeholder="请输入消费金额（如：¥2000）"
            clearable
          />
        </el-form-item>
        <el-form-item label="门店" prop="store">
          <el-select
            v-model="categoryForm.store"
            placeholder="请选择门店"
            clearable
          >
            <el-option label="泛海国际店" value="泛海国际店"></el-option>
            <el-option label="大悦城店" value="大悦城店"></el-option>
            <el-option label="西单店" value="西单店"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addCategoryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddCategory">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MemberCategory',
  data() {
    return {
      // 筛选表单
      searchForm: {
        category: '',
        store: ''
      },
      // 会员类别列表数据
      tableData: [
        {
          categoryName: '非会员',
          discount: '-',
          birthdayGift: '-',
          consumeAmount: '-',
          store: '泛海国际店'
        },
        {
          categoryName: '青铜会员',
          discount: 9.0,
          birthdayGift: '10元标准',
          consumeAmount: '¥2000',
          store: '大悦城店'
        },
        {
          categoryName: '白银会员',
          discount: 8.5,
          birthdayGift: '20元标准',
          consumeAmount: '¥5000',
          store: '西单店'
        },
        {
          categoryName: '黄金会员',
          discount: 8.0,
          birthdayGift: '50元标准',
          consumeAmount: '¥8000',
          store: '泛海国际店'
        },
        {
          categoryName: '铂金会员',
          discount: 7.5,
          birthdayGift: '80元标准',
          consumeAmount: '¥10000',
          store: '大悦城店'
        },
        {
          categoryName: '钻石会员',
          discount: 7.0,
          birthdayGift: '100元标准',
          consumeAmount: '¥15000',
          store: '西单店'
        }
      ],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 100
      },
      // 新增类别弹窗
      addCategoryDialogVisible: false,
      categoryForm: {
        categoryName: '',
        discount: '',
        birthdayGift: '',
        consumeAmount: '',
        store: ''
      },
      // 批量选择相关
      selectedRows: [],
      selectAll: false
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
      this.selectedRows = rows;
      this.selectAll = rows.length === this.tableData.length;
    },
    // 全选逻辑
    handleSelectAll(val) {
      if (val) {
        this.selectedRows = [...this.tableData];
      } else {
        this.selectedRows = [];
      }
    },
    // 分页 - 每页条数变化
    handleSizeChange(val) {
      this.pagination.size = val;
    },
    // 分页 - 当前页变化
    handleCurrentChange(val) {
      this.pagination.page = val;
    },
    // 打开新增类别弹窗
    openAddCategoryDialog() {
      this.categoryForm = {
        categoryName: '',
        discount: '',
        birthdayGift: '',
        consumeAmount: '',
        store: ''
      };
      this.addCategoryDialogVisible = true;
    },
    // 提交新增类别
    handleAddCategory() {
      // 简单校验
      if (!this.categoryForm.categoryName) {
        this.$message.warning('请输入会员类别');
        return;
      }

      // 模拟新增逻辑
      this.tableData.unshift({...this.categoryForm });
      this.pagination.total++;
      this.addCategoryDialogVisible = false;
      this.$message.success('会员类别新增成功');
    },
    // 编辑类别（可扩展弹窗回显逻辑）
    handleEdit(row) {
      this.categoryForm = {...row };
      this.addCategoryDialogVisible = true;
      this.$message.info('点击了编辑，可在此扩展编辑逻辑');
    },
    // 删除类别
    handleDelete(row) {
      this.$confirm(`确定删除【${row.categoryName}】吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        const index = this.tableData.findIndex(item => item.categoryName === row.categoryName);
        if (index!== -1) {
          this.tableData.splice(index, 1);
          this.pagination.total--;
        }
        this.$message.success('会员类别删除成功');
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    // 批量删除
    handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请至少选择1个会员类别');
        return;
      }
      this.$confirm('确定删除选中的会员类别吗？', '提示', {
        type: 'warning'
      }).then(() => {
        // 过滤掉选中的类别
        this.tableData = this.tableData.filter(item =>!this.selectedRows.some(r => r.categoryName === item.categoryName));
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
.member-category-page {
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