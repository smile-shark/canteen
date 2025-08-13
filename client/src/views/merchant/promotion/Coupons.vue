<template>
  <div class="coupon-management">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="优惠券类别">
        <el-select v-model="searchForm.type" placeholder="请选择" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option
            v-for="(type, index) in typeOptions"
            :key="index"
            :label="type"
            :value="index"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择门店">
        <el-select
          v-model="searchForm.store"
          placeholder="请选择门店"
          clearable
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

    <!-- 数据列表 & 操作 -->
    <div class="table-header">
      <span>数据列表</span>
      <el-button type="warning" @click="openAddCouponDialog"
        >新增优惠券</el-button
      >
    </div>

    <!-- 优惠券表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column
        prop="name"
        label="优惠券名称"
        align="center"
      ></el-table-column>
      <el-table-column label="优惠券类型" align="center">
        <template #default="scope">
          {{ typeOptions.find((item, index) => index == scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column label="优惠度">
        <template #default="scope">
          <span v-if="scope.row.type == 0">
            优惠 ￥ {{ scope.row.price }}
          </span>
          <span v-if="scope.row.type == 1"> {{ scope.row.discount }} 折 </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag
            :type="
              new Date() < new Date(scope.row.endTime)
                ? 'success' 
                : 'danger' 
            "
          >
            {{ new Date() < new Date(scope.row.endTime)? "未过期" : "已过期" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="grantNum"
        label="发放量"
        align="center"
      ></el-table-column>
      <el-table-column label="使用量" align="center">
        <template #default="scope">
          {{ scope.row.useNum }}
        </template></el-table-column
      >
      <el-table-column label="有效期" align="center">
        <template #default="scope">
          {{ scope.row.startTime }}<br />{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button type="text" @click="handleView(scope.row)">查看</el-button>
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            type="text"
            style="color: #f56c6c"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @current-change="handleSearch"
      :current-page="pagination.page"
      :page-size="pagination.size"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right"
    ></el-pagination>

    <!-- 新增/编辑优惠券弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑优惠券' : '新增优惠券'"
      :visible.sync="couponDialogVisible"
      width="600px"
    >
      <el-form :model="couponForm" label-width="120px" ref="couponForm">
        <el-form-item label="优惠券名称" prop="name">
          <el-input
            v-model="couponForm.name"
            placeholder="请输入优惠券名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="优惠券类型" prop="type">
          <el-radio-group v-model="couponForm.type">
            <el-radio
              v-for="(type, index) in typeOptions"
              :key="index"
              :label="index"
              >{{ type }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优惠金额" prop="price" v-if="couponForm.type == 0">
          <el-input
            v-model="couponForm.price"
            placeholder="满减优惠金额"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="折扣数"
          prop="discount"
          v-if="couponForm.type == 1"
        >
          <el-input
            v-model="couponForm.discount"
            placeholder="(0-10)折"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="使用门槛" prop="condition">
          <el-input
            v-model="couponForm.condition"
            placeholder="满xx元可以使用"
          ></el-input>
        </el-form-item>
        <el-form-item label="开始日期" prop="startTime">
          <el-date-picker
            v-model="couponForm.startTime"
            type="datetime"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endTime">
          <el-date-picker
            v-model="couponForm.endTime"
            type="datetime"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="发放总量" prop="grantNum">
          <el-input
            v-model.number="couponForm.grantNum"
            type="number"
            min="1"
          ></el-input>
        </el-form-item>
        <el-form-item label="每人限领" prop="astrictNum">
          <el-input
            v-model.number="couponForm.astrictNum"
            type="number"
            min="1"
          ></el-input>
        </el-form-item>
        <el-form-item label="适用门店" prop="shopId">
          <el-select
            v-model="couponForm.shopId"
            placeholder="请选择门店"
            @change="shopChange"
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
        <el-form-item label="使用范围">
          <el-radio-group v-model="couponForm.useArea">
            <el-radio
              v-for="(value, index) in ['全部菜品', '指定菜品', '指定分类']"
              :key="index"
              :label="index"
              >{{ value }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择菜品" v-if="couponForm.useArea == 1">
          <el-table :data="couponForm.discountCouponAreaCuisines">
            <el-table-column
              prop="num"
              label="菜品编号"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="name"
              label="菜品名称"
              align="center"
            ></el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <div @click="deleteFormCuisine(scope.row)">删除</div>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="选择分类" v-if="couponForm.useArea == 2">
          <el-table :data="couponForm.discountCouponAreaCuisineTypes">
            <el-table-column
              prop="name"
              label="分类名称"
              align="center"
            ></el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <div @click="deleteFormCuisineType(scope.row)">删除</div>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="couponDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCoupon">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看优惠券详情弹窗 -->
    <el-dialog
      title="优惠券详情"
      :visible.sync="detailDialogVisible"
      width="80%"
    >
      <coupon-detail
        :coupon-id="currentCouponId"
        @close="detailDialogVisible = false"
      ></coupon-detail>
    </el-dialog>
  </div>
</template>

<script>
import api, { formatDateTime } from "@/api";
import CouponDetail from "./CouponDetail.vue";

export default {
  name: "CouponManagement",
  components: {
    CouponDetail,
  },
  data() {
    return {
      typeOptions: ["满减券", "折扣券"],
      shopOptions: [],
      cuisineTypeOptions: [],
      // 筛选表单
      searchForm: {
        type: "",
        store: "",
      },
      // 表格数据
      tableData: [],
      // 分页配置
      pagination: {
        page: 1,
        size: 10,
        total: 120,
      },
      // 弹窗控制
      couponDialogVisible: false,
      detailDialogVisible: false,
      isEdit: false,
      currentCouponId: null,
      // 优惠券表单数据
      couponForm: {
        discountCouponId: "",
        startTime: "",
        endTime: "",
        name: "",
        type: 0,
        price: 0,
        discount: 0,
        condition: 0,
        grantNum: 0,
        surplusNum: 0,
        useNum: 0,
        astrictNum: 0,
        shopId: "",
        useArea: 0,
        discountCouponAreaCuisines: [],
        discountCouponAreaCuisineTypes: [],
      },
      // 选中的优惠券
      selectedCoupons: [],
    };
  },
  methods: {
    deleteFormCuisine(row) {
      this.couponForm.discountCouponAreaCuisines =
        this.couponForm.discountCouponAreaCuisines.filter(
          (item) => item.cuisineId != row.cuisineId
        );
    },
    deleteFormCuisineType(row) {
      this.couponForm.discountCouponAreaCuisineTypes =
        this.couponForm.discountCouponAreaCuisineTypes.filter(
          (item) => item.cuisineTypeId != row.cuisineTypeId
        );
    },
    // 查询
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.pagination.page = page;
      api.discountCoupon
        .pageList(
          this.pagination.page,
          this.pagination.size,
          this.searchForm.type,
          this.searchForm.store
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
    // 重置查询条件
    resetSearch() {
      this.searchForm = {
        type: "",
        store: "",
      };
    },
    // 选择变化
    handleSelectionChange(rows) {
      this.selectedCoupons = rows;
    },
    // 打开新增弹窗
    openAddCouponDialog() {
      this.isEdit = false;
      this.couponForm = {
        discountCouponId: "",
        startTime: "",
        endTime: "",
        name: "",
        type: 0,
        price: 0,
        discount: 0,
        condition: 0,
        grantNum: 0,
        surplusNum: 0,
        useNum: 0,
        astrictNum: 0,
        shopId: "",
        useArea: 0,
        discountCouponAreaCuisines: [],
        discountCouponAreaCuisineTypes: [],
      };
      this.shopChange();
      this.couponForm.discountCouponAreaCuisineTypes = JSON.parse(
        JSON.stringify(this.cuisineTypeOptions)
      );
      this.couponDialogVisible = true;
    },
    // 编辑优惠券
    handleEdit(row) {
      this.isEdit = true;
      this.currentCouponId = row.id;
      // 格式化有效期为日期范围数组
      const [start, end] = row.validPeriod.split(" 至 ");

      this.couponForm = {
        name: row.name,
        type: row.type,
        denomination: row.denomination,
        threshold: row.threshold || "",
        validPeriod: [start, end],
        total: row.total,
        store: row.store || "全部门店",
      };
      this.couponDialogVisible = true;
    },
    // 保存优惠券
    saveCoupon() {
      // 简单验证
      if (!this.couponForm.name) {
        this.$message.warning("请输入优惠券名称");
        return;
      }
      if (this.couponForm.type != 0 && this.couponForm.type != 1) {
        this.$message.warning("请选择正确的类型");
        return;
      }
      if (!this.couponForm.grantNum || this.couponForm.grantNum <= 0) {
        this.$message.warning("请输入有效的发放总量");
        return;
      }
      if (this.isEdit) {
        this.$message.success("优惠券编辑成功");
        this.couponDialogVisible = false;
      } else {
        // 新增操作
        // 格式化日期
        // this.couponForm.startTime = formatDateTime(this.couponForm.startTime);
        // this.couponForm.endTime = formatDateTime(this.couponForm.endTime);
        api.discountCoupon.add(this.couponForm).then((res) => {
          if (res.data.code == 200) {
            this.$message.success("优惠券新增成功");
            this.couponDialogVisible = false;
            this.searchForm();
          } else {
            this.$message.error(res.data.msg);
          }
        });
      }
    },
    // 查看详情
    handleView(row) {
      this.currentCouponId = row.id;
      this.detailDialogVisible = true;
    },
    // 删除优惠券
    handleDelete(row) {
      this.$confirm(`确定要删除优惠券【${row.name}】吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const index = this.tableData.findIndex((item) => item.id === row.id);
          if (index !== -1) {
            this.tableData.splice(index, 1);
            this.pagination.total--;
          }
          this.$message.success("删除成功");
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    // 根据门店选择改变时获取对应的菜品
    shopChange() {
      api.cuisine.simpleListByShopId(this.couponForm.shopId).then((res) => {
        if (res.data.code == 200) {
          this.couponForm.discountCouponAreaCuisines = res.data.data;
        }
      });
    },
  },
  created() {
    api.shop.simpleList().then((res) => {
      if (res.data.code == 200) {
        this.shopOptions = res.data.data;
      }
    });
    api.cuisineType.list().then((res) => {
      if (res.data.code == 200) {
        this.cuisineTypeOptions = res.data.data;
      }
    });
  },
  mounted() {
    this.handleSearch();
  },
};
</script>

<style scoped>
.coupon-management {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh-40px);
}

.search-form {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}
</style>
