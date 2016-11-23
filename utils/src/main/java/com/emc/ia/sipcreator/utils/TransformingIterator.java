/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.io.IOException;
import java.util.function.Function;

import com.emc.ia.sipcreator.api.CloseableIterator;

public class TransformingIterator<S, T> implements CloseableIterator<T> {

  private final CloseableIterator<? extends S> iterator;
  private final Function<S, T> transform;

  public TransformingIterator(CloseableIterator<? extends S> iterator, Function<S, T> transform) {
    Params.notNull(iterator, "TransformingIterator.iterator");
    Params.notNull(transform, "TransformingIterator.transform");
    this.iterator = iterator;
    this.transform = transform;
  }

  @Override
  public final boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public final T next() {
    return transform.apply(iterator.next());
  }

  @Override
  public final void remove() {
    iterator.remove();
  }

  @Override
  public String toString() {
    return "TransformingIterator [iterator=" + iterator + ", transform=" + transform + "]";
  }

  @Override
  public void close() throws IOException {
    iterator.close();
  }

}
