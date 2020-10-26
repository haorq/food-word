package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.menu.*;
import com.meiyuan.catering.goods.entity.CateringGoodsMenuEntity;

import java.util.List;

/**
 * 菜单表(CateringGoodsMenu)服务层
 *
 * @author wxf
 * @since 2020-03-18 18:35:41
 */
public interface CateringGoodsMenuService  extends IService<CateringGoodsMenuEntity> {
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
    String saveUpdate(GoodsMenuDTO dto);

    /**
     * 列表分页
     *
     * @author: wxf
     * @date: 2020/3/21 16:38
     * @param dto 分页参数
     * @return: {@link PageData< GoodsMenuDTO>}
     * @version 1.0.1
     **/
    PageData<GoodsMenuDTO> listLimit(GoodsMenuLimitQueryDTO dto);

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/3/21 16:48
     * @param menuId 菜单id
     * @return: {@link String}
     * @version 1.0.1
     **/
    String del(Long menuId);

    /**
     * 菜单详情
     *
     * @author: wxf
     * @date: 2020/3/21 16:58
     * @param menuId 菜单id
     * @return: {@link GoodsMenuDTO}
     **/
    GoodsMenuDTO menuInfoById(Long menuId);

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
    String pushMerchant(PushMenuDTO dto);


    /**
     * 商户菜单列表查询
     * @description 商户菜单列表查询
     * @author yaozou
     * @date 2020/3/22 12:14
     * @param dto 查询条件
     * @param merchantId 商户id
     * @since v1.0.0
     * @return {@link PageData< GoodsMenuDTO>}
     */
    PageData<GoodsMenuDTO> listForMerchant(GoodsMenuLimitQueryDTO dto,Long merchantId);

    /**
     * 小程序菜单列表 按照merchantIds的顺序排序
     *
     * @description 小程序菜单列表 按照merchantIds的顺序排序
     * @author yaozou
     * @date 2020/3/24 18:18
     * @param  dto date 菜单日期
     *        merchantIds 商户
     * @since v1.0.0
     * @return {@link PageData<GoodsMenuDTO>}
     */
    PageData<GoodsMenuDTO> listForWechat(GoodsMenuLimitQueryDTO dto);

    /**
     * 微信商户主页--菜单列表
     *
     * @description 微信商户主页--菜单列表
     * @author yaozou
     * @date 2020/3/30 12:57
     * @param dto 查询参数
     * @param merchantId 商户id
     * @since v1.0.0
     * @return {@link PageData<GoodsMenuDTO>}
     */
    List<GoodsMenuDTO> listForWxMerchantIndex(GoodsMenuLimitQueryDTO dto,Long merchantId);

    /**
     * 菜单定时上下架
     * 同步ES
     * @author: wxf
     * @date: 2020/5/19 16:24
     * @return: {@link  List< Long>}
     * @version 1.0.1
     **/
    List<Long> timingUpDown();

    /**
     * 微信获取菜单对应的送达时间
     * 1、菜单模式 返回对应送达时间
     * 2、菜品模式 返回明后两天的时间
     *
     * @author: wxf
     * @date: 2020/3/31 18:13
     * @param dto 查询参数
     * @return: {@link WxMenuServiceTimeDTO}
     **/
    WxMenuServiceTimeDTO wxMenuService(WxMenuServiceTimeQueryDTO dto);

    /**
     * 验证菜单编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 菜单编码
     * @return: {@link boolean}
     * @version 1.0.1
     **/
    boolean validationCode(String code);

    /**
     * 微信端获取根据菜单id
     *
     * @author: wxf
     * @date: 2020/5/22 18:09
     * @param menuId 菜单id
     * @return: {@link GoodsMenuDTO}
     * @version 1.0.1
     **/
    GoodsMenuDTO wxMenuGetById(Long menuId);
}