<template>
  <div class="store-management">
    <!-- 标题 -->
    <el-page-header
      content="门店管理"
      style="margin-bottom: 20px"
    ></el-page-header>

    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="门店">
        <el-input v-model="searchForm.shopOrder" placeholder="请输入门店编号" />
      </el-form-item>
      <el-form-item label="门店状态">
        <el-select v-model="searchForm.state" placeholder="请选择门店状态">
          <el-option label="全部" value="" />
          <el-option label="正常" :value="0" />
          <el-option label="关闭" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据列表 + 操作 -->
    <el-card shadow="hover" style="margin-bottom: 20px">
      <template #header>
        <div class="card-header">
          <span>数据列表</span>
          <el-button type="primary" icon="el-icon-plus" @click="openAddDialog"
            >新增门店</el-button
          >
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="shopOrder" label="ID" width="80" />
        <el-table-column prop="name" label="门店名称" width="120" />
        <el-table-column prop="address" label="地址" width="400" />
        <el-table-column prop="phone" label="电话" width="120" />
        <el-table-column prop="createDate" label="创建日期" width="120" />
        <el-table-column prop="peopleNum" label="人数" width="80" />
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.state === 0 ? 'success' : 'danger'">
              {{ scope.row.state === 0 ? "正常" : "关闭" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="服务类型" width="150">
          <template #default="scope">
            <div>
              <el-tag v-if="scope.row.isDineIn === 0" size="small">堂食</el-tag>
              <el-tag v-if="scope.row.isTakeOut === 0" size="small" type="info"
                >外卖</el-tag
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column label="营业时间" width="180">
          <template #default="scope">
            {{ scope.row.openingHoursStart }} - {{ scope.row.openingHoursEnd }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditDialog(scope.row)"
              >编辑</el-button
            >
            <el-button
              type="text"
              icon="el-icon-delete"
              text-color="red"
              @click="handleDelete(scope.row)"
            >
              删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页部分 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 15px; text-align: right"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="dialogType === 'add' ? '新增门店' : '编辑门店'"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <el-form
        :model="form"
        :rules="formRules"
        ref="formRef"
        label-width="80px"
      >
        <el-col :span="12">
          <el-form-item label="门店编号" prop="shopOrder">
            <el-input
              v-model="form.shopOrder"
              placeholder="请输入门店编号(如001)"
            />
          </el-form-item>
          <el-form-item label="门店名称" prop="shopOrder">
            <el-input
              v-model="form.name"
              placeholder="请输入门店名称"
            />
          </el-form-item>
          <el-form-item label="所在省份" prop="provinceId">
            <el-select
              v-model="form.provinceId"
              placeholder="请选择省份"
              @change="handleProvinceChange('form')"
            >
              <el-option
                v-for="province in provinceOptions"
                :key="province.provinceId"
                :label="province.name"
                :value="province.provinceId"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="所在城市" prop="areaId">
            <el-select
              v-model="form.areaId"
              placeholder="请选择城市"
              :disabled="!form.provinceId"
            >
              <el-option
                v-for="area in formAreaOptions"
                :key="area.areaId"
                :label="area.name"
                :value="area.areaId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input v-model="form.address" placeholder="请输入详细地址" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="门店状态" prop="state">
            <el-select v-model="form.state" placeholder="请选择门店状态">
              <el-option label="正常" :value="0" />
              <el-option label="关闭" :value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11" :offset="1">
          <el-form-item label="门店人数" prop="peopleNum">
            <el-input
              v-model.number="form.peopleNum"
              placeholder="请输入门店人数"
              type="number"
            />
          </el-form-item>
          <el-form-item label="提供堂食" prop="isDineIn">
            <el-select v-model="form.isDineIn" placeholder="请选择">
              <el-option label="是" :value="0" />
              <el-option label="否" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item
            label="提供外卖/自提"
            prop="isTakeOut"
            label-width="120"
          >
            <el-select v-model="form.isTakeOut" placeholder="请选择">
              <el-option label="是" :value="0" />
              <el-option label="否" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.isTakeOut == 0">
            <el-button type="primary" @click="editDelivery"
              >外卖/自提设置</el-button
            >
          </el-form-item>
          <el-form-item
            label="营业开始时间"
            prop="openingHoursStart"
            label-width="120"
          >
            <el-time-picker
              v-model="form.openingHoursStart"
              placeholder="选择开始时间"
              value-format="HH:mm:ss"
            ></el-time-picker>
          </el-form-item>
          <el-form-item
            label="营业结束时间"
            prop="openingHoursEnd"
            label-width="120"
          >
            <el-time-picker
              v-model="form.openingHoursEnd"
              placeholder="选择结束时间"
              value-format="HH:mm:ss"
            ></el-time-picker>
          </el-form-item>
        </el-col>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 外卖编辑 -->
    <el-dialog
      title="外卖/堂食配置"
      :visible.sync="deliveryDialogVisible"
      width="40%"
    >
      <el-form :model="deliveryInfo" label-width="80px">
        <el-form-item label="店铺名称">
          {{ form.name }}
        </el-form-item>
        <el-form-item label="外卖/自提时间" label-width="120">
          <el-time-picker
            v-model="deliveryInfo.deliveryTimeStart"
            placeholder="选择开始时间"
            value-format="HH:mm:ss"
          ></el-time-picker>
          -
          <el-time-picker
            v-model="deliveryInfo.deliveryTimeEnd"
            placeholder="选择开始时间"
            value-format="HH:mm:ss"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="满免配送">
          <el-col :span="5">
            <el-radio-group v-model="deliveryInfo.isFullFreeDelivery">
              <el-radio-button :label="1">是</el-radio-button>
              <el-radio-button :label="2">否</el-radio-button>
            </el-radio-group>
          </el-col>
          <el-col :span="14">
            <el-input
              placeholder="满免配送金额条件"
              v-model="deliveryInfo.fullFreeDeliveryPrice"
              type="number"
              min="0"
            ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="配收费方式" label-width="120">
          <el-radio-group v-model="deliveryInfo.paymentMethod">
            <el-radio-button :label="0">固定区域收费</el-radio-button>
            <el-radio-button :label="1">按距离阶梯收费</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="固定费用设置"
          v-if="deliveryInfo.paymentMethod == 0"
          label-width="200"
        >
          <el-input
            placeholder="设置配送固定费用"
            v-model="deliveryInfo.areaDeliveryPrice"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="设计阶梯收费标准"
          v-if="deliveryInfo.paymentMethod == 1"
          label-width="240"
        >
          <el-input
            style="width: 80px"
            type="number"
            min="0"
            v-model="deliveryInfo.distanceStartNum"
            placeholder="初始公里数"
          ></el-input
          >km距离内收费
          <el-input
            style="width: 80px"
            type="number"
            min="0"
            v-model="deliveryInfo.distanceStartPrice"
            placeholder="初始公里收费价格"
          ></el-input>
          元，超过改距离每<el-input
            style="width: 80px"
            type="number"
            min="0"
            v-model="deliveryInfo.distanceAddNum"
            placeholder="公里数"
          ></el-input
          >收费
          <el-input
            style="width: 80px"
            type="number"
            min="0"
            v-model="deliveryInfo.distanceAddPrice"
            placeholder="每次增加的费用"
          ></el-input
          >元
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deliveryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateDelivery">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";
export default {
  name: "StoreManagement",
  data() {
    return {
      // 搜索表单
      searchForm: {
        shopOrder: "",
        state: "",
        provinceId: "",
        areaId: "",
      },
      // 表格数据
      tableData: [],
      // 分页
      page: 1,
      pageSize: 10,
      total: 0,
      // 弹窗
      dialogVisible: false,
      dialogType: "add",
      // 表单数据
      form: {
        shopId: "",
        shopOrder: "",
        provinceId: "",
        areaId: "",
        address: "",
        phone: "",
        state: 0,
        peopleNum: 0,
        isDineIn: 0,
        isTakeOut: 0,
        openingHoursStart: "",
        openingHoursEnd: "",
      },
      // 表单验证规则
      formRules: {
        shopOrder: [
          { required: true, message: "请输入门店编号", trigger: "blur" },
        ],
        provinceId: [
          { required: true, message: "请选择省份", trigger: "change" },
        ],
        areaId: [{ required: true, message: "请选择城市", trigger: "change" }],
        address: [
          { required: true, message: "请输入详细地址", trigger: "blur" },
        ],
        phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
        openingHoursStart: [
          { required: true, message: "请选择营业开始时间", trigger: "change" },
        ],
        openingHoursEnd: [
          { required: true, message: "请选择营业结束时间", trigger: "change" },
        ],
      },
      // 省份数据
      provinceOptions: [],
      // 城市数据缓存 - 以省份ID为key
      cityDataCache: {},
      // 表单的城市数据
      formAreaOptions: [],
      deliveryDialogVisible: false,
      deliveryInfo: {
        shopId: "",
        deliveryTimeStart: "",
        deliveryTimeEnd: "",
        isFullFreeDelivery: 1,
        fullFreeDeliveryPrice: "",
        paymentMethod: 0,
        area: "",
        areaDeliveryPrice: 0,
        distanceStartNum: 0,
        distanceStartPrice: 0,
        distanceAddNum: 0,
        distanceAddPrice: 0,
      },
    };
  },
  methods: {
    // 开启对于外卖的配置
    updateDelivery() {
      // 外卖的开始时间不能早于营业开始时间
      if (this.deliveryInfo.deliveryTimeStart < this.form.openingHoursStart) {
        this.$message.error("外卖开始时间不能早于营业开始时间");
        return;
      }
      // 外卖的结束时间不能晚于营业的结束时间
      if (this.deliveryInfo.deliveryTimeEnd > this.form.openingHoursEnd) {
        this.$message.error("外卖结束时间不能晚于营业结束时间");
        return;
      }
      this.deliveryInfo.shopId = this.form.shopId;
      api.delivery.saveOrUpdate(this.deliveryInfo).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("配置成功");
          this.deliveryDialogVisible = false;
        } else {
          this.$message.error("配置失败");
        }
      });
    },
    editDelivery() {
      if (this.dialogType == "add") {
        this.deliveryInfo = {
          shopId: "",
          deliveryTimeStart: "",
          deliveryTimeEnd: "",
          isFullFreeDelivery: 1,
          fullFreeDeliveryPrice: "",
          paymentMethod: 0,
          area: "",
          areaDeliveryPrice: 0,
          distanceStartNum: 0,
          distanceStartPrice: 0,
          distanceAddNum: 0,
          distanceAddPrice: 0,
        };
        this.deliveryDialogVisible = true;
      } else {
        api.delivery.infoById(this.form.shopId).then((res) => {
          if (res.data.code != 200 || !res.data.data) {
            this.deliveryInfo = {
              shopId: "",
              deliveryTimeStart: "",
              deliveryTimeEnd: "",
              isFullFreeDelivery: 1,
              fullFreeDeliveryPrice: "",
              paymentMethod: 0,
              area: "",
              areaDeliveryPrice: 0,
              distanceStartNum: 0,
              distanceStartPrice: 0,
              distanceAddNum: 0,
              distanceAddPrice: 0,
            };
          } else {
            this.deliveryInfo = res.data.data;
          }
          this.deliveryDialogVisible = true;
        });
      }
    },
    // 根据省份ID获取省份名称
    getProvinceName(provinceId) {
      if (!provinceId) return "";
      const province = this.provinceOptions.find(
        (item) => item.provinceId === provinceId
      );
      return province ? province.name : "未知省份";
    },

    // 根据省份ID和城市ID获取城市名称
    getCityName(provinceId, areaId) {
      if (!provinceId || !areaId) return "";
      const cityList = this.cityDataCache[provinceId] || [];
      const city = cityList.find((item) => item.areaId === areaId);
      return city ? city.name : "未知城市";
    },

    // 查询
    handleSearch() {
      // 适配API参数格式，添加搜索条件
      api.shop
        .pageList(
          this.page,
          this.pageSize,
          this.searchForm.shopOrder || undefined,
          this.searchForm.state || undefined
        )
        .then((res) => {
          if (res.data.code === 200) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;

            // 预加载所需的城市数据
            this.tableData.forEach((row) => {
              if (row.provinceId && !this.cityDataCache[row.provinceId]) {
                this.fetchCityList(row.provinceId);
              }
            });
          } else {
            this.$message.error(res.data.msg || "获取数据失败");
          }
        })
        .catch((err) => {
          console.error("获取门店列表失败", err);
          this.$message.error("获取门店列表失败");
        });
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        shopOrder: "",
        state: "",
        provinceId: "",
        areaId: "",
      };
    },

    // 省份变更处理
    handleProvinceChange() {
      api.area.listById(this.form.provinceId).then((res) => {
        if (res.data.code == 200) {
          this.formAreaOptions = res.data.data;
        }
      });
    },

    // 新增弹窗
    openAddDialog() {
      this.dialogType = "add";
      this.dialogVisible = true;
      // 重置表单
      this.$nextTick(() => {
        this.$refs.formRef.resetFields();
      });
      this.form = {
        naem:'',
        shopOrder: "",
        provinceId: "",
        areaId: "",
        address: "",
        phone: "",
        state: 0,
        peopleNum: 0,
        isDineIn: 0,
        isTakeOut: 0,
        openingHoursStart: "",
        openingHoursEnd: "",
      };
      this.formAreaOptions = [];
    },

    // 编辑弹窗
    openEditDialog(row) {
      this.dialogType = "edit";
      this.dialogVisible = true;

      // 加载对应城市数据
      api.area.infoByAreaId(row.areaId).then((res) => {
        if (res.data.code == 200) {
          row.provinceId = res.data.data.provinceId;
          api.area.listById(row.provinceId).then((res) => {
            if (res.data.code == 200) {
              this.formAreaOptions = res.data.data;
            }
            // 复制行数据到表单
            this.form = { ...row };
          });
        }
      });
    },

    // 删除门店
    handleDelete(row) {
      this.$confirm("确定要删除该门店吗？删除后数据不可恢复。", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          api.shop
            .delete(row.shopId)
            .then((res) => {
              if (res.data.code === 200) {
                this.$message.success("删除成功");
                this.handleSearch(); // 重新加载列表
              } else {
                this.$message.error(res.data.msg || "删除失败");
              }
            })
            .catch((err) => {
              console.error("删除门店失败", err);
              this.$message.error("删除门店失败");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    // 分页处理
    handleSizeChange(val) {
      this.pageSize = val;
      this.page = 1; // 重置为第一页
      this.handleSearch();
    },

    handleCurrentChange(val) {
      this.page = val;
      this.handleSearch();
    },

    // 新增/编辑确认
    handleConfirm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) {
          return false;
        }

        try {
          if (this.dialogType === "add") {
            const res = await api.shop.add(this.form);
            if (res.data.code === 200) {
              this.$message.success("新增成功");
              this.dialogVisible = false;
              this.handleSearch();
            } else {
              this.$message.error(res.data.msg || "新增失败");
            }
          } else {
            const res = await api.shop.update(this.form);
            if (res.data.code === 200) {
              this.$message.success("编辑成功");
              this.dialogVisible = false;
              this.handleSearch();
            } else {
              this.$message.error(res.data.msg || "编辑失败");
            }
          }
        } catch (err) {
          console.error(
            this.dialogType === "add" ? "新增门店失败" : "编辑门店失败",
            err
          );
          this.$message.error(
            this.dialogType === "add" ? "新增门店失败" : "编辑门店失败"
          );
        }
      });
    },
  },
  async mounted() {
    // 先获取省份数据
    api.province.list().then((res) => {
      if (res.data.code == 200) {
        this.provinceOptions = res.data.data;
      }
    });
    // 再获取门店列表
    this.handleSearch();
  },
};
</script>

<style scoped>
.store-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  background-color: white;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

::v-deep .el-form-item {
  margin-right: 15px;
}

.dialog-footer {
  text-align: right;
}

/* 表格样式优化 */
::v-deep .el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
}

::v-deep .el-table tr:hover > td {
  background-color: #f9fafc !important;
}
</style>
