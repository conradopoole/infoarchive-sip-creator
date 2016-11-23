/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.runtime;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.ia.sipcreator.api.HouseKeeper;

public class HouseKeeperImpl implements Closeable, HouseKeeper {

  private static final Logger LOG = LoggerFactory.getLogger(HouseKeeperImpl.class);

  private final List<Closeable> resources = new ArrayList<Closeable>();

  @Override
  public void add(final Closeable resource) {
    LOG.debug("Registering {} for housekeeping.", resource);
    // Ignoring nulls.
    if (resource != null) {
      resources.add(resource);
    }
  }

  @Override
  public void close() {
    LOG.debug("Cleaning up resources.");
    for (final Closeable resource : resources) {
      try {
        LOG.debug("Cleaning up {}.", resource);
        resource.close();
      } catch (final Exception e) {
        LOG.warn("Failed to clean resource.", e);
      }
    }
    resources.clear();
  }

  @Override
  public String toString() {
    return "HouseKeeperImpl [resources=" + resources + "]";
  }

}
