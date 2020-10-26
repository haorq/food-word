package com.meiyuan.catering.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.shop.ShopEmployeeDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;

import java.util.List;

/**
 * @author fql
 */
public interface CateringShopEmployeeService extends IService<CateringShopEmployeeEntity> {


    /**
     * 方法描述 ： 员工列表
     * @param dto 查询条件
     * @return 员工列表
     */
    PageData<ShopEmployeeDTO> queryPageShopEmployee(EmployeeQueryPageDTO dto);

    /**
     * 方法描述：根据id删除员工
     *
     * @param id 员工id
     * @return 操作结果信息
     */
    String delEmployeeById(Long id);

    /**
     *  方法描述 ：新增/编辑员工信息
     * @param shopEmployeeDTO 参数
     * @return 操作结果信息
     */
    Long saveUpdateEmployeeInfo(ShopEmployeeDTO shopEmployeeDTO);

    /**
     * describe:
     * @author: fql
     * @date: 2020/10/9 18:16
     * @param shopId 商户id
     * @return: {@link String}
     * @version 1.5.0
     **/
    String delAllEmployee(Long shopId);

    /**
     * describe:
     * @author: fql
     * @date: 2020/10/9 18:16
     * @param shopId 商户id
     * @return: {@link String}
     * @version 1.5.0
     **/
    Result<List<Long>> delAllEmployeePc(Long shopId);

    /**
     * describe:
     * @author: fql
     * @date: 2020/10/9 18:32
     * @param shopId 商户id
     * @return: {@link List< CateringShopEmployeeEntity>}
     * @version 1.5.0
     **/
    List<CateringShopEmployeeEntity> selectShopEmployee(Long shopId);

}
