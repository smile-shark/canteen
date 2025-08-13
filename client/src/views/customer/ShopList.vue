<template>
  <div class="shop-selection-page">
    <!-- 顶部导航栏 -->
    <div class="header">
      <div class="header-title">选择门店</div>
    </div>
    <!-- 服务类型选择 -->
    <el-tabs
      v-model="selectedServiceType"
      :stretch="true"
    >
      <el-tab-pane label="堂食" name="dineIn"></el-tab-pane>
      <el-tab-pane label="外卖" name="takeOut"></el-tab-pane>
    </el-tabs>

    <!-- 门店列表 -->
    <div class="shop-list">
      <div
        v-for="shop in shops"
        :key="shop.shopId"
        class="shop-card"
        @click="handleSelectShop(shop)"
      >
        <!-- 门店基本信息 -->
        <div class="shop-header">
          <div class="shop-name-container">
            <h3 class="shop-name">{{ shop.name }}</h3>
            <span class="shop-order" v-if="shop.distance">距离{{ (shop.distance.value/1000).toFixed(1)   }}公里</span>
          </div>
          <div class="shop-status" :class="getShopStatusClass(shop)">
            {{ getShopStatusText(shop) }}
          </div>
        </div>

        <!-- 门店详情 -->
        <div class="shop-details">
          <p class="shop-address">
            <i class="el-icon-location"></i>
            {{ shop.address || "暂无地址信息" }}
          </p>

          <p class="shop-contact">
            <i class="el-icon-phone"></i>
            {{ shop.phone || "暂无联系电话" }}
          </p>

          <div class="shop-hours">
            <i class="el-icon-time"></i>
            <span>营业时间: </span>
            <span>
              {{
                shop.openingHoursStart
                  ? formatTime(shop.openingHoursStart)
                  : "00:00"
              }}
              -
              {{
                shop.openingHoursEnd
                  ? formatTime(shop.openingHoursEnd)
                  : "00:00"
              }}
            </span>
          </div>
        </div>

        <!-- 门店服务信息 -->
        <div class="shop-services">
          <span class="service-tag" v-if="shop.isDineIn === 0">
            <i class="el-icon-cutlery"></i> 支持堂食
          </span>
          <span class="service-tag" v-if="shop.isTakeOut === 0">
            <i class="el-icon-shopping-bag"></i> 支持外卖
          </span>
          <span class="people-count">
            <i class="el-icon-user"></i> 门店人数: {{ shop.peopleNum || 0 }}
          </span>
        </div>
      </div>
    </div>

    <!-- 无结果提示 -->
    <div
      class="no-result"
      v-if="shops.length === 0 && shops.length > 0"
    >
      <i class="el-icon-search"></i>
      <p>未找到匹配的门店</p>
    </div>

  </div>
</template>

<script>
import api from "@/api";  
import { AMap } from "@/amp";

