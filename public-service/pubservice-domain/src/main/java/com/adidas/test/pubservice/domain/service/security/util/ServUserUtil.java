package com.adidas.test.pubservice.domain.service.security.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;

public class ServUserUtil {

  private static final Logger LOG = LoggerFactory.getLogger(ServUserUtil.class.getName());

  public static String encodeHexHash(String val) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-512");
      byte[] result = md.digest(val.getBytes("UTF-8"));
      char[] resultHex = Hex.encode(result);
      String resultHexS = new String(resultHex);
      resultHexS = resultHexS.toUpperCase();
      return resultHexS;
      //loginUser.setPassword(Base64.getEncoder().encodeToString(result));
    } catch (NoSuchAlgorithmException e) {
      LOG.error("Hash algorithm not supported");
    } catch (UnsupportedEncodingException e) {
      LOG.error("Encoding not supported");
    }
    return null;
  }
}
