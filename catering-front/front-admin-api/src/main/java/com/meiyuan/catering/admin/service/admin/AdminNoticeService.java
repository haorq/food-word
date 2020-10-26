package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeQueryV101DTO;
import com.meiyuan.catering.user.dto.notice.v101.NoticeSaveV101DTO;
import com.meiyuan.catering.user.fegin.notice.NoticeClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 广告服务
 *
 * @author zengzhangni
 * @date 2020-05-06
 */
@Service
public class AdminNoticeService {
    @Resource
    private NoticeClient noticeClient;

    public Result saveV101(NoticeSaveV101DTO dto) {
        return noticeClient.saveV101(dto);
    }

    public Result<PageData<NoticeDetailV101DTO>> pageListV101(NoticeQueryV101DTO dto) {
        return noticeClient.pageListV101(dto);
    }

    public Result<NoticeDetailV101DTO> detailByIdV101(Long id) {
        return noticeClient.detailByIdV101(id);
    }

    public Result deleteByIdV101(Long id) {
        boolean del = ClientUtil.getDate(noticeClient.removeById(id));
        return Result.logicResult(del);
    }

    public Result publishByIdV101(Long id) {
        Boolean date = ClientUtil.getDate(noticeClient.publishByIdV101(id));
        return Result.logicResult(date);
    }

    public Result stickByIdV101(Long id, Boolean isStick) {
        Boolean date = ClientUtil.getDate(noticeClient.stickByIdV101(id, isStick));
        return Result.logicResult(date);
    }
}

