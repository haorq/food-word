package com.meiyuan.catering.admin.service.goods;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GiftDTO;
import com.meiyuan.catering.goods.dto.gift.GiftLimitQueryDTO;
import com.meiyuan.catering.goods.feign.GoodsGiftClient;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author wxf
 * @date 2020/3/16 14:55
 * @description 赠品聚合层
 **/
@Service
public class AdminGoodsGiftService {

    @Resource
    GoodsGiftClient giftClient;

    /**
     * 查询所有赠品
     *
     * @author: wxf
     * @date: 2020/5/19 14:56
     * @return: {@link Result< List< GoodsGiftListVo>>}
     * @version 1.0.1
     **/
    public Result<List<GoodsGiftListVo>> listShop() {
        return giftClient.listShop();
    }

    /**
     * 新增修改
     *
     * @author: wxf
     * @date: 2020/5/19 11:58
     * @param dto 新增修改赠品数据
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> saveUpdate(GiftDTO dto) {
        return giftClient.saveUpdate(dto);
    }

    /**
     * 赠品列表
     *
     * @author: wxf
     * @date: 2020/5/19 14:49
     * @param dto 列表查询参数
     * @return: {@link Result< PageData< GiftDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<GiftDTO>> listLimit(GiftLimitQueryDTO dto) {
        return giftClient.listLimit(dto);
    }

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/5/19 14:51
     * @param giftId 赠品id
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(Long giftId) {
        return giftClient.del(giftId);
    }

    /**
     * 赠品详情
     *
     * @author: wxf
     * @date: 2020/3/21 17:49
     * @param giftId 赠品id
     * @return: {@link GiftDTO}
     * @version 1.0.1
     **/
    public Result<GiftDTO> getGiftInfoById(Long giftId) {
        return giftClient.getGiftInfoById(giftId);
    }
}
