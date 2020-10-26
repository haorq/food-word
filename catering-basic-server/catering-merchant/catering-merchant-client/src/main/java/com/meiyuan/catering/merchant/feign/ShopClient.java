package com.meiyuan.catering.merchant.feign;


import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopListV101DTO;
import com.meiyuan.catering.core.dto.base.ShopTagInfoDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.pickup.PickupConfigResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupManagerResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupPointRequestDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupUpdateRequestDTO;
import com.meiyuan.catering.merchant.dto.shop.*;
import com.meiyuan.catering.merchant.dto.shop.config.DeliveryConfigResponseDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.service.CateringShopEmployeeService;
import com.meiyuan.catering.merchant.service.CateringShopService;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 10:07
 * @Description 简单描述 : 店铺Client
 * @Since version-1.0.0
 */
@Service
public class ShopClient {
    @Autowired
    private CateringShopService shopService;
    @Resource
    private CateringShopEmployeeService cateringShopEmployeeService;

    //缓存相关处理

    /**
     * 方法描述 : 刷新所有商户标签缓存【不包含已删除商户】
     */
    public void putAllShopTagInfo() {
        shopService.putAllShopTagInfo();
    }

    /**
     * 获取门店信息 ： 包含已删除门店
     *
     * @param shopId
     * @return
     */
    public ShopInfoDTO getShop(Long shopId) {
        return shopService.getShop(shopId);
    }

    /**
     * 获取门店缓存信息 ： 若门店缓存信息为空则返回 null
     */
    public ShopInfoDTO getShopFromCache(Long shopId) {
        return shopService.getShopFromCache(shopId);
    }

    /**
     * 方法描述 : 查询所有商户店铺（包含已删除店铺、目前仅自提送赠品活动列表查询使用）
     * 根据查询条件查询对应商户（赠品id查询条件不生效）
     * 包含传入商户、店铺id的商户
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/19  10:16
     * @return: 商户查询结果可能为空
     * @Since version-1.0.0
     */
    public Result<List<MerchantShopListVo>> listMerchantShopList(MerchantShopListDTO dto) {
        return shopService.listMerchantShopList(dto);
    }

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
    public Result<PageData<GoodPushShopVo>> shopPage(ShopQueryPagDTO dto) {
        return shopService.shopPage(dto);
    }

    /**
     * 方法描述 : pc-校验店铺、自提点信息是否重复
     *
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:22
     * @return: 失败：Result.getData() == 501
     * @Since version-1.0.0
     */
    public Result<Boolean> verifyShopInfoUnique(ShopVerifyDTO dto) {
        return shopService.verifyShopInfoUnique(dto);
    }

    /**
     * 方法描述 : 促销活动选择商户下拉列表
     *
     * @param keyWord 店铺名
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:32
     * @return: java.util.List<com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo>
     * @Since version-1.0.0
     */
    public Result<List<MerchantShopDTO>> listMerchantSelect(String keyWord) {
        return Result.succ(shopService.listMerchantSelect(keyWord));
    }

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
    public Result<List<ShopDTO>> listGoodManagerShop(ShopQueryConditionDTO dto) {
        return Result.succ(shopService.listGoodManagerShop(dto));
    }

    /**
     * 店铺标签查询
     *
     * @param merchantId 商户id
     * @return
     */
    public Result<List<ShopTagInfoDTO>> listShopTagCache(Long merchantId) {
        return Result.succ(shopService.listShopTagCache(merchantId));
    }

    /**
     * 描述:查询所有商家
     *
     * @param
     * @return
     * @author zengzhangni
     * @date 2020/5/6 16:57
     */
    public Result<List<ShopListV101DTO>> shopListV101() {
        return Result.succ(shopService.shopListV101());
    }

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
    public Result<MerchantLoginResponseDTO> appMerchantLogin(MerchantLoginRequestDTO dto, String key, String vi) {
        return shopService.appMerchantLogin(dto, key, vi);
    }

