package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.vo.user.PusherListVo;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
public interface CateringUserGroundPusherService extends IService<CateringUserGroundPusherEntity> {

    /**
     * 描述：地推员列表查询
     * @author lhm
     * @date 2020/6/23
     * @param dto
     * @return {@link IPage< PusherListVo>}
     * @version 1.1.1
     **/
    IPage<PusherListVo> selectList(PusherQueryDTO dto);


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
