<template>
  <div class="ingredient-list">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="原料名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入原料名称"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="所属类别">
          <el-select
            v-model="searchForm.rawMaterialTypeId"
            placeholder="请选择原料类别"
            clearable
          >
          <el-option label="全部" value=""></el-option>
            <el-option
              v-for="(rawMaterialType, index) in rawMaterialTypeOptions"
              :key="index"
              :label="rawMaterialType.name"
              :value="rawMaterialType.rawMaterialTypeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog"
        >新增原料</el-button
      >
    </div>

    <!-- 原料表格 -->
    <el-table
      :data="ingredientList"
      border
      style="width: 100%; margin-top: 15px"
      stripe
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="num" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="原料名称"></el-table-column>
      <el-table-column prop="inventory" label="库存数量"></el-table-column>
      <el-table-column prop="warningMin" label="库存过低预警"></el-table-column>
      <el-table-column prop="warningMax" label="库存过高预警"></el-table-column>
      <el-table-column label="所属类别">
        <template #default="scope">
          {{ rawMaterialTypeOptions.find(item=>item.rawMaterialTypeId==scope.row.rawMaterialTypeId).name }}
        </template>
      </el-table-column>
      <el-table-column label="所属门店">
        <template #default="scope">
          {{ shopOptions.find(item=>item.shopId==scope.row.shopId).name }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button type="text" @click="openEditDialog(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="text"
            @click="handleDelete(scope.row)"
            style="color: #f56c6c"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @current-change="handleSearch"
      :current-page="pagination.pageNum"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right"
    ></el-pagination>

    <!-- 新增/编辑原料弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑原料' : '新增原料'"
      :visible.sync="dialogVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="form" label-width="120px">
        <el-form-item
          label="原料名称"
          prop="name"
          :rules="{
            required: true,
            message: '请输入原料名称',
            trigger: 'blur',
          }"
        >
          <el-input v-model="form.name" placeholder="请输入原料名称"></el-input>
        </el-form-item>
        <el-form-item label="序号" required>
          <el-input
            v-model="form.num"
            placeholder="数字越小越靠前 （0001）"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="所属类别"
          prop="rawMaterialTypeId" required
        >
          <el-select
            v-model="form.rawMaterialTypeId"
            placeholder="请选择原料类别"
          >
            <el-option
              v-for="(rawMaterialType, index) in rawMaterialTypeOptions"
              :key="index"
              :label="rawMaterialType.name"
              :value="rawMaterialType.rawMaterialTypeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="所属门店"
          prop="shopId"
          :rules="{
            required: true,
            message: '请选择所属门店',
            trigger: 'change',
          }"
        >
          <el-select v-model="form.shopId" placeholder="请选择所属门店">
            <el-option
              v-for="(shop, index) in shopOptions"
              :key="index"
              :label="shop.name"
              :value="shop.shopId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="库存数量" required>
          <el-input
            v-model="form.inventory"
            placeholder="请输入库存数量"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="库存过低预警" required>
          <el-input
            v-model="form.warningMin"
            placeholder="请输入库存过低预警"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="库存过高预警" required>
          <el-input
            v-model="form.warningMax"
            placeholder="请输库存过高预警"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            placeholder="请输入备注信息"
            type="textarea"
            rows="3"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveIngredient">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "IngredientList",
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: "",
        rawMaterialTypeId: "",
      },

      // 原料列表数据
      ingredientList: [],

      // 原料类别列表
      rawMaterialTypeOptions: [],
      shopOptions: [],

      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 10,
      },

      // 弹窗状态
      dialogVisible: false,
      isEdit: false,

      // 表单数据
      form: {
        rawMaterialId: "",
        rawMaterialTypeId: "",
        name: "",
        shopId: "",
        num: "",
        remark: "",
        inventory: "",
        warningMin: 0,
        warningMax: 0,
      },
    };
  },
  methods: {
    // 搜索
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.pagination.pageNum = page;
      api.rawMaterial
        .pageList(
          this.pagination.pageNum,
          this.pagination.pageSize,
          this.searchForm.name,
          this.searchForm.rawMaterialTypeId
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.ingredientList = res.data.data.records;
            this.pagination.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },

    // 打开新增弹窗
    openAddDialog() {
      this.isEdit = false;
      this.form = {
        rawMaterialId: "",
        rawMaterialTypeId: "",
        name: "",
        shopId: "",
        num: "",
        remark: "",
        inventory: "",
        warningMin: 0,
        warningMax: 0,
      };
      this.dialogVisible = true;
    },

    // 打开编辑弹窗
    openEditDialog(row) {
      this.isEdit = true;
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
    },

    // 删除原料
    handleDelete(row) {
      this.$confirm(`确定要删除【${row.name}】吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          api.rawMaterial.delete(row.rawMaterialId).then((res) => {
            if (res.data.code == 200) {
              this.handleSearch();
              this.$message.success("删除成功");
            } else {
              this.$message.error(res.data.msg);
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
        `原料【${row.name}】已${row.status === "1" ? "启用" : "禁用"}`
      );
    },

    // 保存原料
    saveIngredient() {
      if (!this.form.name) {
        this.$message.warning("请输入原料名称");
        return;
      }
      if (!this.form.rawMaterialTypeId) {
        this.$message.warning("请选择原料类别");
        return;
      }

      // 实际项目中调用接口保存
      if (this.isEdit) {
        api.rawMaterial.update(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success("保存成功");
            this.handleSearch();
          } else {
            this.$message.error(res.data.msg);
          }
          this.dialogVisible = false;
        });
      } else {
        api.rawMaterial.add(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success("添加成功");
            this.handleSearch();
          } else {
            this.$message.error(res.data.msg);
          }
          this.dialogVisible = false;
        });
      }
    },
  },
  mounted() {
    api.rawMaterialType.list().then((res) => {
      if (res.data.code == 200) {
        this.rawMaterialTypeOptions = res.data.data;
      }
    });
    api.shop.simpleList().then((res) => {
      if (res.data.code == 200) {
        this.shopOptions = res.data.data;
      }
    });
    this.handleSearch()
  },
};
</script>

<style scoped>
.ingredient-list {
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
