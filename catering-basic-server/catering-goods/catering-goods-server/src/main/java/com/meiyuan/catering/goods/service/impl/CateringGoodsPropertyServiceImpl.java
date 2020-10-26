package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.goods.dao.CateringGoodsPropertyMapper;
import com.meiyuan.catering.goods.entity.CateringGoodsPropertyEntity;
import com.meiyuan.catering.goods.service.CateringGoodsPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品属性表(CateringGoodsProperty)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsPropertyServiceImpl extends ServiceImpl<CateringGoodsPropertyMapper, CateringGoodsPropertyEntity>
        implements CateringGoodsPropertyService {
    @Resource
    private CateringGoodsPropertyMapper cateringGoodsPropertyMapper;


    /**
     * 获取集合根据商品id
     *
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/3/21 14:15
     * @return: {@link List < CateringGoodsPropertyEntity>}
     **/
    @Override
    public List<CateringGoodsPropertyEntity> listByGoodsId(Long goodsId) {
        return cateringGoodsPropertyMapper.listByGoodsId(goodsId);
    }
}