<template>
  <el-container style="height: 100vh; background-color: #fff">
    <!-- 头部区域：返回按钮 + 标题、订单编号 -->
    <el-header style="height: auto; padding: 0">
      <el-row :gutter="0" style="border-bottom: 1px solid #f2f2f2">
        <el-col :span="24">
          <div style="display: flex; align-items: center; padding: 10px 15px">
            <i
              class="el-icon-arrow-left"
              style="cursor: pointer"
              @click="goBack"
            ></i>
            <span style="margin-left: 5px">确认订单</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" style="padding: 15px">
        <el-col :span="24">
          <div
            style="text-align: center; font-size: 16px; color: #666"
            v-if="customerOrder.type == 0"
          >
            {{ diningTableInfo.serialNumber }}
          </div>
        </el-col>
      </el-row>
    </el-header>

    <!-- 主体内容：餐厅名称、菜品列表、费用明细、备注、合计 -->
    <el-main style="padding: 15px">
      <!-- 餐厅名称 -->
      <el-row :gutter="0" style="margin-bottom: 15px">
        <el-col :span="24">
          <div style="font-size: 16px; font-weight: 500">智慧餐厅（XXX店）</div>
        </el-col>
      </el-row>

      <!-- 菜品列表 -->
      <el-row
        :gutter="10"
        style="margin-bottom: 20px"
        v-for="(item, index) in customerOrder.customerOrderCuisines"
        :key="index"
      >
        <el-col :span="24">
          <div
            style="
              display: flex;
              align-items: center;
              padding: 10px;
              border: 1px solid #f2f2f2;
              border-radius: 4px;
            "
          >
            <img :src="item.cuisine?.image" alt="dish" class="dish-img" />
            <div class="dish-info" style="flex: 1">
              <el-row :gutter="0" style="margin-bottom: 5px">
                <el-col :span="18">
                  <span style="font-size: 16px; font-weight: 500">{{
                    item.cuisine?.name
                  }}</span>
                </el-col>
                <el-col :span="6" style="text-align: right">
                  <span style="font-size: 16px; color: #f60"
                    >￥{{ realyPrice(item.cuisine) }}</span
                  >
                </el-col>
              </el-row>
              <el-row :gutter="0">
                <el-col :span="24">
                  <span style="font-size: 16px; color: #999"
                    >x{{ item.num }}</span
                  >
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 费用明细 -->
      <el-row
        :gutter="0"
        style="margin-bottom: 10px"
        v-if="customerOrder.type == 1 || customerOrder.type == 2"
      >
        <el-col :span="12">
          <div style="font-size: 16px">打包费</div>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <div style="font-size: 16px">￥3</div>
        </el-col>
      </el-row>
      <el-row
        :gutter="0"
        style="margin-bottom: 10px"
        v-if="customerOrder.type == 1"
      >
        <el-col :span="12">
          <div style="font-size: 16px">配送费</div>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <div style="font-size: 16px">￥0</div>
        </el-col>
      </el-row>
      <el-row :gutter="0" style="margin-bottom: 20px">
        <el-col :span="12">
          <div>
            <div
              style="
                display: inline-block;
                background-color: #e56534;
                font-weight: bold;
                color: white;
                padding: 7px 10px;
                border-radius: 20%;
              "
            >
              券
            </div>
            优惠券
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <div style="font-size: 16px">
            <span v-if="selectedCoupon.discountCoupon">
              <span v-if="selectedCoupon.discountCoupon.type == 0">
                ￥{{ selectedCoupon.discountCoupon.price }}
              </span>
              <span v-else-if="selectedCoupon.discountCoupon.type == 1">
                {{ selectedCoupon.discountCoupon.discount }}折
              </span>
            </span>
            <span
              v-if="discountCouponList.length > 0"
              style="color: #c3c3c3"
              @click="showSelectCoupons"
              >可选择<i
                class="el-icon-arrow-right"
                style="font-weight: bold"
              ></i
            ></span>
            <span v-else> 无可用 </span>
          </div>
        </el-col>
      </el-row>
      <!-- 备注 -->
      <el-row :gutter="0" style="margin: 15px 0">
        <el-col :span="24">
          <div style="font-size: 16px; margin-bottom: 5px">备注</div>
          <el-input
            :rows="5"
            type="textarea"
            placeholder="请输入备注信息"
            style="width: 100%"
          ></el-input>
        </el-col>
      </el-row>

      <!-- 合计与提交订单 -->
      <el-row :gutter="0" style="position: absolute; width: 96%; bottom: 0">
        <el-col :span="17">
          <div style="font-size: 16px; font-weight: bold; padding-top: 10px">
            合计
            <span
              style="color: #f60"
              :style="{
                textDecoration: selectedCoupon.discountCoupon
                  ? 'line-through'
                  : 'none',
              }"
              >￥{{ customerOrder.allPrice }}
            </span>
            <span
              v-if="selectedCoupon.discountCoupon"
              style="color: #f60; font-size: 1.4rem"
            >
              <span v-if="selectedCoupon.discountCoupon.type == 0">
                ￥{{
                  (
                    customerOrder.allPrice - selectedCoupon.discountCoupon.price
                  ).toFixed(2)
                }}
              </span>
              <span v-else-if="selectedCoupon.discountCoupon.type == 1">
                {{
                  (
                    (customerOrder.allPrice *
                      selectedCoupon.discountCoupon.discount) /
                    10
                  ).toFixed(2)
                }}折
              </span>
            </span>
          </div>
        </el-col>
        <el-col
          :span="7"
          style="
            text-align: center;
            background-color: #f60;
            color: white;
            padding: 10px 20px;
          "
          @click="submitOrder"
        >
          提交订单
        </el-col>
      </el-row>
    </el-main>

    <el-dialog
      :visible.sync="showSelectCoupon.visible"
      :fullscreen="true"
      :show-close="false"
    >
      <template #title>
        <el-row :gutter="0" style="border-bottom: 1px solid #f2f2f2">
          <el-col :span="24">
            <div style="display: flex; align-items: center; padding: 10px 15px">
              <i
                class="el-icon-arrow-left"
                style="cursor: pointer"
                @click="showSelectCoupon.visible = false"
              ></i>
              <span style="margin-left: 5px">优惠券</span>
            </div>
          </el-col>
        </el-row>
      </template>
      <!-- 优惠券列表 -->
      <el-card
        v-for="(coupon, index) in discountCouponList"
        :key="index"
        shadow="hover"
        class="coupon-card"
      >
        <div class="coupon-info">
          <div class="coupon-amount">
            <p class="amount" v-if="coupon.discountCoupon.type == 0">
              ¥{{ coupon.discountCoupon.price }}
            </p>
            <p class="amount" v-if="coupon.discountCoupon.type == 1">
              {{ coupon.discountCoupon.discount }}折
            </p>
            <p class="condition">满{{ coupon.discountCoupon.condition }}可用</p>
          </div>
          <div class="coupon-detail">
            <p class="usage">{{ coupon.discountCoupon.name }}</p>
            <p class="validity">有效期: {{ coupon.discountCoupon.endTime }}</p>
            <div style="text-align: right">
              <el-button
                style="color: white; background-color: #f26c21"
                @click="selectCoupon(coupon)"
                >使用</el-button
              >
            </div>
          </div>
        </div>
      </el-card>
    </el-dialog>
  </el-container>