    /**
     * 方法描述 : app-短信验证码修改密码
     *
     * @Author: MeiTao
     */
    public Result appMerchantUpdatePassword(MerchantPasswordRequestDTO dto, String key, String iv) {
        return shopService.appMerchantUpdatePassword(dto, key, iv);
    }

    /**
     * 批量获取根据商户id集合
     *
     * @param merchantIdList 商户id集合
     * @author: wxf
     * @date: 2020/5/19 18:22
     * @return: {@link Result< List< ShopDTO>>}
     * @version 1.0.1
     **/
    public Result<List<ShopDTO>> listByMerchantIdList(List<Long> merchantIdList) {
        return shopService.listByMerchantIdList(merchantIdList);
    }

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
    public Result<MerchantLoginResponseDTO> appMerchantExchangeIdentity(Long shopId, Integer shopRole) {
        return shopService.appMerchantExchangeIdentity(shopId, shopRole);
    }

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
    public Result<DeliveryConfigResponseDTO> getDeliveryConfig(Long shopId) {
        return shopService.getDeliveryConfig(shopId);
    }

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
    public Result modifyDeliveryConfig(Long shopId, DeliveryConfigResponseDTO dto) {
        return shopService.modifyDeliveryConfig(shopId, dto);
    }

    /**
     * 方法描述 : 门店自提点 查询(自提点管理/自提点解绑)列表
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/6/4 0004 9:39
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.pickup.PickupManagerResponseDTO>
     * @Since version-1.0.0 version-1.1.0
     */
    public Result<PickupManagerResponseDTO> listShopPickupAddress(PickupPointRequestDTO dto) {
        return shopService.listShopPickupAddress(dto);
    }

    /**
     * 门店自提配置信息修改
     *
     * @param shopId
     * @param dto
     * @return
     */
    public Result modifyPickupConfig(Long shopId, PickupConfigResponseDTO dto) {
        return shopService.modifyPickupConfig(shopId, dto);
    }

    /**
     * app 获取门店首页信息
     *
     * @param shopId
     * @return
     */
    public Result<ShopHomeResponseDTO> getShopHomeInfo(Long shopId) {
        return shopService.getShopHomeInfo(shopId);
    }

    /**
     * 店铺自提点绑定解绑
     *
     * @param dto
     * @return
     */
    public Result saveOrDelShopPickup(PickupUpdateRequestDTO dto) {
        return shopService.saveOrDelShopPickup(dto);
    }

    /**
     * 门店业务配置-查询门店业务支持类型
     *
     * @param shopId
     * @param type   修改：业务支持：1：仅配送，2：仅自提，3：全部，仅查询：type为4
     * @return
     */
    public Result<Integer> getShopSupportType(Long shopId, Integer type) {
        return shopService.getShopSupportType(shopId, type);
    }

    /**
     * 方法描述 : 修改店铺售卖模式
     *
     * @param shopId
     * @param type   售卖模式：1-菜单售卖模式 2-商品售卖模式
     * @Author: MeiTao
     * @Date: 2020/5/28 0028 18:23
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @Since version-1.0.0
     */
    public Result<ShopDTO> modifyShopSellType(Long shopId, Integer type) {
        return shopService.modifyShopSellType(shopId, type);
    }


    /**
     * @return {@link Result< String>}
     * @Author lhm
     * @Description wx--订单详情--获取门店头图
     * @Date 2020/6/3
     * @Param [merchantId]
     * @Version v1.1.0
     */
    public Result<String> wxAddressPicture(Long merchantId) {
        return shopService.wxAddressPicture(merchantId);
    }


    /**
     * 方法描述 : app-原密码新密码修改密码
     *
     * @Author: MeiTao
     */
    public Result updatePasswordByOld(Long shopId, MerchantPasswordRequestDTO dto, String key, String iv) {
        return shopService.updatePasswordByOld(shopId, dto, key, iv);
    }

