package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.base.merchant.DeviceNoticeInfoDTO;
import com.meiyuan.catering.core.dto.base.merchant.ShopNoticeInfoDTO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.DeviceTypeEnum;
import com.meiyuan.catering.core.enums.base.merchant.TicketTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.YlyService;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dao.CateringShopPrintingConfigMapper;
import com.meiyuan.catering.merchant.dto.shop.config.*;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopOrderNoticeEntity;
import com.meiyuan.catering.merchant.entity.CateringShopPrintingConfigEntity;
import com.meiyuan.catering.merchant.service.CateringShopEmployeeService;
import com.meiyuan.catering.merchant.service.CateringShopOrderNoticeService;
import com.meiyuan.catering.merchant.service.CateringShopPrintingConfigService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.config.ShopPrintingConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author MeiTao
 * @Date 2020/9/3 0003 15:17
 * @Description 简单描述 :
 * @Since version-1.3.0
 */
@Service
@Slf4j
public class CateringShopPrintingConfigServiceImpl extends ServiceImpl<CateringShopPrintingConfigMapper, CateringShopPrintingConfigEntity>
        implements CateringShopPrintingConfigService {

    @Resource
    CateringShopPrintingConfigMapper shopPrintingConfigMapper;
    @Resource
    CateringShopOrderNoticeService shopOrderNoticeService;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    YlyUtils ylyUtils;
    @Resource
    YlyService ylyService;
    @Resource
    CateringShopEmployeeService shopEmployeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopPrintingConfigDTO saveShopPrintingConfig(ShopPrintingConfigDTO dto) {
        //查询当前设备是否绑定过其他店铺
        ShopPrintingConfigDTO printingConfigDto = this.listShopPrintingConfig(dto.getDeviceNumber());

        ShopNoticeInfoDTO shopNoticeInfo = null;
        //1、若当前设备登陆过其他门店
        if (!ObjectUtils.isEmpty(printingConfigDto)){
            //1.1 登录同一家门店
            if (Objects.equals(printingConfigDto.getShopId(),dto.getShopId())){
                CateringShopPrintingConfigEntity shopPrintingConfigEntity = BaseUtil.objToObj(printingConfigDto, CateringShopPrintingConfigEntity.class);
                shopPrintingConfigEntity.setStatus(StatusEnum.ENABLE.getStatus());
                baseMapper.updateById(shopPrintingConfigEntity);
                //登陆时清空登录前记录订单信息
                this.delShopOrderNoticeInfo(dto.getDeviceNumber(),dto.getShopId());
                //移除登录前的设备未通知订单信息
                merchantUtils.removeNoticeDeviceInfo(dto.getDeviceNumber(),dto.getShopId());
                shopNoticeInfo = BaseUtil.objToObj(shopPrintingConfigEntity, ShopNoticeInfoDTO.class);
            }
            //1.2 若设备更换登陆门店初始化打印配置信息
            if (!Objects.equals(printingConfigDto.getShopId(),dto.getShopId())){
                CateringShopPrintingConfigEntity entity = getDefaultShopPrintingConfig(printingConfigDto);
                entity.setShopId(dto.getShopId());
                boolean save = this.saveOrUpdate(entity);
                //登陆时清空登录前记录订单信息
                this.delShopOrderNoticeInfo(dto.getDeviceNumber() , printingConfigDto.getShopId());
                shopNoticeInfo = BaseUtil.objToObj(entity, ShopNoticeInfoDTO.class);
                merchantUtils.removeNoticeDeviceInfo(dto.getDeviceNumber() , printingConfigDto.getShopId());
                merchantUtils.removeShopOrderNotice(dto.getDeviceNumber()+printingConfigDto.getShopId());
            }
        }

        //2、若当前设备未登录过任何门店
        if (ObjectUtils.isEmpty(printingConfigDto)){
            CateringShopPrintingConfigEntity entity = getDefaultShopPrintingConfig(dto);
            boolean save = this.saveOrUpdate(entity);
            shopNoticeInfo = BaseUtil.objToObj(entity, ShopNoticeInfoDTO.class);
        }

        //3、设备登陆信息入缓存
        merchantUtils.putShopOrderNotice(dto.getDeviceNumber() + dto.getShopId(),shopNoticeInfo);
        return printingConfigDto;
    }

    @Override
    public ShopPrintingConfigDTO listShopPrintingConfig(String deviceNumber) {
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getDel,Boolean.FALSE)
                .eq(CateringShopPrintingConfigEntity::getDeviceNumber,deviceNumber);
        List<CateringShopPrintingConfigEntity> entityList = shopPrintingConfigMapper.selectList(query);

        if (BaseUtil.judgeList(entityList)){
            return BaseUtil.objToObj(entityList.get(0),ShopPrintingConfigDTO.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveShopOrderNotice(OrderNoticeInfoDTO dto) {
        List<Long> shopIds = new ArrayList<>();
        shopIds.add(dto.getShopId());
        if (!ObjectUtils.isEmpty(dto.getPickupId())){
            shopIds.add(dto.getPickupId());
        }
        if (ObjectUtils.isEmpty(dto.getOrderId())){
            log.error("新订单传入id为空，shopId : " + dto.getShopId());
            return Boolean.TRUE;
        }

        //查询当前店铺需要通知的设备号
        List<CateringShopPrintingConfigEntity> shopPrintingConfigEntities = this.listShopPrintingEntity(dto.getShopId());
        if (!BaseUtil.judgeList(shopPrintingConfigEntities)){
            log.debug("当前店铺无任何需要通知的设备，shopId : " + dto.getShopId() + "；订单id ： " + dto.getOrderId());
            return Boolean.TRUE;
        }

        //店铺需要通知的设备信息
        List<CateringShopOrderNoticeEntity> entityList = new ArrayList<>();
        shopPrintingConfigEntities.forEach(entity->{
            CateringShopOrderNoticeEntity shopOrderNoticeEntity = new CateringShopOrderNoticeEntity();
            shopOrderNoticeEntity.setShopId(entity.getShopId());
            shopOrderNoticeEntity.setDeviceNumber(entity.getDeviceNumber());
            shopOrderNoticeEntity.setOrderId(dto.getOrderId());
            shopOrderNoticeEntity.setNoticeSucc(Boolean.FALSE);
            entityList.add(shopOrderNoticeEntity);
        });
        shopOrderNoticeService.saveBatch(entityList);

        //通知信息入缓存
        entityList.forEach(entity->{
            merchantUtils.putNoticeDeviceInfo(entity.getDeviceNumber(),entity.getShopId(),Arrays.asList(dto.getOrderId()));
        });
        return Boolean.TRUE;
    }

    private List<CateringShopPrintingConfigEntity> listShopPrintingEntity(Long shopId) {
        QueryWrapper<CateringShopEmployeeEntity> employeeQuery = new QueryWrapper<>();
        employeeQuery.lambda().eq(CateringShopEmployeeEntity::getShopId,shopId)
                .eq(CateringShopEmployeeEntity::getStatus,1)
                .eq(CateringShopEmployeeEntity::getIsDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringShopEmployeeEntity> listEmployee = shopEmployeeService.list(employeeQuery);
        List<Long> shopIdList = new ArrayList<>();
        if (BaseUtil.judgeList(listEmployee)){
            listEmployee.forEach(e->shopIdList.add(e.getId()));
        }
        shopIdList.add(shopId);

        //查询当前店铺需要通知的设备号
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getDel,DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE.getStatus())
                .ne(CateringShopPrintingConfigEntity::getDeviceType,4)
                .in(CateringShopPrintingConfigEntity::getShopId,shopIdList)
                .groupBy(CateringShopPrintingConfigEntity::getShopId);
        List<CateringShopPrintingConfigEntity> shopPrintingConfigEntities = shopPrintingConfigMapper.selectList(query);
        return shopPrintingConfigEntities;
    }

    @Override
    public Boolean saveShopOrderNotice(Long shopId, List<Long> orderIds) {
        if (!BaseUtil.judgeList(orderIds)){
            log.error("团购活动订单id为空，shopId : " + shopId + ",不进行通知" );
            return Boolean.TRUE;
        }
        //查询当前店铺需要通知的设备号
        List<CateringShopPrintingConfigEntity> shopPrintingConfigEntities = this.listShopPrintingEntity(shopId);

        if (!BaseUtil.judgeList(shopPrintingConfigEntities)){
            log.debug("当前店铺无任何需要通知的设备，shopId : " + shopId + "；订单id ： " + orderIds);
            return Boolean.TRUE;
        }

        shopPrintingConfigEntities.forEach(entity->{
            List<CateringShopOrderNoticeEntity> entityList = new ArrayList<>();
            orderIds.forEach(orderId->{
                CateringShopOrderNoticeEntity shopOrderNoticeEntity = new CateringShopOrderNoticeEntity();
                shopOrderNoticeEntity.setShopId(entity.getShopId());
                shopOrderNoticeEntity.setDeviceNumber(entity.getDeviceNumber());
                shopOrderNoticeEntity.setOrderId(orderId);
                shopOrderNoticeEntity.setNoticeSucc(Boolean.FALSE);
                entityList.add(shopOrderNoticeEntity);
            });
            //保存新订单信息
            shopOrderNoticeService.saveBatch(entityList);
            //新订单信息入缓存
            merchantUtils.putNoticeDeviceInfo(entity.getDeviceNumber(),entity.getShopId(),orderIds);
        });
        return Boolean.TRUE;
    }

    @Override
    public DeviceNoticeInfoDTO getDeviceNotice(ShopPrintingConfigDTO dto,Long accountTypeId) {
        dto.setShopId(accountTypeId);
        String cacheKey = dto.getDeviceNumber() + dto.getShopId();
        ShopNoticeInfoDTO shopOrderNotice = merchantUtils.getShopOrderNotice(cacheKey);

        if (ObjectUtils.isEmpty(shopOrderNotice)){
            //查询当前设备是否绑定过其他店铺
            QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
            query.lambda().eq(CateringShopPrintingConfigEntity::getDel,Boolean.FALSE)
                    .eq(CateringShopPrintingConfigEntity::getShopId,dto.getShopId())
                    .eq(CateringShopPrintingConfigEntity::getDeviceNumber,dto.getDeviceNumber());
            List<CateringShopPrintingConfigEntity> entityList = shopPrintingConfigMapper.selectList(query);

            if (BaseUtil.judgeList(entityList)){
                shopOrderNotice = BaseUtil.objToObj(entityList.get(0), ShopNoticeInfoDTO.class);
            }else {
                log.error("设备绑定信息查询失败,店铺id : " + dto.getShopId() + ";设备号 ： " + dto.getDeviceNumber());
                CateringShopPrintingConfigEntity defaultEntity = getDefaultShopPrintingConfig(dto);
                this.saveOrUpdate(defaultEntity);
                shopOrderNotice = BaseUtil.objToObj(defaultEntity, ShopNoticeInfoDTO.class);
            }
            merchantUtils.putShopOrderNotice(cacheKey,shopOrderNotice);
            return BaseUtil.objToObj(shopOrderNotice, DeviceNoticeInfoDTO.class);
        }

        DeviceNoticeInfoDTO result = BaseUtil.objToObj(shopOrderNotice, DeviceNoticeInfoDTO.class);
        List<Long> orderIds = merchantUtils.getNoticeDeviceInfo(dto.getDeviceNumber() ,dto.getShopId());
        if (BaseUtil.judgeList(orderIds)){
            result.setOrderIds(orderIds);
            //2、移除设备已有订单信息
            this.delShopOrderNoticeInfo(dto.getDeviceNumber(),dto.getShopId(),orderIds);
            shopOrderNotice.setOrderIds(null);
            merchantUtils.putShopOrderNotice(dto.getDeviceNumber() + dto.getShopId(),shopOrderNotice);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopPrintingConfigVO> updateDevicePrintingConfig(DeviceConfigUpdateDTO dto) {
        CateringShopPrintingConfigEntity entity = shopPrintingConfigMapper.selectById(dto.getId());
        this.setDeviceUpdateInfo(dto,entity);
        shopPrintingConfigMapper.updateById(entity);

        ShopNoticeInfoDTO noticeInfo = BaseUtil.objToObj(entity, ShopNoticeInfoDTO.class);
        merchantUtils.putShopOrderNotice(entity.getDeviceNumber() + entity.getShopId(),noticeInfo);
        return Result.succ(BaseUtil.objToObj(entity,ShopPrintingConfigVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delShopPrintingConfig(Long shopId){
        //1、通过门店id查询门店绑定的所有登录设备
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE.getStatus())
                .eq(CateringShopPrintingConfigEntity::getDel,DelEnum.NOT_DELETE.getFlag())
                .eq(CateringShopPrintingConfigEntity::getShopId,shopId);
        List<CateringShopPrintingConfigEntity> shopPrintingConfigEntities = shopPrintingConfigMapper.selectList(query);

        if (!BaseUtil.judgeList(shopPrintingConfigEntities)){
            log.debug("当前门店无任何设备绑定,门店id : " + shopId);
            return;
        }
        //2、清除缓存信息、数据库信息(配置信息、尚未通知的订单信息)
        UpdateWrapper<CateringShopPrintingConfigEntity> updateQuery = new UpdateWrapper();
        updateQuery.lambda().eq(CateringShopPrintingConfigEntity::getShopId,shopId);
        updateQuery.lambda().set(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE_NOT.getStatus());
        this.update(updateQuery);

        //2.2、移除设备已有订单信息
        UpdateWrapper<CateringShopOrderNoticeEntity> updateOrderNoticeQuery = new UpdateWrapper();
        updateOrderNoticeQuery.lambda().eq(CateringShopOrderNoticeEntity::getShopId,shopId)
                .eq(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.FALSE);
        updateOrderNoticeQuery.lambda().set(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.TRUE);
        this.shopOrderNoticeService.update(updateOrderNoticeQuery);

        List<String> deviceNumbers = shopPrintingConfigEntities.stream().map(CateringShopPrintingConfigEntity::getDeviceNumber).collect(Collectors.toList());
        //2.3、删除缓存中绑定关系
        deviceNumbers.forEach(deviceNumber->{
            merchantUtils.removeShopOrderNotice(deviceNumber + shopId);
            merchantUtils.removeNoticeDeviceInfo(deviceNumber ,shopId);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delDevicePrintingConfig(ShopPrintingConfigDTO dto) {
        //1、关闭设备对应打印、新订单通知相关配置信息
        UpdateWrapper<CateringShopPrintingConfigEntity> updateQuery = new UpdateWrapper();
        updateQuery.lambda().eq(CateringShopPrintingConfigEntity::getShopId,dto.getShopId())
                .eq(CateringShopPrintingConfigEntity::getDeviceNumber,dto.getDeviceNumber());
        updateQuery.lambda().set(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE_NOT.getStatus());
        this.update(updateQuery);
        //2、移除设备已有订单信息
        this.delShopOrderNoticeInfo(dto.getDeviceNumber(),dto.getShopId());
        //3、删除缓存中绑定关系、以及尚未通知的订单
        merchantUtils.removeNoticeDeviceInfo(dto.getDeviceNumber(),dto.getShopId());
        merchantUtils.removeShopOrderNotice(dto.getDeviceNumber()+dto.getShopId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrderNoticeInfo(String deviceNumber) {
        //1、关闭设备对应打印、新订单通知相关配置信息
        UpdateWrapper<CateringShopOrderNoticeEntity> orderUpdateQuery = new UpdateWrapper();
        orderUpdateQuery.lambda().eq(CateringShopOrderNoticeEntity::getDeviceNumber,deviceNumber)
                .eq(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.FALSE);
        orderUpdateQuery.lambda().set(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.TRUE);
        shopOrderNoticeService.update(orderUpdateQuery);

        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getDeviceNumber,deviceNumber)
                .eq(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE.getStatus());
        List<CateringShopPrintingConfigEntity> printingConfigEntities = this.list(query);

        if (!BaseUtil.judgeList(printingConfigEntities)){
            return;
        }
        printingConfigEntities.forEach(entity->{
            entity.setStatus(StatusEnum.ENABLE_NOT.getStatus());
            //2、删除缓存中绑定关系、以及尚未通知的订单
            merchantUtils.removeNoticeDeviceInfo(entity.getDeviceNumber(),entity.getShopId());
            merchantUtils.removeShopOrderNotice(entity.getDeviceNumber()+entity.getShopId());
        });
        //3、移除设备绑定店铺
        this.saveOrUpdateBatch(printingConfigEntities);
    }

    @Override
    public Result<ShopPrintingConfigVO> getDevicePrintingConfig(ShopPrintingConfigDTO dto) {
        return Result.succ(BaseUtil.objToObj(getShopPrintingConfig(dto), ShopPrintingConfigVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> saveYlyDevice(YlyDeviceAddDTO dto) {
        //查询当前打印机是否授权过其他店铺
        CateringShopPrintingConfigEntity entity = this.selectOne(dto.getDeviceNumber());
        Long entityId = null;
        if (!ObjectUtils.isEmpty(entity)){
            entityId = entity.getId();
            if (Objects.equals(dto.getShopId(),entity.getShopId())){
                entity.setStatus(StatusEnum.ENABLE.getStatus());
                this.updateById(entity);
                return Result.fail(ErrorCode.PRINT_ADD_ERROR_MSG);
            }
        }
        //打印机授权
        String str = ylyUtils.addPrinter(dto.getDeviceNumber(), dto.getDeviceKey());
        if (BaseUtil.judgeString(str)){
            return Result.fail("终端号或密钥错误请重新输入");
        }
        String s = ylyUtils.printSetIcon(dto.getDeviceNumber(), ylyService.getImgUrl());
        if (BaseUtil.judgeString(s)){
            return Result.fail("易联云打印机打印状态异常");
        }

        CateringShopPrintingConfigEntity ylyDefaultEntity = this.getYlyDefaultConfig(dto);
        ylyDefaultEntity.setId(entityId);
        this.saveOrUpdate(ylyDefaultEntity);
        return Result.succ(String.valueOf(ylyDefaultEntity.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopPrintingConfigDTO> updateYlyDevice(YlyDeviceUpdateDTO dto) {
        CateringShopPrintingConfigEntity entity = BaseUtil.objToObj(dto, CateringShopPrintingConfigEntity.class);
        this.updateById(entity);
        return Result.succ(BaseUtil.objToObj(dto,ShopPrintingConfigDTO.class));
    }

    @Override
    public Result<YlyDeviceInfoVo> getYlyDeviceInfo(YlyDeviceUpdateDTO dto) {
        CateringShopPrintingConfigEntity entity = this.getById(dto.getId());
        if (ObjectUtils.isEmpty(entity)){
            return Result.succ();
        }
        return Result.succ(BaseUtil.objToObj(entity,YlyDeviceInfoVo.class));
    }

    @Override
    public Result<List<YlyDeviceInfoVo>> ylyDevicePage(Long shopId) {
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getShopId, shopId)
                      .eq(CateringShopPrintingConfigEntity::getDeviceType, DeviceTypeEnum.YLY.getStatus())
                      .in(CateringShopPrintingConfigEntity::getDeviceStatus,Arrays.asList(0,1,2))
                      .eq(CateringShopPrintingConfigEntity::getStatus,StatusEnum.ENABLE.getStatus())
                      .eq(CateringShopPrintingConfigEntity::getDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringShopPrintingConfigEntity> entityList = shopPrintingConfigMapper.selectList(query);
        if (!BaseUtil.judgeList(entityList)){
            return Result.succ();
        }
        List<CateringShopPrintingConfigEntity> collect = entityList.stream().filter(BaseUtil.distinctByKey(CateringShopPrintingConfigEntity::getDeviceNumber)).collect(Collectors.toList());

        return Result.succ(BaseUtil.objToObj(collect,YlyDeviceInfoVo.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<CateringShopPrintingConfigEntity> updateYlyDeviceStatus(String machineCode, String online) {
        CateringShopPrintingConfigEntity shopPrintingConfigEntity = selectOne(machineCode);
        if (ObjectUtils.isEmpty(shopPrintingConfigEntity)){
            log.error("打印机绑定店铺查询失败： machineCode-" + machineCode);
            return Result.succ();
        }

        shopPrintingConfigEntity.setDeviceStatus(Integer.valueOf(online));
        this.saveOrUpdate(shopPrintingConfigEntity);
        return Result.succ(shopPrintingConfigEntity);
    }

    public CateringShopPrintingConfigEntity getShopPrintingConfig(ShopPrintingConfigDTO dto) {
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringShopPrintingConfigEntity::getDel,Boolean.FALSE)
                .eq(CateringShopPrintingConfigEntity::getDeviceNumber,dto.getDeviceNumber())
                .eq(CateringShopPrintingConfigEntity::getShopId,dto.getShopId());
        List<CateringShopPrintingConfigEntity> entityList = shopPrintingConfigMapper.selectList(query);

        if (!BaseUtil.judgeList(entityList)){
            throw new CustomException("账号信息查询失败,店铺id ： " + dto.getShopId() + ";设备号 ： " + dto.getDeviceNumber());
        }
        return entityList.get(0);
    }

    /**
     * 方法描述 : 移除设备已有订单信息
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 11:18
     * @param deviceNumber  设备号
     * @param shopId 店铺id
     * @return: void
     * @Since version-1.3.1
     */
    private void delShopOrderNoticeInfo(String deviceNumber,Long shopId,List<Long> orderIds){
        UpdateWrapper<CateringShopOrderNoticeEntity> orderUpdateQuery = new UpdateWrapper();
        orderUpdateQuery.lambda().eq(CateringShopOrderNoticeEntity::getShopId,shopId)
                .eq(CateringShopOrderNoticeEntity::getDeviceNumber,deviceNumber)
                .in(CateringShopOrderNoticeEntity::getOrderId,orderIds);

        orderUpdateQuery.lambda().set(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.TRUE);
        shopOrderNoticeService.update(orderUpdateQuery);
    }

    /**
     * 方法描述 : 退出登录移除设备已有订单信息
     * @Author: MeiTao
     * @Date: 2020/9/4 0004 11:18
     * @param deviceNumber  设备号
     * @param shopId 店铺id
     * @return: void
     * @Since version-1.3.1
     */
    private void delShopOrderNoticeInfo(String deviceNumber,Long shopId){
        UpdateWrapper<CateringShopOrderNoticeEntity> orderUpdateQuery = new UpdateWrapper();
        orderUpdateQuery.lambda().eq(CateringShopOrderNoticeEntity::getShopId,shopId)
                .eq(CateringShopOrderNoticeEntity::getDeviceNumber,deviceNumber);

        orderUpdateQuery.lambda().set(CateringShopOrderNoticeEntity::getNoticeSucc,Boolean.TRUE);
        shopOrderNoticeService.update(orderUpdateQuery);
    }

    /**
     * 方法描述 :  根据门店id以及设备id初始化店铺打印配置数据
     * @Author: MeiTao
     * @Date: 2020/9/3 0003 16:19
     * @param dto 请求参数
     * @return: com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO
     * @Since version-1.3.0
     */
    private CateringShopPrintingConfigEntity getDefaultShopPrintingConfig(ShopPrintingConfigDTO dto) {
        dto.setPrintingTimes(1);
        dto.setAutoPrint(Boolean.TRUE);
        //设备类型--占不使用: 手机端
        dto.setDeviceType(0);
        dto.setTicketType(TicketTypeEnum.ALL.getStatus());
        dto.setVoiceRemind(Boolean.TRUE);
        dto.setNightClose(Boolean.TRUE);
        dto.setStatus(StatusEnum.ENABLE.getStatus());
        dto.setDel(DelEnum.NOT_DELETE.getFlag());
        return BaseUtil.objToObj(dto, CateringShopPrintingConfigEntity.class);
    }

    /**
     * 方法描述 :
     * @Author: MeiTao
     * @Date: 2020/9/9 0009 11:47
     * @param dto
     * @param entity 请求参数
     * @return: void
     * @Since version-1.3.0
     */
    private void setDeviceUpdateInfo(DeviceConfigUpdateDTO dto,CateringShopPrintingConfigEntity entity) {
        if (!ObjectUtils.isEmpty(dto.getPrintingTimes())){
            entity.setPrintingTimes(dto.getPrintingTimes());
        }
        if (!ObjectUtils.isEmpty(dto.getAutoPrint())){
            entity.setAutoPrint(dto.getAutoPrint());
        }
        if (!ObjectUtils.isEmpty(dto.getTicketType())){
            entity.setTicketType(dto.getTicketType());
        }
        if (!ObjectUtils.isEmpty(dto.getVoiceRemind())){
            entity.setVoiceRemind(dto.getVoiceRemind());
        }
        if (!ObjectUtils.isEmpty(dto.getNightClose())){
            entity.setNightClose(dto.getNightClose());
        }
    }

    /**
     * 方法描述 :  获取易联云打印默认配置
     * @Author: MeiTao
     * @Date: 2020/9/27 0027 14:54
     * @param dto 请求参数
     * @return: CateringShopPrintingConfigEntity
     * @Since version-1.5.0
     */
    private CateringShopPrintingConfigEntity getYlyDefaultConfig(YlyDeviceAddDTO dto) {
        CateringShopPrintingConfigEntity entity = BaseUtil.objToObj(dto, CateringShopPrintingConfigEntity.class);
        entity.setPrintingTimes(0);
        entity.setCookTimes(0);
        entity.setAutoPrint(Boolean.TRUE);
        entity.setDeviceType(DeviceTypeEnum.YLY.getStatus());
        entity.setTicketType(3);
        entity.setVoiceRemind(Boolean.TRUE);
        entity.setNightClose(Boolean.FALSE);
        entity.setStatus(StatusEnum.ENABLE.getStatus());
        entity.setDel(DelEnum.NOT_DELETE.getFlag());
        //设备名字初始默认值为设备号
        entity.setDeviceName(dto.getDeviceNumber());
        //设置设备当前状态
        entity.setDeviceStatus(ylyUtils.printerGetPrintStatus(dto.getDeviceNumber()));
        return entity;
    }

    /**
     * 方法描述 : 根据查询添加查询设备信息
     *            查询后会对错误数据进行处理
     * @Author: MeiTao
     * @Date: 2020/9/29 0029 11:01
     * @param deviceNumber 请求参数
     * @return: CateringShopPrintingConfigEntity
     * @Since version-1.5.0
     */
    private CateringShopPrintingConfigEntity selectOne(String deviceNumber) {
        QueryWrapper<CateringShopPrintingConfigEntity> query = new QueryWrapper<>();
        query.lambda().eq(!ObjectUtils.isEmpty(deviceNumber),CateringShopPrintingConfigEntity::getDeviceNumber,deviceNumber)
                      .eq(CateringShopPrintingConfigEntity::getDel,DelEnum.NOT_DELETE.getFlag());
        List<CateringShopPrintingConfigEntity> entityList = baseMapper.selectList(query);
        if (BaseUtil.judgeList(entityList)){
            CateringShopPrintingConfigEntity entity = entityList.get(0);
            if (entityList.size() > 1){
                entityList.remove(0);
                entityList.forEach(e->e.setDel(DelEnum.DELETE.getFlag()));
                this.saveOrUpdateBatch(entityList);
            }
            return entity;
        }
        return null;
    }
}
