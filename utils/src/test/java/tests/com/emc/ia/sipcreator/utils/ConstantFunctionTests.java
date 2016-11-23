/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.ConstantFunction;

public class ConstantFunctionTests {

  @Test
  public void callOnInstanceConstructedWithNullReturnsOnlyNull() {
    check(ConstantFunction.of(null), null, null, new Object());
  }

  @Test
  public void callOnInstanceConstructedWithObjectReturnsOnlyObject() {
    Object o = new Object();
    check(ConstantFunction.of(o), o, null, new Object());
  }

  @Test
  public void toStringOnInstanceConstructedWithObjectReturnsDescriptiveString() {
    Object o = new Object();
    checkToString(o);
  }

  @Test
  public void toStringOnInstanceConstructedWithNullReturnsDescriptiveString() {
    checkToString(null);
  }

  private void checkToString(Object o) {
    ConstantFunction<Object, Object> function = ConstantFunction.of(o);
    String text = function.toString();
    assertTrue(text.contains(ConstantFunction.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(o)));
  }

  private <T, R> void check(Function<T, R> callable, T expectedObject, @SuppressWarnings("unchecked") T... arguments) {
    for (T argument : arguments) {
      assertEquals(expectedObject, callable.apply(argument));
    }
  }
}
