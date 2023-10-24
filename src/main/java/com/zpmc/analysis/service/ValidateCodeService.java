package com.zpmc.analysis.service;

import com.zpmc.analysis.utils.ServerResponse;
import java.io.IOException;

public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    public ServerResponse createCapcha(String oldUuid) throws IOException;

    /**
     * 校验验证码
     */
    public Boolean checkCapcha(String code, String uuid);

}