package com.meiyuan.catering.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;
import com.meiyuan.catering.merchant.entity.CateringShopApplyEntity;
import com.meiyuan.catering.merchant.vo.ShopApplyVO;

public interface CateringShopApplyService extends IService<CateringShopApplyEntity> {

    /**
     * 小程序-添加入驻商家申请
     * @param dto
     * @return
     */
    Result insertShopApply(ShopApplyDTO dto);

    /**
     * 平台-入驻商家列表
     * @param shopApplyListDTO
     * @return
     */
    PageData<ShopApplyVO> listShopApply(ShopApplyListDTO shopApplyListDTO);

    /**
     * 提交审核结果
     * @param dto
     * @return 新增的品牌ID，如果品牌存在，则返回为null
     */
    Result<ShopApplyDTO> ShopApplyConfirm(ShopApplyDTO dto);

    Result<ShopApplyVO> shopApplyDetail(String id);
}
