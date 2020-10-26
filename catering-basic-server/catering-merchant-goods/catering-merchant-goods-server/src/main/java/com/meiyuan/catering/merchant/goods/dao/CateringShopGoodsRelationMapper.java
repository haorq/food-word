package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.category.CategoryRelationDTO;
import com.meiyuan.catering.goods.dto.category.CategoryRelationGoodsQueryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsCategoryRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:09
 */
@Mapper
public interface CateringShopGoodsRelationMapper extends BaseMapper<CateringShopGoodsRelationEntity>{


}