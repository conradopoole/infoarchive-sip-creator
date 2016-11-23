/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.emc.ia.sipcreator.api.FilesCollection;

public class MutableFilesCollection implements FilesCollection {

  private final List<File> files = new ArrayList<File>();

  @Override
  public Iterator<File> iterator() {
    return files.iterator();
  }

  public void add(File file) {
    files.add(file);
  }

  @Override
  public String toString() {
    return "MutableFilesCollection [files=" + files + "]";
  }

}
