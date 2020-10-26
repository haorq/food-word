package com.meiyuan.catering.order.utils;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.enums.base.merchant.yly.YlyDeviceStatusEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.yly.YlyService;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dto.shop.config.YlyDeviceInfoVo;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersGoodsMerchantDTO;
import com.meiyuan.catering.order.enums.DeliveryWayEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Date 2020/9/26 0026 9:19
 * @Description 简单描述 : 订单小票打印模板
 * @Since version-1.4.0
 */
@Slf4j
@Component
public class Prient {

	/**
	 * 方法描述 : yly小票打印
	 * @Author: MeiTao
	 * @Date: 2020/10/15 0015 11:46
	 * @param ylyDeviceList  易联云打印设备集合
	 * @param ordersDetail 订单详细信息
	 * @return: void
	 * @Since version-1.5.0
	 */
	public static String printTicket(List<YlyDeviceInfoVo> ylyDeviceList, OrdersDetailMerchantDTO ordersDetail,YlyUtils ylyUtils) {
		if(!BaseUtil.judgeList(ylyDeviceList)){
			log.info("当前订单无需要打印的设备，订单详细信息：" + ordersDetail.toString());
			return "未绑定打印机";
		}
		if(ObjectUtils.isEmpty(ordersDetail)){
			log.info("订单详细信息查询失败，需要打印该订单的设备号 ： " + JSONObject.toJSONString(ylyDeviceList));
			return "订单详细信息查询失败";
		}
		StringBuffer sb = new StringBuffer();
		List<YlyDeviceInfoVo> ylyDeviceInfoList = ylyDeviceList.stream().filter(BaseUtil.distinctByKey(YlyDeviceInfoVo::getDeviceNumber)).collect(Collectors.toList());
		ylyDeviceInfoList.forEach(ylyDeviceInfoVo -> {
			if (ylyDeviceInfoVo.getPrintingTimes()>0){
				for (int i=0;i<ylyDeviceInfoVo.getPrintingTimes();i++){
					ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
							Prient.getTakeOutTemplate(ordersDetail, new StringBuilder()).toString(),
							String.valueOf(ordersDetail.getOrderId()));
				}
			}
			if (ylyDeviceInfoVo.getCookTimes()>0){
				for (int i=0;i<ylyDeviceInfoVo.getCookTimes();i++){
					ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
							Prient.getKitchenTemplate(ordersDetail, new StringBuilder()),
							String.valueOf(ordersDetail.getOrderId()));
				}
			}
			if (!Objects.equals(ylyDeviceInfoVo.getDeviceStatus(), YlyDeviceStatusEnum.ONE_LINE.getStatus())){
				if (BaseUtil.judgeString(sb.toString())){
					sb.append(",");
				}
				sb.append(ylyDeviceInfoVo.getDeviceName());
			}
		});
		//打印机<名字1，名字2>异常，请及时处理
		if (BaseUtil.judgeString(sb.toString())){
			return "打印机<" + sb.toString() + ">异常，请及时处理";
		}
		return sb.toString();
	}
	/**
	 * 方法描述 : 外卖单小票打印模板 getTakeOutTemplate
	 * @Author: MeiTao
	 * @Date: 2020/9/23 0023 14:53
	 * @param dto 请求参数
	 * @return: java.lang.String
	 * @Since version-1.4.0
	 */
	public static StringBuilder getTakeOutTemplate(OrdersDetailMerchantDTO dto,StringBuilder sb){
		//1、小票开头
		//店铺名称 : 加大：1倍，加粗，居中
		sb.append("<FB><FS><center>" + dto.getShopName());
		if (Objects.equals(dto.getReprint(),Boolean.TRUE)){
			sb.append("(补打)");
		}
		sb.append("</center></FS></FB>\r");
        //订单类型： 加大：2倍，加粗，居中
		if(dto.getDeliveryWay() == 2){
			sb.append("<FB><FS><center>"+"******** "+"自取单"+" ********"+"</center></FS></FB>\r\n");
		}else {
			sb.append("<FB><FS><center>"+"******** "+"配送单"+" ********"+"</center></FS></FB>\r\n");
		}

		//送达时间 : 左对齐，换行打印
		String suffix = Objects.equals(dto.getDeliveryWay(),2) ? "自取" :"送达";
		if (!ObjectUtils.isEmpty(dto.getImmediateDeliveryTime())){
			sb.append("立即送达(建议" + dto.getImmediateDeliveryTime() + suffix +  ")\r\n");
		}else {
			sb.append(dto.getDeliveryDate().substring(5) + " " + dto.getEstimateTime() +"-"+ dto.getEstimateEndTime() + suffix +  "\r\n");
		}
		sb.append("<FH>--------------------------------</FH>");
		//sb.append("<center>*******************************</center>");

		//2、下单人信息拼接
		//姓名 : 加大：2倍，加粗

		sb.append("<FS>"+ dto.getConsigneeName().substring(0,1) +"</FS>**********\r\n");
        //联系方式 : 加大：2倍，加粗
		String phoneNumber = dto.getConsigneePhone().substring(0, 3) + "****" + dto.getConsigneePhone().substring(7, dto.getConsigneePhone().length());
		sb.append("<FS>"+ phoneNumber +"</FS>\r\n");
        //地址 ：  加大：2倍，加粗
		sb.append("<FS>"+ dto.getConsigneeAddress() +"</FS>\r\n");

		//3、菜品相关数据拼接
		handleGoodsInfo(dto,sb);

		//4、订单合计相关内容处理
		sb.append("<FH>--------------------------------</FH>");
		//sb.append("<center>*******************************</center>");
		sb.append("<LR>"+"商品总额:"+","+ "￥" + dto.getGoodsAmount()+"</LR>");
		if (!BaseUtil.judgeZero(dto.getPackPrice())){
			sb.append("<LR>"+"餐盒费:"+","+"￥" + dto.getPackPrice()+"</LR>");
		}
		BigDecimal s = dto.getDeliveryPriceOriginal()==null?new BigDecimal(0):dto.getDeliveryPriceOriginal();
		if (Objects.equals(dto.getDeliveryWay(), DeliveryWayEnum.Delivery.getCode())){
			sb.append("<LR>"+"配送费:"+","+"￥" + s+"</LR>");
		}
		if (BaseUtil.judgeZero(dto.getDiscountFee())){
			sb.append("<LR>"+"优惠总额:"+","+"￥" + "0.00" +"</LR>");
		}else {
			if (BaseUtil.isGteZero(dto.getDiscountFee())){
				sb.append("<LR>"+"优惠总额:"+","+"-￥" + dto.getDiscountFee()+"</LR>");
			}else {
				sb.append("<LR>"+"优惠总额:"+","+"￥" + dto.getDiscountFee().abs()+"</LR>");
			}
		}
		sb.append("<LR>"+"订单金额:"+","+"￥" + dto.getOrderAmount()+"</LR>");

		//5、订单备注相关处理
		handleNoteInfo(dto,sb);

		//6、订单基本信息
		sb.append("<FH>--------------------------------</FH>");
		//sb.append("<center>*******************************</center>");
		sb.append("下单时间:"+DateTimeUtil.getDateTimeDisplayString(dto.getBillingTime(),DateTimeUtil.PATTERN)+"\r\n");
		sb.append("订单编号:" + dto.getOrderNumber() + "\r\n");
		if (BaseUtil.judgeString(dto.getTakeAddress())){
			sb.append("订单归属:" + dto.getTakeAddress() + "\r\n");
		}
		sb.append("打印时间:" + DateTimeUtil.getDateTimeDisplayString(LocalDateTime.now(),DateTimeUtil.PATTERN) + "\r\n");
		return sb;
	}
	/**
	 * 方法描述 : 厨打单小票打印模板
	 * @Author: MeiTao
	 * @Date: 2020/9/26 0026 14:23
	 * @param dto 待打印订单详细信息
	 * @param sb
	 * @return: java.lang.String
	 * @Since version-1.4.0
	 */
	public static String getKitchenTemplate(OrdersDetailMerchantDTO dto,StringBuilder sb){
		//1、小票开头
		sb.append("<FB><FS><center>厨打单");
		if (Objects.equals(dto.getReprint(),Boolean.TRUE)){
			sb.append("(补打)");
		}
		sb.append("</center></FS></FB>\r");
		//送达时间 : 左对齐，换行打印
		String suffix = Objects.equals(dto.getDeliveryWay(),2) ? "自取" :"送达";
		if (!ObjectUtils.isEmpty(dto.getImmediateDeliveryTime())){
			sb.append("立即送达(建议" + dto.getImmediateDeliveryTime() + suffix +  ")\r\n");
		}else {
			sb.append(dto.getDeliveryDate().substring(5) + " " + dto.getEstimateTime() +"-"+ dto.getEstimateEndTime() + suffix +  "\r\n");
		}

		//2、菜品相关数据拼接
		sb.append("\r\n<FH>--------------</FH>菜品<FH>--------------</FH>\r\n\n");
		dto.getOrdersGoods().forEach(good->{
			//菜品名称：原样展示
			//菜品购买份数 ： 右对齐
			sb.append("<LR>"+ good.getGoodsName() +","+ "x"+ good.getQuantity()+"</LR>\r\n");
		});
		//赠品
		if (BaseUtil.judgeList(dto.getOrdersGiftGoods())){
			dto.getOrdersGiftGoods().forEach(gift->{
				//菜品名称：原样展示
				sb.append("<LR>"+"(赠)"+gift.getGoodsName() +","+"x" + gift.getQuantity()+"</LR>\r\n");
			});
		}

		//3、订单备注
		handleNoteInfo(dto,sb);

		//4、订单基本信息
		sb.append("<FB>--------------------------------</FB>");
		sb.append("下单时间 ："+ DateTimeUtil.getDateTimeDisplayString(dto.getBillingTime(),DateTimeUtil.PATTERN)+"\r\n");
		sb.append("订单编号 ：" + dto.getOrderNumber() + "\r\n");
		sb.append("打印时间 ：" + DateTimeUtil.getDateTimeDisplayString(LocalDateTime.now(),DateTimeUtil.PATTERN) + "\r\n");
		return sb.toString();
	}

	/**
	 * 方法描述 : 商品相关数据拼接
	 * @Author: MeiTao
	 * @Date: 2020/9/23 0023 14:45
	 * @param dto
	 * @param sb 请求参数
	 * @Since version-1.4.0
	 */
	private static void handleGoodsInfo(OrdersDetailMerchantDTO dto,StringBuilder sb){
		sb.append("\r\n<FH>--------------</FH>菜品<FH>--------------</FH>\r\n\n");
		//sb.append("<center>**************菜品**************</center>\r\n");
		//普通商品
		if (BaseUtil.judgeList(dto.getOrdersGoods())){
			dto.getOrdersGoods().forEach(good->{
				//菜品名称：原样展示
				sb.append(good.getGoodsName() +"\r\n");
				//菜品购买份数，菜品原价 ： 左右对齐
				sb.append("<LR>"+ "x" + good.getQuantity()+","+good.getStorePrice()+"</LR>\r\n");
			});
		}
		//赠品
		if (BaseUtil.judgeList(dto.getOrdersGiftGoods())){
			dto.getOrdersGiftGoods().forEach(gift->{
				//菜品名称：原样展示
				sb.append("<LR>"+"(赠)"+gift.getGoodsName() +","+"x" + gift.getQuantity()+"</LR>\r\n");
			});
		}
	}

	/**
	 * 方法描述 : 订单备注信息处理
	 * @Author: MeiTao
	 * @Date: 2020/9/23 0023 14:49
	 * @param dto
	 * @param sb 请求参数
	 * @Since version-1.4.0
	 */
	private static void handleNoteInfo(OrdersDetailMerchantDTO dto,StringBuilder sb){
		Boolean b = dto.getTableware() == null || dto.getTableware()== 0;
		if ( b && !BaseUtil.judgeString(dto.getRemarks())){
			return;
		}
		sb.append("<FH>--------------------------------</FH>\r\n");
		sb.append("<FB>备注 ：");
		String tablewareStr = null;
		//处理餐具数
		if (dto.getTableware() != null && dto.getTableware()!= 0 ){
			tablewareStr = Objects.equals(10 ,dto.getTableware()) ? dto.getTableware() + "份以上餐具": dto.getTableware() + "份餐具";
		}
		if (BaseUtil.judgeString(dto.getRemarks())&&BaseUtil.judgeString(tablewareStr)){
			tablewareStr = tablewareStr + "/";
		}

		//处理订单备注
		if (BaseUtil.judgeString(dto.getRemarks())){
			tablewareStr = BaseUtil.judgeString(tablewareStr)?tablewareStr + dto.getRemarks():dto.getRemarks();
		}
		sb.append(tablewareStr);
		sb.append("</FB>\r\n");
	}


	public static OrdersDetailMerchantDTO getTestData(){
		//1、订单详细信息
		OrdersDetailMerchantDTO ordersDetailDto = new OrdersDetailMerchantDTO();
		ordersDetailDto.setOrderNumber("736625302314156032");
		ordersDetailDto.setBillingTime(LocalDateTime.now());
		ordersDetailDto.setConsigneeName("不哈哈(先生)");
		ordersDetailDto.setConsigneePhone("15323562356");
		ordersDetailDto.setEstimateTime("08:00");
		ordersDetailDto.setEstimateEndTime("08:30");
		ordersDetailDto.setEstimateDate("2020-09-02");
		ordersDetailDto.setConsigneeAddress("四川省成都市高新区环球中心F栋10楼12层");
		ordersDetailDto.setDeliveryWay(1);
		ordersDetailDto.setDeliveryWayStr("外卖配送");
		ordersDetailDto.setGoodsAmount(new BigDecimal("56.00"));
		ordersDetailDto.setOrderAmount(new BigDecimal("20.00"));
		ordersDetailDto.setDiscountFee(new BigDecimal("41.00"));
		ordersDetailDto.setDeliveryPriceOriginal(new BigDecimal("5.00"));
		ordersDetailDto.setTakeAddress("测试商家");
		ordersDetailDto.setRemarks("不要辣少葱，谢谢！");
		ordersDetailDto.setTableware(2);
		ordersDetailDto.setPackPrice(new BigDecimal(2));
		ordersDetailDto.setDeliveryDate("2020-09-02");
		//1、订单商品信息
		OrdersGoodsMerchantDTO orderGood1 = new OrdersGoodsMerchantDTO();
		orderGood1.setGoodsName("招牌卤肉饭套餐");
		orderGood1.setQuantity(1);
		orderGood1.setStorePrice(new BigDecimal("36.00"));

		OrdersGoodsMerchantDTO orderGood2 = new OrdersGoodsMerchantDTO();
		orderGood2.setGoodsName("招牌卤肉饭套餐");
		orderGood2.setQuantity(1);
		orderGood2.setStorePrice(new BigDecimal("20.00"));

		ordersDetailDto.setOrdersGoods(Arrays.asList(orderGood1,orderGood2));
		ordersDetailDto.setShopName("测试商家");
		return ordersDetailDto;
	}

	/*		sb.append("<table>");
			sb.append("<tr>");
				sb.append("<td>");
				sb.append("菜品");
				sb.append("</td>");
				sb.append("<td>");
				sb.append("单价");
				sb.append("</td>");
				sb.append("<td>");
				sb.append("小计");
				sb.append("</td>");
			sb.append("</tr>");
			for (Test test : testList) {
				ZMoney=ZMoney+(test.getMoney()*test.getNum());
			sb.append("<tr>");
				sb.append("<td>"+test.getName()+"</td>");
				sb.append("<td>"+test.getMoney()+"</td>");
				sb.append("<td>"+test.getMoney()*test.getNum()+"</td>");
			sb.append("</tr>");
			}
		sb.append("</table>");*/
}