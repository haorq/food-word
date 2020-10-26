package com.meiyuan.catering.merchant.pc.api.report;


import com.alibaba.excel.EasyExcel;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantParamDto;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.pc.service.report.MerchantPcReportService;
import com.meiyuan.catering.merchant.vo.merchant.MerchantBusinessReportVo;
import com.meiyuan.catering.order.dto.OrderHistoryTrendDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleExcelDTO;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author fql
 */
@RestController
@RequestMapping(value = "report")
@Api(tags = "商户报表相关")
public class MerchantPcReportController {

    @Resource
    private MerchantPcReportService merchantPcReportService;

    @ApiOperation("营业数据")
    @PostMapping("/merchant_report")
    public Result<MerchantBusinessReportVo> operatingStatement(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody MerchantParamDto dto) {

        MerchantBusinessReportVo merchantBusinessReportVo = merchantPcReportService.orderListReportMerchant(token,dto);
        return Result.succ(merchantBusinessReportVo);
    }


    @ApiOperation("历史趋势")
    @GetMapping("/history_trend")
    public Result<List<OrderHistoryTrendDTO>> historicalTrend(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                  @ApiParam("1：自然日，2：营业日") Integer type,@ApiParam("1：7天，2：30天") Integer queryType) {

        List<OrderHistoryTrendDTO> orderHistoryTrend = merchantPcReportService.historyTrendBusiness(token.getAccountTypeId(),type, queryType);
        return Result.succ(orderHistoryTrend);
    }


    @ApiOperation("商品销售")
    @PostMapping("/goods_sale_report")
    public Result<PageData<GoodsSaleDTO>> goodsSaleReport(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,@RequestBody GoodsSalePageParamDTO param){
        Long accountTypeId = token.getAccountTypeId();
        if(token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())){
            param.setMerchantId(accountTypeId);
        }else {
            param.setShopId(accountTypeId);
        }
        return merchantPcReportService.goodsSellListQuery(param);
    }

    @ApiOperation(value = "商品销售数据导出", notes = "商品销售数据导出")
    @PostMapping(value = "/goods_sale_export")
    public void orderExcelExport(HttpServletResponse response,@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token, @RequestBody GoodsSalePageParamDTO param) throws IOException {
        Long accountTypeId = token.getAccountTypeId();
        if(token.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())){
            param.setMerchantId(accountTypeId);
        }else {
            param.setShopId(accountTypeId);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("商品销售数据", "UTF-8");

        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<GoodsSaleExcelDTO> list =  merchantPcReportService.goodsExcelExport(param);
        EasyExcel.write(response.getOutputStream(), GoodsSaleExcelDTO.class).sheet("商品销售数据导出").doWrite(list);

    }
}
