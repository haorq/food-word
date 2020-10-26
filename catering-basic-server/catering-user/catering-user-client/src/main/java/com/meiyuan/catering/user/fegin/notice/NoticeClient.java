package com.meiyuan.catering.user.fegin.notice;

import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeQueryV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeSaveV101DTO;
import com.meiyuan.catering.user.service.CateringNoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020-05-06
 * @since v1.1.0
 */
@Service
public class NoticeClient {

    @Resource
    private CateringNoticeService service;

    /**
     * 描述:添加
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 16:57
     * @since v1.1.0
     */
    public Result<Boolean> saveV101(NoticeSaveV101DTO dto) {
        return Result.succ(service.saveV101(dto));
    }

    /**
     * 描述:分页列表
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO>>
     * @author zengzhangni
     * @date 2020/5/21 16:57
     * @since v1.1.0
     */
    public Result<PageData<NoticeDetailV101DTO>> pageListV101(NoticeQueryV101DTO dto) {
        return Result.succ(service.pageListV101(dto));
    }

    /**
     * 描述:详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO>
     * @author zengzhangni
     * @date 2020/5/21 17:42
     * @since v1.1.0
     */
    public Result<NoticeDetailV101DTO> detailByIdV101(Long id) {
        return Result.succ(service.detailByIdV101(id));
    }

    /**
     * 描述:删除
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<Boolean> removeById(Long id) {
        return Result.succ(service.removeById(id));
    }

    /**
     * 描述:发布
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<Boolean> publishByIdV101(Long id) {
        return Result.succ(service.publishByIdV101(id));
    }

    /**
     * 描述:置顶
     *
     * @param id
     * @param isStick
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<Boolean> stickByIdV101(Long id, Boolean isStick) {
        return Result.succ(service.stickByIdV101(id, isStick));
    }

    /**
     * 描述:微信分页
     *
     * @param dto
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData                                                                                                                               <                                                                                                                               com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO>>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<PageData<NoticeDetailV101DTO>> pageListWxV101(BasePageDTO dto) {
        return Result.succ(service.pageListWxV101(dto));
    }

    /**
     * 描述:微信详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<NoticeDetailV101DTO> detailByIdWxV101(Long id) {
        return Result.succ(service.detailByIdWxV101(id));
    }

    /**
     * 描述:添加浏览量
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<Boolean> pageViewAddV101(Long id) {
        return Result.succ(service.pageViewAddV101(id));
    }

    /**
     * 描述:首页公告
     *
     * @param size
     * @return com.meiyuan.catering.core.util.Result<com.baomidou.mybatisplus.core.metadata.IPage                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                               com.meiyuan.catering.user.entity.CateringNoticeEntity>>
     * @author zengzhangni
     * @date 2020/5/21 17:43
     * @since v1.1.0
     */
    public Result<PageData<Notice>> listV101(Integer size) {
        return Result.succ(service.listV101(size));
    }
}
