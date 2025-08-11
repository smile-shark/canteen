<template>
  <div class="takeout-page">
    <!-- 顶部栏，使用 Element UI 布局组件 + 图标做返回 -->
    <el-row :gutter="10" class="top-bar">
      <el-col :span="3">
        <!-- 返回按钮，替换为 Element UI 图标，点击跳转对应路径 -->
        <el-button
          style="font-size: 2.4rem; color: black"
          icon="el-icon-arrow-left"
          type="text"
          @click="goBack"
          class="back-btn"
        ></el-button>
      </el-col>
      <el-col :span="20">
        <el-input
          placeholder="请输入您要搜索的菜品"
          v-model="searchVal"
          clearable
          class="search-input"
          ><el-button
            slot="prepend"
            icon="el-icon-search"
            @click="handleSearch"
          ></el-button
        ></el-input>
      </el-col>
      <div class="delivery-info">
        <el-row
          type="flex"
          justify="space-between"
          v-if="orderServiceType == 0"
        >
          <el-col :span="12">
            <span style="font-size: 1.2rem"
              >{{ diningTableInfo.serialNumber }} 号</span
            >
          </el-col>
          <el-col :span="4">
            <i
              class="el-icon-full-screen"
              style="font-size: 2rem"
              @click="$router.push('/customer/scanqrcode')"
            ></i>
          </el-col>
        </el-row>
        <p v-if="orderServiceType == 2" style="font-size: 1.2rem">
          自提时间：{{ deliveryInfo.deliveryTimeStart }} -
          {{ deliveryInfo.deliveryTimeEnd }}
        </p>
        <el-row v-if="orderServiceType == 1">
          <el-col :span="24">
            <span v-if="deliveryInfo.paymentMethod == 0">
              配送费￥{{ deliveryInfo.areaDeliveryPrice }}
            </span>
            <span v-if="deliveryInfo.paymentMethod == 1">
              {{ deliveryInfo.distanceStartNum }}km内配送费￥{{
                deliveryInfo.distanceStartPrice
              }}，每多{{ deliveryInfo.distanceAddNum }}km加配送费￥{{
                deliveryInfo.distanceAddPrice
              }}
            </span>
            <span v-if="deliveryInfo.isFullFreeDelivery == 1">
              满{{ deliveryInfo.fullFreeDeliveryPrice.toFixed(2) }}免配送费
            </span>
          </el-col>
          <el-col :span="24">
            配送时间：{{ deliveryInfo.deliveryTimeStart }} -
            {{ deliveryInfo.deliveryTimeEnd }}
          </el-col>
        </el-row>
      </div>
    </el-row>

    <!-- 配送信息等，可根据需求用 Element UI 组件调整样式 -->
    <div class="delivery-info">
      <p>&nbsp;</p>
    </div>
    <!-- 菜品列表区域，设置滚动，避免被顶部和底部遮挡 -->
    <div class="cuisineOptions-list" style="margin-top: 4rem">
      <!-- 遍历菜品数据渲染列表，这里先模拟数据，实际可从接口获取 -->
      <div
        v-for="(dish, index) in cuisineOptions"
        :key="index"
        class="dish-item"
      >
        <img :src="dish.image" alt="dish" class="dish-img" />
        <div class="dish-info">
          <h3 class="dish-name">{{ dish.name }}</h3>
          <p class="month-sales">月售{{ dish.monthlySales }}</p>
          <p class="month-sales">剩余{{ dish.inventory }}</p>
          <p class="price">¥{{ dish.price }}</p>
        </div>
        <div
          class="add-btn"
          v-if="
            customerOrder != null &&
            customerOrder.customerOrderCuisines.find(
              (item) => item.cuisineId == dish.cuisineId
            )
          "
          @click="orderChange(dish.cuisineId, false)"
        >
          -
        </div>
        <div
          style="padding: 20px"
          v-if="
            customerOrder != null &&
            customerOrder.customerOrderCuisines.find(
              (item) => item.cuisineId == dish.cuisineId
            )
          "
        >
          {{
            customerOrder.customerOrderCuisines.find(
              (item) => item.cuisineId == dish.cuisineId
            ).num
          }}
        </div>
        <div class="add-btn" @click="orderChange(dish.cuisineId, true)">+</div>
      </div>
    </div>
    <!-- 底部购物车及分类栏，固定定位 -->
    <div class="bottom-bar">
      <div class="category" @click="drawer = true">
        <i class="el-icon-s-fold" style="color: #e56534; font-size: 1.5rem"></i
        >菜品分类
      </div>
      <div class="cart" style="position: relative">
        <div
          style="
            position: absolute;
            top: -3rem;
            left: -9rem;
            width: 5rem;
            height: 5rem;
            background-color: #e56534;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
          "
          @click="shopDrawer = true"
        >
          <img src="@/assets/购物篮.png" style="transform: scale(1.5)" />
        </div>
        <img src="@/assets/购物篮.png" alt="cart" class="cart-img" />
        <span class="total-price">¥0</span>
      </div>
    </div>
    <el-drawer
      title="选择分类"
      :visible.sync="drawer"
      direction="btt"
      size="50%"
    >
      <ul class="cuisine-list">
        <li @click="changeCuisineType('')">全部</li>
        <li
          v-for="(cuisineType, index) in cuisineTypeOptions"
          :key="index"
          @click="changeCuisineType(cuisineType.cuisineTypeId)"
        >
          {{ cuisineType.name }}
        </li>
      </ul>
    </el-drawer>
    <el-drawer
      title="购物车"
      :visible.sync="shopDrawer"
      direction="btt"
      size="50%"
    >
      <div v-if="customerOrder == null">
        <div style="text-align: center">暂无数据</div>
      </div>
      <div v-else style="height: 100%;position: relative;">
        <el-row
          v-for="(
            customerOrderCuisine, index
          ) in customerOrder.customerOrderCuisines"
          :key="index"
          type="flex"
          justify="space-between"
          style="padding: 10px 20px"
        >
          <el-col :span="6">
            <!-- 菜品名称 -->
            {{
              cuisineOptions.find(
                (item) => item.cuisineId == customerOrderCuisine.cuisineId
              ).name
            }}
          </el-col>
          <el-col :span="6" style="color: #e56534">
            ￥{{
              (
                realyPrice(
                  cuisineOptions.find(
                    (item) => item.cuisineId == customerOrderCuisine.cuisineId
                  )
                ) * customerOrderCuisine.num
              ).toFixed(2)
            }}
          </el-col>
          <el-col :span="7" style="display: flex; justify-content: center;justify-items: center;">
            <div
              class="add-btn"
              @click="orderChange(customerOrderCuisine.cuisineId, false)"
            >
              -
            </div>
            <div style="padding:2px 20px">
              {{ customerOrderCuisine.num }}
            </div>
            <div
              class="add-btn"
              @click="orderChange(customerOrderCuisine.cuisineId, true)"
            >
              +
            </div>
          </el-col>
        </el-row>
        <el-row 
          type="flex"
          justify="space-between"
          style="padding: 10px 20px;position: absolute;width: 100%;bottom: 0;;"  >
          <el-col :span="12">
            <strong style="color: #e56534;">￥{{ customerOrder.allPrice }}</strong>
          </el-col>
          <el-col :span="6">
            <el-button class="order-button" @click="placeOrderNow">立即下单</el-button>
          </el-col>
        </el-row>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "Takeout",
  data() {
    return {
      searchVal: "", // 搜索框绑定值，搜索功能后续可结合接口实现
      cuisineOptions: [],
      drawer: false,
      shopDrawer: false,
      cuisineTypeOptions: [],
      orderServiceType: 0,
      shopInfo: {},
      diningTableInfo: {},
      cuisineTypeId: "",
      deliveryInfo: {},
      customerOrder: {
        customerOrderId: "",
        customerid: "",
        staffId: "",
        allPrice: "",
        state: "",
        packingCharges: "",
        deliveryCost: "",
        discountCouponCustomerId: "",
        tablewareNum: "",
        remark: "",
        createTime: "",
        payTime: "",
        source: "",
        type: "",
        diningTableId: "",
        deliveryAddressId: "",
        payType: "",
        delete: "",
        customerOrderCuisines: [],
      },
    };
  },
  methods: {
    // 返回按钮点击事件，跳转到指定路径，这里根据你需求的路径调整，比如回到首页等
    goBack() {
      this.$router.push(`/customer/home/${this.shopInfo.shopId}`);
    },
    // 搜索菜品
    handleSearch() {
      api.cuisine
        .cuisineServiceList(
          this.searchVal,
          this.orderServiceType,
          this.cuisineTypeId,
          this.shopInfo.shopId
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.cuisineOptions = res.data.data;
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    // 类型选择后改变分类
    changeCuisineType(cuisineTypeId) {
      this.cuisineTypeId = cuisineTypeId;
      this.handleSearch();
      this.drawer = false;
    },
    // 处理添加和减少订单的菜品
    orderChange(cuisineId, isAdd) {
      api.customerOrder
        .takeOutAndDineInOrder(cuisineId, this.orderServiceType, isAdd)
        .then((res) => {
          if (res.data.code == 200) {
            this.customerOrder = res.data.data;
            console.log(this.customerOrder);
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    // 获取菜品的真实价格
    realyPrice(cuisine) {
      return cuisine.isSpecialOffer == 1
        ? cuisine.specialOffer != null
          ? cuisine.specialOffer
          : cuisine.price
        : cuisine.price;
    },
    // 处理下单
    placeOrderNow(){
      // 将订单的数据传递到后端，后端创建一个订单，存储在redis中，这样就可以设置一个限时，只有在完成支付后才会存入mysql中
      

    }
  },
  created() {
    if (localStorage.shopInfo) {
      this.shopInfo = JSON.parse(localStorage.shopInfo);
    } else {
      this.$message.error("请先选择门店");
      this.$router.push("/customer/shopList");
    }
  },
  mounted() {
    this.orderServiceType = parseInt(this.$route.params.serverType);
    switch (this.orderServiceType) {
      case 0: // 堂食
        // 获取餐桌的相关信息，如果没有就转跳到扫码的页面
        // 扫码完成后将数据存储在 localStorage.diningTableInfo 中，然后再返回该页面
        if (localStorage.diningTableInfo) {
          this.diningTableInfo = JSON.parse(localStorage.diningTableInfo);
        } else {
          this.$router.push("/customer/scanqrcode");
        }
        break;
      default: // 外卖和自取
        // 获取门店的外卖和自取配置
        api.delivery.infoById(this.shopInfo.shopId).then((res) => {
          if (res.data.code == 200) {
            this.deliveryInfo = res.data.data;
          }
        });
        break;
    }
    // 获取菜品的类型列表
    api.cuisineType.list().then((res) => {
      if (res.data.code == 200) {
        this.cuisineTypeOptions = res.data.data;
      }
    });
    this.handleSearch();
    this.orderChange();
  },
};
</script>

<style scoped>
.order-button{
  color:white;
  background-color: #f87724;
  border-radius: 2rem;
  border:none;
}
.cuisine-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.cuisine-list li {
  padding: 10px;
  border-bottom: 1px solid #ebeef5;
  text-align: center;
}
.takeout-page {
  position: relative;
  min-height: 100vh;
  font-family: Arial, sans-serif;
}
.top-bar {
  padding: 10px 20px;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
}
.back-btn {
  font-size: 20px;
  padding: 0;
}
.search-input {
  width: 100%;
}
.delivery-info {
  border-top: #d8d7d7 1px solid;
  margin-top: 70px;
  height: 50px;
  line-height: 50px;
  padding: 10px 20px;
  background-color: #fff;
}
.delivery-info p {
  margin: 3px 0;
  font-size: 14px;
}
.cuisineOptions-list {
  margin-top: 10px;
  margin-bottom: 80px;
  overflow-y: auto;
  padding: 0 10px;
}
.dish-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  padding: 10px;
}
.dish-img {
  width: 100px;
  height: 80px;
  border-radius: 4px;
  margin-right: 10px;
}
.dish-info {
  flex: 1;
}
.dish-name {
  font-size: 16px;
  margin: 0 0 5px 0;
}
.month-sales {
  font-size: 12px;
  color: #999;
  margin: 0 0 3px 0;
}
.price {
  font-size: 14px;
  color: #f87724;
  margin: 0;
}
.add-btn {
  padding: 5px;
  width: 1rem;
  height: 1rem;
  background-color: #f87724;
  color: #fff;
  border-radius: 50%;
  font-size: 1.5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #fff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  z-index: 10;
}
.category {
  font-size: 1.2rem;
}
.cart {
  display: flex;
  align-items: center;
}
.cart-img {
  width: 30px;
  height: 30px;
  margin-right: 5px;
}
.total-price {
  color: #f87724;
  font-weight: bold;
  font-size: 1.2rem;
}
</style>
