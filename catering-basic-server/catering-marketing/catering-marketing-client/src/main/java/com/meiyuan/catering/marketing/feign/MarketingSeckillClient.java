package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.dto.seckill.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CateringMarketingSeckillClient
 * @Description 秒杀Client
 * @Author gz
 * @Date 2020/5/19 11:48
 * @Version 1.1
 */
@Service
public class MarketingSeckillClient {

    @Autowired
    private CateringMarketingSeckillService seckillService;

    /**
     * 功能描述: 新增/编辑<br>
     *
     * @Param: [dto页面参数，list商品转换数据]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 14:38
     * @Version 1.0.0、1.0.1
     */
    public Result insertOrUpdate(MarketingSeckillAddDTO dto, List<MarketingGoodsTransferDTO> list) {
        return seckillService.insertOrUpdate(dto, list);
    }

    /**
     * 功能描述: 通过id删除<br>
     *
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     * @Version 1.0.0
     */
    public Result delById(Long id) {
        return seckillService.delById(id);
    }

    /**
     * 功能描述: 秒杀详情<br>
     *
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/16 17:43
     * @Version 1.0.0
     */
    public Result<MarketingSeckillDetailsDTO> findById(Long id) {
        return seckillService.findById(id);
    }

    /**
     * 功能描述: 秒杀分页列表<br>
     *
     * @Param: [pageParamDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData       <       com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillListDTO>>
     * @Author: gz
     * @Date: 2020/3/16 18:11
     * @Version 1.0.0
     */
    public Result<PageData<MarketingSeckillListDTO>> pageList(MarketingSeckillPageParamDTO pageParamDTO) {
        return seckillService.pageList(pageParamDTO);
    }

    /**
     * 功能描述: 上下架<br>
     *
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     * @Version 1.0.0
     */
    public Result updateUpDownStatus(MarketingSeckillUpDownDTO dto) {
        return seckillService.updateUpDownStatus(dto);
    }

    /**
     * 功能描述: 秒杀数据校验<br>
     *
     * @Param: MarketingSeckillAddDTO
     * @Return: Boolean
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<Boolean> verifySeckill(MarketingSeckillAddDTO dto) {
        return Result.succ(seckillService.verifySeckill(dto));
    }

    /**
     * 功能描述: 秒杀数据校验<br>
     *
     * @Param: MarketingSeckillDTO
     * @Return: Boolean
     * @Author: GongJunZheng
     * @Date: 2020/8/6 10:33
     * @Version 1.3.0
     */
    public Result verifySeckill(MarketingSeckillAddOrEditDTO dto) {
        seckillService.verifySeckill(dto);
        return Result.succ();
    }

    /**
     * 功能描述: 商户秒杀列表<br>
     *
     * @Param: [pageNo, pageSize, status]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData       <       com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantListDTO>>
     * @Author: gz
     * @Date: 2020/3/21 10:10
     * @Version 1.0.0
     */
    public Result<PageData<SeckillMerchantListDTO>> pageMerchantList(Long pageNo, Long pageSize, Integer status, Long merchantId) {
        return seckillService.pageMerchantList(pageNo, pageSize, status, merchantId);
    }

    /**
     * 功能描述: 秒杀活动详情<br>
     *
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<SeckillMerchantDetailsDTO> getMerchantInfo(Long id) {
        return seckillService.getMerchantInfo(id);
    }

    /**
     * 功能描述: 获取指定时间范围内参加活动的商家ids<br>
     *
     * @Param: begin　开始时间
     * @Param: end　结束时间
     * @Param: objectLimit　活动对象
     * @Return: Map<Long       ,       List       <       String>>　key-商户id | value-商品id集合
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<Map<Long, List<String>>> listMerchantIds(LocalDateTime begin, LocalDateTime end, Integer objectLimit) {
        return Result.succ(seckillService.listMerchantIds(begin, end, objectLimit));
    }

    /**
     * 功能描述: 获取所有的秒杀活动--同步ES<br>
     *
     * @Param:
     * @Return: List<MarketingEsDTO>
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<List<MarketingToEsDTO>> findAllForEs() {
        return Result.succ(seckillService.findAllForEs());
    }


    /**
     * 功能描述: 通过id获取秒杀<br>
     *
     * @Param: id
     * @Return: CateringMarketingSeckillEntity
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<MarketingSeckillDTO> findOne(Long id) {
        return Result.succ(seckillService.findOne(id));
    }

    /**
     * 功能描述: 通过商品id获取秒杀活动信息<br>
     *
     * @Param: goodsId
     * @Return: List<MarketingEsDTO>
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<List<MarketingToEsDTO>> findByGoodsIdForEs(Long goodsId) {
        return Result.succ(seckillService.findByGoodsIdForEs(goodsId));
    }

    /**
     * 功能描述: 通过秒杀活动商品表主键id获取秒杀活动商品信息<br>
     *
     * @Param: seckillGoodsId 秒杀商品id
     * @Return: SeckillGoodsDetailsDTO
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result<SeckillGoodsDetailsDTO> seckillGoodsInfo(Long seckillGoodsId, Long seckillEventId) {
        return Result.succ(seckillService.seckillGoodsInfo(seckillGoodsId, seckillEventId));
    }

    /**
     * 功能描述: 秒杀消息-秒杀抢购成功后通知购物车<br>
     *
     * @Param: SeckillMsgBodyDTO
     * @Return: void
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result sendSeckillMq(SeckillMsgBodyDTO msgBody) {
        seckillService.sendSeckillMq(msgBody);
        return Result.succ();
    }

    /**
     * 同步秒杀商品库存--数据库操作
     * @param seckillGoodsId  活动商品主键id
     * @param number  数量
     * @param userId  用户id --用户id不为null 就进行用户购买数量同步
     * @param isLess 是否减库存 -- true：减库存；false--加库存
     * @param pay 是否支付
     * @param seckillEventId 秒杀场次 v1.3.0
     */
    public Result syncSeckillInvertory(Long seckillGoodsId,Long userId,Integer number,boolean isLess,Boolean pay,Long seckillEventId){
        seckillService.syncSeckillInvertory(seckillGoodsId,userId,number,isLess,pay,seckillEventId);
        return Result.succ();
    }

