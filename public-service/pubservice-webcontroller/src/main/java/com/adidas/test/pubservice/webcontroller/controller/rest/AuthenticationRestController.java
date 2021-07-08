package com.adidas.test.pubservice.webcontroller.controller.rest;

import com.adidas.test.pubservice.common.error.ApplicationException;
import com.adidas.test.pubservice.common.model.security.AuthUser;
import com.adidas.test.pubservice.common.model.security.TokenUser;
import com.adidas.test.pubservice.common.security.JWTUtils;
import com.adidas.test.pubservice.domain.service.security.util.ServUserUtil;
import com.adidas.test.pubservice.webcontroller.util.ApplicationConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationConstants.API_VERSION + "/authentication")
public class AuthenticationRestController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping(
      value = "/",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "register", notes = "Register user and creates JWT Token")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<AuthToken> register(
      @ApiParam(value = "Credentials for getting the jwt Token", required = true) @RequestBody(required = true) final TokenUser tokenUser
  ) throws ApplicationException {
    if (tokenUser == null
        || tokenUser.getUsername() == null
        || tokenUser.getPassword() == null) {
      throw new ApplicationException("authentication failed: not enough parameters");
    }

    String encPass = ServUserUtil.encodeHexHash(tokenUser.getPassword());
    tokenUser.setPassword(encPass);
    //tokenUser.setPassword(new BCryptPasswordEncoder().encode(tokenUser.getPassword()));

    //Autenticación por password (real o nopass(sso))
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            tokenUser.getUsername(),
            tokenUser.getPassword()
        )
    );

    //TokenUser tokenUserAuthenticated = null;
    AuthToken authToken = null;

    Object principal = authentication.getPrincipal();
    if (principal instanceof AuthUser) {
      //tokenUserAuthenticated = generateTokenFromAuth((AuthUser) principal);
      authToken = generateTokenFromAuth((AuthUser) principal);
    }

    /*if (tokenUserAuthenticated != null) {
      return new ResponseEntity<>(tokenUserAuthenticated, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }*/

    if (authToken != null) {
      return new ResponseEntity<>(authToken, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  private AuthToken generateTokenFromAuth(AuthUser authUser) {
    TokenUser tokenUser = new TokenUser();

    tokenUser.setUsername(authUser.getUsername());
    tokenUser.setPassword(null);

    Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

    if (authorities != null && authorities.size() > 0) {
      List<String> permissionsList = authorities.stream().map(simpleGrantedAuthority -> {
        return simpleGrantedAuthority.getAuthority();
      }).collect(Collectors.toList());
      tokenUser.setPermissions(permissionsList.toArray(new String[0]));
    }
    String token = JWTUtils.codeToken(tokenUser);
    AuthToken authToken = new AuthToken();
    authToken.setToken(token);
    authToken.setUser(tokenUser);
    return authToken;
    //return tokenUser;
  }

  @Data
  private class AuthToken {

    String token;
    TokenUser user;
  }

}
