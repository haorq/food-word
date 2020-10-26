package com.meiyuan.catering.admin.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.dto.admin.admin.AdminAddDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminPwdUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.auth.UserLoginDto;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.enums.base.AdminResponseEnum;
import com.meiyuan.catering.admin.fegin.AdminClient;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.admin.vo.admin.admin.AdminDetailsVo;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.config.SmsProperties;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyRestrictService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.notify.RestrictConstant;
import com.meiyuan.catering.core.notify.RestrictNumEnum;
import com.meiyuan.catering.core.util.AesEncryptUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.bcrypt.BcryptPasswordEncoder;
import com.meiyuan.catering.core.util.token.TokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lhm
 * @date 2020/3/17 11:08
 **/
@Service
public class AdminAuthService {

    @Resource
    private AdminClient adminClient;
    @Autowired
    private RoleClient roleClient;
    @Resource
    private SmsProperties smsProperties;
    @Autowired
    private AdminUtils adminUtils;
    @Autowired
    private EncryptPasswordProperties encryptPasswordProperties;
    @Autowired
    private NotifyRestrictService restrictService;

    public Result<Object> login(UserLoginDto vo) {
        String username = vo.getUsername();
        String password = vo.getPassword();

        // 判断用户是否存在 或 禁用
        CateringAdmin nameOrPhone = findByNameOrPhone(username);

        if (null == nameOrPhone || nameOrPhone.getIsDel().equals(true)) {
            return Result.fail("账号不存在，请重新输入");
        }
        if (nameOrPhone.getStatus().equals(StatusEnum.ENABLE_NOT.getStatus())) {
            return Result.fail("您的账号已被禁用,请联系管理员");
        }

        //解密密码
        String desEncrypt = AesEncryptUtil.desEncrypt(password, encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());

        BcryptPasswordEncoder encoder = new BcryptPasswordEncoder();
        if (!encoder.matches(desEncrypt, nameOrPhone.getPassword())) {
            throw new CustomException(AdminResponseEnum.ADMIN_INVALID_PASSWORD.desc());
        }

        String token = TokenUtil.generateIdToken(nameOrPhone.getId(), TokenEnum.ADMIN);
        adminUtils.putAdminToken(nameOrPhone.getId(), token);
        return Result.succ(token);
    }


    /**
     * 添加操作员
     *
     * @param dto
     * @return
     */
    public Result<Object> create(AdminAddDTO dto) {
        return Result.succ(adminClient.create(dto).getData());
    }

    /**
     * 操作员列表查询
     *
     * @param dto
     * @return
     */
    public Result<IPage<AdminListQueryVo>> querySelective(AdminQueryDTO dto) {
        return Result.succ(adminClient.querySelective(dto).getData());
    }

    public Result<Integer> updateAdmin(AdminUpdateDTO dto) {
        return Result.succ(adminClient.updateAdmin(dto).getData());
    }

    public Result<AdminDetailsVo> details(Long id) {
        return Result.succ(adminClient.details(id).getData());
    }


    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    public Result<Boolean> updatePassword(CateringAdmin admin, AdminPwdUpdateDTO dto) {

        //修改密码 通过手机号
        CateringAdmin checkTel = getByPhone(admin.getPhone());
        if (checkTel == null) {
            return Result.fail(AdminResponseEnum.ADMIN_NAME_NOT_EXIST.desc());
        }

        //密码 解密旧密码
        String oldDesEnc = AesEncryptUtil.desEncrypt(dto.getOldPassword(), encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());

        BcryptPasswordEncoder encoder = new BcryptPasswordEncoder();
        if (!encoder.matches(oldDesEnc, checkTel.getPassword())) {
            return Result.fail(AdminResponseEnum.ADMIN_INVALID_OLD_PASSWORD.desc());
        }

        //解密新密码
        String newDesEnc = AesEncryptUtil.desEncrypt(dto.getNewPassword(), encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());

        String encodedPassword = encoder.encode(newDesEnc);
        checkTel.setPassword(encodedPassword);
        adminClient.updateById(checkTel);
        return Result.succ();

    }

    /**
     * 短信验证码
     *
     * @param phone
     * @param code
     * @return
     */
    public Result saveSmsAuthCode(String phone, String code) {
        adminUtils.saveSmsAuthCode(phone, code);
        return Result.succ();
    }


    /**
     * 忘记密码   修改之后需要退出登录
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> forgetPassword(AdminPwdUpdateDTO dto) {

        // 1、短信验证码验证
        String code = adminUtils.getSmsAuthCode(dto.getPhone());
        if (StringUtils.isEmpty(code)) {
            return Result.fail("验证码已失效，请重新发送短信验证码");
        }
        if (!Objects.equals(code, dto.getVefiyCode())) {
            return Result.fail("验证码错误，请重新输入");
        }

        // 2、修改密码
        CateringAdmin byPhone = getByPhone(dto.getPhone());
        String newDesEnc = AesEncryptUtil.desEncrypt(dto.getNewPassword(), encryptPasswordProperties.getKey(), encryptPasswordProperties.getIv());
        BcryptPasswordEncoder encoder = new BcryptPasswordEncoder();
        byPhone.setPassword(encoder.encode(newDesEnc));
        adminClient.updateById(byPhone);
        adminUtils.removeSmsAuthCode(dto.getPhone());
        return Result.succ();


    }


    /**
     * 通过账号或手机号查询管理员
     *
     * @param nameOrPhone
     * @return
     */
    public CateringAdmin findByNameOrPhone(String nameOrPhone) {
        LambdaQueryWrapper<CateringAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringAdmin::getPhone, nameOrPhone).or();
        queryWrapper.eq(CateringAdmin::getUsername, nameOrPhone);
        return adminClient.getOne(queryWrapper).getData();
    }


    /**
     * 获取用户信息
     *
     * @param phone
     * @return
     */
    public CateringAdmin getByPhone(String phone) {
        LambdaQueryWrapper<CateringAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringAdmin::getPhone, phone);
        return adminClient.getOne(queryWrapper).getData();
    }

    private boolean isUsed(int userStatus) {
        // TODO 魔法值改为枚举
        return userStatus == 1;
    }


    public Result sendCode(String phone) {
        // 1、用户是否禁用 和 账号是否存在
        CateringAdmin admin = this.getByPhone(phone);
        if (admin == null) {
            return Result.fail("该账号不存在，请重新输入");
        } else if (!isUsed(admin.getStatus())) {
            return Result.fail("账号已被禁用，请联系平台管理员");
        }

        String code;
        if (Boolean.TRUE.equals(smsProperties.getCode())) {
            code = CodeGenerator.randomCode(6);
            //发送验证码
            restrictService.notifySmsTemplate(phone, NotifyType.VERIFY_CODE_NOTIFY, RestrictNumEnum.RESET_PAY_PWD, RestrictConstant.WEB, code);
        } else {
            code = smsProperties.getDefaultCode();
        }
        //保存验证码
        adminUtils.saveSmsAuthCode(phone, code);

        return Result.succ();
    }

    public List<Map<String,Object>> loginAccountPermission(Long accountId){
        return roleClient.loginAccountPermission(accountId,true);
    }

}



