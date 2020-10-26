package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringDictGroupMapper;
import com.meiyuan.catering.admin.entity.CateringDictGroup;
import com.meiyuan.catering.admin.service.CateringDictGroupService;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年3月2日
 */
@Service
public class CateringDictGroupServiceImpl extends ServiceImpl<CateringDictGroupMapper, CateringDictGroup> implements CateringDictGroupService {

    @Resource
    private CateringDictGroupMapper dtsDictGroupMapper;


    /**
     * 查询所有字典
     *
     * @return
     */
    @Override
    public List<DicAllVo> findDicGroup() {
        List<CateringDictGroup> list = list();
        return list.stream().map(
                i -> {
                    DicAllVo allVo = new DicAllVo();
                    BeanUtils.copyProperties(i, allVo);
                    return allVo;
                }
        ).collect(Collectors.toList());

    }


    /**
     * 通过多个codes查询字典
     *
     * @param codes
     * @return
     */
    @Override
    public List<DicDetailsAllVo> detail(List<String> codes) {
        return dtsDictGroupMapper.listByCode(codes);
    }

    @Override
    public String queryLink(String link) {
        return baseMapper.queryLink(link);
    }
}
