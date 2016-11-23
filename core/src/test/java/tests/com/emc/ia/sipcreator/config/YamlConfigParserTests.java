/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package tests.com.emc.ia.sipcreator.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.config.ConfigParser;
import com.emc.ia.sipcreator.config.YamlConfigParser;
import com.emc.ia.sipcreator.utils.TextResourceLoader;

public class YamlConfigParserTests {

  @Test
  public void foo() throws IOException {
    ConfigParser parser = new YamlConfigParser();

    Config config =
        parser.parse(new TextResourceLoader(new File("src/test/resources/" + StringUtils.replace(getClass().getPackage()
          .getName(), ".", "/"))).apply("conf1.yml"));

    assertEquals("root", config.getName());
    assertTrue(config.isCompound());
    assertEquals(1, config.getChildGroups()
      .size());

    Config main = config.getChildGroups()
      .get(0);
    assertEquals("main", main.getName());
    assertTrue(main.isCompound());
    assertEquals(1, main.getChildGroups()
      .size());

    Config echo = main.getChildGroups()
      .get(0);
    assertEquals("echo", echo.getName());
    assertFalse(echo.isCompound());
    assertEquals("Message", echo.getValue());
    assertEquals("Message", main.getValue("echo"));
  }

  @Test
  public void foo2() throws IOException {
    ConfigParser parser = new YamlConfigParser();

    Config config =
        parser.parse(new TextResourceLoader(new File("src/test/resources/" + StringUtils.replace(getClass().getPackage()
          .getName(), ".", "/"))).apply("conf2.yml"));

    assertEquals("root", config.getName());
    assertTrue(config.isCompound());
    assertEquals(1, config.getChildGroups()
      .size());

    Config main = config.getChildGroups()
      .get(0);
    assertEquals("main", main.getName());
    // assertTrue(main.isCompound());
    assertEquals(3, main.getChildGroups()
      .size());

    Config echo1 = main.getChildGroups()
      .get(0);
    assertEquals("echo", echo1.getName());
    assertFalse(echo1.isCompound());
    assertEquals("Message", echo1.getValue());

  }
}
