/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.Fmt;

public class FmtTests {

  private static final String THIS_IS_A = "This is a {}";
  private static final String MESSAGE = "message";
  private static final String THIS_IS_A_MESSAGE = "This is a message";

  @Test(expected = InvocationTargetException.class)
  public void createUtilityClassThrowsException()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Constructor<?>[] cons = Fmt.class.getDeclaredConstructors();
    cons[0].setAccessible(true);
    cons[0].newInstance((Object[])null);
  }

  @Test
  public void formatNullReturnsNull() {
    assertEquals(null, Fmt.format(null));
  }

  @Test
  public void formatConstantReturnSame() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format(THIS_IS_A_MESSAGE));
  }

  @Test
  public void formatEndsWithVariableReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format(THIS_IS_A, MESSAGE));
  }

  @Test
  public void formatStartsWithVariableReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format("{} is a message", "This"));
  }

  @Test
  public void formatVariableInTheMiddleReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format("This {} message", "is a"));
  }

  @Test
  public void formatVariableOnlyReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format("{}", THIS_IS_A_MESSAGE));
  }

  @Test
  public void formatExplicitPositionVariableMissingReturnSame() {
    assertEquals("This is a {0}", Fmt.format("This is a {0}"));
  }

  @Test
  public void formatPlaceHolderValueMissingReturnSame() {
    assertEquals("{}", Fmt.format("{}"));
  }

  @Test
  public void formatEndsWithPositionalVariableReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format("This is a {0}", MESSAGE));
  }

  @Test
  public void formatRepeatedPositionalVariableReturnInterpolatedString() {
    assertEquals("A B A", Fmt.format("{0} {1} {0}", "A", "B"));
  }

  @Test
  public void formatNestedVariableReturnInterpolatedString() {
    assertEquals("This is a {message}", Fmt.format("This is a {{}}", MESSAGE));
  }

  @Test
  public void formatNullVariableReturnInterpolatedString() {
    assertEquals("This is a null", Fmt.format(THIS_IS_A, (Object)null));
  }

  @Test
  public void formatToStringFailsReturnInterpolatedString() {
    assertEquals("This is a [FAILED " + this.getClass()
      .getCanonicalName() + "$1#toString()]", Fmt.format(THIS_IS_A, new Object() {

        @Override
        public String toString() {
          throw new IllegalStateException("invalid");
        }
      }));
  }

  @Test
  public void formatVariableButNullArrayReturnSame() {
    assertEquals(THIS_IS_A, Fmt.format(THIS_IS_A, (Object[])null));
  }

  public void formatEmptyStringReturnSame() {
    assertEquals("", Fmt.format(""));
  }

  @Test
  public void formatOpenEndedVariableMarkerReturnInterpolatedString() {
    assertEquals("This is a {message", Fmt.format("This {} a {message", "is"));
  }

  @Test
  public void formatEndsWithOpenEndedVariableMarkerReturnInterpolatedString() {
    assertEquals("This is a {", Fmt.format("This {} a {", "is"));
  }

  @Test
  public void formatBeginsWithOpenEndedVariableMarkerReturnInterpolatedString() {
    assertEquals("{This is a message", Fmt.format("{This {} a message", "is"));
  }

  @Test
  public void formatEndsWithUnstartedVariableMarkerReturnInterpolatedString() {
    assertEquals("This is a message}", Fmt.format("This {} a message}", "is"));
  }

  @Test
  public void formatParametersReturnInterpolatedString() {
    assertEquals(THIS_IS_A_MESSAGE, Fmt.format("{} {} a {}", "This", "is", MESSAGE));
  }

}
