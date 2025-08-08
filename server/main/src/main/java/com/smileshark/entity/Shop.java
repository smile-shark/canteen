package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 门店表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("shop")
public class Shop {

    @TableId(value = "shop_id", type = IdType.ASSIGN_UUID)
    private String shopId;

    /**
     * 门店的排序，也可以是编号 (001)
     */
    @TableField("shop_order")
    private String shopOrder;

    @TableField("name")
    private String name;

    @TableField("area_id")
    private String areaId;

    /**
     * 详细的地址
     */
    @TableField("address")
    private String address;

    /**
     * 门店的固定电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 创建的日期
     */
    @TableField("create_date")
    private LocalDate createDate;

    /**
     * 门店的人数
     */
    @TableField("people_num")
    private Integer peopleNum;

    /**
     * 门店的状态(0：正常、1：关闭)
     */
    @TableField("state")
    private Integer state;

    /**
     * 是否提供堂食的服务(0：是、1：否)
     */
    @TableField("is_dine_in")
    private Integer isDineIn;

    /**
     * 是否提供外卖的服务(0：是、1：否)
     */
    @TableField("is_take_out")
    private Integer isTakeOut;

    /**
     * 营业开始时间
     */
    @TableField("opening_hours_start")
    private LocalTime openingHoursStart;

    /**
     * 营业结束时间
     */
    @TableField("opening_hours_end")
    private LocalTime openingHoursEnd;

    @TableField(exist = false)
    private Delivery delivery;
}
