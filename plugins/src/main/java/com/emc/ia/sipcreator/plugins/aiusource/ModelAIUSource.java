/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.aiusource;

import java.util.Iterator;

import com.emc.ia.sipcreator.api.AIUCollection;
import com.emc.ia.sipcreator.api.AIUSource;
import com.emc.ia.sipcreator.api.DigitalObjectsFromModel;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;

public class ModelAIUSource implements AIUSource {

  private final ModelSource resultSupplier;
  private final ModelSerializer converter;
  private final DigitalObjectsFromModel extractor;

  public ModelAIUSource(ModelSerializer converter, DigitalObjectsFromModel extractor, ModelSource resultSupplier) {
    this.converter = converter;
    this.extractor = extractor;
    this.resultSupplier = resultSupplier;
  }

  @Override
  public AIUCollection getAIUs(RuntimeState state) {
    Iterator<Model> models = resultSupplier.getModels(state);
    return new AIUIteratorFromModelIteratorAdapter(converter, extractor, models);
  }
}
