package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.merchant.goods.dto.goods.MerchantMenuDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsSaveDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantMenuGoodsEntity;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;

import java.util.List;
import java.util.Map;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMerchantMenuGoodsService extends IService<CateringMerchantMenuGoodsEntity> {

    /**
     * describe: 销售菜单-分页查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:50
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO>}
     * @version 1.2.0
     **/
    PageData<MerchantMenuGoodsVO> queryPageList(MerchantMenuGoodsQueryDTO dto);

    /**
     * describe: 销售菜单-新增
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:49
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    Map<String, Object> save(MerchantMenuGoodsSaveDTO dto);

    /**
     * describe: 销售菜单-验证菜单名
     * @author: yy
     * @date: 2020/7/24 15:49
     * @param dto
     * @return: {@link Boolean}
     * @version 1.2.0
     **/
    Boolean verificationMenuName(MerchantMenuGoodsQueryDTO dto);

    /**
     * describe: 销售菜单-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/8 10:49
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO}
     * @version 1.2.0
     **/
    MerchantMenuGoodsDetailsVO queryMenuById(Long merchantId, Long id);


    MerchantMenuDTO listByMenuId(Long menuId);

    /**
     * describe: 查询商户下所有已建立过菜单的门店id
     * @author: yy
     * @date: 2020/7/23 15:12
     * @param merchantId
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    List<Long> queryMenuShopByMerchantId(Long merchantId);



    Boolean deleteByGoodsIdAndMerchantId(Long merchantId, Long goodsId);

    Boolean updateMenuShopName(Long shopId,String shopName);

    Boolean deleteMenuShopName(Long shopId);
}
