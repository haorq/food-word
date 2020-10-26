package com.meiyuan.catering.admin.service.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.feign.MerchantPickupAdressClient;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 自提点地址管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
public class AdminCateringMerchantPickupAddressService {
    @Resource
    private MerchantPickupAdressClient merchantPickupAdressClient;

    /**
     * @Author MeiTao
     * @Description 自提点列表分页查询
     * @Date  2020/3/12 0012 15:38
     */
    public Result<IPage<CateringMerchantPickupAddressPageVo>> listMerchantPickupAddressPage(MerchantPickupAdressPageDTO dto) {
        return merchantPickupAdressClient.listMerchantPickupAddressPage(dto);
    }


    /**
     * @Author MeiTao
     * @Description 自提点查询
     * @Date  2020/3/12 0012 15:38
     */
    public Result<MerchantsPickupAddressDTO> getMerchantPickupAddress(Long id) {
        return merchantPickupAdressClient.getMerchantPickupAddress(id);
    }
}
