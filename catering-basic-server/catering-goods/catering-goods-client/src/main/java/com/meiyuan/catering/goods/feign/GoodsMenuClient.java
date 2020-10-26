package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.menu.*;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeDTO;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeQueryDTO;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.service.CateringGoodsMenuService;
import com.meiyuan.catering.goods.service.CateringMerchantMenuGoodsRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author yaoozu
 * @description 商品菜单
 * @date 2020/5/1811:17
 * @since v1.0.2
 */
@Service
public class GoodsMenuClient {
    @Resource
    private CateringGoodsMenuService menuService;
    @Resource
    CateringMerchantMenuGoodsRelationService menuGoodsRelationService;

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
        return Result.succ(menuService.saveUpdate(dto));
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
        return Result.succ(menuService.listLimit(dto));
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
        return Result.succ(menuService.del(menuId));
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
        return Result.succ(menuService.pushMerchant(dto));
    }

    /**
     * @description 商户菜单列表查询
     * @author yaozou
     * @date 2020/3/22 12:14
     * @param dto 查询条件
     * @since v1.0.0
     * @return {@link PageData< GoodsMenuDTO>}
     */
    public Result<PageData<GoodsMenuDTO>> listForMerchant(GoodsMenuLimitQueryDTO dto) {
        return Result.succ(menuService.listForMerchant(dto, dto.getMerchantId()));
    }

    /**
     * @description 微信商户主页--菜单列表
     * @author yaozou
     * @date 2020/5/18 11:33
     * @param queryDTO 查询参数
     * @since v1.0.2
     * @return Result<List<GoodsMenuDTO>> 结果
     */
    public Result<List<GoodsMenuDTO>> listForWxMerchantIndex( GoodsMenuLimitQueryDTO queryDTO){
        return Result.succ(menuService.listForWxMerchantIndex(queryDTO,queryDTO.getMerchantId()));
    }

    /**
     * @description 小程序菜单列表 按照merchantIds的顺序排序
     * @author yaozou
     * @date 2020/5/18 11:33
     * @since v1.0.2
     * @return {@link PageData<GoodsMenuDTO>}
     */
    public Result<PageData<GoodsMenuDTO>> listForWechat(GoodsMenuLimitQueryDTO dto){
        return Result.succ(menuService.listForWechat(dto));
    }

    /**
     * 微信获取菜单对应的送达时间
     * 1、菜单模式 返回对应送达时间
     * 2、菜品模式 返回明后两天的时间
     *
     * @author: yaozou
     * @date: 2020/5/18 11:33
     * @param dto 查询参数
     * @since v1.0.2
     * @return: {@link WxMenuServiceTimeDTO}
     **/
    public Result<WxMenuServiceTimeDTO> wxMenuService(WxMenuServiceTimeQueryDTO dto){
        return Result.succ(menuService.wxMenuService(dto));
    }

   /**
     * 菜单详情
     *
     * @author: yaozou
     * @date: 2020/5/18 11:33
     * @param menuId 菜单id
    *  @since v1.0.2
     * @return: {@link GoodsMenuDTO}
     **/
   public Result<GoodsMenuDTO> menuInfoById(Long menuId){
       return Result.succ(menuService.menuInfoById(menuId));
   }

    /**
     * 菜单定时上下架
     * 同步ES
     * @author: wxf
     * @date: 2020/5/19 16:24
     * @return: {@link Result< List< Long>>}
     * @version 1.0.1
     **/
    public Result<List<Long>> timingUpDown() {
        return Result.succ(menuService.timingUpDown());
    }

    /**
     * 验证菜单编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 菜单编码
     * @return: {@link boolean}
     * @version 1.0.1
     **/
    public Result<Boolean> validationCode(String code) {
        return Result.succ(menuService.validationCode(code));
    }

    /**
     * @description  菜单是否可卖
     * @author yaozou
     * @date 2020/4/27 15:26
     * @param menuId 菜单ID
     * @since v1.0.0
     * @return  boolean true--可购买 false--不可购买
     */
    public Result<Boolean> isMenuSelling(long menuId){
        GoodsMenuDTO menuDTO = menuInfoById(menuId).getData();
        if (menuDTO.getMenuStatus().equals(GoodsStatusEnum.UPPER_SHELF.getStatus())){
            return Result.succ(true);
        }
        return Result.succ(false);
    }


    /**
     * 分类
     * @param dto 查询条件
     * @return
     */
    public Result<List<ShopMenuDTO>> listShopMenu(GoodsMenuLimitQueryDTO dto) {
        List<ShopMenuDTO> dtoList = Collections.emptyList();
        List<ShopMenuDTO> shopMenuList = menuGoodsRelationService.listCategoryList(dto.getMerchantId(), dto.getType());
        if (BaseUtil.judgeList(shopMenuList)) {
            dtoList = shopMenuList;
        }
        return Result.succ(dtoList);
    }

    /**
     * 微信端获取根据菜单id
     *
     * @author: wxf
     * @date: 2020/5/22 18:09
     * @param menuId 菜单id
     * @return: {@link GoodsMenuDTO}
     * @version 1.0.1
     **/
    public Result<GoodsMenuDTO> wxMenuGetById(Long menuId) {
        return Result.succ(menuService.wxMenuGetById(menuId));
    }
}
