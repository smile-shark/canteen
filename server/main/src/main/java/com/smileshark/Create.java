package com.smileshark;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;

public class Create {
    public static void main(String[] args) {
        /**
         * 先配置数据源
         */
        MySqlQuery mySqlQuery = new MySqlQuery() {
            @Override
            public String[] fieldCustom() {
                return new String[]{"Default"};
            }
        };
        // 配置位置1:数据库连接信息
        DataSourceConfig dsc = new DataSourceConfig.Builder(
                "jdbc:mysql://localhost:3306/canteen?&useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                "root", "123456").dbQuery(mySqlQuery).build();
        AutoGenerator generator = new AutoGenerator(dsc);

        // 配置位置2:全局信息配置
        String projectPath = System.getProperty("user.dir"); // 获取项目路径
        String filePath = projectPath + "/src/main/java"; // java下的文件路径
        GlobalConfig global = new GlobalConfig.Builder().outputDir(filePath)// 生成的输出路径
                .author("smile鲨鱼")// 生成的作者名字
                // .enableSwagger()开启swagger，需要添加swagger依赖并配置
                .dateType(DateType.TIME_PACK)// 时间策略
                .commentDate("yyyy年MM月dd日")// 格式化时间格式
                .disableOpenDir()// 禁止打开输出目录，默认false
                .build();

        // 配置位置3:包名规范配置
        PackageConfig packages = new PackageConfig.Builder().entity("entity")// 实体类包名
                .parent("com.smileshark")// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
                .controller("controller")// 控制层包名
                .mapper("mapper")// mapper层包名
                .xml("mapper.xml")// 数据访问层xml包名
                .service("service")// service层包名
                .serviceImpl("service.imp")// service实现类包名
                .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")) // 路径配置信息
                .build();

        TemplateConfig template = new TemplateConfig.Builder().build();
        // 配置位置4:生成策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder().enableCapitalMode()// 开启全局大写命名
                .addInclude("customer", "staff", "merchant", "back_kitchen_order", "wallet",
                        "wallet_transaction_record", "dining_table", "dining_table_type",
                        "table_reservation", "delivery_address", "customer_order",
                        "drawback_application", "drawback_application_image", "discounts",
                        "discount_coupon_customer", "discount_coupon", "discount_coupon_area_cuisine",
                        "discount_coupon_area_cuisine_type", "customer_order_cuisine", "cuisine",
                        "raw_material", "raw_material_type", "cuisine_image", "cuisine_type",
                        "style_of_cooking", "taste", "cuisine_component", "daily_sales_volume",
                        "evaluate", "evaluate_image", "shop", "delivery", "area", "province",
                        "working_schedule", "staff_login_log", "printer", "receipt")// 指定要生成的数据表名
                .entityBuilder() // 实体策略配置
                .disableSerialVersionUID() // 关键修改：禁用生成SerialVersionUID
                .enableChainModel()// 开启链式模型
                .enableLombok()// 开启lombok
                .enableRemoveIsPrefix()// 开启 Boolean 类型字段移除 is 前缀
                .enableTableFieldAnnotation()// 开启生成实体时生成字段注解
                .naming(NamingStrategy.underline_to_camel)// 数据表映射实体命名策略
                .columnNaming(NamingStrategy.underline_to_camel)// 表字段映射实体属性命名规则
                .idType(IdType.ASSIGN_UUID)// 添加全局主键类型
                .formatFileName("%s")// 格式化实体名称
                .enableFileOverride() // 覆盖 Entity 文件
                .build()

                .mapperBuilder()// mapper文件策略
                .enableMapperAnnotation()// 开启mapper注解
                .enableBaseResultMap()// 启用xml文件中的BaseResultMap 生成
                .enableBaseColumnList()// 启用xml文件中的BaseColumnList
                .formatMapperFileName("%sMapper")// 格式化Dao类名称
                .formatXmlFileName("%sMapper")// 格式化xml文件名称
                .enableFileOverride() // 覆盖 Mapper 文件
                .build()

                .serviceBuilder()// service文件策略
                .formatServiceFileName("%sService")// 格式化 service 接口文件名称
                .formatServiceImplFileName("%sServiceImp")// 格式化 service 接口文件名称
                .enableFileOverride() // 覆盖 Service 文件
                .build()

                .controllerBuilder()// 控制层策略
                .enableRestStyle()// 开启生成@RestController
                .formatFileName("%sController")// 格式化文件名称
                .enableFileOverride() // 覆盖 Controller 文件
                .build();
        generator.global(global).template(template)
                .packageInfo(packages).strategy(strategyConfig).execute();
    }
}
