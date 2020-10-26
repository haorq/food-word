package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author MeiTao
 * @Description 商户自提点
 * @Date  2020/3/11 0011 11:05
 */
@Mapper
public interface CateringMerchantPickupAdressMapper extends BaseMapper<CateringMerchantPickupAdressEntity>{

    /**
     * 自提点列表分页查询
     * @param page
     * @param dto
     * @return
     */
    IPage<CateringMerchantPickupAddressPageVo> listMerchantPickupAddressPage(Page page, @Param("dto") MerchantPickupAdressPageDTO dto);

    /**
     * 根据自提点id查询自提点信息
     * @param id
     * @return
     */
    MerchantsPickupAddressDTO getMerchantPickupAddress(Long id);

    /**
     * 方法描述 : wx下单---获取门店当前可用自提点
     *            去除已禁用自提点；已禁用商户下的自提点
     * @Author: MeiTao
     * @Date: 2020/7/25 0025 13:25
     * @param page
     * @param dto 请求参数
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity>
     * @Since version-1.2.0
     */
    IPage<CateringMerchantPickupAdressEntity> shopPickupAddressPage(@Param("page") Page page, @Param("dto") PickupAdressQueryDTO dto);
}