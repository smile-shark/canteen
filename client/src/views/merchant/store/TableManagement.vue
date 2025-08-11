<template>
  <div class="table-management">
    <!-- 标题 -->
    <el-page-header
      content="台桌管理"
      style="margin-bottom: 20px"
    ></el-page-header>

    <!-- 台桌管理主页（列表） -->
    <div v-if="activeTab === 'list'" class="tab-content">
      <!-- 筛选区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
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
        <el-form-item label="桌型">
          <el-select
            v-model="searchForm.diningTableTypeId"
            placeholder="请选择桌型"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="(diningTableType, index) in diningTableTypeOptions"
              :key="index"
              :label="diningtTableTypeStr(diningTableType)"
              :value="diningTableType.diningTableTypeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用状态">
          <el-select
            v-model="searchForm.userState"
            placeholder="请选择使用状态"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="(item, index) in ['空闲', '待点菜', '预结账', '用餐中']"
              :key="index"
              :label="item"
              :value="index"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="operation-btns">
        <el-button
          type="primary"
          icon="el-icon-plus"
          @click="openTableTypeDialog"
        >
          桌型管理
        </el-button>
        <el-button
          type="success"
          icon="el-icon-plus"
          @click="openNewTableDialog"
        >
          新建台桌
        </el-button>
      </div>

      <!-- 台桌列表 -->
      <el-card shadow="hover" style="margin-top: 20px">
        <el-table :data="tableList" border style="width: 100%">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="serialNumber" label="编号" width="80" />
          <el-table-column prop="shopId" label="所属门店">
            <template #default="scope">
              {{
                shopOptions.find((item) => item.shopId == scope.row.shopId).name
              }}
            </template>
          </el-table-column>
          <el-table-column label="桌型">
            <template #default="scope">
              {{
                diningtTableTypeStr(
                  diningTableTypeOptions.find(
                    (item) =>
                      item.diningTableTypeId == scope.row.diningTableTypeId
                  )
                )
              }}
            </template>
          </el-table-column>
          <el-table-column label="使用状态">
            <template #default="scope">
              {{
                scope.row.userState === 0
                  ? "空闲"
                  : scope.row.userState === 1
                  ? "待点菜"
                  : scope.row.userState === 2
                  ? "预结账"
                  : "用餐中"
              }}
            </template>
          </el-table-column>
          <el-table-column label="启用状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.state === 0 ? 'success' : 'warning'">
                {{ scope.row.state === 0 ? "启用" : "停用" }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button
                type="text"
                icon="el-icon-edit"
                @click="openNewTableDialog(scope.row)"
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
        background
          @current-change="handleSearch"
          :current-page="page"
          :page-size="pageSize"
          :total="total"
          style="margin-top: 15px; text-align: right"
        />
      </el-card>
    </div>

    <!-- 桌型管理弹窗（新增/编辑） -->
    <el-dialog
      :title="tableTypeDialogTitle"
      :visible.sync="tableTypeDialogVisible"
      :fullscreen="true"
    >
      <el-row>
        <el-col
          :span="6"
          v-for="(diningTableType, index) in diningTableTypeOptionsDialog"
          :key="index"
          style="padding: 10px"
        >
          <el-card shadow="hover">
            <el-form :model="diningTableType" label-width="80px">
              <el-form-item label="桌位类型">
                <el-select
                  v-model="diningTableType.type"
                  placeholder="请选择桌位类型"
                >
                  <el-option
                    v-for="(item, index) in ['小桌', '大桌', '包间']"
                    :key="index"
                    :label="item"
                    :value="index"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="最多人数">
                <el-input
                  v-model="diningTableType.max"
                  placeholder="请输入"
                  type="number"
                  min="0"
                />
              </el-form-item>
              <el-form-item label="最少人数">
                <el-input
                  v-model="diningTableType.min"
                  placeholder="请输入"
                  type="number"
                  min="0"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  @click="handleDiningTableType(diningTableType)"
                  >保存</el-button
                >
                <el-button @click="handleDiningTableTypeDelete(diningTableType)"
                  >删除</el-button
                >
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        <el-col :span="6" style="padding: 10px">
          <el-card
            shadow="hover"
            style="
              border: 0.8rem gray dashed;
              height: 260px;
              text-align: center;
              line-height: 260px;
            "
            @click.native="
              diningTableTypeOptionsDialog.push({ type: 0, max: 0, min: 0 })
            "
          >
            <i
              class="el-icon-plus"
              style="font-size: 4rem; font-weight: bold"
            ></i>
          </el-card>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 新建/编辑台桌弹窗 -->
    <el-dialog
      :title="newTableDialogTitle"
      :visible.sync="newTableDialogVisible"
      width="500px"
    >
      <el-form :model="newTableForm" label-width="80px">
        <el-form-item label="编号">
          <el-input v-model="newTableForm.serialNumber" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="所属门店">
          <el-select v-model="newTableForm.shopId" placeholder="请选择门店">
            <el-option
              v-for="(shop, index) in shopOptions"
              :key="index"
              :label="shop.name"
              :value="shop.shopId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="桌型">
          <el-select v-model="newTableForm.diningTableTypeId" placeholder="请选择桌型">
            <el-option
              v-for="(diningTableType, index) in diningTableTypeOptions"
              :key="index"
              :label="diningtTableTypeStr(diningTableType)"
              :value="diningTableType.diningTableTypeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用状态">
          <el-radio-group v-model="newTableForm.userState">
            <el-radio
              v-for="(item, index) in ['空闲', '待点菜', '预结账', '用餐中']"
              :key="index"
              :label="index"
              >{{ item }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-radio-group v-model="newTableForm.state">
            <el-radio-button :label="0">启用</el-radio-button>
            <el-radio-button :label="1">停用</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="二维码">
          <div class="qr-code" v-if="newTableForm.qrCode">
            <img :src="newTableForm.qrCode" alt="二维码" />
          </div>
          <el-button
            type="primary"
            icon="el-icon-qr-code"
            @click="generateQrCode"
            v-if="!newTableForm.qrCode"
          >
            保存数据并生成台桌点餐二维码
          </el-button>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="newTableDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleNewTableConfirm">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";
import QRCode from "qrcode";

export default {
  name: "TableManagement",
  data() {
    return {
      diningTableTypeOptions: [],
      diningTableTypeOptionsDialog: [],
      shopOptions: [],
      // 当前激活的标签页（默认列表）
      activeTab: "list",
      // 搜索表单
      searchForm: {
        shopId: "",
        diningTableTypeId: "",
        userState: "",
      },
      // 台桌列表数据
      tableList: [],
      // 分页数据
      page: 1,
      pageSize: 10,
      total: 0,
      // 弹窗状态
      tableTypeDialogVisible: false,
      newTableDialogVisible: false,
      newTableForm: {
        diningTableId: "",
        serialNumber: "",
        diningTableTypeId: "",
        userState: 0,
        shopId: "",
        state: 0,
        qrCode: "",
      },
      // 弹窗标题
      tableTypeDialogTitle: "新增桌型",
      newTableDialogTitle: "新建台桌",
    };
  },
  methods: {
    diningtTableTypeStr(diningTableType) {
      if (!diningTableType) {
        return "空";
      }
      return (
        (diningTableType.type == 0
          ? "小桌"
          : diningTableType.type == 1
          ? "大桌"
          : "包间") +
        "（" +
        diningTableType.min +
        " - " +
        diningTableType.max +
        "人）"
      );
    },
    // 查询台桌列表
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.page = page;
      api.diningTable
        .pageList(
          this.page,
          this.pageSize,
          this.searchForm.shopId,
          this.searchForm.diningTableTypeId,
          this.searchForm.userState
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.tableList = res.data.data.records;
            this.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    // 打开桌型管理弹窗（新增/编辑）
    openTableTypeDialog() {
      this.diningTableTypeOptionsDialog = [...this.diningTableTypeOptions];
      this.tableTypeDialogVisible = true;
    },
    // 桌型管理确定
    handleTableTypeConfirm() {
      if (!this.tableTypeForm.type) {
        this.$message.warning("请选择桌位类型");
        return;
      }
      if (this.tableTypeDialogTitle === "新增桌型") {
        this.tableModelList.push({ ...this.tableTypeForm });
      } else {
        const index = this.tableModelList.findIndex(
          (item) => item.type === this.tableTypeForm.type
        );
        if (index !== -1) {
          this.tableModelList[index] = { ...this.tableTypeForm };
        }
      }
      this.tableTypeDialogVisible = false;
      this.$message.success("操作成功");
    },
    handleDiningTableType(diningTableType) {
      if (!diningTableType.max) {
        this.$message.warning("请输入最多人数");
        return;
      }
      if (!diningTableType.min) {
        this.$message.warning("请输入最少人数");
        return;
      }
      api.diningTableType.update(diningTableType).then((res) => {
        if (res.data.code == 200) {
          this.$message.success(res.data.msg);
          this.searchDiningTableType();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    handleDiningTableTypeDelete(diningTableType) {
      if (diningTableType.diningTableTypeId) {
        api.diningTableType
          .delete(diningTableType.diningTableTypeId)
          .then((res) => {
            if (res.data.code == 200) {
              this.$message.success(res.data.msg);
              this.searchDiningTableType();
            } else {
              this.$message.error(res.data.msg);
            }
          });
      } else {
        this.diningTableTypeOptionsDialog.splice(
          this.diningTableTypeOptionsDialog.indexOf(diningTableType),
          1
        );
      }
    },
    // 打开新建台桌弹窗（新增/编辑）
    openNewTableDialog(row) {
      this.newTableDialogVisible = true;
      if (row) {
        this.newTableDialogTitle = "编辑台桌";
        this.newTableForm = { ...row };
      } else {
        this.newTableDialogTitle = "新建台桌";
        this.newTableForm = {
          diningTableId: "",
          serialNumber: "",
          diningTableTypeId: "",
          userState: 0,
          shopId: "",
          state: 0,
          qrCode: "",
        };
      }
    },
    async createQrCode() {
      if (!this.newTableForm.diningTableId) {
        this.$message.error("为获取到对应的台桌信息");
        return;
      }
      this.newTableForm.qrCode = await QRCode.toDataURL(
        this.newTableForm.diningTableId,
        {
          width: 200,
          quality: 0.9,
          type: "image/jpeg",
        }
      );
      this.handleNewTableConfirm()
    },
    // 生成二维码（模拟）
    generateQrCode() {
      // 实际项目中调用二维码生成接口
      api.diningTable.add(this.newTableForm).then((res) => {
        if (res.data.code == 200) {
          this.newTableForm = res.data.data;
          // 使用返回的id创建二维码，创建完成后重新保存二维码的数据
          this.createQrCode();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 新建/编辑台桌确定
    handleNewTableConfirm() {
      if (!this.newTableForm.serialNumber) {
        this.$message.warning("请输入编号");
        return;
      }
      api.diningTable.update(this.newTableForm).then((res) => {
        if (res.data.code == 200) {
          this.$message.success(res.data.msg);
          this.handleSearch();
          this.newTableDialogVisible = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 删除台桌
    handleDelete(row) {
      this.$confirm("此操作将永久删除该台桌, 是否继续?", "提示", {
        type: "warning",
      })
        .then(() => {
          api.diningTable.delete(row.diningTableId).then((res) => {
            if (res.data.code == 200) {
              this.$message.success("删除成功");
              this.handleSearch(this.page);
            } else {
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    searchDiningTableType() {
      api.diningTableType.list().then((res) => {
        if (res.data.code == 200) {
          this.diningTableTypeOptions = res.data.data;
        }
      });
    },
  },
  mounted() {
    this.searchDiningTableType();
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
.table-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 10px;
}
.operation-btns {
  margin-bottom: 10px;
}
.qr-code {
  text-align: center;
  margin-top: 10px;
}
.qr-code img {
  width: 100px;
  height: 100px;
}
.dialog-footer {
  text-align: right;
}
</style>
