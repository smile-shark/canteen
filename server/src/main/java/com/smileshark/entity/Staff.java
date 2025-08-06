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
 * 员工
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("staff")
public class Staff {

    /**
     * 员工id
     */
    @TableId(value = "staff_id", type = IdType.ASSIGN_UUID)
    private String staffId;

    /**
     * 员工姓名
     */
    @TableField("name")
    private String name;

    /**
     * 员工账号
     */
    @TableField("account")
    private String account;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 账号对应的权限，比如店长就可以登录多个端(0：后厨、1：服务、2：店长、3：收银)
     */
    @TableField("perssion")
    private Integer perssion;

    /**
     * 员工的工号 (0001)
     */
    @TableField("job_num")
    private String jobNum;

    /**
     * 职务(0：店长、1：收银、2：主厨、3、保洁、4：服务员、5：主厨助理、6：切菜员、7：传菜员)
     */
    @TableField("type")
    private Integer type;

    /**
     * 员工所属的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 员工的状态(0：在职，1：离职)
     */
    @TableField("state")
    private Integer state;

    /**
     * 入职日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
