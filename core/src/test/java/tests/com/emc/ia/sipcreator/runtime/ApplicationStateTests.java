/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;

public class ApplicationStateTests {

  private Config config;
  private ApplicationState state;

  @Before
  public void before() {
    config = mock(Config.class);
    state = new ApplicationState(config, new DefaultPluginRegistry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullConfigThrowException() {
    new ApplicationState(null, new DefaultPluginRegistry());
  }

  @Test
  public void getConfigReturnsConfig() {
    assertEquals(config, state.getConfiguration());
  }

  @Test
  public void setObjectNotAlreadySetSetsObject() {
    String object = RandomStringUtils.random(32);
    String key = RandomStringUtils.random(32);

    assertNull(state.getObject(String.class, key));

    state.setObject(key, object);
    assertEquals(object, state.getObject(String.class, key));
  }

  @Test
  public void getPluginsReturnsPluginRegistryInstance() {
    assertNotNull(state.getPlugins());
  }

  @Test(expected = IllegalStateException.class)
  public void setObjectAlreadySetThrowsException() {
    String object = RandomStringUtils.random(32);
    String key = RandomStringUtils.random(32);

    state.setObject(key, object);
    state.setObject(key, object);
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String object = RandomStringUtils.random(32);
    String key = RandomStringUtils.random(32);
    state.setObject(key, object);

    String text = state.toString();
    assertTrue(text.contains(ApplicationState.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(config)));
    assertTrue(text.contains(key));
    assertTrue(text.contains(String.valueOf(object)));
    // TODO: plugins
  }
}
