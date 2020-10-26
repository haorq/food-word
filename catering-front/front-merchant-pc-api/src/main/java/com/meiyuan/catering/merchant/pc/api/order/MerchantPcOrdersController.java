package com.meiyuan.catering.merchant.pc.api.order;

import com.alibaba.excel.EasyExcel;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.pc.service.order.MerchantPcOrderService;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcExcelDTO;
import com.meiyuan.catering.order.dto.order.OrderForMerchantPcParamDto;
import com.meiyuan.catering.order.dto.query.admin.OrderListExcelExportDTO;
import com.meiyuan.catering.order.dto.query.admin.OrdersDetailAdminDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 商户PC订单
 *
 * @author lh
 */
@Api(tags = "订单")
@RestController
@RequestMapping("order")
public class MerchantPcOrdersController {

    @Resource
    private MerchantPcOrderService orderService;

    /**
     * 分页查询商户PC订单流水
     *
     * @param param
     * @return
     */
    @PostMapping("list")
    @ApiOperation("订单流水（包含订单和退款单）")
    public Result<PageData<OrderForMerchantPcDTO>> pageList(
            @RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
            @RequestBody OrderForMerchantPcParamDto param) {


        if (AccountTypeEnum.MERCHANT.getStatus().compareTo(token.getAccountType())==0){
            // 品牌登录
            param.setMerchantId(token.getAccountTypeId());
        }else {
            // 门店／自提点登录
            if (param.getShopId()==null){
                // 品牌登录，切换门店时会传shopId，门店登录不会传
                param.setShopId(token.getAccountTypeId());
            }
        }
        return orderService.list(param);
    }


    /**
     * 商户PC订单流水导出
     *
     * @param param
     * @return
     */
    @PostMapping("excel")
    @ApiOperation("商户PC订单流水导出,订单流水（包含订单和退款单）")
    public void pageListExcel(
            @RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
            HttpServletResponse response,
            @RequestBody OrderForMerchantPcParamDto param) {
        if (AccountTypeEnum.MERCHANT.getStatus().compareTo(token.getAccountType())==0){
            // 品牌登录
            param.setMerchantId(token.getAccountTypeId());
        }else {
            // 门店／自提点登录
            if (param.getShopId()==null){
                // 品牌登录，切换门店时会传shopId，门店登录不会传
                param.setShopId(token.getAccountTypeId());
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        List<OrderForMerchantPcExcelDTO> list = orderService.listExcel(param);
        try {
            String orderStart = DateTimeUtil.format(param.getOrderStartDate(), "yyyy-MM-dd");
            String orderEnd = DateTimeUtil.format(param.getOrderEndDate(), "yyyy-MM-dd");
            String fileName = URLEncoder.encode("订单流水导出"+orderStart+"-"+orderEnd, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), OrderForMerchantPcExcelDTO.class).sheet("订单流水导出").doWrite(list);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @GetMapping("info")
    @ApiOperation(("订单详情"))
    public Result<OrdersDetailAdminDTO> queryByOrderId(@RequestParam("orderId") Long orderId) {
        return Result.succ(orderService.queryByOrderId(orderId));
    }

}
