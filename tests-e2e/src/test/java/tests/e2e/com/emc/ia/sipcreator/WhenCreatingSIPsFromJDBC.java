/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import java.io.IOException;

import org.junit.Test;

import com.emc.ia.sipcreator.testing.e2e.E2ETestCase;

public class WhenCreatingSIPsFromJDBC extends E2ETestCase {

  @Test
  public void shouldGenerateValidSIP() throws IOException {
    generate("sql1").assertFileCount(2)
      .assertPackagingInformation(3)
      .dumpPreservationInformation()
      .assertPreservationInformationIdenticalTo("sql1.result.xml");
  }

}
