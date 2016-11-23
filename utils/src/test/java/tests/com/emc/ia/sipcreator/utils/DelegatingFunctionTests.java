/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.utils.DelegatingFunction;

public class DelegatingFunctionTests {

  private Function<Object, Object> delegate;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    delegate = mock(Function.class);
  }

  @Test
  public void createDefaultConstructorCreatesWithNullDelegate() {
    assertNull(new DelegatingFunction<Object, Object>().getDelegate());
  }

  @Test
  public void createConstructorWithNullDelegateNewInstance() {
    assertNull(new DelegatingFunction<Object, Object>(null).getDelegate());
  }

  @Test
  public void createConstructorWithDelegateNewInstance() {
    assertEquals(delegate, new DelegatingFunction<Object, Object>(delegate).getDelegate());
  }

  @Test
  public void applyDelegateCallReturnResponse() {
    Object object = new Object();
    Object input = new Object();
    when(delegate.apply(input)).thenReturn(object);
    assertEquals(object, new DelegatingFunction<Object, Object>(delegate).apply(input));
  }

  @Test(expected = NullPointerException.class)
  public void applyNullDelegateThrowException() {
    Object input = new Object();
    new DelegatingFunction<Object, Object>().apply(input);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void setDelegateUpdatesDelegate() {
    DelegatingFunction<Object, Object> function = new DelegatingFunction<Object, Object>(delegate);
    assertEquals(delegate, function.getDelegate());
    Function<Object, Object> newDelegate = mock(Function.class);
    function.setDelegate(newDelegate);
    assertEquals(newDelegate, function.getDelegate());

    Object object = new Object();
    Object input = new Object();
    when(newDelegate.apply(input)).thenReturn(object);
    assertEquals(object, function.apply(input));
  }

  @Test
  public void toStringOnInstanceConstructedWithNonNullDelegateReturnsDescriptiveString() {
    DelegatingFunction<Object, Object> function = new DelegatingFunction<>(delegate);
    String text = function.toString();
    assertTrue(text.contains(DelegatingFunction.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(function)));
  }

}
