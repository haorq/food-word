package com.meiyuan.catering.merchant.service.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.enums.GoodsAddTypeEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategorySaveVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/18 16:52
 */
@Service
public class MerchantCategoryService {

    @Autowired
    private MerchantCategoryClient merchantCategoryClient;
    @Autowired
    private MerchantUtils merchantUtils;


    /**
     * 方法描述   商户app--商品分类新增 修改
     * @author: lhm
     * @date: 2020/8/19 14:56
     * @param token
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<String> save(MerchantTokenDTO token, MerchantCategorySaveDTO dto) {
        dto.setMerchantId(token.getShopId());
        return merchantCategoryClient.save(dto,false);
    }

    /**
     * 方法描述  商户app--列表查询
     * @author: lhm
     * @date: 2020/8/19 14:55
     * @param token
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<PageData<MerchantCategoryDownVO>> queryPageListForApp(MerchantTokenDTO token, MerchantCategoryQueryDTO dto) {
        dto.setMerchantId(token.getShopId());
        return merchantCategoryClient.queryPageListForApp(dto);
    }


    /**
     * 方法描述   商品分类-下拉查询所有
     * @author: lhm
     * @date: 2020/8/19 15:05
     * @param token
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<List<MerchantCategoryDownVO>> queryAll(MerchantTokenDTO token) {
        MerchantCategoryQueryDTO dto =new MerchantCategoryQueryDTO();
        dto.setMerchantId(token.getShopId());
        List<MerchantCategoryDownVO> list = merchantCategoryClient.queryPageListForApp(dto).getData().getList();
        return Result.succ(list);
    }



   /**
    * 方法描述   商品分类-查询详情
    * @author: lhm
    * @date: 2020/8/19 15:09
    * @param token
    * @param id
    * @return: {@link }
    * @version 1.3.0
    **/
    public Result<MerchantCategorySaveVO> queryCategoryById(MerchantTokenDTO token, Long id) {
        Long shopId = token.getShopId();
        return merchantCategoryClient.queryCategoryById(shopId, id);
    }


    /**
     * 方法描述   商品分类-删除数据
     * @author: lhm
     * @date: 2020/8/19 15:09
     * @param token
     * @param id
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> delete(MerchantTokenDTO token, Long id) {
        Long shopId = token.getShopId();
        return merchantCategoryClient.deleteForApp(shopId, id);
    }



    /**
     * 方法描述   商品分类排序
     * @author: lhm
     * @date: 2020/8/21 15:20
     * @param token
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateCategorySort(MerchantTokenDTO token, MerchantCategoryOrGoodsSortDTO dto) {
        dto.setShopId(token.getShopId());
        return merchantCategoryClient.updateCategorySort(dto);


    }
}
