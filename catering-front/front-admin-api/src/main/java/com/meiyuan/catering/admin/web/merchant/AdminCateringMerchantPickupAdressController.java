package com.meiyuan.catering.admin.web.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.service.merchant.AdminCateringMerchantPickupAddressService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.merchant.MerchantPickupAdressPageDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPickupAddressPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 商户管理-自提点地址管理
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/merchant/merchantPickupAdress")
@Api(tags = "商户管理-自提点地址管理")
public class AdminCateringMerchantPickupAdressController {
    @Resource
    private AdminCateringMerchantPickupAddressService pickupAddressService;

    /**
     * @Author MeiTao
     * @Description 自提点列表分页查询
     * @Date  2020/3/12 0012 15:38
     */
    @PostMapping("/listMerchantPickupAdressPage")
    @ApiOperation("自提点列表分页查询")
    public Result<IPage<CateringMerchantPickupAddressPageVo>> listMerchantPickupAdressPage(@RequestBody MerchantPickupAdressPageDTO dto){

        return pickupAddressService.listMerchantPickupAddressPage(dto);
    }

    /**
     * @Author MeiTao
     * @Description 自提点查询
     * @Date  2020/3/12 0012 15:38
     */
    @GetMapping("/getMerchantPickupAdress/{id}")
    @ApiOperation("自提点查询")
    public Result<MerchantsPickupAddressDTO> getMerchantPickupAddress(@ApiParam("id")@PathVariable Long id){
        return pickupAddressService.getMerchantPickupAddress(id);
    }

}
