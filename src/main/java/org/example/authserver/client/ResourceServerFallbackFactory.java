package org.example.authserver.client;

import org.example.authserver.entity.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JQ
 */
@Slf4j
public class ResourceServerFallbackFactory implements FallbackFactory<ResourceServerClient> {

    @Override
    public ResourceServerClient create(Throwable cause) {
        return checkPassWordDTO -> {
            log.info("資源服務調用降級邏輯處理...");
            log.error(cause.getMessage());
            return ResponseResult.systemException();
        };
    }
}
