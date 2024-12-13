package org.dows.api.aac;

import org.dows.api.RadeException;

public class AacException  extends RadeException {
    public AacException(String msg) {
        super(msg);
    }

    public AacException(String msg, Throwable e) {
        super(msg, e);
    }

    public AacException(String msg, int code) {
        super(msg, code);
    }

    public AacException(String msg, int code, Throwable e) {
        super(msg, code, e);
    }

    public AacException(Object data) {
        super(data);
    }
}