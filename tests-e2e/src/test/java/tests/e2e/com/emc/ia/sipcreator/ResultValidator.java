/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class ResultValidator {

  private final File folder;
  private final Object owner;

  public ResultValidator(File folder, Object owner) {
    this.folder = folder;
    this.owner = owner;
  }

  public void assertTempFileIsEqualTo(String filename, String resource) throws IOException {

    try (
        Reader actualReader =
            new InputStreamReader(new FileInputStream(new File(folder, filename)), StandardCharsets.UTF_8);
        Reader expectedReader = new InputStreamReader(owner.getClass()
          .getResourceAsStream(resource), StandardCharsets.UTF_8)) {
      String actual = IOUtils.toString(actualReader);
      String expected = IOUtils.toString(expectedReader);

      assertEquals(expected, actual);
    }

  }

}
