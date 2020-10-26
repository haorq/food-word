package com.meiyuan.catering.wx.api.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.address.AddressAddDTO;
import com.meiyuan.catering.user.query.address.AddressQueryDTO;
import com.meiyuan.catering.user.vo.address.AddressDetailVo;
import com.meiyuan.catering.user.vo.address.UserAddressCheckVo;
import com.meiyuan.catering.user.vo.address.UserAddressListVo;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.user.WxUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/3/24 11:09
 **/
@RestController
@RequestMapping("/api/address")
@Api(tags = "微信--用户收货地址相关接口--lhm")
public class WxUserAddressController {

    @Resource
    private WxUserAddressService userAddressService;


    @ApiOperation("新增收货地址（最多10个）")
    @PostMapping("/addUserAddress")
    public Result<Boolean> addUserAddress(@LoginUser UserTokenDTO tokenDTO, @RequestBody AddressAddDTO dto) {
        return userAddressService.addUserAddress(tokenDTO.getToken(), dto);
    }


    @ApiOperation("编辑收货地址")
    @PostMapping("/editUserAddress")
    public Result<Boolean> editUserAddress(@RequestBody AddressAddDTO dto) {
        return userAddressService.editUserAddress(dto);
    }


    @ApiOperation("收货地址列表")
    @PostMapping("/userAddressList")
    public Result<IPage<UserAddressListVo>> userAddressList(@LoginUser UserTokenDTO tokenDTO, @RequestBody AddressQueryDTO dto) {
        return userAddressService.userAddressList(tokenDTO.getToken(), dto);
    }


    @ApiOperation("收货地址详情")
    @GetMapping("/userAddressDetail/{id}")
    public Result<AddressDetailVo> userAddressDetail(@ApiParam("收货地址id") @PathVariable Long id) {
        return userAddressService.userAddressDetail(id);
    }


    @ApiOperation("删除收货地址")
    @DeleteMapping("/deleteAddress/{id}")
    public Result<Boolean> deleteAddress(@ApiParam("收货地址id") @PathVariable Long id) {
        return userAddressService.deleteAddress(id);
    }


    @ApiOperation("C端下单时--收货地址List")
    @PostMapping("/chooseAddressList")
    public Result<UserAddressCheckVo> chooseAddressList(@LoginUser() UserTokenDTO tokenDTO, @RequestBody AddressQueryDTO dto) {
        return userAddressService.chooseAddressList(tokenDTO.getToken(), dto);
    }


    @ApiOperation("C端--附近地址")
    @GetMapping("/nearbyAddress")
    public Result<Object> nearbyAddress(@RequestParam String mapCoordinate) {
        return userAddressService.nearbyAddress(mapCoordinate);
    }


}
