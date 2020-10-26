package com.meiyuan.catering.user.fegin.sharebill;

import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.CartShareBillUserDTO;
import com.meiyuan.catering.user.entity.CateringCartShareBillUserEntity;
import com.meiyuan.catering.user.service.CateringCartShareBillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoozu
 * @description 拼单人
 * @date 2020/5/1915:50
 * @since v1.1.0
 */
@Service
public class CartShareBillUserClient {
    @Autowired
    private CateringCartShareBillUserService shareBillUserService;

    /**
     * @description 获取拼单人信息
     * @author yaozou
     * @date 2020/5/19 15:57
     * @param shareBillNo 拼单号
     * @since v1.1.0
     * @return Result<List<CartShareBillUserDTO>>
     */
    public Result<List<CartShareBillUserDTO>> list(String shareBillNo){
        List<CateringCartShareBillUserEntity> list = shareBillUserService.list(shareBillNo);

        return Result.succ(ConvertUtils.sourceToTarget(list,CartShareBillUserDTO.class));
    }

    /**
     * @description 拼单人信息（key->value格式）
     * @author yaozou
     * @date 2020/4/17 14:24
     * @param shareBillNo 拼单号
     * @since v1.0.0
     * @return
     */
    public Result<Map<Long,CartShareBillUserDTO>> shareBillUserMap(String shareBillNo){
        List<CateringCartShareBillUserEntity> list = shareBillUserService.list(shareBillNo);
        Map<Long,CartShareBillUserDTO> map = new HashMap<>(list.size());
        list.forEach(shareBillUserEntity -> map.put(shareBillUserEntity.getUserId(),ConvertUtils.sourceToTarget(shareBillUserEntity,CartShareBillUserDTO.class)));
        return Result.succ(map);
    }
}
