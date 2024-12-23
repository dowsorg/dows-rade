package org.dows.core.aac;

import org.dows.core.status.StatusCode;

public enum AuthStatusCode implements StatusCode {
    UNAUTHORIZED(),
    FORBIDDEN;

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDescribe() {
        return null;
    }
}
