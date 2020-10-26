package com.meiyuan.catering.merchant.pc.api.employee;


import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopEmployeeDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.pc.service.employee.ShopPcEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author fql
 */
@RestController
@RequestMapping("/employee")
@Api(tags = "门店员工相关接口")
public class ShopPcEmployeeController {

    private static Logger logger = LoggerFactory.getLogger(ShopPcEmployeeController.class);

    @Resource
    private ShopPcEmployeeService shopPcEmployeeService;

    @ApiOperation("门店员工列表")
    @PostMapping("/list")
    public Result<PageData<ShopEmployeeDTO>> queryPageShopEmployee(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody EmployeeQueryPageDTO dto){
        if(Objects.isNull(token.getAccountTypeId())){
            throw new CustomException("获取门店信息失败，请重新登陆");
        }
        dto.setShopId(token.getAccountTypeId());
        return shopPcEmployeeService.queryPageShopEmployee(dto);
    }

    @ApiOperation("新建员工信息/编辑/禁用/重置密码")
    @PostMapping("/save_update")
    public Result<String> updateEmployeeInfo(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody ShopEmployeeDTO shopEmployeeDTO){
        if(Objects.isNull(token.getAccountTypeId())){
            throw new CustomException("获取登录信息失败，请重新登陆");
        }
        if(token.getAccountType().compareTo(AccountTypeEnum.SHOP.getStatus())==0
                ||token.getAccountType().compareTo(AccountTypeEnum.SHOP_PICKUP.getStatus())==0 || token.getAccountType().compareTo(AccountTypeEnum.EMPLOYEE.getStatus())==0){
            logger.info("token data is :",token);
            shopEmployeeDTO.setShopId(token.getAccountTypeId());
        }
        return Result.succ(shopPcEmployeeService.saveUpdateEmployeeInfo(shopEmployeeDTO));
    }

    @ApiOperation("员工删除")
    @DeleteMapping("/del_employee/{id}")
    public Result<String> delEmployee(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @PathVariable Long id){
        if(Objects.isNull(token.getAccountTypeId())){
            throw new CustomException("获取门店信息失败，请重新登陆");
        }
        return Result.succ(shopPcEmployeeService.delEmployeeById(id));
    }

    @ApiOperation("批量删除员工")
    @DeleteMapping("/del_all_employee/{shopId}")
    public Result<String> delAllEmployee(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @PathVariable("shopId") Long shopId){
        if(Objects.isNull(token.getAccountTypeId())){
            throw new CustomException("获取门店信息失败，请重新登陆");
        }
        return Result.succ(shopPcEmployeeService.delAllEmployee(shopId));
    }

}
