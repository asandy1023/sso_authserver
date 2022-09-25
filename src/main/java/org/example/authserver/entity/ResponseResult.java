package org.example.authserver.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JQ
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonPropertyOrder({"code", "message", "data"})
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 返回的對象
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    /**
     * 返回的編碼
     */
    private Integer code;
    /**
     * 返回的信息
     */
    private String message;

    /**
     * 成功返回成功響應碼
     *
     * @return 響應結果
     */
    public static ResponseResult<String> OK() {
        return packageObject("", GlobalCodeEnum.GL_SUCC_0000);
    }

    /**
     * 成功返回響應數據
     *
     * @param data 返回的數據
     * @param <T>  返回的數據類型
     * @return 響應結果
     */
    public static <T> ResponseResult<T> OK(T data) {
        return packageObject(data, GlobalCodeEnum.GL_SUCC_0000);
    }

    /**
     * 對返回的消息進行包裝
     *
     * @param data           返回的數據
     * @param globalCodeEnum 自定義的返回碼枚舉類型
     * @param <T>            返回的數據類型
     * @return 響應結果
     */
    public static <T> ResponseResult<T> packageObject(T data, GlobalCodeEnum globalCodeEnum) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(globalCodeEnum.getCode());
        responseResult.setMessage(globalCodeEnum.getDesc());
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 系統發生異常不可用時返回
     *
     * @param <T> 返回的數據類型
     * @return 響應結果
     */
    public static <T> ResponseResult<T> systemException() {
        return packageObject(null, GlobalCodeEnum.GL_FAIL_9999);
    }

    /**
     * 發現可感知的系統異常時返回
     *
     * @param globalCodeEnum
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> systemException(GlobalCodeEnum globalCodeEnum) {
        return packageObject(null, globalCodeEnum);
    }
}

