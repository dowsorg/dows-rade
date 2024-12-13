package org.dows.security.provider;

import org.dows.security.PhoneCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class AacCellphoneCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PhoneCodeAuthenticationToken authenticationToken = (PhoneCodeAuthenticationToken) authentication;
        //根据手机号（Principal）去查用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        //将认证信息传入进去。
        PhoneCodeAuthenticationToken authenticationResult = new PhoneCodeAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        //将请求的信息传递Token中。
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PhoneCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}