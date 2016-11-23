/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import com.emc.ia.sipcreator.testing.xdb.TemporaryXDBResource;

public class WhenWorkingWithXDB extends E2ETestCase {

  @ClassRule
  public static final TemporaryXDBResource XDB = new TemporaryXDBResource();

  @Test
  public void shouldGenerateValidReport() throws IOException {
    run("xdb1").assertTempFileIsEqualTo("result.txt", "xdb1.result.txt");
  }

}
