package com.meiyuan.catering.merchant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopListV101DTO;
import com.meiyuan.catering.core.dto.base.ShopTagInfoDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.pickup.*;
import com.meiyuan.catering.merchant.dto.shop.*;
import com.meiyuan.catering.merchant.dto.shop.config.DeliveryConfigResponseDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺表(CateringShop)服务层
 *
 * @author wxf
 * @since 2020-03-10 10:23:57
 */
public interface CateringShopService extends IService<CateringShopEntity> {

    /**
     * 方法描述 : 修改商户标签
     *
     * @param merchantId     店铺id
     * @param merchantTagIds 店铺当前标签
     *                       若为空则删除之前所有标签
     * @Author: MeiTao
     * @Date: 2020/6/11 0011 18:08
     * @return: void
     * @Since version-1.0.0
     */
    void updateShopTagV3(Long merchantId, List<Long> merchantTagIds);

    /**
     * 方法描述 : 处理百度位置数据（百度经纬度对应系统地理位置数据表）
     *
     * @param shop          请求参数
     * @param mapCoordinate 请求参数
     * @Author: yaozou
     * @Date: 2020/6/23 0023 9:21
     * @return: void
     * @Since version-1.1.0
     */
    void handlerBaiduLocation(CateringShopEntity shop, String mapCoordinate);

    /**
     * 方法描述 : 查询所有商户店铺
     * 根据查询条件查询对应商户（赠品id查询条件不生效）
     * 包含传入商户、店铺id的商户
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/19  10:16
     * @return: 商户查询结果可能为空
     * @Since version-1.0.0
     */
    Result<List<MerchantShopListVo>> listMerchantShopList(MerchantShopListDTO dto);

    /**
     * 方法描述 : 店铺列表分页查询
     *
     * @param dto 必传：1、商户id
     *            筛选条件：店铺id集合[id为空则查询所有]
     * @Author: MeiTao
     * @Date: 2020/5/19  10:16
     * @return: 商户查询结果可能为空
     * @Since version-1.0.0
     */
    Result<PageData<GoodPushShopVo>> shopPage(ShopQueryPagDTO dto);

    /**
     * 方法描述 : 验证店铺信息是否重复
     *
     * @param keyword 店铺名/注册手机号/详细地址/店铺编码
     * @param type    是否是店铺：true:是，false：否（自提点）
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:22
     * @return: 失败：Result.getData() == 501
     * @Since version-1.0.0
     */
    Result verifyShopInfoUnique(String keyword, Boolean type);

    /**
     * 方法描述 : pc-校验店铺、自提点信息是否重复
     *
     * @param dto 参数
     * @Author: MeiTao
     * @Date: 2020/6/11 0011 14:07
     * @return: Result<Boolean> 重复返回: true,反之返回：false
     * @Since version-1.1.0
     */
    Result<Boolean> verifyShopInfoUnique(ShopVerifyDTO dto);

    /**
     * 方法描述 : 自提点添加
     * 1、添加自提点
     * 2、发送app端 登录用户名密码到对应手机
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:26
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    Result<CateringShopEntity> addPickupAddress(MerchantsPickupAddressDTO dto);

    /**
     * 方法描述 : 自提点修改
     * 1、修改自提点相关信息
     * 2、修改自提点与店铺关联信息
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:30
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    Result updatePickupAddress(PickupAdressUpdateDTO dto);

    /**
     * 方法描述 : 功能描述: 促销活动选择商户下拉列表 <br>
     *
     * @param keyWord 请求参数
     * @Author: gz
     * @Date: 2020/3/24 14:44
     * @return: List<MerchantShopListVo>
     * @Since version-1.1.0
     */
    List<MerchantShopDTO> listMerchantSelect(String keyWord);

    /**
     * 方法描述 : 查询商户(商品管理模块：推送菜品/商品 选择商户下拉列表查询)
     * 1、结果不包含 传入商户、店铺id 对应的店铺
     *
     * @param dto 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: CateringShopEntity 返回值可为空
     * @Since version-1.0.0
     */
    List<ShopDTO> listGoodManagerShop(ShopQueryConditionDTO dto);


    /**
     * 店铺标签查询
     *
     * @param merchantId 商户id
     * @return
     */
    List<ShopTagInfoDTO> listShopTagCache(Long merchantId);

