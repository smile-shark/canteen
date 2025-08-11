<template>
  <el-container style="height: 100vh;">
    <!-- 头部区域 -->
    <el-header style="background-color: #f87724; color: #fff; display: flex; justify-content: space-between; align-items: center;">
      <div class="logo">智慧餐厅</div>
      <div class="header-right">
        <span>{{ username }}</span>
        <el-badge :value="notificationCount" class="ml-2">
          <i class="el-icon-bell"></i>
        </el-badge>
        <el-button type="text" @click="logout" class="ml-3">退出登录</el-button>
      </div>
    </el-header>
    <!-- 主体布局，包含侧边栏和主要内容 -->
    <el-container>
      <!-- 左侧侧边栏 -->
      <el-aside width="200px" style="background-color: #fff; border-right: 1px solid #ebeef5;">
        <el-menu
          default-active="/merchant/menu"  
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          router  
        >
          <!-- 首页菜单 -->
          <el-menu-item index="/merchant/menu">
            <i class="el-icon-menu"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          
          <!-- 门店菜单及子菜单 -->
          <el-submenu index="store">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>门店</span>
            </template>
            <el-menu-item index="/merchant/menu/store/management">门店管理</el-menu-item>
            <el-menu-item index="/merchant/menu/store/staff">员工管理</el-menu-item>
            <el-menu-item index="/merchant/menu/store/table">台桌管理</el-menu-item>
            <el-menu-item index="/merchant/menu/store/hardware">硬件管理</el-menu-item>
          </el-submenu>
          
          <!-- 业务菜单及子菜单 -->
          <el-submenu index="business">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>业务</span>
            </template>
            <el-menu-item index="/merchant/menu/business/dishes">菜品</el-menu-item>
            <el-menu-item index="/merchant/menu/business/dinein">堂食</el-menu-item>
            <el-menu-item index="/merchant/menu/business/takeaway">外卖</el-menu-item>
          </el-submenu>
          
          <!-- 库存菜单及子菜单 -->
          <el-submenu index="inventory">
            <template slot="title">
              <i class="el-icon-box"></i>
              <span>库存</span>
            </template>
            <el-menu-item index="/merchant/menu/inventory/dishes">菜品库</el-menu-item>
            <el-menu-item index="/merchant/menu/inventory/ingredients">原料库</el-menu-item>
            <el-menu-item index="/merchant/menu/inventory/check">库存盘点</el-menu-item>
          </el-submenu>
          
          <!-- 订单菜单及子菜单 -->
          <el-submenu index="orders">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>订单</span>
            </template>
            <el-menu-item index="/merchant/menu/orders/dinein">堂食订单</el-menu-item>
            <el-menu-item index="/merchant/menu/orders/takeaway">外卖订单</el-menu-item>
            <el-menu-item index="/merchant/menu/orders/reservation">餐桌预约订单</el-menu-item>
          </el-submenu>
          
          <!-- 会员菜单及子菜单 -->
          <el-submenu index="members">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>会员</span>
            </template>
            <el-menu-item index="/merchant/menu/members/management">会员管理</el-menu-item>
            <el-menu-item index="/merchant/menu/members/categories">会员类别</el-menu-item>
          </el-submenu>
          
          <!-- 促销菜单及子菜单 -->
          <el-submenu index="promotion">
            <template slot="title">
              <i class="el-icon-present"></i>
              <span>促销</span>
            </template>
            <el-menu-item index="/merchant/menu/promotion/coupons">优惠券</el-menu-item>
          </el-submenu>
          
          <!-- 报表菜单及子菜单 -->
          <el-submenu index="reports">
            <template slot="title">
              <i class="el-icon-pie-chart"></i>
              <span>报表</span>
            </template>
            <el-menu-item index="/merchant/menu/reports/dishes-flow">菜品流水</el-menu-item>
            <el-menu-item index="/merchant/menu/reports/payment">收款报表</el-menu-item>
            <el-menu-item index="/merchant/menu/reports/dishes-ranking">菜品销售排行</el-menu-item>
            <el-menu-item index="/merchant/menu/reports/refund">退单记录</el-menu-item>
            <el-menu-item index="/merchant/menu/reports/store-ranking">门店销售排行</el-menu-item>
          </el-submenu>
          
          <!-- 设置菜单及子菜单 -->
          <el-submenu index="settings">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>设置</span>
            </template>
            <el-menu-item index="/merchant/menu/settings/payment">支付设置</el-menu-item>
            <el-menu-item index="/merchant/menu/settings/account">账号设置</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      
      <!-- 主要内容区域，通过路由视图展示对应页面 -->
      <el-main style="padding: 20px; background-color: #f5f7fa; height: calc(100vh - 60px); overflow-y: auto;">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'MerchantMenu',
  data() {
    return {
      username: 'admin',
      notificationCount: 1
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log('子菜单展开：', key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log('子菜单收起：', key, keyPath);
    },
    logout() {
      // 退出登录跳转至商户登录页
      this.$router.push('/merchant/login');
    }
  }
};
</script>

<style scoped>
.el-header {
  padding: 0 20px;
  height: 60px !important;  /* 固定头部高度 */
}
.logo {
  font-size: 20px;
  font-weight: bold;
}
.header-right {
  display: flex;
  align-items: center;
}
.ml-2 {
  margin-left: 10px;
}
.ml-3 {
  margin-left: 15px;
}
.el-aside {
  height: calc(100vh - 60px);  /* 减去头部高度，填满剩余空间 */
}
.el-menu-vertical-demo {
  border-right: none;
  height: 100%;
}
/* 激活菜单样式 */
.el-menu-item.is-active {
  background-color: #f87724 !important;
  color: #fff !important;
}
</style>