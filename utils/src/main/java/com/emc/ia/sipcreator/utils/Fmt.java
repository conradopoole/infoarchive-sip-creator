/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

public final class Fmt {

  private static final char BEGIN_VARIABLE = '{';
  private static final char END_VARIABLE = '}';

  private Fmt() {
    throw new IllegalStateException("Fmt is a static utility class and its constructor should never be called.");
  }

  public static String format(final String template, final Object... args) {
    if (template == null) {
      return null;
    }

    int lastBegin = 0;
    int lastEnd = 0;
    int position = 0;
    int variableIndex = 0;
    final StringBuilder buf = new StringBuilder();
    final int length = template.length();
    for (;;) {
      lastBegin = template.indexOf(BEGIN_VARIABLE, lastEnd);
      if (lastBegin == -1) {
        if (lastEnd == 0) {
          return template;
        } else {
          buf.append(template.substring(position, length));
          return buf.toString();
        }
      } else {
        buf.append(template.substring(position, lastBegin));
        lastEnd = template.indexOf(END_VARIABLE, lastBegin);
        if (lastEnd == -1) {
          buf.append(template.substring(lastBegin));
          return buf.toString();
        } else if (lastEnd - lastBegin == 1) {
          buf.append(nullSafe(at(args, variableIndex, null)));
          variableIndex += 1;
          position = lastEnd + 1;
        } else {
          // We allow for positional arguments
          final Integer varIndex = tryParseInt(template.substring(lastBegin + 1, lastEnd));
          if (varIndex == null) {
            buf.append(template.substring(lastBegin, lastEnd - 1));
            lastBegin = lastEnd - 1;
            lastEnd = lastBegin;
            position = lastEnd;
          } else {
            buf.append(nullSafe(at(args, varIndex, String.valueOf(varIndex))));
            position = lastEnd + 1;
          }
        }

        if (position >= length) {
          return buf.toString();
        }
      }
    }
  }

  private static Integer tryParseInt(final String substring) {
    try {
      return Integer.parseInt(substring);
    } catch (final NumberFormatException e) {
      return null;
    }
  }

  private static Object at(final Object[] args, final int variableIndex, final String specifier) {
    if (args != null && variableIndex < args.length) {
      return args[variableIndex];
    } else {
      if (specifier == null) {
        return "{}";
      } else {
        return "{" + specifier + "}";
      }
    }
  }

  private static String nullSafe(final Object object) {
    if (object == null) {
      return "null";
    }
    try {
      return object.toString();
    } catch (final Exception e) {
      // TODO: Log this
      return Fmt.format("[FAILED {}#toString()]", object.getClass()
        .getName());
    }
  }

}
