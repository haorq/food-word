package com.meiyuan.catering.user.fegin.user;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.User;
import com.meiyuan.catering.user.dto.user.UserFollowDTO;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import com.meiyuan.catering.user.dto.user.UserSaveDTO;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.enums.UserStatusEnum;
import com.meiyuan.catering.user.service.CateringUserService;
import com.meiyuan.catering.user.vo.user.UserDetailInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/5/19 13:50
 * @description
 **/
@Service
public class UserClient {
    @Resource
    private CateringUserService userService;

    /**
     * zzn 通过用户名/手机号 查询id集合
     *
     * @param keyword 用户名/手机号
     * @return ids
     */
    public Result<List<Long>> getIdsByKeyword(String keyword) {
        return Result.succ(userService.getIdsByKeyword(keyword));
    }


    public Result<UserDetailInfoVo> queryUserById(Long id) {
        return Result.succ(userService.queryUserById(id));
    }


    public void sendUserMq(UserIdDTO userIdDTO) {
        userService.sendUserMq(userIdDTO);
    }


    public Result<User> queryByAccount(String account) {
        return Result.succ(userService.queryByUserId(account));
    }


    public Result<User> queryById(Long userId) {
        CateringUserEntity byId = userService.getById(userId);
        return Result.succ(BaseUtil.objToObj(byId, User.class));
    }




    /**
     * @return {@link Result< Boolean>}
     * @Author lhm
     * @Description 更新用户
     * @Date 2020/5/20
     * @Param [userId, userCompanyId]
     * @Version v1.1.0
     */
    public Result<Boolean> updateUser(User user) {
        CateringUserEntity entity = BaseUtil.objToObj(user, CateringUserEntity.class);
        return Result.succ(userService.updateById(entity));
    }


    /**
     * @return {@link Result< Boolean>}
     * @Author lhm
     * @Description 解绑企业用户
     * @Date 2020/5/20
     * @Param [id]
     * @Version v1.1.0
     */
    public Result<Boolean> unBindUserCompany(Long id) {
        return Result.succ(userService.update(id));
    }


    public Result<Boolean> save(CateringUserEntity user) {
        return Result.succ(userService.save(user));
    }


    /**
     * describe: 添加新用户
     *
     * @param
     * @author: yy
     * @date: 2020/8/4 16:52
     * @return: {@link Result< User>}
     * @version 1.3.0
     **/
    public Result<User> addNewUser(UserSaveDTO dto) {
        return Result.succ(BaseUtil.objToObj(userService.addNewUser(dto), User.class));
    }

    public Result<UserCompanyInfo> getFuzzyUcInfo(Long uidOrCid) {
        return Result.succ(userService.getFuzzyUcInfo(uidOrCid));
    }

    public Result<Map<Long, UserCompanyInfo>> getUcInfoMap(List<Long> uidOrCids) {
        return Result.succ(userService.getUcInfoMap(uidOrCids));
    }

    /**
     * 描述:如果是返回结果是
     * 个人{
     * 所有信息都是用户数据
     * }
     * 公司类型
     * 所有信息都是公司数据
     * {
     * id=公司id
     * userType = 1(公司)
     * name=公司名称
     * phone=公司电话
     * }
     *
     * @param uidOrCid
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.base.UserCompanyInfo>
     * @author zengzhangni
     * @date 2020/5/28 14:40
     * @since v1.1.0
     */
    public Result<UserCompanyInfo> getUcInfoFill(Long uidOrCid) {
        return Result.succ(userService.getUcInfoFill(uidOrCid));
    }

    /**
     * describe: 根据手机号去获取用户信息并判断是否有人使用
     *
     * @param phone
     * @author: yy
     * @date: 2020/8/5 15:10
     * @return: {@link Result< User>}
     * @version 1.3.0
     **/
    public Result<User> queryUser(String phone, String openId) {
        return Result.succ(BaseUtil.objToObj(userService.queryUser(phone, openId), User.class));
    }

    /**
     * describe: 查询前版本已记录，但未填手机号的用户信息
     *
     * @param openId
     * @author: yy
     * @date: 2020/8/5 15:59
     * @return: {@link Result< User>}
     * @version 1.3.0
     **/
    public Result<User> queryByOpenId(String openId) {
        return Result.succ(BaseUtil.objToObj(userService.queryByOpenId(openId, UserStatusEnum.NORMAL.getStatus()), User.class));
    }

    /**
     * describe: 用户关注公众号-暂不用
     *
     * @param dto
     * @param accessToken
     * @author: yy
     * @date: 2020/9/3 15:15
     * @return: {@link String}
     * @version 1.4.0
     **/
    public String userFocusOn(UserFollowDTO dto, String accessToken) {
        return userService.userFocusOn(dto, accessToken);
    }

    /**
     * describe: 根据unionId 更新用户信息-暂不用
     *
     * @param unionId
     * @param userId
     * @author: yy
     * @date: 2020/9/7 17:21
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    public Boolean updateUserByUnionId(String unionId, Long userId) {
        return userService.updateUserByUnionId(unionId, userId);
    }


    public List<User> list() {
        List<CateringUserEntity> list = userService.list();
        return BaseUtil.objToObj(list, User.class);
    }


    /**
     * 方法描述: 通过手机号获取用户ID<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:04
     * @param phone
     * @return: {@link Long}
     * @version 1.5.0
     **/
    public Long getUserIdByPhone(String phone){
        return userService.getUserIdByPhone(phone);
    }
}
