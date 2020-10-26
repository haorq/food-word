package com.meiyuan.catering.admin.web.es;

import com.meiyuan.catering.admin.service.es.AdminEsService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponAloneTestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author wxf
 * @date 2020/3/26 15:30
 * @description 简单描述
 **/
@RestController
@RequestMapping("admin/es")
public class AdminEsController {
    @Resource
    AdminEsService esService;

    /**
     * 同步ES 数据
     *
     * @author: wxf
     * @date: 2020/3/26 15:31
     * @return: {@link Result < String>}
     **/
    @GetMapping("/pushEs")
    @ApiOperation("同步ES数据")
    public Result  pushEs() {
       return esService.pushMerchantGoodsEs();
    }

    /**
     * 方法描述 : 同步店铺ES 数据
     * @Author: MeiTao
     * @Date: 2020/7/13 0013 13:51
     * @return: Result<java.lang.String>
     * @Since version-1.2.0
     */
    @GetMapping("/pushShopEs")
    @ApiOperation("同步店铺ES数据 【1.2.0】")
    public Result<String>  pushShopEs() {
        return esService.pushShopEs();
    }

    /**
    * 设置秒杀活动ES中的场次ID信息
    * @author: GongJunZheng
    * @date: 2020/9/2 11:20
    * @return: {@link String}
    * @version V1.4.0
    **/
    @GetMapping("/setSeckillEventIdsEs")
    @ApiOperation("V1.4.0 设置秒杀活动ES中的场次ID信息")
    public Result<String> setSeckillEventIdsEs() {
        return esService.setSeckillEventIdsEs();
    }

    /**
    * 设置营销团购/秒杀活动ES中的商品添加类型信息
    * @author: GongJunZheng
    * @date: 2020/9/8 18:03
    * @return: {@link }
    * @version V1.4.0
    **/
    @GetMapping("/setMarketingGoodsAddType")
    @ApiOperation("V1.4.0 设置营销团购/秒杀活动ES中的商品添加类型信息")
    public Result<String> setMarketingGoodsAddType() {
        return esService.setMarketingGoodsAddType();
    }

    /**
     * 设置营销团购/秒杀活动ES中的商品销售渠道类型信息
     * @author: GongJunZheng
     * @date: 2020/9/8 18:03
     * @return: {@link }
     * @version V1.4.0
     **/
    @GetMapping("/setMarketingGoodsSaleChannels")
    @ApiOperation("V1.4.0 设置营销团购/秒杀活动ES中的商品销售渠道类型信息")
    public Result<String> setMarketingGoodsSaleChannels() {
        return esService.setMarketingGoodsSaleChannels();
    }

    /**
    * 同步营销秒杀/团购活动至ES
    * @author: GongJunZheng
    * @date: 2020/9/15 17:43
    * @return: {@link String}
    * @version V1.4.0
    **/
    @GetMapping("/pushMarketingEs")
    @ApiOperation("V1.4.0 同步营销秒杀/团购活动至ES")
    public Result<String> pushMarketingEs() {
        return esService.pushMarketingEs();
    }


    @GetMapping("/pushGoodsEsToDb")
    @ApiOperation("V1.4.0 处理数据库分来排序和商品排序")
    public Result<String> pushGoodsEsToDb() {
        return esService.pushGoodsEsToDb();
    }


    @GetMapping("/deleteGoodsIndex")
    @ApiOperation("V1.4.0 删除商品索引")
    public void deleteGoodsIndex() {
        esService.delAndCreateEsIndex();
    }

    @PostMapping("/grouponAloneTest")
    @ApiOperation("V1.4.0 版本测试团购时间修改并同步ES（测试专用）")
    public Result<String> grouponAloneTest(@Valid @RequestBody MarketingGrouponAloneTestDTO dto) {
        return esService.grouponAloneTest(dto);
    }

    @GetMapping("/setMarketingGoodsSpecType")
    @ApiOperation("V1.5.0 设置营销商品的规格类型")
    public Result<String> setMarketingGoodsSpecType() {
        return esService.setMarketingGoodsSpecType();
    }


    @GetMapping("/updateCategory")
    @ApiOperation("V1.5.0 处理商品分类数据")
    public Result<Boolean> updateCategory() {
        return esService.updateCategory();
    }

}
