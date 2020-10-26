package com.meiyuan.catering.merchant.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity;
import com.meiyuan.catering.merchant.service.CateringMerchantPickupAdressService;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author MeiTao
 * @Description 商户自提点表服务实现类
 * @Date  2020/3/11 0011 11:12
 */
@Service
public class MerchantPickupAdressClient{
    @Autowired
    CateringMerchantPickupAdressService merchantPickupAdressService;


    /**
     * 方法描述 : 自提点列表分页查询
     * @Author: MeiTao
     * @Date: 2020/5/19  9:35
     * @param dto
     * @return: CateringMerchantPickupAddressPageVo
     * @Since version-1.0.0
     */
    public  Result<IPage<CateringMerchantPickupAddressPageVo>> listMerchantPickupAddressPage(MerchantPickupAdressPageDTO dto){
       return merchantPickupAdressService.listMerchantPickupAddressPage(dto);
    }


    /**
     * 方法描述 : 自提点查询
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 9:37
     * @param id
     * @return: MerchantsPickupAddressDTO
     * @Since version-1.0
     */
    public Result<MerchantsPickupAddressDTO> getMerchantPickupAddress(Long id){
        return merchantPickupAdressService.getMerchantPickupAddress(id);
    }

    public Result<IPage> page(Page page, QueryWrapper<CateringMerchantPickupAdressEntity> queryWrapper) {
        return Result.succ(merchantPickupAdressService.page(page,queryWrapper));
    }



    public Result<IPage<PickupAddressListVo>> pickupAddressList(PickupAdressQueryDTO dto) {
        return merchantPickupAdressService.pickupAddressList(dto);
    }
}
