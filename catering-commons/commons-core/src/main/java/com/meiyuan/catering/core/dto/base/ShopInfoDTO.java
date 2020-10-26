package com.meiyuan.catering.core.dto.base;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.enums.base.ServiceTypeEnum;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 商户店铺信息
 * @Date 2020/3/24 0024 14:03
 */
@Data
public class ShopInfoDTO implements Serializable {
    private static final long serialVersionUID = -85587825409728230L;
    /**
     * 店铺id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 商户id[1.0.2]
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 门店联系电话
     */
    private String shopPhone;
    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 门店联系人/注册电话
     */
    private String primaryPersonName;

    /**
     * 门店注册电话（门店登录账号）
     */
    private String registerPhone;
    /**
     * 门店类型：1-店铺 2-店铺兼自提点 3-自提点
     */
    private Integer shopType;

    /**
     * 食品经营许可证
     */
    private String foodBusinessLicense;

    /**
     * 经纬度
     */
    private String mapCoordinate;


    /**
     * 售卖模式： 1-菜单售卖模式 2-商品售卖模式
     */
    private Integer sellType;

    /**
     * 门店公告
     */
    private String shopNotice;

    /**
     * 门店LOGO图片
     */
    private String doorHeadPicture;

    /**
     * 门头图片(1.0.1)
     */
    private String shopPicture;

    /**
     * 营业开始时间
     */
    private String openingTime;

    /**
     * 营业结束时间
     */
    private String closingTime;

    /**
     * 完整地址
     */
    private String addressFull;
    /**
     * 类型：是否绑定自身:0- 不是 1-是
     */
    private Boolean isPickupPoint;

    /**
     * 省
     */
    private String addressProvinceCode;
    /**
     * 市
     */
    private String addressCityCode;
    /**
     * 区
     */
    private String addressAreaCode;

    /**
     * 地址省
     */
    private String addressProvince;
    /**
     * 地址市
     */
    private String addressCity;
    /**
     * 地址区
     */
    private String addressArea;
    /**
     * 店铺、自提点地址图片--门店头图
     */
    private String addressPicture;

    /**
     * 营业执照【1.1.0】
     */
    private String businessLicense;

    /**
     * 营业状态：1-营业 2-打样【1.1.1】
     */
    private Integer businessStatus;

    /**
     * 商户服务:1;外卖小程序,2:食堂美食城（json字符串）【1.2.0】
     */
    private String shopService;

    /**
     * 是否可修改商品价格：0：否，1：是 [1.2.0]
     */
    private Boolean changeGoodPrice;

    /**
     * 店铺状态:1：启用，2：禁用
     */
    private Integer shopStatus;
    /**
     * 配送类型： 1-自配送、2-达达配送
     */
    private Integer deliveryType;

    /**
     * 逻辑删除标示
     */
    private Boolean del;

    /**
     * 结算信息执行步骤 ：1-实名认证、2-电子签约、3-绑定银行卡、4-已完成
     */
    private Integer auditStatus;

    /**
     * 方法描述 : 店铺当前营业状态
     *          注： 该营业状态对营业时间进行处理
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 0:07
     * @return: Integer 营业状态：1-营业 2-打样
     */
    public Integer getBusinessStatus() {
        //商家是否手动关闭店铺
        if (ObjectUtils.isEmpty(businessStatus) || Objects.equals(StatusEnum.ENABLE_NOT.getStatus(),this.businessStatus)){
            return  StatusEnum.ENABLE_NOT.getStatus();
        }

        //当前店铺是否在营业时间内
        if (openingTime != null && closingTime != null) {
            return DateTimeUtil.getShopBusinessStatus(openingTime, closingTime);
        }

        return StatusEnum.ENABLE_NOT.getStatus();
    }

    /**
     * 方法描述 : 店铺获取店铺是否手动关闭营业状态
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 0:07
     * @return: Integer 营业状态：1-营业 2-打样
     */
    public Integer getBusinessStatusV5() {
        return businessStatus;
    }

    /**
     * 方法描述 : 获取店铺服务类型处理
     * 判断是否在 ：wx端展示，堂食展示
     *
     * @Author: MeiTao
     * @Date: 2020/7/6 0006 0:11
     * @return: java.lang.Integer 3: wx、堂食都可展示 ，2：堂食展示 ，1：wx端展示, 0：都不可以
     * @Since version-1.1.0
     */
    public Integer getShopServiceType() {
        if (!BaseUtil.judgeString(shopService)) {
            return ServiceTypeEnum.ALL_NOT.getStatus();
        }
        List<Integer> shopServiceList = JSON.parseArray(shopService, Integer.class);

        boolean containsWx = shopServiceList.contains(ServiceTypeEnum.WX.getStatus());
        boolean containsTs = shopServiceList.contains(ServiceTypeEnum.TS.getStatus());

        return containsWx ? (containsTs ? ServiceTypeEnum.WX_TS.getStatus() : ServiceTypeEnum.WX.getStatus()) : (containsTs ? ServiceTypeEnum.TS.getStatus() : ServiceTypeEnum.ALL_NOT.getStatus());
    }


    public List<String> getShopPictureList() {
        if (addressPicture == null) {
            return null;
        }
        return JSON.parseArray(addressPicture, String.class);
    }
}
