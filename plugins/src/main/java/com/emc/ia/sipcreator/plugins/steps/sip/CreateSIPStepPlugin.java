/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import java.io.File;

import com.emc.ia.sdk.sip.assembly.Assembler;
import com.emc.ia.sdk.sip.assembly.BatchSipAssembler;
import com.emc.ia.sdk.sip.assembly.HashedContents;
import com.emc.ia.sdk.sip.assembly.PackagingInformation;
import com.emc.ia.sdk.sip.assembly.SipAssembler;
import com.emc.ia.sdk.sip.assembly.SipSegmentationStrategy;
import com.emc.ia.sdk.sip.assembly.TemplatePdiAssembler;
import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.AbstractStepPlugin;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;

public class CreateSIPStepPlugin extends AbstractStepPlugin {

  private static final String NAME = "sip.create";
  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(Step.class)
    .description("Creates / updates a collection of files")
    .param("files", RuntimeVariable.class, "The files to add to the collection", "", true)
    .param("variable", String.class, "The name of the variable that holds the file collection", "files")
    .build();

  public CreateSIPStepPlugin() {
    super(NAME, DOCUMENTATION);
  }

  @Override
  public Step create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();
    final RuntimeVariable sipDirectory = configUtils.getRuntimeVariable(context, "directory");
    final RuntimeVariable holdingVar = configUtils.getRuntimeVariable(context, "holding");
    final RuntimeVariable applicationVar = configUtils.getRuntimeVariable(context, "application");
    final RuntimeVariable producerVar = configUtils.getRuntimeVariable(context, "producer");
    final RuntimeVariable entityVar = configUtils.getRuntimeVariable(context, "entity");
    final RuntimeVariable schemaVar = configUtils.getRuntimeVariable(context, "schema");
    final RuntimeVariable namespaceVar = configUtils.getRuntimeVariable(context, "namespaces");
    final RuntimeVariable rootElementVar = configUtils.getRuntimeVariable(context, "element");

    BatchSipAssemblerFactory factory = new BatchSipAssemblerFactory() {

      @Override
      public BatchSipAssembler<AIU> create(RuntimeState state) {
        PackagingInformation prototype = PackagingInformation.builder()
          .dss()
          .holding(holdingVar.getValue(state))
          .application(applicationVar.getValue(state))
          .producer(producerVar.getValue(state))
          .entity(entityVar.getValue(state))
          .schema(schemaVar.getValue(state))
          .end()
          .build();

        String rootElement = rootElementVar.getValue(state);
        String namespaces = namespaceVar.getValue(state);

        Assembler<HashedContents<AIU>> pdiAssembler = new TemplatePdiAssembler<>(
            new AIUTemplate("<?xml version=\"1.0\" encoding=\"UTF-8\"?><" + rootElement + " " + namespaces + ">",
                "</" + rootElement + ">"));
        SipAssembler<AIU> sipAssembler =
            SipAssembler.forPdiAndContent(prototype, pdiAssembler, new AIUDigitalObjectsExtractor());

        SipSegmentationStrategy<AIU> segmentationStrategy = createSegmentationStrategy(configUtils, context);

        String directory = sipDirectory.getValue(state);
        BatchSipAssembler<AIU> assembler =
            new BatchSipAssembler<>(sipAssembler, segmentationStrategy, new File(directory));

        return assembler;
      }

    };

    AIUSource aiuSource = configUtils.newObject(context, "source", AIUSource.class);
    return new CreateSIPStep(factory, aiuSource, "files");
  }

  @SuppressWarnings("unchecked")
  private SipSegmentationStrategy<AIU> createSegmentationStrategy(ConfigUtils configUtils, PluginContext context) {
    return (SipSegmentationStrategy<AIU>)configUtils
      .newOptionalObject(context, "segmentation", SipSegmentationStrategy.class)
      .orElse(SipSegmentationStrategy.byMaxAius(100000));
  }

}
