package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区域
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("area")
public class Area {

    @TableId(value = "area_id", type = IdType.ASSIGN_UUID)
    private String areaId;

    /**
     * 区域的名字
     */
    @TableField("name")
    private String name;

    /**
     * 所属的省
     */
    @TableField("province_id")
    private String provinceId;
}
