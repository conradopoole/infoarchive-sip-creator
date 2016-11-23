/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.utils.TransformingIterator;

public class TransformingIteratorTests {

  private CloseableIterator<Object> iterator;
  private Function<Object, String> transform;
  private TransformingIterator<Object, String> transformingIterator;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    iterator = mock(CloseableIterator.class);
    transform = mock(Function.class);
    transformingIterator = new TransformingIterator<>(iterator, transform);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullIteratorThrowException() {
    new TransformingIterator<>(null, transform);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullTransformThrowException() {
    new TransformingIterator<>(iterator, null);
  }

  @Test
  public void toStringReturnDescriptiveString() {
    String text = transformingIterator.toString();
    assertTrue(text.contains(TransformingIterator.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(iterator)));
    assertTrue(text.contains(String.valueOf(transform)));
  }

  @Test
  public void nextDelegateAndReturnTransformed() {
    Object object = new Object();
    String expected = RandomStringUtils.random(32);
    when(iterator.next()).thenReturn(object);
    when(transform.apply(object)).thenReturn(expected);
    assertEquals(expected, transformingIterator.next());
  }

  @Test
  public void hasNextDelegateAndReturnValue() {
    when(iterator.hasNext()).thenReturn(true, true, false);
    assertTrue(transformingIterator.hasNext());
    assertTrue(transformingIterator.hasNext());
    assertFalse(transformingIterator.hasNext());
  }

  @Test
  public void removeDelegate() {
    transformingIterator.remove();
    verify(iterator).remove();
  }

}
