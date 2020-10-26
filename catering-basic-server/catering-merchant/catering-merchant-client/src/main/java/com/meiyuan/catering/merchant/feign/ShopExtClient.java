package com.meiyuan.catering.merchant.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.service.CateringShopExtService;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankExtInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/9 10:57
 * @since v1.1.0
 */
@Service
public class ShopExtClient {

    @Autowired
    private CateringShopExtService shopExtService;

    public Boolean updateSignContract(String shopId, String contractNo) {
        return shopExtService.updateSignContract(shopId, contractNo);
    }

    /**
    * 根据门店ID查询门店账户信息
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/10/10 16:46
    * @return: {@link List<ShopBankExtInfoVo>}
    * @version V1.5.0
    **/
    public Result<List<ShopBankExtInfoVo>> listByShopId(Long shopId) {
        return Result.succ(shopExtService.listByShopId(shopId));
    }

    /**
    * 查询已完成结算信息录入的商家ID集合
    * @author: GongJunZheng
    * @date: 2020/10/10 19:06
    * @return: {@link List<Long>}
    * @version V1.5.0
    **/
    public Result<List<Long>> listFinalAuditStatus() {
        return Result.succ(shopExtService.listFinalAuditStatus());
    }
}
