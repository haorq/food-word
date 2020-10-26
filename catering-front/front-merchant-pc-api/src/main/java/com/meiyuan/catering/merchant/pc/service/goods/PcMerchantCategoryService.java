package com.meiyuan.catering.merchant.pc.service.goods;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategorySaveVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description：
 * @author yy
 * @date 2020/7/7
 */
@Service
public class PcMerchantCategoryService {

    @Autowired
    private MerchantCategoryClient merchantCategoryClient;
    @Autowired
    private MerchantUtils merchantUtils;


    /**
     * describe: 商品分类-新增/修改
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 10:31
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<String> save(MerchantAccountDTO token, MerchantCategorySaveDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        return merchantCategoryClient.save(dto, true);
    }

    /**
     * describe:  商品分类-列表分页查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/7 10:40
     * @return: {com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>>}
     * @version 1.2.0
     **/
    public Result<PageData<MerchantCategoryVO>> queryPageList(MerchantAccountDTO token, MerchantCategoryQueryDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        return merchantCategoryClient.queryPageList(dto);
    }

    /**
     * describe: 商品分类-查询所有
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 9:31
     * @return: {com.meiyuan.catering.core.util.Result<java.util.List<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>>}
     * @version 1.2.0
     **/
    public Result<List<MerchantCategoryDownVO>> queryAll(MerchantAccountDTO token, MerchantCategoryQueryDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        return merchantCategoryClient.queryAll(dto);
    }

    /**
    * 商品分类-查询所有 根据门店ID进行查询V1.3.0
    * @param token 登录信息
    * @param dto 查询条件
    * @author: GongJunZheng
    * @date: 2020/8/11 11:55
    * @return: {@link Result<List<MerchantCategoryDownVO>>}
    * @version V1.3.0
    **/
    public Result<List<MerchantCategoryDownVO>> queryAllByShopId(MerchantAccountDTO token, MerchantCategoryQueryDTO dto) {
        dto.setMerchantId(token.getAccountTypeId());
        dto.setAccountType(token.getAccountType());
        return merchantCategoryClient.queryAllByShopId(dto);
    }

    /**
     * describe: 商品分类-查询详情
     * @author: yy
     * @date: 2020/8/3 10:52
     * @param token
     * @param id
     * @return: {@link Result< MerchantCategorySaveVO>}
     * @version 1.2.0
     **/
    public Result<MerchantCategorySaveVO> queryCategoryById(MerchantAccountDTO token, Long id) {
        Long merchantId = token.getAccountTypeId();
        return merchantCategoryClient.queryCategoryById(merchantId, id);
    }

    /**
     * describe: 商品分类-删除数据
     *
     * @param token
     * @param id
     * @author: yy
     * @date: 2020/7/10 17:32
     * @return: {com.meiyuan.catering.core.util.Result<java.lang.String>}
     * @version 1.2.0
     **/
    public Result<Boolean> delete(MerchantAccountDTO token, Long id) {
        Long merchantId = token.getAccountTypeId();
        return merchantCategoryClient.delete(merchantId, id);
    }

}
