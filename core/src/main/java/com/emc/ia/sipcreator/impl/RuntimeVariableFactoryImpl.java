/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.impl;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;
import com.emc.ia.sipcreator.utils.Params;

public class RuntimeVariableFactoryImpl implements RuntimeVariableFactory {

  private final Function<String, String> loader;
  private final Pattern p = Pattern.compile("(\\\\*)include(dynamic)?\\((.+)\\)");

  public RuntimeVariableFactoryImpl(Function<String, String> loader) {
    Params.notNull(loader, "RuntimeVariableFactoryImpl.loader");
    this.loader = loader;
  }

  @Override
  public RuntimeVariable create(String text) {
    Matcher m = p.matcher(text);
    if (m.matches()) {
      if (m.group(1)
        .length() > 0) {
        return createSimpleVariable(text.substring(1));
      } else {
        return new ExternalRuntimeVariableImpl(loader, createSimpleVariable(m.group(3)));
      }
    } else {
      return createSimpleVariable(text);
    }
  }

  private RuntimeVariable createSimpleVariable(String text) {
    STGroup group = new STGroup('$', '$');
    group.registerModelAdaptor(RuntimeState.class, new RuntimeStateSTAdaptor());
    group.defineTemplate("main", "context", text);
    ST template = group.getInstanceOf("main");
    return new RuntimeVariableImpl(template);
  }

  @Override
  public String toString() {
    return "RuntimeVariableFactoryImpl [loader=" + loader + "]";
  }

}
