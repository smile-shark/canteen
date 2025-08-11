<template>
  <div class="order-list-container">
    <!-- 返回按钮和页面标题 -->
    <div class="header-container">
      <el-button 
        type="text" 
        icon="el-icon-back" 
        class="back-btn"
        @click="$router.push('/customer/profile')"
      ></el-button>
      <div class="page-title">我的订单</div>
    </div>

    <!-- 订单类型筛选标签页 -->
    <el-tabs 
      v-model="activeTab" 
      type="card" 
      class="order-tabs"
      @tab-click="handleTabChange"
    >
      <el-tab-pane label="待付款" name="pendingPayment"></el-tab-pane>
      <el-tab-pane label="待收取" name="pendingReceipt"></el-tab-pane>
      <el-tab-pane label="已完成" name="completed"></el-tab-pane>
      <el-tab-pane label="已取消" name="canceled"></el-tab-pane>
      <el-tab-pane label="待评价" name="pendingReview"></el-tab-pane>
    </el-tabs>

    <!-- 订单列表渲染区域 -->
    <div class="order-items">
      <!-- 无数据提示 -->
      <el-empty 
        v-if="filteredOrders.length === 0" 
        description="暂无相关订单"
        class="empty-state"
      ></el-empty>

      <!-- 订单卡片列表 -->
      <el-card 
        v-for="(order, index) in filteredOrders" 
        :key="index" 
        shadow="hover"
        class="order-card"
      >
        <!-- 订单头部信息 -->
        <div slot="header" class="order-header">
          <div class="order-number">订单编号: {{ order.orderNo }}</div>
          <div class="order-status" :class="getStatusClass(order.statusText)">{{ order.statusText }}</div>
        </div>

        <!-- 订单商品信息 -->
        <div class="order-goods">
          <div 
            v-for="(goods, gIndex) in order.goodsList" 
            :key="gIndex" 
            class="goods-item"
          >
            <img :src="goods.imageUrl" alt="商品图片" class="goods-image">
            <div class="goods-info">
              <div class="goods-name">{{ goods.name }}</div>
              <div class="goods-price">¥{{ goods.price.toFixed(2) }}</div>
              <div class="goods-quantity">x{{ goods.quantity }}</div>
            </div>
          </div>
        </div>

        <!-- 订单金额信息 -->
        <div class="order-amount">
          <span>合计: </span>
          <span class="amount-value">¥{{ order.totalAmount.toFixed(2) }}</span>
        </div>

        <!-- 订单操作按钮 -->
        <div class="order-actions">
          <el-button 
            v-if="order.status === 'pendingPayment'"
            type="text" 
            class="action-btn cancel-btn"
            @click="handleCancelOrder(order.id)"
          >
            取消订单
          </el-button>
          <el-button 
            v-if="order.status === 'pendingPayment'"
            type="primary" 
            class="action-btn pay-btn"
            @click="handlePayOrder(order.id)"
          >
            立即付款
          </el-button>
          <el-button 
            v-if="order.status === 'pendingReview'"
            type="primary" 
            class="action-btn review-btn"
            @click="handleReviewOrder(order.id)"
          >
            去评价
          </el-button>
          <el-button 
            type="text" 
            class="action-btn detail-btn"
            @click="handleViewDetail(order.id)"
          >
            查看详情
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomerOrderList',
  data() {
    return {
      // 当前激活的标签页
      activeTab: 'pendingPayment',
      // 所有订单数据，商品图片统一使用指定链接
      allOrders: [
        {
          id: 1,
          orderNo: 'ORD20230801001',
          status: 'pendingPayment',
          statusText: '待付款',
          totalAmount: 158.00,
          createTime: '2023-08-01 12:30:45',
          goodsList: [
            {
              name: '招牌烤肉套餐',
              price: 128.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            },
            {
              name: '可乐(中杯)',
              price: 10.00,
              quantity: 3,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            }
          ]
        },
        {
          id: 2,
          orderNo: 'ORD20230801002',
          status: 'pendingReceipt',
          statusText: '待收取',
          totalAmount: 88.00,
          createTime: '2023-08-01 10:15:30',
          goodsList: [
            {
              name: '宫保鸡丁',
              price: 38.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            },
            {
              name: '米饭',
              price: 2.00,
              quantity: 2,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            }
          ]
        },
        {
          id: 3,
          orderNo: 'ORD20230731005',
          status: 'completed',
          statusText: '已完成',
          totalAmount: 68.00,
          createTime: '2023-07-31 19:45:12',
          goodsList: [
            {
              name: '鱼香肉丝',
              price: 28.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            },
            {
              name: '酸辣土豆丝',
              price: 16.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            }
          ]
        },
        {
          id: 4,
          orderNo: 'ORD20230731003',
          status: 'canceled',
          statusText: '已取消',
          totalAmount: 128.00,
          createTime: '2023-07-31 15:20:08',
          goodsList: [
            {
              name: '麻辣香锅(中份)',
              price: 128.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            }
          ]
        },
        {
          id: 5,
          orderNo: 'ORD20230730002',
          status: 'pendingReview',
          statusText: '待评价',
          totalAmount: 98.00,
          createTime: '2023-07-30 20:10:55',
          goodsList: [
            {
              name: '糖醋排骨',
              price: 58.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            },
            {
              name: '手撕包菜',
              price: 18.00,
              quantity: 1,
              imageUrl: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/外卖点餐/u181.png'
            }
          ]
        }
      ]
    };
  },
  computed: {
    // 根据当前标签筛选订单
    filteredOrders() {
      return this.allOrders.filter(order => order.status === this.activeTab);
    }
  },
  methods: {
    // 处理标签页切换
    handleTabChange(tab) {
      console.log('切换到订单状态:', tab.name);
      // 实际项目中可以在这里触发数据刷新
      // this.fetchOrders(tab.name);
    },

    // 获取订单状态样式类
    getStatusClass(statusText) {
      const classMap = {
        '待付款': 'status-pending',
        '待收取': 'status-delivering',
        '已完成': 'status-completed',
        '已取消': 'status-canceled',
        '待评价': 'status-review'
      };
      return classMap[statusText] || '';
    },

    // 取消订单
    handleCancelOrder(orderId) {
      this.$confirm('确定要取消订单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实际项目中调用接口取消订单
        this.$message.success('订单已取消');
        // 这里可以更新订单状态或重新获取订单列表
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    },

    // 支付订单
    handlePayOrder(orderId) {
      // 实际项目中跳转到支付页面
      this.$message.success('跳转到支付页面');
      // this.$router.push(`/payment/${orderId}`);
    },

    // 评价订单
    handleReviewOrder(orderId) {
      // 实际项目中跳转到评价页面
      this.$message.success('跳转到评价页面');
      // this.$router.push(`/review/${orderId}`);
    },

    // 查看订单详情
    handleViewDetail(orderId) {
      // 实际项目中跳转到详情页面
      this.$message.success(`查看订单 ${orderId} 详情`);
      // this.$router.push(`/order-detail/${orderId}`);
    }
  },
  created() {
    // 组件创建时获取订单数据
    // 实际项目中调用接口
    // this.fetchOrders(this.activeTab);
  }
};
</script>

