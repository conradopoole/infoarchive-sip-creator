/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.Predicate;

public class FilesIterator implements Iterator<File> {

  private final Predicate<File> inspectFolder;
  private final Predicate<File> includeFile;

  private final Stack<File> directories = new Stack<File>();

  private File current;
  private Iterator<File> currentDirectory;

  public FilesIterator(Predicate<File> inspectFolder, Predicate<File> includeFile, File root) {
    Params.notNull(inspectFolder, "FilesIterator.inspectFolder");
    Params.notNull(includeFile, "FilesIterator.includeFile");
    Params.notNull(root, "FilesIterator.root");

    this.inspectFolder = inspectFolder;
    this.includeFile = includeFile;

    if (root.isFile()) {
      if (includeFile.test(root)) {
        current = root;
      } else {
        current = null;
      }
    } else {
      directories.push(root);
      advance();
    }
  }

  private void advance() {
    while (currentDirectory != null || !directories.isEmpty()) {
      while (currentDirectory != null && currentDirectory.hasNext()) {
        current = currentDirectory.next();
        if (current.isDirectory()) {
          if (inspectFolder.test(current)) {
            directories.push(current);
          }
        } else if (includeFile.test(current)) {
          return;
        }
      }
      current = null;
      nextDirectory();
    }
    current = null;
  }

  private void nextDirectory() {
    if (directories.isEmpty()) {
      currentDirectory = null;
    } else {
      File dir = directories.pop();
      File[] files = dir.listFiles();
      if (files == null) {
        currentDirectory = Collections.emptyListIterator();
      } else {
        currentDirectory = Arrays.asList(files)
          .iterator();
      }
    }
  }

  @Override
  public boolean hasNext() {
    return current != null;
  }

  @Override
  public File next() {
    File previous = current;
    advance();
    return previous;
  }

  @Override
  public String toString() {
    return "FilesIterator [inspectFolder=" + inspectFolder + ", includeFile=" + includeFile + ", current=" + current
        + ", directories=" + directories + ", currentDirectory=" + currentDirectory + "]";
  }

}
