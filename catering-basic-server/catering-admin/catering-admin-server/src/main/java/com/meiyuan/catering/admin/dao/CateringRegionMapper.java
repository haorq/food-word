package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.core.vo.base.CateringRegionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 17:45
 * @Description 简单描述 :  地区
 * @Since version-1.0.0
 */
@Mapper
public interface CateringRegionMapper extends BaseMapper<CateringRegionEntity> {

    /**
     * 方法描述 : 查询所有省
     * @Author: MeiTao
     * @Date: 2020/6/23 0023 17:46
     * @return: java.util.List<com.meiyuan.catering.core.vo.base.CateringRegionVo>
     * @Since version-1.1.0
     */
    List<CateringRegionVo> listProvinceQuery();

    /**
     * 查询当前省下的所有市
     * @param adressCode
     * @return
     */
    List<CateringRegionVo> listCityQuery(String adressCode);

    /**
     * 查询当前市下的所有区
     * @param adressCode
     * @return
     */
    List<CateringRegionVo> listDistrictQuery(String adressCode);
}