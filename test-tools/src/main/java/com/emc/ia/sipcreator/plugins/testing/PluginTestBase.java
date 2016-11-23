/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.Plugin;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.config.ConfigParser;
import com.emc.ia.sipcreator.config.YamlConfigParser;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.HouseKeeperImpl;
import com.emc.ia.sipcreator.runtime.plugins.DefaultPluginRegistry;
import com.emc.ia.sipcreator.runtime.plugins.PluginContextImpl;
import com.emc.ia.sipcreator.spi.PluginRegistry;
import com.emc.ia.sipcreator.utils.ClassPathTextResourceLoader;
import com.emc.ia.sipcreator.utils.ConfigUtilsImpl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

public class PluginTestBase {

  private static final PluginRegistry PLUGINS = new DefaultPluginRegistry();
  private final Function<String, String> loader = new ClassPathTextResourceLoader(getClass());
  private final ConfigUtils utils = new ConfigUtilsImpl(new RuntimeVariableFactoryImpl(loader), loader);
  private final ConfigParser parser = new YamlConfigParser();

  protected PluginChecker checker(Plugin plugin) {
    return new PluginChecker(plugin);
  }

  protected <T> T newInstance(Class<T> type, String id, PluginContext context) {
    return PLUGINS.create(type, id, context);
  }

  protected Plugin getPlugin(String id) {
    List<Plugin> pluginsById = PLUGINS.getPluginsById(id);
    assertEquals(1, pluginsById.size());
    return pluginsById.get(0);
  }

  protected Appender<ILoggingEvent> mockAppender() {
    ch.qos.logback.classic.Logger root =
        (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    @SuppressWarnings("unchecked")
    final Appender<ILoggingEvent> mockAppender = mock(Appender.class);
    when(mockAppender.getName()).thenReturn("MOCK");
    root.addAppender(mockAppender);
    return mockAppender;
  }

  protected RuntimeState state(String key, Object value) {

    RuntimeState state = new RuntimeState();
    state.set(key, value);

    return state;
  }

  protected RuntimeState state(String key, Object value, String key2, Object value2) {

    RuntimeState state = new RuntimeState();
    state.set(key, value);
    state.set(key2, value2);

    return state;
  }

  protected String randomString() {
    return RandomStringUtils.random(32);
  }

  protected String randomAlphaString() {
    return RandomStringUtils.randomAlphabetic(32);
  }

  protected PluginContext context(String path, String pluginConf) {
    return context(path, pluginConf, new Consumer<ApplicationState>() {

      @Override
      public void accept(ApplicationState t) {
      }
    });
  }

  protected PluginContext context(String path, String pluginConf, Consumer<ApplicationState> stateModifier) {
    try {
      Config config = parser.parse(loader.apply(path));
      ApplicationState appState = new ApplicationState(config, PLUGINS);
      appState.setObject("configUtils", utils);
      appState.setObject("housekeeper", new HouseKeeperImpl());
      stateModifier.accept(appState);
      return new PluginContextImpl(appState, appState.getConfiguration(), utils.getGroup(config, pluginConf));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  protected URIResolver classPathResolver(Class<?> class1) {
    return new URIResolver() {

      @Override
      public InputStream open(String uri) throws IOException {
        return class1.getResourceAsStream(uri);
      }
    };
  }

  protected String getPathTo(String path) {
    return new File("src/test/resources/" + StringUtils.replace(getClass().getPackage()
      .getName(), ".", "/") + "/" + path).getAbsolutePath();
  }

}
