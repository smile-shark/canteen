<template>
  <div class="dish-list">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜品名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入菜品名称"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="所属分类">
          <el-select
            v-model="searchForm.cuisineTypeId"
            placeholder="请选择分类"
            clearable
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
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="在售" :value="0"></el-option>
            <el-option label="停售" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>

      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog"
        >新增菜品</el-button
      >
    </div>

    <!-- 菜品表格 -->
    <el-table
      :data="dishList"
      border
      style="width: 100%; margin-top: 15px"
      stripe
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="num" label="ID" width="80"></el-table-column>
      <el-table-column label="菜品图片" width="100">
        <template #default="scope">
          <img
            v-if="scope.row.image"
            :src="scope.row.image"
            alt="菜品图片"
            class="dish-image"
          />
          <span v-else class="dish-image">暂无图片</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="菜品名称"></el-table-column>
      <el-table-column label="所属分类">
        <template #default="scope">
          {{
            cuisineTypeOptions.find(
              (item) => item.cuisineTypeId === scope.row.cuisineTypeId
            )["name"]
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="price"
        label="单价"
        :formatter="formatPrice"
      ></el-table-column>
      <el-table-column prop="state" label="状态">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.state == 0">在售</el-tag>
          <el-tag type="warning" v-if="scope.row.state == 1">停售</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
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
          <el-button type="text" @click="handleDetail(scope.row)"
            >详情</el-button
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

    <!-- 新增/编辑菜品弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑菜品' : '新增菜品'"
      :visible.sync="dialogVisible"
      width="800px"
      append-to-body
    >
      <el-steps
        :active="activeStep"
        finish-status="success"
        style="margin-bottom: 20px"
      >
        <el-step title="基本信息"></el-step>
        <el-step title="口味规格库存预警"></el-step>
        <el-step title="原料与做法"></el-step>
        <el-step title="其他设置"></el-step>
      </el-steps>

      <template v-if="activeStep === 0">
        <el-form :model="form" label-width="120px">
          <el-form-item
            label="菜品名称"
            prop="name"
            :rules="{
              required: true,
              message: '请输入菜品名称',
              trigger: 'blur',
            }"
          >
            <el-input
              v-model="form.name"
              placeholder="请输入菜品名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="所属分类"
            prop="cuisineTypeId"
            :rules="{
              required: true,
              message: '请选择分类',
              trigger: 'change',
            }"
          >
            <el-select v-model="form.cuisineTypeId" placeholder="请选择分类">
              <el-option
                v-for="(cuisineType, index) in cuisineTypeOptions"
                :key="index"
                :label="cuisineType.name"
                :value="cuisineType.cuisineTypeId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="菜品图片">
            <el-upload
              action="null"
              list-type="picture-card"
              :before-remove="beforeRemove"
              :file-list="form.cuisineImages"
              :auto-upload="false"
              :on-change="handleChange"
              accept="image/jpeg,image/png,image/gif,image/jpg"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
          <el-form-item
            label="参考价格"
            prop="price"
            :rules="{ required: true, message: '请输入价格', trigger: 'blur' }"
          >
            <el-input
              v-model="form.price"
              placeholder="请输入价格"
              type="number"
            ></el-input>
          </el-form-item>
          <el-form-item label="菜品描述">
            <el-input
              v-model="form.introduce"
              placeholder="请输入菜品描述"
              type="textarea"
              rows="3"
            ></el-input>
          </el-form-item>
          <el-form-item label="排序号" required>
            <el-input
              v-model="form.num"
              placeholder="数字越小越靠前 (0001)"
              type="number"
            ></el-input>
          </el-form-item>
        </el-form>
      </template>

      <template v-if="activeStep === 1">
        <div>
          <el-form>
            <el-form-item label="可选口味">
              <el-radio-group v-model="form.tasteId">
                <el-radio
                  v-for="(taste, index) in tasteOptions"
                  :key="index"
                  :label="taste.tasteId"
                >
                  {{ taste.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="规格设置">
              <el-select
                v-model="form.cuisineComponentId"
                placeholder="请选择规格"
              >
                <el-option
                  v-for="(cuisineComponent, index) in cuisineComponentOptions"
                  :key="index"
                  :label="cuisineComponent.name"
                  :value="cuisineComponent.cuisineComponentId"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="库存过低预警" required>
              <el-input
                v-model="form.warningMin"
                placeholder="库存过低预警"
                type="number"
              ></el-input>
            </el-form-item>
            <el-form-item label="库存过高预警" required>
              <el-input
                v-model="form.warningMax"
                placeholder="库存过高预警"
                type="number"
              ></el-input>
            </el-form-item>
            <el-form-item label="起卖数量" required>
              <el-input
                v-model="form.minNum"
                placeholder="起卖数量"
                type="number"
              ></el-input>
            </el-form-item>
            <el-form-item label="库存数量" required>
              <el-input
                v-model="form.inventory"
                placeholder="库存数量"
                type="number"
              ></el-input>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <template v-if="activeStep === 2">
        <div>
          <el-form>
            <el-form-item label="荤素">
              <el-input
                v-model="form.meatVegetable"
                placeholder="荤/素"
              ></el-input>
            </el-form-item>
            <el-form-item label="主料">
              <el-input
                v-model="form.mainIngredient"
                placeholder="请输入主料"
                type="textarea"
                rows="3"
              ></el-input>
            </el-form-item>
            <el-form-item label="辅料">
              <el-input
                v-model="form.ingredients"
                placeholder="请输入辅料"
                type="textarea"
                rows="3"
              ></el-input>
            </el-form-item>
            <el-form-item label="烹饪方法">
              <el-input
                v-model="form.cookingMethod"
                placeholder="请输入烹饪方法"
                type="textarea"
                rows="3"
              ></el-input>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <template v-if="activeStep === 3">
        <div>
          <el-form>
            <el-form-item label="是否特价菜">
              <el-radio-group v-model="form.isSpecialOffer">
                <el-radio :label="0">是</el-radio>
                <el-radio :label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="特价价格">
              <el-input
                v-model="form.specialOffer"
                placeholder="请输入特价价格"
                type="number"
              ></el-input>
            </el-form-item>
            <el-form-item label="对应菜系" required>
              <el-select
                v-model="form.styleOfCookingId"
                placeholder="请选择菜系"
              >
                <el-option
                  v-for="(styleOfCooking, index) in styleOfCookingOptions"
                  :key="index"
                  :label="styleOfCooking.name"
                  :value="styleOfCooking.styleOfCookingId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="对应门店" required>
              <el-select v-model="form.shopId" placeholder="请选择门店">
                <el-option
                  v-for="(shop, index) in shopOptions"
                  :key="index"
                  :label="shop.name"
                  :value="shop.shopId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="出售状态">
              <el-radio-group v-model="form.state">
                <el-radio :label="0">在售</el-radio>
                <el-radio :label="1">停售</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="外卖展示">
              <el-radio-group v-model="form.takeOutShow">
                <el-radio :label="0">是</el-radio>
                <el-radio :label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="堂食展示">
              <el-radio-group v-model="form.dineInShow">
                <el-radio :label="0">是</el-radio>
                <el-radio :label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>

          <template v-if="activeStep > 0">
            <el-button @click="prevStep">上一步</el-button>
          </template>

          <template v-if="activeStep < 3">
            <el-button type="primary" @click="nextStep">下一步</el-button>
          </template>

          <template v-if="activeStep === 3">
            <el-button type="primary" @click="saveDish">保存</el-button>
          </template>
        </div>
      </template>
    </el-dialog>

    <!-- 菜品详情弹窗 -->
    <el-dialog
      v-if="form.name&&detailDialogVisible"
      title="菜品详情"
      :visible.sync="detailDialogVisible"
      width="600px"
      append-to-body
    >
      <div class="dish-detail">
        <div class="detail-header">
          <!-- 基本信息 -->
          <div class="basic-info">
            <h3 class="detail-name">{{ form.name }}</h3>
            <p>菜品编号: {{ form.num || "无" }}</p>
          </div>
        </div>

        <div class="detail-content">
          <el-row :gutter="20">
            <el-col :span="12">
              <p><strong>售价:</strong> ¥{{ form.price }}</p>
              <p>
                <strong>是否特价:</strong>
                {{ form.isSpecialOffer === 0 ? "是" : "否" }}
              </p>
              <p>
                <strong>特价价格:</strong>
                {{ form.specialOffer ? "¥" + form.specialOffer : "无" }}
              </p>
            </el-col>
            <el-col :span="12">
              <p>
                <strong>分类:</strong>
                {{
                  cuisineTypeOptions.find(
                    (item) => item.cuisineTypeId === form.cuisineTypeId
                  )["name"]
                }}
              </p>
              <p>
                <strong>菜系:</strong>
                {{
                  styleOfCookingOptions.find(
                    (item) => item.styleOfCookingId === form.styleOfCookingId
                  )["name"]
                }}
              </p>
              <p>
                <strong>口味:</strong>
                {{
                  tasteOptions.find((item) => item.tasteId === form.tasteId)[
                    "name"
                  ]
                }}
              </p>
              <p>
                <strong>所属门店:</strong>
                {{
                  shopOptions.find((item) => item.shopId === form.shopId)[
                    "name"
                  ]
                }}
              </p>
              <p>
                <strong>规格:</strong>
                {{
                  cuisineComponentOptions.find(
                    (item) =>
                      item.cuisineComponentId === form.cuisineComponentId
                  )["name"]
                }}
              </p>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <p><strong>图片:</strong></p>
              <el-image
                v-for="(image, index) in form.cuisineImages"
                :key="index"
                style="width: 100px; height: 100px"
                :src="image.url"
                fit="cover"
              ></el-image>
            </el-col>
          </el-row>

          <div class="detail-section">
            <h4>库存信息</h4>
            <p><strong>起卖数量:</strong> {{ form.minNum || 1 }}</p>
            <p><strong>库存:</strong> {{ form.inventory || 0 }}</p>
            <p><strong>库存过低预警:</strong> {{ form.warningMin || 0 }}</p>
            <p>
              <strong>库存过高预警:</strong>
              {{ form.warningMax || "无限制" }}
            </p>
          </div>

          <div class="detail-section">
            <h4>菜品介绍</h4>
            <p class="introduction">{{ form.introduce || "暂无介绍" }}</p>
          </div>

          <div class="detail-section">
            <h4>售卖状态</h4>
            <p>
              <strong>状态:</strong>
              <el-tag type="success" v-if="form.state === 0">在售</el-tag>
              <el-tag type="warning" v-else>停售</el-tag>
            </p>
            <p>
              <strong>外卖点餐展示:</strong>
              {{ form.takeOutShow === 0 ? "显示" : "隐藏" }}
            </p>
            <p>
              <strong>堂食点餐展示:</strong>
              {{ form.dineInShow === 0 ? "显示" : "隐藏" }}
            </p>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "DishList",
  data() {
    return {
      dialogImageUrl: "",
      detailDialogVisible: false,
      // 搜索表单
      searchForm: {
        name: "",
        cuisineTypeId: "",
        status: "",
      },

      // 菜品列表数据
      dishList: [],

      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },

      cuisineTypeOptions: [],
      tasteOptions: [],
      cuisineComponentOptions: [],
      styleOfCookingOptions: [],
      shopOptions: [],
      // 弹窗状态
      dialogVisible: false,
      isEdit: false,
      activeStep: 0,

      // 表单数据
      form: {
        cuisineId: "",
        name: "",
        num: "",
        price: "",
        creationMethod: "",
        meatVegetable: "",
        mainIngredient: "",
        ingredients: "",
        styleOfCookingId: "",
        cuisineComponentId: "",
        cuisineTypeId: "",
        shopId: "",
        state: 0,
        isSpecialOffer: 0,
        specialOffer: "",
        tasteId: "",
        minNum: "",
        inventory: "",
        introduce: "",
        takeOutShow: 0,
        dineInShow: 0,
        warningMin: "",
        warningMax: "",
        cuisineImages: [],
      },
    };
  },
  methods: {
    // 搜索
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.pageNum = page;
      api.cuisine
        .pageList(
          this.pageNum,
          this.pageSize,
          this.searchForm.name,
          this.searchForm.cuisineTypeId,
          this.searchForm.status
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.dishList = res.data.data.records;
            this.pagination.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    // 打开新增弹窗
    openAddDialog() {
      this.isEdit = false;
      this.activeStep = 0;
      this.form = {
        cuisineId: "",
        name: "",
        num: "",
        price: "",
        creationMethod: "",
        meatVegetable: "",
        mainIngredient: "",
        ingredients: "",
        styleOfCookingId: "",
        cuisineComponentId: "",
        cuisineTypeId: "",
        shopId: "",
        state: "",
        isSpecialOffer: 0,
        specialOffer: "",
        tasteId: "",
        minNum: "",
        inventory: "",
        introduce: "",
        takeOutShow: "",
        dineInShow: "",
        warningMin: "",
        warningMax: "",
        cuisineImages: [],
      };
      this.dialogVisible = true;
    },
    // 打开编辑弹窗
    openEditDialog(row) {
      this.isEdit = true;
      this.activeStep = 0;
      // 获取详细的数据
      api.cuisine.infoById(row.cuisineId).then((res) => {
        if (res.data.code == 200) {
          this.form = res.data.data;
          this.dialogVisible = true;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },

    // 删除菜品
    handleDelete(row) {
      this.$confirm(`确定要删除【${row.name}】吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          api.cuisine.delete(row.cuisineId).then((res) => {
            if (res.data.code == 200) {
              this.$message.success("删除成功");
              this.handleSearch();
            } else {
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    // 查看详情
    handleDetail(row) {
      this.searchDetail(row);
      this.detailDialogVisible = true;
    },

    // 下一步
    nextStep() {
      if (this.activeStep === 0) {
        // 验证基本信息
        if (!this.form.name) {
          this.$message.warning("请输入菜品名称");
          return;
        }
        if (!this.form.cuisineTypeId) {
          this.$message.warning("请选择所属分类");
          return;
        }
        if (!this.form.price) {
          this.$message.warning("请输入价格");
          return;
        }
        if (!this.form.num) {
          this.$message.warning("请输入排序号");
          return;
        }
      }
      if (this.activeStep === 1) {
        if (!this.form.warningMin) {
          this.$message.warning("请输入库存过低预警");
        }
        if (!this.form.warningMax) {
          this.$message.warning("请输入库存过高预警");
        }
        if (!this.form.minNum) {
          this.$message.warning("请输入起卖数量");
        }
        if (!this.form.inventory) {
          this.$message.warning("请输入库存数量");
        }
      }
      this.activeStep++;
    },

    // 上一步
    prevStep() {
      this.activeStep--;
    },

    // 保存菜品
    saveDish() {
      if (!this.form.styleOfCookingId) {
        this.$message.warning("请选择对应菜系");
        return;
      }
      if (!this.form.shopId) {
        this.$message.warning("请选择对应门店");
        return;
      }

      // 实际项目中调用接口保存
      if (this.isEdit) {
        api.cuisine.update(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success("修改成功");
            this.handleSearch();
          } else {
            this.$message.error(res.data.msg);
          }
          this.dialogVisible = false;
        });
      } else {
        api.cuisine.add(this.form).then((res) => {
          if (res.data.code == 200) {
            this.$message.success("保存成功");
            this.handleSearch();
          } else {
            this.$message.error(res.data.msg);
          }
          this.dialogVisible = false;
        });
      }
    },
    // 格式化价格
    formatPrice(row, column) {
      return "¥" + row.price.toFixed(2);
    },
    searchDetail(row) {
      api.cuisine.infoById(row.cuisineId).then((res) => {
        if (res.data.code == 200) {
          this.form = res.data.data;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 添加图片
    handleChange(file, files) {
      const reader = new FileReader();
      // 当文件当读取完成时触发
      reader.onload = (e) => {
        this.form.cuisineImages.push({
          url: e.target.result,
          imageOrder: this.form.cuisineImages.length,
        });
      };
      reader.readAsDataURL(file.raw);
      console.log(this.form.cuisineImages);
    },
    // 减少图片
    beforeRemove(file, files) {
      const index = this.form.cuisineImages.indexOf(file);
      if (index > -1) {
        this.form.cuisineImages.splice(index, 1);
      }
      this.form.cuisineImages.forEach((item, index) => {
        item.imageOrder = index;
      });
    },
  },
  mounted() {
    api.cuisineType.list().then((res) => {
      if (res.data.code == 200) {
        this.cuisineTypeOptions = res.data.data;
      }
    });
    api.taste.list().then((res) => {
      if (res.data.code == 200) {
        this.tasteOptions = res.data.data;
      }
    });
    api.cuisineComponent.list().then((res) => {
      if (res.data.code == 200) {
        this.cuisineComponentOptions = res.data.data;
      }
    });
    api.styleOfCooking.list().then((res) => {
      if (res.data.code == 200) {
        this.styleOfCookingOptions = res.data.data;
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
/* 详情弹窗样式 */
.dish-detail {
  padding: 10px 0;
}

.detail-header {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}

.detail-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 20px;
}

.basic-info .detail-name {
  margin: 0 0 10px;
  font-size: 18px;
  color: #333;
}

.detail-content {
  font-size: 14px;
  color: #666;
}

.detail-section {
  margin: 15px 0;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.detail-section h4 {
  margin: 0 0 10px;
  font-size: 16px;
  color: #333;
}

.introduction {
  line-height: 1.6;
  white-space: pre-line;
}
.dish-list {
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

.dish-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
