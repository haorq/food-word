package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategorySaveDTO;
import com.meiyuan.catering.admin.dto.wxcategory.v101.WxCategoryDetailV101DTO;
import com.meiyuan.catering.admin.entity.CateringWxCategoryEntity;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import com.meiyuan.catering.core.page.PageData;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
public interface CateringWxCategoryService extends IService<CateringWxCategoryEntity> {

    /**
     * 描述:重置小程序类目
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/5/6 15:49
     */
    void resetWxCategory();


    /**
     * 描述:重置缓存删除 key:""
     * 类目存值修改,删除以前的空字符串key
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/3 10:21
     * @since v1.1.0
     */
    void resetWxCategoryAndRemove();

    /**
     * 描述:验证排序
     *
     * @param id
     * @param sort
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/5/25 13:51
     * @since v1.1.0
     */
    String verifySortV1011(Long id, Integer sort);

    /**
     * 批量获取 关联类型是 商家和商品的
     *
     * @author: wxf
     * @date: 2020/5/26 14:00
     * @return: {@link WxCategoryDetailV101DTO}
     * @version 1.0.1
     **/
    List<WxCategoryDetailV101DTO> listByRelevanceType();

    /**
     * 描述:商品下架更新小程序类目
     *
     * @param goodsId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/9 10:47
     * @since v1.1.0
     */
    @Async
    void goodsDownUpdateWxCategory(String goodsId);

    /**
     * describe: 新增/修改
     * @author: yy
     * @date: 2020/8/4 10:27
     * @param dto
     * @return: {@link Long}
     * @version 1.3.0
     **/
    Long saveOrUpdate(WxCategorySaveDTO dto);

    /**
     * describe: 查询详情
     * @author: yy
     * @date: 2020/8/4 14:37
     * @param id
     * @return: {@link WxCategoryDetailVO}
     * @version 1.3.0
     **/
    WxCategoryDetailVO queryDetailById(Long id);

    /**
     * describe: 分页列表
     * @author: yy
     * @date: 2020/8/4 15:02
     * @param dto
     * @return: {@link PageData< WxCategoryPageVO>}
     * @version 1.3.0
     **/
    PageData<WxCategoryPageVO> queryPageList(WxCategoryPageDTO dto);

    /**
     * describe: 根据门店id删除关联门店
     * @author: yy
     * @date: 2020/8/27 14:04
     * @param shopId
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean deleteShopId(Long shopId);

    /**
     * describe: 删除类目
     * @author: yy
     * @date: 2020/9/2 10:56
     * @param id
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    Boolean deleteById(Long id);
}
