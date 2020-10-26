package com.meiyuan.catering.admin.web.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.marketing.AdminMarketingRecommendPrizeService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeDetailVO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@RestController
@RequestMapping("/admin/marketing/recommendPrize")
@Api(tags = "营销管理-推荐有奖活动管理")
public class AdminMarketingRecommendPrizeController {
    @Autowired
    private AdminMarketingRecommendPrizeService recommendPrizeService;

    @ApiOperation("推荐有奖活动列表")
    @PostMapping("/listPage")
    public Result<IPage<RecommendPrizeListVO>> listPage(@RequestBody RecommendPrizeQueryDTO queryDTO) {
        if (queryDTO.getEndTime() != null) {
            queryDTO.setEndTime(queryDTO.getEndTime().plusDays(1));
        }
        return recommendPrizeService.listPage(queryDTO);
    }

    @LogOperation("促销活动-新增推荐有奖活动")
    @ApiOperation("新增推荐有奖活动")
    @PostMapping("/create")
    public Result create(@RequestBody @Valid RecommendPrizeDTO prizeDTO) {
        return recommendPrizeService.create(prizeDTO);
    }

    @LogOperation("促销活动-更新推荐有奖活动")
    @ApiOperation("更新推荐有奖活动")
    @PostMapping("/update")
    public Result update(@RequestBody @Valid RecommendPrizeDTO prizeDTO) {
        return recommendPrizeService.update(prizeDTO);
    }

    @ApiOperation("推荐有奖活动详情")
    @GetMapping("/detail/{id}")
    public Result<RecommendPrizeDetailVO> detail(@PathVariable Long id) {
        return recommendPrizeService.detail(id);
    }

    @LogOperation("促销活动-删除推荐有奖活动")
    @ApiOperation("删除推荐有奖活动")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return recommendPrizeService.delete(id);
    }
}