    /**
     * 更新店铺标签
     *
     * @param merchantId
     * @param shopTagInfoList
     */
    void putShopTag(Long merchantId, List<ShopTagInfoDTO> shopTagInfoList);

    /**
     * 描述:查询所有商家
     *
     * @param
     * @return
     * @author zengzhangni
     * @date 2020/5/6 16:57
     */
    List<ShopListV101DTO> shopListV101();

    /**
     * 方法描述 :  app商户登录
     * 1、校验店铺登录信息
     * 2、缓存商户token
     *
     * @param dto 登录参数
     * @param key 解密商户密码key(AES)
     * @param vi  解密商户密码key
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 15:35
     * @return: MerchantLoginResponseDTO
     * @Since version-1.1.0
     */
    Result<MerchantLoginResponseDTO> appMerchantLogin(MerchantLoginRequestDTO dto, String key, String vi);

    /**
     * 方法描述 : app-短信验证码修改密码
     *
     * @param dto
     * @param key
     * @param iv
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 15:37
     * @return: MerchantLoginResponseDTO
     * @Since version-1.0.0
     */
    Result appMerchantUpdatePassword(MerchantPasswordRequestDTO dto, String key, String iv);

    /**
     * 方法描述 : app端商戶切换登录身份
     *
     * @param shopId
     * @param shopRole 店铺当前登录身份 : 1--店铺，2--自提点
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 15:44
     * @return: MerchantLoginResponseDTO
     * @Since version-1.0.0
     */
    Result<MerchantLoginResponseDTO> appMerchantExchangeIdentity(Long shopId, Integer shopRole);


    /**
     * 批量获取根据商户id集合
     *
     * @param merchantIdList 商户id集合
     * @author: wxf
     * @date: 2020/5/19 18:22
     * @return: {@link Result< List< ShopDTO>>}
     * @version 1.0.1
     **/
    Result<List<ShopDTO>> listByMerchantIdList(List<Long> merchantIdList);

    /**
     * 方法描述 : 商户app ：获取门店配送配置信息
     * 1、获取门店配送配置信息
     * 2、获取门店配送时间范围
     *
     * @param shopId
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 10:54
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.merchant.response.DeliveryConfigResponseDTO>
     * @Since version-1.0.0
     */
    Result<DeliveryConfigResponseDTO> getDeliveryConfig(Long shopId);

    /**
     * 方法描述 : 门店配送配置信息修改
     * 1、修改门店配送配置信息
     * 2、修改门店配送时间范围
     *
     * @param shopId
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 11:09
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    Result modifyDeliveryConfig(Long shopId, DeliveryConfigResponseDTO dto);

    /**
     * 方法描述 : 门店自提点 查询(自提点管理/自提点解绑)列表
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/6/4 0004 9:39
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.pickup.PickupManagerResponseDTO>
     * @Since version-1.0.0 version-1.1.0
     */
    Result<PickupManagerResponseDTO> listShopPickupAddress(PickupPointRequestDTO dto);

    /**
     * 方法描述 : 门店自提配置信息修改
     * 1、门店自提时间段修改
     * 2、门店自提赠品修改
     *
     * @param shopId
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/21 0021 14:36
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    Result modifyPickupConfig(Long shopId, PickupConfigResponseDTO dto);


    /**
     * app 获取门店首页信息
     *
     * @param shopId
     * @return
     */
    Result<ShopHomeResponseDTO> getShopHomeInfo(Long shopId);

    /**
     * 店铺自提点绑定解绑
     *
     * @param dto
     * @return
     */
    Result saveOrDelShopPickup(PickupUpdateRequestDTO dto);

    /**
     * 门店业务配置-查询门店业务支持类型
     *
     * @param shopId
     * @param type   修改：业务支持：1：仅配送，2：仅自提，3：全部，仅查询：type为4
     * @return
     */
    Result<Integer> getShopSupportType(Long shopId, Integer type);

    /**
     * 方法描述 : 修改店铺售卖模式
     *
     * @param shopId
     * @param type   售卖模式：1-菜单售卖模式 2-商品售卖模式
     * @Author: MeiTao
     * @Date: 2020/5/28 0028 18:25
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @Since version-1.0.0
     */
    Result<ShopDTO> modifyShopSellType(Long shopId, Integer type);

