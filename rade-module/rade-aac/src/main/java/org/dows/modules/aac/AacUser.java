package org.dows.modules.aac;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class AacUser extends User implements UserDetails {
    @Getter
    @Setter
    private Long accountId;

    @Getter
    @Setter
    private List<Long> roleIds;

    @Getter
    @Setter
    private boolean superAccount;

    public AacUser(Long accountId, String username, String password, Collection<? extends GrantedAuthority> authorities,List<Long> roleIds,boolean superAccount)  {
        super(username, password, authorities);
        this.accountId = accountId;
        this.roleIds = roleIds;
        this.superAccount = superAccount;
    }
}