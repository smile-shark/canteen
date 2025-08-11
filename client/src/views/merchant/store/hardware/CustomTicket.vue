<template>
  <div class="custom-ticket">
    <!-- 操作按钮 -->
    <div class="operation-buttons">
      <el-button type="primary" @click="openEditDialog">
        <i class="el-icon-edit"></i> 编辑小票模板
      </el-button>
      <el-button type="success" @click="previewTicket">
        <i class="el-icon-view"></i> 预览
      </el-button>
      <el-button type="warning" @click="saveAsDefault">
        <i class="el-icon-save"></i> 保存为默认
      </el-button>
    </div>

    <!-- 小票类型切换 -->
    <el-tabs v-model="activeTicketType" type="card" @tab-click="handleTypeChange">
      <el-tab-pane label="堂食小票" name="dining"></el-tab-pane>
      <el-tab-pane label="外卖小票" name="takeout"></el-tab-pane>
      <el-tab-pane label="充值小票" name="recharge"></el-tab-pane>
      <el-tab-pane label="后厨小票" name="kitchen"></el-tab-pane>
    </el-tabs>

    <!-- 小票预览区域 -->
    <el-card shadow="hover" class="ticket-preview-card">
      <div class="ticket-preview-container">
        <div class="ticket-frame">
          <img 
            :src="currentTicketImage" 
            alt="小票预览" 
            class="ticket-image"
          >
        </div>
      </div>
    </el-card>

    <!-- 编辑弹窗（根据小票类型动态展示配置项） -->
    <el-dialog 
      :title="`编辑${ticketTypeMap[activeTicketType]}模板`" 
      :visible.sync="editDialogVisible" 
      width="700px"
      append-to-body
    >
      <el-form :model="currentTicketConfig" label-width="140px" ref="ticketForm">
        <!-- 通用配置项 - 基本信息 -->
        <el-collapse v-model="expandedPanels">
          <el-collapse-item title="基本信息" name="basic">
            <el-form-item label="标题显示">
              <el-checkbox-group v-model="currentTicketConfig.showTitle">
                <el-checkbox label="storeName">门店名称</el-checkbox>
                <el-checkbox label="welcomeText">欢迎语</el-checkbox>
                <el-checkbox label="ticketType">票据类型</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="欢迎语内容" v-if="currentTicketConfig.showTitle.includes('welcomeText')">
              <el-input v-model="currentTicketConfig.welcomeText" placeholder="请输入欢迎语"></el-input>
            </el-form-item>
            
            <el-form-item label="字体大小">
              <el-select v-model="currentTicketConfig.fontSize">
                <el-option label="小" value="small"></el-option>
                <el-option label="中" value="medium"></el-option>
                <el-option label="大" value="large"></el-option>
              </el-select>
            </el-form-item>
          </el-collapse-item>

          <!-- 订单信息配置（根据类型展示不同项） -->
          <el-collapse-item title="订单信息" name="order">
            <template v-if="activeTicketType === 'dining'">
              <el-form-item label="显示内容">
                <el-checkbox-group v-model="currentTicketConfig.orderInfo">
                  <el-checkbox label="tableNumber">桌号</el-checkbox>
                  <el-checkbox label="orderNumber">订单编号</el-checkbox>
                  <el-checkbox label="orderTime">下单时间</el-checkbox>
                  <el-checkbox label="serverName">服务员</el-checkbox>
                  <el-checkbox label="customerRemark">顾客备注</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </template>

            <template v-if="activeTicketType === 'takeout'">
              <el-form-item label="显示内容">
                <el-checkbox-group v-model="currentTicketConfig.orderInfo">
                  <el-checkbox label="orderNumber">订单编号</el-checkbox>
                  <el-checkbox label="orderTime">下单时间</el-checkbox>
                  <el-checkbox label="deliveryTime">预计送达时间</el-checkbox>
                  <el-checkbox label="customerName">顾客姓名</el-checkbox>
                  <el-checkbox label="customerPhone">顾客电话</el-checkbox>
                  <el-checkbox label="deliveryAddress">配送地址</el-checkbox>
                  <el-checkbox label="customerRemark">顾客备注</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </template>

            <template v-if="activeTicketType === 'recharge'">
              <el-form-item label="显示内容">
                <el-checkbox-group v-model="currentTicketConfig.orderInfo">
                  <el-checkbox label="cardNumber">卡号</el-checkbox>
                  <el-checkbox label="customerName">顾客姓名</el-checkbox>
                  <el-checkbox label="rechargeTime">充值时间</el-checkbox>
                  <el-checkbox label="rechargeAmount">充值金额</el-checkbox>
                  <el-checkbox label="giveAmount">赠送金额</el-checkbox>
                  <el-checkbox label="balance">当前余额</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </template>

            <template v-if="activeTicketType === 'kitchen'">
              <el-form-item label="显示内容">
                <el-checkbox-group v-model="currentTicketConfig.orderInfo">
                  <el-checkbox label="tableNumber">桌号</el-checkbox>
                  <el-checkbox label="orderNumber">订单编号</el-checkbox>
                  <el-checkbox label="orderTime">下单时间</el-checkbox>
                  <el-checkbox label="dishDetails">菜品详情</el-checkbox>
                  <el-checkbox label="cookRemark">厨师备注</el-checkbox>
                  <el-checkbox label="priority">优先级</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </template>
          </el-collapse-item>

          <!-- 商品信息配置 -->
          <el-collapse-item title="商品信息" name="goods" v-if="['dining', 'takeout', 'kitchen'].includes(activeTicketType)">
            <el-form-item label="显示内容">
              <el-checkbox-group v-model="currentTicketConfig.goodsInfo">
                <el-checkbox label="name">商品名称</el-checkbox>
                <el-checkbox label="quantity">数量</el-checkbox>
                <el-checkbox label="price">单价</el-checkbox>
                <el-checkbox label="total">小计</el-checkbox>
                <el-checkbox label="specification">规格</el-checkbox>
                <el-checkbox label="cookMethod">烹饪方式</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-collapse-item>

          <!-- 支付信息配置 -->
          <el-collapse-item title="支付信息" name="payment" v-if="['dining', 'takeout', 'recharge'].includes(activeTicketType)">
            <el-form-item label="显示内容">
              <el-checkbox-group v-model="currentTicketConfig.paymentInfo">
                <el-checkbox label="paymentMethod">支付方式</el-checkbox>
                <el-checkbox label="totalAmount">总金额</el-checkbox>
                <el-checkbox label="discountAmount">优惠金额</el-checkbox>
                <el-checkbox label="paidAmount">实付金额</el-checkbox>
                <el-checkbox label="paymentTime">支付时间</el-checkbox>
                <el-checkbox label="cashier">收银员</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-collapse-item>

          <!-- 底部信息配置 -->
          <el-collapse-item title="底部信息" name="footer">
            <el-form-item label="显示内容">
              <el-checkbox-group v-model="currentTicketConfig.footerInfo">
                <el-checkbox label="storeAddress">门店地址</el-checkbox>
                <el-checkbox label="storePhone">门店电话</el-checkbox>
                <el-checkbox label="businessHours">营业时间</el-checkbox>
                <el-checkbox label="thanksText">感谢语</el-checkbox>
                <el-checkbox label="qrCode">二维码</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="感谢语内容" v-if="currentTicketConfig.footerInfo.includes('thanksText')">
              <el-input v-model="currentTicketConfig.thanksText" placeholder="请输入感谢语"></el-input>
            </el-form-item>
            
            <el-form-item label="二维码类型" v-if="currentTicketConfig.footerInfo.includes('qrCode')">
              <el-select v-model="currentTicketConfig.qrCodeType">
                <el-option label="公众号" value="official"></el-option>
                <el-option label="小程序" value="miniProgram"></el-option>
                <el-option label="付款码" value="payment"></el-option>
              </el-select>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveTicketConfig">保存配置</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog 
      title="小票预览" 
      :visible.sync="previewDialogVisible" 
      width="400px"
      append-to-body
    >
      <div class="preview-content">
        <img 
          :src="currentTicketImage" 
          alt="小票预览" 
          class="preview-image"
        >
      </div>
      <template #footer>
        <el-button type="primary" @click="printTicket">打印测试</el-button>
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CustomTicket',
  data() {
    return {
      // 当前选中的小票类型
      activeTicketType: 'dining',
      
      // 弹窗状态
      editDialogVisible: false,
      previewDialogVisible: false,
      
      // 折叠面板状态
      expandedPanels: ['basic', 'order'],
      
      // 小票类型映射表
      ticketTypeMap: {
        dining: '堂食小票',
        takeout: '外卖小票',
        recharge: '充值小票',
        kitchen: '后厨小票'
      },
      
      // 小票图片映射
      ticketImages: {
        dining: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/%E8%87%AA%E5%AE%9A%E4%B9%89%E5%B0%8F%E7%A5%A8/u7321.png',
        takeout: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/自定义小票/u7333.png',
        recharge: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/自定义小票/u7334.png',
        kitchen: 'https://img.axureshop.com/a6/0c/60/a60c60d5bef4428391a9e4fe7a40ba7b/images/自定义小票/u7339.png'
      },
      
      // 各类小票的配置信息
      ticketConfigs: {
        // 堂食小票配置
        dining: {
          showTitle: ['storeName', 'welcomeText', 'ticketType'],
          welcomeText: '欢迎光临，祝您用餐愉快！',
          fontSize: 'medium',
          orderInfo: ['tableNumber', 'orderNumber', 'orderTime', 'serverName'],
          goodsInfo: ['name', 'quantity', 'price', 'total', 'specification'],
          paymentInfo: ['paymentMethod', 'totalAmount', 'discountAmount', 'paidAmount', 'cashier'],
          footerInfo: ['storeAddress', 'storePhone', 'thanksText', 'qrCode'],
          thanksText: '感谢您的光临，欢迎下次再来！',
          qrCodeType: 'miniProgram'
        },
        
        // 外卖小票配置
        takeout: {
          showTitle: ['storeName', 'welcomeText', 'ticketType'],
          welcomeText: '感谢您的订购！',
          fontSize: 'medium',
          orderInfo: ['orderNumber', 'orderTime', 'deliveryTime', 'customerName', 'customerPhone', 'deliveryAddress'],
          goodsInfo: ['name', 'quantity', 'price', 'total', 'specification', 'cookMethod'],
          paymentInfo: ['paymentMethod', 'totalAmount', 'discountAmount', 'paidAmount', 'deliveryFee'],
          footerInfo: ['storeAddress', 'storePhone', 'businessHours', 'thanksText', 'qrCode'],
          thanksText: '祝您用餐愉快！',
          qrCodeType: 'official'
        },
        
        // 充值小票配置
        recharge: {
          showTitle: ['storeName', 'ticketType'],
          welcomeText: '',
          fontSize: 'medium',
          orderInfo: ['cardNumber', 'customerName', 'rechargeTime', 'rechargeAmount', 'giveAmount', 'balance'],
          paymentInfo: ['paymentMethod', 'paidAmount', 'cashier'],
          footerInfo: ['storePhone', 'thanksText'],
          thanksText: '感谢您的充值！',
          qrCodeType: ''
        },
        
        // 后厨小票配置
        kitchen: {
          showTitle: ['storeName', 'ticketType'],
          welcomeText: '',
          fontSize: 'large',
          orderInfo: ['tableNumber', 'orderNumber', 'orderTime', 'dishDetails', 'priority'],
          goodsInfo: ['name', 'quantity', 'specification', 'cookMethod', 'cookRemark'],
          footerInfo: ['preparationTime', 'thanksText'],
          thanksText: '请尽快准备！',
          qrCodeType: ''
        }
      },
      
      // 当前编辑的小票配置（临时对象）
      currentTicketConfig: {}
    };
  },
  computed: {
    // 当前小票图片
    currentTicketImage() {
      return this.ticketImages[this.activeTicketType];
    }
  },
  watch: {
    // 当切换小票类型时，更新当前配置
    activeTicketType(newVal) {
      this.currentTicketConfig = JSON.parse(JSON.stringify(this.ticketConfigs[newVal]));
    }
  },
  created() {
    // 初始化当前配置
    this.currentTicketConfig = JSON.parse(JSON.stringify(this.ticketConfigs[this.activeTicketType]));
  },
  methods: {
    // 切换小票类型
    handleTypeChange(tab) {
      this.activeTicketType = tab.name;
    },
    
    // 打开编辑弹窗
    openEditDialog() {
      this.editDialogVisible = true;
      // 深拷贝当前配置，避免直接修改原数据
      this.currentTicketConfig = JSON.parse(JSON.stringify(this.ticketConfigs[this.activeTicketType]));
    },
    
    // 保存小票配置
    saveTicketConfig() {
      // 保存配置到对应类型
      this.ticketConfigs[this.activeTicketType] = JSON.parse(JSON.stringify(this.currentTicketConfig));
      this.editDialogVisible = false;
      this.$message.success('配置保存成功！');
    },
    
    // 预览小票
    previewTicket() {
      this.previewDialogVisible = true;
    },
    
    // 打印测试
    printTicket() {
      this.$message.success('打印指令已发送！');
    },
    
    // 保存为默认配置
    saveAsDefault() {
      this.$confirm('确定要将当前配置保存为系统默认模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里可以调用API保存为默认配置
        this.$message.success('已保存为默认模板！');
      }).catch(() => {
        this.$message.info('已取消保存');
      });
    }
  }
};
</script>

<style scoped>
.custom-ticket {
  padding: 20px;
}

.operation-buttons {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.ticket-preview-card {
  margin-top: 20px;
  padding: 20px;
}

.ticket-preview-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.ticket-frame {
  border: 1px solid #e8e8e8;
  padding: 15px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  max-width: 300px;
}

.ticket-image {
  width: 100%;
  height: auto;
}

.preview-content {
  text-align: center;
  padding: 10px;
}

.preview-image {
  max-width: 100%;
  height: auto;
}

::v-deep .el-collapse-item__content {
  padding-top: 15px !important;
}

::v-deep .el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
