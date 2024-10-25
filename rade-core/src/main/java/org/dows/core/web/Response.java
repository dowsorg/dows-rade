package org.dows.core.web;

import java.util.HashMap;

/**
 * 返回信息
 */
public class Response extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Response() {
        put("code", 1000);
        put("message", "success");
    }

    public static Response error() {
        return error(1001, "请求方式不正确或服务出现异常");
    }

    public static Response error(String msg) {
        return error(1001, msg);
    }

    public static Response error(int code, String msg) {
        Response response = new Response();
        response.put("code", code);
        response.put("message", msg);
        return response;
    }

    public static Response okMsg(String msg) {
        Response response = new Response();
        response.put("message", msg);
        return response;
    }

    public static Response ok() {
        return new Response();
    }

    public static Response ok(Object data) {
        return new Response().put("data", data);
    }

    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}