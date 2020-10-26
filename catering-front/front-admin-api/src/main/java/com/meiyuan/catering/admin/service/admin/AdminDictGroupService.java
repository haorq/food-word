package com.meiyuan.catering.admin.service.admin;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.admin.entity.CateringDictGroupItem;
import com.meiyuan.catering.admin.fegin.DictGroupClient;
import com.meiyuan.catering.admin.service.CateringDictGroupItemService;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import com.meiyuan.catering.core.vo.base.DicIntemVo;
import com.meiyuan.catering.user.entity.CateringUserCompanyEntity;
import com.meiyuan.catering.user.service.CateringUserCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @date 2020/3/18 16:07
 **/
@Service
public class AdminDictGroupService {

    @Resource
    private DictGroupClient dictGroupClient;
    @Resource
    private CateringDictGroupItemService itemService;

    @Resource
    private CateringUserCompanyService companyService;

    @Resource
    private AdminUtils adminUtils;
    @Resource
    private DicUtils dicUtils;


    public Result<List<DicAllVo>> findDicGroup() {
        return dictGroupClient.findDicGroup();
    }

    /**
     * @Author lhm
     * @Description  缓存详情
     * @Date 2020/5/19
     * @Param [codes]
     * @return {@link Result< List< DicDetailsAllVo>>}
     * @Version v1.1.0
     */
    public Result<List<DicDetailsAllVo>> detail(List<String> codes) {
        return dictGroupClient.detail(codes);
    }

    /**
     * @Author lhm
     * @Description  刷新所有缓存
     * @Date 2020/5/19
     * @Param []
     * @return {@link Result}
     * @Version v1.1.0
     */
    public Result refreshDicCache() {
        List<String> codes = new ArrayList<>();
        List<DicDetailsAllVo> dataVos = dictGroupClient.detail(codes).getData();
        if (!CollectionUtils.isEmpty(dataVos)) {
            dataVos.forEach(dictDataVo ->
                dicUtils.refreshDicCache(dictDataVo)
            );
        }
        return Result.succ();
    }


    /**
     * 刷新所有企业id的缓存
     *
     * @return
     */
    public Result refreshCompanyCache() {
        List<CateringUserCompanyEntity> list = companyService.list();
        Map<Long, Integer> map = list.stream().collect(Collectors.toMap(CateringUserCompanyEntity::getId, CateringUserCompanyEntity::getCompanyStatus));
        dicUtils.refreshCompanyCache(map);
        return Result.succ();
    }

   /**
    * @Author lhm
    * @Description  新增广告链接--黑接口
    * @Date 2020/5/19
    * @Param [vos]
    * @return {@link Result}
    * @Version v1.1.0
    */
    public Result addAdvertisingUrl(List<DicIntemVo> vos) {
        List<CateringDictGroupItem> groupItems = vos.stream().map(i -> {
            CateringDictGroupItem item = new CateringDictGroupItem();
            item.setCode(IdWorker.getIdStr());
            item.setName(i.getItemName());
            item.setDetails(i.getItemDetails());
            item.setGroupId(36L);
            return item;
        }).collect(Collectors.toList());
        itemService.saveBatch(groupItems);
        return refreshDicCache();
    }


    public Result deleteAdvertisingUrl(Long itemId) {
        itemService.removeById(itemId);
        return refreshDicCache();
    }
}
