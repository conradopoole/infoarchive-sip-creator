/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime.plugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.config.ConfigImpl;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.runtime.plugins.PluginUtils;
import com.emc.ia.sipcreator.spi.PluginRegistry;

public class DefaultPluginRegistryTests {

  private static final String TEST2_PLUGIN_NAME = "test2";

  private static final String TEST1_PLUGIN_NAME = "test1";

  private static final String CONFIG_NAME = "foo";

  private static final String PATH = null;

  private final PluginRegistry registry = new DefaultPluginRegistry();

  private ApplicationState appState;

  @Before
  public void before() {
    Config rootConfig = Mockito.mock(Config.class);
    appState = Mockito.mock(ApplicationState.class);
    when(appState.getConfiguration()).thenReturn(rootConfig);
  }

  @Test
  public void createTest1PluginReturnInstance() {
    String text = RandomStringUtils.random(32);
    Config config = new ConfigImpl(PATH, CONFIG_NAME, Collections.singletonMap("text", text));
    assertEquals(text, registry.create(String.class, TEST1_PLUGIN_NAME, PluginUtils.context(appState, config)));
  }

  @Test
  public void createTest1PluginWithoutTypeReturnInstance() {
    String text = RandomStringUtils.random(32);
    Config config = new ConfigImpl(PATH, CONFIG_NAME, Collections.singletonMap("text", text));
    assertEquals(text, registry.create(null, TEST1_PLUGIN_NAME, PluginUtils.context(appState, config)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createTest1PluginWithoutConfThrowException() {
    Config config = new ConfigImpl(PATH, CONFIG_NAME, null);
    registry.create(null, TEST1_PLUGIN_NAME, PluginUtils.context(appState, config));
  }

  @Test
  public void createTest2StringPluginReturnInstance() {
    String text = RandomStringUtils.randomAlphanumeric(32);
    String expected = StringUtils.reverse(text);
    Config config = new ConfigImpl(PATH, CONFIG_NAME, Collections.singletonMap("text", text));
    assertEquals(expected, registry.create(String.class, TEST2_PLUGIN_NAME, PluginUtils.context(appState, config)));
  }

  @Test
  public void createTest2ObjectPluginReturnInstance() {
    Config config = new ConfigImpl(PATH, CONFIG_NAME, CONFIG_NAME);
    Object result = registry.create(Object.class, TEST2_PLUGIN_NAME, PluginUtils.context(appState, config));
    assertNotNull(result);
    assertEquals(Object.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createTest2ObjectNullInstanceThrowException() {
    Config config = new ConfigImpl(PATH, CONFIG_NAME, null);
    Object result = registry.create(Object.class, TEST2_PLUGIN_NAME, PluginUtils.context(appState, config));
    assertNotNull(result);
    assertEquals(Object.class, result.getClass());
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = registry.toString();
    assertTrue(text.contains(PluginRegistry.class.getSimpleName()));
    assertTrue(text.contains(TEST1_PLUGIN_NAME));
    assertTrue(text.contains(TEST2_PLUGIN_NAME));
    // Not guaranteed currently.
    // assertTrue(text.contains(TestPlugin1.class.getCanonicalName()));
    // assertTrue(text.contains(TestPlugin2.class.getCanonicalName()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNoSuchPluginThrowException() {
    Config config = new ConfigImpl(PATH, CONFIG_NAME, null);
    registry.create(null, "noSuchPlugin", PluginUtils.context(appState, config));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createAmbigiousThrowException() {
    Config config = new ConfigImpl(PATH, CONFIG_NAME, null);
    registry.create(null, TEST2_PLUGIN_NAME, PluginUtils.context(appState, config));
  }

  @Test
  public void getPluginByIdReturnsPluginWithCorrespondId() {

    // Unique id
    List<Plugin> p1s = registry.getPluginsById(TEST1_PLUGIN_NAME);
    assertEquals(1, p1s.size());
    Plugin p1 = p1s.get(0);
    assertEquals(TEST1_PLUGIN_NAME, p1.getId());
    assertEquals(String.class, p1.getType());
    assertTrue(p1 instanceof TestPlugin1);

    // Non-unique id
    List<Plugin> p2s = registry.getPluginsById(TEST2_PLUGIN_NAME);
    assertEquals(2, p2s.size());
    Plugin p2a = p2s.get(0);
    Plugin p2b = p2s.get(1);
    assertEquals(TEST2_PLUGIN_NAME, p2b.getId());

    List<Class<?>> p2types = new ArrayList<>(Arrays.asList(p2a.getType(), p2b.getType()));
    assertTrue(p2types.remove(String.class));
    assertTrue(p2types.remove(Object.class));
    assertTrue(p2types.isEmpty());

    List<Class<?>> p2classes = new ArrayList<>(Arrays.asList(p2a.getClass(), p2b.getClass()));
    assertTrue(p2classes.remove(TestPlugin2a.class));
    assertTrue(p2classes.remove(TestPlugin2b.class));
    assertTrue(p2classes.isEmpty());
  }

  @Test
  public void getAllPluginsReturnsAllPlugins() {

    // TestPlugin1, 2a and 2b should be available
    List<Plugin> allPlugins = registry.getAllPlugins();
    assertEquals(3, allPlugins.size());

    assertTrue(allPlugins.removeAll(registry.getPluginsById(TEST1_PLUGIN_NAME)));
    assertTrue(allPlugins.removeAll(registry.getPluginsById(TEST2_PLUGIN_NAME)));
    assertTrue(allPlugins.isEmpty());
  }

}