<style scoped>
.order-list-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.back-btn {
  margin-right: 10px;
  color: #666;
  font-size: 18px;
}

.back-btn:hover {
  color: #333;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.order-tabs {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.order-card {
  border-radius: 8px;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-number {
  color: #666;
  font-size: 14px;
}

.order-status {
  font-size: 14px;
  font-weight: 500;
}

.status-pending {
  color: #ff9800;
}

.status-delivering {
  color: #2196f3;
}

.status-completed {
  color: #4caf50;
}

.status-canceled {
  color: #f44336;
}

.status-review {
  color: #9c27b0;
}

.order-goods {
  padding: 15px 0;
  border-top: 1px dashed #eee;
  border-bottom: 1px dashed #eee;
}

.goods-item {
  display: flex;
  margin-bottom: 10px;
  align-items: center;
}

.goods-item:last-child {
  margin-bottom: 0;
}

.goods-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.goods-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goods-name {
  color: #333;
  font-weight: 500;
}

.goods-price {
  color: #f56c6c;
  font-weight: bold;
}

.goods-quantity {
  color: #666;
  margin-left: 10px;
}

.order-amount {
  padding: 15px 0;
  text-align: right;
  font-size: 16px;
}

.amount-value {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 10px;
}

.action-btn {
  padding: 5px 15px;
}

.cancel-btn {
  color: #f56c6c;
}

.review-btn {
  background-color: #ff9800;
  border-color: #ff9800;
}

.review-btn:hover {
  background-color: #fb8c00;
  border-color: #fb8c00;
}

.empty-state {
  margin: 50px 0;
}
</style>