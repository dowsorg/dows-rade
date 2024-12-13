package org.dows.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {
    //手机号
    private final Object phone;
    //验证码
    private Object code;


    public PhoneCodeAuthenticationToken(Object phone, Object code) {
        super(null);
        this.phone = phone;
        this.code = code;
        setAuthenticated(false);
    }

    public PhoneCodeAuthenticationToken(Object phone, Object code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.code = code;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}