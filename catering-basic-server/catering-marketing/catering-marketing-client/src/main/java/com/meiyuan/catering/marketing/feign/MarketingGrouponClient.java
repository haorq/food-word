package com.meiyuan.catering.marketing.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.groupon.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.vo.groupon.GrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.GrouponListVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author luohuan
 * @date 2020/5/19
 **/
@Service("cateringMarketingClient")
public class MarketingGrouponClient {

    @Autowired
    private CateringMarketingGrouponService cateringMarketingGrouponService;

    /**
     * 分页查询团购活动
     *
     * @param queryDTO 查询条件
     * @return Result<IPage < GrouponListVO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<IPage<GrouponListVO>> listPage(GrouponQueryDTO queryDTO) {
        return Result.succ(cateringMarketingGrouponService.listPage(queryDTO));
    }

    /**
     * 商家端：分页查询团购活动
     *
     * @param queryDTO   查询条件
     * @param merchantId 商户ID
     * @return Result<IPage < MerchantGrouponListVO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<IPage<MerchantGrouponListVO>> listPageOfMerchant(MerchantGrouponQueryDTO queryDTO, Long merchantId) {
        return Result.succ(cateringMarketingGrouponService.listPageOfMerchant(queryDTO, merchantId));
    }

    /**
     * 新增团购活动
     *
     * @param grouponDTO    团购ID
     * @param goodsTransferDtoS 商品集合
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result  create(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        cateringMarketingGrouponService.create(grouponDTO, goodsTransferDtoS);
        return Result.succ();
    }

    /**
     * 更新团购活动
     *
     * @param grouponDTO    团购DTO
     * @param goodsTransferDtoS 商品集合
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result update(GrouponDTO grouponDTO, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        cateringMarketingGrouponService.update(grouponDTO, goodsTransferDtoS);
        return Result.succ();
    }

    /**
     * 下/下架团购活动
     *
     * @param upDownDTO 上下架参数
     * @return Result<Boolean>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<Boolean> upDown(GrouponUpDownDTO upDownDTO) {
        boolean success = cateringMarketingGrouponService.upDown(upDownDTO);
        return Result.succ(success);
    }

    /**
     * 删除团购活动
     *
     * @param id 团购ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result delete(Long id) {
        cateringMarketingGrouponService.delete(id);
        return Result.succ();
    }

    /**
     * 团购活动详情
     *
     * @param id 团购ID
     * @return Result<GrouponDetailVO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<GrouponDetailVO> detail(Long id) {
        return Result.succ(cateringMarketingGrouponService.detail(id));
    }

    /**
     * 商家端：团购活动详情
     *
     * @param id 团购ID
     * @return Result<MerchantGrouponDetailVO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<MerchantGrouponDetailVO> detailOfMerchant(Long id) {
        return Result.succ(cateringMarketingGrouponService.detailOfMerchant(id));
    }

    /**
     * 开启虚拟成团
     *
     * @param id 团购ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result turnOnVirtual(Long id) {
        cateringMarketingGrouponService.turnOnVirtual(id);
        return Result.succ();
    }

    /**
     * 获取指定时间范围内参加活动的商品ids
     *
     * @param begin 活动开始时间
     * @param end   活动结束时间
     * @return Result<Map < Long, List < String>>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<Map<Long, List<String>>> listGoodsIds(LocalDateTime begin, LocalDateTime end, Integer objectLimit) {
        Map<Long, List<String>> map = cateringMarketingGrouponService.listGoodsIds(begin, end, objectLimit);
        return Result.succ(map);
    }

    /**
     * 统计时间范围内某个商品活动参加次数
     *
     * @param begin            活动开始时间
     * @param end              活动结束时间
     * @param goodsId          商品ID
     * @param ignoredGrouponId 忽略的团购ID
     * @return Result<Long>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<Long> countByTimeAndGoodsId(LocalDateTime begin, LocalDateTime end,
                                              Long goodsId, Long ignoredGrouponId) {
        long count = cateringMarketingGrouponService.countByTimeAndGoodsId(begin, end, goodsId, ignoredGrouponId);
        return Result.succ(count);
    }

    /**
     * 查询所有的团购活动活动（供ES使用）
     *
     * @return Result<List < MarketingEsDTO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<List<MarketingToEsDTO>> findAllForEs() {
        List<MarketingToEsDTO> list = cateringMarketingGrouponService.findAllForEs();
        return Result.succ(list);
    }

    /**
     * 根据商品ID查询营销商品
     *
     * @param goodsId 商品ID
     * @return Result<List < MarketingEsDTO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<List<MarketingToEsDTO>> findByGoodsIdForEs(Long goodsId) {
        List<MarketingToEsDTO> list = cateringMarketingGrouponService.findByGoodsIdForEs(goodsId);
        return Result.succ(list);
    }

    /**
     * 团购定时任务-->定时下架已结束的团购活动
     *
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result grouponDownTimedTask() {
        cateringMarketingGrouponService.grouponDownTimedTask();
        return Result.succ();
    }

    /**
     * 根据ID查询团购活动
     *
     * @param id 团购ID
     * @return Result<GrouponDetailDTO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<GrouponDetailDTO> findById(Long id) {
        return Result.succ(cateringMarketingGrouponService.findById(id));
    }

    /**
    * 新增团购活动V1.3.0
    * @param dto    团购信息
    * @param goodsTransferDtoS 商品集合
    * @author: GongJunZheng
    * @date: 2020/8/9 15:01
    * @return: {@link Result}
    * @version V1.3.0
    **/
    public Result createOrEdit(MarketingGrouponAddOrEditDTO dto, List<MarketingGoodsTransferDTO> goodsTransferDtoS) {
        cateringMarketingGrouponService.createOrEdit(dto, goodsTransferDtoS);
        return Result.succ();
    }

