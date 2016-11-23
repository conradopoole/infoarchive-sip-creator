/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.io.IOException;
import java.util.Iterator;

public class DelegatingCloseableIterator<T> implements CloseableIterator<T> {

  private final Iterator<T> iterator;

  public DelegatingCloseableIterator(Iterator<T> iterator) {
    this.iterator = iterator;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public T next() {
    return iterator.next();
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

}
