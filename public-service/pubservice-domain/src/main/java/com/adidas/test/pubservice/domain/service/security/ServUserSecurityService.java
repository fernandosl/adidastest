package com.adidas.test.pubservice.domain.service.security;

import com.adidas.test.pubservice.common.model.security.AuthUser;
import com.adidas.test.pubservice.domain.entity.UserEntity;
import com.adidas.test.pubservice.domain.service.security.util.ServUserUtil;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServUserSecurityService implements UserDetailsService  {

  private static final Logger LOG = LoggerFactory
      .getLogger(ServUserSecurityService.class.getName());

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = null;

    // get user from db
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("test");
    userEntity.setPassword("pass");

    if("test".equals(username)) {
      Collection<? extends GrantedAuthority> permissions
          = Arrays.asList(
              new SimpleGrantedAuthority("ROLE_PUB_SUBSCRIBE"),
              new SimpleGrantedAuthority("ROLE_EMAIL_SEND_MESSAGE"));

      userDetails = new AuthUser(userEntity.getUsername(),
          new BCryptPasswordEncoder().encode(
              ServUserUtil.encodeHexHash(userEntity.getPassword())),
          permissions);
    }

    if (userDetails == null) {
      LOG.error("User " + username + " not found in app");
      throw new UsernameNotFoundException(
          "User " + username + " not found in app");
    }
    return userDetails;
  }
}
