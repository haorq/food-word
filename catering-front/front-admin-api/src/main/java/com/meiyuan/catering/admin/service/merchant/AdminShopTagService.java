package com.meiyuan.catering.admin.service.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.tag.ShopTagAddDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagListDTO;
import com.meiyuan.catering.merchant.dto.tag.ShopTagQueryDTO;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.feign.ShopTagClient;
import com.meiyuan.catering.merchant.vo.ShopTagDetailVo;
import com.meiyuan.catering.merchant.vo.ShopTagVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/3/16 11:04
 **/
@Service
public class AdminShopTagService {

    @Resource
    private ShopTagClient shopTagClient;
    @Resource
    private ShopClient shopClient;



    /**
     * 店铺管理--店铺标签列表
     *
     * @param dto
     * @return
     */
    public Result<IPage<ShopTagVo>> shopTagList(ShopTagListDTO dto) {
        IPage<ShopTagVo> page = shopTagClient.shopTagList(dto).getData();
        return Result.succ(page);
    }

    /**
     * 添加标签
     *
     * @param dto
     * @return
     */
    public Result<Boolean> addTag(ShopTagAddDTO dto) {
        Boolean aBoolean = shopTagClient.addTag(dto).getData();
        //修改店铺相关标签缓存:修改标签成功
        if (dto.getId() != null){
            if (aBoolean){
                shopClient.putAllShopTagInfo();
            }
        }
        return Result.succ(aBoolean);
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    public Result<Boolean> deleteById(String id) {
        Boolean aBoolean = shopTagClient.deleteById(id).getData();
        if (aBoolean){
            shopClient.putAllShopTagInfo();
        }
        return Result.succ(aBoolean);
    }

    public Result<Map<Object, Object>> queryById(ShopTagQueryDTO dto) {
        Map<Object, Object> map = new HashMap<>(3);
        IPage<ShopTagDetailVo> page = shopTagClient.queryById(dto).getData();
        ShopTagVo tagVo = shopTagClient.selectOne(dto.getId()).getData();
        map.put("tagVo", tagVo);
        map.put("page", page);
        return Result.succ(map);
    }


    /**
     * 查询全部标签
     *
     * @return
     */
    public Result<Object> queryAll() {

        return shopTagClient.queryAll();
    }

    public Result queryByIdTag(String id) {
       return  shopTagClient.getById(id);
    }
}
