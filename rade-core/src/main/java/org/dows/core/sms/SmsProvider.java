package org.dows.core.sms;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.cache.RadeCache;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.RadePluginInvokers;
import org.dows.core.plugin.service.RadePluginService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 发送短信验证码</ br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 6:10 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class SmsProvider {
    private final RadePluginService radePluginService;
    private final RadeCache radeCache;

    /**
     * 发送短信验证码
     *
     * @param phone
     */
    public void sendVerifyCode(String phone, SendSceneEnum sendSceneEnum) {
        // 随机生成4位验证码
        String verifyCode = RandomUtil.randomNumbers(4);
        send(phone, verifyCode);
        radeCache.set(sendSceneEnum.name() + "_sms:" + phone, verifyCode, 60 * 10);
    }

    /**
     * 检查验证码
     *
     * @param phone
     * @param code
     * @return
     */
    public void checkVerifyCode(String phone, String code, SendSceneEnum sendSceneEnum) {
        String key = sendSceneEnum.name() + "_sms:" + phone;
        String cacheCode = radeCache.get(key, String.class);
        boolean flag = StrUtil.isNotEmpty(code) && code.equals(cacheCode);
        if (flag) {
            // 删除验证码
            radeCache.del(key);
        }
        RadePreconditions.check(!flag, "验证码错误");
    }

    /**
     * 发送短信
     *
     * @param phone
     * @param code
     */
    public void send(String phone, String code) {
        List<String> phones = new ArrayList<>();
        phones.add(phone);

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        // 插件key sms-tx、sms-ali，哪个实例存在就调用哪个
        if (radePluginService.getInstanceWithoutCheck("sms-tx") != null) {
            // 调用腾讯短信插件
            RadePluginInvokers.invoke("sms-tx", "send", phones, params);
        } else if (radePluginService.getInstanceWithoutCheck("sms-ali") != null) {
            // 调用阿里短信插件
            RadePluginInvokers.invoke("sms-ali", "send", phones, params);
        } else {
            // 未找到短信插件
            log.error("未找到短信插件，请前往插件市场下载安装");
        }
    }
}

