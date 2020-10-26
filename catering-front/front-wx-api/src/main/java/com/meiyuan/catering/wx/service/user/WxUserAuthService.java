package com.meiyuan.catering.wx.service.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.config.SmsProperties;
import com.meiyuan.catering.core.config.WxProperties;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyRestrictService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.notify.RestrictConstant;
import com.meiyuan.catering.core.notify.RestrictNumEnum;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.finance.feign.UserBalanceAccountClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.pay.service.MyMemberService;
import com.meiyuan.catering.user.dto.feedback.FeedbackAddDTO;
import com.meiyuan.catering.user.dto.feedback.UserFeedbackDTO;
import com.meiyuan.catering.user.dto.user.*;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.enums.UserSexEnum;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.feedback.UserFeedbackClient;
import com.meiyuan.catering.user.fegin.integral.IntegralRecordClient;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.user.fegin.user.UserCompanyClient;
import com.meiyuan.catering.user.fegin.user.UserGroundPusherClient;
import com.meiyuan.catering.user.query.user.UserCompanyBindDTO;
import com.meiyuan.catering.user.vo.user.*;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.user.SendPhoneCodeDTO;
import com.meiyuan.catering.wx.service.common.WxContentCheckService;
import com.meiyuan.catering.wx.utils.OldSystemUtil;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.catering.wx.vo.WxSignatureResultVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author lhm
 * @date 2020/3/23 11:33
 **/
@Service
@Slf4j
public class WxUserAuthService {

    @Resource
    private UserClient userClient;
    @Resource
    private UserCompanyClient userCompanyClient;
    @Resource
    private UserFeedbackClient userFeedbackClient;
    @Resource
    private UserBalanceAccountClient userBalanceAccountClient;
    @Resource
    private IntegralRecordClient integralRecordClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @Autowired
    private WxContentCheckService contentCheckService;
    @Autowired
    private NotifyRestrictService restrictService;
    @Resource
    private WxMaService wxService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxProperties properties;
    @Resource
    private WechatUtils wechatUtils;
    @Resource
    private EncryptPasswordProperties encryptPasswordProperties;
    @Resource
    private OldSystemUtil oldSystemUtil;
    @Resource
    private SmsProperties smsProperties;
    @Resource
    private UserGroundPusherClient pusherClient;
    @Resource
    private MyMemberService myMemberService;

    /**
     * 我的页面--个人信息展示
     *
     * @param tokenDTO
     * @return
     */
    public Result<UserDetailInfoVo> getUserDetailInfo(UserTokenDTO tokenDTO) {
        UserTokenDTO dto = wechatUtils.getUser(tokenDTO.getToken());
        UserDetailInfoVo infoVo = userClient.queryUserById(dto.getUserIdReal()).getData();
        //查询企业信息
        if (dto.getUserType().equals(UserTypeEnum.COMPANY.getStatus())) {
            infoVo.setCompanyName(userCompanyClient.getById(dto.getUserCompanyId()).getData());
        }
        infoVo.setUserType(dto.getUserType());
        return Result.succ(infoVo);
    }

    /**
     * 我的页面--企业信息展示
     *
     * @param
     * @return
     */
    public Result<UserCompanyDetailInfoVo> getUserCompanyDetail(Long userCompanyId) {
        return userCompanyClient.getUserCompanyDetail(userCompanyId);
    }

