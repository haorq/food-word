package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dao.CateringCartShareBillMapper;
import com.meiyuan.catering.user.dto.cart.CartShareBillBaseDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillUserDTO;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.user.entity.CateringCartShareBillEntity;
import com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity;
import com.meiyuan.catering.user.enums.BillStatusEnum;
import com.meiyuan.catering.user.service.CateringCartService;
import com.meiyuan.catering.user.service.CateringCartShareBillService;
import com.meiyuan.catering.user.service.CateringCartShareBillUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yaozou
 * @description 拼单服务
 * ${@link CateringCartShareBillEntity}
 * @date 2020/3/25 14:18
 * @since v1.0.0
 */
@Service
@Slf4j
public class CateringCartShareBillServiceImpl extends ServiceImpl<CateringCartShareBillMapper, CateringCartShareBillEntity> implements CateringCartShareBillService {

    @Autowired
    private CateringCartShareBillUserService shareBillUserService;
    @Autowired
    private CateringCartService cartService;

    @Override
    public CartShareBillDTO getShareBillByNo(String shareBilNo, boolean userInfoFlag) {
        CartShareBillDTO dto = new CartShareBillDTO();

        CateringCartShareBillEntity billEntity = getShareBillEntityByNo(shareBilNo);
        dto.setShareBill(ConvertUtils.sourceToTarget(billEntity, CartShareBillBaseDTO.class));

        if (userInfoFlag) {
            List<CateringCartShareBillUserEntity> entities = shareBillUserService.list(shareBilNo);

            dto.setShareBillUserList(ConvertUtils.sourceToTarget(entities, CartShareBillUserDTO.class));
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateShareBillDTO dto) {
        CateringCartShareBillEntity shareBill =
                notPayShareBillByMerchantAndUser(dto.getMerchantId(), dto.getShopId(), dto.getShareUserId(), dto.getType());
        if (shareBill != null) {
            return shareBill.getShareBillNo();
        }
        shareBill = ConvertUtils.sourceToTarget(dto, CateringCartShareBillEntity.class);
        shareBill.setStatus(BillStatusEnum.CHOOSING.getStatus());
        shareBill.setShareTime(LocalDateTime.now());
        shareBill.setShareBillNo(CodeGenerator.orderNo());
        //保存拼单信息
        save(shareBill);

        //保存拼单人信息
        addUser(shareBill.getShareBillNo(), dto.getShareUserId(), dto.getAvatar(), dto.getNickname());

        return shareBill.getShareBillNo();
    }

    @Override
    public Boolean join(String shareBillNo, long userId, String avatar, String nickname) {
        return addUser(shareBillNo, userId, avatar, nickname);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CartShareBillDTO> cancel(String shareBillNo, long userId) {
        CartShareBillDTO dto = getShareBillByNo(shareBillNo, true);
        CartShareBillBaseDTO cartShareBill = dto.getShareBill();
        if (cartShareBill != null) {
            if (cartShareBill.getShareUserId() != userId) {
                return Result.fail("无法取消，你非发起人~~~~~~");
            }
            removeById(cartShareBill.getId());

            // 删除用户信息
            shareBillUserService.deleteByShareBillNo(shareBillNo, null);
        }

        return Result.succ(dto);
    }

    @Override
    public void exist(String shareBillNo, long userId) {
        CateringCartShareBillEntity entity = getShareBillEntityByNo(shareBillNo);
        if (entity != null) {
            Integer status = entity.getStatus();
            if (!Objects.equals(BillStatusEnum.CHOOSING.getStatus(), status)) {
                //如果拼单人购买了商品 不能退出拼单
                if (cartService.isChooseGoods(userId, shareBillNo)) {
                    boolean payed = Objects.equals(BillStatusEnum.PAYING.getStatus(), status);

                    throw new CustomException(ErrorCode.SHARE_BILL_PAYED_BILL_USER_ERROR,
                            payed ? "好友正在结算，不能退出拼单！" : "好友已提交订单，不能退出拼单！"
                    );
                }
            }
        }
        // 删除用户信息
        shareBillUserService.deleteByShareBillNo(shareBillNo, userId);
    }


    @Override
    public List<CateringCartShareBillEntity> jobAutoCancelShareBill(Integer type, Integer day, List<Long> menuIds) {
        LambdaQueryWrapper<CateringCartShareBillEntity> queryWrapper = new LambdaQueryWrapper();
        if (day != null) {
            LocalDate date = LocalDate.now().minusDays(day);
            queryWrapper.le(true, CateringCartShareBillEntity::getShareTime, date);
        }
        queryWrapper.eq(CateringCartShareBillEntity::getType, type);
        if (menuIds != null && menuIds.size() > 0) {
            queryWrapper.in(CateringCartShareBillEntity::getMenuId, menuIds);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void autoCleanBillData(String shareBillNo) {
        LambdaUpdateWrapper<CateringCartShareBillEntity> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(CateringCartShareBillEntity::getShareBillNo, shareBillNo);
        baseMapper.delete(updateWrapper);
        // 删除用户信息
        shareBillUserService.deleteByShareBillNo(shareBillNo, null);
    }


    @Override
    public CateringCartShareBillEntity notPayShareBillByMerchantAndUser(Long merchantId, Long shopId, Long userId, int type) {
        LambdaQueryWrapper<CateringCartShareBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(merchantId != null, CateringCartShareBillEntity::getMerchantId, merchantId)
                .eq(shopId != null, CateringCartShareBillEntity::getShopId, shopId)
                .eq(CateringCartShareBillEntity::getShareUserId, userId)
                .eq(CateringCartShareBillEntity::getType, type)
                .ne(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYED.getStatus());

        queryWrapper.orderByDesc(CateringCartShareBillEntity::getShareTime);
        List<CateringCartShareBillEntity> list = this.list(queryWrapper);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Boolean shareBillCreateOrder(Long orderId, String shareBillNo) {

        LambdaUpdateWrapper<CateringCartShareBillEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYED.getStatus())
                .set(CateringCartShareBillEntity::getOrderId, orderId)
                .eq(CateringCartShareBillEntity::getShareBillNo, shareBillNo)
                .eq(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYING.getStatus());

        return update(wrapper);
    }

    @Override
    public Boolean settleShareBill(String shareBillNo) {
        LambdaUpdateWrapper<CateringCartShareBillEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYING.getStatus())
                .eq(CateringCartShareBillEntity::getShareBillNo, shareBillNo)
                .eq(CateringCartShareBillEntity::getStatus, BillStatusEnum.CHOOSING.getStatus());
        return update(wrapper);
    }

    @Override
    public Boolean returnChooseGoods(String shareBillNo) {
        LambdaUpdateWrapper<CateringCartShareBillEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CateringCartShareBillEntity::getStatus, BillStatusEnum.CHOOSING.getStatus())
                .eq(CateringCartShareBillEntity::getShareBillNo, shareBillNo)
                .eq(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYING.getStatus());
        return update(wrapper);
    }

    private Boolean addUser(String shareBillNo, long userId, String avatar, String nickname) {
        if (shareBillUserService.getUser(shareBillNo, userId) == null) {
            CateringCartShareBillUserEntity user = new CateringCartShareBillUserEntity();
            user.setUserId(userId);
            user.setShareBillNo(shareBillNo);
            user.setAvatar(avatar);
            user.setNickname(StringUtils.isNotBlank(nickname) ? nickname : "");
            user.setBillStatus(BillStatusEnum.CHOOSING.getStatus());
            return shareBillUserService.save(user);
        }
        return true;

    }

    private CateringCartShareBillEntity getShareBillEntityByNo(String shareBillNo) {
        LambdaQueryWrapper<CateringCartShareBillEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(CateringCartShareBillEntity::getShareBillNo, shareBillNo);
        return baseMapper.selectOne(wrapper);
    }


    @Override
    public String getNotPayShareBill(Long merchantId, Long shopId, Long userId) {

        List<CateringCartShareBillEntity> list = list(queryWrapper(userId, merchantId, shopId));
        if (list != null && list.size() > 0) {
            return list.get(0).getShareBillNo();
        }
        return null;
    }

    @Override
    public CartShareBillBaseDTO getShareBillInfo(Long userId, Long merchantId, Long shopId) {
        List<CateringCartShareBillEntity> list = list(queryWrapper(userId, merchantId, shopId));
        if (list.size() > 0) {
            CartShareBillBaseDTO billBaseDTO = new CartShareBillBaseDTO();
            CateringCartShareBillEntity entity = list.get(0);
            billBaseDTO.setShareBillNo(entity.getShareBillNo());
            billBaseDTO.setShareUserId(entity.getShareUserId());
            return billBaseDTO;
        }
        return null;
    }

    private LambdaQueryWrapper<CateringCartShareBillEntity> queryWrapper(Long userId, Long merchantId, Long shopId) {
        LambdaQueryWrapper<CateringCartShareBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(merchantId != null, CateringCartShareBillEntity::getMerchantId, merchantId)
                .eq(shopId != null, CateringCartShareBillEntity::getShopId, shopId)
                .eq(CateringCartShareBillEntity::getShareUserId, userId)
                .ne(CateringCartShareBillEntity::getStatus, BillStatusEnum.PAYED.getStatus())
                .orderByDesc(CateringCartShareBillEntity::getShareTime);
        return queryWrapper;
    }

    @Override
    public Long judgeShareBillStatus130(String shareBillNo, Long userId) {
        CateringCartShareBillEntity billEntity = getShareBillEntityByNo(shareBillNo);

        if (billEntity == null) {
            throw new CustomException(ErrorCode.SHARE_BILL_FINISHED_ERROR, "来晚啦！！！拼单已经结束咯~~~~");
        }

        Integer status = billEntity.getStatus();
        if (Objects.equals(userId, billEntity.getShareUserId())) {
            if (Objects.equals(status, BillStatusEnum.PAYING.getStatus())) {
                throw new CustomException(ErrorCode.SHARE_BILL_PAYING_ERROR, "拼单还在结算中，点击去结算吧~~~~~");
            }
            if (Objects.equals(status, BillStatusEnum.PAYED.getStatus())) {
                throw new CustomException(ErrorCode.SHARE_BILL_PAYED_ERROR, "拼单已经提交咯，点击查看详情吧~~~~~", billEntity.getOrderId().toString());
            }
        } else {
            List<CateringCartShareBillUserEntity> entities = shareBillUserService.list(shareBillNo);

            //不是选购中的拼单
            if (!Objects.equals(status, BillStatusEnum.CHOOSING.getStatus())) {
                //如果当前用户 没有参与拼单,点击返回到商家的点餐页面，进行个人点餐
                //判断所有参与拼单人中是否有当前用户id

                if (entities != null && entities.size() > 0) {
                    List<Long> collect = entities.stream().map(CateringCartShareBillUserEntity::getUserId).collect(Collectors.toList());
                    if (!collect.contains(userId)) {
                        throw new CustomException(ErrorCode.CAN_NOT_CONTINUE_SHARE_ERROR, "拼单已经结束啦~");
                    } else {
                        if (!cartService.isChooseGoods(userId, shareBillNo)) {
                            throw new CustomException(ErrorCode.CAN_NOT_CONTINUE_SHARE_ERROR, "拼单已经结束啦~");
                        }
                    }
                } else {
                    throw new CustomException(ErrorCode.CAN_NOT_CONTINUE_SHARE_ERROR, "拼单已经结束啦~");
                }
            }
            if (Objects.equals(status, BillStatusEnum.PAYING.getStatus())) {
                throw new CustomException(ErrorCode.SHARE_BILL_PAYING_BILL_USER_ERROR, "好友在结算咯~~~~~");
            }
            if (Objects.equals(status, BillStatusEnum.PAYED.getStatus())) {
                throw new CustomException(ErrorCode.SHARE_BILL_PAYED_BILL_USER_ERROR, "好友已经提交拼单咯~~~~~");
            }
        }
        return billEntity.getShareUserId();
    }
}
