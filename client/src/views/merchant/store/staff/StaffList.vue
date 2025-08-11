<template>
  <div class="tab-content">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="员工">
        <el-input
          v-model="searchForm.num"
          placeholder="请输入姓名/编号/联系方式"
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
      <el-form-item label="状态">
        <el-select v-model="searchForm.state" placeholder="请选择员工状态">
          <el-option label="全部" value="" />
          <el-option label="在职" :value="0" />
          <el-option label="离职" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 + 操作 -->
    <el-card shadow="hover" style="margin-bottom: 20px">
      <template #header>
        <div class="card-header">
          <span>数据列表</span>
          <el-button
            type="primary"
            icon="el-icon-plus"
            @click="openAddStaffDialog"
          >
            新增员工
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="jobNum" label="工号" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="联系方式" width="120" />
        <el-table-column prop="shopName" label="所属门店" />
        <el-table-column prop="createTime" label="入职日期" width="240" />
        <el-table-column label="岗位名称">
          <template #default="scope">
            <el-tag>{{ staffType(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="account" label="登录账号" width="120" />
        <el-table-column label="状态">
          <template #default="scope">
            <el-tag v-if="scope.row.state === 0" type="success">在职</el-tag>
            <el-tag v-else type="warning"> 离职 </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="openViewDialog(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditDialog(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              text-color="red"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @current-change="handleSearch"
        :current-page="page"
        :page-size="szie"
        :total="total"
        style="margin-top: 15px; text-align: right"
      />
    </el-card>

    <!-- 新增/编辑员工弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑员工' : '新增员工'"
      :visible.sync="dialogVisible"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="工号">
          <el-input v-model="form.jobNum" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="员工姓名">
          <el-input v-model="form.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" placeholder="请输入" />
        </el-form-item>
        <!-- <el-form-item label="家庭住址">
          <el-input v-model="form.address" placeholder="请输入" />
        </el-form-item> -->
        <el-form-item label="所属门店">
          <el-select v-model="form.shopId" placeholder="请选择">
            <el-option
              v-for="(shop, index) in shopOptions"
              :key="index"
              :label="shop.name"
              :value="shop.shopId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="岗位名称">
          <!-- 职务(0:店长、1:收银、2:主厨、3:保洁、4:服务员、5:主厨助理、6:切菜员、7:传菜员) -->
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="店长" :value="0"></el-option>
            <el-option label="收银" :value="1"></el-option>
            <el-option label="主厨" :value="2"></el-option>
            <el-option label="保洁" :value="3"></el-option>
            <el-option label="服务员" :value="4"></el-option>
            <el-option label="主厨助理" :value="5"></el-option>
            <el-option label="切菜员" :value="6"></el-option>
            <el-option label="传菜员" :value="7"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账户权限">
          <!-- 账号对应的权限，比如店长就可以登录多个端(0:后厨、1:服务、2:店长、3:收银) -->
          <el-select v-model="form.perssion" placeholder="请选择">
            <el-option label="后厨" :value="0"></el-option>
            <el-option label="服务" :value="1"></el-option>
            <el-option label="店长" :value="2"></el-option>
            <el-option label="收银" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="登录账号">
          <el-input v-model="form.account" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="登录密码">
          <el-input v-model="form.password" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="是否在职">
          <el-select v-model="form.state" placeholder="请选择">
            <el-option label="在职" :value="0"></el-option>
            <el-option label="离职" :value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看员工详情弹窗 -->
    <el-dialog title="员工详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions column="1" border>
        <el-descriptions-item label="工号">{{
          form.jobNum
        }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{
          form.name
        }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{
          form.phone
        }}</el-descriptions-item>
        <el-descriptions-item label="所属门店">{{
          form.shopName
        }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{
          form.createTime
        }}</el-descriptions-item>
        <!-- 职务(0:店长、1:收银、2:主厨、3:保洁、4:服务员、5:主厨助理、6:切菜员、7:传菜员) -->
        <el-descriptions-item label="岗位名称">
          {{
            form.type === 0
              ? "店长"
              : form.type === 1
              ? "收银"
              : form.type === 2
              ? "主厨"
              : form.type === 3
              ? "保洁"
              : form.type === 4
              ? "服务员"
              : form.type === 5
              ? "主厨助理"
              : form.type === 6
              ? "切菜员"
              : form.type === 7
              ? "传菜员"
              : "未知岗位"
          }}
        </el-descriptions-item>

        <!-- 账号对应的权限，比如店长就可以登录多个端(0:后厨、1:服务、2:店长、3:收银) -->
        <el-descriptions-item label="账户权限">
          {{
            form.perssion === 0
              ? "后厨"
              : form.perssion === 1
              ? "服务"
              : form.perssion === 2
              ? "店长"
              : form.perssion === 3
              ? "收银"
              : "未知权限"
          }}
        </el-descriptions-item>
        <el-descriptions-item label="登录账号">{{
          form.account
        }}</el-descriptions-item>
        <el-descriptions-item label="登录密码">{{
          form.password
        }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{
          form.state === 0 ? "在职" : "离职"
        }}</el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "StaffList",
  data() {
    return {
      shopOptions: [],
      // 搜索表单数据
      searchForm: {
        num: "",
        shopId: "",
        state: "",
      },

      // 表格数据
      tableData: [],

      // 分页数据
      page: 1,
      szie: 10,
      total: 0,

      // 弹窗状态
      dialogVisible: false,
      viewDialogVisible: false,
      isEdit: false,

      // 表单数据
      form: {
        staffId: "",
        name: "",
        account: "",
        phone: "",
        password: "",
        perssion: "",
        jobNum: "",
        type: "",
        shopId: "",
        state: 0,
      },
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
    // 查询
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.page = page;
      api.staff
        .pageList(
          this.page,
          this.size,
          this.searchForm.num,
          this.searchForm.shopId,
          this.searchForm.state
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
          } else {
            this.$message.error("查询失败");
          }
        });
    },

    // 新增员工弹窗
    openAddStaffDialog() {
      this.isEdit = false;
      this.dialogVisible = true;
      // 重置表单
      this.form = {
        staffId: "",
        name: "",
        account: "",
        phone: "",
        password: "",
        perssion: "",
        jobNum: "",
        type: "",
        shopId: "",
        state: 0,
      };
    },

    // 查看员工详情
    openViewDialog(row) {
      this.viewDialogVisible = true;
      this.form = { ...row };
    },

    // 编辑弹窗
    openEditDialog(row) {
      this.isEdit = true;
      this.dialogVisible = true;
      this.form = { ...row };
    },

    // 删除
    handleDelete(row) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        type: "warning",
      })
        .then(() => {
          // 实际调用接口删除
          api.staff.delete(row.staffId).then((res) => {
            if (res.data.code == 200) {
              this.$message.success(res.data.msg);
              this.handleSearch(this.page);
            } else {
              this.$message.error(res.data.msg);
            }
          });
          const index = this.tableData.findIndex(
            (item) => item.staffId === row.staffId
          );
          if (index !== -1) {
            this.tableData.splice(index, 1);
            this.total--;
            this.$message({ type: "success", message: "删除成功!" });
          }
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },

    // 表单确认
    handleConfirm() {
      // 验证工号
      if (!this.form.jobNum) {
        this.$message.warning("请输入工号");
        return;
      }

      // 验证员工姓名
      if (!this.form.name) {
        this.$message.warning("请输入员工姓名");
        return;
      }

      // 验证联系方式（简单手机号格式验证）
      if (!this.form.phone) {
        this.$message.warning("请输入联系方式");
        return;
      } else if (!/^1[3-9]\d{9}$/.test(this.form.phone)) {
        this.$message.warning("请输入有效的手机号码");
        return;
      }

      // 验证所属门店
      if (!this.form.shopId) {
        this.$message.warning("请选择所属门店");
        return;
      }

      // 验证岗位名称
      if (this.form.type === undefined || this.form.type === null) {
        this.$message.warning("请选择岗位名称");
        return;
      }

      // 验证账户权限
      if (this.form.perssion === undefined || this.form.perssion === null) {
        this.$message.warning("请选择账户权限");
        return;
      }

      // 验证登录账号
      if (!this.form.account) {
        this.$message.warning("请输入登录账号");
        return;
      }

      // 验证登录密码
      if (!this.form.password) {
        this.$message.warning("请输入登录密码");
        return;
      } else if (this.form.password.length < 6) {
        this.$message.warning("密码长度不能少于6位");
        return;
      }

      // 验证是否在职状态
      if (this.form.state === undefined || this.form.state === null) {
        this.$message.warning("请选择是否在职");
        return;
      }

      // 实际调用接口保存
      if (this.isEdit) {
        api.staff.update(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg);
          } else {
            this.$message.error(res.data.msg);
          }
          this.handleSearch(this.page);
        });
      } else {
        api.staff.add(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg);
          } else {
            this.$message.error(res.data.msg);
          }
          this.handleSearch();
        });
      }
      this.dialogVisible = false;
    },
  },
  mounted() {
    this.handleSearch();
    api.shop.simpleList().then((res) => {
      if (res.data.code == 200) {
        this.shopOptions = res.data.data;
      }
    });
  },
};
</script>

<style scoped>
.tab-content {
  margin-top: 20px;
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.upload-demo {
  margin-top: 10px;
}

/* 身份证预览样式 */
.id-card-preview {
  margin-top: 15px;
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.id-card-item {
  position: relative;
  width: 200px;
  height: 140px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.id-card-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.id-card-item .el-button {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 2px 5px;
}

/* 查看详情图片样式 */
.view-img-item {
  margin-top: 10px;
}

.view-img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
}
</style>
