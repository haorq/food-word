package com.meiyuan.catering.admin.web.goods;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.goods.AdminGoodsLabelService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/12 14:37
 * @description 简单描述
 **/
@RestController
@RequestMapping("/admin/goods/label")
@Api(tags = "商品管理-标签管理")
public class AdminGoodsLabelController {
    @Resource
    AdminGoodsLabelService labelService;

    /**
     * 新增修改标签
     *
     * @param dto 新增修改数据DTO
     * @author: wxf
     * @date: 2020/3/12 13:56
     * @return: java.lang.String
     **/
    @PostMapping("/saveUpdate")
    @ApiOperation("新增修改")
    @LogOperation(value = "商品管理-标签新增修改")
    public Result<String> saveUpdate(@RequestBody LabelDTO dto) {
        return labelService.saveUpdate(dto);
    }

    /**
     * 标签列表分页
     *
     * @param dto 基础查询参数
     * @author: wxf
     * @date: 2020/3/11 16:57
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.db.domain.goods.CateringLabelEntity>
     **/
    @PostMapping("/listLimit")
    @ApiOperation("标签列表分页")
    public Result<PageData<LabelDTO>> listLimit(@RequestBody LabelLimitQueryDTO dto) {
        return labelService.listLimit(dto);
    }

    /**
     * 删除标签 没有绑定商品才能删除
     *
     * @param id 标签id
     * @author: wxf
     * @date: 2020/3/11 18:16
     * @return: java.lang.String
     **/
    @GetMapping("/del/{id}")
    @ApiOperation("删除标签")
    @LogOperation(value = "商品管理-删除标签")
    public Result<String> del(@PathVariable(value = "id") Long id) {
        return labelService.del(id);
    }

    /**
     * 获取标签信息
     *
     * @param id 标签id
     * @author: wxf
     * @date: 2020/3/12 11:47
     * @return: com.meiyuan.catering.dto.admin.goods.label.LabelDTO
     **/
    @GetMapping("/getById/{id}")
    @ApiOperation("获取标签信息根据id")
    public Result<LabelDTO> getById(@PathVariable(value = "id") Long id) {
        return labelService.getById(id);
    }

    /**
     * 全部标签
     *
     * @author: wxf
     * @date: 2020/3/21 11:16
     * @return: {@link List < LabelDTO>}
     **/
    @GetMapping("/allLabel")
    @ApiOperation("全部标签")
    public Result<List<LabelDTO>> allLabel() {
        return labelService.allLabel();
    }
}
