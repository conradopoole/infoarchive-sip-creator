/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.api.RuntimeState;
import com.emc.ia.sipcreator.impl.RuntimeStateSTAdaptor;

public class RuntimeStateSTAdaptorTests {

  private RuntimeState state;

  @Before
  public void before() {
    state = mock(RuntimeState.class);
  }

  @Test
  public void getPropertyReturnsPropertyFromRuntimeState() {
    Object value = new Object();
    String key = RandomStringUtils.random(32);
    when(state.get(key)).thenReturn(value);
    assertEquals(value, new RuntimeStateSTAdaptor().getProperty(null, null, state, null, key));
  }
}
