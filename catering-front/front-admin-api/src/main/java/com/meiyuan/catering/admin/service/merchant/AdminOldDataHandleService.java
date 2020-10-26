package com.meiyuan.catering.admin.service.merchant;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 后台商户管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class AdminOldDataHandleService {
    @Resource
    MerchantClient merchantClient;
    @Resource
    ShopClient shopClient;

    public Result handleShopDataV5() {
        Result result = shopClient.handleShopDataV5();
        merchantClient.putAllShopCache();
        return result;
    }

    public void handleAppTokenV5() {
        shopClient.handleAppTokenV5();
    }
}
