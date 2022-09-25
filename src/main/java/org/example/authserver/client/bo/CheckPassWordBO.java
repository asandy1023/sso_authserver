package org.example.authserver.client.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author JQ
 */
@Data
@Builder
public class CheckPassWordBO {

    /**
     * 用戶名
     */
    private String userName;

    /**
     * 密碼
     */
    private String passWord;

    /**
     * Md5 偏移值salt
     */
    private String salt;

    /**
     * 用戶權限
     */
    private String authorities;

}
