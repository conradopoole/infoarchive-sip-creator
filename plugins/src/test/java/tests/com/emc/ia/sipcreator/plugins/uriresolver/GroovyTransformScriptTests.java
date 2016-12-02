/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.uriresolver;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.SimpleModelImpl;
import com.emc.ia.sipcreator.plugins.transformation.GroovyTransformScriptFactory;

public class GroovyTransformScriptTests {

  private static final String VARIABLE = "variable";

  @Test
  public void test() {
    Function<Model, Model> transformation = new GroovyTransformScriptFactory()
      .build("i = 0; println \"init\" + i;\n transform { it.set(\"variable\",it.get(\"variable\") + i++); }");
    Model model = new SimpleModelImpl();
    model.set(VARIABLE, "value");
    Model model2 = new SimpleModelImpl();
    model2.set(VARIABLE, "value");
    transformation.apply(model);
    transformation.apply(model2);
    assertEquals("value0", model.get(VARIABLE));
    assertEquals("value1", model2.get(VARIABLE));
  }

}
