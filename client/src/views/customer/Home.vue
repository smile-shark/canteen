<template>
  <div>
    <!-- 切换餐厅 -->
     <div style="background-color: #f5f5f5;font-size: 1.4rem;font-weight: bold;padding:1rem" @click="$router.push('/customer/shopList')">
      <i class="el-icon-arrow-left">门店选择</i>
     </div>


    <!-- 餐厅信息 -->
    <div class="restaurant-info">
      <img src="@/assets/logo.png" alt="Logo" class="logo">
      <div class="info">
        <h2>{{ shopInfo.name }}</h2>
        <p style="color:#b9b9b9">中餐 | {{shopInfo.openingHoursStart}}-{{shopInfo.openingHoursEnd}}</p>
        <p><span>{{shopInfo.address}}</span> <a href="#" class="map-link">地图</a></p>
      </div>
    </div>

    <!-- 轮播图 -->
    <van-swipe :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="index in 4" :key="index">
        <img src="@/assets/lb.png" alt="轮播图" class="swipe-image">
      </van-swipe-item>
    </van-swipe>

    <!-- 功能按钮 -->
    <div class="function-buttons">
      <div class="button-row">
        <van-button type="danger" icon="shop-o" @click="$router.push('/customer/home/orderservice/1')" :disabled="Boolean(shopInfo.isTakeOut)">外卖点餐</van-button>
        <van-button type="primary" icon="balance-list" @click="$router.push('/customer/home/orderservice/0')" :disabled="Boolean(shopInfo.isDineIn)">堂食点餐</van-button>
      </div>
      <div class="button-row">
        <van-button type="info" icon="friends-o" @click="$router.push('/customer/home/orderservice/2')" :disabled="Boolean(shopInfo.isDineIn)">自提点餐</van-button>
        <van-button type="warning" icon="clock-o" @click="$router.push('/customer/home/tablereservation')">餐桌预约</van-button>
      </div>
    </div>

    <div style="height: 60px;">&nbsp;</div>
  </div>
</template>

<script>
import api from '@/api';
import { Swipe, SwipeItem, Button } from 'vant';

export default {
  components: {
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Button.name]: Button
  },
  data(){
    return {
      shopInfo:{}
    }
  },
  methods: {
    
  },
  created(){
   api.shop.infoById(this.$route.params.shopId).then(res=>{
    if(res.data.code==200){
      this.shopInfo=res.data.data;
      localStorage.setItem('shopInfo',JSON.stringify(this.shopInfo));
    }
   }) 
  }
};
</script>

<style scoped>
.restaurant-info {
  font-size: 1rem;
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #f5f5f5;
}

.logo {
  width: 60px;
  height: 60px;
  margin-right: 10px;
}

.info {
  flex: 1;
}

.map-link {
  color: orange;
}

.call-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #6699ff;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}

.swipe-image {
  width: 100%;
  height: auto;
}

.function-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.button-row {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 10px;
}

.function-buttons button{
  padding:40px 50px;
  font-size: 1.2rem;}
</style>