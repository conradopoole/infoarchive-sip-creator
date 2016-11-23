/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.digitalobjects;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class NoContentPluginTests extends PluginTestBase {

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("no"));

    chk.name("no");
    chk.type(DigitalObjectsFromModel.class);
    chk.description("Extracts no digital objects from a model.");
  }

  @Test
  public void getDigitalObjectsReturnNothing() throws IOException {

    PluginContext context = context("digitalobjects.yml", "nocontent");

    DigitalObjectsFromModel extractor = newInstance(DigitalObjectsFromModel.class, "no", context);

    assertTrue(extractor.getDigitalObjects(null)
      .isEmpty());
  }

}
