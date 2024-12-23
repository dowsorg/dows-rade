package org.dows.core.status;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lait.zhang@gmail.com
 * @description: TODO
 * @weixin SH330786
 * @date 3/29/2022
 */
public enum RestStatusCode implements StatusCode {
    ReadResponseBodyException(11, "读取响应异常"),

    RetrofitBlockException(12, "RetrofitBlockException"),

    ServiceInstanceChooseException(13, "ServiceInstanceChooseException , No valid service instance selector, Please configure it!");


    @Getter
    final String code;
    @Setter
    @Getter
    String descr;

    RestStatusCode(Integer code, String descr) {
        this.code = code.toString();
        this.descr = descr;
    }

    @Override
    public String getDescribe() {
        return descr;
    }
}
