package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.dto.seckill.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 营销秒杀(CateringMarketingSeckill)服务层
 *
 * @author gz
 * @Description 秒杀Service
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingSeckillService extends IService<CateringMarketingSeckillEntity> {

    /**
     * 方法描述: 新增/编辑<br>
     * 1、基本信息入库；
     * 2、同步ES；
     * 3、同步redis--秒杀信息、库存信息、销量信息；
     * 4、判断结束时间是否是当天，当天结束的活动需要加入到延迟队列自动下架；其他时间通过定时任务处理下架；
     *
     * @param dto  页面参数
     * @param list 商品转换数据
     * @author: gz
     * @date: 2020/6/23 14:09
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result insertOrUpdate(MarketingSeckillAddDTO dto, List<MarketingGoodsTransferDTO> list);

    /**
     * 方法描述: 通过id删除<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/6/23 14:10
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result delById(Long id);

    /**
     * 方法描述: 秒杀详情<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/6/23 14:10
     * @return: {@link Result< MarketingSeckillDetailsDTO>}
     * @version 1.1.1
     **/
    Result<MarketingSeckillDetailsDTO> findById(Long id);

    /**
     * 方法描述: 秒杀分页列表<br>
     *
     * @param pageParamDTO
     * @author: gz
     * @date: 2020/6/23 14:11
     * @return: {@link Result< PageData< MarketingSeckillListDTO>>}
     * @version 1.1.1
     **/
    Result<PageData<MarketingSeckillListDTO>> pageList(MarketingSeckillPageParamDTO pageParamDTO);

    /**
     * 方法描述: 上下架<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/6/23 14:11
     * @return: {@link Result}
     * @version 1.1.1
     **/
    Result updateUpDownStatus(MarketingSeckillUpDownDTO dto);

    /**
     * 方法描述: 商户秒杀列表<br>
     *
     * @param pageNo
     * @param pageSize
     * @param status
     * @param merchantId
     * @author: gz
     * @date: 2020/6/23 14:11
     * @return: {@link Result< PageData< SeckillMerchantListDTO>>}
     * @version 1.1.1
     **/
    Result<PageData<SeckillMerchantListDTO>> pageMerchantList(Long pageNo, Long pageSize, Integer status, Long merchantId);

    /**
     * 方法描述: 秒杀活动详情<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/6/23 14:11
     * @return: {@link Result< SeckillMerchantDetailsDTO>}
     * @version 1.1.1
     **/
    Result<SeckillMerchantDetailsDTO> getMerchantInfo(Long id);

    /**
     * 方法描述: 秒杀数据校验<br>
     *
     * @param dto
     * @author: gz
     * @date: 2020/6/23 14:12
     * @return: {@link boolean}
     * @version 1.1.1
     **/
    boolean verifySeckill(MarketingSeckillAddDTO dto);

    /**
     * 方法描述: 秒杀数据校验<br>
     *
     * @param dto
     * @author: GongJunZheng
     * @date: 2020/8/6 10:33
     * @return: void
     * @version 1.3.0
     **/
    void verifySeckill(MarketingSeckillAddOrEditDTO dto);


    /**
     * 方法描述: 获取秒杀实体<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/6/23 14:12
     * @return: {@link CateringMarketingSeckillEntity}
     * @version 1.1.1
     **/
    CateringMarketingSeckillEntity getById(Long id);

    /**
     * 方法描述: 通过id获取秒杀<br>
     *
     * @param id
     * @author: gz
     * @date: 2020/6/23 14:12
     * @return: {@link MarketingSeckillDTO}
     * @version 1.1.1
     **/
    MarketingSeckillDTO findOne(Long id);

    /**
     * 方法描述: 获取指定时间范围内参加活动的商家ids<br>
     *
     * @param begin       开始时间
     * @param end         结束时间
     * @param objectLimit 活动对象
     * @author: gz
     * @date: 2020/6/23 14:12
     * @return: {@link Map< Long, List< String>> key-商户id | value-商品id集合}
     * @version 1.1.1
     **/
    Map<Long, List<String>> listMerchantIds(LocalDateTime begin, LocalDateTime end, Integer objectLimit);

    /**
     * 方法描述: 获取所有的秒杀活动--同步ES<br>
     *
     * @param
     * @author: gz
     * @date: 2020/6/23 14:13
     * @return: {@link List< MarketingToEsDTO>}
     * @version 1.1.1
     **/
    List<MarketingToEsDTO> findAllForEs();

    /**
     * 方法描述: 通过商品id获取秒杀活动信息<br>
     *
     * @param goodsId
     * @author: gz
     * @date: 2020/6/23 14:13
     * @return: {@link List< MarketingToEsDTO>}
     * @version 1.1.1
     **/
    List<MarketingToEsDTO> findByGoodsIdForEs(Long goodsId);

    /**
     * 方法描述: 通过秒杀活动商品表主键id获取秒杀活动商品信息<br>
     *
     * @param seckillGoodsId
     * @param seckillEventId
     * @author: gz
     * @date: 2020/6/23 14:13
     * @return: {@link SeckillGoodsDetailsDTO}
     * @version 1.1.1
     **/
    SeckillGoodsDetailsDTO seckillGoodsInfo(Long seckillGoodsId, Long seckillEventId);

    /**
     * 方法描述: 通过秒杀活动主键id获取秒杀活动商品信息<br>
     *
     * @param seckillId 秒杀商品id
     * @author: gz
     * @date: 2020/6/23 14:14
     * @return: {@link List< SeckillGoodsDetailsDTO>}
     * @version 1.1.1
     **/
    List<SeckillGoodsDetailsDTO> listSeckillGoods(Long seckillId);

    /**
     * 方法描述: 秒杀消息-秒杀抢购成功后通知购物车<br>
     *
     * @param msgBody
     * @author: gz
     * @date: 2020/6/23 14:14
     * @return: {@link }
     * @version 1.1.1
     **/
    void sendSeckillMq(SeckillMsgBodyDTO msgBody);

    /**
     * 方法描述: <br>
     *
     * @param seckillGoodsId 活动商品主键id
     * @param userId         用户id --用户id不为null 就进行用户购买数量同步
     * @param number         数量
     * @param isLess         是否减库存 -- true：减库存；false--加库存
     * @param pay            是否支付
     * @author: gz
     * @date: 2020/6/23 14:14
     * @param seckillEventId 秒杀场次 v1.3.0
     * @return: {@link }
     * @version 1.1.1
     **/
    void syncSeckillInvertory(Long seckillGoodsId,Long userId,Integer number,boolean isLess,Boolean pay,Long seckillEventId);

    /**
     * 方法描述:  秒杀定时任务--定时下架已结束的活动<br>
     *
     * @param
     * @author: gz
     * @date: 2020/6/23 14:16
     * @return: {@link }
     * @version 1.1.1
     **/
    void seckillTimedTask();

    /**
     * 方法描述: 同步ES 活动数据<br>
     *
     * @param list
     * @author: gz
     * @date: 2020/6/23 14:17
     * @return: {@link }
     * @version 1.1.1
     **/
    void sendEsGoods(List<MarketingToEsDTO> list);

    /**
     * 方法描述: 获得商户当前进行中的秒杀活动<br>
     *
     * @param merchantId  商户ID
     * @param objectLimit 活动对象 2-企业、1-个人
     * @author: gz
     * @date: 2020/6/23 14:18
     * @return: {@link List< SeckillMerchantListDTO>}
     * @version 1.1.1
     **/
    List<SeckillMerchantListDTO> listSeckillingByMerchantId(Long merchantId, Integer objectLimit);

    /**
     * 店铺创建营销秒杀活动
     * 方法描述: 新增<br>
     * 1、基本信息入库；
     * 2、同步ES；
     *
     * @param dto     创建秒杀活动DTO
     * @param collect 秒杀活动商品DTO
     * @author: GongJunZheng
     * @date: 2020/8/6 9:51
     * @return: {@link Boolean}
     **/
    Boolean createOrEdit(MarketingSeckillAddOrEditDTO dto, List<MarketingGoodsTransferDTO> collect);

    /**
     * 冻结秒杀活动ID
     *
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 17:13
     * @return: {@link Boolean}
     **/
    Boolean freeze(Long seckillId);

    /**
     * 删除秒杀活动ID
     *
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 17:13
     * @return: {@link Boolean}
     **/
    Boolean del(Long seckillId);

    /**
     * 方法描述: 商户APP端-营销活动列表<br>
     *
     * @param shopId
     * @param queryDTO
     * @author: gz
     * @date: 2020/8/8 11:40
     * @return: {@link MarketingMerchantAppListVO}
     * @version 1.3.0
     **/
    PageData<MarketingMerchantAppListVO> listMarketing(Long shopId, SeckillMerchantPageParamDTO queryDTO);

    /**
     * 查询指定日期时间秒杀活动信息
     *
     * @param dateTime 日期时间
     * @param shopIds  店铺ID集合
     * @param userType
     * @author: GongJunZheng
     * @date: 2020/8/10 11:19
     * @return: {@link Result<List<CateringMarketingSeckillEntity>>}
     * @version V1.3.0
     **/
    List<CateringMarketingSeckillEntity> selectListByShopIds(LocalDateTime dateTime, List<Long> shopIds, Integer userType);

    /**
     * 方法描述 : 查询店铺是否有秒杀活动
     *
     * @param shopIds
     * @param type    是否是企业用户：true：是
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 9:47
     * @return: list
     * @Since version-1.3.0
     */
    List<Long> listShopHaveSeckill(List<Long> shopIds, Boolean type);

    /**
     * 方法描述 : 查询门店秒杀活动商品最低价格
     *
     * @param shopIds
     * @param type    请求参数
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 17:47
     * @return: Result<java.util.List   <   com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO>>
     * @Since version-1.3.0
     */
    List<ShopGrouponGoodsDTO> listGoodsMinPriceByShop(List<Long> shopIds, Boolean type);

    /**
     * 刷新有效秒杀活动的第二天每个场次的商品库存
     *
     * @author: GongJunZheng
     * @date: 2020/8/12 11:05
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    Boolean seckillEventGoodsTask();

    /**
     * 门店被删除，同步设置该门店的秒杀活动以及活动商品为删除状态
     *
     * @param shopId 门店ID
     * @author: GongJunZheng
     * @date: 2020/8/12 16:33
     * @version V1.3.0
     **/
    void shopDelSync(Long shopId);

    /**
    * 根据门店ID查询秒杀活动ID集合
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/14 14:48
    * @return: {@link List<Long>}
    * @version V1.3.0
    **/
    List<Long> selectByShopId(Long shopId);

    /**
     * 根据门店ID集合查询秒杀活动ID集合
     * @param shopIds 门店ID集合
     * @author: GongJunZheng
     * @date: 2020/8/14 14:48
     * @return: {@link List<Long>}
     * @version V1.3.0
     **/
    List<Long> selectByShopIds(List<Long> shopIds);
}
