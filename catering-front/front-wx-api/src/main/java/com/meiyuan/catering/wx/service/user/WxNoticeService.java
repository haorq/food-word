package com.meiyuan.catering.wx.service.user;

import com.meiyuan.catering.core.dto.user.Notice;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.notice.v101.NoticeDetailV101DTO;
import com.meiyuan.catering.user.fegin.notice.NoticeClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 广告服务
 *
 * @author zengzhangni
 * @date 2020-05-07
 */
@Service
public class WxNoticeService {
    @Resource
    private NoticeClient noticeClient;

    public Result<PageData<NoticeDetailV101DTO>> pageListV101(BasePageDTO dto) {
        return noticeClient.pageListWxV101(dto);
    }

    public Result<NoticeDetailV101DTO> detailByIdV101(Long id) {
        return noticeClient.detailByIdWxV101(id);
    }

    public Result pageViewAddV101(Long id) {
        Boolean update = noticeClient.pageViewAddV101(id).getData();
        return Result.logicResult(update);
    }

    public List<Notice> listV101(Integer size) {
        PageData<Notice> list = ClientUtil.getDate(noticeClient.listV101(size));
        return list.getList();
    }


}

