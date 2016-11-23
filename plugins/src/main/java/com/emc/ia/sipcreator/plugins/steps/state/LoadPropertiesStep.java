/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.plugins.steps.state;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import com.emc.ia.sipcreator.api.ConfigUtils;
import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.Step;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.utils.Params;

public class LoadPropertiesStep implements Step {

  private final RuntimeVariable propertyFileVariable;
  private final URIResolver resolver;
  private final RuntimeVariable prefixVariable;
  private final ConfigUtils configUtils;

  public LoadPropertiesStep(ConfigUtils configUtils, RuntimeVariable propertyFileVariable, URIResolver resolver,
      RuntimeVariable prefixVariable) {
    Params.notNull(configUtils, "LoadPropertiesStep.configUtils");
    Params.notNull(propertyFileVariable, "LoadPropertiesStep.propertyFileVariable");
    Params.notNull(resolver, "LoadPropertiesStep.resolver");
    Params.notNull(prefixVariable, "LoadPropertiesStep.prefixVariable");

    this.configUtils = configUtils;
    this.propertyFileVariable = propertyFileVariable;
    this.resolver = resolver;
    this.prefixVariable = prefixVariable;
  }

  @Override
  public void run(RuntimeState state) {
    try {
      String prefix = prefixVariable.getValue(state);
      String propertyFile = propertyFileVariable.getValue(state);
      try (InputStream stream = resolver.open(propertyFile)) {
        Properties properties = new Properties();
        properties.load(stream);
        for (Entry<Object, Object> entry : properties.entrySet()) {
          String name = prefix + entry.getKey()
            .toString();
          RuntimeVariable variable = configUtils.getRuntimeVariable(String.valueOf(entry.getValue()));
          String value = variable.getValue(state);
          state.set(name, value);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "LoadPropertiesStep [propertyFileVariable=" + propertyFileVariable + ", resolver=" + resolver
        + ", prefixVariable=" + prefixVariable + "]";
  }

}
