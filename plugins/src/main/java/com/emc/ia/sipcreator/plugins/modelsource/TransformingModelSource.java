/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.modelsource;

import java.io.IOException;

import com.emc.ia.sipcreator.api.CloseableIterator;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.ModelTransformation;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.utils.TransformingIterator;

public class TransformingModelSource implements ModelSource {

  private final ModelSource source;
  private final ModelTransformation transform;

  public TransformingModelSource(ModelSource source, ModelTransformation transform) {
    this.source = source;
    this.transform = transform;
  }

  @Override
  public CloseableIterator<Model> getModels(RuntimeState state) {
    return new TransformingIterator<Model, Model>(source.getModels(state), transform.getTransform(state));
  }

  @Override
  public void close() throws IOException {
    source.close();
  }

}
