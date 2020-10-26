package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;

/**
 * @Author MeiTao
 * @Description  店铺自提点关联关系表
 * @Date  2020/3/11 0011 11:12
 */
public interface CateringMerchantPickupAdressService extends IService<CateringMerchantPickupAdressEntity> {

    /**
     * 方法描述 : 自提点列表分页查询
     * @Author: MeiTao
     * @Date: 2020/5/19  9:35
     * @param dto
     * @return: CateringMerchantPickupAddressPageVo
     * @Since version-1.0
     */
    Result<IPage<CateringMerchantPickupAddressPageVo>> listMerchantPickupAddressPage(MerchantPickupAdressPageDTO dto);


    /**
     * 方法描述 : 自提点查询
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 9:37
     * @param id
     * @return: MerchantsPickupAddressDTO
     * @Since version-1.0
     */
    Result<MerchantsPickupAddressDTO> getMerchantPickupAddress(Long id);

    /**
     * 方法描述 : 方法描述 : wx下单---获取门店当前可用自提点
     *            去除已禁用自提点；已禁用商户下的自提点
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:01
     * @param dto
     * @return: IPage<PickupAddressListVo>
     * @Since version-1.1.0
     */
    Result<IPage<PickupAddressListVo>> pickupAddressList(PickupAdressQueryDTO dto);

    /**
     * 方法描述 : 店铺状态修改，关联关系表信息同步
     * @Author: MeiTao
     * @Date: 2020/7/16 0016 16:11
     * @param shopDto 请求参数
     * @return: java.lang.Boolean
     * @Since version-1.2.0
     */
    Boolean updateShopStatus(ShopDTO shopDto);
}
