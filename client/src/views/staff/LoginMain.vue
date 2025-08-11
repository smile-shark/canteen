<template>
  <div class="function-panel">
    <!-- 服务按钮区域 -->
    <div class="function-item">
      <el-button
        @click="$router.push('/staff/login/1')"
        type="primary"
        icon="el-icon-user"
        circle
        size="large"
        class="service-btn"
      ></el-button>
      <p class="function-text">服务</p>
    </div>
    <!-- 收银、后厨按钮区域 -->
    <div class="bottom-functions">
      <div class="function-item">
        <el-button
          @click="$router.push('/staff/login/3')"
          type="info"
          icon="el-icon-money"
          circle
          size="large"
          class="cashier-btn"
        ></el-button>
        <p class="function-text">收银</p>
      </div>
      <div class="function-item">
        <el-button
          @click="$router.push('/staff/login/0')"
          type="warning"
          icon="el-icon-menu"
          circle
          size="large"
          class="kitchen-btn"
        ></el-button>
        <p class="function-text">后厨</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FunctionPanel",
};
</script>

<style scoped>
/* 页面整体布局，让内容垂直居中，分散空白区域 */
.function-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #fff;
  padding: 0 20px;
}
/* 单个功能项样式，控制按钮和文字的布局 */
.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80px; /* 增加按钮间垂直间距以适应放大后的按钮 */
}
/* 按钮基础样式 - 放大3倍 */
.service-btn,
.cashier-btn,
.kitchen-btn {
  width: 150px !important; /* 原尺寸约50px，放大3倍 */
  height: 150px !important; /* 原尺寸约50px，放大3倍 */
  font-size: 60px !important; /* 图标字体大小放大3倍 */
  padding: 0 !important;
}
/* 服务按钮颜色 */
.service-btn {
  background-color: #f89e82;
  border-color: #f89e82;
}
/* 收银按钮颜色 */
.cashier-btn {
  background-color: #87d0e2;
  border-color: #87d0e2;
}
/* 后厨按钮颜色 */
.kitchen-btn {
  background-color: #f6d390;
  border-color: #f6d390;
}
/* 功能文字样式，同步放大 */
.function-text {
  margin-top: 20px;
  color: #333;
  font-size: 28px; /* 原14px，放大2倍以配合按钮尺寸 */
  font-weight: 500;
}
/* 底部两个功能按钮的容器，调整间距以适应放大后的按钮 */
.bottom-functions {
  display: flex;
  justify-content: space-around;
  width: 100%;
  max-width: 600px; /* 原300px，放大2倍 */
}
</style>
