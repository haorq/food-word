package com.meiyuan.catering.admin.service.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.dto.menu.PushMenuDTO;
import com.meiyuan.catering.goods.dto.merchant.MerchantDTO;
import com.meiyuan.catering.goods.feign.GoodsMenuClient;
import com.meiyuan.catering.goods.feign.MerchantMenuGoodsRelationClient;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/21 15:22
 * @description 简单描述
 **/
@Service
public class AdminGoodsMenuService {
    @Resource
    GoodsMenuClient menuClient;
    @Resource
    MerchantMenuGoodsRelationClient merchantMenuGoodsRelationClient;
    @Resource
    ShopClient shopClient;

    /**
     * 新增修改
     * 对时间有相关的验证
     * 会同步ES
     * @author: wxf
     * @date: 2020/3/21 15:15
     * @param dto 新增修改数据DTO
     * @return: {@link String}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(GoodsMenuDTO dto) {
        return menuClient.saveUpdate(dto);
    }

    /**
     * 列表分页
     *
     * @author: wxf
     * @date: 2020/3/21 16:38
     * @param dto 分页参数
     * @return: {@link PageData< GoodsMenuDTO>}
     * @version 1.0.1
     **/
    public Result<PageData<GoodsMenuDTO>> listLimit(GoodsMenuLimitQueryDTO dto) {
        return menuClient.listLimit(dto);
    }

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/3/21 16:48
     * @param menuId 菜单id
     * @return: {@link String}
     * @version 1.0.1
     **/
    public Result<String> del(Long menuId) {
        return menuClient.del(menuId);
    }

    /**
     * 菜单详情
     *
     * @param menuId 菜单id
     * @author: wxf
     * @date: 2020/3/21 16:58
     * @return: {@link GoodsMenuDTO}
     **/
    public Result<GoodsMenuDTO> menuInfoById(Long menuId) {
        Result<GoodsMenuDTO> dtoResult = menuClient.menuInfoById(menuId);
        GoodsMenuDTO dto = null;
        if (dtoResult.success() && null != dtoResult.getData()) {
            dto = dtoResult.getData();
        }
        if (null != dto) {
            Result<List<Long>> listResult = merchantMenuGoodsRelationClient.listByGoodsIdOrMenuId(null, menuId);
            if (listResult.success() && BaseUtil.judgeList(listResult.getData())) {
                List<Long> merchantIdList = listResult.getData();
                List<MerchantDTO> merchantDtoList = Collections.emptyList();
                List<ShopDTO> shopList = Collections.emptyList();
                dto.setMerchantList(merchantDtoList);
                Result<List<ShopDTO>> shopListResult =
                        shopClient.listByMerchantIdList(merchantIdList);
                if (shopListResult.success() && BaseUtil.judgeList(shopListResult.getData())) {
                    shopList = shopListResult.getData();
                    merchantDtoList = shopList.stream().map(
                            i -> {
                                MerchantDTO merchant = new MerchantDTO();
                                merchant.setMerchantId(i.getId());
                                merchant.setShopName(i.getShopName());
                                merchant.setShopCode(i.getShopCode());
                                merchant.setAddressFull(i.getAddressFull());
                                merchant.setPrimaryPersonName(i.getPrimaryPersonName());
                                merchant.setRegisterPhone(i.getRegisterPhone());
                                return merchant;
                            }
                    ).collect(Collectors.toList());
                    dto.setMerchantList(merchantDtoList);
                }
            }
        }
        return Result.succ(dto);
    }

    /**
     * 推送菜单
     * 相关推送验证
     * 同步es
     *
     * @author: wxf
     * @date: 2020/3/21 17:20
     * @param dto 推送参数
     * @return: {@link String}
     **/
    public Result<String> pushMerchant(PushMenuDTO dto) {
        return menuClient.pushMerchant(dto);
    }

    /**
     * 验证菜单编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 菜单编码
     * @return: {@link boolean}
     **/
    public Result<Boolean> validationCode(String code) {
        return menuClient.validationCode(code);
    }
}
