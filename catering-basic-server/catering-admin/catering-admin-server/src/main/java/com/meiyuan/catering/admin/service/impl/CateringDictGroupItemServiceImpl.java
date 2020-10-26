package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringDictGroupItemMapper;
import com.meiyuan.catering.admin.entity.CateringDictGroupItem;
import com.meiyuan.catering.admin.service.CateringDictGroupItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/4/7 10:55
 **/
@Service
public class CateringDictGroupItemServiceImpl extends ServiceImpl<CateringDictGroupItemMapper, CateringDictGroupItem> implements CateringDictGroupItemService {
    @Resource
    private CateringDictGroupItemMapper cateringDictGroupItemMapper;
}
