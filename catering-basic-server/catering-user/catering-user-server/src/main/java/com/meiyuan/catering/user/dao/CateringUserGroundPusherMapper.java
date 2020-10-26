package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.vo.user.PusherListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Mapper
public interface CateringUserGroundPusherMapper extends BaseMapper<CateringUserGroundPusherEntity> {

    /**
     * 描述：地推员列表查询
     * @author lhm
     * @date 2020/6/23
     * @param page
     * @param dto
     * @return {@link IPage< PusherListVo>}
     * @version 1.1.1
     **/
    IPage<PusherListVo> selectList(Page page, @Param("dto") PusherQueryDTO dto);


    /**
     * 描述：获取地推员编码最大值
     * @author lhm
     * @date 2020/6/23
     * @param
     * @return {@link Integer}
     * @version 1.1.1
     **/
    Integer groundPuserCodeMaxInteger();
}
