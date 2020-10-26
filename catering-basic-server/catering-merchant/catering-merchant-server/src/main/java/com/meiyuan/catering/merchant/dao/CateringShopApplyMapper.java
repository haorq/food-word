package com.meiyuan.catering.merchant.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.entity.CateringShopApplyEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.vo.ShopApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户审核表(CateringShopApplyMapper)表数据库访问层
 *
 */
@Mapper
public interface CateringShopApplyMapper extends BaseMapper<CateringShopApplyEntity>{

    IPage<ShopApplyVO>  listShopApply(@Param("page")Page page, @Param("dto") ShopApplyListDTO dto);

    CateringMerchantEntity selectMerchantByPhone(@Param("phone")String phone);

    CateringShopEntity selectShopByNameAndPhone(@Param("phone")String phone, @Param("shopName")String shopName);

    CateringShopEntity selectShopById(@Param("id")Long id);
}