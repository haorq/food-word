package com.meiyuan.catering.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.finance.dao.CateringUserBalanceAccountMapper;
import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountEntity;
import com.meiyuan.catering.finance.query.recharge.UserBalanceAccountQueryDTO;
import com.meiyuan.catering.finance.service.CateringUserBalanceAccountRecordService;
import com.meiyuan.catering.finance.service.CateringUserBalanceAccountService;
import com.meiyuan.catering.finance.vo.account.UserAccountDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO;
import com.meiyuan.catering.user.fegin.user.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
@Slf4j
public class CateringUserBalanceAccountServiceImpl extends ServiceImpl<CateringUserBalanceAccountMapper, CateringUserBalanceAccountEntity> implements CateringUserBalanceAccountService {

    @Resource
    private EncryptPasswordProperties encryptPasswordProperties;
    @Resource
    private CateringUserBalanceAccountRecordService recordService;
    @Autowired
    private UserClient userClient;

    @Override
    public PageData<UserBalanceAccountQueryListVO> pageList(UserBalanceAccountQueryDTO dto) {
        String keyword = CharUtil.disposeChar(dto.getKeyword());

        List<Long> ids = ClientUtil.getDate(userClient.getIdsByKeyword(keyword));
        if (ids != null && ids.size() == 0) {
            return null;
        }

        //查询
        LambdaQueryWrapper<CateringUserBalanceAccountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CollectionUtils.isNotEmpty(ids), CateringUserBalanceAccountEntity::getUserId, ids)
                .eq(dto.getUserType() != null, CateringUserBalanceAccountEntity::getUserType, dto.getUserType())
                .orderByDesc(CateringUserBalanceAccountEntity::getCreateTime);

        IPage<CateringUserBalanceAccountEntity> page = baseMapper.selectPage(dto.getPage(), wrapper);

        //处理数据
        if (page != null && page.getRecords().size() > 0) {

            List<CateringUserBalanceAccountEntity> records = page.getRecords();

            List<Long> uidOrCids = records.stream().map(CateringUserBalanceAccountEntity::getUserId).collect(Collectors.toList());
            Map<Long, UserCompanyInfo> ucInfoMap = ClientUtil.getDate(userClient.getUcInfoMap(uidOrCids));

            List<UserBalanceAccountQueryListVO> vos = records.stream().map(e -> {
                UserBalanceAccountQueryListVO vo = BaseUtil.objToObj(e, UserBalanceAccountQueryListVO.class);
                UserCompanyInfo info = ucInfoMap.get(e.getUserId());
                if (info != null) {
                    vo.setUserName(info.getName());
                    vo.setUserPhone(info.getPhone());
                }
                return vo;
            }).collect(Collectors.toList());

            return new PageData<>(vos, page.getTotal());
        }
        return null;
    }

    @Override
    public BalanceAccountInfo userAccountInfo(Long userId) {
        LambdaQueryWrapper<CateringUserBalanceAccountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserBalanceAccountEntity::getUserId, userId);
        return BaseUtil.objToObj(baseMapper.selectOne(wrapper), BalanceAccountInfo.class);
    }

    public CateringUserBalanceAccountEntity infoByUserId(Long userId) {
        LambdaQueryWrapper<CateringUserBalanceAccountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringUserBalanceAccountEntity::getUserId, userId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Boolean createAccount(Long userId, Integer userType) {
        log.debug("id-{},type:{},用户创建账户开始", userId, userType);
        CateringUserBalanceAccountEntity info = infoByUserId(userId);
        //账户不存在新建账号 已存在忽略
        if (info == null) {
            log.debug("创建新账户..");
            CateringUserBalanceAccountEntity entity = new CateringUserBalanceAccountEntity();
            entity.setUserId(userId);
            entity.setFrozenAmount(BigDecimal.ZERO);
            entity.setBalance(BigDecimal.ZERO);
            entity.setTotalRealAmount(BigDecimal.ZERO);
            entity.setTotalDiscountAmount(BigDecimal.ZERO);
            entity.setTotalCouponAmount(BigDecimal.ZERO);
            entity.setUserType(userType);
            return save(entity);
        }
        return true;
    }

    @Override
    public Boolean updatePayPassword(Long userId, String password) {
        //解密密码
        String oldDesEnc = AesEncryptUtil.desEncrypt(password, encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());
        //加密密码
        password = Md5Util.payPassword(oldDesEnc);
        return baseMapper.updatePayPassword(userId, password);
    }

    @Override
    public Boolean balanceAdd(Long userId, BigDecimal refundAmount) {
        return baseMapper.balanceAdd(userId, refundAmount, null, null);
    }

    @Override
    public Boolean balanceAdd(Long userId, BigDecimal amount, BigDecimal totalRealAmount, BigDecimal totalCouponAmount) {
        return baseMapper.balanceAdd(userId, amount, totalRealAmount, totalCouponAmount);
    }

    @Override
    public Boolean balanceSubtract(Long userId, BigDecimal payAmount) {
        return baseMapper.balanceSubtract(userId, payAmount);
    }

    @Override
    public Boolean systemBalanceAdd(Long userId, BigDecimal amount) {
        return baseMapper.balanceAdd(userId, amount, amount, null);
    }

    @Override
    public BigDecimal balance(Long userId) {
        CateringUserBalanceAccountEntity accountEntity = this.infoByUserId(userId);
        if (accountEntity == null) {
            return BigDecimal.ZERO;
        }
        return accountEntity.getBalance() == null ? BigDecimal.ZERO : accountEntity.getBalance();
    }

    @Override
    public UserBalanceAccountQueryDetailVO getBalanceAccountDetailById(Long id) {
        CateringUserBalanceAccountEntity accountEntity = baseMapper.selectById(id);
        UserBalanceAccountQueryDetailVO vo = BaseUtil.objToObj(accountEntity, UserBalanceAccountQueryDetailVO.class);
        UserCompanyInfo info = ClientUtil.getDate(userClient.getUcInfoFill(vo.getUserId()));
        if (info != null) {
            vo.setExpendVO(recordService.consumeRefundByUserId(info.getId()));
            vo.setUserName(info.getName());
            vo.setUserPhone(info.getPhone());
        }
        return vo;
    }

    @Override
    public UserAccountDetailVO accountDetail(Long userId, BasePageDTO pageDTO) {
        BalanceAccountInfo accountEntity = userAccountInfo(userId);
        UserAccountDetailVO vo = new UserAccountDetailVO();
        vo.setBalance(accountEntity.getBalance());
        vo.setIncomeExpend(recordService.incomeExpendByUserId(userId));
        vo.setRecords(recordService.queryAccountRecordList(userId, pageDTO));
        return vo;
    }
}

