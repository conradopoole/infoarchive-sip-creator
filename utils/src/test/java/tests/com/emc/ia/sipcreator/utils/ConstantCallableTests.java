/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.ConstantCallable;

public class ConstantCallableTests {

  @Test
  public void callOnInstanceConstructedWithNullReturnsOnlyNull() {
    check(ConstantCallable.of(null), null, null, null);
  }

  @Test
  public void callOnInstanceConstructedWithObjectReturnsOnlyObject() {
    Object o = new Object();
    check(ConstantCallable.of(o), o, o, o);
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
    ConstantCallable<Object> callable = ConstantCallable.of(o);
    String text = callable.toString();
    assertTrue(text.contains(ConstantCallable.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(o)));
  }

  private <T> void check(ConstantCallable<T> callable, @SuppressWarnings("unchecked") T... expectedObjects) {
    for (T expectedObject : expectedObjects) {
      assertEquals(expectedObject, callable.call());
    }
  }
}
