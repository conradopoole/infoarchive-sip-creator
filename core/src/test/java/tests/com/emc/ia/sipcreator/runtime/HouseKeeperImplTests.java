/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.runtime;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.emc.ia.sipcreator.runtime.HouseKeeperImpl;

public class HouseKeeperImplTests {

  private Closeable resource1;
  private Closeable resource2;
  private HouseKeeperImpl houseKeeper;

  @Before
  public void before() {
    resource1 = mock(Closeable.class);
    resource2 = mock(Closeable.class);
    houseKeeper = new HouseKeeperImpl();
  }

  @Test
  public void addIsNullSafe() throws IOException {
    houseKeeper.add(resource1);
    houseKeeper.add(null);
    houseKeeper.add(resource2);
    houseKeeper.close();

    verify(resource1).close();
    verify(resource2).close();
  }

  @Test
  public void closeClosesAllEvenIfItExceptionIsThrown() throws IOException {
    IOException exception = new IOException();
    Mockito.doThrow(exception)
      .when(resource1)
      .close();

    houseKeeper.add(resource1);
    houseKeeper.add(null);
    houseKeeper.add(resource2);
    houseKeeper.close();

    verify(resource1).close();
    verify(resource2).close();
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    houseKeeper.add(resource1);
    houseKeeper.add(resource2);
    String text = houseKeeper.toString();
    assertTrue(text.contains(HouseKeeperImpl.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(resource1)));
    assertTrue(text.contains(String.valueOf(resource2)));
  }
}
