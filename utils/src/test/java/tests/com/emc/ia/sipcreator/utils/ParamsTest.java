/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.emc.ia.sipcreator.utils.Params;

public class ParamsTest {

  private static final String KEY = "key";
  private static final String SOME_NAME = "some name";
  private static final String VALUE = "value";

  @Test(expected = InvocationTargetException.class)
  public void createUtilityClassThrowsException()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Constructor<?>[] cons = Params.class.getDeclaredConstructors();
    cons[0].setAccessible(true);
    cons[0].newInstance((Object[])null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnEmptyArrayThrowsException() {
    Params.notEmpty(new String[] {}, "array");
  }

  @Test
  public void notEmptyOnNonEmptyArrayDoesNothing() {
    Params.notEmpty(new String[] { "foo" }, "array");
  }

  @Test
  public void atLeastOnLargerDoesNothing() {
    Params.atLeast(10, 5, VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void atLeastOnSmallerThrowsException() {
    Params.atLeast(10, 15, VALUE);
  }

  @Test
  public void notNullOnNotNullDoesNothing() {
    Params.notNull("some value", SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notNullOnNullThrowsException() {
    Params.notNull(null, SOME_NAME);
  }

  @Test
  public void notEmptyOnNotEmptyStringDoesNothing() {
    Params.notEmpty("not empty", SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnNullStringThrowsException() {
    Params.notEmpty((String)null, SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnEmptyStringThrowsException() {
    Params.notEmpty("", SOME_NAME);
  }

  @Test
  public void notBlankOnNotBlankStringDoesNothing() {
    Params.notBlank("not empty", SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notBlankOnNullStringThrowsException() {
    Params.notBlank(null, SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notBlankOnEmptyStringThrowsException() {
    Params.notBlank("", SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notBlankOnWhitespaceStringThrowsException() {
    Params.notBlank("  ", SOME_NAME);
  }

  @Test
  public void notEmptyOnNotEmptyCollectionDoesNothing() {
    Params.notEmpty(Arrays.asList("some value"), SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnEmptyCollectionThrowsException() {
    Params.notEmpty(Collections.emptyList(), SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnNullCollectionThrowsException() {
    Params.notEmpty((List<Object>)null, SOME_NAME);
  }

  @Test
  public void notTrueOnFalseDoesNothing() {
    Params.notTrue(false, SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notTrueOnTrueThrowException() {
    Params.notTrue(true, SOME_NAME);
  }

  @Test
  public void notFalseOnTrueDoesNothing() {
    Params.notFalse(true, SOME_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notTrueOnFalseThrowsException() {
    Params.notFalse(false, SOME_NAME);
  }

  @Test
  public void notEmptyOnNotEmptyMapDoesNothing() {
    Params.notEmpty(Collections.singletonMap(KEY, VALUE), "map");
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnEmptyMapThrowsException() {
    Params.notEmpty(Collections.emptyMap(), "map");
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmptyOnNullMapThrowsException() {
    Params.notEmpty((Map<String, String>)null, "map");
  }

  @Test
  public void containsOnMapWithKeyPresentDoesNothing() {
    Params.contains(Collections.singletonMap(KEY, VALUE), KEY, "some message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void containsOnMapWithKeyAbsentThrowsException() {
    Params.contains(Collections.singletonMap(KEY, VALUE), "alfa", "some message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void containsOnNullMapThrowsException() {
    Params.contains(null, KEY, "some message");
  }
}
