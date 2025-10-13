<template>
  <div class="payment-order-container">
    <!-- 头部返回与标题 -->
    <div class="header">
      <span class="back-arrow" @click="goBack()">&lt;</span>
      <span class="title">支付订单</span>
    </div>
    <!-- 金额与提示信息 -->
    <div class="amount-section">
      <div class="amount" v-if="customerOrderInfo">
        ¥
        {{
          customerOrderInfo.allPrice +
          customerOrderInfo.packingCharges +
          customerOrderInfo.deliveryCost
        }}
      </div>
      <div class="tip">请尽快完成支付，放弃支付订单将被取消</div>
    </div>
    <!-- 支付方式选择 -->
    <div class="payment-methods">
      <label
        class="method-item"
        :class="{ active: selectedMethod == 4 }"
      >
        <input
          type="radio"
          name="paymentMethod"
          :value="4"
          v-model="selectedMethod"
        />
        <img src="@/assets/walletPay.png" alt="钱包图片" class="method-icon" />
        <span>余额支付</span>
      </label>
      <label
        class="method-item"
        :class="{ active: selectedMethod ==1}"
      >
        <input
          type="radio"
          name="paymentMethod"
          :value="1"
          v-model="selectedMethod"
        />
        <!-- 使用在线支付宝图标 -->
        <img src="@/assets/Alipay.png" alt="支付宝图标" class="method-icon" />
        <span>支付宝支付</span>
      </label>
      <label
        class="method-item"
        :class="{ active: selectedMethod == 0 }"
      >
        <input
          type="radio"
          name="paymentMethod"
          :value="0"
          v-model="selectedMethod"
        />
        <!-- 使用在线微信图标 -->
        <img src="@/assets/WeChatPay.png" alt="微信图标" class="method-icon" />
        <span>微信支付</span>
      </label>
    </div>
    <!-- 立即支付按钮 -->
    <button class="pay-btn" @click="handlePay">立即支付</button>
  </div>
</template>

<script>
import api from "@/api";

export default {
  data() {
    return {
      amount: 151,
      selectedMethod: 4,
      customerOrderInfo: null,
    };
  },
  methods: {
    handlePay() {
     api.customerOrder.payOrder(this.customerOrderInfo.customerOrderId,this.selectedMethod).then(res=>{
        if(res.data.code==200){
            this.$message.success("支付成功");
            this.$router.push('/customer/profile/orderlist')
        }else{
            this.$message.error(res.data.msg);
        }
     })
    },
    goBack() { 
        // 返回上一页
        this.$router.go(-1);
    },
  },
  created() {
    // 接收传递的id
    let customerOrderId = this.$route.query.customerOrderId;
    if (!customerOrderId) {
      this.$message.error("未传递订单信息");
      this.$router.push({
        path: "/customer/home/orderService",
      });
      return;
    }
    // 获取订单信息
    api.customerOrder.placeOrderGet(customerOrderId).then((res) => {
      if (res.data.code == 200) {
        this.customerOrderInfo = res.data.data;
        if(!this.customerOrderInfo.customerOrderId){
            this.customerOrderInfo.customerOrderId=customerOrderId;
        }
      } else {
        this.$message.error(res.data.msg);
      }
    });
  },
};
</script>

<style scoped>
.payment-order-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 16px;
  box-sizing: border-box;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.back-arrow {
  font-size: 20px;
  margin-right: 8px;
  cursor: pointer;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.amount-section {
  text-align: center;
  margin-bottom: 32px;
}

.amount {
  font-size: 36px;
  color: #333;
  margin-bottom: 8px;
}

.tip {
  font-size: 14px;
  color: #999;
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 32px;
}

.method-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.method-item.active {
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
}

.method-icon {
  width: 24px;
  height: 24px;
  margin-right: 8px;
}

.pay-btn {
  width: 100%;
  background-color: #f26522;
  color: #fff;
  font-size: 16px;
  padding: 12px 0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pay-btn:hover {
  background-color: #e05b1f;
}
</style>
