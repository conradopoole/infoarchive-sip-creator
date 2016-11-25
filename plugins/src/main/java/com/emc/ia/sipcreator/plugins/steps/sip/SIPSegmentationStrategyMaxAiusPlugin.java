/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import com.emc.ia.sdk.sip.assembly.SipSegmentationStrategy;
import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

@SuppressWarnings("rawtypes")
public class SIPSegmentationStrategyMaxAiusPlugin extends AbstractPluginImpl<SipSegmentationStrategy> {

  private static final String NAME = "byMaxAius";
  private static final String PARAM_COUNT = "count";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(SipSegmentationStrategy.class)
    .description("Limits the number of AIUs a single SIP will contain to the specified number.")
    .param(PARAM_COUNT, Long.class, "The maxiumum number of AIUs a single SIP should contain.", "100000")
    .build();

  public SIPSegmentationStrategyMaxAiusPlugin() {
    super(NAME, SipSegmentationStrategy.class, DOCUMENTATION);
  }

  @Override
  public SipSegmentationStrategy<AIU> create(PluginContext context) {
    long maxAiusPerSip = context.configUtils().getOptionalLong(context, PARAM_COUNT, 100000L);
    return SipSegmentationStrategy.byMaxAius(maxAiusPerSip);
  }

}
