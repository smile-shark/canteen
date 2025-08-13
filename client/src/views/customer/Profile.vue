<template>
  <div class="profile-container">
    <!-- 用户信息区域 -->
    <div class="user-info">
      <img src="@/assets/用户头像.png" alt="用户头像" class="avatar" />
      <div class="user-detail">
        <div class="user-name">
          <span>{{ customerInfo.account }}</span>
        </div>
        <div class="user-id"><span class="user-level">
            {{
              customerInfo.level == 0
                ? "青铜会员"
                : customerInfo.level == 1
                ? "白银会员"
                : "黄金会员"
            }}
          </span></div>
      </div>
    </div>

    <!-- 钱包和积分区域 -->
    <div class="wallet-points">
      <div class="wallet-item">
        <div class="amount">{{ walletInfo.balance?.toFixed(2) }}</div>
        <div class="label">钱包</div>
      </div>
      <div class="points-item">
        <div class="amount">{{ customerInfo.integral }}</div>
        <div class="label">积分</div>
      </div>
    </div>

    <!-- 我的订单区域 -->
    <div class="order-section">
      <div class="section-header">
        <span class="section-title">我的订单</span>
        <span
          class="view-all"
          @click="$router.push('/customer/profile/orderlist')"
          >查看全部&gt;</span
        >
      </div>
      <el-row :gutter="20" class="order-types">
        <el-col :span="4" class="order-type">
          <div class="order-icon">
            <i class="el-icon-time"></i>
          </div>
          <div class="order-name">待付款</div>
        </el-col>
        <el-col :span="4" class="order-type">
          <div class="order-icon">
            <i class="el-icon-truck"></i>
          </div>
          <div class="order-name">待收取</div>
        </el-col>
        <el-col :span="4" class="order-type">
          <div class="order-icon">
            <i class="el-icon-check"></i>
          </div>
          <div class="order-name">已完成</div>
        </el-col>
        <el-col :span="4" class="order-type">
          <div class="order-icon">
            <i class="el-icon-close"></i>
          </div>
          <div class="order-name">已取消</div>
        </el-col>
        <el-col :span="4" class="order-type">
          <!-- 为待评价补充 Element-UI 图标，使用 el-icon-star-off 示例，可根据喜好替换 -->
          <div class="order-icon">
            <i class="el-icon-star-off"></i>
          </div>
          <div class="order-name">待评价</div>
        </el-col>
      </el-row>
    </div>

    <!-- 我的预约区域 -->
    <div class="reservation-section">
      <div class="section-header">
        <span class="section-title">我的预约</span>
        <span
          class="view-all"
          @click="$router.push('/customer/profile/reservationlist')"
          >查看全部&gt;</span
        >
      </div>
      <el-row :gutter="20" class="reservation-types">
        <el-col :span="6" class="reservation-type">
          <!-- 为等待确认补充 Element-UI 图标，使用 el-icon-loading 示例，可根据喜好替换 -->
          <div class="reservation-icon">
            <i class="el-icon-loading"></i>
          </div>
          <div class="reservation-name">等待确认</div>
        </el-col>
        <el-col :span="6" class="reservation-type">
          <div class="reservation-icon">
            <i class="el-icon-check"></i>
          </div>
          <div class="reservation-name">已完成</div>
        </el-col>
        <el-col :span="6" class="reservation-type">
          <div class="reservation-icon">
            <i class="el-icon-close"></i>
          </div>
          <div class="reservation-name">已取消</div>
        </el-col>
      </el-row>
    </div>

    <!-- 常用工具区域 -->
    <div class="tools-section">
      <div class="section-header">
        <span class="section-title">常用工具</span>
      </div>
      <el-row :gutter="20" class="tools">
        <el-col
          :span="6"
          class="tool-item"
          @click.native="$router.push('/customer/profile/addressmanagement')"
        >
          <div class="tool-icon">
            <i class="el-icon-location-outline"></i>
          </div>
          <div class="tool-name">地址管理</div>
        </el-col>
        <el-col
          :span="6"
          class="tool-item"
          @click.native="$router.push('/customer/profile/coupons')"
        >
          <!-- 为优惠券补充 Element-UI 图标，使用 el-icon-tickets 示例，可根据喜好替换 -->
          <div class="tool-icon">
            <i class="el-icon-tickets"></i>
          </div>
          <div class="tool-name">优惠券</div>
        </el-col>
        <el-col :span="6" class="tool-item">
          <div class="tool-icon">
            <i class="el-icon-service"></i>
          </div>
          <div class="tool-name">客服</div>
        </el-col>
        <el-col :span="6" class="tool-item">
          <div class="tool-icon">
            <i class="el-icon-edit-outline"></i>
          </div>
          <div class="tool-name">意见反馈</div>
        </el-col>
      </el-row>
    </div>
    <!-- 退出账号 -->
    <div class="tools-section">
      <div class="section-header">
        <span class="section-title">退出账号</span>
      </div>
      <el-row :gutter="20" class="tools">
        <el-col
          :span="6"
          class="tool-item"
          @click.native="$router.push('/customer/login')"
        >
          <div class="tool-icon">
            <i class="el-icon-back"></i>
          </div>
          <div class="tool-name" >退出账号</div>
        </el-col>
      </el-row>
    </div>

    <div style="height: 20vh"></div>
  </div>
</template>

<script>
import api from '@/api';

export default {
  name: "Profile",
  data() {
    return {
      customerInfo: {},
      walletInfo:{}
    };
  },
  created() {
    if (localStorage.customerInfo) {
      this.customerInfo = JSON.parse(localStorage.customerInfo);
    } else {
      this.$message.error("请先登录");
      this.$router.push("/customer/login");
    }
    api.wallet.infoById(this.customerInfo.walletId).then(res=>{
      if(res.data.code==200){
        this.walletInfo=res.data.data;
        console.log(this.walletInfo);
        
      }
    })
  },
};
</script>

<style scoped>
/* 基础样式 */
.profile-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 用户信息样式 */
.user-info {
  display: flex;
  align-items: center;
  background-color: #ff6f40;
  padding: 20px;
  color: white;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.user-detail {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.user-name {
  font-size: 18px;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
}

.user-level {
  font-size: 14px;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 12px;
  margin-left: 10px;
}

.user-id {
  font-size: 14px;
  opacity: 0.9;
}

/* 钱包和积分样式 */
.wallet-points {
  display: flex;
  background-color: #ff6f40;
  padding: 0 20px 20px;
  color: white;
}

.wallet-item,
.points-item {
  flex: 1;
  text-align: center;
}

.amount {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 5px;
}

.label {
  font-size: 14px;
  opacity: 0.9;
}

/* 通用区块样式 */
.order-section,
.reservation-section,
.tools-section {
  background-color: white;
  margin: 10px;
  border-radius: 8px;
  padding: 15px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.view-all {
  font-size: 14px;
  color: #999;
  cursor: pointer;
}

/* 订单类型样式 */
.order-types,
.reservation-types,
.tools {
  padding: 10px 0;
}

.order-type,
.reservation-type,
.tool-item {
  text-align: center;
  cursor: pointer;
}

.order-icon,
.reservation-icon,
.tool-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f5f5f5;
  color: #ff6f40;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 8px;
  font-size: 20px;
}

.order-name,
.reservation-name,
.tool-name {
  font-size: 14px;
  color: #666;
}
</style>
