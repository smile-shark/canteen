<template>
  <div class="qr-scanner">
    <!-- 顶部导航 -->
    <div class="header">
      <div class="back-btn" @click="handleBack">
        <i class="el-icon-arrow-left"></i>
      </div>
      <div class="header-title">扫码</div>
      <div class="empty-space"></div>
    </div>

    <!-- 扫码容器 -->
    <div class="scanner-container">
      <!-- 视频预览区域 -->
      <div id="qr-video-container" class="video-container"></div>

      <!-- 扫码框指示器 -->
      <div class="scanner-frame" ref="scannerFrame">
        <!-- 四角标记 -->
        <div class="corner top-left"></div>
        <div class="corner top-right"></div>
        <div class="corner bottom-left"></div>
        <div class="corner bottom-right"></div>

        <!-- 扫描线动画 -->
        <div class="scanner-line"></div>
      </div>

      <!-- 扫码提示 -->
      <div class="scanner-tips">
        <p>请将二维码对准扫描框</p>
        <p class="small-tip">确保光线充足，二维码清晰</p>
      </div>

      <!-- 扫码区域说明遮罩 -->
      <!-- <div class="scanner-mask"></div> -->
    </div>

    <!-- 控制按钮 -->
    <div class="control-buttons">
      <el-button
        type="primary"
        icon="el-icon-video-camera"
        @click="toggleCamera"
        :loading="isLoading"
      >
        {{ isCameraActive ? "关闭摄像头" : "打开摄像头" }}
      </el-button>

      <el-button
        type="success"
        icon="el-icon-refresh"
        @click="resetScanner"
        :disabled="!isCameraActive"
      >
        重新扫描
      </el-button>
    </div>

    <!-- 结果弹窗 -->
    <el-dialog
      title="扫码结果"
      :visible.sync="resultVisible"
      width="80%"
      append-to-body
    >
      <div class="result-content">
        <p>{{ scanResult }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleResultCancel">关闭</el-button>
        <el-button type="primary" @click="handleResultConfirm">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";
import { Html5Qrcode } from "html5-qrcode";

export default {
  data() {
    return {
      html5Qrcode: null,
      isCameraActive: false,
      isLoading: false,
      resultVisible: false,
      scanResult: "",
      scannerBoxSize: 280, // 扫码框尺寸，用于对齐
    };
  },
  mounted() {
    // 页面加载后自动尝试启动摄像头
    this.$nextTick(() => {
      this.startScanner();
    });
  },
  beforeDestroy() {
    // 页面销毁时关闭摄像头
    this.stopScanner();
  },
  methods: {
    // 返回上一页
    handleBack() {
      this.$router.push("/customer/shopList");
    },

    // 切换摄像头状态
    async toggleCamera() {
      if (this.isCameraActive) {
        this.stopScanner();
      } else {
        this.startScanner();
      }
    },

    // 开始扫描
    async startScanner() {
      this.isLoading = true;
      try {
        // 获取可用摄像头设备
        const devices = await Html5Qrcode.getCameras();

        if (devices && devices.length) {
          // 优先使用后置摄像头
          const cameraId = devices.find((d) =>
            d.label.toLowerCase().includes("back")
          )
            ? devices.find((d) => d.label.toLowerCase().includes("back")).id
            : devices[0].id;

          // 初始化扫码器
          this.html5Qrcode = new Html5Qrcode("qr-video-container");

          // 启动扫描
          await this.html5Qrcode.start(
            cameraId,
            {
              fps: 10, // 每秒扫描帧数
              qrbox: {
                // width: this.$refs.scannerFrame.clientWidth * 1.92,
                // height: this.$refs.scannerFrame.clientHeight * 1.11,
                width: this.$refs.scannerFrame.clientWidth * 1.92,
                height: this.$refs.scannerFrame.clientHeight * 1.11,
              }, // 扫码框大小，与视觉框一致
            },
            (decodedText) => {
              // 成功识别到二维码
              this.handleScanSuccess(decodedText);
            },
            (errorMessage) => {
              // 扫描错误（正常情况，忽略即可）
              // console.log("扫描错误:", errorMessage);
            }
          );

          this.isCameraActive = true;
          this.$message.success("摄像头已启动");

          // 调整视频显示，确保与扫码框对齐
          this.adjustVideoLayout();
        } else {
          this.$message.error("未检测到可用摄像头");
        }
      } catch (error) {
        console.error("启动摄像头失败:", error);
        this.$message.error(`启动失败: ${error.message}`);
      } finally {
        this.isLoading = false;
      }
    },

    // 调整视频布局，确保与扫码框对齐
    adjustVideoLayout() {
      const video = document.querySelector("#qr-video-container video");
      const container = document.querySelector(".scanner-container");

      if (video && container) {
        // 监听视频尺寸变化，确保正确显示
        video.addEventListener("loadedmetadata", () => {
          this.setVideoStyle(video, container);
        });

        // 初始设置
        this.setVideoStyle(video, container);
      }
    },

    // 设置视频样式
    setVideoStyle(video, container) {
      // 根据视频宽高比和容器宽高比调整显示方式
      const videoRatio = video.videoWidth / video.videoHeight;
      const containerRatio = container.clientWidth / container.clientHeight;

      if (videoRatio > containerRatio) {
        // 视频更宽，按高度适配
        video.style.width = "auto";
        video.style.height = "100%";
      } else {
        // 视频更高，按宽度适配
        video.style.width = "100%";
        video.style.height = "auto";
      }
    },

    // 停止扫描
    stopScanner() {
      if (this.html5Qrcode && this.isCameraActive) {
        this.html5Qrcode
          .stop()
          .then(() => {
            this.isCameraActive = false;
            this.html5Qrcode = null;
            this.$message.info("摄像头已关闭");
          })
          .catch((error) => {
            console.error("关闭摄像头失败:", error);
          });
      }
    },

    // 重新扫描
    resetScanner() {
      this.resultVisible = false;
      this.scanResult = "";

      if (this.html5Qrcode && this.isCameraActive) {
        // 重新开始扫描
        this.html5Qrcode.reset();
        this.startScanner();
      }
    },

    // 处理扫描成功
    handleScanSuccess(result) {
      this.scanResult = result;
      api.diningTable.infoById(result).then((res) => {
        if (res.data.code == 200) {
          // 1. 根据获取到的信息查询shop的数据
          api.shop.infoById(res.data.data.shopId).then((res) => {
            if (res.data.code == 200) {
              localStorage.setItem("shopInfo", JSON.stringify(res.data.data));
              // 转跳到点餐的页面
              this.$router.push('/customer/home/orderservice/0')
            }else{
                this.$message.error("店铺信息查询失败")
            }
          });
          // 2. 存储桌位的数据
          localStorage.setItem("diningTableInfo", JSON.stringify(res.data.data));
        } else {
          this.$message.error("未查询到对应信息");
        }
      });
      // 停止扫描，避免重复识别
      //   this.resultVisible = true;
      this.html5Qrcode.stop();
      this.isCameraActive = false;
    },

    // 处理结果弹窗-取消
    handleResultCancel() {
      this.resultVisible = false;
      this.scanResult = "";
      // 重新启动扫描
      this.startScanner();
    },

    // 处理结果弹窗-确认
    handleResultConfirm() {
      this.resultVisible = false;
      this.$message.success("已确认扫码结果");
      // 处理扫码结果，例如跳转或解析
      this.handleScanResult(this.scanResult);
    },

    // 处理扫码结果
    handleScanResult(result) {
      console.log("扫码结果:", result);
      // 示例：如果是URL则跳转
      if (this.isValidUrl(result)) {
        window.location.href = result;
      } else {
        // 其他类型的处理逻辑
        this.$router.push({
          path: "/scan-result",
          query: { data: result },
        });
      }
    },

    // 验证是否为有效URL
    isValidUrl(string) {
      try {
        new URL(string);
        return true;
      } catch (_) {
        return false;
      }
    },
  },
};
</script>

<style scoped>
.qr-scanner {
  min-height: 100vh;
  background-color: #000;
  color: #fff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶部导航 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  padding: 0 15px;
  background-color: rgba(0, 0, 0, 0.5);
  position: relative;
  z-index: 20;
}

.back-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.header-title {
  font-size: 18px;
  font-weight: 500;
}

.empty-space {
  width: 40px;
  height: 40px;
}

/* 扫码容器 */
.scanner-container {
  flex: 1;
  position: relative;
  overflow: hidden;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.video-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 10;
}

/* 视频样式 */
.video-container video {
  object-fit: cover;
  max-width: 100%;
  max-height: 100%;
}

/* 扫码框样式 */
.scanner-frame {
  position: absolute;
  width: 280px;
  height: 280px;
  z-index: 15;
  pointer-events: none;
}

/* 四角标记 */
.corner {
  width: 24px;
  height: 24px;
  position: absolute;
  border: 3px solid #4cd964;
  z-index: 16;
}

.top-left {
  top: -2px;
  left: -2px;
  border-right: none;
  border-bottom: none;
}

.top-right {
  top: -2px;
  right: -2px;
  border-left: none;
  border-bottom: none;
}

.bottom-left {
  bottom: -2px;
  left: -2px;
  border-right: none;
  border-top: none;
}

.bottom-right {
  bottom: -2px;
  right: -2px;
  border-left: none;
  border-top: none;
}

/* 扫描线动画 */
.scanner-line {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background-color: #4cd964;
  animation: scanLine 2s linear infinite;
  z-index: 15;
}

@keyframes scanLine {
  0% {
    top: 0;
  }
  50% {
    top: calc(100% - 3px);
  }
  100% {
    top: 0;
  }
}

/* 遮罩层 - 突出扫码区域 */
.scanner-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 12;
  background-color: rgba(0, 0, 0, 0.6);
  /* 中间镂空效果 */
  background-clip: content-box;
  padding: calc(50% - 140px) calc(50% - 140px);
  box-sizing: border-box;
}

/* 扫码提示 */
.scanner-tips {
  position: absolute;
  bottom: 120px;
  left: 0;
  width: 100%;
  text-align: center;
  z-index: 15;
}

.scanner-tips p {
  margin: 5px 0;
  font-size: 16px;
  text-shadow: 0 0 3px rgba(0, 0, 0, 0.5);
}

.small-tip {
  font-size: 14px;
  color: #ddd;
}

/* 控制按钮 */
.control-buttons {
  padding: 20px 15px;
  display: flex;
  gap: 15px;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 20;
  position: relative;
}

.control-buttons .el-button {
  min-width: 140px;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
}

/* 结果弹窗 */
.result-content {
  word-break: break-all;
  padding: 10px 0;
  font-size: 16px;
}

/* 适配不同屏幕尺寸 */
@media (max-width: 375px) {
  .scanner-frame {
    width: 250px;
    height: 250px;
  }

  .scanner-mask {
    padding: calc(50% - 125px) calc(50% - 125px);
  }
}

@media (min-height: 700px) {
  .scanner-tips {
    bottom: 180px;
  }
}
</style>
