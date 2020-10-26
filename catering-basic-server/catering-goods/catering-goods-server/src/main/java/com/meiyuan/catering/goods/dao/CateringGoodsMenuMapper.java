package com.meiyuan.catering.goods.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.goods.GoodsNameAndIdDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuDTO;
import com.meiyuan.catering.goods.dto.menu.GoodsMenuLimitQueryDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单表(CateringGoodsMenu)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-18 18:33:35
 */
@Mapper
public interface CateringGoodsMenuMapper extends BaseMapper<CateringGoodsMenuEntity>{
    /**
     * 商户菜单列表
     * @description 商户菜单列表
     * @author yaozou
     * @date 2020/3/22 12:18
     * @param page 分页参数
     * @param dto 查询条件
     * @param merchantId 商户id
     * @since v1.0.0
     * @return {@link List<GoodsMenuDTO>}
     */
    IPage<GoodsMenuDTO> listForMerchant(IPage page, @Param("dto") GoodsMenuLimitQueryDTO dto, @Param("merchantId") Long merchantId);


    /**
     * 商户菜单商品列表
     * @description 商户菜单商品列表
     * @author yaozou
     * @date 2020/3/22 13:38
     * @param menuId 菜单id
     * @since v1.0.0
     * @return {@link GoodsNameAndIdDTO}
    */
    List<GoodsNameAndIdDTO> listGoodsInMenu(@Param("menuId") Long menuId);

    /**
     * 小程序菜单列表 按照merchantIds的顺序排序
     * @description 小程序菜单列表 按照merchantIds的顺序排序
     * @author yaozou
     * @date 2020/3/24 18:22
     * @param page 分页参数
     * @param dto 菜单日期
     *        merchantIds 商户
     * @since v1.0.0
     * @return  {@link PageData <GoodsMenuDTO>}
     */
    IPage<GoodsMenuDTO> listForWechat(IPage page,@Param("dto") GoodsMenuLimitQueryDTO dto);

    /**
     * 上下架时间段包含的菜单
     *
     * @author: wxf
     * @date: 2020/3/25 18:13
     * @param begin 开始时间
     * @param end 结束时间
     * @return: {@link List< CateringGoodsMenuEntity>}
     **/
    List<CateringGoodsMenuEntity> timeContainMenu(@Param("begin") LocalDate begin,
                                                  @Param("end") LocalDate end);


    /**
     * 微信商户主页--菜单列表
     * @description 微信商户主页--菜单列表
     * @author yaozou
     * @date 2020/3/30 12:57
     * @param dto 查询参数
     * @param merchantId 商户id
     * @since v1.0.0
     * @return {@link List< GoodsMenuDTO>}
     */
    List<GoodsMenuDTO> listForWxMerchantIndex( @Param("dto") GoodsMenuLimitQueryDTO dto, @Param("merchantId") Long merchantId);

}