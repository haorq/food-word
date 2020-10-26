package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.user.dao.CateringAddressMapper;
import com.meiyuan.catering.user.entity.CateringAddressEntity;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.entity.CateringUserEntity;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.query.address.AddressQueryDTO;
import com.meiyuan.catering.user.service.CateringAddressService;
import com.meiyuan.catering.user.vo.address.UserAddressListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址表(CateringAddress)表服务实现类
 *
 * @author mei-tao
 * @since 2020-03-10 15:31:41
 */
@Service("cateringAddressService")
public class CateringAddressServiceImpl  extends ServiceImpl<CateringAddressMapper, CateringAddressEntity> implements CateringAddressService{
    @Resource
    private CateringAddressMapper cateringAddressMapper;




}
