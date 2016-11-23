/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.plugins.PluginContextImpl;
import com.emc.ia.sipcreator.spi.PluginRegistry;

public class PluginContextImplTests {

  private Config rootConfig;
  private Config config;
  private ApplicationState appState;
  private PluginContext context;

  @Before
  public void before() {
    rootConfig = mock(Config.class);
    config = mock(Config.class);
    appState = mock(ApplicationState.class);
    context = new PluginContextImpl(appState, rootConfig, config);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullApplicationStateThrowException() {
    new PluginContextImpl(null, rootConfig, config);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullRootConfigThrowException() {
    new PluginContextImpl(appState, null, config);
  }

  @Test
  public void createNullConfigReturnsInstance() {
    assertNull(new PluginContextImpl(appState, rootConfig, null).getConfig());
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = new PluginContextImpl(appState, rootConfig, config).toString();
    assertTrue(text.contains(PluginContextImpl.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(appState)));
    assertTrue(text.contains(String.valueOf(rootConfig)));
    assertTrue(text.contains(String.valueOf(config)));
  }

  @Test
  public void getObjectDelegatesToAppStateReturnResponse() {
    Object object = new Object();
    String id = RandomStringUtils.random(32);
    Class<Object> type = Object.class;
    when(appState.getObject(type, id)).thenReturn(object);
    assertEquals(object, context.getObject(type, id));
  }

  @Test
  public void newObjectCreatesPluginInstanceRegistersItReturnsResponse() {
    Object object = new Object();
    String id = RandomStringUtils.random(32);
    Class<Object> type = Object.class;
    Config cfg = mock(Config.class);

    PluginRegistry registry = mock(PluginRegistry.class);
    when(registry.create(Matchers.eq(type), Matchers.eq(id), Matchers.notNull(PluginContext.class))).thenReturn(object);
    when(appState.getPlugins()).thenReturn(registry);
    when(appState.getConfiguration()).thenReturn(rootConfig);

    assertEquals(object, context.newObject(type, id, cfg));
    // assertEquals(object, context.getObject(type, id));

    when(appState.getObject(type, id)).thenReturn(object);
  }

  @Test
  public void getConfigReturnsConfig() {
    assertEquals(config, new PluginContextImpl(appState, rootConfig, config).getConfig());
  }

  @Test
  public void getRootConfigReturnsRootConfig() {
    assertEquals(rootConfig, new PluginContextImpl(appState, rootConfig, config).getRootConfig());
  }
}
