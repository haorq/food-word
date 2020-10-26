package com.meiyuan.catering.merchant.goods.fegin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuShopGoodsRelationQueryDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMenuShopGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yy
 * @date 2020/7/8
 */
@Service
public class MenuShopGoodsRelationClient {

    @Autowired
    private CateringMenuShopGoodsRelationService cateringMenuShopGoodsRelationService;

    /**
     * describe: 查询商户菜单关联店铺集合
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 16:46
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity>>}
     * @version 1.2.0
     **/
    public Result<PageData<CateringMenuShopGoodsRelationEntity>> queryPageList(MenuShopGoodsRelationQueryDTO dto) {
        return Result.succ(cateringMenuShopGoodsRelationService.queryPageList(dto));
    }

    /**
     * 方法描述: 获取菜单关联的门店ids<br>
     *
     * @param menuId
     * @author: gz
     * @date: 2020/7/14 19:04
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    public List<Long> listMenuShop(Long menuId) {
        return cateringMenuShopGoodsRelationService.listMenuShop(menuId);
    }
}
