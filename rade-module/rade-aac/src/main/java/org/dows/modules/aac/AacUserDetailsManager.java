package org.dows.modules.aac;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.cache.RadeCache;
import org.dows.core.rbac.RbacProvider;
import org.dows.core.security.SecurityUser;
import org.dows.core.security.SecurityUserRefresh;
import org.dows.core.uat.UserProvider;
import org.dows.security.jwt.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AacUserDetailsManager implements UserDetailsManager , SecurityUserRefresh {

    final private RadeCache radeCache;

    final private RbacProvider rbacProvider;

    final private UserProvider userProvider;

    @Override
    public void createUser(UserDetails user) { throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 중복 검사
    @Override
    public boolean userExists(String username) {
        //return this.userRepository.findByUsername(username).isPresent();
        return false;
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        *//*Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return CustomUserPrincipal.create(user.get());
        }*//*
        throw new UsernameNotFoundException(MessageFormat.format("username {0} not found", username));
    }*/




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
        String[] perms = rbacProvider.getPerms(sysUserEntity.getId());
        for (String perm : perms) {
            authority.add(new SimpleGrantedAuthority(perm));
        }
        Long[] departmentIds = rbacProvider.getDepartmentIdsByRoleIds(sysUserEntity.getId());
        JwtUser jwtUser = new JwtUser(sysUserEntity.getId(), sysUserEntity.getUsername(), sysUserEntity.getPassword(),
                authority, sysUserEntity.getStatus() == 1);
        Long[] roleIds = rbacProvider.getRoles(sysUserEntity);
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