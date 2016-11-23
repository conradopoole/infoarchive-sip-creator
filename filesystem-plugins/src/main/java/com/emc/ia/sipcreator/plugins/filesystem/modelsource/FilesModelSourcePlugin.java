/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.modelsource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.emc.ia.sipcreator.api.AbstractPluginImpl;
import com.emc.ia.sipcreator.api.CompoundModelEnricher;
import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.NoEnricher;
import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.doc.PluginDocumentation;
import com.emc.ia.sipcreator.api.doc.PluginDocumentationBuilder;
import com.emc.ia.sipcreator.utils.ConstantPredicate;

public class FilesModelSourcePlugin extends AbstractPluginImpl<ModelSource> {

  private static final String NAME = "files";

  private static final PluginDocumentation DOCUMENTATION = PluginDocumentationBuilder.builder()
    .name(NAME)
    .type(ModelSource.class)
    .description("Creates models by traversing the filesystem.")
    .param("directory", RuntimeVariable.class, "The directory to start the filesystem traversal from.")
    .param("enrich", EnrichModelFromDomainObject.class, "Model enrichers.")
    .build();

  public FilesModelSourcePlugin() {
    super(NAME, ModelSource.class, DOCUMENTATION);
  }

  @SuppressWarnings("unchecked")
  @Override
  public ModelSource create(PluginContext context) {
    ConfigUtils configUtils = context.configUtils();

    RuntimeVariable rootDirectoryVariable = configUtils.getRuntimeVariable(context, "directory");

    List<EnrichModelFromDomainObject<File>> enrichers = new ArrayList<EnrichModelFromDomainObject<File>>();

    configUtils.forEach(configUtils.getOptionalGroup(context.getConfig(), "enrich"),
        it -> enrichers.add(context.newObject(EnrichModelFromDomainObject.class, it.getName(), it)));

    EnrichModelFromDomainObject<File> enricher = createEnricher(enrichers);

    // TODO: make configurable
    Predicate<File> includeFile = new ConstantPredicate<>(true);
    Predicate<File> inspectDirectory = new ConstantPredicate<>(true);

    return new FilesModelSource(rootDirectoryVariable, enricher, includeFile, inspectDirectory);
  }

  private EnrichModelFromDomainObject<File> createEnricher(List<EnrichModelFromDomainObject<File>> enrichers) {
    if (enrichers.isEmpty()) {
      return new NoEnricher<>();
    } else if (enrichers.size() == 1) {
      return enrichers.get(0);
    } else {
      return new CompoundModelEnricher<>(enrichers);
    }
  }
}
