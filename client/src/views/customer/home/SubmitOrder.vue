<template>
  <el-container style="height: 100vh; background-color: #fff;">
    <!-- 头部区域：返回按钮 + 标题、订单编号 -->
    <el-header style="height: auto; padding: 0;">
      <el-row :gutter="0" style="border-bottom: 1px solid #f2f2f2;">
        <el-col :span="24">
          <div style="display: flex; align-items: center; padding: 10px 15px;">
            <i 
              class="el-icon-arrow-left" 
              style="cursor: pointer;"
              @click="goBack"
            ></i>
            <span style="margin-left: 5px;">确认订单</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="0" style="padding: 15px;">
        <el-col :span="24">
          <div style="text-align: center; font-size: 16px; color: #666;" v-if="customerOrder.type==0">
            {{ diningTableInfo.name }}
          </div>
        </el-col>
      </el-row>
    </el-header>

    <!-- 主体内容：餐厅名称、菜品列表、费用明细、备注、合计 -->
    <el-main style="padding: 15px;">
      <!-- 餐厅名称 -->
      <el-row :gutter="0" style="margin-bottom: 15px;">
        <el-col :span="24">
          <div style="font-size: 16px; font-weight: 500;">
            智慧餐厅（XXX店）
          </div>
        </el-col>
      </el-row>

      <!-- 菜品列表 -->
      <el-row 
        :gutter="10" 
        style="margin-bottom: 20px;"
        v-for="(item, index) in customerOrder.customerOrderCuisines" 
        :key="index"
      >
        <el-col :span="24">
          <div style="display: flex; align-items: center; padding: 10px; border: 1px solid #f2f2f2; border-radius: 4px;">
            <img 
              :src="item.cuisine.image" 
              alt="dish" 
              class="dish-img" 
            />
            <div class="dish-info" style="flex: 1;">
              <el-row :gutter="0" style="margin-bottom: 5px;">
                <el-col :span="18">
                  <span style="font-size: 16px; font-weight: 500;">{{ item.cuisine.name }}</span>
                </el-col>
                <el-col :span="6" style="text-align: right;">
                  <span style="font-size: 16px; color: #f60;">￥{{ realyPrice(item.cuisine) }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="0">
                <el-col :span="24">
                  <span style="font-size: 16px; color: #999;">x{{ item.num }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 费用明细 -->
      <el-row :gutter="0" style="margin-bottom: 10px;" v-if="customerOrder.type==1||customerOrder.type==2">
        <el-col :span="12">
          <div style="font-size: 16px;">打包费</div>
        </el-col>
        <el-col :span="12" style="text-align: right;" >
          <div style="font-size: 16px;">￥3</div>
        </el-col>
      </el-row>
      <el-row :gutter="0" style="margin-bottom: 10px;" v-if="customerOrder.type==1">
        <el-col :span="12">
          <div style="font-size: 16px;">配送费</div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div style="font-size: 16px;">￥0</div>
        </el-col>
      </el-row>
      <el-row :gutter="0" style="margin-bottom: 20px;">
        <el-col :span="12">
         <div>
          <div style="display: inline-block;background-color: #e56534;font-weight: bold;color: white;padding:7px 10px;border-radius: 20%;">券</div>
          优惠券</div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div style="font-size: 16px;">￥0
            <i class="el-icon-arrow-right" style="color: #c3c3c3;font-weight: bold;"></i>
          </div>
        </el-col>
      </el-row>
      <!-- 备注 -->
      <el-row :gutter="0" style="margin: 15px 0;">
        <el-col :span="24">
          <div style="font-size: 16px; margin-bottom: 5px;">备注</div>
          <el-input
          :rows="5"
            type="textarea"
            placeholder="请输入备注信息"
            style="width: 100%;"
          ></el-input>
        </el-col>
      </el-row>

      <!-- 合计与提交订单 -->
      <el-row :gutter="0" style="position: absolute;width: 96%;bottom: 0;">
        <el-col :span="17">
          <div style="font-size: 16px; font-weight: bold;padding-top: 10px;">
            合计 <span style="color: #f60;">￥{{ customerOrder.allPrice }}</span>
          </div>
        </el-col>
        <el-col :span="7" style="text-align: center;background-color: #f60;color: white;padding:10px 20px;"
            @click="submitOrder">
            提交订单
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import api from '@/api';

export default {
  data() {
    return {
      customerOrder: {},
      diningTableInfo:{}
    };
  },
  methods: {
    // 返回上一页
    goBack() {
      this.$router.back();
    },
    // 获取菜品的真实价格
    realyPrice(cuisine) {
      return cuisine.isSpecialOffer === 1 
        ? cuisine.specialOffer !== null 
          ? cuisine.specialOffer 
          : cuisine.price 
        : cuisine.price;
    },
    // 提交订单方法，可根据实际需求对接接口
    submitOrder() {
      // 可在这里编写调用提交订单接口的逻辑
      console.log('提交订单，订单数据：', this.customerOrder);
      // 示例：调用 api 提交订单
      // api.order.submit(this.customerOrder).then(res => {
      //   if (res.data.code === 200) {
      //     this.$message.success('订单提交成功');
      //     // 提交成功后的逻辑，比如跳转到支付页等
      //   } else {
      //     this.$message.error('订单提交失败，请重试');
      //   }
      // });
    }
  },
  created() {
    this.diningTableInfo=JSON.parse(localStorage.diningTableInfo)
    // 接收路由参数
    this.customerOrder = this.$route.query;
    if (!this.customerOrder || !this.customerOrder.customerOrderCuisines) {
      this.$message.error('参数传递错误');
      return;
    }
    // 遍历菜品，补充菜品详情
    const promiseArr = [];
    this.customerOrder.customerOrderCuisines.forEach(customerOrderCuisine => {
      promiseArr.push(
        api.cuisine.getSimpleCuisineById(customerOrderCuisine.cuisineId).then(res => {
          if (res.data.code === 200) {
            customerOrderCuisine.cuisine = res.data.data;
          }
        })
      );
    });
  }
};
</script>

<style scoped>
.dish-img {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  margin-right: 10px;
}
/* 去除 Element UI 自带的一些默认样式影响，可根据实际情况调整 */
.el-header, .el-main, .el-footer {
  padding: 0;
}
.el-input textarea {
  min-height: 60px;
  resize: none;
}
</style>