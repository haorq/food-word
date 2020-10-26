package com.meiyuan.catering.merchant.dao;

import com.meiyuan.catering.merchant.entity.CateringShopExtEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther MeiTao
 * @create ${cfg.dateTime}
 * @describe 店铺扩展表mapper类
 */
@Mapper
public interface CateringShopExtMapper extends BaseMapper<CateringShopExtEntity> {


    CateringShopExtEntity selectByShopId(@Param("shopId") Long shopId);
}
