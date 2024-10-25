package org.dows.core.exception;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 校验处理
 */
public class RadePreconditions {

    /**
     * 条件如果为真 就抛异常 如 RadePreconditions.check(StrUtil.isEmptyIfStr(name), 500,
     * "名称不能为空"); name 字段如果为 null或空字符串，就抛异常
     */
    public static void check(boolean flag, int code, String message, Object... arguments) {
        if (flag) {
            throw getRadeException(message, code, arguments);
        }
    }

    public static void check(boolean flag, String message, Object... arguments) {
        if (flag) {
            throw getRadeException(message, arguments);
        }
    }

    public static void alwaysThrow(String message, Object... arguments) {
        throw getRadeException(message, arguments);
    }

    private static RadeException getRadeException(String message, Object... arguments) {
        Optional<Object> first = Arrays.stream(arguments).filter(o -> o instanceof Throwable)
                .findFirst();
        return new RadeException(formatMessage(message, arguments), (Throwable) first.orElse(null));
    }

    private static RadeException getRadeException(String message, int code, Object... arguments) {
        Optional<Object> first = Arrays.stream(arguments).filter(o -> o instanceof Throwable)
                .findFirst();
        return new RadeException(formatMessage(message, arguments), code, (Throwable) first.orElse(null));
    }


    /**
     * 返回data
     */
    public static void returnData(boolean flag, Object data) {
        if (flag) {
            throw new RadeException(data);
        }
    }

    public static void returnData(Object data) {
        returnData(true, data);
    }

    /**
     * 对象如果为空 就抛异常
     */
    public static void checkEmpty(Object object, String message, Object... arguments) {
        check(ObjectUtil.isEmpty(object), formatMessage(message, arguments));
    }

    public static void checkEmpty(Object object) {
        check(ObjectUtil.isEmpty(object), "参数不能为空");
    }

    private static String formatMessage(String messagePattern, Object... arguments) {
        StringBuilder sb = new StringBuilder();
        int argumentIndex = 0;
        int placeholderIndex = messagePattern.indexOf("{}");
        while (placeholderIndex != -1) {
            sb.append(messagePattern, 0, placeholderIndex);
            if (argumentIndex < arguments.length) {
                sb.append(arguments[argumentIndex++]);
            } else {
                sb.append("{}"); // 如果参数不足，保留原样
            }
            messagePattern = messagePattern.substring(placeholderIndex + 2);
            placeholderIndex = messagePattern.indexOf("{}");
        }
        sb.append(messagePattern); // 添加剩余部分
        return sb.toString();
    }

    @Setter
    @Getter
    public static class ReturnData {
        private Integer type;
        private String message;

        public ReturnData(Integer type, String message, Object... arguments) {
            this.type = type;
            this.message = formatMessage(message, arguments);
        }
    }
}
