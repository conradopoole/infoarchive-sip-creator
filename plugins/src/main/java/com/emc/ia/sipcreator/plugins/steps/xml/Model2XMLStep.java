/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import com.emc.ia.sdk.support.io.FileBuffer;
import com.emc.ia.sipcreator.api.Model;
import com.emc.ia.sipcreator.api.ModelBatchSerializer;
import com.emc.ia.sipcreator.api.ModelSource;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.MutableFilesCollection;
import com.emc.ia.sipcreator.utils.Params;

public class Model2XMLStep implements Step {

  private final ModelSource source;
  private final ModelBatchSerializer serializer;
  private final RuntimeVariable directoryVariable;

  public Model2XMLStep(ModelSource source, ModelBatchSerializer serializer, RuntimeVariable directoryVariable) {
    Params.notNull(source, "Model2XMLStep.source");
    Params.notNull(serializer, "Model2XMLStep.serializer");
    Params.notNull(directoryVariable, "Model2XMLStep.directoryVariable");
    this.source = source;
    this.serializer = serializer;
    this.directoryVariable = directoryVariable;
  }

  @Override
  public void run(RuntimeState state) {
    try {
      String directoryPath = directoryVariable.getValue(state);
      File directory = new File(directoryPath);

      // TODO: fix naming strategy
      File file = new File(directory, UUID.randomUUID()
        .toString() + ".xml");
      // TODO: add segmentation
      serializer.open(new FileBuffer(file));
      Iterator<Model> models = source.getModels(state);
      while (models.hasNext()) {
        Model model = models.next();
        serializer.serialize(model);
      }
      serializer.end();

      MutableFilesCollection collection = new MutableFilesCollection();
      collection.add(file);
      // TODO: warn on overwrite, allow append
      state.set("files", collection);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Model2XMLStep [source=" + source + ", serializer=" + serializer + ", directoryVariable=" + directoryVariable
        + "]";
  }

}
