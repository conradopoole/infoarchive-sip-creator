/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.utils.StrLookupWithImport;

public class StrLookupWithImportTests {

  private StrLookup<String> delegate;
  private Function<String, String> loader;
  private StrLookupWithImport lookup;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    delegate = mock(StrLookup.class);
    loader = mock(Function.class);
    lookup = new StrLookupWithImport(delegate, loader);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNullDelegateThrowException() {
    new StrLookupWithImport(null, loader);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNullLoaderThrowException() {
    new StrLookupWithImport(delegate, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lookupWithBlankKeyThrowException() {
    lookup.lookup(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lookupWithNullKeyThrowException() {
    lookup.lookup("");
  }

  @Test
  public void lookupWithImportDirectiveReturnImportedString() {
    String path = RandomStringUtils.random(32);
    String key = "import:" + path;
    String value = RandomStringUtils.random(32);
    when(loader.apply(path)).thenReturn(value);
    assertEquals(value, lookup.lookup(key));
  }

  @Test
  public void lookupNormalKeyReturnDelegateResponse() {
    String key = RandomStringUtils.random(32);
    String value = RandomStringUtils.random(32);
    when(delegate.lookup(key)).thenReturn(value);
    assertEquals(value, lookup.lookup(key));
  }

  @Test
  public void toStringOnInstanceReturnsDescriptiveString() {
    String text = lookup.toString();
    assertTrue(text.contains(StrLookupWithImport.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(delegate)));
    assertTrue(text.contains(String.valueOf(loader)));
  }
}
