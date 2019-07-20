package com.dlsc.preferencesfx.util;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper methods for working with strings.
 */
public class Strings {

  private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

  /**
   * Checks if a string is null or empty.
   *
   * @param string the string to test, may be {@code null}.
   * @return {@code true} iff the string is not null and not empty
   */
  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }

  /**
   * Calculates the SHA-256 digest of a string.
   *
   * @param string the string to digest, must not be {@code null}.
   * @return the SHA-256 digest of the input string
   */
  public static String sha256(String string) {
    requireNonNull(string);
    try {
      final MessageDigest digest = MessageDigest.getInstance("sha-256");
      final byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
      return hexString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Calculates the HEX representation of a byte array.
   *
   * @param bytes the byte array, must not be {@code null}.
   * @return the HEX representation of the byte array.
   */
  public static String hexString(byte[] bytes) {
    requireNonNull(bytes);
    final int numBytes = bytes.length;
    char[] hexChars = new char[numBytes * 2];
    for (int i = 0; i < numBytes; i++) {
      int v = bytes[i] & 0xFF;
      hexChars[i * 2] = HEX_ARRAY[v >>> 4];
      hexChars[(i * 2) + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

}
