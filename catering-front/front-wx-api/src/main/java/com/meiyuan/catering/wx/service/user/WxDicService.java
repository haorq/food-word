package com.meiyuan.catering.wx.service.user;

import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/31 15:12
 **/
@Service
public class WxDicService {


    @Resource
    private DicUtils dicUtils;


    /**
     * 通过多个codes获取字典
     * @param codes
     * @return
     */
    public List<DicDetailsAllVo> getDicCacheList(List<String> codes) {
        return dicUtils.getDicCacheList(codes);
    }
}
