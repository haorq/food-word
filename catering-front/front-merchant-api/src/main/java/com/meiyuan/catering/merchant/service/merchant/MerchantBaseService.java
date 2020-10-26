package com.meiyuan.catering.merchant.service.merchant;

import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.fegin.VersionManageClient;
import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.config.EncryptPasswordProperties;
import com.meiyuan.catering.core.constant.DefaultPwdMsg;
import com.meiyuan.catering.core.dto.base.merchant.ShopNoticeInfoDTO;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.HttpContextUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.config.ShopPrintingConfigDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.LogBackTypeEnum;
import com.meiyuan.catering.merchant.enums.ShopIsPickupEnum;
import com.meiyuan.catering.merchant.enums.ShopTypeEnum;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.MerchantLoginAccountClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yaoozu
 * @description 商户基本服务
 * @date 2020/3/2010:50
 * @since v1.0.0
 */
@Service
@Slf4j
public class MerchantBaseService {
    @Autowired
    private ShopClient shopClient;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private MerchantClient merchantClient;

    @Autowired
    private EncryptPasswordProperties encryptProperties;

    @Resource
    EsMerchantClient esMerchantClient;

    @Autowired
    private MerchantUtils merchantUtils;

    @Autowired
    private MerchantLoginAccountClient loginAccountClient;

    @Autowired
    VersionManageClient versionManageClient;

    @Autowired
    private ShopPrintingConfigClient shopPrintingConfigClient;

    @Autowired
    private RoleClient roleClient;


    /**
     * @description 商户登录
     * @author yaozou
     * @date 2020/3/20 14:53
     * @param
     * @since v1.0.0 shopService.appMerchantLogin()
     * @return
     */
    public Result<MerchantLoginResponseDTO> login(MerchantLoginRequestDTO dto){

        return shopClient.appMerchantLogin(dto,encryptProperties.getKey(), encryptProperties.getIv());
    }



    /**
     * 方法描述 : 商户app:发送短信验证码
     *          1、验证商户是否存在
     *          2、发送短信验证码
     * @Author: MeiTao
     * @Date: 2020/5/21 0021 9:32
     * @param phone
     * @return: 短信验证码
     * @Since version-1.0.0
     */
    public Result<String> sendSmsCode(String phone){
        MerchantLoginAccountDTO accountDto = new MerchantLoginAccountDTO();
        accountDto.setPhone(phone);
        Result<List<MerchantLoginAccountDTO>> accountListData = loginAccountClient.getAccount(accountDto);

        // 1、商户是否存在，是否可用
        if (!BaseUtil.judgeResultObject(accountListData)){
            return Result.fail("该账号不存在，请重新输入");
        }

        MerchantLoginAccountDTO account = accountListData.getData().get(0);
        if (account.getIsDel()){
            return Result.fail("账号已被删除，请联系平台管理员");
        }

        //获取六位短信验证码
        String code = CodeGenerator.msgCode(6);
        // 2、发送短信验证码
        if (!DefaultPwdMsg.USE_DEFAULT_MSG){
            notifyService.notifySmsTemplate(phone, NotifyType.VERIFY_CODE_NOTIFY,new String[]{code});
        }

        // 3、缓存
        merchantUtils.saveSmsAuthCode(phone,code);
        return Result.succ(code);
    }

    /**
     * 方法描述 : app-短信验证码修改密码
     * @Author: MeiTao
     */
    public Result updatePassword(MerchantPasswordRequestDTO dto){
        return shopClient.appMerchantUpdatePassword(dto,encryptProperties.getKey(), encryptProperties.getIv());
    }

    /**
     * @description 商户登录退出
     * @author yaozou
     * @date 2020/3/20 17:00
     * @param
     * @since v1.0.0
     * @return
     */
    public Result logout(MerchantTokenDTO dto,String deviceNumber){
        if (!BaseUtil.judgeString(deviceNumber)){
            log.error("店铺退出登陆，设备号未传入，店铺id ： " + dto.getShopId() + ";店铺名称 ： " + dto.getShopName());
            return Result.succ();
        }
        ShopPrintingConfigDTO shopPrintingConfig = new ShopPrintingConfigDTO();
        shopPrintingConfig.setDeviceNumber(deviceNumber);
        shopPrintingConfig.setShopId(dto.getShopId());
        shopPrintingConfigClient.delDevicePrintingConfig(shopPrintingConfig);
        return Result.succ();
    }

