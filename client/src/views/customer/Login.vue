<template>
  <div class="auth-page">
    <!-- 背景装饰元素 -->
    <div class="bg-decoration"></div>

    <div class="auth-container">
      <!-- 餐厅Logo和名称 -->
      <div class="restaurant-brand">
        <i class="el-icon-cutlery"></i>
        <h2>智慧餐厅</h2>
      </div>

      <!-- 切换标签 -->
      <el-tabs v-model="activeTab" type="card" class="auth-tabs">
        <el-tab-pane label="登录" name="login">
          <!-- 登录方式切换 -->
          <el-radio-group v-model="loginType" class="login-type">
            <el-radio label="password">密码登录</el-radio>
            <el-radio label="code">验证码登录</el-radio>
          </el-radio-group>

          <el-form
            :model="loginForm"
            :rules="loginRules"
            ref="loginFormRef"
            class="auth-form"
          >
            <!-- 账号输入（密码/验证码登录共用） -->
            <el-form-item prop="account">
              <el-input
                v-model="loginForm.account"
                placeholder="请输入账号或手机号"
                prefix-icon="el-icon-user"
                class="custom-input"
              ></el-input>
            </el-form-item>

            <!-- 密码输入（仅密码登录显示） -->
            <el-form-item prop="password" v-if="loginType === 'password'">
              <el-input
                v-model="loginForm.password"
                placeholder="请输入密码"
                prefix-icon="el-icon-lock"
                :type="showLoginPwd ? 'text' : 'password'"
                class="custom-input"
              >
                <i
                  slot="suffix"
                  :class="showLoginPwd ? 'el-icon-eye-off' : 'el-icon-eye'"
                  @click="showLoginPwd = !showLoginPwd"
                  class="pwd-toggle"
                ></i>
              </el-input>
            </el-form-item>

            <!-- 验证码输入（仅验证码登录显示） -->
            <el-form-item prop="loginCode" v-if="loginType === 'code'">
              <el-row :gutter="10">
                <el-col :span="16">
                  <el-input
                    v-model="loginForm.loginCode"
                    placeholder="请输入验证码"
                    prefix-icon="el-icon-key"
                    class="custom-input"
                  ></el-input>
                </el-col>
                <el-col :span="8">
                  <el-button
                    :class="
                      codeDisabled ? 'send-code-btn disabled' : 'send-code-btn'
                    "
                    :disabled="codeDisabled"
                    @click="sendLoginCode"
                  >
                    {{ codeText }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                style="width: 100%"
                @click="handleLogin"
                class="submit-btn"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注册" name="register">
          <el-form
            :model="registerForm"
            :rules="registerRules"
            ref="registerFormRef"
            class="auth-form"
          >
            <!-- 邮箱账号 -->
            <el-form-item prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="请输入邮箱作为账号"
                prefix-icon="el-icon-message"
                class="custom-input"
              ></el-input>
            </el-form-item>

            <!-- 注册验证码 -->
            <el-form-item prop="registerCode">
              <el-row :gutter="10">
                <el-col :span="16">
                  <el-input
                    v-model="registerForm.registerCode"
                    placeholder="请输入验证码"
                    prefix-icon="el-icon-key"
                    class="custom-input"
                  ></el-input>
                </el-col>
                <el-col :span="8">
                  <el-button
                    :class="
                      regCodeDisabled
                        ? 'send-code-btn disabled'
                        : 'send-code-btn'
                    "
                    :disabled="regCodeDisabled"
                    @click="sendRegisterCode"
                  >
                    {{ regCodeText }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <!-- 手机号码（可选） -->
            <el-form-item prop="phone">
              <el-input
                v-model="registerForm.phone"
                placeholder="请输入手机号码（可选）"
                prefix-icon="el-icon-phone"
                class="custom-input"
              ></el-input>
            </el-form-item>

            <!-- 密码输入1 -->
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                placeholder="请设置密码"
                prefix-icon="el-icon-lock"
                :type="showRegPwd1 ? 'text' : 'password'"
                class="custom-input"
              >
                <i
                  slot="suffix"
                  :class="showRegPwd1 ? 'el-icon-eye-off' : 'el-icon-eye'"
                  @click="showRegPwd1 = !showRegPwd1"
                  class="pwd-toggle"
                ></i>
              </el-input>
            </el-form-item>

            <!-- 密码输入2 -->
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                placeholder="请再次输入密码"
                prefix-icon="el-icon-lock"
                :type="showRegPwd2 ? 'text' : 'password'"
                class="custom-input"
              >
                <i
                  slot="suffix"
                  :class="showRegPwd2 ? 'el-icon-eye-off' : 'el-icon-eye'"
                  @click="showRegPwd2 = !showRegPwd2"
                  class="pwd-toggle"
                ></i>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                style="width: 100%"
                @click="handleRegister"
                class="submit-btn"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  data() {
    // 密码一致性验证
    const validatePwd2 = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };

    return {
        shopId:'',
      // 标签切换
      activeTab: "login",
      // 登录方式
      loginType: "password",

      // 登录表单数据
      loginForm: {
        account: "",
        password: "",
        loginCode: "",
      },

      // 注册表单数据
      registerForm: {
        email: "",
        registerCode: "",
        phone: "",
        password: "",
        confirmPassword: "",
      },

      // 密码显示控制
      showLoginPwd: false,
      showRegPwd1: false,
      showRegPwd2: false,

      // 验证码状态
      codeDisabled: false,
      codeText: "发送验证码",
      regCodeDisabled: false,
      regCodeText: "发送验证码",

      // 登录表单验证规则
      loginRules: {
        account: [
          { required: true, message: "请输入账号或手机号", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        loginCode: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 4, max: 6, message: "验证码长度为4-6位", trigger: "blur" },
        ],
      },

      // 注册表单验证规则
      registerRules: {
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
        ],
        registerCode: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 4, max: 6, message: "验证码长度为4-6位", trigger: "blur" },
        ],
        phone: [
          {
            pattern: /^1[3-9]\d{9}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请设置密码", trigger: "blur" },
          { min: 6, message: "密码长度不能少于6位", trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          { validator: validatePwd2, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    // 发送登录验证码
    sendLoginCode() {
      if (!this.loginForm.account) {
        this.$message.warning("请先输入账号");
        return;
      }

      // 模拟验证码发送倒计时
      let countdown = 60;
      this.codeDisabled = true;
      this.codeText = `${countdown}s后重发`;

      const timer = setInterval(() => {
        countdown--;
        this.codeText = `${countdown}s后重发`;

        if (countdown <= 0) {
          clearInterval(timer);
          this.codeDisabled = false;
          this.codeText = "发送验证码";
        }
      }, 1000);

      api.global.sendEmail(this.loginForm.account).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("验证码已发送，请注意查收");
        } else {
          this.$message.error("验证码发送失败，请稍后再试");
          clearInterval(timer);
          this.codeDisabled = false;
          this.codeText = "发送验证码";
        }
      });
    },

    // 发送注册验证码
    sendRegisterCode() {
      if (!this.registerForm.email) {
        this.$message.warning("请先输入邮箱");
        return;
      }

      // 模拟验证码发送倒计时
      let countdown = 60;
      this.regCodeDisabled = true;
      this.regCodeText = `${countdown}s后重发`;

      const timer = setInterval(() => {
        countdown--;
        this.regCodeText = `${countdown}s后重发`;

        if (countdown <= 0) {
          clearInterval(timer);
          this.regCodeDisabled = false;
          this.regCodeText = "发送验证码";
        }
      }, 1000);
      
      api.global.sendEmail(this.registerForm.email).then((res) => {
        if (res.data.code == 200) {
          this.$message.success(res.data.msg);
        } else {
          this.$message.error("验证码发送失败，请稍后再试");
          clearInterval(timer);
          this.regCodeDisabled = false;
          this.regCodeText = "发送验证码";
        }
      });
    },

    // 处理登录
    handleLogin() {
      if (!this.loginForm.account) {
        this.$message.error("请输入正确的账号");
        return;
      }
      if (this.loginType == "password") {
        api.customer.passwordLogin(this.loginForm.account,this.loginForm.password).then(res=>{
            if(res.data.code==200){
                this.$message.success(res.data.msg)
                localStorage.setItem('customerInfo',JSON.stringify(res.data.data))
                if(this.shopId){
                    this.$router.push(`/customer/home/${this.shopId}`)
                }else{
                    this.$router.push("/customer/shopList")
                }
            }else{
                this.$message.error(res.data.msg)
            }
        })
    } else {
        if (!this.codeDisabled) {
          this.$message.error("请先发送验证码");
          return;
        }
        api.customer.verifyCodeLogin(this.loginForm.account,this.loginForm.verifyCodeLogin).then(res=>{
            if(res.data.code==200){
                localStorage.setItem('customerInfo',JSON.stringify(res.data.data));
                this.$message.success(res.data.msg)
                if(this.shopId){
                    this.$router.push(`/customer/home/${this.shopId}`)
                }else{
                    this.$router.push("/customer/shopList")
                }
            }else{
                this.$message.error(res.data.msg)
            }
        })
      }
    },

    // 处理注册
    handleRegister() {
      if(!this.registerForm.email){
        this.$message.error("请输入用户名");
        return
      }
      if(!this.registerForm.password){
        this.$message.error("请输入密码");
        return

      }
      if(!this.registerForm.confirmPassword){
        this.$message.error("请输入确认密码");
        return
      }
      if(this.registerForm.password!== this.registerForm.confirmPassword){
        this.$message.error("两次输入的密码不一致");
        return
      }
      this.registerForm.account=this.registerForm.email;
      api.customer.register(this.registerForm,this.registerForm.registerCode).then(res=>{
            if(res.data.code==200){
                localStorage.setItem('customerInfo',JSON.stringify(res.data.data));
                this.$message.success(res.data.msg)
                if(this.shopId){
                    this.$router.push(`/customer/home/${this.shopId}`)
                }else{
                    this.$router.push("/customer/shopList")
                }
            }else{
                this.$message.error(res.data.msg)
            }
        })
    },
  },
  mounted(){
    // 获取缓存中的门店数据，如果没有就转跳到 /customer/shopList ,否则到 /customer/home/:shopId
    if(localStorage.shopInfo){
        this.shopId=JSON.parse(localStorage.shopInfo).shopId
    }
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff8f5;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: 0;
  background-image: radial-gradient(#ff6f4020 2px, transparent 2px),
    radial-gradient(#ff6f4020 2px, transparent 2px);
  background-size: 40px 40px;
  background-position: 0 0, 20px 20px;
}

.auth-container {
  width: 100%;
  max-width: 400px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(255, 111, 64, 0.15);
  padding: 30px;
  position: relative;
  z-index: 1;
}

/* 餐厅品牌区域 */
.restaurant-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
  color: #ff6f40;
}

.restaurant-brand i {
  font-size: 32px;
  margin-right: 10px;
}

.restaurant-brand h2 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

/* 标签样式 */
.auth-tabs {
  margin-bottom: 20px;
}

.el-tabs__item {
  color: #666;
}

.el-tabs__item.is-active {
  color: #ff6f40;
}

.el-tabs__active-bar {
  background-color: #ff6f40 !important;
}

/* 登录方式切换 */
.login-type {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #fff8f5;
  border-radius: 8px;
}

.el-radio__input.is-checked .el-radio__inner {
  border-color: #ff6f40 !important;
  background-color: #ff6f40 !important;
}

.el-radio__label {
  color: #666;
}

.el-radio__input.is-checked + .el-radio__label {
  color: #ff6f40 !important;
  font-weight: 500;
}

/* 表单样式 */
.auth-form {
  margin-top: 20px;
}

.custom-input {
  border-radius: 8px;
  border-color: #ffd8cc;
  transition: all 0.3s;
}

.custom-input:focus {
  border-color: #ff6f40 !important;
  box-shadow: 0 0 0 2px rgba(255, 111, 64, 0.2) !important;
}

.custom-input .el-input__prefix {
  color: #ff6f40;
}

.pwd-toggle {
  color: #999;
  cursor: pointer;
  transition: color 0.3s;
}

.pwd-toggle:hover {
  color: #ff6f40;
}

/* 验证码按钮 */
.send-code-btn {
  width: 100%;
  background-color: #fff0ea;
  color: #ff6f40;
  border: 1px solid #ff6f40;
  border-radius: 8px;
  transition: all 0.3s;
}

.send-code-btn:hover {
  background-color: #ffefe8;
  color: #e85a30;
  border-color: #e85a30;
}

.send-code-btn.disabled {
  background-color: #f5f5f5;
  color: #c0c4cc;
  border-color: #dcdfe6;
  cursor: not-allowed;
}

/* 提交按钮 */
.submit-btn {
  background-color: #ff6f40 !important;
  border-color: #ff6f40 !important;
  border-radius: 8px;
  height: 44px;
  font-size: 16px;
  transition: all 0.3s;
}

.submit-btn:hover {
  background-color: #e85a30 !important;
  border-color: #e85a30 !important;
}

/* 表单验证提示 */
.el-form-item__error {
  color: #ff6f40 !important;
  background-color: #fff8f5;
  padding: 2px 8px;
  border-radius: 4px;
}

/* 消息提示框样式覆盖 */
.el-message--success {
  background-color: #fff0ea !important;
  border-color: #ff6f40 !important;
}

.el-message--success .el-message__content {
  color: #e85a30 !important;
}

.el-message--warning {
  background-color: #fff8f5 !important;
  border-color: #ff6f40 !important;
}

.el-message--warning .el-message__content {
  color: #e85a30 !important;
}
</style>
