/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.steps.sip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;
import com.emc.ia.sipcreator.testing.sip.SIPFileValidator;

public class CreateSIPStepPluginTests extends PluginTestBase {

  private static final String SIP_CREATE = "sip.create";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(SIP_CREATE));
    chk.name(SIP_CREATE);
    chk.type(Step.class);
  }

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void runCreatesTheXML() throws IOException {
    File dir = testFolder.newFolder("outputdir");

    PluginContext context = context("createsip.yml", SIP_CREATE);

    Step step = newInstance(Step.class, SIP_CREATE, context);

    List<Model> models = new ArrayList<Model>();
    models.add(model());
    models.add(model());

    RuntimeState state = state("models", models, "dir", dir.getAbsolutePath());

    step.run(state);

    File[] files = dir.listFiles();
    assertNotNull(files);
    assertEquals(1, files.length);

    SIPFileValidator validator = new SIPFileValidator(files[0]);

    validator.assertFileCount(2)
      .assertPackagingInformation(2)
      .assertPreservationInformationIdenticalToText(getExpected(models));

  }

  private String getExpected(List<Model> models) throws IOException {
    StringBuilder writer = new StringBuilder(512);
    writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects xmlns=\"myschema\">\n");
    for (Model model : models) {
      writer.append(
          String.format("<object><name>%s</name><value>%s</value></object>", model.get("name"), model.get("value")));
    }
    writer.append("</objects>");
    return writer.toString();

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
