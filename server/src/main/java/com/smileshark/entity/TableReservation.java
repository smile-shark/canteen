package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 餐桌预约
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("table_reservation")
public class TableReservation {

    @TableId(value = "table_reservation_id", type = IdType.ASSIGN_UUID)
    private String tableReservationId;

    /**
     * 预约对应的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 预约的客户
     */
    @TableField("customer_id")
    private String customerId;

    /**
     * 预约的员工(客户通过电话直接预约的)
     */
    @TableField("staff_id")
    private String staffId;

    /**
     * 预约日期
     */
    @TableField("reservation_date")
    private LocalDate reservationDate;

    /**
     * 预约时间
     */
    @TableField("reservation_time")
    private LocalDate reservationTime;

    /**
     * 预约的餐桌类型
     */
    @TableField("dining_table_type_id")
    private String diningTableTypeId;

    /**
     * 就餐人数
     */
    @TableField("people_num")
    private Integer peopleNum;

    /**
     * 预订餐桌数
     */
    @TableField("table_num")
    private Integer tableNum;

    /**
     * 联系人
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 备注
     */
    @TableField("remake")
    private String remake;

    /**
     * 预约状态：0：待确认、1：已完成、2：已取消
     */
    @TableField("state")
    private Integer state;

    /**
     * 预约的创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
