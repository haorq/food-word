package com.meiyuan.catering.merchant.pc.service.goods;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.meiyuan.catering.admin.fegin.WxCategoryClient;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.enums.base.OperateTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.es.GoodsEsDeteleDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCancelDTO;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.feign.GoodsLabelRelationClient;
import com.meiyuan.catering.goods.feign.LabelClient;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsUpOrDownDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsDetailsVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantGoodsListVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @date 2020/7/6
 * @description
 **/
@Service
public class PcMerchantGoodsService {

    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private GoodsLabelRelationClient goodsLabelRelationClient;
    @Autowired
    private LabelClient labelClient;
    @Autowired
    private   MerchantUtils merchantUtils;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;
    @Autowired
    private WxCategoryClient wxCategoryClient;
    @Autowired
    private ShopGoodsClient shopGoodsClient;
    /**
     * 描述：商户商品 新增/修改
     *
     * @param dto
     * @return {@link Result< String>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<Boolean> saveOrUpdateMerchantGoods(MerchantGoodsDTO dto) {
        Result<Boolean> result = merchantGoodsClient.saveOrUpdateMerchantGoods(dto);
        return result;
    }

    /**
     * 描述：商品列表查询
     *
     * @param dto
     * @return {@link Result< MerchantGoodsListVO>}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public Result<PageData<MerchantGoodsListVO>> merchantGoodsList(MerchantGoodsQueryDTO dto) {
        Result<PageData<MerchantGoodsListVO>> result = merchantGoodsClient.merchantGoodsList(dto);

        List<MerchantGoodsListVO> list = result.getData().getList();

        List<Long> goodsIds = list.stream().map(MerchantGoodsListVO::getGoodId).collect(Collectors.toList());
        Map<Long, List<String>> labelMap =new HashMap<>(32);
        Map<Long, List<Long>> activityMap =new HashMap<>(32);
        //标签
        if(BaseUtil.judgeList(goodsIds)){
            labelMap= labelClient.listNameByGoodsId(dto.getMerchanId(), goodsIds);
            //是否参加活动 todo 待验证
            activityMap = marketingGoodsClient.isJoinActivity(dto.getMerchanId(), goodsIds).getData();
        }
        Map<Long, List<String>> finalLabelMap = labelMap;
        Map<Long, List<Long>> finalActivityMap = activityMap;
        list.forEach(vo -> {
            String infoPicture = vo.getInfoPicture();
            String before = BaseUtil.subFirstByComma(infoPicture);
            vo.setInfoPicture(before);
            if(ObjectUtils.isNotEmpty(finalLabelMap)){
                vo.setLabelList(finalLabelMap.get(vo.getGoodId()));
            }
            List<Long> longs = finalActivityMap.get(vo.getGoodId());
            vo.setIsJoinActivity(BaseUtil.judgeList(longs));
        });

        return result;
    }


    /**
     * 描述：商品库--商品详情
     *
     * @param goodsId
     * @return {@link Result< MerchantGoodsDTO>}
     * @author lhm
     * @date 2020/7/7
     * @version 1.2.0
     **/
    public Result<MerchantGoodsDetailsVO> merchantGoodsDetail(Long goodsId, Long merchantId) {
        return merchantGoodsClient.merchantGoodsDetail(goodsId, merchantId);
    }

    /**
     * 描述：pc商品上下架
     *
     * @param dto
     * @return {@link Result< Boolean>}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    public Result<Boolean> merchantGoodsUpOrDown(MerchantGoodsUpOrDownDTO dto) {
        Result<Boolean> result = merchantGoodsClient.merchantGoodsUpOrDown(dto);
        if (result.getData()) {
            // 更新ES商品状态
            GoodsExtToEsDTO esDto = new GoodsExtToEsDTO();
            esDto.setMerchantId(dto.getMerchantId().toString());
            esDto.setGoodsId(dto.getGoodsId());
            esDto.setMerchantGoodsStatus(dto.getMerchantGoodsStatus());
            //V1.3.0 功能  上下架需要还原门店本身的状态

            merchantGoodsClient.sendMerchantGoodsUpdateMsg(esDto);
        }
        return result;
    }

    public Result<List<LabelDTO>> allLabel() {
        return labelClient.allLabel();

    }


    /**
     * 方法描述   v1.3.0 商户商品删除  只能删除商户自己创建的商品  活动中的商品不能删除
     * 1.数据库删除--商户商品表+是否推送给门店（推送至门店的商品也需要删除）+菜单关联商品表
     * 2.es删除--merchantId+goodsId 条件删除
     *
     * @param goodsId
     * @author: lhm
     * @date: 2020/8/4 13:54
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> deleteMerchantGoods(Long merchantId, Long goodsId) {
        //商品相关表数据
        Result<Boolean> result = merchantGoodsClient.deleteMerchantGoods(goodsId);
        if (result.success() && result.getData()) {
            GoodsCancelDTO dto = new GoodsCancelDTO();
            dto.setGoodsId(goodsId);
            dto.setMerchantId(merchantId);
            result = merchantGoodsClient.cancelPush(dto);
            // 处理ES商品信息
            if (result.success() && result.getData()) {
                //异步删除小程序类目
                wxCategoryClient.asyncGoodsDownUpdateWxCategory(goodsId.toString());
                GoodsEsDeteleDTO deteleDTO=new GoodsEsDeteleDTO();
                deteleDTO.setGoodsId(goodsId);
                deteleDTO.setMerchantId(merchantId);
                deteleDTO.setOperateType(OperateTypeEnum.GOODS_DEL.getStatus());
                goodsSenderMq.goodsDeleteFanout(deteleDTO);
            }
        }
        return result;
    }

    public Result<Boolean> updateGoodsSort(MerchantAccountDTO token, MerchantCategoryOrGoodsSortDTO dto) {
        dto.setShopId(1295605101233500161L);
        dto.setMerchantId(1286941948698587137L);
        return shopGoodsClient.updateGoodsSort(dto);

    }
}
