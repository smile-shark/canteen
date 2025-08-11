<template>
  <div class="account-setting">
    <h2>账号设置</h2>
    <el-form
      ref="passwordForm"
      :model="form"
      label-width="100px"
      class="form-container"
    >
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input
          v-model="form.oldPassword"
          type="password"
          placeholder="请输入旧密码"
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="form.newPassword"
          type="password"
          placeholder="请输入新密码"
        />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="请确认新密码"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'AccountSetting',
  data() {
    return {
      form: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    };
  },
  props: {
    // 可根据实际需求添加父组件传递的属性，比如用户信息等
  },
  methods: {
    onSubmit() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          // 表单验证通过，可在此处调用接口提交密码修改
          console.log('表单验证通过，提交数据：', this.form);
          // 示例：调用接口
          // this.$axios.post('/api/changePassword', this.form).then(response => {
          //   if (response.data.code === 200) {
          //     this.$message.success('密码修改成功');
          //     this.onCancel();
          //   } else {
          //     this.$message.error('密码修改失败，请稍后重试');
          //   }
          // }).catch(error => {
          //   console.error('请求错误：', error);
          //   this.$message.error('网络异常，请稍后重试');
          // });
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    },
    onCancel() {
      // 重置表单
      this.$refs.passwordForm.resetFields();
      // 可在此处添加关闭弹窗或返回上一级页面的逻辑
    }
  },
  watch: {
    // 可根据实际需求添加监听逻辑
  }
};
</script>

<style scoped>
.account-setting {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.form-container {
  width: 400px;
}
</style>