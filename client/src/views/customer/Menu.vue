<template>
  <div>
    <div>
        <router-view></router-view>
    </div>
    <van-tabbar v-model="active" @change="onChange" style="border-top: 1px solid #eee;">
      <van-tabbar-item :to="homePath()" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/customer/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>

  </div>
</template>

<script>
import { Tabbar, TabbarItem } from 'vant';

export default {
  components: {
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem
  },
  data() {
    return {
      active: 0
    };
  },
  methods: {
    onChange(index) {
      // 处理标签页切换逻辑
      console.log('Tab changed to:', index);
    },
    homePath(){
      if(localStorage.shopId){
        return `/customer/home/${localStorage.shopId}`
      }else{
        return `/customer/home/${JSON.parse(localStorage.shopInfo).shopId}`
      }
    }
  }
};
</script>

<style scoped>
* {
  font-size: 1.5rem;
}
</style>