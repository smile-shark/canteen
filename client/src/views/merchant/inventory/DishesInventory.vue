<template>
  <div class="dish-library">
    <div class="page-header">
      <h2>菜品库管理</h2>
    </div>

    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="菜品名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入菜品名称"
          clearable
          style="width: 200px"
        ></el-input>
      </el-form-item>

      <el-form-item label="菜品类别">
        <el-select
          v-model="searchForm.cuisineTypeId"
          placeholder="请选择类别"
          clearable
          style="width: 180px"
        >
          <el-option label="全部" value=""></el-option>
          <el-option
            v-for="(cuisineType, index) in cuisineTypeOptions"
            :key="index"
            :label="cuisineType.name"
            :value="cuisineType.cuisineTypeId"
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
            v-for="(shop, index) in shopOptions"
            :key="index"
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
      <el-button type="warning" icon="el-icon-refresh" @click="resetSearch"
        >刷新</el-button
      >
    </div>

    <!-- 菜品表格 -->
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
        label="菜品名称"
        align="center"
      ></el-table-column>
      <el-table-column
        label="类别"
        align="center"
      >
      <template slot-scope="scope">
        {{ cuisineTypeOptions.find(item=>scope.row.cuisineTypeId==item.cuisineTypeId).name }}
      </template>
    </el-table-column>
      <el-table-column
        label="售价(元)"
        align="center"
        width="100"
      ><template #default="scope">
        <span v-if="scope.row.isSpecialOffer">
          <span style="font-weight: bold;font-size: 1.2rem;">￥{{ scope.row.SpecialOffer }}</span>
          <span style="text-decoration: line-through;color:gray">￥{{ scope.row.price }}</span>
        </span>
        <span v-else style="font-size: 1.2rem;font-weight: bold;">￥{{ scope.row.price }}</span>
      </template></el-table-column>
      <el-table-column
        label="所属门店"
        align="center"
      ><template #default="scope">
        {{ shopOptions.find(item=>item.shopId==scope.row.shopId).name }}
      </template></el-table-column>
      <el-table-column prop="inventory" label="当前库存" align="center" width="100">
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
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.state === 0 ? 'success' : 'info'">
            {{ scope.row.state === 0 ? "在售" : "停售" }}
          </el-tag>
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
      @current-change="handleSearch"
      :current-page="pagination.pageNum"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right"
    ></el-pagination>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
    v-if="form.name"
      :title="isEdit ? '编辑菜品' : '新增菜品'"
      :visible.sync="dialogVisible"
      width="600px"
      append-to-body
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="菜品名称" prop="name">
          <storng>{{ form.name }}</storng>
        </el-form-item>

        <el-form-item label="菜品类别" prop="cuisineTypeId">
          <storng>{{ cuisineTypeOptions.find(item=>item.cuisineTypeId==form.cuisineTypeId).name }}</storng>
        </el-form-item>

        <el-form-item label="所属门店" prop="shopId">
          <storng>{{ shopOptions.find(item=>item.shopId==form.shopId).name }}</storng>
        </el-form-item>

        <el-form-item label="售价(元)" prop="price">
          <span v-if="form.isSpecialOffer">
          <span style="font-weight: bold;font-size: 1.2rem;">￥{{ form.SpecialOffer }}</span>
          <span style="text-decoration: line-through;color:gray">￥{{ form.price }}</span>
        </span>
        <span v-else style="font-size: 1.2rem;font-weight: bold;">￥{{ form.price }}</span>
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
  name: "DishLibrary",
  data() {
    return {
      cuisineTypeOptions: [],
      shopOptions: [],
      // 搜索表单
      searchForm: {
        name: "",
        cuisineTypeId: "",
        shopId: "",
      },

      // 表格数据
      tableData: [],

      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 50,
      },

      // 选中的行
      selectedRows: [],

      // 弹窗状态
      dialogVisible: false,
      isEdit: false,

      // 表单数据
      form: {
        cuisineId:'',
        name:'',
        num:'',
        price:'',
        cuisineTypeId:'',
        shopId:'',
        state:0,
        isSpecialOffer:0,
        specialOffer:null,
        inventory:0,
        warningMin:0,
        warningMax:0
      },

      // 表单验证规则
      rules: {
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
      api.cuisine
        .inventoryPageList(
          this.pagination.pageNum,
          this.pagination.pageSize,
          this.searchForm.name,
          this.searchForm.cuisineTypeId,
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
        cuisineTypeId: "",
        shopId: "",
      };
      this.handleSearch(1);
    },


    // 导出数据
    exportData() {
      this.$message.success("数据导出成功");
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
      if(!this.form.cuisineId){
        this.$message.error("请选择菜品");  
        return;
      }
      if(!this.form.warningMin){
        this.$message.error("请填写预警库存");
        return;
      }
      if(!this.form.warningMax){
        this.$message.error("请填写超出库存");
        return;
      }
      api.cuisine.update(this.form).then(res=>{
        if(res.data.code==200){
          this.$message.success("修改成功");
          this.handleSearch();
          this.dialogVisible = false;
        }else{
          this.$message.error(res.data.msg);
        }
      })
    },
  },
  mounted() {
    api.cuisineType.list().then((res) => {
      if (res.data.code == 200) {
        this.cuisineTypeOptions = res.data.data;
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
.dish-library {
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
