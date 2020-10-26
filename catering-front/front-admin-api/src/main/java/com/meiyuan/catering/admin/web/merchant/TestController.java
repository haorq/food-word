package com.meiyuan.catering.admin.web.merchant;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingShopDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.marketing.dto.groupon.ShopGrouponGoodsDTO;
import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantShopAddDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryPagDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.enums.ShopTypeEnum;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.service.CateringMerchantLoginAccountService;
import com.meiyuan.catering.merchant.service.CateringShopService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.pay.service.MyMemberService;
import com.meiyuan.catering.user.dto.user.User;
import com.meiyuan.catering.user.fegin.user.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/3 0003 16:15
 * @Description 简单描述 : 测试
 * @Since version-1.3.0
 */
@RestController
@RequestMapping("/admin/merchant/test")
@Api(tags = "测试")
public class TestController {

    @Autowired
    private EsMarketingClient marketingClient;

    @Resource
    private ShopGoodsSkuClient shopGoodsSkuClient;

    @Resource
    private CateringMerchantLoginAccountService loginAccountService;
    @Resource
    private MerchantClient merchantClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private CateringShopService shopService;
    @Autowired
    MarketingGrouponClient marketingGrouponClient;

    @Autowired
    MarketingSeckillClient marketingSeckillClient;
    @Autowired
    EsMerchantClient esMerchantClient;


    @Autowired
    private MyMemberService myMemberService;
    @Autowired
    private UserClient userClient;

    @ApiOperation("初始化会员,绑定支付标识(openId)")
    @PostMapping("/initUserMember")
    public Result initUserMember() {
        List<User> list = userClient.list();
        return Result.succ();
    }
    @ApiOperation("初始化门店会员")
    @PostMapping("/initShopMember")
    public Result initShopMember() {
        List<CateringShopEntity> list = shopClient.list();
        for (CateringShopEntity entity : list) {
           try {
               myMemberService.createPersonalMember(entity.getId());
           }catch (Exception e){
               System.out.println(entity);
           }
        }
        return Result.succ();
    }


    @PostMapping("/listShopIdByCity")
    @ApiOperation("秒杀查询门店id")
    public Result<List<Long>> listShopIdByCity(@RequestBody ShopQueryDTO dto) {
        return shopClient.listShopIdByCity(dto);
    }


    @PostMapping("/listShopGoodsDiscount")
    @ApiOperation("查询门店商品最低折扣")
    public Result<List<ShopGoodsDiscountDTO>> listShopGoodsDiscount(@RequestBody ShopDiscountGoodsDTO dto) {
        return shopGoodsSkuClient.listShopGoodsDiscount(dto);
    }

    @PostMapping("/listShopHaveSeckill")
    @ApiOperation("查询门店秒杀活动最低价格")
    public Result<List<Long>> listShopHaveSeckill(@RequestBody ShopQueryPagDTO dto) {
        return marketingSeckillClient.listShopHaveSeckill(dto.getShopIds(),Boolean.FALSE);
    }

    @PostMapping("/listSeckillGoodsMinPriceByShop")
    @ApiOperation("查询门店秒杀活动最低价格")
    public Result<List<ShopGrouponGoodsDTO>> listSeckillGoodsMinPriceByShop(@RequestBody ShopQueryPagDTO dto) {
        return marketingSeckillClient.listGoodsMinPriceByShop(dto.getShopIds(),Boolean.FALSE);
    }

    @PostMapping("/listGoodsMinPriceByShop")
    @ApiOperation("查询门店团购活动最低价格")
    public Result<List<ShopGrouponGoodsDTO>> listGoodsMinPriceByShop(@RequestBody ShopQueryPagDTO dto) {
        return marketingGrouponClient.listGoodsMinPriceByShop(dto.getShopIds(),Boolean.FALSE);
    }

    @PostMapping("/shopPage")
    @ApiOperation("店铺分页")
    public Result<PageData<GoodPushShopVo>> shopPage(@RequestBody ShopQueryPagDTO dto) {
        return shopClient.shopPage(dto);
    }

    @PostMapping("/listShop")
    @ApiOperation("店铺查询所有")
    public Result<List<EsMarketingDTO>> listShop(@RequestBody EsMarketingShopDTO dto ) {
        return marketingClient.listByShopIds(dto);
    }

    @PostMapping("/listShopDiscountGoods")
    @ApiOperation("查询包含折扣商品的店铺")
    public Result<List<Long>> listShopDiscountGoods(@RequestBody ShopDiscountGoodsDTO dto ) {
        return shopGoodsSkuClient.listShopDiscountGoods(dto);
    }

