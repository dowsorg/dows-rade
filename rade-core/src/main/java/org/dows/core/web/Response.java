package org.dows.core.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.core.aac.AuthStatusCode;
import org.dows.core.status.ArgumentStatuesCode;
import org.dows.core.status.CommonStatusCode;
import org.dows.core.status.CrudStatusCode;
import org.dows.core.status.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serial;
import java.util.Date;
import java.util.HashMap;

/**
 * 返回信息
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
@Schema(name = "响应", title = "响应数据")
public class Response<T> extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(title = "状态码")
    //@ApiModelProperty(value = "状态码")
    private String code;
    @Schema(title = "描述")
    //@ApiModelProperty(value = "描述")
    private String descr;
    @Schema(title = "状态(成功:true,失败:false)")
    //@ApiModelProperty("状态(成功:true,失败:false)")
    private Boolean status = true;
    @Schema(title = "响应时间")
    //@ApiModelProperty(value = "响应时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp = new Date();

    @Schema(title = "响应数据")
    //@ApiModelProperty(value = "响应数据")
    private T data;

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

    /*public static Response ok() {
        return new Response();
    }

    public static Response ok(Object data) {
        return new Response().put("data", data);
    }*/

    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }




/*    public Response() {
        this.code = HttpStatus.OK.value();
    }*/


    public Response(String code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    public Response(String code, String descr, T data) {
        this.code = code;
        this.descr = descr;
        this.data = data;
    }


    public Response(Boolean status, String code, String descr, T data) {
        this.code = code;
        this.descr = descr;
        this.data = data;
        this.status = status;
    }

    public Response(Boolean status, StatusCode statusCode) {
        this.status = status;
        this.code = statusCode.getCode();
        this.descr = statusCode.getDescribe();
    }

    public Response(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.descr = statusCode.getDescribe();
    }

    public Response(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.descr = statusCode.getDescribe();
        this.data = data;

    }


    public static <T> Response<T> ok() {
        Response<T> api = new Response(CommonStatusCode.SUCCESS);
        api.setStatus(true);
        return api;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Response<T> ok(T data) {
        Response<T> api = new Response(CommonStatusCode.SUCCESS);
        api.setStatus(true);
        api.setData(data);
        return api;
    }


    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static <T> Response<T> failed(String code, String message) {
        return new Response<T>(false, code, message, null);
    }

    public static <T> Response<T> failed(StatusCode statusCode) {

        return new Response<T>(false, statusCode);
    }

    public static <T> Response<T> failed(String message) {
        return new Response<T>(false, CommonStatusCode.FAILED.getCode(), message, null);
    }


    /**
     * 创建失败
     * 删除失败
     * 更新失败
     * 查询失败
     */
    public static <T> Response<T> crudFailed(CrudStatusCode crudStatusCode) {
        Response<T> api = new Response(crudStatusCode);
        api.setStatus(false);
        return api;
    }


    /**
     * 参数验证失败返回结果
     *
     * @param argumentStatuesCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> validateFailed(ArgumentStatuesCode argumentStatuesCode) {
        Response<T> api = new Response(argumentStatuesCode);
        api.setStatus(false);
        return api;
    }

    /**
     * 未登录返回结果
     * 未授权返回结果
     * 其他 参考 AuthStatusCode
     *
     * @param authStatusCode
     * @param <T>
     * @return
     */
    public static <T> Response<T> authFailed(AuthStatusCode authStatusCode) {
        Response<T> api = new Response(authStatusCode);
        api.setStatus(false);
        return api;
    }

    public static <T> Response<T> ok(StatusCode statusCode) {
        return new Response<T>(statusCode.getCode(), statusCode.getDescribe(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Response<T> ok(T data, String message) {
        return new Response<T>(CommonStatusCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param statusCode 错误码
     */
    public static <T> Response<T> fail(StatusCode statusCode) {
        return new Response<T>(false, statusCode.getCode(), statusCode.getDescribe(), null);
    }

    /**
     * 失败返回结果
     *
     * @param statusCode 错误码
     * @param message    错误信息
     */
    public static <T> Response<T> fail(StatusCode statusCode, String message) {
        return new Response<T>(false, statusCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Response<T> fail(String message) {
        return new Response<T>(false, CommonStatusCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Response<T> fail() {
        return fail(CommonStatusCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Response<T> validateFailed() {
        return fail(ArgumentStatuesCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Response<T> validateFailed(String message) {
        return new Response<T>(ArgumentStatuesCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Response<T> unauthorized(T data) {
        return new Response<T>(AuthStatusCode.UNAUTHORIZED.getCode(), AuthStatusCode.UNAUTHORIZED.getDescribe(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Response<T> forbidden(T data) {
        return new Response<T>(AuthStatusCode.FORBIDDEN.getCode(), AuthStatusCode.FORBIDDEN.getDescribe(), data);
    }

    /**
     * 统一返回
     */
    public ResponseEntity<Response<T>> responseEntity() {
        return new ResponseEntity<Response<T>>(this, HttpStatus.valueOf(this.getCode()));
    }
}