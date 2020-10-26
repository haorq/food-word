package com.meiyuan.catering.admin.service.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.GoodsDTO;
import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import com.meiyuan.catering.goods.dto.label.LabelRelationDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.goods.feign.GoodsLabelRelationClient;
import com.meiyuan.catering.goods.feign.LabelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/16 10:25
 * @description 简单描述
 **/
@Service
public class AdminGoodsLabelService {
    @Resource
    LabelClient labelClient;
    @Resource
    GoodsLabelRelationClient labelRelationClient;
    @Resource
    GoodsClient goodsClient;

    /**
     * 新增修改标签
     *
     * @author: wxf
     * @date: 2020/5/19 15:19
     * @param dto 新增修改数据DTO
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(LabelDTO dto) {
        return labelClient.saveUpdate(dto);
    }

    /**
     * 标签列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 15:28
     * @param dto 基础查询参数
     * @return: {@link Result< PageData< LabelDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<LabelDTO>> listLimit(LabelLimitQueryDTO dto) {
        return labelClient.listLimit(dto);
    }

    /**
     * 删除标签
     * 默认标签不能删除
     * @author: wxf
     * @date: 2020/5/19 15:31
     * @param id 标签id
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(Long id) {
        return labelClient.del(id);
    }

    /**
     * 获取标签信息
     *
     * @author: wxf
     * @date: 2020/5/19 15:32
     * @param id 标签id
     * @return: {@link Result< LabelDTO>}
     * @version 1.0.1
     **/
    public Result<LabelDTO> getById(Long id) {
        Result<LabelDTO> result = labelClient.getById(id);
        LabelDTO dto = null;
        if (result.success() && null != result.getData()) {
            dto = result.getData();
            Result<List<LabelRelationDTO>> listResult = labelRelationClient.listByLabelId(id);
            if (listResult.success() && BaseUtil.judgeList(listResult.getData())) {
                List<LabelRelationDTO> labelList = listResult.getData();
                List<Long> goodsIdList = labelList.stream().map(LabelRelationDTO::getGoodsId).collect(Collectors.toList());
                if (BaseUtil.judgeList(goodsIdList)) {
                    Result<List<GoodsDTO>> goodsResult = goodsClient.listByIdList(goodsIdList);
                    if (goodsResult.success() && BaseUtil.judgeList(goodsResult.getData())) {
                        List<GoodsDTO> goodsList = goodsResult.getData();
                        dto.setSimpleGoodsDTO(BaseUtil.objToObj(goodsList, SimpleGoodsDTO.class));
                        dto.setGoodsCount(goodsList.size());
                    }
                }
            }
        }
        return Result.succ(dto);
    }

    /**
     * 全部标签
     *
     * @author: wxf
     * @date: 2020/3/21 11:16
     * @return: {@link List < LabelDTO>}
     * @version 1.0.1
     **/
    public Result<List<LabelDTO>> allLabel() {
        return labelClient.allLabel();
    }
}
