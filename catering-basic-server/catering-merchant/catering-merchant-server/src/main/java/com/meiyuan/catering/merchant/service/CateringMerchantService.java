package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.RegisterPhoneChangeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantInfoVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo;

import java.util.List;

/**
 * 商户表(CateringMerchant)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
public interface CateringMerchantService extends IService<CateringMerchantEntity> {

    //===================商户缓存处理相关接口===================

    /**
     * 方法描述 : 刷新商户缓存
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 10:27
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.2.0
     */
    Result refreshMerchantCatch();

    /**
     * 店铺信息入redis
     */
    void putAllShopCache();

    /**
     * 店铺所有配置信息入redis
     */
    void putAllShopConfigCache();

    /**
     * 方法描述 : pc-店铺列表
     *           1、参数中结束时间加一天
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 18:30
     * @param dto
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.0.0
     */
    IPage<CateringMerchantPageVo> listLimit(MerchantLimitQueryDTO dto);

    /**
     * 商户店铺详情查询
     * @param dto
     * @return
     */
    Result<MerchantShopDetailVo> getMerchantShopDetail(ShopQueryDTO dto );

    /**
     * 添加店铺
     * @param dto
     * @return 若为店铺返回店铺id，自提点返回空
     */
    Result<Long> insertMerchantShop(MerchantShopAddDTO dto);

    /**
     *  通过查询条件查询商户信息
     * @param queryCondition
     * @return
     */
    List<MerchantInfoVo> listMerchantInfo(MerchantQueryConditionDTO queryCondition);

    /**
     * 方法描述 : 更新店铺缓存
     *          若商户id为空则不做处理
     * @Author: MeiTao
     * @Date: 2020/6/3 0003 14:21
     * @param shopEntity
     * @return: void
     * @Since version-1.0.0
     */
    void updateShopCache(CateringShopEntity shopEntity);

    /**
     * 全部商户
     *
     * @author: wxf
     * @date: 2020/4/22 13:54
     * @param flag 是否包含自提点 true 包含 false 不包含
     * @return: {@link List< ShopDTO>}
     **/
    List<ShopDTO> allMerchant(boolean flag);

    /**
     * 查询所有门店信息 不包含自提点
     * @author: GongJunZheng
     * @date: 2020/8/19 18:49
     * @return: {@link List<ShopDTO>}
     * @version V1.3.0
     **/
    List<ShopDTO> findAll();

    /**
     *  改变营业（门店/自提点）状态：营业中 变为 打烊中/暂停营业，打样中/暂停营业 变为 营业中
     * @author yaozou
     * @date 2020/3/20 16:37
     * @param shopId 门店ID
     * @param type  类型：判断是查询还是修改：false：查询，true：修改"
     * @since v1.0.0
     * @return
     */
    Result<ShopResponseDTO> modifyMerchantBusinessStatus(Long shopId, Boolean type);


    /**
     * 方法描述 :  修改门店基本信息
     *             调用位置 ： 1、pc端 ： 修改门店信息；2、app端： 修改门店信息
     *            1、修改门店基本信息
     *            2、当前店铺绑定自提点数据同步
     *            3、修改门店缓存
     * @Author: MeiTao
     * @Date: 2020/5/21 0021 11:04
     * @param shopId
     * @param dto
     * @return: Result<com.meiyuan.catering.merchant.dto.ShopDTO>
     * @Since version-1.0.0
     */
    Result<ShopDTO> modifyMerchantBaseInfo(Long shopId , MerchantModifyInfoRequestDTO dto);

    /**
     * 方法描述 : 管理平台商户列表分页查询
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    Result<IPage<MerchantPageVo>> merchantPage(MerchantQueryPage dto);


    /**
     * 方法描述 : 添加修改商户
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    Result<Long> saveOrUpdateMerchant(MerchantDTO dto);

    /**
     * 方法描述 : 获取商户编码
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 11:25
     * @param
     * @return: String 商户编码
     * @Since version-1.1.1
     */
    String getMerchantCode();

    /**
     * 方法描述 : 商户详情获取
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 商户Id
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    Result<MerchantDetailDTO> queryMerchantDetail(MerchantQueryPage dto);

    /**
     * 方法描述 : 商户状态修改
     *          1、redis信息同步
     *          2、es信息同步
     * @Author: MeiTao
     * @Date: 2020/7/5 0005 11:18
     * @param dto 请求参数
     * @return: Result 商户下店铺id【未删除】
     * @Since version-1.1.0
     */
    Result<List<Long>> updateMerchantStatus(MerchantDTO dto);

    /**
     * 方法描述 : pc - 店铺注册手机号修改
     * @Author: MeiTao
     * @Date: 2020/7/8 0008 9:37
     * @param dto 请求参数
     * @return: Result<java.lang.String>
     * @Since version-1.2.0
     */
    Result<String> updateShopRegisterPhone(RegisterPhoneChangeDTO dto);

    /**
     * 方法描述 : admin-商户信息验重
     * @Author: MeiTao
     * @Date: 2020/7/10 0010 13:11
     * @param dto 商户名，电话号码
     * @return: com.meiyuan.catering.core.util.Result<java.lang.String>
     * @Since version-1.2.0
     */
    Result<Boolean> verifyMerchantInfoUnique(MerchantVerifyDTO dto);

    /**
     * 方法描述 : pc-端获取商户登陆信息
     * @Author: MeiTao
     * @Date: 2020/7/24 0024 10:31
     * @param phone 请求参数
     * @return: com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO
     * @Since version-1.2.0
     */
    Result<MerchantDTO> pcLoginInfo(String phone);

    /**
     * 方法描述 : 商户登录账号老数据处理【1.3.0】
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 10:36
     * @return: Result<java.lang.Boolean>
     * @Since version-1.3.0
     */
    Result<Boolean> handelMerchantData();

    /**
     * 方法描述 : 查询商户登录账号信息
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 14:51
     * @param account 请求参数
     * @return: java.util.List<MerchantLoginAccountDTO>
     * @Since version-1.3.0
     */
    Result<List<MerchantLoginAccountDTO>> listMerchantAccount(String account);

    /**
     * 小程序 - 商户申请，平台审核通过后新增品牌
     * @param dto
     * @return Long 品牌ID
     */
    Long insertMerchantForApply(MerchantDTO dto);

}