<template>
  <div class="coupon-detail">
    <!-- 优惠券基本信息 -->
    <el-card class="coupon-base-info" shadow="hover">
      <el-row :gutter="20">
        <el-col :span="16">
          <h3 style="margin-bottom: 15px;">{{ couponInfo.name }}</h3>
          <el-descriptions column="3" border>
            <el-descriptions-item label="优惠券ID">{{ couponInfo.id }}</el-descriptions-item>
            <el-descriptions-item label="优惠券类型">{{ couponInfo.type }}</el-descriptions-item>
            <el-descriptions-item label="面值">{{ couponInfo.denomination }}</el-descriptions-item>
            <el-descriptions-item label="使用门槛">{{ couponInfo.threshold || '无' }}</el-descriptions-item>
            <el-descriptions-item label="适用门店">{{ couponInfo.store }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag 
                :type="couponInfo.status === '未过期'? 'success' : 
                       couponInfo.status === '已过期'? 'info' : 'danger'"
              >
                {{ couponInfo.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="有效期">{{ couponInfo.validPeriod }}</el-descriptions-item>
            <el-descriptions-item label="发放总量">{{ couponInfo.total }}</el-descriptions-item>
            <el-descriptions-item label="已使用量">{{ couponInfo.used }}</el-descriptions-item>
            <el-descriptions-item label="剩余数量">{{ couponInfo.total - couponInfo.used }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              {{ ((couponInfo.used / couponInfo.total) * 100).toFixed(2) }}%
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ couponInfo.createTime }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header">使用数据统计</div>
            <el-progress 
              :percentage="(couponInfo.used / couponInfo.total) * 100" 
              stroke-width="8"
              style="margin-bottom: 20px;"
            ></el-progress>
            <div class="statistic-item">
              <span>已使用：</span>
              <span class="number">{{ couponInfo.used }}</span>
            </div>
            <div class="statistic-item">
              <span>未使用：</span>
              <span class="number">{{ couponInfo.total - couponInfo.used }}</span>
            </div>
            <div class="statistic-item">
              <span>使用率：</span>
              <span class="number">{{ ((couponInfo.used / couponInfo.total) * 100).toFixed(2) }}%</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 领取记录表格 -->
    <el-card class="coupon-records" shadow="hover" style="margin-top: 20px;">
      <div slot="header">
        <span>优惠券领取及使用记录</span>
      </div>
      
      <el-form :inline="true" :model="searchRecordForm" class="record-search-form">
        <el-form-item label="用户">
          <el-input 
            v-model="searchRecordForm.userKeyword" 
            placeholder="请输入用户名或手机号" 
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="使用状态">
          <el-select v-model="searchRecordForm.useStatus" placeholder="请选择状态" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="已使用" value="已使用"></el-option>
            <el-option label="未使用" value="未使用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchRecords">查询</el-button>
          <el-button @click="resetRecordSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table
        :data="recordTableData"
        border
        style="width: 100%; margin-top: 10px;"
      >
        <el-table-column prop="couponNo" label="优惠券编号" align="center"></el-table-column>
        <el-table-column prop="username" label="用户名称" align="center"></el-table-column>
        <el-table-column prop="phone" label="手机号" align="center"></el-table-column>
        <el-table-column prop="receiveTime" label="领取时间" align="center"></el-table-column>
        <el-table-column prop="useStatus" label="使用状态" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.useStatus === '已使用'? 'success' : 'warning'">
              {{ scope.row.useStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="useTime" label="使用时间" align="center">
          <template #default="scope">
            {{ scope.row.useTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="关联订单号" align="center">
          <template #default="scope">
            {{ scope.row.orderNo || '-' }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleRecordSizeChange"
        @current-change="handleRecordCurrentChange"
        :current-page="recordPagination.page"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="recordPagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="recordPagination.total"
        style="margin-top: 15px; text-align: right;"
      ></el-pagination>
    </el-card>

    <!-- 底部操作按钮 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-button @click="$emit('close')">关闭</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CouponDetail',
  props: {
    couponId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // 优惠券详情信息
      couponInfo: {},
      // 领取记录表格数据
      recordTableData: [],
      // 记录查询表单
      searchRecordForm: {
        userKeyword: '',
        useStatus: ''
      },
      // 记录分页配置
      recordPagination: {
        page: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    // 加载优惠券详情数据
    this.loadCouponDetail()
    // 加载领取记录
    this.loadRecordData()
  },
  methods: {
    // 加载优惠券详情
    loadCouponDetail() {
      // 实际项目中这里会根据couponId调用接口获取数据
      // 模拟数据
      const mockData = {
        1: {
          id: 1,
          name: '满100减20优惠券',
          denomination: '¥20',
          type: '满减券',
          threshold: '满100元使用',
          status: '未过期',
          validPeriod: '2023-06-01 至 2023-12-31',
          total: 10000,
          used: 3200,
          store: '全部门店',
          createTime: '2023-05-15 10:30:00'
        },
        2: {
          id: 2,
          name: '8折折扣券',
          denomination: '8折',
          type: '折扣券',
          threshold: '满50元使用',
          status: '未过期',
          validPeriod: '2023-07-01 至 2023-12-31',
          total: 5000,
          used: 1800,
          store: '泛海国际店',
          createTime: '2023-06-20 14:20:00'
        },
        3: {
          id: 3,
          name: '满200减50优惠券',
          denomination: '¥50',
          type: '满减券',
          threshold: '满200元使用',
          status: '已过期',
          validPeriod: '2023-01-01 至 2023-05-31',
          total: 8000,
          used: 7800,
          store: '大悦城店',
          createTime: '2022-12-25 09:15:00'
        },
        4: {
          id: 4,
          name: '新用户专享券',
          denomination: '¥15',
          type: '全场赠券',
          threshold: '无',
          status: '已停用',
          validPeriod: '2023-06-01 至 2023-12-31',
          total: 2000,
          used: 1500,
          store: '全部门店',
          createTime: '2023-05-30 16:40:00'
        }
      }
      
      this.couponInfo = mockData[this.couponId] || mockData[1]
    },
    
    // 加载领取记录数据
    loadRecordData() {
      // 实际项目中这里会根据couponId调用接口获取数据
      // 模拟数据
      const baseNo = `CP${this.couponId}${new Date().getFullYear()}`
      const mockRecords = []
      
      for (let i = 1; i <= 35; i++) {
        const isUsed = Math.random() > 0.3 // 70%概率已使用
        const useTime = isUsed 
         ? `${this.randomDate(new Date(2023, 5, 1), new Date())}` 
          : ''
          
        mockRecords.push({
          couponNo: `${baseNo}${i.toString().padStart(4, '0')}`,
          username: `用户${i}`,
          phone: `138${Math.floor(Math.random() * 100000000).toString().padStart(8, '0')}`,
          receiveTime: `${this.randomDate(new Date(2023, 5, 1), new Date())}`,
          useStatus: isUsed? '已使用' : '未使用',
          useTime: useTime,
          orderNo: isUsed? `ORD${Math.floor(Math.random() * 1000000)}` : ''
        })
      }
      
      this.recordTableData = mockRecords
      this.recordPagination.total = mockRecords.length
    },
    
    // 生成随机日期
    randomDate(start, end) {
      const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()))
      return date.toLocaleString('zh-CN', { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      }).replace(/\//g, '-')
    },
    
    // 搜索记录
    searchRecords() {
      console.log('搜索记录条件:', this.searchRecordForm)
      // 实际项目中调用接口查询
    },
    
    // 重置记录搜索
    resetRecordSearch() {
      this.searchRecordForm = {
        userKeyword: '',
        useStatus: ''
      }
    },
    
    // 记录分页大小变化
    handleRecordSizeChange(val) {
      this.recordPagination.size = val
    },
    
    // 记录页码变化
    handleRecordCurrentChange(val) {
      this.recordPagination.page = val
    }
  }
}
</script>

<style scoped>
.coupon-detail {
  padding: 10px 0;
}

.coupon-base-info {
  margin-bottom: 15px;
}

.statistic-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 5px 0;
  border-bottom: 1px dashed #eee;
}

.statistic-item:last-child {
  border-bottom: none;
}

.number {
  font-weight: bold;
  color: #1890ff;
}

.record-search-form {
  margin-bottom: 10px;
  padding: 10px 0;
}
</style>
