package com.meiyuan.catering.user.fegin.address;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.address.AddressDTO;
import com.meiyuan.catering.user.entity.CateringAddressEntity;
import com.meiyuan.catering.user.service.CateringAddressService;
import com.meiyuan.catering.user.vo.address.AddressDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 13:43
 * @description
 **/
@Service
public class AddressClient {

    @Resource
    private CateringAddressService cateringAddressService;


    public void updateBatchById(List<AddressDTO> list) {
        List<CateringAddressEntity> entities = BaseUtil.objToObj(list, CateringAddressEntity.class);
        cateringAddressService.updateBatchById(entities);
    }

    public Result<Boolean> save(AddressDTO dto) {
        CateringAddressEntity addressEntity = BaseUtil.objToObj(dto, CateringAddressEntity.class);
        return Result.succ(cateringAddressService.save(addressEntity));

    }

    public Result<AddressDTO> getById(Long id) {
        CateringAddressEntity byId = cateringAddressService.getById(id);
        return Result.succ(BaseUtil.objToObj(byId, AddressDTO.class));
    }

    public Result<Boolean> updateById(AddressDTO dto) {
        CateringAddressEntity entity = BaseUtil.objToObj(dto, CateringAddressEntity.class);
        return Result.succ(cateringAddressService.updateById(entity));
    }

    public Result<List<AddressDTO>> list(LambdaQueryWrapper<CateringAddressEntity> queryWrapper) {
        List<CateringAddressEntity> list = cateringAddressService.list(queryWrapper);
        List<AddressDTO> dtos =new ArrayList<>();
        if(BaseUtil.judgeList(list)){
           dtos = BaseUtil.objToObj(list, AddressDTO.class);
        }
        return Result.succ(dtos);
    }


    /**
     * 收货地址详情
     *
     * @param id
     * @return
     */
    public Result<AddressDetailVo> userAddressDetail(Long id) {
        CateringAddressEntity entity = cateringAddressService.getById(id);
        AddressDetailVo vo = new AddressDetailVo();
        if (!ObjectUtils.isEmpty(entity)) {
            BeanUtils.copyProperties(entity, vo);
        }
        return Result.succ(vo);
    }
}
