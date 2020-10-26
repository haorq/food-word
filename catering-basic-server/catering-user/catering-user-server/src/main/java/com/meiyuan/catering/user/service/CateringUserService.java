package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.user.dto.user.User;
import com.meiyuan.catering.user.dto.user.UserFollowDTO;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import com.meiyuan.catering.user.dto.user.UserSaveDTO;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.vo.user.UserDetailInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 用户表(CateringUser)服务层
 *
 * @author mei-tao
 * @since 2020-03-10 15:30:41
 */
public interface CateringUserService extends IService<CateringUserEntity> {

    /**
     * zzn 通过用户名/手机号 查询id集合
     *
     * @param keyword 用户名/手机号
     * @return ids
     */
    List<Long> getIdsByKeyword(String keyword);


    /**
     * 用户信息
     *
     * @param userId
     * @return
     */
    UserDetailInfoVo queryUserById(Long userId);

    /**
     * 新用户消息入队
     *
     * @param userIdDTO
     */
    void sendUserMq(UserIdDTO userIdDTO);

    /**
     * 描述:修改用户
     *
     * @param id
     * @return boolean
     * @author zengzhangni
     * @date 2020/6/23 15:05
     * @since v1.1.1
     */
    boolean update(Long id);

    /**
     * 描述:通过企业account查询用户
     *
     * @param account
     * @return com.meiyuan.catering.user.dto.user.User
     * @author zengzhangni
     * @date 2020/6/23 15:00
     * @since v1.1.1
     */
    User queryByUserId(String account);


    /**
     * describe: 添加新用户
     *
     * @param dto
     * @author: yy
     * @date: 2020/8/4 16:53
     * @return: {@link CateringUserEntity}
     * @version 1.3.0
     **/
    CateringUserEntity addNewUser(UserSaveDTO dto);


    /**
     * 描述: 通过用户id或者公司id查询对应信息
     * <p>
     * 如果是返回结果是
     * 个人{
     * 所有信息都是用户数据
     * }
     * <p>
     * 公司类型
     * {
     * id=公司id
     * userType = 1(公司)
     * name=用户名称
     * phone=用户电话
     * }
     *
     * @param uidOrCid userId 或者 userCompanyId
     * @return com.meiyuan.catering.core.dto.base.UserCompanyInfo
     * @author zengzhangni
     * @date 2020/5/28 14:15
     * @since v1.1.0
     */
    UserCompanyInfo getUcInfo(Long uidOrCid);

    /**
     * 描述: 先查询用户 不存在再查询企业用户
     *
     * @param uidOrCid
     * @return com.meiyuan.catering.core.dto.base.UserCompanyInfo
     * @author zengzhangni
     * @date 2020/8/27 10:00
     * @since v1.3.0
     */
    UserCompanyInfo getFuzzyUcInfo(Long uidOrCid);

    /**
     * 描述:
     * <p>
     * 如果是返回结果是
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
     * @return com.meiyuan.catering.core.dto.base.UserCompanyInfo
     * @author zengzhangni
     * @date 2020/5/28 14:19
     * @since v1.1.0
     */
    UserCompanyInfo getUcInfoFill(Long uidOrCid);

    /**
     * 描述:批量获取信息
     *
     * @param uidOrCids 用户或公司 id集合
     * @return java.util.List<com.meiyuan.catering.core.dto.base.UserCompanyInfo>
     * @author zengzhangni
     * @date 2020/6/23 10:53
     * @since v1.1.1
     */
    List<UserCompanyInfo> getUcInfoList(List<Long> uidOrCids);

    /**
     * 描述:获取信息
     *
     * @param uidOrCids
     * @return java.util.Map
     * @author zengzhangni
     * @date 2020/6/23 15:00
     * @since v1.1.1
     */
    Map<Long, UserCompanyInfo> getUcInfoMap(List<Long> uidOrCids);

    /**
     * describe: 根据手机号去获取用户信息
     *
     * @param phone
     * @author: yy
     * @date: 2020/8/5 15:09
     * @return: {@link User}
     * @version 1.3.0
     **/
    CateringUserEntity queryUser(String phone, String openId);

    /**
     * describe: 查询前版本已记录，但未填手机号的用户信息
     *
     * @param openId
     * @param status
     * @author: yy
     * @date: 2020/8/5 16:00
     * @return: {@link CateringUserEntity}
     * @version 1.3.0
     **/
    CateringUserEntity queryByOpenId(String openId, Integer status);

    /**
     * describe: 用户关注公众号
     * @author: yy
     * @date: 2020/9/3 15:24
     * @param dto
     * @param accessToken
     * @return: {@link String}
     * @version 1.4.0
     **/
    String userFocusOn(UserFollowDTO dto,String accessToken);

    /**
     * describe: 根据unionId建立当前用户地推关系
     * @author: yy
     * @date: 2020/9/8 9:38
     * @param unionId
     * @param userId
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    Boolean updateUserByUnionId(String unionId, Long userId);

    /**
     * 方法描述: 通过手机号获取用户ID<br>
     *
     * @author: gz
     * @date: 2020/10/9 11:04
     * @param phone
     * @return: {@link Long}
     * @version 1.5.0
     **/
    Long getUserIdByPhone(String phone);
}
