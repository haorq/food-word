package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.constant.UserWxXinSecretConstant;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.UserLevelEnum;
import com.meiyuan.catering.core.enums.base.UserSubscribeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.user.dao.CateringUserMapper;
import com.meiyuan.catering.user.dto.user.*;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.entity.CateringUserGroundPusherEntity;
import com.meiyuan.catering.user.enums.PullNewUserEnum;
import com.meiyuan.catering.user.enums.UserStatusEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.sender.UserIdSendMq;
import com.meiyuan.catering.user.service.CateringUserCompanyService;
import com.meiyuan.catering.user.service.CateringUserGroundPusherService;
import com.meiyuan.catering.user.service.CateringUserService;
import com.meiyuan.catering.user.vo.user.UserDetailInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表(CateringUser)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringUserService")
public class CateringUserServiceImpl extends ServiceImpl<CateringUserMapper, CateringUserEntity> implements CateringUserService {
    @Resource
    private UserIdSendMq userSenderMq;
    @Resource
    private CateringUserCompanyService companyService;
    @Resource
    private CateringUserGroundPusherService groundPusherService;

    /**
     * zzn 通过用户名/手机号 查询id集合
     *
     * @param keyword 用户名/手机号
     * @return ids
     */
    @Override
    public List<Long> getIdsByKeyword(String keyword) {
        List<Long> userIds = null;
        if (StringUtils.isNotBlank(keyword)) {
            userIds = this.baseMapper.selectIdsByKeyword(keyword);
            List<Long> companyIds = companyService.getIdsByKeyword(keyword);
            userIds.addAll(companyIds);
        }
        return userIds;
    }

