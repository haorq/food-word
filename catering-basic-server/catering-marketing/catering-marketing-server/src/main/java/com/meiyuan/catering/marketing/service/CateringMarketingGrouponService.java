package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.vo.groupon.GrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.GrouponListVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponListVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 营销团购(CateringMarketingGroupon)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingGrouponService extends IService<CateringMarketingGrouponEntity> {

    /**
     * 分页查询团购活动
     *
     * @param queryDTO
     * @return
     */
    IPage<GrouponListVO> listPage(GrouponQueryDTO queryDTO);

    /**
     * 商家端：分页查询团购活动
     *
     * @param queryDTO
     * @param merchantId
     * @return
     */
    IPage<MerchantGrouponListVO> listPageOfMerchant(MerchantGrouponQueryDTO queryDTO, Long merchantId);

    /**
     * 新增团购活动
     *  @param grouponDTO
     * @param goodsTransferDtoS
     */
    void create(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS);

    /**
     * 更新团购活动
     *  @param grouponDTO
     * @param goodsTransferDtoS
     */
    void update(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS);

    /**
     * 上/下架团购活动
     *
     * @param upDownDTO@return
     */
    boolean upDown(GrouponUpDownDTO upDownDTO);

    /**
     * 删除团购活动
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 团购活动详情
     *
     * @param id
     * @return
     */
    GrouponDetailVO detail(Long id);

    /**
     * 商家端：团购活动详情
     *
     * @param id
     * @return
     */
    MerchantGrouponDetailVO detailOfMerchant(Long id);

    /**
     * 开启虚拟成团
     *
     * @param id
     */
    void turnOnVirtual(Long id);

    /**
     * 方法描述: 获取指定时间范围内参加活动的商品ids<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:53
     * @param begin
     * @param end
     * @param objectLimit
     * @return: {@link Map< Long, List< String>>}
     * @version 1.1.1
     **/
    Map<Long, List<String>> listGoodsIds(LocalDateTime begin, LocalDateTime end,Integer objectLimit);

    /**
     * 方法描述: 统计时间范围内某个商品活动参加次数<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:54
     * @param begin
     * @param end
     * @param goodsId
     * @param ignoredGrouponId
     * @return: {@link long}
     * @version 1.1.1
     **/
    long countByTimeAndGoodsId(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end,
                               @Param("goodsId") Long goodsId, @Param("ignoredGrouponId") Long ignoredGrouponId);

    /**
     * 查询所有的团购活动活动（供ES使用）
     *
     * @return
     */
    List<MarketingToEsDTO> findAllForEs();
    /**
     * 方法描述: 通过商品id获取团购信息--同步Es<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:54
     * @param goodsId
     * @return: {@link List< MarketingToEsDTO>}
     * @version 1.1.1
     **/
    List<MarketingToEsDTO> findByGoodsIdForEs(Long goodsId);

    /**
     * 方法描述: 团购定时任务-->定时下架已结束的团购活动<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:58
     * @param
     * @return: {@link }
     * @version 1.1.1
     **/
    void grouponDownTimedTask();
    /**
     * 方法描述: 通过id获取团购详情<br>
     *
     * @author: gz
     * @date: 2020/6/23 13:58
     * @param id
     * @return: {@link GrouponDetailDTO}
     * @version 1.1.1
     **/
    GrouponDetailDTO findById(Long id);

    /**
     * 方法描述 : 查询当前时间有团购活动的店铺id
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 9:47
     * @param shopIds
     * @param type 是否是企业用户：true：是
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.groupon.GrouponDetailDTO>
     * @Since version-1.3.0
     */
    List<Long> listShopHaveGroupon(List<Long> shopIds, Boolean type);
    /**
     * 新增/编辑团购活动V1.3.0
     * @param dto    团购信息
     * @param goodsTransferDtoS 商品集合
     * @author: GongJunZheng
     * @date: 2020/8/9 15:01
     * @return: {@link Result}
     * @version V1.3.0
     **/
    void createOrEdit(MarketingGrouponAddOrEditDTO dto, List<MarketingGoodsTransferDTO> goodsTransferDtoS);

    /**
     * 功能描述: 团购数据校验<br>
     * @Author: GongJunZheng
     * @Date: 2020/8/6 10:33
     * @param dto
     * @return: void
     * @version 1.3.0
     */
    void verifyGroupon(MarketingGrouponAddOrEditDTO dto);

    /**
     * 冻结团购活动
     * @param grouponId 团购活动ID
     * @author: GongJunZheng
     * @date: 2020/8/9 16:49
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean freeze(Long grouponId);

    /**
     * 删除团购活动
     * @param grouponId 团购活动ID
     * @author: GongJunZheng
     * @date: 2020/8/9 16:50
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean del(Long grouponId);

    /**
     * 方法描述 : 查询店铺所有活动中商品对应最低价格
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 16:44
     * @param shopIds 店铺ids
     * @param type 是否是企业用户
     * @return: List<ShopGrouponGoodsDTO>
     * @Since version-1.3.0
     */
    List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(List<Long> shopIds,Boolean type);

    /**
     * 根据店铺ID查询最小营销团购商品价格
     * @param shopId 店铺ID
     * @param objectLimit 用户类型
     * @author: GongJunZheng
     * @date: 2020/8/11 18:02
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal minPriceByShopId(Long shopId,Integer objectLimit);


    /**
     * 方法描述   获取团购详情
     * @author: lhm
     * @date: 2020/8/12 10:17
     * @param grouponId
     * @return: {@link }
     * @version 1.3.0
     **/
    MerchantGrouponDetailVO getDetailGroupon(Long grouponId);

    /**
    * 门店被删除，同步设置该门店的团购活动以及活动商品为删除状态
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/12 16:32
    * @version V1.3.0
    **/
    void shopDelSync(Long shopId);

    /**
    * 根据门店ID查询有效营销团购活动ID集合
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/14 14:56
    * @return: {@link List<Long>}
    * @version V1.3.0
    **/
    List<Long> selectByShopId(Long shopId);
    
    /**
    * 根据门店ID集合查询营销团购活动ID集合
    * @param shopIds 门店ID集合
    * @author: GongJunZheng
    * @date: 2020/8/26 10:32
    * @return: {@link }
    * @version V1.3.0
    **/
    List<Long> selectByShopIds(List<Long> shopIds);

    /**
     * 开启或者关闭团购虚拟成团V1.3.0
     * @param grouponId 团购活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 13:55
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean openOrCloseVirtual(Long grouponId);

    /**
     * V1.4.0 发送团购活动结束MQ消息（测试专用）
     * @param id 团购活动ID
     * @param endTime 团购活动结束时间
     * @param down 上/下架枚举
     * @author: GongJunZheng
     * @date: 2020/9/23 10:15
     * @return: void
     * @version V1.4.0
     **/
    void sendGrouponTimedTaskMsg(Long id, LocalDateTime endTime, MarketingUpDownStatusEnum down);
}
