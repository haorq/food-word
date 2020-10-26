package com.meiyuan.catering.merchant.api.order;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.dada.CancelOrderReqDto;
import com.meiyuan.catering.merchant.dto.dada.DealDadaMessageReqDto;
import com.meiyuan.catering.merchant.dto.dada.OrderIdReqDto;
import com.meiyuan.catering.order.dto.order.BizDataForMerchantDTO;
import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import com.meiyuan.catering.order.dto.order.PrintPaperDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.order.MerchantOrderService;
import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import com.meiyuan.catering.order.dto.OrdersCheckDTO;
import com.meiyuan.catering.order.dto.OrdersCheckParamDTO;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.vo.DeliveryGoodsVo;
import com.meiyuan.catering.order.vo.PrintOutPaperVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author xie-xi-jie
 * @Description 商户-订单管理
 * @Date 2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/app/order")
@Api(tags = "商户-订单管理")
public class MerchantOrderController {
    @Resource
    private MerchantOrderService merchantOrderService;

    /**
     * @Description 订单列表分页查询包含订单总金额
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/list")
    @ApiOperation("商户——【订单列表】包含订单总金额")
    public Result<OrdersListAmountMerchantDTO> list(@LoginMerchant MerchantTokenDTO token, @RequestBody OrdersListMerchantParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.orderListMerchant(paramDTO);
    }

    /**
     * @Description 订单列表分页查询
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/listLimit")
    @ApiOperation("商户——【订单列表】分页查询")
    public Result<PageData<OrdersListMerchantDTO>> listLimit(@LoginMerchant MerchantTokenDTO token, @RequestBody OrdersListMerchantParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.orderListQueryMerchant(paramDTO);
    }


    /**
     * 订单列表，按照配送／自取日期分组
     *
     * @param token
     * @param paramDTO
     * @return
     */
    @PostMapping("/listGroupByEstimateDate")
    @ApiOperation("商户——订单列表，按照配送／自取日期分组")
    public Result<PageData<OrdersListMerchantGroupByDateDTO>> listGroupByEstimateDate(
            @LoginMerchant MerchantTokenDTO token,
            @RequestBody OrdersListMerchantParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return merchantOrderService.listGroupByEstimateDate(paramDTO);
    }


    /**
     * @Description 订单详情
     * @Date 2020/3/12 0012 15:38
     */
    @GetMapping("/detail/{orderId}")
    @ApiOperation("商户——【订单详情】")
    public Result<OrdersDetailMerchantDTO> orderDetailQuery(@LoginMerchant MerchantTokenDTO token, @PathVariable(value = "orderId") Long orderId) {
        return this.merchantOrderService.orderDetailQueryMerchant(orderId);
    }


    /**
     * @Description 商户——【订单验证】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/check")
    @ApiOperation("商户——【订单验证】")
    public Result<OrdersCheckDTO> orderCheckByCode(@LoginMerchant MerchantTokenDTO token, @Valid @RequestBody OrdersCheckParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.orderCheckByCode(paramDTO);
    }

    /**
     * @Description 商户——【订单验证二次确认，取餐时间是否是当天的订单：是：true，否：false】
     * @Version 1.0.1
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/estimateTimeCheck")
    @ApiOperation("商户——【订单验证二次确认】")
    public Result<EstimateTimeCheckDTO> estimateTimeCheck(@LoginMerchant MerchantTokenDTO token, @Valid @RequestBody OrdersCheckParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.estimateTimeCheck(paramDTO);
    }

    /**
     * @Description 商户——【订单分布情况】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/distribution")
    @ApiOperation("商户——【订单分布情况】")
    public Result<List<OrdersDistributionDTO>> orderDistribution(@LoginMerchant MerchantTokenDTO token, @RequestBody OrdersDistributionParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.orderDistribution(paramDTO);
    }

    /**
     * @Description 商户——【订单查询状态数量统计】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/statusCount")
    @ApiOperation("商户——【订单查询状态数量统计】")
    public Result<OrdersStatusCountDTO> ordersStatusCountMerchant(@LoginMerchant MerchantTokenDTO token, @RequestBody OrdersListMerchantParamDTO param) {
        BeanUtils.copyProperties(token, param);
        return this.merchantOrderService.ordersStatusCountMerchant(param);
    }

    /**
     * @Description 商户——【订单退款消息】
     * @Date 2020/3/12 0012 15:38
     */
    @PostMapping("/refund/message")
    @ApiOperation("商户——【订单退款消息】")
    public Result<Integer> refundMessage(@LoginMerchant MerchantTokenDTO token) {
        MerchantBaseDTO param = new MerchantBaseDTO();
        BeanUtils.copyProperties(token, param);
        return this.merchantOrderService.refundMessage(param);
    }


