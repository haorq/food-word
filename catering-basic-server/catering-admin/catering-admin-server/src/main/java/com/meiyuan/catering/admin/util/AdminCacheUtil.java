package com.meiyuan.catering.admin.util;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.admin.entity.CateringWxCategoryEntity;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.admin.vo.wxcategory.WxCategoryDetailVO;
import com.meiyuan.catering.core.dto.base.RedisAdvertisingDTO;
import com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkEnum;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkTypeEnum;
import com.meiyuan.catering.core.enums.base.WxCategoryStatusEnum;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CacheUtil;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/6/1 16:35
 * @since v1.1.0
 */
@Component
public class AdminCacheUtil {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.REDIS_ADVERTISING_KEY))
    private AdvancedCache advertisingCache;
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.REDIS_WX_CATEGORY_KEY))
    private AdvancedCache wxCategoryCache;


    /**
     * 描述:删除缓存广告
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/3 15:21advertisingList
     * @since v1.1.0
     */
    public void delAdvertising() {
        CacheUtil.removeAll(advertisingCache);
    }

    /**
     * 描述:删除缓存的小程序类目
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/3 15:21
     * @since v1.1.0
     */
    public void remove() {
        CacheUtil.removeAll(wxCategoryCache);
    }

    /**
     * describe: 根据集合添加类目缓存
     * @author: yy
     * @date: 2020/8/13 16:17
     * @param detailList
     * @return: {@link }
     * @version 1.3.0
     **/
    public void putWxCategory(List<WxCategoryDetailVO> detailList) {
        if(CollectionUtils.isEmpty(detailList)){
            return;
        }
        detailList.forEach(detail->{
            // 禁用
            if(WxCategoryStatusEnum.DISABLE.getStatus().equals(detail.getStatus())){
                return;
            }
            RedisWxCategoryDTO dto = BaseUtil.objToObj(detail, RedisWxCategoryDTO.class);
            this.putWxCategory(dto);
        });
    }
    public void putWxCategoryGoodsList(List<CateringWxCategoryEntity> entitys) {
        for (CateringWxCategoryEntity entity : entitys) {
            RedisWxCategoryDTO redisDTO = (RedisWxCategoryDTO)wxCategoryCache.get(entity.getId());
            redisDTO.setStoryGoodsList(entity.getStoryGoodsList());
            putWxCategory(redisDTO);
        }
    }

    public void putWxCategory(RedisWxCategoryDTO dto) {
        wxCategoryCache.put(dto.getId().toString(), dto);
    }

    /**
     * describe: 单个类目添加缓存
     * @author: yy
     * @date: 2020/8/13 16:18
     * @param detail
     * @return: {@link }
     * @version 1.3.0
     **/
    public void putWxCategory(WxCategoryDetailVO detail) {
        RedisWxCategoryDTO dto = BaseUtil.objToObj(detail, RedisWxCategoryDTO.class);
        dto.setId(detail.getId());
        this.putWxCategory(dto);
    }

    /**
     * describe: 根据id移除缓存中的小程序类目
     * @author: yy
     * @date: 2020/9/2 10:21
     * @param id
     * @return: {@link }
     * @version 1.4.0
     **/
    public boolean removeWxCategory(Long id){
        if(id == null){
            return false;
        }
        return this.wxCategoryCache.remove(id);
    }

    /**
     * describe: 添加单个广告缓存
     * @author: yy
     * @date: 2020/9/4 9:54
     * @param dto
     * @return: {@link }
     * @version 1.4.0
     **/
    public void putAdvertising(RedisAdvertisingDTO dto) {
        if(dto == null){
            return;
        }
        this.advertisingCache.put(dto.getId().toString(), dto);
    }

    /**
     * describe: 根据id移除广告缓存
     * @author: yy
     * @date: 2020/9/4 9:55
     * @param id
     * @return: {@link boolean}
     * @version 1.4.0
     **/
    public boolean removeAdvertising(Long id) {
        return this.advertisingCache.remove(id);
    }

    /**
     * describe: 添加多个广告缓存
     * @author: yy
     * @date: 2020/9/4 9:55
     * @param list
     * @return: {@link }
     * @version 1.4.0
     **/
    public void putAdvertising(List<AdvertisingDetailVO> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(detail->{
            Boolean enabled = detail.getEnabled();
            Boolean shows = detail.getShows();
            if (WxCategoryStatusEnum.DISABLE.getFlag().equals(enabled) || Boolean.FALSE.equals(shows)) {
                return;
            }
            String linkStr = detail.getLinkStr();
            Integer linkType = detail.getLinkType();
            if(AdvertisingLinkTypeEnum.CUSTOMIZE.getStatus().equals(linkType)){
                if(CollectionUtils.isEmpty(detail.getAdvertisingExtList())){
                    linkStr = null;
                }else{
                    linkStr = AdvertisingLinkTypeEnum.CUSTOMIZE.getUrl() + detail.getId();
                }
            }else if (AdvertisingLinkTypeEnum.INSIDE.getStatus().equals(linkType)){
                String link = detail.getLink();
                if(AdvertisingLinkEnum.APPOINT_SHOP.getStatus().equals(link)){
                    linkStr = linkStr + "?shopId=" + detail.getShopId();
                }else if(AdvertisingLinkEnum.APPOINT_GOODS.getStatus().equals(link)){
                    linkStr = linkStr + "?shopId=" + detail.getShopId() + "&goodsId="+detail.getGoodsId()+"&showDetail=true";
                }
            }else {
                linkStr = null;
            }
            detail.setLink(linkStr);
            RedisAdvertisingDTO dto = BaseUtil.objToObj(detail, RedisAdvertisingDTO.class);
            this.putAdvertising(dto);
        });
    }

}
