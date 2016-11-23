/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.plugins.uriresolver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.emc.ia.sipcreator.api.PluginContext;
import com.emc.ia.sipcreator.api.URIResolver;
import com.emc.ia.sipcreator.plugins.testing.PluginChecker;
import com.emc.ia.sipcreator.plugins.testing.PluginTestBase;

public class DefaultUriResolverPluginTests extends PluginTestBase {

  private static final String DEFAULTURIRESOLVER = "defaulturiresolver";

  @Test
  public void verifyContract() {
    PluginChecker chk = new PluginChecker(getPlugin(DEFAULTURIRESOLVER));

    chk.name(DEFAULTURIRESOLVER);
    chk.type(URIResolver.class);
    chk.description("Loads a resource from the filesystem.");
  }

  @Test
  public void openAbsolutepathReturnContent() throws IOException {

    PluginContext context = context("uriresolver.yml", DEFAULTURIRESOLVER);

    URIResolver resolver = newInstance(URIResolver.class, DEFAULTURIRESOLVER, context);

    try (InputStream stream = resolver.open(getPathTo("resolver.txt"))) {
      assertEquals("The content the resolver returned.",
          IOUtils.toString(new InputStreamReader(stream, StandardCharsets.UTF_8)));
    }
  }

}
