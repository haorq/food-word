package com.meiyuan.catering.goods.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.dto.goods.GoodsGiftListDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsGiftEntity;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 赠品表(CateringGoodsGift)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-18 18:34:32
 */
@Mapper
public interface CateringGoodsGiftMapper extends BaseMapper<CateringGoodsGiftEntity>{

    /**
     * 查询赠品列表
     * @param dto
     * @return
     */
    List<GoodsGiftListVo> listGiftGood(GoodsGiftListDTO dto);

    /**
     * 最大赠品编码
     *
     * @author: wxf
     * @date: 2020/6/22 14:01
     * @return: {@link CateringGoodsGiftEntity}
     * @version 1.1.0
     **/
    CateringGoodsGiftEntity maxDbCode();
}