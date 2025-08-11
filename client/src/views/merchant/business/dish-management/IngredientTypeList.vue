<template>
  <div class="category-list">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog"
        >新增原料类别</el-button
      >
    </div>
    <!-- 原料类别表格 -->
    <el-table
      :data="categoryList"
      border
      style="width: 100%; margin-top: 15px"
      stripe
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="num" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="原料类别名称"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button
            type="text"
            @click="handleDelete(scope.row)"
            style="color: #f56c6c"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑原料类别弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑原料类别' : '新增原料类别'"
      :visible.sync="dialogVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="form" label-width="120px">
        <el-form-item
          label="原料类别名称"
          prop="name"
          :rules="{
            required: true,
            message: '请输入原料类别名称',
            trigger: 'blur',
          }"
        >
          <el-input v-model="form.name" placeholder="请输入原料类别名称"></el-input>
        </el-form-item>
        <el-form-item label="原料类别编号">
          <el-input
            placeholder="请输入原料类别编号（001）"
            v-model="form.num"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.description"
            placeholder="请输入原料类别备注"
            type="textarea"
            rows="3"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCategory">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "CategoryList",
  data() {
    return {
      // 原料类别列表数据
      categoryList: [],

      // 弹窗状态
      dialogVisible: false,
      isEdit: false,

      // 表单数据
      form: {
        name: "",
        num: "",
        remark: "",
      },
    };
  },
  methods: {
    // 搜索
    handleSearch() {
      api.rawMaterialType.list().then((res) => {
        if (res.data.code == 200) {
          this.categoryList = res.data.data;
        }
      });
    },
    // 打开新增弹窗
    openAddDialog() {
      this.isEdit = false;
      this.form = {
        name: "",
        num: "",
        remark: "",
      };
      this.dialogVisible = true;
    },
    // 删除原料类别
    handleDelete(row) {
      this.$confirm(`确定要删除【${row.name}】吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          api.rawMaterialType.delete(row.rawMaterialTypeId).then((res) => {
            if (res.data.code == 200) {
              this.$message.success("删除成功");
              this.handleSearch();
            } else {
              this.$message.error(res.data.msg || "删除失败");
            }
          });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    // 状态改变
    handleStatusChange(row) {
      console.log("状态改变:", row);
      this.$message.success(
        `原料类别【${row.name}】已${row.status === "1" ? "启用" : "禁用"}`
      );
    },

    // 保存原料类别
    saveCategory() {
      if (!this.form.name) {
        this.$message.warning("请输入原料类别名称");
        return;
      }

      api.rawMaterialType.add(this.form).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("新增成功");
          this.handleSearch();
          this.dialogVisible = false;
        } else {
          this.$message.error(res.data.msg || "新增失败");
        }
      });
    },
  },
  mounted(){
    this.handleSearch();
  }
};
</script>

<style scoped>
.category-list {
  width: 100%;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.search-form {
  display: flex;
  gap: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
