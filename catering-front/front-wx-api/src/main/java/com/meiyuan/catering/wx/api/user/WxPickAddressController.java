package com.meiyuan.catering.wx.api.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;
import com.meiyuan.catering.wx.service.user.WxPickAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhm
 * @date 2020/3/25 18:31
 **/
@RestController
@RequestMapping("/api/pickupAddress")
@Api(tags = "微信--自提点地址服务--lhm")
public class WxPickAddressController {

    @Autowired
    private WxPickAddressService pickAddressService;


    @ApiOperation("c端下单时--自提点地址列表")
    @PostMapping("/pickupAddressList")
    public Result<IPage<PickupAddressListVo>> pickupAddressList(@RequestBody PickupAdressQueryDTO dto) {
        return pickAddressService.pickupAddressList(dto);
    }


}