    /**
     * 功能描述: 秒杀定时任务--定时下架已结束的活动<br>
     *
     * @Param:
     * @Return: void
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result seckillTimedTask() {
        seckillService.seckillTimedTask();
        return Result.succ();
    }

    /**
     * 功能描述: 同步ES 活动数据<br>
     *
     * @Param: List<MarketingEsDTO>
     * @Return: void
     * @Author: gz
     * @Date: 2020/3/21 10:33
     * @Version 1.0.0
     */
    public Result sendEsGoods(List<MarketingToEsDTO> list) {
        seckillService.sendEsGoods(list);
        return Result.succ();
    }

    /**
     * @param queryDTO merchantId 商户ID
     * @return
     * @description 获得商户当前进行中的秒杀活动
     * @author yaozou
     * @date 2020/6/1 11:11
     * @since v1.1.0
     */
    public Result<List<SeckillMerchantListDTO>> listSeckillingByMerchantId(MarketingSeckillQueryDTO queryDTO) {
        return Result.succ(seckillService.listSeckillingByMerchantId(queryDTO.getMerchantId(), queryDTO.getObjectLimit()));
    }

    /**
     * 创建/编辑秒杀活动
     *
     * @param dto     秒杀基本信息
     * @param collect 秒杀商品信息集合
     * @author: GongJunZheng
     * @date: 2020/8/6 15:53
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    public Result<Boolean> createOrEdit(MarketingSeckillAddOrEditDTO dto, List<MarketingGoodsTransferDTO> collect) {
        return Result.succ(seckillService.createOrEdit(dto, collect));
    }

    /**
     * 根据秒杀ID获取秒杀实体信息
     *
     * @param seckillId 秒杀ID
     * @author: GongJunZheng
     * @date: 2020/8/6 15:55
     * @return: {@link CateringMarketingSeckillEntity}
     * @version 1.3.0
     **/
    public CateringMarketingSeckillEntity getById(Long seckillId) {
        return seckillService.getById(seckillId);
    }

    /**
     * 冻结秒杀活动
     *
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 17:07
     * @return: {@link Result<Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> freeze(Long seckillId) {
        return Result.succ(seckillService.freeze(seckillId));
    }

    /**
     * 删除秒杀活动
     *
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 17:40
     * @return: {@link Result<Boolean>}
     * @version 1.3.0
     **/
    public Result<Boolean> del(Long seckillId) {
        return Result.succ(seckillService.del(seckillId));
    }

    /**
     * app-营销活动列表
     *
     * @param shopId
     * @param queryDTO
     * @return
     */
    public Result<PageData<MarketingMerchantAppListVO>> listMarketing(Long shopId, SeckillMerchantPageParamDTO queryDTO) {
        return Result.succ(seckillService.listMarketing(shopId, queryDTO));
    }

    /**
     * 查询指定日期时间秒杀活动信息
     *
     * @param dateTime 日期时间
     * @param shopIds  店铺ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 11:19
     * @return: {@link Result<List<CateringMarketingSeckillEntity>>}
     * @version V1.3.0
     **/
    public Result<List<CateringMarketingSeckillEntity>> selectListByShopIds(LocalDateTime dateTime, List<Long> shopIds, Integer userType) {
        return Result.succ(seckillService.selectListByShopIds(dateTime, shopIds, userType));
    }

    /**
     * 方法描述 : 查询店铺是否有秒杀活动
     *
     * @param shopIds
     * @param type    是否是企业用户：true：是
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 9:47
     * @return: Result<List       <       Long>>
     * @Since version-1.3.0
     */
    public Result<List<Long>> listShopHaveSeckill(List<Long> shopIds, Boolean type) {
        return Result.succ(seckillService.listShopHaveSeckill(shopIds, type));
    }

    /**
     * 方法描述 : 查询门店秒杀活动商品最低价格
     *
     * @param shopIds
     * @param type    请求参数
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 17:47
     * @return: Result<java.util.List       <       com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO>>
     * @Since version-1.3.0
     */
    public Result<List<ShopGrouponGoodsDTO>> listGoodsMinPriceByShop(List<Long> shopIds, Boolean type) {
        return Result.succ(seckillService.listGoodsMinPriceByShop(shopIds, type));
    }

    /**
     * 刷新有效秒杀活动的第二天每个场次的商品库存
     *
     * @author: GongJunZheng
     * @date: 2020/8/12 11:05
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    public Result<Boolean> seckillEventGoodsTask() {
        return Result.succ(seckillService.seckillEventGoodsTask());
    }
}
