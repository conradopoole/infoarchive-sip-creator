/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;
import com.emc.ia.sipcreator.config.ConfigImpl;
import com.emc.ia.sipcreator.config.ConfigParser;
import com.emc.ia.sipcreator.config.YamlConfigParser;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.runtime.ApplicationState;
import com.emc.ia.sipcreator.runtime.plugins.PluginContextImpl;
import com.emc.ia.sipcreator.utils.ClassPathTextResourceLoader;
import com.emc.ia.sipcreator.utils.ConfigUtilsImpl;

public class ConfigUtilsImplTests {

  private static final String BOOLS_ID = "bools";
  private static final String MAIN_ID = "main";
  private final Function<String, String> loader = new ClassPathTextResourceLoader(getClass());
  private final ConfigParser parser = new YamlConfigParser();
  private final Config conf1 = config("conf1.yml");

  private final RuntimeVariableFactory runtimeVariableFactory = new RuntimeVariableFactoryImpl(loader);
  private final ConfigUtils utils = new ConfigUtilsImpl(runtimeVariableFactory, loader);

  private RuntimeState state;
  private String value;

  private Consumer<Config> proc;
  private ApplicationState appState;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {

    appState = mock(ApplicationState.class);
    proc = mock(Consumer.class);
    value = RandomStringUtils.random(32);
    state = mock(RuntimeState.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullRuntimeVariableFactoryThrowException() {
    new ConfigUtilsImpl(null, loader);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullLoaderThrowException() {
    new ConfigUtilsImpl(runtimeVariableFactory, null);
  }

  @Test
  public void forEachNullParentDoNothing() {
    utils.forEach(null, proc);
    verify(proc, never()).accept(any(Config.class));
  }

  @Test
  public void forEachChildGroupInorderOncePerGroup() {
    StringBuilder b = new StringBuilder();
    utils.forEach(utils.getGroup(conf1, MAIN_ID), x -> b.append(x.getName())
      .append(x.getValue()));
    assertEquals("a1b2c3", b.toString());
  }

  @Test
  public void getGroupPluginContextGroupExistsReturnGroup() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, MAIN_ID));

    Config config = utils.getGroup(context, "a");
    assertEquals("a", config.getName());
    assertEquals("1", config.getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getGroupPluginContextGroupNotExistingThrowException() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, MAIN_ID));
    utils.getGroup(context, "d");
  }

  @Test
  public void getOptionalGroupGroupExistsReturnGroup() {
    Config config = utils.getOptionalGroup(utils.getGroup(conf1, MAIN_ID), "a");
    assertEquals("a", config.getName());
    assertEquals("1", config.getValue());
  }

  @Test
  public void getRuntimeVariableVariableReturnVariable() {
    when(state.get("var")).thenReturn(value);
    assertEquals(value, utils.getRuntimeVariable("$context.var$")
      .getValue(state));
  }

  @Test
  public void getRuntimeVariableFromContextReturnVariable() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, MAIN_ID));
    assertEquals("1", utils.getRuntimeVariable(context, "a")
      .getValue(state));
  }

  @Test
  public void getRuntimeVariablesManyFromContextReturnVariables() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, conf1);
    StringBuilder b = new StringBuilder();
    when(state.get("var")).thenReturn(value);
    for (RuntimeVariable var : utils.getRuntimeVariables(context, "vars")) {
      b.append(var.getValue(state));
    }
    assertEquals("first" + value + "third", b.toString());
  }

  @Test
  public void getRuntimeVariablesSingleFromContextReturnVariable() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, MAIN_ID));
    StringBuilder b = new StringBuilder();
    for (RuntimeVariable var : utils.getRuntimeVariables(context, "a")) {
      b.append(var.getValue(state));
    }
    assertEquals("1", b.toString());
  }

  @Test
  public void getBooleanIsTrueReturnTrue() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, BOOLS_ID));
    assertTrue(utils.getBoolean(context, "yes", false));
  }

  @Test
  public void getBooleanIsFalseReturnFalse() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, BOOLS_ID));
    assertFalse(utils.getBoolean(context, "no", true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getBooleanIsUnknownThrowException() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, BOOLS_ID));
    utils.getBoolean(context, "unknown", true);
  }

  @Test
  public void getBooleanIsBlankReturnDefault() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, BOOLS_ID));
    assertTrue(utils.getBoolean(context, "blank", true));
  }

  @Test
  public void getBooleanIsMissingReturnDefault() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, utils.getGroup(conf1, BOOLS_ID));
    assertFalse(utils.getBoolean(context, "missing", false));
  }

  @Test
  public void getOptionalGroupGroupNotExistingReturnNull() {
    assertNull(utils.getOptionalGroup(utils.getGroup(conf1, MAIN_ID), "d"));
  }

  @Test
  public void getStringExistsReturnsString() {
    assertEquals("1", utils.getString(utils.getGroup(utils.getGroup(conf1, MAIN_ID), "a")));
  }

  @Test
  public void getRuntimeVariableConstantReturnRuntimeVariable() {
    assertEquals("1", utils.getRuntimeVariable(utils.getGroup(utils.getGroup(conf1, MAIN_ID), "a"))
      .getValue(state));
  }

  @Test
  public void getSingleChidlGroupReturnSingleGroup() {
    Config onlyOne = utils.getSingleChildGroup(utils.getGroup(conf1, "single"));
    assertEquals("onlyOne", onlyOne.getName());
    assertEquals("123", onlyOne.getValue());
  }

  @Test
  public void groupSelectorFirstNotReturnsPredicate() {
    String name1 = RandomStringUtils.random(32);
    String name2 = RandomStringUtils.random(32);
    String name3 = RandomStringUtils.random(32);
    Predicate<Config> pred = utils.groupSelectorFirstNot(name1, name2);

    assertFalse(pred.test(new ConfigImpl(name1, "")));
    assertFalse(pred.test(new ConfigImpl(name2, "")));
    assertTrue(pred.test(new ConfigImpl(name3, "")));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = utils.toString();
    assertTrue(text.contains(RuntimeVariableFactoryImpl.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(loader)));
    assertTrue(text.contains(String.valueOf(runtimeVariableFactory)));
  }

  @Test
  public void getOptionalLongMissingReturnsDefault() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, conf1);
    assertEquals(1234L, utils.getOptionalLong(context, "missing", 1234L));
  }

  @Test
  public void getOptionalLongPresentReturnsValue() {
    PluginContextImpl context = new PluginContextImpl(appState, conf1, conf1);
    assertEquals(1111L, utils.getOptionalLong(context, "long", 1234L));
  }

  private Config config(String path) {
    try {
      return parser.parse(loader.apply(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
