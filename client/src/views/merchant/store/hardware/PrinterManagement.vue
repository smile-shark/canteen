<template>
  <div class="printer-management">
    <!-- 筛选区域 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="打印机">
        <el-input v-model="searchForm.keyword" placeholder="请输入设备名称/SN号"></el-input>
      </el-form-item>
      <el-form-item label="门店">
        <el-select v-model="searchForm.store" placeholder="请选择门店">
          <el-option label="全部" value=""></el-option>
          <el-option label="正常" value="正常"></el-option>
          <el-option label="关闭" value="关闭"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" icon="el-icon-plus" @click="openAddPrinterDialog">新增打印机</el-button>

    <!-- 数据列表 -->
    <el-table :data="tableData" border style="width: 100%; margin-top: 20px;">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="编号"></el-table-column>
      <el-table-column prop="deviceName" label="设备名称"></el-table-column>
      <el-table-column prop="ticketType" label="小票类型"></el-table-column>
      <el-table-column prop="printerType" label="打印机类型"></el-table-column>
      <el-table-column prop="brand" label="设备品牌"></el-table-column>
      <el-table-column prop="sn" label="设备SN号"></el-table-column>
      <el-table-column prop="key" label="设备Key"></el-table-column>
      <el-table-column prop="ticketSize" label="打印机小票规格"></el-table-column>
      <el-table-column prop="store" label="所属门店"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-switch v-model="scope.row.status" active-color="#f87724"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="text" icon="el-icon-edit" @click="openEditPrinterDialog(scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      style="margin-top: 20px; text-align: right;"
    ></el-pagination>

    <!-- 新增/编辑打印机弹窗 -->
    <el-dialog :title="isEdit ? '编辑打印机' : '新增打印机'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="设备编号">
          <el-input v-model="form.id"></el-input>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="form.deviceName"></el-input>
        </el-form-item>
        <el-form-item label="小票类型">
          <el-radio-group v-model="form.ticketType">
            <el-radio label="普通小票">普通小票</el-radio>
            <el-radio label="标签小票">标签小票</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="打印机类型">
          <el-radio-group v-model="form.printerType">
            <el-radio label="云打印机">云打印机</el-radio>
            <el-radio label="网口打印机">网口打印机</el-radio>
            <el-radio label="USB打印机">USB打印机</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备品牌">
          <el-input v-model="form.brand"></el-input>
        </el-form-item>
        <el-form-item label="设备SN号">
          <el-input v-model="form.sn"></el-input>
        </el-form-item>
        <el-form-item label="设备Key">
          <el-input v-model="form.key"></el-input>
        </el-form-item>
        <el-form-item label="小票规格">
          <el-radio-group v-model="form.ticketSize">
            <el-radio label="80mm">80mm</el-radio>
            <el-radio label="58mm">58mm</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属门店">
          <el-select v-model="form.store" placeholder="请选择门店">
            <el-option label="泛海国际店" value="泛海国际店"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PrinterManagement',
  data() {
    return {
      searchForm: { keyword: '' },
      tableData: [
        // 模拟数据，实际可通过接口获取
        {
          id: '0001',
          deviceName: '惠普DJ2720',
          ticketType: '普通小票',
          printerType: '云打印',
          brand: '惠普',
          sn: 'HP LaserJet P3005',
          key: '1111111',
          ticketSize: '80mm',
          store: '泛海国际店',
          status: true
        }
      ],
      page: 1,
      pageSize: 10,
      total: 100,
      dialogVisible: false,
      isEdit: false,
      form: {
        id: '',
        deviceName: '',
        ticketType: '普通小票',
        printerType: '云打印',
        brand: '',
        sn: '',
        key: '',
        ticketSize: '80mm',
        store: '泛海国际店',
        status: true
      }
    }
  },
  methods: {
    handleSearch() {
      // 调用接口查询打印机列表
      console.log('查询条件：', this.searchForm)
    },
    openAddPrinterDialog() {
      this.isEdit = false
      this.dialogVisible = true
      this.form = { ...this.defaultForm }
    },
    openEditPrinterDialog(row) {
      this.isEdit = true
      this.dialogVisible = true
      this.form = { ...row }
    },
    handleDelete(row) {
      this.$confirm('确定删除该打印机？', '提示', { type: 'warning' }).then(() => {
        const index = this.tableData.findIndex(item => item.id === row.id)
        if (index !== -1) {
          this.tableData.splice(index, 1)
          this.$message.success('删除成功')
        }
      })
    },
    handleSave() {
      if (this.isEdit) {
        // 编辑逻辑：找到对应项更新
        const index = this.tableData.findIndex(item => item.id === this.form.id)
        if (index !== -1) {
          this.tableData.splice(index, 1, { ...this.form })
        }
      } else {
        // 新增逻辑：添加到列表
        this.tableData.unshift({ ...this.form, id: `000${this.tableData.length + 1}` })
      }
      this.dialogVisible = false
      this.$message.success('保存成功')
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.handleSearch()
    },
    handleCurrentChange(val) {
      this.page = val
      this.handleSearch()
    }
  },
  computed: {
    defaultForm() {
      return {
        id: '',
        deviceName: '',
        ticketType: '普通小票',
        printerType: '云打印',
        brand: '',
        sn: '',
        key: '',
        ticketSize: '80mm',
        store: '泛海国际店',
        status: true
      }
    }
  }
}
</script>

<style scoped>
.printer-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
</style>