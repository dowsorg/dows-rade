package org.dows.core.status;

import lombok.Getter;

public enum MqStatusCode implements StatusCode {

    COMUSER_EXCEPTION(461000, "MQ消费异常:%s"),
    PRODUCER_EXCEPTION(461001, "MQ生产异常:%s");

    @Getter
    private String code;
    private String descr;

    MqStatusCode(Integer code, String descr) {
        this.code = code.toString();
        this.descr = descr;
    }

    @Override
    public String getDescribe() {
        return descr;
    }
}
