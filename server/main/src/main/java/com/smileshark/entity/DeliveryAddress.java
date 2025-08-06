package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收货地址
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("delivery_address")
public class DeliveryAddress {

    @TableId(value = "delivery_address_id", type = IdType.ASSIGN_UUID)
    private String deliveryAddressId;

    /**
     * 收货人姓名
     */
    @TableField("name")
    private String name;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 地址详细
     */
    @TableField("address")
    private String address;

    /**
     * 地址的创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
