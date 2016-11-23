/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.api.RuntimeVariableFactory;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.utils.TextResourceLoader;

public class RuntimeVariableFactoryImplTests {

  private static final String B_TXT = "b.txt";
  private Function<String, String> loader;
  private RuntimeVariableFactory factory;
  private RuntimeState state;

  @Before
  public void before() {
    loader = new TextResourceLoader(new File("src/test/resources/" + StringUtils.replace(getClass().getPackage()
      .getName(), ".", "/")));
    factory = new RuntimeVariableFactoryImpl(loader);
    state = mock(RuntimeState.class);
  }

  @Test
  public void createSimpleVariableReturnContentOfVariable() {
    when(state.get("var")).thenReturn(B_TXT);
    assertEquals(B_TXT, factory.create("$context.var$")
      .getValue(state));
  }

  @Test
  public void createExternalVariableReturnContentOfReferencedResource() {
    when(state.get("var")).thenReturn(B_TXT);
    assertEquals("5 Bs. BBBBB", factory.create("include($context.var$)")
      .getValue(state));
  }

  @Test
  public void createEscapedExternalVariableReturnEscapedContent() {
    when(state.get("var")).thenReturn(B_TXT);
    assertEquals("include(b.txt)", factory.create("\\include($context.var$)")
      .getValue(state));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = factory.toString();
    assertTrue(text.contains(RuntimeVariableFactoryImpl.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(loader)));
  }
}
