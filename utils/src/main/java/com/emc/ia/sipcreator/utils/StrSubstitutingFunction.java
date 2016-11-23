/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.utils;

import java.util.function.Function;

import org.apache.commons.lang3.text.StrSubstitutor;

public class StrSubstitutingFunction implements Function<String, String> {

  private final StrSubstitutor substitutor;

  public StrSubstitutingFunction(StrSubstitutor substitutor) {
    Params.notNull(substitutor, "StrSubstitutingFunction.substitutor");
    this.substitutor = substitutor;
  }

  @Override
  public String apply(String text) {
    return substitutor.replace(text);
  }

  @Override
  public String toString() {
    return "StrSubstitutingFunction [substitutor=" + substitutor + "]";
  }

}
