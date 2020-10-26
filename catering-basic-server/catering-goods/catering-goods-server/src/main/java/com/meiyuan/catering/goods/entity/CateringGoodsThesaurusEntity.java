package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品词库(CateringGoodsThesaurus)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:38:10
 */
@Data
@TableName("catering_goods_thesaurus")
public class CateringGoodsThesaurusEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 720164580609085341L;
    /**
     * 关键字
     */
    @TableField(value = "key_world")
    private String keyWorld;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Long sort;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
}