package com.meiyuan.catering.goods.service.impl;

import com.meiyuan.catering.goods.service.CateringGoodsExtendPropertyService;
import com.meiyuan.catering.goods.dao.CateringGoodsExtendPropertyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商品扩展属性表(CateringGoodsExtendProperty)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsExtendPropertyServiceImpl implements CateringGoodsExtendPropertyService {
    @Resource
    private CateringGoodsExtendPropertyMapper cateringGoodsExtendPropertyMapper;

    
}