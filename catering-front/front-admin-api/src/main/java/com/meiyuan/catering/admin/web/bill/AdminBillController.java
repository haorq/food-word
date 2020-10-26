package com.meiyuan.catering.admin.web.bill;

import com.alibaba.excel.EasyExcel;
import com.meiyuan.catering.admin.service.order.AdminOrderService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.bill.BillExcelExportDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.BillMerchantInfoDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.ShopBillDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.ShopBillDetailDTO;
import com.meiyuan.catering.merchant.vo.shop.bill.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author herui
 * @Description 后台-对账报表
 * @Date 2020/09/01 13:13
 */
@RestController
@RequestMapping("/admin/bill")
@Api(tags = "后台-对账报表")
public class AdminBillController {
    @Resource
    private AdminOrderService adminOrderService;

    @PostMapping("/listLimit")
    @ApiOperation("后台——【对账报表】分页查询 v1.4.0")
    public Result<ShopBillVo> listLimit(@RequestBody ShopBillDTO paramDTO) {
        return this.adminOrderService.listBillShop(paramDTO);
    }

    @PostMapping("/detail")
    @ApiOperation("后台——【报表门店详情】分页查询 v1.4.0")
    public Result<PageData<ShopBillDetailVo>> orderDetailQuery(@RequestBody ShopBillDTO paramDTO) {
        return this.adminOrderService.billShopDetailQuery(paramDTO);
    }

    @ApiOperation(value = "对账报表门店详情excel导出 v1.4.0", notes = "对账报表门店详情excel导出")
    @PostMapping(value = "/excelExport")
    public void excelExport(HttpServletResponse response,@RequestBody ShopBillDTO paramDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        List<BillExcelExportDTO> list = this.adminOrderService.billExcelExport(paramDTO);
        String name = (list.size()>0?list.get(0).getShopName():"error") + DateTimeUtil.getDateTimeDisplayString(LocalDateTime.now(),"MMdd");
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), BillExcelExportDTO.class).sheet("sheet1").doWrite(list);
    }

    @GetMapping("/getCityCode")
    @ApiOperation("后台——【报表管理营业概况】城市查询 v1.4.0")
    public Result<List<ShopBillCityVo>> getCityCode() {
        return this.adminOrderService.getBillShopCityCode();
    }

    @PostMapping("/getBillGeneral")
    @ApiOperation("后台——【报表管理营业概况】获取营业概况 v1.4.0")
    public Result<ShopBillGeneralVo> getBillGeneral(@RequestBody ShopBillDTO paramDTO) {
        return this.adminOrderService.getBillGeneral(paramDTO);
    }

    @PostMapping("/getShopByName")
    @ApiOperation("后台——【报表管理】根据门店名、品牌名字、【后面新增的编码】获取列表 v1.4.0")
    public Result<List<BillMerchantInfoVo>> getMerchantInfo(@RequestBody BillMerchantInfoDTO paramDTO) {
        return this.adminOrderService.getMerchantInfo(paramDTO);
    }
}
