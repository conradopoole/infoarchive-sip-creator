/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.spi;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.emc.ia.sipcreator.spi.ReturnCodes;

public class ReturnCodesTest {

  @Test(expected = InvocationTargetException.class)
  public void createUtilityClassThrowsException()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Constructor<?>[] cons = ReturnCodes.class.getDeclaredConstructors();
    cons[0].setAccessible(true);
    cons[0].newInstance((Object[])null);
  }

  @Test
  public void returnCodeForGeneralErrorIs1() {
    assertEquals(1, ReturnCodes.ERR_GENERAL);
  }

  @Test
  public void returnCodeForSuccessIs0() {
    assertEquals(0, ReturnCodes.SUCCESS);
  }

  @Test
  public void returnCodeForUsageErrorIs64() {
    assertEquals(64, ReturnCodes.ERR_USAGE);
  }
}
