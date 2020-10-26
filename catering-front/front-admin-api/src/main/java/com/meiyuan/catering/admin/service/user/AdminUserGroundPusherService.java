package com.meiyuan.catering.admin.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.core.qrcode.QrCodeUtils;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.TicketSelectListDTO;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.user.dto.user.PusherAddOrUpdateDTO;
import com.meiyuan.catering.user.dto.user.PusherQueryDTO;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.entity.CateringUserPusherTicketRelationEntity;
import com.meiyuan.catering.user.fegin.user.UserGroundPusherClient;
import com.meiyuan.catering.user.fegin.user.UserPusherTicketRelationClient;
import com.meiyuan.catering.user.vo.user.PusherDetailsVo;
import com.meiyuan.catering.user.vo.user.PusherListVo;
import com.meiyuan.catering.user.vo.user.UserPusherTicketRelationListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @date 2020/5/6 14:25
 **/
@Service
public class AdminUserGroundPusherService {

    @Resource
    private UserGroundPusherClient groundPusherClient;

    @Resource
    private UserPusherTicketRelationClient userPusherTicketRelationClient;
    @Resource
    private AdminUtils adminUtils;
    @Resource
    private QrCodeUtils qrCodeUtils;
    @Resource
    private MarketingTicketClient marketingTicketClient;

    /**
     * @return {@link Result< IPage< PusherListVo>>}
     * @Author lhm
     * @Description 地推员列表分页查询
     * @Date 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<IPage<PusherListVo>> selectList(PusherQueryDTO dto) {
        return Result.succ(groundPusherClient.selectList(dto).getData());
    }

    /**
     * @return {@link Result< Boolean>}
     * @Author lhm
     * @Description 添加或修改地推员信息
     * @Date 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<Boolean> updateOrAdd(PusherAddOrUpdateDTO dto) {
        CateringUserGroundPusherEntity entity = new CateringUserGroundPusherEntity();
        BeanUtils.copyProperties(dto, entity);
        boolean flag;
        if (entity.getId() != null) {
            flag = groundPusherClient.updateById(entity).getData();
        } else {
            entity.setId(IdWorker.getId());
            entity.setCreateTime(LocalDateTime.now());
            entity.setPusherCode(getPusherCode());
            entity.setQrCode(qrCodeUtils.createPusherImage(entity.getId()));
            flag = groundPusherClient.save(entity).getData();
        }
        userPusherTicketRelationClient.saveOrUpdateBatchRelation(dto.getTicketIds(), entity.getId());
        return flag ? Result.succ() : Result.fail();
    }


    /**
     * @return {@link Result<PusherDetailsVo>}
     * @Author lhm
     * @Description 地推员详情
     * @Date 2020/5/19
     * @Param [id]
     * @Version v1.1.0
     */
    public Result<PusherDetailsVo> detailsById(Long id) {
        PusherDetailsVo data = groundPusherClient.getById(id).getData();
        if (!ObjectUtils.isEmpty(data)) {
            List<UserPusherTicketRelationListVo> list = selectByPusherId(id);
            data.setEntities(list);
        }
        return Result.succ(data);
    }


    /**
     * @return {@link Boolean}
     * @Author lhm
     * @Description 删除优惠券与地推员关联关系
     * @Date 2020/5/19
     * @Param [ticketId]
     * @Version v1.1.0
     */
    public Boolean removeByPusherId(Long ticketId) {
        LambdaQueryWrapper<CateringUserPusherTicketRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserPusherTicketRelationEntity::getTicketId, ticketId);
        return userPusherTicketRelationClient.remove(queryWrapper).getData();
    }

    public List<UserPusherTicketRelationListVo> selectByPusherId(Long pusherId) {
        List<UserPusherTicketRelationListVo> vos = userPusherTicketRelationClient.selectByPusherId(pusherId);
        List<TicketSelectListDTO> dtos = marketingTicketClient.selectTicket(MarketingTicketSendTicketPartyEnum.PLATFORM).getData();
        List<Long> ids = dtos.stream().map(TicketSelectListDTO::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(vos)) {
           vos = vos.stream().filter(e -> ids.contains(e.getTicketId())).collect(Collectors.toList());
        }
        return vos;
    }

    /**
     * @description: 获取地推员二维码的最大值
     * @author: lhm
     * @date: 2020/6/23
     * @param
     * @return: {@link String}
     * @version 1.1.1
     **/
    public String getPusherCode(){
        //获取数据库中当前地推员编码最大值
        Integer pusherCodeMax = groundPusherClient.getPusherCode().getData();
        if(pusherCodeMax==null){
            pusherCodeMax = 0;
        }
        return adminUtils.getPusherCode(pusherCodeMax);
    }


}
