/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

public class BriefExceptionFormatter implements ExceptionFormatter {

  private static final String TRACE = " ";
  private static final String CAUSE = ">";

  private Predicate<StackTraceElement> filter;

  public BriefExceptionFormatter() {
    this(ConstantPredicate.of(true));
  }

  public BriefExceptionFormatter(Predicate<StackTraceElement> filter) {
    Params.notNull(filter, "BriefExceptionFormatter.filter");
    this.filter = filter;
  }

  @Override
  public String format(Throwable t) {
    StringBuilder b = new StringBuilder();
    summarize(b, t);
    return b.toString();
  }

  private void summarize(StringBuilder b, Throwable e) {
    describe(b, e);
    addCause(b, e, 1);
  }

  private void describe(StringBuilder b, Throwable e) {
    b.append(nameOf(e));

    String msg = e.getMessage();
    if (msg != null) {
      b.append(" \"");
      b.append(msg);
      b.append("\" ");
    }

    // Add source
    List<StackTraceElement> stackTrace = traceOf(e);
    if (!stackTrace.isEmpty()) {
      StackTraceElement element = stackTrace.get(0);
      b.append(" at ");
      b.append(element.getClassName());
      b.append('#');
      b.append(element.getMethodName());
      b.append(" (");
      b.append(element.getFileName());
      b.append(':');
      b.append(element.getLineNumber());
      b.append(')');
    }

  }

  private List<StackTraceElement> traceOf(Throwable e) {
    StackTraceElement[] trace = e.getStackTrace();
    List<StackTraceElement> result = new ArrayList<StackTraceElement>(trace.length);
    for (StackTraceElement traceElement : trace) {
      if (filter.test(traceElement)) {
        result.add(traceElement);
      }
    }
    return result;
  }

  private String nameOf(Throwable e) {
    return e.getClass()
      .getName();
  }

  private void addCause(StringBuilder b, Throwable e, int indent) {
    Throwable cause = e.getCause();
    if (cause == null) {
      // Add final trace.
      List<StackTraceElement> stackTrace = traceOf(e);
      for (int i = 1; i < stackTrace.size(); ++i) {
        StackTraceElement traceElement = stackTrace.get(i);
        b.append(indentation(TRACE, indent));
        b.append(traceElement);
      }

    } else {
      b.append(indentation(CAUSE, indent));
      describe(b, cause);
      addCause(b, cause, indent + 1);
    }
  }

  private String indentation(String indentText, int indent) {
    return "\n " + StringUtils.repeat(indentText, indent) + " ";
  }

  @Override
  public String toString() {
    return "BriefExceptionFormatter []";
  }

}
