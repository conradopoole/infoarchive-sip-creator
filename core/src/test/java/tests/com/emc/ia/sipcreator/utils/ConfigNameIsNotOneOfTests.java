/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.config.ConfigImpl;
import com.emc.ia.sipcreator.utils.ConfigNameIsNotOneOf;

public class ConfigNameIsNotOneOfTests {

  private ConfigNameIsNotOneOf pred;
  private Set<String> names;
  private String name1;

  @Before
  public void before() {
    name1 = RandomStringUtils.random(32);
    String name2 = RandomStringUtils.random(32);
    names = new HashSet<>(Arrays.asList(name1, name2));
    pred = new ConfigNameIsNotOneOf(names);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNull() {
    new ConfigNameIsNotOneOf(null);
  }

  @Test
  public void testNameNotPresentReturnTrue() {
    Config config = new ConfigImpl("", "notPresent", null);
    assertTrue(pred.test(config));
  }

  @Test
  public void testNamePresentReturnFalse() {
    Config config = new ConfigImpl("", name1, null);
    assertFalse(pred.test(config));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = pred.toString();
    assertTrue(text.contains(ConfigNameIsNotOneOf.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(names)));
  }
}
