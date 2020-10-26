package com.meiyuan.catering.merchant.pc.service.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.exception.AdminUnauthorizedException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.RegisterPhoneChangeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.enums.ShopTypeEnum;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.feign.MerchantLoginAccountClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategorySaveDTO;
import com.meiyuan.catering.merchant.goods.enums.CategoryTypeEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantCategoryClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantMenuGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo;
import com.meiyuan.catering.order.feign.OrdersShopDebtClient;
import com.meiyuan.catering.pay.service.MyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 后台商户管理服务
 * @Date  2020/3/12 0012 15:37
 */
@Service
@Slf4j
public class MerchantPcMerchantService {
    @Resource
    MerchantClient merchantClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    EsMerchantClient esMerchantClient;
    @Resource
    MerchantLoginAccountClient loginAccountClient;
    @Resource
    MerchantMenuGoodsClient merchantMenuGoodsClient;
    @Resource
    MerchantCategoryClient merchantCategoryClient;
    @Resource
    MyMemberService myMemberService;
    @Resource
    RoleClient roleClient;
    @Resource
    ShopClient shopClient;
    @Resource
    OrdersShopDebtClient ordersShopDebtClient;

    /**
     * pc-店铺列表
     * @param dto 分页条件
     * @return 店铺列表
     */
    public Result<IPage<CateringMerchantPageVo>> shopPage(MerchantLimitQueryDTO dto) {
        return merchantClient.listLimit(dto);
    }

    /**
     * pc-商户店铺详情查询
     * @param dto
     * @return
     */
    public Result<MerchantShopDetailVo> getMerchantShopDetail(ShopQueryDTO dto ) {
        return merchantClient.getMerchantShopDetail(dto);
    }

