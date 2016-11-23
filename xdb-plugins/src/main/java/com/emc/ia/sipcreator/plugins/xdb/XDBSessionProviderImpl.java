/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import java.io.Closeable;
import java.io.IOException;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;

public class XDBSessionProviderImpl implements com.emc.ia.sipcreator.plugins.xdb.XDBSessionProvider, Closeable {

  private final String database;
  private final String password;
  private final String username;

  private final XhiveDriverIf driver;

  public XDBSessionProviderImpl(String url, String database, String username, String password) {
    this.database = database;
    this.username = username;
    this.password = password;

    driver = XhiveDriverFactory.getDriver(url);
    if (!driver.isInitialized()) {
      driver.init();
    }

    // TODO: housekeeper
    // TODO: pooling
  }

  @Override
  public XhiveSessionIf getSession() {
    XhiveSessionIf session = driver.createSession();
    session.connect(username, password.toCharArray(), database);
    return session;
  }

  @Override
  public void releaseSession(XhiveSessionIf session) {
    if (session != null) {
      session.disconnect();
      session.terminate();
    }
  }

  @Override
  public void close() throws IOException {
    driver.close();
  }
}
