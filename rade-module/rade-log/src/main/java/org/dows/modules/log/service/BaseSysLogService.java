package org.dows.modules.log.service;

import cn.hutool.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.crud.BaseService;
import org.dows.modules.log.entity.BaseSysLogEntity;

/**
 * 系统日志
 */
public interface BaseSysLogService extends BaseService<BaseSysLogEntity> {
    /**
     * 清理日志
     *
     * @param isAll 是否全部清除
     */
    void clear(boolean isAll);

    /**
     * 日志记录
     *
     * @param requestParams 请求参数
     * @param request       请求
     */
    void record(HttpServletRequest request, JSONObject requestParams);
}
