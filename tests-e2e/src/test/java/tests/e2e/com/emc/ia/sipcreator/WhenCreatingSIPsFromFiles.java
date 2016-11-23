/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import java.io.IOException;

import org.junit.Test;

public class WhenCreatingSIPsFromFiles extends E2ETestCase {

  @Test
  public void shouldGenerateValidSIP() throws IOException {
    generate("files1").assertFileCount(2)
      .assertPackagingInformation(3)
      .assertPreservationInformationIdenticalTo("files1.result.xml");
  }

  @Test
  public void shouldGenerateValidSIP2() throws IOException {
    generate("files2").assertFileCount(5)
      .assertPackagingInformation(3)
      .assertContentFileIdenticalTo("file1.txt", "samplefiles/file1.txt")
      .assertContentFileIdenticalTo("file2.txt", "samplefiles/file2.txt")
      .assertContentFileIdenticalTo("file3.txt", "samplefiles/file3.txt")
      .assertPreservationInformationIdenticalTo("files1.result.xml");

  }

}
