package com.meiyuan.catering.merchant.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.shop.RegisterPhoneChangeDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopApplyListDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.service.CateringMerchantService;
import com.meiyuan.catering.merchant.service.CateringShopApplyService;
import com.meiyuan.catering.merchant.vo.ShopApplyVO;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantInfoVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @Author MeiTao
  * @Date 2020/5/19 0019 9:31
  * @Description 简单描述:商户client
  * @Since version-1.0
  */
@Service
public class MerchantClient{

     @Autowired
     CateringMerchantService merchantService;

    @Autowired
    CateringShopApplyService cateringShopApplyService;

    /**
     * ===================商户缓存处理相关接口===================
     */

    public Result refreshMerchantCatch() {
        return merchantService.refreshMerchantCatch();
    }

    public void putAllShopCache(){
        merchantService.putAllShopCache();
    }

    public void putAllShopConfigCache(){
        merchantService.putAllShopConfigCache();
    }
    /**
     * pc-店铺列表
     * @param dto
     * @return
     */
    public Result<IPage<CateringMerchantPageVo>> listLimit(MerchantLimitQueryDTO dto){
        return Result.succ(merchantService.listLimit(dto));
    }

    /**
     * pc-商户店铺详情查询
     * @param  dto
     * @return
     */
    public Result<MerchantShopDetailVo> getMerchantShopDetail(ShopQueryDTO dto ){
       return merchantService.getMerchantShopDetail(dto);
    }

    /**
     * 添加店铺
     * @param dto
     * @return 店铺id
     */
    public Result<Long> insertMerchantShop(MerchantShopAddDTO dto){
       return merchantService.insertMerchantShop(dto);
    }

    /**
     *  通过查询条件查询商户信息
     * @param queryCondition
     * @return
     */
    public Result<List<MerchantInfoVo>> listMerchantInfo(MerchantQueryConditionDTO queryCondition){
     return Result.succ(merchantService.listMerchantInfo(queryCondition));
    }

   /**
     * 全部商户
     *
     * @author: wxf
     * @date: 2020/4/22 13:54
     * @param flag 是否包含自提点 true 包含 false 不包含
     * @return: {@link List< ShopDTO>}
     **/
    public Result<List<ShopDTO>> allMerchant(boolean flag){
         return Result.succ(merchantService.allMerchant(flag));
    }

    /**
    * 查询所有门店信息 不包含自提点
    * @author: GongJunZheng
    * @date: 2020/8/19 18:49
    * @return: {@link List<ShopDTO>}
    * @version V1.3.0
    **/
    public Result<List<ShopDTO>> findAll() {
        return Result.succ(merchantService.findAll());
    }


    /**
     *  改变营业（门店/自提点）状态：营业中 变为 打烊中/暂停营业，打样中/暂停营业 变为 营业中
     * @author yaozou
     * @date 2020/3/20 16:37
     * @param shopId 门店ID
     * @param type  类型：判断是查询还是修改：false：查询，true：修改"
     * @since v1.0.0
     * @return
     */
    public Result<ShopResponseDTO> modifyMerchantBusinessStatus(Long shopId, Boolean type){
        return merchantService.modifyMerchantBusinessStatus(shopId,type);
    }

    /**
     * 方法描述 :（app） 修改门店基本信息
     *          1、pc端 ： 修改门店信息；2、app端： 修改门店信息
     * @Author: MeiTao
     * @Date: 2020/5/21 0021 11:04
     * @param shopId
     * @param dto
     * @return: Result<com.meiyuan.catering.merchant.dto.ShopDTO>
     * @Since version-1.0.0
     */
    public Result<ShopDTO> modifyMerchantBaseInfo(Long shopId , MerchantModifyInfoRequestDTO dto){
         return merchantService.modifyMerchantBaseInfo(shopId, dto);
    }

