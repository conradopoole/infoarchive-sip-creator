/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.xdb;

import com.xhive.core.interfaces.XhiveSessionIf;

public interface XDBSessionProvider {

  XhiveSessionIf getSession();

  void releaseSession(XhiveSessionIf session);
}
