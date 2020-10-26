package com.meiyuan.catering.merchant.service.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.dto.menu.ShopMenuDTO;
import com.meiyuan.catering.goods.feign.GoodsMenuClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yaoozu
 * @description 菜单服务
 * @date 2020/3/2211:13
 * @since v1.0.0
 */
@Service
public class MerchantGoodsMenuService {
    @Resource
    GoodsMenuClient menuClient;

    /**
     * 列表分页
     *
     * @param dto 分页参数
     * @author: wxf
     * @date: 2020/3/21 16:38
     * @return: {@link PageData < GoodsMenuDTO>}
     **/
    public Result<PageData<GoodsMenuDTO>> listLimit(@NotNull GoodsMenuLimitQueryDTO dto, Long merchantId) {
        dto.setMerchantId(merchantId);
        return menuClient.listForMerchant(dto);
    }

    /**
     * 菜单详情
     *
     * @param menuId 菜单id
     * @author: wxf
     * @date: 2020/3/21 16:58
     * @return: {@link GoodsMenuDTO}
     **/
    public Result<GoodsMenuDTO> menuInfoById(Long menuId) {
        return menuClient.menuInfoById(menuId);
    }

    /**
     * 分类
     * @param merchantId
     * @return
     */
    public Result<List<ShopMenuDTO>> listShopMenu(Long merchantId,Integer type) {
        GoodsMenuLimitQueryDTO dto = new GoodsMenuLimitQueryDTO();
        dto.setMerchantId(merchantId);
        dto.setType(type);
        return menuClient.listShopMenu(dto);
    }
}
