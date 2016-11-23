/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.filesystem;

import java.io.File;
import java.util.List;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.utils.MutableFilesCollection;
import com.emc.ia.sipcreator.utils.Params;

public class FilesStep implements Step {

  private final List<RuntimeVariable> files;
  private final String variableName;

  public FilesStep(List<RuntimeVariable> files, String variableName) {
    Params.notNull(files, "FilesStep.files");
    Params.notBlank(variableName, "FilesStep.variableName");
    this.files = files;
    this.variableName = variableName;
  }

  @Override
  public void run(RuntimeState state) {
    MutableFilesCollection filesCollection = getFilesCollection(state);

    for (RuntimeVariable pathVariable : files) {
      String path = pathVariable.getValue(state);
      // TODO: relative folder?
      File file = new File(path);
      filesCollection.add(file);
    }
  }

  private MutableFilesCollection getFilesCollection(RuntimeState state) {
    Object object = state.get(variableName);
    MutableFilesCollection collection = null;
    if (object instanceof MutableFilesCollection) {
      collection = (MutableFilesCollection)object;
    } else { // if (object == null) {
      collection = new MutableFilesCollection();
      state.set(variableName, collection);
      // } else {
      // Todo handle merging of traversal-based?
      // TODO: warn that we are overwriting
      // collection = new MutableFilesCollection();
      // state.set(variableName, collection);
    }
    return collection;
  }

}
