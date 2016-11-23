/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.state;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class SetVariableStepPluginsTest extends PluginTestBase {

  private static final String SET = "set";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(SET));

    chk.name(SET);
    chk.type(Step.class);
    chk.description("Sets the specified variables.");
    chk.param("<name>", RuntimeVariable.class, "Sets the variable <name> to the specified value.");
  }

  @Test
  public void foo() {
    PluginContext context = context("set.yml", SET);

    String value = randomString();

    Step echo = newInstance(Step.class, SET, context);
    RuntimeState state = state("name", value);
    echo.run(state);

    assertEquals("123", state.get("constant"));
    assertEquals(value, state.get("variable"));
    assertEquals("interpo" + value + "lated", state.get("interpolated"));
  }

}
