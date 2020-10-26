package com.meiyuan.catering.merchant.goods.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantMenuGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantMenuGoodsEntity;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Mapper
public interface CateringMerchantMenuGoodsMapper extends BaseMapper<CateringMerchantMenuGoodsEntity> {

    /**
     * describe: 销售菜单-分页查询
     * @author: yy
     * @date: 2020/7/29 18:27
     * @param page
     * @param dto
     * @return: {@link IPage< MerchantMenuGoodsVO>}
     * @version 1.2.0
     **/
    IPage<MerchantMenuGoodsVO> queryPageList(@Param("page") Page page,
                                             @Param("dto") MerchantMenuGoodsQueryDTO dto);

    /**
     * describe: 销售菜单-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/7/8 14:25
     * @return: {com.meiyuan.catering.merchant.goods.vo.MerchantMenuGoodsDetailsVO}
     * @version 1.2.0
     **/
    MerchantMenuGoodsDetailsVO queryMenuById(@Param("merchantId") Long merchantId, @Param("id") Long id);

    /**
     * describe: 查询商户下所有已建立过菜单的门店id
     *
     * @param merchantId
     * @author: yy
     * @date: 2020/7/23 15:14
     * @return: {@link List< Long>}
     * @version 1.2.0
     **/
    List<Long> queryMenuShopByMerchantId(@Param("merchantId") Long merchantId);

    /**
     * 方法描述   销售菜单 取消授权--删除菜单的关联信息
     * @author: lhm
     * @date: 2020/7/23 14:04
     * @param merchantId
     * @param goodsId
     * @return: {@link }
     * @version 1.1.0
     **/
    Boolean deleteByGoodsIdAndMerchantId(@Param("merchantId") Long merchantId, @Param("goodsId") Long goodsId);


}
