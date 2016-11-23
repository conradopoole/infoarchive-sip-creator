/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;

import com.emc.ia.sipcreator.api.FilesCollection;

public final class FileSources {

  private FileSources() {
    throw new IllegalStateException(
        "FileSources is a static utility class and its constructor should never be called.");
  }

  public static Iterator<File> of(Object object) {
    if (object instanceof FilesCollection) {
      return ((FilesCollection)object).iterator();
    } else if (object instanceof File) {
      return Collections.singleton((File)object)
        .iterator();
    } else if (object instanceof String) {
      return Collections.singleton(new File((String)object))
        .iterator();
    } else if (object == null) {
      return Collections.<File>emptyList()
        .iterator();
    } else {
      throw new IllegalArgumentException("Object of type " + object.getClass()
        .getCanonicalName() + " cannot be coerced to a collection of files.");
    }
  }
}
