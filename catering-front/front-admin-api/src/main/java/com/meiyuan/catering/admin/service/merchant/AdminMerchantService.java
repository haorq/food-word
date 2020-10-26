package com.meiyuan.catering.admin.service.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsStatusUpdateDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.enums.marketing.MarketingMerchantShopTypeEnum;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantDetailDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryPage;
import com.meiyuan.catering.merchant.dto.merchant.MerchantVerifyDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.vo.merchant.MerchantPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 后台商户管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class AdminMerchantService {
    @Resource
    MerchantClient merchantClient;
    
    @Autowired
    MarketingActivityClient marketingActivityClient;

    @Resource
    EsMerchantClient esMerchantClient;

    @Resource
    EsMarketingClient esMarketingClient;

    @Resource
    MerchantCategoryClient merchantCategoryClient;

    /**
     * 方法描述 : 管理平台商户列表分页查询
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    public Result<IPage<MerchantPageVo>> merchantPage(MerchantQueryPage dto) {
        return merchantClient.merchantPage(dto);
    }

    /**
     * 方法描述 : admin-添加修改商户
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> saveOrUpdateMerchant(MerchantDTO dto) {
        Result<Long> result = merchantClient.saveOrUpdateMerchant(dto);

        if(ObjectUtils.isEmpty(dto.getId())){
            MerchantCategorySaveDTO merchantCategory = new MerchantCategorySaveDTO();
            merchantCategory.setCategoryName("我的分类");
            merchantCategory.setMerchantId(result.getData());
            merchantCategory.setDefaulCategory(CategoryTypeEnum.DEFAULT.getStatus());
            merchantCategoryClient.save(merchantCategory,true);

            //处理品牌活动中心是否有平台活动标识
            marketingActivityClient.selectPlatFormActivity(result.getData(),dto.getMerchantAttribute());
        }
        return result;
    }

    /**
     * 方法描述 : 商户详情获取
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 商户Id
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    public Result<MerchantDetailDTO> queryMerchantDetail(MerchantQueryPage dto) {
        return merchantClient.queryMerchantDetail(dto);
    }

    /**
     * 方法描述 : 商户状态修改
     *          1、redis信息同步
     *          2、es信息同步
     * @Author: MeiTao
     * @Date: 2020/7/5 0005 11:18
     * @param dto 请求参数
     * @return: Result 商户下店铺id【未删除店铺】
     * @Since version-1.1.0
     */
    public Result<List<Long>> updateMerchantStatus(MerchantDTO dto) {
        Result<List<Long>> listResult = merchantClient.updateMerchantStatus(dto);

        List<Long> shopIds = listResult.getData();
        //更新商户下店铺信息
        if (BaseUtil.judgeList(shopIds)){
            List<EsMerchantDTO> esMerchants = new ArrayList<>();
            shopIds.forEach(shopId->{
                EsMerchantDTO esMerchant = esMerchantClient.getByMerchantId(shopId.toString()).getData();
                if (!ObjectUtils.isEmpty(esMerchant)){
                    esMerchant.setMerchantStatus(dto.getMerchantStatus());
                    esMerchants.add(esMerchant);
                }
            });

            if (BaseUtil.judgeList(esMerchants)){
                esMerchantClient.saveUpdateBatch(esMerchants);
            }
            EsStatusUpdateDTO statusUpdateDto = new EsStatusUpdateDTO();
            statusUpdateDto.setId(dto.getId());
            statusUpdateDto.setStatus(dto.getMerchantStatus());
            statusUpdateDto.setType(MarketingMerchantShopTypeEnum.MERCHANT.getStatus());
            esMarketingClient.merchantShopUpdateSync(statusUpdateDto);
        }

        return listResult;
    }

    /**
     * 方法描述 : admin-商户信息验重
     * @Author: MeiTao
     * @Date: 2020/7/10 0010 9:32
     * @param dto 商户名，电话号码
     * @return: Result<java.lang.String>
     * @Since version-1.2.0
     */
    public Result<Boolean> verifyMerchantInfoUnique(MerchantVerifyDTO dto) {
        return merchantClient.verifyMerchantInfoUnique(dto);
    }

    /**
     * 方法描述 : 商户登录账号老数据处理【1.3.0】
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 10:36
     * @return: Result<java.lang.Boolean>
     * @Since version-1.3.0
     */
    public Result<Boolean> handelMerchantData() {
        return merchantClient.handelMerchantData();
    }
}
