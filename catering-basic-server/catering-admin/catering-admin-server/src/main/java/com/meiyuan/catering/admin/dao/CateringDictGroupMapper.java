package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.entity.CateringDictGroup;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年3月2日
 */
@Mapper
public interface CateringDictGroupMapper extends BaseMapper<CateringDictGroup> {

    /**
     * 字典详情
     *
     * @param codes
     * @return
     */
    List<DicDetailsAllVo> listByCode(@Param("codes") List<String> codes);

    /**
     * 描述: 查询广告字典连接
     *
     * @param link
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/3 14:41
     * @since v1.1.0
     */
    String queryLink(String link);
}
