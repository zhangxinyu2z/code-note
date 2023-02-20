package com.br.zz.business;

import lombok.Data;

/**
 * @author xinyu.zhang
 * @since 2023/2/9 18:28
 */
@Data
public class OpenApiConfigure {


    /**
     * 搜索链接
     */
    private String searchUrl;
    /**
     * 基本信息
     */
    private String baseInfoUrl;

    /**
     *详细信息
     */
    private String detailInfoUrl;

    /**
     * 主要人员
     */
    private String mainStaffUrl;
    /**
     * 股东信息
     */
    private String holderInfoUrl;
    /**
     * 变更记录
     */
    private String changeRecordUrl;

    /**
     * token
     * @return
     */
    private String token;

    /**
     * 探迹secretId
     */
    private String secretId;

    /**
     * 探迹secretKey
     */
    private String secretKey;
}
