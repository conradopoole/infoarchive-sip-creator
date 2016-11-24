/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import com.emc.ia.sipcreator.testing.e2e.E2ETestCase;
import com.emc.ia.sipcreator.testing.xdb.TemporaryXDBResource;

public class WhenCreatingSIPsFromCSVWithStagingButNoContent extends E2ETestCase {

  @ClassRule
  public static final TemporaryXDBResource XDB = new TemporaryXDBResource();

  @Test
  public void shouldGenerateValidSIP() throws IOException {

    generate("csv2").assertFileCount(2)
      .assertPackagingInformation(2)
      .assertPreservationInformationIdenticalTo("csv1.result.xml");
  }
}
