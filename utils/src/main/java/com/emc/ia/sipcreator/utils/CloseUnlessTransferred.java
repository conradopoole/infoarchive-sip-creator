/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

public class CloseUnlessTransferred<T extends AutoCloseable> implements AutoCloseable {

  private final T resource;
  private boolean transferred;

  public CloseUnlessTransferred(T resource) {
    this.resource = resource;
  }

  @Override
  public void close() throws Exception {
    if (!transferred) {
      resource.close();
    }
  }

  public T getResource() {
    return resource;
  }

  public boolean isTransferred() {
    return transferred;
  }

  public void setTransferred(boolean transferred) {
    this.transferred = transferred;
  }

  public static <T extends AutoCloseable> CloseUnlessTransferred<T> of(T resource) {
    return new CloseUnlessTransferred<>(resource);
  }
}
