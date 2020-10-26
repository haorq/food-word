package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.merchant.goods.dto.merchant.MenuGoodsRelationDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMenuGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMenuGoodsRelationService extends IService<CateringMenuGoodsRelationEntity> {

    /**
     * describe: 分页查询此商户菜单关联的商品id
     * @author: yy
     * @date: 2020/7/14 10:32
     * @param dto
     * @return: {java.util.Set<java.lang.String>}
     * @version 1.2.0
     **/
    Set<String> queryList(MenuGoodsRelationDTO dto);
}
