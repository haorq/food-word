package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_merchant_menu_goods")
public class CateringMerchantMenuGoodsEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 378706866377634193L;
    /**
     *  商户id
     */
    @TableField("merchant_id")
    private Long merchantId;
    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;
    /**
     * 0-否 1-是
     */
    @TableField("is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

}
