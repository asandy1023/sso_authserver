package org.example.authserver.entity;

/**
 * @author JQ
 */
public enum GlobalCodeEnum {
    /**
     * 全局返回碼定義
     */
    GL_SUCC_0000(0, "成功"),
    GL_FAIL_9996(996, "不支持的HttpMethod"),
    GL_FAIL_9997(997, "HTTP錯誤"),
    GL_FAIL_9998(998, "參數錯誤"),
    GL_FAIL_9999(999, "系統異常"),

    /**
     * 認證授權業務異常碼定義
     */
    BUSI_INVALID_GRANT(1001, "非法授权操作");

    /**
     * 編碼
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    GlobalCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根據編碼獲取枚舉類型
     *
     * @param code 編碼
     * @return
     */
    public static GlobalCodeEnum getByCode(String code) {
        //判空
        if (code == null) {
            return null;
        }
        //循環處理
        GlobalCodeEnum[] values = GlobalCodeEnum.values();
        for (GlobalCodeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

