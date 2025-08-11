<template>
  <div class="dine-in-management">
    <!-- 页面标题 -->
    <div class="page-title">外卖管理</div>
    
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="开启" :value="0"></el-option>
          <el-option label="关闭" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="门店">
        <el-select v-model="searchForm.storeId" placeholder="请选择门店" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option v-for="shop in shopOptions" :key="shop.shopId" :label="shop.name" :value="shop.shopId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
    
    <!-- 数据表格 -->
    <el-table 
      :data="tableData" 
      border 
      stripe
      @selection-change="handleSelectionChange"
      style="width: 100%; margin-top: 15px;"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="shopOrder" label="ID" width="80" align="center"></el-table-column>
      <el-table-column prop="name" label="门店名称" align="center"></el-table-column>
      <el-table-column prop="isTakeOut" label="外卖状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.isTakeOut == 0 ? 'success' : 'danger'">
            {{ scope.row.isTakeOut == 0 ? '开启' : '关闭' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="营业时间" align="center">
        <template #default="scope">
          {{ scope.row.openingHoursStart }} - {{ scope.row.openingHoursEnd }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-radio-group v-model="scope.row.isTakeOut" @input="handleIsDineInChange(scope.row)">
            <el-radio :label="0">开启</el-radio>
            <el-radio :label="1">关闭</el-radio>
          </el-radio-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.pageNum"
      :page-sizes="[10, 20, 50]"
      :page-size="pagination.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      style="margin-top: 15px; text-align: right;"
    ></el-pagination>

  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'DineInManagement',
  data() {
    return {
      shopOptions:[],
      // 搜索表单
      searchForm: {
        status: '',
        storeId: '',
      },
      
      
      // 表格数据
      tableData: [],
      
      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      
      // 选中的行
      selectedRows: [],
      
    }
  },
  methods: {
    // 搜索
    handleSearch(page) {
      if(typeof page!='number'){
        page=1
      }
      this.pagination.pageNum=page
      api.shop.pageListTakeOut(this.pagination.pageNum,this.pagination.pageSize,this.searchForm.storeId,this.searchForm.status).then(res=>{
        if(res.data.code==200){
          this.tableData=res.data.data.records
          this.pagination.total=res.data.data.total
        }else{
          this.$message.error(res.data.msg)
        }
      })
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        status: '',
        storeId: '',
      }
    },
    handleIsDineInChange(row){
      api.shop.update(row).then(res=>{
        if(res.data.code==200){
          this.$message.success('修改成功')
        }else{
          this.$message.error(res.data.msg)
        }
      })
    }
  },
  mounted(){
    api.shop.simpleList().then(res=>{
      if(res.data.code==200){
        this.shopOptions=res.data.data
      }
    })
    this.handleSearch()
  }
}
</script>

<style scoped>
.dine-in-management {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh - 40px);
}

.page-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.search-form {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.operation-buttons {
  margin-bottom: 15px;
  display: flex;
  gap: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
    