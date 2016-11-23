/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.impl.RuntimeVariableImpl;

public class RuntimeVariableImplTests {

  private RuntimeVariable variable;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    Function<String, String> loader = mock(Function.class);
    RuntimeVariableFactory factory = new RuntimeVariableFactoryImpl(loader);
    variable = factory.create("$context.var$");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullTemplateThrowException() {
    new RuntimeVariableImpl(null);
  }

  @Test
  public void getValueLoadsReferencedFileAndReturnsContent() {
    String rendered = RandomStringUtils.random(32);
    RuntimeState state = mock(RuntimeState.class);
    when(state.get("var")).thenReturn(rendered);
    assertEquals(rendered, variable.getValue(state));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = variable.toString();
    assertTrue(text.contains(RuntimeVariableImpl.class.getSimpleName()));

  }
}
