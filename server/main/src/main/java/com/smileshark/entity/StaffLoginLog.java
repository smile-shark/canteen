package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 员工登录日志
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("staff_login_log")
public class StaffLoginLog {

    /**
     * 对应的员工
     */
    @TableField("staff_id")
    private String staffId;

    /**
     * 登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;

    /**
     * 退出时间
     */
    @TableField("quit_time")
    private LocalDateTime quitTime;

    /**
     * 登录的ip地址
     */
    @TableField("login_ip")
    private String loginIp;

    @TableField(exist = false)
    private String staffJobNum;
    @TableField(exist = false)
    private String staffName;
    @TableField(exist = false)
    private String shopName;
    @TableField(exist = false)
    private Integer staffType;
}
