package com.meiyuan.catering.admin.service.merchant;

import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.ShopApplyVO;
import com.meiyuan.catering.order.feign.OrdersShopDebtClient;
import com.meiyuan.catering.pay.service.MyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author herui
 * @date 2020-09-29 15:07
 * @version 1.5.0
 * @desc 商户申请入驻
 */
@Service
@Slf4j
public class AdminShopApplyService {

    @Resource
    MerchantClient merchantClient;

    @Resource
    MerchantCategoryClient merchantCategoryClient;

    @Autowired
    MarketingActivityClient marketingActivityClient;

    @Resource
    MerchantUtils merchantUtils;

    @Resource
    EsMerchantClient esMerchantClient;

    @Autowired
    MyMemberService myMemberService;

    @Resource
    RoleClient roleClient;

    @Resource
    OrdersShopDebtClient ordersShopDebtClient;

    /**
     * 获取审核列表
     * @param dto
     * @return
     */
    public Result<PageData<ShopApplyVO>> listShopApply(ShopApplyListDTO dto){
        return merchantClient.listShopApply(dto);
    }

    /**
     * 提交审核结果
     * @param shopApplyDTO
     * @return
     */
    public Result<ShopApplyDTO> ShopApplyConfirm(ShopApplyDTO shopApplyDTO){
        Result<ShopApplyDTO> result =  merchantClient.ShopApplyConfirm(shopApplyDTO);
        if(result.getData() != null){
            //这里同平台新增品牌后的代码
            insertMerchantCategory(result.getData().getMerchantId());
            //这里同商户PC新增门店后的代码
            insertShopCategory(result.getData().getShopId());
        }
        return result;
    }

    /**
     * 查看申请详情
     * @param id
     * @return
     */
    public Result<ShopApplyVO> shopApplyDetail(String id){
        return merchantClient.shopApplyDetail(id);
    }

    private void insertMerchantCategory(Long merchantId){
        if(merchantId != null){
            MerchantCategorySaveDTO merchantCategory = new MerchantCategorySaveDTO();
            merchantCategory.setCategoryName("我的分类");
            merchantCategory.setMerchantId(merchantId);
            merchantCategory.setDefaulCategory(CategoryTypeEnum.DEFAULT.getStatus());
            merchantCategoryClient.save(merchantCategory,true);

            //处理品牌活动中心是否有平台活动标识 品牌属性:1:自营，2：非自营
            marketingActivityClient.selectPlatFormActivity(merchantId,2);
        }
    }

    //TODO 何锐 这里的代码要和pc那里新增店铺保持一致 com.meiyuan.catering.merchant.pc.service.merchant.MerchantPcMerchantService#insertShop
    private void insertShopCategory(Long shopId){
        //添加自提点则不同步es
//        if (ObjectUtils.isEmpty(result.getData())){
//            return result;
//        }
        //通联创建个人会员
        myMemberService.createPersonalMember(shopId);
        //店铺添加默认角色
        roleClient.insertDefaultRole(shopId);
        //添加店铺默认商品分类
        MerchantCategorySaveDTO merchantCategory = new MerchantCategorySaveDTO();
        merchantCategory.setCategoryName("默认分类");
        merchantCategory.setMerchantId(shopId);
        merchantCategory.setDefaulCategory(CategoryTypeEnum.DEFAULT.getStatus());
        merchantCategoryClient.save(merchantCategory,Boolean.FALSE);
        //创建门店负债信息
        ordersShopDebtClient.create(shopId);
        ShopInfoDTO shop = merchantUtils.getShop(shopId);

        if (!ObjectUtils.isEmpty(shop)){
            //新增店铺信息同步es
            EsMerchantDTO esMerchantDTO = new EsMerchantDTO();
            esMerchantDTO.setId(shop.getId().toString());
            esMerchantDTO.setMerchantId(shop.getMerchantId().toString());
            esMerchantDTO.setShopId(shop.getId().toString());
            esMerchantDTO.setMerchantName(shop.getShopName());

            esMerchantDTO.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
            esMerchantDTO.setProvinceCode(shop.getAddressProvinceCode());
            esMerchantDTO.setEsCityCode(shop.getAddressCityCode());
            esMerchantDTO.setAreaCode(shop.getAddressAreaCode());

            esMerchantDTO.setShopGrade(0.0D);
            esMerchantDTO.setOrdersNum(0);

            //店铺是否可在小程序展示
            esMerchantDTO.setShopService(shop.getShopServiceType());
            esMerchantDTO.setShopStatus(shop.getShopStatus());

            MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shop.getMerchantId());
            esMerchantDTO.setAuditStatus(merchantInfo.getAuditStatus());
            esMerchantDTO.setMerchantStatus(merchantInfo.getMerchantStatus());

            esMerchantClient.saveUpdate(esMerchantDTO);
        }else {
            log.info("【商户入驻申请】es添加商户："+ shop.getShopName() +",es信息同步失败");
        }

    }

}
