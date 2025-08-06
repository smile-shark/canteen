package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 菜品详情
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cuisine")
public class Cuisine {

    @TableId(value = "cuisine_id", type = IdType.ASSIGN_UUID)
    private String cuisineId;

    /**
     * 菜品的名称
     */
    @TableField("name")
    private String name;

    /**
     * 编号，用于方便每个门店进行记忆(001)
     */
    @TableField("num")
    private String num;

    /**
     * 价格
     */
    @TableField("price")
    private Double price;

    /**
     * 制作方法
     */
    @TableField("creation_method")
    private String creationMethod;

    /**
     * 荤素
     */
    @TableField("meat_vegetable")
    private String meatVegetable;

    /**
     * 主料
     */
    @TableField("main_ingredient")
    private String mainIngredient;

    /**
     * 辅料
     */
    @TableField("ingredients")
    private String ingredients;

    /**
     * 对应的菜系
     */
    @TableField("style_of_cooking_id")
    private String styleOfCookingId;

    /**
     * 对应的规格
     */
    @TableField("cuisine_component_id")
    private String cuisineComponentId;

    /**
     * 菜品对应的分类
     */
    @TableField("cuisine_type_id")
    private String cuisineTypeId;

    /**
     * 该菜品对用的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 菜品的出售状态(0：在售、1：停售)
     */
    @TableField("state")
    private Integer state;

    /**
     * 是否特价(0：否、1：是)
     */
    @TableField("is_special_offer")
    private Integer isSpecialOffer;

    /**
     * 特价价格
     */
    @TableField("special_offer")
    private Double specialOffer;

    /**
     * 菜品对应的口味
     */
    @TableField("taste_id")
    private String tasteId;

    /**
     * 起卖的数量
     */
    @TableField("min_num")
    private Integer minNum;

    /**
     * 库存数量
     */
    @TableField("inventory")
    private Long inventory;

    /**
     * 菜品的介绍
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 是否外卖展示(0：是、1：否)
     */
    @TableField("take_out_show")
    private Integer takeOutShow;

    /**
     * 是否堂食展示(0：是、1：否)
     */
    @TableField("dine_in_show")
    private Integer dineInShow;

    /**
     * 库存过低预警数量
     */
    @TableField("warning_min")
    private Long warningMin;

    /**
     * 库存过高预警数量
     */
    @TableField("warning_max")
    private Long warningMax;

    @TableField(exist = false)
    private String image;
    @TableField(exist = false)
    private List<CuisineImage> cuisineImages;
}