export default {
  name: "ShopSelection",
  data() {
    return {
      // 门店假数据
      shops: [],

      // 状态管理
      selectedServiceType: "dineIn", // 默认选择堂食

      page: 1,
      size: 10,
      lat:null,
      lng:null,
    };
  },
  watch:{
    selectedServiceType(){
      this.searchUpdateState()
    }
  },
  methods: {
    searchUpdateState() {
      this.shops=[]
      this.handleSearch(1)
    },
    // 处理搜索
    handleSearch(page) {
      if (typeof page != "number") {
        page = 1;
      }
      this.page = page;
      
      api.shop
        .pageListByRange(
          this.page,
          this.size,
          this.selectedServiceType == "dineIn" ? 0 : 1,
          this.selectedServiceType == "takeOut" ? 0 : 1,
          this.lng,
          this.lat
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.shops.push(...res.data.data);
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },


    // 获取门店状态文本
    getShopStatusText(shop) {
      if (shop.state === 1) {
        return "已关闭";
      }

      // 简单判断是否在营业时间内
      const now = new Date();
      const currentHours = now.getHours();
      const currentMinutes = now.getMinutes();

      if (shop.openingHoursStart && shop.openingHoursEnd) {
        const [startHours, startMinutes] = shop.openingHoursStart
          .split(":")
          .map(Number);
        const [endHours, endMinutes] = shop.openingHoursEnd
          .split(":")
          .map(Number);

        const isOpen =
          (currentHours > startHours ||
            (currentHours === startHours && currentMinutes >= startMinutes)) &&
          (currentHours < endHours ||
            (currentHours === endHours && currentMinutes <= endMinutes));

        return isOpen ? "营业中" : "未营业";
      }

      return "营业中";
    },

    // 获取门店状态样式类
    getShopStatusClass(shop) {
      const statusText = this.getShopStatusText(shop);

      switch (statusText) {
        case "营业中":
          return "status-open";
        case "未营业":
          return "status-closed";
        case "已关闭":
          return "status-shutdown";
        default:
          return "";
      }
    },

    // 格式化时间显示
    formatTime(timeStr) {
      if (!timeStr) return "00:00";
      // 确保时间格式为HH:MM
      const parts = timeStr.split(":");
      if (parts.length >= 2) {
        return `${parts[0].padStart(2, "0")}:${parts[1].padStart(2, "0")}`;
      }
      return timeStr;
    },

    // 选择门店
    handleSelectShop(shop) {
      // 检查门店是否可用
      if (shop.state === 1) {
        this.$message.warning("该门店已关闭，无法选择");
        return;
      }

      if (this.selectedServiceType === "dineIn" && shop.isDineIn !== 0) {
        this.$message.warning("该门店不提供堂食服务");
        return;
      }

      if (this.selectedServiceType === "takeOut" && shop.isTakeOut !== 0) {
        this.$message.warning("该门店不提供外卖服务");
        return;
      }

      localStorage.setItem('shopId',shop.shopId)
      // 返回选中的门店
      this.$router.push(`/customer/home/${shop.shopId}`)
    },

    // 返回上一页
    handleBack() {
      this.$router.back();
    },
    onComplete(data){
      this.lat=data.position.kT
      this.lng=data.position.KL
      this.handleSearch();
    }
  },
  mounted() {
    // 这里需要获取用户的定位，然后根据定位来查找对应的门店
    AMap(this.onComplete)
  },
};
</script>

<style scoped>
.shop-selection-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 20px;
}

/* 头部导航 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  background-color: #ffffff;
  padding: 0 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left,
.header-right {
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  font-size: 18px;
  font-weight: 500;
  color: #333333;
}

/* 服务类型选择 */
.service-type-tabs {
  display: flex;
  background-color: #ffffff;
  border-bottom: 1px solid #eeeeee;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 15px 0;
  font-size: 16px;
  color: #666666;
  position: relative;
}

.tab-item.active {
  color: #1890ff;
  font-weight: 500;
}

.tab-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background-color: #1890ff;
}

/* 搜索框 */
.search-container {
  padding: 10px 15px;
  background-color: #ffffff;
}

.search-container .el-input {
  width: 100%;
}

/* 门店列表 */
.shop-list {
  padding: 10px 15px;
}

.shop-card {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.shop-card:active {
  transform: scale(0.99);
}

/* 门店头部 */
.shop-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.shop-name-container {
  flex: 1;
}

.shop-name {
  font-size: 17px;
  font-weight: 500;
  color: #333333;
  margin: 0 0 5px 0;
}

.shop-order {
  font-size: 12px;
  color: #999999;
  background-color: #f5f5f5;
  padding: 2px 6px;
  border-radius: 4px;
}

.shop-status {
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-open {
  background-color: #e6f7ee;
  color: #00a854;
}

.status-closed {
  background-color: #fff7e6;
  color: #ff7d00;
}

.status-shutdown {
  background-color: #ffe6e6;
  color: #f53f3f;
}

/* 门店详情 */
.shop-details {
  margin-bottom: 12px;
}

.shop-address,
.shop-contact,
.shop-hours {
  font-size: 14px;
  color: #666666;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
}

.shop-details i {
  color: #1890ff;
  margin-right: 5px;
  width: 18px;
  text-align: center;
}

/* 门店服务 */
.shop-services {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.service-tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  background-color: #f0f7ff;
  color: #1890ff;
  display: flex;
  align-items: center;
}

.service-tag i {
  margin-right: 3px;
  font-size: 11px;
}

.people-count {
  font-size: 12px;
  color: #888888;
  display: flex;
  align-items: center;
}

.people-count i {
  margin-right: 3px;
  font-size: 11px;
}

/* 无结果和加载提示 */
.no-result,
.loading {
  text-align: center;
  padding: 40px 0;
  color: #999999;
}

.no-result i,
.loading i {
  font-size: 40px;
  margin-bottom: 15px;
  display: inline-block;
}

/* 适配小屏幕 */
@media (max-width: 375px) {
  .shop-name {
    font-size: 16px;
  }

  .shop-address,
  .shop-contact,
  .shop-hours {
    font-size: 13px;
  }
}
</style>
