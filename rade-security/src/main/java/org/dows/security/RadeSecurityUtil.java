package org.dows.security;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import org.dows.core.cache.RadeCache;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.security.SecurityDetail;
import org.dows.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Security 工具类
 */
public class RadeSecurityUtil {

    private static final RadeCache RADE_CACHE = SpringUtil.getBean(RadeCache.class);

    /***************后台********************/
    /**
     * 获取后台登录的用户名
     */
    public static String getAdminUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获得jwt中的信息
     *
     * @param requestParams 请求参数
     * @return jwt
     */
    public static JSONObject getAdminUserInfo(JSONObject requestParams) {
        JSONObject tokenInfo = requestParams.getJSONObject("tokenInfo");
        if (tokenInfo != null) {
            tokenInfo.set("department",
                    RADE_CACHE.get("admin:department:" + tokenInfo.get("userId")));
            tokenInfo.set("roleIds", RADE_CACHE.get("admin:roleIds:" + tokenInfo.get("userId")));
        }
        return tokenInfo;
    }

    /**
     * 后台账号退出登录
     *
     * @param adminUserId 用户ID
     * @param username    用户名
     */
    public static void adminLogout(Long adminUserId, String username) {
        RADE_CACHE.del("admin:department:" + adminUserId, "admin:passwordVersion:" + adminUserId,
                "admin:userInfo:" + adminUserId, "admin:userDetails:" + username);
    }

    /**
     * 后台账号退出登录
     *
     * @param userEntity 用户
     */
    public static void adminLogout(SecurityDetail userEntity) {
        adminLogout(userEntity.getId(), userEntity.getUsername());
    }


    /**
     * 获取当前用户id
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((JwtUser) principal).getUserId();
            }
        }
        RadePreconditions.check(true, 401, "未登录");
        return null;
    }

    /**
     * 获取当前用户类型
     */
    public static UserTypeEnum getCurrentUserType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((JwtUser) principal).getUserTypeEnum();
            }
        }
        // 还未登录,未知类型
        return UserTypeEnum.UNKNOWN;
    }

    /**
     * app退出登录,移除缓存信息
     */
    public static void appLogout() {
        RADE_CACHE.del("app:userDetails" + getCurrentUserId());
    }
}
