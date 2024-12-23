package org.dows.core.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.Skip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对参数进行签名
 * sxpcwlkj.com
 */
public class SignUtil {
    private static Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 获取秘钥
     *
     * @return
     */
    public static String getAseKey() {
        //随机生成密钥  128位   256位
        //需要一个长度为16的字符串 16*8=128 bit    32*8=256 bit
        String key = RandomUtil.randomString(32);
        System.err.println("生成1个256bit的加密key:" + key);
        return key;
    }

    /**
     * 加签
     * 对paramValues进行签名，其中ignoreParamNames这些参数不参与签名
     *
     * @param paramNames 参数
     * @param key        秘钥
     * @return
     */
    public static String signAes(Map<String, Object> paramNames, List<String> ignoreParamNames, String key) {
        //构建
        try {

            // 顺序按照首字母ASCII码进行升序排列
            String content = params2ASCLL(paramNames, ignoreParamNames);
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, str2byte(key));
            //加密
            byte[] encrypt = aes.encrypt(content);
            return byte2str(encrypt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验签/解密
     *
     * @param key              秘钥
     * @param requestParams    源串
     * @param ignoreParamNames 忽略属性
     * @return
     */
    public static Boolean verifyAes(String key, Map<String, Object> requestParams, List<String> ignoreParamNames) {
        //构建
        try {
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("秘钥不能为空!");
            }
            if (!mapIsEmpty(requestParams, "sign")) {
                log.info("sign,不能为空！");
                throw new RuntimeException("验签失败!");
            }
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, str2byte(key));
            //加密
            byte[] content = aes.decrypt(requestParams.get("sign").toString());
            //加密为16进制表示
            String encryptHex = aes.encryptHex(content);
            //解密为字符串
            String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
            String oid_str = params2ASCLL(requestParams, ignoreParamNames);
            return oid_str.equals(decryptStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =============================== 非对称加密 ==============================

    public static RSA getRsaKey() {
        RSA rsa = new RSA();
        //获得私钥
        rsa.getPrivateKey();
        rsa.getPrivateKeyBase64();
        //获得公钥
        rsa.getPublicKey();
        rsa.getPublicKeyBase64();
        return rsa;
    }

    /**
     * 非对称
     * 签名处理
     * 公钥加密
     *
     * @param publicKey：私钥文件
     * @param paramValues：签名源内容,请求字段
     * @return
     */
    public static String signRsa(String publicKey, Map<String, Object> paramValues, List<String> ignoreParamNames) {
        try {
            // 顺序按照首字母ASCII码进行升序排列
            String content = params2ASCLL(paramValues, ignoreParamNames);
            RSA rsa = new RSA(null, publicKey);
            byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
            return byte2str(encrypt);
        } catch (RuntimeException e) {
            log.error("签名失败{}", e.getMessage());
        }
        return null;
    }

    /**
     * 非对称
     * 签名验证
     *
     * @param privateKey：公钥
     * @param requestParams：请求数据
     * @param ignoreParamNames：签名过滤属性（默认过滤：sign）
     * @return
     */
    public static Boolean verifyRsa(String privateKey, Map<String, Object> requestParams, List<String> ignoreParamNames) throws RuntimeException {
        try {
            if (StringUtils.isEmpty(privateKey)) {
                throw new RuntimeException("私钥不能为空!");
            }
            RSA rsa = new RSA(privateKey, null);
            if (!mapIsEmpty(requestParams, "sign")) {
                log.info("sign,不能为空！");
                throw new RuntimeException("验签失败!");
            }
            byte[] aByte = HexUtil.decodeHex(requestParams.get("sign").toString());
            byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);
            String stred = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
            String oid_str = params2ASCLL(requestParams, ignoreParamNames);
            return oid_str.equals(stred);
        } catch (Exception e) {
            log.error("签名验证异常{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //============================ 工具类 =============================

    private static byte[] str2byte(String data) throws IOException {
        return data.getBytes();
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2str(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }


    /**
     * 参数排序
     *
     * @param params
     * @return
     */
    public static String params2ASCLL(Map<String, Object> params, List<String> ignoreParamNames) throws RuntimeException {

        if (params == null || params.size() == 0) {
            return null;
        }

        if (params.containsKey("sign")) {
            params.remove("sign");
        }

        // 排除不需要签名的参数
        if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
            for (String ignoreParamName : ignoreParamNames) {
                params.remove(ignoreParamName);
            }
        }

        if (params.containsKey("timestamp")) {
            if (isExceedTheTimeLimit(params.get("timestamp").toString())) {
                throw new RuntimeException("请求时间戳已过期.");
            }
        }
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);// 排序请求参数

        StringBuilder s2 = new StringBuilder();
        for (String key : sortedKeys) {
            s2.append(key).append("=").append(params.get(key)).append("&");
        }
        s2.deleteCharAt(s2.length() - 1);
        //System.out.println(s2);
        return s2.toString();
    }


    /**
     * 类，获取所有参数
     *
     * @param object 对象
     * @return
     */
    public static Map<String, Object> getAllParams(Object object) {
        Map<String, Object> params = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!"serialVersionUID".equals(f.getName())) {
                params.put(f.getName(), String.valueOf(getFieldValueByName(f.getName(), object)));
            }
        }
        return params;
    }

    /**
     * 类，根据属性名获取属性值
     *
     * @param fieldName 属性名
     * @param object    对象
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object object) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 类，获取需要忽略签名的参数名
     *
     * @param classType 类
     * @return
     * @IgnoreSign 自定义注解，可以获取到需要忽略签名的参数名
     */
    public static List<String> getIgnoreSignParmas(Class classType) {
        final ArrayList<String> igoreSignFieldNames = new ArrayList<String>();
        if (classType != null) {
            ReflectionUtils.doWithFields(classType, new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    igoreSignFieldNames.add(field.getName());
                }
            }, new ReflectionUtils.FieldFilter() {
                public boolean matches(Field field) {
                    //属性类标注了@IgnoreSign
                    Skip typeIgnore = AnnotationUtils.findAnnotation(field.getType(), Skip.class);
                    //属性定义处标注了@IgnoreSign
                    Skip varIgnoreSign = field.getAnnotation(Skip.class);
                    return typeIgnore != null || varIgnoreSign != null;
                }
            });
            if (igoreSignFieldNames.size() > 1) {
                System.out.println(classType.getCanonicalName() + "不需要签名的属性:" + igoreSignFieldNames.toString());
            }
        }
        return igoreSignFieldNames;
    }

