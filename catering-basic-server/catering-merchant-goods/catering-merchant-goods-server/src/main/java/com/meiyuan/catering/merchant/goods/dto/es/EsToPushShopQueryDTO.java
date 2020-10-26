package com.meiyuan.catering.merchant.goods.dto.es;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author lhm
 * @date 2020/7/13
 * @description
 **/
@Data
@ApiModel("推送门店查询模型")
public class EsToPushShopQueryDTO {
  private   Long goodId;
  private  Long shopId;
  private  List<String> skuCodes;

}
