package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.goods.GoodsSort;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.goods.dto.category.CategoryDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.goods.MerchantGoodsNameQueryDTO;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.goods.dao.CateringMerchantCategoryMapper;
import com.meiyuan.catering.merchant.goods.dao.CateringShopGoodsSpuMapper;
import com.meiyuan.catering.merchant.goods.dto.goods.GoodsSortMaxDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringCategoryShopRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsExtendEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.enums.GoodsAddTypeEnum;
import com.meiyuan.catering.merchant.goods.mq.sender.MerchantGoodsSenderMq;
import com.meiyuan.catering.merchant.goods.service.CateringCategoryShopRelationService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantCategoryService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsExtendService;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSpuService;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryDownVO;
import com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.msg.request.ParamRequest;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.provider.ConfigFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
@Slf4j
public class CateringMerchantCategoryServiceImpl extends ServiceImpl<CateringMerchantCategoryMapper, CateringMerchantCategoryEntity>
        implements CateringMerchantCategoryService {

    @Resource
    private CateringMerchantCategoryMapper cateringMerchantCategoryMapper;
    @Autowired
    private MerchantGoodsSenderMq merchantGoodsSenderMq;
    @Autowired
    private CateringMerchantGoodsExtendService merchantGoodsExtendService;
    @Autowired
    private CateringCategoryShopRelationService cateringCategoryShopRelationService;
    @Autowired
    private CateringShopGoodsSpuMapper shopGoodsSpuMapper;

    @Autowired
    private CateringShopGoodsSpuService shopGoodsSpuService;
    /**
     * 分类商品关联
     */
    @Resource
    private CateringMerchantGoodsExtendService cateringMerchantGoodsExtendService;

    @Resource
    private MerchantUtils merchantUtils;



    private  final Integer sort1=1;

    /**
     * describe: 商品分类-新增/修改
     *
     * @param dto
     * @param categoryAndType true为商户新增分类  false为门店新增分类
     * @author: yy
     * @date: 2020/7/7 10:54
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(MerchantCategorySaveDTO dto, Boolean categoryAndType) {
        Long merchantId = dto.getMerchantId();

        CateringMerchantCategoryEntity entity = BaseUtil.objToObj(dto, CateringMerchantCategoryEntity.class);
        entity.setDefaultCategory(dto.getDefaulCategory());
        Long id = entity.getId() == null ? IdWorker.getId() : entity.getId();
        //名称验重
        String categoryName = entity.getCategoryName();
        if (this.isCategoryName(id, categoryName, merchantId)) {
            throw new CustomException("分类名称【" + categoryName + "】已存在，请重新输入");
        }
        // 新增
        if (dto.getId() == null) {
            boolean save = isSave(categoryAndType, entity, id);
            return String.valueOf(save);
        }
        boolean flag = this.updateById(entity);
        //修改
        if (flag) {
            //修改关联商品的分类名称
            boolean update = isUpdate(entity);
            if (update) {
                // 同步ES
                sendCategoryEs(merchantId, entity, id);
            }

        }
        return String.valueOf(flag);
    }

    private boolean isSave(Boolean categoryAndType, CateringMerchantCategoryEntity entity, Long id) {
        entity.setId(id);
        //设置分类添加来源
        if (categoryAndType) {
            entity.setCategoryAddType(GoodsAddTypeEnum.MERCHANT.getStatus());
        } else {
            entity.setCategoryAddType(GoodsAddTypeEnum.SHOP.getStatus());
            //关联关系表插入一条信息
            saveCategoryShopRe(entity, id);
        }
        return this.save(entity);
    }


    /**
     * 方法描述   修改关联关系
     *
     * @param entity
     * @author: lhm
     * @date: 2020/8/19 14:21
     * @return: {@link }
     * @version 1.3.0
     **/
    private boolean isUpdate(CateringMerchantCategoryEntity entity) {
        UpdateWrapper<CateringMerchantGoodsExtendEntity> query = new UpdateWrapper<>();
        query.lambda().set(CateringMerchantGoodsExtendEntity::getCategoryName, entity.getCategoryName()).eq(CateringMerchantGoodsExtendEntity::getCategoryId, entity.getId());
        return merchantGoodsExtendService.update(query);
    }


    /**
     * 方法描述   发送修改分类的es
     *
     * @param merchantId
     * @param entity
     * @param id
     * @author: lhm
     * @date: 2020/8/19 14:20
     * @return: {@link }
     * @version 1.3.0
     **/
    private void sendCategoryEs(Long merchantId, CateringMerchantCategoryEntity entity, Long id) {
        MerchantGoodsToEsDTO esDTO = new MerchantGoodsToEsDTO();
        esDTO.setCategoryId(id.toString());
        esDTO.setCategoryName(entity.getCategoryName());
        esDTO.setMerchantId(merchantId.toString());
        merchantGoodsSenderMq.sendMerchantGoodsCategoryUpdateMsg(esDTO);
    }

    private void saveCategoryShopRe(CateringMerchantCategoryEntity dto, Long id) {
        //查询最大的sort值
        Integer count = getSortMax(dto.getMerchantId());

        CateringCategoryShopRelationEntity categoryShopRelationEntity = new CateringCategoryShopRelationEntity();
        categoryShopRelationEntity.setCategoryId(id);
        categoryShopRelationEntity.setShopId(dto.getMerchantId());
        categoryShopRelationEntity.setCategoryAddType(dto.getCategoryAddType());
        //TODO 待处理
        categoryShopRelationEntity.setSort(count);

        cateringCategoryShopRelationService.save(categoryShopRelationEntity);
    }


    /**
     * describe: 商品分类-列表分页查询
     *
     * @param merchantCategoryQueryDTO
     * @author: yy
     * @date: 2020/7/7 10:54
     * @return: {com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>}
     * @version 1.2.0
     **/
    @Override
    public PageData<MerchantCategoryVO> queryPageList(MerchantCategoryQueryDTO merchantCategoryQueryDTO) {
        Page<MerchantCategoryQueryDTO> page = new Page<>(merchantCategoryQueryDTO.getPageNo(), merchantCategoryQueryDTO.getPageSize());
        IPage<MerchantCategoryVO> iPage = this.baseMapper.queryPageList(page, merchantCategoryQueryDTO);
        return new PageData<>(iPage);
    }

    /**
     * describe: 商品分类-查询所有
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/9 9:37
     * @return: {java.util.List<com.meiyuan.catering.merchant.goods.vo.MerchantCategoryVO>}
     * @version 1.2.0
     **/
    @Override
    public List<MerchantCategoryDownVO> queryAll(MerchantCategoryQueryDTO dto) {
        return this.baseMapper.queryAll(dto);
    }

    /**
     * describe: 商品分类-查询详情
     *
     * @param merchantId
     * @param id
     * @author: yy
     * @date: 2020/8/3 10:46
     * @return: {@link CateringMerchantCategoryEntity}
     * @version 1.2.0
     **/
    @Override
    public CateringMerchantCategoryEntity queryCategoryById(Long merchantId, Long id) {
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getId, id)
                .eq(CateringMerchantCategoryEntity::getMerchantId, merchantId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * describe: 商品分类-删除数据
     *
     * @param merchantId
     * @param id
     * isShop true 为是门店的操作  false为商户的操作
     * @author: yy
     * @date: 2020/7/10 17:33
     * @return: {java.lang.String}
     * @version 1.2.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long merchantId, Long id,boolean isShop) {
        CateringMerchantCategoryEntity deleteEntity = getById(id);
        if (ObjectUtils.isEmpty(deleteEntity)) {
            throw new CustomException("不存在此分类！");
        }

        // 找到默认分类 id
        Long defaultId;
        CateringMerchantCategoryEntity entity = this.getDefaultCategoryEntity(merchantId);
        if(ObjectUtils.isEmpty(entity)){
            //如果没有默认分类  则重新创建默认分类 （是为了处理之前的老数据）
            entity = setCategoryEntity(merchantId);
        }
        defaultId = entity.getId();
        String defaultName = entity.getCategoryName();
        if (id.equals(defaultId)) {
            throw new CustomException("默认商品分类不能删除！");
        }
        long shopId = 0;
        if(isShop){
            shopId=merchantId;
            merchantId=merchantUtils.getShop(shopId).getMerchantId();
            entity.setMerchantId(merchantId);
        }

        List<GoodsSort> goodsSort = shopGoodsSpuMapper.getShopGoodsSort(id, merchantId);

        //修改关联表数据
        cateringMerchantGoodsExtendService.updateByCategoryId(id, entity);
        //删除分类主表数据
        boolean flag = this.removeById(deleteEntity);

        //删除分类之后要将下面的商品移到默认分类下，并且对默认分类下面的商品依次排序

        if(BaseUtil.judgeList(goodsSort)) {
            updateSpu(defaultId, goodsSort);

        }
        //修改spu表商品排序数据
        if (flag) {
            //推送es
            pushEs(merchantId, id, defaultId, defaultName, goodsSort);
        }
        return flag;
    }

    private void updateSpu(Long defaultId, List<GoodsSort> goodsSort) {
        Map<Long, List<GoodsSort>> shopGoodsIds = goodsSort.stream().collect(Collectors.groupingBy(GoodsSort::getShopId));
        shopGoodsIds.forEach((k, v) -> {
            if (BaseUtil.judgeList(v)) {
                Set<Long> goodsId = v.stream().map(GoodsSort::getGoodsId).collect(Collectors.toSet());
                QueryWrapper<CateringShopGoodsSpuEntity> entitySpu = new QueryWrapper<>();
                entitySpu.lambda().eq(CateringShopGoodsSpuEntity::getShopId, k).in(CateringShopGoodsSpuEntity::getGoodsId, goodsId);
                List<CateringShopGoodsSpuEntity> spuEntities = shopGoodsSpuMapper.selectList(entitySpu);
                final GoodsSortMaxDTO[] max = {shopGoodsSpuMapper.getShopGoodsSortMax(defaultId, k)};
                final Integer[] sort = {max[0].getSort()+(sort1)};
                v.forEach(i -> {
                    if (ObjectUtils.isEmpty(max[0])) {
                        max[0] = new GoodsSortMaxDTO();
                        max[0].setShopId(k);
                        max[0].setSort(0);
                    }
                    i.setSort(sort[0]);
                    sort[0]++;
                    spuEntities.stream().filter(s -> s.getGoodsId().equals(i.getGoodsId())).forEach(t -> {
                        log.info("门店商品对应的排序号为：{}",i.getSort());
                        t.setSort(i.getSort());
                    });
                });
                shopGoodsSpuService.updateBatchById(spuEntities);
            }
        });
    }

    private void pushEs(Long merchantId, Long id, Long defaultId, String defaultName, List<GoodsSort> goodsSort) {
        MerchantGoodsToEsDTO dto = new MerchantGoodsToEsDTO();
        dto.setSorts(goodsSort);
        dto.setMerchantId(merchantId.toString());
        dto.setCategoryId(id.toString());
        dto.setDefaultCategoryId(defaultId.toString());
        dto.setDefaultCategoryName(defaultName);
        // 同步ES
        merchantGoodsSenderMq.sendMerchantGoodsCategoryUpdateMsg(dto);
    }

    private CateringMerchantCategoryEntity setCategoryEntity(Long merchantId) {
        CateringMerchantCategoryEntity entity;
        Long defaultId;
        entity =new CateringMerchantCategoryEntity();
        entity.setDefaultCategory(DefaultEnum.DEFAULT.getStatus());
        entity.setCategoryName("默认分类");
        entity.setMerchantId(merchantId);
        defaultId= IdWorker.getId();
        isSave(false, entity, defaultId);
        return entity;
    }

    private void addCategory( CateringMerchantCategoryEntity entity,Long merchantId) {
        MerchantCategorySaveDTO merchantCategory = new MerchantCategorySaveDTO();

        merchantCategory.setDefaulCategory(CategoryTypeEnum.DEFAULT.getStatus());
        save(merchantCategory,Boolean.FALSE);
    }



    @Override
    public List<MerchantCategoryVO> queryByMerchantId(Long merchantId) {
        LambdaQueryWrapper<CateringMerchantCategoryEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMerchantCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringMerchantCategoryEntity::getMerchantId, merchantId);
        return ConvertUtils.sourceToTarget(this.list(queryWrapper), MerchantCategoryVO.class);
    }

    /**
     * describe: 类目名是否存在
     *
     * @param id
     * @param categoryName
     * @param merchantId
     * @author: yy
     * @date: 2020/7/10 18:03
     * @return: {boolean}
     * @version 1.2.0
     **/
    private boolean isCategoryName(Long id, String categoryName, Long merchantId) {
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getMerchantId, merchantId)
                .eq(CateringMerchantCategoryEntity::getCategoryName, categoryName)
                .eq(CateringMerchantCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringMerchantCategoryEntity categoryEntity = this.baseMapper.selectOne(queryWrapper);
        if (null == categoryEntity || categoryEntity.getId().equals(id)) {
            return false;
        }
        return true;
    }

    /**
     * describe: 获取商户的默认分类
     *
     * @param merchantId
     * @author: yy
     * @date: 2020/7/10 17:56
     * @return: {com.meiyuan.catering.merchant.goods.entity.CateringMerchantCategoryEntity}
     * @version 1.2.0
     **/
    private CateringMerchantCategoryEntity getDefaultCategoryEntity(Long merchantId) {
        QueryWrapper<CateringMerchantCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantCategoryEntity::getMerchantId, merchantId)
                .eq(CateringMerchantCategoryEntity::getDefaultCategory, CategoryTypeEnum.DEFAULT.getStatus())
                .eq(CateringMerchantCategoryEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.baseMapper.selectOne(queryWrapper);
    }


    @Override
    public List<CategoryDTO> queryCategoryByIds(Long shopId, Set<Long> categoryIds) {
        return baseMapper.queryCategoryByIds(shopId, categoryIds);
    }


    /**
     * 商品名称模糊搜索返回对应分类集合
     *
     * @author: wxf
     * @date: 2020/4/7 17:45
     * @return: {@link List< GoodsCategoryAndLabelDTO>}
     **/
    @Override
    public List<GoodsCategoryAndLabelDTO> listByGoodsName(MerchantGoodsNameQueryDTO dto) {
        //查门店下所有的商品得到分类id 通过分类id去拿信息
        List<Long> idsList = baseMapper.listCategoryId(dto);
        List<GoodsCategoryAndLabelDTO> categoryDTOS=new ArrayList<>();
        if(BaseUtil.judgeList(idsList)){
            Set<Long> idsSet = new HashSet<>(idsList);
            categoryDTOS = baseMapper.queryCategoryByShopId(Long.valueOf(dto.getMerchantId()), idsSet);
        }
        return categoryDTOS;
    }


    /**
     * 方法描述   app--商品分类 条件查询 排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/19 14:36
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    public PageData<MerchantCategoryDownVO> queryPageListForApp(MerchantCategoryQueryDTO dto) {
        IPage<MerchantCategoryDownVO> page = cateringMerchantCategoryMapper.queryPageListForApp(dto.getPage(), dto);
        return new PageData<>(page);
    }


    /**
     * 方法描述   商品分类删除
     *
     * @param shopId
     * @param id
     * @author: lhm
     * @date: 2020/8/19 15:12
     * @return: {@link }
     * @version 1.3.0
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteForApp(Long shopId, Long id) {
        this.delete(shopId,id,true);
        boolean remove = removeById(id);
        if (remove) {
            //删除关联关系
            QueryWrapper<CateringCategoryShopRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringCategoryShopRelationEntity::getCategoryId, id).eq(CateringCategoryShopRelationEntity::getShopId, shopId);
            cateringCategoryShopRelationService.remove(queryWrapper);
        }
        return remove;
    }

    @Override
    public Integer getSortMax(Long shopId) {
        List<Integer> list = getIntegers(shopId);
        return BaseUtil.judgeList(list) ? list.get(list.size() - 1)+(sort1):sort1;

    }


    /**
     * 方法描述   商户app--分类排序
     *
     * @param dto
     * @author: lhm
     * @date: 2020/8/21 15:25
     * @return: {@link }
     * @version 1.3.0
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateCategorySort(MerchantCategoryOrGoodsSortDTO dto) {
        //1.数据库修改   1.上移 2.下移
        //下移
        List<CateringCategoryShopRelationEntity> entities = baseMapper.queryList(dto);
        CateringCategoryShopRelationEntity one = cateringCategoryShopRelationService.getOne(getQuery(dto.getFirstId(), dto.getShopId()));
        Integer categorySort;
        if(dto.getUpOrDown().equals(BaseUtil.SIZE2)){
            final Integer[] sort = {dto.getFirstSort()+2};
            entities.forEach(i->{
                i.setSort(sort[0]);
                sort[0]++;
            });
            one.setSort(dto.getFirstSort()+(sort1));
            entities.add(one);
        }  else {
            final Integer[] sort = {dto.getFirstSort()+(sort1)};
            entities.stream().filter(i -> !i.getCategoryId().equals(dto.getFirstId())).forEach(i -> {
                i.setSort(sort[0]);
                sort[0]++;
            });
            one.setSort(dto.getFirstSort());
            entities.add(one);
        }
        entities.forEach(s->{
            System.out.println(s.getSort());
        });
        return cateringCategoryShopRelationService.updateBatchById(entities);

    }




    @Override
    public Integer getSortMin(Long shopId) {
        List<Integer> list = getIntegers(shopId);
        return BaseUtil.judgeList(list) ? list.get(0)-(sort1) : sort1;

    }

    @Override
    public List<MerchantCategoryDownVO> queryAllByShopId(MerchantCategoryQueryDTO dto) {
        List<MerchantCategoryDownVO> list;
        if(dto.getAccountType().equals(AccountTypeEnum.MERCHANT.getStatus())){
            list= baseMapper.queryAll(dto);
        }else {
            list= baseMapper.queryAllByShopId(dto);
        }
         return list;
    }

    @Override
    public CategoryDTO queryCategoryByIdForShop(Long shopId, Long categoryId) {
        return baseMapper.queryCategoryByIdForShop(shopId,categoryId);

    }

    private List<Integer> getIntegers(Long shopId) {
        QueryWrapper<CateringCategoryShopRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringCategoryShopRelationEntity::getSort).eq(CateringCategoryShopRelationEntity::getShopId, shopId).orderByAsc(CateringCategoryShopRelationEntity::getSort);
        List<Integer> list = cateringCategoryShopRelationService.list(queryWrapper).stream().map(CateringCategoryShopRelationEntity::getSort).collect(Collectors.toList());
        return list;
    }


    private QueryWrapper<CateringCategoryShopRelationEntity> getQuery(Long categoryId,Long shopId) {
        QueryWrapper<CateringCategoryShopRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringCategoryShopRelationEntity::getShopId, shopId).eq(CateringCategoryShopRelationEntity::getCategoryId,categoryId);
        return queryWrapper;
    }

}
