package com.meiyuan.catering.admin.service.admin;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategoryPageDTO;
import com.meiyuan.catering.admin.dto.wxcategory.WxCategorySaveDTO;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryPageVO;
import com.meiyuan.catering.core.dto.base.ShopListV101DTO;
import com.meiyuan.catering.core.dto.base.WxCategoryGoodsDTO;
import com.meiyuan.catering.core.enums.base.WxCategoryTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.wxcategory.WxCategoryGoodsVO;
import com.meiyuan.catering.goods.entity.CateringGoodsEntity;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSpuClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 小程序类目
 *
 * @author zengzhangni
 * @date 2020-05-06
 */
@Service
@Slf4j
public class AdminWxCategoryService {

    @Resource
    private WxCategoryClient wxCategoryClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MerchantGoodsClient merchantGoodsClient;
    @Resource
    private ShopGoodsSpuClient shopGoodsSpuClient;
    @Resource
    private GoodsClient goodsClient;

    public Result<List<ShopListV101DTO>> shopListV101() {
        List<ShopListV101DTO> entities = ClientUtil.getDate(shopClient.shopListV101());
        return Result.succ(entities);
    }

    public Result<String> verifySortV1011(Long id, Integer sort) {
        return wxCategoryClient.verifySortV1011(id, sort);
    }

    /**
     * describe: 刷新redis小程序类目
     *
     * @author: zengzhangni
     * @date: 2020/8/13 16:07
     * @return: {@link Result}
     * @version 1.3.0
     **/
    public Result<Boolean> resetWxCategory() {
        wxCategoryClient.resetWxCategory();
        return Result.succ();
    }

    /**
     * describe: 新增/修改
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/3 17:10
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> saveOrUpdate(WxCategorySaveDTO dto) {
        Integer type = dto.getType();
        // 是爆款推荐修改
        if (dto.getId() != null && WxCategoryTypeEnum.HOT_MONEY.getStatus().equals(type)) {
            List<WxCategoryGoodsDTO> storyGoodsList = dto.getStoryGoodsList();
            if (CollectionUtils.isNotEmpty(storyGoodsList)) {
                List<Long> idList = storyGoodsList.stream().map(WxCategoryGoodsDTO::getShopId).collect(Collectors.toList());
                List<CateringShopEntity> shopEntityList = shopClient.queryByIdList(idList);
                if (CollectionUtils.isEmpty(shopEntityList)) {
                    throw new CustomException("找不到选择的所有门店！");
                }
                shopEntityList.forEach(shop -> {
                    Boolean del = shop.getDel();
                    if (DelEnum.NOT_DELETE.getFlag().equals(del)) {
                        return;
                    }
                    throw new CustomException("找不到门店【" + shop.getShopName() + "】！");
                });
                List<Long> spuIdList = storyGoodsList.stream().map(WxCategoryGoodsDTO::getShopGoodsId).collect(Collectors.toList());
                Map<Long, WxCategoryGoodsDTO> idMap = storyGoodsList.stream().collect(Collectors.toMap(WxCategoryGoodsDTO::getShopGoodsId, e -> e, (k1, k2) -> k1));
                List<WxCategoryGoodsVO> shopGoodsList = shopGoodsSpuClient.queryByIdList(spuIdList);
                if (CollectionUtils.isEmpty(shopGoodsList)) {
                    throw new CustomException("找不到选择的所有商品！");
                }
                shopGoodsList.forEach(spu -> {
                    Boolean del = spu.getDel();
                    String goodsName = spu.getGoodsName();
                    WxCategoryGoodsDTO goodsDto = idMap.get(spu.getShopGoodsId());
                    if(Objects.nonNull(goodsDto) && !BaseUtil.isEmptyStr(goodsDto.getGoodsName())){
                        goodsName = goodsDto.getGoodsName();
                    }
                    if (DelEnum.DELETE.getFlag().equals(del)
                            || Objects.equals(spu.getMerchantGoodsStatus(), GoodsStatusEnum.LOWER_SHELF.getStatus())
                            || GoodsStatusEnum.LOWER_SHELF.getStatus().equals(spu.getShopGoodsStatus())) {
                        throw new CustomException("找不到门店【" + spu.getShopName() + "】的商品【" + goodsName + "】！");
                    }
                });
            }
        }
        Long id = ClientUtil.getDate(wxCategoryClient.saveOrUpdate(dto));
        boolean blank = StringUtils.isNotBlank(id.toString());
        return Result.succ(blank);
    }

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/8/4 14:24
     * @return: {@link Result<WxCategoryDetailVO>}
     * @version 1.3.0
     **/
    public Result<WxCategoryDetailVO> queryDetailById(Long id) {
        Result<WxCategoryDetailVO> result = wxCategoryClient.queryDetailById(id);
        WxCategoryDetailVO detail = result.getData();
        if (null == detail) {
            return result;
        }
        List<WxCategoryGoodsVO> storyGoodsListJson = detail.getStoryGoodsList();
        if (CollectionUtils.isEmpty(storyGoodsListJson)) {
            return result;
        }
        List<WxCategoryGoodsVO> storyGoodsList = BaseUtil.objToObj(storyGoodsListJson, WxCategoryGoodsVO.class);
        Integer type = detail.getType();
        // 是爆款推荐
        if (WxCategoryTypeEnum.HOT_MONEY.getStatus().equals(type)) {
            List<Long> shopGoodsIdList = storyGoodsList.stream().map(WxCategoryGoodsVO::getShopGoodsId).collect(Collectors.toList());
            storyGoodsList = merchantGoodsClient.queryByShopGoodsId(shopGoodsIdList);
            detail.setStoryGoodsList(storyGoodsList);
        } else {
            List<Long> idList = storyGoodsList.stream().map(e -> {
                String goodsId = e.getGoodsId();
                return Long.valueOf(BaseUtil.isEmptyStr(goodsId) ? "0" : goodsId);
            }).collect(Collectors.toList());
            List<CateringGoodsEntity> goodsEntityList = this.goodsClient.queryByIdList(idList);
            List<WxCategoryGoodsVO> finalStoryGoodsList = Lists.newArrayList();
            goodsEntityList.forEach(e -> {
                WxCategoryGoodsVO goods = new WxCategoryGoodsVO();
                goods.setGoodsName(e.getGoodsName());
                goods.setGoodsId(e.getId().toString());
                finalStoryGoodsList.add(goods);
            });
            detail.setStoryGoodsList(finalStoryGoodsList);
        }
        result.setData(detail);
        return result;
    }

    /**
     * describe: 分页列表
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/4 15:01
     * @return: {@link Result< PageData< WxCategoryPageVO>>}
     * @version 1.3.0
     **/
    public Result<PageData<WxCategoryPageVO>> queryPageList(WxCategoryPageDTO dto) {
        return wxCategoryClient.queryPageList(dto);
    }

    /**
     * describe: 删除类目
     *
     * @param id
     * @author: yy
     * @date: 2020/9/2 10:52
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> deleteById(Long id) {
        return wxCategoryClient.deleteById(id);
    }

}

