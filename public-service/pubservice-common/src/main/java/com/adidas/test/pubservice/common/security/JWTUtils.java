package com.adidas.test.pubservice.common.security;

import com.adidas.test.pubservice.common.model.security.TokenUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class JWTUtils {

  protected final static Log logger = LogFactory.getLog(JWTUtils.class);

  @Autowired
  private Environment env;

  //public static final String base64SecretBytes = "wCY+za749pI8bXLgNWGsMcG9wrsYR6mjR8N7SAL5qaE=";
  private static String base64SecretBytes = "wCY+za749pI8bXLgNWGsMcG9wrsYR6mjR8N7SAL5qaE=";

  @PostConstruct
  private void init() {
    base64SecretBytes = env.getProperty("jwt.key");
    if (base64SecretBytes == null) {
      base64SecretBytes = "wCY+za749pI8bXLgNWGsMcG9wrsYR6mjR8N7SAL5qaE=";
    }
  }

  public static String codeToken(TokenUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("user", user);

    long now = (new Date()).getTime();
    Date validity = new Date(now + 1000 * 60 * 60 * 1); //1 hour

    String jwtToken = Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuer("com.adidas")
        .setIssuedAt(new Date())
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS512, base64SecretBytes)
        .compact();
    return jwtToken;

  }


  @SuppressWarnings("unchecked")
  public static TokenUser decodeToken(String token) {
    TokenUser tokenUser = new TokenUser();

    // Valida el token utilizando la cadena secreta
    Jws<Claims> claims = Jwts.parser()
        .setSigningKey(base64SecretBytes)
        .parseClaimsJws(token);

    //Creamos el usuario a partir de la informacion del token
    Claims body = claims.getBody();

    final Map<String, Object> userMap = (HashMap<String, Object>) body.get("user");

		tokenUser.setUsername((String) userMap.get("username"));

    List<String> permissionsL = ((ArrayList<String>) userMap.get("permissions"));
    String[] permissionsS = new String[permissionsL.size()];

    permissionsL.toArray(permissionsS);
		tokenUser.setPermissions(permissionsS);

    return tokenUser;
  }

  public static boolean verifyPreAccesToken(String token) {

    // checks token using secret
    Jws<Claims> claims = Jwts.parser()
        .setSigningKey(base64SecretBytes)
        .parseClaimsJws(token);

    return true;
  }

  public static String generateNewKey() {
    Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    byte[] secretBytes = secret.getEncoded();
    String base64SecretBytes = DatatypeConverter.printBase64Binary(secretBytes);

    return base64SecretBytes;
  }
}
