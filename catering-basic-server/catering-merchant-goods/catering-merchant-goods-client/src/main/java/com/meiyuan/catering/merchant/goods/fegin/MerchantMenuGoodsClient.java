package com.meiyuan.catering.merchant.goods.fegin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsSaveDTO;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantMenuGoodsService;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yy
 * @date 2020/7/8
 */
@Service
public class MerchantMenuGoodsClient {

    @Autowired
    private CateringMerchantMenuGoodsService cateringMerchantMenuGoodsService;

    /**
     * describe: 销售菜单-分页查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:39
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantMenuGoodsVO>> queryPageList(MerchantMenuGoodsQueryDTO dto) {
        return Result.succ(cateringMerchantMenuGoodsService.queryPageList(dto));
    }

    /**
     * describe: 销售菜单-新增
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:39
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<Map<String, Object>> save(MerchantMenuGoodsSaveDTO dto) {
        return Result.succ(cateringMerchantMenuGoodsService.save(dto));
    }


    /**
     * describe: 售菜单-验证菜单名
     * @author: yy
     * @date: 2020/7/24 15:49
     * @param dto
     * @return: {@link Result< Boolean>}
     * @version 1.2.0
     **/
    public Result<Boolean> verificationMenuName(MerchantMenuGoodsQueryDTO dto) {
        return Result.succ(cateringMerchantMenuGoodsService.verificationMenuName(dto));
    }

    /**
     * describe: 销售菜单-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/8 10:48
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO>}
     * @version 1.2.0
     **/
    public Result<MerchantMenuGoodsDetailsVO> queryMenuById(Long merchantId, Long id) {
        return Result.succ(cateringMerchantMenuGoodsService.queryMenuById(merchantId, id));
    }

    /**
     * describe: 查询商户下所有已建立过菜单的门店id
     *
     * @param merchantId
     * @author: yy
     * @date: 2020/7/23 15:11
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    public List<Long> queryMenuShopByMerchantId(Long merchantId) {
        return cateringMerchantMenuGoodsService.queryMenuShopByMerchantId(merchantId);
    }



    /**
     * 方法描述   修改菜单门店名称
     * @author: lhm
     * @date: 2020/8/14 9:32
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    public Boolean updateMenuShopName(Long shopId,String shopName) {
        return cateringMerchantMenuGoodsService.updateMenuShopName(shopId,shopName);
    }


    /**
     * 方法描述   删除菜单门店
     * @author: lhm
     * @date: 2020/8/14 9:33
     * @param shopId
     * @return: {@link }
     * @version 1.3.0
     **/
    public Boolean deleteMenuShopName(Long shopId) {
        return cateringMerchantMenuGoodsService.deleteMenuShopName(shopId);
    }
}
