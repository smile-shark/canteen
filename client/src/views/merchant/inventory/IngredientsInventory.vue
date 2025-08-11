<template>
  <div class="material-library">
    <div class="page-header">
      <h2>原料库管理</h2>
    </div>

    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="原料名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入原料名称"
          clearable
          style="width: 200px"
        ></el-input>
      </el-form-item>

      <el-form-item label="原料类别">
        <el-select
          v-model="searchForm.rawMaterialTypeId"
          placeholder="请选择类别"
          clearable
          style="width: 180px"
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

      <el-form-item label="所属门店">
        <el-select
          v-model="searchForm.shopId"
          placeholder="请选择门店"
          clearable
          style="width: 180px"
        >
          <el-option label="全部" value=""></el-option>
          <el-option
            v-for="shop in shopOptions"
            :key="shop.shopId"
            :label="shop.name"
            :value="shop.shopId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="operation-buttons">
      <el-button type="success" icon="el-icon-download" @click="exportData"
        >导出数据</el-button
      >
      <el-button type="warning" icon="el-icon-refresh" @click="refreshData"
        >刷新</el-button
      >
      <el-button
        type="info"
        icon="el-icon-shopping-cart"
        @click="createPurchaseOrder"
        >生成采购单</el-button
      >
    </div>

    <!-- 原料表格 -->
    <el-table
      :data="tableData"
      border
      stripe
      @selection-change="handleSelectionChange"
      style="width: 100%; margin-top: 15px"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        prop="num"
        label="编号"
        width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="name"
        label="原料名称"
        align="center"
      ></el-table-column>
      <el-table-column label="类别" align="center"
        ><template #default="scope">
          {{
            rawMaterialTypeOptions.find(
              (item) => item.rawMaterialTypeId == scope.row.rawMaterialTypeId
            ).name
          }}
        </template></el-table-column
      >
      <el-table-column prop="shopId" label="所属门店" align="center"
        ><template #default="scope">
          {{ shopOptions.find((item) => item.shopId == scope.row.shopId).name }}
        </template></el-table-column
      >
      <el-table-column
        prop="inventory"
        label="当前库存"
        align="center"
        width="100"
      >
        <template #default="scope">
          <span
            :class="{
              'text-danger': scope.row.inventory <= scope.row.warningMin,
              'text-warning': scope.row.inventory >= scope.row.warningMax,
            }"
          >
            {{ scope.row.inventory }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button type="text" @click="openEditDialog(scope.row)"
            >编辑</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      @current-change="handleSearch"
      :current-page="pagination.pageNum"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right"
    ></el-pagination>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-if="dialogVisible && form.name"
      :title="isEdit ? '编辑原料' : '新增原料'"
      :visible.sync="dialogVisible"
      width="600px"
      append-to-body
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="原料名称" prop="name">
          <strong>{{ form.name }}</strong>
        </el-form-item>

        <el-form-item label="原料类别" prop="rawMaterialTypeId">
          <strong>{{
            rawMaterialTypeOptions.find(
              (item) => item.rawMaterialTypeId == form.rawMaterialTypeId
            ).name
          }}</strong>
        </el-form-item>

        <el-form-item label="所属门店" prop="shopId">
          <strong>{{
            shopOptions.find((item) => item.shopId == form.shopId)?.name
          }}</strong>
        </el-form-item>

        <el-form-item label="当前库存" prop="inventory" required>
          <el-input
            v-model.number="form.inventory"
            placeholder="请输入当前库存"
            type="number"
            min="0"
          ></el-input>
        </el-form-item>

        <el-form-item label="库存过低线" prop="warningMin" required>
          <el-input
            v-model.number="form.warningMin"
            placeholder="库存低于此值将提示"
            type="number"
            min="0"
          ></el-input>
        </el-form-item>

        <el-form-item label="库存超出线" prop="warningMax" required>
          <el-input
            v-model.number="form.warningMax"
            placeholder="库存高于此值将提示"
            type="number"
            min="0"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "MaterialLibrary",
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: "",
        rawMaterialTypeId: "",
        shopId: "",
      },

      // 表格数据
      tableData: [],

      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },

      // 选中的行
      selectedRows: [],

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
        inventory: 0,
        warningMin: 0,
        warningMax: 0,
      },

      // 表单验证规则
      rules: {
        name: [
          { required: true, message: "请输入原料名称", trigger: "blur" },
          {
            min: 2,
            max: 50,
            message: "原料名称长度在 2 到 50 个字符",
            trigger: "blur",
          },
        ],
        rawMaterialTypeId: [
          { required: true, message: "请选择原料类别", trigger: "change" },
        ],
        shopId: [
          { required: true, message: "请选择所属门店", trigger: "change" },
        ],
        unit: [{ required: true, message: "请输入采购单位", trigger: "blur" }],
        purchasePrice: [
          { required: true, message: "请输入采购价", trigger: "blur" },
          {
            type: "number",
            min: 0,
            message: "采购价不能为负数",
            trigger: "blur",
          },
        ],
        inventory: [
          { required: true, message: "请输入当前库存", trigger: "blur" },
          {
            type: "number",
            min: 0,
            message: "库存不能为负数",
            trigger: "blur",
          },
        ],
        warningMin: [
          { required: true, message: "请输入库存过低线", trigger: "blur" },
          { type: "number", min: 0, message: "不能为负数", trigger: "blur" },
        ],
        warningMax: [
          { required: true, message: "请输入库存超出线", trigger: "blur" },
          { type: "number", min: 0, message: "不能为负数", trigger: "blur" },
        ],
        supplier: [
          { required: true, message: "请输入供应商名称", trigger: "blur" },
        ],
      },
      rawMaterialTypeOptions: [],
      shopOptions: [],
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
          this.searchForm.rawMaterialTypeId,
          this.searchForm.shopId
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableData = res.data.data.records;
            this.pagination.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        name: "",
        rawMaterialTypeId: "",
        shopId: "",
      };
      this.refreshData();
    },

    // 刷新数据
    refreshData() {
      // 模拟刷新数据
      this.handleSearch();
    },

    // 导出数据
    exportData() {
      this.$message.success("数据导出成功");
    },

    // 生成采购单
    createPurchaseOrder() {
      if (this.selectedRows.length === 0) {
        this.$message.warning("请先选择需要采购的原料");
        return;
      }
      this.$message.success(
        `已生成采购单，共 ${this.selectedRows.length} 种原料`
      );
    },

    // 选择变化
    handleSelectionChange(rows) {
      this.selectedRows = rows;
    },

    // 打开编辑弹窗
    openEditDialog(row) {
      this.isEdit = true;
      this.form = { ...row };
      this.dialogVisible = true;
    },

    // 提交表单
    submitForm() {
      if (!this.form.rawMaterialId) {
        this.$message.error("请选择原料类别");
        return;
      }
      if (!this.form.shopId) {
        this.$message.error("请选择所属门店");
        return;
      }
      api.rawMaterial.update(this.form).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("修改成功");
          this.handleSearch();
          this.dialogVisible = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
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
    this.handleSearch();
  },
};
</script>

<style scoped>
.material-library {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh - 40px);
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.search-form {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.operation-buttons {
  margin-bottom: 15px;
  display: flex;
  gap: 10px;
}

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

.text-warning {
  color: #e6a23c;
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}
</style>