    /**
     * 方法描述 : 管理平台商户列表分页查询
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    public Result<IPage<MerchantPageVo>> merchantPage(MerchantQueryPage dto) {
        return merchantService.merchantPage(dto);
    }

    /**
     * 方法描述 : admin-添加修改商户
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 请求参数
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    public Result<Long> saveOrUpdateMerchant(MerchantDTO dto) {
        return merchantService.saveOrUpdateMerchant(dto);
    }

    /**
     * 方法描述 : 商户详情获取
     * @Author: MeiTao
     * @Date: 2020/7/2 0002 18:27
     * @param dto 商户Id
     * @return: IPage<CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    public Result<MerchantDetailDTO> queryMerchantDetail(MerchantQueryPage dto) {
        return merchantService.queryMerchantDetail(dto);
    }

    /**
     * 方法描述 : 商户状态修改
     *          1、redis信息同步
     *          2、es信息同步
     * @Author: MeiTao
     * @Date: 2020/7/5 0005 11:18
     * @param dto 请求参数
     * @return: Result 商户下店铺id【未删除】
     * @Since version-1.1.0
     */
    public Result<List<Long>> updateMerchantStatus(MerchantDTO dto) {
        return merchantService.updateMerchantStatus(dto);
    }

    /**
     * 方法描述 : pc - 店铺注册手机号修改
     * @Author: MeiTao
     * @Date: 2020/7/8 0008 9:37
     * @param dto 请求参数
     * @return: Result<java.lang.String>
     * @Since version-1.2.0
     */
    public Result<String> updateShopRegisterPhone(RegisterPhoneChangeDTO dto) {
        return merchantService.updateShopRegisterPhone(dto);
    }

    public Result<Boolean> verifyMerchantInfoUnique(MerchantVerifyDTO dto) {
        return merchantService.verifyMerchantInfoUnique(dto);
    }

    /**
     * 方法描述 : pc-端获取商户登陆信息
     *            1、PC端短信验证码修改密码
     *            2、pc端登录查询账号是否存在
     * @Author: MeiTao
     * @Date: 2020/7/24 0024 10:31
     * @param phone 请求参数
     * @return: com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO
     * @Since version-1.2.0
     */
    public Result<MerchantDTO> pcLoginInfo(String phone) {
        return merchantService.pcLoginInfo(phone);
    }

    /**
     * 方法描述 : 商户登录账号老数据处理【1.3.0】
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 10:36
     * @return: Result<java.lang.Boolean>
     * @Since version-1.3.0
     */
    public Result<Boolean> handelMerchantData() {
        return merchantService.handelMerchantData();
    }

    /**
    * 根据商户/品牌ID获取基本信息
     *
    * @param id 商户/品牌ID
    * @author: GongJunZheng
    * @date: 2020/8/6 12:34
    * @return: {@link CateringMerchantEntity}
    **/
    public CateringMerchantEntity getById(Long id) {
        return merchantService.getById(id);
    };

    /**
     * 方法描述 : 查询商户登录账号信息
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 14:51
     * @param account 请求参数
     * @return: java.util.List<MerchantLoginAccountDTO>
     * @Since version-1.3.0
     */
    public Result<List<MerchantLoginAccountDTO>> listMerchantAccount(String account){
        return merchantService.listMerchantAccount(account);
    }

    /**
     * 小程序-添加入驻店铺申请
     * @param dto
     * @return Result
     */
    public Result insertShopApply(ShopApplyDTO dto){
        return cateringShopApplyService.insertShopApply(dto);
    }

    public Result<PageData<ShopApplyVO>> listShopApply(ShopApplyListDTO dto){
        return Result.succ(cateringShopApplyService.listShopApply(dto));
    }

    public Result<ShopApplyDTO> ShopApplyConfirm(ShopApplyDTO dto){
        return cateringShopApplyService.ShopApplyConfirm(dto);
    }

    public Result<ShopApplyVO> shopApplyDetail(String id){
        return cateringShopApplyService.shopApplyDetail(id);
    }
}
