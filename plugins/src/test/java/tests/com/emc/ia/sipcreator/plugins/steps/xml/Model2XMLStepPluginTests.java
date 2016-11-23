/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class Model2XMLStepPluginTests extends PluginTestBase {

  private static final String MODEL2XML = "model2xml";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(MODEL2XML));
    chk.name(MODEL2XML);
    chk.type(Step.class);
    chk.description("Converts models into XML.");
    chk.param("source", ModelSource.class, "The source of model instances.");
    chk.param("serializer", ModelSerializer.class, "The model serializer.");
    chk.param("directory", RuntimeVariable.class, "The directory where to place the serialized file.");
  }

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void runCreatesTheXML() throws IOException {
    File dir = testFolder.newFolder("outputdir");

    PluginContext context = context("model2xml.yml", MODEL2XML);

    Step step = newInstance(Step.class, MODEL2XML, context);

    List<Model> models = new ArrayList<Model>();
    models.add(model());
    models.add(model());

    RuntimeState state = state("models", models, "dir", dir.getAbsolutePath());

    step.run(state);

    File[] files = dir.listFiles();
    assertNotNull(files);
    assertEquals(1, files.length);

    File file = files[0];
    // TODO: verify naming
    assertEquals(expected(models), IOUtils.toString(file.toURI(), StandardCharsets.UTF_8));

  }

  private Object expected(List<Model> models) {

    StringBuilder b = new StringBuilder(512);
    b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects>");
    for (Model model : models) {
      b.append(
          String.format("<object><name>%s</name><value>%s</value></object>", model.get("name"), model.get("value")));
    }
    b.append("</objects>");
    return b.toString();
  }

  private Model model() {
    String name = RandomStringUtils.randomAlphanumeric(32);
    String value = RandomStringUtils.randomAlphanumeric(32);
    Model model = new SimpleModelImpl();
    model.set("name", name);
    model.set("value", value);
    return model;
  }

}
