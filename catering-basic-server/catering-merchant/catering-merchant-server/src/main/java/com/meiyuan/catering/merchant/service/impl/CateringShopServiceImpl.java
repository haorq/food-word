package com.meiyuan.catering.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.service.CateringRoleService;
import com.meiyuan.catering.core.constant.AccountRecordConstant;
import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.dto.base.*;
import com.meiyuan.catering.core.enums.base.*;
import com.meiyuan.catering.core.enums.base.merchant.BusinessStatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.dada.DadaErrEnum;
import com.meiyuan.catering.core.enums.base.merchant.tl.TlAuditStatusEnum;
import com.meiyuan.catering.core.exception.AppUnauthorizedException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.dada.DadaUtils;
import com.meiyuan.catering.core.util.dada.client.DadaApiResponse;
import com.meiyuan.catering.core.util.merchant.ShopUtils;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dao.CateringShopMapper;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.gift.ShopGiftGoodResponseDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.pickup.*;
import com.meiyuan.catering.merchant.dto.shop.*;
import com.meiyuan.catering.merchant.dto.shop.config.DeliveryConfigResponseDTO;
import com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO;
import com.meiyuan.catering.merchant.dto.shop.config.TimeRangeResponseDTO;
import com.meiyuan.catering.merchant.entity.*;
import com.meiyuan.catering.merchant.enums.*;
import com.meiyuan.catering.merchant.sender.MerchantSendMq;
import com.meiyuan.catering.merchant.service.*;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.ShopTagInfoVo;
import com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopListVo;
import com.meiyuan.catering.merchant.vo.shop.ShopChoicePageVO;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.meiyuan.catering.core.enums.base.merchant.BusinessStatusEnum.CLOSE;
import static com.meiyuan.catering.core.enums.base.merchant.BusinessStatusEnum.OPEN;

