package com.adidas.test.pubservice.common.security;

import com.adidas.test.pubservice.common.error.ApiError;
import com.adidas.test.pubservice.common.model.security.AuthUser;
import com.adidas.test.pubservice.common.model.security.TokenUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final String HEADER_STRING = "jwttoken";
  private final String TOKEN_PREFIX = "Bearer ";

  @Autowired
  private Environment environment;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);
    TokenUser tokenUser = null;
    String authToken = null;
    if (header != null && header.startsWith(TOKEN_PREFIX)) {
      authToken = header.replace(TOKEN_PREFIX, "");
      try {
        tokenUser = JWTUtils.decodeToken(authToken);
      } catch (IllegalArgumentException e) {
        logger.error("Error recoverint user from token", e);
        writeTokenErrorResponse(e, res);
        return;
      } catch (ExpiredJwtException e) {
        logger.warn("Token expired", e);
        writeTokenErrorResponse(e, res);
        return;
      } catch (SignatureException e) {
        logger.error("Non trusted token signature");
        writeTokenErrorResponse(e, res);
        return;
      }
    } else {
      logger.warn("JWT not found in header");
    }
    if (tokenUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = loadUserFromToken(tokenUser);

      if (JWTUtils.verifyPreAccesToken(authToken)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

        ((AuthUser) authentication.getPrincipal()).setJwtTokenHeader(authToken);

        logger.info("User succesfully authenticated with jwttoken : " + tokenUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    chain.doFilter(req, res);
  }

  private void writeTokenErrorResponse(Exception e, HttpServletResponse res)
      throws IOException, ServletException {
    ApiError error = new ApiError(HttpStatus.FORBIDDEN, "Error de token",
        e.getLocalizedMessage(), null, null);
    error.setSubCode("INVALID_TOKEN");

    res.setStatus(error.getHttpStatus().value());

    ObjectMapper mapper = new ObjectMapper();
    PrintWriter out = res.getWriter();
    out.print(mapper.writeValueAsString(error));
    out.flush();
  }

  private String generateTokenUser(String username) {
    TokenUser tokenUser = new TokenUser();
    tokenUser.setUsername(username);

    List<String> permissions = new ArrayList<>();
    permissions.add("ADMIN");

    tokenUser.setPermissions(permissions.toArray(new String[]{}));

    return JWTUtils.codeToken(tokenUser);
  }

  public UserDetails loadUserFromToken(TokenUser tokenUser) {
    //List<SimpleGrantedAuthority> permissions = new ArrayList<SimpleGrantedAuthority>();

    List<SimpleGrantedAuthority> permissionsList = null;
    if (tokenUser.getPermissions() != null) {
      permissionsList = Arrays.stream(tokenUser.getPermissions()).map(permission -> {
        return new SimpleGrantedAuthority(permission);
      }).collect(Collectors.toList());
    }

    AuthUser user = new AuthUser(tokenUser.getUsername(),
        new BCryptPasswordEncoder().encode(""),
        permissionsList);

    return user;
  }
}
