package com.meiyuan.catering.merchant.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.entity.CateringShopConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺配置表(CateringShopConfig)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 10:17:02
 */
@Mapper
public interface CateringShopConfigMapper extends BaseMapper<CateringShopConfigEntity>{

    /**
     * 方法描述 : 修改门店配送相关金额
     * @Author: MeiTao
     * @Date: 2020/6/3 0003 13:51
     * @param dto
     * @return: void
     * @Since version-1.1.0
     */
    void modifyDeliveryConfig(@Param("dto")CateringShopConfigEntity dto);
}