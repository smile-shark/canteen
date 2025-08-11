<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="login-bg"></div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <div class="login-header">
        <el-avatar shape="circle" :size="60" class="login-logo">
          <i class="el-icon-user" style="font-size: 30px"></i>
        </el-avatar>
        <h2 class="login-title">员工登录系统</h2>
        <p class="login-desc" v-if="staffPerssion == 0">后厨登录</p>
        <p class="login-desc" v-else-if="staffPerssion == 1">服务登录</p>
        <p class="login-desc" v-else>收银登录</p>
      </div>

      <!-- 登录表单 -->
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        label-position="left"
        label-width="70px"
      >
        <el-form-item label="账号" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
            @keyup.enter.native="handleLogin"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin"
          >
            <i
              slot="suffix"
              class="el-icon-view"
              :style="{ cursor: 'pointer' }"
              @click="showPassword = !showPassword"
            ></i>
          </el-input>
        </el-form-item>

        <el-form-item>
          <template #label>
            <el-button
              @click="$router.push('/staff/loginmain')"
              type="primary"
              size="small"
              >返回</el-button
            >
          </template>
          <el-button
            type="primary"
            class="login-btn"
            @click="handleLogin"
            :loading="loginLoading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 底部版权信息 -->
    <div class="login-footer">© 2025 员工管理系统 版权所有</div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "StaffLogin",
  data() {
    return {
      // 登录表单数据
      loginForm: {
        username: "",
        password: "",
      },
      // 表单验证规则
      loginRules: {
        username: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 3,
            max: 20,
            message: "账号长度在 3 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度不能少于 6 个字符", trigger: "blur" },
        ],
      },
      // 控制密码显示/隐藏
      showPassword: false,
      // 记住密码
      rememberMe: false,
      // 登录加载状态
      loginLoading: false,
      // 员工权限
      staffPerssion: 0,
    };
  },
  mounted() {
    // 获取路由参数中的权限信息
    this.staffPerssion = parseInt(this.$route.params.perssion) || 0;
  },
  methods: {
    // 处理登录
    handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loginLoading = true;
          api.staff.login(this.loginForm.username,this.loginForm.password,this.staffPerssion).then(res=>{
            if(res.data.code==200){
              this.$message.success("登录成功");
              localStorage.setItem('staffInfo',JSON.stringify(res.data.data))
              switch (this.staffPerssion) {
                case 0:
                  this.$router.push("/staff/back-kitchen");
                  break;
                case 1:
                  this.$router.push("/staff/service");
                  break;
                case 3:
                  this.$router.push("/staff/cashiering");
                  break;
              }

            }else{
              this.$message.error(res.data.msg);
            }
            this.loginLoading = false;
          })
        }
      });
    },

    // 忘记密码处理
    handleForgotPassword() {
      this.$alert("请联系管理员重置密码", "忘记密码", {
        confirmButtonText: "确定",
        type: "info",
      });
    },
  },
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #f5f7fa url("@/assets/staff-login-background.jpg") no-repeat
    center center;
  background-size: cover;
  filter: blur(5px);
  z-index: 1;
  opacity: 0.7;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  padding: 30px;
  position: relative;
  z-index: 2;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-logo {
  background-color: #409eff;
  margin: 0 auto 15px;
}

.login-title {
  color: #303133;
  margin: 0 0 10px;
  font-weight: 500;
}

.login-desc {
  color: #606266;
  margin: 0;
  font-size: 14px;
}

.login-form {
  width: 100%;
}

.remember-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.remember-checkbox {
  color: #606266;
}

.forgot-link {
  font-size: 14px;
}

.login-btn {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
}

.login-footer {
  color: #606266;
  font-size: 12px;
  margin-top: 30px;
  position: relative;
  z-index: 2;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-card {
    width: 90%;
    padding: 20px;
  }
}
</style>
