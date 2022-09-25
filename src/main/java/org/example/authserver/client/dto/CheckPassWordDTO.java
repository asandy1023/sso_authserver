package org.example.authserver.client.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author JQ
 */
@Data
@Builder
public class CheckPassWordDTO {

    /**
     * 登錄賬號
     */
    private String userName;
}
