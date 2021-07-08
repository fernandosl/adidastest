package com.adidas.test.pubservice.common.model.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 * Auth user for security context user, used in webcontroller app
 */
public class AuthUser extends org.springframework.security.core.userdetails.User {

  private String jwtTokenHeader;

  public AuthUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
  }

  public String getJwtTokenHeader() {
    return this.jwtTokenHeader;
  }

  public void setJwtTokenHeader(String jwtTokenHeader) {
    this.jwtTokenHeader = jwtTokenHeader;
  }
}