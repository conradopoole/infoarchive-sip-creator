/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.flow;

import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class NopStepPluginTests extends PluginTestBase {

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("nop"));
    chk.name("nop");
    chk.type(Step.class);
    chk.description("Does nothing.");
  }

  @Test
  public void runDoesNothing() {
    PluginContext context = context("nop.yml", "main");

    Step step = newInstance(Step.class, "nop", context);
    step.run(state("name", "world"));

    // TODO: verify that it did nothing
  }

}