    /**
     * 绑定企业用户
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> bindUserCompany(String token, UserCompanyBindDTO dto) {
        //1.查询企业用户是否存在
        LambdaQueryWrapper<CateringUserCompanyEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringUserCompanyEntity::getIsDel, false);
        queryWrapper.eq(CateringUserCompanyEntity::getAccount, dto.getAccount());
        UserCompanyDTO userCompany = userCompanyClient.getUserCompany(queryWrapper);
        if (null == userCompany) {
            throw new CustomException("该企业账号不存在");
        }
        if (userCompany.getCompanyStatus().equals(StatusEnum.ENABLE_NOT.getStatus())) {
            throw new CustomException("该企业账号已禁用，请联系管理员哦");
        }
        User data = userClient.queryByAccount(dto.getAccount()).getData();
        if (!ObjectUtils.isEmpty(data)) {
            throw new CustomException("该企业账号已被其他用户绑定啦~");
        }
        //2.验证密码
        String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());
        if (!Md5Util.passwordEncrypt(desEncrypt).equals(userCompany.getPassword())) {
            throw new CustomException("账号或密码错误，请重新输入");
        }

        //3.绑定企业id 修改 user_type
        UserTokenDTO tokenDTO = wechatUtils.getUser(token);
        User user = userClient.queryById(tokenDTO.getUserIdReal()).getData();
        user.setUserCompanyId(userCompany.getId());
        user.setUserType(UserTypeEnum.COMPANY.getStatus());
        Boolean flag = userClient.updateUser(user).getData();

        //4.将企业用户信息  缓存
        tokenDTO.setUserType(UserTypeEnum.COMPANY.getStatus());
        tokenDTO.setUserCompanyId(userCompany.getId());
        wechatUtils.saveTokenInfo(tokenDTO);
        wechatUtils.putUserType(tokenDTO.getOpenId(), tokenDTO.getUserType());
        userBalanceAccountClient.createAccount(userCompany.getId(), UserTypeEnum.COMPANY.getStatus());

        // 注册个人会员-通联云支付
        registerPersonalMember(userCompany.getId(), user.getOpenId());

        return Result.succ(flag);
    }

    private void registerPersonalMember(Long id, String openId) {
        try {
            myMemberService.createPersonalMember(id);
        } catch (AllinpayException e) {
            log.error("msg:{}", e.getMessage());
        }
        try {
            myMemberService.applyBindMiniAcct(id, openId);
        } catch (AllinpayException e) {
            log.error("msg:{}", e.getMessage());
        }

    }


    /**
     * 解绑企业用户
     *
     * @param token
     * @return
     */

    public Result<Boolean> unBindUserCompany(String token) {
        // 更新数据库信息
        UserTokenDTO tokenDTO = wechatUtils.getUser(token);
        Boolean data = userClient.unBindUserCompany(tokenDTO.getUserCompanyId()).getData();
        //更新redis 信息
        tokenDTO.setUserCompanyId(null);
        tokenDTO.setUserType(UserTypeEnum.PERSONAL.getStatus());
        wechatUtils.saveTokenInfo(tokenDTO);
        wechatUtils.removeUserType(tokenDTO.getOpenId());
        return Result.succ(data);
    }


    /**
     * 我的---建议反馈
     *
     * @param user
     * @param dto
     * @return
     */
    public Result<Boolean> userFeedback(UserTokenDTO user, FeedbackAddDTO dto) {
        Result<String> result = this.contentCheckService.checkText(dto.getUserFeedback());
        if (result.failure()) {
            log.error("评论内容中有敏感词汇 content=【{}】", dto.getUserFeedback());
            throw new CustomException("评论内容不能包含敏感词汇");
        }
        UserFeedbackDTO feedbackEntity = new UserFeedbackDTO();
        BeanUtils.copyProperties(dto, feedbackEntity);
        feedbackEntity.setUserId(user.getUserId());
        feedbackEntity.setUserName(user.getNickname());
        feedbackEntity.setDel(false);
        feedbackEntity.setCreateTime(LocalDateTime.now());
        if (StringUtils.isEmpty(dto.getPhone())) {
            feedbackEntity.setPhone(user.getPhone());
        } else {
            feedbackEntity.setPhone(dto.getPhone());
        }
        return userFeedbackClient.save(feedbackEntity);
    }

