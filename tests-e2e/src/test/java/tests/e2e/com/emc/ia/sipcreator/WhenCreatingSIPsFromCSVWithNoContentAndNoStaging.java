/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import java.io.IOException;

import org.junit.Test;

public class WhenCreatingSIPsFromCSVWithNoContentAndNoStaging extends E2ETestCase {

  @Test
  public void shouldGenerateValidSIP() throws IOException {
    generate("csv1").assertFileCount(2)
      .assertPackagingInformation(2)
      .assertPreservationInformationIdenticalTo("csv1.result.xml");
  }
}
