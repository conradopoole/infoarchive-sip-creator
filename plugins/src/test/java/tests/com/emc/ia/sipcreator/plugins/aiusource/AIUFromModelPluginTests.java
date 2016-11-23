/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.aiusource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class AIUFromModelPluginTests extends PluginTestBase {

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin("aiuFromModel"));
    chk.name("aiuFromModel");
    chk.type(AIUSource.class);
    chk.description("Converts a collection of models to AIUs");
    chk.param("models", ModelSource.class, "The model source.");
    chk.param("serializer", ModelSerializer.class, "A serializer that knows how to serialize this model to XML.");
    chk.param("content", DigitalObjectsFromModel.class, "The digital object extrator.", "no");
  }

  @Test
  public void convert() throws IOException {

    PluginContext context = context("aiufrommodel.yml", "aius");

    AIUSource aiuSource = newInstance(AIUSource.class, "aiuFromModel", context);

    List<Model> models = new ArrayList<Model>();
    Model model = new SimpleModelImpl();
    String name = RandomStringUtils.randomAlphanumeric(32);
    String value = RandomStringUtils.randomAlphanumeric(32);

    model.set("name", name);
    model.set("value", value);
    models.add(model);

    RuntimeState state = state("models", models);
    Iterator<AIU> aius = aiuSource.getAIUs(state);
    AIU first = aius.next();
    StringWriter writer = new StringWriter();
    first.writeAsXml(writer);
    assertEquals(String.format("<object><name>%s</name><value>%s</value></object>", name, value), writer.toString());
    assertTrue(first.getDigitalObjects()
      .isEmpty());

    assertFalse(aius.hasNext());
  }

}
