package com.dlsc.preferencesfx.util;

/**
 * Since google/guava pull request #3023 isn't merged yet containing the method "containsIgnoreCase"
 * which is needed in this project, pulled in the changes in this class in the meantime.
 * As soon as it has been merged, calls from this class can be referenced to the "Ascii" class.
 */
public class StringUtils {

  /**
   * Returns the non-negative index value of the alpha character {@code c}, regardless of case. Ie,
   * 'a'/'A' returns 0 and 'z'/'Z' returns 25. Non-alpha characters return a value of 26 or greater.
   */
  private static int getAlphaIndex(char c) {
    // Fold upper-case ASCII to lower-case and make zero-indexed and unsigned (by casting to char).
    return (char) ((c | 0x20) - 'a');
  }

  /**
   * Searches through {@code source} to find {@code target}, ignoring the case of
   * any ASCII alphabetic characters between {@code 'a'} and {@code 'z'}
   * or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @param source       the characters being searched.
   * @param sourceOffset offset of the source string.
   * @param sourceCount  count of the source string.
   * @param target       the characters being searched for.
   * @param targetOffset offset of the target string.
   * @param targetCount  count of the target string.
   * @param fromIndex    the index to begin searching from.
   */
  private static int indexOfIgnoreCase(CharSequence source, int sourceOffset, int sourceCount,
                                       CharSequence target, int targetOffset, int targetCount,
                                       int fromIndex) {
    if (fromIndex >= sourceCount) {
      return (targetCount == 0 ? sourceCount : -1);
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    if (targetCount == 0) {
      return fromIndex;
    }

    char first = target.charAt(targetOffset);
    int firstAlphaIndex = getAlphaIndex(first);
    int max = sourceOffset + (sourceCount - targetCount);

    for (int i = sourceOffset + fromIndex; i <= max; i++) {
      /* Look for first character. */
      while (i <= max) {
        char sourceI = source.charAt(i);
        if (sourceI == first) {
          break;
        }
        int sourceIAlphaIndex = getAlphaIndex(sourceI);
        if (sourceIAlphaIndex >= 26 || sourceIAlphaIndex != firstAlphaIndex) {
          ++i;
          continue;
        }
        break;
      }

      /* Found first character, now look at the rest of v2 */
      if (i <= max) {
        int j = i + 1;
        int end = j + targetCount - 1;
        char sourceJ;
        int sourceJAlphaIndex;
        char targetK;
        for (int k = targetOffset + 1; j < end; j++, k++) {
          sourceJ = source.charAt(j);
          targetK = target.charAt(k);
          if (sourceJ == targetK) {
            continue;
          }
          sourceJAlphaIndex = getAlphaIndex(sourceJ);
          if (sourceJAlphaIndex < 26 && sourceJAlphaIndex == getAlphaIndex(targetK)) {
            continue;
          }
          break;
        }

        if (j == end) {
          /* Found whole string. */
          return i - sourceOffset;
        }
      }
    }
    return -1;
  }

  /**
   * Returns the index within the {@code sequence} of the first occurrence of {@code subSequence},
   * starting at {@code fromIndex}, ignoring the case of any ASCII alphabetic characters
   * between {@code 'a'} and {@code 'z'} or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @param sequence    the sequence to be searched in.
   * @param subSequence the subsequence to search for.
   * @param fromIndex   the index from which to start the search.
   * @return the index of the first occurrence of the {@code subSequence}, or {@code -1} if there is
   *         no such occurrence.
   * @since NEXT
   */
  public static int indexOfIgnoreCase(
      CharSequence sequence, CharSequence subSequence, int fromIndex) {
    return indexOfIgnoreCase(sequence, 0, sequence.length(),
        subSequence, 0, subSequence.length(), fromIndex);
  }

  /**
   * Returns the index within the {@code sequence} of the first occurrence of {@code subSequence},
   * ignoring the case of any ASCII alphabetic characters
   * between {@code 'a'} and {@code 'z'} or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @param sequence    the sequence to be searched in.
   * @param subSequence the subsequence to search for.
   * @return the index of the first occurrence of the {@code subSequence},
   *         or {@code -1} if there is no such occurrence.
   * @since NEXT
   */
  public static int indexOfIgnoreCase(CharSequence sequence, CharSequence subSequence) {
    return indexOfIgnoreCase(sequence, subSequence, 0);
  }

  /**
   * Indicates whether the character sequence {@code sequence} contains the {@code subSequence},
   * ignoring the case of any ASCII alphabetic characters between {@code 'a'} and {@code 'z'}
   * or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @since NEXT
   */
  public static boolean containsIgnoreCase(CharSequence sequence, CharSequence subSequence) {
    // Calling length() is the null pointer check (so do it before we can exit early).
    int length = sequence.length();
    if (sequence == subSequence) {
      return true;
    }
    // if subSequence is longer than sequence, it is impossible for sequence to contain subSequence
    if (subSequence.length() > length) {
      return false;
    }
    return indexOfIgnoreCase(sequence, subSequence) > -1;
  }

  /**
   * Returns if the character sequence {@code seq} starts with the character sequence {@code prefix}
   * starting at {@code fromIndex}, ignoring the case of any ASCII alphabetic characters
   * between {@code 'a'} and {@code 'z'} or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @since NEXT
   */
  public static boolean startsWithIgnoreCase(CharSequence seq, CharSequence prefix, int fromIndex) {
    int seqOffset = fromIndex;
    int prefixOffset = 0;
    int prefixCounter = prefix.length();
    // Note: fromIndex might be near -1>>>1.
    if ((fromIndex < 0) || (fromIndex > seq.length() - prefixCounter)) {
      return false;
    }
    while (--prefixCounter >= 0) {
      char charSeq = seq.charAt(seqOffset++);
      char charPrefix = prefix.charAt(prefixOffset++);
      if (charSeq == charPrefix) {
        continue;
      }
      int seqAlphaIndex = getAlphaIndex(charSeq);
      if (seqAlphaIndex < 26 && seqAlphaIndex == getAlphaIndex(charPrefix)) {
        continue;
      }
      return false;
    }
    return true;
  }

  /**
   * Returns if the character sequence {@code seq} starts with the character sequence {@code prefix}
   * ignoring the case of any ASCII alphabetic characters
   * between {@code 'a'} and {@code 'z'} or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @since NEXT
   */
  public static boolean startsWithIgnoreCase(CharSequence seq, CharSequence prefix) {
    return startsWithIgnoreCase(seq, prefix, 0);
  }

  /**
   * Returns if the character sequence {@code seq} ends with the character sequence {@code suffix}
   * ignoring the case of any ASCII alphabetic characters
   * between {@code 'a'} and {@code 'z'} or {@code 'A'} and {@code 'Z'} inclusive.
   *
   * @since NEXT
   */
  public static boolean endsWithIgnoreCase(CharSequence seq, CharSequence suffix) {
    return startsWithIgnoreCase(seq, suffix, seq.length() - suffix.length());
  }
}
