/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sipcreator.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.emc.ia.sipcreator.api.Config;
import com.emc.ia.sipcreator.config.ConfigParser;
import com.emc.ia.sipcreator.config.YamlConfigParser;
import com.emc.ia.sipcreator.exceptions.ConfigurationException;
import com.emc.ia.sipcreator.impl.RuntimeVariableFactoryImpl;
import com.emc.ia.sipcreator.utils.DelegatingFunction;
import com.emc.ia.sipcreator.utils.StrLookupWithImport;
import com.emc.ia.sipcreator.utils.StrSubstitutingFunction;
import com.emc.ia.sipcreator.utils.TextResourceLoader;

public final class SipCreatorAppFactory {

  private SipCreatorAppFactory() {
    throw new IllegalStateException(
        "SipCreatorAppFactory is a static utility class and its constructor should never be called.");
  }

  public static SipCreatorApp fromOptions(CommandLineOptions options) {
    final Map<String, String> settings = getSettings(options);
    final Function<String, String> loader = new TextResourceLoader(options.getConfigDirectory());
    final String specification = getSpecification(options, settings, loader);
    Config config = parse(specification);
    return new SipCreatorApp(config, new RuntimeVariableFactoryImpl(loader), loader);
  }

  private static Config parse(final String specification) {
    try {
      ConfigParser parser = new YamlConfigParser();
      return parser.parse(specification);
    } catch (IOException e) {
      throw new ConfigurationException("Failed to parse configuration file.", e);
    }
  }

  private static String getSpecification(CommandLineOptions options, Map<String, String> settings,
      Function<String, String> loader) {
    File configFile = options.getConfigFile();

    if (!configFile.exists() || !configFile.canRead()) {
      throw new ConfigurationException("Configuration file " + configFile.getAbsolutePath() + " not found.");
    }

    return getResolvedConfigurationString(configFile, settings, loader);
  }

  private static String getResolvedConfigurationString(File file, Map<String, String> settings,
      Function<String, String> loader) {
    DelegatingFunction<String, String> f = new DelegatingFunction<>();

    StrLookup<String> lookup = new StrLookupWithImport(StrLookup.mapLookup(settings), f);
    StrSubstitutor substitutor = new StrSubstitutor(lookup);

    Function<String, String> resolvingLoader = loader.andThen(new StrSubstitutingFunction(substitutor));
    f.setDelegate(resolvingLoader);

    return resolvingLoader.apply(file.getAbsolutePath());
  }

  private static Map<String, String> getSettings(CommandLineOptions options) {
    File configDir = options.getConfigDirectory();
    Optional<File> settingsFile = options.getSettingsFile();
    Map<String, String> manualSettings = options.getSettings();

    return readSettings(configDir, settingsFile, manualSettings);
  }

  private static Map<String, String> readSettings(File configDirectory, Optional<File> settingsFile,
      Map<String, String> settings) {

    final Map<String, String> variables = new HashMap<String, String>();
    variables.putAll(System.getenv());

    final Properties systemProperties = System.getProperties();
    for (final Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
      variables.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
    }

    variables.put(".", configDirectory.getAbsolutePath());
    variables.put("now", DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis()));

    if (settingsFile.isPresent()) {
      File file = settingsFile.get();
      readProperties(variables, file);
    } else {
      // By convention we look for settings.properties if the settings file is not explicitly specified
      File file = new File(configDirectory, "settings.properties");
      if (file.exists()) {
        readProperties(variables, file);
      }
    }
    // Overrides
    variables.putAll(settings);
    return variables;
  }

  private static void readProperties(Map<String, String> map, File file) {
    Properties properties = new Properties();
    try (InputStream stream = new FileInputStream(file)) {
      properties.load(stream);
      for (Map.Entry<Object, Object> object : properties.entrySet()) {
        String key = String.valueOf(object.getKey());
        String value = String.valueOf(object.getValue());
        map.put(key, value);
      }
    } catch (IOException e) {
      throw new ConfigurationException(
          "Failed to load settings file " + file.getAbsolutePath() + ". Reason: \"" + e.getMessage() + "\"", e);
    }
  }

}