    /**
     * 功能描述: 团购数据校验<br>
     * @Param: MarketingGrouponAddOrEditDTO
     * @Return: Boolean
     * @Author: GongJunZheng
     * @Date: 2020/8/6 10:33
     * @Version 1.3.0
     */
    public Result verifyGroupon(MarketingGrouponAddOrEditDTO dto) {
        cateringMarketingGrouponService.verifyGroupon(dto);
        return Result.succ();
    }

    /**
    * 根据团购活动ID查询信息
    * @param grouponId 团购活动ID
    * @author: GongJunZheng
    * @date: 2020/8/9 16:10
    * @return: {@link Result<CateringMarketingGrouponEntity>}
    * @version V1.3.0
    **/
    public Result<CateringMarketingGrouponEntity> getById(Long grouponId) {
        return Result.succ(cateringMarketingGrouponService.getById(grouponId));
    }

    /**
    * 冻结团购活动
    * @param grouponId 团购活动ID
    * @author: GongJunZheng
    * @date: 2020/8/9 16:49
    * @return: {@link Result<Boolean>}
    * @version V1.3.0
    **/
    public Result<Boolean> freeze(Long grouponId) {
        return Result.succ(cateringMarketingGrouponService.freeze(grouponId));
    }

    /**
    * 删除团购活动
    * @param grouponId 团购活动ID
    * @author: GongJunZheng
    * @date: 2020/8/9 16:50
    * @return: {@link Result<Boolean>}
    * @version V1.3.0
    **/
    public Result<Boolean> del(Long grouponId) {
        return Result.succ(cateringMarketingGrouponService.del(grouponId));
    }

    /**
     * 方法描述 : 查询店铺是否有团购活动
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 9:47
     * @param shopIds
     * @param type 是否是企业用户：true：是
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.groupon.GrouponDetailDTO>
     * @Since version-1.3.0
     */
    public Result<List<Long>> listShopHaveGroupon(List<Long> shopIds,Boolean type) {
        return Result.succ(cateringMarketingGrouponService.listShopHaveGroupon(shopIds,type));
    }

    /**
     * 方法描述 : 查询店铺所有活动中商品对应最低价格
     * @Author: MeiTao
     * @Date: 2020/8/10 0010 16:44
     * @param shopIds 店铺id
     * @return: List<ShopGrouponGoodsDTO>
     * @Since version-1.3.0
     */
    public Result<List<ShopGrouponGoodsDTO>> listGoodsMinPriceByShop(List<Long> shopIds,Boolean type) {
        return Result.succ(cateringMarketingGrouponService.listGoodsMinPriceByShop(shopIds,type));
    }

    /**
    * 根据店铺ID查询最小营销团购商品价格
    * @param shopId 店铺ID
    * @param objectLimit 用户类型
    * @author: GongJunZheng
    * @date: 2020/8/11 18:02
    * @return: {@link BigDecimal}
    * @version V1.3.0
    **/
    public Result<BigDecimal> minPriceByShopId(Long shopId,Integer objectLimit) {
        return Result.succ(cateringMarketingGrouponService.minPriceByShopId(shopId,objectLimit));
    }


    /**
     * 方法描述   获取团购详情
     * @author: lhm
     * @date: 2020/8/12 10:17
     * @param grouponId
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<MerchantGrouponDetailVO> getDetailGroupon(Long grouponId) {
        return Result.succ(cateringMarketingGrouponService.getDetailGroupon(grouponId));
    }

    /**
    * 开启或者关闭团购虚拟成团V1.3.0
    * @param grouponId 团购活动ID
    * @author: GongJunZheng
    * @date: 2020/8/17 13:55
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    public Result<Boolean> openOrCloseVirtual(Long grouponId) {
        return Result.succ(cateringMarketingGrouponService.openOrCloseVirtual(grouponId));
    }
}
