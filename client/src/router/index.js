import Vue from "vue";
import VueRouter from "vue-router";
import CustomerMenu from "@/views/customer/Menu.vue";
import CustomerHome from "@/views/customer/Home.vue";
import CustomerProfile from "@/views/customer/Profile.vue";
import CustomerOrderService from "@/views/customer/home/OrderService.vue";
import CustomerTableReservation from "@/views/customer/home/TableReservation.vue";
import StaffLoginMain from "@/views/staff/LoginMain.vue";
import StaffLogin from "@/views/staff/Login.vue";
import MerchantLogin from "@/views/merchant/Login.vue";
import MerchantMenu from "@/views/merchant/Menu.vue";
import MerchantHome from "@/views/merchant/home/Home.vue";
import CustomerAddressManagement from "@/views/customer/profile/AddressManagement.vue";
import CustomerCoupons from "@/views/customer/profile/Coupons.vue";
import CustomerOrderList from "@/views/customer/profile/OrderList.vue";
import CustomerReservationList from "@/views/customer/profile/ReservationList.vue";
import StaffBackKitchenHome from "@/views/staff/back-kitchen/Home.vue";
import StaffCashieringHome from "@/views/staff/cashiering/Home.vue";
import StaffServiceHome from "@/views/staff/service/Home.vue";
import ShopList from "@/views/customer/ShopList.vue";
import CustomerLogin from "@/views/customer/Login.vue";
import CustomerScanQrCode from "@/views/customer/ScanQrCode.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/merchant/login",
    name: "MerchantLogin",
    component: MerchantLogin,
  },
  {
    path: "/merchant/menu",
    name: "MerchantMenu",
    component: MerchantMenu,
    children: [
      // 首页
      {
        path: "",
        name: "MerchantHome",
        component: MerchantHome,
      },
      // 门店相关
      // 门店管理
      {
        path: "store/management",
        name: "StoreManagement",
        component: () => import("@/views/merchant/store/StoreManagement.vue"),
      },
      // 员工管理
      {
        path: "store/staff",
        name: "StaffManagement",
        component: () => import("@/views/merchant/store/StaffManagement.vue"),
      },
      // 台桌管理
      {
        path: "store/table",
        name: "TableManagement",
        component: () => import("@/views/merchant/store/TableManagement.vue"),
      },
      // 硬件管理
      {
        path: "store/hardware",
        name: "HardwareManagement",
        component: () =>
          import("@/views/merchant/store/HardwareManagement.vue"),
      },
      // 业务相关
      // 菜品
      {
        path: "business/dishes",
        name: "Dishes",
        component: () => import("@/views/merchant/business/Dishes.vue"),
      },
      // 堂食
      {
        path: "business/dinein",
        name: "DineIn",
        component: () => import("@/views/merchant/business/DineIn.vue"),
      },
      // 外卖
      {
        path: "business/takeaway",
        name: "Takeaway",
        component: () => import("@/views/merchant/business/Takeaway.vue"),
      },
      // 库存相关
      // 菜品库
      {
        path: "inventory/dishes",
        name: "DishesInventory",
        component: () =>
          import("@/views/merchant/inventory/DishesInventory.vue"),
      },
      // 原料库
      {
        path: "inventory/ingredients",
        name: "IngredientsInventory",
        component: () =>
          import("@/views/merchant/inventory/IngredientsInventory.vue"),
      },
      // 库存盘点
      {
        path: "inventory/check",
        name: "InventoryCheck",
        component: () =>
          import("@/views/merchant/inventory/InventoryCheck.vue"),
      },
      // 订单相关
      // 堂食订单
      {
        path: "orders/dinein",
        name: "DineInOrders",
        component: () => import("@/views/merchant/orders/DineInOrders.vue"),
      },
      // 外卖订单
      {
        path: "orders/takeaway",
        name: "TakeawayOrders",
        component: () => import("@/views/merchant/orders/TakeawayOrders.vue"),
      },
      // 餐桌预约订单
      {
        path: "orders/reservation",
        name: "ReservationOrders",
        component: () =>
          import("@/views/merchant/orders/ReservationOrders.vue"),
      },
      // 会员相关
      // 会员管理
      {
        path: "members/management",
        name: "MemberManagement",
        component: () =>
          import("@/views/merchant/members/MemberManagement.vue"),
      },
      // 会员类别
      {
        path: "members/categories",
        name: "MemberCategories",
        component: () =>
          import("@/views/merchant/members/MemberCategories.vue"),
      },
      // 促销相关
      // 优惠券
      {
        path: "promotion/coupons",
        name: "Coupons",
        component: () => import("@/views/merchant/promotion/Coupons.vue"),
      },
      // 报表相关
      // 菜品流水
      {
        path: "reports/dishes-flow",
        name: "DishesFlow",
        component: () => import("@/views/merchant/reports/DishesFlow.vue"),
      },
      // 收款报表
      {
        path: "reports/payment",
        name: "PaymentReport",
        component: () => import("@/views/merchant/reports/PaymentReport.vue"),
      },
      // 菜品销售排行
      {
        path: "reports/dishes-ranking",
        name: "DishesRanking",
        component: () => import("@/views/merchant/reports/DishesRanking.vue"),
      },
      // 退单记录
      {
        path: "reports/refund",
        name: "RefundRecords",
        component: () => import("@/views/merchant/reports/RefundRecords.vue"),
      },
      // 门店销售排行
      {
        path: "reports/store-ranking",
        name: "StoreSalesRanking",
        component: () =>
          import("@/views/merchant/reports/StoreSalesRanking.vue"),
      },
      // 设置相关
      // 支付设置
      {
        path: "settings/payment",
        name: "PaymentSettings",
        component: () =>
          import("@/views/merchant/settings/PaymentSettings.vue"),
      },
      // 账号设置
      {
        path: "settings/account",
        name: "AccountSettings",
        component: () =>
          import("@/views/merchant/settings/AccountSettings.vue"),
      },
    ],
  },
  {
    path: "/staff/login/:perssion",
    name: "StaffLogin",
    component: StaffLogin,
  },
  {
    path: "/staff/loginmain",
    name: "StaffLoginMain",
    component: StaffLoginMain,
  },
  {
    path: "/staff/back-kitchen",
    name: "StaffBackKitchenHome",
    component: StaffBackKitchenHome,
  },
  {
    path: "/staff/cashiering",
    name: "StaffCashieringHome",
    component: StaffCashieringHome,
  },
  {
    path: "/staff/service",
    name: "StaffServiceHome",
    component: StaffServiceHome,
  },
  {
    path: "/",
    redirect: "/customer/shopList",
  },
  {
    path: "/customer/shopList",
    name: "ShopList",
    component: ShopList,
  },
  {
    path: "/customer/login",
    name: "CustomerLogin",
    component: CustomerLogin,
  },
  {
    path: "/customer",
    name: "CustomerMenu",
    component: CustomerMenu,
    children: [
      // 默认home页面
      {
        path: "",
        redirect: { name: "CustomerHome" },
      },
      {
        path: "home/:shopId([0-9a-fA-F]{32})",
        name: "CustomerHome",
        component: CustomerHome,
      },
      {
        path: "profile",
        name: "CustomerProfile",
        component: CustomerProfile,
      },
    ],
  },
  {
    path: "/customer/home/orderservice/:serverType",
    name: "CustomerOrderService",
    component: CustomerOrderService,
  },
  {
    path: "/customer/home/tablereservation",
    name: "CustomerTableReservation",
    component: CustomerTableReservation,
  },
  {
    path: "/customer/profile/addressmanagement",
    name: "CustomerAddressManagement",
    component: CustomerAddressManagement,
  },
  {
    path: "/customer/profile/coupons",
    name: "CustomerCoupons",
    component: CustomerCoupons,
  },
  {
    path: "/customer/profile/orderlist",
    name: "CustomerOrderList",
    component: CustomerOrderList,
  },
  {
    path: "/customer/profile/reservationlist",
    name: "CustomerReservationList",
    component: CustomerReservationList,
  },
  {
    path: "/customer/scanqrcode",
    name: "CustomerScanQrCode",
    component: CustomerScanQrCode,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
