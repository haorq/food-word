package com.meiyuan.catering.merchant.api.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.merchant.SaleOverviewParamDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopHomeResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupConfigResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupManagerResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupPointRequestDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupUpdateRequestDTO;
import com.meiyuan.catering.merchant.dto.shop.config.DeliveryConfigResponseDTO;
import com.meiyuan.catering.merchant.service.merchant.MerchantShopService;
import com.meiyuan.catering.merchant.vo.merchant.MerchantSaleSurveyVO;
import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersCountMerchantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Author MeiTao
 * @Description 店铺
 * @Date  2020/3/22 0022 21:09
 */
@RestController
@RequestMapping("/app/shop")
@Api(tags = "店铺-信息")
public class MerchantShopController {

    @Autowired
    private MerchantShopService shopService;

    @GetMapping("/getPickupConfig/{shopId}")
    @ApiOperation("获取门店自提配置信息")
    public Result<PickupConfigResponseDTO> getPickupConfig(@LoginMerchant MerchantTokenDTO tokenDTO, @ApiParam("店铺id") @PathVariable Long shopId){

        return shopService.getPickupConfig(shopId);
    }

    @GetMapping("/getDeliveryConfig/{shopId}")
    @ApiOperation("获取门店配送配置信息")
    public Result<DeliveryConfigResponseDTO> getDeliveryConfig(@LoginMerchant MerchantTokenDTO tokenDTO, @ApiParam("店铺id") @PathVariable Long shopId){

        return shopService.getDeliveryConfig(shopId);
    }

    @PostMapping("/modifyDeliveryConfig/{shopId}")
    @ApiOperation("门店配送配置信息修改")
    public Result modifyDeliveryConfig(@LoginMerchant MerchantTokenDTO tokenDTO,@ApiParam("店铺id") @PathVariable Long shopId,
                                       @ApiParam("店铺配送配置信息") @Validated @RequestBody DeliveryConfigResponseDTO dto){

        return shopService.modifyDeliveryConfig(shopId,dto);
    }

    @PostMapping("/modifyPickupConfig/{shopId}")
    @ApiOperation("门店自提配置信息修改")
    public Result modifyPickupConfig(@LoginMerchant MerchantTokenDTO tokenDTO,@ApiParam("店铺id")@PathVariable Long shopId ,
                                     @ApiParam("店铺自提配置信息") @RequestBody PickupConfigResponseDTO dto){

        return shopService.modifyPickupConfig(shopId,dto);
    }

    @GetMapping("/getShopHomeInfo/{shopId}")
    @ApiOperation("获取门店设置首页信息【1.5.0】")
    public Result<ShopHomeResponseDTO> getShopHomeInfo(@LoginMerchant MerchantTokenDTO tokenDTO, @ApiParam("店铺id") @PathVariable Long shopId){

        return shopService.getShopHomeInfo(shopId);
    }

    @PostMapping("/listShopPickupAddress")
    @ApiOperation(value = "门店自提点 查询(自提点管理/自提点解绑)列表",notes = "version-1.1.0修改，店铺可对既是店铺又是自提点的店铺进行绑定")
    public Result<PickupManagerResponseDTO> listShopPickupAddress(@LoginMerchant MerchantTokenDTO tokenDTO, @ApiParam("自提点列表查询接收参数DTO") @RequestBody PickupPointRequestDTO dto){

        return shopService.listShopPickupAddress(dto);
    }

    @PostMapping("/saveOrDelShopPickup")
    @ApiOperation("店铺自提点绑定解绑")
    public Result saveOrDelShopPickup(@LoginMerchant MerchantTokenDTO tokenDTO,@ApiParam("自提点绑定解绑接收参数DTO") @RequestBody PickupUpdateRequestDTO dto){
        return shopService.saveOrDelShopPickup(dto);
    }

    @GetMapping("/getShopSupportType/{shopId}/{type}")
    @ApiOperation(value = "门店业务配置-查询门店业务支持类型",notes = "业务支持：1：仅配送，2：仅自提，3：全部")
    public Result<Integer> getShopSupportType(@LoginMerchant MerchantTokenDTO tokenDTO,
                                              @ApiParam("店铺id")@PathVariable Long shopId ,
                                              @ApiParam(value = "修改：业务支持：1：仅配送，2：仅自提，3：全部，查询：type为4",required = false) @PathVariable(required = false) Integer type){
        return shopService.getShopSupportType(shopId,type);
    }

    @GetMapping("/modifyShopSellType/{shopId}/{type}")
    @ApiOperation(value = "售卖模式修改/查询",notes = "修改：售卖模式 ： 1-菜单售卖模式 2-商品售卖模式，查询type为：3")
    public Result<Integer> modifyShopSellType(@LoginMerchant MerchantTokenDTO tokenDTO,
                                              @ApiParam("店铺id")@PathVariable Long shopId,
                                              @ApiParam(value = "修改：模式：1-菜单售卖模式 2-商品售卖模式，查询type为：3",required = false) @PathVariable(required = false) Integer type){
        return shopService.modifyShopSellType(shopId,type);
    }

    @GetMapping("/getShopManagerHome")
    @ApiOperation(value = "获取门店管理首页信息")
    public Result<OrdersCountMerchantDTO> getShopManagerHome(@LoginMerchant MerchantTokenDTO tokenDTO){
        MerchantBaseDTO param = new MerchantBaseDTO();
        BeanUtils.copyProperties(tokenDTO, param);
        return shopService.getShopManagerHome(param);
    }

    @PostMapping("/sale_overview")
    @ApiOperation("营业概览")
    public Result<MerchantSaleSurveyVO> saleOverview(@LoginMerchant MerchantTokenDTO tokenDTO, @ApiParam("营业概览参数") @RequestBody SaleOverviewParamDTO dto){
        throw new CustomException("接口已启用");
    }

}
