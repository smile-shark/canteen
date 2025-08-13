<template>
  <div class="my-coupons">
    <!-- 返回按钮 -->
    <el-row>
      <el-col :span="2">
        <el-button
          icon="el-icon-arrow-left"
          type="text"
          @click="$router.push('/customer/profile')"
          style="font-weight: bold; font-size: 2rem"
        ></el-button>
      </el-col>
      <el-col :span="20" style="padding: 12px 0">
        <h2 class="page-title">我的优惠券</h2>
      </el-col>
    </el-row>

    <!-- 分类标签 -->
    <el-tabs v-model="activeTab" class="coupon-tabs" :stretch="true">
      <el-tab-pane
        v-for="(value, index) in ['未使用', '已使用', '已过期']"
        :key="index"
        :name="value"
        :label="value"
      ></el-tab-pane>
    </el-tabs>

    <!-- 优惠券列表 -->
    <el-card
      v-for="(coupon, index) in coupons"
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
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "MyCoupons",
  data() {
    return {
      activeTab: '未使用',
      coupons: [],
    };
  },
  watch: {
    activeTab() {
      this.handleSearch();
    },
  },
  methods: {
    handleSearch() {
      api.discountCouponCustomer
        .list(this.activeTab == "未使用" ? 0 : this.activeTab == "已使用" ? 1 : 2)
        .then((res) => {
          if (res.data.code == 200) {
            this.coupons = res.data.data;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
  },
  computed: {},
  mounted(){
      this.handleSearch();
  }
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
</style>