    @Override
    public UserDetailInfoVo queryUserById(Long id) {
        LambdaQueryWrapper<CateringUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserEntity::getId, id);
        CateringUserEntity one = this.getOne(queryWrapper);
        return ConvertUtils.sourceToTarget(one, UserDetailInfoVo.class);
    }

    @Override
    public void sendUserMq(UserIdDTO userIdDTO) {
        userSenderMq.sendUserMq(userIdDTO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long id) {
        LambdaQueryWrapper<CateringUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserEntity::getDel, false);
        queryWrapper.eq(CateringUserEntity::getUserCompanyId, id);
        CateringUserEntity userEntity = getOne(queryWrapper);
        userEntity.setUserCompanyId(null);
        userEntity.setUserType(UserTypeEnum.PERSONAL.getStatus());
        return updateById(userEntity);
    }

    @Override
    public User queryByUserId(String account) {
        CateringUserEntity userEntity = this.baseMapper.queryByUserId(account);
        return BaseUtil.objToObj(userEntity, User.class);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CateringUserEntity addNewUser(UserSaveDTO dto) {
        String phone = dto.getPhone();
        if (!VerifyUtil.verifyPhone(phone)) {
            log.error("新增用户手机号格式错误：" + phone);
            throw new CustomException("抱歉！系统无法识别您的手机号！");
        }
        String openId = dto.getOpenId();
        if (BaseUtil.isEmptyStr(openId)) {
            throw new CustomException("抱歉！无法获取您的微信用户标识！");
        }
        String unionId = dto.getUnionId();
        if (BaseUtil.isEmptyStr(unionId)) {
            throw new CustomException("抱歉！无法获取您的公共用户标识！");
        }
        CateringUserEntity entity = ConvertUtils.sourceToTarget(dto, CateringUserEntity.class);

        Long groundPusherId = dto.getGroundPusherId();
        Long referrerId = dto.getReferrerId();
        String shareFlag = dto.getShareFlag();
        // 推荐有奖或是分享小程序
        if(Objects.nonNull(referrerId) || !BaseUtil.isEmptyStr(shareFlag)){
            entity.setPullNewUser(PullNewUserEnum.INVITED.getStatus());
        }else if(Objects.nonNull(groundPusherId)){
            entity.setPullNewUser(PullNewUserEnum.PUSH.getStatus());
        }

        entity.setNickname(dto.getNickName());
        entity.setUserName(dto.getNickName());

        String country = dto.getCountry();
        String province = dto.getProvince();
        String city = dto.getCity();

        country = BaseUtil.isEmptyStr(country) ? "" : country;
        province = BaseUtil.isEmptyStr(province) ? "" : province;
        city = BaseUtil.isEmptyStr(city) ? "" : city;

        String area = country + province + city;
        entity.setArea(area);
        entity.setStatus(StatusEnum.ENABLE.getStatus());
        entity.setUserType(UserTypeEnum.PERSONAL.getStatus());
        entity.setRegisterIp(HttpContextUtils.getRealIp(HttpContextUtils.getHttpServletRequest()));
        entity.setDel(DelEnum.NOT_DELETE.getFlag());
        entity.setUserLevel(UserLevelEnum.ORDINARY.getStatus());
        entity.setFirstPhone(dto.getPhone());
        entity.setAvatar(dto.getAvatar());

        Long userId = IdWorker.getId();
        entity.setId(userId);
        entity.setCreateBy(userId);
        entity.setUpdateBy(userId);
        boolean saveBool = this.save(entity);
        if (!saveBool) {
            throw new CustomException("网络跑掉了，注册失败！");
        }
        return entity;
    }

    @Override
    public UserCompanyInfo getUcInfo(Long uidOrCid) {
        return baseMapper.getUcInfo(uidOrCid);
    }

    @Override
    public UserCompanyInfo getFuzzyUcInfo(Long uidOrCid) {
        CateringUserEntity userEntity = getById(uidOrCid);
        if (userEntity != null) {
            return new UserCompanyInfo(uidOrCid, userEntity.getUserName(), userEntity.getPhone(), UserTypeEnum.PERSONAL.getStatus());
        }
        CateringUserCompanyEntity entity = companyService.getById(uidOrCid);
        if (entity != null) {
            return new UserCompanyInfo(uidOrCid, entity.getCompanyName(), entity.getAccount(), UserTypeEnum.COMPANY.getStatus());
        }
        return null;
    }

    @Override
    public UserCompanyInfo getUcInfoFill(Long uidOrCid) {
        UserCompanyInfo ucInfo = getUcInfo(uidOrCid);
        if (ucInfo != null && ucInfo.getUserType().equals(UserTypeEnum.COMPANY.getStatus())) {
            CateringUserCompanyEntity company = companyService.getById(uidOrCid);
            ucInfo.setId(company.getId());
            ucInfo.setName(company.getCompanyName());
            ucInfo.setPhone(company.getAccount());
        }
        return ucInfo;
    }

    @Override
    public List<UserCompanyInfo> getUcInfoList(List<Long> uidOrCids) {
        List<UserCompanyInfo> listUcInfo = baseMapper.getListUcInfo(uidOrCids);
        List<UserCompanyInfo> companyInfos = companyService.getListUcInfo(uidOrCids);
        listUcInfo.addAll(companyInfos);
        return listUcInfo;
    }

    @Override
    public Map<Long, UserCompanyInfo> getUcInfoMap(List<Long> uidOrCids) {
        List<UserCompanyInfo> ucInfoList = getUcInfoList(uidOrCids);
        return ucInfoList.stream().collect(Collectors.toMap(UserCompanyInfo::getId, info -> info));
    }

    @Override
    public CateringUserEntity queryUser(String phone, String openId) {
        QueryWrapper<CateringUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringUserEntity::getPhone, phone);
        CateringUserEntity entity = this.getOne(queryWrapper);
        if (entity == null || BaseUtil.isEmptyStr(openId)) {
            return entity;
        }
        if (openId.equals(entity.getOpenId())) {
            return entity;
        }
        throw new CustomException("手机号已有用户使用,请更换手机号！");
    }

    @Override
    public CateringUserEntity queryByOpenId(String openId, Integer status) {
        QueryWrapper<CateringUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringUserEntity::getOpenId, openId)
                .eq(CateringUserEntity::getStatus, status);
        return this.getOne(queryWrapper, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String userFocusOn(UserFollowDTO dto, String accessToken) {
        String eventKey = dto.getEventKey();
        if(BaseUtil.isEmptyStr(eventKey)){
            return "";
        }
        String openId = dto.getFromUserName();
        if (BaseUtil.isEmptyStr(openId)) {
            throw new CustomException("抱歉！无权获取您的公众号标识！");
        }
        String idStr = eventKey.replace("qrscene_","").trim();
        Long groundPusherId = Long.valueOf(idStr);
        log.debug(openId + " 关注了：" + eventKey + " 地推员！");
        if (Boolean.FALSE.equals(this.isStatus(groundPusherId))) {
            throw new CustomException("地推员已被禁用！");
        }
        // 获取用户unionId
        Map<String, String> unionIdMap = new HashMap<>();
        unionIdMap.put("access_token", accessToken);
        unionIdMap.put("lang", UserWxXinSecretConstant.LANG);
        unionIdMap.put("openid", openId);
        String result = HttpUtil.sendGet(UserWxXinSecretConstant.PUBLIC_USER_IN_FO_URL, unionIdMap);
        UserPublicNumberDTO userPublicNumber = UserPublicNumberDTO.fromJson(result);
        if (userPublicNumber == null) {
            throw new CustomException("抱歉！无权获取您的公众号用户信息！");
        }
        if (UserSubscribeEnum.NOT_FOCUS_ON.getStatus().equals(userPublicNumber.getSubscribe())) {
            return openId;
        }
        String unionId = userPublicNumber.getUnionid();
        if (BaseUtil.isEmptyStr(unionId)) {
            throw new CustomException("抱歉！无权获取您的平台用户标识！");
        }
        CateringUserEntity userEntity = this.queryByUnionId(unionId);
        if (userEntity == null) {
            userEntity = new CateringUserEntity();
            userEntity.setStatus(StatusEnum.ENABLE.getStatus());
            userEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        }
        // 已建立关系
        if(userEntity.getGroundPusherId() != null){
            return "";
        }
        String nickname = userPublicNumber.getNickname();

        userEntity.setGroundPusherId(groundPusherId);
        userEntity.setUnionId(unionId);
        userEntity.setGender(userPublicNumber.getSex());
        userEntity.setUserName(nickname);
        userEntity.setNickname(nickname);

        if (null == userEntity.getId()) {
            Long userId = IdWorker.getId();
            userEntity.setId(userId);
            userEntity.setCreateBy(userId);
            userEntity.setUpdateBy(userId);
            this.save(userEntity);
        } else {
            this.updateById(userEntity);
        }
        return openId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserByUnionId(String unionId, Long userId) {
        if (BaseUtil.isEmptyStr(unionId)) {
            return false;
        }
        CateringUserEntity entity = this.getById(userId);
        if (entity == null) {
            return false;
        }
        entity.setUnionId(unionId);
        CateringUserEntity unionIdEntity = this.queryByUnionId(unionId);
        if (unionIdEntity == null) {
            entity.setPullNewUser(PullNewUserEnum.NON_EXISTENT.getStatus());
        } else {
            entity.setPullNewUser(PullNewUserEnum.PUSH.getStatus());
            entity.setGroundPusherId(unionIdEntity.getGroundPusherId());
            this.removeById(unionIdEntity.getId());
        }
        boolean bool = this.updateById(entity);
        if (!bool || PullNewUserEnum.NON_EXISTENT.getStatus().equals(entity.getPullNewUser())) {
            return bool;
        }
        UserIdDTO userIdMq = new UserIdDTO();
        userIdMq.setGroundPusherId(entity.getGroundPusherId());
        userIdMq.setGroundUserId(entity.getId());
        this.sendUserMq(userIdMq);
        return true;
    }

    @Override
    public Long getUserIdByPhone(String phone) {

        return this.baseMapper.getUserIdByPhone(phone);
    }

    /**
     * describe: 判断是否存在地推员
     *
     * @param groundPushId
     * @author: yy
     * @date: 2020/9/8 9:54
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    private Boolean isStatus(Long groundPushId) {
        if (groundPushId == null) {
            return false;
        }
        QueryWrapper<CateringUserGroundPusherEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringUserGroundPusherEntity::getId, groundPushId)
                .eq(CateringUserGroundPusherEntity::getPusherStatus, StatusEnum.ENABLE.getStatus());
        CateringUserGroundPusherEntity byId = groundPusherService.getOne(queryWrapper);
        return !ObjectUtils.isEmpty(byId);
    }

    /**
     * describe: 根据unionId查询用户信息
     *
     * @param unionId
     * @author: yy
     * @date: 2020/9/8 9:54
     * @return: {@link CateringUserEntity}
     * @version 1.4.0
     **/
    private CateringUserEntity queryByUnionId(String unionId) {
        if (BaseUtil.isEmptyStr(unionId)) {
            return null;
        }
        QueryWrapper<CateringUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringUserEntity::getUnionId, unionId)
                .eq(CateringUserEntity::getStatus, UserStatusEnum.NORMAL.getStatus());
        return this.baseMapper.selectOne(queryWrapper);
    }
}
