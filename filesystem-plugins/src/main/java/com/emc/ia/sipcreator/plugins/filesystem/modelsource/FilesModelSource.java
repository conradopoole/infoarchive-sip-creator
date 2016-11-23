/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.modelsource;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.utils.FilesIterator;
import com.emc.ia.sipcreator.utils.Params;

public class FilesModelSource implements ModelSource {

  private final RuntimeVariable rootDirectoryVariable;
  private final EnrichModelFromDomainObject<File> enricher;
  private final Predicate<File> includeFile;
  private final Predicate<File> inspectDirectory;

  public FilesModelSource(RuntimeVariable rootDirectoryVariable, EnrichModelFromDomainObject<File> enricher,
      Predicate<File> includeFile, Predicate<File> inspectDirectory) {
    Params.notNull(rootDirectoryVariable, "FilesModelSource.rootDirectoryVariable");
    Params.notNull(enricher, "FilesModelSource.enricher");
    Params.notNull(includeFile, "FilesModelSource.includeFile");
    Params.notNull(inspectDirectory, "FilesModelSource.inspectDirectory");
    this.rootDirectoryVariable = rootDirectoryVariable;
    this.enricher = enricher;
    this.includeFile = includeFile;
    this.inspectDirectory = inspectDirectory;
  }

  @Override
  public CloseableIterator<Model> getModels(RuntimeState state) {
    String rootDirectory = rootDirectoryVariable.getValue(state);
    FilesIterator files = new FilesIterator(inspectDirectory, includeFile, new File(rootDirectory));
    return new ModelFromFiles(files, enricher);
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public String toString() {
    return "FilesModelSource [rootDirectoryVariable=" + rootDirectoryVariable + ", enricher=" + enricher
        + ", includeFile=" + includeFile + ", inspectDirectory=" + inspectDirectory + "]";
  }

}
