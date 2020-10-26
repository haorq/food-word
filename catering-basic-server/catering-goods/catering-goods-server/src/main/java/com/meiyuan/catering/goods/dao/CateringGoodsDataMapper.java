package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.entity.CateringGoodsDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品综合数据表(CateringGoodsData)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:21
 */
@Mapper
public interface CateringGoodsDataMapper extends BaseMapper<CateringGoodsDataEntity>{

   /**
    * 批量获取根据商品id集合
    *
    * @author: wxf
    * @date: 2020/6/22 18:12
    * @param goodsIdList 商品id集合
    * @return: {@link List< CateringGoodsDataEntity>}
    * @version 1.1.0
    **/
   List<CateringGoodsDataEntity> list(@Param("list")List<Long> goodsIdList);
}