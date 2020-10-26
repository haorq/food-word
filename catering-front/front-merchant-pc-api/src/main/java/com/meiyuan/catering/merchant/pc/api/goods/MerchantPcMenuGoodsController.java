package com.meiyuan.catering.merchant.pc.api.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsMenuQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsSaveDTO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsMenuListVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;
import com.meiyuan.catering.merchant.pc.service.goods.PcMerchantMenuGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * description：销售菜单控制层
 * @author yy
 * @date 2020/7/7
 * @version 1.2.0
 */
@RestController
@RequestMapping("/goods/menu")
@Api(tags = "商品-销售菜单")
public class MerchantPcMenuGoodsController {

    @Resource
    private PcMerchantMenuGoodsService pcMerchantMenuGoodsService;

    @ApiOperation("销售菜单-分页查询 V1.2.0")
    @PostMapping("/queryPageList")
    public Result<PageData<MerchantMenuGoodsVO>> queryPageList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                               @RequestBody MerchantMenuGoodsQueryDTO dto) {
        return pcMerchantMenuGoodsService.queryPageList(token, dto);
    }

    @ApiOperation("销售菜单-新增（返回id）/修改 V1.2.0")
    @PostMapping("/save")
    public Result<String> save(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                               @RequestBody MerchantMenuGoodsSaveDTO dto) {
        return pcMerchantMenuGoodsService.save(token, dto);
    }

    @ApiOperation("销售菜单-验证菜单名 V1.2.0")
    @PostMapping("/verificationMenuName")
    public Result<Boolean> verificationMenuName(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                               @RequestBody MerchantMenuGoodsQueryDTO dto) {
        return pcMerchantMenuGoodsService.verificationMenuName(token, dto);
    }


    @ApiOperation("销售菜单-查询详情 V1.2.0")
    @GetMapping("/queryMenuById/{id}")
    public Result<MerchantMenuGoodsDetailsVO> queryMenuById(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                            @PathVariable(value = "id") Long id) {
        return pcMerchantMenuGoodsService.queryMenuById(token,id);
    }

    @PostMapping("/queryMenuPageList")
    @ApiOperation("销售菜单-选择商品 V1.2.0")
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuPageList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                                       @RequestBody MerchantGoodsMenuQueryDTO dto){
        return pcMerchantMenuGoodsService.queryMenuPageList(token,dto);
    }

    @PostMapping("/queryMenuExistencePageList")
    @ApiOperation("销售菜单-已选择商品 V1.2.0")
    public Result<PageData<MerchantGoodsMenuListVO>> queryMenuExistencePageList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO token,
                                                                                @RequestBody MerchantGoodsMenuQueryDTO dto){
        return pcMerchantMenuGoodsService.queryMenuExistencePageList(token,dto);
    }
}
