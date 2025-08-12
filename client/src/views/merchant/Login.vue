<template>
  <div class="login-container">
    <div class="login-box">
      <h2>智慧餐厅</h2>
      <van-cell-group>
        <van-field v-model="account" label="" placeholder="请输入用户名" left-icon="contact" @keydown.enter="handleLogin"/>
        <van-field v-model="password" :type="showPassword?'':'password'" label="" placeholder="请输入密码" left-icon="lock"  @keydown.enter="handleLogin"/>
        <i class="el-icon-view" @click="showPassword=!showPassword"></i>
      </van-cell-group>
      <van-button type="primary" block @click="handleLogin">登录</van-button>
    </div>
  </div>
</template>

<script>
import api from '@/api';
export default {
  data() {
    return {
      account: '',
      password: '',
      showPassword:false
    };
  },
  methods: {
    handleLogin() {
      // 1. 账号不能为空
      if (!this.account) {
        this.$message.error('请输入账号');
        return;
      }
      // 2. 密码不能小于6位
      if (this.password.length < 6) {
        this.$message.error('密码不能小于6位');
        return;
      }
      api.merchant.login(this.account,this.password).then(res=>{
        if(res.data.code==200){
          this.$message.success('登录成功');
          // 其中是只有id加密的token数据
          localStorage.setItem('merchantInfo',res.data.data)
          // 转跳到首页
          this.$router.push('/merchant/menu')
        }else{
          this.$message.error(res.data.msg);
        }
      })

    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #FF6A00; /* 橙色背景 */
}

.login-box {
  width: 300px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  text-align: center;
}

h2 {
  margin-bottom: 20px;
}

.van-cell-group {
  margin-bottom: 20px;
}
</style>