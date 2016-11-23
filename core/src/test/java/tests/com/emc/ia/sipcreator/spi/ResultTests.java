/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import com.emc.ia.sipcreator.spi.Result;

public class ResultTests {

  private int returnCode;
  private String message;
  private Result result;

  @Before
  public void before() {
    returnCode = RandomUtils.nextInt();
    message = RandomStringUtils.random(32);
    result = new Result(returnCode, message);
  }

  @Test
  public void getReturnCodeReturnsReturnCode() {
    assertEquals(returnCode, result.getReturnCode());
  }

  @Test
  public void getMessageReturnsMessage() {
    assertEquals(message, result.getReturnMessage());
  }

  @Test
  public void ofReturnCodeCreatesWithBlankMessage() {
    Result res = Result.of(returnCode);
    assertEquals("", res.getReturnMessage());
    assertEquals(returnCode, res.getReturnCode());
  }

  @Test
  public void toStringReturnsDescriptiveString() {
    String text = result.toString();
    assertTrue(text.contains(Result.class.getSimpleName()));
    assertTrue(text.contains(String.valueOf(returnCode)));
    assertTrue(text.contains(String.valueOf(message)));
  }
}
