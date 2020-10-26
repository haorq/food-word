package com.meiyuan.catering.core.util.dada;

import lombok.Data;

/**
 * @author lh
 */
@Data
public class DaDaService {

    private String appId;
    private String appSecret;
    private Boolean isOnline;
    private String domain;
    private String sourceId;
}
