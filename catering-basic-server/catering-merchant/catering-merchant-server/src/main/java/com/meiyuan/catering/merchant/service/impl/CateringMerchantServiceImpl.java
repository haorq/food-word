package com.meiyuan.catering.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.BusinessStatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.dada.TakeOutDeliveryTypeEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.merchant.ShopUtils;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dao.CateringMerchantMapper;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.RegisterPhoneChangeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.entity.*;
import com.meiyuan.catering.merchant.enums.*;
import com.meiyuan.catering.merchant.service.*;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.utils.ShopConfigDefaultValue;
import com.meiyuan.catering.merchant.vo.merchant.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商户表(CateringMerchant)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringMerchantServiceImpl extends ServiceImpl<CateringMerchantMapper,CateringMerchantEntity>
        implements CateringMerchantService {
    @Resource
    CateringMerchantMapper cateringMerchantMapper;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    CateringShopService shopService;
    @Resource
    CateringShopTagService shopTagService;
    @Resource
    CateringShopConfigService shopConfigService;
    @Resource
    CateringShopDeliveryTimeRangeService timeRangeService;
    @Resource
    CateringMerchantPickupAdressService pickupAdressService;
    @Resource
    CateringMerchantAuditService merchantAuditService;
    @Resource
    CateringMerchantLoginAccountServiceImpl loginAccountService;
    @Resource
    NotifyService notifyService;
    @Resource
    CateringShopPrintingConfigService shopPrintingConfigService;
    @Resource
    CateringShopExtService shopExtService;

    //===================商户缓存处理相关接口===================

    @Override
    public Result refreshMerchantCatch() {
        //查询未删除商户
        QueryWrapper<CateringMerchantEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantEntity::getDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringMerchantEntity> entityList = cateringMerchantMapper.selectList(query);

        Map<String,MerchantInfoDTO> map = new HashMap<>(20);
        if (BaseUtil.judgeList(entityList)){
            entityList.forEach(entity->{
                map.put(entity.getId().toString(),BaseUtil.objToObj(entity,MerchantInfoDTO.class));
            });
        }

        merchantUtils.putMerchantList(map);
        return Result.succ(map);
    }

    @Override
    public void putAllShopCache() {
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE)
                .ne(CateringShopEntity::getShopType,ShopTypeEnum.BUSINESS_POINT.getStatus());
        List<CateringShopEntity> list = shopService.list(query);

        Map<String, ShopInfoDTO> map = new HashMap<>(50);
        list.forEach(s->{
            ShopInfoDTO shopInfoDto = ConvertUtils.sourceToTarget(s, ShopInfoDTO.class);
            CateringShopExtEntity shopExtEntity = shopExtService.getByShopId(s.getId());
            if (!ObjectUtils.isEmpty(shopExtEntity)){
                shopInfoDto.setAuditStatus(shopExtEntity.getAuditStatus());
            }else {
                shopInfoDto.setAuditStatus(1);
            }
            if (!Objects.equals(s.getDeliveryType(),TakeOutDeliveryTypeEnum.DADA.getStatus())){
                shopInfoDto.setDeliveryType(1);
            }
            if (!ObjectUtils.isEmpty(shopExtEntity)){
                shopInfoDto.setBusinessLicense(shopExtEntity.getBusinessLicense());
                shopInfoDto.setFoodBusinessLicense(shopExtEntity.getFoodBusinessLicense());
            }
            map.put(s.getId().toString(),shopInfoDto);
        });
        merchantUtils.putAllShopInfo(map);

    }


    @Override
    public void putAllShopConfigCache() {
        QueryWrapper<CateringShopConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopConfigEntity::getDel, DelEnum.NOT_DELETE);
        //获取所有店铺配置信息
        List<CateringShopConfigEntity> list = shopConfigService.list(query);

        Map<String, ShopConfigInfoDTO> map = new HashMap<>(50);
        list.forEach(s->{
            map.put(s.getShopId().toString(),ConvertUtils.sourceToTarget(s,ShopConfigInfoDTO.class));
        });

        //获取商户id
        List<CateringShopEntity> shopEntities = shopService.list();

        //店铺信息存入redis参数
        Map<String, ShopConfigInfoDTO> mapResult = new HashMap<>(50);

        shopEntities.forEach(shopEntity->{
            ShopConfigInfoDTO shopConfigInfoDTO = map.get(shopEntity.getId().toString());
            //组装店铺 自提/配送 时间范围
            shopConfigInfoDTO = this.setShopTimeRangeInfo(shopConfigInfoDTO);
            if (shopConfigInfoDTO != null){
                mapResult.put(shopEntity.getId().toString(),shopConfigInfoDTO);
            }
        });

        merchantUtils.putAllShopConfigInfo(mapResult);
    }
    //===================商户缓存处理相关接口===================


    @Override
    public IPage<CateringMerchantPageVo> listLimit(MerchantLimitQueryDTO dto) {
        //结束时间加一天
        if (!ObjectUtils.isEmpty(dto.getEndTime())){
            dto.setEndTime(dto.getEndTime().plusDays(1));
        }
        return cateringMerchantMapper.listCateringMerchantPage(dto.getPage(),dto);
    }

    @Override
    public Result<MerchantShopDetailVo> getMerchantShopDetail(ShopQueryDTO dto ) {
        MerchantShopDetailVo merchantShopDetailVo = cateringMerchantMapper.getMerchantShopDetail(dto);
        //若店铺信息查询失败跳转登录页
        if (ObjectUtils.isEmpty(merchantShopDetailVo)){
            throw new AdminUnauthorizedException();
        }

        if (!ObjectUtils.isEmpty(merchantShopDetailVo.getAddressPicture())){
            merchantShopDetailVo.setAddressPictureList(JSONArray.parseArray(merchantShopDetailVo.getAddressPicture(), String.class));
        }
        return Result.succ(merchantShopDetailVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> insertMerchantShop(MerchantShopAddDTO dto) {
        //添加店铺
        if (!Objects.equals(ShopTypeEnum.BUSINESS_POINT.getStatus(),dto.getShopType())){
            return this.insertShop(dto);
        }else {
            //添加自提点
            MerchantsPickupAddressDTO addressDTO = ConvertUtils.sourceToTarget(dto, MerchantsPickupAddressDTO.class);
            Result<CateringShopEntity> data = shopService.addPickupAddress(addressDTO);
            //添加门店营业资质信息
            this.insertShopExtEntity(dto,data.getData().getId());
            return Result.succ();
        }
    }

    /**
     * 方法描述 : 添加店铺
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 17:30
     * @param dto
     * @return: java.lang.Long
     * @Since version-1.0.0
     */
    private Result<Long>  insertShop(MerchantShopAddDTO dto){
        Integer deliveryType = dto.getDeliveryType();
        //商户app登录密码
        String code = PasswordUtil.getPassword();

        //添加店铺信息，以及登陆信息
        CateringShopEntity shopEntity = insertShopEntity(dto,code);
        Result<Long> succ = Result.succ(shopEntity.getId());
        //若开启达达配送
        if (Objects.equals(dto.getDeliveryType(),TakeOutDeliveryTypeEnum.DADA.getStatus())){
            try {
                shopService.batchSyncShopInfoToDaDa(Arrays.asList(shopEntity.getId()));
            }catch (Exception e){
                this.handleDadaErr(shopEntity,e,succ);
            }
        }
        //添加门店营业资质信息
        this.insertShopExtEntity(dto,shopEntity.getId());

        //添加店铺默认自提地址
        insertPickupAddressEntity(shopEntity);

        //添加店铺默认配置信息
        CateringShopConfigEntity shopConfigEntity = insertShopDefaultConfig(shopEntity.getId());

        //添加店铺默认配送时间段
        CateringShopDeliveryTimeRangeEntity deliveryTimeRangeEntity = setShopTimeRange(shopEntity.getId(),ShopDeliveryTypeEnum.DELIVERY.getStatus());

        //添加店铺默认自提时间范围
        CateringShopDeliveryTimeRangeEntity pickupTimeRangeEntity = setShopTimeRange(shopEntity.getId(),ShopDeliveryTypeEnum.PICKUP.getStatus());

        //组装存入redis的商户配置
        ShopConfigInfoDTO shopConfigRedis = ConvertUtils.sourceToTarget(shopConfigEntity, ShopConfigInfoDTO.class);

        shopConfigRedis.setDeliveryTimeRanges(Collections.singletonList(ConvertUtils.sourceToTarget(deliveryTimeRangeEntity, TimeRangeDTO.class)));
        shopConfigRedis.setPickupTimeRanges(Collections.singletonList(ConvertUtils.sourceToTarget(pickupTimeRangeEntity, TimeRangeDTO.class)));

        //店铺信息存入redis
        ShopInfoDTO shopInfoDTO = ConvertUtils.sourceToTarget(shopEntity, ShopInfoDTO.class);
        shopInfoDTO.setAuditStatus(1);
        merchantUtils.putShop(shopEntity.getId(),shopInfoDTO);
        merchantUtils.putShopConfigInfo(shopEntity.getId(),shopConfigRedis);

        //账号创建成功通知商户
        if (!DefaultPwdMsg.USE_DEFAULT_PWD){
            notifyService.notifySmsTemplate(dto.getRegisterPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getRegisterPhone(), code});
        }
        return succ;
    }


    /**
     * 方法描述 : 添加店铺扩展信息
     * @Author: MeiTao
     * @Date: 2020/10/9 0009 9:47
     * @param dto 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    private void insertShopExtEntity(MerchantShopAddDTO dto,Long shopId) {
        CateringShopExtEntity entity = BaseUtil.objToObj(dto, CateringShopExtEntity.class);
        entity.setShopId(shopId);
        //通联相关信息初始化
        entity.setSignStatus(2);
        entity.setAuditStatus(1);
        entity.setBankCardPro(0L);
        shopExtService.save(entity);
    }

    /**
     * 方法描述 : 添加店铺信息
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 17:16
     * @param dto
     * @param code 登陆密码
     * @return: com.meiyuan.catering.merchant.entity.CateringShopEntity
     * @Since version-1.0.0
     */
    private CateringShopEntity insertShopEntity(MerchantShopAddDTO dto,String code){
        //店铺编码获取
        String shopCode = shopService.getShopCode();
        CateringShopEntity shopEntity = BaseUtil.objToObj(dto,CateringShopEntity.class);
        long shopId = IdWorker.getId();
        shopEntity.setId(shopId);

        shopEntity.setShopCode(shopCode);
        shopEntity.setIsPickupPoint(ShopTypeEnum.BUSINESS_SELF.getStatus().equals(dto.getShopType()) ? Boolean.FALSE:Boolean.TRUE);

        String encodedPassword = Md5Util.passwordEncrypt(code);
        shopEntity.setPassword(encodedPassword);
        if(shopEntity.getShopSource() == null || shopEntity.getShopSource() == 0){
            shopEntity.setShopSource(SourceEnum.PLATFORM.getStatus());
        }
        shopEntity.setSellType(ShopSellTypeEnum.GOOD.getStatus());
        shopEntity.setDel(DelEnum.NOT_DELETE.getFlag());
        shopEntity.setBusinessStatus(BusinessStatusEnum.OPEN.getStatus());
        shopService.save(shopEntity);
        //同步店铺地址信息
        shopService.handlerBaiduLocation(shopEntity,dto.getMapCoordinate());
        this.saveShopLoginAccount(shopEntity);
        return shopEntity;
    }

    /**
     * 方法描述 : 添加店铺,自提点，店铺兼自提点登陆账号信息
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 13:40
     * @param shopEntity 请求参数
     * @return: void
     * @Since version-1.1.0
     */
    private void saveShopLoginAccount(CateringShopEntity shopEntity) {
        //添加店铺登录账户信息
        CateringMerchantLoginAccountEntity accountEntity = new CateringMerchantLoginAccountEntity();
        accountEntity.setPhone(shopEntity.getRegisterPhone());
        accountEntity.setPassword(shopEntity.getPassword());
        Integer accountType = AccountTypeEnum.PICKUP.getStatus();
        if (!Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())) {
            accountType = Objects.equals(ShopTypeEnum.BUSINESS_SELF.getStatus(),shopEntity.getShopType())? AccountTypeEnum.SHOP.getStatus(): AccountTypeEnum.SHOP_PICKUP.getStatus();
        }
        accountEntity.setAccountType(accountType);
        accountEntity.setAccountTypeId(shopEntity.getId());
        accountEntity.setAccountStatus(shopEntity.getShopStatus());
        accountEntity.setIsDel(Boolean.FALSE);
        loginAccountService.save(accountEntity);
    }

    /**
     * 方法描述 : 添加店铺默认自提地址
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 17:19
     * @param shopEntity
     * @return: com.meiyuan.catering.merchant.entity.CateringMerchantPickupAdressEntity
     * @Since version-1.0.0
     */
    private CateringMerchantPickupAdressEntity insertPickupAddressEntity(CateringShopEntity shopEntity){
        CateringMerchantPickupAdressEntity pickupAddressEntity = ConvertUtils.sourceToTarget(shopEntity, CateringMerchantPickupAdressEntity.class);
        pickupAddressEntity.setId(IdWorker.getId());
        pickupAddressEntity.setChargerName(shopEntity.getPrimaryPersonName());
        pickupAddressEntity.setChargerPhone(shopEntity.getShopPhone());
        pickupAddressEntity.setPickupId(shopEntity.getId());
        pickupAddressEntity.setShopId(shopEntity.getId());
        pickupAddressEntity.setName(shopEntity.getShopName());
        pickupAddressEntity.setShopStatus(shopEntity.getShopStatus());
        pickupAddressEntity.setIsDel(DelEnum.NOT_DELETE.getFlag());
        pickupAdressService.save(pickupAddressEntity);
        return pickupAddressEntity;
    }

    /**
     * 组装店铺自提配送时间范围
     * @param shopConfigInfoDTO
     * @return
     */
    private ShopConfigInfoDTO setShopTimeRangeInfo(ShopConfigInfoDTO shopConfigInfoDTO) {
        return timeRangeService.setShopTimeRangeInfo(shopConfigInfoDTO);
    }

    @Override
    public List<MerchantInfoVo> listMerchantInfo(MerchantQueryConditionDTO queryCondition) {
        return cateringMerchantMapper.listMerchantInfo(queryCondition);
    }

    @Override
    public void updateShopCache(CateringShopEntity shopEntity) {
        if (!Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            //更新店铺相应缓存
            ShopInfoDTO shopInfoDTO = ConvertUtils.sourceToTarget(shopEntity, ShopInfoDTO.class);
            merchantUtils.putShop(shopEntity.getId(),shopInfoDTO);
        }
    }

    /**
     * 全部商户
     *
     * @author: wxf
     * @date: 2020/4/22 13:54
     * @param flag 是否包含自提点 true 包含 false 不包含
     * @return: {@link List< ShopDTO>}
     **/
    @Override
    public List<ShopDTO> allMerchant(boolean flag) {
        QueryWrapper<CateringShopEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        if (!flag) {
            queryWrapper.lambda().ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus());
        }
        List<ShopDTO> dtoList = Collections.emptyList();
        List<CateringShopEntity> list = shopService.list(queryWrapper);
        if (BaseUtil.judgeList(list)) {
            dtoList = BaseUtil.objToObj(list, ShopDTO.class);
        }
        return dtoList;
    }

    @Override
    public List<ShopDTO> findAll() {
        List<ShopDTO> dtoList = Collections.emptyList();
        List<CateringShopEntity> all = baseMapper.findAll();
        if (BaseUtil.judgeList(all)) {
            dtoList = BaseUtil.objToObj(all, ShopDTO.class);
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopResponseDTO> modifyMerchantBusinessStatus(Long shopId, Boolean type) {
        CateringShopEntity shop = shopService.getById(shopId);

        //判断是查询还是修改
        if (type){
            shop = shopService.modifyShopBusinessStatus(shop);
        }

        ShopResponseDTO shopResponseDTO = ConvertUtils.sourceToTarget(shop, ShopResponseDTO.class);
        shopResponseDTO.setBusinessStatus(ShopUtils.getBusinessStatus(shop.getBusinessStatus(),shop.getOpeningTime(),shop.getClosingTime()));
        return Result.succ(shopResponseDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopDTO> modifyMerchantBaseInfo(Long shopId , MerchantModifyInfoRequestDTO dto) {
        CateringShopEntity shop = shopService.getById(shopId);

        if (!Objects.equals(ShopTypeEnum.BUSINESS_POINT.getStatus(),shop.getShopType())){
            //更新店铺
            return this.updateShop(shop,dto);
        }else {
            //修改自提点
            this.updatePickup(shop,dto);
            //4、修改店铺扩展信息
            CateringShopExtEntity shopExtEntity = shopExtService.getByShopId(shopId);
            BeanUtils.copyProperties(dto,shopExtEntity);
            shopExtService.saveOrUpdate(shopExtEntity);
            return Result.succ(ConvertUtils.sourceToTarget(shop,ShopDTO.class));
        }

    }

    private void updatePickup(CateringShopEntity shop, MerchantModifyInfoRequestDTO dto) {
        //更新基本信息
        this.handleShopBaseInfo(shop,dto);

        //是否同步店铺自提点关联关系信息相关标识
        Boolean isShopPickupRelation = Boolean.FALSE;
        //是否同步 pc端、app端 登陆账号相关信息
        Boolean isPcAccount = Boolean.FALSE;
        if (!StringUtils.isEmpty(dto.getShopName())&& !Objects.equals(dto.getShopName(),shop.getShopName())){
            shop.setShopName(dto.getShopName());
            isShopPickupRelation = Boolean.TRUE;
        }

        //店铺状态:1：启用，2：禁用
        if (!ObjectUtils.isEmpty(dto.getShopStatus())&& !Objects.equals(dto.getShopStatus(),shop.getShopStatus())){
            shop.setShopStatus(dto.getShopStatus());
            isPcAccount = Boolean.TRUE;
            isShopPickupRelation = Boolean.TRUE;
        }

        //1、若修改手机号则 重新生成初始密码，并发送短信通知【若设置默认密码则无需发送短信】
        boolean isNotify = !Objects.equals(dto.getRegisterPhone(), shop.getRegisterPhone());
        String code = PasswordUtil.getPassword();
        if (BaseUtil.judgeString(dto.getRegisterPhone())&&isNotify) {
            shop.setPassword(Md5Util.passwordEncrypt(code));
            shop.setRegisterPhone(dto.getRegisterPhone());
            shop.setShopPhone(dto.getRegisterPhone());
            isShopPickupRelation = Boolean.TRUE;
            isPcAccount = Boolean.TRUE;
        }
        if (isPcAccount){
            //更新登陆账号信息
            this.syncLoginAccount(shop.getId(),shop.getShopStatus());
        }

        //门店位置修改，更新店铺信息
        if (!StringUtils.isEmpty(dto.getMapCoordinate()) && !Objects.equals(dto.getMapCoordinate(),shop.getMapCoordinate())){
            shop.setAddressDetail(dto.getAddressDetail());
            shop.setMapCoordinate(dto.getMapCoordinate());
            shopService.handlerBaiduLocation(shop,dto.getMapCoordinate());
            isShopPickupRelation = Boolean.TRUE;
        }else {
            //修改自提点信息
            shopService.updateById(shop);
        }

        //2、修改门店与自提点绑定关系信息(若修改了手机号或者店铺地址)
        if(isShopPickupRelation){
            //修改对应的绑定关系信息
            this.updateShopPickupRelation(shop);
        }

        //若修改手机号则发送短信通知对应手机
        if (BaseUtil.judgeString(dto.getRegisterPhone())&&isNotify) {
            //清除商户app端店铺登录token
            String token = Md5Util.md5(shop.getId().toString() + shop.getRegisterPhone());

            merchantUtils.removeMerAppToken(token);
            if((!DefaultPwdMsg.USE_DEFAULT_PWD)){
                notifyService.notifySmsTemplate(dto.getRegisterPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getRegisterPhone(), code});
            }
        }
    }

    /**
     * 方法描述 : pc-app : 修改店铺信息
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 14:42
     * @param shop
     * @param dto 请求参数
     * @return: Result<com.meiyuan.catering.merchant.dto.merchant.ShopDTO>
     * @Since version-1.3.0
     */
    private Result<ShopDTO> updateShop(CateringShopEntity shop, MerchantModifyInfoRequestDTO dto) {
        this.handleShopBaseInfo(shop,dto);
        //是否同步店铺自提点关联关系信息相关标识
        Boolean isShopPickupRelation = Boolean.FALSE;
        //是否发送消息同步es数据相关标识（所有索引都可能需要更新）
        Boolean isSendMsg = Boolean.FALSE;
        //是否同步 pc端、app端 登陆账号相关信息
        Boolean isPcAccount = Boolean.FALSE;

        //店铺状态:1：启用，2：禁用 ，同步 商户es信息
        if (!ObjectUtils.isEmpty(dto.getShopStatus())&& !Objects.equals(dto.getShopStatus(),shop.getShopStatus())){
            shop.setShopStatus(dto.getShopStatus());
            isPcAccount = Boolean.TRUE;
            isShopPickupRelation = Boolean.TRUE;
            isSendMsg = Boolean.FALSE;
        }
        if (!StringUtils.isEmpty(dto.getShopName())&& !Objects.equals(dto.getShopName(),shop.getShopName())){
            shop.setShopName(dto.getShopName());
            isSendMsg = Boolean.TRUE;
            isShopPickupRelation = Boolean.TRUE;
        }
        if (!ObjectUtils.isEmpty(dto.getPrimaryPersonName())&& !Objects.equals(dto.getPrimaryPersonName(),shop.getPrimaryPersonName())){
            shop.setPrimaryPersonName(dto.getPrimaryPersonName());
            isShopPickupRelation = Boolean.TRUE;
        }
        if (isPcAccount){
            //更新登陆账号信息
            this.syncLoginAccount(shop.getId(),shop.getShopStatus());
        }
        shop.setUpdateTime(LocalDateTime.now());
        //门店位置修改，更新店铺信息
        Boolean updateDaDa = Boolean.FALSE;
        if (!StringUtils.isEmpty(dto.getMapCoordinate()) && !Objects.equals(dto.getMapCoordinate(),shop.getMapCoordinate())){
            shop.setAddressDetail(dto.getAddressDetail());
            shop.setMapCoordinate(dto.getMapCoordinate());
            shopService.handlerBaiduLocation(shop,dto.getMapCoordinate());
            isShopPickupRelation = Boolean.TRUE;
            isSendMsg = Boolean.TRUE;
            updateDaDa = Boolean.TRUE;
        }else {
            //修改门店信息
            shopService.updateById(shop);
        }

        //2、修改门店与自提点绑定关系信息(若修改了手机号或者店铺地址)
        if(isShopPickupRelation){
            //修改对应的绑定关系信息
            this.updateShopPickupRelation(shop);
        }

        //3、修改门店缓存
        updateShopCache(shop);
        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shop,shopDTO);

        List<String> list = JSON.parseArray(shop.getAddressPicture(), String.class);
        shopDTO.setAddressPictureList(list);
        shopDTO.setAddressPictureDTOS(shopService.handelAddressPicture(list));

        //4、修改店铺扩展信息
        CateringShopExtEntity shopExtEntity = shopExtService.getByShopId(shop.getId());
        this.handleShopExtEntity(dto,shopExtEntity);

        //5、发送消息更新es相关数据
        if (isSendMsg){
            shopService.updateMerchantInfoSendMq(shopDTO);
            //只更新店铺信息
        }

        //6、修改达达配送端店铺信息达达配送
        Result<ShopDTO> succ = Result.succ(shopDTO);
        if (ObjectUtils.isEmpty(dto.getDeliveryType())){
            dto.setDeliveryType(shop.getDeliveryType());
        }
        if ( TakeOutDeliveryTypeEnum.DADA.getStatus().equals(dto.getDeliveryType())){
            try {
                shopService.batchSyncShopInfoToDaDa(Arrays.asList(shop.getId()));
            }catch (Exception e){
                this.handleDadaErr(shop,e,succ);
            }
        }
        return succ;
    }

    private void handleDadaErr(CateringShopEntity shop, Exception e,Result succ) {
        log.error("店铺添加达达配送失败，异常信息："  + e);
        log.error("店铺添加达达配送失败，店铺信息："  + shop.toString());
        shop.setDeliveryType(1);
        shopService.updateById(shop);
        succ.setMsg( e.getMessage());
        succ.setCode(1001);
    }

    private void handleShopExtEntity(MerchantModifyInfoRequestDTO dto, CateringShopExtEntity shopExtEntity) {
        if (!ObjectUtils.isEmpty(shopExtEntity)){
            BeanUtils.copyProperties(dto,shopExtEntity);
            shopExtService.saveOrUpdate(shopExtEntity);
        }
    }

    /**
     * 方法描述 :  店铺基本信息修改设置
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 11:49
     * @param shop
     * @param dto 请求参数
     * @Since version-1.3.0
     */
    private void handleShopBaseInfo(CateringShopEntity shop, MerchantModifyInfoRequestDTO dto) {
        //1、门店基本信息修改，若为空表示删除门店公告(app删除门店公告则传入空字符串)
        if (dto.getShopNotice() != null){
            shop.setShopNotice(dto.getShopNotice());
        }
        if (dto.getAddressPictureList() != null){
            shop.setAddressPicture(JSONObject.toJSONString(dto.getAddressPictureList()));
        }
        if (!StringUtils.isEmpty(dto.getShopLogo())){
            shop.setDoorHeadPicture(dto.getShopLogo());
        }
        if(!StringUtils.isEmpty(dto.getShopPicture())){
            shop.setShopPicture(dto.getShopPicture());
        }
        if (!ObjectUtils.isEmpty(dto.getChangeGoodPrice())){
            shop.setChangeGoodPrice(shop.getChangeGoodPrice());
        }
        if(BaseUtil.judgeString(dto.getClosingTime())&&BaseUtil.judgeString(dto.getOpeningTime())){
            shop.setClosingTime(dto.getClosingTime());
            shop.setOpeningTime(dto.getOpeningTime());
        }

        //门店门牌号修改
        if (dto.getDoorNumber() != null){
            shop.setDoorNumber(dto.getDoorNumber());
            shop.setAddressFull(shop.getAddressDetail() + dto.getDoorNumber());
        }
        if (!ObjectUtils.isEmpty(dto.getChangeGoodPrice())){
            shop.setChangeGoodPrice(dto.getChangeGoodPrice());
        }

        if (!ObjectUtils.isEmpty(dto.getDeliveryType())){
            shop.setDeliveryType(dto.getDeliveryType());
        }

        if (!StringUtils.isEmpty(dto.getShopPhone())&& !Objects.equals(dto.getShopPhone(),shop.getShopPhone())){
            shop.setShopPhone(dto.getShopPhone());
        }
        if (!StringUtils.isEmpty(dto.getIdCardBack())){
            shop.setIdCardBack(dto.getIdCardBack());
        }
        if (!StringUtils.isEmpty(dto.getIdCardPositive())){
            shop.setIdCardPositive(dto.getIdCardPositive());
        }
        if (!StringUtils.isEmpty(dto.getIdCardBack())){
            shop.setIdCardBack(dto.getIdCardBack());
        }
        if (!StringUtils.isEmpty(dto.getBusinessLicense())){
            shop.setBusinessLicense(dto.getBusinessLicense());
        }
        if (!StringUtils.isEmpty(dto.getFoodBusinessLicense())){
            shop.setFoodBusinessLicense(dto.getFoodBusinessLicense());
        }
    }

    /**
     * 方法描述 : 更新pc端登录账号状态
     *            1、pc端修改店铺、自提点
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 14:53
     * @param accountTypeId 登录账号对应id
     * @param status 登录账号对应账号状态
     * @Since version-1.3.0
     */
    private void syncLoginAccount(Long accountTypeId,Integer status){
        //更新登陆账号信息
        UpdateWrapper<CateringMerchantLoginAccountEntity> updateQuery = new UpdateWrapper<>();
        updateQuery.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId,accountTypeId);
        updateQuery.lambda().set(CateringMerchantLoginAccountEntity::getAccountStatus,status);
        loginAccountService.update(updateQuery);
    }

    /**
     * 方法描述 : 店铺、自提点修改后修改对应绑定关系
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 16:19
     * @param shopEntity 请求参数
     * @Since version-1.1.0
     */
    private void updateShopPickupRelation(CateringShopEntity shopEntity){
        QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId,shopEntity.getId());
        List<CateringMerchantPickupAdressEntity> pickupAddressEntities = pickupAdressService.list(query);
        if (BaseUtil.judgeList(pickupAddressEntities)){
            pickupAddressEntities.forEach(pickupAddressEntity->{
                Long pickupAddressEntityId = pickupAddressEntity.getId();
                pickupAddressEntity.setChargerPhone(shopEntity.getRegisterPhone());
                pickupAddressEntity.setName(shopEntity.getShopName());
                pickupAddressEntity.setChargerName(shopEntity.getPrimaryPersonName());
                BeanUtils.copyProperties(shopEntity,pickupAddressEntity);
                pickupAddressEntity.setId(pickupAddressEntityId);
                pickupAddressEntity.setIsDel(shopEntity.getDel());
            });
            pickupAdressService.updateBatchById(pickupAddressEntities);
        }
    }

    /**
     * 添加店铺默认配置信息
     * @param shopId
     */
    private CateringShopConfigEntity insertShopDefaultConfig(Long shopId){
        CateringShopConfigEntity shopConfig = new CateringShopConfigEntity();
        shopConfig.setShopId(shopId);
        shopConfig.setDeliveryObject(ShopDeliveryObjectEnum.ALL.getStatus());
        shopConfig.setDeliveryRule(ShopDeliveryRuleEnum.FIXATION.getStatus());
        shopConfig.setDeliveryRange(ShopConfigDefaultValue.DELIVERY_RANGE);
        shopConfig.setDeliveryPrice(ShopConfigDefaultValue.DELIVERY_PRICE);
        shopConfig.setBusinessSupport(BusinessSupportEnum.ALL.getStatus());
        shopConfig.setAutoReceipt(ShopAutoReceiptEnum.AUTO_RECEIPT.getStatus());
        //设置店铺默认初始配置信息
        shopConfigService.save(shopConfig);
        return shopConfig;
    }

    /**
     * 添加店铺 配送/自提 时间范围
     * @param shopId
     * @param type 配送：1 自提：2
     */
    private CateringShopDeliveryTimeRangeEntity setShopTimeRange(Long shopId,Integer type){
        CateringShopDeliveryTimeRangeEntity timeRange = new CateringShopDeliveryTimeRangeEntity();
        timeRange.setType(type);
        timeRange.setShopId(shopId);
        //默认时间：08:00 - 19:59
        timeRange.setIsDel(DelEnum.NOT_DELETE.getFlag());
        timeRange.setStartTime(ShopConfigDefaultValue.START_TIME);
        timeRange.setEndTime(ShopConfigDefaultValue.END_TIME);
        timeRangeService.save(timeRange);
        return timeRange;
    }

    @Override
    public Result<IPage<MerchantPageVo>> merchantPage(MerchantQueryPage dto) {
        //结束时间加一天
        if (!ObjectUtils.isEmpty(dto.getEndTime())){
            dto.setEndTime(dto.getEndTime().plusDays(1));
        }
        return Result.succ(cateringMerchantMapper.merchantPage(dto.getPage(),dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> saveOrUpdateMerchant(MerchantDTO dto) {
        if (ObjectUtils.isEmpty(dto.getId())){
            Long merchantId = this.insertMerchant(dto);
            return Result.succ(merchantId);
        }
        //修改商户信息
        this.updateMerchant(dto);
        return Result.succ();
    }

    /**
     * 方法描述 : 修改商户信息
     * @Author: MeiTao
     */
    private void updateMerchant(MerchantDTO dto) {
        CateringMerchantEntity merchantEntity = cateringMerchantMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(merchantEntity)){
            throw new CustomException("当前商户不存在");
        }

        merchantEntity.setMerchantName(dto.getMerchantName());
        merchantEntity.setLegalPersonName(dto.getLegalPersonName());
        Boolean sendMsg = Boolean.FALSE;
        if (!Objects.equals(dto.getPhone(),merchantEntity.getPhone())){
            merchantEntity.setPhone(dto.getPhone());
            sendMsg = Boolean.TRUE;
        }

        //若平台修改资质信息默认审核通过，添加审核记录
        Boolean bBusinessLicense = Objects.equals(dto.getBusinessLicense(), merchantEntity.getBusinessLicense());
        Boolean bHygieneLicense = Objects.equals(dto.getHygieneLicense(), merchantEntity.getHygieneLicense());
        if (bBusinessLicense || bHygieneLicense){
            merchantEntity.setHygieneLicense(dto.getHygieneLicense());
            merchantEntity.setBusinessLicense(dto.getBusinessLicense());
            merchantEntity.setAuditStatus(AuditStatusEnum.PASS.getStatus());
            CateringMerchantAuditEntity merchantAuditEntity = BaseUtil.objToObj(merchantEntity, CateringMerchantAuditEntity.class);
            merchantAuditEntity.setMerchantId(merchantEntity.getId());
            merchantAuditEntity.setId(null);
            merchantAuditService.save(merchantAuditEntity);
        }
        cateringMerchantMapper.updateById(merchantEntity);

        //更新商户标签信息
        List<MerchantShopTagsVo> merchantTagsVoList = new ArrayList<>();
        List<Long> tagIds = new ArrayList<>();
        if (BaseUtil.judgeList(dto.getMerchantTagIds())){
            //根据标签id去重
            tagIds = dto.getMerchantTagIds().stream().distinct().collect(Collectors.toList());
            //通过传入标签id查询标签名字
            List<CateringShopTagEntity> shopTagEntities = new ArrayList<>(shopTagService.listByIds(tagIds));
            merchantTagsVoList = BaseUtil.objToObj(shopTagEntities, MerchantShopTagsVo.class);
        }
        shopService.updateShopTagV3(merchantEntity.getId(), tagIds);

        //更商户新缓存信息
        merchantUtils.putMerchant(BaseUtil.objToObj(merchantEntity,MerchantInfoDTO.class));
        //品牌标签缓存更新
        List<ShopTagInfoDTO> cacheList = new ArrayList<>();
        if (BaseUtil.judgeList(merchantTagsVoList)) {
            cacheList = ConvertUtils.sourceToTarget(merchantTagsVoList, ShopTagInfoDTO.class);
        }
        merchantUtils.putShopTag(merchantEntity.getId(), cacheList);

        if (sendMsg){
            CateringMerchantLoginAccountEntity loginAccount = loginAccountService.getLoginAccount(merchantEntity.getId());
            notifyService.notifySmsTemplate(merchantEntity.getPhone(), NotifyType.UPDATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{loginAccount.getPhone(), "不变"});
            merchantUtils.removeMerchantPcToken(String.valueOf(merchantEntity.getId()));
        }

    }

    /**
     * 方法描述 : 添加商户/品牌
     * @Author: MeiTao
     * @Date: 2020/7/3 0003 16:52
     * @param dto 请求参数
     * @Since version-1.1.0
     */
    private Long insertMerchant(MerchantDTO dto) {
        CateringMerchantEntity merchantEntity = BaseUtil.objToObj(dto, CateringMerchantEntity.class);
        Long merchantId = IdWorker.getId();
        merchantEntity.setId(merchantId);

        String merchantCode = this.getMerchantCode();
        merchantEntity.setMerchantCode(merchantCode);
        merchantEntity.setMerchantStatus(StatusEnum.ENABLE.getStatus());

        merchantEntity.setAuditStatus(AuditStatusEnum.WAIT_UPLOAD.getStatus());

        //若平台上传资质信息默认审核通过，添加审核记录,修改审核状态
        if (BaseUtil.judgeString(dto.getBusinessLicense()) && BaseUtil.judgeString(dto.getHygieneLicense())){
            merchantEntity.setAuditStatus(AuditStatusEnum.PASS.getStatus());
            CateringMerchantAuditEntity merchantAuditEntity = BaseUtil.objToObj(merchantEntity, CateringMerchantAuditEntity.class);
            merchantAuditEntity.setId(null);
            merchantAuditEntity.setMerchantId(merchantEntity.getId());
            merchantAuditEntity.setCreateTime(LocalDateTime.now());
            merchantAuditService.save(merchantAuditEntity);
        }

        //添加商户pc端 登录信息
        CateringMerchantLoginAccountEntity accountEntity = new CateringMerchantLoginAccountEntity();
        String merchantLoginAccount = loginAccountService.getMerchantLoginAccount();
        accountEntity.setPhone(merchantLoginAccount);
        String code = PasswordUtil.getPassword();
        accountEntity.setPassword(Md5Util.passwordEncrypt(code));
        accountEntity.setAccountType(AccountTypeEnum.MERCHANT.getStatus());
        accountEntity.setAccountTypeId(merchantId);
        accountEntity.setAccountStatus(merchantEntity.getMerchantStatus());
        accountEntity.setIsDel(DelEnum.NOT_DELETE.getFlag());
        loginAccountService.save(accountEntity);

        this.saveOrUpdate(merchantEntity);

        List<ShopTagInfoDTO> cacheList = new ArrayList<>();
        //品牌标签处理
        if (BaseUtil.judgeList(dto.getMerchantTagIds())){
            //根据标签id去重
            List<Long> tagIds = dto.getMerchantTagIds().stream().distinct().collect(Collectors.toList());
            //通过传入标签id查询标签名字
            List<CateringShopTagEntity> shopTagEntities = new ArrayList<>(shopTagService.listByIds(tagIds));
            cacheList = BaseUtil.objToObj(shopTagEntities,ShopTagInfoDTO.class);
            shopTagService.insertShopTagRelation(merchantEntity.getId(),dto.getMerchantTagIds());
        }

        //商户信息入缓存，商户标签信息入缓存
        merchantUtils.putMerchant(BaseUtil.objToObj(merchantEntity, MerchantInfoDTO.class));
        merchantUtils.putShopTag(merchantEntity.getId(),cacheList);


        if (!DefaultPwdMsg.USE_DEFAULT_PWD){
            notifyService.notifySmsTemplate(merchantEntity.getPhone(), NotifyType.CREATE_MERCHANT_SUCCESS_NOTIFY, new String[]{merchantLoginAccount, code});
        }
        return merchantId;
    }

    @Override
    public String getMerchantCode(){
        String merchantCode = merchantUtils.merchantCode(null);

        if (BaseUtil.judgeString(merchantCode)){
            return merchantCode;
        }

        //获取数据库中当前商户编码最大值
        Integer merchantCodeMax = cateringMerchantMapper.getMerchantCodeMax();

        if (ObjectUtils.isEmpty(merchantCodeMax)){
            merchantCodeMax = 0;
        }

        merchantCodeMax += 1;

        return merchantUtils.merchantCode(merchantCodeMax);
    }

    @Override
    public Result<MerchantDetailDTO> queryMerchantDetail(MerchantQueryPage queryPage) {
        CateringMerchantEntity merchantEntity = cateringMerchantMapper.selectById(queryPage.getId());
        MerchantDetailDTO merchantDetail = BaseUtil.objToObj(merchantEntity, MerchantDetailDTO.class);

        List<ShopTagInfoDTO> shopTags = merchantUtils.getShopTag(queryPage.getId());

        List<Long> merchantTagIds = new ArrayList<>();
        if (BaseUtil.judgeList(shopTags)){
            shopTags.forEach(tag->merchantTagIds.add(tag.getId()));
            merchantDetail.setMerchantTagIds(merchantTagIds);
        }
        MerchantLimitQueryDTO dto = new MerchantLimitQueryDTO();
        dto.setMerchantId(queryPage.getId());
        merchantDetail.setShopList(cateringMerchantMapper.listCateringMerchantPage(queryPage.getPage(), dto));
        return Result.succ(merchantDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<List<Long>> updateMerchantStatus(MerchantDTO dto) {
        CateringMerchantEntity merchantEntity = cateringMerchantMapper.selectById(dto.getId());
        merchantEntity.setMerchantStatus(dto.getMerchantStatus());
        cateringMerchantMapper.updateById(merchantEntity);

        //修改登陆账号信息
        UpdateWrapper<CateringMerchantLoginAccountEntity> updateQuery = new UpdateWrapper<>();
        updateQuery.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId,merchantEntity.getId())
                            .eq(CateringMerchantLoginAccountEntity::getAccountType,AccountTypeEnum.MERCHANT.getStatus());
        updateQuery.lambda().set(CateringMerchantLoginAccountEntity::getAccountStatus,merchantEntity.getMerchantStatus());
        loginAccountService.update(updateQuery);

        //redis信息同步
        merchantUtils.putMerchant(BaseUtil.objToObj(merchantEntity,MerchantInfoDTO.class));

        //返回当前商户下店铺id，用于es信息同步
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getMerchantId,dto.getId())
                .eq(CateringShopEntity::getDel,DelEnum.NOT_DELETE.getStatus());
        List<CateringShopEntity> shopList = shopService.list(query);

        List<Long> shopIds = new ArrayList<>();
        if (BaseUtil.judgeList(shopList)){
            shopList.forEach(s->shopIds.add(s.getId()));
        }

        return Result.succ(shopIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateShopRegisterPhone(RegisterPhoneChangeDTO dto) {
        CateringShopEntity shopEntity = shopService.getById(dto.getId());
        if(ObjectUtils.isEmpty(shopEntity)){
            return Result.fail("店铺不存在");
        }

        if (Objects.equals(shopEntity.getRegisterPhone(),dto.getRegisterPhone())) {
            return Result.fail("修改手机号不能与原手机号相同");
        }
        Result<String> succ = Result.succ(shopEntity.getRegisterPhone());

        String code = PasswordUtil.getPassword();
        String password = Md5Util.passwordEncrypt(code);
        shopEntity.setPassword(password);
        shopEntity.setRegisterPhone(dto.getRegisterPhone());

        //若为自提点注册手机号与门店电话同步修改
        if (Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            shopEntity.setShopPhone(dto.getRegisterPhone());
        }

        //1、同步登陆账户相关信息、店铺自身信息
        CateringMerchantLoginAccountEntity loginAccount = loginAccountService.getLoginAccount(shopEntity.getId());
        String oldPassword = loginAccount.getPassword();
        loginAccount.setPassword(password);
        loginAccount.setPhone(dto.getRegisterPhone());
        loginAccountService.updateById(loginAccount);

        shopService.updateById(shopEntity);

        //2、修改门店与自提点绑定关系信息
        this.updateShopPickupRelation(shopEntity);

        //3、修改门店缓存
        if (!Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            ShopInfoDTO shop = merchantUtils.getShop(shopEntity.getId());
            shop.setRegisterPhone(shop.getRegisterPhone());
            merchantUtils.putShop(shopEntity.getId(),shop);
        }

        //4、删除门店pc端登陆缓存、删除门店app端登陆缓存
        MerchantTokenDTO merchantAppToken = merchantUtils.getMerchantByToken(String.valueOf(loginAccount.getAccountTypeId()));
        if (!ObjectUtils.isEmpty(merchantAppToken)){
            merchantAppToken.setLogBackType(LogBackTypeEnum.CHANGE_PHONE.getStatus());
            merchantUtils.saveAppMerchantToken(loginAccount.getAccountTypeId(),merchantAppToken);
        }

        String pcToken = merchantUtils.getMerchantPcToken(loginAccount.getAccountTypeId().toString());
        if (!ObjectUtils.isEmpty(pcToken)){
            String pcTokenNew = TokenUtil.generatePcToken(loginAccount.getAccountTypeId(), loginAccount.getAccountType(), LogBackTypeEnum.CHANGE_PHONE.getStatus());
            merchantUtils.putMerchantPcToken(loginAccount.getAccountTypeId().toString(),pcTokenNew,Boolean.TRUE);
        }
        //修改达达配送端店铺信息达达配送
        if (shopEntity.getDeliveryType() == TakeOutDeliveryTypeEnum.DADA.getStatus()){
            try {
                shopService.batchSyncShopInfoToDaDa(Arrays.asList(shopEntity.getId()));
            }catch (Exception e){
                this.handleDadaErr(shopEntity,e,succ);
            }
        }
        //5、发送短信验证码
        if (!DefaultPwdMsg.USE_DEFAULT_MSG ) {
            notifyService.notifySmsTemplate(dto.getRegisterPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getRegisterPhone(), code});
        }
        //6、处理app语音通知
        if (!Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            shopPrintingConfigService.delShopPrintingConfig(shopEntity.getId());
        }
        return succ;
    }


    @Override
    public Result<Boolean> verifyMerchantInfoUnique(MerchantVerifyDTO dto) {
        QueryWrapper<CateringMerchantEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantEntity::getDel,DelEnum.NOT_DELETE.getStatus())
                .eq(BaseUtil.judgeString(dto.getPhone()),CateringMerchantEntity::getPhone,dto.getPhone())
                .eq(BaseUtil.judgeString(dto.getMerchantName()),CateringMerchantEntity::getMerchantName,dto.getMerchantName());
        List<CateringMerchantEntity> merchantEntities = cateringMerchantMapper.selectList(query);
        return Result.succ(BaseUtil.judgeList(merchantEntities));
    }

    @Override
    public Result<MerchantDTO> pcLoginInfo(String phone) {
        QueryWrapper<CateringMerchantEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantEntity::getDel,DelEnum.NOT_DELETE.getStatus())
                .eq(CateringMerchantEntity::getPhone,phone);
        List<CateringMerchantEntity> merchantList= cateringMerchantMapper.selectList(query);
        if (BaseUtil.judgeList(merchantList)){
            return Result.succ(BaseUtil.objToObj(merchantList.get(0),MerchantDTO.class));
        }
        return Result.succ(null);
    }

    @Override
    public Result<Boolean> handelMerchantData() {
        return loginAccountService.handelMerchantData();
    }

    @Override
   public Result<List<MerchantLoginAccountDTO>> listMerchantAccount(String account){
        return Result.succ(cateringMerchantMapper.listMerchantAccount(account));
   }

    @Override
    public Long insertMerchantForApply(MerchantDTO dto) {
        Long merchantId = insertMerchant(dto);
        return merchantId;
    }
}