    /**
     * @description 商户登录退出
     * @author yaozou
     * @return
     */
    public Result logout(String shopId, String deviceNumber){
        if (!BaseUtil.judgeString(deviceNumber)){
            log.error("店铺退出登陆，设备号未传入，店铺id ： " + shopId);
            return Result.succ();
        }

        shopPrintingConfigClient.delOrderNoticeInfo(deviceNumber);

        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        AppJwtTokenDTO merchantTokenDto = this.getLogInfo(httpServletRequest,shopId);
        Map<String, String> headers = HttpContextUtils.getHeaders(httpServletRequest);
        String tokenNew = headers.get("x-catering-token");

        if (ObjectUtils.isEmpty(merchantTokenDto)){
            log.error("=============退出登录token未传===========");
            return Result.succ();
        }

        //token清除
        //1、通过id获取缓存中的token
        MerchantTokenDTO merchantByToken = null;
        if (Objects.equals(merchantTokenDto.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())){
            merchantByToken = merchantUtils.getMerchantByToken(merchantTokenDto.getEmployeeId().toString());
        }else {
            merchantByToken =  merchantUtils.getMerchantByToken(merchantTokenDto.getShopId().toString());
        }

        //若token已被删除则无需再次清除token
        if (ObjectUtils.isEmpty(merchantByToken)){
            return Result.succ();
        }

        //1、若正常退出登陆，清除token
        String tokenOld = merchantByToken.getToken();
        Boolean isDelToken = Objects.equals(tokenOld,tokenNew)&&Objects.equals(merchantByToken.getLogBackType(), LogBackTypeEnum.NORMAL.getStatus());
        if (Objects.equals(merchantTokenDto.getAccountType(), AccountTypeEnum.EMPLOYEE.getStatus())&&isDelToken){
            merchantUtils.removeMerAppToken(merchantTokenDto.getEmployeeId().toString());
        }else if (isDelToken){
            merchantUtils.removeMerAppToken(merchantTokenDto.getShopId().toString());
        }
        return Result.succ();
    }

    /**
     * 方法描述 : 获取登陆身份信息
     * @Author: MeiTao
     * @Date: 2020/10/18 0018 12:08
     * @param httpServletRequest
     * @param shopId 请求参数
     * @return: com.meiyuan.catering.merchant.dto.merchant.AppJwtTokenDTO
     * @Since version-1.5.0
     */
    private AppJwtTokenDTO getLogInfo(HttpServletRequest httpServletRequest,String shopId) {
        Map<String, String> headers = HttpContextUtils.getHeaders(httpServletRequest);
        String tokenNew = headers.get("x-catering-token");
        if (ObjectUtils.isEmpty(tokenNew)){
            log.error("退出登陆token未传，不会清除token，shopId ： " +shopId);
            return null;
        }
        AppJwtTokenDTO merchantTokenDto = null;
        try {
            merchantTokenDto = TokenUtil.getFromToken(tokenNew, TokenEnum.MERCHANT_APP, AppJwtTokenDTO.class);
        } catch (Exception e) {
            log.error("token解析失败，" + tokenNew +";shopId ： " +shopId);
        }
        return merchantTokenDto;
    }

    /**
     * @description 改变营业（门店/自提点）状态：营业中 变为 打烊中/暂停营业，打样中/暂停营业 变为 营业中
     * @author yaozou
     * @date 2020/3/20 16:37
     * @param shopId 门店ID
     * @param type  类型：判断是查询还是修改：false：查询，true：修改"
     * @since v1.0.0
     * @return
     */
   public Result<ShopResponseDTO> modifyBusinessStatus(Long shopId, Boolean type){
       return merchantClient.modifyMerchantBusinessStatus(shopId,type);
   }

    /**
     * @description 修改门店信息（除营业状态）
     *              1、修改门店基本信息
     *              2、商户信息同步es
     * @author yaozou
     * @date 2020/3/20 16:37
     * @param shopId 门店ID
     *        request
     * @since v1.0.0
     * @return
     */
   public Result<ShopResponseDTO> modifyBaseInfo(Long shopId , MerchantModifyInfoRequestDTO dto){
       Result<ShopDTO> shopDtoResult = merchantClient.modifyMerchantBaseInfo(shopId, dto);
       //同步es相关数据
       ShopDTO shopDTO = shopDtoResult.getData();
//
//       if (!ObjectUtils.isEmpty(shopDTO)&& !Objects.equals(shopDTO.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
//           updateMerchantEs(shopDTO);
//       }
       return Result.succ(entityToDto(shopDTO));
    }

