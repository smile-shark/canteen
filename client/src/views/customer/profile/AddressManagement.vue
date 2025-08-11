<template>
  <div class="address-management">
    <!-- 返回按钮 -->
    <el-row>
      <el-col :span="2">
        <el-button icon="el-icon-back" type="text" @click="$router.push('/customer/profile')"></el-button>
      </el-col>
      <el-col :span="20">
        <h2 class="page-title">地址管理</h2>
      </el-col>
    </el-row>

    <!-- 地址列表 -->
    <el-card
      v-for="(address, index) in addresses"
      :key="index"
      shadow="hover"
      class="address-card"
    >
      <div class="address-info">
        <div class="name-phone">
          <span class="name">{{ address.name }}</span>
          <span class="phone">{{ address.phone }}</span>
          <el-tag type="warning" v-if="address.isDefault">默认</el-tag>
        </div>
        <div class="address-detail">{{ address.detail }}</div>
        <div class="address-actions">
          <el-button
            icon="el-icon-edit"
            type="text"
            @click="editAddress(address)"
          >编辑</el-button>
          <el-button
            icon="el-icon-delete"
            type="text"
            @click="deleteAddress(address, index)"
          >删除</el-button>
          <el-button
            icon="el-icon-setting"
            type="text"
            @click="setDefault(address)"
            v-if="!address.isDefault"
          >设为默认</el-button>
        </div>
      </div>
    </el-card>

    <!-- 新增地址按钮 -->
    <el-button
      type="primary"
      class="add-address-btn"
      @click="showAddDialog = true"
    >新增地址</el-button>

    <!-- 新增/编辑地址弹窗 -->
    <el-dialog
      title="地址编辑"
      :visible.sync="showAddDialog"
      width="50%"
    >
      <el-form :model="form" label-width="120px">
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input type="textarea" v-model="form.detail"></el-input>
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="form.isDefault"></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AddressManagement',
  data() {
    return {
      addresses: [
        {
          id: 1,
          name: '张三',
          phone: '18811112345',
          detail: '北京市朝阳区xx路xx号',
          isDefault: true
        }
      ],
      showAddDialog: false,
      form: {
        name: '',
        phone: '',
        detail: '',
        isDefault: false,
        id: null
      }
    };
  },
  methods: {
    // 编辑地址
    editAddress(address) {
      this.showAddDialog = true;
      this.form = { ...address };
    },
    // 删除地址
    deleteAddress(address, index) {
      this.$confirm('确定要删除该地址吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.addresses.splice(index, 1);
        this.$message.success('地址已删除');
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    // 设置默认地址
    setDefault(address) {
      this.addresses.forEach(item => {
        item.isDefault = false;
      });
      address.isDefault = true;
      this.$message.success('已设为默认地址');
    },
    // 保存地址（新增/编辑）
    saveAddress() {
      if (this.form.id) {
        // 编辑已有地址
        const index = this.addresses.findIndex(item => item.id === this.form.id);
        this.addresses.splice(index, 1, this.form);
        this.$message.success('地址编辑成功');
      } else {
        // 新增地址
        this.form.id = new Date().getTime();
        this.addresses.push(this.form);
        this.$message.success('地址新增成功');
      }
      this.showAddDialog = false;
      this.form = {
        name: '',
        phone: '',
        detail: '',
        isDefault: false,
        id: null
      };
    }
  }
};
</script>

<style scoped>
.address-management {
  padding: 20px;
}
.page-title {
  margin: 0;
}
.address-card {
  margin-bottom: 10px;
}
.address-info {
  padding: 10px;
}
.name-phone {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}
.name {
  font-weight: bold;
}
.phone {
  margin-left: 10px;
}
.address-detail {
  margin-bottom: 10px;
}
.address-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.add-address-btn {
  margin-top: 10px;
  width: 100%;
}
.dialog-footer {
  text-align: right;
}
</style>