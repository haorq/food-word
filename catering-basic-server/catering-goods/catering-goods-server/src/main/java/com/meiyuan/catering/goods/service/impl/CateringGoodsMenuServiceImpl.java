package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.enums.base.WeekEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringGoodsMenuMapper;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.menu.*;
import com.meiyuan.catering.goods.entity.CateringGoodsMenuEntity;
import com.meiyuan.catering.goods.entity.CateringMerchantMenuGoodsRelationEntity;
import com.meiyuan.catering.goods.enums.DataBindTypeEnum;
import com.meiyuan.catering.goods.enums.FixedOrAllEnum;
import com.meiyuan.catering.goods.enums.GoodsStatusEnum;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringGoodsMenuService;
import com.meiyuan.catering.goods.service.CateringMerchantMenuGoodsRelationService;
import com.meiyuan.catering.goods.util.GoodsUtil;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.enums.ShopSellTypeEnum;
import com.meiyuan.catering.merchant.service.CateringMerchantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单表(CateringGoodsMenu)表服务实现类
 *
 * @author wxf
 * @since 2020-03-18 18:38:59
 */
@Service("cateringGoodsMenuService")
public class CateringGoodsMenuServiceImpl extends ServiceImpl<CateringGoodsMenuMapper, CateringGoodsMenuEntity>
        implements CateringGoodsMenuService {
    @Resource
    private CateringGoodsMenuMapper cateringGoodsMenuMapper;
    @Resource
    CateringMerchantMenuGoodsRelationService merchantMenuGoodsRelationService;
    @Resource
    CateringMerchantService merchantService;
    @Resource
    GoodsUtil goodsUtil;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Value("${goods.merchant.id}")
    private Long goodsMerchantId;


    /**
     * 新增修改
     * 对时间有相关的验证
     * 会同步ES
     * @author: wxf
     * @date: 2020/3/21 15:15
     * @param dto 新增修改数据DTO
     * @return: {@link String}
     * @version 1.0.1
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveUpdate(GoodsMenuDTO dto) {
        if (CollectionUtils.isEmpty(dto.getGoodsIdList())) {
            throw new CustomException("菜品为空");
        }
        List<Long> goodsIdList = dto.getGoodsIdList();
        CateringGoodsMenuEntity menu = BaseUtil.objToObj(dto, CateringGoodsMenuEntity.class);
        menu.setMenuStatus(GoodsStatusEnum.LOWER_SHELF.getStatus());
        String code;
        if (menu.getAutoCode()) {
            code = goodsUtil.menuCode(String.valueOf(goodsMerchantId));
        } else {
            code = menu.getMenuCode();
        }
        String returnString;
        // null 新增 反则修改
        if (null == dto.getId()) {
            validationCode(code);
            verifyMenuTime(dto, Boolean.TRUE);
            long menuId = IdWorker.getId();
            menu.setId(menuId);
            menu.setMenuCode(code);
            int insertSize = cateringGoodsMenuMapper.insert(menu);
            // 保存菜单对应菜品
            this.saveMerchantMenuGoods(goodsIdList, menuId, insertSize, "新增", true);
            returnString = BaseUtil.insertUpdateDelSetString(insertSize, "新增成功", "新增失败");
        } else {
            validationCode(code, menu.getId());
            verifyMenuTime(dto, Boolean.FALSE);
            Integer status = GoodsStatusEnum.LOWER_SHELF.getStatus();
            if (dto.getUpperShelfTime().isEqual(LocalDate.now())) {
                status = GoodsStatusEnum.UPPER_SHELF.getStatus();
            }
            menu.setMenuStatus(status);
            int updateSize = cateringGoodsMenuMapper.updateById(menu);
            // 保存菜单对应菜品
            this.saveMerchantMenuGoods(goodsIdList, menu.getId(), updateSize, "修改", false);
            returnString = BaseUtil.insertUpdateDelSetString(updateSize, "修改成功", "修改失败");
        }
        return returnString;
    }

    /**
     * 列表分页
     *
     * @param dto 分页参数
     * @author: wxf
     * @date: 2020/3/21 16:38
     * @return: {@link PageData < GoodsMenuDTO>}
     * @version 1.0.1
     **/
    @Override
    public PageData<GoodsMenuDTO> listLimit(GoodsMenuLimitQueryDTO dto) {
        QueryWrapper<CateringGoodsMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsMenuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        if (BaseUtil.judgeString(dto.getMenuName())) {
            queryWrapper.lambda().like(CateringGoodsMenuEntity::getMenuName, "%" + dto.getMenuName() + "%");
        }
        if (null != dto.getMenuStatus()) {
            queryWrapper.lambda().eq(CateringGoodsMenuEntity::getMenuStatus, dto.getMenuStatus());
        }
         if (null != dto.getUpperShelfTime()) {
             queryWrapper.lambda().ge(CateringGoodsMenuEntity::getUpperShelfTime, dto.getUpperShelfTime())
                     .le(CateringGoodsMenuEntity::getLowerShelfTime, dto.getUpperShelfTime());
         }
        queryWrapper.lambda().orderByDesc(CateringGoodsMenuEntity::getCreateTime);
        Page<CateringGoodsMenuEntity> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        cateringGoodsMenuMapper.selectPage(page, queryWrapper);
        PageData<GoodsMenuDTO> pageData = new PageData<>();
        List<CateringGoodsMenuEntity> menuList = page.getRecords();
        if (BaseUtil.judgeList(menuList)) {
            List<GoodsMenuDTO> dtoList = BaseUtil.objToObj(menuList, GoodsMenuDTO.class);
            List<Long> menuIdList = dtoList.stream().map(GoodsMenuDTO::getId).collect(Collectors.toList());
            QueryWrapper<CateringMerchantMenuGoodsRelationEntity> relationWrapper = new QueryWrapper<>();
            relationWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType, DataBindTypeEnum.MENU_PUSH.getStatus())
                    .in(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuIdList);
            List<CateringMerchantMenuGoodsRelationEntity> relationList = merchantMenuGoodsRelationService.list(relationWrapper);
            if (BaseUtil.judgeList(relationList)) {
                Map<Long, List<CateringMerchantMenuGoodsRelationEntity>> relationMap =
                        relationList.stream().collect(Collectors.groupingBy(CateringMerchantMenuGoodsRelationEntity::getMenuId));
                dtoList.forEach(
                        i -> {
                            List<CateringMerchantMenuGoodsRelationEntity> list = relationMap.getOrDefault(i.getId(), null);
                            if (BaseUtil.judgeList(list)) {
                                i.setFixedOrAll(list.get(0).getFixedOrAll());
                            } else {
                                i.setFixedOrAll(FixedOrAllEnum.NO_PUSH.getStatus());
                            }
                        }
                );
            } else {
                dtoList.forEach(i -> i.setFixedOrAll(FixedOrAllEnum.NO_PUSH.getStatus()));
            }
            pageData.setList(dtoList);
        }
        pageData.setTotal((int)page.getTotal());
        return pageData;
    }

    /**
     * 删除
     *
     * @param menuId 菜单id
     * @author: wxf
     * @date: 2020/3/21 16:48
     * @return: {@link String}
     * @version 1.0.1
     **/
    @Override
    public String del(Long menuId) {
        QueryWrapper<CateringGoodsMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsMenuEntity::getId, menuId)
                .eq(CateringGoodsMenuEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringGoodsMenuEntity entity = cateringGoodsMenuMapper.selectOne(queryWrapper);
        String returnString;
        if (null != entity) {
            entity.setDel(DelEnum.DELETE.getFlag());
            int updateSize = cateringGoodsMenuMapper.updateById(entity);
            returnString = BaseUtil.insertUpdateDelSetString(updateSize, "删除成功", "删除失败");
        } else {
            returnString = "菜单已被删除";
        }
        return returnString;
    }

    /**
     * 菜单详情
     *
     * @param menuId 菜单id
     * @author: wxf
     * @date: 2020/3/21 16:58
     * @return: {@link GoodsMenuDTO}
     **/
    @Override
    public GoodsMenuDTO menuInfoById(Long menuId) {
        CateringGoodsMenuEntity menu = cateringGoodsMenuMapper.selectById(menuId);
        if (null == menu) {
            throw new CustomException("id对应菜单不存在");
        }
        GoodsMenuDTO dto = BaseUtil.objToObj(menu, GoodsMenuDTO.class);
        dto.setGoodsInfoList(cateringGoodsMenuMapper.listGoodsInMenu(menu.getId()));
        return dto;
    }

    /**
     * 推送菜单
     * 相关推送验证
     * 同步es
     *
     * @author: wxf
     * @date: 2020/3/21 17:20
     * @param dto 推送参数
     * @return: {@link String}
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pushMerchant(PushMenuDTO dto) {
        // 1-所有商家 2-指定商家
        Integer fixedOrAll = dto.getFixedOrAll();
        validationPush(dto);
        List<Long> merchantIdList = dto.getMerchantIdList();
        Long menuId = dto.getId();
        CateringGoodsMenuEntity menu = cateringGoodsMenuMapper.selectById(menuId);
        String returnString;
        String trueString = "推送成功";
        String falseString = "推送失败";
        // 指定商家
        if (FixedOrAllEnum.FIXED.getStatus().equals(fixedOrAll)) {
            List<CateringMerchantMenuGoodsRelationEntity> saveList = merchantIdList.stream().map(
                    i -> {
                        CateringMerchantMenuGoodsRelationEntity entity = new CateringMerchantMenuGoodsRelationEntity();
                        entity.setId(IdWorker.getId());
                        entity.setMerchantId(i);
                        entity.setMenuId(menuId);
                        entity.setStatus(menu.getMenuStatus());
                        entity.setFixedOrAll(FixedOrAllEnum.FIXED.getStatus());
                        entity.setDataBindType(DataBindTypeEnum.MENU_PUSH.getStatus());
                        return entity;
                    }
            ).collect(Collectors.toList());
            boolean flag = merchantMenuGoodsRelationService.saveBatch(saveList);
            if (flag) {
                goodsPushMerchantToEs(saveList);
            }
            returnString = BaseUtil.insertUpdateDelBatchSetString(flag, trueString, falseString);
        } else {
            QueryWrapper<CateringMerchantEntity> merchantQueryWrapper = new QueryWrapper<>();
            merchantQueryWrapper.lambda().eq(CateringMerchantEntity::getDel, DelEnum.NOT_DELETE.getFlag());
            // 全部商家
            // 之前推送过的商家  数据不动
            QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuId)
                    .eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType,
                            DataBindTypeEnum.MENU_PUSH.getStatus());
            List<CateringMerchantMenuGoodsRelationEntity> pushedList = merchantMenuGoodsRelationService.list(queryWrapper);
            // 过滤推送过的商户
            if (BaseUtil.judgeList(pushedList)) {
                List<Long> merchantIds =
                        pushedList.stream().map(CateringMerchantMenuGoodsRelationEntity::getMerchantId).collect(Collectors.toList());
                merchantQueryWrapper.lambda().notIn(CateringMerchantEntity::getId, merchantIds);
                returnString = batchSaveRelation(menuId, trueString, falseString, merchantQueryWrapper, menu, pushedList);
            } else {
                returnString = batchSaveRelation(menuId, trueString, falseString, merchantQueryWrapper, menu, pushedList);
            }
        }
        return returnString;
    }

    /**
     * 商品推送商家信息 同步到ES
     *
     * @author: wxf
     * @date: 2020/3/30 10:19
     * @param saveList 推送数据
     **/
    private void goodsPushMerchantToEs(List<CateringMerchantMenuGoodsRelationEntity> saveList) {
        List<GoodsMerchantMenuGoodsDTO> dtoList = BaseUtil.objToObj(saveList, GoodsMerchantMenuGoodsDTO.class);
        goodsSenderMq.goodsMenuPush(dtoList);
    }

    /**
     * 批量保存关联关系
     *
     * @author: wxf
     * @date: 2020/3/27 16:33
     * @param menuId
     * @param trueString
     * @param falseString
     * @param merchantQueryWrapper
     * @return: {@link String}
     **/
    private String batchSaveRelation(Long menuId, String trueString, String falseString, QueryWrapper<CateringMerchantEntity> merchantQueryWrapper
                                    ,CateringGoodsMenuEntity menu, List<CateringMerchantMenuGoodsRelationEntity> pushedList) {
        List<CateringMerchantEntity> merchantList = merchantService.list(merchantQueryWrapper);
        List<CateringMerchantMenuGoodsRelationEntity> collect = merchantList.stream().map(
                i -> {
                    CateringMerchantMenuGoodsRelationEntity entity = new CateringMerchantMenuGoodsRelationEntity();
                    entity.setId(IdWorker.getId());
                    entity.setMerchantId(i.getId());
                    entity.setMenuId(menuId);
                    entity.setDataBindType(DataBindTypeEnum.MENU_PUSH.getStatus());
                    entity.setFixedOrAll(FixedOrAllEnum.FIXED.getStatus());
                    entity.setStatus(menu.getMenuStatus());
                    return entity;
                }
        ).collect(Collectors.toList());
        if (BaseUtil.judgeList(pushedList)) {
            pushedList.forEach(i -> i.setFixedOrAll(FixedOrAllEnum.FIXED.getStatus()));
            collect.addAll(pushedList);
        }
        boolean flag = merchantMenuGoodsRelationService.saveOrUpdateBatch(collect);
        if (flag) {
            goodsPushMerchantToEs(collect);
        }
        return BaseUtil.insertUpdateDelBatchSetString(flag, trueString, falseString);
    }

    /**
     * 保存菜单对应菜品
     *
     * @author: wxf
     * @date: 2020/3/21 16:20
     * @param goodsIdList 商品id集合
     * @param menuId 菜单id
     * @param addUpdateFlag ture 新增 反则修改
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveMerchantMenuGoods(List<Long> goodsIdList, long menuId, int saveOrUpdateSize, String str, boolean addUpdateFlag) {
        if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus().equals(saveOrUpdateSize)) {
            // 先删除后修改
            QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType,
                    DataBindTypeEnum.MENU_BIND_GOODS.getStatus())
                    .eq(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuId);
            merchantMenuGoodsRelationService.remove(queryWrapper);
            List<CateringMerchantMenuGoodsRelationEntity> saveList = goodsIdList.stream().map(
                    i -> {
                        CateringMerchantMenuGoodsRelationEntity entity = new CateringMerchantMenuGoodsRelationEntity();
                        entity.setId(IdWorker.getId());
                        entity.setGoodsId(i);
                        entity.setMenuId(menuId);
                        entity.setDataBindType(DataBindTypeEnum.MENU_BIND_GOODS.getStatus());
                        entity.setStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
                        return entity;
                    }
            ).collect(Collectors.toList());
            boolean flag = merchantMenuGoodsRelationService.saveBatch(saveList);
            if (flag && addUpdateFlag) {
                goodsPushMerchantToEs(saveList);
            }
        } else {
            throw new CustomException(str + "失败");
        }
    }

    @Override
    public PageData<GoodsMenuDTO> listForMerchant(GoodsMenuLimitQueryDTO dto, Long merchantId) {
        IPage<GoodsMenuDTO> ipage = cateringGoodsMenuMapper.listForMerchant(dto.getPage(),dto,merchantId);
        PageData<GoodsMenuDTO> page = new PageData<>(ipage.getRecords(),ipage.getTotal());
        return page;
    }

    @Override
    public PageData<GoodsMenuDTO> listForWechat(GoodsMenuLimitQueryDTO dto) {
        IPage<GoodsMenuDTO> ipage = cateringGoodsMenuMapper.listForWechat(dto.getPage(),dto);
        PageData<GoodsMenuDTO> page = new PageData<>(ipage.getRecords(),ipage.getTotal());
        return page;
    }

    /**
     * 验证菜单时间
     *
     * @param dto 菜单信息
     * @author: wxf
     * @date: 2020/3/25 11:55
     * @param flag 新增还是修改 ture 新增
     * @return: {@link Boolean}
     * @version 1.0.1
     **/
    private Boolean verifyMenuTime(GoodsMenuDTO dto, boolean flag) {
        // 验证送达时间
        if (null == dto.getServiceTime()) {
            throw new CustomException("送达时间为空");
        }
        // 验证上下架时间非空
        if (null == dto.getUpperShelfTime()) {
            throw new CustomException("上下架时间为空");
        }
        dto.setLowerShelfTime(dto.getUpperShelfTime());
        // 送达时间
        LocalDate serviceTime = dto.getServiceTime();
        //  上架时间
        LocalDate upperShelfTime = dto.getUpperShelfTime();
        //  下架时间
        LocalDate lowerShelfTime = dto.getLowerShelfTime();
        // 验证规则
        // 1、上下架时间必须是同一天
        // 2、送达 时间 不能 早于 等于 今天
        // 3、上架时间 不能 早于 等于 今天
        // 4、送达时间是 上下架时间的延后一天
        // 5、验证上下架时间有没有其它的菜单
        LocalDate today = LocalDate.now();
        // 上下架时间必须是同一天
        if (!upperShelfTime.isEqual(lowerShelfTime)) {
            throw new CustomException("上下架时间必须是同一天");
        }
        if (flag && !serviceTime.isAfter(today)) {
            // 送达 时间 不能 早于 等于 今天
            throw new CustomException("送达 时间 不能 早于 等于 今天");
        }
        if (flag && !upperShelfTime.isAfter(today)) {
            // 上架时间 不能 早于 等于 今天
            throw new CustomException("上架时间 不能 早于 等于 今天");
        }
        // 送达时间是 上下架时间的延后一天
        long oneDay = 1;
        // 送达时间和 下架时间 直接差多少天
        long day = serviceTime.toEpochDay() - lowerShelfTime.toEpochDay();
        if (oneDay != day) {
            throw new CustomException("送达时间是 上下架时间的延后一天");
        }
        List<CateringGoodsMenuEntity> menuList = cateringGoodsMenuMapper.timeContainMenu(upperShelfTime, lowerShelfTime);
        if (flag) {
            if (BaseUtil.judgeList(menuList)) {
                throw new CustomException(upperShelfTime + " 至 " + lowerShelfTime + "已有菜单存在");
            }
        } else {
            if (BaseUtil.judgeList(menuList)) {
                CateringGoodsMenuEntity menu = menuList.get(0);
                if (!menu.getId().equals(dto.getId())) {
                    throw new CustomException(upperShelfTime + " 至 " + lowerShelfTime + "已有菜单存在");
                }
            }
        }

        return true;
    }

    /**
     * 验证菜单编码
     *
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @param code 菜单编码
     * @return: {@link boolean}
     * @version 1.0.1
     **/
    @Override
    public boolean validationCode(String code) {
        QueryWrapper<CateringGoodsMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsMenuEntity::getMenuCode, code);
        int count = this.count(queryWrapper);
        if (count >= 1) {
            throw new CustomException("编码重复");
        }
        return true;
    }

    /**
     * 微信端获取根据菜单id
     *
     * @param menuId 菜单id
     * @author: wxf
     * @date: 2020/5/22 18:09
     * @return: {@link GoodsMenuDTO}
     * @version 1.0.1
     **/
    @Override
    public GoodsMenuDTO wxMenuGetById(Long menuId) {
        CateringGoodsMenuEntity menu = this.getById(menuId);
        return null != menu ? BaseUtil.objToObj(menu, GoodsMenuDTO.class) : null;
    }

    /**
     * 验证菜单编码
     *
     * @param code 菜单编码
     * @param id
     * @author: wxf
     * @date: 2020/3/26 18:25
     * @return: {@link boolean}
     **/
    private boolean validationCode(String code, Long id) {
        QueryWrapper<CateringGoodsMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsMenuEntity::getMenuCode, code);
        List<CateringGoodsMenuEntity> list = this.list(queryWrapper);
        if (list.size() > 1) {
            throw new CustomException("编码重复");
        }
        if (list.size() == 1) {
            Long menuId = list.get(0).getId();
            if (!id.equals(menuId)) {
                throw new CustomException("编码重复");
            }
        }
        return true;
    }

    /**
     * 推送验证
     *
     * @author: wxf
     * @date: 2020/3/27 15:29
     * @param dto 推送信息
     **/
    private void validationPush(PushMenuDTO dto) {
        // 1-所有商家 2-指定商家
        Integer fixedOrAll = dto.getFixedOrAll();
        if (null == dto.getId()) {
            throw new CustomException("推送菜单为空");
        }
        if (null == dto.getFixedOrAll()) {
            throw new CustomException("指明推送全部商家还是指定商家");
        }
        if (FixedOrAllEnum.FIXED.getStatus().equals(fixedOrAll) && !BaseUtil.judgeList(dto.getMerchantIdList())) {
            throw new CustomException("推送商户为空");
        }
        // 验证推送类型
        // 只能指定商家 更改为 全部商家
        Long menuId = dto.getId();
        QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuId)
                .eq(CateringMerchantMenuGoodsRelationEntity::getDataBindType, DataBindTypeEnum.MENU_PUSH.getStatus());
        List<CateringMerchantMenuGoodsRelationEntity> relationList = merchantMenuGoodsRelationService.list(queryWrapper);
        // 空 没有推送商家
        if (BaseUtil.judgeList(relationList) && FixedOrAllEnum.FIXED.getStatus().equals(fixedOrAll)
                && FixedOrAllEnum.ALL.getStatus().equals(relationList.get(0).getFixedOrAll())) {
            // 如果 传进来是 指定商家  之前库里是全部商家 直接提示前端
            throw new CustomException("这个菜单已经推送给全部商家了");
        }
    }

    @Override
    public List<GoodsMenuDTO> listForWxMerchantIndex(GoodsMenuLimitQueryDTO dto, Long merchantId) {
        return cateringGoodsMenuMapper.listForWxMerchantIndex(dto,merchantId);
    }

    /**
     * 菜单定时上下架
     * 同步ES
     * @author: wxf
     * @date: 2020/5/19 16:24
     * @return: {@link  List< Long>}
     * @version 1.0.1
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Long> timingUpDown() {
        // 跟新今天上架
        QueryWrapper<CateringGoodsMenuEntity> todayWrapper = new QueryWrapper<>();
        todayWrapper.lambda().eq(CateringGoodsMenuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                             .eq(CateringGoodsMenuEntity::getUpperShelfTime, LocalDate.now());
        List<CateringGoodsMenuEntity> todayList = this.list(todayWrapper);
        if (CollectionUtils.isNotEmpty(todayList)) {
            todayList.forEach(i -> i.setMenuStatus(GoodsStatusEnum.UPPER_SHELF.getStatus()));
            this.updateBatchById(todayList);
        }
        // 更新昨天下架
        QueryWrapper<CateringGoodsMenuEntity> yesterdayWrapper = new QueryWrapper<>();
        yesterdayWrapper.lambda().eq(CateringGoodsMenuEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringGoodsMenuEntity::getUpperShelfTime, LocalDate.now().plusDays(-1));
        List<CateringGoodsMenuEntity> yesterdayList = this.list(yesterdayWrapper);
        List<Long> lowerShelfMenuIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(yesterdayList)) {
            yesterdayList.forEach(i -> {
                i.setMenuStatus(GoodsStatusEnum.LOWER_SHELF.getStatus());
                lowerShelfMenuIds.add(i.getId());
            });
            this.updateBatchById(yesterdayList);
        }
        pushUpDownToEs(todayList, yesterdayList);
        return lowerShelfMenuIds;
    }

    /**
     * 微信获取菜单对应的送达时间
     * 1、菜单模式 返回对应送达时间
     * 2、菜品模式 返回明后两天的时间
     *
     * @param dto 查询参数
     * @author: wxf
     * @date: 2020/3/31 18:13
     * @return: {@link WxMenuServiceTimeDTO}
     **/
    @Override
    public WxMenuServiceTimeDTO wxMenuService(WxMenuServiceTimeQueryDTO dto) {
        if (null == dto.getSellType()) {
            throw new CustomException("售卖模式未空");
        }
        Integer sellType = dto.getSellType();
        WxMenuServiceTimeDTO menuDto = new WxMenuServiceTimeDTO();
        List<String> serviceTimeList = new ArrayList<>();
        if (ShopSellTypeEnum.GOOD_MENU.getStatus().equals(sellType)) {
            if (null == dto.getId()) {
                throw new CustomException("菜单为空");
            }
            CateringGoodsMenuEntity menu = this.getById(dto.getId());
            if (null == menu) {
                throw new CustomException("没有查询到对应菜单");
            }
            String str = BaseUtil.dayOfWeek(menu.getServiceTime());
            serviceTimeList.add(str);
        }

        //品牌属性:1:自营，2:非自营
        Integer selfSupport = 1;
        Integer selfSupportNot = 2;
        if (ShopSellTypeEnum.GOOD.getStatus().equals(sellType) && Objects.equals(dto.getMerchantAttribute(),selfSupport)) {
            LocalDate today = LocalDate.now();
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(1)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(2)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(3)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(4)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(5)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(6)));
            serviceTimeList.add(BaseUtil.dayOfWeek(today.plusDays(7)));
        }

        if (ShopSellTypeEnum.GOOD.getStatus().equals(sellType) && Objects.equals(dto.getMerchantAttribute(),selfSupportNot)) {
            LocalDate today = LocalDate.now();
            //2020-10-11 (周日)
            int value = today.getDayOfWeek().getValue();
            WeekEnum parse = WeekEnum.parse(value);
            String s =  "（" + parse.getDesc() + "）";
            serviceTimeList.add("今日" + s);

        }
        menuDto.setServiceTime(serviceTimeList);
        return menuDto;
    }



    /**
     * 上下架 推送 es
     *
     * @author: wxf
     * @date: 2020/3/31 9:51
     * @param todayList
     **/
    @Transactional(rollbackFor = Exception.class)
    public void pushUpDownToEs(List<CateringGoodsMenuEntity> todayList, List<CateringGoodsMenuEntity> yesterdayList) {
        List<CateringMerchantMenuGoodsRelationEntity> pushList = new ArrayList<>();
        QueryWrapper<CateringMerchantMenuGoodsRelationEntity> queryWrapper = new QueryWrapper<>();
        if (BaseUtil.judgeList(todayList)) {
            List<Long> menuIdList = todayList.stream().map(CateringGoodsMenuEntity::getId).collect(Collectors.toList());
            merchantMenuGoodsRelationService.updateMenuStatus(GoodsStatusEnum.UPPER_SHELF.getStatus(), menuIdList);
            queryWrapper.lambda().in(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuIdList);
            List<CateringMerchantMenuGoodsRelationEntity> list = merchantMenuGoodsRelationService.list(queryWrapper);
            if (BaseUtil.judgeList(list)) {
                list.forEach(i -> i.setStatus(GoodsStatusEnum.UPPER_SHELF.getStatus()));
                pushList.addAll(list);
            }
        }
        if (BaseUtil.judgeList(yesterdayList)) {
            List<Long> menuIdList = yesterdayList.stream().map(CateringGoodsMenuEntity::getId).collect(Collectors.toList());
            merchantMenuGoodsRelationService.updateMenuStatus(GoodsStatusEnum.LOWER_SHELF.getStatus(), menuIdList);
            queryWrapper.lambda().in(CateringMerchantMenuGoodsRelationEntity::getMenuId, menuIdList);
            List<CateringMerchantMenuGoodsRelationEntity> list = merchantMenuGoodsRelationService.list(queryWrapper);
            if (BaseUtil.judgeList(list)) {
                list.forEach(i -> i.setStatus(GoodsStatusEnum.LOWER_SHELF.getStatus()));
                pushList.addAll(list);
            }
        }
        if (BaseUtil.judgeList(pushList)) {
            boolean flag = merchantMenuGoodsRelationService.updateBatchById(pushList);
            if (flag) {
                goodsSenderMq.goodsMenuPush(BaseUtil.objToObj(pushList, GoodsMerchantMenuGoodsDTO.class));
            }
        }
    }
}