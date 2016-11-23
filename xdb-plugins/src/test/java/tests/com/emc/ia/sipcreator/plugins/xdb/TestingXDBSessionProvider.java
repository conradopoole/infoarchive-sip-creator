/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.xdb;

import com.emc.ia.sipcreator.plugins.xdb.XDBSessionProvider;
import com.emc.ia.sipcreator.testing.xdb.TemporaryXDBResource;
import com.xhive.core.interfaces.XhiveSessionIf;

public class TestingXDBSessionProvider implements XDBSessionProvider {

  private final TemporaryXDBResource xdb;

  public TestingXDBSessionProvider(TemporaryXDBResource xdb) {
    this.xdb = xdb;
  }

  @Override
  public XhiveSessionIf getSession() {
    return xdb.getSession();
  }

  @Override
  public void releaseSession(XhiveSessionIf session) {
    xdb.releaseSession(session);
  }

}