    /**
     * 方法描述 : wx--订单详情--查看门店头图 v1.1.0
     *
     * @param shopId 请求参数
     * @Author: lhm
     * @Date: 2020/6/23 0023 9:24
     * @return: com.meiyuan.catering.core.util.Result<java.lang.String>
     * @Since version-1.1.0
     */
    Result<String> wxAddressPicture(Long shopId);

    /**
     * 方法描述 : 原密码新密码修改密码
     *
     * @param shopId
     * @param dto
     * @param key
     * @param iv
     * @Author: MeiTao
     * @Date: 2020/6/4 0004 11:33
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    Result updatePasswordByOld(Long shopId, MerchantPasswordRequestDTO dto, String key, String iv);

    /**
     * 方法描述 : 修改商户信息发送mq消息
     * 1、修改店铺地址
     *
     * @param shopDTO
     * @Author: MeiTao
     * @Date: 2020/6/5 0005 10:41
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    void updateMerchantInfoSendMq(ShopDTO shopDTO);

    /**
     * 方法描述 : 图片地址处理
     *
     * @param list
     * @Author: MeiTao
     * @Date: 2020/6/5 0005 11:17
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.merchant.ShopAddressPictureDTO>
     * @Since version-1.0.0
     */
    List<ShopAddressPictureDTO> handelAddressPicture(List<String> list);

    /**
     * 方法描述 : 获取店铺编码
     *
     * @param
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 11:25
     * @return: String 商户编码
     * @Since version-1.1.1
     */
    String getShopCode();

    /**
     * 方法描述 : 修改店铺营业状态
     *
     * @param shop 请求参数
     * @Author: MeiTao
     * @Date: 2020/6/24 0024 10:29
     * @return: CateringShopEntity
     * @Since version-1.1.0
     */
    CateringShopEntity modifyShopBusinessStatus(CateringShopEntity shop);

    /**
     * 方法描述 : pc-店铺-启用禁用
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 17:59
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    Result<ShopDTO> updateShopStatus(StatusUpdateDTO dto);


    /**
     * 方法描述 : 商品推送店铺查询(商品管理模块：推送菜品/商品 选择商户下拉列表查询)
     * 1、传入商户id对应下的门店，不包含传入店铺对应门店
     *
     * @param dto 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: CateringShopEntity 返回值可为空
     * @Since version-1.2.0
     */
    Result<PageData<GoodPushShopVo>> goodPushShopPage(GoodPushShopPagDTO dto);

    /**
     * 方法描述 : 查询商品已推送门店
     * 1、传入商户id对应下的门店
     *
     * @param dto 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: CateringShopEntity 返回值可为空
     * @Since version-1.2.0
     */
    Result<PageData<GoodPushShopVo>> goodPushedShopPage(GoodPushShopPagDTO dto);

    /**
     * 方法描述 : 店铺wx端是否可下单查询
     *
     * @param shopId 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: code 不为0 则不可下单
     * @Since version-1.2.0
     */
    Result isWxPlaceOrder(Long shopId);

    /**
     * 方法描述 : 店铺wx端是否可展示
     *
     * @param shopId 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: code 不为0 则不可在小程序端展示
     * @Since version-1.2.0
     */
    Result isWxShow(Long shopId);

    /**
     * 方法描述 : 商家店铺所在城市查询
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/13 0013 16:47
     * @return: Result<Map < List < com.meiyuan.catering.merchant.dto.shop.ShopCityDTO>>>
     * @Since version-1.2.0
     */
    Result<Map<String, List<ShopCityDTO>>> listShopCity(ShopCityQueryDTO dto);

    /**
     * 方法描述 : 管理平台--店铺对应所有城市查询【1.4.0】
     *
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 15:42
     * @return: Result<java.util.List < ShopCityDTO>>
     * @Since version-1.4.0
     */
    Result<List<ShopCityDTO>> listShopCity();

    /**
     * 方法描述: 通过门店ids获取门店信息<br>
     *
     * @param shopIds
     * @author: gz
     * @date: 2020/7/14 13:10
     * @return: {@link Result< List< ShopDTO>>}
     * @version 1.2.0
     **/
    Result<List<ShopDTO>> listShopByIds(List<Long> shopIds);

