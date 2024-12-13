package org.dows.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常处理
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RadeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;
    private Object data;

    public RadeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RadeException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RadeException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public RadeException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public RadeException(Object data) {
        this.data = data;
    }
}
