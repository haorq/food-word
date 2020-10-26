package com.meiyuan.catering.goods.service.impl;

import com.meiyuan.catering.goods.service.CateringGoodsThesaurusService;
import com.meiyuan.catering.goods.dao.CateringGoodsThesaurusMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商品词库(CateringGoodsThesaurus)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsThesaurusServiceImpl implements CateringGoodsThesaurusService {
    @Resource
    private CateringGoodsThesaurusMapper cateringGoodsThesaurusMapper;

    
}