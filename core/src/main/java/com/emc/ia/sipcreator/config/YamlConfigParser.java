/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.config;

import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.emc.ia.sipcreator.api.Config;

public class YamlConfigParser implements ConfigParser {

  @Override
  public Config parse(String text) throws IOException {

    Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(), new YamlResolver());

    Map map = (Map)yaml.load(text);

    ConfigImpl conf = new ConfigImpl("root", map);

    return conf;
  }

}
