// amp.js
import AMapLoader from "@amap/amap-jsapi-loader";
export function AMap(onComplete) {
  window._AMapSecurityConfig = {
    securityJsCode: "1ed63c4a9418f8b7142007aea096a860", // 安全密钥
  };
  AMapLoader.load({
    key: "50276c957e49ab16ac51070dc84b24e1", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.Scale", "AMap.Geolocation"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  }).then((AMap) => {
    AMap.plugin("AMap.Geolocation", function () {
      const geolocation = new AMap.Geolocation({
        // 是否使用高精度定位，默认：true
        enableHighAccuracy: true,
        // 设置定位超时时间，默认：无穷大
        timeout: 10000,
        // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
        buttonOffset: new AMap.Pixel(10, 20),
        //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
        zoomToAccuracy: true,
        //  定位按钮的排放位置,  RB表示右下
        buttonPosition: "RB",
      });
      geolocation.getCurrentPosition(function (status, result) {
        if (status == "complete") {
          try {
            onComplete(result);
          } catch (e) {}
        }
        if (status == "error") {
          onError(result);
        }
      });
      //   function onComplete(data) {
      //     console.log("onComplete");

      //     console.log(data);
      //     // data是具体的定位信息
      //   }
      function onError(data) {
        console.log("onError");
        console.log(data);
        // data是具体的定位信息

        getCityInfo(); // ip精准定位失败后获取城市定位（普通ip定位）
      }
      function getCityInfo() {
        // ip精准定位失败后获取城市定位（普通ip定位）
        geolocation.getCityInfo(function (status, result) {
          if (status == "complete") {
            onCityComplete(result);
            resolve();
          }
          if (status == "error") {
            onCityError(result);
            reject();
          }
        });
        function onCityComplete(data) {
          console.log("城市定位数据：", data);
          // data是具体的定位信息
        }
        function onCityError(data) {
          console.log("城市定位错误信息：", data);
          // data是具体的定位信息
          ElMessage.error(`获取定位失败，失败原因：${data.message}`);
        }
      }
    });
  });
}
