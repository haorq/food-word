package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.base.ShopListV101DTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopListDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupPointRequestDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupPointResponseDTO;
import com.meiyuan.catering.merchant.dto.shop.*;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.vo.ShopTagInfoVo;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 店铺表(CateringShop)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 10:17:02
 */
@Mapper
public interface CateringShopMapper extends BaseMapper<CateringShopEntity>{
    /**
     * 查询所有店铺
     * @param dto
     * @return
     */
    List<MerchantShopListVo> listMerchantShopList(@Param("dto") MerchantShopListDTO dto);

    /**
     * 验证店铺信息是否重复
     * @param keyword 店铺名/注册手机号/详细地址/店铺编码
     * @param type 是否是店铺：true:是 ，false：否（自提点）
     * @return
     */
    List<CateringShopEntity> verifyShopInfoUnique(String keyword,Boolean type);

    /**
     * 方法描述 : 促销活动选择商户下拉列表
     * @Author: gz
     * @Date: 2020/3/24 14:44
     * @param keyWord 请求参数
     * @return: java.util.List<com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo>
     * @Since version-1.1.0
     */
    List<MerchantShopDTO> listMerchantSelect(String keyWord);

    /**
     * 查询商户标签
     * @return
     */
    List<ShopTagInfoVo> getAllShopTagInfo();

    /**
     * 查询所有店铺信息（未删除）
     * @return
     */
    List<ShopListV101DTO> shopListV101();

    /**
     * 方法描述 : pc-校验店铺信息是否重复
     * @Author: MeiTao
     * @Date: 2020/6/11 0011 14:07
     * @param dto
     * @return: List<CateringShopEntity>
     * @Since version-1.1.0
     */
    List<CateringShopEntity> verifyShopInfoUniqueDto(@Param("dto") ShopVerifyDTO dto);

    /**
     * 方法描述 : 查询店铺编码当前redis自增最大值
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:53
     * @param
     * @return: java.lang.Integer
     * @Since version-1.1.1
     */
    Integer getShopCodeMax();

    /**
     * 方法描述 :商品推送店铺查询(商品管理模块：推送菜品/商品 选择商户下拉列表查询)
     *     1、传入商户id对应下的门店，不包含传入店铺对应门店
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 15:47
     * @param page
     * @param dto 请求参数
     * @param set 请求参数
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>
     * @Since version-1.1.0
     */
    IPage<GoodPushShopVo> goodPushShopPage(@Param("page") Page page, @Param("dto") GoodPushShopPagDTO dto, @Param("set") Set<Long> set);

    /**
     * 方法描述 : 查询商品已推送门店
     *        传入商户id对应下的门店
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 15:59
     * @param page
     * @param dto 请求参数
     * @return: IPage<com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>
     * @Since version-1.1.0
     */
    IPage<GoodPushShopVo> goodPushedShopPage(Page page, GoodPushShopPagDTO dto);

    /**
     * 方法描述 : 获取微信端所有可用店铺市地址
     * @Author: MeiTao
     * @Date: 2020/7/13 0013 16:48
     * @param dto 请求参数
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.shop.ShopCityDTO>
     * @Since version-1.2.0
     */
    List<ShopCityDTO> listShopCity(@Param("dto")ShopCityQueryDTO dto);


    List<CateringShopEntity> listDistance12(@Param("lat")double lat,@Param("lng")double lng);

    /**
     * 方法描述 : 秒杀活动【通过城市编码查询微信端可展示门店id】
     * @Author: MeiTao
     * @Date: 2020/8/25 0025 17:47
     * @param cityCode 请求参数
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<Long> listShopIdByCity(@Param("cityCode") String cityCode);

    /**
     * 方法描述 : 查询门店已绑定自提点信息
     * @Author: MeiTao
     * @Date: 2020/8/28 0028 14:28
     * @param dto 请求参数
     * @return: java.util.List<com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity>
     * @Since version-1.3.0
     */
    List<PickupPointResponseDTO> listShopPickupAddress(@Param("dto")PickupPointRequestDTO dto);

    /**
     * 方法描述 : 查询门店未绑定自提点信息
     * @Author: MeiTao
     * @Date: 2020/8/28 0028 14:28
     * @param keyword 请求参数
     * @return: java.util.List<com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity>
     * @Since version-1.3.0
     */
    List<PickupPointResponseDTO> listShopPickupAddressNot(@Param("keyword")String keyword);

    /**
     * 方法描述 : 平台报表查询门店信息
     *          不包含已删除门店
     * @param dto 条件为空则不进行过滤
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 10:55
     * @param dto 请求参数
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.shop.MerchantShopInfoDTO>
     * @Since version-1.3.0
     */
    List<MerchantShopInfoDTO> reportShopQuery(@Param("dto")ShopQueryDTO dto);

    /**
     * describe: 选择门店
     * @author: yy
     * @date: 2020/9/3 14:37
     * @param page
     * @param dto
     * @return: {@link IPage< ShopChoicePageVO>}
     * @version 1.4.0
     **/
    IPage<ShopChoicePageVO> queryPageChoiceShop(@Param("page") Page page, @Param("dto") ShopChoicePageDTO dto);

    CateringShopEmployeeEntity getEmployeeById(@Param("id") Long id);
}
