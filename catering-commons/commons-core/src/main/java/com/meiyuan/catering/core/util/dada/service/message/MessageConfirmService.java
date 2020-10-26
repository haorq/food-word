package com.meiyuan.catering.core.util.dada.service.message;

import com.meiyuan.catering.core.util.dada.config.UrlConstant;
import com.meiyuan.catering.core.util.dada.service.BaseService;

/**
 * @author lh
 */
public class MessageConfirmService extends BaseService {

    public MessageConfirmService(String params) {
        super(UrlConstant.MESSAGE_CONFIRM_URL, params);
    }
}
