package com.meiyuan.catering.core.redis.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.core.vo.base.DicIntemVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhm
 * 字典表缓存
 * @date 2020/3/30 16:59
 **/
@Component
public class DicUtils {
    private static final Logger logger = LoggerFactory.getLogger(DicUtils.class);
    @CreateCache(name = JetcacheNames.ADMIN_DIC, area = JetcacheAreas.ADMIN_AREA)
    private Cache<String, DicDetailsAllVo> dicCache;

    @CreateCache(name = JetcacheNames.USER_COMPANY_STATUS, area = JetcacheAreas.USER_AREA)
    private Cache<Long, Integer> companyCache;


    /**
     * 刷新所有缓存
     *
     * @param vo
     */
    public void refreshDicCache(DicDetailsAllVo vo) {
        dicCache.put(vo.getCode(), vo);
    }


    /**
     * 获取缓存
     *
     * @param code
     */
    public DicDetailsAllVo getDicCache(String code) {
        return dicCache.get(code);
    }

    /**
     * 通过多个codes获取缓存
     *
     * @param codes
     * @return
     */
    public List<DicDetailsAllVo> getDicCacheList(List<String> codes) {
        List<DicDetailsAllVo> vos = codes.stream().map(i -> {
            DicDetailsAllVo dicDetailsAllVo = dicCache.get(i);
            return dicDetailsAllVo;
        }).collect(Collectors.toList());
        return vos;
    }

    /**
     * 缓存公司id  状态
     * @param companyId
     */
    public  void put(Long companyId,Integer status){
        companyCache.put(companyId,status);
    }

    /**
     * 获取企业状态
     * @param companyId
     * @return
     */
    public Integer getCompanyStatus(Long companyId) {
        return companyCache.get(companyId);
    }

    /**
     * 刷新所有字典缓存
     */
    public void refreshCompanyCache(Map<Long,Integer> map) {
        companyCache.putAll(map);
    }


    public Map<Integer,String> getNames(String type){
        DicDetailsAllVo vo = dicCache.get(type);
        if (vo == null){
            return new HashMap<>(16);
        }
        List<DicIntemVo> item = vo.getVos();
        if(CollectionUtils.isEmpty(item)){
            return new HashMap<>(16);
        }
        Map<Integer,String> map = new HashMap<>(item.size());
        try {
            map = item.stream().collect(Collectors.toMap(i -> Integer.valueOf(i.getItemCode()), DicIntemVo::getItemName));
        } catch (NumberFormatException e) {
            logger.error("字典转换异常");
        }
        return map;
    }
}
