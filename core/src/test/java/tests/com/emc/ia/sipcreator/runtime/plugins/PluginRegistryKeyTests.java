/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.runtime.plugins.PluginRegistryKey;

public class PluginRegistryKeyTests {

  private Class<?> type;
  private Class<?> otherType;
  private String id;
  private String otherId;

  static class TestClass {
  }

  static class TestClass2 {
  }

  @Before
  public void before() {
    type = TestClass.class;
    otherType = TestClass2.class;
    id = RandomStringUtils.random(32);
    otherId = id + RandomStringUtils.random(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullTypeThrowException() {
    new PluginRegistryKey(null, id);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullIdThrowException() {
    new PluginRegistryKey(type, null);
  }

  @Test
  public void getTypeReturnsType() {
    assertEquals(type, new PluginRegistryKey(type, id).getType());
  }

  @Test
  public void getIdReturnsId() {
    assertEquals(id, new PluginRegistryKey(type, id).getId());
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = new PluginRegistryKey(type, id).toString();
    assertTrue(text.contains(PluginRegistryKey.class.getSimpleName()));
    assertTrue(text.contains(id));
    assertTrue(text.contains(type.getCanonicalName()));
  }

  @Test
  public void equalsOtherNullReturnFalse() {
    PluginRegistryKey key = new PluginRegistryKey(type, id);
    assertFalse(key.equals(null)); // NOPMD
  }

  @Test
  public void equalsOtherSameReturnTrue() {
    PluginRegistryKey key = new PluginRegistryKey(type, id);
    assertTrue(key.equals(key)); // NOPMD
    assertEquals(key.hashCode(), key.hashCode());
  }

  @Test
  public void equalsOtherIdenticalReturnTrue() {
    PluginRegistryKey key1 = new PluginRegistryKey(type, id);
    PluginRegistryKey key2 = new PluginRegistryKey(type, id);
    assertTrue(key1.equals(key2)); // NOPMD
    assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void equalsOtherDifferentTypeReturnFalse() {
    PluginRegistryKey key1 = new PluginRegistryKey(type, id);
    PluginRegistryKey key2 = new PluginRegistryKey(otherType, id);
    assertFalse(key1.equals(key2));
  }

  @Test
  public void equalsOtherDifferentIdReturnFalse() {
    PluginRegistryKey key1 = new PluginRegistryKey(type, id);
    PluginRegistryKey key2 = new PluginRegistryKey(type, otherId);
    assertFalse(key1.equals(key2));
  }

}