</template>

<script>
import api from "@/api";

export default {
  data() {
    return {
      customerOrder: {},
      diningTableInfo: {},
      discountCouponList: [],
      shopInfo: {},
      selectedCoupon: {},
      showSelectCoupon: {
        visible: false,
      },
    };
  },
  methods: {
    selectCoupon(coupon) {
      this.selectedCoupon = coupon;
      this.customerOrder.discountCouponCustomerId=coupon.discountCouponCustomerId
      this.showSelectCoupon.visible = false;
    },
    showSelectCoupons() {
      this.showSelectCoupon.visible = true;
    },
    // 返回上一页
    goBack() {
      this.$router.back();
    },
    // 获取菜品的真实价格
    realyPrice(cuisine) {
      if (!cuisine) {
        return 0;
      }
      return cuisine.isSpecialOffer === 1
        ? cuisine.specialOffer !== null
          ? cuisine.specialOffer
          : cuisine.price
        : cuisine.price;
    },
    // 提交订单方法，可根据实际需求对接接口
    submitOrder() {
      // 可在这里编写调用提交订单接口的逻辑
      console.log("提交订单，订单数据：", this.customerOrder);
      // 示例：调用 api 提交订单
      // api.order.submit(this.customerOrder).then(res => {
      //   if (res.data.code === 200) {
      //     this.$message.success('订单提交成功');
      //     // 提交成功后的逻辑，比如跳转到支付页等
      //   } else {
      //     this.$message.error('订单提交失败，请重试');
      //   }
      // });
    },
  },
  created() {
    this.diningTableInfo = JSON.parse(localStorage.diningTableInfo);
    this.shopInfo = JSON.parse(localStorage.shopInfo);
  },
  mounted() {
    // 接收路由参数
    this.customerOrder = this.$route.query;
    if (!this.customerOrder || !this.customerOrder.customerOrderCuisines) {
      this.$message.error("参数传递错误");
      return;
    }
    // 遍历菜品，补充菜品详情
    for (let i in this.customerOrder.customerOrderCuisines) {
      api.cuisine
        .getSimpleCuisineById(
          this.customerOrder.customerOrderCuisines[i].cuisineId
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.customerOrder.customerOrderCuisines[i].cuisine = res.data.data;
            this.customerOrder = JSON.parse(JSON.stringify(this.customerOrder));
          }
        });
    }
    // 获取可用的优惠券
    let cuisineIds = this.customerOrder.customerOrderCuisines.map(
      (item) => item.cuisineId
    );

    api.discountCoupon
      .usableCoupon(
        this.shopInfo.shopId,
        cuisineIds,
        this.customerOrder.allPrice
      )
      .then((res) => {
        if (res.data.code == 200) {
          this.discountCouponList = res.data.data;
        }
      });
  },
};
</script>

<style scoped>
.my-coupons {
  padding: 20px;
}
.page-title {
  margin: 0;
}
.coupon-tabs {
  margin-bottom: 20px;
}
.coupon-card {
  margin-bottom: 10px;
}
.coupon-info {
  display: flex;
  padding: 10px;
}
.coupon-amount {
  background-color: #f26c21;
  color: #fff;
  padding: 10px;
  text-align: center;
  width: 120px;
}
.amount {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}
.condition {
  font-size: 14px;
}
.coupon-detail {
  padding: 0 10px;
}
.usage {
  font-weight: bold;
  margin-bottom: 5px;
}
.validity {
  font-size: 14px;
  color: #999;
}
.dish-img {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  margin-right: 10px;
}
/* 去除 Element UI 自带的一些默认样式影响，可根据实际情况调整 */
.el-header,
.el-main,
.el-footer {
  padding: 0;
}
.el-input textarea {
  min-height: 60px;
  resize: none;
}
</style>
