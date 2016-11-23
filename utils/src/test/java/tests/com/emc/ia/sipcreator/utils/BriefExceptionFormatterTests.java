/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.BriefExceptionFormatter;
import com.emc.ia.sipcreator.utils.ClassPathTextResourceLoader;

public class BriefExceptionFormatterTests {

  private String formatSampleFlow(BriefExceptionFormatter formatter) {
    try {
      new ExceptionFormatterTestFlow().entryPoint();
    } catch (Exception e) {
      return formatter.format(e);
    }
    return "Exception not thrown";
  }

  @Test
  public void formatSampleFlowReturnBriefSummary() {
    String expected = new ClassPathTextResourceLoader(getClass()).apply("brief.txt");
    assertEquals(expected, formatSampleFlow(new BriefExceptionFormatter(x -> notTestPackage(x))));
  }

  private boolean notTestPackage(StackTraceElement elt) {
    String name = elt.getClassName();
    boolean result = !(name.startsWith("sun.reflect") || name.startsWith("org.junit") || name.startsWith("org.eclipse")
        || name.startsWith("java.lang.reflect") || name.startsWith("org.gradle") || name.startsWith("com.sun.proxy")
        || name.startsWith("java.lang") || name.startsWith("java.util.concurrent"));
    return result;
  }

  @Test
  public void toStringReturnDescriptiveString() {
    String text = new BriefExceptionFormatter().toString();
    assertTrue(text.contains(BriefExceptionFormatter.class.getSimpleName()));
  }
}
