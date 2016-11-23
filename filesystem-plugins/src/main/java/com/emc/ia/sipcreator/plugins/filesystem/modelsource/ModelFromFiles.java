/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem.modelsource;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.EnrichModelFromDomainObject;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.SimpleModelImpl;

public class ModelFromFiles implements CloseableIterator<Model> {

  private final Iterator<File> files;
  private final EnrichModelFromDomainObject<File> enricher;

  public ModelFromFiles(Iterator<File> files, EnrichModelFromDomainObject<File> enricher) {
    this.files = files;
    this.enricher = enricher;
  }

  @Override
  public boolean hasNext() {
    return files.hasNext();
  }

  @Override
  public Model next() {
    File file = files.next();
    SimpleModelImpl model = new SimpleModelImpl();
    model.set("name", file.getName());
    model.set("size", file.length());
    model.set("absolutepath", file.getAbsolutePath());
    enricher.enrich(model, file);
    return model;
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

}
