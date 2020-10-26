package com.meiyuan.catering.admin.service.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.service.goods.AdminGoodsService;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.groupon.GrouponDTO;
import com.meiyuan.catering.marketing.dto.groupon.GrouponQueryDTO;
import com.meiyuan.catering.marketing.dto.groupon.GrouponUpDownDTO;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.service.CateringMarketingGroupOrderService;
import com.meiyuan.catering.marketing.vo.groupon.GrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.GrouponListVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/3/17
 * 团购服务
 **/
@Service
public class AdminMarketingGrouponService {

    @Autowired
    private MarketingGrouponClient marketingFeignClient;

    @Autowired
    private AdminGoodsService goodsService;
    @Autowired
    private CateringMarketingGroupOrderService groupOrderService;

    /**
     * 分页查询团购活动
     *
     * @param queryDTO
     * @return
     */
    public Result<IPage<GrouponListVO>> listPage(GrouponQueryDTO queryDTO) {
        return marketingFeignClient.listPage(queryDTO);
    }


    /**
     * 新增团购活动
     *
     * @param grouponDTO
     */
    public Result create(GrouponDTO grouponDTO) {
        List<MarketingGoodsTransferDTO> goodsTransferDtoS = getGoodsTransferDtoS(grouponDTO);
        return marketingFeignClient.create(grouponDTO, goodsTransferDtoS);
    }

    /**
     * GrouponDTO转换为MarketingGoodsTransferDTO
     *
     * @param grouponDTO
     * @return
     */
    private List<MarketingGoodsTransferDTO> getGoodsTransferDtoS(GrouponDTO grouponDTO) {
        Map<String, GoodsBySkuDTO> skuDtoMap = getGoodsBySkuDtoMap(grouponDTO);

        return grouponDTO.getGrouponGoodsList().stream()
                .map(goodsDTO -> {
                    GoodsBySkuDTO goodsBySkuDTO = skuDtoMap.get(goodsDTO.getSkuCode());
                    MarketingGoodsTransferDTO goodsTransferDTO = new MarketingGoodsTransferDTO();
                    goodsTransferDTO.setGoodsId(goodsBySkuDTO.getGoodsId());
                    goodsTransferDTO.setCode(goodsBySkuDTO.getSpuCode());
                    goodsTransferDTO.setGoodsName(goodsBySkuDTO.getGoodsName());
                    goodsTransferDTO.setMinQuantity(goodsDTO.getMinQuantity());
                    goodsTransferDTO.setActivityPrice(goodsDTO.getActivityPrice());
                    goodsTransferDTO.setStorePrice(goodsBySkuDTO.getMarketPrice());
                    goodsTransferDTO.setLabelList(goodsBySkuDTO.getLabelNames());
                    goodsTransferDTO.setGoodsPicture(goodsBySkuDTO.getInfoPicture());
                    goodsTransferDTO.setGoodsDesc(goodsBySkuDTO.getGoodsDescribeText());
                    goodsTransferDTO.setMinGrouponQuantity(goodsDTO.getMinGrouponQuantity());
                    goodsTransferDTO.setSku(goodsDTO.getSkuCode());
                    goodsTransferDTO.setSkuValue(goodsDTO.getPropertyValue());
                    return goodsTransferDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 获取规格商品列表
     *
     * @param grouponDTO
     * @return
     */
    private Map<String, GoodsBySkuDTO> getGoodsBySkuDtoMap(GrouponDTO grouponDTO) {
        List<String> skuCodes = grouponDTO.getGrouponGoodsList().stream()
                .map(goodsDTO -> goodsDTO.getSkuCode())
                .collect(Collectors.toList());
        Result<List<GoodsBySkuDTO>> goodsResult = goodsService.listGoodsBySkuCodeList(skuCodes, grouponDTO.getShopId());
        if (goodsResult.failure() || CollectionUtils.isEmpty(goodsResult.getData())) {
            throw new CustomException("没有获取到商品数据");
        }
        List<GoodsBySkuDTO> skuDtoS = goodsResult.getData();
        return skuDtoS.stream().collect(Collectors.toMap(GoodsBySkuDTO::getSkuCode, Function.identity()));
    }


    /**
     * 更新团购活动
     *
     * @param grouponDTO
     */
    public Result update(GrouponDTO grouponDTO) {
        List<MarketingGoodsTransferDTO> goodsTransferDtoS = getGoodsTransferDtoS(grouponDTO);
        Result result = marketingFeignClient.update(grouponDTO, goodsTransferDtoS);
        // 下架结束团购
        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(grouponDTO.getUpDown())) {
            groupOrderService.endGroup(grouponDTO.getId(), true);
        }
        return result;
    }

    /**
     * 下/下架团购活动
     *
     * @param upDownDTO@return
     */
    public Result<Boolean> upDown(GrouponUpDownDTO upDownDTO) {
        return marketingFeignClient.upDown(upDownDTO);
    }

    /**
     * 删除团购活动
     *
     * @param id
     */
    public Result delete(Long id) {
        return marketingFeignClient.delete(id);
    }

    /**
     * 团购活动详情
     *
     * @param id
     * @return
     */
    public Result<GrouponDetailVO> detail(Long id) {
        return marketingFeignClient.detail(id);
    }
}
