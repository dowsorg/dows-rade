package org.dows.modules.log.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.config.IgnoredUrlsProperties;
import org.dows.core.config.LogProperties;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.security.SecurityProvider;
import org.dows.core.util.IPUtils;
import org.dows.core.util.PathUtils;
import org.dows.modules.log.entity.BaseSysLogEntity;
import org.dows.modules.log.mapper.BaseSysLogMapper;
import org.dows.modules.log.service.BaseSysLogService;
import org.dows.modules.sys.service.BaseSysConfService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executor;

/**
 * 系统日志
 */
@RequiredArgsConstructor
@Service
public class BaseSysLogServiceImpl extends BaseServiceImpl<BaseSysLogMapper, BaseSysLogEntity>
        implements BaseSysLogService {

    private final BaseSysConfService baseSysConfService;

    private final IgnoredUrlsProperties ignoredUrlsProperties;

    private final IPUtils ipUtils;

    private final LogProperties logProperties;

    private final Executor logTaskExecutor;


    private final SecurityProvider securityProvider;

    @Override
    public Object page(JSONObject requestParams, Page<BaseSysLogEntity> page, QueryWrapper queryWrapper) {
        /*queryWrapper
                .select(BaseSysLogEntityTableDef.BASE_SYS_LOG_ENTITY.ALL_COLUMNS, BaseSysUserEntityTableDef.BASE_SYS_USER_ENTITY.NAME)
                .from(BaseSysLogEntityTableDef.BASE_SYS_LOG_ENTITY)
                .leftJoin(BaseSysUserEntityTableDef.BASE_SYS_USER_ENTITY)
                .on(BaseSysLogEntity::getUserId, BaseSysUserEntity::getId);*/
        return mapper.paginate(page, queryWrapper);
    }

    @Override
    public void clear(boolean isAll) {
        if (isAll) {
            this.remove(QueryWrapper.create().ge(BaseSysLogEntity::getId, 0));
        } else {
            String keepDay = baseSysConfService.getValue("logKeep");
            int keepDays = Integer.parseInt(StrUtil.isNotEmpty(keepDay) ? keepDay : "30");
            Date beforeDate = DateUtil.offsetDay(new Date(), -keepDays);
            this.remove(QueryWrapper.create().lt(BaseSysLogEntity::getCreateTime, beforeDate));
        }
    }

    @Override
    public void record(HttpServletRequest request, JSONObject requestParams) {
        String requestURI = request.getRequestURI();
        if (isIgnoreUrl(requestURI)) {
            // 配置了忽略记录请求日志
            return;
        }
        String ipAddr = ipUtils.getIpAddr(request);
        // 异步记录日志
        recordAsync(ipAddr, requestURI, requestParams);
    }

    private boolean isIgnoreUrl(String requestURI) {
        return PathUtils.isMatch(ignoredUrlsProperties.getLogUrls(), requestURI);
//        return PathUtils.isMatch(securityProvider.getLogUrls(), requestURI);
    }

    public void recordAsync(String ipAddr, String requestURI, JSONObject requestParams) {
        logTaskExecutor.execute(() -> {
            JSONObject userInfo = securityProvider.getAdminUserInfo(requestParams);

            Long userId = null;
            if (userInfo != null) {
                userId = userInfo.getLong("userId");
            }

            JSONObject newJSONObject = JSONUtil.parseObj(JSONUtil.toJsonStr(requestParams));
            newJSONObject.remove("tokenInfo");
            newJSONObject.remove("refreshToken");
            newJSONObject.remove("body");
            if (newJSONObject.toString().getBytes().length > logProperties.getMaxByteLength()) {
                // 超过指定
                newJSONObject.clear();
            }
            BaseSysLogEntity logEntity = new BaseSysLogEntity();
            logEntity.setAction(requestURI);
            logEntity.setIp(ipAddr);
            if (userId != null) {
                logEntity.setUserId(userId);
            }
            logEntity.setParams(newJSONObject);
            save(logEntity);

        });
    }
}