    /**
     * 方法描述 : 店铺信息修改更新es相关数据
     *           若为自提点则不做任何处理
     * @Author: MeiTao
     * @Date: 2020/6/3 0003 14:45
     * @param shop
     * @return: void
     * @Since version-1.0.0
     */
    private void updateMerchantEs(ShopDTO shop) {
        if(!Objects.equals(shop.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            EsMerchantDTO esMerchantDTO = esMerchantClient.getByMerchantId(shop.getMerchantId().toString()).getData();
            if (ObjectUtils.isEmpty(esMerchantDTO)){
                log.debug("商户：" + shop.getMerchantId() + "，商户es信息获取失败");
                return;
            }
            esMerchantDTO.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
            esMerchantDTO.setProvinceCode(shop.getAddressProvinceCode());
            esMerchantDTO.setEsCityCode(shop.getAddressCityCode());
            esMerchantDTO.setAreaCode(shop.getAddressAreaCode());
            BeanUtils.copyProperties(shop,esMerchantDTO);

            esMerchantClient.saveUpdate(esMerchantDTO);
        }
    }

    private int shopType(int shopType,boolean isPickup){
        // 类型：1--店铺，2--自提点，3--既是店铺也是自提点
        int type = ShopIsPickupEnum.PICKUP.getStatus();
        if ( !Objects.equals(shopType,ShopTypeEnum.BUSINESS_POINT.getStatus())){
            type = ShopIsPickupEnum.SHOP.getStatus();
            if (isPickup){
                type = ShopIsPickupEnum.SHOP_AND_PICKUP.getStatus();
            }
        }
        return type;
    }

    private boolean isUsed(int shopStatus){
        return shopStatus == StatusEnum.ENABLE.getStatus();
    }

    private ShopResponseDTO entityToDto(ShopDTO shop){
        ShopResponseDTO shopResponseDTO = ConvertUtils.sourceToTarget(shop,ShopResponseDTO.class);
        if (!Objects.equals(shop.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            // 类型：1--店铺，2--自提点，3--既是店铺也是自提点
            int type = shopType(shop.getShopType(),shop.getIsPickupPoint());
            shopResponseDTO.setType(type);
            shopResponseDTO.setShopId(shop.getId());
            shopResponseDTO.setShopLogo(shop.getDoorHeadPicture());
        }else {
            shopResponseDTO.setType(ShopIsPickupEnum.PICKUP.getStatus());
            shopResponseDTO.setShopId(shop.getId());
        }
        return shopResponseDTO;
    }

    /**
     * 店铺切换登录身份
     * @param shopId
     * @param shopRole 店铺登录身份 : 1--店铺，2--自提点
     * @return
     */
    public Result<MerchantLoginResponseDTO> exchangeIdentity(Long shopId, Integer shopRole) {
        return shopClient.appMerchantExchangeIdentity(shopId,shopRole);
    }

    /**
     * 方法描述 : 原密码新密码修改密码
     * @Author: MeiTao
     */
    public Result updatePasswordByOld(Long shopId, MerchantPasswordRequestDTO dto) {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        AppJwtTokenDTO tokenDto = this.getLogInfo(httpServletRequest,null);
        if (ObjectUtils.isEmpty(tokenDto)){
            return Result.fail("登录token未传，门店联系电话 phone ： " + dto.getPhone());
        }
        shopId = Objects.equals(tokenDto.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())?tokenDto.getEmployeeId():tokenDto.getAccountTypeId();
        return shopClient.updatePasswordByOld(shopId,dto,encryptProperties.getKey(), encryptProperties.getIv());
    }

    /**
     * 方法描述 : app版本是否更新相关参数查询
     * @Author: MeiTao
     * @Date: 2020/8/13 0013 16:35
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.vo.shop.AppVersionInfoVo>
     * @Since version-1.3.0
     */
    public Result<AppVersionInfoVo> appUpdateVersion() {
        return versionManageClient.getVersionManageInfo("610537588337");
    }

    /**
     * 方法描述 : 获取设备新订单缓存信息
     * @Author: MeiTao
     * @Date: 2020/9/8 0008 15:21
     * @param s 请求参数
     * @return: com.meiyuan.catering.core.dto.base.merchant.ShopNoticeInfoDTO
     * @Since version-1.3.0
     */
    public ShopNoticeInfoDTO getShopOrderNotice(String s) {
        return merchantUtils.getShopOrderNotice(s);
    }

    /**
     * 获取没有的权限集合
     * @param token
     * @return
     */
    public Result<List<MerchantMenuVO>> getPermissionsAbsence(MerchantTokenDTO token) {
        Long accountTypeId = token.getAccountTypeId();
        return roleClient.getMerchantPermissionsAbsence(accountTypeId);
    }

    /**
     * 获取所有的权限集合
     * @param token
     * @return
     */
    public Result<List<MerchantMenuVO>> getAllPermissions(MerchantTokenDTO token) {
        return roleClient.getMerchantAllPermissions();
    }
}
