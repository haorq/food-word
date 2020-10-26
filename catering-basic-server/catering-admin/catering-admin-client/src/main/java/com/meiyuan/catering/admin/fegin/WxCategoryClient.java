package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategorySaveDTO;
import com.meiyuan.catering.admin.dto.wxcategory.v101.WxCategoryDetailV101DTO;
import com.meiyuan.catering.admin.service.CateringWxCategoryService;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 10:47 10 47
 * @description
 **/
@Service
public class WxCategoryClient {
    @Autowired
    private CateringWxCategoryService cateringWxCategoryService;

    /**
     * @return {@link }
     * @Author lhm
     * @Description 微信类目
     * @Date 11:01 2020/5/19
     * @Param []
     * @Version v1.1.0
     */
    public void resetWxCategory() {
        cateringWxCategoryService.resetWxCategoryAndRemove();
    }

    /**
     * 描述:验证排序
     *
     * @param id
     * @param sort
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/25 13:50
     * @since v1.1.0
     */
    public Result<String> verifySortV1011(Long id, Integer sort) {
        return Result.succ(cateringWxCategoryService.verifySortV1011(id, sort));
    }

    /**
     * 批量获取 关联类型是 商家和商品的
     *
     * @author: wxf
     * @date: 2020/5/26 14:00
     * @return: {@link WxCategoryDetailV101DTO}
     * @version 1.0.1
     **/
    public Result<List<WxCategoryDetailV101DTO>> listByRelevanceType() {
        return Result.succ(cateringWxCategoryService.listByRelevanceType());
    }

    /**
     * 描述:商品下架更新小程序类目
     *
     * @param goodsId
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/6/9 11:52
     * @since v1.1.0
     */
    public Result<Boolean> asyncGoodsDownUpdateWxCategory(String goodsId) {
        cateringWxCategoryService.goodsDownUpdateWxCategory(goodsId);
        return Result.succ();
    }

    /**
     * describe: 新增/修改
     * @author: yy
     * @date: 2020/8/3 17:10
     * @param dto
     * @return: {@link Result< Long>}
     * @version 1.3.0
     **/
    public Result<Long> saveOrUpdate(WxCategorySaveDTO dto){
        return Result.succ(cateringWxCategoryService.saveOrUpdate(dto));
    }

    /**
     * describe: 查询详情
     * @author: yy
     * @date: 2020/8/4 14:36
     * @param id
     * @return: {@link Result< WxCategoryDetailVO>}
     * @version 1.3.0
     **/
    public Result<WxCategoryDetailVO> queryDetailById(Long id) {
        return Result.succ(cateringWxCategoryService.queryDetailById(id));
    }

    /**
     * describe: 分页列表
     * @author: yy
     * @date: 2020/8/4 15:02
     * @param dto
     * @return: {@link Result< PageData< WxCategoryPageVO>>}
     * @version 1.3.0
     **/
    public Result<PageData<WxCategoryPageVO>> queryPageList(WxCategoryPageDTO dto) {
        return Result.succ(cateringWxCategoryService.queryPageList(dto));
    }

    /**
     * describe: 根据门店id删除关联门店
     * @author: yy
     * @date: 2020/8/27 14:03
     * @param shopId
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> deleteShopId(Long shopId){
        return Result.succ(cateringWxCategoryService.deleteShopId(shopId));
    }

    /**
     * describe: 删除类目
     * @author: yy
     * @date: 2020/9/2 10:52
     * @param id
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> deleteById(Long id) {
        return Result.succ(cateringWxCategoryService.deleteById(id));
    }
}
