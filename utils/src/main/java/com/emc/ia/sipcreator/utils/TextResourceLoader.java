/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

public class TextResourceLoader implements Function<String, String> {

  private final File configDirectory;

  public TextResourceLoader(File configDirectory) {
    Params.notNull(configDirectory, "TextResourceLoader.configDirectory");
    Params.notFalse(configDirectory.isDirectory(), "TextResourceLoader.configDirectory = {} is not a directory",
        configDirectory.getAbsolutePath());
    this.configDirectory = configDirectory;
  }

  @Override
  public String apply(String path) {
    try {
      File file = new File(path);
      if (!file.isAbsolute()) {
        file = new File(configDirectory, path);
      }
      return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load configuration fragment specified by path " + path, e);
    }
  }

  @Override
  public String toString() {
    return "TextResourceLoader [configDirectory=" + configDirectory.getAbsolutePath() + "]";
  }

}
