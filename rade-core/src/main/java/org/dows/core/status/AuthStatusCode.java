package org.dows.core.status;

public enum AuthStatusCode implements StatusCode {
    /**
     * 401
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    /**
     * 402
     */
    PASSWORD_ERROR(402, "用户名或密码错误"),
    /**
     * 403
     */
    FORBIDDEN(403, "没有相关权限"),

    JWT_EXPIRED(404, "token已过期"),
    JWT_INVALID(405, "token签名不合法"),


    PAYLOAD_SIGN_ERROR(406, "签名错误,详情:{ %s }"),
    TOKEN_VERIFY_ERROR(407, "%s"),
    KEY_GEN_ERROR(408, "%s"),
    TOKEN_PARSE_ERROR(409, "%s");


    private String code;
    private String descr;

    AuthStatusCode(Integer code, String descr) {
        this.code = code.toString();
        this.descr = descr;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescribe() {
        return descr;
    }


}
