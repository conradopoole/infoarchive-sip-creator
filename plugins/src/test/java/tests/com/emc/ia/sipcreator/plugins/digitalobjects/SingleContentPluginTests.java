/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.digitalobjects;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.emc.ia.sdk.sip.assembly.DigitalObject;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class SingleContentPluginTests extends PluginTestBase {

  private static final String SINGLE = "single";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(SINGLE));
    chk.name(SINGLE);
    chk.type(DigitalObjectsFromModel.class);
    chk.description("Extracts a single digital object from a model.");
    chk.param("path", String.class, "The property of the model that holds the path.");
    chk.param("reference", String.class, "The property of the model that holds the reference");
  }

  @Test
  public void getDigitalObjectsReturnSingle() throws IOException {
    String referenceInformation = RandomStringUtils.random(32);

    PluginContext context = context("digitalobjects.yml", SINGLE);

    DigitalObjectsFromModel extractor = newInstance(DigitalObjectsFromModel.class, SINGLE, context);

    Model model = new SimpleModelImpl();
    model.set("thereference", referenceInformation);
    model.set("thefile", getPathTo("digitalobject.txt"));
    List<DigitalObject> objects = extractor.getDigitalObjects(model);

    assertEquals(1, objects.size());

    DigitalObject object = objects.get(0);
    assertEquals(referenceInformation, object.getReferenceInformation());
    assertEquals("Content of the digital object.", textOf(object));
  }

  private String textOf(DigitalObject object) throws IOException {
    try (InputStream stream = object.get()) {
      return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }
  }

}
