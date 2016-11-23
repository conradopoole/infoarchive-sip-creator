/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.function.Function;

import org.apache.commons.lang3.text.StrLookup;

public class StrLookupWithImport extends StrLookup<String> {

  private static final String PREFIX = "import:";
  private final StrLookup<String> delegate;
  private final Function<String, String> loader;

  public StrLookupWithImport(StrLookup<String> delegate, Function<String, String> loader) {
    Params.notNull(delegate, "StrLookupWithImport.delegate");
    Params.notNull(loader, "StrLookupWithImport.loader");
    this.delegate = delegate;
    this.loader = loader;
  }

  @Override
  public String lookup(String key) {
    Params.notBlank(key, "StrLookupWithImport#lookup:key");

    if (key.startsWith(PREFIX)) {
      String path = key.substring(PREFIX.length());
      return loader.apply(path);
    }

    return delegate.lookup(key);
  }

  @Override
  public String toString() {
    return "StrLookupWithImport [delegate=" + delegate + ", loader=" + loader + "]";
  }

}
