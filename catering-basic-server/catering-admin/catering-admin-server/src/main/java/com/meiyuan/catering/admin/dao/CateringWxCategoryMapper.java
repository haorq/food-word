package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.entity.CateringWxCategoryEntity;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Mapper
public interface CateringWxCategoryMapper extends BaseMapper<CateringWxCategoryEntity> {


    /**
     * describe: 查询所有小程序类目详情
     * @author: yy
     * @date: 2020/8/13 16:13
     * @return: {@link List< WxCategoryDetailVO>}
     * @version 1.3.0
     **/
    List<WxCategoryDetailVO> queryAllWxCategory();

    /**
     * 描述:查询该商品关联的分类
     *
     * @param goodsId
     * @return java.util.List<com.meiyuan.catering.admin.entity.CateringWxCategoryEntity>
     * @author zengzhangni
     * @date 2020/6/9 11:42
     * @since v1.1.0
     */
    List<CateringWxCategoryEntity> queryGoodsWxCategory(String goodsId);

    /**
     * describe: 查询详情
     * @author: yy
     * @date: 2020/8/4 14:39
     * @param id
     * @return: {@link WxCategoryDetailVO}
     * @version 1.3.0
     **/
    WxCategoryDetailVO queryDetailById(@Param("id") Long id);

    /**
     * describe: 分页列表
     * @author: yy
     * @date: 2020/8/4 15:21
     * @param page
     * @param dto
     * @return: {@link IPage< WxCategoryPageVO>}
     * @version 1.3.0
     **/
    IPage<WxCategoryPageVO> queryPageList(@Param("page") Page page, @Param("dto") WxCategoryPageDTO dto);

    /**
     * describe: 根据门店id删除关联门店
     * @author: yy
     * @date: 2020/8/27 14:06
     * @param shopId
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean deleteShopId(@Param("shopId") String shopId);
}
