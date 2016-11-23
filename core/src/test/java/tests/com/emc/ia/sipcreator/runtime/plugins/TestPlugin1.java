/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime.plugins;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class TestPlugin1 extends AbstractPluginImpl<String> {

  private static final String NAME = "test1";
  private static final String PARAM_TEXT = "text";

  private static PluginDocumentation documentation = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(String.class)
    .description("A test plugin")
    .param(PARAM_TEXT, String.class, "The text to return")
    .build();

  public TestPlugin1() {
    super(NAME, String.class, documentation);
  }

  @Override
  public String create(PluginContext context) {

    Config config = context.getConfig();
    return (String)config.getValue(PARAM_TEXT);

  }

}