    /**
     * 方法描述 : pc-店铺-启用禁用
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 17:59
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.1.0
     */
    public Result<ShopDTO> updateShopStatus(StatusUpdateDTO dto) {
        return shopService.updateShopStatus(dto);
    }

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
    public Result<PageData<GoodPushShopVo>> goodPushShopPage(GoodPushShopPagDTO dto) {
        return shopService.goodPushShopPage(dto);
    }

    /**
     * 方法描述 : 查询商品已推送门店
     * 1、传入商~户id对应下的门店
     *
     * @param dto 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: CateringShopEntity 返回值可为空
     * @Since version-1.2.0
     */
    public Result<PageData<GoodPushShopVo>> goodPushedShopPage(GoodPushShopPagDTO dto) {
        return shopService.goodPushedShopPage(dto);
    }

    /**
     * 方法描述 : 店铺wx端是否可下单查询
     *
     * @param shopId 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: code 不为0 则不可下单
     * @Since version-1.2.0
     */
    public Result isWxPlaceOrder(Long shopId) {
        return shopService.isWxPlaceOrder(shopId);
    }

    /**
     * 方法描述 : 店铺wx端是否可展示
     *
     * @param shopId 查询条件
     * @Author: MeiTao
     * @Date: 2020/5/19 0019 10:41
     * @return: code 不为0 则不可在小程序端展示
     * @Since version-1.2.0
     */
    public Result isWxShow(Long shopId) {
        return shopService.isWxShow(shopId);
    }


    public Result<Map<String, List<ShopCityDTO>>> listShopCity(ShopCityQueryDTO dto) {
        return shopService.listShopCity(dto);
    }

    /**
     * 方法描述 : 管理平台--店铺对应所有城市查询【1.4.0】
     *
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<List<ShopCityDTO>> listShopCity() {
        return shopService.listShopCity();
    }

    /**
     * 方法描述: 通过门店ids获取门店信息<br>
     *
     * @param shopIds
     * @author: gz
     * @date: 2020/7/14 13:10
     * @return: {@link Result< List< ShopDTO>>}
     * @version 1.2.0
     **/
    public Result<List<ShopDTO>> listShopByIds(List<Long> shopIds) {
        return shopService.listShopByIds(shopIds);
    }

    /**
     * 方法描述 :
     *
     * @param merchantId 请求参数
     * @Author: MeiTao
     * @Date: 2020/7/15 0015 18:20
     * @return: Result<java.util.List < GoodPushShopVo>>
     * @Since version-1.2.0
     */
    public Result<List<GoodPushShopVo>> listShopPull(Long merchantId) {
        return shopService.listShopPull(merchantId);
    }

    /**
     * 方法描述 : 报表门店下拉
     *
     * @Since version-1.4.0
     */
    public Result<List<GoodPushShopVo>> listShopReportPull(Long merchantId) {
        return shopService.listShopReportPull(merchantId);
    }

    /**
     * 方法描述 : 删除店铺信息
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 14:20
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.3.0
     */
    public Result delShop(ShopDelDTO dto) {
        return shopService.delShop(dto);
    }

    /**
     * 方法描述 : 活动中心 - 新增营销活动选择门店
     *
     * @param dto 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/6 0006 10:18
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData < com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>>
     * @Since version-1.3.0
     */
    public Result<PageData<GoodPushShopVo>> shopMarketPage(ShopMarketPagDTO dto) {
        return shopService.shopMarketPage(dto);
    }

