package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeQueryV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeSaveV101DTO;
import com.meiyuan.catering.user.entity.CateringNoticeEntity;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
public interface CateringNoticeService extends IService<CateringNoticeEntity> {
    /**
     * 描述:分页列表
     *
     * @param dto
     * @return
     * @author zengzhangni
     * @date 2020/5/6 15:13
     */
    PageData<NoticeDetailV101DTO> pageListV101(NoticeQueryV101DTO dto);

    /**
     * 描述:添加浏览量
     *
     * @param id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/7 10:43
     */
    Boolean pageViewAddV101(Long id);

    /**
     * 描述:添加
     *
     * @param dto
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/21 16:55
     * @since v1.1.0
     */
    Boolean saveV101(NoticeSaveV101DTO dto);

    /**
     * 描述:详情
     *
     * @param id
     * @return com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @since v1.1.0
     */
    NoticeDetailV101DTO detailByIdV101(Long id);

    /**
     * 描述:发布
     *
     * @param id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @since v1.1.0
     */
    Boolean publishByIdV101(Long id);

    /**
     * 描述:置顶
     *
     * @param id
     * @param isStick
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @since v1.1.0
     */
    Boolean stickByIdV101(Long id, Boolean isStick);

    /**
     * 描述:微信分页
     *
     * @param dto
     * @return com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO>
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @since v1.1.0
     */
    PageData<NoticeDetailV101DTO> pageListWxV101(BasePageDTO dto);

    /**
     * 描述:微信详情
     *
     * @param id
     * @return com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @since v1.1.0
     */
    NoticeDetailV101DTO detailByIdWxV101(Long id);

    /**
     * 描述:微信首页公告
     *
     * @param size
     * @return com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.core.dto.user.Notice>
     * @author zengzhangni
     * @date 2020/5/21 17:44
     * @date 2020/6/23 13:59
     * @since v1.1.0
     * @since v1.1.1
     */
    PageData<Notice> listV101(Integer size);
}
