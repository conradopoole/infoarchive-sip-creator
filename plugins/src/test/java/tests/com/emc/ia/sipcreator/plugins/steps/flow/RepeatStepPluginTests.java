/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.flow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class RepeatStepPluginTests extends PluginTestBase {

  private static final String REPEAT = "repeat";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(REPEAT));

    chk.name(REPEAT);
    chk.type(Step.class);
    chk.description("Repeates a sequence of steps a specified number of times.");
    chk.param("times", RuntimeVariable.class,
        "The runtime variable representing the number of times the contained steps should be repeated.");
    chk.param("variable", String.class,
        "The variable that should hold the value indicating which repetition of the contained steps are currently being executed.",
        "i");
    chk.param("steps", Step.class, "The steps that should be repeated.");
  }

  @Test
  public void runDoesNothing() {
    PluginContext context = context("repeat.yml", REPEAT);

    Step step = newInstance(Step.class, REPEAT, context);
    RuntimeState state = state("result", "");
    step.run(state);
    assertEquals("123", state.get("result"));
  }

}
