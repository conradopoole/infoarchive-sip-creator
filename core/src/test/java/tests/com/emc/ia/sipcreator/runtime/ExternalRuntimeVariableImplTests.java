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
import com.emc.ia.sipcreator.api.RuntimeVariable;
import com.emc.ia.sipcreator.impl.ExternalRuntimeVariableImpl;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.utils.TextResourceLoader;

public class ExternalRuntimeVariableImplTests {

  private RuntimeVariable fileVariable;
  private Function<String, String> loader;
  private RuntimeVariable variable;

  @Before
  public void before() {
    loader = new TextResourceLoader(new File("src/test/resources/" + StringUtils.replace(getClass().getPackage()
      .getName(), ".", "/")));
    RuntimeVariableFactoryImpl factory = new RuntimeVariableFactoryImpl(loader);
    fileVariable = factory.create("$context.var$");
    variable = factory.create("include($context.var$)");
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullLoaderThrowException() {
    new ExternalRuntimeVariableImpl(null, fileVariable);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createNullFileVariableThrowException() {
    new ExternalRuntimeVariableImpl(loader, null);
  }

  @Test
  public void getValueLoadsReferencedFileAndReturnsContent() {
    RuntimeState state = mock(RuntimeState.class);
    when(state.get("var")).thenReturn("a.txt");
    assertEquals("10 As. AAAAAAAAAA", variable.getValue(state));
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = variable.toString();
    assertTrue(text.contains(ExternalRuntimeVariableImpl.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(loader)));
    assertTrue(text.contains(String.valueOf(fileVariable)));
  }
}
