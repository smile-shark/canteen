<template>
  <div class="my-coupons">
    <!-- 返回按钮 -->
    <el-row>
      <el-col :span="2">
        <el-button icon="el-icon-back" type="text" @click="$router.push('/customer/profile')"></el-button>
      </el-col>
      <el-col :span="20">
        <h2 class="page-title">我的优惠券</h2>
      </el-col>
    </el-row>

    <!-- 分类标签 -->
    <el-tabs v-model="activeTab" type="card" class="coupon-tabs">
      <el-tab-pane label="未使用" name="unused"></el-tab-pane>
      <el-tab-pane label="已使用" name="used"></el-tab-pane>
      <el-tab-pane label="已过期" name="expired"></el-tab-pane>
    </el-tabs>

    <!-- 优惠券列表 -->
    <el-card
      v-for="(coupon, index) in filteredCoupons"
      :key="index"
      shadow="hover"
      class="coupon-card"
    >
      <div class="coupon-info">
        <div class="coupon-amount">
          <p class="amount">¥{{ coupon.amount }}</p>
          <p class="condition">{{ coupon.condition }}</p>
        </div>
        <div class="coupon-detail">
          <p class="usage">{{ coupon.usage }}</p>
          <p class="validity">有效期: {{ coupon.validity }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MyCoupons',
  data() {
    return {
      activeTab: 'unused',
      coupons: [
        {
          id: 1,
          amount: 20,
          condition: '满100可用',
          usage: '仅限堂食可用',
          validity: '2021-12-31',
          status: 'unused'
        },
        {
          id: 2,
          amount: 20,
          condition: '满100可用',
          usage: '仅限堂食可用',
          validity: '2021-12-31',
          status: 'unused'
        },
        {
          id: 3,
          amount: 20,
          condition: '满100可用',
          usage: '仅限外卖可用',
          validity: '2021-12-31',
          status: 'unused'
        },
        {
          id: 4,
          amount: 20,
          condition: '满100可用',
          usage: '仅限堂食可用',
          validity: '2021-12-31',
          status: 'unused'
        },
        {
          id: 5,
          amount: 20,
          condition: '满100可用',
          usage: '仅限堂食可用',
          validity: '2020-12-31',
          status: 'expired'
        },
        {
          id: 6,
          amount: 20,
          condition: '满100可用',
          usage: '仅限堂食可用',
          validity: '2020-12-31',
          status: 'used'
        }
      ]
    };
  },
  computed: {
    filteredCoupons() {
      return this.coupons.filter(coupon => coupon.status === this.activeTab);
    }
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