    /**
     * 方法描述 : 秒杀活动【通过城市编码查询微信端可展示门店id】
     *
     * @param dto 条件为空则不进行过滤
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 10:39
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    public Result<List<Long>> listShopIdByCity(ShopQueryDTO dto) {
        return shopService.listShopIdByCity(dto);
    }

    /**
     * 方法描述 : 通过手机号查询店铺信息
     * 1、pc端发送短信验证码
     *
     * @param phone 请求参数
     * @Author: MeiTao
     * @Date: 2020/8/19 0019 9:20
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    public Result<ShopDTO> getByPhone(String phone) {
        return shopService.getByPhone(phone);
    }


    /**
     * 方法描述 : 平台报表查询门店信息
     * 不包含已删除门店
     *
     * @param dto 条件为空则不进行过滤
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<List<MerchantShopInfoDTO>> reportShopQuery(ShopQueryDTO dto) {
        return shopService.reportShopQuery(dto);
    }

    /**
     * 方法描述 : pc - 报表门店下拉查询
     * 不包含已删除门店
     *
     * @Author: MeiTao
     * @Since version-1.4.0
     */
    public Result<List<ShopListV101DTO>> reportShopQuery() {
        return Result.succ(shopService.shopListV101());
    }

    /**
     * describe: 选择门店
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/3 14:16
     * @return: {@link Result< PageData< ShopChoicePageVO >>}
     * @version 1.4.0
     **/
    public Result<PageData<ShopChoicePageVO>> queryPageChoiceShop(ShopChoicePageDTO dto) {
        return Result.succ(shopService.queryPageChoiceShop(dto));
    }

    /**
     * describe: 根据id查询实体
     *
     * @param shopId
     * @author: yy
     * @date: 2020/9/14 17:00
     * @return: {@link CateringShopEntity}
     * @version 1.4.0
     **/
    public CateringShopEntity queryById(Long shopId) {
        if (shopId == null) {
            return null;
        }
        return shopService.getById(shopId);
    }

    /**
     * describe: 根据id集合查询多条实体
     *
     * @param idList
     * @author: yy
     * @date: 2020/9/14 17:01
     * @return: {@link List< CateringShopEntity>}
     * @version 1.4.0
     **/
    public List<CateringShopEntity> queryByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        return shopService.getBaseMapper().selectBatchIds(idList);
    }

    /**
     * describe: 查询员工列表
     *
     * @param dto
     * @return
     */
    public PageData<ShopEmployeeDTO> queryPageShopEmployee(EmployeeQueryPageDTO dto) {
        return cateringShopEmployeeService.queryPageShopEmployee(dto);
    }

    /**
     * 方法描述 ：新增/编辑员工信息
     *
     * @param shopEmployeeDTO 参数
     * @return 操作结果信息
     */
    public Long saveUpdateEmployeeInfo(ShopEmployeeDTO shopEmployeeDTO) {
        return cateringShopEmployeeService.saveUpdateEmployeeInfo(shopEmployeeDTO);
    }

    public String delEmployeeById(Long id) {
        return cateringShopEmployeeService.delEmployeeById(id);
    }

    public String delAllEmployee(Long shopId){
        return cateringShopEmployeeService.delAllEmployee(shopId);
    }

    public Result<List<Long>> delAllEmployeePc(Long shopId){
        return cateringShopEmployeeService.delAllEmployeePc(shopId);
    }

    public List<CateringShopEmployeeEntity> selectShopEmployee(Long shopId){
        return cateringShopEmployeeService.selectShopEmployee(shopId);
    }


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
     * @return
     */
    public Result syncShopInfoToDaDa(
            Long shopId,
            String shopName,
            String cityName,
            String areaName,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String shopContacts,
            String shopMobile) {
        shopService.syncShopInfoToDaDa(shopId, shopName, cityName, areaName, address, lat, lng, shopContacts, shopMobile);
        return Result.succ();
    }

    /**
     * 批量同步门店数据到达达
     *
     * @param shopIdList
     * @return
     */
    public Result batchSyncShopInfoToDaDa(List<Long> shopIdList) {
        shopService.batchSyncShopInfoToDaDa(shopIdList);
        return Result.succ();
    }

    public List<CateringShopEntity> list() {
       return shopService.list();
    }

    public Result handleShopDataV5() {
        return shopService.handleShopDataV5();
    }

    public CateringShopEmployeeEntity getEmployeeById(Long accountTypeId) {
        return shopService.getEmployeeById(accountTypeId);
    }


    public void handleAppTokenV5() {
        shopService.handleAppTokenV5();
    }
}
