package org.example.authserver.client;

import org.example.authserver.client.bo.CheckPassWordBO;
import org.example.authserver.client.dto.CheckPassWordDTO;
import org.example.authserver.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author JQ
 */
@FeignClient(value = "sso-resourceserver", configuration = ResourceServerConfiguration.class, fallbackFactory = ResourceServerFallbackFactory.class)
public interface ResourceServerClient {

    /**
     * 登錄密碼驗證接口
     *
     * @param checkPassWordDTO
     * @return
     */
    @PostMapping("/auth/checkPassWord")
    public ResponseResult<CheckPassWordBO> checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
