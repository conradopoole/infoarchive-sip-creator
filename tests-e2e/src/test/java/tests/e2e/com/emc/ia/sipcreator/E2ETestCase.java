/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.e2e.com.emc.ia.sipcreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import com.emc.ia.sipcreator.cli.DefaultCommandLineInitiator;
import com.emc.ia.sipcreator.spi.Result;
import com.emc.ia.sipcreator.spi.ReturnCodes;
import com.emc.ia.sipcreator.testing.sip.SIPFileValidator;

public class E2ETestCase {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  public SIPFileValidator generate(String resourceName) throws IOException {
    File sipsFolder = folder.newFolder("sips");
    File tempFolder = folder.newFolder("temp");
    System.setProperty("sipsfolder", sipsFolder.getAbsolutePath());
    System.setProperty("tempfolder", tempFolder.getAbsolutePath());
    System.setProperty("configfolder", new File("src/test/resources/" + getClass().getPackage()
      .getName()
      .replace('.', '/')).getAbsolutePath());

    Result result = app(resourceName);
    assertEquals(ReturnCodes.SUCCESS, result.getReturnCode());
    File[] sipFiles = sipsFolder.listFiles();
    assertNotNull(sipFiles);
    assertEquals(1, sipFiles.length);
    File sipFile = sipFiles[0];
    return new SIPFileValidator(getClass(), sipFile);

  }

  private Result app(String resourceName) {
    try {
      URL url = getClass().getResource(resourceName + ".yml");
      File file = new File(url.toURI());
      String path = file.getAbsolutePath();
      String[] args = { "--config", path };
      return new DefaultCommandLineInitiator().parse(args)
        .call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ResultValidator run(String resourceName) throws IOException {
    File tempFolder = folder.newFolder("temp");
    System.setProperty("tempfolder", tempFolder.getAbsolutePath());
    System.setProperty("configfolder", new File("src/test/resources/" + getClass().getPackage()
      .getName()
      .replace('.', '/')).getAbsolutePath());

    Result result = app(resourceName);
    assertEquals(ReturnCodes.SUCCESS, result.getReturnCode());

    return new ResultValidator(tempFolder, this);
  }

}
