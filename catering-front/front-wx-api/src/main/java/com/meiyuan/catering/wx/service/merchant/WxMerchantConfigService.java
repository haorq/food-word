package com.meiyuan.catering.wx.service.merchant;

import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopCityDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopCityQueryDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yaoozu
 * @description 商户服务
 * @date 2020/3/2414:08
 * @since v1.0.0
 */
@Service
@Slf4j
public class WxMerchantConfigService {

    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private ShopClient shopClient;

    public Result<ShopConfigInfoDTO> merchantTimeRange(Long merchantId) {
        return Result.succ(merchantUtils.getShopConfigInfo(merchantId));
    }

    public Result<Map<String, List<ShopCityDTO>>> listShopCity(ShopCityQueryDTO dto) {
        return shopClient.listShopCity(dto);
    }
}
