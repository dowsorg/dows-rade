package org.dows.modules.aac;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.UserInfo;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.security.SecurityUser;
import org.dows.core.security.SecurityUserRefresh;
import org.dows.modules.rbac.service.BaseSysPermsService;
//import org.dows.modules.uat.user.entity.BaseSysUserEntity;
//import org.dows.modules.uat.user.service.BaseSysUserService;
import org.dows.security.jwt.JwtUser;
import org.dows.core.uat.UserProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获得用户信息
 */
@Component
@RequiredArgsConstructor
public class JwtSecurityUserServiceImpl implements UserDetailsService, SecurityUserRefresh {

    final private BaseSysPermsService baseSysPermsService;
    final private RadeCache radeCache;

    final private UserProvider userProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*BaseSysUserEntity sysUserEntity = baseSysUserService.getMapper().selectOneByQuery(
                QueryWrapper.create().eq(BaseSysUserEntity::getUsername, username)
                        .eq(BaseSysUserEntity::getStatus, 1));*/
        SecurityUser sysUserEntity = userProvider.getUserInfoByUsername(username);

        if (ObjectUtil.isEmpty(sysUserEntity)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (sysUserEntity.getStatus() != 1) {
            throw new UsernameNotFoundException("用户名已被禁用");
        }
        List<GrantedAuthority> authority = new ArrayList<>();
        String[] perms = baseSysPermsService.getPerms(sysUserEntity.getId());
        for (String perm : perms) {
            authority.add(new SimpleGrantedAuthority(perm));
        }
        Long[] departmentIds = baseSysPermsService.getDepartmentIdsByRoleIds(sysUserEntity.getId());
        JwtUser jwtUser = new JwtUser(sysUserEntity.getId(), sysUserEntity.getUsername(), sysUserEntity.getPassword(),
                authority, sysUserEntity.getStatus() == 1);
        Long[] roleIds = baseSysPermsService.getRoles(sysUserEntity);
        radeCache.set("admin:userDetails:" + jwtUser.getUsername(), jwtUser);
        radeCache.set("admin:passwordVersion:" + sysUserEntity.getId(), sysUserEntity.getPasswordV());
        radeCache.set("admin:userInfo:" + sysUserEntity.getId(), sysUserEntity);
        radeCache.set("admin:department:" + sysUserEntity.getId(), departmentIds);
        radeCache.set("admin:roleIds:" + sysUserEntity.getId(), roleIds);
        return jwtUser;
    }

    @Override
    public void refreshUserDetails(String username) {
        loadUserByUsername(username);
    }
}
