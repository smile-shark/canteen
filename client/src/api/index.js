import router from "@/router";
import axios from "axios";
import { Loading } from "element-ui";
const myapi = axios.create({
  baseURL: "/api",
});

let loadingInstance;
// 请求拦截器
myapi.interceptors.request.use(
  (config) => {
    loadingInstance = Loading.service({ fullscreen: true });
    // 这里留着做token的一些提交
    // 1. 验证并添加customer的token
    if (localStorage.customerInfo) {
      config.headers["CustomerToken"] = JSON.parse(
        localStorage.customerInfo
      ).token;
    }
    // 2. 验证并添加staff的token
    if (localStorage.staffInfo) {
      config.headers["StaffToken"] = JSON.parse(localStorage.staffInfo).token;
    }
    // 3. 验证并添加merchant的token
    if (localStorage.merchantInfo) {
      config.headers["MerchantToken"] = localStorage.merchantInfo;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
// 响应拦截器
myapi.interceptors.response.use(
  (response) => {
    if (loadingInstance) {
      loadingInstance.close();
    }
    if (response.data.code == 401) {
      // token失效，跳转登录页面
      router.push("/customer/login");
    }
    return response;
  },
  (error) => {
    loadingInstance.close();
    return Promise.reject(error);
  }
);

export default {
  global: {
    sendEmail: (email) =>
      myapi.post("/global/sendEmail", null, { params: { email } }),
    addressToPos: (address) =>
      myapi.get("/global/addressToPos", { params: { address } }),
  },
  merchant: {
    login: (account, password) =>
      myapi.post("/merchant/login", null, { params: { account, password } }),
  },
  province: {
    list: () => myapi.get("/province/list"),
  },
  area: {
    listById: (id) => myapi.get("/area/listById", { params: { id } }),
    infoByAreaId: (AreaId) =>
      myapi.get("/area/infoByAreaId", { params: { AreaId } }),
  },
  shop: {
    add: (shop) => myapi.post("/shop/add", shop),
    pageList: (page, size, num, state) =>
      myapi.get("/shop/pageList", { params: { page, size, num, state } }),
    update: (shop) => myapi.put("/shop/update", shop),
    delete: (id) => myapi.delete("/shop/delete", { params: { id } }),
    simpleList: () => myapi.get("/shop/simpleList"),
    pageListDineIn: (page, size, shopId, isDineIn) =>
      myapi.get("/shop/pageListDineIn", {
        params: { page, size, shopId, isDineIn },
      }),
    pageListTakeOut: (page, size, shopId, isTakeOut) =>
      myapi.get("/shop/pageListTakeOut", {
        params: { page, size, shopId, isTakeOut },
      }),
    pageListByRange: (page, size, isDineIn, isTakeOut, longitude, latitude) =>
      myapi.get("/shop/pageListByRange", {
        params: { page, size, isDineIn, isTakeOut, longitude, latitude },
      }),
    infoById: (id) => myapi.get("/shop/infoById", { params: { id } }),
  },
  staff: {
    pageList: (page, size, num, shopId, state) =>
      myapi.get("/staff/pageList", {
        params: { page, size, num, shopId, state },
      }),
    add: (staff) => myapi.post("/staff/add", staff),
    update: (staff) => myapi.put("/staff/update", staff),
    delete: (id) => myapi.delete("/staff/delete", { params: { id } }),
    simpleListByShopId: (shopId) =>
      myapi.get("/staff/simpleListByShopId", { params: { shopId } }),
    login: (account, password, perssion) =>
      myapi.post("/staff/login", null, {
        params: { account, password, perssion },
      }),
  },
  workingSchedule: {
    pageList: (page, size, shopId, startDate, endDate) =>
      myapi.get("/workingSchedule/pageList", {
        params: {
          page: page,
          size: size,
          shopId: shopId,
          startDate: formatDate(startDate),
          endDate: formatDate(endDate),
        },
      }),
    add: (workingSchedule) =>
      myapi.post("/workingSchedule/add", workingSchedule),
  },
  staffLoginLog: {
    pageList: (page, size, shopId, startDate, endDate) =>
      myapi.get("/staffLoginLog/pageList", {
        params: {
          page,
          size,
          shopId,
          startDate: formatDate(startDate),
          endDate: formatDate(endDate),
        },
      }),
  },
  cuisineType: {
    list: () => myapi.get("/cuisineType/list"),
    add: (cuisineType) => myapi.post("/cuisineType/add", cuisineType),
    delete: (id) => myapi.delete("/cuisineType/delete", { params: { id } }),
  },
  taste: {
    list: () => myapi.get("/taste/list"),
    add: (taste) => myapi.post("/taste/add", taste),
    delete: (id) => myapi.delete("/taste/delete", { params: { id } }),
  },
  styleOfCooking: {
    list: () => myapi.get("/styleOfCooking/list"),
    add: (styleOfCooking) => myapi.post("/styleOfCooking/add", styleOfCooking),
    delete: (id) => myapi.delete("/styleOfCooking/delete", { params: { id } }),
  },
  cuisineComponent: {
    list: () => myapi.get("/cuisineComponent/list"),
    add: (cuisineComponent) =>
      myapi.post("/cuisineComponent/add", cuisineComponent),
    delete: (id) =>
      myapi.delete("/cuisineComponent/delete", { params: { id } }),
  },
  rawMaterialType: {
    list: () => myapi.get("/rawMaterialType/list"),
    add: (rawMaterialType) =>
      myapi.post("/rawMaterialType/add", rawMaterialType),
    delete: (id) => myapi.delete("/rawMaterialType/delete", { params: { id } }),
  },
  cuisine: {
    simpleListByShopId: (shopId) =>
      myapi.get("/cuisine/simpleListByShopId", { params: { shopId } }),
    pageList: (page, size, cuisineName, cuisineType, state) =>
      myapi.get("/cuisine/pageList", {
        params: { page, size, cuisineName, cuisineType, state },
      }),
    add: (cuisine) => myapi.post("/cuisine/add", cuisine),
    update: (cuisine) => myapi.put("/cuisine/update", cuisine),
    delete: (id) => myapi.delete("/cuisine/delete", { params: { id } }),
    infoById: (id) => myapi.get("/cuisine/infoById", { params: { id } }),
    inventoryPageList: (page, size, name, cuisineTypeId, shopId) =>
      myapi.get("/cuisine/inventoryPageList", {
        params: { page, size, name, cuisineTypeId, shopId },
      }),
    cuisineServiceList: (name, serviceType, cuisineTypeId, shopId) =>
      myapi.get("/cuisine/cuisineServiceList", {
        params: {
          name,
          serviceType,
          cuisineTypeId,
          shopId,
        },
      }),
    getSimpleCuisineById: (id) =>
      myapi.get("/cuisine/getSimpleCuisineById", { params: { id } }),
  },
  rawMaterial: {
    pageList: (page, size, name, rawMaterialTypeId, shopId) =>
      myapi.get("/rawMaterial/pageList", {
        params: { page, size, name, rawMaterialTypeId, shopId },
      }),
    add: (rawMaterial) => myapi.post("/rawMaterial/add", rawMaterial),
    update: (rawMaterial) => myapi.put("/rawMaterial/update", rawMaterial),
    delete: (id) => myapi.delete("/rawMaterial/delete", { params: { id } }),
  },
  diningTableType: {
    list: () => myapi.get("/diningTableType/list"),
    add: (diningTableType) =>
      myapi.post("/diningTableType/add", diningTableType),
    delete: (id) => myapi.delete("/diningTableType/delete", { params: { id } }),
    update: (diningTableType) =>
      myapi.put("/diningTableType/update", diningTableType),
  },
  diningTable: {
    pageList: (page, size, shopId, diningTableTypeId, userState) =>
      myapi.get("/diningTable/pageList", {
        params: { page, size, shopId, diningTableTypeId, userState },
      }),
    listByShopId: (shopId) =>
      myapi.get("/diningTable/listByShopId", {
        params: { shopId },
      }),
    add: (diningTable) => myapi.post("/diningTable/add", diningTable),
    update: (diningTable) => myapi.put("/diningTable/update", diningTable),
    delete: (id) => myapi.delete("/diningTable/delete", { params: { id } }),
    infoById: (id) => myapi.get("/diningTable/infoById", { params: { id } }),
  },
  delivery: {
    infoById: (id) => myapi.get("/delivery/infoById", { params: { id } }),
    saveOrUpdate: (delivery) => myapi.post("/delivery/saveOrUpdate", delivery),
  },
  customer: {
    passwordLogin: (account, password) =>
      myapi.post("/customer/passwordLogin", null, {
        params: { account, password },
      }),
    verifyCodeLogin: (account, verifyCode) =>
      myapi.post("/customer/verifyCodeLogin", null, {
        params: { account, verifyCode },
      }),
    register: (customer, verifyCode) =>
      myapi.post("/customer/register", customer, { params: { verifyCode } }),
    pageList: (page, size, name, shopId) =>
      myapi.get("/customer/pageList", { params: { page, size, name, shopId } }),
  },
  wallet: {
    infoById: (id) => myapi.get("/wallet/infoById", { params: { id } }),
  },
  customerOrder: {
    takeOutAndDineInOrder: (cuisineId, orderType, isAdd) =>
      myapi.post("/customerOrder/takeOutAndDineInOrder", null, {
        params: { cuisineId, orderType, isAdd },
      }),
    dineInOrder: (cuisineId, orderType, isAdd, diningTableId) =>
      myapi.post("/customerOrder/dineInOrder", null, {
        params: { cuisineId, orderType, isAdd, diningTableId },
      }),
  },
  discountCoupon: {
    pageList: (page, size, type, shopId) =>
      myapi.get("/discountCoupon/pageList", {
        params: { page, size, type, shopId },
      }),
    add: (discountCoupon) => myapi.post("/discountCoupon/add", discountCoupon),
    sendCouponList: () => myapi.get("/discountCoupon/sendCouponList"),
    sendCoupon: (couponId, customerId) =>
      myapi.post("/discountCoupon/sendCoupon", null, {
        params: { couponId, customerId },
      }),
    usableCoupon: (shopId, cuisineIds, price) =>
      myapi.get("/discountCoupon/usableCoupon", {
        params: { shopId, cuisineIds, price },
        paramsSerializer:{
          indexes:null
        }
      }),
  },
  discountCouponCustomer: {
    list: (state) =>
      myapi.get("/discountCouponCustomer/list", { params: { state } }),
  },
};

// 转换为 LocalDate 可接受的格式
export function formatDate(date) {
  if (!date) return null;

  const d = new Date(date);
  // 使用本地时间获取年月日
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`; // "2025-08-08"
}
export function formatDateTime(dateTime) {
  if (!dateTime) return null;
  const dt = new Date(dateTime);
  const year = dt.getFullYear();
  const month = String(dt.getMonth() + 1).padStart(2, "0");
  const day = String(dt.getDate()).padStart(2, "0");
  const hour = String(dt.getHours()).padStart(2, "0");
  const minute = String(dt.getMinutes()).padStart(2, "0");
  const second = String(dt.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
}