    /**
     * 允许客户端请求时间误差为10分钟
     */
    public static boolean isExceedTheTimeLimit(String timeStamp) {
        if (timeStamp == null || timeStamp.isEmpty() || timeStamp.equals("null")) {
            throw new RuntimeException("缺少请求时间戳[timestamp].");
        }
        return System.currentTimeMillis() - Long.parseLong(timeStamp) > 600000;
    }

    public static Map<String, Object> requestToMap(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 判断Map中是否存在这个属性
     *
     * @param map
     * @param key
     * @return
     */
    public static Boolean mapIsEmpty(Map<String, Object> map, String key) {
        if (!map.containsKey(key) || StringUtils.isEmpty(map.get(key).toString())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    //=======================  测试 ========================


    public static void main(String[] args) throws Exception {

        //根据一个对象，获取所有参数
        // new T() 是反之一个对象，new T()，User 等
        //T 是一个类 t=new Member();

        Object member = new Object();


        Map<String, Object> parmaAll = getAllParams(member);
        //需要忽略的参数
        List<String> ignoreSignParmas = getIgnoreSignParmas(Object.class);


        // test1();

        test2();
    }

    /**
     * 对称加密
     * 秘钥加密解密
     *
     * @throws Exception
     */
    public static void test1(Map<String, Object> parma, List<String> ignoreSignParmas) throws Exception {
        String appId = "xxx";
        // 生成一个新秘钥
        String key = getAseKey();

        // 设置秘钥
        key = "dRH46LEzma4O5wAzjLA0U2spJaIAIAH5";
        String apiUrl = "http://xxxxxxxxx";


        //参数签名测试例子
        Map<String, Object> data = new HashMap<>();

        data.put("userId", "13389186557");
        data.put("otcCode", "10000");
        data.put("pickupAmount", "10");

        data.put("appid", appId);
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));

        // 对称加密,加签
        String signStr = signAes(data, null, key);
        System.out.println(signStr);

        // ========对称加密,验签（请求的服务器端验签）==========
        Boolean b = verifyAes(key, data, null);
        if (!b) {
            throw new RuntimeException("验签失败");
        } else {
            log.info("验签成功");
        }
        // ========对称加密,验签（请求的服务器端验签）==========

        // 把签名结果放入请求参数中
        data.put("sign", signStr);
        //请求
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("appid", appId);

        //发送get请求并接收响应数据
        //String result = HttpUtil.createGet(apiUrl).addHeaders(headers).form(data).execute().body();
        //发送post请求并接收响应数据
        String result = HttpUtil.createPost(apiUrl).addHeaders(headers).form(data).execute().body();
        System.out.println(result);
    }


    /**
     * 非对称加密
     * 公钥加密，私钥解密
     *
     * @throws Exception
     */
    public static void test2() throws Exception {
        String appId = "xxx";
        String apiUrl = "http://xxxxxxxxx";
        RSA rsaKey = getRsaKey();
        String publicKey = rsaKey.getPublicKeyBase64();
        String privateKey = rsaKey.getPrivateKeyBase64();


        //参数签名测试例子
        Map<String, Object> data = new HashMap<>();

        data.put("userId", "13389186557");
        data.put("otcCode", "10000");
        data.put("pickupAmount", "10");

        data.put("appid", appId);
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));

        // 对称加密,加签
        String signStr = signRsa(publicKey, data, null);

        System.out.println(signStr);

        // ========对称加密,验签（请求的服务器端验签）==========
        Boolean b = verifyRsa(privateKey, data, null);
        if (!b) {
            throw new RuntimeException("验签失败");
        } else {
            log.info("验签成功");
        }
        // ========对称加密,验签（请求的服务器端验签）==========

        // 把签名结果放入请求参数中
        data.put("sign", signStr);

        //请求
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        headers.put("appid", appId);

        //发送get请求并接收响应数据
        //String result = HttpUtil.createGet(apiUrl).addHeaders(headers).form(data).execute().body();
        //发送post请求并接收响应数据
        String result = HttpUtil.createPost(apiUrl).addHeaders(headers).form(data).execute().body();
        System.out.println(result);

    }

}
