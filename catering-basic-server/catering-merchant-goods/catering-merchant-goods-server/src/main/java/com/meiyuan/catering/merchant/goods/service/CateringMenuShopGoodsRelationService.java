package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuShopGoodsRelationQueryDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuShopGoodsRelationEntity;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMenuShopGoodsRelationService extends IService<CateringMenuShopGoodsRelationEntity> {

    /**
     * describe: 查询商户菜单关联店铺集合
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 16:42
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MenuShopGoodsRelationVO>}
     * @version 1.2.0
     **/
    PageData<CateringMenuShopGoodsRelationEntity> queryPageList(MenuShopGoodsRelationQueryDTO dto);

    /**
     * 方法描述: 获取菜单关联的门店ids<br>
     *
     * @param menuId
     * @author: gz
     * @date: 2020/7/14 19:04
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    List<Long> listMenuShop(Long menuId);
}
