/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.api;

import java.util.function.Function;

public interface ModelTransformation {

  Function<Model, Model> getTransform(RuntimeState state);
}
