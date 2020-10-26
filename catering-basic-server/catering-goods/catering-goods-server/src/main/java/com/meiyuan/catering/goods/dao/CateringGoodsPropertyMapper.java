package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.property.GoodsPropertyDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsPropertyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品属性表(CateringGoodsProperty)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:21
 */
@Mapper
public interface CateringGoodsPropertyMapper extends BaseMapper<CateringGoodsPropertyEntity>{
    /**
     * 获取集合根据商品id
     *
     * @author: wxf
     * @date: 2020/3/21 14:15
     * @param goodsId 商品id
     * @return: {@link List< CateringGoodsPropertyEntity>}
     **/
    List<CateringGoodsPropertyEntity> listByGoodsId(Long goodsId);

}