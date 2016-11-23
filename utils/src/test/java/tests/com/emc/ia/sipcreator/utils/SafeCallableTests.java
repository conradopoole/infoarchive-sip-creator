/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.utils.SafeCallable;

public class SafeCallableTests {

  private Callable<Object> callable;
  private Function<Throwable, Object> onError;
  private Object expected;

  @Before
  @SuppressWarnings("unchecked")
  public void beforeEachTest() {
    callable = mock(Callable.class);
    onError = mock(Function.class);
    expected = mock(Object.class);
  }

  @Test(expected = IllegalArgumentException.class)
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void createWithNullCallableThrowsException() {
    new SafeCallable(null, onError);
  }

  @Test(expected = IllegalArgumentException.class)
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void createWithNullFunctionThrowsException() {
    new SafeCallable(callable, null);
  }

  @Test
  public void callWithNoErrorReturnsValue() throws Exception {
    SafeCallable<Object> safeCallable = SafeCallable.of(callable, onError);
    when(callable.call()).thenReturn(expected);
    assertEquals(expected, safeCallable.call());
  }

  @Test
  public void callWithErrorReturnsValueFromHandler() throws Exception {
    SafeCallable<Object> safeCallable = SafeCallable.of(callable, onError);
    Exception e = new Exception();
    when(callable.call()).thenThrow(e);
    when(onError.apply(e)).thenReturn(expected);
    assertEquals(expected, safeCallable.call());
  }

  @Test
  public void callWithErrorReturnsConstant() throws Exception {
    SafeCallable<Object> safeCallable = SafeCallable.ofConstant(callable, expected);
    Exception e = new Exception();
    when(callable.call()).thenThrow(e);
    assertEquals(expected, safeCallable.call());
  }

  @Test
  public void toStringOnInstanceReturnsDescriptiveString() {
    SafeCallable<Object> safeCallable = SafeCallable.of(callable, onError);
    String text = safeCallable.toString();
    assertTrue(text.contains(SafeCallable.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(callable)));
    assertTrue(text.contains(String.valueOf(onError)));
  }
}