    /**
     * @Author MeiTao
     * @Description 添加店铺
     * @Date  2020/3/16 0016 15:22
     */
    @Transactional(rollbackFor = Exception.class)
    public Result insertShop(MerchantShopAddDTO dto) {
        Result<Long> result = merchantClient.insertMerchantShop(dto);

        //添加自提点则不同步es
        if (ObjectUtils.isEmpty(result.getData())){
            return result;
        }
        //通联创建个人会员
        myMemberService.createPersonalMember(result.getData());
        //店铺添加默认角色
        roleClient.insertDefaultRole(result.getData());

        //添加店铺默认商品分类
        MerchantCategorySaveDTO merchantCategory = new MerchantCategorySaveDTO();
        merchantCategory.setCategoryName("默认分类");
        merchantCategory.setMerchantId(result.getData());
        merchantCategory.setDefaulCategory(CategoryTypeEnum.DEFAULT.getStatus());
        merchantCategoryClient.save(merchantCategory,Boolean.FALSE);
        //创建门店负债信息
        ordersShopDebtClient.create(result.getData());
        //新增店铺信息同步es
        ShopInfoDTO shop = merchantUtils.getShop(result.getData());
        if (!ObjectUtils.isEmpty(shop)){
            EsMerchantDTO esMerchantDTO = new EsMerchantDTO();
            esMerchantDTO.setId(shop.getId().toString());
            esMerchantDTO.setMerchantId(shop.getMerchantId().toString());
            esMerchantDTO.setShopId(shop.getId().toString());
            esMerchantDTO.setMerchantName(shop.getShopName());

            esMerchantDTO.setLocation(BaseUtil.locationToEsConver(shop.getMapCoordinate()));
            esMerchantDTO.setProvinceCode(shop.getAddressProvinceCode());
            esMerchantDTO.setEsCityCode(shop.getAddressCityCode());
            esMerchantDTO.setAreaCode(shop.getAddressAreaCode());

            esMerchantDTO.setShopGrade(0.0D);
            esMerchantDTO.setOrdersNum(0);

            //店铺是否可在小程序展示
            esMerchantDTO.setShopService(shop.getShopServiceType());
            esMerchantDTO.setShopStatus(shop.getShopStatus());

            MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shop.getMerchantId());
            esMerchantDTO.setAuditStatus(merchantInfo.getAuditStatus());
            esMerchantDTO.setMerchantStatus(merchantInfo.getMerchantStatus());

            esMerchantClient.saveUpdate(esMerchantDTO);
        }else {
            log.info("es添加商户："+ shop.getShopName() +",es信息同步失败");
        }
        return result;
    }

    /**
     * 方法描述 : 修改门店信息
     * @Author: MeiTao
     */
    public Result updateMerchantShop(MerchantShopUpdateDTO dto) {
        ShopInfoDTO shop = merchantUtils.getShop(dto.getShopId());
        MerchantModifyInfoRequestDTO merchantModifyInfo = BaseUtil.objToObj(dto, MerchantModifyInfoRequestDTO.class);
        merchantModifyInfo.setShopLogo(dto.getDoorHeadPicture());
        Result<ShopDTO> shopDtoResult = merchantClient.modifyMerchantBaseInfo(dto.getShopId(), merchantModifyInfo);

        ShopDTO shopDto = shopDtoResult.getData();
        if (Objects.equals(shopDto.getShopType(),ShopTypeEnum.BUSINESS_POINT.getStatus())){
            return Result.succ();
        }

        //修改菜单对应门店名称
        if (!Objects.equals(dto.getShopName(),shop.getShopName())){
            merchantMenuGoodsClient.updateMenuShopName(shopDto.getId(),shopDto.getShopName());
        }
        return shopDtoResult;
    }

    /**
     * 方法描述 : 获取登陆账户基本信息
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 13:33
     * @param accountDto 请求参数
     * @return: Result<MerchantAccountDTO>
     * @Since version-1.1.0
     */
    public Result<MerchantAccountDTO> getAccountInfo(MerchantAccountDTO accountDto) {
        MerchantLoginAccountDTO queryCondition = new MerchantLoginAccountDTO();
        queryCondition.setAccountTypeId(accountDto.getAccountTypeId());
        Result<List<MerchantLoginAccountDTO>> accountData = loginAccountClient.getAccount(queryCondition);

        if (!BaseUtil.judgeResultList(accountData)){
            throw new AdminUnauthorizedException("账号可能已被删除,请联系管理员");
        }

        MerchantLoginAccountDTO loginAccountDto = accountData.getData().get(0);
        accountDto.setPhone(loginAccountDto.getPhone());
        accountDto.setAccountStatus(loginAccountDto.getAccountStatus());

        String perm = AccountTypeEnum.parse(loginAccountDto.getAccountType()).getDesc();
        accountDto.setPerms(Arrays.asList(perm));

        //若登录账号为店铺还需判断商户状态
        if(Objects.equals(loginAccountDto.getAccountType(),AccountTypeEnum.SHOP_PICKUP.getStatus())||
           Objects.equals(loginAccountDto.getAccountType(),AccountTypeEnum.SHOP.getStatus())){
            //@TODO 员工登录判断员工
            ShopInfoDTO shopInfo = merchantUtils.getShop(loginAccountDto.getAccountTypeId());
            MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shopInfo.getMerchantId());
            if (Objects.equals(merchantInfo.getMerchantStatus(), StatusEnum.ENABLE_NOT.getStatus())){
                accountDto.setAccountStatus(StatusEnum.ENABLE_NOT.getStatus());
            }
        }
        //若为员工登录则将AccountType设置为店铺id
        if (Objects.equals(loginAccountDto.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            accountDto.setEmployeeId(loginAccountDto.getAccountTypeId());
            CateringShopEmployeeEntity employee = shopClient.getEmployeeById(loginAccountDto.getAccountTypeId());
            if (ObjectUtils.isEmpty(employee)){
                log.error("员工门店信息查询失败，员工id ： " + loginAccountDto.getAccountTypeId());
                throw new AdminUnauthorizedException("账号可能已被删除,请联系管理员");
            }
            accountDto.setAccountTypeId(employee.getShopId());
            ShopInfoDTO shopInfo = merchantUtils.getShop(employee.getShopId());
            if (ObjectUtils.isEmpty(shopInfo)){
                log.error("员工门店信息查询失败，员工id ： " + loginAccountDto.getAccountTypeId());
                throw new AdminUnauthorizedException("账号可能已被删除,请联系管理员");
            }
            MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(shopInfo.getMerchantId());
            if (ObjectUtils.isEmpty(merchantInfo)){
                log.error("员工门店信息查询失败，员工id ： " + loginAccountDto.getAccountTypeId());
                throw new AdminUnauthorizedException("账号可能已被删除,请联系管理员");
            }
            if (Objects.equals(merchantInfo.getMerchantStatus(), StatusEnum.ENABLE_NOT.getStatus())||
                    Objects.equals(shopInfo.getShopStatus(), StatusEnum.ENABLE_NOT.getStatus())){
                accountDto.setAccountStatus(StatusEnum.ENABLE_NOT.getStatus());
            }
        }
        return Result.succ(accountDto);
    }

    /**
     * 方法描述 : 获取门店服务类型
     * @Author: MeiTao
     * @Date: 2020/7/7 0007 14:13
     * @param merchantId 请求参数
     * @return: Result<com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo>
     * @Since version-1.1.0
     */
    public Result<String> getShopService(Long merchantId) {
        MerchantInfoDTO merchantInfo = merchantUtils.getMerchant(merchantId);
        if (ObjectUtils.isEmpty(merchantInfo)){
            return Result.fail("当前商户不存在");
        }
        return Result.succ(merchantInfo.getMerchantService());
    }


    /**
     * 方法描述 : pc - 店铺注册手机号修改
     * @Author: MeiTao
     * @Date: 2020/7/8 0008 9:37
     * @param dto 请求参数
     * @return: com.meiyuan.catering.core.util.Result<java.lang.String>
     * @Since version-1.2.0
     */
    public Result<String> updateShopRegisterPhone(RegisterPhoneChangeDTO dto) {
        return merchantClient.updateShopRegisterPhone(dto);
    }
}
