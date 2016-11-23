/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.File;
import java.util.Iterator;
import java.util.function.Predicate;

import com.emc.ia.sipcreator.api.FilesCollection;

public class FilesCollectionByTraversal implements FilesCollection {

  private final Predicate<File> inspectFolder;
  private final Predicate<File> includeFile;
  private final File root;

  public FilesCollectionByTraversal(Predicate<File> inspectFolder, Predicate<File> includeFile, File root) {
    Params.notNull(inspectFolder, "DefaultFilesCollection.inspectFolder");
    Params.notNull(includeFile, "DefaultFilesCollection.includeFile");
    Params.notNull(root, "DefaultFilesCollection.root");
    this.includeFile = includeFile;
    this.inspectFolder = inspectFolder;
    this.root = root;
  }

  @Override
  public Iterator<File> iterator() {
    return new FilesIterator(inspectFolder, includeFile, root);
  }

  @Override
  public String toString() {
    return "DefaultFilesCollection [inspectFolder=" + inspectFolder + ", includeFile=" + includeFile + ", root=" + root
        + "]";
  }

}
