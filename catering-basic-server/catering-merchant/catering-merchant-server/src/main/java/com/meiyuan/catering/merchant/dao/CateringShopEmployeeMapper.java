package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.dto.shop.ShopEmployeeDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author fql
 */
@Mapper
public interface CateringShopEmployeeMapper extends BaseMapper<CateringShopEmployeeEntity> {

    /**
     * 方法描述 ： 员工列表
     * @param dto 查询条件
     * @param page 分页
     * @return 员工列表
     */
    IPage<ShopEmployeeDTO> queryPageShopEmployee(@Param("page") Page page, @Param("dto") EmployeeQueryPageDTO dto);

    /**
     * describe: 查询所有有效电话号码
     * @author: fql
     * @date: 2020/9/30 18:05
     * @return: {@link List<String>}
     * @version 1.5.0
     **/
    @Select("select phone from catering_shop_employee where is_del = 0")
    List<String> selectAll();

    CateringShopEmployeeEntity selectByPhone(@Param("phone") String phone);

}