    @GetMapping("/getMerchantLoginAccount")
    @ApiOperation("获取当前商户账号中最大值")
    public String listShopDiscountGoods() {
        return loginAccountService.getMerchantLoginAccount();
    }

    @GetMapping("/addShopList/{num}/{phone}/{maxNum}")
    @ApiOperation("添加门店假数据")
    public Result<Long> addShopList(@PathVariable Integer num,@PathVariable Long phone,@PathVariable Long maxNum) {
        Long merchantId1 = 1296257753865523202L;
        String merchantService1 = "[1,2]";
        Long merchantId2 = 1296353105998254081L;
        String merchantService2 = "[1]";
        for (int i = 0;i < num.intValue();i++){
            String shopInfo = "{\"shopType\":1,\"shopName\":\"明天自营门店\",\"shopStatus\":1,\"doorHeadPicture\":\"https://dev-my-common.meiy520.com/catering-dev/20200820/u2cm0kwq0uswm3dx5m51.jpg\",\"primaryPersonName\":\"明天\",\"registerPhone\":\"18784381872\",\"shopPhone\":\"18784381872\",\"openingTime\":\"08:00\",\"closingTime\":\"22:59\",\"doorNumber\":\"H0002\",\"addressDetail\":\"四川省成都市青羊区王家塘街84号\",\"mapCoordinate\":\"104.067923,30.679943\",\"shopNotice\":\"是daDS\",\"shopService\":\"[1,2]\",\"changeGoodPrice\":false,\"businessLicense\":\"https://dev-my-common.meiy520.com/catering-dev/20200820/2c0g8a2x4qex8t8s608k.jpg\",\"foodBusinessLicense\":\"https://dev-my-common.meiy520.com/catering-dev/20200820/mibuimdu00s6bwadyc62.jpg\"}";
            CateringShopEntity shopEntity = JSONObject.parseObject(shopInfo, CateringShopEntity.class);
            shopEntity.setRegisterPhone(String.valueOf(phone+ maxNum + i));
            Long s = maxNum + i + 1;
            shopEntity.setShopName(shopEntity.getShopName() + s);
            shopEntity.setMerchantId(merchantId1);
            shopEntity.setShopService(merchantService1);
            if (i%2 == 0){
                shopEntity.setShopService(merchantService2);
                shopEntity.setMerchantId(merchantId2);
            }
            MerchantShopAddDTO merchantShopAddDto = BaseUtil.objToObj(shopEntity, MerchantShopAddDTO.class);
            merchantClient.insertMerchantShop(merchantShopAddDto);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Result.succ();
    }

    @GetMapping("/syscShopEs/{merchantId}")
    @ApiOperation("syscShopEs添加门店假数据")
    public void syscShopEs(@PathVariable Long merchantId) {
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getMerchantId,merchantId)
                    .ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus())
                    .eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringShopEntity> list = shopService.list(query);
        List<EsMerchantDTO> objects = new ArrayList<>();
        list.forEach(s->{
            ShopInfoDTO shop = merchantUtils.getShop(s.getId());
            if (!ObjectUtils.isEmpty(shop)){
                //新增店铺信息同步es
                EsMerchantDTO esMerchantDTO = new EsMerchantDTO();
                esMerchantDTO.setId(shop.getId().toString());
                esMerchantDTO.setMerchantId(shop.getMerchantId().toString());
                esMerchantDTO.setShopId(shop.getId().toString());
                esMerchantDTO.setMerchantName(shop.getShopName());

                esMerchantDTO.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
                esMerchantDTO.setProvinceCode(shop.getAddressProvinceCode());
                esMerchantDTO.setEsCityCode(shop.getAddressCityCode());
                esMerchantDTO.setAreaCode(shop.getAddressAreaCode());

                esMerchantDTO.setShopGrade(0.0D);
                esMerchantDTO.setOrdersNum(0);

                //店铺是否可在小程序展示
                esMerchantDTO.setShopService(shop.getShopServiceType());
                esMerchantDTO.setShopStatus(shop.getShopStatus());

                MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shop.getMerchantId());
                esMerchantDTO.setAuditStatus(merchantInfo.getAuditStatus());
                esMerchantDTO.setMerchantStatus(merchantInfo.getMerchantStatus());

//                esMerchantClient.saveUpdate(esMerchantDTO);
                objects.add(esMerchantDTO);
            }else {
                System.out.println("es添加商户："+ shop.getShopName() +",es信息同步失败");
            }

        });

        esMerchantClient.saveUpdateBatch(objects);

    }
}
