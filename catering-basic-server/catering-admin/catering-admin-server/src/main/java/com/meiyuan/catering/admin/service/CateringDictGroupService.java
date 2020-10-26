package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.entity.CateringDictGroup;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;

import java.util.List;

/**
 * @author lhm
 * @date 2020/3/18 16:05
 **/
public interface CateringDictGroupService extends IService<CateringDictGroup> {

    /**
     * 查询所有字典
     *
     * @return
     */
    List<DicAllVo> findDicGroup();

    /**
     * 字典详情
     *
     * @param codes
     * @return
     */
    List<DicDetailsAllVo> detail(List<String> codes);

    /**
     * 描述:查询广告字典连接
     *
     * @param link
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/3 14:42
     * @since v1.1.0
     */
    String queryLink(String link);
}