    public Result<UserTokenDTO> switchUserType(UserTokenDTO userToken) {
        Integer userType = userToken.getUserType();
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            userToken.setUserType(UserTypeEnum.COMPANY.getStatus());
        } else {
            userToken.setUserType(UserTypeEnum.PERSONAL.getStatus());
        }
        wechatUtils.saveTokenInfo(userToken);
        wechatUtils.putUserType(userToken.getOpenId(), userToken.getUserType());
        return Result.succ();
    }

    /**
     * c端---我的首页
     *
     * @param user
     * @return
     */
    public Result<UserIndexVo> userIndex(UserTokenDTO user) {
        UserIndexVo indexVo = ConvertUtils.sourceToTarget(user, UserIndexVo.class);
        indexVo.setIntegral(integralRecordClient.sumByUserId(user.getUserId()).getData());
        indexVo.setTicket(getTotal(user.getUserId()));
        indexVo.setCompanyId(user.getUserCompanyId());
        log.info("indexVo:{}", JSON.toJSONString(indexVo));
        return Result.succ(indexVo);
    }


    private Integer getTotal(Long userId) {
        return userTicketClient.pageMyTicket(1L, 10L, 1, userId, null).getData().getTotal();
    }


    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public Result<Boolean> sendCode(String phone) {
        String code;
        if (Boolean.TRUE.equals(smsProperties.getCode())) {
            code = CodeGenerator.randomCode(6);
            //发送验证码
            restrictService.notifySmsTemplate(phone, NotifyType.VERIFY_CODE_NOTIFY, RestrictNumEnum.RESET_PAY_PWD, RestrictConstant.WX, code);
        } else {
            code = smsProperties.getDefaultCode();
        }
        log.debug("发送手机验证码:phone={},code={}", phone, code);
        //保存验证码
        wechatUtils.saveSmsAuthCode(phone, code);
        return Result.succ();
    }

    /**
     * 验证验证码
     *
     * @param dto
     * @return
     */
    public Result<Boolean> verifyCode(UserTokenDTO tokenDTO, SendPhoneCodeDTO dto) {
        String phone = dto.getPhone();
        String authCode = wechatUtils.getSmsAuthCode(phone);
        boolean equals = Objects.equals(dto.getCode(), authCode);
        if (equals) {
            //验证成功 删除验证码
            wechatUtils.removeSmsAuthCode(phone);
            updateUserPhone(tokenDTO, phone);
        }
        return equals ? Result.succ() : Result.fail("验证码错误，请重新输入");
    }


    /**
     * 用户首次登陆获取设置手机号
     *
     * @param tokenDTO
     * @param phone
     */
    public void updateUserPhone(UserTokenDTO tokenDTO, String phone) {
        User data = userClient.queryById(tokenDTO.getUserIdReal()).getData();
        data.setFirstPhone(phone);
        data.setPhone(phone);
        userClient.updateUser(data);
        tokenDTO.setPhone(phone);
        wechatUtils.saveTokenInfo(tokenDTO);
    }


    /**
     * 实时获取用户信息
     *
     * @param tokenDTO
     * @return
     */
    public Result<UserDetailInfoVo> getUserInfo(UserTokenDTO tokenDTO) {
        UserTokenDTO user = wechatUtils.getUser(tokenDTO.getToken());
        UserDetailInfoVo infoVo = ConvertUtils.sourceToTarget(user, UserDetailInfoVo.class);
        infoVo.setId(user.getUserId());
        infoVo.setNickName(user.getNickname());
        infoVo.setUserName(user.getNickname());
        BalanceAccountInfo info = null;
        if (user.getUserType().equals(UserTypeEnum.PERSONAL.getStatus())) {
            info = userBalanceAccountClient.userAccountInfo(user.getUserId()).getData();
        } else {
            info = userBalanceAccountClient.userAccountInfo(user.getUserCompanyId()).getData();
        }
        if (info == null || BaseUtil.isEmptyStr(info.getPassword())) {
            infoVo.setIsPayPassWord(false);
        } else {
            infoVo.setIsPayPassWord(true);
        }
        return Result.succ(infoVo);
    }


    /**
     * 处理老系统数据
     */
    public void disposeOldSystemInfo(UserTokenDTO tokenDTO, String phone) {
        oldSystemUtil.disposeOldSystemInfo(tokenDTO, phone);
    }


    /**
     * 判断地推员是否被禁用
     *
     * @param groundPushId
     * @return
     */
    private Boolean isStatus(Long groundPushId) {
        if (null == groundPushId) {
            return false;
        }
        PusherDetailsVo byId = pusherClient.getById(groundPushId).getData();
        if (ObjectUtils.isEmpty(byId)) {
            return false;
        }
        boolean flag = false;
        if (byId.getPusherStatus().equals(StatusEnum.ENABLE.getStatus())) {
            flag = true;
        }
        return flag;
    }

    /**
     * describe: 立即领取-老用户发券不建立关系
     *
     * @param tokenDTO
     * @param groundPusherId
     * @return: {@link Result< Boolean>}
     **/
    public Result<Boolean> isNewUser(UserTokenDTO tokenDTO, Long groundPusherId) {
        if (ObjectUtils.isEmpty(tokenDTO)) {
            return Result.succ(false);
        }
        // 禁用
        if (Boolean.FALSE.equals(this.isStatus(groundPusherId))) {
            return Result.succ(true);
        }
        User user = userClient.queryById(tokenDTO.getUserId()).getData();
        if (ObjectUtils.isEmpty(user)) {
            Result.succ(true);
        }
        if (UserTypeEnum.COMPANY.getStatus().equals(tokenDTO.getUserType())) {
            return Result.succ(true);
        }
        UserIdDTO userIdDTO = new UserIdDTO();
        //老用户
        userIdDTO.setGroundUserId(user.getId());
        //避免前端获取不到地推员id
        userIdDTO.setGroundPusherId(groundPusherId);
        userIdDTO.setPhone(tokenDTO.getPhone());
        tokenDTO.setGroundPusherId(groundPusherId);
        wechatUtils.saveTokenInfo(tokenDTO);
        log.info("老用户领地推员券消息 dto:{}", userIdDTO);
        userClient.sendUserMq(userIdDTO);
        return Result.succ(true);
    }

    /**
     * describe: 获取用户手机号
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/4 16:07
     * @return: {@link Result< String>}
     * @version 1.3.0
     **/
    public Result<UserSessionInfoVO> getPhone(UserSessionInfoDTO dto) {
        String code = dto.getCode();
        String openId = wechatUtils.getOpenId(code);
        String sessionKey = wechatUtils.getSessionKey(openId);
        WxMaUserService wxMaUserService = this.wxService.getUserService();
        if (BaseUtil.isEmptyStr(sessionKey) || BaseUtil.isEmptyStr(openId)) {
            try {
                WxMaJscode2SessionResult result = wxMaUserService.getSessionInfo(code);
                sessionKey = result.getSessionKey();
                openId = result.getOpenid();
            } catch (WxErrorException e) {
                log.error("微信登录,调用官方接口失败：{}", (code + " " + e.getLocalizedMessage()));
                throw new CustomException("微信官方好像断线了，请检查下网络！");
            }
        }
        wechatUtils.putOpenIdAndSessionKey(code, openId, sessionKey);
        String encryptedData = dto.getEncryptedData();
        String iv = dto.getIv();
        try {
            WxMaPhoneNumberInfo phoneNumberInfo = wxMaUserService.getPhoneNoInfo(sessionKey, encryptedData, iv);
            String phone = phoneNumberInfo.getPhoneNumber();
            UserSessionInfoVO userSessionInfo = new UserSessionInfoVO();
            userSessionInfo.setOpenId(openId);
            userSessionInfo.setPhone(phone);
            return Result.succ(userSessionInfo);
        } catch (Exception e) {
            log.error("获取手机号失败：{}", e.getLocalizedMessage() + " " + sessionKey);
            throw new CustomException("网络拥堵，小膳被挤出来了！！！");
        }
    }

    /**
     * describe: 微信登录-手机号登录
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/4 16:42
     * @return: {@link Result<String>}
     * @version 1.3.0
     **/
    @Transactional(rollbackFor = Exception.class)
    public Result<UserDetailVO> loginByPhone(UserSaveDTO dto) {
        String phone = dto.getPhone();
        if (BaseUtil.isEmptyStr(phone)) {
            throw new CustomException("系统未获取到您的手机号！");
        }
        Long groundPusherId = dto.getGroundPusherId();
        if (Boolean.FALSE.equals(this.isStatus(groundPusherId))) {
            groundPusherId = null;
            dto.setGroundPusherId(groundPusherId);
        }
        // 查询用户是否已存在
        User user = userClient.queryUser(phone, dto.getOpenId()).getData();
        if (user == null) {
            user = this.addUser(dto);
        }
        Long userId = user.getId();
        // 头像为空-更新
        if (BaseUtil.isEmptyStr(user.getAvatar())) {
            String openId = dto.getOpenId();
            String sessionKey = wechatUtils.getSessionKey(openId);
            WxMaUserInfo wxMaUserInfo = this.wxService.getUserService()
                    .getUserInfo(sessionKey, dto.getEncryptedData(), dto.getIv());
            String unionId = wxMaUserInfo.getUnionId();
            String avatar = wxMaUserInfo.getAvatarUrl();
            if (BaseUtil.isEmptyStr(unionId)) {
                user.setUnionId(unionId);
            }
            user.setAvatar(avatar);
            userClient.updateUser(user);
        }
        String openId = user.getOpenId();
        // token缓存
        UserTokenDTO tokenDTO = BaseUtil.objToObj(user, UserTokenDTO.class);
        String token = CharUtil.getRandomString(32);
        tokenDTO.setGroundPusherId(groundPusherId);
        tokenDTO.setToken(token);
        tokenDTO.setUserId(userId);
        Integer ticketNumber = this.getTotal(userId);
        UserDetailVO userDetail = this.getUserDetail(user);
        Integer userType = wechatUtils.getUserType(openId);
        if (Objects.isNull(userType)) {
            userType = UserTypeEnum.PERSONAL.getStatus();
        }
        Integer gender = userDetail.getGender();
        Result<BigDecimal> balanceRsl = userBalanceAccountClient.balance(userId);
        userDetail.setToken(token);
        userDetail.setAvatarUrl(user.getAvatar());
        userDetail.setBalance(balanceRsl.getData());
        userDetail.setTicketNumber(ticketNumber);
        userDetail.setSex(null == gender ? UserSexEnum.NEUTRAL.getDesc() : UserSexEnum.parse(gender).getDesc());
        userDetail.setUserTypeStr(UserTypeEnum.parse(userType).getDesc());
        userDetail.setNickName(user.getNickname());
        userDetail.setUserType(userType);
        tokenDTO.setUserType(userType);
        wechatUtils.saveTokenInfo(tokenDTO);
        wechatUtils.putToken(userId, token);
        // 不是新用户-发老用户券
        if (!Boolean.TRUE.equals(user.getNewUser())) {
            this.isNewUser(tokenDTO, groundPusherId);
        }
        return Result.succ(userDetail);
    }

    /**
     * describe: 微信退出登录
     *
     * @param token
     * @author: yy
     * @date: 2020/8/5 11:03
     * @return: {@link Result< Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> signOut(UserTokenDTO token) {
        Long userId = token.getUserId();
        boolean bool = wechatUtils.removeTokenInfo(token.getToken());
        bool = bool && wechatUtils.removeToken(userId);
        Integer userType = token.getUserType();
        if(UserTypeEnum.COMPANY.getStatus().equals(userType)){
            wechatUtils.putUserType(token.getOpenId(), token.getUserType());
        }else {
            wechatUtils.removeUserType(token.getOpenId());
        }
        return Result.succ(bool);
    }

    /**
     * 返回前端的userInfo
     *
     * @param user
     * @return: {@link Result<UserDetailVO>}
     */
    private UserDetailVO getUserDetail(User user) {
        UserDetailVO infoVo = BaseUtil.objToObj(user, UserDetailVO.class);
        BalanceAccountInfo accountEntity = userBalanceAccountClient.userAccountInfo(user.getId()).getData();
        if (accountEntity == null || BaseUtil.isEmptyStr(accountEntity.getPassword())) {
            infoVo.setIsPayPassWord(false);
        } else {
            infoVo.setIsPayPassWord(true);
        }
        return infoVo;
    }


    /**
     * describe: 新增用户或是修改手机号
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/8 9:31
     * @return: {@link User}
     * @version 1.4.0
     **/
    private User addUser(UserSaveDTO dto) {
        String phone = dto.getPhone();
        String openId = dto.getOpenId();

        String sessionKey = wechatUtils.getSessionKey(openId);
        WxMaUserInfo wxMaUserInfo = this.wxService.getUserService()
                .getUserInfo(sessionKey, dto.getEncryptedData(), dto.getIv());
        String unionId = wxMaUserInfo.getUnionId();
        User user = userClient.queryByOpenId(openId).getData();
        if (null == user) {
            dto.setAvatar(wxMaUserInfo.getAvatarUrl());
            dto.setCity(wxMaUserInfo.getCity());
            dto.setCountry(wxMaUserInfo.getCountry());
            dto.setCity(wxMaUserInfo.getCity());
            dto.setGender(Integer.valueOf(wxMaUserInfo.getGender()));
            dto.setNickName(wxMaUserInfo.getNickName());
            dto.setUnionId(wxMaUserInfo.getUnionId());
            dto.setOpenId(wxMaUserInfo.getOpenId());
            // 添加新用户
            user = userClient.addNewUser(dto).getData();
            Long userId = user.getId();
            user.setNewUser(Boolean.TRUE);
            // 创建余额账户
            userBalanceAccountClient.createAccount(userId, UserTypeEnum.PERSONAL.getStatus());
            // 注册个人会员-通联云支付
            myMemberService.registerPersonalMember(userId, openId);
            // 发送新用户消息队列
            Long groundPusherId = user.getGroundPusherId();
            UserIdDTO userIdMq = new UserIdDTO();
            userIdMq.setUserId(userId);
            userIdMq.setPhone(user.getPhone());
            userIdMq.setUserType(user.getUserType());
            userIdMq.setReferrerId(dto.getReferrerId());
            userIdMq.setReferrerType(dto.getReferrerType());
            if (groundPusherId != null) {
                userIdMq.setGroundPusherId(groundPusherId);
                userIdMq.setGroundUserId(userId);
            }
            this.userClient.sendUserMq(userIdMq);
        } else {
            if (!BaseUtil.isEmptyStr(unionId)) {
                user.setUnionId(unionId);
            }
            user.setPhone(phone);
            userClient.updateUser(user);
        }
        // 处理老系统信息
        UserTokenDTO tokenDTO = BaseUtil.objToObj(user, UserTokenDTO.class);
        tokenDTO.setUserId(user.getId());
        oldSystemUtil.disposeOldSystemInfo(tokenDTO, phone);
        return user;
    }

    public Result<WxSignatureResultVO> getWxUrlSignature(String signUrl) {
        WxSignatureResultVO vo = null;
        try {
            if (StringUtils.isNotBlank(signUrl) && signUrl.contains("#")) {
                signUrl = signUrl.split("#")[0];
            }
            WxJsapiSignature signature = wxMpService.createJsapiSignature(signUrl);
            String ticket = wxMpService.getTicket(TicketType.JSAPI);
            log.debug("微信签名ticket=={}", ticket);
            if (Objects.isNull(signature)) {
                return Result.fail("签名失败");
            }
            vo = WxSignatureResultVO.builder()
                    .appId(signature.getAppId())
                    .nonceStr(signature.getNonceStr())
                    .signature(signature.getSignature())
                    .timestamp(String.valueOf(signature.getTimestamp()))
                    .url(signature.getUrl()).build();
        } catch (WxErrorException e) {
            log.error("签名异常:{}", e);
            return Result.fail("签名失败");
        }
        return Result.succ(vo);
    }
}
