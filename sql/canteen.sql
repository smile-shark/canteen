-- 修复后的智慧餐厅数据库脚本

-- 创建客户表
CREATE TABLE `customer` (
  `customer_id` char(32) PRIMARY KEY COMMENT '客户id',
  `integral` longblob COMMENT '积分',
  `wallet_id` char(32) COMMENT '客户对用的钱包',
  `head_portrait` text COMMENT '头像',
  `account` varchar(255) COMMENT '账号',
  `password` varchar(255) COMMENT '密码',
  `level` int DEFAULT 0 COMMENT '会员等级(0：青铜、1：白银、2：黄金...)',
  `consumption_plance` double(10,2) DEFAULT 0 COMMENT '消费总额(可用根据这个消费升级会员等级)',
  `consumption_num` longblob COMMENT '消费的总次数',
  `delivery_address_id` char(32) COMMENT '默认的收货地址',
  `phone` char(11) COMMENT '绑定的手机号',
  `sex` int DEFAULT 0 COMMENT '性别(0：男、1：女、2：保密)',
  `birthday` date COMMENT '用户的生日',
  `create_time` datetime COMMENT '创建时间',
  `shop_id` char(32) COMMENT '创建的门店，可用这个来区分门店的新人绩效',
  INDEX `idx_wallet_id` (`wallet_id`),
  INDEX `idx_delivery_address_id` (`delivery_address_id`),
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '客户';

-- 创建员工表
CREATE TABLE `staff` (
  `staff_id` char(32) PRIMARY KEY COMMENT '员工id',
  `name` varchar(20) COMMENT '员工姓名',
  `account` varchar(255) COMMENT '员工账号',
  `phone` char(11) COMMENT '手机号',
  `password` varchar(30) COMMENT '密码',
  `perssion` int COMMENT '账号对应的权限，比如店长就可以登录多个端(0：后厨、1：服务、2：店长、3：收银)',
  `job_num` varchar(10) COMMENT '员工的工号 (0001)',
  `type` int COMMENT '职务(0：店长、1：收银、2：主厨、3、保洁、4：服务员、5：主厨助理、6：切菜员、7：传菜员)',
  `shop_id` char(32) COMMENT '员工所属的门店',
  `state` int DEFAULT 0 COMMENT '员工的状态(0：在职，1：离职)',
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '员工';

-- 创建商户表
CREATE TABLE `merchant` (
  `merchant_id` char(32) PRIMARY KEY COMMENT '商户id',
  `account` varchar(30) COMMENT '商户的账号',
  `password` varchar(30) COMMENT '商户账号的密码'
) COMMENT = '商户';

-- 创建后厨订单表
CREATE TABLE `back_kitchen_order` (
  `back_kitchen_order_id` char(32) PRIMARY KEY,
  `customer_order_id` char(40) COMMENT '对用的客户订单，其中包含了订单的所有数据',
  `create_time` datetime COMMENT '创建时间（这个时间决定后厨是否该出餐）',
  INDEX `idx_customer_order_id` (`customer_order_id`)
) COMMENT = '后厨订单';

-- 创建钱包表
CREATE TABLE `wallet` (
  `wallet_id` char(32) PRIMARY KEY,
  `balance` double(10,2) COMMENT '余额',
  `pay_password` char(32) COMMENT '支付密码，使用MD5加盐加密'
) COMMENT = '钱包';

-- 创建钱包交易记录表
CREATE TABLE `wallet_transaction_record` (
  `wallet_transaction_record_id` char(32) PRIMARY KEY,
  `wallet_id` char(32) COMMENT '交易记录对应的钱包',
  `type` int DEFAULT 0 COMMENT '交易类型(0：储值、1：消费)',
  `balance` double(10,2) COMMENT '交易金额',
  `record_fun` int COMMENT '交易方式，后面两个是消费时的(0：支付宝支付、1：微信支付、2：现金支付、3：银行卡、0：外卖、1：门店)',
  INDEX `idx_wallet_id` (`wallet_id`)
) COMMENT = '钱包交易记录';

-- 创建餐桌类型表
CREATE TABLE `dining_table_type` (
  `dining_table_type_id` char(32) PRIMARY KEY,
  `type` int COMMENT '桌位类型(0：小桌、1：大桌、2：包间)',
  `max` int COMMENT '最多人数',
  `min` int COMMENT '最少人数'
) COMMENT = '餐桌类型';

-- 创建餐桌表
CREATE TABLE `dining_table` (
  `dining_table_id` char(32) PRIMARY KEY,
  `serial_number` text COMMENT '编号（1F 10010号）',
  `dining_table_type_id` char(32) COMMENT '餐桌的类型',
  `user_state` int COMMENT '餐桌的状态(0：空闲、1：待点菜、2：预结账、3：用餐中)',
  `shop_id` char(32) COMMENT '所属的门店',
  `state` int DEFAULT 0 COMMENT '启用状态(0：启用、1：停用)',
  `qr_code` text COMMENT '台桌点餐的二维码，生成后这里存储url，或者可用尝试其他类型的数据',
  INDEX `idx_dining_table_type_id` (`dining_table_type_id`),
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '餐桌';

-- 创建餐桌预约表
CREATE TABLE `table_reservation` (
  `table_reservation_id` char(32) PRIMARY KEY,
  `shop_id` char(32) COMMENT '预约对应的门店',
  `customer_id` char(32) COMMENT '预约的客户',
  `staff_id` char(32) COMMENT '预约的员工(客户通过电话直接预约的)',
  `reservation_date` date COMMENT '预约日期',
  `reservation_time` date COMMENT '预约时间',
  `dining_table_type_id` char(32) COMMENT '预约的餐桌类型',
  `people_num` int COMMENT '就餐人数',
  `table_num` int COMMENT '预订餐桌数',
  `name` varchar(255) COMMENT '联系人',
  `phone` char(11) COMMENT '手机号',
  `remake` text COMMENT '备注',
  `state` int COMMENT '预约状态：0：待确认、1：已完成、2：已取消',
  `create_time` datetime COMMENT '预约的创建时间',
  INDEX `idx_shop_id` (`shop_id`),
  INDEX `idx_customer_id` (`customer_id`),
  INDEX `idx_staff_id` (`staff_id`),
  INDEX `idx_dining_table_type_id` (`dining_table_type_id`)
) COMMENT = '餐桌预约';

-- 创建收货地址表
CREATE TABLE `delivery_address` (
  `delivery_address_id` char(32) PRIMARY KEY,
  `name` varchar(255) COMMENT '收货人姓名',
  `phone` char(11) COMMENT '联系电话',
  `address` text COMMENT '地址详细',
  `create_time` datetime COMMENT '地址的创建时间'
) COMMENT = '收货地址';

-- 创建客户订单表
CREATE TABLE `customer_order` (
  `customer_order_id` char(40) PRIMARY KEY COMMENT '订单id yyyyMMdd+UUID32',
  `customer_id` char(32) COMMENT '客户id',
  `staff_id` char(32) COMMENT '如果是服务员点的就需要这个id',
  `all_price` double(10,2) COMMENT '订单总额',
  `state` int DEFAULT 0 COMMENT '订单状态(0：待付款、1：待收取、2：已完成、3：已取消、4：待评价、5：未处理、6：已接单、7：已拒绝、8：已送达、9：已自提、10：已作废、11：已退款)',
  `packing_charges` double(10,2) COMMENT '打包费',
  `delivery_cost` double(10,2) COMMENT '配送费',
  `discount_coupon_customer_id` char(32) COMMENT '使用的优惠券',
  `tableware_num` int COMMENT '餐具数量',
  `remark` text COMMENT '备注',
  `create_time` datetime COMMENT '创建时间',
  `pay_time` datetime COMMENT '支付时间',
  `source` int COMMENT '订单来源(0：门店、1：线上)',
  `type` int DEFAULT 0 COMMENT '订单类型(0：堂食、1：外卖、2：自取)',
  `dining_table_id` char(32),
  `delivery_address_id` char(32),
  `pay_type` int DEFAULT 0 COMMENT '支付方式(0：微信支付、1：支付宝支付、2：现金支付、3：银行卡支付、4：会员卡支付)',
  `delete` int DEFAULT 0 COMMENT '是否被用户删除(0：未删除、1：已删除)',
  INDEX `idx_customer_id` (`customer_id`),
  INDEX `idx_staff_id` (`staff_id`),
  INDEX `idx_discount_coupon_customer_id` (`discount_coupon_customer_id`),
  INDEX `idx_dining_table_id` (`dining_table_id`),
  INDEX `idx_delivery_address_id` (`delivery_address_id`)
) COMMENT = '客户订单';

-- 创建退款申请表
CREATE TABLE `drawback_application` (
  `customer_order_id` char(40) PRIMARY KEY COMMENT '被退款的订单',
  `state` int COMMENT '申请的状态(0：未处理、1：已驳回、2：已处理)',
  `drawback_reason` text COMMENT '退款的原因',
  `rejeck_reason` text COMMENT '驳回的原因',
  `balance` double(10,2) COMMENT '退款的金额，理论不能超过订单',
  `staff_id` char(32) COMMENT '处理人',
  INDEX `idx_staff_id` (`staff_id`)
) COMMENT = '退款申请';

-- 创建退款申请图片表
CREATE TABLE `drawback_application_image` (
  `customer_order_id` char(40) COMMENT '被退款的订单',
  `url` text COMMENT '图片的连接',
  `show_order` int COMMENT '展示的排序',
  INDEX `idx_customer_order_id` (`customer_order_id`)
) COMMENT = '退款申请的照片';

-- 创建优惠表
CREATE TABLE `discounts` (
  `customer_order_id` char(40) COMMENT '优惠的客户订单',
  `type` int DEFAULT 1 COMMENT '优惠的方式(0：打折、1：免单、2：会员优惠、3：满减优惠)',
  `num` double(10,2) DEFAULT 0 COMMENT '折扣值:(0-10)，如果免单就填0',
  `balance` double(10,2) COMMENT '优惠的金额',
  `content` text COMMENT '优惠原因',
  INDEX `idx_customer_order_id` (`customer_order_id`)
) COMMENT = '除开优惠券的其他优惠';

-- 创建优惠券表
CREATE TABLE `discount_coupon` (
  `discount_coupon_id` char(32) PRIMARY KEY,
  `start_time` datetime COMMENT '有效期开始时间',
  `end_time` datetime COMMENT '有效期结束时间',
  `name` text COMMENT '优惠券的名称',
  `type` int COMMENT '优惠券的类型(0：满减券、1：折扣券)',
  `price` double(10,2) COMMENT '优惠券金额',
  `discount` double(10,2) COMMENT '优惠的折扣数(0-10)',
  `condition` double(10,2) COMMENT '满多少可用使用的门槛',
  `grant_num` longblob COMMENT '发放的数量',
  `surplus_num` longblob COMMENT '剩余的数量',
  `use_num` longblob COMMENT '优惠券的使用量，有些可能领取了但是没有被使用',
  `astrict_num` longblob COMMENT '每人限制领取的数量',
  `shop_id` char(32) COMMENT '所属的门店',
  `use_area` int COMMENT '可用使用的范围(0：全部菜品、1：指定菜品、3：指定分类)',
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '优惠券';

-- 创建客户优惠券表
CREATE TABLE `discount_coupon_customer` (
  `discount_coupon_customer_id` char(32) PRIMARY KEY COMMENT '一个客户可能有多个同总的优惠券，使用的时候不能直接使用优惠券的发行id',
  `customer_id` char(32) COMMENT '所属的客户',
  `staff_id` char(32) COMMENT '如果是堂食收银收的，如果要使用就需要这个id',
  `discount_coupon_id` char(32) COMMENT '对应的优惠券',
  `get_time` datetime COMMENT '客户获取优惠券的时间',
  `use_time` datetime COMMENT '优惠券的使用时间',
  `state` int COMMENT '优惠券状体(0：未使用、1：已使用、2：已过期)',
  INDEX `idx_customer_id` (`customer_id`),
  INDEX `idx_staff_id` (`staff_id`),
  INDEX `idx_discount_coupon_id` (`discount_coupon_id`)
) COMMENT = '用户拥有的优惠券，优惠券和客户的中间表';

-- 创建优惠券使用范围菜品表
CREATE TABLE `discount_coupon_area_cuisine` (
  `discount_coupon_id` char(32) COMMENT '所属的优惠券',
  `cuisine_id` char(32) COMMENT '对应的菜品',
  `show_order` int COMMENT '展示时的排序',
  INDEX `idx_discount_coupon_id` (`discount_coupon_id`),
  INDEX `idx_cuisine_id` (`cuisine_id`)
) COMMENT = '优惠券使用范围的菜品';

-- 创建优惠券使用范围菜品类别表
CREATE TABLE `discount_coupon_area_cuisine_type` (
  `discount_coupon_id` char(32) COMMENT '所属的优惠券',
  `cuisine_type_id` char(32) COMMENT '对应的菜品类别',
  `show_order` int COMMENT '展示时的排序',
  INDEX `idx_discount_coupon_id` (`discount_coupon_id`),
  INDEX `idx_cuisine_type_id` (`cuisine_type_id`)
) COMMENT = '优惠券使用范围的菜品类型';

-- 创建订单菜品列表表
CREATE TABLE `customer_order_cuisine` (
  `customer_order_id` char(40) COMMENT '客户订单id',
  `cuisine_id` char(32),
  `num` int COMMENT '当前菜品的数量',
  `cuisine_order` int COMMENT '菜品的排序',
  INDEX `idx_customer_order_id` (`customer_order_id`),
  INDEX `idx_cuisine_id` (`cuisine_id`)
) COMMENT = '订单菜品列表';

-- 创建菜系表
CREATE TABLE `style_of_cooking` (
  `style_of_cooking_id` char(32) PRIMARY KEY,
  `name` varchar(20) COMMENT '菜系的名字'
) COMMENT = '菜系';

-- 创建口味表
CREATE TABLE `taste` (
  `taste_id` char(32) PRIMARY KEY,
  `name` varchar(30) COMMENT '口味的名字',
  `num` varchar(10) COMMENT '序号(0001)',
  `remark` text COMMENT '备注'
) COMMENT = '口味';

-- 创建菜品分量表
CREATE TABLE `cuisine_component` (
  `cuisine_component_id` char(32) PRIMARY KEY,
  `name` varchar(30) COMMENT '分量的名字',
  `num` varchar(10) COMMENT '序号(0001)',
  `remark` text COMMENT '备注'
) COMMENT = '菜品的分量/规格';

-- 创建菜品类别表
CREATE TABLE `cuisine_type` (
  `cuisine_type_id` char(32) PRIMARY KEY,
  `name` varchar(30) COMMENT '分类的名称(招牌菜、热菜...)',
  `num` varchar(10) COMMENT '序号(0001)',
  `remark` text COMMENT '备注'
) COMMENT = '菜品的分类';

-- 创建菜品表
CREATE TABLE `cuisine` (
  `cuisine_id` char(32) PRIMARY KEY,
  `name` varchar(255) COMMENT '菜品的名称',
  `num` varchar(10) COMMENT '编号，用于方便每个门店进行记忆(001)',
  `price` double(10,2) COMMENT '价格',
  `creation_method` text COMMENT '制作方法',
  `meat_vegetable` text COMMENT '荤素',
  `main_ingredient` text COMMENT '主料',
  `ingredients` text COMMENT '辅料',
  `style_of_cooking_id` char(32) COMMENT '对应的菜系',
  `cuisine_component_id` char(32) COMMENT '对应的规格',
  `cuisine_type_id` char(32) COMMENT '菜品对应的分类',
  `shop_id` char(32) COMMENT '该菜品对用的门店',
  `state` int DEFAULT 0 COMMENT '菜品的出售状态(0：在售、1：停售)',
  `is_special_offer` int DEFAULT 0 COMMENT '是否特价(0：否、1：是)',
  `special_offer` double(10,2) COMMENT '特价价格',
  `taste_id` char(32) COMMENT '菜品对应的口味',
  `min_num` int DEFAULT 1 COMMENT '起卖的数量',
  `inventory` longblob COMMENT '库存数量',
  `introduce` text COMMENT '菜品的介绍',
  `take_out_show` int DEFAULT 0 COMMENT '是否外卖展示(0：是、1：否)',
  `dine_in_show` int DEFAULT 0 COMMENT '是否堂食展示(0：是、1：否)',
  `warning_min` longblob COMMENT '库存过低预警数量',
  `warning_max` longblob COMMENT '库存过高预警数量',
  INDEX `idx_style_of_cooking_id` (`style_of_cooking_id`),
  INDEX `idx_taste_id` (`taste_id`),
  INDEX `idx_cuisine_component_id` (`cuisine_component_id`),
  INDEX `idx_cuisine_type_id` (`cuisine_type_id`),
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '菜品详情';

-- 创建菜品图片表
CREATE TABLE `cuisine_image` (
  `cuisine_id` char(32) COMMENT '菜品id',
  `image` text COMMENT '图片的url',
  `image_order` int COMMENT '图片的排序',
  INDEX `idx_cuisine_id` (`cuisine_id`)
) COMMENT = '菜品的图片，一个菜可能有多个图片';

-- 创建原料分类表
CREATE TABLE `raw_material_type` (
  `raw_material_type_id` char(32) PRIMARY KEY,
  `name` varchar(30) COMMENT '分类的名称',
  `num` varchar(10) COMMENT '序号(0001)',
  `remark` text COMMENT '备注'
) COMMENT = '原料分类表';

-- 创建原料表
CREATE TABLE `raw_material` (
  `raw_material_id` char(32) PRIMARY KEY,
  `raw_material_type_id` char(32) COMMENT '原料对应的分类',
  `name` text COMMENT '原料的名称',
  `shop_id` char(32) COMMENT '所属的门店',
  `num` varchar(10) COMMENT '序号(0001)',
  `remark` text COMMENT '备注',
  `inventory` longblob COMMENT '库存数量',
  `warning_min` longblob COMMENT '库存过低预警数量',
  `warning_max` longblob COMMENT '库存过高预警数量',
  INDEX `idx_raw_material_type_id` (`raw_material_type_id`),
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '原料表';

-- 创建菜品每日销售数量表
CREATE TABLE `daily_sales_volume` (
  `cuisine_id` char(32) COMMENT '对应的菜品',
  `sales_date` date COMMENT '销售的日期',
  `num` longblob COMMENT '每日的销售数量',
  INDEX `idx_cuisine_id` (`cuisine_id`)
) COMMENT = '菜品每日销售数量';

-- 创建评价表
CREATE TABLE `evaluate` (
  `evaluate_id` char(32) PRIMARY KEY,
  `customer_order_id` char(40) COMMENT '客户订单id',
  `score` int COMMENT '评价分数(1-5)对应星数',
  `content` text COMMENT '评价的内容',
  `time` datetime COMMENT '评价的时间',
  INDEX `idx_customer_order_id` (`customer_order_id`)
) COMMENT = '评价';

-- 创建评价图片表
CREATE TABLE `evaluate_image` (
  `evaluate_id` char(32) COMMENT '评价id',
  `url` text COMMENT '图片连接',
  `image_order` int COMMENT '图片的排序',
  INDEX `idx_evaluate_id` (`evaluate_id`)
) COMMENT = '评价图片';

-- 创建省表
CREATE TABLE `province` (
  `province_id` char(32) PRIMARY KEY,
  `name` text COMMENT '省的名字'
) COMMENT = '省';

-- 创建区域表
CREATE TABLE `area` (
  `area_id` char(32) PRIMARY KEY,
  `name` text COMMENT '区域的名字',
  `province_id` char(32) COMMENT '所属的省',
  INDEX `idx_province_id` (`province_id`)
) COMMENT = '区域';

-- 创建门店表
CREATE TABLE `shop` (
  `shop_id` char(32) PRIMARY KEY,
  `shop_order` varchar(10) COMMENT '门店的排序，也可以是编号 (001)',
  `area_id` char(32),
  `address` text COMMENT '详细的地址',
  `phone` text COMMENT '门店的固定电话',
  `create_date` date COMMENT '创建的日期',
  `people_num` int COMMENT '门店的人数',
  `state` int COMMENT '门店的状态(0：正常、1：关闭)',
  `is_dine_in` int DEFAULT 0 COMMENT '是否提供堂食的服务(0：是、1：否)',
  `is_take_out` int DEFAULT 0 COMMENT '是否提供外卖的服务(0：是、1：否)',
  `opening_hours_start` time COMMENT '营业开始时间',
  `opening_hours_end` time COMMENT '营业结束时间',
  INDEX `idx_area_id` (`area_id`)
) COMMENT = '门店表';

-- 创建配送表
CREATE TABLE `delivery` (
  `shop_id` char(32) COMMENT '对应的门店',
  `delivery_time_start` time COMMENT '外卖/自提时间开始',
  `delivery_time_end` time COMMENT '外卖/自提时间结束',
  `is_full_free_delivery` int COMMENT '是否开始满免配送费',
  `full_free_delivery_price` double(10,2) COMMENT '满多少元免费配送',
  `payment_method` int COMMENT '配送费收取方式(0：固定区域收费、1：按距离阶梯收费)',
  `area` double(10,1) COMMENT '设置区域半径',
  `area_delivery_price` double(10,2) COMMENT '设置区域固定费用',
  `distance_start_num` double(10,1) COMMENT '初始收费公里数',
  `distance_start_price` double(10,2) COMMENT '初始收费公价格',
  `distance_add_num` double(10,1) COMMENT '没增加的公里数',
  `distance_add_price` double(10,2) COMMENT '每次增加的费用',
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '配送的配置有点多，就分开了';

-- 创建排班表
CREATE TABLE `working_schedule` (
  `staff_id` char(32) COMMENT '排班对应的员工',
  `working_date` date COMMENT '日期',
  `type` int COMMENT '排班的类型(0：早班、1：晚班、2：休)',
  INDEX `idx_staff_id` (`staff_id`)
) COMMENT = '排班表';

-- 创建员工登录日志表
CREATE TABLE `staff_login_log` (
  `staff_id` char(32) COMMENT '对应的员工',
  `login_time` datetime COMMENT '登录时间',
  `quit_time` datetime COMMENT '退出时间',
  `login_ip` varchar(15) COMMENT '登录的ip地址',
  INDEX `idx_staff_id` (`staff_id`)
) COMMENT = '员工登录日志';

-- 创建打印机表
CREATE TABLE `printer` (
  `printer_id` char(32) PRIMARY KEY,
  `num` varchar(10) COMMENT '打印机的编号 (00001)',
  `name` varchar(30) COMMENT '设备的名称 (惠普DJ2720)',
  `type` int DEFAULT 0 COMMENT '打印机的类型(0：云打印机、1：网口打印机、2：USB打印机)',
  `receipt_type` int DEFAULT 0 COMMENT '小票类型(0：普通小票、1：标签小票)',
  `brand` varchar(30) COMMENT '设备的品牌',
  `sn_num` text COMMENT '设备的SN号',
  `printer_key` text COMMENT '设备key',
  `receipt_specification` int COMMENT '设备规格(0：80mm、1：58mm)',
  `shop_id` char(32) COMMENT '打印机所属的门店',
  `state` int DEFAULT 0 COMMENT '状态(0：可用、1：不可用)',
  INDEX `idx_shop_id` (`shop_id`)
) COMMENT = '打印机';

-- 创建小票表
CREATE TABLE `receipt` (
  `type` int COMMENT '小票的类型(0：堂食小票、1：外卖小票、2：后厨小票、3：充值小票)',
  `shop_name` int COMMENT '门店名称(0：不显示、1显示)---可用的小票类型(013)',
  `title_welecome` int COMMENT '头部欢迎语(0：不显示、1显示)---可用的小票类型(013)',
  `title_welecome_word` text COMMENT '头部欢迎内容',
  `receipt_type` int COMMENT '票据类型(0：不显示、1显示)---可用的小票类型(0123)',
  `table_num` int COMMENT '桌号/取餐号(0：不显示、1显示)---可用的小票类型(02)',
  `people_num` int COMMENT '人数(0：不显示、1显示)---可用的小票类型(0)',
  `oreder_num` int COMMENT '订单编号(0：不显示、1显示)---可用的小票类型(0123)',
  `oreder_create_time` int COMMENT '下单时间(0：不显示、1显示)---可用的小票类型(012)',
  `cuisine_detail` int COMMENT '菜品详情(0：不显示、1显示)---可用的小票类型(012)',
  `remark` int COMMENT '备注(0：不显示、1显示)---可用的小票类型(02)',
  `pay_detail` int COMMENT '支付详情(0：不显示、1显示)---可用的小票类型(01)',
  `discount_detail` int COMMENT '优惠详情(0：不显示、1显示)---可用的小票类型(01)',
  `shop_address` int COMMENT '门店地址(0：不显示、1显示)---可用的小票类型(013)',
  `shop_phone` int COMMENT '门店电话(0：不显示、1显示)---可用的小票类型(013)',
  `wechat_public_account_qr_code` int COMMENT '公众号二维码(0：不显示、1显示)---可用的小票类型(013)',
  `mini_program_qr_code` int COMMENT '小程序二维码(0：不显示、1显示)---可用的小票类型(013)',
  `closing_remarks_gratitude` int COMMENT '结束致谢语(0：不显示、1显示)---可用的小票类型(01)',
  `closing_remarks_gratitude_word` int COMMENT '结束致谢语内容',
  `customer_name` int COMMENT '顾客姓名(0：不显示、1显示)---可用的小票类型(13)',
  `customer_phone` int COMMENT '顾客手机号(0：不显示、1显示)---可用的小票类型(13)',
  `customer_address` int COMMENT '配送地址(0：不显示、1显示)---可用的小票类型(1)',
  `take_out_time` int COMMENT '配送时间/自提时间(0：不显示、1显示)---可用的小票类型(12)',
  `food_container_cost` int COMMENT '餐盒费(0：不显示、1显示)---可用的小票类型(1)',
  `delivery_cost` int COMMENT '配送费(0：不显示、1显示)---可用的小票类型(1)',
  `recharge_time` int COMMENT '充值时间(0：不显示、1显示)---可用的小票类型(3)',
  `recharge_detail` int COMMENT '充值详情(0：不显示、1显示)---可用的小票类型(3)'
) COMMENT = '小票的样式管理';

-- 添加外键约束
ALTER TABLE `delivery` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `discount_coupon_area_cuisine` ADD FOREIGN KEY (`discount_coupon_id`) REFERENCES `discount_coupon` (`discount_coupon_id`);
ALTER TABLE `discount_coupon_area_cuisine` ADD FOREIGN KEY (`cuisine_id`) REFERENCES `cuisine` (`cuisine_id`);

ALTER TABLE `discount_coupon_customer` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);
ALTER TABLE `discount_coupon_customer` ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`);
ALTER TABLE `discount_coupon_customer` ADD FOREIGN KEY (`discount_coupon_id`) REFERENCES `discount_coupon` (`discount_coupon_id`);

ALTER TABLE `customer_order` ADD FOREIGN KEY (`discount_coupon_customer_id`) REFERENCES `discount_coupon_customer` (`discount_coupon_customer_id`);
ALTER TABLE `customer_order` ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`);
ALTER TABLE `customer_order` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);
ALTER TABLE `customer_order` ADD FOREIGN KEY (`dining_table_id`) REFERENCES `dining_table` (`dining_table_id`);
ALTER TABLE `customer_order` ADD FOREIGN KEY (`delivery_address_id`) REFERENCES `delivery_address` (`delivery_address_id`);

ALTER TABLE `discount_coupon` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `customer` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);
ALTER TABLE `customer` ADD FOREIGN KEY (`delivery_address_id`) REFERENCES `delivery_address` (`delivery_address_id`);
ALTER TABLE `customer` ADD FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`wallet_id`);

ALTER TABLE `table_reservation` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);
ALTER TABLE `table_reservation` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);
ALTER TABLE `table_reservation` ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`);
ALTER TABLE `table_reservation` ADD FOREIGN KEY (`dining_table_type_id`) REFERENCES `dining_table_type` (`dining_table_type_id`);

ALTER TABLE `drawback_application` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);
ALTER TABLE `drawback_application` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

ALTER TABLE `drawback_application_image` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);

ALTER TABLE `discounts` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);

ALTER TABLE `discount_coupon_area_cuisine_type` ADD FOREIGN KEY (`discount_coupon_id`) REFERENCES `discount_coupon` (`discount_coupon_id`);
ALTER TABLE `discount_coupon_area_cuisine_type` ADD FOREIGN KEY (`cuisine_type_id`) REFERENCES `cuisine_type` (`cuisine_type_id`);

ALTER TABLE `cuisine` ADD FOREIGN KEY (`style_of_cooking_id`) REFERENCES `style_of_cooking` (`style_of_cooking_id`);
ALTER TABLE `cuisine` ADD FOREIGN KEY (`taste_id`) REFERENCES `taste` (`taste_id`);
ALTER TABLE `cuisine` ADD FOREIGN KEY (`cuisine_component_id`) REFERENCES `cuisine_component` (`cuisine_component_id`);
ALTER TABLE `cuisine` ADD FOREIGN KEY (`cuisine_type_id`) REFERENCES `cuisine_type` (`cuisine_type_id`);
ALTER TABLE `cuisine` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `raw_material` ADD FOREIGN KEY (`raw_material_type_id`) REFERENCES `raw_material_type` (`raw_material_type_id`);
ALTER TABLE `raw_material` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `cuisine_image` ADD FOREIGN KEY (`cuisine_id`) REFERENCES `cuisine` (`cuisine_id`);

ALTER TABLE `printer` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `staff_login_log` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

ALTER TABLE `staff` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

ALTER TABLE `working_schedule` ADD FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

ALTER TABLE `area` ADD FOREIGN KEY (`province_id`) REFERENCES `province` (`province_id`);

ALTER TABLE `shop` ADD FOREIGN KEY (`area_id`) REFERENCES `area` (`area_id`);

ALTER TABLE `evaluate` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);

ALTER TABLE `evaluate_image` ADD FOREIGN KEY (`evaluate_id`) REFERENCES `evaluate` (`evaluate_id`);

ALTER TABLE `customer_order_cuisine` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);
ALTER TABLE `customer_order_cuisine` ADD FOREIGN KEY (`cuisine_id`) REFERENCES `cuisine` (`cuisine_id`);

ALTER TABLE `back_kitchen_order` ADD FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`customer_order_id`);

ALTER TABLE `daily_sales_volume` ADD FOREIGN KEY (`cuisine_id`) REFERENCES `cuisine` (`cuisine_id`);

ALTER TABLE `dining_table` ADD FOREIGN KEY (`dining_table_type_id`) REFERENCES `dining_table_type` (`dining_table_type_id`);
ALTER TABLE `dining_table` ADD FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);