/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.sql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.mockito.Mockito;

import com.emc.ia.sipcreator.plugins.sql.SQLResultConverter;

public class SQLResultConverterTests {

  @Test(expected = InvocationTargetException.class)
  public void createUtilityClassThrowsException()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Constructor<?>[] cons = SQLResultConverter.class.getDeclaredConstructors();
    cons[0].setAccessible(true);
    cons[0].newInstance((Object[])null);
  }

  @Test
  public void convertStringReturnString() throws SQLException, IOException {
    String value = RandomStringUtils.random(32);
    assertEquals(value, SQLResultConverter.convert(value));
  }

  @Test
  public void convertClobReturnString() throws SQLException, IOException {
    Clob clob = mock(Clob.class);
    String value = RandomStringUtils.random(32);
    Mockito.when(clob.getCharacterStream())
      .thenReturn(new StringReader(value));
    assertEquals(value, SQLResultConverter.convert(clob));
  }
}
