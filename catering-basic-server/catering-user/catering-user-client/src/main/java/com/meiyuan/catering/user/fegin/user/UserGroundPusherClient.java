package com.meiyuan.catering.user.fegin.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.service.CateringUserGroundPusherService;
import com.meiyuan.catering.user.vo.user.PusherDetailsVo;
import com.meiyuan.catering.user.vo.user.PusherListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/5/19 13:49
 * @description
 **/
@Service
public class UserGroundPusherClient {

    @Resource
    private CateringUserGroundPusherService cateringUserGroundPusherService;


    public Result<IPage<PusherListVo>> selectList(PusherQueryDTO dto) {
        return Result.succ(cateringUserGroundPusherService.selectList(dto));
    }

    public Result<Boolean> updateById(CateringUserGroundPusherEntity entity) {
        return Result.succ(cateringUserGroundPusherService.updateById(entity));
    }

    public Result<Boolean> save(CateringUserGroundPusherEntity entity) {
        return Result.succ(cateringUserGroundPusherService.save(entity));
    }


    public Result<PusherDetailsVo> getById(Long id) {
        CateringUserGroundPusherEntity byId = cateringUserGroundPusherService.getById(id);
        return Result.succ(BaseUtil.objToObj(byId, PusherDetailsVo.class));
    }

    public Result<Integer> getPusherCode() {
        return Result.succ(cateringUserGroundPusherService.groundPuserCodeMaxInteger());
    }
}
