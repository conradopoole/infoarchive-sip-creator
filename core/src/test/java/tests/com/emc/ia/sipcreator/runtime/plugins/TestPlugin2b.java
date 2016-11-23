/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime.plugins;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class TestPlugin2b extends AbstractPluginImpl<Object> {

  private static final String NAME = "test2";

  private static PluginDocumentation documentation = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Object.class)
    .description("A test plugin")
    .build();

  public TestPlugin2b() {
    super(NAME, Object.class, documentation);
  }

  @Override
  public Object create(PluginContext context) {

    Config config = context.getConfig();
    if (config.hasValue()) {
      return new Object();
    } else {
      return null;
    }

  }

}
