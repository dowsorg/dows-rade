package org.dows.core.status;

import lombok.Setter;
import lombok.ToString;

/**
 * 请求相关状态码
 */
@ToString
public enum ReqeustStatusCode implements StatusCode {

    REQUEST_LIMIT(210001, "请求次数受限");


    private final String code;
    @Setter
    private String descr;

    ReqeustStatusCode(Integer code, String message) {
        this.code = code.toString();
        this.descr = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return descr;
    }

}
