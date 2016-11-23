/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

public class ClassPathTextResourceLoader implements Function<String, String> {

  private final Class<?> parent;

  public ClassPathTextResourceLoader(Class<?> parent) {
    this.parent = parent;
    Params.notNull(parent, "ClasspathResourceLoader.parent");
  }

  @Override
  public String apply(String path) {
    try {
      URL url = parent.getResource(path);
      if (url == null) {
        throw new IllegalArgumentException("Resource " + path + " not found relative to " + parent.getCanonicalName());
      } else {
        return IOUtils.toString(url, StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load configuration fragment specified by path " + path, e);
    }
  }

  @Override
  public String toString() {
    return "ClasspathResourceLoader [parent=" + parent + "]";
  }

}
