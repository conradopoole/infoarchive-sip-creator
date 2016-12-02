/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.flow;

import com.emc.ia.sipcreator.api.RuntimeState;

public interface RuntimeCondition {

  boolean test(RuntimeState state);
}
