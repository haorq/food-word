package com.meiyuan.catering.admin.web.order;

import com.alibaba.excel.EasyExcel;
import com.meiyuan.catering.admin.service.order.AdminOrderService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author xie-xi-jie
 * @Description 后台-订单管理
 * @Date 2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/order")
@Api(tags = "后台-订单管理")
public class AdminOrderController {
    @Resource
    private AdminOrderService adminOrderService;

    /**
     * @Description 订单列表分页查询
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/listLimit")
    @ApiOperation("后台——【订单列表】分页查询")
    public Result<PageData<OrdersListAdminDTO>> listLimit(@RequestBody OrdersListAdminParamDTO paramDTO) {
        return this.adminOrderService.ordersListQuery(paramDTO);
    }

    /**
     * @Description 订单详情
     * @Date 2020/3/12 0012 15:38
     */
    @GetMapping("/detail/{orderId}")
    @ApiOperation("后台——【订单详情】")
    public Result<OrdersDetailAdminDTO> orderDetailQuery(@PathVariable(value = "orderId") Long orderId) {
        return this.adminOrderService.orderDetailQuery(orderId);
    }

    @ApiOperation(value = "订单excel导出", notes = "订单excel导出")
    @PostMapping(value = "excelExport")
    public void orderExcelExport(HttpServletResponse response,@RequestBody OrdersListAdminParamDTO paramDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName;
        if (paramDTO.getType() == 1){
            fileName = URLEncoder.encode("订单导出", "UTF-8");
        }else {
            fileName = URLEncoder.encode("备餐表导出", "UTF-8");
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        if (paramDTO.getType() == 1){
            List<OrderListExcelExportDTO> list =  adminOrderService.excelExport(paramDTO);
            EasyExcel.write(response.getOutputStream(), OrderListExcelExportDTO.class).sheet("订单导出").doWrite(list);
        }else {
            List<OrderGoodsListExcelExportDTO> list =  adminOrderService.listOrderGoodsExcel(paramDTO);
            EasyExcel.write(response.getOutputStream(), OrderGoodsListExcelExportDTO.class).sheet("订单导出").doWrite(list);
        }
    }

}
