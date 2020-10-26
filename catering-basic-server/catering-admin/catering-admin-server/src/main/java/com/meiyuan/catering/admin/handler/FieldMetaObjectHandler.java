package com.meiyuan.catering.admin.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.core.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公共字段，自动填充值
 * @author admin
 */
@Slf4j
@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = "createTime";
    private final static String CREATOR = "createBy";
    private final static String UPDATE_DATE = "updateTime";
    private final static String UPDATER = "updateBy";
    private final static String DEL_FLAG = "del";
    private final static String MERCHANT_ID = "merchantId";
    private final static String CONTEXT_PATH = "/manage";
    private Long merchantId;

    @Value("${goods.merchant.id}")
    public void setMerchantId(Long id) {
        this.merchantId = id;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        //创建时间
        setInsertFieldValByName(CREATE_DATE, now, metaObject);
        //更新时间
        setInsertFieldValByName(UPDATE_DATE, now, metaObject);
        //删除标识
        setInsertFieldValByName(DEL_FLAG, DelEnum.NOT_DELETE.getFlag(), metaObject);
        String contextPath = Strings.EMPTY;
        if (HttpContextUtils.getHttpServletRequest() != null) {
            contextPath = HttpContextUtils.getHttpServletRequest().getContextPath();
        }
        // 后台请求
        if (CONTEXT_PATH.equals(contextPath)) {
            Object info = HttpContextUtils.getHttpServletRequest().getAttribute("info");
            if (info != null) {
                CateringAdmin admin = (CateringAdmin) info;
                setInsertFieldValByName(CREATOR, admin.getId(), metaObject);
                setInsertFieldValByName(UPDATER, admin.getId(), metaObject);
                setInsertFieldValByName(MERCHANT_ID, merchantId, metaObject);
            }
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新时间
        setUpdateFieldValByName(UPDATE_DATE, LocalDateTime.now(), metaObject);
        String contextPath = Strings.EMPTY;
        if (HttpContextUtils.getHttpServletRequest() != null) {
            contextPath = HttpContextUtils.getHttpServletRequest().getContextPath();
        }
        // 后台请求
        if (CONTEXT_PATH.equals(contextPath)) {
            Object info = HttpContextUtils.getHttpServletRequest().getAttribute("info");
            if (info != null) {
                CateringAdmin admin = (CateringAdmin) info;
                setUpdateFieldValByName(UPDATER, admin.getId(), metaObject);
            }

        }
    }

}
