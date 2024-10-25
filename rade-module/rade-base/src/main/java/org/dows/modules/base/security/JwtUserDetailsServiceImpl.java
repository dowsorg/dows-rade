package org.dows.modules.base.security;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.security.jwt.JwtUser;
import org.dows.modules.base.entity.sys.BaseSysUserEntity;
import org.dows.modules.base.service.sys.BaseSysPermsService;
import org.dows.modules.base.service.sys.BaseSysUserService;
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
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    final private BaseSysUserService baseSysUserService;
    final private BaseSysPermsService baseSysPermsService;
    final private RadeCache radeCache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseSysUserEntity sysUserEntity = baseSysUserService.getMapper().selectOneByQuery(
                QueryWrapper.create().eq(BaseSysUserEntity::getUsername, username)
                        .eq(BaseSysUserEntity::getStatus, 1));
        if (ObjectUtil.isEmpty(sysUserEntity)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<GrantedAuthority> authority = new ArrayList<>();
        String[] perms = baseSysPermsService.getPerms(sysUserEntity.getId());
        for (String perm : perms) {
            authority.add(new SimpleGrantedAuthority(perm));
        }
        Long[] departmentIds = baseSysPermsService.getDepartmentIdsByRoleIds(sysUserEntity.getId());
        JwtUser jwtUser = new JwtUser(sysUserEntity.getId(), sysUserEntity.getUsername(), sysUserEntity.getPassword(),
                authority,
                sysUserEntity.getStatus() == 1);
        Long[] roleIds = baseSysPermsService.getRoles(sysUserEntity);
        radeCache.set("admin:userDetails:" + jwtUser.getUsername(), jwtUser);
        radeCache.set("admin:passwordVersion:" + sysUserEntity.getId(),
                sysUserEntity.getPasswordV());
        radeCache.set("admin:userInfo:" + sysUserEntity.getId(), sysUserEntity);
        radeCache.set("admin:department:" + sysUserEntity.getId(), departmentIds);
        radeCache.set("admin:roleIds:" + sysUserEntity.getId(), roleIds);
        return jwtUser;
    }
}