/**
 * 店铺表(CateringShop)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
@Slf4j
public class CateringShopServiceImpl extends ServiceImpl<CateringShopMapper, CateringShopEntity>
        implements CateringShopService {
    @Resource
    private CateringShopMapper shopMapper;

    @Resource
    private CateringShopConfigService shopConfigService;

    @Autowired
    private CateringShopTagService shopTagService;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private CateringMerchantPickupAdressService pickupAdressService;

    @Autowired
    private CateringShopDeliveryTimeRangeService shopDeliveryTimeRangeService;

    @Autowired
    private CateringSelfMentionGiftService selfMentionGiftServiceService;

    @Resource
    private NotifyService notifyService;
    @Resource
    private CateringMerchantLoginAccountServiceImpl loginAccountService;

    @Autowired
    private MerchantSendMq merchantSendMq;
    @Resource
    private CateringShopPrintingConfigService shopPrintingConfigService;
    @Resource
    private CateringShopOrderNoticeService shopOrderNoticeService;
    @Resource
    private DadaUtils dadaUtils;
    @Resource
    CateringShopEmployeeService shopEmployeeService;
    @Resource
    private CateringRoleService roleService;
    @Resource
    private CateringShopExtService shopExtService;
    @Resource
    private CateringShopBankService shopBankService;

    /**
     * ===========店铺缓存相关处理===========
     */

    @Override
    public void putAllShopTagInfo() {
        Map<String, List<ShopTagInfoDTO>> map = new HashMap<>(40);
        //查询出所有商家(包含未添加任何标签的商家)
        List<ShopTagInfoVo> shopTagList = shopMapper.getAllShopTagInfo();

        if (BaseUtil.judgeList(shopTagList)) {
            shopTagList.forEach(tag -> {
                Long merchantId = tag.getMerchantId();
                List<ShopTagInfoDTO> list = map.get(merchantId.toString());

                if (BaseUtil.judgeList(list)) {
                    ShopTagInfoDTO shopTagInfoDTO = ConvertUtils.sourceToTarget(tag, ShopTagInfoDTO.class);
                    if (BaseUtil.judgeString(shopTagInfoDTO.getTagName())) {
                        list.add(shopTagInfoDTO);
                    }
                } else {
                    list = new ArrayList<>();
                    ShopTagInfoDTO shopTagInfoDTO = ConvertUtils.sourceToTarget(tag, ShopTagInfoDTO.class);
                    if (BaseUtil.judgeString(shopTagInfoDTO.getTagName())) {
                        list.add(shopTagInfoDTO);
                    }
                }
                map.put(tag.getMerchantId().toString(), list);
            });
            merchantUtils.putAllShopTagInfo(map);
        }
    }

    @Override
    public ShopInfoDTO getShopFromCache(Long shopId) {
        return merchantUtils.getShop(shopId);
    }

    @Override
    public ShopInfoDTO getShop(Long shopId) {
        ShopInfoDTO shopInfoDTO = merchantUtils.getShop(shopId);
        if (shopInfoDTO != null) {
            if (shopInfoDTO.getDel() == null) {
                // 缓存有数据但DEL为空
                shopInfoDTO.setDel(Boolean.FALSE);
            }
            return shopInfoDTO;
        }
        CateringShopEntity shopEntity = shopMapper.selectById(shopId);
        if (shopEntity == null) {
            return null;
        }
        shopInfoDTO = ConvertUtils.sourceToTarget(shopEntity, ShopInfoDTO.class);
        return shopInfoDTO;
    }
    //===========店铺缓存相关处理===========

    /**
     * 方法描述 : 修改店铺信息，更新店铺自提点关联关系
     *
     * @param shop
     * @Author: MeiTao
     * @Date: 2020/6/2 0002 10:31
     * @return: void
     * @Since version-1.0.0
     */
    private void updateShopPickupRelation(CateringShopEntity shop) {
        QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId, shop.getId());

        List<CateringMerchantPickupAdressEntity> list = pickupAdressService.list(query);

        if (BaseUtil.judgeList(list)) {
            list.forEach(entity -> {
                entity.setChargerName(shop.getPrimaryPersonName());
                if (Objects.equals(shop.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus())) {
                    entity.setChargerPhone(shop.getRegisterPhone());
                }
            });
            pickupAdressService.updateBatchById(list);
        }
    }

    @Override
    public void handlerBaiduLocation(CateringShopEntity shop, String mapCoordinate) {
        double lat = new Double(mapCoordinate.split(",")[1]), lng = new Double(mapCoordinate.split(",")[0]);
        LocationDTO location = LatitudeUtils.reverseGeocoding(lat, lng);

        shop.setAddressProvince(location.getProvince());
        shop.setAddressCity(location.getCity());
        shop.setAddressArea(location.getDistrict());

        shop.setAddressAreaCode(location.getDistrictCode().toString());

        // 获得省市区信息
        CateringRegionDTO region = merchantUtils.getRegionCache(shop.getAddressAreaCode());
        if (ObjectUtils.isEmpty(region)) {
            throw new CustomException("该区域暂不开放,请重新输入");
        }

        shop.setAddressCityCode(region.getCityCode());
        shop.setAddressProvinceCode(region.getProvinceCode());
        String addressFull = new StringBuffer().append(shop.getAddressDetail()).toString();
        if (BaseUtil.judgeString(shop.getDoorNumber())) {
            addressFull = new StringBuilder().append(addressFull).append(shop.getDoorNumber()).toString();
        }

        shop.setAddressFull(addressFull);
        shopMapper.updateById(shop);
    }

    @Override
    public Result<List<MerchantShopListVo>> listMerchantShopList(MerchantShopListDTO dto) {
        return Result.succ(shopMapper.listMerchantShopList(dto));
    }

    @Override
    public Result<PageData<GoodPushShopVo>> shopPage(ShopQueryPagDTO dto) {
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().in(BaseUtil.judgeList(dto.getShopIds()), CateringShopEntity::getId, dto.getShopIds())
                .eq(CateringShopEntity::getMerchantId, dto.getMerchantId())
                .eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getStatus());
        IPage iPage = shopMapper.selectPage(dto.getPage(), query);
        if (BaseUtil.judgeList(iPage.getRecords())) {
            iPage.setRecords(BaseUtil.objToObj(iPage.getRecords(), GoodPushShopVo.class));
        }
        return Result.succ(new PageData<>(iPage));
    }

    @Override
    public Result verifyShopInfoUnique(String keyword, Boolean type) {
        List<CateringShopEntity> shop = shopMapper.verifyShopInfoUnique(keyword, type);
        if (!BaseUtil.judgeList(shop)) {
            return Result.succ();
        }
        return Result.succ(ErrorCode.SHOP_INFO_REPEAT);
    }

    @Override
    public Result<Boolean> verifyShopInfoUnique(ShopVerifyDTO dto) {
        List<CateringShopEntity> shopEntities = shopMapper.verifyShopInfoUniqueDto(dto);
        if (BaseUtil.judgeList(shopEntities)){
            return Result.succ(BaseUtil.judgeList(shopEntities));
        }
        if (BaseUtil.judgeString(dto.getRegisterPhone())){
            QueryWrapper<CateringShopEmployeeEntity> queryEn = new QueryWrapper<>();
            queryEn.lambda().eq(CateringShopEmployeeEntity::getPhone,dto.getRegisterPhone())
                    .eq(CateringShopEmployeeEntity::getIsDel,DelEnum.NOT_DELETE.getFlag());
            List<CateringShopEmployeeEntity> list = shopEmployeeService.list(queryEn);
            if (BaseUtil.judgeList(list)){
                return Result.succ(BaseUtil.judgeList(list));
            }
        }
        return Result.succ();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CateringShopEntity> addPickupAddress(MerchantsPickupAddressDTO dto) {
        CateringShopEntity shop = new CateringShopEntity();
        BeanUtils.copyProperties(dto, shop);

        String shopCode = this.getShopCode();
        shop.setShopCode(shopCode);

        shop.setShopSource(SourceEnum.PLATFORM.getStatus());
        shop.setShopType(ShopTypeEnum.BUSINESS_POINT.getStatus());
        //自提点没有服务类型设置默认值为0
        shop.setShopService("[0]");
        shop.setShopPhone(dto.getRegisterPhone());
        String code = PasswordUtil.getPassword();
        String encodedPassword = Md5Util.passwordEncrypt(code);
        shop.setPassword(encodedPassword);
        shop.setDel(DelEnum.NOT_DELETE.getFlag());
        shop.setBusinessStatus(BusinessStatusEnum.OPEN.getStatus());
        //添加自提点
        shopMapper.insert(shop);
        //存入自提点位置信息
        this.handlerBaiduLocation(shop, dto.getMapCoordinate());

        CateringMerchantLoginAccountEntity loginAccountEntity = new CateringMerchantLoginAccountEntity();
        loginAccountEntity.setPhone(shop.getRegisterPhone());
        loginAccountEntity.setPassword(Md5Util.passwordEncrypt(code));
        loginAccountEntity.setAccountTypeId(shop.getId());
        loginAccountEntity.setAccountStatus(shop.getShopStatus());
        loginAccountEntity.setAccountType(AccountTypeEnum.PICKUP.getStatus());
        loginAccountEntity.setIsDel(DelEnum.NOT_DELETE.getFlag());

        //添加登录账号信息
        loginAccountService.save(loginAccountEntity);
        if (!DefaultPwdMsg.USE_DEFAULT_PWD) {
            notifyService.notifySmsTemplate(dto.getRegisterPhone(), NotifyType.CREATE_ACCOUNT_SUCCESS_NOTIFY, new String[]{dto.getRegisterPhone(), code});
        }
        return Result.succ(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePickupAddress(PickupAdressUpdateDTO dto) {
        CateringShopEntity shop = new CateringShopEntity();
        shop.setId(dto.getId());
        shop.setShopName(dto.getShopName());
        shop.setShopStatus(dto.getShopStatus());
        shopMapper.updateById(shop);

        //修改自提点名称：修改店铺与自提点绑定关系表
        if (!ObjectUtils.isEmpty(dto.getShopName())) {
            QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper<>();
            query.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId, dto.getId());
            List<CateringMerchantPickupAdressEntity> list = pickupAdressService.list(query);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(s -> s.setName(dto.getShopName()));
                //更新自提点名字
                pickupAdressService.updateBatchById(list);
            }
        }
        return Result.succ();
    }

    @Override
    public List<MerchantShopDTO> listMerchantSelect(String keyWord) {
        //查询所有店铺
        QueryWrapper<CateringShopEntity> queryShop = new QueryWrapper<>();
        queryShop.lambda().eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getStatus())
                .like(CateringShopEntity::getShopService, "%1%")
                .ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus());

        List<CateringShopEntity> shopEntities = baseMapper.selectList(queryShop);

        //查询所有商家
        List<MerchantShopDTO> merchantList = this.baseMapper.listMerchantSelect(keyWord);

        merchantList.forEach(merchant -> {
            List<ShopActivityDTO> children = new ArrayList<>();
            shopEntities.forEach(shop -> {
                if (Objects.equals(merchant.getValue(), shop.getMerchantId())) {
                    ShopActivityDTO dto = BaseUtil.objToObj(shop, ShopActivityDTO.class);
                    dto.setValue(shop.getId());
                    dto.setLabel(shop.getShopName());
                    children.add(dto);
                }
            });
            merchant.setChildren(children);
        });

        return merchantList;
    }

    @Override
    public List<ShopDTO> listGoodManagerShop(ShopQueryConditionDTO dto) {
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus())
                .eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .like(!ObjectUtils.isEmpty(dto.getShopName()), CateringShopEntity::getShopName, dto.getShopName())
                .eq(!ObjectUtils.isEmpty(dto.getAddressProvinceCode()), CateringShopEntity::getAddressProvinceCode, dto.getAddressProvinceCode())
                .eq(!ObjectUtils.isEmpty(dto.getAddressCityCode()), CateringShopEntity::getAddressCityCode, dto.getAddressCityCode())
                .eq(!ObjectUtils.isEmpty(dto.getAddressAreaCode()), CateringShopEntity::getAddressAreaCode, dto.getAddressAreaCode())
                .notIn(BaseUtil.judgeList(dto.getMerchantIds()), CateringShopEntity::getMerchantId, dto.getMerchantIds())
                .notIn(BaseUtil.judgeList(dto.getShopIds()), CateringShopEntity::getId, dto.getShopIds());

        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            query.lambda().and(wra -> wra.like(CateringShopEntity::getPrimaryPersonName, dto.getKeyword()).or()
                    .like(CateringShopEntity::getRegisterPhone, dto.getKeyword()));
        }

        //根据查询条件查询所有店铺
        List<CateringShopEntity> shopEntities = shopMapper.selectList(query);
        List<ShopDTO> shopList = new ArrayList<>();
        if (!BaseUtil.judgeList(shopEntities)) {
            return shopList;
        }
        return ConvertUtils.sourceToTarget(shopEntities, ShopDTO.class);
    }

    /**
     * describe: 商品推送-商户下拉列表查询
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 16:08
     * @return: {com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>>}
     * @version 1.2.0
     **/
    @Override
    public Result<PageData<GoodPushShopVo>> goodPushShopPage(GoodPushShopPagDTO dto) {
        List<Long> removeShopIds = dto.getRemoveShopIds();
        List<Long> shopIds = dto.getShopIds();
        Set<Long> newList = new HashSet<>();
        newList.addAll(shopIds);
        if (!CollectionUtils.isEmpty(removeShopIds)) {
            for (Long removeId : removeShopIds) {
                newList.remove(removeId);
            }
        }
        IPage<GoodPushShopVo> pageData = shopMapper.goodPushShopPage(dto.getPage(), dto, newList);
        return Result.succ(new PageData<>(pageData));
    }

    /**
     * describe: 查询商品已推送门店
     * 1、传入商户id对应下的门店
     *
     * @param dto
     * @author: yy
     * @date: 2020/7/8 16:15
     * @return: {com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.merchant.vo.merchant.GoodPushShopVo>>}
     * @version 1.2.0
     **/
    @Override
    public Result<PageData<GoodPushShopVo>> goodPushedShopPage(GoodPushShopPagDTO dto) {
        //若为空则直接返回
        if (!BaseUtil.judgeList(dto.getShopIds())) {
            IPage<GoodPushShopVo> pageData = dto.getPage();
            pageData.setCurrent(1);
            ((Page<GoodPushShopVo>) pageData).setSearchCount(Boolean.TRUE);
            pageData.setPages(0L);

            return Result.succ(new PageData<>(pageData));
        }

        return Result.succ(new PageData<>(shopMapper.goodPushedShopPage(dto.getPage(), dto)));
    }


    /**
     * 通过商户id 获取标签信息
     *
     * @param merchantId
     * @return
     */
    @Override
    public List<ShopTagInfoDTO> listShopTagCache(Long merchantId) {
        return merchantUtils.getShopTag(merchantId);
    }

    /**
     * 修改店铺缓存
     *
     * @param merchantId
     * @param shopTagInfoList
     */
    @Override
    public void putShopTag(Long merchantId, List<ShopTagInfoDTO> shopTagInfoList) {
        merchantUtils.putShopTag(merchantId, shopTagInfoList);
    }

    @Override
    public List<ShopListV101DTO> shopListV101() {
        return baseMapper.shopListV101();
    }

    @Override
    public Result<MerchantLoginResponseDTO> appMerchantLogin(MerchantLoginRequestDTO dto, String key, String iv) {
        Long shopId = null;
        Integer type = null;
        // 1、根据手机号获得店铺信息
        CateringMerchantLoginAccountEntity loginAccountEntity = loginAccountService.getLoginAccountByPhone(dto.getPhone());
        if (ObjectUtils.isEmpty(loginAccountEntity)) {
            return Result.fail("账号不存在，请重新输入");
        }
        //员工禁用则不允许登陆
        if (Objects.equals(loginAccountEntity.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            if (Objects.equals(StatusEnum.ENABLE_NOT.getStatus(),loginAccountEntity.getAccountStatus())){
                return Result.fail("账号已被禁用，请联系门店负责人");
            }
        }

        // 3、校验密码是否一致
        String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), key, iv);
        if (!BaseUtil.judgeString(desEncrypt)) {
            return Result.fail("密码错误，请重新输入");
        }
        if (!Md5Util.md5(desEncrypt).equals(loginAccountEntity.getPassword())) {
            return Result.fail("密码错误，请重新输入");
        }
        CateringShopEntity shopEntity = baseMapper.selectById(loginAccountEntity.getAccountTypeId());
        //若为员工id则查询门店信息
        CateringShopEmployeeEntity shopEmployeeEntity = null;
        if (Objects.equals(loginAccountEntity.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            shopEmployeeEntity = shopEmployeeService.getById(loginAccountEntity.getAccountTypeId());
            if (ObjectUtils.isEmpty(shopEmployeeEntity)){
                return Result.fail("账号不存在，请重新输入");
            }
            if (Objects.equals(DelEnum.DELETE.getStatus(),shopEmployeeEntity.getIsDel())){
                return Result.fail("账号已被删除，请联系门店负责人");
            }
            //员工表0表示禁用
            if (Objects.equals(0,shopEmployeeEntity.getStatus())){
                return Result.fail("账号已被禁用，请联系门店负责人");
            }
            Boolean permissionLogin = roleService.checkIfHasPermissionLogin(loginAccountEntity.getAccountTypeId(), 1);
            if (!permissionLogin){
                return Result.fail("无登录权限，请联系门店负责人");
            }
            Result<ShopBankInfoVo> shopBankInfoData = shopBankService.getShopBankInfo(shopEmployeeEntity.getShopId());
            ShopBankInfoVo shopBankInfo = shopBankInfoData.getData();
            if (ObjectUtils.isEmpty(shopBankInfo)){
                return Result.fail("当前店铺结算信息尚未完善，请联系店长");
            }
            if (!Objects.equals(shopBankInfo.getAuditStatus(), TlAuditStatusEnum.FINISH.getStatus())){
                return Result.fail("当前店铺结算信息尚未完善，请联系店长");
            }
            shopEntity =  baseMapper.selectById(shopEmployeeEntity.getShopId());
            shopId = shopEmployeeEntity.getShopId();
            type = 4;
        }else if(Objects.equals(shopEntity.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            shopId = loginAccountEntity.getAccountTypeId();
            type = 2;
        }else {
            shopId = loginAccountEntity.getAccountTypeId();
            type = 1;
        }

        if (Objects.equals(shopEntity.getDel(), DelEnum.DELETE.getFlag())) {
            return Result.fail("门店已被删除，请联系工作人员");
        }

        // 4、检验成功后：1--生成token,2--缓存登录信息
        Map<String, Object> map = new HashMap<>(5);
        //账号类型，账号类型对应id
        map.put("shopId", shopId);
        map.put("accountTypeId", loginAccountEntity.getAccountTypeId());
        map.put("accountType", loginAccountEntity.getAccountType());
        map.put("only", PasswordUtil.getPassword());
        if (!ObjectUtils.isEmpty(shopEmployeeEntity)){
            map.put("employeeId",shopEmployeeEntity.getId());
        }
        String token = TokenUtil.generateToken(map, TokenEnum.MERCHANT_APP);
        MerchantTokenDTO merchantTokenDto = BaseUtil.objToObj(shopEntity, MerchantTokenDTO.class);
        merchantTokenDto.setShopId(shopId);
        merchantTokenDto.setToken(token);
        merchantTokenDto.setType(type);
        merchantTokenDto.setAccountTypeId(loginAccountEntity.getAccountTypeId());
        merchantTokenDto.setLogBackType(LogBackTypeEnum.NORMAL.getStatus());
        //判断店铺是否能够切换登录身份
        Boolean changeRole = Boolean.FALSE;
        Boolean firstLogin = ObjectUtils.isEmpty(loginAccountEntity.getLastLoginTime()) ? Boolean.TRUE : Boolean.FALSE;
        // 5、记录最后一次登录信息
        shopEntity.setLastLoginIp(HttpContextUtils.getRealIp(HttpContextUtils.getHttpServletRequest()));
        shopEntity.setLastLoginTime(LocalDateTime.now());
        shopMapper.updateById(shopEntity);
        loginAccountEntity.setLastLoginTime(LocalDateTime.now());
        loginAccountService.updateById(loginAccountEntity);

        Long accountTypeId = loginAccountEntity.getAccountTypeId();
        MerchantLoginResponseDTO data = getMerchantLoginResponseDTO(shopEntity, accountTypeId, type, token, merchantTokenDto, changeRole, firstLogin);
        //短信验证码修改手机号
        data.getShop().setLoginPhone(loginAccountEntity.getPhone());
        data.getShop().setRegisterPhone(loginAccountEntity.getPhone());
        //修改当前登录设备打印小票的相关配置【若登录身份为自提点则不做处理】
        if (!Objects.equals(shopEntity.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus()) && !BaseUtil.judgeString(dto.getDeviceNumber())) {
            log.error("门店登录设备号传入为空，门店id ： " + data.getShop().getShopId() + "；登陆token ： " + data.getToken());
        }
        if (!Objects.equals(shopEntity.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus())
                && BaseUtil.judgeString(dto.getDeviceNumber())) {
            ShopPrintingConfigDTO shopPrintingConfigDto = new ShopPrintingConfigDTO();
            shopPrintingConfigDto.setShopId(loginAccountEntity.getAccountTypeId());
            shopPrintingConfigDto.setDeviceNumber(dto.getDeviceNumber());
            shopPrintingConfigService.saveShopPrintingConfig(shopPrintingConfigDto);
        }
        //保存登录
        merchantUtils.saveAppMerchantToken(loginAccountEntity.getAccountTypeId(),merchantTokenDto);
        return Result.succ(data);
    }

    @Override
    public Result<MerchantLoginResponseDTO> appMerchantUpdatePassword(MerchantPasswordRequestDTO dto, String key, String iv) {
        // 1、短信验证码验证
        String code = merchantUtils.getSmsAuthCode(dto.getPhone());
        if (StringUtils.isEmpty(code)) {
            return Result.fail("验证码已失效，请重新发送短信验证码");
        }
        if (!Objects.equals(code, dto.getSmsCode())) {
            return Result.fail("验证码错误，请重新输入");
        }

        // 2、修改密码
        MerchantLoginAccountDTO accountDto = new MerchantLoginAccountDTO();
        accountDto.setPhone(dto.getPhone());
        Result<List<MerchantLoginAccountDTO>> account = loginAccountService.getAccount(accountDto);

        if (!BaseUtil.judgeResultObject(account)) {
            return Result.fail("账号不存在，请重新输入");
        }

        MerchantLoginAccountDTO loginAccountDto = account.getData().get(0);
        String oldPassword = loginAccountDto.getPassword();
        if (loginAccountDto.getIsDel()) {
            return Result.fail("账号已被删除，请联系管理员！！！");
        }

        //解密密码
        String desEncrypt = AesEncryptUtil.desEncrypt(dto.getPassword(), key, iv);

        if (!BaseUtil.judgeString(desEncrypt)) {
            return Result.fail("密码错误，请重新输入");
        }
        if (Objects.equals(Md5Util.passwordEncrypt(desEncrypt), loginAccountDto.getPassword())) {
            return Result.fail(501, "原密码与新密码相同");
        }
        String passwordEncrypt = Md5Util.passwordEncrypt(desEncrypt);
        loginAccountDto.setPassword(passwordEncrypt);

        CateringMerchantLoginAccountEntity entity = new CateringMerchantLoginAccountEntity();
        entity.setId(loginAccountDto.getId());
        entity.setPassword(passwordEncrypt);
        loginAccountService.updateById(entity);

        //刪除缓存中的短信验证码
        merchantUtils.removeSmsAuthCode(dto.getPhone());

        //String token = Md5Util.md5(oldPassword + loginAccountDto.getAccountTypeId());
        //处理app端登陆token
        merchantUtils.removeMerAppToken(loginAccountDto.getAccountTypeId().toString());

        //处理pc端登录token
        String merchantPcToken = merchantUtils.getMerchantPcToken(loginAccountDto.getAccountTypeId().toString());
        if (BaseUtil.judgeString(merchantPcToken)){
            String tokenPc = TokenUtil.generatePcToken(loginAccountDto.getAccountTypeId(),loginAccountDto.getAccountType(),LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
            merchantUtils.putMerchantPcToken(loginAccountDto.getAccountTypeId().toString(),tokenPc,Boolean.TRUE);
        }

        //若为店铺、店铺兼自提点、员工移除语音通知相关设备
        if (Objects.equals(loginAccountDto.getAccountType(), AccountTypeEnum.SHOP.getStatus())
                || Objects.equals(loginAccountDto.getAccountType(), AccountTypeEnum.SHOP_PICKUP.getStatus())
                || Objects.equals(loginAccountDto.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())) {
            shopPrintingConfigService.delShopPrintingConfig(loginAccountDto.getAccountTypeId());
        }
        log.info("update password merchant:{} , ip:{}", loginAccountDto.getId(), HttpContextUtils.getRealIp(HttpContextUtils.getHttpServletRequest()));
        return Result.succ();
    }

    @Override
    public Result<MerchantLoginResponseDTO> appMerchantExchangeIdentity(Long shopId, Integer shopRole) {
        // 查询店铺信息，判断店铺是否能够切换
        CateringShopEntity shop = shopMapper.selectById(shopId);

        Boolean isPickupPoint = shop.getIsPickupPoint();
        if (isPickupPoint == null) {
            throw new CustomException("当前自提点不是店铺不能切换");
        }
        if (Objects.equals(isPickupPoint, Boolean.FALSE)) {
            throw new CustomException("当前店铺不是自提点不能切换");
        }

        //判断店铺是否能够切换登录身份
        Boolean changeRole = Boolean.FALSE;

        //生成token
        String token = Md5Util.md5(shop.getId().toString());
        int type = shopRole;
        //改变当前店铺登录身份
        if (Objects.equals(shop.getIsPickupPoint(), Boolean.TRUE)) {
            type = ShopIsPickupEnum.SHOP_AND_PICKUP.getStatus();
        }
        //若店铺以自提点的身份进行登录(跟换token，改变类型)
        if (Objects.equals(shopRole, ShopIsPickupEnum.PICKUP.getStatus())) {
            token = Md5Util.md5(shop.getId().toString() + shopRole);
            type = ShopIsPickupEnum.PICKUP.getStatus();
        }

        //判断当前商户是否已切换登录身份
        MerchantTokenDTO merchantByToken = merchantUtils.getMerchantByToken(token);
        //是否是首次登录:true:是，false：否
        Boolean firstLogin = shop.getLastLoginTime() == null ? Boolean.TRUE : Boolean.FALSE;

        return Result.succ(getMerchantLoginResponseDTO(shop, shopId, type, token, merchantByToken, changeRole, firstLogin));
    }

    /**
     * 批量获取根据商户id集合
     *
     * @param merchantIdList 商户id集合
     * @author: wxf
     * @date: 2020/5/19 18:22
     * @return: {@link Result< List< CateringShopEntity>>}
     * @version 1.0.1
     **/
    @Override
    public Result<List<ShopDTO>> listByMerchantIdList(List<Long> merchantIdList) {
        QueryWrapper<CateringShopEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringShopEntity::getMerchantId, merchantIdList);
        List<CateringShopEntity> list = this.list(queryWrapper);
        List<ShopDTO> dtoList = Collections.emptyList();
        if (BaseUtil.judgeList(list)) {
            dtoList = BaseUtil.objToObj(list, ShopDTO.class);
        }
        return Result.succ(dtoList);
    }

    /**
     * 方法描述 : 根据手机号获得店铺信息
     *
     * @Author: MeiTao
     */
    private CateringShopEntity findEntityByPhone(String phone) {
        // 1、根据手机号获得店铺信息
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getRegisterPhone, phone)
                .eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringShopEntity> shopList = shopMapper.selectList(query);

        CateringShopEntity shopEntity = null;
        if (BaseUtil.judgeList(shopList)) {
            shopEntity = shopList.get(0);
        }
        return shopEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShopTagV3(Long merchantId, List<Long> shopTags) {
        //删除店铺之前标签关联信息
        shopTagService.delOldMerchantShopTagV3(merchantId);

        if (BaseUtil.judgeList(shopTags)) {
            shopTagService.insertShopTagRelation(merchantId, shopTags);
        }
    }

    /**
     * 方法描述 : 组装商户登录返回数据并将信息存入缓存
     *
     * @param shop
     * @param type            当前店铺登录身份： 1--店铺，2--自提点，3--既是店铺也是自提点
     * @param token
     * @param merchantByToken
     * @param changeRole      是否能够切换登录身份
     * @Author: MeiTao
     * @Date: 2020/5/21 0021 9:28
     * @return: MerchantLoginResponseDTO
     * @Since version-1.0.0
     */
    private MerchantLoginResponseDTO getMerchantLoginResponseDTO(CateringShopEntity shop, Long accountTypeId, Integer type, String token, MerchantTokenDTO merchantByToken, Boolean changeRole, Boolean firstLogin) {
        //组装返回数据
        ShopResponseDTO responseDTO = ConvertUtils.sourceToTarget(shop, ShopResponseDTO.class);
        responseDTO.setType(type);

        responseDTO.setChangeRole(changeRole);
        responseDTO.setShopId(shop.getId());
        responseDTO.setShopLogo(shop.getDoorHeadPicture());
        //是否是首次登录:true:是，false：否
        responseDTO.setFirstLogin(firstLogin);

        MerchantLoginResponseDTO data = new MerchantLoginResponseDTO();
        data.setShop(responseDTO);
        data.setToken(token);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<DeliveryConfigResponseDTO> getDeliveryConfig(Long shopId) {
        DeliveryConfigResponseDTO result = new DeliveryConfigResponseDTO();

        QueryWrapper<CateringShopConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopConfigEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopConfigEntity::getShopId, shopId);
        CateringShopConfigEntity shopConfig = shopConfigService.getOne(query);

        //设置店铺配送基本信息
        BeanUtils.copyProperties(shopConfig, result);

        //设置配送时间段
        result.setPickupTimes(shopDeliveryTimeRangeService.getTimeRangeList(shopId, ShopDeliveryTypeEnum.DELIVERY.getStatus()));
        return Result.succ(result);
    }

    /**
     * 方法描述 : 门店配送配置信息修改
     * 1、修改门店配送配置信息
     * 2、修改门店配送时间范围
     *
     * @param shopId
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 11:09
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyDeliveryConfig(Long shopId, DeliveryConfigResponseDTO dto) {
        //校验配送时间时间段格式是否正确
        this.checkTimeRange(dto.getPickupTimes());

        CateringShopConfigEntity shopConfig = new CateringShopConfigEntity();
        BeanUtils.copyProperties(dto, shopConfig);

        //修改店铺配置信
        shopConfigService.modifyDeliveryConfig(shopConfig);

        //店铺配送时间段信息
        saveOrUpdateTimeRange(dto.getPickupTimes(), ShopDeliveryTypeEnum.DELIVERY.getStatus(), shopId);

        //修改门店配送缓存
        shopConfig.setShopId(shopId);
        shopConfig = shopConfigService.getById(shopConfig.getId());
        this.updateShopConfigCache(shopConfig);
        return Result.succ();
    }

    private void checkTimeRange(List<TimeRangeResponseDTO> pickupTimes) {
        if (!BaseUtil.judgeList(pickupTimes)) {
            throw new CustomException("至少包含一个配送时间段");
        }
        List<TimeRangeDTO> timeRangeList = ConvertUtils.sourceToTarget(pickupTimes, TimeRangeDTO.class);
        DateTimeUtil.checkTimeRange(timeRangeList);
    }

    /**
     * 店鋪自提配送时间段批量添加修改
     *
     * @param pickupTimes 需要添加或修改的时间范围（有id则为修改）
     * @param type        时间范围类型:1：店铺配送时间范围，2：店铺自提时间范围
     * @param shopId
     */
    private void saveOrUpdateTimeRange(List<TimeRangeResponseDTO> pickupTimes, Integer type, Long shopId) {
        if (!BaseUtil.judgeList(pickupTimes)) {
            throw new CustomException(501, "店铺自提或配送时间段至少应有一个");
        }
        //查询店铺对应类型时间范围
        QueryWrapper<CateringShopDeliveryTimeRangeEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopDeliveryTimeRangeEntity::getShopId, shopId)
                .eq(CateringShopDeliveryTimeRangeEntity::getType, type)
                .eq(CateringShopDeliveryTimeRangeEntity::getIsDel, DelEnum.NOT_DELETE.getFlag());

        List<CateringShopDeliveryTimeRangeEntity> timeRangeEntitys = shopDeliveryTimeRangeService.list(query);

        //获取店铺需要删除的时间段
        List<CateringShopDeliveryTimeRangeEntity> delTimeRangeEntityList = new ArrayList<>();
        Map<Long, TimeRangeResponseDTO> pickupTimeMap = new HashMap<>(3);
        pickupTimes.forEach(t -> {
            if (t.getId() != null) {
                pickupTimeMap.put(t.getId(), t);
            }
        });

        timeRangeEntitys.forEach(t -> {
            TimeRangeResponseDTO timeRangeResponseDTO = pickupTimeMap.get(t.getId());
            if (timeRangeResponseDTO == null) {
                t.setIsDel(DelEnum.DELETE.getFlag());
                delTimeRangeEntityList.add(t);
            }
        });

        //删除店铺 自提/配送 时间段
        if (BaseUtil.judgeList(delTimeRangeEntityList)) {
            shopDeliveryTimeRangeService.updateBatchById(delTimeRangeEntityList);
        }

        if (BaseUtil.judgeList(pickupTimes)) {
            //id为空则是添加新的配送时间段，反之则为修改
            List<CateringShopDeliveryTimeRangeEntity> times = new ArrayList<>();
            pickupTimes.forEach(t -> {
                CateringShopDeliveryTimeRangeEntity timeRange = new CateringShopDeliveryTimeRangeEntity();
                BeanUtils.copyProperties(t, timeRange);
                if (t.getId() == null) {
                    timeRange.setShopId(shopId);
                    timeRange.setType(type);
                }
                timeRange.setIsDel(DelEnum.NOT_DELETE.getFlag());
                times.add(timeRange);
            });

            //修改、添加店铺配送时间段
            shopDeliveryTimeRangeService.saveOrUpdateBatch(times);
        }
    }

    /**
     * 方法描述 : 更新店铺缓存信息
     *
     * @param shopConfigEntity
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 15:16
     * @return: void
     * @Since version-1.0.0
     */
    public void updateShopConfigCache(CateringShopConfigEntity shopConfigEntity) {
        ShopConfigInfoDTO shopConfigInfo = ConvertUtils.sourceToTarget(shopConfigEntity, ShopConfigInfoDTO.class);
        //组装店铺自提配送时间范围
        shopConfigInfo = shopDeliveryTimeRangeService.setShopTimeRangeInfo(shopConfigInfo);
        CateringShopEntity shopEntity = shopMapper.selectById(shopConfigEntity.getShopId());
        //更新店铺缓存
        merchantUtils.putShopConfigInfo(shopEntity.getId(), shopConfigInfo);
    }

    @Override
    public Result<PickupManagerResponseDTO> listShopPickupAddress(PickupPointRequestDTO dto) {
        PickupManagerResponseDTO response = new PickupManagerResponseDTO();
        response.setIsPickupPoint(Boolean.FALSE);
        //类型:1、查询已添加自提点
        if (Objects.equals(BoundTypeEnum.BOUND.getStatus(), dto.getType())) {
            response.setPointList(shopMapper.listShopPickupAddress(dto));

            //查询店铺是否绑定自身
            QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper<>();
            query.lambda().eq(CateringMerchantPickupAdressEntity::getShopId, dto.getId())
                    .eq(CateringMerchantPickupAdressEntity::getIsDel, DelEnum.NOT_DELETE.getFlag())
                    .eq(CateringMerchantPickupAdressEntity::getPickupId, dto.getId());
            List<CateringMerchantPickupAdressEntity> selfList = pickupAdressService.list(query);
            response.setIsPickupPoint(BaseUtil.judgeList(selfList));
            return Result.succ(response);
        }

        //2、查询当前店铺未绑定自提点列表
        //2.1 查询店铺已添加自提点
        QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMerchantPickupAdressEntity::getShopId, dto.getId())
                .eq(CateringMerchantPickupAdressEntity::getIsDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringMerchantPickupAdressEntity> haveBoundPickup = pickupAdressService.list(query);

        List<Long> shopIds = new ArrayList<>();
        shopIds.add(dto.getId());
        if (BaseUtil.judgeList(haveBoundPickup)) {
            haveBoundPickup.forEach(pickup -> {
                shopIds.add(pickup.getPickupId());
            });
        }
        //2.2：查询当前店铺未添加的自提点
        response.setPointList(getPickupPointNotAdd(shopIds, dto.getKeyWord()));

        return Result.succ(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result modifyPickupConfig(Long shopId, PickupConfigResponseDTO dto) {
        //校验配送时间时间段格式是否正确
        this.checkTimeRange(dto.getPickupTimes());

        //1、门店赠品信息修改
        updateShopGift(dto.getGiftGoods(), shopId);

        //自提时间范围修改
        saveOrUpdateTimeRange(dto.getPickupTimes(), ShopDeliveryTypeEnum.PICKUP.getStatus(), shopId);

        //更新缓存
        QueryWrapper<CateringShopConfigEntity> query = new QueryWrapper();
        query.lambda().eq(CateringShopConfigEntity::getShopId, shopId)
                .eq(CateringShopConfigEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringShopConfigEntity shopConfigEntity = shopConfigService.getOne(query);
        updateShopConfigCache(shopConfigEntity);
        return Result.succ();
    }

    /**
     * 修改门店赠品信息
     *
     * @param giftGoods
     */
    private void updateShopGift(List<ShopGiftGoodResponseDTO> giftGoods, Long shopId) {
        //1、查询店铺已添加赠品
        QueryWrapper<CateringSelfMentionGiftEntity> queryGift = new QueryWrapper<>();
        queryGift.lambda().eq(CateringSelfMentionGiftEntity::getShopId, shopId)
                .eq(CateringSelfMentionGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringSelfMentionGiftEntity> shopGifts = selfMentionGiftServiceService.list(queryGift);

        Map<Long, CateringSelfMentionGiftEntity> entityMap = shopGifts.stream()
                .collect(Collectors.toMap(CateringSelfMentionGiftEntity::getGiftId, giftEntities -> giftEntities));

        //若传入赠品为空，则移除所有已存在赠品
        if (!BaseUtil.judgeList(giftGoods)) {
            if (BaseUtil.judgeList(shopGifts)) {
                List<Long> ids = shopGifts.stream().map(CateringSelfMentionGiftEntity::getId).collect(Collectors.toList());
                selfMentionGiftServiceService.removeByIds(ids);
            }
            return;
        }

        //转化为实体类
        List<CateringSelfMentionGiftEntity> list = new ArrayList<>();
        giftGoods.forEach(gift -> {
            CateringSelfMentionGiftEntity giftEntity = ConvertUtils.sourceToTarget(gift, CateringSelfMentionGiftEntity.class);
            giftEntity.setShopId(shopId);
            //若传入id为0
            if (gift.getId() == 0) {
                //若当前赠品已经添加过
                CateringSelfMentionGiftEntity selfMentionGiftEntity = entityMap.get(gift.getGiftId());
                Long id = null;
                if (selfMentionGiftEntity != null) {
                    id = selfMentionGiftEntity.getId();
                }
                giftEntity.setId(id);
            }
            list.add(giftEntity);
        });

        Map<Long, CateringSelfMentionGiftEntity> resultMap = list.stream().collect(Collectors.toMap(CateringSelfMentionGiftEntity::getGiftId, giftEntitie -> giftEntitie));

        //获取店铺删除赠品
        List<Long> del = new ArrayList<>();
        shopGifts.forEach(shopGift -> {
            CateringSelfMentionGiftEntity selfMentionGiftEntity = resultMap.get(shopGift.getGiftId());
            //若当前店铺添加修改赠品中无该赠品则删除该赠品
            if (selfMentionGiftEntity == null) {
                del.add(shopGift.getId());
            }
        });

        if (BaseUtil.judgeList(del)) {
            selfMentionGiftServiceService.removeByIds(del);
        }

        //添加或者修改赠品
        selfMentionGiftServiceService.saveOrUpdateBatch(list);
    }

    /**
     * 组装自提点信息
     *
     * @param pickupList
     * @return
     */
    private List<PickupPointResponseDTO> getPickupPoints(List<CateringMerchantPickupAdressEntity> pickupList, PickupManagerResponseDTO response) {
        List<PickupPointResponseDTO> pointList = new ArrayList<>();
        pickupList.forEach(pickupPoint -> {
            if (Objects.equals(pickupPoint.getPickupId(), pickupPoint.getShopId())) {
                response.setIsPickupPoint(!pickupPoint.getIsDel());
            } else {
                PickupPointResponseDTO responseDto = ConvertUtils.sourceToTarget(pickupPoint, PickupPointResponseDTO.class);
                pointList.add(responseDto);
            }
        });
        response.setPointList(pointList);
        return pointList;
    }

    /**
     * 方法描述 : 查询当前店铺未添加自提点并组装返回结果
     *
     * @param shopIds 已添加自提点id
     * @param keyword 关键字：自提点名称，负责人手机号
     * @Author: MeiTao
     * @Date: 2020/6/4 0004 10:04
     * @return: java.util.List<com.meiyuan.catering.merchant.dto.pickup.PickupPointResponseDTO>
     * @Since version-1.0.0
     */
    private List<PickupPointResponseDTO> getPickupPointNotAdd(List<Long> shopIds, String keyword) {
        //获取所有自提点
        List<PickupPointResponseDTO> pointList = shopMapper.listShopPickupAddressNot(keyword);

        return pointList.stream().filter(e -> !shopIds.contains(e.getPickupId())).collect(Collectors.toList());
    }

    /**
     * 获取门店首页信息
     *
     * @param shopId
     * @return
     */
    @Override
    public Result<ShopHomeResponseDTO> getShopHomeInfo(Long shopId) {
        ShopHomeResponseDTO shopHome = new ShopHomeResponseDTO();
        CateringShopEntity shop = shopMapper.selectById(shopId);
        BeanUtils.copyProperties(shop, shopHome);
        List<String> list = JSON.parseArray(shop.getAddressPicture(), String.class);
        shopHome.setAddressPictureList(list);
        shopHome.setAddressPictureDTOS(handelAddressPicture(list));
        shopHome.setBusinessStatus(ShopUtils.getBusinessStatus(shop.getBusinessStatus(), shop.getOpeningTime(), shop.getClosingTime()));

        MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
        shopHome.setMerchantAttribute(!ObjectUtils.isEmpty(merchant) ? merchant.getMerchantAttribute() : 1);
        if (!Objects.equals(ShopTypeEnum.BUSINESS_POINT.getStatus(), shop.getShopType())) {
            QueryWrapper<CateringShopConfigEntity> query = new QueryWrapper<>();
            query.lambda().eq(CateringShopConfigEntity::getShopId, shopId);
            CateringShopConfigEntity shopConfig = shopConfigService.getOne(query);
            shopHome.setBusinessSupport(shopConfig.getBusinessSupport());
        }

        if (!Objects.equals(shop.getShopStatus(), StatusEnum.ENABLE_NOT.getStatus())) {
            shopHome.setShopStatus(merchant.getMerchantStatus());
        }

        return Result.succ(shopHome);
    }

    /**
     * 店铺自提点绑定解绑
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveOrDelShopPickup(PickupUpdateRequestDTO dto) {
        CateringShopEntity pickupPoint = shopMapper.selectById(dto.getPickupPointId());
        Result<String> success = Result.succ();
        if (ObjectUtils.isEmpty(pickupPoint) || Objects.equals(pickupPoint.getDel(), DelEnum.DELETE.getFlag())) {
            log.debug("自提点已被删除请解绑其他自提点，自提点id ：" + dto.getPickupPointId());
            success.setMsg("当前自提点已被删除");
            return success;
        }

        //判断该店铺是否绑定过该自提点
        QueryWrapper<CateringMerchantPickupAdressEntity> query = new QueryWrapper();
        query.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId, dto.getPickupPointId())
                .eq(CateringMerchantPickupAdressEntity::getShopId, dto.getShopId());
        List<CateringMerchantPickupAdressEntity> pickupShopRelation = pickupAdressService.list(query);

        //若当前操作为绑定
        if (Objects.equals(dto.getType(), BoundTypeEnum.BOUND.getStatus())) {
            return shopBoundPickupPoint(dto, pickupShopRelation, pickupPoint);
        }

        //解除绑定
        if (pickupShopRelation == null) {
            throw new CustomException("当前店铺尚未绑定该自提点");
        }
        //解除绑定关系
        pickupShopRelation.forEach(entity -> {
            entity.setIsDel(DelEnum.DELETE.getFlag());
        });

        pickupAdressService.updateBatchById(pickupShopRelation);
        success.setMsg("解绑成功");
        return success;
    }

    private void updateShopInfo(PickupUpdateRequestDTO dto) {
        ShopUpdateDTO shopUpdate = new ShopUpdateDTO();
        shopUpdate.setId(dto.getShopId());

        String token = Md5Util.md5(dto.getShopId().toString());
        MerchantTokenDTO merchantByToken = merchantUtils.getMerchantByToken(token);

        if (merchantByToken == null) {
            throw new AppUnauthorizedException();
        }

        //若为解绑
        if (Objects.equals(dto.getType(), BoundTypeEnum.NOT_BOUND.getStatus())) {
            shopUpdate.setIsPickupPoint(Boolean.FALSE);
            merchantByToken.setType(ShopIsPickupEnum.SHOP.getStatus());
        } else {
            shopUpdate.setIsPickupPoint(Boolean.TRUE);
            merchantByToken.setType(ShopIsPickupEnum.SHOP_AND_PICKUP.getStatus());
        }
        //更新店铺信息并更新缓存
        CateringShopEntity shopEntity = shopMapper.selectById(shopUpdate.getId());
        shopEntity.setIsPickupPoint(shopUpdate.getIsPickupPoint());
        shopEntity.setUpdateTime(LocalDateTime.now());
        shopMapper.updateById(shopEntity);

        //跟新店铺相应缓存
        ShopInfoDTO shopInfoDTO = ConvertUtils.sourceToTarget(shopEntity, ShopInfoDTO.class);
        merchantUtils.putShop(shopEntity.getId(), shopInfoDTO);

        //更新店铺token信息
        merchantUtils.saveTokenInfo(merchantByToken);
    }

    private Result shopBoundPickupPoint(PickupUpdateRequestDTO dto, List<CateringMerchantPickupAdressEntity> pickupShopRelation,
                                        CateringShopEntity pickupPoint) {
        //若无绑定关系
        if (!BaseUtil.judgeList(pickupShopRelation)) {
            CateringMerchantPickupAdressEntity shopPickupRelation = ConvertUtils.sourceToTarget(pickupPoint, CateringMerchantPickupAdressEntity.class);
            shopPickupRelation.setId(null);
            shopPickupRelation.setShopId(dto.getShopId());
            shopPickupRelation.setPickupId(pickupPoint.getId());
            shopPickupRelation.setChargerName(pickupPoint.getPrimaryPersonName());
            shopPickupRelation.setChargerPhone(pickupPoint.getRegisterPhone());
            shopPickupRelation.setName(pickupPoint.getShopName());

            shopPickupRelation.setShopStatus(pickupPoint.getShopStatus());
            shopPickupRelation.setIsDel(DelEnum.NOT_DELETE.getFlag());
            //添加店铺自提点绑定关系
            pickupAdressService.save(shopPickupRelation);
        } else if (pickupShopRelation.get(0).getIsDel()) {
            //若之前该自提点绑定过该店铺，恢复绑定关系
            pickupShopRelation.forEach(entity -> {
                entity.setIsDel(DelEnum.NOT_DELETE.getFlag());
            });
            pickupAdressService.updateBatchById(pickupShopRelation);
        } else {
            return Result.fail(501, "当前店铺已绑定该自提点");
        }
        Result<String> success = Result.succ();
        success.setMsg("绑定成功");
        return success;
    }

    /**
     * 门店业务配置-查询门店业务支持类型
     *
     * @param shopId
     * @param type   修改：业务支持：1：仅配送，2：仅自提，3：全部，仅查询：type为4
     * @return
     */
    @Override
    public Result<Integer> getShopSupportType(Long shopId, Integer type) {
        QueryWrapper<CateringShopConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopConfigEntity::getShopId, shopId);
        CateringShopConfigEntity shopConfig = shopConfigService.getOne(query);

        //若type不为4则修改门店业务支持类型
        if (!com.google.common.base.Objects.equal(type, BusinessSupportEnum.GAIN_SUPPORT_TYPE.getStatus())) {
            shopConfig.setBusinessSupport(type);
            shopConfigService.updateById(shopConfig);

            //更新缓存
            CateringShopEntity shopEntity = shopMapper.selectById(shopId);
            ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopEntity.getId());
            shopConfigInfo.setBusinessSupport(type);
            merchantUtils.putShopConfigInfo(shopEntity.getId(), shopConfigInfo);
        }
        return Result.succ(shopConfig.getBusinessSupport());
    }

    @Override
    public Result<ShopDTO> modifyShopSellType(Long shopId, Integer type) {
        CateringShopEntity shopEntity = shopMapper.selectById(shopId);
        if (ObjectUtils.isEmpty(shopEntity)) {
            throw new CustomException("店铺查询失败");
        }

        if (!Objects.equals(type, ShopSellTypeEnum.SHOP_SELL_TYPE.getStatus())) {
            //更新店铺信息及缓存
            ShopInfoDTO shop = merchantUtils.getShop(shopEntity.getMerchantId());
            shop.setSellType(type);
            merchantUtils.putShop(shopEntity.getId(), shop);

            shopEntity.setSellType(type);
            shopMapper.updateById(shopEntity);

            //发送消息
            MerchantSellTypeMqDTO sellTypeMqDTO = new MerchantSellTypeMqDTO();
            sellTypeMqDTO.setMerchantId(shopEntity.getMerchantId());
            sellTypeMqDTO.setSellType(shopEntity.getSellType());
            merchantSendMq.sendMerchantMq(sellTypeMqDTO);
        }

        ShopDTO shopDTO = BaseUtil.objToObj(shopEntity, ShopDTO.class);
        return Result.succ(shopDTO);
    }


    @Override
    public Result<String> wxAddressPicture(Long shopId) {
        CateringShopEntity shopEntity = getById(shopId);
        String picture = null;
        if (!ObjectUtils.isEmpty(shopEntity)) {
            picture = shopEntity.getAddressPicture();
        }
        return Result.succ(picture);
    }


    @Override
    public Result updatePasswordByOld(Long shopId, MerchantPasswordRequestDTO dto, String key, String iv) {
        //校验参数是否正确
        this.checkParameter(dto);
        //查询店铺信息
        QueryWrapper<CateringMerchantLoginAccountEntity> queryAccount = new QueryWrapper<>();
        queryAccount.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopId)
                .eq(CateringMerchantLoginAccountEntity::getIsDel, DelEnum.NOT_DELETE.getStatus());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountService.list(queryAccount);

        if (!BaseUtil.judgeList(loginAccountEntities)) {
            return Result.fail("当前登录店铺已被删除");
        }
        CateringMerchantLoginAccountEntity loginAccountEntity = loginAccountEntities.get(0);
        if (BaseUtil.judgeString(dto.getSmsCode())){
            dto.setPhone(loginAccountEntity.getPhone());
            return this.appMerchantUpdatePassword(dto,key,iv);
        }

        if (BaseUtil.judgeString(dto.getOldPassWord())){
            String desEncryptOld = AesEncryptUtil.desEncrypt(dto.getOldPassWord(), key, iv);
            String oldPassword = Md5Util.passwordEncrypt(desEncryptOld);

            if (!Objects.equals(oldPassword, loginAccountEntity.getPassword())) {
                return Result.fail("原密码输入错误");
            }
        }

        String desEncryptNew = AesEncryptUtil.desEncrypt(dto.getPassword(), key, iv);
        String newPassword = Md5Util.passwordEncrypt(desEncryptNew);

        if (Objects.equals(newPassword, loginAccountEntity.getPassword())) {
            return Result.fail("原密码与新密码相同");
        }

        loginAccountEntity.setPassword(newPassword);
        loginAccountService.updateById(loginAccountEntity);
//        String token = Md5Util.md5(oldPassword + loginAccountEntity.getAccountTypeId());
        //处理app端登录token
        merchantUtils.removeMerAppToken(loginAccountEntity.getAccountTypeId().toString());
//        MerchantTokenDTO merchantAppToken = merchantUtils.getMerchantByToken(String.valueOf(loginAccountEntity.getAccountTypeId()));
//        merchantAppToken.setLogBackType(LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
//        merchantUtils.saveAppMerchantToken(loginAccountEntity.getAccountTypeId(),merchantAppToken);

        //处理pc端登录token
        String merchantPcToken = merchantUtils.getMerchantPcToken(loginAccountEntity.getAccountTypeId().toString());
        if (BaseUtil.judgeString(merchantPcToken)){
            String tokenPc = TokenUtil.generatePcToken(loginAccountEntity.getAccountTypeId(),loginAccountEntity.getAccountType(),LogBackTypeEnum.CHANGE_PASSWORD.getStatus());
            merchantUtils.putMerchantPcToken(loginAccountEntity.getAccountTypeId().toString(),tokenPc,Boolean.TRUE);
        }

        if (Objects.equals(loginAccountEntity.getAccountType(), AccountTypeEnum.SHOP.getStatus())
                || Objects.equals(loginAccountEntity.getAccountType(), AccountTypeEnum.SHOP_PICKUP.getStatus())
                || Objects.equals(loginAccountEntity.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())) {
            shopPrintingConfigService.delShopPrintingConfig(loginAccountEntity.getAccountTypeId());
        }
        return Result.succ();
    }

    /**
     * 方法描述 : 校验修改密码参数是否正确
     * @Author: MeiTao
     * @Date: 2020/10/18 0018 13:06
     * @param dto 请求参数
     * @return: void
     * @Since version-1.5.0
     */
    private void checkParameter(MerchantPasswordRequestDTO dto) {
        if (ObjectUtils.isEmpty(dto.getSmsCode())&&ObjectUtils.isEmpty(dto.getOldPassWord())){
            log.error("传入参数 : " + dto.toString());
            throw new CustomException("传入参数不对");
        }
    }

    @Override
    public void updateMerchantInfoSendMq(ShopDTO shopDTO) {
        merchantSendMq.sendMerchantInfoMq(shopDTO);
    }

    @Override
    public List<ShopAddressPictureDTO> handelAddressPicture(List<String> list) {
        List<ShopAddressPictureDTO> result = new ArrayList<>();
        if (BaseUtil.judgeList(list)) {
            list.forEach(urlString -> {
                ShopAddressPictureDTO shopAddressPictureDTO = new ShopAddressPictureDTO();
                Map<String, String> map = urlSplit(urlString);
                shopAddressPictureDTO.setH(map.get("h"));
                shopAddressPictureDTO.setW(map.get("w"));
                shopAddressPictureDTO.setUrl(urlString);
                result.add(shopAddressPictureDTO);
            });
        }
        return result;
    }

    /**
     * 方法描述 : 解析出url参数中的键值对
     *
     * @param url url地址
     * @Author: lzf
     * @Date: 2020/6/4 0004 16:05
     * @return: Map<String, String>  url请求参数部分,若参数为空返回空对象
     * @Since version-1.0.0
     */
    public Map<String, String> urlSplit(String url) {
        Map<String, String> mapRequest = new HashMap<>(4);
        String[] arrSplit = null;
        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (!"".equals(arrSplitEqual[0])) {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 方法描述 : 去掉url中的路径，留下请求参数部分
     * 若请求中没有参数则返回null
     *
     * @param strUrl
     * @Author: lzf
     * @Date: 2020/6/4 0004 16:02
     * @return: java.lang.String url请求参数部分
     * @Since version-1.0.0
     */
    private static String truncateUrlPage(String strUrl) {
        String strAllParam = null;
        String[] arrSplit = null;
        strUrl = strUrl.trim().toLowerCase();
        arrSplit = strUrl.split("[?]");
        if (strUrl.length() > 1) {
            if (arrSplit.length > 1) {
                for (int i = 1; i < arrSplit.length; i++) {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }

    @Override
    public String getShopCode() {
        String shopCode = merchantUtils.shopCode(null);

        if (BaseUtil.judgeString(shopCode)) {
            return shopCode;
        }

        //获取数据库中当前商户编码最大值
        Integer shopCodeMax = shopMapper.getShopCodeMax();

        if (ObjectUtils.isEmpty(shopCodeMax)) {
            shopCodeMax = 0;
        }

        shopCodeMax += 1;

        return merchantUtils.shopCode(shopCodeMax);
    }

    @Override
    public CateringShopEntity modifyShopBusinessStatus(CateringShopEntity shop) {
        if (Objects.equals(shop.getBusinessStatus(), BusinessStatusEnum.CLOSE.getStatus())) {
            shop.setBusinessStatus(OPEN.getStatus());
        } else {
            shop.setBusinessStatus(CLOSE.getStatus());
        }
        //1、更新店铺/自提点信息
        shopMapper.updateById(shop);

        //2、若为店铺则更新对应缓存
        if (!Objects.equals(shop.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus())) {
            ShopInfoDTO shopInfoDTO = ConvertUtils.sourceToTarget(shop, ShopInfoDTO.class);
            merchantUtils.putShop(shop.getId(), shopInfoDTO);
        }

        return shop;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopDTO> updateShopStatus(StatusUpdateDTO dto) {
        CateringShopEntity shopEntity = shopMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(shopEntity)) {
            throw new CustomException("店铺查询失败");
        }

        if (!Objects.equals(shopEntity.getShopStatus(), dto.getStatus())) {
            shopEntity.setShopStatus(dto.getStatus());
            shopMapper.updateById(shopEntity);
            UpdateWrapper<CateringMerchantLoginAccountEntity> updateQuery = new UpdateWrapper<>();
            updateQuery.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, shopEntity.getId());
            updateQuery.lambda().set(CateringMerchantLoginAccountEntity::getAccountStatus, shopEntity.getShopStatus());
            loginAccountService.update(updateQuery);
            //若为店铺则更新店铺缓存
            if (!Objects.equals(shopEntity.getShopType(), ShopTypeEnum.BUSINESS_POINT.getStatus())) {
                ShopInfoDTO shop = merchantUtils.getShop(shopEntity.getId());
                shop.setShopStatus(dto.getStatus());
                merchantUtils.putShop(shop.getId(), shop);
            }
            //更新店铺自提点关联信息
            pickupAdressService.updateShopStatus(BaseUtil.objToObj(shopEntity, ShopDTO.class));
        }

        return Result.succ(BaseUtil.objToObj(shopEntity, ShopDTO.class));
    }

    @Override
    public Result<Boolean> isWxPlaceOrder(Long shopId) {
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
        Result wxShow = this.isWxShow(shop, merchant);

        if (wxShow.failure()) {
            return wxShow;
        }

        if (Objects.equals(shop.getBusinessStatus(), BusinessStatusEnum.CLOSE.getStatus())) {
            return Result.fail(ErrorCode.SHOP_BUSINESS_STATUS_ERROR, ErrorCode.SHOP_BUSINESS_STATUS_MSG);
        }

        return Result.succ();
    }

    @Override
    public Result isWxShow(Long shopId) {
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        if (shop == null) {
            CateringShopEntity shopEntity = this.getById(shopId);
            if (shopEntity == null) {
                return Result.fail(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
            }
            shop = ConvertUtils.sourceToTarget(shopEntity, ShopInfoDTO.class);
        }
        MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
        return this.isWxShow(shop, merchant);
    }


    /**
     * 方法描述 : 微信是否可在小程序端展示
     *
     * @param shop     店铺信息
     * @param merchant 商户信息
     * @Author: MeiTao
     * @Date: 2020/7/11 0011 9:46
     * @return: code 不为0 则不可在小程序端展示
     * @Since version-1.2.0
     */
    private Result isWxShow(ShopInfoDTO shop, MerchantInfoDTO merchant) {
        if (ObjectUtils.isEmpty(shop)) {
            return Result.fail(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
        }

        if (Objects.equals(shop.getShopStatus(), StatusEnum.ENABLE_NOT.getStatus())) {
            return Result.fail(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
        }

        if (Objects.equals(shop.getShopServiceType(), ServiceTypeEnum.TS.getStatus()) ||
                Objects.equals(shop.getShopServiceType(), ServiceTypeEnum.ALL_NOT.getStatus())) {
            return Result.fail(ErrorCode.SHOP_SERVICE_ERROR, ErrorCode.SHOP_SERVICE_MSG);
        }

        if (ObjectUtils.isEmpty(merchant)) {
            return Result.fail(ErrorCode.MERCHANT_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }

        //4、商户禁用不可下单
        if (Objects.equals(merchant.getMerchantStatus(), StatusEnum.ENABLE_NOT.getStatus())) {
            return Result.fail(ErrorCode.MERCHANT_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }

        //5、审核未通过不可下单
        if (!Objects.equals(merchant.getAuditStatus(), AuditStatusEnum.PASS.getStatus())) {
            return Result.fail(ErrorCode.MERCHANT_AUDIT_STATUS_ERROR, ErrorCode.MERCHANT_AUDIT_STATUS_MSG);
        }
        return Result.succ();
    }


    @Override
    public Result<Map<String, List<ShopCityDTO>>> listShopCity(ShopCityQueryDTO dto) {
        List<ShopCityDTO> shopCityList = shopMapper.listShopCity(dto);

        Map<String, List<ShopCityDTO>> map = new LinkedHashMap(50);
        if (!BaseUtil.judgeList(shopCityList)) {
            return Result.succ(map);
        }

        shopCityList.forEach(shopCity -> {
            ShopCityInfoDTO shopCityCache = merchantUtils.getShopCity(shopCity.getAddressCityCode());

            //若缓存中不存在则重新获取对应经纬度
            if (ObjectUtils.isEmpty(shopCityCache)) {
                String address = shopCity.getAddressProvince() + shopCity.getAddressCity();
                Location coordinate = null;
                try {
                    coordinate = LatitudeUtils.getCoordinate(address);
                } catch (IOException e) {
                    log.error("wx首页搜索获取店铺对应城市市中心经纬度失败！城市地址 : " + address, e);
                }
                double lat = new Double(coordinate.getLat()), lng = new Double(coordinate.getLng());
                double[] doubles = GpsCoordinateUtils.calBD09toGCJ02(lat, lng);
                String strCoordinate = doubles[1] + "," + doubles[0];
                shopCityCache = BaseUtil.objToObj(shopCity, ShopCityInfoDTO.class);
                shopCityCache.setMapCoordinate(strCoordinate);
                merchantUtils.putShopCity(shopCityCache);
            }
            shopCityCache.setMapCoordinate(shopCityCache.getMapCoordinate());

            String addressCity = shopCity.getAddressCity();
            String cityFirstWord = String.valueOf(HypyUtil.cn2py(addressCity).charAt(0));

            List<ShopCityDTO> shopCityListMap = map.get(cityFirstWord);

            if (!BaseUtil.judgeList(shopCityListMap)) {
                shopCityListMap = new ArrayList<>();
            }

            shopCityListMap.add(BaseUtil.objToObj(shopCityCache, ShopCityDTO.class));
            map.put(cityFirstWord, shopCityListMap);
        });

        return Result.succ(map);
    }

    @Override
    public Result<List<ShopCityDTO>> listShopCity() {
        return Result.succ(shopMapper.listShopCity(new ShopCityQueryDTO()));
    }

    @Override
    public Result<List<ShopDTO>> listShopByIds(List<Long> shopIds) {
        LambdaQueryWrapper<CateringShopEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringShopEntity::getId, shopIds);
        List<CateringShopEntity> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Result.succ();
        }
        List<ShopDTO> shopList = ConvertUtils.sourceToTarget(list, ShopDTO.class);
        return Result.succ(shopList);
    }

    @Override
    public Result<List<GoodPushShopVo>> listShopPull(Long merchantId) {
        LambdaQueryWrapper<CateringShopEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus())
                .eq(CateringShopEntity::getMerchantId, merchantId);
        List<CateringShopEntity> list = this.list(queryWrapper);
        return Result.succ(this.handleShopPull(list));
    }

    @Override
    public Result<List<GoodPushShopVo>> listShopReportPull(Long merchantId) {
        LambdaQueryWrapper<CateringShopEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringShopEntity::getMerchantId, merchantId)
                .ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus());
        List<CateringShopEntity> list = this.list(queryWrapper);
        return Result.succ(this.handleShopPull(list));
    }

    /**
     * 方法描述 : 门店相关下拉返回数据处理
     *
     * @param list
     * @Author: MeiTao
     * @Date: 2020/9/7 0007 16:40
     * @return: java.util.List<GoodPushShopVo>
     * @Since version-1.4.0
     */
    private List<GoodPushShopVo> handleShopPull(List<CateringShopEntity> list) {
        if (!BaseUtil.judgeList(list)) {
            return null;
        }

        List<GoodPushShopVo> goodPushShop = new ArrayList<>();
        list.forEach(shop -> {
            GoodPushShopVo goodPushShopVo = BaseUtil.objToObj(shop, GoodPushShopVo.class);
            goodPushShopVo.setShopId(shop.getId());
            goodPushShop.add(goodPushShopVo);
        });
        return goodPushShop;
    }

    @Override
    public Result delShop(ShopDelDTO dto) {
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getId, dto.getShopId())
                .eq(CateringShopEntity::getMerchantId, dto.getMerchantId());
        List<CateringShopEntity> shopEntities = shopMapper.selectList(query);

        if (!BaseUtil.judgeList(shopEntities)) {
            throw new CustomException("店铺信息查询失败");
        }
        CateringShopEntity shopEntity = shopEntities.get(0);

        if (shopEntity.getDel()) {
            throw new CustomException("店铺已删除");
        }
        shopEntity.setDel(DelEnum.DELETE.getFlag());
        //门店信息删除
        shopMapper.updateById(shopEntity);

        UpdateWrapper<CateringShopConfigEntity> updateQuery = new UpdateWrapper<>();
        updateQuery.lambda().eq(CateringShopConfigEntity::getShopId, shopEntity.getId());
        updateQuery.lambda().set(CateringShopConfigEntity::getDel, DelEnum.DELETE.getStatus());
        //门店配置信息删除
        shopConfigService.update(updateQuery);

        //删除门店自提点绑定关联关系
        UpdateWrapper<CateringMerchantPickupAdressEntity> pickupAddressQuery = new UpdateWrapper<>();
        pickupAddressQuery.lambda().eq(CateringMerchantPickupAdressEntity::getPickupId, dto.getShopId())
                .or(wra -> wra.eq(CateringMerchantPickupAdressEntity::getShopId, dto.getShopId()));
        pickupAddressQuery.lambda().set(CateringMerchantPickupAdressEntity::getIsDel, Boolean.TRUE);
        pickupAdressService.update(pickupAddressQuery);

        QueryWrapper<CateringMerchantLoginAccountEntity> queryAccount = new QueryWrapper<>();
        queryAccount.lambda().eq(CateringMerchantLoginAccountEntity::getAccountTypeId, dto.getShopId());
        List<CateringMerchantLoginAccountEntity> loginAccountEntities = loginAccountService.list(queryAccount);

        if (!BaseUtil.judgeList(loginAccountEntities)) {
            throw new CustomException("登陆账号信息查询失败");
        }
        CateringMerchantLoginAccountEntity accountEntity = loginAccountEntities.get(0);

        accountEntity.setIsDel(DelEnum.DELETE.getFlag());
        //删除门店登陆账号
        loginAccountService.updateById(accountEntity);

        //清除app端门店登陆token
        String token = Md5Util.md5(accountEntity.getPassword() + accountEntity.getAccountTypeId());
        merchantUtils.removeMerAppToken(token);

        //清除pc端门店登陆token
        merchantUtils.removeMerchantPcToken(accountEntity.getAccountTypeId().toString());

        //清除门店缓存中的信息
        merchantUtils.removeShop(accountEntity.getAccountTypeId());
        merchantUtils.removeShopConfigInfo(accountEntity.getAccountTypeId());
        return Result.succ();
    }

    @Override
    public Result<PageData<GoodPushShopVo>> shopMarketPage(ShopMarketPagDTO dto) {
        GoodPushShopPagDTO pageDto = BaseUtil.objToObj(dto, GoodPushShopPagDTO.class);
        if (!BaseUtil.judgeList(pageDto.getShopIds())) {
            pageDto.setShopIds(new ArrayList<>());
        }
        return this.goodPushShopPage(pageDto);
    }

    @Override
    public List<ShopDTO> listShopByCondition(ShopQueryDTO dto) {
        List<ShopDTO> shopList = new ArrayList<>();
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(!ObjectUtils.isEmpty(dto.getMerchantId()), CateringShopEntity::getMerchantId, dto.getMerchantId())
                .eq(!ObjectUtils.isEmpty(dto.getShopStatus()), CateringShopEntity::getShopStatus, dto.getShopStatus())
                .eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getStatus())
                .eq(BaseUtil.judgeString(dto.getAddressProvinceCode()), CateringShopEntity::getAddressProvinceCode, dto.getAddressProvinceCode())
                .eq(BaseUtil.judgeString(dto.getAddressCityCode()), CateringShopEntity::getAddressCityCode, dto.getAddressCityCode())
                .eq(BaseUtil.judgeString(dto.getAddressAreaCode()), CateringShopEntity::getAddressAreaCode, dto.getAddressAreaCode());
        List<CateringShopEntity> shopEntities = shopMapper.selectList(query);
        if (BaseUtil.judgeList(shopEntities)) {
            shopList = BaseUtil.objToObj(shopEntities, ShopDTO.class);
        }
        return shopList;
    }

    @Override
    public List<CateringShopEntity> listDistance12(double lat, double lng) {

        return shopMapper.listDistance12(lat, lng);
    }

    @Override
    public Result<ShopDTO> getByPhone(String phone) {
        if (!BaseUtil.judgeString(phone)) {
            return Result.succ();
        }
        QueryWrapper<CateringShopEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .ne(CateringShopEntity::getShopType, ShopTypeEnum.BUSINESS_POINT.getStatus())
                .eq(CateringShopEntity::getRegisterPhone, phone);
        List<CateringShopEntity> shopEntities = shopMapper.selectList(query);

        if (!BaseUtil.judgeList(shopEntities)) {
            return Result.succ();
        }
        return Result.succ(BaseUtil.objToObj(shopEntities.get(0), ShopDTO.class));
    }

    @Override
    public Result<List<Long>> listShopIdByCity(ShopQueryDTO dto) {

        return Result.succ(shopMapper.listShopIdByCity(dto.getAddressCityCode()));
    }

    @Override
    public Result<List<MerchantShopInfoDTO>> reportShopQuery(ShopQueryDTO dto) {
        return Result.succ(shopMapper.reportShopQuery(dto));
    }

    @Override
    public PageData<ShopChoicePageVO> queryPageChoiceShop(ShopChoicePageDTO dto) {
        return new PageData<>(this.baseMapper.queryPageChoiceShop(dto.getPage(), dto));
    }


    @Override
    public void syncShopInfoToDaDa(Long shopId, String shopName, String cityName, String areaName, String address, BigDecimal lat, BigDecimal lng, String shopContacts, String shopMobile) {
        DadaApiResponse dadaApiResponse = dadaUtils.addOrUpdateShop(shopId, shopName, cityName, areaName, address, lat, lng, shopContacts, shopMobile);
        if (dadaApiResponse.getCode() != 0) {
            // 门店信息上报到达达失败
            log.error("门店达达信息同步失败，返回结果 ： " + JSONObject.toJSONString(dadaApiResponse));
            int code = 200;
            JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(dadaApiResponse.getResult()));
            if(jsonObject != null){
                JSONArray failedListArray = jsonObject.getJSONArray("failedList");
                if(failedListArray != null){
                    List<JSONObject> failedList = failedListArray.toJavaList(JSONObject.class);
                    if(failedList.size() > 0){
                        JSONObject failed = failedList.get(0);
                        if(failed != null ){
                            code = failed.getInteger("code");
                        }
                    }
                }else {
                    code = dadaApiResponse.getCode();
                }
            }else {
                code = dadaApiResponse.getCode();
            }
            throw new CustomException(DadaErrEnum.parse(code).getDesc());
        }
    }

    @Override
    public void batchSyncShopInfoToDaDa(List<Long> shopIdList) {
        if (CollectionUtils.isEmpty(shopIdList)) {
            return;
        }
        List<ShopDTO> shopList = listShopByIds(shopIdList).getData();
        if (CollectionUtils.isEmpty(shopList)) {
            return;
        }
        for (ShopDTO shop : shopList) {
            String mapCoordinate = shop.getMapCoordinate();
            // 百度系地图坐标经纬度
            String[] mapCoordanateArr = mapCoordinate.split(",");
            // 高德系地图坐标经纬度（达达支持等是高德系）
            double[] mapGc = GpsCoordinateUtils.bd09_To_Gcj02(Double.valueOf(mapCoordanateArr[0]).doubleValue(), Double.valueOf(mapCoordanateArr[1]).doubleValue());
            syncShopInfoToDaDa(
                    shop.getShopId(),
                    shop.getShopName(),
                    shop.getAddressCity(),
                    shop.getAddressArea(),
                    shop.getAddressDetail(),
                    BigDecimal.valueOf(mapGc[0]),
                    BigDecimal.valueOf(mapGc[1]),
                    shop.getPrimaryPersonName(),
                    shop.getRegisterPhone());
        }
    }

    @Override
    public Result handleShopDataV5() {
        QueryWrapper<CateringShopEntity> queryShop = new QueryWrapper<>();
        queryShop.lambda().eq(CateringShopEntity::getDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringShopEntity> shopEntities = shopMapper.selectList(queryShop);
        List<CateringShopExtEntity> shopExtList = new ArrayList<>();
        if (BaseUtil.judgeList(shopEntities)){
            shopEntities.forEach(shopEntity -> {
                if (ObjectUtils.isEmpty(shopEntity.getDeliveryType())||Objects.equals(shopEntity.getDeliveryType(),0)||Objects.equals(shopEntity.getDeliveryType(),1)){
                    shopEntity.setDeliveryType(2);
                    try{
                        this.batchSyncShopInfoToDaDa(Arrays.asList(shopEntity.getId()));
                    }catch (Exception e){
                        log.error("店铺添加达达配送失败，店铺 ： " + shopEntity.toString(),e);
                        shopEntity.setDeliveryType(1);
                    }
                }
                QueryWrapper<CateringShopExtEntity> query = new QueryWrapper<>();
                query.lambda().eq(CateringShopExtEntity::getShopId,shopEntity.getId());
                List<CateringShopExtEntity> list = shopExtService.list(query);
                if (!BaseUtil.judgeList(list)){
                    CateringShopExtEntity shopExtEntity = new CateringShopExtEntity();
                    shopExtEntity.setShopId(shopEntity.getId());
                    shopExtEntity.setSignStatus(2);
                    shopExtEntity.setBusinessLicense(shopEntity.getBusinessLicense());
                    shopExtEntity.setFoodBusinessLicense(shopEntity.getFoodBusinessLicense());
                    shopExtEntity.setIdCardPositive(shopEntity.getIdCardPositive());
                    shopExtEntity.setIdCardBack(shopEntity.getIdCardBack());
                    shopExtEntity.setAuditStatus(1);
                    shopExtEntity.setDel(false);
                    shopExtEntity.setCreateTime(LocalDateTime.now());
                    shopExtEntity.setUpdateTime(LocalDateTime.now());
                    shopExtList.add(shopExtEntity);
                }
            });
            this.updateBatchById(shopEntities);
        }
        if (BaseUtil.judgeList(shopExtList)){
            shopExtService.saveBatch(shopExtList);
        }
        return Result.succ();
    }

    @Override
    public CateringShopEmployeeEntity getEmployeeById(Long accountTypeId) {
        return this.baseMapper.getEmployeeById(accountTypeId);
    }

    @Override
    public void handleAppTokenV5() {
        UpdateWrapper<CateringShopPrintingConfigEntity> query = new UpdateWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getDel,DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopPrintingConfigEntity::getStatus, StatusEnum.ENABLE.getStatus());
        query.lambda().set(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE_NOT.getStatus());
        shopPrintingConfigService.update(query);

        UpdateWrapper<CateringShopOrderNoticeEntity> queryOrderNotice = new UpdateWrapper<>();
        queryOrderNotice.lambda().eq(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.FALSE);
        queryOrderNotice.lambda().set(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.TRUE);
        shopOrderNoticeService.update(queryOrderNotice);
    }
}
