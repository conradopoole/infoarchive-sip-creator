/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emc.ia.sipcreator.utils.StrSubstitutingFunction;

public class StrSubstitutingFunctionTests {

  private StrSubstitutor substitutor;
  private StrSubstitutingFunction function;

  @Before
  public void before() {
    substitutor = Mockito.mock(StrSubstitutor.class);
    function = new StrSubstitutingFunction(substitutor);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullSubstitutorThrowException() {
    new StrSubstitutingFunction(null);
  }

  @Test
  public void applyDelegatesToSubstitutorReturnsResponse() {
    String input = RandomStringUtils.random(32);
    String expected = RandomStringUtils.random(32);
    Mockito.when(substitutor.replace(input))
      .thenReturn(expected);
    assertEquals(expected, function.apply(input));
  }

  @Test
  public void toStringOnInstanceReturnsDescriptiveString() {
    String text = function.toString();
    assertTrue(text.contains(StrSubstitutingFunction.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(substitutor)));
  }

}