    /**
     * @param token
     * @param paramDTO
     * @return
     * @desc 【商户——红点统一消息】
     * @version v1.2.0
     * @date 2020-07-10
     */
    @PostMapping("/redMessage")
    @ApiOperation(("商户——【红点消息】"))
    public Result<Map<String, Integer>> redMessage(@LoginMerchant MerchantTokenDTO token, @RequestBody OrdersDistributionParamDTO paramDTO) {
        BeanUtils.copyProperties(token, paramDTO);
        return this.merchantOrderService.redMessage(paramDTO);
    }


    @PostMapping("/deliveryGoods/info")
    @ApiOperation("商户——【 待核销商品明细】   xxj    1.1.0")
    public Result<List<DeliveryGoodsVo>> deliveryGoodsInfo(@LoginMerchant MerchantTokenDTO token, @RequestBody DeliveryGoodsParamDTO dto) {
        BeanUtils.copyProperties(token, dto);
        return this.merchantOrderService.deliveryGoodsInfo(dto);
    }

    @PostMapping("/print/info")
    @ApiOperation("商户——【打印小票的数据】 v1.4.0 herui")
    public Result<List<OrdersDetailMerchantDTO>> getPrintInfo(@LoginMerchant MerchantTokenDTO token, @RequestBody PrintPaperDTO printPaperDTO) {
        return this.merchantOrderService.getPrintInfo(printPaperDTO, token.getShopId());
    }

    @GetMapping("bizDataForMerchant")
    @ApiOperation("商户——【经营数据】")
    public Result<BizDataForMerchantDTO> bizDataForMerchant(
            @RequestParam("shopId") Long shopId,
            @RequestParam(value = "orderDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,
            @RequestParam(value = "orderStartDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderStartDate,
            @RequestParam(value = "orderEndDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderEndDate) {
        return merchantOrderService.bizDataForMerchant(shopId, orderDate, orderStartDate, orderEndDate);
    }

    /**
     * 查询被达达取消的订单
     *
     * @param shopId
     * @return
     */
    @GetMapping("listCancelOrderByDada")
    @ApiOperation("商户——【查询被达达取消的订单】")
    public Result<List<OrderDeliveryStatusDto>> listCancelOrderByDada(@RequestParam("shopId") Long shopId) {
        return merchantOrderService.listCancelOrderByDada(shopId);
    }

    /**
     * 处理达达骑手取消的订单
     *
     * @param param
     * @return
     */
    @PostMapping("confirmDadaCancelOrderMessage")
    @ApiOperation("处理达达骑手取消的订单")
    public Result confirmDadaCancelOrderMessage(@RequestBody DealDadaMessageReqDto param) {
        merchantOrderService.confirmDadaCancelOrderMessage(param.getOrderId(), param.getIsConfirm());
        return Result.succ();
    }

    /**
     * 重发单到达达
     *
     * @param param
     * @return
     */
    @PostMapping("reAddOrderToDada")
    @ApiOperation("重发单到达达")
    public Result reAddOrderToDada(@RequestBody OrderIdReqDto param) {
        merchantOrderService.reAddOrderToDada(param.getOrderId());
        return Result.succ();
    }


    /**
     * 取消达达配送单
     *
     * @param param
     * @return
     */
    @PostMapping("cancelDadaOrder")
    @ApiOperation(("取消达达配送单"))
    public Result cancelDadaOrder(@RequestBody CancelOrderReqDto param) {
        merchantOrderService.cancelDadaOrder(param.getOrderId(), param.getCancelReasonId(), param.getCancelReason());
        return Result.succ();
    }

    /**
     * 妥投异常之物品返回完成
     *
     * @param param
     * @return
     */
    @PostMapping("confirmDadaOrderGoods")
    @ApiOperation("妥投异常之物品返回完成")
    public Result confirmDadaOrderGoods(@RequestBody OrderIdReqDto param) {
        merchantOrderService.confirmDadaOrderGoods(param.getOrderId());
        return Result.succ();
    }
}
