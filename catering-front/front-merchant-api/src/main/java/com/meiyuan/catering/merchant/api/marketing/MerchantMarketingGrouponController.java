package com.meiyuan.catering.merchant.api.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.MerchantGrouponQueryDTO;
import com.meiyuan.catering.marketing.enums.MarketingGrouponStatusEnum;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponDetailVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponListVO;
import com.meiyuan.catering.marketing.vo.groupon.MerchantMarketingGrouponDetailVO;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.service.marketing.MerchantMarketingGrouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author luohuan
 * @date 2020/3/23
 **/
@RestController
@RequestMapping("/app/marketing/groupon")
@Api(tags = "营销-团购活动")
public class MerchantMarketingGrouponController {

    @Autowired
    private MerchantMarketingGrouponService grouponService;

    @ApiOperation("分页查询团购活动列表")
    @PostMapping("/listPage")
    public Result<PageData<MerchantGrouponListVO>> listPage(@LoginMerchant MerchantTokenDTO token, @RequestBody MerchantGrouponQueryDTO queryDTO) {
        Result<IPage<MerchantGrouponListVO>> pageResult = grouponService.listPage(queryDTO, token.getShopId());
        if (pageResult == null || pageResult.failure()) {
            return Result.succ(new PageData<>());
        }
        IPage<MerchantGrouponListVO> page = pageResult.getData();
        List<MerchantGrouponListVO> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(e -> {
                e.setSourceStr(SourceEnum.parse(e.getSource()).getDesc());
                e.setStatusStr(MarketingGrouponStatusEnum.parse(e.getStatus()).getDesc());
            });
        }
        return Result.succ(new PageData<>(records, page.getTotal(), page.getPages() == queryDTO.getPageNo()));
    }

    @ApiOperation("查询团购活动详情")
    @GetMapping("/detail/{id}")
    public Result<MerchantGrouponDetailVO> detail(@LoginMerchant MerchantTokenDTO token, @PathVariable Long id) {
        Result<MerchantGrouponDetailVO> result = grouponService.detail(id);
        if (result == null || result.failure()) {
            return Result.succ(new MerchantGrouponDetailVO());
        }
        MerchantGrouponDetailVO detailVO = result.getData();
        if (detailVO != null) {
            detailVO.setSourceStr(SourceEnum.parse(detailVO.getSource()).getDesc());
            detailVO.setStatusStr(MarketingGrouponStatusEnum.parse(detailVO.getStatus()).getDesc());
        }
        return Result.succ(detailVO);
    }

    @ApiOperation("开启虚拟成团")
    @PostMapping("/turnOn/virtual/{id}")
    public Result turnOnVirtual(@LoginMerchant MerchantTokenDTO token, @PathVariable Long id) {
        return grouponService.turnOnVirtual(id);
    }

    @GetMapping("/freeze/{id}")
    @ApiOperation("V1.3.0冻结团购活动")
    public Result<String> freeze(@LoginMerchant MerchantTokenDTO token, @PathVariable("id") Long id) {
        return grouponService.freeze(id);
    }

}
