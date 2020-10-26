package com.meiyuan.catering.merchant.pc.service.common;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.meiyuan.catering.admin.fegin.DictGroupClient;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MerchantPcDictService
 * @Description
 * @Author gz
 * @Date 2020/7/10 9:35
 * @Version 1.2.0
 */
@Service
public class MerchantPcDictService {

    @Autowired
    private DicUtils dicUtils;
    @Resource
    private DictGroupClient dictGroupClient;

    /**
     * 通过多个codes获取字典
     * @param codes
     * @return
     */
    public List<DicDetailsAllVo> getDicCacheList(List<String> codes) {
        return dicUtils.getDicCacheList(codes);
    }



    public Result<List<DicAllVo>> findDicGroup() {
        return dictGroupClient.findDicGroup();
    }


    /**
     * @Author lhm
     * @Description  刷新所有缓存
     * @Date 2020/5/19
     * @Param []
     * @return {@link Result}
     * @Version v1.1.0
     */
    public Result refreshDicCache() {
        List<String> codes = new ArrayList<>();
        List<DicDetailsAllVo> dataVos = dictGroupClient.detail(codes).getData();
        if (!CollectionUtils.isEmpty(dataVos)) {
            dataVos.forEach(dictDataVo ->
                    dicUtils.refreshDicCache(dictDataVo)
            );
        }
        return Result.succ();
    }
}
