<template>
  <div class="inventory-check">
    <div class="page-header">
      <h2>库存盘点管理</h2>
    </div>
    
    <!-- 盘点单列表页 -->
    <div v-if="currentView === 'list'">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="盘点单号">
          <el-input 
            v-model="searchForm.sheetNo" 
            placeholder="请输入盘点单号" 
            clearable
            style="width: 200px;"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="盘点类型">
          <el-select 
            v-model="searchForm.type" 
            placeholder="请选择类型" 
            clearable
            style="width: 180px;"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="菜品盘点" value="dish"></el-option>
            <el-option label="原料盘点" value="material"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属门店">
          <el-select 
            v-model="searchForm.store" 
            placeholder="请选择门店" 
            clearable
            style="width: 180px;"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="大悦城店" value="大悦城店"></el-option>
            <el-option label="泛海国际店" value="泛海国际店"></el-option>
            <el-option label="西单店" value="西单店"></el-option>
            <el-option label="王府井店" value="王府井店"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="审核状态">
          <el-select 
            v-model="searchForm.auditStatus" 
            placeholder="请选择状态" 
            clearable
            style="width: 180px;"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="待审核" value="pending"></el-option>
            <el-option label="审核通过" value="passed"></el-option>
            <el-option label="审核驳回" value="rejected"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 操作按钮 -->
      <div class="operation-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="openAddSheet">新增盘点单</el-button>
        <el-button type="success" icon="el-icon-download" @click="exportData">导出数据</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>
      
      <!-- 盘点单表格 -->
      <el-table 
        :data="tableData" 
        border 
        stripe
        style="width: 100%; margin-top: 15px;"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="sheetNo" label="盘点单号" align="center"></el-table-column>
        <el-table-column prop="type" label="盘点类型" align="center">
          <template #default="scope">
            <el-tag type="info" v-if="scope.row.type === 'dish'">菜品盘点</el-tag>
            <el-tag type="success" v-if="scope.row.type === 'material'">原料盘点</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="store" label="所属门店" align="center"></el-table-column>
        <el-table-column prop="creator" label="制单人" align="center"></el-table-column>
        <el-table-column prop="createTime" label="制单时间" align="center"></el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" align="center">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.auditStatus === 'pending'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.auditStatus === 'passed'">审核通过</el-tag>
            <el-tag type="danger" v-if="scope.row.auditStatus === 'rejected'">审核驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditor" label="审核人" align="center"></el-table-column>
        <el-table-column prop="auditTime" label="审核时间" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="220">
          <template #default="scope">
            <el-button type="text" @click="viewDetail(scope.row)">查看详情</el-button>
            
            <template v-if="scope.row.auditStatus === 'pending'">
              <el-button type="text" @click="editSheet(scope.row)">编辑</el-button>
              <el-button type="text" @click="handleAudit(scope.row)">审核</el-button>
              <el-button 
                type="text" 
                style="color: #f56c6c" 
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
            
            <template v-if="scope.row.auditStatus === 'rejected'">
              <el-button type="text" @click="editSheet(scope.row)">重新编辑</el-button>
              <el-button type="text" @click="handleDelete(scope.row)" style="color: #f56c6c">删除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 15px; text-align: right;"
      ></el-pagination>
    </div>
    
    <!-- 新增/编辑盘点单 -->
    <div v-if="currentView === 'edit'">
      <el-card>
        <el-form :model="sheetForm" label-width="120px">
          <el-form-item label="盘点单号">
            <el-input v-model="sheetForm.sheetNo" disabled></el-input>
          </el-form-item>
          
          <el-form-item label="盘点类型" prop="type">
            <el-radio-group v-model="sheetForm.type" @change="handleTypeChange">
              <el-radio label="dish">菜品盘点</el-radio>
              <el-radio label="material">原料盘点</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="所属门店" prop="store">
            <el-select v-model="sheetForm.store" placeholder="请选择门店">
              <el-option label="大悦城店" value="大悦城店"></el-option>
              <el-option label="泛海国际店" value="泛海国际店"></el-option>
              <el-option label="西单店" value="西单店"></el-option>
              <el-option label="王府井店" value="王府井店"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="选择盘点项">
            <el-button type="primary" @click="openSelectItems">选择{{ sheetForm.type === 'dish'? '菜品' : '原料' }}</el-button>
          </el-form-item>
          
          <!-- 盘点项表格 -->
          <el-table 
            :data="sheetForm.items" 
            border 
            style="width: 100%; margin: 15px 0;"
          >
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="id" label="编号" align="center"></el-table-column>
            <el-table-column prop="name" label="名称" align="center"></el-table-column>
            <el-table-column prop="category" label="类别" align="center"></el-table-column>
            <el-table-column prop="unit" label="单位" align="center"></el-table-column>
            <el-table-column prop="systemStock" label="系统库存" align="center"></el-table-column>
            <el-table-column prop="actualStock" label="实际库存" align="center">
              <template #default="scope">
                <el-input 
                  v-model.number="scope.row.actualStock" 
                  type="number" 
                  min="0"
                  @change="calculateDiff(scope.row)"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="diff" label="差异量" align="center">
              <template #default="scope">
                <span :class="{
                  'text-danger': scope.row.diff < 0,
                  'text-success': scope.row.diff > 0
                }">
                  {{ scope.row.diff }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" align="center">
              <template #default="scope">
                <el-input v-model="scope.row.remark"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="80">
              <template #default="scope">
                <el-button 
                  type="text" 
                  style="color: #f56c6c" 
                  @click="removeItem(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-form-item label="制单人" prop="creator">
            <el-input v-model="sheetForm.creator" placeholder="请输入制单人"></el-input>
          </el-form-item>
          
          <el-form-item label="盘点日期" prop="checkDate">
            <el-date-picker
              v-model="sheetForm.checkDate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择盘点日期"
            ></el-date-picker>
          </el-form-item>
          
          <el-form-item label="盘点备注">
            <el-input 
              v-model="sheetForm.remark" 
              type="textarea" 
              rows="3" 
              placeholder="请输入盘点备注信息"
            ></el-input>
          </el-form-item>
        </el-form>
        
        <div style="margin-top: 20px; text-align: right;">
          <el-button @click="backToList">取消</el-button>
          <el-button type="primary" @click="saveSheet">保存盘点单</el-button>
        </div>
      </el-card>
    </div>
    
    <!-- 审核盘点单 -->
    <div v-if="currentView === 'audit'">
      <el-card>
        <el-form :model="auditForm" label-width="120px">
          <el-form-item label="盘点单号">
            <el-input v-model="auditForm.sheetNo" disabled></el-input>
          </el-form-item>
          
          <el-form-item label="盘点类型">
            <el-tag type="info" v-if="auditForm.type === 'dish'">菜品盘点</el-tag>
            <el-tag type="success" v-if="auditForm.type === 'material'">原料盘点</el-tag>
          </el-form-item>
          
          <el-form-item label="所属门店">
            <el-input v-model="auditForm.store" disabled></el-input>
          </el-form-item>
          
          <el-form-item label="制单人">
            <el-input v-model="auditForm.creator" disabled></el-input>
          </el-form-item>
          
          <el-form-item label="盘点日期">
            <el-input v-model="auditForm.checkDate" disabled></el-input>
          </el-form-item>
          
          <!-- 盘点项表格 -->
          <el-table 
            :data="auditForm.items" 
            border 
            style="width: 100%; margin: 15px 0;"
          >
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="id" label="编号" align="center"></el-table-column>
            <el-table-column prop="name" label="名称" align="center"></el-table-column>
            <el-table-column prop="category" label="类别" align="center"></el-table-column>
            <el-table-column prop="unit" label="单位" align="center"></el-table-column>
            <el-table-column prop="systemStock" label="系统库存" align="center"></el-table-column>
            <el-table-column prop="actualStock" label="实际库存" align="center"></el-table-column>
            <el-table-column prop="diff" label="差异量" align="center">
              <template #default="scope">
                <span :class="{
                  'text-danger': scope.row.diff < 0,
                  'text-success': scope.row.diff > 0
                }">
                  {{ scope.row.diff }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" align="center"></el-table-column>
          </el-table>
          
          <el-form-item label="盘点备注">
            <el-input 
              v-model="auditForm.remark" 
              type="textarea" 
              rows="3" 
              disabled
            ></el-input>
          </el-form-item>
          
          <el-form-item label="审核人" prop="auditor">
            <el-input v-model="auditForm.auditor" placeholder="请输入审核人"></el-input>
          </el-form-item>
          
          <el-form-item label="审核意见" prop="auditOpinion">
            <el-radio-group v-model="auditForm.auditOpinion">
              <el-radio label="passed">通过</el-radio>
              <el-radio label="rejected">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="审核备注">
            <el-input 
              v-model="auditForm.auditRemark" 
              type="textarea" 
              rows="3" 
              placeholder="请输入审核备注信息"
            ></el-input>
          </el-form-item>
        </el-form>
        
        <div style="margin-top: 20px; text-align: right;">
          <el-button @click="backToList">取消</el-button>
          <el-button type="primary" @click="submitAudit">提交审核</el-button>
        </div>
      </el-card>
    </div>
    
    <!-- 查看详情 -->
    <el-dialog 
      title="盘点单详情" 
      :visible.sync="detailVisible" 
      width="80%"
      append-to-body
    >
      <el-card>
        <el-descriptions column="1" border>
          <el-descriptions-item label="盘点单号">{{ detailData.sheetNo }}</el-descriptions-item>
          <el-descriptions-item label="盘点类型">
            <el-tag type="info" v-if="detailData.type === 'dish'">菜品盘点</el-tag>
            <el-tag type="success" v-if="detailData.type === 'material'">原料盘点</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="所属门店">{{ detailData.store }}</el-descriptions-item>
          <el-descriptions-item label="制单人">{{ detailData.creator }}</el-descriptions-item>
          <el-descriptions-item label="制单时间">{{ detailData.createTime }}</el-descriptions-item>
          <el-descriptions-item label="盘点日期">{{ detailData.checkDate }}</el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag type="warning" v-if="detailData.auditStatus === 'pending'">待审核</el-tag>
            <el-tag type="success" v-if="detailData.auditStatus === 'passed'">审核通过</el-tag>
            <el-tag type="danger" v-if="detailData.auditStatus === 'rejected'">审核驳回</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核人">{{ detailData.auditor || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ detailData.auditTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="盘点备注">{{ detailData.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审核备注">{{ detailData.auditRemark || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <el-table 
          :data="detailData.items" 
          border 
          style="width: 100%; margin-top: 15px;"
        >
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column prop="id" label="编号" align="center"></el-table-column>
          <el-table-column prop="name" label="名称" align="center"></el-table-column>
          <el-table-column prop="category" label="类别" align="center"></el-table-column>
          <el-table-column prop="unit" label="单位" align="center"></el-table-column>
          <el-table-column prop="systemStock" label="系统库存" align="center"></el-table-column>
          <el-table-column prop="actualStock" label="实际库存" align="center"></el-table-column>
          <el-table-column prop="diff" label="差异量" align="center">
            <template #default="scope">
              <span :class="{
                'text-danger': scope.row.diff < 0,
                'text-success': scope.row.diff > 0
              }">
                {{ scope.row.diff }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" align="center"></el-table-column>
        </el-table>
      </el-card>
    </el-dialog>
    
    <!-- 选择菜品/原料弹窗 -->
    <el-dialog 
      :title="`选择${sheetForm.type === 'dish'? '菜品' : '原料'}`" 
      :visible.sync="selectItemsVisible" 
      width="800px"
      append-to-body
    >
      <el-input 
        v-model="searchItemKeyword" 
        placeholder="请输入名称搜索" 
        clearable
        style="margin-bottom: 15px;"
      ></el-input>
      
      <el-table 
        :data="filteredItems" 
        border 
        stripe
        @selection-change="handleItemSelectionChange"
        style="width: 100%;"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="编号" align="center"></el-table-column>
        <el-table-column prop="name" label="名称" align="center"></el-table-column>
        <el-table-column prop="category" label="类别" align="center"></el-table-column>
        <el-table-column prop="unit" label="单位" align="center"></el-table-column>
        <el-table-column prop="stock" label="当前库存" align="center"></el-table-column>
      </el-table>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="selectItemsVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSelectItems">确定选择</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InventoryCheck',
  data() {
    return {
      // 当前视图状态：list-列表, edit-新增/编辑, audit-审核
      currentView: 'list',
      
      // 搜索表单
      searchForm: {
        sheetNo: '',
        type: '',
        store: '',
        auditStatus: ''
      },
      
      // 盘点单列表数据
      tableData: [
        {
          sheetNo: 'PCK20230615001',
          type: 'dish',
          store: '大悦城店',
          creator: '张三',
          createTime: '2023-06-15 09:30:00',
          auditStatus: 'pending',
          auditor: '',
          auditTime: ''
        },
        {
          sheetNo: 'PCK20230614002',
          type: 'material',
          store: '泛海国际店',
          creator: '李四',
          createTime: '2023-06-14 14:20:00',
          auditStatus: 'passed',
          auditor: '王五',
          auditTime: '2023-06-14 16:45:00'
        },
        {
          sheetNo: 'PCK20230613001',
          type: 'dish',
          store: '西单店',
          creator: '赵六',
          createTime: '2023-06-13 10:15:00',
          auditStatus: 'rejected',
          auditor: '钱七',
          auditTime: '2023-06-13 11:30:00'
        }
      ],
      
      // 分页信息
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 30
      },
      
      // 盘点单表单数据
      sheetForm: {
        sheetNo: '',
        type: 'dish',
        store: '',
        creator: '',
        checkDate: '',
        remark: '',
        items: []
      },
      
      // 审核表单数据
      auditForm: {
        sheetNo: '',
        type: '',
        store: '',
        creator: '',
        createTime: '',
        checkDate: '',
        remark: '',
        items: [],
        auditor: '',
        auditOpinion: 'passed',
        auditRemark: ''
      },
      
      // 详情弹窗数据
      detailData: {},
      detailVisible: false,
      
      // 选择项弹窗
      selectItemsVisible: false,
      searchItemKeyword: '',
      selectedItems: [],
      
      // 可选菜品列表
      dishItems: [
        { id: 'D001', name: '宫保鸡丁', category: '热菜', unit: '份', stock: 45 },
        { id: 'D002', name: '凉拌黄瓜', category: '凉菜', unit: '份', stock: 8 },
        { id: 'D003', name: '西红柿鸡蛋汤', category: '汤品', unit: '碗', stock: 120 },
        { id: 'D004', name: '扬州炒饭', category: '主食', unit: '份', stock: 30 },
        { id: 'D005', name: '可乐', category: '饮品', unit: '瓶', stock: 55 }
      ],
      
      // 可选原料列表
      materialItems: [
        { id: 'M001', name: '黄瓜', category: '蔬菜类', unit: 'kg', stock: 35 },
        { id: 'M002', name: '猪肉', category: '肉类', unit: 'kg', stock: 8 },
        { id: 'M003', name: '西红柿', category: '蔬菜类', unit: 'kg', stock: 60 },
        { id: 'M004', name: '大豆油', category: '调料类', unit: '桶', stock: 12 },
        { id: 'M005', name: '干辣椒', category: '调料类', unit: 'kg', stock: 3 }
      ]
    }
  },
  computed: {
    // 根据类型和搜索词过滤可选项目
    filteredItems() {
      const items = this.sheetForm.type === 'dish'? this.dishItems : this.materialItems
      if (!this.searchItemKeyword) return items
      
      return items.filter(item => 
        item.name.toLowerCase().includes(this.searchItemKeyword.toLowerCase())
      )
    }
  },
  methods: {
    // 搜索盘点单
    handleSearch() {
      console.log('搜索条件:', this.searchForm)
      // 前端筛选逻辑示例
      const filteredData = this.tableData.filter(item => {
        if (this.searchForm.sheetNo &&!item.sheetNo.includes(this.searchForm.sheetNo)) return false
        if (this.searchForm.type && item.type!== this.searchForm.type) return false
        if (this.searchForm.store && item.store!== this.searchForm.store) return false
        if (this.searchForm.auditStatus && item.auditStatus!== this.searchForm.auditStatus) return false
        return true
      })
      this.pagination.total = filteredData.length
      this.pagination.pageNum = 1
    },
    
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        sheetNo: '',
        type: '',
        store: '',
        auditStatus: ''
      }
      this.refreshData()
    },
    
    // 刷新数据
    refreshData() {
      this.$message.success('数据已刷新')
    },
    
    // 导出数据
    exportData() {
      this.$message.success('数据导出成功')
    },
    
    // 分页相关
    handleSizeChange(val) {
      this.pagination.pageSize = val
    },
    handleCurrentChange(val) {
      this.pagination.pageNum = val
    },
    
    // 打开新增盘点单
    openAddSheet() {
      // 生成盘点单号：PCK + 日期 + 序号
      const date = new Date()
      const dateStr = date.toISOString().slice(0, 10).replace(/-/g, '')
      const randomNum = Math.floor(1000 + Math.random() * 9000)
      const sheetNo = `PCK${dateStr}${randomNum}`
      
      this.sheetForm = {
        sheetNo,
        type: 'dish',
        store: '',
        creator: '',
        checkDate: date.toISOString().slice(0, 10),
        remark: '',
        items: []
      }
      this.currentView = 'edit'
    },
    
    // 编辑盘点单
    editSheet(row) {
      // 查找完整的盘点单数据
      const fullData = this.getSheetDetail(row.sheetNo)
      this.sheetForm = {...fullData }
      this.currentView = 'edit'
    },
    
    // 处理盘点类型变更
    handleTypeChange() {
      // 清空已选择的项目
      this.sheetForm.items = []
    },
    
    // 打开选择项目弹窗
    openSelectItems() {
      this.searchItemKeyword = ''
      this.selectedItems = []
      this.selectItemsVisible = true
    },
    
    // 处理项目选择变化
    handleItemSelectionChange(rows) {
      this.selectedItems = rows
    },
    
    // 确认选择项目
    confirmSelectItems() {
      if (this.selectedItems.length === 0) {
        this.$message.warning('请至少选择一项')
        return
      }
      
      // 将选中的项目添加到盘点单中，避免重复添加
      this.selectedItems.forEach(item => {
        const exists = this.sheetForm.items.some(i => i.id === item.id)
        if (!exists) {
          this.sheetForm.items.push({
            id: item.id,
            name: item.name,
            category: item.category,
            unit: item.unit,
            systemStock: item.stock,
            actualStock: item.stock,
            diff: 0,
            remark: ''
          })
        }
      })
      
      this.selectItemsVisible = false
    },
    
    // 计算差异量
    calculateDiff(row) {
      row.diff = row.actualStock - row.systemStock
    },
    
    // 移除项目
    removeItem(index) {
      this.sheetForm.items.splice(index, 1)
    },
    
    // 保存盘点单
    saveSheet() {
      if (!this.sheetForm.store) {
        this.$message.warning('请选择所属门店')
        return
      }
      
      if (!this.sheetForm.creator) {
        this.$message.warning('请输入制单人')
        return
      }
      
      if (!this.sheetForm.checkDate) {
        this.$message.warning('请选择盘点日期')
        return
      }
      
      if (this.sheetForm.items.length === 0) {
        this.$message.warning('请至少选择一项盘点项目')
        return
      }
      
      // 模拟保存逻辑
      const isEdit = this.tableData.some(item => item.sheetNo === this.sheetForm.sheetNo)
      
      if (isEdit) {
        // 编辑现有盘点单
        const index = this.tableData.findIndex(item => item.sheetNo === this.sheetForm.sheetNo)
        this.tableData.splice(index, 1, {
          sheetNo: this.sheetForm.sheetNo,
          type: this.sheetForm.type,
          store: this.sheetForm.store,
          creator: this.sheetForm.creator,
          createTime: new Date().toLocaleString(),
          auditStatus: 'pending',
          auditor: '',
          auditTime: ''
        })
        this.$message.success('盘点单更新成功')
      } else {
        // 新增盘点单
        this.tableData.unshift({
          sheetNo: this.sheetForm.sheetNo,
          type: this.sheetForm.type,
          store: this.sheetForm.store,
          creator: this.sheetForm.creator,
          createTime: new Date().toLocaleString(),
          auditStatus: 'pending',
          auditor: '',
          auditTime: ''
        })
        this.pagination.total++
        this.$message.success('盘点单创建成功')
      }
      
      this.backToList()
    },
    
    // 处理审核
    handleAudit(row) {
      const fullData = this.getSheetDetail(row.sheetNo)
      this.auditForm = {
       ...fullData,
        auditor: '',
        auditOpinion: 'passed',
        auditRemark: ''
      }
      this.currentView = 'audit'
    },
    
    // 提交审核
    submitAudit() {
      if (!this.auditForm.auditor) {
        this.$message.warning('请输入审核人')
        return
      }
      
      // 更新盘点单状态
      const index = this.tableData.findIndex(item => item.sheetNo === this.auditForm.sheetNo)
      if (index!== -1) {
        this.tableData[index] = {
         ...this.tableData[index],
          auditStatus: this.auditForm.auditOpinion,
          auditor: this.auditForm.auditor,
          auditTime: new Date().toLocaleString()
        }
      }
      
      this.$message.success('审核已提交')
      this.backToList()
    },
    
    // 查看详情
    viewDetail(row) {
      this.detailData = this.getSheetDetail(row.sheetNo)
      this.detailVisible = true
    },
    
    // 删除盘点单
    handleDelete(row) {
      this.$confirm(`确定要删除盘点单【${row.sheetNo}】吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.tableData.findIndex(item => item.sheetNo === row.sheetNo)
        if (index!== -1) {
          this.tableData.splice(index, 1)
          this.pagination.total--
        }
        this.$message.success('删除成功')
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    
    // 返回列表页
    backToList() {
      this.currentView = 'list'
    },
    
    // 获取盘点单详情（模拟数据）
    getSheetDetail(sheetNo) {
      // 在实际应用中，这里应该从服务器获取完整数据
      const row = this.tableData.find(item => item.sheetNo === sheetNo)
      
      // 模拟不同类型的盘点单详情
      let items = []
      if (row.type === 'dish') {
        items = [
          { 
            id: 'D001', 
            name: '宫保鸡丁', 
            category: '热菜', 
            unit: '份', 
            systemStock: 45, 
            actualStock: 43, 
            diff: -2,
            remark: '部分食材变质'
          },
          { 
            id: 'D005', 
            name: '可乐', 
            category: '饮品', 
            unit: '瓶', 
            systemStock: 55, 
            actualStock: 55, 
            diff: 0,
            remark: ''
          }
        ]
      } else {
        items = [
          { 
            id: 'M001', 
            name: '黄瓜', 
            category: '蔬菜类', 
            unit: 'kg', 
            systemStock: 35, 
            actualStock: 32, 
            diff: -3,
            remark: '部分枯萎'
          },
          { 
            id: 'M002', 
            name: '猪肉', 
            category: '肉类', 
            unit: 'kg', 
            systemStock: 8, 
            actualStock: 8, 
            diff: 0,
            remark: ''
          }
        ]
      }
      
      return {
       ...row,
        checkDate: row.createTime.split(' ')[0],
        remark: row.auditStatus === 'rejected'? '数据有误，需要重新盘点' : '',
        items,
        auditRemark: row.auditStatus === 'rejected'? '差异数据未说明原因' : ''
      }
    }
  }
}
</script>

<style scoped>
.inventory-check {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh - 40px);
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
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

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

.text-success {
  color: #67c23a;
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}
</style>
