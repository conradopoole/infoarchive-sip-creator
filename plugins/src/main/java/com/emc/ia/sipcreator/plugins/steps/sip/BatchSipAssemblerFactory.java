/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.sip;

import com.emc.ia.sdk.sip.assembly.BatchSipAssembler;
import com.emc.ia.sipcreator.api.AIU;
import com.emc.ia.sipcreator.api.RuntimeState;

public interface BatchSipAssemblerFactory {

  BatchSipAssembler<AIU> create(RuntimeState state);
}