    /**
     * 方法描述 :
     *
     * @param merchantId 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/15 0015 18:20
     * @return: Result<java.util.List < GoodPushShopVo>>
     * @Since version-1.2.0
     */
    Result<List<GoodPushShopVo>> listShopPull(Long merchantId);

    /**
     * 方法描述 : 报表门店下拉
     *
     * @param merchantId 商户id
     * @Author: MeiTao
     * @Date: 2020/7/15 0015 18:20
     * @return: Result<java.util.List < GoodPushShopVo>>
     * @Since version-1.4.0
     */
    Result<List<GoodPushShopVo>> listShopReportPull(Long merchantId);

    /**
     * 方法描述 : 删除店铺信息
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 14:20
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.3.0
     */
    Result delShop(ShopDelDTO dto);

    /**
     * 方法描述 : 活动中心 - 新增营销活动选择门店
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/6 0006 10:18
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>>
     * @Since version-1.3.0
     */
    Result<PageData<GoodPushShopVo>> shopMarketPage(ShopMarketPagDTO dto);

    /**
     * 方法描述 : 店铺集合查询通用接口
     *
     * @param dto 条件为空则不进行过滤
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 10:39
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    List<ShopDTO> listShopByCondition(ShopQueryDTO dto);


    List<CateringShopEntity> listDistance12(double lat, double lng);

    /**
     * 方法描述 : 通过登录手机号查询店铺
     * 1、不包含已删除店铺
     * 2、不包含自提点
     *
     * @param phone 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/24 0024 12:00
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    Result<ShopDTO> getByPhone(String phone);

    /**
     * 方法描述 : 秒杀活动【通过城市编码查询微信端可展示门店id】
     *
     * @param dto 条件为空则不进行过滤
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 10:39
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    Result<List<Long>> listShopIdByCity(ShopQueryDTO dto);


    //===========店铺缓存相关处理===========

    /**
     * 方法描述 : 刷新所有商户标签缓存【不包含已删除商户】
     */
    void putAllShopTagInfo();

    /**
     * 方法描述 :
     *
     * @param shopId 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 18:22
     * @return: com.meiyuan.catering.core.dto.base.ShopInfoDTO
     * @Since version-1.3.0
     */
    ShopInfoDTO getShopFromCache(Long shopId);

    /**
     * 方法描述 : 获取门店信息 ： 包含已删除门店
     *
     * @param shopId 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 18:29
     * @return: com.meiyuan.catering.core.dto.base.ShopInfoDTO
     * @Since version-1.3.0
     */
    ShopInfoDTO getShop(Long shopId);

    /**
     * 方法描述 : 平台报表查询门店信息
     * 不包含已删除门店
     *
     * @param dto 条件为空则不进行过滤
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 10:55
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.shop.MerchantShopInfoDTO>
     * @Since version-1.3.0
     */
    Result<List<MerchantShopInfoDTO>> reportShopQuery(ShopQueryDTO dto);

    /**
     * describe: 选择门店
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/3 14:32
     * @return: {@link PageData< ShopChoicePageVO>}
     * @version 1.4.0
     **/
    PageData<ShopChoicePageVO> queryPageChoiceShop(ShopChoicePageDTO dto);

    /**
     * 同步门店信息到达达
     *
     * @param shopId       门店唯一标示，不存在新增，存在更新
     * @param shopName
     * @param cityName
     * @param areaName
     * @param address
     * @param lat
     * @param lng
     * @param shopContacts
     * @param shopMobile
     */
    void syncShopInfoToDaDa(
            Long shopId,
            String shopName,
            String cityName,
            String areaName,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String shopContacts,
            String shopMobile);

    /**
     * 批量同步门店数据到达达
     *
     * @param shopIdList
     */
    void batchSyncShopInfoToDaDa(List<Long> shopIdList);

    /**
     * 方法描述 : 1.5.0 店铺老数据处理
     * @Author: MeiTao
     * @Date: 2020/10/12 0012 10:08
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.5.0
     */
    Result handleShopDataV5();

    /**
     * 根据id获取员工
     * @param accountTypeId
     * @return
     */
    CateringShopEmployeeEntity getEmployeeById(Long accountTypeId);

    /**
     * 方法描述 : app登陆token清除老数据处理
     * @Author: MeiTao
     * @Date: 2020/10/21 0021 14:26
     * @return: void
     * @Since version-1.5.0
     */
    void handleAppTokenV5();
}
