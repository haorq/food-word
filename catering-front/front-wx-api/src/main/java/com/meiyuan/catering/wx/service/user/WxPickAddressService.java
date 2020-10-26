package com.meiyuan.catering.wx.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.feign.MerchantPickupAdressClient;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author lhm
 * @date 2020/3/25 18:47
 **/
@Service
@Slf4j
public class WxPickAddressService {


    @Resource
    private MerchantPickupAdressClient merchantPickupAdressClient;


    /**
     方法描述 : wx下单---获取门店当前可用自提点
     *            去除已禁用自提点；已禁用商户下的自提点
     * @param dto
     * @return
     */
    public Result<IPage<PickupAddressListVo>> pickupAddressList(PickupAdressQueryDTO dto) {
        return merchantPickupAdressClient.pickupAddressList(dto);
    }

}
