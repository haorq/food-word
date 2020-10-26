package com.meiyuan.catering.admin.service.marketing;

import com.google.common.collect.Lists;
import com.meiyuan.catering.admin.service.goods.AdminGoodsService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.seckill.*;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName AdminMarketingSeckillService
 * @Description 促销秒杀业务层
 * @Author gz
 * @Date 2020/3/16 14:51
 * @Version 1.1
 */
@Slf4j
@Service
public class AdminMarketingSeckillService {

    @Autowired
    private MarketingSeckillClient seckillClient;
    @Autowired
    private AdminGoodsService goodsService;
    /**
     * 功能描述: 新增/编辑<br>
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 14:53
     */
    public Result insertOrUpdate(MarketingSeckillAddDTO dto){
        if (CollectionUtils.isEmpty(dto.getGoodsItem())) {
            return Result.fail("未选择商品");
        }
        Result<Boolean> verifyResult = seckillClient.verifySeckill(dto);
        if(verifyResult.success() && !verifyResult.getData()){
            return Result.fail("该时间内,已经存在相同名称的活动");
        }
        List<MarketingGoodsAddDTO> goodsDtos = dto.getGoodsItem();
        Map<String, MarketingGoodsAddDTO> dtoMap = goodsDtos.stream().collect(Collectors.toMap(MarketingGoodsAddDTO::getSku, Function.identity()));
        ArrayList<String> skuList = Lists.newArrayList(dtoMap.keySet());
        Result<List<GoodsBySkuDTO>> result = goodsService.listGoodsBySkuCodeList(skuList,dto.getShopId());
        if(result.failure()){
            log.error("通过商品sku集合调动商品服务获取商品数据失败!params={},result={}",skuList,result);
            return Result.fail("商品服务调用错误");
        }
        List<GoodsBySkuDTO> goodsList = result.getData();
        log.debug("调用商品服务获取商品信息:{}",goodsList);
        if(CollectionUtils.isEmpty(goodsList)){
            return Result.fail("没有获取到商品数据");
        }
        List<MarketingGoodsTransferDTO> collect
                = goodsList.stream().map(e -> goodsConvert(e,dtoMap.get(e.getSkuCode()))).collect(Collectors.toList());
        return seckillClient.insertOrUpdate(dto,collect);
    }
    /**
     * 功能描述: 通过id删除<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     */
    public Result remove(Long id) {
        return seckillClient.delById(id);
    }
    /**
     * 功能描述: 秒杀详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/16 17:43
     */
    public Result<MarketingSeckillDetailsDTO> getInfo(Long id) {
        return seckillClient.findById(id);
    }
    /**
     * 功能描述: 秒杀分页列表<br>
     * @Param: [pageParamDTO]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillListDTO>>
     * @Author: gz
     * @Date: 2020/3/16 18:11
     */
    public Result<PageData<MarketingSeckillListDTO>> page(MarketingSeckillPageParamDTO pageParamDTO) {
        return seckillClient.pageList(pageParamDTO);
    }
    /**
     * 功能描述: 上下架<br>
     * @Param: [dto]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/16 17:00
     */
    public Result upDown(MarketingSeckillUpDownDTO dto) {
        return seckillClient.updateUpDownStatus(dto);
    }




    /**
     * 商品数据DTO转换
     * @param dto
     * @return
     */
    private MarketingGoodsTransferDTO goodsConvert(GoodsBySkuDTO dto,MarketingGoodsAddDTO addDTO){
        MarketingGoodsTransferDTO tarDto = new MarketingGoodsTransferDTO();
        tarDto.setCode(dto.getSpuCode());
        tarDto.setGoodsId(dto.getGoodsId());
        tarDto.setGoodsName(dto.getGoodsName());
        tarDto.setGoodsPicture(dto.getInfoPicture());
        tarDto.setSku(dto.getSkuCode());
        tarDto.setSkuValue(dto.getPropertyValue());
        // 商品价格 -原价
        tarDto.setStorePrice(dto.getMarketPrice());
        tarDto.setActivityPrice(addDTO.getSalesPrice());
        tarDto.setLimitQuantity(addDTO.getLimitQuantity());
        tarDto.setQuantity(addDTO.getQuantity());
        tarDto.setMinQuantity(addDTO.getMinQuantity());
        tarDto.setLabelList(dto.getLabelNames());
        tarDto.setGoodsDesc(dto.getGoodsDescribeText());
        return tarDto;
    }
}
